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
package edu.cmu.cs.dennisc.croquet;

abstract class DialogOperationWithControls<C extends AbstractDialogOperationContext<?>> extends AbstractDialogOperation<C> {
	private static final String NULL_EXPLANATION = "good to go";
	private Label explanationLabel = new Label( NULL_EXPLANATION ) {
		@Override
		protected javax.swing.JLabel createAwtComponent() {
			javax.swing.JLabel rv = new javax.swing.JLabel() {
				@Override
				protected void paintComponent(java.awt.Graphics g) {
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
						g2.translate( OFFSET+x, OFFSET+x );
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
		};
	};
	private ModelContext.ChildrenObserver childrenObserver = new ModelContext.ChildrenObserver() {
		public void addingChild(HistoryNode child) {
		}
		public void addedChild(HistoryNode child) {
			DialogOperationWithControls.this.updateExplanation( (C)child.findContextFor( DialogOperationWithControls.this ) );
		}
	};
	
	public DialogOperationWithControls( Group group, java.util.UUID id ) {
		super( group, id );
		this.explanationLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
		//this.explanationLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 16, 0, 0 ) );
		this.explanationLabel.setForegroundColor( java.awt.Color.RED.darker().darker() );
	}

	protected abstract Component<?> createMainPanel( C context, Dialog dialog, Label explanationLabel );
	protected abstract Component<?> createControlsPanel( C context, Dialog dialog );
	protected void updateExplanation( C context ) {
		this.explanationLabel.setText( "todo" );
//		if( context != null ) {
//			String explanation = this.getExplanationIfNextButtonShouldBeDisabled( context );
//			if( this.externalNextButtonDisabler != null ) {
//				String externalExplanation = this.externalNextButtonDisabler.getExplanationIfNextButtonShouldBeDisabled( context );
//				if( externalExplanation != null ) {
//					explanation = externalExplanation;
//				}
//			}
//			this.okOperation.setEnabled( explanation == null );
//			if( explanation != null ) {
//				this.explanationLabel.setText( explanation );
//			} else {
//				this.explanationLabel.setText( NULL_EXPLANATION );
//			}
//		} else {
//			this.explanationLabel.setText( "todo: updateOkOperationAndExplanation context==null" );
//			this.okOperation.setEnabled( true );
//		}
	}
	
	@Override
	protected final Container<?> createContentPane(C context, Dialog dialog) {
		Component< ? > mainPanel = this.createMainPanel( context, dialog, this.explanationLabel );
		Component< ? > controlPanel = this.createControlsPanel( context, dialog );
		GridBagPanel rv = new GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		rv.addComponent( mainPanel, gbc );
		gbc.weighty = 0.0;
		rv.addComponent( new HorizontalSeparator(), gbc );
		rv.addComponent( controlPanel, gbc );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
		this.updateExplanation( context );
		return rv;
	}
	@Override
	protected final void releaseContentPane(C context, Dialog dialog, Container<?> contentPane) {
		if( contentPane != null ) {
			Application.getSingleton().getRootContext().removeChildrenObserver( this.childrenObserver );
		} else {
			context.cancel();
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class WizardDialogOperation extends DialogOperationWithControls<WizardDialogOperationContext> {
	protected static final Group ENCLOSING_WIZARD_DIALOG_GROUP = new Group( java.util.UUID.fromString( "100a8027-cf11-4070-abd5-450f8c5ab1cc" ), "ENCLOSING_WIZARD_DIALOG_GROUP" );
	private static class NextOperation extends ActionOperation {
		public NextOperation() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "e1239539-1eb0-411d-b808-947d0b1c1e94" ) );
		}
		@Override
		void localize() {
			super.localize();
			this.setName( "Next >" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
		}
	}
	private static class PreviousOperation extends ActionOperation {
		public PreviousOperation() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "e1239539-1eb0-411d-b808-947d0b1c1e94" ) );
		}
		@Override
		void localize() {
			super.localize();
			this.setName( "< Back" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
		}
	}
	private static class FinishOperation extends ActionOperation {
		public FinishOperation() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "6acdd95e-849c-4281-9779-994e9807b25b" ) );
		}
		@Override
		void localize() {
			super.localize();
			this.setName( "Finish" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
		}
	}
	private static class CancelOperation extends ActionOperation {
		public CancelOperation() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "3363c6f0-c8a2-48f2-aefc-c53894ec8a99" ) );
		}
		@Override
		void localize() {
			super.localize();
			this.setName( "Cancel" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
		}
	}

	private PreviousOperation prevOperation = new PreviousOperation();
	private NextOperation nextOperation = new NextOperation();
	private FinishOperation finishOperation = new FinishOperation();
	private CancelOperation cancelOperation = new CancelOperation();
	public WizardDialogOperation(Group group, java.util.UUID individualId, boolean isCancelDesired) {
		super(group, individualId);
	}
	public WizardDialogOperation(Group group, java.util.UUID individualId) {
		this(group, individualId, true);
	}

	@Override
	protected WizardDialogOperationContext createContext( ModelContext< ? > parent, java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return parent.createWizardDialogOperationContext( this, e, viewController );
	}
	protected abstract WizardStep[] prologue( WizardDialogOperationContext context );
	protected abstract void epilogue( WizardDialogOperationContext context, boolean isOk );
	
	@Override
	protected Component< ? > createControlsPanel( WizardDialogOperationContext context, Dialog dialog ) {
		LineAxisPanel rv = new LineAxisPanel();
		rv.addComponent( BoxUtilities.createHorizontalGlue() );
		rv.addComponent( prevOperation.createButton() );
		rv.addComponent( nextOperation.createButton() );
		rv.addComponent( BoxUtilities.createHorizontalSliver( 8 ) );
		rv.addComponent( finishOperation.createButton() );
		rv.addComponent( BoxUtilities.createHorizontalSliver( 8 ) );
		rv.addComponent( cancelOperation.createButton() );
		return rv;
	}
	@Override
	protected Component< ? > createMainPanel( WizardDialogOperationContext context, Dialog dialog, Label explanationLabel ) {
		BorderPanel rv = new BorderPanel();
		rv.addComponent( new Label( "steps" ), BorderPanel.Constraint.LINE_START );
		rv.addComponent( new Label( "todo" ), BorderPanel.Constraint.CENTER );
		rv.addComponent( explanationLabel, BorderPanel.Constraint.PAGE_END );
		return rv;
	}
	
	public static void main( String[] args ) {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			try {
				edu.cmu.cs.dennisc.javax.swing.plaf.nimbus.NimbusUtilities.installModifiedNimbus( lookAndFeelInfo );
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		}
		org.alice.stageide.StageIDE stageIDE = new org.alice.stageide.StageIDE();
		WizardDialogOperation wizardDialogOperation = new WizardDialogOperation( null, null ) {
			@Override
			protected java.awt.Dimension getDesiredDialogSize( edu.cmu.cs.dennisc.croquet.Dialog dialog ) {
				return new java.awt.Dimension( 640, 480 );
			}
			@Override
			protected WizardStep[] prologue( WizardDialogOperationContext context ) {
				return null;
			}
			@Override
			protected void epilogue( WizardDialogOperationContext context, boolean isOk ) {
				// TODO Auto-generated method stub
				
			}
		};
		wizardDialogOperation.fire();
		System.exit( 0 );
	}
}
