package org.alice.serialization.tweedle;

import org.alice.serialization.DispatchingEncoder;
import org.apache.commons.lang.StringUtils;
import org.lgna.project.ast.*;
import org.lgna.project.code.CodeOrganizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Encoder extends SourceCodeGenerator implements DispatchingEncoder {
	private final String INDENTION = "  ";
	private int indent = 0;
	private static final HashMap<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = new HashMap<>();
	static {
		codeOrganizerDefinitionMap.put("Scene", CodeOrganizer.sceneClassCodeOrganizer );
		codeOrganizerDefinitionMap.put("Program", CodeOrganizer.programClassCodeOrganizer );
	}

	private final Set<AbstractDeclaration> terminalNodes;

	Encoder( Set<AbstractDeclaration> terminals ) {
		super(codeOrganizerDefinitionMap, CodeOrganizer.defaultCodeOrganizer);
		terminalNodes = terminals ;
	}

	Encoder() {
		super(codeOrganizerDefinitionMap, CodeOrganizer.defaultCodeOrganizer);
		terminalNodes = new HashSet<>();
	}

	public String encode( AbstractNode node ) {
		node.encode( this );
		return getCodeStringBuilder().toString();
	}

	@Override
	public void encodeNamedUserType( NamedUserType userType ) {
		userType.generateCode( this );
	}

	private void appendNewLine() {
		appendChar( '\n' );
	}

	@Override protected void appendClassHeader( NamedUserType userType ) {
		getCodeStringBuilder().append( "class " ).append( userType.getName() )
			 .append( " extends " ).append( userType.getSuperType().getName() );
		// TODO Only show for models and replace with resource identifier
		//		if (userType.isModel())
		getCodeStringBuilder().append( " models " ).append( userType.getName() );
		openBlock();
	}

	@Override protected void appendClassFooter() {
		closeBlock();
	}

	@Override
	public void appendConstructor( NamedUserConstructor constructor ) {
		appendIndent();
		appendTypeName( constructor.getDeclaringType() );
		appendParameters( constructor );
		appendStatement( constructor.body.getValue() );
		appendNewLine();
	}

	@Override protected void appendResourceExpression( ResourceExpression resourceExpression ) {
		appendString( "//TODO append resources" );
		appendString( resourceExpression.toString() );
	}

	@Override protected void appendTypeName( AbstractType<?, ?, ?> type ) {
		appendString( type.getName() );
	}

	@Override protected void appendTargetExpression( Expression target, AbstractMethod method ) {
		appendExpression( target );
		appendChar( '.' );
	}

	@Override public void appendMethodHeader( AbstractMethod method ) {
		if( method.isStatic() ) {
			appendString( "static " );
		}
		appendTypeName( method.getReturnType() );
		appendSpace();
		appendString( method.getName() );
		appendParameters( method );
	}


	@Override protected void openBlock() {
		appendString( " {\n" );
		pushIndent();
	}

	private void pushIndent() {
		indent++;
	}

	@Override protected void closeBlock() {
		popIndent();
		appendIndent();
		super.closeBlock();
	}

	private void popIndent() {
		indent--;
	}

	private void appendIndent() {
		appendString( StringUtils.repeat( INDENTION, indent ) );
	}

	@Override public void appendLocalDeclaration( LocalDeclarationStatement stmt ) {
		appendSingleStatement( () -> {
			UserLocal localVar = stmt.local.getValue();
			appendTypeName( localVar.getValueType() );
			appendSpace();
			appendString( localVar.getValidName() );
			appendAssignment();
			appendExpression( stmt.initializer.getValue() );
		} );
	}


	@Override protected int pushStatementDisabled() {
		appendString( "**" );
		return super.pushStatementDisabled();
	}

	@Override protected void appendStatementCompletion() {
		super.appendStatementCompletion();
		appendNewLine();
	}

	@Override public void appendMethod( UserMethod method ) {
		appendNewLine();
		appendIndent();
		super.appendMethod( method );
		appendNewLine();
	}

	@Override protected void appendSingleStatement( Runnable appender ) {
		appendIndent();
		super.appendSingleStatement( appender );
	}

	@Override protected void todo( Object o ) {
		appendString( "todo_" );
		getCodeStringBuilder().append( o );
	}

	@Override protected String getTextWithImports() {
		return getText();
	}

	@Override public void formatMultiLineComment( String value ) {
	}

	@Override public String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, Locale locale ) {
		return "//";
	}

	@Override protected void appendAssignment() {
		appendString( " <- " );
	}

	@Override public void appendConditional( ConditionalStatement stmt ) {
		appendIndent();
		super.appendConditional( stmt );
		appendNewLine();
	}

	@Override public void appendCountLoop( CountLoop loop ) {
		appendIndent();
		super.appendCountLoop( loop );
		appendNewLine();
	}

	@Override public void appendForEach( AbstractForEachLoop loop ) {
		appendIndent();
		super.appendForEach( loop );
		appendNewLine();
	}

	@Override protected void appendForEachToken() {
		appendString( "forEach");
	}

	@Override protected void appendInEachToken() {
		appendString( " in " );
	}

	@Override public void appendWhileLoop( WhileLoop loop ) {
		appendIndent();
		super.appendWhileLoop( loop );
		appendNewLine();
	}

	@Override public void appendGetter( Getter getter ) {
		appendIndent();
		super.appendGetter( getter );
		appendNewLine();
	}

	@Override public void appendSetter( Setter setter ) {
		appendIndent();
		super.appendSetter( setter );
		appendNewLine();
	}

	@Override public void appendDoInOrder( DoInOrder doInOrder ) {
		appendIndent();
		appendString( "doInOrder" );
		super.appendDoInOrder( doInOrder );
		appendNewLine();
	}

	@Override public void appendDoTogether( DoTogether doTogether ) {
		appendIndent();
		appendString( "doTogether" );
		appendStatement( doTogether.body.getValue() );
		appendNewLine();
	}

	@Override public void appendEachInTogether( AbstractEachInTogether eachInTogether ) {
		appendIndent();
		appendString( "eachTogether" );
		UserLocal itemValue = eachInTogether.item.getValue();
		Expression items = eachInTogether.getArrayOrIterableProperty().getValue();
		appendEachItemsClause( itemValue, items );
		appendStatement( eachInTogether.body.getValue() );
		appendNewLine();
	}
}
