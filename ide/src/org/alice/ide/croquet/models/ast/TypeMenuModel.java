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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class TypeMenuModel extends org.lgna.croquet.MenuModel {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice, TypeMenuModel > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized TypeMenuModel getInstance( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		TypeMenuModel rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new TypeMenuModel( type );
			map.put( type, rv );
		}
		return rv;
	}

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
	private TypeMenuModel( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( java.util.UUID.fromString( "90a5b2c2-1182-4c05-ac90-a1dc405a7a2f" ) );
		this.type = type;
		//this.setName( type.getName() );
		this.setSmallIcon( org.alice.ide.common.TypeIcon.getInstance( type ) );
	}
	
	@Override
	protected String getTutorialNoteName() {
		return this.type.getName();
	}
	@Override
	protected org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< TypeMenuModel > createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< TypeMenuModel >( this, this.type, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice.class );
	}

	@Override
	protected void handleShowing( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		super.handleShowing( menuItemContainer, e );

		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleMenuSelected" );
		
		org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, org.alice.ide.croquet.models.ast.EditTypeOperation.getInstance( this.type ).getMenuItemPrepModel() );
		org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, org.alice.ide.croquet.models.ast.rename.RenameTypeOperation.getInstance( this.type ).getMenuItemPrepModel() );
//		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
//		if( ide.isInstanceCreationAllowableFor( this.type ) ) {
//			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType = ide.getSceneType();
//			org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, new org.alice.ide.operations.ast.DeclareFieldOfPredeterminedTypeOperation( ownerType, this.type ).getMenuItemPrepModel() );
//		}
		org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, TypeSaveAsOperation.getInstance( this.type ).getMenuItemPrepModel() );
		menuItemContainer.addSeparator();
		org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, EditConstructorOperation.getInstance( this.type.getDeclaredConstructor() ).getMenuItemPrepModel() );
		menuItemContainer.addSeparator();
		org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, DeclareProcedureOperation.getInstance( this.type ).getMenuItemPrepModel() );
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.type.methods ) {
			if( method.isProcedure() ) {
				org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, EditMethodOperation.getInstance( method ).getMenuItemPrepModel() );
			}
		}
		menuItemContainer.addSeparator();
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.type.methods ) {
			if( method.isFunction() ) {
				org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, EditMethodOperation.getInstance( method ).getMenuItemPrepModel() );
			}
		}
		org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, DeclareFunctionOperation.getInstance( this.type ).getMenuItemPrepModel() );
		menuItemContainer.addSeparator();
		org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, org.alice.ide.operations.ast.DeclareFieldOperation.getInstance( this.type ).getMenuItemPrepModel() );
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : this.type.fields ) {
			org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElement( menuItemContainer, new org.alice.ide.operations.ast.EditFieldOperation( field ).getMenuItemPrepModel() );
		}
	}
	@Override
	protected void handleHiding( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		menuItemContainer.forgetAndRemoveAllMenuItems();
		super.handleHiding( menuItemContainer, e );
	}
}
