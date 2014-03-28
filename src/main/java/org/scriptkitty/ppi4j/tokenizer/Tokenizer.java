package org.scriptkitty.ppi4j.tokenizer;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizerException;
import org.scriptkitty.ppi4j.token.BOMToken;
import org.scriptkitty.ppi4j.token.HereDocToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public final class Tokenizer
{
    //~ Instance fields

    private boolean chopEOF = true;

    private Token current;

    private String currentLine;
    private int currentLineCol;

    private TokenScanner delegate;

    private boolean forceEOF;

    private int hdOffset = 0;
    private int lineNumber = 0;

    private int offset;
    private final Pattern SEPARATOR = Pattern.compile("^__(?:DATA|END)__\\s*$");

    private int sIndex;

    private char[] source;
    private int tIndex;

    private List<Token> tokens;
    private final Pattern TRAILING_WS = Pattern.compile("\\s$");

    private TokenScanner zone;

    //~ Constructors

    public Tokenizer(String source)
    {
        this.tokens = new ArrayList<Token>();
        this.source = prepSource(source);

        switchToZone(WhitespaceToken.class);
        switchToScanner(BOMToken.class);
    }

    //~ Methods

    public List<Token> getTokens() throws TokenizerException
    {
        while (true)
        {
            if (!parseNextLine())
            {
                break;
            }
        }

        finalizeEOF();

        return tokens;
    }

    public Token next() throws TokenizerException
    {
        if (!hasNext() && (tIndex > tokens.size()))
        {
            return Token.EOF;
        }

        if (tIndex < tokens.size())
        {
            // the index will be incremeted after the token is fetched
            return tokens.get(tIndex++);
        }

        while (true)
        {
            if (!parseNextLine())
            {
                finalizeEOF();

                if (tIndex < tokens.size())
                {
                    // the index will be incremeted after the token is fetched
                    return tokens.get(tIndex++);
                }

                break;
            }
        }

        if (tIndex < tokens.size())
        {
            // the index will be incremeted after the token is fetched
            return tokens.get(tIndex++);
        }

        return Token.EOF;
    }

    protected void appendToCurrentToken(String content)
    {
        appendToCurrentToken(content, false);
    }

    protected void appendToCurrentToken(String content, boolean incCol)
    {
        current.appendContent(content);

        if (incCol)
        {
            incLineColumn(content.length());
        }
    }

    protected boolean chopEnabled()
    {
        return chopEOF;
    }

    protected void createToken(Class<? extends Token> clazz)
    {
        createToken(clazz, "");
    }

    protected void createToken(Class<? extends Token> clazz, String content)
    {
        createToken(clazz, content, true);
    }

    protected void decrLineColumn()
    {
        decrLineColumn(1);
    }

    protected void decrLineColumn(int decr)
    {
        currentLineCol -= decr;
    }

    protected void finalizeToken()
    {
        // no token, nothing to do
        if (current == null)
        {
            return;
        }

        current.setStartOffset(offset);

        /*
         * once the token is finalized, subtract the content length to find the it's starting column position - if it's
         * < 0, it's 0, the column start
         */
        int column = currentLineCol - current.getLength();
        current.setColumn(((column < 0) ? 0 : column) + 1);

        // update the tracked offset
        offset += current.getLength();

        // track we saw a heredoc token, we will have to adjust the offset when the next line is processed
        if (current instanceof HereDocToken)
        {
            calcOffsetAfterHereDoc((HereDocToken) current);
        }

        // keep a list of the tokens we've seen to assist w/ decision making later
        tokens.add(current);

        // null out the current token
        current = null;

        // reset the current scanner to whatever 'zone' we are in
        delegate = zone;
    }

    protected void forceEOF()
    {
        forceEOF = true;
    }

    protected int getCurrentCharOfCurrentLine()
    {
        return currentLine.codePointAt(currentLineCol);
    }

    protected String getCurrentContent()
    {
        if (current == null)
        {
            throw new RuntimeException("current == null");
        }

        return current.getContent();
    }

    protected String getCurrentLine()
    {
        return currentLine;
    }

    protected int getCurrentLineCol()
    {
        return currentLineCol;
    }

    protected int getCurrentLineLength()
    {
        return currentLine.length();
    }

    protected Token getCurrentToken()
    {
        return current;
    }

    protected Token getLastSignificantToken()
    {
        // safe, the token array is guarenteed to have an entry, even if it's the 'NULL' token
        return getLastSignificantTokens(1)[0];
    }

    protected Token[] getLastSignificantTokens(int count)
    {
        int index = tokens.size();
        List<Token> list = new ArrayList<Token>(count);

        while (index > 0)
        {
            Token token = tokens.get(--index);

            if (token.isSignificant())
            {
                list.add(token);
            }

            if (list.size() == count)
            {
                break;
            }
        }

        for (int i = 0; i < (count - list.size()); i++)
        {
            list.add(WhitespaceToken.NULL);
        }

        return list.toArray(new Token[list.size()]);
    }

    protected Token getLastToken()
    {
        if (tokens.isEmpty())
        {
            return WhitespaceToken.NULL;
        }

        return tokens.get(tokens.size() - 1);
    }

    protected String getNextCharacter()
    {
        return getNextCharacter(0);
    }

    protected String getNextCharacter(int offset)
    {
        // add the offset to the current column to determine where in the string we start
        int start = ((currentLineCol == -1) ? 0 : currentLineCol) + offset;
        return currentLine.substring(start, start + 1);
    }

    protected int getOffset()
    {
        return offset;
    }

    protected String getRestOfCurrentLine()
    {
        return getRestOfCurrentLine(0);
    }

    protected String getRestOfCurrentLine(int offset)
    {
        int start = (currentLineCol == -1) ? 0 : currentLineCol;
        return currentLine.substring(start + offset);
    }

    protected TokenScanner getScanner()
    {
        return delegate;
    }

    protected boolean hasCurrentLine()
    {
        return (currentLine != null);
    }

    protected boolean hasCurrentToken()
    {
        return (current != null);
    }

    protected boolean hasNext()
    {
        return (forceEOF || (sIndex < source.length));
    }

    protected void incLineColumn()
    {
        incLineColumn(1);
    }

    protected void incLineColumn(int inc)
    {
        currentLineCol += inc;
    }

    protected boolean isEndOfCurrentLine()
    {
        return (currentLineCol >= currentLine.length());
    }

    protected String nextLine()
    {
        StringBuffer buffer = new StringBuffer();

        while (hasNext())
        {
            // note the index will be incremented
            char c = source[sIndex++];
            buffer.append(c);

            if ((c == '\r') || (c == '\n'))
            {
                if ((c == '\r') && hasNext() && (source[sIndex] == '\n'))
                {
                    buffer.append(source[sIndex++]);
                }

                lineNumber++;
                break;
            }
        }

        return buffer.toString();
    }

    protected boolean prepNextLine(boolean inScan)
    {
        if (!hasNext())
        {
            if (!inScan)
            {
                currentLineCol = -1;
                currentLine = null;
            }
            else
            {
                // when scanning, set the cursor to the end of the line and the rest should cascade out
                currentLineCol = currentLine.length();
            }

            return false;
        }

        currentLineCol = -1;
        currentLine = nextLine();

        return true;
    }

    protected void switchToScanner(Class<? extends Token> clazz)
    {
        delegate = getSwitchTo(clazz);
    }

    protected void switchToToken(Class<? extends Token> clazz)
    {
        switchToToken(clazz, current.getContent());
    }

    protected void switchToToken(Class<? extends Token> clazz, String content)
    {
        /*
         * meh - i don't really like how this is going to cause some object churn, but b/c the tokens can change right
         * up to the point of being finalized. perl can cheat and just rebless the class to have it change type - we
         * need to create create a new instance entirely
         */
        createToken(clazz, content, false);
    }

    protected void switchToZone(Class<? extends Token> clazz)
    {
        zone = getSwitchTo(clazz);
    }

    private void calcOffsetAfterHereDoc(HereDocToken token)
    {
        hdOffset = token.getHereDocLength();

        if (token.isTerminated())
        {
            hdOffset += token.getTerminatorLine().length();
        }
    }

    private void createToken(Class<? extends Token> clazz, String content, boolean finalize)
    {
        if ((current != null) && (current.getClass() == clazz))
        {
            return;
        }

        try
        {
            // finalize any existing token
            if (finalize)
            {
                finalizeToken();
            }

            Token token = clazz.newInstance();

            token.setLineNumber(lineNumber);

            if (content != null)
            {
                token.setContent(content);
            }

            this.current = token;

            switchToScanner(token.getClass());
        }
        catch (InstantiationException e)
        {
            // should never happen...
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            // should never happen...
            throw new RuntimeException(e);
        }
    }

    private void finalizeEOF()
    {
        // finish any partially completed tokens
        finalizeToken();

        // no more tokens, nothing to do...
        if (tokens.isEmpty())
        {
            return;
        }

        // find the last token and if it is 'NULL', remove it.
        if (getLastToken().equals(WhitespaceToken.NULL))
        {
            tokens.remove(tokens.size() - 1);
        }

        Token last = getLastToken();
        String content = last.getContent();

        if (chopEOF && content.endsWith(" "))
        {
            content = content.substring(0, content.length() - 1);
            if ("".equals(content))
            {
                tokens.remove(tokens.size() - 1);
            }
            else
            {
                last.setContent(content);
            }
        }
    }

    private TokenScanner getSwitchTo(Class<? extends Token> clazz)
    {
        if (TokenScanner.SCANNERS.containsKey(clazz))
        {
            return TokenScanner.SCANNERS.get(clazz);
        }

        return TokenScanner.SCANNERS.get(Token.class);
    }

    private boolean parseNextLine() throws TokenizerException
    {
        offset += hdOffset;
        hdOffset = 0;

        if (!prepNextLine())
        {
            // EOF, finalize the last token we saw...
            finalizeToken();
            return false;
        }

        if (!delegate.tokenizerOnLineStart(this, currentLine))
        {
            while (true)
            {
                if (!processNextChar())
                {
                    break;
                }
            }
        }

        // trigger any actions that occur when the line has ended
        delegate.tokenizerOnLineEnd(this, currentLine);

        return true;
    }

    private boolean prepNextLine()
    {
        return prepNextLine(false);
    }

    private char[] prepSource(String source)
    {
        if (SEPARATOR.matcher(source).find() || (source.length() == 0) || TRAILING_WS.matcher(source).find())
        {
            chopEOF = false;
        }
        else
        {
            source += " ";
        }

        return source.toCharArray();
    }

    private boolean processNextChar() throws TokenizerException
    {
        currentLineCol++;
        if (isEndOfCurrentLine())
        {
            return false;
        }

        try
        {

            if (!delegate.tokenizerOnChar(this))
            {
                // TODO: this can probably come out once all the tokens have been mapped
                if (current == null)
                {
                    throw new RuntimeException("processNextChar() : current == null");
                }
                /*
                 * PPI can return the name of the token it needs to create (in addtion to 1/0/undef) in the event the
                 * current token is not defined, however we can't (well, i'm not creating a value object for it), so it
                 * will be the responsibility of the scanning delegate to create it for us.
                 */
                current.appendContent(getNextCharacter());
            }
        }
        catch (Exception e)
        {
            finalizeToken();

            Token token = tokens.remove(tokens.size() - 1);

            throw new TokenizerException(token.getLineNumber(), token.getColumn(), e);
        }

        return true;
    }
}
