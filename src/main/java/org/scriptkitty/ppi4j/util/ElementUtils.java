package org.scriptkitty.ppi4j.util;

import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;


/**
 * set of utility methods useful when dealing with objects as the base <code>Element</code> type.
 */
public class ElementUtils
{
    //~ Methods

    public static boolean isCloseBraceToken(Element element)
    {
        return (isCloseSquare(element) || isCloseCurlyToken(element) || isCloseParenToken(element));
    }

    public static boolean isCloseCurlyToken(Element element)
    {
        return (element instanceof StructureToken) && ((StructureToken) element).isCloseCurly();
    }

    public static boolean isCloseParenToken(Element element)
    {
        return (element instanceof StructureToken) && ((StructureToken) element).isCloseParen();
    }

    public static boolean isCloseSquare(Element element)
    {
        return (element instanceof StructureToken) && ((StructureToken) element).isCloseSquare();
    }

    public static boolean isCompoundWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isCompoundKeyword();
    }

    public static boolean isConditionalWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isConditionalKeyword();
    }

    public static boolean isContinueWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isContinueKeyword();
    }

    public static boolean isControlWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isControlKeyword();
    }

    public static boolean isDashArrowOperatorToken(Element element)
    {
        return (element instanceof OperatorToken) && ((OperatorToken) element).isDashArrow();
    }

    public static boolean isElseWordToken(Element element)
    {
        return ((element instanceof WordToken) && ((WordToken) element).isElseKeyword());
    }

    public static boolean isElsIfWordToken(Element element)
    {
        return ((element instanceof WordToken) && ((WordToken) element).isElsIfKeyword());
    }

    public static boolean isEmptyWhitespace(Element element)
    {
        return ((element instanceof WhitespaceToken) && ((WhitespaceToken) element).isEmptyWhitespace());
    }

    public static boolean isEqualArrowOperatorToken(Element element)
    {
        return ((element instanceof OperatorToken) && ((OperatorToken) element).isEqualArrow());
    }

    public static boolean isForeachWordToken(Element element)
    {
        return ((element instanceof WordToken) && ((WordToken) element).isForeachKeyword());
    }

    public static boolean isForWordToken(Element element)
    {
        return ((element instanceof WordToken) && ((WordToken) element).isForKeyword());
    }

    public static boolean isIfWordToken(Element element)
    {
        return ((element instanceof WordToken) && ((WordToken) element).isIfKeyword());
    }

    public static boolean isLoopWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isLoopKeyword();
    }

    public static boolean isNoWordToken(Element element)
    {
        return ((element instanceof WordToken) && ((WordToken) element).isNoKeyword());
    }

    public static boolean isOpenBraceToken(Element element)
    {
        return (isOpenSquareToken(element) || isOpenCurlyToken(element) || isOpenParenToken(element));
    }

    public static boolean isOpenCurlyToken(Element element)
    {
        return (element instanceof StructureToken) && ((StructureToken) element).isOpenCurly();
    }

    public static boolean isOpenParenToken(Element element)
    {
        return (element instanceof StructureToken) && ((StructureToken) element).isOpenParen();
    }

    public static boolean isOpenSquareToken(Element element)
    {
        return (element instanceof StructureToken) && ((StructureToken) element).isOpenSquare();
    }

    public static boolean isPackageWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isPackageKeyword();
    }

    public static boolean isScheduledWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isScheduledKeyword();
    }

    public static boolean isSemiColonToken(Element element)
    {
        return (element instanceof StructureToken) && ((StructureToken) element).isSemiColon();
    }

    public static boolean isSubWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isSubKeyword();
    }

    public static boolean isUntilWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isUntilKeyword();
    }

    public static boolean isUseWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isUseKeyword();
    }

    public static boolean isV6WordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isV6Keyword();
    }

    public static boolean isVariableWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isVariableKeyword();
    }

    public static boolean isWhileWordToken(Element element)
    {
        return (element instanceof WordToken) && ((WordToken) element).isWhileKeyword();
    }
}
