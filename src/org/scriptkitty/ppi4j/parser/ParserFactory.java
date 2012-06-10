package org.scriptkitty.ppi4j.parser;

import org.scriptkitty.ppi4j.util.IErrorProxy;
import org.scriptkitty.ppi4j.util.NullErrorProxy;


public class ParserFactory
{
    //~ Methods

    public static Parser createParser(ITokenProvider provider)
    {
        return createParser(provider, null);
    }

    public static Parser createParser(ITokenProvider provider, IErrorProxy proxy)
    {
        if (proxy == null)
        {
            proxy = NullErrorProxy.getInstance();
        }

        return new Parser(provider, proxy);
    }
}
