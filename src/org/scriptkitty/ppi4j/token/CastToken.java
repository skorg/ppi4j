package org.scriptkitty.ppi4j.token;

import org.scriptkitty.perl.lang.Symbols;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>CastToken</code> represents a prefix which forces a value into a different context.
 *
 * <p>a 'cast' in ppi4j terms is one or more characters used as a prefix which forces a value into a different class or
 * content. this includes referencing, dereferencing, and a few other minor cases.</p>
 *
 * <p>for expressions such as <code>@$foo</code> or <code>@{ $foo{bar} }</code>, the <code>@</code> in both cases
 * represents a cast, in this case, an array dereference.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Cast.pm">CPAN - PPI::Token::Cast</a>
 */
public class CastToken extends Token
{
    //~ Methods

    /**
     * does this token represent a cast to an array?
     *
     * @return <code>true</code> if array cast, <code>false</code> otherwise
     */
    public boolean isArrayCast()
    {
        return Symbols.isArraySigil(getContent());
    }

    /**
     * does this token represent a cast using <code>$#</code>?
     *
     * @return <code>true</code> if <code>$#</code> cast, <code>false</code> otherwise
     */
    public boolean isDollarPoundCast()
    {
        return Symbols.isDollarPound(getContent());
    }

    /**
     * does this token represent a cast to a hash?
     *
     * @return <code>true</code> if hash cast, <code>false</code> otherwise
     */
    public boolean isHashCast()
    {
        return Symbols.isHashSigil(getContent());
    }

    /**
     * does this token represent a cast to a scalar?
     *
     * @return <code>true</code> if scalar cast, <code>false</code> otherwise
     */
    public boolean isScalarCast()
    {
        return Symbols.isScalarSigil(getContent());
    }
}
