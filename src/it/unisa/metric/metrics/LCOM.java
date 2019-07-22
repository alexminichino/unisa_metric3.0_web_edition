package it.unisa.metric.metrics;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;
import it.unisa.metric.visitor.SimpleNameVisitor;
//
public class LCOM {
	
	public long classLcom(ClassInfo classInf){
		long notRelated = 0;
		long related = 0;
		
		for(int i=0; i<classInf.getMethods().size(); i++){
			MethodVertex methodFixed = classInf.getMethods().get(i);
			if(methodFixed.getMethodFieldAccessed() != null){
				Stack<MethodVertex> alreadyMatched = new Stack<MethodVertex>();
				for(IVariableBinding fixField : methodFixed.getMethodFieldAccessed()){
					for(int j=i+1; j<classInf.getMethods().size(); j++){
						if(!alreadyMatched.contains(classInf.getMethods().get(j))){
							MethodVertex methodConfronting = classInf.getMethods().get(j);
							if(methodConfronting.getMethodFieldAccessed() != null){
								for(IVariableBinding confField : methodConfronting.getMethodFieldAccessed()){
									//System.out.println("QQ CONFRONTING METHOD: "+methodFixed.getName()+" AND "+methodConfronting+" FIX FIELD: "+fixField+" CONFRONTING FIELD: "+confField);
									if(fixField.equals(confField)){
										alreadyMatched.push(methodConfronting);
										//System.out.println("KK CONFRONT SUCCESS");
										break;
									}
								}
							}
						}
					}
				}
				//System.out.println("KK "+classInf.getMethods().size()+" - "+(i+1)+ " - "+alreadyMatched.size());
				notRelated = notRelated + ((classInf.getMethods().size() - i - 1 - alreadyMatched.size()));
			}else{
				//System.out.println("LL "+classInf.getMethods().size()+" - "+(i+1));
				notRelated = notRelated + (classInf.getMethods().size() - i - 1);
			}
		}
		
		related = Math.round(numberOfCouples(classInf.getMethods().size()) - notRelated);
		return notRelated - related;
	}
	
	public long packageLcom(PackageInfo packInf){
		long lcomP = 0;
		
		for(ClassInfo classInf : packInf.getClasses()){
			lcomP = lcomP + classLcom(classInf);
		}
	
		return lcomP;
	}
	
	public ArrayList<IVariableBinding> checkFieldUsage(CompilationUnit cu, IMethodBinding method, IVariableBinding[] fieldDeclaration){
		//start
		MethodDeclaration methodNode = (MethodDeclaration) cu.findDeclaringNode(method);
		
		SimpleNameVisitor snv = new SimpleNameVisitor(fieldDeclaration);
		
		//check default object constructor init()
		if(methodNode != null){
			
			ASTParser parser = ASTParser.newParser(AST.JLS9);
			
			parser.setSource(("public class DEFAULT_CLASS_NAME {"+methodNode.toString()+"}").toCharArray());
			
			parser.setKind(ASTParser.K_COMPILATION_UNIT);

			CompilationUnit compilation = (CompilationUnit) parser.createAST(null);	
							
			compilation.accept(snv);
		}
	
		//end 
		
		return snv.getFieldAccessed();
	}
	
	private double numberOfCouples(int numberOfMethods){
		if(numberOfMethods>0){
			return factorial(numberOfMethods)/(2*factorial(numberOfMethods - 2));
		}else{
			return -1;
		}
		
	}
	
	private double factorial(int number){
		double result = 1;
		for(double i=number; i>0; i--){
			result = result * i;
		}
		return result;
	}
}
