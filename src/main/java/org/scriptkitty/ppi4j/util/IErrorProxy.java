package org.scriptkitty.ppi4j.util;

import org.scriptkitty.ppi4j.Token;


/**
 * an error proxy provides a way to report any encountered errors to the end user.
 */
public interface IErrorProxy
{
    //~ Methods

    /**
     * report an error that occurred while attempting to build the abstract syntax tree.
     *
     * @param cause error
     */
    void reportASTVisitorError(Throwable cause);

    /**
     * report an error that occurred while tokenizing the source code.
     *
     * @param cause error
     */
    void reportTokenizerError(Throwable cause);

    /**
     * report an 'umatched brace' was encountered during parsing
     *
     * @param token token representing the unmatched brace
     */
    void reportUnmatchedBrace(Token token);
}
