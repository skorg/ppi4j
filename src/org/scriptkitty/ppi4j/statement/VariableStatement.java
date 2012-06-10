package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public final class VariableStatement extends Statement
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Statement#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override public Type getType()
    {
        return null;
    }
}
