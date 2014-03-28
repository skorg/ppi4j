package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.number.ExpNumberToken;
import org.scriptkitty.ppi4j.token.number.VersionNumberToken;


/**
 * delegate class responsible for handling the text considered to be a floating point number (<code>1.234</code>)
 *
 * @see org.scriptkitty.ppi4j.token.number.FloatNumberToken
 */
final class FloatNumberScanner extends TokenScanner
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        String next = tokenizer.getNextCharacter();

        // allow underscores and numbers to pass through
        if ("_".equals(next) || NumberScanner.isNumber(next))
        {
            return false;
        }

        // if we see a 2nd decimal point, we might be a version number of the '..' operator
        if (".".equals(next))
        {
            String content = tokenizer.getCurrentContent();

            // we're the '..' operator token
            if (content.endsWith("."))
            {
                // switch to a number and chop off the '.'
                tokenizer.switchToToken(NumberToken.class);
                tokenizer.getCurrentToken().setContent(content.substring(0, content.length() - 1));
                // create an operator token
                tokenizer.createToken(OperatorToken.class, "..");
                return true;
            }

            tokenizer.switchToToken(VersionNumberToken.class);
            return false;
        }

        if (NumberScanner.isExponent(next))
        {
            tokenizer.switchToToken(ExpNumberToken.class);
            return false;
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
