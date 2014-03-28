package org.scriptkitty.ppi4j.finder;

import org.scriptkitty.ppi4j.Element;


public abstract class RecursiveRule extends AbstractRule
{
    //~ Methods

    @Override public final boolean recurse(Element element)
    {
        return true;
    }
}
