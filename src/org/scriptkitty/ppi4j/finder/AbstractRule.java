package org.scriptkitty.ppi4j.finder;

import org.scriptkitty.ppi4j.Element;


abstract class AbstractRule implements Rule
{
    //~ Methods

    protected final boolean matchesClass(Element element, Class<? extends Element> want)
    {
        return (element.getClass() == want);
    }

    protected final boolean matchesSubclass(Element element, Class<? extends Element> want)
    {
        return (matchesClass(element, want) || (element.getClass().getSuperclass() == want));
    }
}
