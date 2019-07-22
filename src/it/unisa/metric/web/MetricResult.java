package it.unisa.metric.web;

import java.io.Serializable;

public class MetricResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String className;
	private String packageName;
	private String methodName;
	private int value;
	private MetricLevel metricLevel;
	


	public MetricResult(String className, String packageName, String methodName, int value) {
		this.className = className;
		this.packageName = packageName;
		this.methodName = methodName;
		this.value = value;
		if(className ==null && methodName==null) {
			metricLevel= MetricLevel.PACKAGE;
		}
		else {
			metricLevel=metricLevel.METHOD;
		}
	}
	
	public MetricResult(String className, String methodName, int value) {
		this.className = className;
		this.methodName = methodName;
		this.value = value;
		metricLevel=MetricLevel.METHOD;
	}
	
	public MetricResult(String className, int value) {
		this.className = className;
		this.value = value;
		metricLevel= metricLevel.CLASS;
	}
	
	


	public int getValue() {
		return value;
	}




	public void setValue(int value) {
		this.value = value;
	}




	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	
	
	
}
