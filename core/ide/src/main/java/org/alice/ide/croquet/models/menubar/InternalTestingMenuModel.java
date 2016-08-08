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
package org.alice.ide.croquet.models.menubar;


/**
 * @author Dennis Cosgrove
 */
public class InternalTestingMenuModel extends org.lgna.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static InternalTestingMenuModel instance = new InternalTestingMenuModel();
	}

	public static InternalTestingMenuModel getInstance() {
		return SingletonHolder.instance;
	}

	private static org.lgna.croquet.BooleanState createSgDebugFrameLazyIsFrameShowingState() {
		org.lgna.croquet.BooleanState rv = org.lgna.croquet.imp.frame.LazyIsFrameShowingState.createNoArgumentConstructorInstance(
				org.lgna.croquet.Application.INFORMATION_GROUP,
				org.lgna.debug.tree.croquet.SgDebugFrame.class );

		rv.initializeIfNecessary();
		rv.setTextForBothTrueAndFalse( "Debug SceneGraph" );
		rv.getImp().getSwingModel().getAction().putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F7, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK ) );

		return rv;
	}

	private static org.lgna.croquet.BooleanState createGlrDebugFrameLazyIsFrameShowingState() {
		org.lgna.croquet.BooleanState rv = org.lgna.croquet.imp.frame.LazyIsFrameShowingState.createNoArgumentConstructorInstance(
				org.lgna.croquet.Application.INFORMATION_GROUP,
				org.lgna.debug.tree.croquet.GlrDebugFrame.class );

		rv.initializeIfNecessary();
		rv.setTextForBothTrueAndFalse( "Debug Render" );
		rv.getImp().getSwingModel().getAction().putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F8, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK ) );

		return rv;
	}

	private static org.lgna.croquet.BooleanState createPickDebugFrameLazyIsFrameShowingState() {
		org.lgna.croquet.BooleanState rv = org.lgna.croquet.imp.frame.LazyIsFrameShowingState.createNoArgumentConstructorInstance(
				org.lgna.croquet.Application.INFORMATION_GROUP,
				org.lgna.debug.pick.croquet.PickDebugFrame.class );

		rv.initializeIfNecessary();
		rv.setTextForBothTrueAndFalse( "Debug Pick" );
		rv.getImp().getSwingModel().getAction().putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F9, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK ) );

		return rv;
	}

	private InternalTestingMenuModel() {
		super( java.util.UUID.fromString( "6ee5bc6c-f45f-4eb9-bc4b-67fc524a05e8" ),
				createSgDebugFrameLazyIsFrameShowingState().getMenuItemPrepModel(),
				createGlrDebugFrameLazyIsFrameShowingState().getMenuItemPrepModel(),
				createPickDebugFrameLazyIsFrameShowingState().getMenuItemPrepModel(),
				SEPARATOR,
				org.alice.stageide.showme.ShowMeHowToAddGalleryModelsIteratingOperation.getInstance().getMenuItemPrepModel(),
				SEPARATOR,
				org.alice.ide.javacode.croquet.JavaCodeFrameComposite.getInstance().getIsFrameShowingState().getMenuItemPrepModel(),
				SEPARATOR,
				org.alice.ide.croquet.models.ui.debug.ThrowBogusExceptionOperation.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.debug.ThrowBogusGlExceptionOperation.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.debug.ThrowBogusLgnaExceptionOperation.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.debug.RaiseAnomalousSituationOperation.getInstance().getMenuItemPrepModel(),
				SEPARATOR,
				org.alice.ide.croquet.models.ui.debug.BreakProjectAddNullMethodOperation.getInstance().getMenuItemPrepModel(),
				SEPARATOR,
				new org.alice.ide.testing.framesize.croquet.CycleFrameSizeOperation().getMenuItemPrepModel(),
				SEPARATOR,
				org.alice.ide.croquet.models.ui.debug.ActiveTransactionHistoryComposite.getInstance().getIsFrameShowingState().getMenuItemPrepModel(),
				//org.alice.ide.croquet.models.ui.debug.IsAbstractSyntaxTreeShowingState.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.preferences.IsFullTypeHierarchyDesiredState.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.preferences.IsIncludingPackagePrivateUserMethods.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.preferences.IsIncludingProtectedUserMethods.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.preferences.IsIncludingPrivateUserMethods.getInstance().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.ui.preferences.IsIncludingManagedUserMethods.getInstance().getMenuItemPrepModel(),
				org.alice.stageide.raytrace.ExportToPovRayOperation.getInstance().getMenuItemPrepModel(),
				new org.alice.ide.highlight.ShowMeOperation().getMenuItemPrepModel() );
	}
}
