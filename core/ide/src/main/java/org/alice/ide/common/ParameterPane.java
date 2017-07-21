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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class ParameterPane extends TransientPane {
	private org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.UserParameter> parametersProperty;

	public ParameterPane( org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.UserParameter> parametersProperty, final org.lgna.project.ast.UserParameter parameter ) {
		super( org.alice.ide.ast.draganddrop.expression.ParameterAccessDragModel.getInstance( parameter ) );
		this.parametersProperty = parametersProperty;
		this.addComponent( new org.alice.ide.ast.components.DeclarationNameLabel( parameter ) );
		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getColorFor( org.lgna.project.ast.ParameterAccess.class ) );
		if( this.parametersProperty != null ) {
			final org.alice.ide.operations.ast.DeleteParameterOperation deleteParameterOperation = new org.alice.ide.operations.ast.DeleteParameterOperation( this.parametersProperty, parameter );
			final org.alice.ide.ast.code.ForwardShiftParameterOperation forwardShiftCodeParameterOperation = new org.alice.ide.ast.code.ForwardShiftParameterOperation( this.parametersProperty, parameter );
			final org.alice.ide.ast.code.BackwardShiftParameterOperation backwardShiftCodeParameterOperation = new org.alice.ide.ast.code.BackwardShiftParameterOperation( this.parametersProperty, parameter );
			this.setPopupPrepModel( new org.lgna.croquet.MenuModel( java.util.UUID.fromString( "5b9b75d7-ce04-4f3d-8915-b825f357cef2" ) ) {
				@Override
				public void handlePopupMenuPrologue( org.lgna.croquet.views.PopupMenu popupMenu, org.lgna.croquet.history.PopupPrepStep context ) {
					super.handlePopupMenuPrologue( popupMenu, context );
					java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
					models.add( org.alice.ide.ast.rename.RenameParameterComposite.getInstance( parameter ).getLaunchOperation().getMenuItemPrepModel() );
					if( forwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						models.add( forwardShiftCodeParameterOperation.getMenuItemPrepModel() );
					}
					if( backwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						models.add( backwardShiftCodeParameterOperation.getMenuItemPrepModel() );
					}
					models.add( org.lgna.croquet.MenuModel.SEPARATOR );
					models.add( deleteParameterOperation.getMenuItemPrepModel() );
					org.lgna.croquet.views.MenuItemContainerUtilities.setMenuElements( popupMenu, models );
				}
			}.getPopupPrepModel() );
		} else {
			this.setPopupPrepModel( org.alice.ide.croquet.models.ast.ParameterAccessMenuModel.getInstance( parameter ).getPopupPrepModel() );
		}
	}
}
