package org.scriptkitty.ppi4j.visitor;

import org.scriptkitty.ppi4j.Document;
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


public interface INodeVisitor
{
    //~ Methods

    void visit(BlockStructure struct);

    void visit(BreakStatement stmt);

    void visit(CompoundStatement stmt);

    void visit(ConditionStructure struct);

    void visit(ConstructorStructure struct);

    void visit(DataStatement stmt);

    void visit(Document document);

    void visit(EndStatement stmt);

    void visit(ExpressionStatement stmt);

    void visit(ForLoopStructure struct);

    void visit(GivenStatement stmt);

    void visit(GivenStructure struct);

    void visit(IncludeStatement stmt);

    void visit(ListStructure struct);

    void visit(Node node);

    void visit(NullStatement stmt);

    void visit(PackageStatement stmt);

    void visit(Perl6IncludeStatement stmt);

    void visit(ScheduledStatement stmt);

    void visit(Statement stmt);

    void visit(Structure struct);

    void visit(SubscriptStructure struct);

    void visit(SubStatement stmt);

    void visit(Token token);

    void visit(UnmatchedBrace stmt);

    void visit(VariableStatement stmt);

    void visit(WhenStatement stmt);

    void visit(WhenStructure struct);
}
