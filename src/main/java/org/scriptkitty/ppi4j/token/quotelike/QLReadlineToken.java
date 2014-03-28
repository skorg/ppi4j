package org.scriptkitty.ppi4j.token.quotelike;

import org.scriptkitty.ppi4j.token.QuoteLikeToken;


/**
 * a <code>QLReadlineToken</code> token is used to read a single line or all the lines from a file.
 *
 * <pre>
 *   # read in a single line
 *   $line = <FILE>;
 *
 *   # from a scalar handle
 *   $line = <$filehandle>;
 *
 *   # read all the lines
 *   @lines = <FILE>;
 * </pre>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/QuoteLike/ReadLine.pm">CPAN - PPI::Token::QuoteLike::ReadLine</a>
 */
public final class QLReadlineToken extends QuoteLikeToken
{
    // empty implementation
}
