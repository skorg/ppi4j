package org.scriptkitty.ppi4j.util;

import org.scriptkitty.ppi4j.Token;


/**
 * an implementation of <code>IErrorProxy</code> that prints all errors to the local console/tty.
 */
public class ConsoleErrorProxy implements IErrorProxy
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.util.IErrorProxy#reportASTVisitorError(java.lang.Throwable)
     */
    @Override public void reportASTVisitorError(Throwable throwable)
    {
        System.out.println("ast visitor error: " + throwable);
    }

    /*
     * @see org.scriptkitty.ppi4j.util.IErrorProxy#reportTokenizerError(java.lang.Throwable)
     */
    @Override public void reportTokenizerError(Throwable throwable)
    {
        System.out.println("tokenizer error: " + throwable);
    }

    /*
     * @see org.scriptkitty.ppi4j.util.IErrorProxy#reportUnmatchedBrace(org.scriptkitty.ppi4j.Token)
     */
    @Override public void reportUnmatchedBrace(Token token)
    {
        System.out.println("uncountered umatched brace : " + token);
    }
}
