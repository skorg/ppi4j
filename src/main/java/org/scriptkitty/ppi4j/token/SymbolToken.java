package org.scriptkitty.ppi4j.token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scriptkitty.perl.lang.General;
import org.scriptkitty.perl.lang.Symbols;
import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.Token;


/**
 * a <code>SymbolToken</code> represents variables and other symbols that start with a sigil.
 *
 * <p>some of the boolean methods in this class were inspired by <code>Perl::Critic::Utils</code>.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Symbol.pm">CPAN - PPI::Token::Symbol</a>
 */
public class SymbolToken extends Token
{
    //~ Static fields/initializers

    /** '::' to 'main' resolver pattern - <code>/(?<=[\$\@\%\&\*])::/</code> */
    private static final Pattern RESOLVER = Pattern.compile("(?<=[\\$\\@\\%\\&\\*])::");

    //~ Enums

    public enum Sigil
    {
        AMP("&"), ARRAY("@"), HASH("%"), SCALAR("$"), TYPEGLOB("*");

        private String type;

        private Sigil(String type)
        {
            this.type = type;
        }

        public String asString()
        {
            return type;
        }

        static Sigil toSigil(char c)
        {
            switch (c)
            {
                case '&':
                {
                    return AMP;
                }
                case '@':
                {
                    return ARRAY;
                }
                case '%':
                {
                    return HASH;
                }
                case '*':
                {
                    return TYPEGLOB;
                }
                default:
                {
                    return SCALAR;
                }
            }
        }
    }

    //~ Methods

    /**
     * get the normalized, canonical version of the symbol
     *
     * <p>this does not fully resolve the symbol, but instead removes syntax variations, ie: <code>$::foo'bar::baz</code> becomes <code>
     * $main::foo::bar::baz</code>.</p>
     *
     * @return canonical version
     */
    public String getCanonical()
    {
        // trim whitespace
        Matcher matcher = RESOLVER.matcher(getContent().trim());
        return matcher.replaceAll("main::").replaceAll("'", "::");
    }

    /**
     * get the name of the symbol, minus its sigil
     *
     * @return symbol name
     */
    public String getName()
    {
        return getContent().substring(1);
    }

    /**
     * get the <b>apparent</b> type of this symbol in the form of its sigil
     *
     * @return sigil
     */
    public final Sigil getRawSigil()
    {
        return Sigil.toSigil(getContent().charAt(0));
    }

    /**
     * get the actual symbol this token refers to.
     *
     * <p>ie, a token of <code>$foo</code> may actually be referring to <code>@foo</code> if it is found in the form <code>
     * $foo[0]</code>.</p>
     *
     * @return the resolved symbol
     */
    public final String getSymbol()
    {
        String symbol = getCanonical();
        char sigil = symbol.charAt(0);

        Element next = getNextSignificantSibling();

        // easy case, we can't be anything else...
        if (Symbols.isHashSigil(sigil) || Symbols.isAmpSigil(sigil) || !(next instanceof Structure))
        {
            return symbol;
        }

        Structure.BraceType type = ((Structure) next).getBraceType();

        if (type == Structure.BraceType.UNKNOWN)
        {
            return symbol;
        }

        if (Symbols.isScalarSigil(sigil))
        {
            Element before = getPrevSignificantSibling();
            if (before instanceof CastToken)
            {
                if (((CastToken) before).isScalarCast() || ((CastToken) before).isArrayCast())
                {
                    return symbol;
                }
            }

            if (type == Structure.BraceType.SQUARE)
            {
                return "@" + symbol.substring(1);
            }

            if (type == Structure.BraceType.CURLY)
            {
                return "%" + symbol.substring(1);
            }
        }
        else if (Symbols.isArraySigil(sigil))
        {
            if (type == Structure.BraceType.CURLY)
            {
                return "%" + symbol.substring(1);
            }
        }

        return symbol;
    }

    /**
     * get the <b>actual</b> type of this symbol in the form of its sigil
     *
     * @return sigil
     */
    public final Sigil getSymbolSigil()
    {
        return Sigil.toSigil(getSymbol().charAt(0));
    }

    /**
     * does this token contain a module component, ie: <code>::</code>?
     *
     * @return <code>true</code> if contains a module component, <code>false</code> otherwise
     */
    public final boolean hasQualifiedName()
    {
        return General.isQualified(getContent());
    }
}
