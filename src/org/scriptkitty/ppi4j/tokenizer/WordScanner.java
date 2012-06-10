package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.AttributeToken;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.DataToken;
import org.scriptkitty.ppi4j.token.EndToken;
import org.scriptkitty.ppi4j.token.LabelToken;
import org.scriptkitty.ppi4j.token.MagicToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.SeparatorToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.quote.InterpolateQuoteToken;
import org.scriptkitty.ppi4j.token.quote.LiteralQuoteToken;
import org.scriptkitty.ppi4j.token.quotelike.QLCommandToken;
import org.scriptkitty.ppi4j.token.quotelike.QLRegExpToken;
import org.scriptkitty.ppi4j.token.quotelike.QLWordsToken;
import org.scriptkitty.ppi4j.token.regexp.REMatchToken;
import org.scriptkitty.ppi4j.token.regexp.RESubstituteToken;
import org.scriptkitty.ppi4j.token.regexp.RETransliterateToken;
import org.scriptkitty.ppi4j.util.ElementUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


final class WordScanner extends TokenScanner
{
    //~ Static fields/initializers

    private static final String DATA_SEPARATOR = "__DATA__";

    private static final String END_SEPARATOR = "__END__";

    private static final Pattern LABEL = Pattern.compile("^(\\s*:)(?!:)");

    private static final Pattern WORD = Pattern.compile("^((?!\\d)\\w+(?:(?:'|::)\\w+)*(?:::)?)");

    private static final Pattern WORD_S = Pattern.compile("^(\\w+)'");

    private static final Pattern WS_CLOSE_CURLY = Pattern.compile("^\\s*\\}");

    private static final Pattern WS_EQ_ARROW = Pattern.compile("^\\s*=>");

    private static final Pattern NEWLINE = Pattern.compile("(.*)?(\r*\n|\r)");

    private static final Set<String> BACKOFF = new HashSet<String>()
    {
        private static final long serialVersionUID = 1392765528532340141L;

        {
            //J- do not format...
            add("eq"); add("ne"); add("ge"); add("le"); add("gt"); add("lt");
            add("q"); add("qq"); add("qw"); add("qr"); add("m"); add("s");
            add("tr"); add("y"); add("pack"); add("unpack");
            //J+
        }
    };

    private static final Map<String, Class<? extends Token>> QUOTELIKE = new HashMap<String, Class<? extends Token>>()
    {
        private static final long serialVersionUID = 4763629395850401177L;

        {
            put("q", LiteralQuoteToken.class);
            put("qq", InterpolateQuoteToken.class);
            put("qr", QLRegExpToken.class);
            put("qw", QLWordsToken.class);
            put("qx", QLCommandToken.class);
            put("m", REMatchToken.class);
            put("s", RESubstituteToken.class);
            put("y", RETransliterateToken.class);
            put("tr", RETransliterateToken.class);
        }
    };

    //~ Methods

    @Override protected boolean tokenizerCommit(Tokenizer tokenizer) throws TokenizingException
    {
        String line = tokenizer.getRestOfCurrentLine();

        String word = matchWord(line);
        tokenizer.incLineColumn(word.length());

        if (isSubAttribute(tokenizer))
        {
            tokenizer.createToken(AttributeToken.class, word);
            if (tokenizer.isEndOfCurrentLine())
            {
                return true;
            }

            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        if (END_SEPARATOR.equals(word))
        {
            processSeparator(tokenizer, word, EndToken.class);
            return true;
        }

        if (DATA_SEPARATOR.equals(word))
        {
            processSeparator(tokenizer, word, DataToken.class);
            return true;
        }

        if (QUOTELIKE.containsKey(word))
        {
            tokenizer.createToken(QUOTELIKE.get(word), word);
        }
        else
        {
            createWordToken(tokenizer, word);
            tokenizer.finalizeToken();
        }

        if (tokenizer.isEndOfCurrentLine())
        {
            return true;
        }

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        String line = tokenizer.getRestOfCurrentLine();

        String word = matchWord(line);
        tokenizer.appendToCurrentToken(word, true);

        // might be a subroutine attribute...
        if (isSubAttribute(tokenizer))
        {
            tokenizer.switchToToken(AttributeToken.class);
            return tokenizer.getScanner().tokenizerCommit(tokenizer);
        }

        String content = tokenizer.getCurrentToken().getContent();

        // check for quote-like operator...
        if (QUOTELIKE.containsKey(content) && !isLiteral(tokenizer, content))
        {
            tokenizer.switchToToken(QUOTELIKE.get(content));
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        // or one of the word operators...
        if (OperatorScanner.OPERATORS.contains(content) && !isLiteral(tokenizer, content))
        {
            tokenizer.switchToToken(OperatorToken.class);
            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        // unless we're a simple identifier, we must be a normal bareword
        if (content.contains(":"))
        {
            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        if (!tokenizer.isEndOfCurrentLine())
        {
            // if the next char is a ':', we're a label
            String next = tokenizer.getNextCharacter();
            if (":".equals(next))
            {
                tokenizer.appendToCurrentToken(":", true);
                tokenizer.switchToToken(LabelToken.class);
            }
            // if not a label, '_' on its own is the magic file handle
            else if ("_".equals(content))
            {
                tokenizer.switchToToken(MagicToken.class);
            }
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private void createWordToken(Tokenizer tokenizer, String word)
    {
        // not a simple identifier or it's a literal word
        if (word.contains(":") || isLiteral(tokenizer, word))
        {
            tokenizer.createToken(WordToken.class, word);
            return;
        }

        // word operator
        if (OperatorScanner.OPERATORS.contains(word))
        {
            tokenizer.createToken(OperatorToken.class, word);
            return;
        }

        // if the next char is a ':', then we're a label
        Matcher matcher = LABEL.matcher(tokenizer.getRestOfCurrentLine());
        if (matcher.find())
        {
            /*
             * unless it's after 'sub', in which case it's a sub name and an attribute operator
             *
             * this could have been checked at the top level, but it would impose an additional per-word performance
             * penalty and all other cases where the attribute operator doesn't directly touch the object name already
             * work
             */
            if (ElementUtils.isSubWordToken(tokenizer.getLastSignificantToken()))
            {
                tokenizer.createToken(WordToken.class, word);
                return;
            }

            tokenizer.createToken(LabelToken.class, word);
            tokenizer.appendToCurrentToken(matcher.group(1), true);
            return;
        }

        if ("_".equals(word))
        {
            tokenizer.createToken(MagicToken.class, word);
            return;
        }

        tokenizer.createToken(WordToken.class, word);
    }

    private boolean isLiteral(Tokenizer tokenizer, String content)
    {
        Token prev = tokenizer.getLastSignificantToken();
        String rest = tokenizer.getRestOfCurrentLine();

        // forced if method name or subroutine name...
        if (ElementUtils.isDashArrowOperatorToken(prev) || ElementUtils.isSubWordToken(prev))
        {
            return true;
        }

        // if sandwiched btwn { }, probably a bareword hash key
        if ("{".equals(content) && WS_CLOSE_CURLY.matcher(rest).find())
        {
            return true;
        }

        // if the word is followed by a '=>', it's probably a word and not a regexp
        if (WS_EQ_ARROW.matcher(rest).find())
        {
            return true;
        }

        return false;
    }

    private boolean isSubAttribute(Tokenizer tokenizer)
    {
        return tokenizer.getLastSignificantToken().hasAttribute(Token.Attribute.HINT);
    }

    private String matchWord(String line)
    {
        Matcher matcher = WORD.matcher(line);
        if (!matcher.find())
        {
            return "";
        }

        String word = matcher.group(1);

        /*
         * special case: eq'foo could be treated like the word "eq'foo", so just unwind and make it "eq" or one of the
         * other entries in the BACKOFF set.
         */
        matcher = WORD_S.matcher(word);
        if (matcher.find() && BACKOFF.contains(matcher.group(1)))
        {
            word = matcher.group(1);
        }

        return word;
    }

    private void processSeparator(Tokenizer tokenizer, String word, Class<? extends Token> clazz)
    {
        // create and finalize the separator token (__END__ or __DATA__)
        tokenizer.createToken(SeparatorToken.class, word);
        tokenizer.finalizeToken();

        // switch the parsing zone...
        tokenizer.switchToZone(clazz);

        if (tokenizer.isEndOfCurrentLine())
        {
            return;
        }

        /*
         * anything immediately following the separator is converted into a comment, followed by a whitespace (newline)
         * if it exists.
         */
        String line = tokenizer.getRestOfCurrentLine();
        tokenizer.incLineColumn(line.length());

        Matcher matcher = NEWLINE.matcher(line);

        if (matcher.matches())
        {
            String comment = matcher.group(1);

            if ((comment != null) && !comment.equals(""))
            {
                tokenizer.createToken(CommentToken.class, comment);
            }

            tokenizer.createToken(WhitespaceToken.class, matcher.group(2));
        }
        else
        {
            tokenizer.createToken(CommentToken.class, line);
        }

        tokenizer.finalizeToken();
    }
}
