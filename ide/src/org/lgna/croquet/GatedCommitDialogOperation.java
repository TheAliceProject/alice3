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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class GatedCommitDialogOperation extends DialogOperation {
	private static final String NULL_EXPLANATION = "good to go";
	private static final String NULL_STEP_EXPLANATION = "null step";
	public static final class InternalCompleteOperationResolver extends IndirectResolver< InternalCompleteOperation, GatedCommitDialogOperation > {
		private InternalCompleteOperationResolver( GatedCommitDialogOperation indirect ) {
			super( indirect );
		}
		public InternalCompleteOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalCompleteOperation getDirect( GatedCommitDialogOperation indirect ) {
			return indirect.completeOperation;
		}
	}
	public static final class InternalCancelOperationResolver extends IndirectResolver< InternalCancelOperation, GatedCommitDialogOperation > {
		private InternalCancelOperationResolver( GatedCommitDialogOperation indirect ) {
			super( indirect );
		}
		public InternalCancelOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalCancelOperation getDirect( GatedCommitDialogOperation indirect ) {
			return indirect.cancelOperation;
		}
	}
	private static abstract class InternalDialogOperation extends ActionOperation {
		private final GatedCommitDialogOperation gatedCommitDialogOperation;
		private org.lgna.croquet.components.Dialog dialog;
		public InternalDialogOperation( java.util.UUID id, GatedCommitDialogOperation gatedCommitDialogOperation ) {
			super( DIALOG_IMPLEMENTATION_GROUP, id );
			this.gatedCommitDialogOperation = gatedCommitDialogOperation;
		}
		public GatedCommitDialogOperation getGatedCommitDialogOperation() {
			return this.gatedCommitDialogOperation;
		}
		public org.lgna.croquet.components.Dialog getDialog() {
			return this.dialog;
		}
		public void setDialog( org.lgna.croquet.components.Dialog dialog ) {
			this.dialog = dialog;
		}
		@Override
		protected void localize() {
			//note: do not invoke super
			//super.localize();
		}
	}
	protected static final class InternalCompleteOperation extends InternalDialogOperation {
		private InternalCompleteOperation( GatedCommitDialogOperation gatedCommitDialogOperation ) {
			super( java.util.UUID.fromString( "fc908f6f-4b72-48b6-9b65-352dc9f2e18b" ), gatedCommitDialogOperation );
		}
		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			if( this.getGatedCommitDialogOperation().isClearedToClose( this.getDialog(), true ) ) {
				this.getGatedCommitDialogOperation().isCompleted = true;
				step.finish();
				this.getDialog().setVisible( false );
			}
		}
		@Override
		protected InternalCompleteOperationResolver createResolver() {
			return new InternalCompleteOperationResolver( this.getGatedCommitDialogOperation() );
		}
	}
	protected static final class InternalCancelOperation extends InternalDialogOperation {
		private InternalCancelOperation( GatedCommitDialogOperation gatedCommitDialogOperation ) {
			super( java.util.UUID.fromString( "3363c6f0-c8a2-48f2-aefc-c53894ec8a99" ), gatedCommitDialogOperation );
		}
		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			if( this.getGatedCommitDialogOperation().isClearedToClose( this.getDialog(), false ) ) {
				step.cancel();
				this.getDialog().setVisible( false );
			}
		}
		@Override
		protected InternalCancelOperationResolver createResolver() {
			return new InternalCancelOperationResolver( this.getGatedCommitDialogOperation() );
		}
	}
	
	private org.lgna.croquet.history.event.Listener listener = new org.lgna.croquet.history.event.Listener() {
		public void changing( org.lgna.croquet.history.event.Event<?> e ) {
		}
		public void changed( org.lgna.croquet.history.event.Event<?> e ) {
			GatedCommitDialogOperation.this.handleFiredEvent( e );
		}
	};

	private static class ExplanationLabel extends org.lgna.croquet.components.JComponent< javax.swing.JLabel > {
		@Override
		protected javax.swing.JLabel createAwtComponent() {
			javax.swing.JLabel rv = new javax.swing.JLabel( NULL_EXPLANATION ) {
				@Override
				protected void paintComponent( java.awt.Graphics g ) {
					if( this.getText() == NULL_EXPLANATION ) {
						//pass
					} else {
						super.paintComponent( g );
					}
				}
			};

			float f0 = 0.0f;
			float fA = 0.333f;
			float fB = 0.667f;
			float f1 = 1.0f;

			final int SCALE = 20;
			final int OFFSET = 0;

			f0 *= SCALE;
			fA *= SCALE;
			fB *= SCALE;
			f1 *= SCALE;

			final java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
			path.moveTo( fA, f0 );
			path.lineTo( fB, f0 );
			path.lineTo( f1, fA );
			path.lineTo( f1, fB );
			path.lineTo( fB, f1 );
			path.lineTo( fA, f1 );
			path.lineTo( f0, fB );
			path.lineTo( f0, fA );
			path.closePath();
			rv.setIcon( new javax.swing.Icon() {
				public int getIconWidth() {
					return SCALE + OFFSET + OFFSET;
				}
				public int getIconHeight() {
					return SCALE + OFFSET + OFFSET;
				}
				public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					java.awt.geom.AffineTransform m = g2.getTransform();
					java.awt.Font font = g2.getFont();
					java.awt.Paint paint = g2.getPaint();
					try {
						g2.translate( OFFSET + x, OFFSET + y );
						g2.fill( path );
						g2.setPaint( java.awt.Color.WHITE );
						g2.draw( path );
						g2.setPaint( java.awt.Color.WHITE );
						g2.setFont( new java.awt.Font( null, java.awt.Font.BOLD, 12 ) );
						g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
						final int FUDGE_X = 1;
						edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, "X", FUDGE_X, 0, SCALE, SCALE );
					} finally {
						g2.setTransform( m );
						g2.setPaint( paint );
						g2.setFont( font );
					}
				}
			} );
			return rv;
		}		
	}
	
	private final ExplanationLabel explanationLabel = new ExplanationLabel();
	private final InternalCompleteOperation completeOperation = new InternalCompleteOperation( this );
	private final InternalCancelOperation cancelOperation = new InternalCancelOperation( this );

	public GatedCommitDialogOperation( Group group, java.util.UUID id ) {
		super( group, id );
		this.explanationLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
		this.explanationLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 6, 8, 2, 0 ) );
		this.explanationLabel.setForegroundColor( java.awt.Color.RED.darker().darker() );
	}

	private boolean isCompleted;

	protected boolean isClearedToClose( org.lgna.croquet.components.Dialog dialog, boolean isCommit ) {
		return true;
	}
	@Override
	protected final boolean isClearedToClose( org.lgna.croquet.components.Dialog dialog ) {
		return this.isClearedToClose( dialog, false ) && super.isClearedToClose( dialog );
	}
	public final Operation getCompleteOperation() {
		return this.completeOperation;
	}
	public final Operation getCancelOperation() {
		return this.cancelOperation;
	}
	
	@Override
	protected void localize() {
		super.localize();
		this.completeOperation.setName( this.findLocalizedText( "commit" ) );
		this.cancelOperation.setName( this.findLocalizedText( "cancel" ) );
	}
	protected abstract org.lgna.croquet.components.Component< ? > createMainPanel( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.JComponent< javax.swing.JLabel > explanationLabel );
	protected abstract org.lgna.croquet.components.Component< ? > createControlsPanel( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog );
	protected abstract void release( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, boolean isCompleted );

	protected abstract String getExplanation( org.lgna.croquet.history.CompletionStep<?> step );
	protected void updateExplanation( org.lgna.croquet.history.CompletionStep<?> step ) {
		String explanation;
		if( step != null ) {
			explanation = this.getExplanation( step );
			if( explanation != null ) {
				//pass
			} else {
				explanation = NULL_EXPLANATION;
			}
		} else {
			explanation = NULL_STEP_EXPLANATION;
		}
		this.explanationLabel.getAwtComponent().setText( explanation );
		boolean isEnabled = explanation == NULL_EXPLANATION || explanation == NULL_STEP_EXPLANATION;
		this.getCompleteOperation().setEnabled( isEnabled );
	}

	public void handleFiredEvent( org.lgna.croquet.history.event.Event<?> event ) {
		org.lgna.croquet.history.CompletionStep<?> s = null;
		if( event != null ) {
			org.lgna.croquet.history.TransactionNode< ? > node = event.getNode();
			if( node != null ) {
				s = node.getFirstStepOfEquivalentModel( this, org.lgna.croquet.history.CompletionStep.class );
			}
		}
		this.updateExplanation( s );
	}
	@Override
	protected final org.lgna.croquet.components.Container< ? > createContentPane( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog ) {
		org.lgna.croquet.components.Component< ? > mainPanel = this.createMainPanel( step, dialog, this.explanationLabel );
		if( mainPanel != null ) {
			org.lgna.croquet.components.Component< ? > controlPanel = this.createControlsPanel( step, dialog );
			org.lgna.croquet.components.GridBagPanel rv = new org.lgna.croquet.components.GridBagPanel();
			rv.setBackgroundColor( mainPanel.getBackgroundColor() );

			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
			gbc.fill = java.awt.GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			rv.addComponent( mainPanel, gbc );
			gbc.weighty = 0.0;
			rv.addComponent( new org.lgna.croquet.components.HorizontalSeparator(), gbc );
			rv.addComponent( controlPanel, gbc );

			step.addListener( this.listener );
			this.updateExplanation( step );

			this.completeOperation.setDialog( dialog );
			this.cancelOperation.setDialog( dialog );
			this.isCompleted = false;
			return rv;
		} else {
			return null;
		}
	}
	@Override
	protected final void releaseContentPane( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container< ? > contentPane ) {
		if( contentPane != null ) {
			step.removeListener( this.listener );
			this.release( step, dialog, this.isCompleted );
		} else {
			step.cancel();
		}
	}
}
