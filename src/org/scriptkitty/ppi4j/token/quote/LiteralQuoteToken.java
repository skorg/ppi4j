package org.scriptkitty.ppi4j.token.quote;

import org.scriptkitty.ppi4j.token.QuoteToken;
import org.scriptkitty.ppi4j.token.SectionedToken;


/**
 * a <code>LiteralQuoteToken</code> token represents the single literal quote-like operator (<code>q//</code>).
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Quote/Literal.pm">CPAN - PPI::Token::Quote::Literal</a>
 */
public class LiteralQuoteToken extends QuoteToken implements SectionedToken
{
    //~ Instance fields

    private SectionedToken.Contents contents = new SectionedToken.Contents();

    //~ Methods

    /**
     * {@inheritDoc}
     *
     * <p>this method is currently unsupported for <code>LiteralQuoteToken</code>s.</p>
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

    /*
     * @see org.scriptkitty.ppi4j.token.QuoteToken#getLiteral()
     */
    @Override public String getLiteral()
    {
        return getString().replace("\\'", "'").replace("\\\\", "\\");
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
