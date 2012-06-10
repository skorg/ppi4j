package org.scriptkitty.ppi4j;

import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <code>Structure</code> is the root class for all perl bracing structures.
 *
 * <p>this covers all forms of <code>[ ... ]</code>, <code>{ ... }</code> and <code>( ... )</code> brace types, and
 * includes cases where only one half of the pair exists.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Structure.pm">CPAN - PPI::Structure</a>
 */
public class Structure extends Node
{
    //~ Static fields/initializers

    private static final Map<String, String> MATCH = new HashMap<String, String>()
    {
        private static final long serialVersionUID = 6559226239429393235L;

        {
            put("{", "}");
            put("}", "{");
            put("[", "]");
            put("]", "[");
            put("(", ")");
            put(")", "(");
        }
    };

    //~ Enums

    public enum BraceType
    {
        CURLY, ROUND, SQUARE, UNKNOWN
    }

    //~ Instance fields

    private Class<? extends Structure> changeTo;

    /** finish brace */
    private Token finish;

    /** start brace */
    private Token start;

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Element#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    /**
     * set the subclass this structure needs to become before it is added to the document.
     *
     * <p>note: this method is only intended to be used by the parser.</p>
     *
     * @param changeTo the structure subclass to become
     */
    public void changeTo(Class<? extends Structure> changeTo)
    {
        this.changeTo = changeTo;
    }

    /**
     * converts this structure into one of it's subclasses.
     *
     * <p>note: this method is only intended to be used by the parser.</p>
     *
     * @return coverted structure
     */
    public Structure convert()
    {
        if (changeTo == null)
        {
            return this;
        }

        Structure struct = (Structure) super.convert(changeTo);

        struct.setStart(start);
        struct.setFinish(finish);

        changeTo = null;

        return struct;
    }

    /**
     * get the brace type used by the structure
     *
     * @return brace type used
     */
    public final BraceType getBraceType()
    {
        BraceType braces = BraceType.UNKNOWN;

        if (((StructureToken) start).isOpenSquare())
        {
            braces = BraceType.SQUARE;
        }
        else if (((StructureToken) start).isOpenCurly())
        {
            braces = BraceType.CURLY;
        }
        else if (((StructureToken) start).isOpenParen())
        {
            braces = BraceType.ROUND;
        }

        return braces;
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#getContent()
     */
    @Override public String getContent()
    {
        StringBuffer buffer = new StringBuffer();

        if (hasStart())
        {
            buffer.append(start.getContent());
        }

        buffer.append(super.getContent());

        if (hasFinish())
        {
            buffer.append(finish.getContent());
        }

        return buffer.toString();
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#getElementCount()
     */
    @Override public int getElementCount()
    {
        int count = super.getElementCount();

        if (hasStart())
        {
            count++;
        }

        if (hasFinish())
        {
            count++;
        }

        return count;
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#getElements()
     */
    @Override public List<Element> getElements()
    {
        List<Element> elements = new ArrayList<Element>(super.getElements());
        addStartFinish(elements);

        return Collections.unmodifiableList(elements);
    }

    /**
     * get the token that represents the closing brace of the structure.
     *
     * <p>it can be quite common for a structure to not have a closing brace, ie: when a user has not added the closing
     * brace of a subroutine.</p>
     *
     * @return token or <code>null</code> if a closing brace has not been encountered
     */
    public Token getFinish()
    {
        return finish;
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#getFirstElement()
     */
    @Override public Element getFirstElement()
    {
        if (hasStart())
        {
            return start;
        }

        if (hasChildren())
        {
            return super.getFirstElement();
        }

        return finish;
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#getLastElement()
     */
    @Override public Element getLastElement()
    {
        if (hasFinish())
        {
            return finish;
        }

        if (hasChildren())
        {
            return super.getLastElement();
        }

        return start;
    }

    /**
     * get the token that represents the opening brace of the structure.
     *
     * <p>under normal parsing circumstances, the structure should always have an opening brace but may occur if the
     * document is manipulated.</p>
     *
     * @return token or <code>null</code> if an opening brace has not been encountered
     */
    public Token getStart()
    {
        return start;
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#getTokens()
     */
    @Override public List<Token> getTokens()
    {
        List<Token> tokens = new ArrayList<Token>();

        tokens.addAll(super.getTokens());
        addStartFinish(tokens);

        return tokens;
    }

    /**
     * has a finish brace been encountered?
     *
     * @return <code>true</code> if a close brace has been seen, <code>false</code> otherwise
     *
     * @see    #getFinish()
     */
    public final boolean hasFinish()
    {
        return (finish != null);
    }

    /**
     * has a start brace been encountered?
     *
     * @return <code>true</code> if an open brace has been seen, <code>false</code> otherwise
     *
     * @see    #getStart()
     */
    public final boolean hasStart()
    {
        return (start != null);
    }

    /**
     * does the structure have a <code>start</code> and <code>finish</code> token?
     *
     * @return <code>true</code> if the structure has both tokens, <code>false</code> otherwise
     */
    public final boolean isComplete()
    {
        return (hasStart() && hasFinish());
    }

    /**
     * does the string represent the closing brace for the structure?
     *
     * <p>note: this method is only intended to be used by the parser.</p>
     *
     * @param  match string to match
     *
     * @return <code>true</code> if the string represents the closing brace for the structure, <code>false</code>
     *         otherwise
     */
    public boolean isStartOpposite(String match)
    {
        if (!MATCH.containsKey(match))
        {
            return false;
        }

        return start.getContent().equals(MATCH.get(match));
    }

    /**
     * set the token that finishes this structure
     *
     * <p>note: this method is only intended to be used by the parser.</p>
     *
     * @param token finish token
     */
    public void setFinish(Token token)
    {
        this.finish = token;
        Element.parent.put(token, this);
    }

    // TODO: add insertBefore
    // TODO: add insertAfter

    /**
     * set the token that starts this structure
     *
     * <p>note: this method is only intended to be used by the parser.</p>
     *
     * @param token start token
     */
    public void setStart(Token token)
    {
        this.start = token;
        Element.parent.put(token, this);
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override public String toString()
    {
        String start = (this.start == null) ? "???" : this.start.getContent();
        String finish = (this.finish == null) ? "???" : this.finish.getContent();

        return start + " ... " + finish;
    }

    @Override protected void removeReferences()
    {
        super.removeReferences();

        start = finish = null;
    }

    // safe b/c tokens are elements
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addStartFinish(List list)
    {
        if (hasStart())
        {
            list.add(0, start);
        }

        if (hasFinish())
        {
            list.add(list.size() - 1, finish);
        }
    }
}
