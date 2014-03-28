package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.AttributeToken;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.MagicToken;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.PrototypeToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.number.FloatNumberToken;
import org.scriptkitty.ppi4j.util.ElementUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * delegate class responsible for handling text whose token type has yet to be determined
 */
final class UnknownScanner extends TokenScanner
{
    //~ Static fields/initializers

    static final Pattern CONTROL_CHAR = Pattern.compile("^(\\^[\\p{Upper}_]\\w+\\})");

    //~ Methods

    /*
     * @see
     * org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        String next = tokenizer.getNextCharacter();
        String content = tokenizer.getCurrentToken().getContent();

        boolean consumed = false;

        if ("*".equals(content))
        {
            consumed = handleAstrix(tokenizer, next);
        }
        else if ("$".equals(content))
        {
            consumed = handleDollar(tokenizer, content, next);
        }
        else if ("@".equals(content))
        {
            consumed = handleAt(tokenizer, content, next);
        }
        else if ("%".equals(content))
        {
            consumed = handlePercent(tokenizer, content, next);
        }
        else if ("&".equals(content))
        {
            consumed = handleAmpersand(tokenizer, next);
        }
        else if ("-".equals(content))
        {
            consumed = handleDash(tokenizer, next);
        }
        else if (":".equals(content))
        {
            consumed = handleColon(tokenizer, next);
        }
        else
        {
            throw new TokenizingException("encountered unknown content [" + content + "]");
        }

        return consumed;
    }

    private boolean handleAmpersand(Tokenizer tokenizer, String next) throws TokenizingException
    {
        // symbol
        if (matchesSymbol(next))
        {
            tokenizer.switchToToken(SymbolToken.class);
            return false;
        }

        // bitwise-and number, ie: &2
        if (next.matches("\\d"))
        {
            tokenizer.switchToToken(OperatorToken.class);
            tokenizer.finalizeToken();
        }
        // the ampersand is a cast
        else if (matchesCast(next))
        {
            tokenizer.switchToToken(CastToken.class);
        }
        // probably the binary 'and' operator
        else
        {
            tokenizer.switchToToken(OperatorToken.class);
        }

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private boolean handleAstrix(Tokenizer tokenizer, String next) throws TokenizingException
    {
        // symbol...
        if (next.matches("(?:(?!\\d)\\w|\\:)"))
        {
            // unless the previous token is a number, which would make us an operator
            Token prev = tokenizer.getLastSignificantToken();
            if (!(prev instanceof NumberToken))
            {
                tokenizer.switchToToken(SymbolToken.class);
                return false;
            }
        }
        else if ("{".equals(next))
        {
            // control-char symbol, ie: *{^_Foo}
            if (restMatchesControlChar(tokenizer))
            {
                tokenizer.switchToToken(MagicToken.class);
                return false;
            }

            // glob cast
            tokenizer.switchToToken(CastToken.class);
            tokenizer.finalizeToken();

            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }
        else if ("$".equals(next))
        {
            // operator/operand sensitive, multiple or glob cast
            Token prev = tokenizer.getLastSignificantToken();

            // symbol or number, switch to operator
            if ((prev instanceof SymbolToken) || (prev instanceof NumberToken) || ElementUtils.isOpenParenToken(prev) ||
                    ElementUtils.isOpenSquareToken(prev))
            {
                tokenizer.switchToToken(OperatorToken.class);
            }
            else
            {
                /*
                 * according to PPI, there are other tests that could be added here before falling back to the default
                 * 'Cast' token.
                 *
                 * it should also be noted that 'getLastSignificantToken' never returns null, so an extra level of if/else
                 * was removed (end result is to become a Cast token anyway)
                 */
                tokenizer.switchToToken(CastToken.class);
            }

            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }
        // power operator (**) or mutiply-equals (*=)
        else if ("*".equals(next) || "=".equals(next))
        {
            tokenizer.switchToToken(OperatorToken.class);
            return false;
        }

        // at this point we're an operator...
        tokenizer.switchToToken(OperatorToken.class);
        tokenizer.finalizeToken();

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private boolean handleAt(Tokenizer tokenizer, String content, String next) throws TokenizingException
    {
        // symbol
        if (matchesSymbol(next))
        {
            tokenizer.switchToToken(SymbolToken.class);
            return false;
        }

        // magic
        if (isMagic(content, next))
        {
            tokenizer.switchToToken(MagicToken.class);
            return false;
        }

        // control-character symbol, ie: @{^Foo}
        if ("{".equals(next) && restMatchesControlChar(tokenizer))
        {
            tokenizer.switchToToken(MagicToken.class);
            return false;
        }

        // must be a cast...
        tokenizer.switchToToken(CastToken.class);
        tokenizer.finalizeToken();

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private boolean handleColon(Tokenizer tokenizer, String next) throws TokenizingException
    {
        // ::foo style bareword
        if (":".equals(next))
        {
            tokenizer.switchToToken(WordToken.class);
            return false;
        }

        // is the ':' indicating we are an attribute, if yes, set the hint indicator for later use
        if (haveSeenAttrIndicator(tokenizer))
        {
            tokenizer.switchToToken(OperatorToken.class);
            tokenizer.getCurrentToken().setAttribute(Token.Attribute.HINT);
            tokenizer.finalizeToken();
        }
        else
        {
            // we might be a label, but most likely the ternary operator (?:)
            tokenizer.switchToToken(OperatorToken.class);
        }

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private boolean handleDash(Tokenizer tokenizer, String next) throws TokenizingException
    {
        if (matchesNumber(next))
        {
            tokenizer.switchToToken(NumberToken.class);
            return false;
        }

        if (next.equals("."))
        {
            tokenizer.switchToToken(FloatNumberToken.class);
            return false;
        }

        /*
         * dashed word, ie: -foo
         *
         * PPI has a 'Token::DashedWord' but according to the docs, it is not used and any tokens meeting this criteria
         * are treated as a 'Token::Word' instead (w/ the exception of file test operators, ie: -e), so ppi4j will just
         * handle the logic here.
         */
        if (next.matches("[a-zA-Z]"))
        {
            Matcher matcher = Pattern.compile("(\\w+)").matcher(tokenizer.getRestOfCurrentLine());
            if (matcher.find())
            {
                tokenizer.appendToCurrentToken(matcher.group(1), true);
            }

            // check for file test operators
            if (tokenizer.getCurrentToken().getContent().matches("^\\-[rwxoRWXOezsfdlpSbctugkTBMAC]$"))
            {
                tokenizer.switchToToken(OperatorToken.class);
            }
            else
            {
                tokenizer.switchToToken(WordToken.class);
            }

            tokenizer.finalizeToken();
        }
        else
        {
            // numeric negative operator, ie: --
            tokenizer.switchToToken(OperatorToken.class);
        }

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private boolean handleDollar(Tokenizer tokenizer, String content, String next) throws TokenizingException
    {
        // symbol
        if (next.matches("[a-zA-Z_]"))
        {
            tokenizer.switchToToken(SymbolToken.class);
            return false;
        }

        // magic
        if (isMagic(content, next))
        {
            tokenizer.switchToToken(MagicToken.class);
            return false;
        }

        // control-character symbol, ie: ${^MATCH}
        if ("{".equals(next) && restMatchesControlChar(tokenizer))
        {
            tokenizer.switchToToken(MagicToken.class);
            return false;
        }

        // must be a cast...
        tokenizer.switchToToken(CastToken.class);
        tokenizer.finalizeToken();

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private boolean handlePercent(Tokenizer tokenizer, String content, String next) throws TokenizingException
    {
        // number
        if (matchesNumber(next))
        {
            tokenizer.switchToToken(OperatorToken.class);
            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        // magic variable
        if ("^".equals(next) || isMagic(content, next))
        {
            tokenizer.switchToToken(MagicToken.class);
            return false;
        }

        // symbol
        if (matchesSymbol(next))
        {
            tokenizer.switchToToken(SymbolToken.class);
            return false;
        }

        // control-character symbol, ie: %{^Foo}
        if ("{".equals(next) && restMatchesControlChar(tokenizer))
        {
            tokenizer.switchToToken(MagicToken.class);
            return false;
        }

        // cast
        if (matchesCast(next))
        {
            tokenizer.switchToToken(CastToken.class);
            tokenizer.finalizeToken();
        }
        // probably the mod operator, ie % 2
        else
        {
            tokenizer.switchToToken(OperatorToken.class);
        }

        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private boolean haveSeenAttrIndicator(Tokenizer tokenizer)
    {
        Token[] previous = tokenizer.getLastSignificantTokens(3);

        // if we just saw an attribute or prototype, we're an attribute
        if ((previous[0] instanceof AttributeToken) || (previous[0] instanceof PrototypeToken))
        {
            return true;
        }

        // now we need a bareword...
        if (previous[0] instanceof WordToken)
        {
            // anonymous subroutine...
            if (ElementUtils.isSubWordToken(previous[0]))
            {
                return true;
            }

            // named subroutine...
            if (ElementUtils.isSubWordToken(previous[1]) &&
                    ((previous[2] instanceof StructureToken) || ElementUtils.isEmptyWhitespace(previous[2])))
            {
                return true;
            }
        }

        return false;
    }

    private boolean isMagic(String content, String next)
    {
        return MagicScanner.MAGIC.contains(content + next);
    }

    private boolean matchesCast(String line)
    {
        return line.matches("[\\$@%{]");
    }

    private boolean matchesNumber(String line)
    {
        return line.matches("\\d");
    }

    private boolean matchesSymbol(String line)
    {
        return line.matches("[\\w:]");
    }

    private boolean restMatchesControlChar(Tokenizer tokenizer)
    {
        return CONTROL_CHAR.matcher(tokenizer.getRestOfCurrentLine(1)).find();
    }
}
