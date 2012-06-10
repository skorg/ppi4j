package org.scriptkitty.ppi4j.exception;

/**
 * thrown to indicate an illegal <code>Statement</code> object exists as a child of an element.
 */
public class EmptyNodeException extends RuntimeException
{
    //~ Static fields/initializers

    private static final long serialVersionUID = 5291864062011694092L;

    //~ Constructors

    public EmptyNodeException(String message)
    {
        super(message);
    }
}
