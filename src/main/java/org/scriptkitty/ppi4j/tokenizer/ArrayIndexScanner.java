package org.scriptkitty.ppi4j.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.exception.TokenizingException;


/**
 * delegate class responsible for handling the text considered to an array index (<code>$#array</code>)
 *
 * @see org.scriptkitty.ppi4j.token.ArrayIndexToken
 */
final class ArrayIndexScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** array index pattern - <code>/^([\w:']+)/</code> */
    private static final Pattern ARRAY_INDEX = Pattern.compile("^([\\w:']+)");

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        Matcher matcher = ARRAY_INDEX.matcher(tokenizer.getRestOfCurrentLine());

        if (matcher.find())
        {
            tokenizer.appendToCurrentToken(matcher.group(1), true);
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
