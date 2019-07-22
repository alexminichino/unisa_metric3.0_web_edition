package it.unisa.metric.struct.table;

import org.eclipse.jdt.core.dom.Modifier;
/**
 * Abstract class for table record.
 * Contains lexical values for fields and variables.
 * Contains bit masks for fields and variables modifiers.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public abstract class Record {
	
	/**
	 * Lexical value for single letter names.
	 */
	public static final int SINGLE_LETTER = 1;
	/**
	 * Lexical value for one word names.
	 */
	public static final int ONE_WORD = 2;
	/**
	 * Lexical value for more words names.
	 */
	public static final int MORE_WORDS = 3;
	/**
	 * Lexical values for other names.
	 */
	public static final int OTHER = 4;
	
	/**
	 * Bit mask for <code>public</code> modifier.
	 */
	public static final int PUBLIC = Modifier.PUBLIC;
	/**
	 * Bit mask for <code>protected</code> modifier.
	 */
	public static final int PROTECTED = Modifier.PROTECTED;
	/**
	 * Bit mask for <code>private</code> modifier.
	 */
	public static final int PRIVATE = Modifier.PRIVATE;
	/**
	 * Bit mask for <code>transient</code> modifier.
	 */
	public static final int TRANSIENT = Modifier.TRANSIENT;
	/**
	 * Bit mask for <code>volatile</code> modifier.
	 */
	public static final int VOLATILE = Modifier.VOLATILE;
	/**
	 * Bit mask for <code>static</code> modifier.
	 */
	public static final int STATIC = Modifier.STATIC;
	/**
	 * Bit mask for <code>final</code> modifier.
	 */
	public static final int FINAL = Modifier.FINAL;
	
	/**
	 * This record instance represent a field?
	 * @return <code>True</code> if this Record instance represent a field; <code>false</code> otherwise.
	 */
	public abstract boolean isField();
	/**
	 * This record instance represent a variable?
	 * @return <code>True</code> if this Recordo instance represent a variable; <code>false</code> otherwise.
	 */
	public abstract boolean isVariable();
	/**
	 * This field/variable type is primitive?
	 * @return <code>True</code> if field/variable type is primitive; <code>false</code> otherwise.
	 */
	public abstract boolean isPrimitive();
	/**
	 * This field/variable type is referral type?
	 * @return <code>True</code> if field/variable type is referral type; <code>false</code> otherwise.
	 */
	public abstract boolean isReferralType();
	/**
	 * This field/variable is an array?
	 * @return <code>True</code> if is an array; <code>false</code> otherwise.
	 */
	public abstract boolean isArray();
	/**
	 * Gets name lexical value.
	 * @return Name lexical value.
	 */
	public abstract int getLexical();
	
	/**
	 * Gets a FieldRecord instance if this is one.
	 * @return FieldRecord instance.
	 */
	public FieldRecord getFieldRecord() {
		return (FieldRecord) this;
	}
	
	/**
	 * Gets a VariableRecord instance if this is one.
	 * @return VariableRecord instance.
	 */
	public VariableRecord getVariableRecord() {
		return (VariableRecord) this;
	}
	
	/**
	 * Gets lexical value from name.
	 * @param name Field/variable name.
	 * @return Name lexical value.
	 */
	protected int calculateLexical(String name) {
		if(name.length() == 1)
			return SINGLE_LETTER;
		if(name.matches(".*[0-9].*"))
			return OTHER;
		while(name.startsWith("_"))
			name = name.substring(1);
		while(name.matches(".*_+$"))
			name = name.substring(0, name.length() - 1);
		if(name.matches("[A-Z]+"))
			return ONE_WORD;
		if(name.matches(".*[A-Z].*"))
			return MORE_WORDS;
		if(name.matches(".*_+.*"))
			if(name.split("_").length > 1)
				return MORE_WORDS;
		return ONE_WORD;
	}
	
}
