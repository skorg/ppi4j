package org.scriptkitty.ppi4j.token;

import org.scriptkitty.perl.lang.Operators;

import org.scriptkitty.ppi4j.Token;


/**
 * an <code>OperatorToken</code> represents all operator tokens, including ones that look like <code>WordToken</code>
 * objects.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Operator.pm">CPAN - PPI::Token::Operator</a>
 */
public class OperatorToken extends Token
{
    //~ Methods

    /**
     * does this token represent the deference (<code>-&gt;</code>) operator?
     *
     * @return <code>true</code> if operator, <code>false</code> otherwise
     */
    public boolean isDashArrow()
    {
        return Operators.isDashArrow(getContent());
    }

    /**
     * does this token represent the comma synonym (<code>=&gt;</code>) operator?
     *
     * @return <code>true</code> if operator, <code>false</code> otherwise
     */
    public boolean isEqualArrow()
    {
        return Operators.isEqualArrow(getContent());
    }
}
