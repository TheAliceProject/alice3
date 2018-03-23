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

import java.util.Locale;

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

		public Builder setResourcesTypeWrapper( org.lgna.project.resource.ResourcesTypeWrapper resourcesTypeWrapper ) {
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

		public Builder addCodeOrganizerDefinition( String key, org.lgna.project.code.CodeOrganizer.CodeOrganizerDefinition organizerDef ) {
			this.codeOrganizerDefinitionMap.put( key, organizerDef );
			return this;
		}

		public Builder addDefaultCodeOrganizerDefinition( org.lgna.project.code.CodeOrganizer.CodeOrganizerDefinition organizerDef ) {
			this.defaultCodeDefinitionOrganizer = organizerDef;
			return this;
		}

		public Builder addImportStaticMethod( java.lang.reflect.Method mthd ) {
			assert mthd != null;
			assert java.lang.reflect.Modifier.isStatic( mthd.getModifiers() ) : mthd;
			this.importStaticMethods.add( JavaMethod.getInstance( mthd ) );
			return this;
		}

		public JavaCodeGenerator build() {
			return new JavaCodeGenerator( this );
		}

		private boolean isLambdaSupported;
		private boolean isPublicStaticFinalFieldGetterDesired;
		private org.lgna.project.resource.ResourcesTypeWrapper resourcesTypeWrapper;
		private final java.util.List<JavaPackage> importOnDemandPackages = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private final java.util.List<JavaMethod> importStaticMethods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private final java.util.Map<String, org.lgna.project.code.CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		private org.lgna.project.code.CodeOrganizer.CodeOrganizerDefinition defaultCodeDefinitionOrganizer;
		private String commentsLocalizationBundleName;
	}

	protected JavaCodeGenerator( Builder builder ) {
		super( builder.codeOrganizerDefinitionMap, builder.defaultCodeDefinitionOrganizer );
		this.isLambdaSupported = builder.isLambdaSupported;
		this.isPublicStaticFinalFieldGetterDesired = builder.isPublicStaticFinalFieldGetterDesired;
		this.resourcesTypeWrapper = builder.resourcesTypeWrapper;
		this.packagesMarkedForOnDemandImport = java.util.Collections.unmodifiableList( builder.importOnDemandPackages );
		this.staticMethodsMarkedForImport = java.util.Collections.unmodifiableList( builder.importStaticMethods );
		this.commentsLocalizationBundleName = builder.commentsLocalizationBundleName;
	}

	@Override int pushStatementDisabled() {
		int count = super.pushStatementDisabled();
		if( count == 1 ) {
			this.appendString( "\n/* disabled\n" );
		}
		return count;
	}

	@Override int popStatementDisabled() {
		int count = super.popStatementDisabled();
		if( count == 0 ) {
			this.appendString( "\n*/\n" );
		}
		return count;
	}

	@Override boolean isLambdaSupported() {
		return this.isLambdaSupported;
	}

	boolean isPublicStaticFinalFieldGetterDesired() {
		return this.isPublicStaticFinalFieldGetterDesired;
	}

	@Override void appendInt( int n ) {
		if( n == Integer.MAX_VALUE ) {
			this.appendString( "Integer.MAX_VALUE" );
		} else if( n == Integer.MIN_VALUE ) {
			this.appendString( "Integer.MIN_VALUE" );
		} else {
			getCodeStringBuilder().append( n );
		}
	}

	@Override void appendFloat( float f ) {
		if( Float.isNaN( f ) ) {
			this.appendString( "Float.NaN" );
		} else if( f == Float.POSITIVE_INFINITY ) {
			this.appendString( "Float.POSITIVE_INFINITY" );
		} else if( f == Float.NEGATIVE_INFINITY ) {
			this.appendString( "Float.NEGATIVE_INFINITY" );
		} else {
			getCodeStringBuilder().append( f );
			this.appendChar( 'f' );
		}
	}

	@Override void appendDouble( double d ) {
		if( Double.isNaN( d ) ) {
			this.appendString( "Double.NaN" );
		} else if( d == Double.POSITIVE_INFINITY ) {
			this.appendString( "Double.POSITIVE_INFINITY" );
		} else if( d == Double.NEGATIVE_INFINITY ) {
			this.appendString( "Double.NEGATIVE_INFINITY" );
		} else {
			getCodeStringBuilder().append( d );
		}
	}

	private void appendResource( org.lgna.common.Resource resource ) {
		getCodeStringBuilder().append( org.lgna.project.resource.ResourcesTypeWrapper.getTypeName() )
													.append( "." )
													.append( org.lgna.project.resource.ResourcesTypeWrapper.getFixedName( resource ) );
	}

	@Override void appendResourceExpression( ResourceExpression resourceExpression ) {
		org.lgna.common.Resource resource = resourceExpression.resource.getValue();
		if( resource != null ) {
			if( this.resourcesTypeWrapper != null ) {
				UserField field = this.resourcesTypeWrapper.getFieldForResource( resource );
				if( field != null ) {
					getCodeStringBuilder().append( field.getDeclaringType().getName() );
					getCodeStringBuilder().append( "." );
					getCodeStringBuilder().append( field.getName() );
				} else {
					this.appendResource( resource );
				}
			} else {
				this.appendResource( resource );
			}
		} else {
			getCodeStringBuilder().append( "null" ); //todo?
		}
	}

	@Override void appendTypeName( AbstractType<?, ?, ?> type ) {
		if( type instanceof JavaType ) {
			JavaType javaType = (JavaType)type;
			if (!javaType.isPrimitive()) {
				JavaPackage javaPackage = javaType.getPackage();
				if( javaPackage != null ) {
					JavaType enclosingType = javaType.getEnclosingType();
					//todo: choose EnclosingTypeName.ClassName instead?
					if (enclosingType != null || !packagesMarkedForOnDemandImport.contains( javaPackage )) {
						this.typesToImport.add( javaType );
					} else {
						this.packagesToImportOnDemand.add( javaPackage );
					}
				}
				// else - should be covered already by the primitive check
			}
		}
		//todo: handle imports
		this.appendString( type.getName() );
	}

	@Override void appendCallerExpression( Expression callerExpression, AbstractMethod method ) {
		boolean isImportStatic = false;
		if( method instanceof JavaMethod ) {
			if( method.isStatic() ) {
				if( this.staticMethodsMarkedForImport.contains( method ) ) {
					isImportStatic = true;
				}
			}
		}
		if( isImportStatic ) {
			this.methodsToImportStatic.add( (JavaMethod)method );
		} else {
			this.appendExpression( callerExpression );
			this.appendChar( '.' );
		}
	}

	@Override void appendParameters( Code code ) {
		this.appendChar( '(' );
		String prefix = "";
		int i = 0;
		for( AbstractParameter parameter : code.getRequiredParameters() ) {
			this.appendString( prefix );
			this.appendTypeName( parameter.getValueType() );
			this.appendSpace();
			String parameterName = parameter.getValidName();
			this.appendString( parameterName != null ? parameterName : "p" + i );
			prefix = ",";
			i += 1;
		}
		this.appendChar( ')' );
	}

	@Override void appendMethodHeader( AbstractMethod method ) {
		AbstractMethod overridenMethod = AstUtilities.getOverridenMethod( method );
		if( overridenMethod != null ) {
			this.appendString( "@Override " );
		}
		method.getAccessLevel().appendCode( this );
		if( method.isStatic() ) {
			this.appendString( "static " );
		}
		this.appendTypeName( method.getReturnType() );
		this.appendSpace();
		this.appendString( method.getName() );
		this.appendParameters( method );
	}

	private void appendArgument( AbstractArgument argument ) {
		AbstractParameter parameter = argument.parameter.getValue();
		AbstractType<?, ?, ?> type = argument.getExpressionTypeForParameterType( parameter.getValueType() );
		pushTypeForLambda( type );
		try {
			argument.appendCode( this );
		} finally {
			assert popTypeForLambda() == type;
		}
	}

	@Override void appendArguments( ArgumentOwner argumentOwner ) {
		String prefix = "";
		for( SimpleArgument argument : argumentOwner.getRequiredArgumentsProperty() ) {
			this.appendString( prefix );
			this.appendArgument( argument );
			prefix = ",";
		}
		for( SimpleArgument argument : argumentOwner.getVariableArgumentsProperty() ) {
			this.appendString( prefix );
			this.appendArgument( argument );
			prefix = ",";
		}
		for( JavaKeyedArgument argument : argumentOwner.getKeyedArgumentsProperty() ) {
			this.appendString( prefix );
			this.appendArgument( argument );
			prefix = ",";
		}
	}

	@Override @Deprecated
	void todo( Object o ) {
		getCodeStringBuilder().append( "todo_" );
		getCodeStringBuilder().append( o );
	}

	private String getImportsPrefix() {
		return "";
	}

	private String getImportsPostfix() {
		return "";
	}

	@Override String getText( boolean areImportsDesired ) {
		StringBuilder rvStringBuilder = new StringBuilder();
		if( areImportsDesired ) {
			rvStringBuilder.append( this.getImportsPrefix() );
			for( JavaPackage packageToImportOnDemand : this.packagesToImportOnDemand ) {
				rvStringBuilder.append( "import " );
				rvStringBuilder.append( packageToImportOnDemand.getName() );
				rvStringBuilder.append( ".*;" );
			}
			for( JavaType typeToImport : this.typesToImport ) {
				JavaPackage pack = typeToImport.getPackage();
				if( "java.lang".contentEquals( pack.getName() ) ) {
					//pass
				} else {
					rvStringBuilder.append( "import " );
					rvStringBuilder.append( typeToImport.getPackage().getName() );
					rvStringBuilder.append( '.' );
					JavaType enclosingType = typeToImport.getEnclosingType();
					if( enclosingType != null ) {
						rvStringBuilder.append( enclosingType.getName() );
						rvStringBuilder.append( '.' );
					}
					rvStringBuilder.append( typeToImport.getName() );
					rvStringBuilder.append( ';' );
				}
			}
			for( JavaMethod methodToImportStatic : this.methodsToImportStatic ) {
				rvStringBuilder.append( "import static " );
				rvStringBuilder.append( methodToImportStatic.getDeclaringType().getPackage().getName() );
				rvStringBuilder.append( '.' );
				rvStringBuilder.append( methodToImportStatic.getDeclaringType().getName() );
				rvStringBuilder.append( '.' );
				rvStringBuilder.append( methodToImportStatic.getName() );
				rvStringBuilder.append( ';' );
			}
			rvStringBuilder.append( this.getImportsPostfix() );
		}
		rvStringBuilder.append( getCodeStringBuilder() );
		return rvStringBuilder.toString();
	}

	@Override protected String getMemberPrefix( AbstractMember member ) {
		String memberComment = this.getLocalizedMultiLineComment( member.getDeclaringType(), member.getName(), java.util.Locale.getDefault() );
		if( memberComment != null ) {
			return "\n" + memberComment + "\n";
		}
		else {
			return "";
		}
	}

	@Override protected String getMemberPostfix( AbstractMember member ) {
		String memberComment = this.getLocalizedMultiLineComment( member.getDeclaringType(), member.getName() + ".end", java.util.Locale.getDefault() );
		if( memberComment != null ) {
			return "\n" + memberComment + "\n";
		}
		else {
			return "";
		}
	}

	@Override protected String getSectionPrefix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
		String sectionComment = this.getLocalizedMultiLineComment( declaringType, sectionName, java.util.Locale.getDefault() );
		if( sectionComment != null ) {
			return "\n\n" + sectionComment + "\n";
		}
		else {
			return "";
		}
	}

	@Override protected String getSectionPostfix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
		String sectionComment = this.getLocalizedMultiLineComment( declaringType, sectionName + ".end", java.util.Locale.getDefault() );
		if( sectionComment != null ) {
			return "\n" + sectionComment + "\n";
		}
		else {
			return "";
		}
	}

	private static String[] splitIntoLines( String src ) {
		return src.split( "\n" );
	}

	private String formatMultiLineComment( String commentText ) {
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

	private String getLocalizedMultiLineComment( AbstractType<?, ?, ?> type, String sectionName, Locale locale ) {
		String comment = localizedComment( type, sectionName, locale );
		if( comment != null ) {
			comment = formatMultiLineComment( comment );
		}
		return comment;
	}

	@Override String localizedComment( AbstractType<?, ?, ?> type, String itemName, Locale locale ) {
		if( commentsLocalizationBundleName != null ) {
			java.util.ResourceBundle resourceBundle = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( commentsLocalizationBundleName, locale );
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

	private final boolean isLambdaSupported;
	private final boolean isPublicStaticFinalFieldGetterDesired;

	private final java.util.Set<JavaPackage> packagesToImportOnDemand = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
	private final java.util.Set<JavaType> typesToImport = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
	private final java.util.Set<JavaMethod> methodsToImportStatic = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();

	private final java.util.List<JavaPackage> packagesMarkedForOnDemandImport;
	private final java.util.List<JavaMethod> staticMethodsMarkedForImport;

	private final org.lgna.project.resource.ResourcesTypeWrapper resourcesTypeWrapper;

	private final String commentsLocalizationBundleName;
}
