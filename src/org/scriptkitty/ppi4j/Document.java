package org.scriptkitty.ppi4j;

import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public final class Document extends Node
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Element#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    public void destroy()
    {
        removeReferences();
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#isScoped()
     */
    @Override public boolean isScoped()
    {
        return true;
    }
}
