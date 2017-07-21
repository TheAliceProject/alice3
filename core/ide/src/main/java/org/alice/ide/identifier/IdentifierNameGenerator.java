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

package org.alice.ide.identifier;

/**
 * @author Dennis Cosgrove
 */
public enum IdentifierNameGenerator {
	SINGLETON;
	public String convertConstantNameToMethodName( String constantName, String prefix ) {
		StringBuilder sb = new StringBuilder();
		if( prefix != null ) {
			sb.append( prefix );
		}
		boolean isUpperNext = sb.length() > 0;
		for( char c : constantName.toCharArray() ) {
			if( c == '_' ) {
				isUpperNext = true;
			} else {
				if( isUpperNext ) {
					sb.append( c );
				} else {
					sb.append( Character.toLowerCase( c ) );
				}
				isUpperNext = false;
			}
		}
		return sb.toString();
	}

	public String convertConstantNameToMethodName( String constantName ) {
		return this.convertConstantNameToMethodName( constantName, null );
	}

	private String convertFirstCharacterToLowerCase( String name ) {
		if( name != null ) {
			if( name.length() > 0 ) {
				return Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );
			} else {
				return name;
			}
		} else {
			return null;
		}
	}

	public String createIdentifierNameFromInstanceCreation( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		String rv = "";
		if( instanceCreation != null ) {
			org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
			if( constructor != null ) {
				org.lgna.project.ast.AbstractType<?, ?, ?> type = constructor.getDeclaringType();
				if( type != null ) {
					String typeName = type.getName();
					if( typeName != null ) {
						if( type instanceof org.lgna.project.ast.JavaType ) {
							if( typeName.length() > 1 ) {
								if( ( typeName.charAt( 0 ) == 'S' ) && Character.isUpperCase( typeName.charAt( 1 ) ) ) {
									typeName = typeName.substring( 1 );
								}
							}
						}
						rv = this.convertFirstCharacterToLowerCase( typeName );
					}
				}
			}
		}
		return rv;
		//		if( instanceCreation != null ) {
		//			java.lang.reflect.Field fld = this.getFldFromInstanceCreationInitializer( instanceCreation );
		//			if( fld != null ) {
		//				return org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( fld.getName() );
		//			} else {
		//				org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
		//				org.lgna.project.ast.AbstractType<?, ?, ?> abstractType = constructor.getDeclaringType();
		//				String typeName = abstractType.getName();
		//				if( typeName != null ) {
		//					//todo: move to api configuration
		//					if( typeName.length() > 1 ) {
		//						if( ( typeName.charAt( 0 ) == 'S' ) && Character.isUpperCase( typeName.charAt( 1 ) ) ) {
		//							typeName = typeName.substring( 1 );
		//						}
		//					}
		//					return org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertFirstCharacterToLowerCase( typeName );
		//				} else {
		//					return "";
		//				}
		//			}
		//		} else {
		//			return "";
		//		}

		//		if( instanceCreation != null ) {
		//		java.lang.reflect.Field fld = this.getFldFromInstanceCreationInitializer( instanceCreation );
		//		if( fld != null ) {
		//			return org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( fld.getName() );
		//		} else {
		//			org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
		//			org.lgna.project.ast.AbstractType<?, ?, ?> abstractType = constructor.getDeclaringType();
		//			String typeName = abstractType.getName();
		//			if( typeName != null ) {
		//				return org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertFirstCharacterToLowerCase( typeName );
		//			} else {
		//				return "";
		//			}
		//		}
		//	} else {
		//		return "";
		//	}
	}
}
