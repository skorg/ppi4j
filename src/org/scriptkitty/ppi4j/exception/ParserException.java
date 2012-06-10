package org.scriptkitty.ppi4j.exception;

/**
 * a <code>ParserException</code> is a wrapper for any exceptions that are encountered during parsing and is able to
 * indicate which line number and column the exception occured on.
 */
public class ParserException extends Exception
{
    //~ Static fields/initializers

    private static final long serialVersionUID = 19851790279784295L;

    //~ Instance fields

    private int column;

    private int line;

    //~ Constructors

    public ParserException(int line, int column, Throwable t)
    {
        super(t);

        this.line = line;
        this.column = column;
    }

    //~ Methods

    /**
     * get the column the error occured on
     *
     * @return column
     */
    public int getColumn()
    {
        return column;
    }

    /**
     * get the line number the error occured on
     *
     * @return line number
     */
    public int getLineNumber()
    {
        return line;
    }
}
