package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.number.VersionNumberToken;
import org.scriptkitty.ppi4j.util.ElementUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * delegate class responsible for handling the text considered to be a byte-packed number
 *
 * @see org.scriptkitty.ppi4j.token.number.VersionNumberToken
 */
final class VersionNumberScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** version string pattern - <code>/^(v\d+(?:\.\d+)*)/</code> */
    private static final Pattern VSTRING = Pattern.compile("^(v\\d+(?:\\.\\d+)*)");

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerCommit(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerCommit(Tokenizer tokenizer) throws TokenizingException
    {
        Token prev = tokenizer.getLastSignificantToken();

        // if the previous token is a subroutine or package, we're not a version number
        if (!(ElementUtils.isSubWordToken(prev) || ElementUtils.isPackageWordToken(prev)))
        {
            Matcher matcher = VSTRING.matcher(tokenizer.getRestOfCurrentLine());
            if (matcher.find())
            {
                tokenizer.createToken(VersionNumberToken.class);
                tokenizer.appendToCurrentToken(matcher.group(1), true);

                tokenizer.finalizeToken();
                return tokenizer.getScanner().tokenizerOnChar(tokenizer);
            }
        }

        // not a v-string, we're a word...
        tokenizer.switchToScanner(WordToken.class);
        return tokenizer.getScanner().tokenizerCommit(tokenizer);
    }

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        String next = tokenizer.getNextCharacter();

        // allow underscores and numbers to pass through
        if ("_".equals(next) || NumberScanner.isNumber(next))
        {
            // token already created...
            return false;
        }

        if (".".equals(next))
        {
            String content = tokenizer.getCurrentContent();

            // we have '..', which is an operator...
            if (content.endsWith("."))
            {
                // chop off the '.'
                tokenizer.getCurrentToken().setContent(content.substring(0, content.length() - 1));
                // create an operator token
                tokenizer.createToken(OperatorToken.class, "..");
                return true;
            }

            return false;
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
