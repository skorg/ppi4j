package org.scriptkitty.ppi4j.token.quotelike;

import org.scriptkitty.ppi4j.token.QuoteLikeToken;


/**
 * a <code>QLBacktickToken</code> token represents a command output capturing quote (<code>``</code>).
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/QuoteLike/Backtick.pm">CPAN - PPI::Token::QuoteLike::Backtick</a>
 */
public final class QLBacktickToken extends QuoteLikeToken
{
    //~ Methods

    /**
     * {@inheritDoc}
     *
     * <p>this method is unsupported for <code>QLBacktickToken</code>s.</p>
     */
    @Override public void addSection(Section section) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    /*
     * @see org.scriptkitty.ppi4j.token.QuoteLikeToken#getStringEndIndex()
     */
    @Override protected int getStringEndIndex()
    {
        return getContent().length() - 1;
    }

    /*
     * @see org.scriptkitty.ppi4j.token.QuoteLikeToken#getStringStartIndex()
     */
    @Override protected int getStringStartIndex()
    {
        return (getContent().length() > 1) ? 1 : 0;
    }
}
