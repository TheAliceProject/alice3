/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.project.ast;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.java.util.Sets;
import org.lgna.common.EachInTogetherRunnable;
import org.lgna.common.Resource;
import org.lgna.common.ThreadUtilities;
import org.lgna.project.code.CodeAppender;
import org.lgna.project.code.CodeOrganizer;
import org.lgna.project.resource.ResourcesTypeWrapper;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeGenerator extends SourceCodeGenerator{

	public static class Builder {
		public Builder() {
		}

		public Builder isLambdaSupported( boolean isLambdaSupported ) {
			this.isLambdaSupported = isLambdaSupported;
			return this;
		}

		public Builder isPublicStaticFinalFieldGetterDesired( boolean isPublicStaticFinalFieldGetterDesired ) {
			this.isPublicStaticFinalFieldGetterDesired = isPublicStaticFinalFieldGetterDesired;
			return this;
		}

		public Builder setResourcesTypeWrapper( ResourcesTypeWrapper resourcesTypeWrapper ) {
			this.resourcesTypeWrapper = resourcesTypeWrapper;
			return this;
		}

		public Builder addCommentsLocalizationBundleName( String bundleName ) {
			this.commentsLocalizationBundleName = bundleName;
			return this;
		}

		public Builder addImportOnDemandPackage( Package pckg ) {
			this.importOnDemandPackages.add( JavaPackage.getInstance( pckg ) );
			return this;
		}

		public Builder addCodeOrganizerDefinition( String key, CodeOrganizer.CodeOrganizerDefinition organizerDef ) {
			this.codeOrganizerDefinitionMap.put( key, organizerDef );
			return this;
		}

		public Builder addDefaultCodeOrganizerDefinition( CodeOrganizer.CodeOrganizerDefinition organizerDef ) {
			this.defaultCodeDefinitionOrganizer = organizerDef;
			return this;
		}

		public Builder addImportStaticMethod( java.lang.reflect.Method mthd ) {
			assert mthd != null;
			assert Modifier.isStatic( mthd.getModifiers() ) : mthd;
			this.importStaticMethods.add( JavaMethod.getInstance( mthd ) );
			return this;
		}

		public JavaCodeGenerator build() {
			return new JavaCodeGenerator( this );
		}

		private boolean isLambdaSupported;
		private boolean isPublicStaticFinalFieldGetterDesired;
		private ResourcesTypeWrapper resourcesTypeWrapper;
		private final List<JavaPackage> importOnDemandPackages = Lists.newLinkedList();
		private final List<JavaMethod> importStaticMethods = Lists.newLinkedList();
		private final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = Maps.newHashMap();
		private CodeOrganizer.CodeOrganizerDefinition defaultCodeDefinitionOrganizer;
		private String commentsLocalizationBundleName;
	}

	JavaCodeGenerator( Builder builder ) {
		super( builder.codeOrganizerDefinitionMap, builder.defaultCodeDefinitionOrganizer );
		this.isLambdaSupported = builder.isLambdaSupported;
		this.isPublicStaticFinalFieldGetterDesired = builder.isPublicStaticFinalFieldGetterDesired;
		this.resourcesTypeWrapper = builder.resourcesTypeWrapper;
		this.packagesMarkedForOnDemandImport = Collections.unmodifiableList( builder.importOnDemandPackages );
		this.staticMethodsMarkedForImport = Collections.unmodifiableList( builder.importStaticMethods );
		this.commentsLocalizationBundleName = builder.commentsLocalizationBundleName;
	}

	@Override int pushStatementDisabled() {
		int count = super.pushStatementDisabled();
		if( count == 1 ) {
			appendString( "\n/* disabled\n" );
		}
		return count;
	}

	@Override int popStatementDisabled() {
		int count = super.popStatementDisabled();
		if( count == 0 ) {
			appendString( "\n*/\n" );
		}
		return count;
	}

	@Override boolean isLambdaSupported() {
		return isLambdaSupported;
	}

	@Override boolean isPublicStaticFinalFieldGetterDesired() {
		return isPublicStaticFinalFieldGetterDesired;
	}

	@Override protected void appendAssignment() {
		appendChar( '=' );
	}

	private void appendResource( Resource resource ) {
		getCodeStringBuilder().append( ResourcesTypeWrapper.getTypeName() )
													.append( "." )
													.append( ResourcesTypeWrapper.getFixedName( resource ) );
	}

	@Override protected void appendResourceExpression( ResourceExpression resourceExpression ) {
		Resource resource = resourceExpression.resource.getValue();
		if( resource != null ) {
			if( resourcesTypeWrapper != null ) {
				UserField field = resourcesTypeWrapper.getFieldForResource( resource );
				if( field != null ) {
					getCodeStringBuilder().append( field.getDeclaringType().getName() );
					getCodeStringBuilder().append( "." );
					getCodeStringBuilder().append( field.getName() );
				} else {
					appendResource( resource );
				}
			} else {
				appendResource( resource );
			}
		} else {
			getCodeStringBuilder().append( "null" ); //todo?
		}
	}

	@Override protected void appendClassHeader( NamedUserType userType ) {
		appendString( "class " );
		appendTypeName( userType );
		appendString( " extends " );
		appendTypeName( userType.superType.getValue() );
		appendString( "{" );
	}

	@Override protected void appendClassFooter() {
		appendString( "}" );
	}

	@Override protected void appendSection( CodeOrganizer codeOrganizer, NamedUserType userType,
																					Map.Entry<String, List<CodeAppender>> entry ) {
		boolean shouldCollapseSection = codeOrganizer.shouldCollapseSection( entry.getKey() );
		appendSectionPrefix( userType, entry.getKey(), shouldCollapseSection );
		super.appendSection( codeOrganizer, userType, entry );
		appendSectionPostfix( userType, entry.getKey(), shouldCollapseSection );
	}

	private void appendSectionPrefix( AbstractType<?, ?, ?> declaringType, String sectionName,
																							boolean shouldCollapse ) {
		String sectionComment = getLocalizedMultiLineComment( declaringType, sectionName );
		if( sectionComment != null ) {
			getCodeStringBuilder().append( "\n\n" ).append( sectionComment ).append( "\n" );
		}
	}

	private void appendSectionPostfix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
		String sectionComment = getLocalizedMultiLineComment( declaringType, sectionName + ".end" );
		if( sectionComment != null ) {
			getCodeStringBuilder().append( "\n" ).append( sectionComment ).append( "\n" );
		}
	}

	@Override
	public void appendConstructor( NamedUserConstructor constructor ) {
		appendMemberPrefix( constructor );
		appendAccessLevel( constructor.getAccessLevel() );
		appendTypeName( constructor.getDeclaringType() );
		appendParameters( constructor );
		constructor.body.getValue().appendCode( this );
		appendMemberPostfix( constructor );
	}

	@Override protected void appendTypeName( AbstractType<?, ?, ?> type ) {
		if( type instanceof JavaType ) {
			JavaType javaType = (JavaType)type;
			if (!javaType.isPrimitive()) {
				JavaPackage javaPackage = javaType.getPackage();
				if( javaPackage != null ) {
					JavaType enclosingType = javaType.getEnclosingType();
					//todo: choose EnclosingTypeName.ClassName instead?
					if (enclosingType != null || !packagesMarkedForOnDemandImport.contains( javaPackage )) {
						typesToImport.add( javaType );
					} else {
						packagesToImportOnDemand.add( javaPackage );
					}
				}
				// else - should be covered already by the primitive check
			}
		}
		//todo: handle imports
		appendString( type.getName() );
	}

	@Override protected void appendTargetExpression( Expression target, AbstractMethod method ) {
		boolean isImportStatic = false;
		if( method instanceof JavaMethod ) {
			if( method.isStatic() ) {
				if( staticMethodsMarkedForImport.contains( method ) ) {
					isImportStatic = true;
				}
			}
		}
		if( isImportStatic ) {
			methodsToImportStatic.add( (JavaMethod)method );
		} else {
			appendExpression( target );
			appendChar( '.' );
		}
	}

	@Override public void appendMethod( UserMethod method ) {
		appendMemberPrefix( method );
		super.appendMethod( method );
		appendMemberPostfix( method );
	}

	@Override public void appendMethodHeader( AbstractMethod method ) {
		AbstractMethod overridenMethod = AstUtilities.getOverridenMethod( method );
		if( overridenMethod != null ) {
			appendString( "@Override " );
		}
		method.getAccessLevel().appendCode( this );
		if( method.isStatic() ) {
			appendString( "static " );
		}
		appendTypeName( method.getReturnType() );
		appendSpace();
		appendString( method.getName() );
		appendParameters( method );
	}

	@Override @Deprecated protected void todo( Object o ) {
		getCodeStringBuilder().append( "todo_" );
		getCodeStringBuilder().append( o );
	}

	@Override protected String getTextWithImports() {
		StringBuilder importsBuilder = getImports();
		importsBuilder.append( getCodeStringBuilder() );
		return importsBuilder.toString();
	}

	private StringBuilder getImports() {
		StringBuilder sb = new StringBuilder();
		for( JavaPackage packageToImportOnDemand : packagesToImportOnDemand ) {
			sb.append( "import " );
			sb.append( packageToImportOnDemand.getName() );
			sb.append( ".*;" );
		}
		for( JavaType typeToImport : typesToImport ) {
			JavaPackage pack = typeToImport.getPackage();
			if (!"java.lang".contentEquals( pack.getName() )) {
				sb.append( "import " );
				sb.append( typeToImport.getPackage().getName() );
				sb.append( '.' );
				JavaType enclosingType = typeToImport.getEnclosingType();
				if( enclosingType != null ) {
					sb.append( enclosingType.getName() );
					sb.append( '.' );
				}
				sb.append( typeToImport.getName() );
				sb.append( ';' );
			}
		}
		for( JavaMethod methodToImportStatic : methodsToImportStatic ) {
			sb.append( "import static " );
			sb.append( methodToImportStatic.getDeclaringType().getPackage().getName() );
			sb.append( '.' );
			sb.append( methodToImportStatic.getDeclaringType().getName() );
			sb.append( '.' );
			sb.append( methodToImportStatic.getName() );
			sb.append( ';' );
		}
		return sb;
	}

	@Override public void appendDoInOrder( DoInOrder doInOrder ) {
		appendString( "\n/*" );
		appendString( ResourceBundleUtilities
						.getStringFromSimpleNames( doInOrder.getClass(), "org.alice.ide.controlflow.Templates" ) );
		appendString( "*/ " );
		super.appendDoInOrder( doInOrder );
	}

	@Override public void appendDoTogether( DoTogether doTogether ) {
		JavaType threadUtilitiesType = JavaType.getInstance( ThreadUtilities.class );
		JavaMethod doTogetherMethod = threadUtilitiesType.getDeclaredMethod( "doTogether", Runnable[].class );
		TypeExpression callerExpression = new TypeExpression( threadUtilitiesType );
		appendTargetExpression( callerExpression, doTogetherMethod );
		appendString( doTogetherMethod.getName() );
		appendString( "(" );
		String prefix = "";
		for( Statement statement : doTogether.body.getValue().statements ) {
			appendString( prefix );
			if( isLambdaSupported() ) {
				appendString( "()->{" );
			} else {
				appendString( "new Runnable(){public void run(){" );
			}
			if( statement instanceof DoInOrder ) {
				DoInOrder doInOrder = (DoInOrder)statement;
				BlockStatement blockStatement = doInOrder.body.getValue();
				for( Statement subStatement : blockStatement.statements ) {
					subStatement.appendCode( this );
				}
			} else {
				statement.appendCode( this );
			}
			if( isLambdaSupported() ) {
				appendString( "}" );
			} else {
				appendString( "}}" );
			}
			prefix = ",";
		}
		appendString( ");" );
	}

	@Override public void appendEachInTogether( AbstractEachInTogether eachInTogether ) {
		JavaType threadUtilitiesType = JavaType.getInstance( ThreadUtilities.class );
		JavaMethod eachInTogetherMethod = threadUtilitiesType.getDeclaredMethod( "eachInTogether", EachInTogetherRunnable.class, Object[].class );
		TypeExpression callerExpression = new TypeExpression( threadUtilitiesType );
		appendTargetExpression( callerExpression, eachInTogetherMethod );
		appendString( eachInTogetherMethod.getName() );
		appendString( "(" );

		UserLocal itemValue = eachInTogether.item.getValue();
		AbstractType<?, ?, ?> itemType = itemValue.getValueType();
		if( isLambdaSupported() ) {
			appendString( "(" );
			appendTypeName( itemType );
			appendSpace();
			appendString( itemValue.getName() );
			appendString( ")->" );
		} else {
			appendString( "new " );
			appendTypeName( JavaType.getInstance( EachInTogetherRunnable.class ) );
			appendString( "<" );
			appendTypeName( itemType );
			appendString( ">() { public void run(" );
			appendTypeName( itemType );
			appendSpace();
			appendString( itemValue.getName() );
			appendString( ")" );
		}
		eachInTogether.body.getValue().appendCode( this );
		if (!isLambdaSupported()) {
			appendString( "}" );
		}
		Expression arrayOrIterableExpression = eachInTogether.getArrayOrIterableProperty().getValue();
		if( arrayOrIterableExpression instanceof ArrayInstanceCreation ) {
			ArrayInstanceCreation arrayInstanceCreation = (ArrayInstanceCreation)arrayOrIterableExpression;
			for( Expression variableLengthExpression : arrayInstanceCreation.expressions ) {
				appendString( "," );
				appendExpression( variableLengthExpression );
			}
		} else {
			appendString( "," );
			appendExpression( arrayOrIterableExpression );
		}
		appendString( ");" );
	}

	private void appendMemberPrefix( AbstractMember member ) {
		String memberComment = getLocalizedMultiLineComment( member.getDeclaringType(), member.getName() );
		if( memberComment != null ) {
			getCodeStringBuilder().append( "\n" ).append( memberComment ).append( "\n" );
		}
	}

	private void appendMemberPostfix( AbstractMember member ) {
		String memberComment = getLocalizedMultiLineComment( member.getDeclaringType(), member.getName() + ".end" );
		if( memberComment != null ) {
			getCodeStringBuilder().append( "\n" ).append( memberComment ).append( "\n" );
		}
	}

	@Override public void formatMultiLineComment( String comment ) {
		appendChar( '\n' );
		for( String line : splitIntoLines(comment) ) {
			appendString( "// " );
			appendString( line );
			appendChar( '\n' );
		}
	}

	private static String[] splitIntoLines( String src ) {
		return src.split( "\n" );
	}

	private String formatBlockComment( String commentText ) {
		String[] commentLines = splitIntoLines( commentText );
		StringBuilder sb = new StringBuilder();

		sb.append( "/* " );
		for( int i = 0; i < commentLines.length; i++ ) {
			sb.append( commentLines[ i ] );
			if( i < ( commentLines.length - 1 ) ) {
				sb.append( "\n * " ); // End each line with a new line and start each line with " *"
			}
		}
		sb.append( " */" );
		return sb.toString();
	}

	private String getLocalizedMultiLineComment( AbstractType<?, ?, ?> type, String sectionName ) {
		String comment = getLocalizedComment( type, sectionName, Locale.getDefault() );
		if( comment != null ) {
			comment = formatBlockComment( comment );
		}
		return comment;
	}

	@Override public String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, Locale locale ) {
		if( commentsLocalizationBundleName != null ) {
			ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle( commentsLocalizationBundleName, locale );
			String key;
			AbstractType<?, ?, ?> t = type;
			boolean done = false;
			String returnVal = null;
			do {
				if( t != null ) {
					key = t.getName() + "." + itemName;
					t = t.getSuperType();
				} else {
					key = itemName;
					done = true;
				}
				try {
					returnVal = resourceBundle.getString( key );
					break;
				} catch( RuntimeException re ) {
					//pass;
				}
			} while( !done );
			if( returnVal != null ) {
				returnVal = returnVal.replaceAll( "<classname>", type.getName() );
				returnVal = returnVal.replaceAll( "<objectname>", itemName );
			}
			return returnVal;
		}
		return null;
	}

	@Override public void appendField( UserField field ) {
		appendMemberPrefix(field);
		appendAccessLevel( field.getAccessLevel() );
		if( field.isStatic() ) {
			appendString( "static " );
		}
		if( field.isFinal() ) {
			appendString( "final " );
		}
		super.appendField( field );
		appendMemberPostfix(field);
	}

	@Override public void appendGetter( Getter getter ) {
		appendMemberPrefix( getter );
		super.appendGetter( getter );
		appendMemberPostfix( getter );
	}

	@Override public void appendSetter( Setter setter ) {
		appendMemberPrefix( setter );
		super.appendSetter( setter );
		appendMemberPostfix( setter );
	}

	@Override public void appendLambda( UserLambda lambda ) {
		appendMemberPrefix( lambda );
		super.appendLambda( lambda );
		appendMemberPostfix( lambda );
	}

	private final boolean isLambdaSupported;
	private final boolean isPublicStaticFinalFieldGetterDesired;

	private final Set<JavaPackage> packagesToImportOnDemand = Sets.newHashSet();
	private final Set<JavaType> typesToImport = Sets.newHashSet();
	private final Set<JavaMethod> methodsToImportStatic = Sets.newHashSet();

	private final List<JavaPackage> packagesMarkedForOnDemandImport;
	private final List<JavaMethod> staticMethodsMarkedForImport;

	private final ResourcesTypeWrapper resourcesTypeWrapper;

	private final String commentsLocalizationBundleName;
}
