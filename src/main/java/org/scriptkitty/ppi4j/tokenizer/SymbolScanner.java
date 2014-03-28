package org.scriptkitty.ppi4j.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.MagicToken;


final class SymbolScanner extends TokenScanner
{
    //~ Static fields/initializers

    private static final Pattern END_OF_SYMBOL = Pattern.compile("^([\\w:\']+)");

    private static final Pattern MAGIC = Pattern.compile("^(?:\\$|\\@)\\d+");

    private static final Pattern OVERSUCK = Pattern.compile(
        "^([\\$@%&*] (?: : (?!:) | (?: \\w+ | \' (?!\\d) \\w+ | \\:: \\w+ ) (?: (?: \' (?!\\d) \\w+ | \\:: \\w+ ))* (?: :: )? ))",
        Pattern.COMMENTS);

    private static final Pattern SYMBOL = Pattern.compile("[\\$%*@&]::(?:[^\\w]|$)");

    //~ Methods

    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        // pull everything in, we'll handle 'extra' stuff at the end...
        Matcher matcher = END_OF_SYMBOL.matcher(tokenizer.getRestOfCurrentLine());
        if (matcher.find())
        {
            tokenizer.appendToCurrentToken(matcher.group(1), true);
        }

        String content = tokenizer.getCurrentToken().getContent();

        matcher = MAGIC.matcher(content);

        // magic
        if ("@_".equals(content) || "$_".equals(content) || matcher.find())
        {
            tokenizer.switchToToken(MagicToken.class);
            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);

        }

        // shortcut for most of the X:: symbols...
        if ("$::".equals(content) && !tokenizer.isEndOfCurrentLine())
        {
            String next = tokenizer.getNextCharacter();
            if ("|".equals(next))
            {
                tokenizer.appendToCurrentToken(next, true);
                tokenizer.switchToToken(MagicToken.class);

                tokenizer.finalizeToken();
                return tokenizer.getScanner().tokenizerOnChar(tokenizer);
            }
        }

        matcher.reset();
        matcher.usePattern(SYMBOL);

        if (content.matches("[\\$%*@&]::(?:[^\\w]|$)"))
        {
            String current = content.substring(0, 3);

            tokenizer.getCurrentToken().setContent(current);
            tokenizer.decrLineColumn(content.length() - current.length());

            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);

        }

        matcher.reset();
        matcher.usePattern(OVERSUCK);

        if (!matcher.find())
        {
            throw new TokenizingException("symbol trim regexp did not match");
        }

        String matched = matcher.group(1);

        if (matched.length() != content.length())
        {
            tokenizer.getCurrentToken().setContent(matched);
            tokenizer.incLineColumn(matched.length() - content.length());
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
