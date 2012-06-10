package org.scriptkitty.ppi4j.token.number;

import org.scriptkitty.ppi4j.token.NumberToken;


/**
 * an <code>HexNumberToken</code> is used to represent base-16 numbers.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Number/Hex.pm">CPAN - PPI::Token::Number::Hex</a>
 */
public final class HexNumberToken extends NumberToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#getBase()
     */
    @Override public int getBase()
    {
        return 16;
    }

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#getLiteral()
     */
    @Override protected String getLiteral()
    {
        return getContent().replaceFirst("0x", "");
    }
}
