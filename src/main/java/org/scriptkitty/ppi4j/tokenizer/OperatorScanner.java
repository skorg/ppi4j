package org.scriptkitty.ppi4j.tokenizer;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.HereDocToken;
import org.scriptkitty.ppi4j.token.number.FloatNumberToken;
import org.scriptkitty.ppi4j.token.quotelike.QLReadlineToken;


/**
 * delegate class responsible for handling the text considered to be an operator
 *
 * <p>The list of valid operators are:</p>
 *
 * <pre>
 *  ++   --   **   !    ~    +    -
 *  =~   !~   *    /    %    x
 *  <<   >>   lt   gt   le   ge   cmp
 *  ==   !=   <=>  .    ..   ...  ,
 *  &    |    ^    &&   ||   //
 *  ?    :    =    +=   -=   *=   .=   //=
 *  <    >    <=   >=   <>   =>   ->
 *  and  or   dor  not  eq   ne
 * </pre>
 */
final class OperatorScanner extends TokenScanner
{
    //~ Static fields/initializers

    /** heredoc pattern - <code>/^(?: (?!\d)\w | \s*['"`] | \\\w)/</code> */
    private static Pattern HEREDOC = Pattern.compile("^(?: (?!\\d)\\w | \\s*['\"`] | \\\\\\w)", Pattern.COMMENTS);

    /** set of perl operators */
    static final Set<String> OPERATORS = new HashSet<String>()
    {
        private static final long serialVersionUID = 363785937428807655L;

        {
            add("->");
            add("++");
            add("--");
            add("**");
            add("!");
            add("~");
            add("+");
            add("-");
            add("=~");
            add("!~");
            add("*");
            add("/");
            add("%");
            add("x");
            add(".");
            add("<<");
            add(">>");
            add("<");
            add(">");
            add("<=");
            add(">=");
            add("lt");
            add("gt");
            add("le");
            add("ge");
            add("==");
            add("!=");
            add("<=>");
            add("eq");
            add("ne");
            add("cmp");
            add("~~");
            add("&");
            add("|");
            add("^");
            add("&&");
            add("||");
            add("//");
            add("..");
            add("...");
            add("?");
            add(":");
            add("=");
            add("+=");
            add("-=");
            add("*=");
            add(".=");
            add("/=");
            add("//=");
            add("=>");
            add("<>");
            add("and");
            add("or");
            add("xor");
            add("not");
            add(",");
        }
    };

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        String next = tokenizer.getNextCharacter();
        String content = tokenizer.getCurrentToken().getContent();

        if (OPERATORS.contains(content + next))
        {
            // we're not consuming 'next', so return false...
            return false;
        }

        // handle special case of decimal, ie: .1234
        if (content.equals(".") && next.matches("^[0-9]$"))
        {
            tokenizer.switchToToken(FloatNumberToken.class);
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        // heredoc
        if (content.equals("<<"))
        {
            Matcher matcher = HEREDOC.matcher(tokenizer.getRestOfCurrentLine());
            if (matcher.find())
            {
                tokenizer.switchToToken(HereDocToken.class);
                return tokenizer.getScanner().tokenizerOnChar(tokenizer);
            }
        }

        // 'null' readline
        if (content.equals("<>"))
        {
            tokenizer.switchToToken(QLReadlineToken.class);
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
