package org.scriptkitty.ppi4j;

import java.util.List;

import org.scriptkitty.ppi4j.finder.SimpleRule;
import org.scriptkitty.ppi4j.statement.CompoundStatement;
import org.scriptkitty.ppi4j.statement.GivenStatement;
import org.scriptkitty.ppi4j.statement.WhenStatement;
import org.scriptkitty.ppi4j.util.ElementUtils;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public class Statement extends Node
{
    //~ Static fields/initializers

    private static String ONE = "1";

    private static String SEMI = ";";

    //~ Enums

    public enum Type
    {
        CONTINUE, FOR, FOREACH, IF, LABEL, NO, NONE, REQUIRE, UNKNOWN, USE, WHILE
    }

    //~ Instance fields

    private Class<? extends Statement> changeTo;

    //~ Constructors

    public Statement()
    {
        // default constructor
    }

    public Statement(Token token)
    {
        addChild(token);
    }

    //~ Methods

    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    public final void changeTo(Class<? extends Statement> changeTo)
    {
        this.changeTo = changeTo;
    }

    public final boolean containsStructure(final Class<? extends Structure> clazz)
    {
        List<Structure> list = find(new SimpleRule()
            {
                @Override public boolean matches(Element element)
                {
                    return matchesSubclass(element, clazz);
                }
            });

        return (!list.isEmpty());
    }

    public final boolean containsToken(Class<? extends Token> clazz)
    {
        return containsToken(clazz, null);
    }

    public final boolean containsToken(final Class<? extends Token> clazz, final String content)
    {
        List<Token> list = find(new SimpleRule()
            {
                @Override public boolean matches(Element element)
                {
                    if (matchesSubclass(element, clazz))
                    {
                        if (content != null)
                        {
                            return content.equals(element.getContent());
                        }

                        return true;
                    }

                    return false;
                }
            });

        return (!list.isEmpty());
    }

    public final Statement convert()
    {
        if (changeTo == null)
        {
            return this;
        }

        return (Statement) super.convert(changeTo);
    }

    /**
     * get the syntatic type of the statement.
     *
     * <p>the default implementation returns a type of <code>NONE</code>, however sub-classes may return something different.</p>
     *
     * @return statement type
     */
    public Type getType()
    {
        return Type.NONE;
    }

    public boolean isComplete()
    {
        return ElementUtils.isSemiColonToken(getSigChild(-1));
    }

    /**
     * returns <code>true</code> if the statement terminates with a ';'.
     */
    public boolean isNormal()
    {
        if ((changeTo == CompoundStatement.class) || (changeTo == WhenStatement.class) || (changeTo == GivenStatement.class))
        {
            return false;
        }

        return true;
    }

    /**
     * returns <code>true</code> if the statement represents the <code>true</code> value that must be returned at the end of a perl module.
     */
    public final boolean isTerminator()
    {
        // easy check, if we don't have exactly 2, we can never be the terminator
        if (getSigChidrenCount() != 2)
        {
            return false;
        }

        Element first = getSigChild(0);
        Element second = getSigChild(1);

        //J-
        /*
         * there can be cases where a Structure object can be the first significant token, ie:
         *
         * #!/usr/bin/perl
         * ();
         *
         * or the last significant token, ie:
         *
         * #!/usr/bin/perl
         * $a{lc($b)}
         */
        //J+
        if ((first instanceof Token) && (second instanceof Token))
        {
            if (ONE.equals(first.getContent()) && SEMI.equals(second.getContent()))
            {
                return true;
            }
        }

        return false;
    }

}
