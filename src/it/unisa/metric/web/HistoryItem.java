package it.unisa.metric.web;

public class HistoryItem {
	private String name;
	private String date;
	private String url;
	private String analysisId;
	
	public HistoryItem(String historyItemToParse, String absoluteParentUrl) {
		this(historyItemToParse.split("-")[0],historyItemToParse.split("-")[1],absoluteParentUrl);
		this.analysisId = historyItemToParse;
	}
	
	public HistoryItem(String name, String date, String url) {
		this.name = name;
		this.date = date;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getanalysisId() {
		return analysisId;
	}

	public void setanalysisId(String analysisId) {
		this.analysisId = analysisId;
	}
	
	
	
}
