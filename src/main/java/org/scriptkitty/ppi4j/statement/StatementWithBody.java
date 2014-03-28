package org.scriptkitty.ppi4j.statement;

import java.util.List;

import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.structure.BlockStructure;


abstract class StatementWithBody extends Statement
{
    //~ Methods

    public final BlockStructure getBody()
    {
        List<BlockStructure> list = find(BlockStructure.class, false, false);

        if (list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }

    public final boolean hasBody()
    {
        return (getBody() != null);
    }

    /*
     * @see org.scriptkitty.ppi4j.Statement#isComplete()
     */
    @Override public final boolean isComplete()
    {
        Element child = getSigChild(-1);
        return ((child instanceof BlockStructure) && ((BlockStructure) child).isComplete());
    }

    /*
     * @see org.scriptkitty.ppi4j.Statement#isNormal()
     */
    @Override public final boolean isNormal()
    {
        return false;
    }

    /*
     * @see org.scriptkitty.ppi4j.Node#isScoped()
     */
    @Override public boolean isScoped()
    {
        return true;
    }
}
