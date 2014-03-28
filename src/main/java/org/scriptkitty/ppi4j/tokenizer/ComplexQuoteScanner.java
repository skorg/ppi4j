package org.scriptkitty.ppi4j.tokenizer;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.Element.Attribute;
import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.SectionedToken;


/**
 * delegate class responsible for handling the text considered to be a a complex quote
 *
 * @see org.scriptkitty.ppi4j.token.quote.InterpolateQuoteToken
 * @see org.scriptkitty.ppi4j.token.quote.LiteralQuoteToken
 * @see org.scriptkitty.ppi4j.token.quotelike.QLCommandToken
 * @see org.scriptkitty.ppi4j.token.quotelike.QLReadlineToken
 * @see org.scriptkitty.ppi4j.token.quotelike.QLRegExpToken
 * @see org.scriptkitty.ppi4j.token.quotelike.QLWordsToken
 * @see org.scriptkitty.ppi4j.token.regexp.REMatchToken
 * @see org.scriptkitty.ppi4j.token.regexp.RESubstituteToken
 * @see org.scriptkitty.ppi4j.token.regexp.RETransliterateToken
 */
final class ComplexQuoteScanner extends SimpleQuoteScanner
{
    //~ Static fields/initializers

    /** modifier pattern - <code>/[^\W\d_]/</code> */
    private static final Pattern MODIFIER = Pattern.compile("[^\\W\\d_]");

    /** whitespace pattern - <code>/^(\s*(?:\#.*)?)/s</code> */
    private static final Pattern WHITESPACE = Pattern.compile("^(\\s*(?:\\\\#.*)?)", Pattern.DOTALL);

    /** delimiter pattern - <code>m/ \A [^\w\s] \z /smx</code> */
    private static final Pattern DELIMITER = Pattern.compile("\\A[^\\w\\s]\\z", Pattern.DOTALL | Pattern.MULTILINE);

    private static final ComplexQuoteScanner self = new ComplexQuoteScanner();

    /** set of 'complex' operators: <code>q qq qx qw qr m s tr y / &lt; ?</code> */
    private static final Set<String> OPERATORS = new HashSet<String>()
    {
        private static final long serialVersionUID = -5016370887124834087L;

        /*
         * NOTE: '?' (which is kind of deprecated) is not currently supported - not sure of the context differences between it as a regexp
         * operator and ternary operator
         */
        {
            //J-
            add("q"); add("qq"); add("qx"); add("qw"); add("qr"); add("m"); add("s"); add("tr"); add("y");
            add("/"); add("<"); add("?");
            //J+
        }
    };

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.SimpleQuoteScanner#fill(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected void fill(Tokenizer tokenizer) throws TokenizingException
    {
        String content = tokenizer.getCurrentContent();
        boolean threePart = hasThreePart(content);

        String separator = null;

        if (OPERATORS.contains(content))
        {
            // scanForGap will return true if we saw EOF, which is the same as EOL :)
            if (scanForGap(tokenizer) || tokenizer.isEndOfCurrentLine())
            {
                return;
            }

            if ("/".equals(content))
            {
                separator = "/";
            }
            else if ("<".equals(content))
            {
                separator = "<";
            }
            else
            {
                separator = tokenizer.getNextCharacter();
                tokenizer.appendToCurrentToken(separator, true);
            }
        }

        if (isOpenBrace(separator))
        {
            fillBrace(tokenizer, separator, threePart);
        }
        else
        {
            fillNormal(tokenizer, separator, threePart);
        }

        if (hasModifiers(content))
        {
            appendModifiers(tokenizer);
        }
    }

    static TokenScanner getInstance()
    {
        return self;
    }

    private void appendModifiers(Tokenizer tokenizer)
    {
        // the tokenizer is currently sitting on the separator, so increment
        tokenizer.incLineColumn();

        while (!tokenizer.isEndOfCurrentLine())
        {
            String next = tokenizer.getNextCharacter();
            Matcher matcher = MODIFIER.matcher(next);

            if (matcher.find())
            {
                tokenizer.appendToCurrentToken(next, true);
                ((SectionedToken) tokenizer.getCurrentToken()).addModifier(next);
                continue;
            }

            // back the cursor up so 'next' can be reconsumed...
            tokenizer.decrLineColumn();
            break;
        }
    }

    private void createSection(Tokenizer tokenizer, String content, boolean eof, String separator)
    {
        SectionedToken.Section section = new SectionedToken.Section();

        section.type = separator + separator;
        section.position = tokenizer.getCurrentToken().getContent().length();
        // match against braces properly, ie []
        section.complete = content.endsWith(separator.substring(separator.length() - 1));

        if (eof)
        {
            // equivalent of 'chop'
            content = content.substring(0, content.length() - 1);
            section.size = content.length();
        }
        else
        {
            section.size = content.length() - 1;
        }

        ((SectionedToken) tokenizer.getCurrentToken()).addSection(section);
    }

    private void fillBrace(Tokenizer tokenizer, String open, boolean threePart) throws TokenizingException
    {
        String close = getClose(open);

        StringBuffer buffer = new StringBuffer();
        boolean eof = scanForBrace(tokenizer, buffer, open, close);

        createSection(tokenizer, buffer.toString(), eof, open + close);
        tokenizer.appendToCurrentToken(buffer.toString());

        if (eof || !threePart)
        {
            return;
        }

        // move into the next section
        tokenizer.incLineColumn();

        // scanForGap will return true if we saw EOF, which is the same as EOL :)
        if (scanForGap(tokenizer) || tokenizer.isEndOfCurrentLine())
        {
            return;
        }

        open = tokenizer.getNextCharacter();

        if (isOpenBrace(open))
        {
            tokenizer.appendToCurrentToken(open, true);

            buffer.setLength(0);

            close = getClose(open);
            eof = scanForBrace(tokenizer, buffer, open, close);

            if (eof && (buffer.length() == 0))
            {
                return;
            }

            createSection(tokenizer, buffer.toString(), eof, open + close);
            tokenizer.appendToCurrentToken(buffer.toString());
        }
        else if (DELIMITER.matcher(open).matches())
        {
            tokenizer.appendToCurrentToken(open, true);
            fillNormal(tokenizer, open, false);
        }
        else
        {
            // this is an error - PPI allows it, so we will too
            createSection(tokenizer, "", false, "");
            tokenizer.getCurrentToken().setAttribute(Attribute.INVALID);

            // rollback the cursor so the char is handled else where
            tokenizer.decrLineColumn();
        }
    }

    private void fillNormal(Tokenizer tokenizer, String separator, boolean threePart)
    {
        StringBuffer buffer = new StringBuffer();
        boolean eof = scanForUnescapedChar(tokenizer, buffer, separator);

        if (eof && (buffer.length() == 0))
        {
            return;
        }

        createSection(tokenizer, buffer.toString(), eof, separator);
        tokenizer.appendToCurrentToken(buffer.toString());

        if (!threePart)
        {
            return;
        }

        // move into the next section
        tokenizer.incLineColumn();

        // if we're three part, just call again
        fillNormal(tokenizer, separator, false);
    }

    private String getClose(String open)
    {
        if ("(".equals(open))
        {
            return ")";
        }

        if ("<".equals(open))
        {
            return ">";
        }

        if ("[".equals(open))
        {
            return "]";
        }

        if ("{".equals(open))
        {
            return "}";
        }

        throw new RuntimeException();
    }

    private boolean hasModifiers(String content)
    {
        // if we are any of these, we do not have a modifier
        return (!("q".equals(content) || "qq".equals(content) || "qw".equals(content) || "qx".equals(content) || "<".equals(content)));
    }

    private boolean hasThreePart(String content)
    {
        return ("s".equals(content) || "tr".equals(content) || "y".equals(content));
    }

    private boolean isOpenBrace(String content)
    {
        return ("(".equals(content) || "<".equals(content) || "[".equals(content) || "{".equals(content));
    }

    /*
     * cursor is left on character following the gap
     */
    private boolean scanForGap(Tokenizer tokenizer) throws TokenizingException
    {
        while (tokenizer.hasCurrentLine())
        {
            String rest = tokenizer.getRestOfCurrentLine();
            Matcher matcher = WHITESPACE.matcher(rest);

            // the regexp can match 0 chars, so this should always work
            if (!matcher.find())
            {
                throw new TokenizingException(String.format("failed to match [%s] using pattern [%s]", rest,
                        WHITESPACE.pattern()));
            }

            String matched = matcher.group();
            tokenizer.appendToCurrentToken(matched);

            // matched the gap, adjust the cursor
            if (matched.length() != rest.length())
            {
                tokenizer.incLineColumn(matched.length());
                return false;
            }

            // matched entire line, load in next and try again...
            if (!tokenizer.prepNextLine(true))
            {
                return false;
            }

            // increment the line column so we start at position 0
            tokenizer.incLineColumn();
        }

        // if we got here, we saw EOF
        return true;
    }
}
