package org.scriptkitty.ppi4j.structure;

import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public final class SubscriptStructure extends Structure
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Structure#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
