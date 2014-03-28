package org.scriptkitty.ppi4j.statement;

import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


/**
 * <code>SubStatement</code>s are used to describe subroutine statements, as described in <a href="http://perldoc.perl.org/perlsub.html">
 * perlsub</a>.
 *
 * <p>please note, the following are defined as {@link ScheduledStatement}s:</p>
 *
 * <ul>
 *   <li><code>BEGIN</code></li>
 *   <li><code>INIT</code></li>
 *   <li><code>CHECK</code></li>
 *   <li><code>END</code></li>
 * </ul>
 *
 * <p>anonymous subroutine definitions are part of normal {@link Statement} definitions.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Statement/Sub.pm">CPAN - PPI::Statement::Sub</a>
 */
public class SubStatement extends StatementWithBody
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Statement#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    // TODO: add getAttributes()

    /**
     * get the <code>Token</code> that represents the suborutine name
     *
     * @return <code>Token</code> object or the <code>Token.NULL</code> object if it is 'naked'
     */
    public Token getName()
    {
        return getToken(WordToken.class, 1);
    }

    /**
     * is the subroutine is a forward declaration?
     *
     * <p>a subroutine is considered to be a forward declaration if it does not have a body, ie:</p>
     *
     * <pre>sub foo;</pre>
     *
     * @return <code>true</code> if the subroutine is a forward declaration, <code>false</code> otherwise.
     */
    public final boolean isForward()
    {
        return (getBody() == null);
    }

    // TODO: add getPrototype()

    /**
     * convenience method to check if this is a special reserved subroutine.
     *
     * <p>note: this does not check against any special list of subroutine names. instead, it returns <code>true</code> if the name is all
     * in uppercase, as defined in <a href="http://perldoc.perl.org/perlsub.html">perlsub</a>, with the exception of:</p>
     *
     * <ul>
     *   <li><code>BEGIN</code></li>
     *   <li><code>INIT</code></li>
     *   <li><code>CHECK</code></li>
     *   <li><code>UNITCHECK</code></li>
     *   <li><code>END</code></li>
     * </ul>
     *
     * <p>which are defined as {@link ScheduledStatement}s.</p>
     *
     * @return <code>true</code> if the name represents a reserved subroutine name, <code>false</code> otherwise.
     */
    public final boolean isReserved()
    {
        Token token = getName();

        if (token.isNull())
        {
            return false;
        }

        String name = token.getContent();
        return name.toUpperCase().equals(name);
    }

    /*
     * @see org.scriptkitty.ppi4j.statement.StatementWithBody#isScoped()
     */
    @Override public final boolean isScoped()
    {
        return false;
    }
}
