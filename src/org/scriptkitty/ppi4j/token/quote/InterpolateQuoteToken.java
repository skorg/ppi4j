package org.scriptkitty.ppi4j.token.quote;

import org.scriptkitty.ppi4j.token.QuoteToken;
import org.scriptkitty.ppi4j.token.SectionedToken;


/**
 * an <code>InterpolateQuoteToken</code> token represents the single interpolation quote-like operator
 * (<code>qq//</code>).
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Quote/Interpolate.pm">CPAN -
 *      PPI::Token::Quote::Interpolate</a>
 */
public class InterpolateQuoteToken extends QuoteToken implements SectionedToken
{
    //~ Instance fields

    private SectionedToken.Contents contents = new SectionedToken.Contents();

    //~ Methods

    /**
     * {@inheritDoc}
     *
     * <p>this method is currently unsupported for <code>InterpolateQuoteToken</code>s.</p>
     */
    @Override public void addModifier(String modifier) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    /*
     * @see org.scriptkitty.ppi4j.token.SectionedToken#addSection(org.scriptkitty.ppi4j.token.SectionedToken.Section)
     */
    @Override public void addSection(Section section)
    {
        contents.addSection(section);
    }

    /**
     * {@inheritDoc}
     *
     * <p>this method is currently unsupported for <code>InterpolateQuoteToken</code>s.</p>
     */
    @Override public String getLiteral() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("getLiteral() not supported");
    }

    /*
     * @see org.scriptkitty.ppi4j.token.QuoteToken#getStringEndIndex()
     */
    @Override protected int getStringEndIndex()
    {
        return getStringStartIndex() + contents.getSection(0).size;
    }

    /*
     * @see org.scriptkitty.ppi4j.token.QuoteToken#getStartIndex()
     */
    @Override protected int getStringStartIndex()
    {
        return contents.getSection(0).position;
    }
}
