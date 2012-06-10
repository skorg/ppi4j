package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.token.EndToken;

import java.util.regex.Matcher;


/**
 * delegate class responsible for handling the text that comes after an <code>__END__</code> statement
 *
 * @see org.scriptkitty.ppi4j.token.EndToken
 */
final class EndScanner extends TokenScanner
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnLineStart(org.scriptkitty.ppi4j.tokenizer.Tokenizer,
     * java.lang.String)
     */
    @Override protected boolean tokenizerOnLineStart(Tokenizer tokenizer, String line)
    {
        Matcher matcher = PodScanner.POD.matcher(line);

        if (matcher.find())
        {
            tokenizeMatchedPodLine(tokenizer, line, matcher);
        }
        else
        {
            if (tokenizer.hasCurrentToken())
            {
                tokenizer.appendToCurrentToken(line);
            }
            else
            {
                tokenizer.createToken(EndToken.class, line);
            }
        }

        return true;
    }
}
