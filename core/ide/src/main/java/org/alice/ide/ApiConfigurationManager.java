/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class ApiConfigurationManager {
	@Deprecated
	public static ApiConfigurationManager EPIC_HACK_getActiveInstance() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			return ide.getApiConfigurationManager();
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "remove epic hack" );
			return org.alice.stageide.StoryApiConfigurationManager.getInstance();
		}
	}

	public abstract java.util.Comparator<org.lgna.project.ast.AbstractType<?, ?, ?>> getTypeComparator();

	public abstract java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryProcedureSubComposites();

	public abstract java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryFunctionSubComposites();

	public abstract java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryOrAlphabeticalProcedureSubComposites();

	public abstract java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryOrAlphabeticalFunctionSubComposites();

	//override to create user types if desired
	public org.lgna.project.ast.AbstractType<?, ?, ?> getTypeFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return type;
	}

	public final org.lgna.project.ast.AbstractType<?, ?, ?> getTypeFor( Class<?> cls ) {
		return this.getTypeFor( org.lgna.project.ast.JavaType.getInstance( cls ) );
	}

	private final edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> getNamedUserTypesAsTree() {
		IDE ide = IDE.getActiveInstance();
		org.lgna.project.Project project = ide.getProject();
		if( project != null ) {
			return org.lgna.project.ProgramTypeUtilities.getNamedUserTypesAsTree( project );
		} else {
			return null;
		}
	}

	protected abstract boolean isNamedUserTypesAcceptableForSelection( org.lgna.project.ast.NamedUserType type );

	public final edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> getNamedUserTypesAsTreeFilteredForSelection() {
		edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> rv = getNamedUserTypesAsTree();
		if( rv != null ) {
			for( edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> child : rv.getChildren() ) {
				if( this.isNamedUserTypesAcceptableForSelection( child.getValue() ) ) {
					//pass
				} else {
					rv.removeChild( child );
				}
			}
		}
		return rv;
	}

	protected abstract boolean isNamedUserTypesAcceptableForGallery( org.lgna.project.ast.NamedUserType type );

	public final edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> getNamedUserTypesAsTreeFilteredForGallery() {
		edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> rv = getNamedUserTypesAsTree();
		if( rv != null ) {
			for( edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> child : rv.getChildren() ) {
				if( this.isNamedUserTypesAcceptableForGallery( child.getValue() ) ) {
					//pass
				} else {
					rv.removeChild( child );
				}
			}
		}
		return rv;
	}

	public final java.util.List<org.lgna.project.ast.JavaType> getPrimeTimeSelectableJavaTypes() {
		java.util.List<org.lgna.project.ast.JavaType> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.addPrimeTimeJavaTypes( rv );
		return rv;
	}

	public final java.util.List<org.lgna.project.ast.JavaType> getSecondarySelectableJavaTypes() {
		java.util.List<org.lgna.project.ast.JavaType> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.addSecondaryJavaTypes( rv );
		return rv;
	}

	//	public final java.util.List< org.lgna.project.ast.NamedUserType > getTypesDeclaredInAlice() {
	//		java.util.List< org.lgna.project.ast.NamedUserType > rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	//		this.addAliceTypes( rv, true );
	//		return rv;
	//	}
	protected java.util.List<? super org.lgna.project.ast.JavaType> addPrimeTimeJavaTypes( java.util.List<? super org.lgna.project.ast.JavaType> rv ) {
		rv.add( org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE );
		rv.add( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE );
		rv.add( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE );
		rv.add( org.lgna.project.ast.JavaType.STRING_TYPE );
		return rv;
	}

	protected java.util.List<? super org.lgna.project.ast.JavaType> addSecondaryJavaTypes( java.util.List<? super org.lgna.project.ast.JavaType> rv ) {
		return rv;
	}

	protected boolean isInclusionOfTypeDesired( org.lgna.project.ast.UserType<?> userType ) {
		return true;
		//return valueTypeInAlice.methods.size() > 0 || valueTypeInAlice.fields.size() > 0;
	}

	//	protected java.util.List< ? super org.lgna.project.ast.NamedUserType > addAliceTypes( java.util.List< ? super org.lgna.project.ast.NamedUserType > rv, boolean isInclusionOfTypesWithoutMembersDesired ) {
	//		org.lgna.project.ast.NamedUserType sceneType = this.getSceneType();
	//		if( sceneType != null ) {
	//			rv.add( sceneType );
	//			for( org.lgna.project.ast.AbstractField field : sceneType.getDeclaredFields() ) {
	//				org.lgna.project.ast.AbstractType< ?, ?, ? > valueType = field.getValueType();
	//				if( valueType instanceof org.lgna.project.ast.NamedUserType ) {
	//					org.lgna.project.ast.NamedUserType valueTypeInAlice = (org.lgna.project.ast.NamedUserType)valueType;
	//					if( rv.contains( valueType ) ) {
	//						//pass
	//					} else {
	//						if( isInclusionOfTypesWithoutMembersDesired || isInclusionOfTypeDesired( valueTypeInAlice ) ) {
	//							rv.add( valueTypeInAlice );
	//						}
	//					}
	//				}
	//			}
	//		}
	//		return rv;
	//	}

	private final java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, String> mapTypeToText = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static String createExampleText( String examples ) {
		return "<html><em>examples:</em> " + examples + "</html>";
	}

	public String getMenuTextForType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( this.mapTypeToText.size() == 0 ) {
			this.mapTypeToText.put( org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE, createExampleText( "0.25, 1.0, 3.14, 98.6" ) );
			this.mapTypeToText.put( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE, createExampleText( "1, 2, 42, 100" ) );
			this.mapTypeToText.put( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE, createExampleText( "true, false" ) );
			this.mapTypeToText.put( org.lgna.project.ast.JavaType.STRING_TYPE, createExampleText( "\"hello\", \"goodbye\"" ) );
		}
		return this.mapTypeToText.get( type );
	}

	public boolean isSignatureLocked( org.lgna.project.ast.Code code ) {
		return code.isSignatureLocked();
	}

	public abstract boolean isDeclaringTypeForManagedFields( org.lgna.project.ast.UserType<?> type );

	public final boolean isSelectable( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return this.isInstanceFactoryDesiredForType( type );
	}

	public abstract boolean isInstanceFactoryDesiredForType( org.lgna.project.ast.AbstractType<?, ?, ?> type );

	public abstract org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForThis( org.lgna.project.ast.AbstractType<?, ?, ?> type );

	public abstract org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForThisFieldAccess( org.lgna.project.ast.UserField field );

	public abstract org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForParameterAccess( org.lgna.project.ast.UserParameter parameter );

	public abstract org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForLocalAccess( org.lgna.project.ast.UserLocal local );

	public abstract org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForParameterAccessMethodInvocation( org.lgna.project.ast.UserParameter parameter, org.lgna.project.ast.AbstractMethod method );

	public abstract java.util.List<org.lgna.project.ast.JavaType> getTopLevelGalleryTypes();

	public abstract org.lgna.project.ast.JavaType getGalleryResourceParentFor( org.lgna.project.ast.JavaType type );

	public abstract java.util.List<org.lgna.project.ast.AbstractDeclaration> getGalleryResourceChildrenFor( org.lgna.project.ast.AbstractType<?, ?, ?> type );

	public abstract org.lgna.project.ast.AbstractConstructor getGalleryResourceConstructorFor( org.lgna.project.ast.AbstractType<?, ?, ?> argumentType );

	public abstract org.lgna.croquet.views.SwingComponentView<?> createReplacementForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess );

	public abstract org.lgna.croquet.CascadeItem<?, ?> getCustomFillInFor( org.lgna.project.annotations.ValueDetails<?> valueDetails );

	public abstract org.alice.ide.ast.ExpressionCreator getExpressionCreator();

	public abstract org.lgna.project.ast.UserType<?> augmentTypeIfNecessary( org.lgna.project.ast.UserType<?> rv );

	public abstract boolean isTabClosable( org.lgna.project.ast.AbstractCode code );

	public abstract boolean isExportTypeDesiredFor( org.lgna.project.ast.NamedUserType type );

	public abstract org.alice.ide.iconfactory.IconFactoryManager createIconFactoryManager();
}
