package it.unisa.metric.gui;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import it.unisa.metric.report.Report;
import it.unisa.metric.report.ReportManager;
import it.unisa.metric.struct.tree.CommentNode;

/**
 * Provides the GUI e to display the details of a commentNode and a report
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class CommentView {
	private HashMap<String, ArrayList<Report>> reports;
	
	/**
	 * Constructor
	 * @param comment comment to display
	 * @param reports reports near comment
	 */
	public CommentView(CommentNode comment, HashMap<String, ArrayList<Report>> reports) {
		this.reports = reports;
		 JTextArea textArea = new JTextArea(getCommentMetrics(comment));
    	 JScrollPane scrollReport = new JScrollPane(textArea);  
    	 ArrayList<Report> nearReports = ReportManager.getNearReportByFilePath(reports, comment.getFilePath(), comment.getParentLineNumber());
    	 JList list = new JList(new Vector<Report>(nearReports));
         //Handle click
         list.addMouseListener(new DetailClickListener(list));
         JScrollPane scrollMetrics = new JScrollPane(); 
         scrollMetrics = new JScrollPane(list);
         JPanel panel = new JPanel(); 
         panel.add(scrollMetrics);
         scrollMetrics.getViewport().add(list);
    	 textArea.setLineWrap(true);  
    	 textArea.setWrapStyleWord(true); 
    	 scrollReport.setPreferredSize( new Dimension( 500, 600 ) );
    	 scrollMetrics.setPreferredSize( new Dimension( 500, 600 ) );
    	 panel.add(scrollReport);
    	 JOptionPane.showMessageDialog(null, panel, "Comment metrics", JOptionPane.PLAIN_MESSAGE);
	}
	
	
	/**
	 * Gets the metrcis of commentNode as a String
	 * @param comment comment to read metrics
	 * @return a string with metrics 
	 */
	public String getCommentMetrics(CommentNode comment) {
		String toReturn = "VDFP=" + comment.getVerticalDistanceFromParent() +
				"\nHDFP=" + comment.getHorizontalDistanceFromParent() + 
				"\nRDFP=" + comment.getRealDistanceFromParent() + 
				"\nNOW=" + comment.getNumberOfWords() + 
				"\nparentLineNumber=" + comment.getParentLineNumber() + 
				"\nfilePath="+comment.getFilePath()+
				"\nusedWords=";
		for(Map.Entry<String, Integer> entry : comment.getusedWordsMap().entrySet()) {
			toReturn+="\n"+entry.getKey()+" = "+entry.getValue();
		}
		return toReturn;
	}
	
	
	/**
	 * Handles the click on a report and provides to visualize the details
	 * @author Alexander Minichino
	 * @version 1.0
	 * @since 1.0
	 */
	private class DetailClickListener extends MouseAdapter{
		private JList list;
		
		public DetailClickListener(JList list) {
			this.list = list;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			int index = list.locationToIndex(e.getPoint());
	          if (index >= 0) {
	        	Report r = (Report)list.getModel().getElementAt(index);
	        	String filesString="";
	       		for (String file : r.getFiles()) {
	  					filesString+=file+"\n";
	  			}
	        	JTextArea bugId = new JTextArea(r.getBugId()+"");
	        	JTextArea description = new JTextArea(r.getDescription());
	        	JTextArea summary = new JTextArea(r.getSummary());
	        	JTextArea reportTime = new JTextArea(r.getReportTime()+"");
	        	JTextArea commit = new JTextArea(r.getCommit());
	        	JTextArea status = new JTextArea(r.getStatus());
	       		JTextArea files = new JTextArea(filesString);
	       		bugId.setLineWrap(true);
	        	bugId.setWrapStyleWord(true);
	        	bugId.setEditable(false);
	        	description.setLineWrap(true);
	        	description.setWrapStyleWord(true);
	        	description.setEditable(false);
	        	summary.setLineWrap(true);
	        	summary.setWrapStyleWord(true);
	        	summary.setEditable(false);
	        	reportTime.setLineWrap(true);
	        	reportTime.setWrapStyleWord(true);
	        	reportTime.setEditable(false);
	        	commit.setLineWrap(true);
	        	commit.setWrapStyleWord(true);
	        	commit.setEditable(false);
	        	status.setLineWrap(true);
	        	status.setWrapStyleWord(true);
	        	status.setEditable(false);
	        	files.setLineWrap(true);
	        	files.setWrapStyleWord(true);
	        	files.setEditable(false);
	        	JPanel panel = new JPanel();
	       		panel.setLayout(new GridLayout(7, 2, 5, 5));
	       		panel.add(new JLabel("Bug id"));
	       		panel.add(new JScrollPane(bugId, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	       		panel.add(new JLabel("Summary"));
	       		panel.add(new JScrollPane(summary, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	       		panel.add(new JLabel("Description"));
	       		panel.add(new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	       		panel.add(new JLabel("Report Time"));
	       		panel.add(new JScrollPane(reportTime, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	       		panel.add(new JLabel("Commit"));
	       		panel.add(new JScrollPane(commit, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	       		panel.add(new JLabel("Status"));
	       		panel.add(new JScrollPane(status, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	       		panel.add(new JLabel("Files"));
	       		panel.add(new JScrollPane(files, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	       		panel.setPreferredSize(new Dimension(800, 600));
	       		JScrollPane jp = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	       		JOptionPane.showMessageDialog(null, jp, "Report details", JOptionPane.PLAIN_MESSAGE);
	        }
		}
	}
}
