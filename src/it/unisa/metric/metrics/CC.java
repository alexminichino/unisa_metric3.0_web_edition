package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.PackageInfo;

public class CC {

	public int packageCc(PackageInfo packageInf) {
		int concrete=0;
		for(ClassInfo classInf : packageInf.getClasses()) {
			if(classInf.isConcrete()) {
				concrete++;
			}
		}

		return concrete;
	}
}
