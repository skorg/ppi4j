package org.scriptkitty.ppi4j.token.quote;

import org.scriptkitty.ppi4j.token.QuoteToken;

import java.util.regex.Pattern;


/**
 * a <code>DoubleQuoteToken</code> token represents a doule quoted string literal.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Quote/Double.pm">CPAN - PPI::Token::Quote::Double</a>
 */
public class DoubleQuoteToken extends QuoteToken
{
    //~ Static fields/initializers

    // TODO: implement simplify?

    private static final Pattern INTERPOLATIONS = Pattern.compile("(?<!\\\\)(?:\\\\)*[\\$\\@]");

    //~ Methods

    /**
     * {@inheritDoc}
     *
     * <p>this method is currently unsupported for <code>DoubleQuoteToken</code>s.</p>
     */
    @Override public String getLiteral() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("getLiteral() not supported");
    }

    /**
     * does the string contain any interpolated values?
     *
     * @return <code>true</code> if there are interpolated values, <code>false</code> otherwise
     */
    public boolean hasInterpolations()
    {
        return INTERPOLATIONS.matcher(getContent()).find();
    }
}
