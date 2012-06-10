package org.scriptkitty.ppi4j.token.number;

import org.scriptkitty.ppi4j.token.NumberToken;


/**
 * an <code>OctalNumberToken</code> is used to represent base-8 numbers.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Number/Octal.pm">CPAN - PPI::Token::Number::Octal</a>
 */
public final class OctalNumberToken extends NumberToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#getBase()
     */
    @Override public int getBase()
    {
        return 8;
    }
}
