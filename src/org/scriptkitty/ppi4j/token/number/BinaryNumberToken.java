package org.scriptkitty.ppi4j.token.number;

import org.scriptkitty.ppi4j.token.NumberToken;


/**
 * a <code>BinaryNumberToken</code> is used to represent base-2 numbers.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Number/Binary.pm">CPAN - PPI::Token::Number::Binary</a>
 */
public final class BinaryNumberToken extends NumberToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#getBase()
     */
    @Override public int getBase()
    {
        return 2;
    }

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#getLiteral()
     */
    @Override protected String getLiteral()
    {
        return getContent().replaceFirst("0b", "");
    }
}
