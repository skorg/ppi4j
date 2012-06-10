package org.scriptkitty.ppi4j.parser;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizerException;


public interface ITokenProvider
{
    //~ Methods

    Token nextToken() throws TokenizerException;

    void rollback(Token token);
}
