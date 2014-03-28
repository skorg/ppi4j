package org.scriptkitty.ppi4j.exception;

/**
 * thrown to indicate there was an error parsing content into a token
 */
public class TokenizingException extends Exception
{
    //~ Static fields/initializers

    private static final long serialVersionUID = 7844389438372729780L;

    //~ Constructors

    public TokenizingException(String error)
    {
        super(error);
    }
}
