package it.unisa.metric.struct.graph;

import it.unisa.metric.metrics.LOC;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.PackageDeclaration;

public class PackageInfo {

	//declaration of static field
	private static ArrayList<PackageInfo> packageList = new ArrayList<PackageInfo>(); //static list for the package info
	
	//declaration of instance field
	PackageDeclaration packageDeclaration;
	ArrayList<ClassInfo> classes;
	int loc = 0;
	
	//constructor used to access the packageList
	public PackageInfo(){
		
	}
	
	public PackageInfo(PackageDeclaration packageDeclaration){
		
		this.packageDeclaration = packageDeclaration;
		
		classes = new ArrayList<ClassInfo>();
		
		packageList.add(this);
	}
	
	public PackageDeclaration getPackageDeclaration(){
		return packageDeclaration;
	}
	
	public ArrayList<ClassInfo> getClasses(){
		return classes;
	}
	
	public void addClass(ClassInfo classInf){
		classes.add(classInf);
	}
	
	public ArrayList<PackageInfo> getPackageList(){
		return packageList;
	}
	
	public String getName(){
		if(packageDeclaration != null){
			return packageDeclaration.getName().toString();
		}else{
			return "Not declared";
		}
	}
	
	public int getLoc(){
		return loc;
	}
	
	public void calculateLoc(){
		LOC loc = new LOC();
		this.loc=loc.packageLoc(this);
	}
	
	public void update(){
		for(PackageInfo pack : packageList){
			pack.calculateLoc();
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		 sb.append("PackageName: "+packageDeclaration.getName().toString());
		 sb.append(", LOC: "+loc);
		 sb.append(", Number of class in the package: "+classes.size());
		
		return sb.toString();
	}
}
