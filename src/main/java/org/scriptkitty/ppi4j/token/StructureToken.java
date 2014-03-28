package org.scriptkitty.ppi4j.token;

import org.scriptkitty.perl.lang.Structures;
import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.EmptyNodeException;


/**
 * a <code>StructureToken</code> represents a character that defines code structure.
 *
 * <p>the characters that define code structure as as follows:</p>
 *
 * <ul>
 *   <li><code>{</code></li>
 *   <li><code>(</code></li>
 *   <li><code>[</code></li>
 *   <li><code>}</code></li>
 *   <li><code>)</code></li>
 *   <li><code>]</code></li>
 *   <li><code>;</code></li>
 * </ul>
 *
 * <p>a note on sibilings of <code>StructureToken</code>s: as an <code>Element</code> braces sit outside the normal tree structure and in
 * this context, they never have siblings. however, as <code>Token</code>s, they do have siblings.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Structure.pm">CPAN - PPI::Token::Structure</a>
 */
public final class StructureToken extends Token
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Element#getNextSibling()
     */
    @Override public Element getNextSibling()
    {
        if (isSemiColon())
        {
            return super.getNextSibling();
        }

        return null;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getNextSignificantSibling()
     */
    @Override public Element getNextSignificantSibling()
    {
        if (isSemiColon())
        {
            return super.getNextSignificantSibling();
        }

        return null;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getNextToken()
     */
    @Override public Token getNextToken() throws EmptyNodeException
    {
        if (isSemiColon())
        {
            return super.getNextToken();
        }

        // our parent has to be a structure...
        Structure parent = (Structure) getParent();
        if (parent == null)
        {
            return null;
        }

        // if it's an open brace, descend into children...
        if (isOpenBrace())
        {
            Element child = parent.getSigChild(0);
            if (child != null)
            {
                return child.getFirstToken();
            }
            else if (parent.hasFinish())
            {
                // empty structure, next is closing brace
                return parent.getFinish();
            }

            /*
             * anything that slips through is a structure w/ an opening but no closing brace. we just have to go w/ it and continue as if we
             * started w/ a closing brace
             */
        }

        // fall back to the parent's implementation
        return parent.getNextToken();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getPrevSibling()
     */
    @Override public Element getPrevSibling()
    {
        if (isSemiColon())
        {
            return super.getPrevSibling();
        }

        return null;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getPrevSignificantSibling()
     */
    @Override public Element getPrevSignificantSibling()
    {
        if (isSemiColon())
        {
            return super.getPrevSignificantSibling();
        }

        return null;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getPrevToken()
     */
    @Override public Token getPrevToken() throws EmptyNodeException
    {
        if (isSemiColon())
        {
            return super.getNextToken();
        }

        // our parent has to be a structure...
        Structure parent = (Structure) getParent();
        if (parent == null)
        {
            return null;
        }

        // if it's an close brace, descend into children...
        if (isCloseBrace())
        {
            Element child = parent.getSigChild(0);
            if (child != null)
            {
                return child.getFirstToken();
            }
            else if (parent.hasFinish())
            {
                // empty structure, next is open brace
                return parent.getStart();
            }

            /*
             * anything that slips through is a structure w/ a closing but no opening brace. we just have to go w/ it and continue as if we
             * started w/ an opening brace
             */
        }

        // fall back to the parent's implementation
        return parent.getPrevToken();
    }

    /**
     * does this token represent a closing brace?
     *
     * @return <code>true</code> if closing brace, <code>false</code> otherwise
     *
     * @see    #isCloseCurly()
     * @see    #isCloseParen()
     * @see    #isCloseSquare()
     */
    public boolean isCloseBrace()
    {
        return Structures.isCloseBrace(getContent());
    }

    /**
     * does this token represent a closing curly <code>}</code>?
     *
     * @return <code>true</code> if closing curly, <code>false</code> otherwise
     */
    public boolean isCloseCurly()
    {
        return Structures.isCloseCurly(getContent());
    }

    /**
     * does this token represent a closing paren <code>)</code>?
     *
     * @return <code>true</code> if closing paren, <code>false</code> otherwise
     */
    public boolean isCloseParen()
    {
        return Structures.isCloseParen(getContent());
    }

    /**
     * does this token represent a closing square <code>]</code>?
     *
     * @return <code>true</code> if closing square, <code>false</code> otherwise
     */
    public boolean isCloseSquare()
    {
        return Structures.isCloseSquare(getContent());
    }

    /**
     * does this token represent an opening brace?
     *
     * @return <code>true</code> if opening brace, <code>false</code> otherwise
     *
     * @see    #isOpenCurly()
     * @see    #isOpenParen()
     * @see    #isOpenSquare()
     */
    public boolean isOpenBrace()
    {
        return Structures.isOpenBrace(getContent());
    }

    /**
     * does this token represent an opening curly <code>{</code>?
     *
     * @return <code>true</code> if opening curly, <code>false</code> otherwise
     */
    public boolean isOpenCurly()
    {
        return Structures.isOpenCurly(getContent());
    }

    /**
     * does this token represent an opening paren <code>(</code>?
     *
     * @return <code>true</code> if opening paren, <code>false</code> otherwise
     */
    public boolean isOpenParen()
    {
        return Structures.isOpenParen(getContent());
    }

    /**
     * does this token represent an opening square <code>[</code>?
     *
     * @return <code>true</code> if opening square, <code>false</code> otherwise
     */
    public boolean isOpenSquare()
    {
        return Structures.isOpenSquare(getContent());
    }

    /**
     * does this token represent a semi-colon <code>;</code>?
     *
     * @return <code>true</code> if semi-colon, <code>false</code> otherwise
     */
    public boolean isSemiColon()
    {
        return Structures.isSemiColon(getContent());
    }
}
