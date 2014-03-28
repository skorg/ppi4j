package org.scriptkitty.ppi4j.token.number;

import org.scriptkitty.ppi4j.token.NumberToken;


/**
 * a <code>FloatNumberToken</code> is used to represent floating point numbers.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Number/Float.pm">CPAN - PPI::Token::Number::Float</a>
 */
public class FloatNumberToken extends NumberToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#convert(java.lang.String)
     */
    @Override protected Number convert(String literal)
    {
        return Float.valueOf(literal);
    }
}
