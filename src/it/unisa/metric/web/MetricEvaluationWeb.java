package it.unisa.metric.web;


import it.unisa.metric.MetricEvaluation;
import it.unisa.metric.Utils;
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

import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import com.google.gson.Gson;


public class MetricEvaluationWeb extends MetricEvaluation{
	private ArrayList<MetricResultsMean> totalResults;
	public MetricEvaluationWeb(DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph, CommentTree tree) {
		super(graph, tree);
		totalResults= new ArrayList<>();
	}
	
	@Override
	protected void stampComplexity(){
		Utils.print("Complexity of each method");
		MetricResultsMean results = new MetricResultsMean("Ciclomatic", MetricLevel.METHOD);
		for(MethodVertex v : graph.vertexSet()){
			MetricResult result = new MetricResult(v.getClassInfo().getName(), v.getName(), v.getComplexity());
			Utils.print("Class: "+result.getClassName()+", Method: "+result.getMethodName()+" Ciclomatic complexity: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void stampLoc(){
		Utils.print("Loc of each method");
		MetricResultsMean results = new MetricResultsMean("LOC", MetricLevel.METHOD);
		for(MethodVertex v : graph.vertexSet()){
			MetricResult result = new MetricResult(v.getClassInfo().getName(), v.getName(), v.getLoc());
			Utils.print("Class: "+result.getClassName()+", Method: "+result.getMethodName()+" LOC: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
//	@Override
//	protected void stampClassInfo(){
//		Utils.print("Class info");
//		ClassInfo ci = new ClassInfo();
//		for(ClassInfo c : ci.getClassList()){
//			Utils.print(c.toString());
//		}
//	}
	
	@Override
	protected void ditcEvaluation(){
		ClassInfo ci = new ClassInfo();
		DIT dp = new DIT();
		Utils.print("DIT Class evaluation");
		MetricResultsMean results = new MetricResultsMean("DIT", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(),dp.classDit(c));
			Utils.print("Class: "+result.getClassName()+" DIT value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void wmccEvaluation(){
		ClassInfo ci = new ClassInfo();
		WMC wp = new WMC();
		Utils.print("WMC Class evaluation");
		MetricResultsMean results = new MetricResultsMean("WMC", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(), wp.classWmc(c));
			Utils.print("Class: "+result.getClassName()+" WMC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void nocEvaluation(){
		ClassInfo ci = new ClassInfo();
		NOC wp = new NOC();
		Utils.print("NOC evaluation");
		MetricResultsMean results = new MetricResultsMean("NOC", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(), wp.classNoc(c));
			Utils.print("Class: "+result.getClassName()+" NOC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void cboEvaluation(){
		ClassInfo ci = new ClassInfo();
		CBO wp = new CBO();
		Utils.print("CBO evaluation");
		MetricResultsMean results = new MetricResultsMean("CBO", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(), wp.classCbo(c, graph));
			Utils.print("Class: "+result.getClassName()+" CBO value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void loccEvaluation(){
		ClassInfo ci = new ClassInfo();
		LOC loc = new LOC();
		Utils.print("LOC Class evaluation");
		MetricResultsMean results = new MetricResultsMean("LOC", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(),loc.classLoc(c));
			Utils.print("Class: "+result.getClassName()+" LOC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void lcomcEvaluation(){
		ClassInfo ci = new ClassInfo(); 
		LCOM wp = new LCOM();
		Utils.print("LCOM evaluation");
		MetricResultsMean results = new MetricResultsMean("LCOM", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(),(int)wp.classLcom(c));
			Utils.print("Class: "+result.getClassName()+" LCOM value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void ncpEvaluation(){
		PackageInfo pi = new PackageInfo();
		NCp wp = new NCp();
		Utils.print("NCp evaluation");
		MetricResultsMean results = new MetricResultsMean("NCp",MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,wp.packageNCp(p));
			Utils.print("Package: "+result.getPackageName()+" NCp value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void ceEvaluation() {
		PackageInfo pi = new PackageInfo();
		Ce ce= new Ce();
		Utils.print("Ce evaluation");
		MetricResultsMean results = new MetricResultsMean("Ce", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,ce.packageCe(p, graph));
			Utils.print("Package: "+result.getPackageName()+" Ce value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void caEvaluation() {
		PackageInfo pi = new PackageInfo();
		Ca ca= new Ca();
		Utils.print("Ca evaluation");
		MetricResultsMean results = new MetricResultsMean("Ca", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,ca.packageCa(p, graph));
			Utils.print("Package: "+result.getPackageName()+" Ca value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	@Override
	protected void ccEvaluation() {
		PackageInfo pi = new PackageInfo();
		CC cc= new CC();
		Utils.print("CC evaluation");
		MetricResultsMean results = new MetricResultsMean("CC", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,cc.packageCc(p));
			Utils.print("Package: "+result.getPackageName()+" CC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	
	protected void acEvaluation() {
		PackageInfo pi = new PackageInfo();
		AC ac= new AC();
		Utils.print("AC evaluation");
		MetricResultsMean results = new MetricResultsMean("AC", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,ac.packageAc(p));
			Utils.print("Package: "+result.getPackageName()+" AC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void aEvaluation() { 
		PackageInfo pi = new PackageInfo();
		A a= new A();
		Utils.print("A evaluation");
		MetricResultsMean results = new MetricResultsMean("A", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null, (int) a.packageA(p));
			Utils.print("Package: "+result.getPackageName()+" A value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void iEvaluation() {
		PackageInfo pi = new PackageInfo();
		I i= new I();
		Utils.print("I evaluation");
		MetricResultsMean results = new MetricResultsMean("I",MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,(int) i.packageI(p, graph));
			Utils.print("Package: "+result.getPackageName()+" I value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void ditpEvaluation(){
		PackageInfo pi = new PackageInfo();
		DIT dit = new DIT();
		Utils.print("DIT Package evaluation");
		MetricResultsMean results = new MetricResultsMean("DIT", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,dit.packageDit(p));
			Utils.print("Package: "+result.getPackageName()+" DIT  value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void wmcpEvaluation(){
		PackageInfo pi = new PackageInfo();
		WMC wmc = new WMC();
		Utils.print("WMC Package evaluation");
		MetricResultsMean results = new MetricResultsMean("WMC", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,wmc.packageWmc(p));
			Utils.print("Package: "+result.getPackageName()+" WMC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void nmpEvaluation(){
		PackageInfo pi = new PackageInfo();
		NM nm = new NM();
		Utils.print("NM Package evaluation");
		MetricResultsMean results = new MetricResultsMean("NM", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,nm.nmPackage(p));
			Utils.print("Package: "+result.getPackageName()+" NM value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void nmcEvaluation(){
		ClassInfo ci = new ClassInfo();
		NM nm = new NM();
		Utils.print("NM Class evaluation");
		MetricResultsMean results = new MetricResultsMean("NM",MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(),nm.nmClass(c));
			Utils.print("Class: "+result.getClassName()+" NM value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void rfcEvaluation(){
		ClassInfo ci = new ClassInfo();
		RFC rfc = new RFC();
		Utils.print("RFC Class evaluation");
		MetricResultsMean results = new MetricResultsMean("RCF", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(),rfc.classRfc(c, graph));
			Utils.print("Class: "+result.getClassName()+" RFC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void nmsEvaluation(){
		ClassInfo ci = new ClassInfo();
		NMS nms = new NMS();
		Utils.print("NMS Class evaluation");
		MetricResultsMean results = new MetricResultsMean("NMS", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(),nms.classNms(c, graph));
			Utils.print("Class: "+result.getClassName()+" NMS value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void ficEvaluation(){
		ClassInfo ci = new ClassInfo();
		FI fi = new FI();
		Utils.print("FI Class evaluation");
		MetricResultsMean results = new MetricResultsMean("FI", MetricLevel.CLASS);
		for(ClassInfo c : ci.getClassList()){
			MetricResult result = new MetricResult(c.getName(), (int)fi.classFi(c, graph));
			Utils.print("Class: "+result.getClassName()+" FI value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void fipEvaluation(){
		PackageInfo pi = new PackageInfo();
		FI fi = new FI();
		Utils.print("FI Package evaluation");
		MetricResultsMean results = new MetricResultsMean("FI", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,(int)fi.packageFi(p, graph));
			Utils.print("Package: "+result.getPackageName()+" FI value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void locpEvaluation(){
		PackageInfo pi = new PackageInfo();
		LOC loc = new LOC();
		Utils.print("LOC Package evaluation");
		MetricResultsMean results = new MetricResultsMean("LOC", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,+loc.packageLoc(p));
			Utils.print("Package: "+result.getPackageName()+" LOC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void lcompEvaluation(){
		PackageInfo pi = new PackageInfo();
		LCOM lcom = new LCOM();
		Utils.print("LCOM Package evaluation");
		MetricResultsMean results = new MetricResultsMean("LCOM", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,(int)lcom.packageLcom(p));
			Utils.print("Package: "+result.getPackageName()+" LCOM value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
	@Override
	protected void ncEvaluation(){
		ClassInfo ci = new ClassInfo();
		NC nc = new NC();
		Utils.print("NC Class evaluation");
		MetricResultsMean results = new MetricResultsMean("NC", MetricLevel.PACKAGE);
		for(ClassInfo c : ci.getClassList()){
			if(!c.isAnonymous()){
				MetricResult result = new MetricResult(c.getName(),nc.ncClass(c, tree));
				Utils.print("Class: "+result.getClassName()+" NC value: "+result.getValue());
				results.addResultValue(result);
			}
		}
		
		totalResults.add(results);
	}
	@Override
	protected void ncpackageEvaluation(){
		PackageInfo pi = new PackageInfo();
		NC nc = new NC();
		Utils.print("NC Package evaluation");
		MetricResultsMean results = new MetricResultsMean("NC", MetricLevel.PACKAGE);
		for(PackageInfo p : pi.getPackageList()){
			MetricResult result = new MetricResult(null,p.getName(),null,nc.ncPackage(p, tree));
			Utils.print("Package: "+result.getPackageName()+" NC value: "+result.getValue());
			results.addResultValue(result);
		}
		
		totalResults.add(results);
	}
//	@Override
//	protected void stamPackageInfo(){
//		Utils.print("Package info");
//		PackageInfo pi = new PackageInfo();
//		for(PackageInfo p : pi.getPackageList()){
//			Utils.print(p.toString());
//		}
//		
//		totalResults.add(results);
//		
//	}
	
	public String getJSONResult() {
		Gson g = new Gson();
		return g.toJson(totalResults);
	}
	
	

}
