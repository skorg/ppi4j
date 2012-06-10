package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.finder.SimpleRule;
import org.scriptkitty.ppi4j.structure.BlockStructure;
import org.scriptkitty.ppi4j.structure.ConditionStructure;
import org.scriptkitty.ppi4j.structure.ListStructure;
import org.scriptkitty.ppi4j.token.LabelToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.quotelike.QLWordsToken;
import org.scriptkitty.ppi4j.util.ElementUtils;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <code>CompoundStatement</code>s are used to describe all current forms of compound statements, as described in <a
 * href="http://perldoc.perl.org/perlsyn.html">perlsyn</a>.
 *
 * <p>the following blocks are covered:</p>
 *
 * <ul>
 *   <li><code>if</code></li>
 *   <li><code>unless</code></li>
 *   <li><code>for</code></li>
 *   <li><code>foreach</code></li>
 *   <li><code>while</code></li>
 *   <li><code>continue</code></li>
 * </ul>
 *
 * <p>please note this does <b>not</b> cover "simple" statements with trailing conditions, nor is <code>do</code>
 * considered to be part of a compound statement.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Statement/Compound.pm">CPAN - PPI::Statement::Compound</a>
 */
public class CompoundStatement extends StatementWithBody
{
    //~ Static fields/initializers

    private static final Map<String, Type> TYPES = new HashMap<String, Type>()
    {
        private static final long serialVersionUID = 999515001187261414L;

        {
            put("if", Type.IF);
            put("unless", Type.IF);
            put("while", Type.WHILE);
            put("until", Type.WHILE);
            put("for", Type.FOR);
            put("foreach", Type.FOREACH);
        }
    };

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Statement#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public final void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    public final boolean canContinue()
    {
        Token token = getFirstToken();

        return (ElementUtils.isWhileWordToken(token) || ElementUtils.isUntilWordToken(token) ||
            ElementUtils.isForeachWordToken(token));
    }

    public ConditionStructure getConditional()
    {
        Element child = getSigChild(hasLabel() ? 2 : 1);

        if (child instanceof ConditionStructure)
        {
            return (ConditionStructure) child;
        }

        return null;
    }

    public BlockStructure getContinue()
    {
        List<BlockStructure> structs = find(BlockStructure.class, false, false);

        if (structs.size() == 2)
        {
            return structs.get(1);
        }

        return null;
    }

    public Token getKeyword()
    {
        return getToken(WordToken.class, (hasLabel() ? 1 : 0));
    }

    /**
     * get the statement's label
     *
     * @return a <code>Token> representing the label or <code>null</code> if it does not exist</code>
     */
    public final Token getLabel()
    {
        return getToken(LabelToken.class, 0);
    }

    /**
     * {@inheritDoc}
     *
     * <p>possible return types are:</p>
     *
     * <ul>
     *   <li><code>CONTINUE</code></li>
     *   <li><code>IF</code></li>
     *   <li><code>FOR</code></li>
     *   <li><code>FOREACH</code></li>
     *   <li><code>LABEL</code></li>
     *   <li><code>UNKNOWN</code></li>
     *   <li><code>WHILE</code></li>
     * </ul>
     */
    @Override public final Type getType()
    {
        if (getSigChidrenCount() != 0)
        {
            int position = 0;
            Element element = getSigChild(position);

            // labeled statement
            if (element instanceof LabelToken)
            {

                if (getSigChidrenCount() == 1)
                {
                    return Type.LABEL;
                }

                element = getSigChild(++position);
            }

            if (ElementUtils.isForWordToken(element) || ElementUtils.isForeachWordToken(element))
            {
                return handleForOrForeach(position, element);
            }

            if (element instanceof WordToken)
            {
                return TYPES.get(element.getContent());
            }

            if (element instanceof BlockStructure)
            {
                return Type.CONTINUE;
            }
        }

        return Type.UNKNOWN;
    }

    /**
     * does the statement have a conditional
     *
     * <p>the conditional may not exist, for example, if the statement malformed.</p>
     *
     * @return <code>true</code> if conditional exists, <code>false</code> otherwise
     */
    public final boolean hasConditional()
    {
        return (getConditional() != null);
    }

    /**
     * does the statement have a 'continue' block?
     *
     * @return <code>true</code> if there is a continue block, <code>false</code> otherwise
     */
    public boolean hasContinue()
    {
        return (getContinue() != null);
    }

    /**
     * does the statement have a label
     *
     * <pre>
     *  LABEL: {
     *    ...
     *  }
     * </pre>
     *
     * @return <code>true</code> if the statement has a label, <code>false</code> otherwise
     */
    public final boolean hasLabel()
    {
        return (getFirstToken() instanceof LabelToken);
    }

    /**
     * is the compound statement valid syntactically?
     *
     * <p>this method will return <code>true</code> in all cases except when <code>getType()</code> indicates it is an
     * <code>if</code> or <code>unless</code> statement. the reason for this is that PPI (and by extension ppi4j) will
     * parse the following into a compound statement even though it is not valid syntax in order to capture the <code>
     * elsif</code>/<code>else</code> portions all in one statement.</p>
     *
     * <pre>
     *   if (...)
     *   [
     *   ]
     *   elsif [...]
     *   {
     *   }
     *   else
     *   {
     *   }
     * </pre>
     *
     * @return <code>true <code>if the statement is valid, <code>false</code> otherwise</code>
     */
    public boolean isValidSyntax()
    {
        if (getType() != Type.IF)
        {
            return true;
        }

        List<Structure> list = find(new SimpleRule()
            {
                @Override public boolean matches(Element element)
                {
                    if (matchesSubclass(element, Structure.class))
                    {
                        if (!matchesClass(element, BlockStructure.class) &&
                                !matchesClass(element, ConditionStructure.class))
                        {
                            return true;
                        }
                    }

                    return false;
                }
            });

        return list.isEmpty();
    }

    private Type handleForOrForeach(int position, Element element)
    {
        if (getSigChidrenCount() < 2)
        {
            return TYPES.get(element.getContent());
        }

        element = getSigChild(++position);

        if (element instanceof Token)
        {
            if (isForeachContent(element.getContent()) || isSymbolOrQLWords(element))
            {
                return Type.FOREACH;
            }
        }

        if (element instanceof ListStructure)
        {
            return Type.FOREACH;
        }

        return Type.FOR;
    }

    private boolean isForeachContent(String content)
    {
        return ("my".equals(content) || "our".equals(content) || "state".equals(content));
    }

    private boolean isSymbolOrQLWords(Element element)
    {
        return ((element instanceof SymbolToken) || (element instanceof QLWordsToken));
    }
}
