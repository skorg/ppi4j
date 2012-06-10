package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>WhitespaceToken</code> represents sections of whitespace in perl documents.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Whitespace.pm">CPAN - PPI::Token::Whitespace</a>
 */
public class WhitespaceToken extends Token
{
    //~ Static fields/initializers

    /** represents a null string */
    public static final Token NULL = new WhitespaceToken()
    {
        {
            setContent("");
        }
    };

    //~ Methods

    /**
     * does this token represent empty whitespace?
     *
     * @return <code>true</code> if empty whitespace, <code>false</code> otherwise
     */
    public boolean isEmptyWhitespace()
    {
        return "".equals(getContent());
    }

    /*
     * @see org.scriptkitty.ppi4j.Token#isSignificant()
     */
    @Override public boolean isSignificant()
    {
        return false;
    }
}
