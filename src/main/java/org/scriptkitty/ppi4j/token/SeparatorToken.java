package org.scriptkitty.ppi4j.token;

import org.scriptkitty.perl.lang.Words;


/**
 * a <code>SeparatorToken</code> represents a <code>__DATA__</code> or <code>__END</code> keyword.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Separator.pm">CPAN - PPI::Token::Separator</a>
 */
public final class SeparatorToken extends WordToken
{
    //~ Methods

    /**
     * does this token represent the data separator keyword <code>__DATA__</code>?
     *
     * @return <code>true</code> if data separator, <code>false</code> otherwise
     */
    public boolean isDataSeparator()
    {
        return Words.isDataSeparator(getContent());
    }

    /**
     * does this token represent the end separator keyword <code>__END__</code>?
     *
     * @return <code>true</code> if end separator, <code>false</code> otherwise
     */
    public boolean isEndSeparator()
    {
        return Words.isEndSeparator(getContent());
    }
}
