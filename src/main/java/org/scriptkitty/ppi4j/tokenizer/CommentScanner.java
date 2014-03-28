package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;


/**
 * delegate class responsible for handling the text considered to be a comment
 *
 * @see org.scriptkitty.ppi4j.token.CommentToken
 */
final class CommentScanner extends TokenScanner
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerCommit(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerCommit(Tokenizer tokenizer)
    {
        String rest = tokenizer.getRestOfCurrentLine();

        // the newline gets its own token
        if (rest.endsWith("\n"))
        {
            // create the comment w/ the newline removed
            tokenizer.createToken(CommentToken.class, rest.replace("\n", ""));
            tokenizer.createToken(WhitespaceToken.class, "\n");
        }
        else
        {
            tokenizer.createToken(CommentToken.class, rest);
        }

        tokenizer.incLineColumn(tokenizer.getCurrentLineLength() - 1);

        return true;
    }

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        if ("\n".equals(tokenizer.getNextCharacter()))
        {
            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        return false;
    }

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnLineEnd(org.scriptkitty.ppi4j.tokenizer.Tokenizer,
     * java.lang.String)
     */
    @Override protected void tokenizerOnLineEnd(Tokenizer tokenizer, String line)
    {
        tokenizer.finalizeToken();
    }
}
