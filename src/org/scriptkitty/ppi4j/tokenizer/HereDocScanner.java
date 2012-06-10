package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.perl.lang.HereDoc;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizingException;
import org.scriptkitty.ppi4j.token.HereDocToken;
import org.scriptkitty.ppi4j.token.HereDocToken.Mode;
import org.scriptkitty.ppi4j.token.OperatorToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * delegate class responsible for handling the text considered to be heredoc (<code><<EOF</code>)
 *
 * @see org.scriptkitty.ppi4j.token.HereDocToken
 */
final class HereDocScanner extends TokenScanner
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.tokenizer.TokenScanner#tokenizerOnChar(org.scriptkitty.ppi4j.tokenizer.Tokenizer)
     */
    @Override protected boolean tokenizerOnChar(Tokenizer tokenizer) throws TokenizingException
    {
        /*
         * TODO: handle null terminated heredoc
         *
         * from perlop:
         *
         * The terminating string may be either an identifier (a word), or some quoted text. An unquoted identifier works
         * like double quotes. There may not be a space between the << and the identifier, unless the identifier is
         * explicitly quoted. (If you put a space it will be treated as a null identifier, which is valid, and matches
         * the first empty line.) The terminating string must appear by itself (unquoted and with no surrounding
         * whitespace) on the terminating line.
         *
         */
        String rest = tokenizer.getRestOfCurrentLine();
        String content = HereDoc.getIdentifier(rest);

        // we're a << operator
        if (content == null)
        {
            tokenizer.switchToToken(OperatorToken.class);
            tokenizer.finalizeToken();
            return tokenizer.getScanner().tokenizerOnChar(tokenizer);
        }

        tokenizer.appendToCurrentToken(content, true);

        HereDocToken token = (HereDocToken) tokenizer.getCurrentToken();

        String terminator = parseTerminator(content, token);
        token.setTerminator(terminator);

        Pattern pattern = Pattern.compile("^" + terminator + "(\r*\n|\r)$");

        // track the heredoc length and the it's start offset
        int offset = tokenizer.getOffset() + rest.length() + token.getLength() - content.length();

        String line = tokenizer.nextLine();
        List<String> heredoc = new ArrayList<String>();

        while (!"".equals(line))
        {
            // keep the terminator line for consistency when serializing but don't consume it as any token
            if (pattern.matcher(line).matches() || terminator.equals(line))
            {
                token.setTerminatorLineAndOffset(line, offset);
                break;
            }

            heredoc.add(line);
            line = tokenizer.nextLine();
        }

        if (!heredoc.isEmpty())
        {
            if (tokenizer.chopEnabled())
            {
                // trim the extra ' ' that was added by the tokenizer
                handleEOF(heredoc);
            }

            token.setHereDocAndOffset(heredoc, offset);
        }

        /*
         * set a hint so it can be inexpensively repaired if it is serialized back out
         *
         * TODO: consider implementing serialization
         */
        tokenizer.getCurrentToken().setAttribute(Token.Attribute.DAMAGED);

        tokenizer.finalizeToken();
        return tokenizer.getScanner().tokenizerOnChar(tokenizer);
    }

    private void handleEOF(List<String> heredoc)
    {
        int index = heredoc.size() - 1;
        String content = heredoc.get(index);

        if (content.endsWith(" "))
        {
            heredoc.set(index, content.substring(0, content.length() - 1));
        }
    }

    private String parseTerminator(String content, final HereDocToken token) throws TokenizingException
    {
        String terminator = HereDoc.parseTerminator(content, new HereDoc.TerminatorMode()
            {
                @Override public void setCommand()
                {
                    token.setMode(Mode.COMMAND);
                }

                @Override public void setInterpolate()
                {
                    token.setMode(Mode.INTERPOLATE);
                }

                @Override public void setLiteral()
                {
                    token.setMode(Mode.LITERAL);
                }
            });

        if (terminator == null)
        {
            throw new TokenizingException("unable to parse hererdoc terminator [" + content + "]");
        }

        return terminator;
    }
}
