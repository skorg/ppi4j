package org.scriptkitty.ppi4j.tokenizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.ArrayIndexToken;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.MagicToken;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.PrototypeToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.token.UnknownToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.number.VersionNumberToken;
import org.scriptkitty.ppi4j.token.quote.DoubleQuoteToken;
import org.scriptkitty.ppi4j.token.quote.InterpolateQuoteToken;
import org.scriptkitty.ppi4j.token.quote.LiteralQuoteToken;
import org.scriptkitty.ppi4j.token.quote.SingleQuoteToken;
import org.scriptkitty.ppi4j.token.quotelike.QLBacktickToken;
import org.scriptkitty.ppi4j.token.quotelike.QLCommandToken;
import org.scriptkitty.ppi4j.token.quotelike.QLReadlineToken;
import org.scriptkitty.ppi4j.token.quotelike.QLRegExpToken;
import org.scriptkitty.ppi4j.token.quotelike.QLWordsToken;
import org.scriptkitty.ppi4j.token.regexp.REMatchToken;
import org.scriptkitty.ppi4j.util.ElementUtils;


final class WhitespaceScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** comment pattern - <code>/^\s*#/</code> */
    private static final Pattern COMMENT = Pattern.compile("^\\s*#");

    private static final Pattern READLINE = Pattern.compile("^<(?!\\d)\\w+>");

    private static final Map<Integer, Class<? extends Token>> COMMIT = new HashMap<Integer, Class<? extends Token>>()
    {
        private static final long serialVersionUID = 4421796814489362729L;

        {
            put(Integer.valueOf(';'), StructureToken.class);
            put(Integer.valueOf('['), StructureToken.class);
            put(Integer.valueOf(']'), StructureToken.class);
            put(Integer.valueOf('{'), StructureToken.class);
            put(Integer.valueOf('}'), StructureToken.class);
            // note '(' is missing here, handled separately...
            put(Integer.valueOf(')'), StructureToken.class);
            put(Integer.valueOf('#'), CommentToken.class);
            put(Integer.valueOf('v'), VersionNumberToken.class);
            put(Integer.valueOf('_'), WordToken.class);
        }
    };

    private static final Set<Class<? extends Token>> OPERATOR_CONTEXT = new HashSet<Class<? extends Token>>()
    {
        private static final long serialVersionUID = 1L;

        {
            add(SymbolToken.class);
            add(MagicToken.class);
            add(NumberToken.class);
            add(ArrayIndexToken.class);
            add(DoubleQuoteToken.class);
            add(InterpolateQuoteToken.class);
            add(LiteralQuoteToken.class);
            add(SingleQuoteToken.class);
            add(QLBacktickToken.class);
            add(QLCommandToken.class);
            add(QLReadlineToken.class);
            add(QLRegExpToken.class);
            add(QLWordsToken.class);
        }
    };

    private static final Map<Integer, Class<? extends Token>> MAP = new HashMap<Integer, Class<? extends Token>>()
    {
        private static final long serialVersionUID = 9019741478003051844L;

        {
            put(Integer.valueOf('\\'), CastToken.class);
            put(Integer.valueOf('"'), DoubleQuoteToken.class);
            put(Integer.valueOf('='), OperatorToken.class);
            put(Integer.valueOf('?'), OperatorToken.class);
            put(Integer.valueOf('|'), OperatorToken.class);
            put(Integer.valueOf('+'), OperatorToken.class);
            put(Integer.valueOf('>'), OperatorToken.class);
            put(Integer.valueOf('.'), OperatorToken.class);
            put(Integer.valueOf('!'), OperatorToken.class);
            put(Integer.valueOf('~'), OperatorToken.class);
            put(Integer.valueOf('^'), OperatorToken.class);
            put(Integer.valueOf(','), OperatorToken.class);
            put(Integer.valueOf('\''), SingleQuoteToken.class);
            put(Integer.valueOf('`'), QLBacktickToken.class);
            put(Integer.valueOf('*'), UnknownToken.class);
            put(Integer.valueOf('$'), UnknownToken.class);
            put(Integer.valueOf('@'), UnknownToken.class);
            put(Integer.valueOf('&'), UnknownToken.class);
            put(Integer.valueOf(':'), UnknownToken.class);
            put(Integer.valueOf('%'), UnknownToken.class);
            put(Integer.valueOf('_'), WordToken.class);

            // 9 = '\t', 10 = '\n'. 13 = '\r', 32 = ' '
            put(9, WhitespaceToken.class);
            put(10, WhitespaceToken.class);
            put(13, WhitespaceToken.class);
            put(32, WhitespaceToken.class);
        }
    };

    /** 'use' perl v6 pattern - <code>/^use v6\-alpha\;/</code> */
    private static final Pattern PERL6 = Pattern.compile("^use v6\\-alpha\\;");

    /** set of words (functions and keywords) that will almost certainly be a regexp if they follow a '/'. */
    private static final Set<String> REGEXP_WORDS = new HashSet<String>()
    {
        private static final long serialVersionUID = -7059618483996134511L;

        {
            add("split");
            add("if");
            add("unless");
            add("grep");
            add("map");
        }
    };

    /** whitespace pattern - <code>/^\s*$/</code> */
    private static final Pattern WHITESPACE = Pattern.compile("^\\s*$");

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        if (tokenizer.isEndOfCurrentLine())
        {
            return true;
        }

        int c = tokenizer.getCurrentCharOfCurrentLine();

        if (isCommit(c))
        {
            switchScannerForCommit(tokenizer, c);
            return tokenizer.getScanner().tokenizerCommit(tokenizer);
        }

        if (isClassMap(c))
        {
            /*
             * meh - PPI creates the token after '__TOKENIZER__on_char' completes (in Tokenizer::_process_next_char), but we need to do it
             * here b/c there is no way to signal what type of token should be created.
             *
             * upon further reflection, i guess i could have just set the class to create as state on the tokenizer, but meh...
             */
            createClassToken(tokenizer, c);
            return false;
        }

        if (MAP.containsKey(c))
        {

            tokenizer.createToken(MAP.get(c));
            return false;
        }

        // '(' = 40
        if (c == 40)
        {
            handleOpenParen(tokenizer);
            return false;
        }

        // '<' = 60
        if (c == 60)
        {
            handleOpenAngle(tokenizer);
            return false;
        }

        // '/' = 47
        if (c == 47)
        {
            handleForwardSlash(tokenizer);
            return false;
        }

        // 'x' = 120
        if (c == 120)
        {
            // 'x' is 'special case, as it also represents a letter and could be consumed
            return handleX(tokenizer);
        }

        // '-' = 45
        if (c == 45)
        {
            handleDash(tokenizer);
            return false;
        }

        // >= 128 outside ascii
        if (c >= 128)
        {
            if (Character.isWhitespace(c))
            {
                tokenizer.createToken(WhitespaceToken.class);
                return false;
            }

            if (Character.isLetterOrDigit(c))
            {
                tokenizer.switchToScanner(WordToken.class);
                return tokenizer.getScanner().tokenizerOnChar(tokenizer);

            }
        }

        throw new TokenizingException("encountered unknown character [" + (char) c + "]");
    }

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnLineEnd(org.scriptkitty.ppi4j.tokenizer.Tokenizer, java.lang.String)
     */
    @Override protected void tokenizerOnLineEnd(Tokenizer tokenizer, String line)
    {
        tokenizer.finalizeToken();
    }

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnLineStart(org.scriptkitty.ppi4j.tokenizer.Tokenizer, java.lang.String)
     */
    @Override protected boolean tokenizerOnLineStart(Tokenizer tokenizer, String line)
    {
        Matcher matcher = WHITESPACE.matcher(line);

        if (matcher.find())
        {
            tokenizer.createToken(WhitespaceToken.class, line);
            return true;
        }

        matcher.reset();
        matcher.usePattern(COMMENT);

        if (matcher.find())
        {
            tokenizer.createToken(CommentToken.class, line);
            tokenizer.finalizeToken();
            return true;
        }

        matcher.reset();
        matcher.usePattern(PodScanner.POD);

        if (matcher.find())
        {
            tokenizeMatchedPodLine(tokenizer, line, matcher);
            return true;
        }

        matcher.reset();
        matcher.usePattern(PERL6);

        if (matcher.find())
        {
            /*
             * currently PPI just sucks all the lines after the 'use' statement into an array, but does nothing w/ them after the fact.
             * rather then doing that, we tell the tokenizer to force an EOF and abort processing. if PPI ever does something w/ these
             * lines, we will too.
             */
            tokenizer.forceEOF();
        }

        return false;
    }

    private void createClassToken(Tokenizer tokenizer, int c)
    {
        if (Character.isDigit(c))
        {
            tokenizer.createToken(NumberToken.class);
            return;
        }

        tokenizer.createToken(MAP.get(c));
    }

    private void handleDash(Tokenizer tokenizer)
    {
        /*
         * PPI's implementation of this is a bit different b/c it's asking the tokenizer for an 'opcontext', but i can't find anywhere else
         * that method call is used, so it is simplified here for now...
         */
        Token prev = tokenizer.getLastSignificantToken();

        if (OPERATOR_CONTEXT.contains(prev.getClass()) || ElementUtils.isCloseCurlyToken(prev))
        {
            tokenizer.createToken(OperatorToken.class);
        }
        else
        {
            tokenizer.createToken(UnknownToken.class);
        }
    }

    private void handleForwardSlash(Tokenizer tokenizer)
    {
        tokenizer.finalizeToken();

        // either 'divide by' or start of a regexp
        Token prev = tokenizer.getLastSignificantToken();

        /*
         * most times following an operator, we're a regexp...  ,  - arg in list  .. - 2nd condition in a flip flop  =~ - bound regexp  !~ -
         * bond regexp
         */
        if ((prev instanceof OperatorToken))
        {
            tokenizer.createToken(REMatchToken.class);
            return;
        }

        // after symbol or number
        if (ElementUtils.isCloseSquare(prev) || (prev instanceof NumberToken))
        {
            tokenizer.createToken(OperatorToken.class);
            return;
        }

        // after going into scope/brackets
        if (ElementUtils.isOpenCurlyToken(prev) || ElementUtils.isOpenParenToken(prev) || ElementUtils.isSemiColonToken(prev))
        {
            tokenizer.createToken(REMatchToken.class);
            return;
        }

        // functions and keywords
        if ((prev instanceof WordToken) && REGEXP_WORDS.contains(prev.getContent()))
        {
            // TOOD: move regexp words into token utils?
            tokenizer.createToken(REMatchToken.class);
            return;
        }

        // very first thing in the file
        if ("".equals(prev.getContent()))
        {
            tokenizer.createToken(REMatchToken.class);
            return;
        }

        /*
         * check char after slash, there are some things that would be highly illogic to see if it's an operator, so we assume it's a regexp
         */
        if (!tokenizer.isEndOfCurrentLine() && tokenizer.getNextCharacter().matches("(?:\\^|\\[|\\\\)"))
        {
            tokenizer.createToken(REMatchToken.class);
            return;
        }

        // assume operator...possible other test cases could be added
        tokenizer.createToken(OperatorToken.class);
    }

    private void handleOpenAngle(Tokenizer tokenizer)
    {
        tokenizer.finalizeToken();

        // let's see if we're a 'less than' or a 'readline quote-like'
        Token prev = tokenizer.getLastSignificantToken();

        /*
         * the most common group of less-thans   $foo < $bar  1 < $bar  $#foo < $bar
         */
        if ((prev instanceof SymbolToken) || (prev instanceof MagicToken) || (prev instanceof NumberToken) ||
                (prev instanceof ArrayIndexToken))
        {
            tokenizer.createToken(OperatorToken.class);
            return;
        }

        // we're either '<<' or heredoc - adjust offset b/c 'current' char hasn't been consumed yet
        if (tokenizer.getNextCharacter(1).equals("<"))
        {
            tokenizer.createToken(OperatorToken.class);
            return;
        }

        /*
         * most common group of readlines  while ( <...> )  while <>;
         */
        String content = prev.getContent();
        if (ElementUtils.isOpenParenToken(prev))
        {
            tokenizer.createToken(QLReadlineToken.class);
            return;
        }

        if ((prev instanceof WordToken) && "while".equals(content))
        {
            tokenizer.createToken(QLReadlineToken.class);
            return;
        }

        if ((prev instanceof OperatorToken) && ("=".equals(content) || ",".equals(content)))
        {
            tokenizer.createToken(QLReadlineToken.class);
            return;
        }

        //J-
        /*
         * could go either way...
         *   $foo->{bar} < 2
         *   grep { .. } <foo>;
         */
        //J+
        if (ElementUtils.isCloseCurlyToken(prev))
        {
            if (READLINE.matcher(tokenizer.getRestOfCurrentLine()).find())
            {
                // almost definitely a readline...
                tokenizer.createToken(QLReadlineToken.class);
                return;
            }
        }

        // default is to guess the operator
        tokenizer.createToken(OperatorToken.class);
    }

    private void handleOpenParen(Tokenizer tokenizer)
    {
        tokenizer.finalizeToken();

        Token[] previous = tokenizer.getLastSignificantTokens(3);

        if (previous.length > 0)
        {
            //J-
            /*
             * prototype if:
             *   - token[0] is a bareword
             *   - token[1] is the 'sub' keyword
             *   - token[2] is a structure or empty whitespace
             */
            //J+
            if ((previous[0] instanceof WordToken) && ElementUtils.isSubWordToken(previous[1]) &&
                    ((previous[2] instanceof StructureToken) || ElementUtils.isEmptyWhitespace(previous[2])))
            {
                tokenizer.createToken(PrototypeToken.class);
                return;
            }

            // prototyped anonymous subroutine
            if (ElementUtils.isSubWordToken(previous[0]) && !ElementUtils.isDashArrowOperatorToken(previous[1]))
            {
                tokenizer.createToken(PrototypeToken.class);
                return;
            }
        }

        tokenizer.createToken(StructureToken.class);
    }

    private boolean handleX(Tokenizer tokenizer) throws TokenizingException
    {
        if (!tokenizer.isEndOfCurrentLine())
        {
            Token prev = tokenizer.getLastSignificantToken();
            String next = tokenizer.getNextCharacter();

            //J-
            /*
             * handle arcane case where the 'string' "x10" means x is an operator.
             *
             * 'string' in this case means:
             *    -single quote
             *    - double quote
             *
             * PPI claims the previous token class should match this pattern:
             *     $prev =~ /::Quote::(?:Operator)?(?:Single|Double|Execute)$/
             *
             * but i can't find any refernce to Quote::Operator / Quote::Execute tokens, so there may be other cases
             * this is applicable for...
             */
            //J+
            if (next.matches("\\d") && ((prev instanceof SingleQuoteToken) || (prev instanceof DoubleQuoteToken)))
            {
                tokenizer.createToken(OperatorToken.class);
                return false;
            }
        }

        tokenizer.switchToScanner(WordToken.class);
        return tokenizer.getScanner().tokenizerCommit(tokenizer);
    }

    private boolean isClassMap(int c)
    {
        return (MAP.containsKey(c) || Character.isDigit(c));
    }

    private boolean isCommit(int c)
    {
        // 'x' = 118 - 'v' == 120
        return (COMMIT.containsKey(c) || (Character.isLetter(c) && !((c == 118) || (c == 120))));
    }

    private void switchScannerForCommit(Tokenizer tokenizer, int c)
    {
        if (COMMIT.containsKey(c))
        {
            tokenizer.switchToScanner(COMMIT.get(c));
            return;
        }

        // don't need to check for 'x' or 'y' here b/c 'isCommit' prevents those chars
        tokenizer.switchToScanner(WordToken.class);
    }
}
