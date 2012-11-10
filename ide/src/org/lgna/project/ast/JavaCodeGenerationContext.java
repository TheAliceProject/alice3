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
/* package-private */class JavaCodeGenerationContext {
	private final StringBuilder codeStringBuilder = new StringBuilder();
	private final java.util.Set<JavaType> typesToImport = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();

	/* package-private */JavaCodeGenerationContext() {

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

	/* package-private */void appendTypeName( AbstractType<?, ?, ?> type ) {
		if( type instanceof JavaType ) {
			JavaType javaType = (JavaType)type;
			if( javaType.isPrimitive() ) {
				//pass
			} else {
				if( javaType.getPackage() != null ) {
					this.typesToImport.add( javaType );
				} else {
					// should be covered already by the primitive check
				}

			}
		}
		//todo: handle imports
		this.appendString( type.getName() );
	}

	/* package-private */void appendExpression( Expression expression ) {
		expression.appendJava( this );
	}

	/* package-private */void appendArguments( ArgumentOwner argumentOwner ) {
		String prefix = "";
		for( SimpleArgument argument : argumentOwner.getRequiredArgumentsProperty() ) {
			this.appendString( prefix );
			argument.appendJava( this );
			prefix = ",";
		}
		for( SimpleArgument argument : argumentOwner.getVariableArgumentsProperty() ) {
			this.appendString( prefix );
			argument.appendJava( this );
			prefix = ",";
		}
		for( JavaKeyedArgument argument : argumentOwner.getKeyedArgumentsProperty() ) {
			this.appendString( prefix );
			argument.appendJava( this );
			prefix = ",";
		}
	}

	/* package-private */void appendSpace() {
		this.appendChar( ' ' );
	}

	/* package-private */void appendSemicolon() {
		this.appendChar( ';' );
	}

	/* package-private */void appendNewline() {
		this.appendChar( '\n' );
	}

	@Deprecated
	/* package-private */void todo( Object o ) {
		codeStringBuilder.append( "todo" );
		codeStringBuilder.append( o );
	}

	/* package-private */String getText() {
		StringBuilder rvStringBuilder = new StringBuilder();
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
		rvStringBuilder.append( this.codeStringBuilder );
		return rvStringBuilder.toString();
	}
}
