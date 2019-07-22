package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
//method complete, class complete
public class LOC {
	
	public int methodLoc(MethodVertex v){
		return v.getLoc();
	}
	
	public int classLoc(ClassInfo classInf){
		return classInf.getLoc();
	}
	
	public int methodLocEvaluation(String method){		
		//organize string into array of string
		List<String> list = new LinkedList<String>(Arrays.asList(method.split("\n")));
				
		//blank lines are already not counted
		//remove javadoc (Comments are not read)
		boolean intoJavadocBlock = false;
		for(int i=0; i<list.size(); i++){
			if(intoJavadocBlock){
				if(list.get(i).contains("*/")){
					intoJavadocBlock =  false;
				}
				list.remove(i);
				i--;
			}else{
				if(list.get(i).contains("/**")){
					intoJavadocBlock = true;
					list.remove(i);
					i--;
				}
			}
		}
				
		return list.size();
	}
	
	
	public int classLocEvaluation(ASTNode node){
		
		String classCode = node.toString();
		
		//organize string into array of string
		List<String> list = new LinkedList<String>(Arrays.asList(classCode.split("\n")));
						
		//blank lines are already not counted
		//remove javadoc (Comments are not read)
		boolean intoJavadocBlock = false;
		for(int i=0; i<list.size(); i++){
			if(intoJavadocBlock){
				if(list.get(i).contains("*/")){
					intoJavadocBlock =  false;
				}
				list.remove(i);
				i--;
			}else{
				if(list.get(i).contains("/**")){
					intoJavadocBlock = true;
					list.remove(i);
					i--;
				}
			}
		}
		
		//remove nested classes
		for(int i=1; i<list.size(); i++){
			if(list.get(i).contains(" class ")){
				//found a class, starting procedure to delete the nested class
				Stack<String> trashHolder = new Stack<String>();
				trashHolder.push(list.get(i));
				int j=i+1;
				while(!trashHolder.isEmpty() && j< list.size()){
					if(list.get(j).contains("{")){
						if(!list.get(j).contains("}")){
							trashHolder.push(list.get(j));
						}
					}
					else if(list.get(j).contains("}")){
						trashHolder.pop();
					}
					j++;
				}
				//delete string from i to j
				for(int k=0; k<(j-i); k++){
					list.remove(i);
				}
				i--;
			}
		}
		
		
		CompilationUnit cu = (CompilationUnit) node.getRoot();
		
		//counting import and package declaration
		int importNumber = 0;
		int packageDeclared = 0;
		if(cu.types().size()>1){
			if(((AbstractTypeDeclaration)cu.types().get(0)).equals( node)){
				//add the import library and the package declaration only to the first class of the java file
				importNumber = cu.imports().size();
				if(cu.getPackage() != null){
					packageDeclared = 1;
				}
			}else{
				importNumber = 0;
				packageDeclared = 0;
			}
		}else{
			importNumber = cu.imports().size();
			if(cu.getPackage() != null){
				packageDeclared = 1;
			}
		}		
	
		return (list.size() + importNumber + packageDeclared);
	}
	
	public int packageLoc(PackageInfo packInf){
		int loc = 0;
		for(ClassInfo classInf : packInf.getClasses()){
			loc = loc + classInf.getLoc();
		}
		return loc;
	}
}
