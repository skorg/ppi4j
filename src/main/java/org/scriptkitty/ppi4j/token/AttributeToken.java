package org.scriptkitty.ppi4j.token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>AttributeToken</code> represents a subroutine attribute.
 *
 * <p>given the code, <code>sub foo : bar(something) {}</code>, the <code>bar(something)</code> part is the attribute. the token represents
 * the entire attribute, as its braces and contents are not parsed into the document tree and are treated by perl (and thusly, by us) as a
 * single string.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Attribute.pm">CPAN - PPI::Token::Attribute</a>
 */
public class AttributeToken extends Token
{
    //~ Static fields/initializers

    /** identifier pattern - <code>/^(.+?)\(/</code> */
    private static final Pattern IDENTIFIER = Pattern.compile("^(.+?)\\(");

    /** parameters pattern - <code>/\((.*)\)$/</code> */
    private static final Pattern PARAMETERS = Pattern.compile("\\((.*)\\)$");

    //~ Methods

    /**
     * get the identifier part of the attribute
     *
     * <p>for the attribute <code>foo(bar)</code>, this method would return <code>foo</code>.</p>
     *
     * @return identifier
     */
    public String getIdentifier()
    {
        String content = getContent();
        Matcher matcher = IDENTIFIER.matcher(content);

        if (matcher.find())
        {
            return matcher.group(1);
        }

        return content;
    }

    /**
     * get the attribute parameter string
     *
     * <p>for the attribute <code>foo(bar)</code>, this method would return <code>bar</code> -for <code>foo()</code>, an empty string is
     * returned. if no attributes are specified, <code>foo</code>, <code>null</code> is returned.</p>
     *
     * @return attributes or <code>null</code> if none are specified
     */
    public String getParameters()
    {
        Matcher matcher = PARAMETERS.matcher(getContent());

        if (matcher.find())
        {
            return matcher.group(1);
        }

        return null;
    }
}
