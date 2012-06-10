package org.scriptkitty.ppi4j.finder;

import org.scriptkitty.ppi4j.Element;


public abstract class SimpleRule extends AbstractRule
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.finder.Rule#recurse(org.scriptkitty.ppi4j.Element)
     */
    @Override public final boolean recurse(Element element)
    {
        return false;
    }
}
