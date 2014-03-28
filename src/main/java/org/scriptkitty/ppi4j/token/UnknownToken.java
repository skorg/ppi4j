package org.scriptkitty.ppi4j.token;

import org.scriptkitty.ppi4j.Token;


/**
 * an <code>UnknownToken</code> represents an unknown or as-yet undetermined token type.
 *
 * <p><code>UnknownToken</code>s generally only exist during the tokenization phase until they are resolved into a concrete type.</p>
 *
 * <p>if an <code>UnknownToken</code> is found inside of a <code>Document</code> file a bug immediately! :)</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Unknown.pm">CPAN - PPI::Token::Unknown</a>
 */
public class UnknownToken extends Token
{
    // empty implementation
}
