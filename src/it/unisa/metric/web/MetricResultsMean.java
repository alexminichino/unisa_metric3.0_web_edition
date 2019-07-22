package it.unisa.metric.web;

import java.io.Serializable;
import it.unisa.metric.web.MetricResult;

public class MetricResultsMean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private String metricName;
	private double mean;
	private transient int count;
	private transient int sum;
	private MetricLevel metricLevel;
	
	public MetricResultsMean(String metricName, MetricLevel metricLevel) {
		this.metricName = metricName;
		this.metricLevel=metricLevel;
	}
	
	public void addResultValue(MetricResult r) {
		count++;
		sum+=r.getValue();
		updateMean();
	}
	
	private void updateMean() {
		mean =sum/count;
	}

	public String getMetricName() {
		return metricName;
	}

	public double getMean() {
		return mean;
	}

	public MetricLevel getMetricLevel() {
		return metricLevel;
	}
	
	
}


