/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeGenerator {
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
	};

	protected JavaCodeGenerator( Builder builder ) {
		this.isLambdaSupported = builder.isLambdaSupported;
		this.isPublicStaticFinalFieldGetterDesired = builder.isPublicStaticFinalFieldGetterDesired;
		this.resourcesTypeWrapper = builder.resourcesTypeWrapper;
		this.packagesMarkedForOnDemandImport = java.util.Collections.unmodifiableList( builder.importOnDemandPackages );
		this.staticMethodsMarkedForImport = java.util.Collections.unmodifiableList( builder.importStaticMethods );
		this.codeOrganizerMap = java.util.Collections.unmodifiableMap( builder.codeOrganizerDefinitionMap );
		this.defaultCodeOrganizer = builder.defaultCodeDefinitionOrganizer;
		this.commentsLocalizationBundleName = builder.commentsLocalizationBundleName;
	}

	/* package-private */void pushStatementDisabled() {
		this.statementDisabledCount++;
		if( this.statementDisabledCount == 1 ) {
			this.appendString( "\n/* disabled\n" );
		}
	}

	/* package-private */void popStatementDisabled() {
		if( this.statementDisabledCount == 1 ) {
			this.appendString( "\n*/\n" );
		}
		this.statementDisabledCount--;
	}

	/* package-private */boolean isLambdaSupported() {
		return this.isLambdaSupported;
	}

	/* package-private */boolean isPublicStaticFinalFieldGetterDesired() {
		return this.isPublicStaticFinalFieldGetterDesired;
	}

	/* package-private */void appendBoolean( boolean b ) {
		this.codeStringBuilder.append( b );
	}

	/* package-private */void appendChar( char c ) {
		this.codeStringBuilder.append( c );
	}

	/* package-private */void appendInt( int n ) {
		if( n == Integer.MAX_VALUE ) {
			this.appendString( "Integer.MAX_VALUE" );
		} else if( n == Integer.MIN_VALUE ) {
			this.appendString( "Integer.MIN_VALUE" );
		} else {
			this.codeStringBuilder.append( n );
		}
	}

	/* package-private */void appendFloat( float f ) {
		if( Float.isNaN( f ) ) {
			this.appendString( "Float.NaN" );
		} else if( f == Float.POSITIVE_INFINITY ) {
			this.appendString( "Float.POSITIVE_INFINITY" );
		} else if( f == Float.NEGATIVE_INFINITY ) {
			this.appendString( "Float.NEGATIVE_INFINITY" );
		} else {
			this.codeStringBuilder.append( f );
			this.appendChar( 'f' );
		}
	}

	/* package-private */void appendDouble( double d ) {
		if( Double.isNaN( d ) ) {
			this.appendString( "Double.NaN" );
		} else if( d == Double.POSITIVE_INFINITY ) {
			this.appendString( "Double.POSITIVE_INFINITY" );
		} else if( d == Double.NEGATIVE_INFINITY ) {
			this.appendString( "Double.NEGATIVE_INFINITY" );
		} else {
			this.codeStringBuilder.append( d );
		}
	}

	/* package-private */void appendString( String s ) {
		this.codeStringBuilder.append( s );
	}

	private void appendResource( org.lgna.common.Resource resource ) {
		this.codeStringBuilder.append( org.lgna.project.resource.ResourcesTypeWrapper.getTypeName() );
		this.codeStringBuilder.append( "." );
		this.codeStringBuilder.append( org.lgna.project.resource.ResourcesTypeWrapper.getFixedName( resource ) );
	}

	/* package-private */void appendResourceExpression( ResourceExpression resourceExpression ) {
		org.lgna.common.Resource resource = resourceExpression.resource.getValue();
		if( resource != null ) {
			if( this.resourcesTypeWrapper != null ) {
				UserField field = this.resourcesTypeWrapper.getFieldForResource( resource );
				if( field != null ) {
					this.codeStringBuilder.append( field.getDeclaringType().getName() );
					this.codeStringBuilder.append( "." );
					this.codeStringBuilder.append( field.getName() );
				} else {
					this.appendResource( resource );
				}
			} else {
				this.appendResource( resource );
			}
		} else {
			this.codeStringBuilder.append( "null" ); //todo?
		}
	}

	/* package-private */void appendTypeName( AbstractType<?, ?, ?> type ) {
		if( type instanceof JavaType ) {
			JavaType javaType = (JavaType)type;
			if( javaType.isPrimitive() ) {
				//pass
			} else {
				JavaPackage javaPackage = javaType.getPackage();
				if( javaPackage != null ) {
					JavaType enclosingType = javaType.getEnclosingType();
					//todo: choose EnclosingTypeName.ClassName instead?
					boolean isTypeImportingDesired;
					if( enclosingType != null ) {
						isTypeImportingDesired = true;
					} else {
						isTypeImportingDesired = packagesMarkedForOnDemandImport.contains( javaPackage ) == false;
					}
					if( isTypeImportingDesired ) {
						this.typesToImport.add( javaType );
					} else {
						this.packagesToImportOnDemand.add( javaPackage );
					}
				} else {
					// should be covered already by the primitive check
				}

			}
		}
		//todo: handle imports
		this.appendString( type.getName() );
	}

	/* packag-private */void appendCallerExpression( Expression callerExpression, AbstractMethod method ) {
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

	/* package-private */void appendExpression( Expression expression ) {
		expression.appendJava( this );
	}

	/* package-private */void appendAccessLevel( AccessLevel accessLevel ) {
		accessLevel.appendJava( this );
	}

	/* package-private */AbstractType<?, ?, ?> peekTypeForLambda() {
		return this.typeForLambdaStack.peek();
	}

	private void appendArgument( AbstractArgument argument ) {
		AbstractParameter parameter = argument.parameter.getValue();
		AbstractType<?, ?, ?> type = argument.getExpressionTypeForParameterType( parameter.getValueType() );
		this.typeForLambdaStack.push( type );
		try {
			argument.appendJava( this );
		} finally {
			assert this.typeForLambdaStack.pop() == type;
		}
	}

	/* package-private */void appendParameters( Code code ) {
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

	/* package-private */void appendMethodHeader( AbstractMethod method ) {
		AbstractMethod overridenMethod = AstUtilities.getOverridenMethod( method );
		if( overridenMethod != null ) {
			this.appendString( "@Override " );
		}
		this.appendAccessLevel( method.getAccessLevel() );
		if( method.isStatic() ) {
			this.appendString( "static " );
		}
		this.appendTypeName( method.getReturnType() );
		this.appendSpace();
		this.appendString( method.getName() );
		this.appendParameters( method );
	}

	/* package-private */void appendArguments( ArgumentOwner argumentOwner ) {
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

	/* package-private */void appendSpace() {
		this.appendChar( ' ' );
	}

	/* package-private */void appendSemicolon() {
		this.appendChar( ';' );
	}

	@Deprecated
	/* package-private */void todo( Object o ) {
		codeStringBuilder.append( "todo_" );
		codeStringBuilder.append( o );
	}

	protected String getImportsPrefix() {
		return "";
	}

	protected String getImportsPostfix() {
		return "";
	}

	/* package-private */String getText( boolean areImportsDesired ) {
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
		rvStringBuilder.append( this.codeStringBuilder );
		return rvStringBuilder.toString();
	}

	/* package-private */org.lgna.project.code.CodeOrganizer getNewCodeOrganizerForTypeName( String typeName ) {
		if( this.codeOrganizerMap.containsKey( typeName ) ) {
			return new org.lgna.project.code.CodeOrganizer( this.codeOrganizerMap.get( typeName ) );
		}
		else {
			return new org.lgna.project.code.CodeOrganizer( this.defaultCodeOrganizer );
		}
	}

	protected String getMethodPrefix( UserMethod method ) {
		return "";
	}

	protected String getMethodPostfix( UserMethod method ) {
		return "";
	}

	protected String getSectionPrefix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
		return "";
	}

	protected String getSectionPostfix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
		return "";
	}

	/* package-private */final void appendSectionPrefix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
		this.codeStringBuilder.append( this.getSectionPrefix( declaringType, sectionName, shouldCollapse ) );
	}

	/* package-private */final void appendSectionPostfix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
		this.codeStringBuilder.append( this.getSectionPostfix( declaringType, sectionName, shouldCollapse ) );
	}

	/* package-private */final void appendMethodPrefix( UserMethod method ) {
		this.codeStringBuilder.append( this.getMethodPrefix( method ) );
	}

	/* package-private */final void appendMethodPostfix( UserMethod method ) {
		this.codeStringBuilder.append( this.getMethodPostfix( method ) );
	}

	public Iterable<UserMethod> getMethods( UserType<?> type ) {
		return type.methods;
	}

	public String[] getVariableAndConstantNameForCountLoop( CountLoop countLoop ) {
		UserCode code = countLoop.getFirstAncestorAssignableTo( UserCode.class );
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<CountLoop> countLoopCrawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( CountLoop.class );
		code.crawl( countLoopCrawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY, null );
		int index = countLoopCrawler.getList().indexOf( countLoop );
		char c;
		if( index != -1 ) {
			c = (char)( ( (int)'A' ) + index );
		} else {
			c = '_';
		}
		return new String[] { "index" + c, "COUNT_" + c };
	}

	private static String[] splitIntoLines( String src ) {
		return src.split( "\n" );
	}

	public String formatCommentStringForSection( String commentText ) {
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

	public String formatCommentStringForItem( String commentText ) {
		return formatCommentStringForSection( commentText );
	}

	public static String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, String bundleName, java.util.Locale locale ) {
		if( bundleName != null ) {
			java.util.ResourceBundle resourceBundle = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( bundleName, locale );
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
			} while( done );
			return returnVal;
		}
		return null;
	}

	public String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, java.util.Locale locale ) {
		String comment = getLocalizedComment( type, itemName, this.commentsLocalizationBundleName, locale );
		return comment;
	}

	public String getLocalizedCommentForSection( AbstractType<?, ?, ?> type, String sectionName, java.util.Locale locale ) {
		String comment = getLocalizedComment( type, sectionName, this.commentsLocalizationBundleName, locale );
		if( comment != null ) {
			comment = formatCommentStringForSection( comment );
		}
		return comment;
	}

	public String getLocalizedCommentForItemName( AbstractType<?, ?, ?> type, String itemName, java.util.Locale locale ) {
		String comment = getLocalizedComment( type, itemName, this.commentsLocalizationBundleName, locale );
		if( comment != null ) {
			comment = formatCommentStringForItem( comment );
		}
		return comment;
	}

	private final StringBuilder codeStringBuilder = new StringBuilder();

	private final boolean isLambdaSupported;
	private final boolean isPublicStaticFinalFieldGetterDesired;
	private final java.util.Set<JavaPackage> packagesToImportOnDemand = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
	private final java.util.Set<JavaType> typesToImport = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
	private final java.util.Set<JavaMethod> methodsToImportStatic = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();

	private final java.util.List<JavaPackage> packagesMarkedForOnDemandImport;
	private final java.util.List<JavaMethod> staticMethodsMarkedForImport;

	private final edu.cmu.cs.dennisc.java.util.DStack<AbstractType<?, ?, ?>> typeForLambdaStack = edu.cmu.cs.dennisc.java.util.Stacks.newStack();

	private final org.lgna.project.resource.ResourcesTypeWrapper resourcesTypeWrapper;

	private int statementDisabledCount;

	private final java.util.Map<String, org.lgna.project.code.CodeOrganizer.CodeOrganizerDefinition> codeOrganizerMap;
	private final org.lgna.project.code.CodeOrganizer.CodeOrganizerDefinition defaultCodeOrganizer;
	private final String commentsLocalizationBundleName;
}
