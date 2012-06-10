package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.visitor.INodeVisitor;


public class Perl6IncludeStatement extends IncludeStatement
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Statement#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
