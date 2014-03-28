package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * an <code>EndToken</code> represents the useless content after the <code>__END__</code> tag.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/End.pm">CPAN - PPI::Token::End</a>
 */
public class EndToken extends Token
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
