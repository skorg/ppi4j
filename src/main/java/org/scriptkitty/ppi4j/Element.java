package org.scriptkitty.ppi4j;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.scriptkitty.ppi4j.exception.EmptyNodeException;
import org.scriptkitty.ppi4j.structure.ListStructure;
import org.scriptkitty.ppi4j.structure.SubscriptStructure;
import org.scriptkitty.ppi4j.util.ElementUtils;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


/**
 * <code>Element</code> is the base class for all ppi4j objects.
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Element.pm">CPAN - PPI::Element</a>
 */
public abstract class Element
{
    //~ Static fields/initializers

    // TODO: implement delete, prune, remove, etc
    protected static final Map<Element, Element> parent = new WeakHashMap<>();

    //~ Enums

    public enum Attribute
    {
        DAMAGED, DEREFERENCE, HINT, INVALID
    }

    //~ Instance fields

    private Attribute attribute;

    //~ Methods

    /**
     * accept an instance of an <code>INodeVisitor</code>.
     *
     * @param visitor visitor
     */
    public abstract void accept(INodeVisitor visitor);

    /**
     * get all child elements lexically within the node.
     *
     * <p>in the case of <code>Structure<code>classes, the returned list will <b>not</b> include the brace tokens at either end of the
     * structure.</code></code></p>
     *
     * <p>the list returned will be wrapped by <code>Collections.unmodifiableList(List)</code> so the underlying element structure can not
     * be changed.</p>
     *
     * @return child elements
     */
    public abstract List<Element> getChildren();

    /**
     * get the contents of the element as if it were a line of source code.
     *
     * <p>note: because of the way heredoc is handled, any heredoc content is not included in the output of this method and you should not
     * attempt to <code>eval</code> or execute the result.</p>
     *
     * @return contents of element as if it were a line of code
     */
    // TODO add note about 'serialize' if it ever gets done :)
    public abstract String getContent();

    /**
     * get all child elements structurally within the node.
     *
     * <p>in the case of <code>Structure</code>classes, the returned list will include the brace tokens at either end of the structure
     * (provided they are set).</p>
     *
     * <p>the list returned will be wrapped by <code>Collections.unmodifiableList(List)</code> so the underlying element structure can not
     * be changed.</p>
     *
     * @return child elements
     */
    public abstract List<Element> getElements();

    /**
     * get the end offset of the element.
     *
     * @return end offset
     */
    public abstract int getEndOffset();

    /**
     * get the first element structurally within a <code>Node</code> object.
     *
     * <p>if this method is called against a <code>Token</code>, the same object will be returned. as with the <code>getElements()</code>
     * method, this does include the brace tokens for <code>Structure</code> objects.</p>
     *
     * @return first element or <code>null</code> if the node does not contain any elements
     *
     * @see    Node#getElements()
     */
    public abstract Element getFirstElement();

    /**
     * get the last element structurally within a <code>Node</code> object.
     *
     * <p>if this method is called against a <code>Token</code>, the same object will be returned. as with the <code>getElements()</code>
     * method, this does include the brace tokens for <code>Structure</code> objects.</p>
     *
     * @return last element or <code>null</code> if the node does not contain any elements
     *
     * @see    Node#getElements()
     */
    public abstract Element getLastElement();

    /**
     * get all 'significant' child elements lexically within the node.
     *
     * <p>in the case of <code>Structure</code> classes, the returned list will <b>not</b> include the brace tokens at either end of the
     * structure.</p>
     *
     * <p>the list returned will be wrapped by <code>Collections.unmodifiableList(List)</code> so the underlying element structure can not
     * be changed.</p>
     *
     * @return 'significant' child elements
     *
     * @see    #isSignificant()
     */
    public abstract List<Element> getSigChildren();

    /**
     * get the start offset of the element.
     *
     * @return start offset
     */
    public abstract int getStartOffset();

    /**
     * get a list of all the tokens that comprise the element.
     *
     * <p>this is essentially the equivalent of getting back this part of the document as if it had not been parsed.</p>
     *
     * @return list of tokens
     */
    public abstract List<Token> getTokens();

    /**
     * is the element significant?
     *
     * <p>this method allows for distinguishing between tokens that comprise the code and tokens that are not significant, such as
     * whitespace, POD, or the portion of a file after (and including) the <code>__END__</code> token.</p>
     *
     * @return <code>true</code> if the element is significant, <code>false</code> otherwise.
     */
    public abstract boolean isSignificant();

    /**
     * get the top level <code>Document</code> for the element.
     *
     * @return document or <code>null</code> if the element is not contained within a document.
     */
    public final Document getDocument()
    {
        Element top = getTop();

        if (top instanceof Document)
        {
            return (Document) top;
        }

        return null;
    }

    /**
     * get the first <code>Token</code> object contained within this element.
     *
     * <p>if this method is invoked against a <code>Node</code> subclass, it will descend into its children searching for the first <code>
     * Token</code> object. if this method is invoked on a <code>Token</code> object, it will return the same object.</p>
     *
     * @return first token
     *
     * @throws EmptyNodeException if an illegal, empty <code>Statement</code>exists as a child of this element
     */
    public final Token getFirstToken() throws EmptyNodeException
    {
        return (Token) searchElements(new Iterator()
            {
                @Override String eMessage()
                {
                    return "found empty node while getting first token";
                }

                @Override boolean matches(Element element)
                {
                    return (element instanceof Node);
                }

                @Override Element next(Element element)
                {
                    return element.getFirstElement();
                }
            });
    }

    /**
     * get the last <code>Token</code> object contained within this element.
     *
     * <p>if this method is invoked against a <code>Node</code> subclass, it will descend into its children searching for the last <code>
     * Token</code> object. if this method is invoked on a <code>Token</code> object, it will return the same object.</p>
     *
     * @return last token
     *
     * @throws EmptyNodeException if an illegal, empty <code>Statement</code>exists as a child of this element
     */
    public final Token getLastToken() throws EmptyNodeException
    {
        return (Token) searchElements(new Iterator()
            {
                @Override String eMessage()
                {
                    return "found empty node while getting last token";
                }

                @Override boolean matches(Element element)
                {
                    return (element instanceof Node);
                }

                @Override Element next(Element element)
                {
                    return element.getLastElement();
                }
            });
    }

    /**
     * get the next element immediately following this one.
     *
     * @return the element immediately following this one or <code>null</code> if there is no next sibling.
     */
    public Element getNextSibling()
    {
        if (parent.containsKey(this))
        {
            return getSibling(parent.get(this).getChildren(), true);
        }

        return null;
    }

    /**
     * get the next 'significant' element immediately following this one.
     *
     * @return the 'significant' element immediately following this one or <code>null</code> if there is no next sibling.
     *
     * @see    #isSignificant()
     */
    public Element getNextSignificantSibling()
    {
        if (parent.containsKey(this))
        {
            return getSibling(parent.get(this).getSigChildren(), true);
        }

        return null;
    }

    /**
     * get the <code>Token <code>that is immediately after the current element, even if it is not within the same parent node as this
     * one.</code></code>
     *
     * @return the next token or <code>null</code> if there are no more tokens after the element
     *
     * @throws EmptyNodeException if an illegal, empty <code>Statement</code>exists as a child of this element
     */
    public Token getNextToken() throws EmptyNodeException
    {
        return prevOrNextToken(new PrevOrNextToken()
            {
                @Override Element getElement(Element element)
                {
                    return element.getNextSibling();
                }

                @Override Token getToken(Element element) throws EmptyNodeException
                {
                    return element.getFirstToken();
                }

                @Override Token getToken(Structure struct)
                {
                    return struct.getFinish();
                }

                @Override boolean hasToken(Structure struct)
                {
                    return struct.hasFinish();
                }
            });
    }

    /**
     * get the parent of this element.
     *
     * <p>if the element is contained within a <code>Node</code>, this method will return that node.</p>
     *
     * @return element's parent
     */
    public Element getParent()
    {
        return parent.get(this);
    }

    /**
     * get the first parent statement that is lexically 'above' this element.
     *
     * <p>if the object itself is an instance of a <code>Statement</code>, the same instance will be returned.</p>
     *
     * @return the parent satement or <code>null</code> if the element is not in a statement and not a statement itself
     */
    public final Statement getParentStatement()
    {
        Element cursor = this;

        while (!(cursor instanceof Statement))
        {
            cursor = parent.get(cursor);
        }

        return (Statement) cursor;
    }

    /**
     * get the previous element immediately following this one.
     *
     * @return the element previously following this one or <code>null</code> if there is no previous sibling.
     */
    public Element getPrevSibling()
    {
        if (parent.containsKey(this))
        {
            return getSibling(parent.get(this).getChildren(), false);
        }

        return null;
    }

    /**
     * get the previous 'significant' element immediately following this one.
     *
     * @return the 'significant' element previously following this one or <code>null</code> if there is no previous sibling.
     *
     * @see    #isSignificant()
     */
    public Element getPrevSignificantSibling()
    {
        if (parent.containsKey(this))
        {
            return getSibling(parent.get(this).getSigChildren(), false);
        }

        return null;
    }

    /**
     * get the <code>Token <code>that is immediately before the current element, even if it is not within the same parent node as this
     * one.</code></code>
     *
     * @return the previous token or <code>null</code> if there are no more tokens before the element
     *
     * @throws EmptyNodeException if an illegal, empty <code>Statement</code>exists as a child of this element
     */
    public Token getPrevToken() throws EmptyNodeException
    {
        return prevOrNextToken(new PrevOrNextToken()
            {
                @Override Element getElement(Element element)
                {
                    return element.getPrevSibling();
                }

                @Override Token getToken(Element element) throws EmptyNodeException
                {
                    return element.getLastToken();
                }

                @Override Token getToken(Structure struct)
                {
                    return struct.getStart();
                }

                @Override boolean hasToken(Structure struct)
                {
                    return struct.hasStart();
                }
            });
    }

    /**
     * get a list of all the tokens that comprise the element.
     *
     * <p>this method primarily exists as a convience method to assist with unit-testing.</p>
     *
     * @return array of tokens
     *
     * @see    #getTokens()
     */
    public final Token[] getTokenArray()
    {
        List<Token> tokens = getTokens();
        return tokens.toArray(new Token[tokens.size()]);
    }

    /**
     * get the top level node in the document tree.
     *
     * <p>most of the time, this should be a <code>Document</code> object but could be same object if the element has been removed from the
     * document.</p>
     *
     * @return the top most element, which may the the same instance as the calling object
     */
    public final Element getTop()
    {
        try
        {
            return searchElements(new Iterator()
                {
                    @Override boolean matches(Element element)
                    {
                        return parent.containsKey(element);
                    }

                    @Override Element next(Element element)
                    {
                        return parent.get(element);
                    }
                });
        }
        catch (EmptyNodeException e)
        {

            // can never happen based upon implementation above - compiler bitches w/o this
            throw new RuntimeException();
        }
    }

    /**
     * does the element have the specified attribute.
     *
     * <p>note: this method is only intended to be used by the classes that attempt to serialize the element.</p>
     *
     * @param attribute attribute
     */
    public final boolean hasAttribute(Attribute attribute)
    {
        return (this.attribute == attribute);
    }

    /**
     * is the passed element an ancestor of this one
     *
     * <p>elements are considered to be ancestors of themselves.</p>
     *
     * @param  element possible ancestor
     *
     * @return <code>true</code> if the passed element is an ancestor of this one, <code>false</code> otherwise
     */
    public final boolean isAncestorOf(Element element)
    {
        // the passed element starts as the cursor
        return isFamily(element, this);
    }

    /**
     * is the passed element a decendeant of this one
     *
     * <p>elements are considered to be decendants of themselves.</p>
     *
     * @param  element possible decendant
     *
     * @return <code>true</code> if the passed element is a decendeant of this one, <code>false</code> otherwise
     */
    public final boolean isDescendantOf(Element element)
    {
        // 'this' starts as the cursor
        return isFamily(this, element);
    }

    /**
     * is the element a literal hash key?
     *
     * <p>note: subroutines, ie: <code>%hash = ( foo() => 1 );</code> are not considered to be hash keys.</p>
     *
     * @return <code>true</code> if the element represents a hash key, <code>false</code> otherwise
     *
     * @see    <a href="http://search.cpan.org/dist/Perl-Critic/lib/Perl/Critic/Utils.pm">Perl::Critic::Utils</a>
     */
    public final boolean isHashKey()
    {
        // we're followed by arg list, this is a function call
        Element next = getNextSignificantSibling();
        if (next instanceof ListStructure)
        {
            return false;
        }

        // declarative style: %hash = (foo => bar);
        if (ElementUtils.isEqualArrowOperatorToken(next))
        {
            return true;
        }

        // curly-brace style: $hash{foo} = bar;
        Element parent = getParent();
        if ((parent != null) && (parent.getParent() instanceof SubscriptStructure))
        {
            return true;
        }

        return false;
    }

    /**
     * is the parent of this element of the specified class type?
     *
     * @param  clazz parent class type
     *
     * @return <code>true</code> if class type matches that of the parent, <code>false</code> otherwise
     */
    public final boolean parentIs(Class<? extends Element> clazz)
    {
        if (parent.containsKey(this))
        {
            return (getParent().getClass() == clazz);
        }

        return false;
    }

    /**
     * is the parent statment of this element of the specified statement type?
     *
     * @param  clazz parent statement class type
     *
     * @return <code>true</code> if class type matches that of the parent, <code>false</code> otherwise
     */
    public final boolean parentStatementIs(Class<? extends Statement> clazz)
    {
        Statement stmt = getParentStatement();
        return ((stmt != null) && (stmt.getClass() == clazz));
    }

    /**
     * set an attribute on the element.
     *
     * <p>note: this method is only intended to be used by the parser.</p>
     *
     * @param attribute attribute
     */
    public final void setAttribute(Attribute attribute)
    {
        this.attribute = attribute;
    }

    /**
     * retrieve an item from a list.
     *
     * @param  list  list
     * @param  index item position
     *
     * @return the requested element or <code>null</code> if it does not exist at the specified index
     */
    protected final Element fetch(List<Element> list, int index)
    {
        try
        {
            return list.get((index >= 0) ? index : (list.size() + index));
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    /**
     * remove this element and all that reference it from the <code>parent</code> map
     */
    protected void removeReferences()
    {
        for (Element child : getElements())
        {
            child.removeReferences();
        }

        if (parent.containsKey(this))
        {
            parent.remove(this);
        }
    }

    private Element getSibling(List<Element> kids, boolean next)
    {
        int offset = (next) ? 1 : -1;
        int index = kids.indexOf(this) + offset;

        if (index == -1)
        {
            return null;
        }

        return fetch(kids, index);
    }

    private boolean isFamily(Element cursor, Element element)
    {
        while (element != cursor)
        {
            if (!parent.containsKey(cursor))
            {
                return false;
            }

            cursor = parent.get(cursor);
        }

        return true;
    }

    private Token prevOrNextToken(PrevOrNextToken provider) throws EmptyNodeException
    {
        Element cursor = this;

        while (true)
        {
            Element element = provider.getElement(cursor);

            if (element != null)
            {
                if (element instanceof Token)
                {
                    return (Token) element;
                }

                return provider.getToken(element);
            }

            if ((cursor = getParent()) == null)
            {
                return null;
            }

            if ((cursor instanceof Structure) && provider.hasToken((Structure) cursor))
            {
                return provider.getToken((Structure) cursor);
            }
        }
    }

    private Element searchElements(Iterator iterator) throws EmptyNodeException
    {
        Element cursor = this;

        while (iterator.matches(cursor))
        {
            if ((cursor = iterator.next(cursor)) == null)
            {
                throw new EmptyNodeException(iterator.eMessage());
            }
        }

        return cursor;
    }

    //~ Inner Classes

    // horray for closures! ;)
    private abstract class Iterator
    {
        abstract boolean matches(Element element);

        abstract Element next(Element element);

        String eMessage()
        {
            return "";
        }
    }

    // horray for closures! ;)
    private abstract class PrevOrNextToken
    {
        abstract Element getElement(Element element);

        abstract Token getToken(Element element) throws EmptyNodeException;

        abstract Token getToken(Structure struct);

        abstract boolean hasToken(Structure struct);
    }
}
