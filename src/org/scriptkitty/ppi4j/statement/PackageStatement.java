package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public final class PackageStatement extends Statement
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Statement#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    public Token getName()
    {
        return getToken(WordToken.class, 1);
    }
}
