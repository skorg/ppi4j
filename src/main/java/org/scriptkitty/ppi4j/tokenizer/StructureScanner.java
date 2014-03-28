package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.StructureToken;


/**
 * delegate class responsible for handling the text considered to be a structure (<code>{ ; }</code>)
 *
 * @see org.scriptkitty.ppi4j.token.StructureToken
 */
final class StructureScanner extends TokenScanner
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerCommit(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerCommit(Tokenizer tokenizer)
    {
        tokenizer.createToken(StructureToken.class, tokenizer.getNextCharacter());
        tokenizer.finalizeToken();

        return true;
    }

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
