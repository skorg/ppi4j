package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>PodToken</code> object represents a complete section of pod documentation with a perl document.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Pod.pm">CPAN - PPI::Token::POD</a>
 */
public class PodToken extends Token
{
    //~ Methods

    // TODO: add merge

    /**
     * Returns the pod content as an array of individual lines.
     */
    public String[] asLines()
    {
        return getContent().split("(?:\015{1,2}\012|\015|\012)");
    }

    /*
     * @see org.scriptkitty.ppi4j.Token#isSignificant()
     */
    @Override public boolean isSignificant()
    {
        return false;
    }
}
