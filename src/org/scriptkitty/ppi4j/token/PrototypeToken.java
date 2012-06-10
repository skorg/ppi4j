package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>PrototypeToken</code>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Prototype.pm">CPAN - PPI::Token::Prototype</a>
 */
public class PrototypeToken extends Token
{
    //~ Methods

    /**
     * get the prototype pattern, stripped of any braces and whitespace
     *
     * @return prototype
     */
    public String getPrototype()
    {
        return getContent().replace("(", "").replace(")", "").replace(" ", "");
    }
}
