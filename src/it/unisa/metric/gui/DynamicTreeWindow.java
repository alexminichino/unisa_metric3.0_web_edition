package it.unisa.metric.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import it.unisa.metric.report.Report;
import it.unisa.metric.struct.tree.CommentNode;
import it.unisa.metric.struct.tree.CommentTree;
import it.unisa.metric.struct.tree.Node;
import it.unisa.metric.struct.tree.PackageNode;
import it.unisa.metric.struct.tree.ProjectNode;
 
/**
 * Provides the frame for the dynamic tree and manages events
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class DynamicTreeWindow extends JPanel{ 
    private DynamicTree treePanel;
    private CommentTree tree;
    private HashMap<String, ArrayList<Report>> reports;
 
    
    /**
     * Constructor
     * @param tree comment tree
     * @param reportsMap reports collection
     */
    public DynamicTreeWindow(CommentTree tree, HashMap<String, ArrayList<Report>> reportsMap) {
    	super(new BorderLayout());
    	this.tree = tree;
    	this.reports = reportsMap;
        treePanel = new DynamicTree(appendRoot(), new ExpandListener(), new SelectionListener(), new HandleMouseClick());
        treePanel.setPreferredSize(new Dimension(1200, 800));
        add(treePanel, BorderLayout.CENTER);
        createAndShowGUI();
    }
    
    
    /**
     * Creates the GUI and show it
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Dynamic comment tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.
        this.setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
   
    /**
     * Appends the root to the tree
     * @return appended element
     */
    private DefaultMutableTreeNode appendRoot() {
        DefaultMutableTreeNode r = new DefaultMutableTreeNode((Node)tree.getRoot());
        appendChildOf(r);
        return r;
    }
    
    
    /**
     * Performs the append of all unexplored children from a parent
     * @param parent of unexplored children
     */
    private void appendChildOf(DefaultMutableTreeNode parent) {
		Object nodeInfo = parent.getUserObject();
        Enumeration<Node> children = ((Node)nodeInfo).children();
		while(children.hasMoreElements()) {
			Node child = children.nextElement();
			parent.add(new DefaultMutableTreeNode(child));
		}
    }
    
    
    /**
     * Expand and collapse custom listener
     * @author Alexander Minichino
     * @version 1.0
     * @since 1.0
     */
    private class ExpandListener implements TreeWillExpandListener{
		@Override
		public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
			TreePath path = event.getPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//			Enumeration<DefaultMutableTreeNode> children = node.children();
//			while(children.hasMoreElements()) {
//				DefaultMutableTreeNode child = children.nextElement();
//				Object nodeInfo = child.getUserObject();
//				appendChildOf(child);
//			}
		}

		@Override
		public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
			
		}
    }
    
    
    /**
     * Custom selection listener
     * @author Alexander Minichino
     * @version 1.0
     * @since 1.0
     */
    private class SelectionListener implements TreeSelectionListener{
    	@Override
    	public void valueChanged(TreeSelectionEvent e) {
    		 	DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
    	        if (node == null) return;
    	        Object nodeInfo = node.getUserObject();
    	       
    	        int nodeType= ((Node)nodeInfo).getType();
    	        if(nodeType == Node.PROJECT) {
    	        	treePanel.displayNodeTextInfo(((ProjectNode)nodeInfo).toString(), 1);
    	        	treePanel.displayNodeTextComments("");
    	        }
    	        else if(nodeType == Node.PACKAGE) {
    	        	treePanel.displayNodeTextInfo(((PackageNode)nodeInfo).getPackageName(),1);
    	        	treePanel.displayNodeTextComments("");
    	        }
    	        else {
    	        	treePanel.displayNodeTextInfo(((CommentNode) nodeInfo).getObjectCommented().toString(), ((CommentNode) nodeInfo).getParentLineNumber());
    	        	String comments = "";
    	        	for (String c : ((CommentNode) nodeInfo).getCommentList()) {
    	        		comments+=c+"\n";
					}
    	        	treePanel.displayNodeTextComments(comments);
    	        }
    	}
    }
    
    
    /**
     * Custom mouse event listener
     * @author Alexander Minichino
     * @version 1.0
     * @since 1.0
     */
    private class HandleMouseClick implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 2) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
                if (node == null) return;
                Object nodeInfo = node.getUserObject();
                if(nodeInfo instanceof CommentNode) {
                	try {
                		new CommentView((CommentNode)nodeInfo, reports);
					} catch (NullPointerException ex) {
						ex.printStackTrace();
					}
                }	
            }
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}