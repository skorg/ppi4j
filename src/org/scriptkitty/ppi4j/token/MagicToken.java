package org.scriptkitty.ppi4j.token;

/**
 * a <code>MagicToken</code> represents one of perl's 'magic' variables.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Magic.pm">CPAN - PPI::Token::Magic</a>
 */
public final class MagicToken extends SymbolToken
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.token.SymbolToken#getCanonical()
     */
    @Override public String getCanonical()
    {
        return getContent();
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>note</b>:magic variable names retain their sigil.</p>
     */
    @Override public String getName()
    {
        return getContent();
    }
}
