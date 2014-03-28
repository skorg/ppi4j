package org.scriptkitty.ppi4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.scriptkitty.ppi4j.finder.Rule;
import org.scriptkitty.ppi4j.token.HereDocToken;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


/**
 * <code>Node</code> is the base class for all <code>Element <code>objects that are able to contain <code>Document</code>, <code>
 * Statement</code>, and <code>Structure</code> objects.</code></code>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Node.pm">CPAN - PPI::Node</a>
 */
public abstract class Node extends Element
{
    //~ Instance fields

    /** indicates if the node contains a <code>HereDocToken</code> child element */
    private boolean containsHereDoc;

    /** list of all the node's child elements */
    private List<Element> children;

    /** list of all the node's significant child elements */
    private List<Element> significant;

    //~ Constructors

    protected Node()
    {
        // easier to handle significant children this way
        children = new ArrayList<>();
        significant = new ArrayList<>();
    }

    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Element#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    /**
     * add a child element to the node
     *
     * @param child child element
     */
    public final void addChild(Element child)
    {
        children.add(child);

        if (child.isSignificant())
        {
            significant.add(child);
        }

        if (child instanceof HereDocToken)
        {
            containsHereDoc = true;
        }

        Element.parent.put(child, this);
    }

    /**
     * is the specified element contained logically 'within' a node.
     *
     * <p><code>Structure</code> objects are a special case where thebrace tokens at either side are generally considered to be 'within' the
     * structure object, even if they are not actually in the structure's elements.</p>
     *
     * @param  element element
     *
     * @return <code>true</code> if the element is contained by this one, <code>false</code> otherwise
     */
    public boolean contains(Element element)
    {
        while ((element = element.getParent()) != null)
        {
            if (this == element)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * find all elements in node that match the specified class.
     *
     * <p>note: if the specified class is subclassed, this method will find those instances as well. this method will also recurse into the
     * child elements of each node it encounters.</p>
     *
     * @param  want element class
     *
     * @return list of elements that match the specified class
     *
     * @see    #find(Class, boolean)
     * @see    #find(Class, boolean, boolean)
     */
    public final <T extends Element> List<T> find(Class<? extends Element> want)
    {
        return find(want, false);
    }

    /**
     * find all elements in a node that match the criteria defined by the passed <code>Rule</code> implementation.
     *
     * @param  rule finder rule
     *
     * @return list of elements that match the specified criteria
     */
    public final <T extends Element> List<T> find(Rule rule)
    {
        ArrayList<T> found = new ArrayList<>();
        LinkedList<Element> queue = new LinkedList<>(children);

        while (!queue.isEmpty())
        {
            Element element = queue.poll();

            if (rule.matches(element))
            {
                // meh!
                @SuppressWarnings("unchecked")
                T t = (T) element;
                found.add(t);
            }

            // finder doesn't support recursing into children or we don't have any
            if (!rule.recurse(element) || !(element instanceof Node))
            {
                continue;
            }

            // depth-first keeps the queue size down and provides a better logical order
            if (element instanceof Structure)
            {
                if (((Structure) element).hasFinish())
                {
                    queue.add(0, ((Structure) element).getFinish());
                }

                queue.addAll(0, ((Node) element).children);

                if (((Structure) element).hasStart())
                {
                    queue.add(0, ((Structure) element).getStart());
                }
            }
            else
            {
                queue.addAll(0, ((Node) element).children);
            }
        }

        return found;
    }

    /**
     * find all elements in node that match the specified class.
     *
     * <p>note: this method will also recurse into the child elements of each node it encounters.</p>
     *
     * @param  want   element class
     * @param  incSub <code>true</code>to include subclasses, <code>false</code> otherwise
     *
     * @return list of elements that match the specified class
     *
     * @see    #find(Class)
     * @see    #find(Class, boolean, boolean)
     */
    public final <T extends Element> List<T> find(Class<? extends Element> want, boolean incSub)
    {
        return find(want, incSub, true);
    }

    /**
     * find all elements in node that match the specified class.
     *
     * @param  want    element class
     * @param  incSub  <code>true</code>to include subclasses, <code>false</code> otherwise
     * @param  recurse <code>true</code>to recurse into the element's children, <code>false</code> otherwise
     *
     * @return list of elements that match the specified class
     *
     * @see    #find(Class, boolean)
     * @see    #find(Class, boolean)
     */
    public final <T extends Element> List<T> find(final Class<? extends Element> want, final boolean incSub, final boolean recurse)
    {
        return find(new Rule()
            {
                @Override public boolean matches(Element element)
                {
                    if (element.getClass() == want)
                    {
                        return true;
                    }

                    if (incSub && (element.getClass().getSuperclass() == want))
                    {
                        return true;
                    }

                    return false;
                }

                @Override public boolean recurse(Element element)
                {
                    return recurse;
                }
            });
    }

    /**
     * access a child element in the node.
     *
     * <p>elements returned from this method will include those that are not 'significant'. specifying a negative index will retrieve
     * children from the end of the list.</p>
     *
     * @param  index element position
     *
     * @return child element or <code>null</code> if there is no element at the specified index
     *
     * @see    #isSignificant()
     */
    public final Element getChild(int index)
    {
        return fetch(children, index);
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getChildren()
     */
    @Override public List<Element> getChildren()
    {
        return Collections.unmodifiableList(children);
    }

    /**
     * get the number of lexical children contained by the node.
     *
     * <p>the returned value will also include any 'insignificant' elements the node may have.</p>
     *
     * @return number of children
     *
     * @see    #isSignificant()
     */
    public final int getChildrenCount()
    {
        return children.size();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getContent()
     */
    @Override public String getContent()
    {
        StringBuffer buffer = new StringBuffer();

        for (Element child : children)
        {
            buffer.append(child.getContent());
        }

        return buffer.toString();
    }

    /**
     * access an element in the node
     *
     * @param  index element position
     *
     * @return child element or <code>null</code> if there is no element at the specified index
     */
    public final Element getElement(int index)
    {
        return fetch(getElements(), index);
    }

    /**
     * get the number of structural elements contained by this node.
     *
     * @return number of contained elements
     *
     * @see    #getElements()
     */
    public int getElementCount()
    {
        return getChildrenCount();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getElements()
     */
    @Override public List<Element> getElements()
    {
        // default implementation is the same
        return getChildren();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getEndOffset()
     */
    @Override public int getEndOffset()
    {
        if (containsHereDoc)
        {
            List<HereDocToken> list = find(HereDocToken.class, false, false);

            if (!list.isEmpty())
            {
                return list.get(list.size() - 1).getEndOffset();
            }
        }

        return getLastElement().getEndOffset();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getFirstElement()
     */
    @Override public Element getFirstElement()
    {
        return getChild(0);
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getLastElement()
     */
    @Override public Element getLastElement()
    {
        return getChild(children.size() - 1);
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getParent()
     */
    @Override public final Node getParent()
    {
        return (Node) super.getParent();
    }

    /**
     * get the number of 'significant' lexical children contained by the node.
     *
     * @return number of 'significant' children
     *
     * @see    #isSignificant()
     */
    public final int getSigChidrenCount()
    {
        return significant.size();
    }

    /**
     * access a 'significant' child element in the node.
     *
     * <p>specifying a negative index will retrieve children from the end of the list.</p>
     *
     * @param  index element position
     *
     * @return child element or <code>null</code> if there is no element at the specified index
     *
     * @see    #isSignificant()
     */
    public final Element getSigChild(int index)
    {
        return fetch(significant, index);
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getSigChildren()
     */
    @Override public List<Element> getSigChildren()
    {
        return Collections.unmodifiableList(significant);
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getStartOffset()
     */
    @Override public int getStartOffset()
    {
        return getFirstElement().getStartOffset();
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#getTokens()
     */
    @Override public List<Token> getTokens()
    {
        ArrayList<Token> tokens = new ArrayList<>();

        for (Element element : children)
        {
            for (Token token : element.getTokens())
            {
                tokens.add(token);
            }
        }

        return tokens;
    }

    /**
     * does the node have children?
     *
     * @return <code>true</code> if the node has children, <code>false</code> otherwise
     */
    public final boolean hasChildren()
    {
        return (!children.isEmpty());
    }

    /**
     * does the node have 'significant' children?
     *
     * @return <code>true</code> if the node has 'significant' children, <code>false</code> otherwise
     *
     * @see    #isSignificant()
     */
    public final boolean hasSignificant()
    {
        return (!significant.isEmpty());
    }

    /**
     * does the node represent a lexical scope boundary?
     *
     * @return <code>true</code> if a boundary, <code>false</code> otherwise
     */
    public boolean isScoped()
    {
        return false;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#isSignificant()
     */
    @Override public boolean isSignificant()
    {
        return true;
    }

    /**
     * convert this node into another one.
     *
     * <p>note: this method is only intended to be used by the parser.</p>
     *
     * @param  clazz class to convert to
     *
     * @return covereted node
     */
    protected final Node convert(Class<? extends Node> clazz)
    {
        try
        {
            Node stmt = clazz.newInstance();
            stmt.setChildren(children, significant);

            for (Element e : children)
            {
                Element.parent.put(e, stmt);
            }

            return stmt;
        }
        catch (InstantiationException e)
        {
            // can never happen...
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            // can never happen...
            throw new RuntimeException(e);
        }
    }

    /**
     * get a token of the specified class type from the element's significant children
     *
     * @param  clazz token class
     * @param  index index
     *
     * @return <code>Token</code> object or a <code>Token.NULL</code> object if the requested token does not exist
     */
    protected final <T extends Token> T getToken(Class<? extends Token> clazz, int index)
    {
        Element element = getSigChild(index);

        if ((element != null) && ((element.getClass() == clazz) || (element.getClass().getSuperclass() == clazz)))
        {
            @SuppressWarnings("unchecked")
            T token = (T) element;
            return token;
        }

        @SuppressWarnings("unchecked")
        T token = (T) Token.NULL;
        return token;
    }

    /*
     * @see org.scriptkitty.ppi4j.Element#removeReferences()
     */
    @Override protected void removeReferences()
    {
        super.removeReferences();

        children.clear();
        significant.clear();
    }

    private void setChildren(List<Element> children, List<Element> significant)
    {
        this.children = children;
        this.significant = significant;
    }
}
