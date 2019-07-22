package it.unisa.metric.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class StatementVisitor extends ASTVisitor{

	private int complexity = 1;
	
	
	public boolean visit(SwitchCase node){
		complexity++;
		return true;
	}
	
	public boolean visit(IfStatement node){
		complexity++;
		return true;
	}
	
	public boolean visit(WhileStatement node){
		complexity++;
		return true;
	}
	
	public boolean visit(ForStatement node){
		complexity++;
		return true;
	}
	
	public boolean visit(DoStatement node){
		complexity++;
		return true;
	}
	
	public boolean visit(TryStatement node){
		complexity++;
		return true;
	}
	
	public int getComplexity(){
		return complexity;
	}
}
