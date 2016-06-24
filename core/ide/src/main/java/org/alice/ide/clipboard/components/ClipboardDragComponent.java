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
package org.alice.ide.clipboard.components;

import org.alice.ide.clipboard.DragReceptorState;

/**
 * @author Dennis Cosgrove
 */
public class ClipboardDragComponent extends org.lgna.croquet.views.DragComponent<org.lgna.croquet.DragModel> {
	private class ClipboardDropReceptor extends org.lgna.croquet.AbstractDropReceptor {
		private org.alice.ide.clipboard.DragReceptorState dragReceptorState = org.alice.ide.clipboard.DragReceptorState.IDLE;

		@Override
		public boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
			return dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementDragModel;
		}

		private void setDragReceptorState( org.alice.ide.clipboard.DragReceptorState dragReceptorState ) {
			this.dragReceptorState = dragReceptorState;
			ClipboardDragComponent.this.repaint();
		}

		@Override
		public void dragStarted( org.lgna.croquet.history.DragStep step ) {
			this.setDragReceptorState( org.alice.ide.clipboard.DragReceptorState.STARTED );
		}

		@Override
		public void dragEntered( org.lgna.croquet.history.DragStep step ) {
			this.setDragReceptorState( org.alice.ide.clipboard.DragReceptorState.ENTERED );
			//			step.getDragSource().hideDragProxy();
		}

		@Override
		public org.lgna.croquet.DropSite dragUpdated( org.lgna.croquet.history.DragStep step ) {
			return org.alice.ide.clipboard.Clipboard.SINGLETON.getDropSite();
		}

		@Override
		protected org.lgna.croquet.Model dragDroppedPostRejectorCheck( org.lgna.croquet.history.DragStep step ) {
			org.lgna.croquet.DragModel dragModel = step.getModel();
			if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementDragModel ) {
				org.alice.ide.ast.draganddrop.statement.StatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.StatementDragModel)dragModel;
				org.lgna.project.ast.Statement statement = statementDragModel.getStatement();
				boolean isCopy = edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.isQuoteControlUnquoteDown( step.getLatestMouseEvent() );
				if( isCopy ) {
					return org.alice.ide.clipboard.CopyToClipboardOperation.getInstance( statement );
				} else {
					return org.alice.ide.clipboard.CutToClipboardOperation.getInstance( statement );
				}
			} else {
				return null;
			}
		}

		@Override
		public void dragExited( org.lgna.croquet.history.DragStep step, boolean isDropRecipient ) {
			//			step.getDragSource().showDragProxy();
			this.setDragReceptorState( DragReceptorState.STARTED );
		}

		@Override
		public void dragStopped( org.lgna.croquet.history.DragStep step ) {
			this.setDragReceptorState( DragReceptorState.IDLE );
		}

		@Override
		public org.lgna.croquet.views.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
			return ClipboardDragComponent.this;
		}

		@Override
		public org.lgna.croquet.views.SwingComponentView<?> getViewController() {
			return ClipboardDragComponent.this;
		}
	}

	private final ClipboardDropReceptor dropReceptor = new ClipboardDropReceptor();
	private final org.lgna.croquet.views.FlowPanel subject = new org.lgna.croquet.views.FlowPanel();

	public ClipboardDragComponent( org.lgna.croquet.DragModel dragModel ) {
		super( dragModel, true );
	}

	@Override
	protected boolean isClickAndClackAppropriate() {
		return true;
	}

	public org.lgna.croquet.DropReceptor getDropReceptor() {
		return this.dropReceptor;
	}

	@Override
	public org.lgna.croquet.DragModel getModel() {
		if( org.alice.ide.clipboard.Clipboard.SINGLETON.isStackEmpty() ) {
			return null;
		} else {
			return super.getModel();
		}
	}

	public void refresh() {
		this.subject.forgetAndRemoveAllComponents();
		if( org.alice.ide.clipboard.Clipboard.SINGLETON.isStackEmpty() ) {
			this.setToolTipText( null );
		} else {
			this.setToolTipText( "" );
			org.lgna.project.ast.Node node = org.alice.ide.clipboard.Clipboard.SINGLETON.peek();
			if( node instanceof org.lgna.project.ast.Statement ) {
				org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)node;
				subject.addComponent( org.alice.ide.x.PreviewAstI18nFactory.getInstance().createStatementPane( statement ) );
				subject.revalidateAndRepaint();
			}
		}
		this.repaint();
	}

	@Override
	public org.lgna.croquet.views.SwingComponentView<?> getSubject() {
		return this.subject;
	}

	private static final org.alice.ide.clipboard.icons.ClipboardIcon ICON = new org.alice.ide.clipboard.icons.ClipboardIcon();

	@Override
	protected org.lgna.croquet.views.imp.JDragView createAwtComponent() {
		org.lgna.croquet.views.imp.JDragView rv = new org.lgna.croquet.views.imp.JDragView() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( ( ICON.getOrigWidth() * 4 ) / 5, ( ICON.getOrigHeight() * 4 ) / 5 );
			}

			@Override
			public javax.swing.JToolTip createToolTip() {
				return new edu.cmu.cs.dennisc.javax.swing.tooltips.JToolTip( ClipboardDragComponent.this.subject.getAwtComponent() );
			}

			@Override
			public void paint( java.awt.Graphics g ) {
				super.paint( g );
				synchronized( ICON ) {
					ICON.setDimension( this.getSize() );
					ICON.setFull( org.alice.ide.clipboard.Clipboard.SINGLETON.isStackEmpty() == false );
					ICON.setDragReceptorState( dropReceptor.dragReceptorState );
					ICON.paintIcon( this, g, 0, 0 );
				}
				if( org.alice.ide.clipboard.Clipboard.SINGLETON.getStackSize() > 1 ) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					Object prevTextAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING );
					g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, Integer.toString( org.alice.ide.clipboard.Clipboard.SINGLETON.getStackSize() ), this.getSize() );
					g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, prevTextAntialiasing );
				}
			}
		};
		rv.setOpaque( false );
		return rv;
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRect( x, y, width, height );
	}

	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
	}
}
