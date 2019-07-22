package it.unisa.metric.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import it.unisa.metric.gui.CompoundIcon.Axis;
import it.unisa.metric.struct.tree.CommentNode;
import it.unisa.metric.struct.tree.Node;
import it.unisa.metric.struct.tree.PackageNode;
import it.unisa.metric.struct.tree.ProjectNode;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
 
/**
 * Provides the GUI e to display the comment tree
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class DynamicTree extends JPanel {
	protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    protected RSyntaxTextArea sourceCodeArea;
    protected JEditorPane editorPaneComments;
    protected RTextScrollPane textScrollPaneInfo;
 
    
    /**
     * Constructor 
     * @param rootNode a root of tree
     * @param willExpandListener expand and collapse event listener
     * @param treeSelectionListener selection event listener
     * @param mouseListener mouse event listener
     */
    public DynamicTree(DefaultMutableTreeNode rootNode, TreeWillExpandListener willExpandListener, TreeSelectionListener treeSelectionListener, MouseListener mouseListener) {
        super(new GridLayout(1,0));
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.collapsePath(new TreePath(rootNode));;
        tree.addTreeSelectionListener(treeSelectionListener);
        tree.addMouseListener(mouseListener);
        tree.setCellRenderer(new TreeRendered());
        tree.setCellEditor(new TreeCellEditor(tree, (DefaultTreeCellRenderer) tree.getCellRenderer()));
        tree.addTreeWillExpandListener(willExpandListener);
        JScrollPane treeScrollPane = new JScrollPane(tree);
        sourceCodeArea = new RSyntaxTextArea();
        textScrollPaneInfo = new RTextScrollPane(sourceCodeArea);
        sourceCodeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        sourceCodeArea.setCodeFoldingEnabled(true);
        sourceCodeArea.setEditable(false);
        editorPaneComments = new JEditorPane();
        editorPaneComments.setEditable(false);
        JScrollPane textScrollPaneComments = new JScrollPane(editorPaneComments);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(textScrollPaneInfo);
        bottomPanel.add(textScrollPaneComments);
        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeScrollPane);
        splitPane.setBottomComponent(bottomPanel);
        Dimension minimumSize = new Dimension(100, 200);
        textScrollPaneInfo.setMinimumSize(minimumSize);
        textScrollPaneComments.setMinimumSize(minimumSize);
        treeScrollPane.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(300);
        add(splitPane);
    }
 
    
    /**
     * Remove all nodes except the root node
     */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }
 
    
    /** 
     * Remove the currently selected node
     **/
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)(currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        } 
        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }
 
    
    /**
     * Add child to the currently selected node
     * @param child child to add
     * @return added treeNode
     */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
 
        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
        }
 
        return addObject(parentNode, child, true);
    }
 
    
    /**
     * Add child to the specific node
     * @param parent specific parent node
     * @param child child to add
     * @return added treeNode
     */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
        return addObject(parent, child, false);
    }
 
    
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,Object child, boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
 
        if (parent == null) {
            parent = rootNode;
        }
        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
 
        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }


    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @param path path of image
     * @return ImageIcon
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = DynamicTree.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    
    /**
     * formats a string with the style of a comment
     * @param comment to format
     * @return formatted comment
     */
    private String getFormattedComment(String comment) {
    	return "<html><div style = 'color:#008000'>"+comment+"</div></html>";
    }

    
	/**
	 * Displays the source code in text area
	 * @param infos source code
	 * @param startLine start line number
	 */
	protected void displayNodeTextInfo(String infos, int startLine) {
		if(infos != null && infos != "")
			sourceCodeArea.setText(infos);
		textScrollPaneInfo.getGutter().setLineNumberingStartIndex(startLine);
		sourceCodeArea.setCaretPosition(0);
	 }
	
	 
	 /**
	 * Displays the comment in text area
	 * @param comment comment to display
	 */
	protected void displayNodeTextComments(String comment) {
		 editorPaneComments.setContentType("text/html");
		 editorPaneComments.setText(getFormattedComment(comment));
		 editorPaneComments.setCaretPosition(0);
	 }
	 
	
	 /**
	  * Custom tree renderer
	 * @author Alexander Minichino
	 * @version 1.0
	 * @since 1.0
	 */
	private class TreeRendered extends DefaultTreeCellRenderer  {
		private final ImageIcon projectIcon;
		private final ImageIcon packageIcon;
		private final ImageIcon packageDeclarationIcon;
		private final ImageIcon publicClassIcon;
		private final ImageIcon protectedClassIcon;
		private final ImageIcon privateClassIcon;
		private final ImageIcon defaultClassIcon;
		private final ImageIcon publicMethodIcon;
		private final ImageIcon protectedMethodIcon;
		private final ImageIcon privateMethodIcon;
		private final ImageIcon defaultMethodIcon;
		private final ImageIcon enumIcon;
		private final ImageIcon publicFieldIcon;
		private final ImageIcon protectedFieldIcon;
		private final ImageIcon privateFieldIcon;
		private final ImageIcon defaultFieldIcon;
		private final ImageIcon expressionIcon;
		private final ImageIcon unknownObjectIcon;
		private final ImageIcon interfaceIcon;
		private final ImageIcon abstractIcon;
		private final ImageIcon staticIcon;
		private final ImageIcon finalIcon;
		private final ImageIcon nativeIcon;
		private final ImageIcon synchronizedIcon;
		private final ImageIcon transiedIcon;
		private final ImageIcon volatileIcon;
		
		
		/**
		 * Constructor
		 */
		TreeRendered() {
			projectIcon = createImageIcon("images/jworkingSet_obj.png");
			packageIcon = createImageIcon("images/package_obj.png");
			packageDeclarationIcon = createImageIcon("images/packd_obj.png");
			publicClassIcon = createImageIcon("images/class_obj.png");
			protectedClassIcon = createImageIcon("images/innerclass_protected_obj.png");
			privateClassIcon = createImageIcon("images/innerclass_private_obj.png");
			defaultClassIcon = createImageIcon("images/class_default_obj.png");
			publicMethodIcon = createImageIcon("images/methpub_obj.png"); 
			protectedMethodIcon = createImageIcon("images/methpro_obj.png"); 
			privateMethodIcon = createImageIcon("images/methpri_obj.png"); 
			defaultMethodIcon = createImageIcon("images/methdef_obj.png"); 
			enumIcon = createImageIcon("images/enum_obj.png"); 
			publicFieldIcon = createImageIcon("images/field_public_obj.png");
			protectedFieldIcon = createImageIcon("images/field_protected_obj.png");
			privateFieldIcon = createImageIcon("images/field_private_obj.png");
			defaultFieldIcon = createImageIcon("images/field_default_obj.png");
			expressionIcon = createImageIcon("images/expression_obj.png");
			unknownObjectIcon = createImageIcon("images/unknown_obj.png");
			interfaceIcon = createImageIcon("images/int_obj.png");
			abstractIcon = createImageIcon("images/abstract_co.png");
			staticIcon = createImageIcon("images/static_co.png");
			finalIcon = createImageIcon("images/final_co.png");
			nativeIcon = createImageIcon("images/native_co.png");
			synchronizedIcon = createImageIcon("images/synch_co.png");
			transiedIcon = createImageIcon("images/transient_co.png");
			volatileIcon =  createImageIcon("images/volatile_co.png");
		}
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus); 
			if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
			      Object nodeInfo = ((DefaultMutableTreeNode) value).getUserObject();
			      int nodeType= ((Node)nodeInfo).getType();
			      if(nodeType == Node.PROJECT) {
			    	  this.setText((((ProjectNode)nodeInfo).getProjectName()));
			    	  this.setIcon(projectIcon);
			      }
			      else if(nodeType == Node.PACKAGE) {
			    	  this.setText(((PackageNode)nodeInfo).getPackageName());
			    	  this.setIcon(packageIcon);
			      }
			      else if(nodeType == Node.CLASS) {
			    	  ASTNode n = ((CommentNode)nodeInfo).getObjectCommented();
			    	  this.setText( ((TypeDeclaration)n).getName().toString());
			    	  int mod = ((TypeDeclaration)n).getModifiers();
			    	  if(((TypeDeclaration)n).isInterface()){
			    		  this.setIcon(interfaceIcon);
			    	  }
			    	  else if(Modifier.isPublic(mod)) {
			    		  this.setIcon(publicClassIcon);
			    	  }
			    	  else if(Modifier.isProtected(mod)) {
			    		  this.setIcon(protectedClassIcon);		    		  
			    	  }
			    	  else if(Modifier.isPrivate(mod)) {
			    		  this.setIcon(privateClassIcon);
			    	  }
			    	  else {
			    		  this.setIcon(defaultClassIcon);
			    	  }
			    	  addAttitionalIcons(mod, this); 
			      }
			      else if(nodeType == Node.METHOD) {
			    	  ASTNode n = ((CommentNode)nodeInfo).getObjectCommented();
			    	  
			    	  List<SingleVariableDeclaration>  params = ((MethodDeclaration)n).parameters();
			    	  this.setText( ((MethodDeclaration)n).getName().toString() + params.stream().map(param -> param.toString()).collect(Collectors.joining(":", "(", ")")));		    	  
			    	  int mod = ((MethodDeclaration)n).getModifiers();
			    	  if(Modifier.isPublic(mod)) {
			    		  this.setIcon(publicMethodIcon);
			    	  }
			    	  else if(Modifier.isProtected(mod)) {
			    		  this.setIcon(protectedMethodIcon);	    		  
			    	  }
			    	  else if(Modifier.isPrivate(mod)) {
			    		  this.setIcon(privateMethodIcon);
			    	  }
			    	  else {
			    		  this.setIcon(defaultMethodIcon);
			    	  }
			    	  addAttitionalIcons(mod, this);
			      }
			      else if(nodeType == Node.ENUM) {
			    	  ASTNode n = ((CommentNode)nodeInfo).getObjectCommented();
			    	  this.setText( ((EnumDeclaration)n).getName().toString());
			    	  this.setIcon(enumIcon);
				  }	
			      else if(nodeType == Node.FIELD) {
			    	  ASTNode n = ((CommentNode)nodeInfo).getObjectCommented();
			    	  int mod = ((FieldDeclaration)n).getModifiers();
			    	  if(Modifier.isPublic(mod)) {
			    		  this.setIcon(publicFieldIcon);
			    	  }
			    	  else if(Modifier.isProtected(mod)) {
			    		  this.setIcon(protectedFieldIcon);	    		  
			    	  }
			    	  else if(Modifier.isPrivate(mod)) {
			    		  this.setIcon(privateFieldIcon);
			    	  }
			    	  else {
			    		  this.setIcon(defaultFieldIcon);
			    	  }
			    	  addAttitionalIcons(mod, this);
	
			    	  Object f = ((FieldDeclaration)n).fragments().get(0);
			    	  if(f instanceof VariableDeclarationFragment) {
			    		  this.setText(((VariableDeclarationFragment)f).getName().toString()); 
			    	  }
			    	  else {
			    		  this.setText( ((FieldDeclaration)n).toString());
			    	  }
			      }
			      else {
			    	  ASTNode n = ((CommentNode)nodeInfo).getObjectCommented();  
			    	  if(n instanceof ExpressionStatement) {
			    		  this.setText(((ExpressionStatement)n).getExpression().toString());
			    		  this.setIcon(expressionIcon);
			    	  }
			    	  else if(n instanceof PackageDeclaration) {
			    		  this.setText(n.toString());
				    	  this.setIcon(packageDeclarationIcon);
			    	  }
			    	  else {
			    		  this.setText( n.toString());
			    		  this.setIcon(unknownObjectIcon);
			    	  }
			      }  
			      return this;
			    }
			return null;
		}
		
		
		/**
		 * Sets additional icons
		 * @param mod modifier
		 * @param label label to add the icon
		 */
		private void addAttitionalIcons(int mod, JLabel label ) {
			  ArrayList<Icon> icons = new ArrayList<>();
			  icons.add(label.getIcon());
			  if(Modifier.isAbstract(mod)) {
				  icons.add(abstractIcon);
			  }
			  if(Modifier.isStatic(mod)) {
				  icons.add(staticIcon);
	    	  }
			  if(Modifier.isFinal(mod)) {
				  icons.add(finalIcon);
	    	  }
			  if(Modifier.isNative(mod)) {
				  icons.add(nativeIcon);
	    	  }
			  if(Modifier.isSynchronized(mod)) {
				  icons.add(synchronizedIcon);
	    	  }
			  if(Modifier.isTransient(mod)) {
				  icons.add(transiedIcon);
	    	  }
			  if(Modifier.isVolatile(mod)) {
				  icons.add(volatileIcon);
	    	  }
			  label.setIcon(new CompoundIcon(Axis.X_AXIS, 1, CompoundIcon.RIGHT, CompoundIcon.TOP, icons.toArray(new Icon[icons.size()])));
		}
		 
	 }
	
	
	 /**
	 * Custom cell editor
	 * @author Alexander Minichino
	 * @version 1.0
	 * @since 1.0
	 */
	private class TreeCellEditor extends DefaultTreeCellEditor{

		public TreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
			super(tree, renderer);
		}
		@Override
		public boolean isCellEditable(EventObject event) {
			if(event instanceof MouseEvent){
	            return false;
	        }
			return super.isCellEditable(event); 
		}
		 
	 }
	
}