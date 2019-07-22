package it.unisa.metric.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a report record and exposes the methods for accessing and editing it
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class Report {
	private int bugId;
	private String summary;
	private String description;
	private Date reportTime;
	private long reportTimestamp;
	private String status;
	private String commit;
	private long commitTimestamp;
	private ArrayList<String> files;
	private HashMap<Integer, String> filesWithBugRow;
	/**
	 * 
	 */
	public static final int NEAR_ROW_LIMIT = 5;
	

	/**
	 * Constructor
	 * @param bugId id of bug
	 * @param summary summary of bug
	 * @param description description of bug
	 * @param reportTime report time of bug
	 * @param reportTimestamp report timestamp of bug
	 * @param status status of bug
	 * @param commit commit of bug
	 * @param commitTimestamp commit timestamp of bug
	 */
	public Report(int bugId, String summary, String description, Date reportTime, long reportTimestamp, String status, String commit, long commitTimestamp) {
		super();
		this.bugId = bugId;
		this.summary = summary;
		this.description = description;
		this.reportTime = reportTime;
		this.reportTimestamp = reportTimestamp;
		this.status = status;
		this.commit = commit;
		this.commitTimestamp = commitTimestamp;
		this.files = new ArrayList<>();
		this.filesWithBugRow = new HashMap<>();
	}


	/**
	 * Constructor
	 */
	public Report() {
		this.files = new ArrayList<>();
		this.filesWithBugRow = new HashMap<>();
	}


	/**
	 * Gets the bug id
	 * @return bug id
	 */
	public int getBugId() {
		return bugId;
	}

	
	/**
	 * Sets the bug id
	 * @param bug id
	 */
	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	
	/**
	 * Gets summary of bug
	 * @return bug summary
	 */
	public String getSummary() {
		return summary;
	}


	/**
	 * Sets summary of bug
	 * @param summary bug summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	
	/**
	 * Gets description of bug
	 * @return bug description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Sets description of bug
	 * @param description bug description
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * Gets report time of bug
	 * @return bug report time
	 */
	public Date getReportTime() {
		return reportTime;
	}


	/**
	 * Sets report time of bug
	 * @param reportTime bug report time
	 */
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}


	/**
	 * Sets report timestamp of bug
	 * @return bug report timestamp
	 */
	public long getReportTimestamp() {
		return reportTimestamp;
	}

	/**
	 * Sets report timestamp of bug
	 * @param reportTimeStamp bug report timestamp
	 */
	public void setReportTimestamp(long reportTimestamp) {
		this.reportTimestamp = reportTimestamp;
	}

	
	/**
	 * Gets the status of bug
	 * @return bug status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * Sets the status of bug 
	 * @param status bug status
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * Gets the commit of bug
	 * @return bug commit
	 */
	public String getCommit() {
		return commit;
	}


	/**
	 * Sets the commit of bug 
	 * @param commit bug commit
	 */
	public void setCommit(String commit) {
		this.commit = commit;
	}


	/**
	 *  Gets the commit timestamp of bug
	 * @return bug commit timestamp
	 */
	public long getCommitTimestamp() {
		return commitTimestamp;
	}


	/**
	 * Sets the commit timestamp of bug
	 * @param commitTimestamp bug commit timestamp
	 */
	public void setCommitTimestamp(long commitTimestamp) {
		this.commitTimestamp = commitTimestamp;
	}


	/**
	 * Gets the files involved by the bug
	 * @return files involved by the bug
	 */
	public ArrayList<String> getFiles() {
		return files;
	}


	/**
	 * Adds a file to the list of files involved in the bug
	 * @param file file to add
	 */
	public void addFile(String file) {
		this.files.add(file);
	}


	/**
	 * Gets the files involved by the bug in a map that has the line number as a key
	 * @return map with files
	 */
	public HashMap<Integer,String> getFilesWithBugRow() {
		return filesWithBugRow;
	}


	/**
	 * Adds a file to the map
	 * @param row line number of the bug
	 * @param file file involved by the bug
	 */
	public void addFileWithBugRow(int row, String file) {
		this.filesWithBugRow.put(row, file);
	}
	
	
	/**
	 * Checks if there are any bugs reported near a line
	 * @param nodeLine line number
	 * @return a boolean indicating if there are any bugs nearby
	 */
	public boolean isNearRow(int nodeLine) {
		for (Map.Entry<Integer, String> entry: filesWithBugRow.entrySet() ) {
			if(Math.abs(entry.getKey()-nodeLine) <= NEAR_ROW_LIMIT)
				return true;
		}
		return false;
	}
	
	
	/**
	 * Gets bug reports whose files are close to a line
	 * @param nodeRow line number
	 * @return bug reports
	 */
	public Entry<Integer, String> getCloserFileWithRow(int nodeRow){
		Entry<Integer, String> closer = null;
		int min = NEAR_ROW_LIMIT;
		for (Entry<Integer, String> entry: filesWithBugRow.entrySet() ) {
			if(entry.getKey() <= min) {
				closer = entry;
				min = entry.getKey();
			}
		}
		return closer;
	}

	
	@Override
	public String toString() {
		return "BUG_ID= " + bugId +", STATUS= " + status + ", COMMIT= " + commit ;
	}
	
	
}
