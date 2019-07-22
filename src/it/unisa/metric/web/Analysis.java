package it.unisa.metric.web;

import java.io.Serializable;
import java.util.Date;

public class Analysis implements Serializable{
	private String name;
	private Date date;
	private String JSONAverages;
	private String logFilename;
	private String jsonComentTree;
	
	
	/**
	 * @param name name of project
	 * @param date date of analysis
	 * @param jSONAverages json data of avarages
	 * @param logFilename 
	 */
	public Analysis(String name, Date date, String JSONAverages, String logFilename, String jsonCommentTree) {
		this.name = name;
		this.date = date;
		this.JSONAverages = JSONAverages;
		this.logFilename = logFilename;
		this.jsonComentTree = jsonCommentTree;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getJSONAverages() {
		return JSONAverages;
	}


	public void setJSONAverages(String JSONAverages) {
		this.JSONAverages = JSONAverages;
	}


	public String getLogFilename() {
		return logFilename;
	}


	public void setLogFilename(String logFilename) {
		this.logFilename = logFilename;
	}


	public String getJsonComentTree() {
		return jsonComentTree;
	}


	public void setJsonComentTree(String jsonComentTree) {
		this.jsonComentTree = jsonComentTree;
	}
	
	
	
	
	
}


