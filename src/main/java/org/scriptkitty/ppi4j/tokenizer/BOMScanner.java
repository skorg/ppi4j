package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.token.WhitespaceToken;


/**
 * delegate class responsible for handling BOM, unicode byte order marks
 *
 * @see org.scriptkitty.ppi4j.token.BOMToken
 */
final class BOMScanner extends TokenScanner
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnLineStart(org.scriptkitty.ppi4j.tokenizer.Tokenizer, java.lang.String)
     */
    @Override protected boolean tokenizerOnLineStart(Tokenizer tokenizer, String line)
    {
        // TODO: figure out what constitutes a BOM

        tokenizer.switchToScanner(WhitespaceToken.class);
        return tokenizer.getScanner().tokenizerOnLineStart(tokenizer, line);
    }
}
