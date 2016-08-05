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
package org.lgna.project.resource;

/**
 * @author Dennis Cosgrove
 */
public class ResourcesTypeWrapper {
	private static final String NAME_FOR_UNNAMED = "UNNAMED";

	private static String fixNameIfNecessary( String name ) {
		StringBuilder sb = new StringBuilder();

		if( name != null ) {
			for( char c : name.toCharArray() ) {
				if( Character.isLetterOrDigit( c ) ) {
					sb.append( c );
				} else {
					sb.append( '_' );
				}
			}
		}

		if( sb.length() > 0 ) {
			char c0 = sb.charAt( 0 );
			if( Character.isLetter( c0 ) || ( c0 == '_' ) ) {
				//pass
			} else {
				sb.insert( 0, "_" );
			}
			return sb.toString();
		} else {
			return NAME_FOR_UNNAMED;
		}
	}

	public static final String getTypeName() {
		return "Resources";
	}

	public static final String getFixedName( org.lgna.common.Resource resource ) {
		return fixNameIfNecessary( resource.getName() );
	}

	public ResourcesTypeWrapper( org.lgna.project.Project project ) {
		java.util.Set<org.lgna.common.Resource> resources = project.getResources();
		if( ( resources != null ) && ( resources.size() > 0 ) ) {
			this.type = new org.lgna.project.ast.NamedUserType();
			this.type.name.setValue( getTypeName() );
			this.type.superType.setValue( org.lgna.project.ast.JavaType.OBJECT_TYPE );
			int unnamedCount = 0;
			int duplicateCount = 0;
			for( org.lgna.common.Resource resource : resources ) {
				org.lgna.project.ast.UserField field = new org.lgna.project.ast.UserField();
				field.accessLevel.setValue( org.lgna.project.ast.AccessLevel.PUBLIC );
				field.isStatic.setValue( true );
				field.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
				field.valueType.setValue( org.lgna.project.ast.JavaType.getInstance( resource.getClass() ) );

				String name = getFixedName( resource );
				for( org.lgna.project.ast.UserField prevField : type.fields ) {
					if( prevField.name.getValue().equals( name ) ) {
						if( name.equals( NAME_FOR_UNNAMED ) ) {
							name += "_" + unnamedCount;
							unnamedCount++;
						} else {
							name += "_duplicate_" + duplicateCount;
							duplicateCount++;
						}
					}
				}

				field.name.setValue( name );
				field.initializer.setValue( org.lgna.project.ast.AstUtilities.createInstanceCreation(
						resource.getClass(),
						new Class<?>[] {
								Class.class,
								String.class,
								String.class
						},
						new org.lgna.project.ast.Expression[] {
								new org.lgna.project.ast.TypeLiteral( this.type ),
								new org.lgna.project.ast.StringLiteral( "resources/" + resource.getOriginalFileName() ),
								new org.lgna.project.ast.StringLiteral( resource.getContentType() )
						}
						) );

				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( field );
				this.type.fields.add( field );
			}
			this.mapResourceToField = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		} else {
			this.type = null;
			this.mapResourceToField = null;
		}
	}

	public org.lgna.project.ast.NamedUserType getType() {
		return this.type;
	}

	public org.lgna.project.ast.UserField getFieldForResource( org.lgna.common.Resource resource ) {
		if( this.mapResourceToField != null ) {
			return this.mapResourceToField.get( resource );
		} else {
			return null;
		}
	}

	private final org.lgna.project.ast.NamedUserType type;
	private final java.util.Map<org.lgna.common.Resource, org.lgna.project.ast.UserField> mapResourceToField;
}
