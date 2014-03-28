package org.scriptkitty.ppi4j.tokenizer;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.ArrayIndexToken;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.MagicToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.SymbolToken;


final class MagicScanner extends TokenScanner
{
    //~ Static fields/initializers

    // TODO: there are regexp patterns here that can be statically compiled...

    private static final Pattern ARRAY_INDEX = Pattern.compile("^(\\$\\#)\\w");

    private static final Pattern QUICK_TEST = Pattern.compile("^\\$.*[\\w:\\$\\{]$");

    static final Set<String> MAGIC = new HashSet<String>()
    {
        private static final long serialVersionUID = -3187395763182662143L;

        {
            //J-
            add("$1"); add("$2"); add("$3"); add("$4"); add("$5"); add("$6"); add("$7"); add("$8"); add("$9");

            add("$_"); add("$&"); add("$`"); add("$'"); add("$+"); add("@+"); add("%+"); add("$*"); add("$.");
            add("$/"); add("$|"); add("$\\"); add("$\""); add("$;"); add("$%"); add("$="); add("$-"); add("@-");
            add("%-"); add("$)"); add("$~"); add("$^"); add("$:"); add("$?"); add("$!"); add("%!"); add("$@");
            add("$$"); add("$<"); add("$>"); add("$("); add("$0"); add("$["); add("$]"); add("@_"); add("@*");

            add("$::|"); add("$}"); add("$,"); add("$#"); add("$#+"); add("$#-");

            add("$^L"); add("$^A"); add("$^E"); add("$^C"); add("$^D"); add("$^F"); add("$^H"); add("$^I");
            add("$^M"); add("$^N"); add("$^O"); add("$^P"); add("$^R"); add("$^S"); add("$^T"); add("$^V");
            add("$^W"); add("$^X"); add("%^H");
            //J+
        }
    };

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        // this represents the what the new content of the current token would be...
        String newContent = tokenizer.getCurrentToken().getContent() + tokenizer.getNextCharacter();

        if (QUICK_TEST.matcher(newContent).matches())
        {
            if (newContent.matches("^(\\$(?:\\_[\\w:]|::))") || newContent.matches("^\\$\'[\\w]"))
            {
                // if we have $'\d - ie: $'1 - we are a magic token plus a digit
                if (newContent.matches("^\\$\'\\d$"))
                {
                    tokenizer.finalizeToken();
                    return tokenizer.getScanner().tokenizerOnChar(tokenizer);
                }

                // otherwise we are a symbol in the style $_foo, $::foo or $'foo
                tokenizer.switchToToken(SymbolToken.class);
                return tokenizer.getScanner().tokenizerOnChar(tokenizer);
            }

            // we're a scalar dereference ($$foo)
            if (newContent.matches("^\\$\\$\\w"))
            {
                tokenizer.switchToToken(CastToken.class, "$");
                tokenizer.finalizeToken();
                tokenizer.createToken(SymbolToken.class, "$");

                return false;
            }

            // *might* be a dereference of one of the control-char symbols
            if ("$${".equals(newContent))
            {
                String rest = tokenizer.getRestOfCurrentLine(1);
                if (UnknownScanner.CONTROL_CHAR.matcher(rest).find())
                {
                    // we're a dereference ($${^foo})
                    tokenizer.switchToToken(CastToken.class, "$");
                    tokenizer.finalizeToken();
                    tokenizer.createToken(MagicToken.class, "$");

                    return false;
                }
            }

            // index dereferencing cast (although matches magic variable: $#)
            if ("$#$".equals(newContent) || "$#{".equals(newContent))
            {
                tokenizer.switchToToken(CastToken.class);
                tokenizer.finalizeToken();

                return tokenizer.getScanner().tokenizerOnChar(tokenizer);
            }

            Matcher matcher = ARRAY_INDEX.matcher(newContent);
            if (matcher.find())
            {
                tokenizer.switchToToken(ArrayIndexToken.class, matcher.group(1));
                return tokenizer.getScanner().tokenizerOnChar(tokenizer);
            }

            // esacped char magic
            if (newContent.matches("^\\$\\^\\w+$"))
            {
                // advance the tokenizer and grab the next character...
                tokenizer.incLineColumn();

                String next = tokenizer.getNextCharacter();

                // maybe like $^M
                if (MAGIC.contains(newContent) && (!"".equals(next) && !next.matches("\\w")))
                {
                    tokenizer.getCurrentToken().setContent(newContent);
                }
                // maybe long magic variable, ie: $^WIDE_SYSTEM_CALLS
                else
                {
                    // the 'next' char wasn't consumed, roll it back...
                    tokenizer.decrLineColumn();
                    return false;
                }
            }

            // $#{ - this is a cast '$#', followed by a block '{'
            if (newContent.matches("^\\$\\#\\{"))
            {
                tokenizer.switchToToken(CastToken.class, "$#");
                tokenizer.finalizeToken();
                tokenizer.createToken(SymbolToken.class, "$");

                return false;
            }
        }
        else if (newContent.matches("^%\\^"))
        {
            if ("%^".equals(newContent))
            {
                return false;
            }

            // escaped magic char, ie: %^H
            if (MAGIC.contains(newContent))
            {
                tokenizer.getCurrentToken().setContent(newContent);
                tokenizer.incLineColumn();
            }
            // backoff, treat '%' as an operator
            else
            {
                tokenizer.switchToToken(OperatorToken.class, "%");
                tokenizer.decrLineColumn(1);
            }
        }
        // $#+ and $#-
        else if (MAGIC.contains(newContent))
        {
            tokenizer.incLineColumn(newContent.length() - tokenizer.getCurrentToken().getContent().length());
            tokenizer.getCurrentToken().setContent(newContent);
        }
        else
        {
            Matcher matcher = UnknownScanner.CONTROL_CHAR.matcher(tokenizer.getRestOfCurrentLine());
            if (matcher.find())
            {
                tokenizer.appendToCurrentToken(matcher.group(1), true);
            }
        }

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }
}
