package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.exception.TokenizingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * delegate class responsible for handling the text considered to be a prototype
 *
 * @see org.scriptkitty.ppi4j.token.PrototypeToken
 */
final class PrototypeScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** end of prototype pattern - <code>/(.*?(?:\)|$))/</code> */
    private static final Pattern CLOSE = Pattern.compile("(.*?(?:\\)|$))");

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        String line = tokenizer.getRestOfCurrentLine();
        Matcher matcher = CLOSE.matcher(line);

        if (matcher.find())
        {
            tokenizer.appendToCurrentToken(matcher.group(1), true);
        }

        // shortcut EOF
        if (line.matches("\\)$"))
        {
            return true;
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
