package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>BOMToken</code> represents unicode byte order marks.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/BOM.pm">CPAN - PPI::Token::BOM</a>
 */
public class BOMToken extends Token
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Token#isSignificant()
     */
    @Override public boolean isSignificant()
    {
        return false;
    }
}
