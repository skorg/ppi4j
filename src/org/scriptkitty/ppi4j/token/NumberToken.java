package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>NumberToken</code> is used to represent base-10 numbers.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Number.pm">CPAN - PPI::Token::Number</a>
 */
public class NumberToken extends Token
{
    //~ Methods

    /**
     * get the numeric base for the number
     *
     * <p>this is 10 for decimal numbers, 2 for binary, 8 for octal, and 16 for hexidecimal.</p>
     *
     * @return base
     */
    public int getBase()
    {
        return 10;
    }

    /**
     * get the numeric value of this token
     *
     * @return number object
     *
     * @throws NumberFormatException if the token's content can not be parsed into an integer
     */
    public final Number toNumber() throws NumberFormatException
    {
        return convert(getLiteral());
    }

    /*
     * convert the literal to its numeric value.
     */
    protected Number convert(String literal)
    {
        return Long.valueOf(literal, getBase());
    }

    /*
     * get the literal value to convert
     */
    protected String getLiteral()
    {
        return getContent();
    }
}
