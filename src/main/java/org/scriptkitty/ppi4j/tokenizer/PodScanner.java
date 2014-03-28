package org.scriptkitty.ppi4j.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * delegate class responsible for handling the text that comes after the start of pod (<code>=head1</code>)
 *
 * @see org.scriptkitty.ppi4j.token.PodToken
 */
final class PodScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** pod pattern - <code>/^=(\w+)/</code> */
    static final Pattern POD = Pattern.compile("^=(\\w+)");

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnLineStart(org.scriptkitty.ppi4j.tokenizer.Tokenizer, java.lang.String)
     */
    @Override protected boolean tokenizerOnLineStart(Tokenizer tokenizer, String line)
    {
        tokenizer.appendToCurrentToken(line);

        Matcher matcher = POD.matcher(line);

        if (matcher.find() && matcher.group(1).equals("cut"))
        {
            tokenizer.finalizeToken();
        }

        return true;
    }
}
