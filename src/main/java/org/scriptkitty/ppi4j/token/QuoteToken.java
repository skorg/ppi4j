package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>QuoteToken</code> is the base class for all "quote" tokens.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Quote.pm">CPAN - PPI::Token::Quote</a>
 */
public abstract class QuoteToken extends Token
{
    //~ Methods

    /**
     * get the value of the string as perl would see it.
     *
     * <p>this method strips any quote marks from the string and resolves <code>\\</code> to <code>\</code> and <code>\'</code> to <code>
     * '</code>.</p>
     *
     * @return literal value
     *
     * @throws UnsupportedOperationException if this method is not supported by the <code>QuoteToken</code> subclass
     */
    public abstract String getLiteral() throws UnsupportedOperationException;

    /**
     * get the string contained by this token, minus its 'quotes'
     *
     * <p>ie, <code>foo</code> would be returned for all of the following:</p>
     *
     * <ul>
     *   <li><code>'foo'</code></li>
     *   <li><code>"foo"</code></li>
     *   <li><code>q{foo}</code></li>
     *   <li><code>qq&lt;foo&gt;</code></li>
     * </ul>
     *
     * @return string minus its quotes
     */
    public final String getString()
    {
        return getContent().substring(getStringStartIndex(), getStringEndIndex());
    }

    /**
     * get the end offset of the string contents
     *
     * @return end offset
     */
    public final int getStringEndOffset()
    {
        return getStringStartOffset() + getString().length();
    }

    /**
     * get the start offset of the string contents
     *
     * @return start offset
     */
    public final int getStringStartOffset()
    {
        return getStartOffset() + getStringStartIndex();
    }

    /**
     * get the end index of the token's string value.
     *
     * <p>ie, if the token is a <code>SingleQuote</code> token, the end index would be one to move behind the ending <code>'</code>
     * character.</p>
     *
     * @return end index
     */
    protected int getStringEndIndex()
    {
        return getContent().length() - 1;
    }

    /**
     * get the start index of the token's string value.
     *
     * <p>ie, if the token is a <code>SingleQuote</code> token, the start index would be one to move past the starting <code>'</code>
     * character.</p>
     *
     * @return start index
     */
    protected int getStringStartIndex()
    {
        return (getContent().length() > 1) ? 1 : 0;
    }
}
