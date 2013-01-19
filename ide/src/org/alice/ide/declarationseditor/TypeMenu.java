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
package org.alice.ide.declarationseditor;

/**
 * @author Dennis Cosgrove
 */
public class TypeMenu extends org.lgna.croquet.MenuModel {
	private static java.util.Map<org.lgna.project.ast.NamedUserType, TypeMenu> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

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
		final boolean ICON_IS_DESIRED = true;
		if( ICON_IS_DESIRED ) {
			this.setSmallIcon( new org.alice.ide.common.TypeIcon( this.type, true ) );
		} else {
			int depth = org.lgna.project.ast.StaticAnalysisUtilities.getUserTypeDepth( this.type );
			StringBuilder sb = new StringBuilder();
			if( depth > 0 ) {
				for( int i = 0; i < depth; i++ ) {
					sb.append( '-' );
				}
				sb.append( ' ' );
			}
			sb.append( this.type.getName() );
			this.setName( sb.toString() );
		}
	}

	@Override
	protected void handleShowing( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> procedureModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> functionModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

		for( org.lgna.project.ast.UserMethod method : this.type.methods ) {
			if( method.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.NONE ) {
				org.lgna.croquet.StandardMenuItemPrepModel model = org.alice.ide.croquet.models.ast.EditMethodOperation.getLocalizedToNameInstance( method ).getMenuItemPrepModel();
				if( method.isProcedure() ) {
					procedureModels.add( model );
				} else {
					functionModels.add( model );
				}
			}
		}

		if( procedureModels.size() > 0 ) {
			procedureModels.add( 0, ProceduresSeparator.getInstance() );
		}
		if( functionModels.size() > 0 ) {
			functionModels.add( 0, FunctionsSeparator.getInstance() );
		}

		procedureModels.add( org.alice.ide.ast.declaration.AddProcedureComposite.getInstance( this.type ).getOperation().getMenuItemPrepModel() );
		functionModels.add( org.alice.ide.ast.declaration.AddFunctionComposite.getInstance( this.type ).getOperation().getMenuItemPrepModel() );

		DeclarationTabState tabState = DeclarationsEditorComposite.getInstance().getTabState();
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		org.lgna.croquet.Operation operation = tabState.getItemSelectionOperation( type );
		operation.setSmallIcon( org.alice.ide.croquet.models.ast.EditMethodOperation.TYPE_ICON );
		operation.setName( type.getName() );

		models.add( operation.getMenuItemPrepModel() );
		models.add( null );
		models.addAll( procedureModels );
		models.add( null );
		models.addAll( functionModels );

		//		if( org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().isDeclaringTypeForManagedFields( type ) ) {
		//			models.add( null );
		//			java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> unmanagedFieldModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		//			if( unmanagedFieldModels.size() > 0 ) {
		//				models.add( ManagedFieldsSeparator.getInstance() );
		//			}
		//		}
		models.add( null );
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> fieldModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		if( fieldModels.size() > 0 ) {
			models.add( FieldsSeparator.getInstance() );
		}
		models.add( org.alice.ide.ast.declaration.AddUnmanagedFieldComposite.getInstance( type ).getOperation().getMenuItemPrepModel() );
		org.lgna.croquet.components.MenuItemContainerUtilities.setMenuElements( menuItemContainer, models );

		super.handleShowing( menuItemContainer, e );
	}
}
