package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>CommentToken</code> represents a comment in perl source code.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Comment.pm">CPAN - PPI::Token::Comment</a>
 */
public class CommentToken extends Token
{
    //~ Methods

    /**
     * does the token represent an inline comment?
     *
     * <p>an inline comment is a comment that occurs anywhere within a line.</p>
     *
     * @return <code>true <code>if inline comment, <code>false</code> otherwise</code>
     */
    public boolean isInlineComment()
    {
        return !isLineComment();
    }

    /**
     * does the token represent a line comment?
     *
     * <p>a line comment starts at the beginning of a line and ends with a newline.</p>
     *
     * @return <code>true <code>if line comment, <code>false</code> otherwise</code>
     */
    public boolean isLineComment()
    {
        return getContent().endsWith("\n");
    }

    /*
     * @see org.scriptkitty.ppi4j.Token#isSignificant()
     */
    @Override public boolean isSignificant()
    {
        return false;
    }
}
