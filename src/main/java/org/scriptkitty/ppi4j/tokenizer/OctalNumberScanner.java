package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;


/**
 * delegate class responsible for handling the text considered to an octal number (<code>0777</code>)
 *
 * @see org.scriptkitty.ppi4j.token.number.OctalNumberToken
 */
final class OctalNumberScanner extends TokenScanner
{
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

        if (NumberScanner.isNumber(next))
        {
            // can't have 8's and 9's, but need to include all for proper tokenization
            if ("8".equals(next) || "9".equals(next))
            {
                tokenizer.getCurrentToken().setAttribute(Token.Attribute.INVALID);
            }

            return false;
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
