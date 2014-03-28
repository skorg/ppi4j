package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.exception.TokenizingException;

import java.util.regex.Pattern;


/**
 * delegate class responsible for handling the text considered to a hexidecimal number (<code>0x1234</code>)
 *
 * @see org.scriptkitty.ppi4j.token.number.HexNumberToken
 */
final class HexNumberScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** hex pattern - <code>/[\da-f]/</code> */
    private static final Pattern HEX = Pattern.compile("[\\da-f]");

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

        if (HEX.matcher(next).matches())
        {
            return false;
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
