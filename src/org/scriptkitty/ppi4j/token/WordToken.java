package org.scriptkitty.ppi4j.token;

import org.scriptkitty.perl.lang.General;
import org.scriptkitty.perl.lang.Words;

import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.statement.BreakStatement;
import org.scriptkitty.ppi4j.statement.IncludeStatement;
import org.scriptkitty.ppi4j.statement.PackageStatement;
import org.scriptkitty.ppi4j.statement.SubStatement;
import org.scriptkitty.ppi4j.util.ElementUtils;


/**
 * a <code>WordToken</code> represents all 'word' tokens.
 *
 * <p>in addition to barewords, all other valid words (core functions, those that include <code>::</code> separators)
 * are included here.</p>
 *
 * <p>many of the boolean methods in this class were inspired by <code>Perl::Critic::Utils</code>.</p>
 *
 * @see <a href="http://search.cpan.org/dist/PPI/lib/PPI/Token/Word.pm">CPAN - PPI::Token::Word</a>
 * @see <a href="http://search.cpan.org/dist/Perl-Critic/lib/Perl/Critic/Utils.pm">Perl::Critic::Utils</a>
 */
public class WordToken extends Token
{
    //~ Methods

    /**
     * get the literal value of the token
     *
     * <p>qualified names that use a <code>'</code> as a delimiter (deprecated) are expanded to <code>::</code>, ie:
     * <code>Foo'Bar</code> becomes <code>Foo::Bar</code>.</p>
     *
     * @return literal value
     *
     * @see    #getContent()
     */
    public String getLiteral()
    {
        return getContent().replace("'", "::");
    }

    /**
     * does this token contain a module component, ie: <code>::</code>?
     *
     * @return <code>true</code> if contains a module component, <code>false</code> otherwise
     */
    public boolean hasQualifiedName()
    {
        return General.isQualified(getContent());
    }

    /**
     * does this token represent a 'bareword'?
     *
     * @return <code>true</code> if a bareword, <code>false</code> otherwise
     */
    public boolean isBareword()
    {
        return Words.isBareword(getContent());
    }

    /**
     * does this token represent builtin function?
     *
     * @return <code>true</code> if a builtin, <code>false</code> otherwise
     */
    public boolean isBuiltin()
    {
        return Words.isBuiltin(getContent());
    }

    /**
     * does this token represent builtin function whose arguments are a list?
     *
     * @return <code>true</code> if a builtin with list args, <code>false</code> otherwise
     */
    public boolean isBuiltinWithListContext()
    {
        return Words.isBuiltinWithListContext(getContent());
    }

    /**
     * does this token represent builtin function that can take multiple arguments?
     *
     * @return <code>true</code> if a builtin that can take multiple args, <code>false</code> otherwise
     */
    public boolean isBuiltinWithMultipleArgs()
    {
        return Words.isBuiltinWithMultipleArgs(getContent());
    }

    /**
     * does this token represent builtin function that takes no arguments?
     *
     * @return <code>true</code> if a builtin that takes no args, <code>false</code> otherwise
     */
    public boolean isBuiltinWithNoArgs()
    {
        return Words.isBuiltinWithNoArgs(getContent());
    }

    /**
     * does this token represent builtin function that takes one argument?
     *
     * @return <code>true</code> if a builtin that takes one arg, <code>false</code> otherwise
     */
    public boolean isBuiltinWithOneArg()
    {
        return Words.isBuiltinWithOneArg(getContent());
    }

    /**
     * does this token represent builtin function that takes no more than one argument?
     *
     * @return <code>true</code> if a builtinthat takes no more than one arg, <code>false</code> otherwise
     */
    public boolean isBuiltinWithOptionalArg()
    {
        return Words.isBuiltinWithOptionalArg(getContent());
    }

    /**
     * does this token represent builtin function that zero and/or one argument?
     *
     * @return <code>true</code> if a builtin that takes zero and/or one arg, <code>false</code> otherwise
     */
    public boolean isBuiltinWithZeroOrOneArgs()
    {
        return Words.isBuiltinWithZeroOrOneArgs(getContent());
    }

    /**
     * does this token represent a class name?
     *
     * <p>if the token that immediately follows this one is the deference operator, <code>-&gt;</code>, it usually means
     * it is the name of a class.</p>
     *
     * @return <code>true</code> if class name, <code>false</code> otherwise
     */
    public boolean isClassName()
    {
        return (ElementUtils.isDashArrowOperatorToken(getNextSignificantSibling()) && !isMethodCall());
    }

    /**
     * does this token represent a compound keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isCompoundKeyword()
    {
        return Words.isCompoundKeyword(getContent());
    }

    /**
     * does this token represent a conditional keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isConditionalKeyword()
    {
        return Words.isConditionalKeyword(getContent());
    }

    /**
     * does this token represent the <code>continue</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isContinueKeyword()
    {
        return Words.isContinueKeyword(getContent());
    }

    /**
     * does this token represent a control keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isControlKeyword()
    {
        return Words.isControlKeyword(getContent());
    }

    /**
     * does this token represent the <code>else</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isElseKeyword()
    {
        return Words.isElseKeyword(getContent());
    }

    /**
     * does this token represent the <code>elsif</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isElsIfKeyword()
    {
        return Words.isElsIfKeyword(getContent());
    }

    /**
     * does this token represent a global file handle?
     *
     * <p>note: if the file handle is represented as a typeglob (ie: <code>*STDIN</code>, this method will return <code>
     * false</code>.</p>
     *
     * @return <code>true</code> if a file handle, <code>false</code> otherwise
     */
    public boolean isFileHandle()
    {
        return Words.isFileHandle(getContent());
    }

    /**
     * does this token represent the <code>foreach</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isForeachKeyword()
    {
        return Words.isForeachKeyword(getContent());
    }

    /**
     * does this token represent the <code>for</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isForKeyword()
    {
        return Words.isForKeyword(getContent());
    }

    /**
     * does this token represent a static method call?
     *
     * @return <code>true</code> if a method call, <code>false</code> otherwise
     */
    public boolean isFunctionCall()
    {
        return (!(isHashKey() || isMethodCall() || isClassName() || isSubroutineName() || isIncludedModuleName() ||
                isPackageDeclaration() || isBareword() || isFileHandle() || isLabelPointer()));
    }

    public boolean isIfKeyword()
    {
        return Words.isIfKeyword(getContent());
    }

    /**
     * does this token represent the name of a module being included?
     *
     * @return <code>true</code> if included module name, <code>false</code> otherwise
     */
    public boolean isIncludedModuleName()
    {
        if (parentStatementIs(IncludeStatement.class))
        {
            return (((IncludeStatement) getParentStatement()).getSigChild(1) == this);
        }

        return false;
    }

    /**
     * does this token represnt a label in a statement?
     *
     * <p>note: this is not the same thing as the label declaration, ie: <code>LABEL: if (...)</code></p>
     *
     * @return <code>true</code> if a label pointer, <code>false</code> otherwise
     */
    public boolean isLabelPointer()
    {
        Statement stmt = getParentStatement();
        Element prev = getPrevSignificantSibling();

        if ((stmt instanceof BreakStatement) && ElementUtils.isControlWordToken(prev))
        {
            return true;
        }

        return false;
    }

    /**
     * does this token represent a loop keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isLoopKeyword()
    {
        return Words.isLoopKeyword(getContent());
    }

    /**
     * does this token represent a method call?
     *
     * <p>if this token immediately follows the deference operator, <code>-&lt;</code>, it usually means it is the name
     * of a method.</p>
     *
     * @return <code>true</code> if a method call, <code>false</code> otherwise
     */
    public boolean isMethodCall()
    {
        return ElementUtils.isDashArrowOperatorToken(getPrevSignificantSibling());
    }

    /**
     * does this token represent the <code>no</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isNoKeyword()
    {
        return Words.isNoKeyword(getContent());
    }

    /**
     * does this token represent the name of a package.
     *
     * @return <code>true</code> if package name, <code>false</code> otherwise
     */
    public boolean isPackageDeclaration()
    {
        if (parentStatementIs(PackageStatement.class))
        {
            return (((PackageStatement) getParentStatement()).getSigChild(1) == this);
        }

        return false;
    }

    public boolean isPackageKeyword()
    {
        return Words.isPackageKeyword(getContent());
    }

    /**
     * does this token represent a scheduled keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isScheduledKeyword()
    {
        return Words.isScheduledKeyword(getContent());
    }

    /**
     * does this token represent the <code>sub</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isSubKeyword()
    {
        return Words.isSubKeyword(getContent());
    }

    /**
     * does this token represent the name of a subroutine.
     *
     * @return <code>true</code> if subroutine name, <code>false</code> otherwise
     */
    public boolean isSubroutineName()
    {
        Element prev = getPrevSignificantSibling();

        if ((getParentStatement() instanceof SubStatement) && ElementUtils.isSubWordToken(prev))
        {
            return true;
        }

        return false;
    }

    /**
     * does this token represent the <code>unless</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isUnlessKeyword()
    {
        return Words.isUnlessKeyword(getContent());
    }

    /**
     * does this token represent the <code>until</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isUntilKeyword()
    {
        return Words.isUntilKeyword(getContent());
    }

    /**
     * does this token represent the <code>use</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isUseKeyword()
    {
        return Words.isUseKeyword(getContent());
    }

    /**
     * does this token represent the <code>v6</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isV6Keyword()
    {
        return Words.isV6Keyword(getContent());
    }

    /**
     * does this token represent a variable keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isVariableKeyword()
    {
        return Words.isVariableKeyword(getContent());
    }

    /**
     * does this token represent the <code>while</code> keyword?
     *
     * @return <code>true</code> if keyword, <code>false</code> otherwise
     */
    public boolean isWhileKeyword()
    {
        return Words.isWhileKeyword(getContent());
    }
}
