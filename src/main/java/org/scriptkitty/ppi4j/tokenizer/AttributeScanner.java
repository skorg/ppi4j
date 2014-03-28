package org.scriptkitty.ppi4j.tokenizer;

import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.exception.TokenizingException;


/**
 * delegate class responsible for handling the text considered to be an attribute
 *
 * @see org.scriptkitty.ppi4j.token.AttributeToken
 */
final class AttributeScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** attribute end pattern - <code>/^((?:\.|[^()])*?[()])/</code> */
    private static final Pattern ATTR_END = Pattern.compile("^((?:\\.|[^()])*?[()])");

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        boolean eof = false;

        if ("(".equals(tokenizer.getNextCharacter()))
        {
            StringBuffer buffer = new StringBuffer();
            eof = scanFor(tokenizer, buffer, ATTR_END, false, new Depth()
                {
                    @Override public int calculate(String matched)
                    {
                        return (matched.endsWith("(")) ? 1 : -1;
                    }

                    @Override public int initial()
                    {
                        return 0;
                    }
                });

            tokenizer.appendToCurrentToken(buffer.toString());
        }

        tokenizer.finalizeToken();

        // if we saw EOF, we're done, otherwise we keep going :)
        return (eof) ? true : tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
