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
package org.alice.ide.declarationseditor;

/**
 * @author Dennis Cosgrove
 */
public class TypeMenu extends org.lgna.croquet.MenuModel {
	private static final java.awt.Font TYPE_FONT;
	private static final java.awt.Font BONUS_FONT;
	static {
		java.awt.Font defaultFont = javax.swing.UIManager.getFont( "defaultFont" );
		if( defaultFont != null ) {
			//pass
		} else {
			defaultFont = new java.awt.Font( "SansSerif", java.awt.Font.PLAIN, 12 );
		}
		TYPE_FONT = defaultFont.deriveFont( 18.0f );
		BONUS_FONT = defaultFont.deriveFont( java.awt.Font.ITALIC );
	}
	private static java.util.Map<org.lgna.project.ast.NamedUserType, TypeMenu> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized TypeMenu getInstance( org.lgna.project.ast.NamedUserType type ) {
		TypeMenu rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new TypeMenu( type );
			map.put( type, rv );
		}
		return rv;
	}

	private final org.lgna.project.ast.NamedUserType type;

	private TypeMenu( org.lgna.project.ast.NamedUserType type ) {
		super( java.util.UUID.fromString( "d4ea32ce-d9d6-452e-8d99-2d8078c01251" ) );
		this.type = type;
	}

	@Override
	protected void localize() {
		super.localize();
		this.setSmallIcon( new org.alice.ide.common.TypeIcon( this.type, true, TYPE_FONT, BONUS_FONT ) );
	}

	@Override
	protected void handleShowing( org.lgna.croquet.views.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		DeclarationTabState declarationTabState = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();

		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> procedureModels = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> functionModels = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> managedFieldModels = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> unmanagedFieldModels = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		org.lgna.croquet.data.ListData<DeclarationComposite<?, ?>> data = declarationTabState.getData();
		final java.util.Set<org.lgna.croquet.StandardMenuItemPrepModel> set = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
		for( org.lgna.project.ast.UserMethod method : this.type.methods ) {
			if( method.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.NONE ) {
				org.lgna.croquet.StandardMenuItemPrepModel model = declarationTabState.getItemSelectionOperationForMethod( method ).getMenuItemPrepModel();
				if( data.contains( DeclarationComposite.getInstance( method ) ) ) {
					set.add( model );
				}
				if( method.isProcedure() ) {
					procedureModels.add( model );
				} else {
					functionModels.add( model );
				}
			}
		}

		final boolean EDIT = false;
		for( org.lgna.project.ast.UserField field : this.type.fields ) {
			if( field.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.MANAGED ) {
				if( EDIT ) {
					managedFieldModels.add( org.alice.ide.ast.declaration.ManagedEditFieldComposite.getInstance( field ).getLaunchOperation().getMenuItemPrepModel() );
				} else {
					managedFieldModels.add( HighlightFieldOperation.getInstance( field ).getMenuItemPrepModel() );
				}
			} else {
				if( EDIT ) {
					unmanagedFieldModels.add( org.alice.ide.ast.declaration.UnmanagedEditFieldComposite.getInstance( field ).getLaunchOperation().getMenuItemPrepModel() );
				} else {
					unmanagedFieldModels.add( HighlightFieldOperation.getInstance( field ).getMenuItemPrepModel() );
				}
			}
		}

		if( procedureModels.size() > 0 ) {
			procedureModels.add( 0, ProceduresSeparator.getInstance() );
		}
		if( functionModels.size() > 0 ) {
			functionModels.add( 0, FunctionsSeparator.getInstance() );
		}

		procedureModels.add( org.alice.ide.ast.declaration.AddProcedureComposite.getInstance( this.type ).getLaunchOperation().getMenuItemPrepModel() );
		functionModels.add( org.alice.ide.ast.declaration.AddFunctionComposite.getInstance( this.type ).getLaunchOperation().getMenuItemPrepModel() );

		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		org.lgna.croquet.Operation operation = declarationTabState.getItemSelectionOperationForType( type );
		operation.setName( type.getName() );

		if( data.contains( DeclarationComposite.getInstance( type ) ) ) {
			set.add( operation.getMenuItemPrepModel() );
		}
		models.add( operation.getMenuItemPrepModel() );

		if( org.alice.ide.croquet.models.ui.preferences.IsIncludingConstructors.getInstance().getValue() ) {
			models.add( SEPARATOR );
			for( org.lgna.project.ast.NamedUserConstructor constructor : type.getDeclaredConstructors() ) {
				org.lgna.croquet.StandardMenuItemPrepModel model = declarationTabState.getItemSelectionOperationForConstructor( constructor ).getMenuItemPrepModel();
				if( data.contains( DeclarationComposite.getInstance( constructor ) ) ) {
					set.add( model );
				}
				models.add( model );
			}
		}

		models.add( SEPARATOR );
		models.addAll( procedureModels );
		models.add( SEPARATOR );
		models.addAll( functionModels );

		if( org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().isDeclaringTypeForManagedFields( type ) ) {
			models.add( SEPARATOR );
			if( managedFieldModels.size() > 0 ) {
				models.add( ManagedFieldsSeparator.getInstance() );
				models.addAll( managedFieldModels );
			}
			final boolean IS_SHOW_ME_HOW_PREFERRED = false;
			if( IS_SHOW_ME_HOW_PREFERRED ) {
				models.add( org.alice.stageide.showme.ShowMeHowToAddGalleryModelsIteratingOperation.getInstance().getMenuItemPrepModel() );
			} else {
				models.add( org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite.getInstance().getLaunchOperation().getMenuItemPrepModel() );
			}
		}

		models.add( SEPARATOR );
		if( ( unmanagedFieldModels.size() > 0 ) || ( managedFieldModels.size() > 0 ) ) {
			if( managedFieldModels.size() > 0 ) {
				models.add( UnmanagedFieldsSeparator.getInstance() );
			} else {
				models.add( FieldsSeparator.getInstance() );
			}
			models.addAll( unmanagedFieldModels );
		}
		models.add( org.alice.ide.ast.declaration.AddUnmanagedFieldComposite.getInstance( type ).getLaunchOperation().getMenuItemPrepModel() );

		org.lgna.croquet.views.MenuItemContainerUtilities.MenuElementObserver observer = new org.lgna.croquet.views.MenuItemContainerUtilities.MenuElementObserver() {
			@Override
			public void update( org.lgna.croquet.views.MenuItemContainer menuItemContainer, org.lgna.croquet.StandardMenuItemPrepModel model, org.lgna.croquet.views.ViewController<?, ?> menuElement ) {
				if( menuElement != null ) {
					edu.cmu.cs.dennisc.java.awt.font.TextWeight textWeight = set.contains( model ) ? edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD : edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT;
					menuElement.changeFont( textWeight );
				}
			}
		};
		org.lgna.croquet.views.MenuItemContainerUtilities.setMenuElements( menuItemContainer, models, observer );

		super.handleShowing( menuItemContainer, e );
	}
}
