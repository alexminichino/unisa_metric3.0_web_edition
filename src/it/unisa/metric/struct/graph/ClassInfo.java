package it.unisa.metric.struct.graph;

import it.unisa.metric.metrics.LOC;

import java.util.ArrayList;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ClassInfo {
	
	//declaration of static variable
	private static ArrayList<ClassInfo> classList = new ArrayList<ClassInfo>(); //static list for the class info
	private static boolean updated = false; //static boolean to check if the list has been updated or not
	
	//declaration of instance variable
	public static final int ANONYMOUS_CLASS_DECLARATION = 1;
	private ITypeBinding classBinding;
	private ASTNode node;//this info is not needed but it's here for future requirements
	private String name;
	private int loc;//class loc
	private boolean inter = false;;
	private boolean abs = false;
	private ArrayList<ClassInfo> children = null;
	private ClassInfo parent = null;
	private int complexity = 0;//it gets updated after the graph is built
	private PackageInfo packageInf = null;
	private ArrayList<MethodVertex> methods;
	private boolean anonymous = false;
	
	//constructor used to access the classList
	public ClassInfo(){
		
	}
	
	//constructor that effectively create a ClassInfo object
	public ClassInfo(ITypeBinding classBinding, ASTNode node){
		//set the class binding
		this.classBinding = classBinding;
		
		//set the compilation unit
		this.node = node;
		
		//analize modifieres
		if(Flags.isAbstract(classBinding.getModifiers())){
			abs=true;
		}
		if(classBinding.isInterface()){
			inter=true;
		}
		
		if(node.getNodeType()==ANONYMOUS_CLASS_DECLARATION){
			//AnonymousClassDeclaration node
			//set the name of the class as ANONYMOUS
			name="ANONYMOUS";
			anonymous = true;
			
			/*
			//set the name of the class as the class interface name 
			for(ITypeBinding interf : classBinding.getInterfaces()){
				//check the interfaces
			}
			*/
		}else{
			//TypeDeclaration node
			//set class name
			name = classBinding.getQualifiedName();
		}

		//inizializzo children
		children = new ArrayList<ClassInfo>();
		
		//calcola loc di classe
		LOC cloc = new LOC();	
		loc = cloc.classLocEvaluation(node);
		
		//add package info
		
		CompilationUnit cu = (CompilationUnit) node.getRoot();
		
		//check if package already exist into packagelist
		PackageInfo pi = new PackageInfo();
		if(cu.getPackage() != null){
			for(PackageInfo pack : pi.getPackageList()){
				if(pack.getPackageDeclaration().getName().toString().equals(cu.getPackage().getName().toString())){
					//package already exist in the list
					packageInf = pack;
				}
			}
		}
		
		
		//the package was not found in the package list
		if(packageInf == null){
			//create the new package
			packageInf = new PackageInfo(cu.getPackage());
		}
		
		//add the class to the package list
		packageInf.addClass(this);
		
		
		//inizialize arraylist of methods
		methods = new ArrayList<MethodVertex>();
		
		//aggiungo la classe alla lista delle classi
		classList.add(this);
	}
	
	private void addChildren(ClassInfo child){
		children.add(child);
	}
	
	public void setLoc(int loc){
		this.loc=loc;
	}
	
	public ITypeBinding getClassBinding(){
		return classBinding;
	}

	public boolean isAbstract(){
		return abs;
	}
	
	public boolean isInterface(){
		return inter;
	}
	
	public int getLoc(){
		return loc;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<ClassInfo> getChildren(){
		return children;
	}
	
	public ClassInfo getParent(){
		return parent;
	}
	
	// if not interface and not abstract, the class is concrete.
	public boolean isConcrete(){
		return (!inter && !abs);
	}
	
	/**
	 * Find the parent in the ClassInfo List.
	 * @param binding
	 */
	public void findParent(ITypeBinding superClass){
		for(int i=0; i<classList.size(); i++){
			if(superClass.getQualifiedName().equals(classList.get(i).getClassBinding().getQualifiedName())){
				//parent found
				//add the parent referral
				parent = classList.get(i);				
				
				//add child to the parent
				classList.get(i).addChildren(this);
			}
		}
		return;
	}
	
	public boolean isSuperClass(){
		if(children.isEmpty()){
			//is subclass
			return false;
		}else{
			//is superclass
			return true;
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(packageInf.getPackageDeclaration() != null){
			sb.append("Package: "+packageInf.getPackageDeclaration().getName());
			
		}else{
			sb.append("Package: not declared");
		}
		
		sb.append(" Class Name: "+name+" [");
		
		if(isAbstract()){
			sb.append(" Abstract ");
		}
		if(isInterface()){
			sb.append(" Interface ");
		}
		if(isSuperClass()){
			sb.append(" Superclass ");
		}
		if(isConcrete()){
			sb.append(" Concrete ");
		}
		sb.append("] ");
		if(!getChildren().isEmpty()){
			sb.append("Children: ");
			for(ClassInfo child : this.getChildren()){
				sb.append("<"+child.getName()+">");
			}
		}
		if(getParent() != null){
			sb.append("Parent: "+this.getParent().getName()+" ");
		}
		return sb.toString();
	}
	
	public void setComplexity(int complexity){
		this.complexity=complexity;
	}
	
	public int getComplexity(){
		return complexity;
	}
	
	public ArrayList<ClassInfo> getClassList(){
		return classList;
	}
	
	public void update(){
		if(!updated){
			for(ClassInfo ci : classList){
				//interfaces cannot be extended they implements other classes
				if(!ci.isInterface()){
					ci.findParent(ci.getClassBinding().getSuperclass());
				}
			}
			updated=true;
			//libero il class binding
			classBinding = null;
		}
	}
	
	public PackageInfo getPackage(){
		return packageInf;
	}
	
	public ASTNode getClassASTNode(){
		return node;
	}
	
	public ArrayList<MethodVertex> getMethods(){
		return methods;
	}
	
	public void addMethod(MethodVertex v){
		methods.add(v);
		return;
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}

}