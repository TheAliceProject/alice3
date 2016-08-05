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
package org.alice.stageide.type.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class ContainsTabPane extends org.lgna.croquet.views.MigPanel {
	private final org.lgna.croquet.views.TextField filterTextField;
	private final org.lgna.croquet.views.List<org.lgna.project.ast.Member> listView;

	private final java.awt.event.FocusListener focusListener = new java.awt.event.FocusListener() {
		@Override
		public void focusGained( java.awt.event.FocusEvent e ) {
		}

		@Override
		public void focusLost( java.awt.event.FocusEvent e ) {
			getComposite().getMemberListState().clearSelection();
		}
	};

	private final java.awt.event.ActionListener downAction = new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.project.ast.Member> state = getComposite().getMemberListState();
			if( state.getItemCount() > 0 ) {
				state.setSelectedIndex( 0 );
			}
			listView.requestFocusLater();
		}
	};

	private final java.awt.event.ActionListener prevUpAction;
	private final java.awt.event.ActionListener upAction = new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.project.ast.Member> state = getComposite().getMemberListState();
			if( state.getSelectedIndex() == 0 ) {
				state.clearSelection();
				filterTextField.requestFocusLater();
			} else {
				if( prevUpAction != null ) {
					prevUpAction.actionPerformed( e );
				}
			}
		}
	};

	public ContainsTabPane( org.alice.stageide.type.croquet.ContainsTab tab ) {
		super( tab, "fill", "[grow,shrink]", "[grow 0, shrink 0][grow 0, shrink 0][grow, shrink]" );
		java.awt.Color color = new java.awt.Color( 221, 221, 255 );
		this.setBackgroundColor( color );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

		this.filterTextField = tab.getFilterState().createTextField();
		this.filterTextField.enableSelectAllWhenFocusGained();

		this.listView = tab.getMemberListState().createList();
		this.listView.setCellRenderer( new org.alice.stageide.type.croquet.views.renderers.MemberCellRenderer() );
		org.lgna.croquet.views.ScrollPane listScrollPane = new org.lgna.croquet.views.VerticalScrollBarPaintOmittingWhenAppropriateScrollPane( this.listView );

		this.addComponent( new org.lgna.croquet.views.Label( "<html>Search for a procedure or function<br>whose class you would like to select.</html>" ), "wrap" );
		this.addComponent( this.filterTextField, "growx, wrap" );
		this.addComponent( listScrollPane, "grow" );

		this.filterTextField.registerKeyboardAction( this.downAction, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_DOWN, 0 ), Condition.WHEN_FOCUSED );

		javax.swing.KeyStroke upKeyStroke = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_UP, 0 );
		this.prevUpAction = this.listView.getAwtComponent().getActionForKeyStroke( upKeyStroke );
		this.listView.registerKeyboardAction( this.upAction, upKeyStroke, Condition.WHEN_FOCUSED );
	}

	@Override
	public org.alice.stageide.type.croquet.ContainsTab getComposite() {
		return (org.alice.stageide.type.croquet.ContainsTab)super.getComposite();
	}

	@Override
	public void handleCompositePreActivation() {
		super.handleCompositePreActivation();
		this.filterTextField.requestFocusLater();
		this.listView.getAwtComponent().addFocusListener( this.focusListener );

	}

	@Override
	public void handleCompositePostDeactivation() {
		this.listView.getAwtComponent().removeFocusListener( this.focusListener );
		super.handleCompositePostDeactivation();
	}
}
