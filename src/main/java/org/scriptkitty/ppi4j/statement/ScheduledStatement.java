package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public final class ScheduledStatement extends SubStatement
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Statement#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    /*
     * @see org.scriptkitty.ppi4j.statement.SubStatement#getName()
     */
    @Override public Token getName()
    {
        WordToken token = getToken(WordToken.class, 0);

        if (token.isSubKeyword())
        {
            return getToken(WordToken.class, 1);
        }

        return token;
    }

    @Override public Type getType()
    {
        return null;
    }
}
