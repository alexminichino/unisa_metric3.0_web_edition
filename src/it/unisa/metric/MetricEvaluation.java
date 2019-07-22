package it.unisa.metric;

import it.unisa.metric.metrics.A;
import it.unisa.metric.metrics.AC;
import it.unisa.metric.metrics.CBO;
import it.unisa.metric.metrics.CC;
import it.unisa.metric.metrics.Ca;
import it.unisa.metric.metrics.Ce;
import it.unisa.metric.metrics.DIT;
import it.unisa.metric.metrics.FI;
import it.unisa.metric.metrics.I;
import it.unisa.metric.metrics.LCOM;
import it.unisa.metric.metrics.LOC;
import it.unisa.metric.metrics.NC;
import it.unisa.metric.metrics.NCp;
import it.unisa.metric.metrics.NM;
import it.unisa.metric.metrics.NMS;
import it.unisa.metric.metrics.NOC;
import it.unisa.metric.metrics.RFC;
import it.unisa.metric.metrics.WMC;
import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;
import it.unisa.metric.struct.tree.CommentTree;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class MetricEvaluation {
	protected CommentTree tree;
	protected DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph;
	
	
	public MetricEvaluation(DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph, CommentTree tree){
		this.tree = tree;
		this.graph = graph;
	}
	
	public void metrics(){
		Utils.print("Starting analysis block");
		//Package informations
		stamPackageInfo();
		
		//Class informations
		stampClassInfo();
	
		//calcolo metriche
		
		//Metriche per i metodi
		stampComplexity();//complexity methods
		stampLoc();//loc methods
				
		//Metriche per le classi
		//DIT
		ditcEvaluation();
		//WMC
		wmccEvaluation();
		//NOC
		nocEvaluation();
		//RFC
		rfcEvaluation();	
		//CBO
		cboEvaluation();
		//LOC
		loccEvaluation();
		//NC
		ncEvaluation();
		//FI
		ficEvaluation();	
		//NMS
		nmsEvaluation();	
		//NM
		nmcEvaluation();	
		//LCOM
		lcomcEvaluation();
				
		//Metriche per i package
		//NCp
		ncpEvaluation();
		//DIT
		ditpEvaluation();
		//WMC
		wmcpEvaluation();
		//NM
		nmpEvaluation();
		//AC
		acEvaluation();
		//CC
		ccEvaluation();
		//Ce
		ceEvaluation();
		//Ca
		caEvaluation();
		//A
		aEvaluation();
		//I
		iEvaluation();
		//FI
		fipEvaluation();
		//LOC
		locpEvaluation();
		//LCOM
		lcompEvaluation();
		//NC
		ncpackageEvaluation();

		Utils.print("Analysis block ended");
	}

	protected void stampComplexity(){
		Utils.print("Complexity of each method");
		for(MethodVertex v : graph.vertexSet()){
			Utils.print("Class: "+v.getClassInfo().getName()+", Method: "+v.getName()+" Ciclomatic complexity: "+v.getComplexity());
		}
	}
	
	protected void stampLoc(){
		Utils.print("Loc of each method");
		for(MethodVertex v : graph.vertexSet()){
			Utils.print("Class: "+v.getClassInfo().getName()+", Method: "+v.getName()+" LOC: "+v.getLoc());
		}
	}
	
	protected void stampClassInfo(){
		Utils.print("Class info");
		ClassInfo ci = new ClassInfo();
		for(ClassInfo c : ci.getClassList()){
			Utils.print(c.toString());
		}
	}
	
	protected void ditcEvaluation(){
		ClassInfo ci = new ClassInfo();
		DIT dp = new DIT();
		Utils.print("DIT Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" DIT value: "+dp.classDit(c));
		}
	}
	
	protected void wmccEvaluation(){
		ClassInfo ci = new ClassInfo();
		WMC wp = new WMC();
		Utils.print("WMC Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" WMC value: "+wp.classWmc(c));
		}
	}
	
	protected void nocEvaluation(){
		ClassInfo ci = new ClassInfo();
		NOC wp = new NOC();
		Utils.print("NOC evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" NOC value: "+wp.classNoc(c));
		}
	}
	
	protected void cboEvaluation(){
		ClassInfo ci = new ClassInfo();
		CBO wp = new CBO();
		Utils.print("CBO evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" NOC value: "+wp.classCbo(c, graph));
		}
	}
	
	protected void loccEvaluation(){
		ClassInfo ci = new ClassInfo();
		LOC loc = new LOC();
		Utils.print("LOC Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" LOC value: "+loc.classLoc(c));
		}
	}
	
	protected void lcomcEvaluation(){
		ClassInfo ci = new ClassInfo();
		LCOM wp = new LCOM();
		Utils.print("LCOM evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" LCOM value: "+wp.classLcom(c));
		}
	}
	
	protected void ncpEvaluation(){
		PackageInfo pi = new PackageInfo();
		NCp wp = new NCp();
		Utils.print("NCp evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" NCp value: "+wp.packageNCp(p));
		}
	}
	
	
	protected void ceEvaluation() {
		PackageInfo pi = new PackageInfo();
		Ce ce= new Ce();
		Utils.print("Ce evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" Ce value: "+ce.packageCe(p, graph));
		}
		
	}
	protected void caEvaluation() {
		PackageInfo pi = new PackageInfo();
		Ca ca= new Ca();
		Utils.print("Ca evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" Ca value: "+ca.packageCa(p, graph));
		}
		
	}
	protected void ccEvaluation() {
		PackageInfo pi = new PackageInfo();
		CC cc= new CC();
		Utils.print("CC evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" CC value: "+cc.packageCc(p));
		}
		
	}
	protected void acEvaluation() {
		PackageInfo pi = new PackageInfo();
		AC ac= new AC();
		Utils.print("AC evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" AC value: "+ac.packageAc(p));
		}
		
	}
	protected void aEvaluation() {
		PackageInfo pi = new PackageInfo();
		A a= new A();
		Utils.print("A evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" A value: "+a.packageA(p));
		}
		
	}
	protected void iEvaluation() {
		PackageInfo pi = new PackageInfo();
		I i= new I();
		Utils.print("I evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" I value: "+i.packageI(p, graph));
		}
		
	}
	protected void ditpEvaluation(){
		PackageInfo pi = new PackageInfo();
		DIT dit = new DIT();
		Utils.print("DIT Package evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" DIT  value: "+dit.packageDit(p));
		}
	}
	protected void wmcpEvaluation(){
		PackageInfo pi = new PackageInfo();
		WMC wmc = new WMC();
		Utils.print("WMC Package evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" WMC value: "+wmc.packageWmc(p));
		}
	}
	protected void nmpEvaluation(){
		PackageInfo pi = new PackageInfo();
		NM nm = new NM();
		Utils.print("NM Package evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" NM value: "+nm.nmPackage(p));
		}
	}
	protected void nmcEvaluation(){
		ClassInfo ci = new ClassInfo();
		NM nm = new NM();
		Utils.print("NM Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" NM value: "+nm.nmClass(c));
		}
	}
	protected void rfcEvaluation(){
		ClassInfo ci = new ClassInfo();
		RFC rfc = new RFC();
		Utils.print("RFC Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" RFC value: "+rfc.classRfc(c, graph));
		}
	}
	protected void nmsEvaluation(){
		ClassInfo ci = new ClassInfo();
		NMS nms = new NMS();
		Utils.print("NMS Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" NMS value: "+nms.classNms(c, graph));
		}
	}
	protected void ficEvaluation(){
		ClassInfo ci = new ClassInfo();
		FI fi = new FI();
		Utils.print("FI Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			Utils.print("Class: "+c.getName()+" FI value: "+fi.classFi(c, graph));
		}
	}
	protected void fipEvaluation(){
		PackageInfo pi = new PackageInfo();
		FI fi = new FI();
		Utils.print("FI Package evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" FI value: "+fi.packageFi(p, graph));
		}
	}
	protected void locpEvaluation(){
		PackageInfo pi = new PackageInfo();
		LOC loc = new LOC();
		Utils.print("LOC Package evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" LOC value: "+loc.packageLoc(p));
		}
	}
	
	protected void lcompEvaluation(){
		PackageInfo pi = new PackageInfo();
		LCOM lcom = new LCOM();
		Utils.print("LCOM Package evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" LCOM value: "+lcom.packageLcom(p));
		}
	}
	
	protected void ncEvaluation(){
		ClassInfo ci = new ClassInfo();
		NC nc = new NC();
		Utils.print("NC Class evaluation");
		for(ClassInfo c : ci.getClassList()){
			if(!c.isAnonymous()){
				Utils.print("Class: "+c.getName()+" NC value: "+nc.ncClass(c, tree));
			}
		}
	}
	
	protected void ncpackageEvaluation(){
		PackageInfo pi = new PackageInfo();
		NC nc = new NC();
		Utils.print("NC Package evaluation");
		for(PackageInfo p : pi.getPackageList()){
			Utils.print("Package: "+p.getName()+" NC value: "+nc.ncPackage(p, tree));
		}
	}
	
	protected void stamPackageInfo(){
		Utils.print("Package info");
		PackageInfo pi = new PackageInfo();
		for(PackageInfo p : pi.getPackageList()){
			Utils.print(p.toString());
		}
	}

}
