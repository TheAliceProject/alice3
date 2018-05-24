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

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.code.BackwardShiftParameterOperation;
import org.alice.ide.ast.code.ForwardShiftParameterOperation;
import org.alice.ide.ast.components.DeclarationNameLabel;
import org.alice.ide.ast.draganddrop.expression.ParameterAccessDragModel;
import org.alice.ide.ast.rename.RenameParameterComposite;
import org.alice.ide.croquet.models.ast.ParameterAccessMenuModel;
import org.alice.ide.operations.ast.DeleteParameterOperation;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.StandardMenuItemPrepModel;
import org.lgna.croquet.history.PopupPrepStep;
import org.lgna.croquet.views.MenuItemContainerUtilities;
import org.lgna.croquet.views.PopupMenu;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.UserParameter;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ParameterPane extends TransientPane {
	private NodeListProperty<UserParameter> parametersProperty;

	public ParameterPane( NodeListProperty<UserParameter> parametersProperty, final UserParameter parameter ) {
		super( ParameterAccessDragModel.getInstance( parameter ) );
		this.parametersProperty = parametersProperty;
		this.addComponent( new DeclarationNameLabel( parameter ) );
		this.setBackgroundColor( ThemeUtilities.getActiveTheme().getColorFor( ParameterAccess.class ) );
		if( this.parametersProperty != null ) {
			final DeleteParameterOperation deleteParameterOperation = new DeleteParameterOperation( this.parametersProperty, parameter );
			final ForwardShiftParameterOperation forwardShiftCodeParameterOperation = new ForwardShiftParameterOperation( this.parametersProperty, parameter );
			final BackwardShiftParameterOperation backwardShiftCodeParameterOperation = new BackwardShiftParameterOperation( this.parametersProperty, parameter );
			this.setPopupPrepModel( new MenuModel( UUID.fromString( "5b9b75d7-ce04-4f3d-8915-b825f357cef2" ) ) {
				@Override
				public void handlePopupMenuPrologue( PopupMenu popupMenu, PopupPrepStep context ) {
					super.handlePopupMenuPrologue( popupMenu, context );
					List<StandardMenuItemPrepModel> models = Lists.newLinkedList();
					models.add( RenameParameterComposite.getInstance( parameter ).getLaunchOperation().getMenuItemPrepModel() );
					if( forwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						models.add( forwardShiftCodeParameterOperation.getMenuItemPrepModel() );
					}
					if( backwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						models.add( backwardShiftCodeParameterOperation.getMenuItemPrepModel() );
					}
					models.add( MenuModel.SEPARATOR );
					models.add( deleteParameterOperation.getMenuItemPrepModel() );
					MenuItemContainerUtilities.setMenuElements( popupMenu, models );
				}
			}.getPopupPrepModel() );
		} else {
			this.setPopupPrepModel( ParameterAccessMenuModel.getInstance( parameter ).getPopupPrepModel() );
		}
	}
}
