package it.unisa.metric.struct.table;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/**
 * Represents entry record for variables.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class VariableRecord extends Record {

	/**
	 * Variable name.
	 */
	private String name;
	/**
	 * Variable source method binding.
	 */
	private IMethodBinding method;
	/**
	 * Variable declaration line.
	 */
	private int line;
	/**
	 * Variable type binding.
	 */
	private ITypeBinding type;
	/**
	 * Variable end scope line.
	 */
	private int endScopeLine;
	/**
	 * Variable lexical value.
	 */
	private int lexical;
	/**
	 * Variable source class binding.
	 */
	private ITypeBinding sourceClass;
	/**
	 * This variable type is primitive?
	 */
	private boolean primitive;
	
	/**
	 * Creates a VariableRecord from a CompilationUnit, a VariableDeclarationFragment and a IVariableBinding.
	 * @param cu Compilation unit (root of the AST).
	 * @param fragment Variable declaration fragment.
	 * @param binding Variable binding.
	 */
	public VariableRecord(CompilationUnit cu, VariableDeclarationFragment fragment, IVariableBinding binding) {
		name = binding.getName();
		method = binding.getDeclaringMethod();
		line = cu.getLineNumber(fragment.getStartPosition());
		type = binding.getType();
		primitive = type.isPrimitive();
		Block block = (Block) fragment.getParent().getParent();
		endScopeLine = cu.getLineNumber(block.getLength() + block.getStartPosition());
		lexical = calculateLexical(name);
		// standard case: the variable is in a method
		if(method != null)
			sourceClass = method.getDeclaringClass();
		else { // static block case: the variable is in a static block
			ASTNode tmp = fragment;
			while((tmp = tmp.getParent()) != null && !(tmp instanceof TypeDeclaration));
			sourceClass = ((TypeDeclaration) tmp).resolveBinding();
		}
	}
	
	/**
	 * Gets variable name.
	 * @return Variable name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets variable source method binding.
	 * @return Variable source method binding.
	 */
	public IMethodBinding getMethod() {
		return method;
	}
	
	/**
	 * Gets variable declaration line.
	 * @return Variable declaration line.
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Gets variable type binding.
	 * @return Variable type binding.
	 */
	public ITypeBinding getType() {
		return type;
	}
	
	/**
	 * Gets variable end scope line.
	 * @return Variable end scope line.
	 */
	public int getEndScopeLine() {
		return endScopeLine;
	}
	
	/**
	 * Gets variable lexical as an integer.
	 * {@link Record} contains all lexical values.
	 * @return Variable lexical as an integer.
	 * @see Record
	 * @see #getLexicalToString()
	 */
	public int getLexical() {
		return lexical;
	}
	
	/**
	 * Gets variable source class binding.
	 * @return Variable source class binding.
	 */
	public ITypeBinding getSourceClass() {
		return sourceClass;
	}
	
	/**
	 * This is a field?
	 * @return Always <code>false</code> because this is not a FieldRecord instance.
	 */
	public boolean isField() {
		return false;
	}
	
	/**
	 * This is a variable?
	 * @return Always <code>true</code> because this is a VariableRecord instance.
	 */
	public boolean isVariable() {
		return true;
	}
	
	/**
	 * Variable type is a primitive type?
	 * @return <code>True</code> if variable type is a primitive type; <code>false</code> if is a referral type.
	 */
	public boolean isPrimitive() {
		return primitive;
	}
	
	/**
	 * Variable type is a referral type?
	 * @return <code>True</code> if variable type is a referral type; <code>false</code> if is a primitive type.
	 */
	public boolean isReferralType() {
		return !primitive;
	}
	
	/**
	 * Variable type is array?
	 * @return <code>True</code> if variable type is an array; <code>false</code> if is not.
	 */
	public boolean isArray() {
		return type.isArray();
	}
	
	/**
	 * Gets variable lexical as a string.
	 * @return Variable lexical.
	 * @see Record
	 * @see #getLexical()
	 */
	public String getLexicalToString() {
		if(lexical == SINGLE_LETTER)
			return "single letter";
		if(lexical == ONE_WORD)
			return "one word";
		if(lexical == MORE_WORDS)
			return "more words";
		if(lexical == OTHER)
			return "others";
		return null;
	}
	
	/**
	 * Gets a string representation of a VariableRecord.
	 * @return VariableRecord as string.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("VARIABLE " + sourceClass.getQualifiedName() + ".");
		if(method != null) {
			sb.append(method.getName() + "(");
			ITypeBinding[] params = method.getParameterTypes();
			for(int i = 0; i < params.length; i++) {
				sb.append(params[i].getQualifiedName());
				if(i < params.length - 1)
					sb.append(", ");
			}
			sb.append("): [");
		} else
			sb.append("static: [");
		sb.append(line + " - " + endScopeLine + "] ");
		sb.append(type.getQualifiedName() + " " + name + " ");
		sb.append("(" + getLexicalToString() + ")");
		return sb.toString();
	}
}
