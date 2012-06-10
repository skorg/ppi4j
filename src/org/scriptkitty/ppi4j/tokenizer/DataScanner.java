package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.token.DataToken;


/**
 * delegate class responsible for handling the text that comes after a <code>__DATA__</code> statement
 *
 * @see org.scriptkitty.ppi4j.token.DataToken
 */
final class DataScanner extends TokenScanner
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnLineStart(org.scriptkitty.ppi4j.tokenizer.Tokenizer,
     * java.lang.String)
     */
    @Override protected boolean tokenizerOnLineStart(Tokenizer tokenizer, String line)
    {
        /*
         * PPI::Tokenizer::_process_next_char would create this token in the 'if ( $result eq '1' )' section after
         * returning from __TOKENIZER__on_char, but that only works b/c perl allows for multiple return types.
         *
         * we can achieve the same thing here by just swallowing up any lines that occur until EOF
         */
        if (tokenizer.hasCurrentToken())
        {
            tokenizer.appendToCurrentToken(line);
        }
        else
        {
            tokenizer.createToken(DataToken.class, line);
        }

        return true;
    }
}
