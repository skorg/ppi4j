package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;

import java.util.regex.Pattern;


/**
 * delegate class responsible for handling the text considered to a binary number (<code>0b1110011</code>)
 *
 * @see org.scriptkitty.ppi4j.token.number.BinaryNumberToken
 */
final class BinaryNumberScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** binary pattern - <code>/[\w\d]/</code> */
    private static final Pattern BINARY = Pattern.compile("[\\w\\d]");

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        String next = tokenizer.getNextCharacter();

        // allow underscores to pass through
        if ("_".equals(next))
        {
            return false;
        }

        if (BINARY.matcher(next).matches())
        {
            // can only have 1's and 0's, but need to include all for proper tokenization
            if (!("0".equals(next) || "1".equals(next)))
            {
                tokenizer.getCurrentToken().setAttribute(Token.Attribute.INVALID);
            }

            return false;
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
