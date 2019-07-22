package it.unisa.metric.struct.table;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Represents entry record for fields.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class FieldRecord extends Record {

	/**
	 * Field name.
	 */
	private String name;
	/**
	 * Field declaration line.
	 */
	private int line;
	/**
	 * Field type binding.
	 */
	private ITypeBinding type;
	/**
	 * Field modifiers.
	 */
	private int modifiers;
	/**
	 * Field lexical value.
	 */
	private int lexical;
	/**
	 * Field source class binding.
	 */
	private ITypeBinding sourceClass;
	/**
	 * Field type is primitive?
	 */
	private boolean primitive;
	
	/**
	 * Creates a FieldRecord from a CompilationUnit, a VariableDeclarationFragment and a IVariableBinding.
	 * @param cu Compilation unit (root of the AST).
	 * @param fragment Variable declaration fragment.
	 * @param binding Field binding.
	 */
	public FieldRecord(CompilationUnit cu, VariableDeclarationFragment fragment, IVariableBinding binding) {
		name = binding.getName();
		line = cu.getLineNumber(fragment.getStartPosition());
		type = binding.getType();
		primitive = type.isPrimitive();
		lexical = calculateLexical(name);
		sourceClass = binding.getDeclaringClass();
		modifiers = binding.getModifiers();		
	}
	
	/**
	 * Gets field name.
	 * @return Field name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets field declaration line.
	 * @return Field declaration line.
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Gets field type.
	 * @return field type.
	 */
	public ITypeBinding getType() {
		return type;
	}
	
	/**
	 * Gets field modifiers as an integer.
	 * {@link Record} contains bit masks for each modifier.
	 * To get field modifiers as string array, see {@link #getModifiersToString()}. 
	 * @return Field modifiers as an integer.
	 * @see Record
	 * @see #getModifiersToString()
	 */
	public int getModifiers() {
		return modifiers;
	}
	
	/**
	 * Gets field lexical as an integer.
	 * {@link Record} contains all lexical values.
	 * @return Field lexical as an integer.
	 * @see Record
	 * @see #getLexicalToString()
	 */
	public int getLexical() {
		return lexical;
	}
	
	/**
	 * Gets field source class binding.
	 * @return Field source class binding.
	 */
	public ITypeBinding getSourceClass() {
		return sourceClass;
	}
	
	/**
	 * This is a field?
	 * @return Always <code>true</code> because this is a FieldRecord instance.
	 */
	public boolean isField() {
		return true;
	}
	
	/**
	 * This is a variable?
	 * @return Always <code>false</code> because this is not a VariableRecord instance.
	 */
	public boolean isVariable() {
		return false;
	}
	
	/**
	 * Field type is a primitive type?
	 * @return <code>True</code> if field type is a primitive type; <code>false</code> if is a referral type.
	 * @see #isReferralType()
	 */
	public boolean isPrimitive() {
		return primitive;
	}
	
	/**
	 * Field type is referral type?
	 * @return <code>True</code> if field type is referral type; <code>false</code> if is a primitive type.
	 * @see #isPrimitive()
	 */
	public boolean isReferralType() {
		return !primitive;
	}
	
	/**
	 * Field type is an array?
	 * @return <code>True</code> if field type is an array; <code>false</code> if is not.
	 */
	public boolean isArray() {
		return type.isArray();
	}
	
	/**
	 * Gets a string array that contains field modifiers.
	 * @return Field modifiers.
	 * @see Record
	 * @see #getModifiers()
	 */
	public String[] getModifiersToString() {
		StringBuilder sb = new StringBuilder();
		if(Modifier.isPublic(modifiers))
			sb.append("public ");
		else if(Modifier.isProtected(modifiers))
			sb.append("protected ");
		else if(Modifier.isPrivate(modifiers))
			sb.append("private ");
		
		if(Modifier.isTransient(modifiers))
			sb.append("transient ");
		else if(Modifier.isVolatile(modifiers))
			sb.append("volatile ");
		
		if(Modifier.isStatic(modifiers))
			sb.append("static ");
		
		if(Modifier.isFinal(modifiers))
			sb.append("final ");
		
		if(sb.length() > 0)
			sb.substring(0, sb.length() - 1);
		
		return sb.toString().split(" ");
	}
	
	/**
	 * Gets field lexical as a string.
	 * @return Field lexical.
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
	 * Gets a string representation of a FieldRecord.
	 * @return Field as string.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FIELD \t " + sourceClass.getQualifiedName() + ": [");
		sb.append(line + "] ");
		for(String modifier : getModifiersToString())
			sb.append(modifier + " ");
		sb.append(type.getQualifiedName() + " ");
		sb.append(name + " (" + getLexicalToString() + ")");
		return sb.toString();
	}
}
