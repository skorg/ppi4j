package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>QuoteLikeToken</code> is the base class for all "quote-like" tokens.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/QuoteLike.pm">CPAN - PPI::Token::QuoteLike</a>
 */
public abstract class QuoteLikeToken extends Token implements SectionedToken
{
    //~ Instance fields

    private SectionedToken.Contents contents = new SectionedToken.Contents();

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.SectionedToken#addModifier(java.lang.String)
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
     * get the string contained by this token, minus its 'quotes'
     *
     * <p>ie, <code>foo</code> would be returned for all of the following:</p>
     *
     * <ul>
     *   <li><code>`foo`</code></li>
     *   <li><code>&lt;foo&gt;</code></li>
     *   <li><code>qx{foo}</code></li>
     *   <li><code>qr/foo/</code></li>
     *   <li><code>qw/foo/;</code></li>
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

    protected final SectionedToken.Contents getContents()
    {
        return contents;
    }

    /**
     * get the end index of the token's string value.
     *
     * <p>ie, if the token is a <code>QLBacktickToken</code> token, the end index would be one to move behind the ending <code>`</code>
     * character.</p>
     *
     * @return end index
     */
    protected int getStringEndIndex()
    {
        return (contents.hasSections()) ? (getStringStartIndex() + contents.getSection(0).size) : 0;
    }

    /**
     * get the start index of the token's string value.
     *
     * <p>ie, if the token is a <code>QLBacktickToken</code> token, the start index would be one to move past the starting <code>`</code>
     * character.</p>
     *
     * @return start index
     */
    protected int getStringStartIndex()
    {
        return (contents.hasSections()) ? contents.getSection(0).position : 0;
    }
}
