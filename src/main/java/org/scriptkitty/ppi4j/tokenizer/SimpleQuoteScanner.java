package org.scriptkitty.ppi4j.tokenizer;

import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.exception.TokenizingException;


/**
 * delegate class responsible for handling the text considered to be a a simple quote
 *
 * @see org.scriptkitty.ppi4j.token.quote.DoubleQuoteToken
 * @see org.scriptkitty.ppi4j.token.quotelike.QLBacktickToken
 * @see org.scriptkitty.ppi4j.token.quote.SingleQuoteToken
 */
class SimpleQuoteScanner extends TokenScanner
{
    //~ Static fields/initializers

    private static final SimpleQuoteScanner self = new SimpleQuoteScanner();

    //~ Methods

    @SuppressWarnings("unused")
    protected void fill(Tokenizer tokenizer) throws TokenizingException
    {
        StringBuffer buffer = new StringBuffer();

        scanForUnescapedChar(tokenizer, buffer, tokenizer.getCurrentContent());
        tokenizer.appendToCurrentToken(buffer.toString());
    }

    protected final boolean scanForBrace(Tokenizer tokenizer, StringBuffer buffer, final String open, String close)
    {
        Pattern pattern = compile("(?:" + Pattern.quote(open) + "|" + Pattern.quote(close) + ")");

        boolean eof = scanFor(tokenizer, buffer, pattern, false, new Depth()
            {
                @Override public int calculate(String matched)
                {
                    return (matched.endsWith(open) ? 1 : -1);
                }

                @Override public int initial()
                {
                    return 1;
                }
            });

        tokenizer.decrLineColumn();

        return eof;
    }

    protected final boolean scanForUnescapedChar(Tokenizer tokenizer, StringBuffer buffer, String separator)
    {
        return scanFor(tokenizer, buffer, compile(Pattern.quote(separator)), true, new Depth()
            {
                @Override public int calculate(String matched)
                {
                    return 0;
                }

                @Override public int initial()
                {
                    return 0;
                }
            });
    }

    @Override protected final boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        fill(tokenizer);
        tokenizer.finalizeToken();

        return true;
    }

    static TokenScanner getInstance()
    {
        return self;
    }

    private Pattern compile(String pattern)
    {
        return Pattern.compile("^(.*?(?<!\\\\)(?:\\\\\\\\)*" + pattern + ")");
    }
}
