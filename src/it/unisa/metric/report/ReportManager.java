package it.unisa.metric.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Provides methods to read and manage reports
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class ReportManager {
	
	
	/**
	 * Reads reports from an excel file
	 * @param excelFilePath path of excell file
	 * @return
	 */
	public static ArrayList<Report> readBooksFromFile(String excelFilePath) {
	    ArrayList<Report> reportList = new ArrayList<>();
	    FileInputStream inputStream = null; 
	    Workbook workbook = null;
		try {
			inputStream = new FileInputStream(new File(excelFilePath));
			workbook = new XSSFWorkbook(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    Sheet firstSheet = workbook.getSheetAt(0);
	    Iterator<Row> iterator = firstSheet.iterator();
	 
	    while (iterator.hasNext()) {
	        Row nextRow = iterator.next();
	        Iterator<Cell> cellIterator = nextRow.cellIterator();
	        Report aReport = new Report();
	        boolean isHead = false;
	        while (cellIterator.hasNext()) {
	            Cell nextCell = cellIterator.next();
	            int columnIndex = nextCell.getColumnIndex();
	 
	            switch (columnIndex) {
	            case 1:
	            	if (nextCell.getCellTypeEnum() == CellType.NUMERIC) {
	            		aReport.setBugId((int)nextCell.getNumericCellValue());
	            		break;
	            	}
	            	isHead= true;
	                break;
	            
	        	case 2:
	            	if (nextCell.getCellTypeEnum() == CellType.STRING) {
	            		aReport.setSummary(nextCell.getStringCellValue());
	            		break;
	            	}
	            	isHead= true;
	                break;
	        	case 3:
	            	if (nextCell.getCellTypeEnum() == CellType.STRING) {
	            		aReport.setDescription(nextCell.getStringCellValue());
	            		break;
	            	}
	            	isHead= true;
	                break;
	        	case 4:
	            	if (nextCell.getCellTypeEnum() == CellType.STRING) {
	            		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            		Date date;
						try {
							date = formatter.parse(nextCell.getStringCellValue());
							aReport.setReportTime(date);
						} catch (ParseException e) {
							isHead = true;
						}
						finally {
							break;
						}
	            	}
	            	isHead= true;
	                break;
	        	case 5:
	            	if (nextCell.getCellTypeEnum() == CellType.NUMERIC) {
	            		aReport.setReportTimestamp((long)nextCell.getNumericCellValue());
	            		break;
	            	}
	            	isHead= true;
	                break;
	        	case 6:
	            	if (nextCell.getCellTypeEnum() == CellType.STRING) {
	            		aReport.setStatus(nextCell.getStringCellValue());
	            		break;
	            	}
	            	isHead= true;
	                break;
	        	case 7:
	            	if (nextCell.getCellTypeEnum() == CellType.STRING) {
	            		aReport.setCommit(nextCell.getStringCellValue());
	            		break;
	            	}
	            	isHead= true;
	                break;
	        	case 8:
	            	if (nextCell.getCellTypeEnum() == CellType.NUMERIC) {
	            		aReport.setCommitTimestamp((long)nextCell.getNumericCellValue());
	            		break;
	            	}
	            	isHead= true;
	                break;
	        	case 9:
	            	if (nextCell.getCellTypeEnum() == CellType.STRING) {
	            		String[] files = nextCell.getStringCellValue().split(" ");
	            		for (String f: files)
	            			aReport.addFile(f);
	            		break;
	            	}
	            	isHead= true;
	                break;
	        	case 10:
	            	if (nextCell.getCellTypeEnum() == CellType.STRING) {
	            		String[] files = nextCell.getStringCellValue().split(" ");
	            		for (String f: files) {	   
	            			aReport.addFileWithBugRow(Integer.parseInt(f.split(":")[0]), f.split(":")[1]);
	            		}
	            		break;
	            	}
	            	isHead= true;
	                break;
	            }
	        }
	        if (!isHead) {
	        	reportList.add(aReport);
	        }
	    }
	    try {
			workbook.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return reportList;
	}
	

	/**
	 * Gets a map containing a list of reports having as key the file involved in the reports
	 * @param excelFilePath excell file path
	 * @param basePath base path
	 * @return
	 */
	public static HashMap<String, ArrayList<Report>> getReportsMap(String excelFilePath, String basePath) {
		ArrayList<Report> reports = readBooksFromFile(excelFilePath);
		HashMap<String, ArrayList<Report>> reportMap = new HashMap<>();
		for (Report  r: reports) {
			for (String file : r.getFiles()) {
				String absolutePath = mergePath(basePath, file);
				if(reportMap.containsKey(absolutePath)) {
					reportMap.get(absolutePath).add(r);
				}
				else {
					ArrayList<Report> newList = new ArrayList<>();
					newList.add(r);
					reportMap.put(absolutePath, newList);
				}
			}
		}
		return reportMap;
	}
	
	
	/**
	 * Gets bug reports that involve a given file to a given line
	 * @param reports Hashmap of reports to look for
	 * @param filePath file path
	 * @param nodeLine line number
	 * @return filtered hash map of reports
	 */
	public static ArrayList<Report> getNearReportByFilePath(HashMap<String, ArrayList<Report>> reports, String filePath, int nodeLine) {
		ArrayList<Report> results = new ArrayList<>();
		if(reports != null) {
			//O(1) time access
			if(reports.containsKey(Paths.get(filePath).toString()))
				for (Report r : reports.get(Paths.get(filePath).toString())) {
					if(r.isNearRow(nodeLine) && !results.contains(r)) {
						results.add(r);
					}
				}
		}
		return results;
	}
	
	
	/**
	 * Check if there are bug reports that involve a given file to a given line
	 * @param reports Hashmap of reports to look for
	 * @param filePath file path
	 * @param nodeLine line number
	 * @return a boolean indicating if there are any bugs nearby
	 */
	public static boolean hasNeighborsReports(HashMap<String, ArrayList<Report>> reports, String filePath, int nodeLine) {
		return getNearReportByFilePath(reports, filePath, nodeLine).size() > 0;
	}
	
	
	/**
	 * Gets a path from two paths
	 * @param base base path
	 * @param path relative path
	 * @return
	 */
	private static String mergePath(String base, String path) {
		path = path.replaceAll("\\\\+", "/").replaceAll("/+", "/");
		Path pathObj = Paths.get(path);
		int indexOfPath = pathObj.toString().length();
		while(!Paths.get(base).toString().contains(pathObj.toString().substring(0,indexOfPath)) && indexOfPath > 0) {
			indexOfPath--;
		}
		ArrayList<String> parts = new ArrayList<String>();
		Paths.get(pathObj.toString().substring(indexOfPath)).forEach(p-> parts.add(p.toString()));
		return Paths.get(base, parts.toArray(new String[parts.size()])).toString();
	}

}
