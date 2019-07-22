package it.unisa.metric.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class SimpleNameVisitor extends ASTVisitor{
	
	private IVariableBinding[] classFieldDeclaration;
	private ArrayList<IVariableBinding> fieldAccessed = new ArrayList<IVariableBinding>();
	
	public SimpleNameVisitor(IVariableBinding[] fieldDeclaration ){
		this.classFieldDeclaration = fieldDeclaration;
	}

	public boolean visit(SimpleName node){
		if(!node.toString().equals("DEFAULT_CLASS_NAME")){
			if(!node.isDeclaration()){
				for(IVariableBinding vb : classFieldDeclaration){
					if(vb.getName().toString().equals(node.toString())){
						fieldAccessed.add(vb);
						break;
					}
				}
			}
		}
		return true;
	}
	
	public ArrayList<IVariableBinding> getFieldAccessed(){
		return fieldAccessed;
	}
}
