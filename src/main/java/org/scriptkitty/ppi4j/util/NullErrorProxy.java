package org.scriptkitty.ppi4j.util;

import org.scriptkitty.ppi4j.Token;


/**
 * an implementation of <code>IErrorProxy</code> that supresses all error messages.
 */
public final class NullErrorProxy implements IErrorProxy
{
    //~ Static fields/initializers

    private static IErrorProxy self;

    //~ Constructors

    private NullErrorProxy()
    {
    }

    //~ Methods

    /**
     * get an instance of the error proxy
     *
     * @return null proxy instance
     */
    public static IErrorProxy getInstance()
    {
        if (self == null)
        {
            self = new NullErrorProxy();
        }

        return self;
    }

    /*
     * @see org.scriptkitty.ppi4j.util.IErrorProxy#reportASTVisitorError(java.lang.Throwable)
     */
    @Override public void reportASTVisitorError(Throwable cause)
    {
        // do nothing
    }

    /*
     * @see org.scriptkitty.ppi4j.util.IErrorProxy#reportTokenizerError(java.lang.Throwable)
     */
    @Override public void reportTokenizerError(Throwable cause)
    {
        // do nothing
    }

    @Override public void reportUnmatchedBrace(Token token)
    {
        // do nothing
    }
}
