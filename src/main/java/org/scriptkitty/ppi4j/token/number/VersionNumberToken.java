package org.scriptkitty.ppi4j.token.number;

import org.scriptkitty.ppi4j.token.NumberToken;


/**
 * a <code>VersionNumberToken</code> represents a token with multiple decimal points.
 *
 * <p>perl does not treat them as numbers, but the parser treats them as such. ie:</p>
 *
 * <ul>
 *   <li><code>1.1.0</code></li>
 *   <li><code>127.0.0.1</code></li>
 *   <li><code>10_000.10_000.10_000</code></li>
 *   <li><code>v1.2.3.4</code></li>
 * </ul>
 *
 * <p><b>note</b>: {@link #toNumber()} is not supported for this token type and will throw an explict <code>NumberFormatException</code> if
 * called.</p>
 */
public class VersionNumberToken extends NumberToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#getBase()
     */
    @Override public int getBase()
    {
        return 256;
    }

    /*
     * @see org.scriptkitty.ppi4j.token.NumberToken#convert(java.lang.String)
     */
    @Override protected Number convert(String literal)
    {
        //J-
        /*
         * PPI does this:
         *
         *   $content =~ s/^v//;
         *   join '', map { chr $_ } ( split /\./, $content );
         *
         * which turns the numbers into letters, which makes no sense at all. i can't really see why you'd
         * ever need to treat one of these like an actual number, so this is unsupported for now.
         */
        //J+
        throw new NumberFormatException("version numbers are not currently supported");
    }
}
