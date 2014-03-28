package org.scriptkitty.ppi4j.parser;

import java.util.Stack;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizerException;
import org.scriptkitty.ppi4j.tokenizer.Tokenizer;


public class DefaultTokenProvider implements ITokenProvider
{
    //~ Instance fields

    private Stack<Token> rollback;

    private Tokenizer tokenizer;

    //~ Constructors

    public DefaultTokenProvider(String source)
    {
        this.rollback = new Stack<>();
        this.tokenizer = new Tokenizer(source);
    }

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.lexer.ITokenProvider#nextToken()
     */
    @Override public Token nextToken() throws TokenizerException
    {
        if (!rollback.isEmpty())
        {
            return rollback.pop();
        }

        return tokenizer.next();
    }

    /*
     * @see org.scriptkitty.ppi4j.lexer.ITokenProvider#rollback(org.scriptkitty.ppi4j.ast.Token)
     */
    @Override public void rollback(Token token)
    {
        rollback.push(token);
    }
}
