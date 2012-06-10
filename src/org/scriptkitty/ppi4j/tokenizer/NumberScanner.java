package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.number.BinaryNumberToken;
import org.scriptkitty.ppi4j.token.number.ExpNumberToken;
import org.scriptkitty.ppi4j.token.number.FloatNumberToken;
import org.scriptkitty.ppi4j.token.number.HexNumberToken;
import org.scriptkitty.ppi4j.token.number.OctalNumberToken;

import java.util.regex.Pattern;


/**
 * delegate class responsible for handling the text considered to a number (<code>1</code>) or delegating to another
 * number subclass scanner
 *
 * @see org.scriptkitty.ppi4j.token.NumberToken
 */
final class NumberScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** hex, binary, or octal pattern <code>/^-?0_*$/</code> */
    private static final Pattern HBO = Pattern.compile("^-?0_*$");

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

        if (HBO.matcher(tokenizer.getCurrentContent()).matches())
        {
            if ("x".equals(next))
            {
                tokenizer.switchToToken(HexNumberToken.class);
                return false;
            }

            if ("b".equals(next))
            {
                tokenizer.switchToToken(BinaryNumberToken.class);
                return false;
            }

            if (isNumber(next))
            {
                tokenizer.switchToToken(OctalNumberToken.class);
                return false;
            }
        }

        if (isNumber(next))
        {
            // token has already been created
            return false;
        }

        if (".".equals(next))
        {
            tokenizer.switchToToken(FloatNumberToken.class);
            return false;
        }

        if (isExponent(next))
        {
            tokenizer.switchToToken(ExpNumberToken.class);
            return false;
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    static boolean isExponent(String next)
    {
        return ("e".equals(next) || "E".equals(next));
    }

    static boolean isNumber(String next)
    {
        return Character.isDigit(next.charAt(0));
    }
}
