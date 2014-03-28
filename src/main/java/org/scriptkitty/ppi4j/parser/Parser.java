package org.scriptkitty.ppi4j.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Node;
import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.ParserException;
import org.scriptkitty.ppi4j.exception.TokenizerException;
import org.scriptkitty.ppi4j.statement.BreakStatement;
import org.scriptkitty.ppi4j.statement.CompoundStatement;
import org.scriptkitty.ppi4j.statement.DataStatement;
import org.scriptkitty.ppi4j.statement.EndStatement;
import org.scriptkitty.ppi4j.statement.ExpressionStatement;
import org.scriptkitty.ppi4j.statement.GivenStatement;
import org.scriptkitty.ppi4j.statement.IncludeStatement;
import org.scriptkitty.ppi4j.statement.NullStatement;
import org.scriptkitty.ppi4j.statement.PackageStatement;
import org.scriptkitty.ppi4j.statement.Perl6IncludeStatement;
import org.scriptkitty.ppi4j.statement.ScheduledStatement;
import org.scriptkitty.ppi4j.statement.SubStatement;
import org.scriptkitty.ppi4j.statement.UnmatchedBrace;
import org.scriptkitty.ppi4j.statement.VariableStatement;
import org.scriptkitty.ppi4j.statement.WhenStatement;
import org.scriptkitty.ppi4j.structure.BlockStructure;
import org.scriptkitty.ppi4j.structure.ConditionStructure;
import org.scriptkitty.ppi4j.structure.ConstructorStructure;
import org.scriptkitty.ppi4j.structure.ForLoopStructure;
import org.scriptkitty.ppi4j.structure.GivenStructure;
import org.scriptkitty.ppi4j.structure.ListStructure;
import org.scriptkitty.ppi4j.structure.SubscriptStructure;
import org.scriptkitty.ppi4j.structure.WhenStructure;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.LabelToken;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.SeparatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.quotelike.QLWordsToken;
import org.scriptkitty.ppi4j.util.ElementUtils;
import org.scriptkitty.ppi4j.util.IErrorProxy;


/**
 */
public final class Parser
{
    //~ Static fields/initializers

    private static final Set<String> BLOCKS = new HashSet<String>()
    {
        private static final long serialVersionUID = 6527452436640141537L;

        {
            //J-
            add("sub"); add("grep"); add("map"); add("sort"); add("do");
            //J+
        }
    };

    private static final Set<String> CTORS = new HashSet<String>()
    {
        private static final long serialVersionUID = 6202294787825693419L;

        {
            //J-
            // hash constructors
            add("scalar"); add("="); add("||="); add(","); add("=>");
            // per perlref
            add("+");
            // per perlref
            add("return");
            // programatic -- perlfunc says first arg is a reference and 'bless {; ...} failes to compile
            add("bless");
            //J+
        }
    };

    private static Map<String, Class<? extends Statement>> STMTS = new HashMap<String, Class<? extends Statement>>()
    {
        private static final long serialVersionUID = -709562570341786376L;

        {

            // these affect timing of execution
            put("BEGIN", ScheduledStatement.class);
            put("CHECK", ScheduledStatement.class);
            put("UNITCHECK", ScheduledStatement.class);
            put("INIT", ScheduledStatement.class);
            put("END", ScheduledStatement.class);

            // loading and context statements
            put("package", PackageStatement.class);
            put("no", IncludeStatement.class);
            put("require", IncludeStatement.class);

            // 'use' and 'sub' statements are handled outside of this map

            // variable declarations
            put("my", VariableStatement.class);
            put("local", VariableStatement.class);
            put("our", VariableStatement.class);
            put("state", VariableStatement.class);

            // compound statements
            put("if", CompoundStatement.class);
            put("unless", CompoundStatement.class);
            put("for", CompoundStatement.class);
            put("foreach", CompoundStatement.class);
            put("while", CompoundStatement.class);
            put("until", CompoundStatement.class);

            // switch statements
            put("given", GivenStatement.class);
            put("when", WhenStatement.class);
            put("default", WhenStatement.class);

            // various ways to break out of scope
            put("redo", BreakStatement.class);
            put("next", BreakStatement.class);
            put("last", BreakStatement.class);
            put("return", BreakStatement.class);
            put("goto", BreakStatement.class);

            // special sections of the file
            put("__DATA__", DataStatement.class);
            put("__END__", EndStatement.class);
        }
    };

    //~ Instance fields

    private IErrorProxy proxy;

    private ITokenProvider provider;

    private Stack<Token> delayed = new Stack<>();

    //~ Constructors

    protected Parser(ITokenProvider provider, IErrorProxy proxy)
    {
        this.proxy = proxy;
        this.provider = provider;
    }

    //~ Methods

    public Document parse() throws TokenizerException, ParserException
    {
        Token token = Token.NULL;
        Document document = new Document();

        try
        {
            while (!isEOF((token = nextToken(false))))
            {
                if (!token.isSignificant())
                {
                    addElement(document, token);
                }
                else
                {
                    processToken(document, token);
                }

            }

            // add any remaining delayed tokens to the document
            addDelayed(document);

            // TODO: support for v6 blocks?

            return document;
        }
        catch (TokenizerException e)
        {
            document.destroy();
            throw e;
        }
        catch (Exception e)
        {
            document.destroy();
            throw new ParserException(token.getLineNumber(), token.getColumn(), e);
        }
    }

    private void addDelayed(Node node)
    {
        for (Token token : delayed)
        {
            node.addChild(token);
        }

        delayed.clear();
    }

    private void addElement(Node parent, Element element)
    {
        if (parent.getClass() == Statement.class)
        {
            Element first = parent.getSigChild(0);
            Element second = parent.getSigChild(1);

            if ((first instanceof LabelToken) && (second != null))
            {
                // we're a labeled statement
                if (STMTS.containsKey(second.getContent()))
                {
                    ((Statement) parent).changeTo(STMTS.get(second.getContent()));
                }
            }
        }

        if (element instanceof Statement)
        {
            element = ((Statement) element).convert();
        }
        else if (element instanceof Structure)
        {
            element = ((Structure) element).convert();
        }

        // add any delayed tokens first
        addDelayed(parent);

        // add the passed token
        parent.addChild(element);
    }

    private Statement createStatement(Class<? extends Statement> clazz, Token token)
    {
        try
        {
            Statement stmt = clazz.newInstance();
            stmt.addChild(token);

            return stmt;
        }
        catch (InstantiationException e)
        {
            // should never happen...
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            // should never happen...
            throw new RuntimeException(e);
        }
    }

    private Structure createStructure(Class<? extends Structure> clazz, Token token)
    {
        try
        {
            Structure struct = clazz.newInstance();
            struct.setStart(token);

            return struct;
        }
        catch (InstantiationException e)
        {
            // should never happen...
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            // should never happen...
            throw new RuntimeException(e);
        }
    }

    private void delay(Token token)
    {
        delayed.add(token);
    }

    private boolean forContinues(Element last, Token token)
    {
        // LABEL for (EXPR; EXPR; EXPR) BLOCK
        if (ElementUtils.isForWordToken(last) || ElementUtils.isForeachWordToken(last))
        {
            // LABEL for ...
            if (ElementUtils.isOpenParenToken(token) || (token instanceof QLWordsToken))
            {
                return true;
            }

            if (last instanceof QLWordsToken)
            {
                // LABEL for VAR QW{} ...
                // LABEL foreach VAR QW{} ...
                return ElementUtils.isOpenCurlyToken(token);
            }

            // now we behave like a foreach
            return foreachContinues(last, token);
        }

        if (last instanceof BlockStructure)
        {
            // LABEL for (EXPR; EXPR; EXPR) BLOCK - nothing can continue
            return false;
        }

        if (last instanceof QLWordsToken)
        {
            // LABEL for VAR QW{} ...
            // LABEL foreach VAR QW{} ...
            return ElementUtils.isOpenCurlyToken(token);
        }

        // TODO: this should be replaced w/ something else
        // should never happen
        throw new RuntimeException();
    }

    private boolean foreachContinues(Element last, Token token)
    {
        // LABEL foreach VAR (LIST) BLOCK
        // LABEL foreach VAR (LIST) BLOCK continue BLOCK
        if (last instanceof SymbolToken)
        {
            // LABEL foreach my $scalar ...
            if (ElementUtils.isOpenParenToken(token) || (token instanceof QLWordsToken))
            {
                return true;
            }

            return false;
        }

        if (ElementUtils.isForWordToken(last) || ElementUtils.isForeachWordToken(last))
        {
            if ((token instanceof WordToken) && (STMTS.get(token.getContent()) == VariableStatement.class))
            {
                return true;
            }

            if (startsWithDollar(token) || ElementUtils.isOpenParenToken(token) || (token instanceof QLWordsToken))
            {
                return true;
            }

            return false;
        }

        if (STMTS.get(last.getContent()) == VariableStatement.class)
        {
            return startsWithDollar(token);
        }

        // handle rare case: my $foo qw{bar} ...
        if (last instanceof QLWordsToken)
        {
            // LABEL for VAR QW ...
            // LABEL foreach VAR QW ...
            return ElementUtils.isOpenCurlyToken(token);
        }

        // TODO: this should be replaced w/ something else
        // should never happen
        throw new RuntimeException();
    }

    private boolean ifContinues(Statement stmt, Element last, Token token)
    {
        // only implicitly end on a block
        if (!(last instanceof BlockStructure))
        {
            // if (EXPR) ...
            // if (EXPR) BLOCK else ...
            // if (EXPR) BLOCK elsif (EXPR) BLOCK ...
            return true;
        }

        // if the token before the block is 'else', we're done
        if (ElementUtils.isElseWordToken(stmt.getSigChild(-2)))
        {
            return false;
        }

        // otherwise, continue for 'elsif' or 'else' only...
        if (ElementUtils.isElsIfWordToken(token) || ElementUtils.isElseWordToken(token))
        {
            return true;
        }

        return false;
    }

    private boolean isEOF(Token token)
    {
        return token.isEOF();
    }

    private boolean isSubScript(Node parent, Element child)
    {
        boolean subScript = false;

        if (ElementUtils.isDashArrowOperatorToken(child))
        {
            // $foo->{}
            subScript = true;
            child.setAttribute(Element.Attribute.DEREFERENCE);
        }
        else if (child instanceof SubscriptStructure)
        {
            // $foo[]{}
            subScript = true;
        }
        else if ((child instanceof SymbolToken) && (startsWithDollar(child) || startsWithAt(child)))
        {
            // $foo{}, @foo{}
            subScript = true;
        }
        else if (child instanceof BlockStructure)
        {
            // dereference - ${$hash_ref}{foo} or ${burfle}{foo}
            // hash slice  - @{$hash_ref}{'foo', 'bar'}
            Element last = parent.getSigChild(-2);
            if ((last instanceof CastToken) && (startsWithDollar(last) || startsWithAt(last)))
            {
                subScript = true;
            }
        }

        return subScript;
    }

    //J--
    /*
     * a label can be any one of:
     *
     * LABEL while (EXPR) BLOCK
     * LABEL while (EXPR) BLOCK continue BLOCK
     * LABEL for (EXPR; EXPR; EXPR) BLOCK
     * LABEL foreach VAR (LIST) BLOCK
     * LABEL foreach VAR (LIST) BLOCK continue BLOCK
     * LABEL BLOCK continue BLOCK
     */
    //J+
    private boolean labelContinues(Statement stmt, Token token)
    {
        // word after the label
        if (ElementUtils.isLoopWordToken(token))
        {
            return true;
        }

        // labled blocks
        if (ElementUtils.isOpenCurlyToken(token))
        {
            return true;
        }

        return false;
    }

    private Token nextToken() throws TokenizerException
    {
        return nextToken(true);
    }

    private Token nextToken(boolean delay) throws TokenizerException
    {
        Token token = provider.nextToken();

        while (delay && !token.isSignificant())
        {
            delay(token);
            token = provider.nextToken();
        }

        return token;
    }

    private void parseOpenBrace(Node parent, Token token) throws TokenizerException
    {
        // rollback into the token, create a new statement, and parse it...
        rollback(token);

        Statement stmt = new Statement();

        parseStatement(stmt);
        addElement(parent, stmt);
    }

    private void parseStatement(Statement stmt) throws TokenizerException
    {
        if (stmt instanceof EndStatement)
        {
            Token token = null;
            while (!isEOF((token = nextToken(false))))
            {
                stmt.addChild(token);
            }

            rollback(null);
            return;
        }

        Token token = null;
        while (!isEOF((token = nextToken())))
        {
            if (ElementUtils.isCloseBraceToken(token) || (token instanceof SeparatorToken))
            {
                rollback(token);
                return;
            }

            if (!stmt.isNormal() && !statementContinues(stmt, token))
            {
                rollback(token);
                return;
            }

            if (!(token instanceof StructureToken))
            {
                addElement(stmt, token);
                continue;
            }

            if (ElementUtils.isSemiColonToken(token))
            {
                addElement(stmt, token);
                return;
            }

            Structure struct = resolveStructure(stmt, token);
            parseStructure(struct);

            addElement(stmt, struct);
        }

        rollback(token);
    }

    private void parseStructure(Structure struct) throws TokenizerException
    {
        Token token = null;
        while (!isEOF((token = nextToken())))
        {

            // anything that's not a structure requires a 'resolved' statement
            if (!(token instanceof StructureToken))
            {
                /*
                 * add any delayed tokens to the structure as resolveNewStatement may delay and roll itself back
                 */
                addDelayed(struct);

                Statement stmt = resolveStatement(struct, token);

                parseStatement(stmt);
                addElement(struct, stmt);
            }

            // opening of another structure directly inside us...
            else if (ElementUtils.isOpenBraceToken(token))
            {
                parseOpenBrace(struct, token);
            }

            // close of a structure - could be an error...
            else if (ElementUtils.isCloseBraceToken(token))
            {
                if (struct.isStartOpposite(token.getContent()))
                {
                    addDelayed(struct);
                    struct.setFinish(token);

                    // are we really for my $i (@foo)
                    if ((struct instanceof ForLoopStructure) && (2 > struct.find(Statement.class, true).size()))
                    {
                        struct.changeTo(ListStructure.class);
                    }
                }
                else
                {
                    /*
                     * umatched closing brace - the user typed something wrong or just didn't include it - either way, it's an error that
                     * needs to be handled gracefully. for now, treat it as implicitly ending the structure - this will cause the least
                     * damage across the various reasons this could have occurred.
                     */
                    proxy.reportUnmatchedBrace(token);

                    rollback(token);
                }

                return;
            }
            else
            {
                addElement(struct, createStatement(NullStatement.class, token));
            }
        }

        // eof, add any insignificant trailing tokens
        addDelayed(struct);
    }

    private void processToken(Document document, Token token) throws TokenizerException
    {
        if (ElementUtils.isSemiColonToken(token))
        {
            // ';' on it's own is a 'null' statement
            addElement(document, createStatement(NullStatement.class, token));
        }
        else if (!(token instanceof StructureToken))
        {
            Statement stmt = resolveStatement(document, token);

            // move the lexing down into the statement
            addDelayed(document);

            parseStatement(stmt);
            addElement(document, stmt);
        }
        else if (ElementUtils.isOpenBraceToken(token))
        {
            parseOpenBrace(document, token);
        }
        else if (ElementUtils.isCloseBraceToken(token))
        {
            /*
             * if the situation is encoutered, we are at the top of the tree. either a parsing error occurred or there is a mistake in the
             * code
             */
            Statement stmt = createStatement(UnmatchedBrace.class, token);
            addElement(document, stmt);
        }
    }

    private Class<? extends Structure> resolveOpenBracket(Node parent, Token token)
    {
        Class<? extends Structure> clazz = null;
        Element element = parent.getSigChild(-1);

        // TODO: there are additional cases to be caught...

        if (ElementUtils.isDashArrowOperatorToken(element))
        {
            // $foo->[]
            element.setAttribute(Element.Attribute.DEREFERENCE);
            clazz = SubscriptStructure.class;
        }
        else if (element instanceof SubscriptStructure)
        {
            // $foo{}[]
            clazz = SubscriptStructure.class;
        }
        else if ((element instanceof SymbolToken) && (startsWithDollar(element) || startsWithAt(element)))
        {
            // $foo[], @foo[]
            clazz = SubscriptStructure.class;
        }
        else
        {
            // assume annonymouse arrayref constructor
            clazz = ConstructorStructure.class;
        }

        return clazz;
    }

    private Class<? extends Structure> resolveOpenCurly(Node parent, Token token) throws TokenizerException
    {
        Element element = parent.getSigChild(-1);

        if (element != null)
        {
            if (isSubScript(parent, element))
            {
                return SubscriptStructure.class;
            }

            if (BLOCKS.contains(element.getContent()))
            {
                return BlockStructure.class;
            }

            if (CTORS.contains(element.getContent()))
            {
                return ConstructorStructure.class;
            }
        }

        if (parent instanceof CompoundStatement)
        {
            // we will only encounter blocks in a compound statement
            return BlockStructure.class;
        }

        // are we 2nd or 3rd argument of 'use'
        if (parent instanceof IncludeStatement)
        {
            int count = parent.getSigChidrenCount();
            if (((count == 2) || (count == 3)) && (parent.getSigChild(2) instanceof NumberToken))
            {
                // this is something like use constant { ... };
                return ConstructorStructure.class;
            }
        }

        /*
         * according to PPI, unless we are at the start of the statement, everything else should be a block. it's also possibly a bad
         * choice, but will have to do for now. :)
         */
        if (element != null)
        {
            return BlockStructure.class;
        }

        // special case: param of a core function, ie: map({ $_ => 1 } @foo)
        if ((parent instanceof Statement) && parent.parentIs(ListStructure.class))
        {
            String content = parent.getParent().getParent().getSigChild(-2).getContent();
            if ("map".equals(content) || "grep".equals(content) || "sort".equals(content))
            {
                return BlockStructure.class;
            }
        }

        // scan ahead...
        int position = 0;

        Token next = null;
        while (!isEOF((next = nextToken())))
        {
            String content = next.getContent();

            // if >= 3, default to block, otherwise block per perlref
            if ((++position >= 3) || ((position == 1) && ";".equals(content)))
            {

                // block, per perlref
                rollback(next);
                break;
            }

            if (((position == 1) && "}".equals(content)) || ((position == 2) && "=>".equals(content)))
            {
                rollback(next);
                return ConstructorStructure.class;
            }

            delay(next);
        }

        // trigger the delayed tokens to be added back to the buffer
        rollback(null);

        if (parent instanceof Statement)
        {
            ((Statement) parent).changeTo(CompoundStatement.class);
        }

        return BlockStructure.class;
    }

    private Class<? extends Structure> resolveOpenParen(Node parent, Token token)
    {
        Element element = parent.getSigChild(-1);

        if (ElementUtils.isConditionalWordToken(element))
        {
            return ConditionStructure.class;
        }

        if (ElementUtils.isForWordToken(element) || ElementUtils.isForeachWordToken(element))
        {
            return ForLoopStructure.class;
        }

        // if we're paret of a for/foreach statement, we're a ForLoop
        if (parent instanceof CompoundStatement)
        {
            CompoundStatement.Type type = ((CompoundStatement) parent).getType();
            if ((type == CompoundStatement.Type.FOR) || (type == CompoundStatement.Type.FOREACH))
            {
                return ForLoopStructure.class;
            }
        }

        if (parent instanceof GivenStatement)
        {
            return GivenStructure.class;
        }

        if (parent instanceof WhenStatement)
        {
            return WhenStructure.class;
        }

        // if previous element is '->', set dereference attribute
        if (ElementUtils.isDashArrowOperatorToken(element))
        {
            element.setAttribute(Element.Attribute.DEREFERENCE);
        }

        // default to list
        return ListStructure.class;
    }

    private Statement resolveStatement(Node parent, Token token) throws TokenizerException
    {
        // this might be 'parent' =>
        if (((parent instanceof ListStructure) || (parent instanceof ConstructorStructure)) && (token instanceof WordToken))
        {
            Token next = nextToken();
            if (ElementUtils.isEqualArrowOperatorToken(next))
            {
                rollback(next);
                return createStatement(ExpressionStatement.class, token);
            }

            rollback(next);
        }

        // try and grab the statement type from the map - ok if we get 'null'
        Class<? extends Statement> clazz = Parser.STMTS.get(token.getContent());

        // handle barewords for subscripts
        if (parent instanceof SubscriptStructure)
        {
            clazz = resolveSubScript(clazz, token);
        }

        // if we're resolved at this point, return...
        if (clazz != null)
        {
            return createStatement(clazz, token);
        }

        if (ElementUtils.isSubWordToken(token))
        {
            clazz = resolveSubroutine(token);
        }
        else if (ElementUtils.isUseWordToken(token))
        {
            clazz = resolveUse(token);
        }
        else if ((parent instanceof WhenStructure) || (parent instanceof GivenStructure) || (parent instanceof ConditionStructure) ||
                (parent instanceof ListStructure))
        {
            clazz = ExpressionStatement.class;
        }
        else if (token instanceof LabelToken)
        {
            clazz = CompoundStatement.class;
        }
        else
        {
            // safest fall through...
            clazz = Statement.class;
        }

        return createStatement(clazz, token);
    }

    private Structure resolveStructure(Statement parent, Token token) throws TokenizerException
    {
        Class<? extends Structure> clazz = null;

        if (ElementUtils.isOpenCurlyToken(token))
        {
            clazz = resolveOpenCurly(parent, token);
        }
        else if (ElementUtils.isOpenSquareToken(token))
        {
            clazz = resolveOpenBracket(parent, token);
        }
        else if (ElementUtils.isOpenParenToken(token))
        {
            clazz = resolveOpenParen(parent, token);
        }
        else
        {
            // safest fall through...
            clazz = Structure.class;
        }

        return createStructure(clazz, token);
    }

    private Class<? extends Statement> resolveSubroutine(Token token) throws TokenizerException
    {
        Class<? extends Statement> clazz = null;

        Token next = nextToken();
        if (!isEOF(next))
        {
            // scheduled statements that are defined as subroutines are still scheduled
            if (ElementUtils.isScheduledWordToken(next))
            {
                clazz = ScheduledStatement.class;
            }
            else if (ElementUtils.isSubWordToken(token) && (next instanceof WordToken))
            {
                clazz = SubStatement.class;
            }
            else
            {
                // safest fall through
                clazz = Statement.class;
            }
        }
        else
        {
            // eof, subroutine is most likely...
            clazz = SubStatement.class;
        }

        rollback(next);

        return clazz;
    }

    private Class<? extends Statement> resolveSubScript(Class<? extends Statement> resolved, Token token) throws TokenizerException
    {
        //J-
        /*
         * simple case, we're just an expression
         *
         * PPI does this:
         *
         * if ( $Parent->isa('PPI::Structure::Subscript') ) {
         *      # Fast obvious case, just an expression
         *      unless ($class and $class->isa('PPI::Statement::Expression') ) {
         *          return 'PPI::Statement::Expression';
         *      }
         *      ...
         *
         * but i don't see any way 'resolved' ($class above) could ever be an expression statement b/c there are no
         * keywords that map to one, but this doesn't really hurt anything, so...
         */
        //J+

        if ((resolved == null) || (resolved == ExpressionStatement.class))
        {
            return ExpressionStatement.class;
        }

        /*
         * more complex case, we might be 'my' or 'our' followed by a symbol. if the next token is a '}', then we're sothing like $h{foo}
         */
        Class<? extends Statement> clazz = null;
        Token next = nextToken();

        if (!isEOF(next))
        {
            if (ElementUtils.isCloseCurlyToken(next))
            {
                clazz = ExpressionStatement.class;
            }
            else
            {
                clazz = resolved;
            }
        }
        else
        {
            // reached EOF, so we're probably resolving like $h{our which will become $h{foo}...at least we think
            clazz = ExpressionStatement.class;
        }

        rollback(next);

        return clazz;
    }

    private Class<? extends Statement> resolveUse(Token token) throws TokenizerException
    {
        Class<? extends Statement> clazz = null;

        Token next = nextToken();
        if (!isEOF(next))
        {
            if (ElementUtils.isV6WordToken(next))
            {
                clazz = Perl6IncludeStatement.class;
            }
            else
            {
                clazz = IncludeStatement.class;
            }
        }
        else
        {
            // eof, treat as regular include statement
            clazz = IncludeStatement.class;
        }

        rollback(next);
        return clazz;
    }

    private void rollback(Token token)
    {
        if (token != null)
        {
            provider.rollback(token);
        }

        while (!delayed.isEmpty())
        {
            Token delay = delayed.pop();
            provider.rollback(delay);
        }
    }

    private boolean startsWithAt(Element element)
    {
        return element.getContent().startsWith("@");
    }

    private boolean startsWithDollar(Element element)
    {
        return element.getContent().startsWith("$");
    }

    private boolean statementContinues(Statement stmt, Token token)
    {
        // handle simple block case: { print 1; }
        if ((stmt.getSigChidrenCount() == 1) && (stmt.getSigChild(0) instanceof BlockStructure))
        {
            return false;
        }

        // anything other then these statements - Scheduled|Sub|Compound|Given|When - ends
        if (statementEnds(stmt))
        {
            return true;
        }

        Element last = stmt.getLastElement();

        // scheduled/sub/given/when follow same rule and can be handled first...
        if (!(stmt instanceof CompoundStatement))
        {
            return (!(last instanceof BlockStructure));
        }

        //
        // on to the compound statements...
        //
        // first up, 'if' statements - include 'unless', which is for ppl who can't understand 'if not'
        //
        // should be one of the following
        // if (EXPR) BLOCK
        // if (EXPR) BLOCK else BLOCK
        // if (EXPR) BLOCK elsif (EXPR) BLOCK ... else BLOCK
        //
        Statement.Type type = stmt.getType();
        if (type == Statement.Type.IF)
        {
            return ifContinues(stmt, last, token);
        }

        // labels...
        if (type == Statement.Type.LABEL)
        {
            return labelContinues(stmt, token);
        }

        //
        // handle common "after round braces" case
        // LABEL while (EXPR) ...
        // LABEL for (EXPR; EXPR; EXPR) ...
        // LABEL for VAR (LIST) ...
        // LABEL foreach VAR (LIST) ...
        //
        if ((last instanceof Structure) && (((Structure) last).getBraceType() == Structure.BraceType.ROUND))
        {
            return ElementUtils.isOpenCurlyToken(token);
        }

        // for...
        if (type == Statement.Type.FOR)
        {
            return forContinues(last, token);
        }

        //
        // comon continue case
        // LABEL while (EXPR) BLOCK continue ...
        // LABEL foreach VAR (LIST) BLOCK continue ...
        //
        if (ElementUtils.isContinueWordToken(last))
        {
            return ElementUtils.isOpenCurlyToken(token);
        }

        //
        // common continuable blocks
        // LABEL while (EXPR) BLOCK
        // LABEL while (EXPR) BLOCK ...
        // LABEL for (EXPR; EXPR; EXPR) BLOCK
        // LABEL foreach VAR (LIST) BLOCK
        // LABEL foreach VAR (LIST) BLOCK ...
        // LABEL BLOCK ...
        //
        if (last instanceof BlockStructure)
        {
            //
            // is this the block for a continue?
            // LABEL while (EXPR) BLOCK continue BLOCK
            // LABEL foreach VAR (LIST) BLOCK continue BLOCK
            // LABEL BLOCK continue BLOCK
            //
            if (ElementUtils.isContinueWordToken(stmt.getSigChild(-2)))
            {
                return false;
            }

            return ElementUtils.isContinueWordToken(token);
        }

        if (type == Statement.Type.WHILE)
        {
            if (ElementUtils.isWhileWordToken(last) || ElementUtils.isUntilWordToken(last))
            {
                return ElementUtils.isOpenParenToken(token);
            }
        }

        if (type == Statement.Type.FOREACH)
        {
            return foreachContinues(last, token);
        }

        // TODO: this should be replaced w/ something else
        // should never happen
        throw new RuntimeException("unhandled type [" + type + "]");
    }

    private boolean statementEnds(Statement stmt)
    {
        boolean ends = true;

        if (stmt instanceof ScheduledStatement)
        {
            ends = false;
        }
        else if (stmt instanceof SubStatement)
        {
            ends = false;
        }
        else if (stmt instanceof CompoundStatement)
        {
            ends = false;
        }
        else if (stmt instanceof GivenStatement)
        {
            ends = false;
        }

        return ends;
    }
}
