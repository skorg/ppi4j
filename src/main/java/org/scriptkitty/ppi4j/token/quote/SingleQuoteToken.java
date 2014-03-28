package org.scriptkitty.ppi4j.token.quote;

import org.scriptkitty.ppi4j.token.QuoteToken;


/**
 * a <code>SingleQuoteToken</code> token represents a single quoted string literal.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Quote/Single.pm">CPAN - PPI::Token::Quote::Single</a>
 */
public final class SingleQuoteToken extends QuoteToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.QuoteToken#getLiteral()
     */
    @Override public String getLiteral()
    {
        return getString().replace("\\'", "'").replace("\\\\", "\\");
    }
}
