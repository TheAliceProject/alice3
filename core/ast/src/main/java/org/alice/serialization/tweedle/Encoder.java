package org.alice.serialization.tweedle;

import org.alice.serialization.DispatchingEncoder;
import org.lgna.project.ast.*;
import org.lgna.project.code.CodeOrganizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Encoder extends SourceCodeGenerator implements DispatchingEncoder {
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

	@Override protected void appendClassHeader( NamedUserType userType ) {
		getCodeStringBuilder().append( "class " ).append( userType.getName() )
			 .append( " extends " ).append( userType.getSuperType().getName() );
		// TODO Only show for models and replace with resource identifier
		//		if (userType.isModel())
		getCodeStringBuilder().append( " models " ).append( userType.getName() )
			 .append( " {\n" );
	}

	@Override protected void appendClassFooter() {
		getCodeStringBuilder().append( "\n}" );
	}

	@Override protected void appendInt( int n ) {

	}

	@Override protected void appendFloat( float f ) {

	}

	@Override protected void appendDouble( double d ) {

	}

	@Override protected void appendResourceExpression( ResourceExpression resourceExpression ) {

	}

	@Override protected void appendTypeName( AbstractType<?, ?, ?> type ) {

	}

	@Override protected void appendCallerExpression( Expression callerExpression, AbstractMethod method ) {

	}

	@Override public void appendParameters( Code code ) {

	}

	@Override public void appendMethodHeader( AbstractMethod method ) {

	}

	@Override public void appendArguments( ArgumentOwner argumentOwner ) {

	}

	@Override protected void todo( Object o ) {

	}

	@Override protected String getTextWithImports() {
		return null;
	}

	@Override protected void appendMemberPrefix( AbstractMember member ) {

	}

	@Override protected void appendMemberPostfix( AbstractMember member ) {

	}

	@Override protected void appendSectionPrefix( AbstractType<?, ?, ?> declaringType, String sectionName,
																							 boolean shouldCollapse ) {
	}

	@Override protected void appendSectionPostfix( AbstractType<?, ?, ?> declaringType, String sectionName,
																								boolean shouldCollapse ) {
	}

	@Override public void formatMultiLineComment( String value ) {

	}

	@Override public String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, Locale locale ) {
		return "";
	}
}
