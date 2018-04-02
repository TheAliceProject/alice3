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
	private final String NODE_DISABLE = "*<";
	private final String NODE_ENABLE = ">*";
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

	@Override protected String getTextWithImports() {
		return getText();
	}

	/** Class structure **/

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

	/** Methods and Fields **/

	@Override
	public void appendConstructor( NamedUserConstructor constructor ) {
		appendIndent();
		appendTypeName( constructor.getDeclaringType() );
		appendParameters( constructor );
		appendStatement( constructor.body.getValue() );
		appendNewLine();
	}

	@Override public void appendMethod( UserMethod method ) {
		appendNewLine();
		appendIndent();
		super.appendMethod( method );
		appendNewLine();
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

	/** Statements **/

	@Override public void appendLocalDeclaration( LocalDeclarationStatement stmt ) {
		appendSingleStatement( stmt, () -> {
			UserLocal localVar = stmt.local.getValue();
			appendTypeName( localVar.getValueType() );
			appendSpace();
			appendString( localVar.getValidName() );
			appendAssignmentOperator();
			appendExpression( stmt.initializer.getValue() );
		} );
	}

	@Override protected void appendStatementCompletion( Statement stmt ) {
		super.appendStatementCompletion( stmt );
		appendStatementEnd( stmt );
	}

	@Override protected void appendStatementCompletion() {
		super.appendStatementCompletion();
		appendNewLine();
	}

	private void appendStatementEnd( Statement stmt ) {
		if (!stmt.isEnabled.getValue()) {
			appendSpace();
			appendString( NODE_ENABLE);
		}
		appendNewLine();
	}

	@Override protected void pushStatementDisabled() {
		appendString( NODE_DISABLE );
		super.pushStatementDisabled();
	}

	@Override protected void appendArgument( AbstractParameter parameter, AbstractArgument argument ) {
		String label = parameter.getName();
		// TODO Better handle missing names when parameter.getValueType().isEnum()
		if (null == label) {
			label ="unknown";
		}
		appendString( label );
		appendString( ": " );
		argument.appendCode( this );
	}

	@Override protected void appendSingleStatement( Statement stmt, Runnable appender ) {
		appendIndent( stmt );
		super.appendSingleStatement( stmt, appender );
	}

	@Override protected void appendSingleCodeLine( Runnable appender ) {
		appendIndent();
		super.appendSingleCodeLine( appender );
	}

	/** Code Flow **/

	@Override protected void appendCodeFlowStatement( Statement stmt, Runnable appender ) {
		appendIndent( stmt );
		appender.run();
		appendStatementEnd( stmt );
	}

	@Override public void appendCountLoop( CountLoop loop ) {
		appendCodeFlowStatement(loop, () -> {
			appendString( "countUpTo( " );
			appendString( loop.getVariableName() );
			appendString( " < " );
			appendExpression( loop.count.getValue() );
			appendString( " )" );
			appendStatement( loop.body.getValue() );
		});
	}

	@Override protected void appendForEachToken() {
		appendString( "forEach");
	}

	@Override protected void appendInEachToken() {
		appendString( " in " );
	}

	@Override public void appendDoInOrder( DoInOrder doInOrder ) {
		appendLabeledCodeFlow( doInOrder, "doInOrder" );
	}

	@Override public void appendDoTogether( DoTogether doTogether ) {
		appendLabeledCodeFlow( doTogether, "doTogether" );
	}

	private void appendLabeledCodeFlow( AbstractStatementWithBody stmt, String label ) {
		appendCodeFlowStatement(stmt, () -> {
			appendString( label );
			appendStatement( stmt.body.getValue() );
		});
	}

	@Override public void appendEachInTogether( AbstractEachInTogether eachInTogether ) {
		appendCodeFlowStatement(eachInTogether, () -> {
			appendString( "eachTogether" );
			UserLocal itemValue = eachInTogether.item.getValue();
			Expression items = eachInTogether.getArrayOrIterableProperty().getValue();
			appendEachItemsClause( itemValue, items );
			appendStatement( eachInTogether.body.getValue() );
		});
	}

	/** Expressions **/

	@Override protected void appendTargetExpression( Expression target, AbstractMethod method ) {
		appendExpression( target );
		appendChar( '.' );
	}

	@Override protected void appendResourceExpression( ResourceExpression resourceExpression ) {
		appendString( "//TODO append resources" );
		appendString( resourceExpression.toString() );
	}

	/** Comments **/

	@Override protected void appendSingleLineComment( String line ) {
		appendIndent();
		super.appendSingleLineComment( line );
	}

		@Override public String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, Locale locale ) {
		return "//";
	}

	/** Primitives and syntax **/

	@Override protected void openBlock() {
		appendString( " {\n" );
		pushIndent();
	}

	@Override protected void closeBlock() {
		popIndent();
		appendIndent();
		super.closeBlock();
	}

	@Override protected void appendAssignmentOperator() {
		appendString( " <- " );
	}

	@Override protected void appendTypeName( AbstractType<?, ?, ?> type ) {
		appendString( type.getName() );
	}

	/** Formatting **/

	private void pushIndent() {
		indent++;
	}

	private void popIndent() {
		indent--;
	}

	private void appendIndent() {
		appendString( StringUtils.repeat( INDENTION, indent ) );
	}

	private void appendIndent( Statement stmt ) {
		final int indent = stmt.isEnabled.getValue() ? this.indent : this.indent - 1;
		appendString( StringUtils.repeat( INDENTION, indent ) );
	}

	/** **/

	@Override protected void todo( Object o ) {
		appendString( "todo_" );
		getCodeStringBuilder().append( o );
	}
}
