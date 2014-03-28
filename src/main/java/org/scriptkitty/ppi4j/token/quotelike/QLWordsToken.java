package org.scriptkitty.ppi4j.token.quotelike;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.token.QuoteLikeToken;


/**
 * a <code>QLWordsToken</code> token represents the quote-like operator (<code>qw//</code>) that acts as a constructor for a list of words.
 *
 * <pre>
 *   # create a list for a significant chunk of the alphabet
 *   my @list = qw{a b c d e f g h i j k l};
 * </pre>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/QuoteLike/Words.pm">CPAN - PPI::Token::QuoteLike::Words</a>
 */
public final class QLWordsToken extends QuoteLikeToken
{
    //~ Static fields/initializers

    private static final Pattern EMPTY = Pattern.compile("^\\s*$");

    //~ Methods

    /**
     * get the list of words contained constructed by operator
     *
     * @return list of words
     */
    public List<String> getLiteral()
    {
        String str = getString();

        if (EMPTY.matcher(str).matches())
        {
            return Collections.emptyList();
        }

        return Arrays.asList(str.trim().split(" "));
    }
}
