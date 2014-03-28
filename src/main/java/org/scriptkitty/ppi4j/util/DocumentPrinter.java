package org.scriptkitty.ppi4j.util;

import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Node;
import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.PodToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.visitor.AbstractNodeVisitor;


/**
 * an implementation of <code>INodeVisitor</code> that prints the contents of a {@link org.scriptkitty.ppi4j.Document} to the console/tty.
 */
public class DocumentPrinter extends AbstractNodeVisitor
{
    //~ Instance fields

    private boolean incAll;
    private boolean incComments;
    private boolean incPOD;
    private boolean incTokens;
    private boolean incWhitespace;

    //~ Constructors

    public DocumentPrinter()
    {
        // tokens always printed by default
        this.incTokens = true;
    }

    //~ Methods

    /**
     * toggle whether all 'insignificant' tokes should be included in the output.
     *
     * <p>insignificant tokesn include the following:</p>
     *
     * <ul>
     *   <li>whitespace</li>
     *   <li>comments</li>
     *   <li>pod</li>
     * </ul>
     */
    public void includeAll(boolean include)
    {
        this.incAll = include;
    }

    /**
     * toggle whether comments should be included in the output.
     */
    public void includeComments(boolean include)
    {
        this.incComments = include;
    }

    /**
     * toggle whether pod should be included in the output.
     */
    public void includePOD(boolean include)
    {
        this.incPOD = include;
    }

    /**
     * toggle whether the individual tokens comprising the statement, structure, etc should be printed.
     *
     * <p>this option is enabled by default.</p>
     */
    public void includeTokens(boolean include)
    {
        this.incTokens = include;
    }

    /**
     * toggle whether whitespace should be included in the output.
     */
    public void includeWhitespace(boolean include)
    {
        this.incWhitespace = include;
    }

    @Override public void visit(Node node)
    {
        print(node, (node instanceof Structure) ? true : false);
        visitChildren(node);
    }

    /*
     * @see org.scriptkitty.ppi4j.ast.AbstractNodeVisitor#visit(org.scriptkitty.ppi4j.Token)
     */
    @Override public void visit(Token token)
    {
        if (!incTokens)
        {
            return;
        }

        if (token.isSignificant() || incAll)
        {
            print(token);
        }
        else if ((token instanceof WhitespaceToken) && incWhitespace)
        {
            print(token);
        }
        else if ((token instanceof CommentToken) && incComments)
        {
            print(token);
        }
        else if ((token instanceof PodToken) && incPOD)
        {
            print(token);
        }
    }

    private void print(Element element)
    {
        print(element, true);
    }

    private void print(Element element, boolean toString)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < (getLevel() * 2); i++)
        {
            buffer.append(" ");
        }

        buffer.append(element.getClass().getName());
        if (toString)
        {
            buffer.append(" ");
            buffer.append(element.toString());
        }

        System.out.println(buffer);
    }
}
