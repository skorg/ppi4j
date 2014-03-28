package org.scriptkitty.ppi4j.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.WordToken;


/**
 * delegate class responsible for handling the text considered to an exponential notation number (<code>1e+2</code>)
 *
 * @see org.scriptkitty.ppi4j.token.number.ExpNumberToken
 */
final class ExpNumberScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** illegal character pattern - <code>/\.(e)$/</code> */
    private static final Pattern ILLEGAL = Pattern.compile("\\.(e)$");

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

        String content = tokenizer.getCurrentContent();

        if (content.endsWith("e") || content.endsWith("E"))
        {
            // allow leading +, - signs
            if ("+".equals(next) || "-".equals(next))
            {
                return false;
            }

            Matcher matcher = ILLEGAL.matcher(content);
            if (matcher.find())
            {
                // drop the '.' and switch to a number token...
                tokenizer.getCurrentToken().setContent(content.replace(".", ""));
                tokenizer.switchToToken(NumberToken.class);

                tokenizer.createToken(OperatorToken.class, ".");
                // this should just be the letter 'e'
                tokenizer.createToken(WordToken.class, matcher.group(1));

                return tokenizer.getScanner().tokenizerOnChar(tokenizer);
            }

            /*
             * PPI indicates there is an illegal character in the exponent, but it is not consumed so i am not sure why this indicator gets
             * set
             */
            tokenizer.getCurrentToken().setAttribute(Token.Attribute.INVALID);
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
