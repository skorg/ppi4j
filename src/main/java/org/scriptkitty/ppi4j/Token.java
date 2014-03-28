package org.scriptkitty.ppi4j;

import org.scriptkitty.ppi4j.visitor.INodeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * a <code>Token</code> represents is the abstract base class for all tokens. in ppi4j terms, a 'token' is a <code>
 * Element</code> that directly represents bits of source code.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token.pm">CPAN - PPI::Token</a>
 */
public abstract class Token extends Element
{
    //~ Static fields/initializers

    /**
     * represents <code>null</code> as a <code>Token</code> object - this token will never appear in a <code>
     * Document</code> but can be passed to/returned from a method that uses a <code>Token</code> instead of <code>
     * null</code> when the required token does not exist.
     *
     * <p>all methods will return an apporiate 'null' value so callers may deal with the object as they would any other
     * token.</p>
     */
    public static final Token NULL = new Token(false, true)
    {
        {
            setContent("null");
        }
    };

    /**
     * represents <code>EOF</code> as a <code>Token</code> - this token will only be seen by a caller if they are
     * working with tokens via the <code>Tokenizer</code>.
     */
    public static final Token EOF = new Token(true, false)
    {
        {
            setContent("EOF");
        }
    };

    //~ Instance fields

    private int column;

    private StringBuffer content;

    private boolean empty;
    private boolean eof;

    private int lineNumber;

    private int start;

    //~ Constructors

    public Token()
    {
        this(false, false);
    }

    private Token(boolean eof, boolean empty)
    {
        this.eof = eof;
        this.empty = empty;
    }

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Element#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public final void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    /**
     * add content to the token - this appends to an existing content
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param content content
     */
    public final void appendContent(String content)
    {
        if (this.content == null)
        {
            setContent(content);
        }
        else
        {
            this.content.append(content);
        }
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getChildren()
     */
    @Override public final List<Element> getChildren()
    {
        return Collections.emptyList();
    }

    /**
     * get the column of the line the token starts on
     *
     * @return start column
     */
    public final int getColumn()
    {
        return column;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getContent()
     */
    @Override public final String getContent()
    {
        // content will only ever be null if we're the 'null' token
        return (content != null) ? content.toString() : null;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getElements()
     */
    @Override public final List<Element> getElements()
    {
        return getChildren();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getEndOffset()
     */
    @Override public int getEndOffset()
    {
        return getStartOffset() + getLength();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getFirstElement()
     */
    @Override public final Token getFirstElement()
    {
        return this;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getLastElement()
     */
    @Override public final Token getLastElement()
    {
        return this;
    }

    /**
     * get the length of the token content
     *
     * @return length
     */
    public final int getLength()
    {
        return content.length();
    }

    /**
     * get the line number the token appears on
     *
     * @return line number
     */
    public final int getLineNumber()
    {
        return lineNumber;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getSignificant()
     */
    @Override public final List<Element> getSigChildren()
    {
        return getChildren();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getStartOffset()
     */
    @Override public final int getStartOffset()
    {
        return start;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getTokens()
     */
    @Override public final List<Token> getTokens()
    {
        List<Token> list = new ArrayList<Token>();
        list.add(this);

        return list;
    }

    /**
     * does this token represented <code>EOF</code>?
     *
     * @return <code>true</code> if <code>EOF</code>, <code>false</code> otherwise
     */
    public final boolean isEOF()
    {
        return eof;
    }

    /**
     * does this token represented <code>null</code>?
     *
     * @return <code>true</code> if <code>null</code>, <code>false</code> otherwise
     */
    public final boolean isNull()
    {
        return empty;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#isSignificant()
     */
    @Override public boolean isSignificant()
    {
        return true;
    }

    /**
     * set the column of the line the token starts on
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param column start column
     */
    public final void setColumn(int column)
    {
        this.column = column;
    }

    /**
     * set the content of the token - this replaces any content currently set
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param content content
     */
    public final void setContent(String content)
    {
        this.content = new StringBuffer(content);
    }

    /**
     * set the line number of the token in the document
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param lineNo line number
     */
    public final void setLineNumber(int lineNo)
    {
        this.lineNumber = lineNo;
    }

    /**
     * set the start offset of the token in the document
     *
     * <p>note: this method is only intended to be used by the tokenizer.</p>
     *
     * @param start start offset
     */
    public final void setStartOffset(int start)
    {
        this.start = start;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        String text = getContent();

        text = text.replaceAll("\\r", "\\\\r");
        text = text.replaceAll("\\n", "\\\\n");

        buffer.append(getClass().getName());

        buffer.append(" [ '");
        buffer.append(text);
        buffer.append("' ");
        buffer.append(getLineNumber());
        buffer.append(" ");
        buffer.append(getColumn());
        buffer.append(" ");
        buffer.append(getLength());
        buffer.append(" ");
        buffer.append(getStartOffset());
        buffer.append(" ");
        buffer.append(getEndOffset());
        buffer.append(" ]");

        return buffer.toString();
    }
}
