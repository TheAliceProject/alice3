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
package org.alice.ide.declarationseditor.components;

/**
 * @author Dennis Cosgrove
 */
public class BackwardForwardView extends org.lgna.croquet.views.MigPanel {
	public BackwardForwardView( org.alice.ide.declarationseditor.BackwardForwardComposite composite ) {
		super( composite, "insets 0", "[]0[]", "" );
		// note:
		// trigger side effect to initialize isEnabled
		org.alice.ide.declarationseditor.DeclarationCompositeHistory.getInstance();

		final boolean ARE_BACKWARD_AND_FORWARD_BUTTONS_DESIRED = false;
		if( ARE_BACKWARD_AND_FORWARD_BUTTONS_DESIRED ) {
			org.lgna.croquet.views.Button backwardButton = org.alice.ide.declarationseditor.BackwardOperation.getInstance().createButtonWithRightClickCascade( org.alice.ide.declarationseditor.BackwardCascade.getInstance() );
			org.lgna.croquet.views.Button forwardButton = org.alice.ide.declarationseditor.ForwardOperation.getInstance().createButtonWithRightClickCascade( org.alice.ide.declarationseditor.ForwardCascade.getInstance() );

			backwardButton.tightenUpMargin();
			forwardButton.tightenUpMargin();

			final boolean ARE_CASCADE_BUTTONS_DESIRED = false;
			if( ARE_CASCADE_BUTTONS_DESIRED ) {
				org.lgna.croquet.views.PopupButton backwardPopupButton = org.alice.ide.declarationseditor.BackwardCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();
				org.lgna.croquet.views.PopupButton forwardPopupButton = org.alice.ide.declarationseditor.ForwardCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();

				backwardPopupButton.tightenUpMargin();
				forwardPopupButton.tightenUpMargin();

				org.lgna.croquet.views.BorderPanel backwardPanel = new org.lgna.croquet.views.BorderPanel.Builder()
						.center( backwardButton )
						.lineEnd( backwardPopupButton )
						.build();
				org.lgna.croquet.views.BorderPanel forwardPanel = new org.lgna.croquet.views.BorderPanel.Builder()
						.center( forwardButton )
						.lineEnd( forwardPopupButton )
						.build();

				this.addComponent( backwardPanel );
				this.addComponent( forwardPanel, "growy" );
			} else {
				this.addComponent( backwardButton );
				this.addComponent( forwardButton, "growy" );
			}
		}

		if( org.alice.ide.preferences.IsToolBarShowing.getValue() ) {
			//pass
		} else {
			this.addComponent( org.alice.ide.clipboard.Clipboard.SINGLETON.getDragComponent(), "gap 8" );
		}

		this.setMaximumSizeClampedToPreferredSize( true );
	}
}
