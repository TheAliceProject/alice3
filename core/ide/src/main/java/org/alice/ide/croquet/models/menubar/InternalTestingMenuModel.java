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

import org.alice.ide.croquet.models.ui.debug.ActiveTransactionHistoryComposite;
import org.alice.ide.croquet.models.ui.debug.BreakProjectAddNullMethodOperation;
import org.alice.ide.croquet.models.ui.debug.RaiseAnomalousSituationOperation;
import org.alice.ide.croquet.models.ui.debug.ThrowBogusExceptionOperation;
import org.alice.ide.croquet.models.ui.debug.ThrowBogusGlExceptionOperation;
import org.alice.ide.croquet.models.ui.debug.ThrowBogusLgnaExceptionOperation;
import org.alice.ide.croquet.models.ui.preferences.IsFullTypeHierarchyDesiredState;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingManagedUserMethods;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingPackagePrivateUserMethods;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingPrivateUserMethods;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingProtectedUserMethods;
import org.alice.ide.highlight.ShowMeOperation;
import org.alice.ide.javacode.croquet.JavaCodeFrameComposite;
import org.alice.ide.testing.framesize.croquet.CycleFrameSizeOperation;
import org.alice.stageide.raytrace.ExportToPovRayOperation;
import org.alice.stageide.showme.ShowMeHowToAddGalleryModelsIteratingOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.PredeterminedMenuModel;
import org.lgna.croquet.imp.frame.LazyIsFrameShowingState;
import org.lgna.debug.pick.croquet.PickDebugFrame;
import org.lgna.debug.tree.croquet.GlrDebugFrame;
import org.lgna.debug.tree.croquet.SgDebugFrame;

import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class InternalTestingMenuModel extends PredeterminedMenuModel {
	private static class SingletonHolder {
		private static InternalTestingMenuModel instance = new InternalTestingMenuModel();
	}

	public static InternalTestingMenuModel getInstance() {
		return SingletonHolder.instance;
	}

	private static BooleanState createSgDebugFrameLazyIsFrameShowingState() {
		BooleanState rv = LazyIsFrameShowingState.createNoArgumentConstructorInstance(
				Application.INFORMATION_GROUP,
				SgDebugFrame.class );

		rv.initializeIfNecessary();
		rv.setTextForBothTrueAndFalse( "Debug SceneGraph" );
		rv.getImp().getSwingModel().getAction().putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_F7, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK ) );

		return rv;
	}

	private static BooleanState createGlrDebugFrameLazyIsFrameShowingState() {
		BooleanState rv = LazyIsFrameShowingState.createNoArgumentConstructorInstance(
				Application.INFORMATION_GROUP,
				GlrDebugFrame.class );

		rv.initializeIfNecessary();
		rv.setTextForBothTrueAndFalse( "Debug Render" );
		rv.getImp().getSwingModel().getAction().putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_F8, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK ) );

		return rv;
	}

	private static BooleanState createPickDebugFrameLazyIsFrameShowingState() {
		BooleanState rv = LazyIsFrameShowingState.createNoArgumentConstructorInstance(
				Application.INFORMATION_GROUP,
				PickDebugFrame.class );

		rv.initializeIfNecessary();
		rv.setTextForBothTrueAndFalse( "Debug Pick" );
		rv.getImp().getSwingModel().getAction().putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_F9, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK ) );

		return rv;
	}

	private InternalTestingMenuModel() {
		super( UUID.fromString( "6ee5bc6c-f45f-4eb9-bc4b-67fc524a05e8" ),
				createSgDebugFrameLazyIsFrameShowingState().getMenuItemPrepModel(),
				createGlrDebugFrameLazyIsFrameShowingState().getMenuItemPrepModel(),
				createPickDebugFrameLazyIsFrameShowingState().getMenuItemPrepModel(),
				SEPARATOR,
				ShowMeHowToAddGalleryModelsIteratingOperation.getInstance().getMenuItemPrepModel(),
				SEPARATOR,
				JavaCodeFrameComposite.getInstance().getIsFrameShowingState().getMenuItemPrepModel(),
				SEPARATOR,
				ThrowBogusExceptionOperation.getInstance().getMenuItemPrepModel(),
				ThrowBogusGlExceptionOperation.getInstance().getMenuItemPrepModel(),
				ThrowBogusLgnaExceptionOperation.getInstance().getMenuItemPrepModel(),
				RaiseAnomalousSituationOperation.getInstance().getMenuItemPrepModel(),
				SEPARATOR,
				BreakProjectAddNullMethodOperation.getInstance().getMenuItemPrepModel(),
				SEPARATOR,
				new CycleFrameSizeOperation().getMenuItemPrepModel(),
				SEPARATOR,
				ActiveTransactionHistoryComposite.getInstance().getIsFrameShowingState().getMenuItemPrepModel(),
				//org.alice.ide.croquet.models.ui.debug.IsAbstractSyntaxTreeShowingState.getInstance().getMenuItemPrepModel(),
				IsFullTypeHierarchyDesiredState.getInstance().getMenuItemPrepModel(),
				IsIncludingPackagePrivateUserMethods.getInstance().getMenuItemPrepModel(),
				IsIncludingProtectedUserMethods.getInstance().getMenuItemPrepModel(),
				IsIncludingPrivateUserMethods.getInstance().getMenuItemPrepModel(),
				IsIncludingManagedUserMethods.getInstance().getMenuItemPrepModel(),
				ExportToPovRayOperation.getInstance().getMenuItemPrepModel(),
				new ShowMeOperation().getMenuItemPrepModel() );
	}
}
