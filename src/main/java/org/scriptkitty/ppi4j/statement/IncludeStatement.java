package org.scriptkitty.ppi4j.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.scriptkitty.perl.lang.Keyword;
import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.QuoteLikeToken;
import org.scriptkitty.ppi4j.token.QuoteToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.ElementUtils;
import org.scriptkitty.ppi4j.visitor.INodeVisitor;


/**
 * <code>IncludeStatement</code>s represent statements that cause the inclusion (or exclusion) of other code.
 *
 * <p>statements starting with the following are covered by this class:</p>
 *
 * <ul>
 *   <li><code>no</code></li>
 *   <li><code>use</code></li>
 *   <li><code>require</code></li>
 * </ul>
 *
 * <p>and the following three situations:</p>
 *
 * <ul>
 *   <li>a pragma is being used, see {@link #isPragma()}</li>
 *   <li>a module is being included or excluded (as is the case with <code>no</code>)</li>
 *   <li>a dependency is being defined on a particular version of perl, see {@link #isVersion()}</li>
 * </ul>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Statement/Include.pm">CPAN - PPI::Statement::Include</a>
 */
public class IncludeStatement extends Statement
{
    //~ Methods

    /*
     * @see org.scriptkitty.ppi4j.Statement#accept(org.scriptkitty.ppi4j.ast.INodeVisitor)
     */
    @Override public void accept(INodeVisitor visitor)
    {
        visitor.visit(this);
    }

    /**
     * get the arguments passed to the module.
     *
     * <p>arguments are what follow the module/pragma or version depenency declaration, i.e. what is used to construct the what gets passed
     * to the module's <code>import</code> subroutine.</p>
     *
     * <p>the returned list will contain <code>Token</code> objects and can be cast as such.</p>
     *
     * @return list of elements or an empty list if no arguments are found
     */
    public List<Element> getArguments()
    {
        List<Element> args = new ArrayList<>(getSigChildren());

        // remove the "use", "no", or "require"
        args.remove(0);

        // remove the terminator - ha! - he'll be back!
        if (ElementUtils.isSemiColonToken(args.get(args.size() - 1)))
        {
            args.remove(args.size() - 1);
        }

        // remove the module name or version dependency
        args.remove(0);

        if (args.isEmpty())
        {
            return Collections.emptyList();
        }

        // do we have a required version...
        if (args.get(0) instanceof NumberToken)
        {
            // yup, and that's it...
            if (args.size() == 1)
            {
                return Collections.emptyList();
            }

            // yes, and additional arguments
            if (!(args.get(1) instanceof OperatorToken))
            {
                args.remove(0);
            }
        }

        return args;
    }

    /**
     * get the <code>Token</code> that represents the module (includes pragma names) specified in the include statement.
     *
     * <p>the following are covered:</p>
     *
     * <pre>
     *  use strict;
     *  use My::Module;
     *  no strict;
     *  require My::Module;
     * </pre>
     *
     * <p>however, these are not:</p>
     *
     * <pre>
     *  use 5.006;
     *  require 5.005;
     *  require "explicit/file/name.pl";
     * </pre>
     *
     * @return the module name or <code>Token.NULL</code> if a name has not been specified
     */
    public Token getModule()
    {
        return getToken(WordToken.class, 1);
    }

    /**
     * get the <code>Token</code> that represents the the minimum version of the module required by the statement.
     *
     * @return the module version or <code>Token.NULL</code> if a version has not been specified
     */
    public Token getModuleVersion()
    {
        // ie: use Module 'x'
        if (!getToken(OperatorToken.class, 3).isNull())
        {
            return Token.NULL;
        }

        return getToken(NumberToken.class, 2);
    }

    /**
     * get the <code>Token</code> that represents the name of what is being 'included'.
     *
     * <p>this is a convienence method that will return either the version of perl the code depends on or the module being 'included'.</p>
     *
     * @return version dependency or module name
     *
     * @see    #getModule()
     * @see    #getVersion()
     */
    public Token getName()
    {
        if (isVersion())
        {
            return getVersion();
        }

        return getModule();
    }

    /**
     * get the <code>Token</code> that represents something being required.
     *
     * <p>anything not covered by <code>getModule()</code> and <code>getVersion()</code> is handled here:</p>
     *
     * <pre>
     *  require qw(foo.pl);
     *  require "explicit/file/name.pl";
     * </pre>
     *
     * @return the module name or <code>Token.NULL</code> if a name has not been specified
     */
    public final Token getRequireToken()
    {
        Token token = getModule();

        if (token.isNull())
        {
            token = getVersion();

            if (token.isNull())
            {
                if (((token = getToken(QuoteToken.class, 1))).isNull())
                {
                    token = getToken(QuoteLikeToken.class, 1);
                }
            }
        }

        return token;
    }

    /*
     * @see org.scriptkitty.ppi4j.Statement#getType()
     *
     * TODO: list specific types
     */
    @Override public Type getType()
    {
        String content = getToken(WordToken.class, 0).getContent();

        if ("use".equals(content))
        {
            return Type.USE;
        }

        if ("require".equals(content))
        {
            return Type.REQUIRE;
        }

        if ("no".equals(content))
        {
            return Type.NO;
        }

        return Type.UNKNOWN;
    }

    /**
     * get the <code>Token</code> that represents the version of perl the code depends on.
     *
     * <p>this covers the following:</p>
     *
     * <pre>
     *  use 5.6.1;
     *  use v5.6.1;
     *
     *  require 5.6.1;
     *  require v5.6.1;
     * </pre>
     *
     * @return the version number or <code>Token.NULL</code> if the statement is not a version dependency
     *
     * @see    #getModule()
     */
    public Token getVersion()
    {
        return getToken(NumberToken.class, 1);
    }

    /**
     * does this include statement represent a pragma?
     *
     * <p>note: ppi4j assumes that any "module name" matching a set of lowercase letters (and perhaps number, as in the case of <code>use
     * utf8;</code>) is a prama.</p>
     *
     * @return <code>true</code> if the include is for a pragma, <code>false</code> otherwise
     *
     * @see    #getModule()
     */
    public boolean isPragma()
    {
        Token token = getModule();

        if (!token.isNull())
        {
            return Keyword.isPragma(getModule().getContent());
        }

        return false;
    }

    /**
     * does this include statement represent <code>use base</code> pragma?
     *
     * @return <code>true</code> if pragma, <code>false</code> otherwise
     */
    public boolean isUseBase()
    {
        return (isPragma() && getModule().toKeyword().isBaseKeyword());
    }

    /**
     * does this include statement represent <code>use fields</code> pragma?
     *
     * @return <code>true</code> if pragma, <code>false</code> otherwise
     */
    public boolean isUseFields()
    {
        return (isPragma() && getModule().toKeyword().isFieldsKeyword());
    }

    /**
     * does this include statement represent <code>use parent</code> pragma?
     *
     * @return <code>true</code> if pragma, <code>false</code> otherwise
     */
    public boolean isUseParent()
    {
        return (isPragma() && getModule().toKeyword().isParentKeyword());
    }

    /**
     * does this include statement represent <code>use strict</code> pragma?
     *
     * @return <code>true</code> if pragma, <code>false</code> otherwise
     */
    public boolean isUseStrict()
    {
        return (isPragma() && getModule().toKeyword().isStrictKeyword());
    }

    /**
     * does this include statement represent a dependency on the version of perl the code is compatabile with.
     *
     * @return <code>true</code> if the include is for a version dependency, <code>false</code> otherwise
     *
     * @see    #getVersion()
     */
    public boolean isVersion()
    {
        return !getVersion().isNull();
    }

    // TODO: port PPI::Statement::Include::version_literal()
}
