package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.visitor.INodeVisitor;


/**
 * <code>GivenStatement</code>s are used to describe <code>switch</code> statements, as described in <a
 * href="http://perldoc.perl.org/perlsyn.html">perlsyn</a>.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Statement/Given.pm">CPAN - PPI::Statement::Given</a>
 */
public class GivenStatement extends StatementWithBody
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
