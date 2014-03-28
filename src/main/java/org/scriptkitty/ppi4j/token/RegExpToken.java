package org.scriptkitty.ppi4j.token;

import java.util.List;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>RegExpToken</code> is the base class for all regular expression tokens.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Regexp.pm">CPAN - PPI::Token::Regexp</a>
 */
public abstract class RegExpToken extends Token implements SectionedToken
{
    //~ Instance fields

    // TODO: add methods to get delimiters

    private SectionedToken.Contents contents = new SectionedToken.Contents();

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.SectionedToken#addModifier(java.lang.String)
     */
    @Override public void addModifier(String modifier)
    {
        contents.addModifier(modifier);
    }

    /*
     * @see org.scriptkitty.ppi4j.token.SectionedToken#addSection(org.scriptkitty.ppi4j.token.SectionedToken.Section)
     */
    @Override public void addSection(Section section)
    {
        contents.addSection(section);
    }

    /**
     * get the portion of the regular expression that represents the match string
     *
     * <p>if the expression does not have a match string or if the expression is incomplete (missing the closing delimiter), <code>
     * null</code> will be returned.</p>
     *
     * @return match string or <code>null</code> if it does not exist
     *
     * @see    #isMatchComplete()
     */
    public String getMatch()
    {
        return getSectionContent(0);
    }

    /**
     * get the list of modifiers assigned to this regular expression
     *
     * <p><b>note</b>: the tokenizer does not distinguish between what is a valid modifier and what is not. it is up to the calling user to
     * validate the list of values returned.</p>
     *
     * @return modifier list
     */
    public List<String> getModifiers()
    {
        return contents.getModifiers();
    }

    /**
     * get the portion of the regular expression that represents the substitution string
     *
     * <p>if the expression does not have a substitution string or if the expression is incomplete (missing the closing delimiter), <code>
     * null</code> will be returned.</p>
     *
     * @return substitution expression or <code>null</code> if it does not exist
     *
     * @see    #isSubstitutionComplete()
     */
    public String getSubstitution()
    {
        return getSectionContent(1);
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
        return isComplete(0);
    }

    /**
     * is the substitution portion of the regular expression complete?
     *
     * @return <code>true</code> if complete, <code>false</code> otherwise
     *
     * @see    #getSubstitution()
     */
    public boolean isSubstitutionComplete()
    {
        return isComplete(1);
    }

    /**
     * does the regular expression represented by this token support substitutions?
     *
     * <p>this method returns <code>true</code> for all but the <code>REMatchToken</code>.</p>
     *
     * @return <code>true</code> if supported, <code>false</code> otherwise
     */
    public boolean supportsSubstitution()
    {
        return true;
    }

    private String getSectionContent(int index)
    {
        Section section = contents.getSection(index);

        if (!isComplete(index))
        {
            return null;
        }

        return getContent().substring(section.position, section.position + section.size);
    }

    private boolean isComplete(int index)
    {
        Section section = contents.getSection(index);

        if ((section != null) && section.complete)
        {
            return true;
        }

        return false;
    }
}
