package org.scriptkitty.ppi4j.token.regexp;

import org.scriptkitty.ppi4j.token.RegExpToken;


/**
 * a <code>REMatchToken</code> is used to represent a single match regular expression.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Regexp/Match.pm">CPAN - PPI::Token::Regexp::Match</a>
 */
public final class REMatchToken extends RegExpToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.RegExpToken#hasSubstitution()
     */
    @Override public boolean supportsSubstitution()
    {
        // a match token never has a substitution
        return false;
    }
}
