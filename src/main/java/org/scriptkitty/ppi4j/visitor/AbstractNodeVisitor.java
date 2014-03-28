package org.scriptkitty.ppi4j.visitor;

import java.util.List;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Element;
import org.scriptkitty.ppi4j.Node;
import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.statement.BreakStatement;
import org.scriptkitty.ppi4j.statement.CompoundStatement;
import org.scriptkitty.ppi4j.statement.DataStatement;
import org.scriptkitty.ppi4j.statement.EndStatement;
import org.scriptkitty.ppi4j.statement.ExpressionStatement;
import org.scriptkitty.ppi4j.statement.GivenStatement;
import org.scriptkitty.ppi4j.statement.IncludeStatement;
import org.scriptkitty.ppi4j.statement.NullStatement;
import org.scriptkitty.ppi4j.statement.PackageStatement;
import org.scriptkitty.ppi4j.statement.Perl6IncludeStatement;
import org.scriptkitty.ppi4j.statement.ScheduledStatement;
import org.scriptkitty.ppi4j.statement.SubStatement;
import org.scriptkitty.ppi4j.statement.UnmatchedBrace;
import org.scriptkitty.ppi4j.statement.VariableStatement;
import org.scriptkitty.ppi4j.statement.WhenStatement;
import org.scriptkitty.ppi4j.structure.BlockStructure;
import org.scriptkitty.ppi4j.structure.ConditionStructure;
import org.scriptkitty.ppi4j.structure.ConstructorStructure;
import org.scriptkitty.ppi4j.structure.ForLoopStructure;
import org.scriptkitty.ppi4j.structure.GivenStructure;
import org.scriptkitty.ppi4j.structure.ListStructure;
import org.scriptkitty.ppi4j.structure.SubscriptStructure;
import org.scriptkitty.ppi4j.structure.WhenStructure;


public abstract class AbstractNodeVisitor implements INodeVisitor
{
    //~ Instance fields

    private int level;

    //~ Methods

    @Override public abstract void visit(Token token);

    @Override public void visit(BlockStructure struct)
    {
        visit((Structure) struct);
    }

    @Override public void visit(BreakStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(CompoundStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(ConditionStructure struct)
    {
        visit((Structure) struct);
    }

    @Override public void visit(ConstructorStructure struct)
    {
        visit((Structure) struct);
    }

    @Override public void visit(DataStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(Document document)
    {
        visit((Node) document);
    }

    @Override public void visit(EndStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(ExpressionStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(ForLoopStructure struct)
    {
        visit((Structure) struct);
    }

    @Override public void visit(GivenStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(GivenStructure struct)
    {
        visit((Structure) struct);
    }

    @Override public void visit(IncludeStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(ListStructure struct)
    {
        visit((Structure) struct);
    }

    @Override public void visit(Node node)
    {
        visitChildren(node);
    }

    @Override public void visit(NullStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(PackageStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(Perl6IncludeStatement stmt)
    {
        visit((IncludeStatement) stmt);
    }

    @Override public void visit(ScheduledStatement stmt)
    {
        visit((SubStatement) stmt);
    }

    @Override public void visit(Statement stmt)
    {
        visit((Node) stmt);
    }

    @Override public void visit(Structure struct)
    {
        visit((Node) struct);
    }

    @Override public void visit(SubscriptStructure struct)
    {
        visit((Structure) struct);
    }

    @Override public void visit(SubStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(UnmatchedBrace stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(VariableStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(WhenStatement stmt)
    {
        visit((Statement) stmt);
    }

    @Override public void visit(WhenStructure struct)
    {
        visit((Structure) struct);
    }

    protected final int getLevel()
    {
        return level;
    }

    protected List<Element> getToVisit(Node node)
    {
        return node.getChildren();
    }

    protected final void visitChildren(Node node)
    {
        level++;

        for (Element child : getToVisit(node))
        {
            child.accept(this);
        }

        level--;
    }
}
