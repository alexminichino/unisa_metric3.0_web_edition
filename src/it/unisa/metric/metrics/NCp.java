package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.PackageInfo;

//class complete
public class NCp {
	public int packageNCp(PackageInfo packInf){
		return packInf.getClasses().size();
	}
}
