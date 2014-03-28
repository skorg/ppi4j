package org.scriptkitty.ppi4j.tokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.ArrayIndexToken;
import org.scriptkitty.ppi4j.token.AttributeToken;
import org.scriptkitty.ppi4j.token.BOMToken;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.DataToken;
import org.scriptkitty.ppi4j.token.EndToken;
import org.scriptkitty.ppi4j.token.HereDocToken;
import org.scriptkitty.ppi4j.token.MagicToken;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.PodToken;
import org.scriptkitty.ppi4j.token.PrototypeToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.token.UnknownToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.number.BinaryNumberToken;
import org.scriptkitty.ppi4j.token.number.ExpNumberToken;
import org.scriptkitty.ppi4j.token.number.FloatNumberToken;
import org.scriptkitty.ppi4j.token.number.HexNumberToken;
import org.scriptkitty.ppi4j.token.number.OctalNumberToken;
import org.scriptkitty.ppi4j.token.number.VersionNumberToken;
import org.scriptkitty.ppi4j.token.quote.DoubleQuoteToken;
import org.scriptkitty.ppi4j.token.quote.InterpolateQuoteToken;
import org.scriptkitty.ppi4j.token.quote.LiteralQuoteToken;
import org.scriptkitty.ppi4j.token.quote.SingleQuoteToken;
import org.scriptkitty.ppi4j.token.quotelike.QLBacktickToken;
import org.scriptkitty.ppi4j.token.quotelike.QLCommandToken;
import org.scriptkitty.ppi4j.token.quotelike.QLReadlineToken;
import org.scriptkitty.ppi4j.token.quotelike.QLRegExpToken;
import org.scriptkitty.ppi4j.token.quotelike.QLWordsToken;
import org.scriptkitty.ppi4j.token.regexp.REMatchToken;
import org.scriptkitty.ppi4j.token.regexp.RESubstituteToken;
import org.scriptkitty.ppi4j.token.regexp.RETransliterateToken;


abstract class TokenScanner
{
    //~ Static fields/initializers

    protected static final Map<Class<?>, TokenScanner> SCANNERS = new HashMap<Class<?>, TokenScanner>()
    {
        private static final long serialVersionUID = 9222743349048466636L;

        {
            put(ArrayIndexToken.class, new ArrayIndexScanner());
            put(AttributeToken.class, new AttributeScanner());
            put(BinaryNumberToken.class, new BinaryNumberScanner());
            put(BOMToken.class, new BOMScanner());
            put(CastToken.class, new CastScanner());
            put(CommentToken.class, new CommentScanner());
            put(DataToken.class, new DataScanner());
            put(EndToken.class, new EndScanner());
            put(ExpNumberToken.class, new ExpNumberScanner());
            put(FloatNumberToken.class, new FloatNumberScanner());
            put(HereDocToken.class, new HereDocScanner());
            put(HexNumberToken.class, new HexNumberScanner());
            put(MagicToken.class, new MagicScanner());
            put(NumberToken.class, new NumberScanner());
            put(OctalNumberToken.class, new OctalNumberScanner());
            put(OperatorToken.class, new OperatorScanner());
            put(PodToken.class, new PodScanner());
            put(PrototypeToken.class, new PrototypeScanner());
            put(StructureToken.class, new StructureScanner());
            put(SymbolToken.class, new SymbolScanner());
            put(VersionNumberToken.class, new VersionNumberScanner());
            put(UnknownToken.class, new UnknownScanner());
            put(WhitespaceToken.class, new WhitespaceScanner());
            put(WordToken.class, new WordScanner());

            put(SingleQuoteToken.class, SimpleQuoteScanner.getInstance());
            put(DoubleQuoteToken.class, SimpleQuoteScanner.getInstance());
            put(QLBacktickToken.class, SimpleQuoteScanner.getInstance());

            put(QLWordsToken.class, ComplexQuoteScanner.getInstance());
            put(QLRegExpToken.class, ComplexQuoteScanner.getInstance());
            put(QLCommandToken.class, ComplexQuoteScanner.getInstance());
            put(QLReadlineToken.class, ComplexQuoteScanner.getInstance());

            put(REMatchToken.class, ComplexQuoteScanner.getInstance());
            put(RESubstituteToken.class, ComplexQuoteScanner.getInstance());
            put(RETransliterateToken.class, ComplexQuoteScanner.getInstance());

            put(LiteralQuoteToken.class, ComplexQuoteScanner.getInstance());
            put(InterpolateQuoteToken.class, ComplexQuoteScanner.getInstance());
        }
    };

    //~ Methods

    protected final boolean scanFor(Tokenizer tokenizer, StringBuffer buffer, Pattern pattern, boolean rewind, Depth calc)
    {
        int depth = calc.initial();

        while (tokenizer.hasCurrentLine())
        {
            String rest = tokenizer.getRestOfCurrentLine();
            Matcher matcher = pattern.matcher(rest);

            // if we don't find a match...
            if (!matcher.find())
            {
                // append the rest of the line and prepare the next...
                buffer.append(rest);

                if (!tokenizer.prepNextLine(true))
                {
                    break;
                }

                // increment the line column so we start at position 0
                tokenizer.incLineColumn();
                continue;
            }

            buffer.append(matcher.group());

            // adjust the line column based upon if we rewind upon match
            tokenizer.incLineColumn(matcher.group().length() - (rewind ? 1 : 0));

            // track any nesting levels that may be associated with the token type
            depth += calc.calculate(matcher.group());

            // keep going if we're not 0
            if (depth == 0)
            {
                return false;
            }
        }

        // if we got here, we saw EOF
        return true;
    }

    protected final void tokenizeMatchedPodLine(Tokenizer tokenizer, String line, Matcher matcher)
    {
        tokenizer.createToken(PodToken.class, line);
        if (!matcher.group(1).equals("cut"))
        {
            /*
             * PPI claims this is an error (and correctly so, '=cut' should not start a section of pod) and we should not switch the scanner
             * to 'pod' mode, however creating the pod token does just that.
             *
             * while the code seems to function the same w/o this 'if' block, this may catches some edge case i am not aware of, hence it
             * still being here. :)
             */
            tokenizer.switchToScanner(PodToken.class);
        }
    }

    @SuppressWarnings("unused")
    protected boolean tokenizerCommit(Tokenizer tokenizer) throws TokenizingException
    {
        return false;
    }

    /**
     * @return <code>true</true> if the class has consumed the character, <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        return false;
    }

    protected void tokenizerOnLineEnd(Tokenizer tokenizer, String line)
    {
        // default does nothing...
    }

    /**
     * @return <code>true</true> if the class has consumed the line, <code>false</code> otherwise
     */
    protected boolean tokenizerOnLineStart(Tokenizer tokenizer, String line)
    {
        return false;
    }

    //~ Inner Interfaces

    protected interface Depth
    {
        int calculate(String matched);

        int initial();
    }
}
