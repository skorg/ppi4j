package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public final class DataStatement extends Statement
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
     * @see org.scriptkitty.ppi4j.Statement#isComplete()
     */
    @Override public boolean isComplete()
    {
        // data statements are never complete
        return false;
    }
}
