package org.scriptkitty.ppi4j.token.quotelike;

import org.scriptkitty.ppi4j.token.QuoteLikeToken;

import java.util.List;


/**
 * a <code>QLRegExpToken</code> token represents the quote-like operator (<code>qr//</code>)used to construct anonymous
 * regular expression objects.
 *
 * <pre>
 *   # create a regexp object for a module filename
 *   my $module = qr/\.pm$/;
 * </pre>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/QuoteLike/RegExp.pm">CPAN -
 *      PPI::Token::QuoteLike::RegExp</a>
 */
public final class QLRegExpToken extends QuoteLikeToken
{
    //~ Methods

    // TODO: provide method to get delimiters

    /*
     * @see org.scriptkitty.ppi4j.token.SectionedToken#addModifier(java.lang.String)
     */
    @Override public void addModifier(String modifier)
    {
        getContents().addModifier(modifier);
    }

    /**
     * get the portion of the regular expression that represents the match string
     *
     * <p>if the expression does not have a match string or if the expression is incomplete (missing the closing
     * delimiter), <code>null</code> will be returned.</p>
     *
     * @return match string or <code>null</code> if it does not exist
     *
     * @see    #isMatchComplete()
     */
    public String getMatch()
    {
        if (isMatchComplete())
        {
            return null;
        }

        Section section = getContents().getSection(0);
        return getContent().substring(section.position, section.position + section.size);
    }

    /**
     * get the list of modifiers assigned to this regular expression
     *
     * <p><b>note</b>: the tokenizer does not distinguish between what is a valid modifier and what is not. it is up to
     * the calling user to validate the list of values returned.</p>
     *
     * @return modifier list
     */
    public List<String> getModifiers()
    {
        return getContents().getModifiers();
    }

    /**
     * does the regular expression have any modifiers?
     *
     * @return <code>true</code> if it has modifieres, <code>false</code> otherwise
     */
    public boolean hasModifiers()
    {
        return !getModifiers().isEmpty();
    }

    /**
     * is the match portion of the regular expression complete?
     *
     * @return <code>true</code> if complete, <code>false</code> otherwise
     *
     * @see    #getMatch()
     */
    public boolean isMatchComplete()
    {
        Section section = getContents().getSection(0);

        if ((section != null) && section.complete)
        {
            return true;
        }

        return false;
    }
}
