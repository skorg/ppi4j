package org.scriptkitty.ppi4j.token;

import java.util.Collections;
import java.util.List;

import org.scriptkitty.ppi4j.Token;


/**
 * a <code>HereDocToken</code> represents heredoc.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/HereDoc.pm">CPAN - PPI::Token::HereDoc</a>
 */
public final class HereDocToken extends Token
{
    //~ Enums

    public enum Mode
    {
        COMMAND, INTERPOLATE, LITERAL
    }

    //~ Instance fields

    private int hdOffset = 0;

    private int tOffset = 0;

    private List<String> heredoc;

    private Mode mode;

    private String terminator;

    private String tLine;

    //~ Methods

    /**
     * {@inheritDoc}
     *
     * <p><b>IMPORTANT</b>:even though <code>HereDocToken</code>s do not include the 'heredoc' and 'terminator' as part of their content,
     * the end offset calculation does.</p>
     *
     * @see #getContent()
     */
    @Override public int getEndOffset()
    {
        if (tLine != null)
        {
            return tOffset + tLine.length();
        }

        if (hdOffset > 0)
        {
            return hdOffset + getHereDocLength();
        }

        return super.getEndOffset();
    }

    /**
     * get the contents of the heredoc
     *
     * @return heredoc contents
     */
    public List<String> getHereDoc()
    {
        if (heredoc == null)
        {
            return Collections.emptyList();
        }

        return heredoc;
    }

    /**
     * get the length of the heredoc contents
     *
     * @return heredoc length
     */
    public int getHereDocLength()
    {
        int length = 0;

        for (String line : getHereDoc())
        {
            length += line.length();
        }

        return length;
    }

    /**
     * get the starting offset of the heredoc contents
     *
     * @return offset
     */
    public int getHereDocOffset()
    {
        return hdOffset;
    }

    /**
     * get the number of heredoc lines
     *
     * @return line count
     */
    public int getLineCount()
    {
        return getHereDoc().size();
    }

    /**
     * get the heredoc terminator
     *
     * <p>note: the returned string does not include any newline characters.</p>
     *
     * @return terminator
     *
     * @see    #getTerminatorLine()
     */
    public String getTerminator()
    {
        return terminator;
    }

    /**
     * get the heredoc terminating line
     *
     * <p>note: the returned string includes newline characters.</p>
     *
     * @return terminating line
     *
     * @see    #getTerminator()
     */
    public String getTerminatorLine()
    {
        return tLine;
    }

    /**
     * get the starting offset of the heredoc terminator
     *
     * @return offset
     */
    public int getTerminatorOffset()
    {
        return tOffset + getHereDocLength();
    }

    public boolean isMode(Mode mode)
    {
        return (this.mode == mode);
    }

    /**
     * is the heredoc terminated
     *
     * @return <code>true</code> if terminated, false otherwise
     */
    public boolean isTerminated()
    {
        return (tLine != null);
    }

    /**
     * set the heredoc contents and its starting offset
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param heredoc heredoc
     * @param offset  staring offset
     */
    public void setHereDocAndOffset(List<String> heredoc, int offset)
    {
        this.heredoc = heredoc;
        this.hdOffset = offset;
    }

    /**
     * set the heredoc 'mode'
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param mode mode
     */
    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    /**
     * set the heredoc terminator
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param terminator terminator
     */
    public void setTerminator(String terminator)
    {
        this.terminator = terminator;
    }

    /**
     * set the terminator line and its starting offset
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param line   terminator line, including any newline chars
     * @param offset staring offset
     */
    public void setTerminatorLineAndOffset(String line, int offset)
    {
        this.tLine = line;
        this.tOffset = offset;
    }

    /*
     * @see org.scriptkitty.ppi4j.Token#toString()
     */
    @Override public String toString()
    {
        StringBuffer buffer = new StringBuffer(super.toString());

        buffer.append(" [ '");

        for (String text : getHereDoc())
        {
            text = text.replaceAll("\\r", "\\\\r");
            text = text.replaceAll("\\n", "\\\\n");

            buffer.append(text);
        }

        buffer.append("' ]");

        return buffer.toString();
    }
}
