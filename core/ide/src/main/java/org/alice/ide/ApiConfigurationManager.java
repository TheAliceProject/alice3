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

package org.alice.ide;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.tree.DefaultNode;
import edu.cmu.cs.dennisc.tree.Node;
import org.alice.ide.ast.ExpressionCreator;
import org.alice.ide.iconfactory.IconFactoryManager;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.member.FilteredJavaMethodsSubComposite;
import org.alice.stageide.StoryApiConfigurationManager;
import org.lgna.croquet.CascadeItem;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Code;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserParameter;
import org.lgna.project.ast.UserType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class ApiConfigurationManager {
	@Deprecated
	public static ApiConfigurationManager EPIC_HACK_getActiveInstance() {
		IDE ide = IDE.getActiveInstance();
		if( ide != null ) {
			return ide.getApiConfigurationManager();
		} else {
			Logger.todo( "remove epic hack" );
			return StoryApiConfigurationManager.getInstance();
		}
	}

	public abstract Comparator<AbstractType<?, ?, ?>> getTypeComparator();

	public abstract List<FilteredJavaMethodsSubComposite> getCategoryProcedureSubComposites();

	public abstract List<FilteredJavaMethodsSubComposite> getCategoryFunctionSubComposites();

	public abstract List<FilteredJavaMethodsSubComposite> getCategoryOrAlphabeticalProcedureSubComposites();

	public abstract List<FilteredJavaMethodsSubComposite> getCategoryOrAlphabeticalFunctionSubComposites();

	//override to create user types if desired
	public AbstractType<?, ?, ?> getTypeFor( AbstractType<?, ?, ?> type ) {
		return type;
	}

	public final AbstractType<?, ?, ?> getTypeFor( Class<?> cls ) {
		return this.getTypeFor( JavaType.getInstance( cls ) );
	}

	private final DefaultNode<NamedUserType> getNamedUserTypesAsTree() {
		IDE ide = IDE.getActiveInstance();
		Project project = ide.getProject();
		if( project != null ) {
			return ProgramTypeUtilities.getNamedUserTypesAsTree( project );
		} else {
			return null;
		}
	}

	protected abstract boolean isNamedUserTypesAcceptableForSelection( NamedUserType type );

	public final Node<NamedUserType> getNamedUserTypesAsTreeFilteredForSelection() {
		DefaultNode<NamedUserType> rv = getNamedUserTypesAsTree();
		if( rv != null ) {
			for( DefaultNode<NamedUserType> child : rv.getChildren() ) {
				if( this.isNamedUserTypesAcceptableForSelection( child.getValue() ) ) {
					//pass
				} else {
					rv.removeChild( child );
				}
			}
		}
		return rv;
	}

	protected abstract boolean isNamedUserTypesAcceptableForGallery( NamedUserType type );

	public final DefaultNode<NamedUserType> getNamedUserTypesAsTreeFilteredForGallery() {
		DefaultNode<NamedUserType> rv = getNamedUserTypesAsTree();
		if( rv != null ) {
			for( DefaultNode<NamedUserType> child : rv.getChildren() ) {
				if( this.isNamedUserTypesAcceptableForGallery( child.getValue() ) ) {
					//pass
				} else {
					rv.removeChild( child );
				}
			}
		}
		return rv;
	}

	public final List<JavaType> getPrimeTimeSelectableJavaTypes() {
		List<JavaType> rv = Lists.newLinkedList();
		this.addPrimeTimeJavaTypes( rv );
		return rv;
	}

	public final List<JavaType> getSecondarySelectableJavaTypes() {
		List<JavaType> rv = Lists.newLinkedList();
		this.addSecondaryJavaTypes( rv );
		return rv;
	}

	//	public final java.util.List< org.lgna.project.ast.NamedUserType > getTypesDeclaredInAlice() {
	//		java.util.List< org.lgna.project.ast.NamedUserType > rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	//		this.addAliceTypes( rv, true );
	//		return rv;
	//	}
	protected List<? super JavaType> addPrimeTimeJavaTypes( List<? super JavaType> rv ) {
		rv.add( JavaType.DOUBLE_OBJECT_TYPE );
		rv.add( JavaType.INTEGER_OBJECT_TYPE );
		rv.add( JavaType.BOOLEAN_OBJECT_TYPE );
		rv.add( JavaType.STRING_TYPE );
		return rv;
	}

	protected List<? super JavaType> addSecondaryJavaTypes( List<? super JavaType> rv ) {
		return rv;
	}

	protected boolean isInclusionOfTypeDesired( UserType<?> userType ) {
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

	private final Map<AbstractType<?, ?, ?>, String> mapTypeToText = Maps.newHashMap();

	private static String createExampleText( String examples ) {
		return "<html><em>examples:</em> " + examples + "</html>";
	}

	public String getMenuTextForType( AbstractType<?, ?, ?> type ) {
		if( this.mapTypeToText.size() == 0 ) {
			this.mapTypeToText.put( JavaType.DOUBLE_OBJECT_TYPE, createExampleText( "0.25, 1.0, 3.14, 98.6" ) );
			this.mapTypeToText.put( JavaType.INTEGER_OBJECT_TYPE, createExampleText( "1, 2, 42, 100" ) );
			this.mapTypeToText.put( JavaType.BOOLEAN_OBJECT_TYPE, createExampleText( "true, false" ) );
			this.mapTypeToText.put( JavaType.STRING_TYPE, createExampleText( "\"hello\", \"goodbye\"" ) );
		}
		return this.mapTypeToText.get( type );
	}

	public boolean isSignatureLocked( Code code ) {
		return code.isSignatureLocked();
	}

	public abstract boolean isDeclaringTypeForManagedFields( UserType<?> type );

	public final boolean isSelectable( AbstractType<?, ?, ?> type ) {
		return this.isInstanceFactoryDesiredForType( type );
	}

	public abstract boolean isInstanceFactoryDesiredForType( AbstractType<?, ?, ?> type );

	public abstract CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForThis( AbstractType<?, ?, ?> type );

	public abstract CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForThisFieldAccess( UserField field );

	public abstract CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForParameterAccess( UserParameter parameter );

	public abstract CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForLocalAccess( UserLocal local );

	public abstract CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForParameterAccessMethodInvocation( UserParameter parameter, AbstractMethod method );

	public abstract JavaType getGalleryResourceParentFor( JavaType type );

	public abstract List<AbstractDeclaration> getGalleryResourceChildrenFor( AbstractType<?, ?, ?> type );

	public abstract AbstractConstructor getGalleryResourceConstructorFor( AbstractType<?, ?, ?> argumentType );

	public abstract SwingComponentView<?> createReplacementForFieldAccessIfAppropriate( FieldAccess fieldAccess );

	public abstract CascadeItem<?, ?> getCustomFillInFor( ValueDetails<?> valueDetails );

	public abstract ExpressionCreator getExpressionCreator();

	public abstract UserType<?> augmentTypeIfNecessary( UserType<?> rv );

	public abstract boolean isTabClosable( AbstractCode code );

	public abstract boolean isExportTypeDesiredFor( NamedUserType type );

	public abstract IconFactoryManager createIconFactoryManager();
}
