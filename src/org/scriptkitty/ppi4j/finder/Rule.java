package org.scriptkitty.ppi4j.finder;

import org.scriptkitty.ppi4j.Element;


public interface Rule
{
    //~ Methods

    boolean matches(Element element);

    boolean recurse(Element element);
}
