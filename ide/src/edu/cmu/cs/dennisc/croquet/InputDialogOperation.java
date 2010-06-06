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

/**
 * @author Dennis Cosgrove
 */
public abstract class InputDialogOperation extends Operation<InputDialogOperationContext> {
	private class ButtonOperation extends ActionOperation {
		private boolean isOk;
		private Dialog dialog;
		public ButtonOperation(java.util.UUID individualId, String name, boolean isOk) {
			super( Application.INHERIT_GROUP, individualId );
			this.setName(name);
			this.isOk = isOk;
		}
		public void setDialog(Dialog dialog) {
			if( dialog != null ) {
				assert this.dialog == null;
			}
			this.dialog = dialog;
		}
		@Override
		protected final void perform(ActionOperationContext context) {
			assert this.dialog != null;
			InputDialogOperation.this.isOk = this.isOk;
			this.dialog.setVisible( false );
		}
	}


	private static final String NULL_EXPLANATION = "good to go";
	//private StringStateOperation explanationState = new StringStateOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "e9bb246f-f65d-487b-b226-230bbd4d4fdb" ), NULL_EXPLANATION );
	private ButtonOperation okOperation;
	private ButtonOperation cancelOperation;
	private Label explanationLabel = new Label( NULL_EXPLANATION );
	private boolean isOk = false;
	
	public InputDialogOperation(Group group, java.util.UUID individualUUID, String name, boolean isCancelDesired) {
		super(group, individualUUID);
		this.setName( name );
		this.okOperation = new ButtonOperation(java.util.UUID.fromString("f6019ff0-cf2b-4d6c-8c8d-14cac8154ebc"), "OK", true);
		if( isCancelDesired ) {
			this.cancelOperation = new ButtonOperation(java.util.UUID.fromString( "2a7e61c8-119a-45b1-830c-f59edda720a0"), "Cancel", false);
		} else {
			this.cancelOperation = null;
		}
		this.explanationLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
		this.explanationLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 4, 0, 0 ) );
	}
	public InputDialogOperation(Group group, java.util.UUID individualUUID, String title) {
		this(group, individualUUID, title, true);
	}
	public InputDialogOperation(Group group, java.util.UUID individualUUID) {
		this(group, individualUUID, null);
	}

	@Override
	protected InputDialogOperationContext createContext( ModelContext< ? > parent, java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return parent.createInputDialogOperationContext( this, e, viewController );
	}


	protected String getExplanationIfOkButtonShouldBeDisabled() {
		return null;
	}
	protected abstract Component<?> prologue( ModelContext context );
	protected abstract void epilogue( ModelContext context, boolean isOk );

	protected void updateOkOperationAndExplanation() {
		String explanation = this.getExplanationIfOkButtonShouldBeDisabled();
		this.okOperation.setEnabled( explanation == null );
		if( explanation != null ) {
			this.explanationLabel.setText( explanation );
			this.explanationLabel.setForegroundColor( java.awt.Color.RED.darker().darker() );
		} else {
			this.explanationLabel.setText( NULL_EXPLANATION );
			this.explanationLabel.setForegroundColor( this.explanationLabel.getBackgroundColor() );
		}
	}

	protected java.awt.Dimension getDesiredDialogSize( Dialog dialog ) {
		return null;
	}
	@Override
	protected final void perform(InputDialogOperationContext childContext) {
		Component<?> contentPane = this.prologue(childContext);
		if( contentPane != null ) {
			class OkCancelPanel extends Panel {
				private Button okButton = okOperation.createButton();
				public OkCancelPanel() {
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
						this.internalAddComponent( okButton );
						if( cancelOperation != null ) {
							//this.internalAddComponent( BoxUtilities.createHorizontalSliver( 2 ) );
							this.internalAddComponent( cancelOperation.createButton() );
						}
					} else {
						this.internalAddComponent( BoxUtilities.createHorizontalGlue() );
						if( cancelOperation != null ) {
							this.internalAddComponent( cancelOperation.createButton() );
							//this.internalAddComponent( BoxUtilities.createHorizontalSliver( 2 ) );
						}
						this.internalAddComponent( okButton );
					}
					this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,0,4,0 ) );
				}
				@Override
				protected java.awt.LayoutManager createLayoutManager(javax.swing.JPanel jPanel) {
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
						return new java.awt.FlowLayout( java.awt.FlowLayout.CENTER, 2, 0 );
					} else {
						return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.LINE_AXIS );
					}
				}
				public Button getOkButton() {
					return this.okButton;
				}
			};
			
			OkCancelPanel okCancelPanel = new OkCancelPanel();
			okCancelPanel.setBackgroundColor( null );

			ViewController<?,?> viewController = childContext.getViewController();
			Component<?> owner;
			if( viewController != null ) {
				owner = viewController;
			} else {
				owner = Application.getSingleton().getFrame().getContentPanel();
			}
			final Dialog dialog = new Dialog( owner );
			dialog.setTitle( this.getName() );
			dialog.setDefaultButton( okCancelPanel.getOkButton() );
			dialog.setDefaultCloseOperation( Dialog.DefaultCloseOperation.DISPOSE );
//			dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
//			dialog.addWindowListener(new java.awt.event.WindowListener() {
//				public void windowActivated(java.awt.event.WindowEvent e) {
//				}
//				public void windowDeactivated(java.awt.event.WindowEvent e) {
//				}
//				public void windowIconified(java.awt.event.WindowEvent e) {
//				}
//				public void windowDeiconified(java.awt.event.WindowEvent e) {
//				}
//				public void windowOpened(java.awt.event.WindowEvent e) {
//				}
//				public void windowClosing(java.awt.event.WindowEvent e) {
//					if (InputPanel.this.isDisposeDesired(e)) {
//						e.getWindow().dispose();
//					}
//				}
//				public void windowClosed(java.awt.event.WindowEvent e) {
//				}
//			});

			
			this.okOperation.setDialog(dialog);
			this.cancelOperation.setDialog(dialog);
			this.isOk = false;
			
			//todo:
			this.explanationLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 16, 0, 0 ) );
			
			PageAxisPanel southPanel = new PageAxisPanel();
			southPanel.addComponent( this.explanationLabel );
			southPanel.addComponent( okCancelPanel );

			java.awt.Color backgroundColor = contentPane.getBackgroundColor();
			this.explanationLabel.setBackgroundColor( backgroundColor );
			
			BorderPanel borderPanel = dialog.getContentPanel();
			borderPanel.setBackgroundColor( backgroundColor );
			borderPanel.addComponent( contentPane, BorderPanel.Constraint.CENTER );
			borderPanel.addComponent( southPanel, BorderPanel.Constraint.SOUTH );

			java.awt.Dimension size = this.getDesiredDialogSize( dialog );
			if( size != null ) {
				dialog.getAwtWindow().setSize( size );
			} else {
				dialog.pack();
			}
			//dialog.pack();
			//edu.cmu.cs.dennisc.java.awt.WindowUtilties.setLocationOnScreenToCenteredWithin(dialog.getAwtWindow(), button.getRoot().getAwtWindow());

			ModelContext.ChildrenObserver childrenObserver = new ModelContext.ChildrenObserver() {
				public void addingChild(HistoryTreeNode child) {
				}
				public void addedChild(HistoryTreeNode child) {
					InputDialogOperation.this.updateOkOperationAndExplanation();
				}
			};
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate.  observer should not need to be added to the root" );
			Application.getSingleton().getRootContext().addChildrenObserver( childrenObserver );
			this.updateOkOperationAndExplanation();
			dialog.setVisible( true );
			this.epilogue(childContext, this.isOk);
			Application.getSingleton().getRootContext().removeChildrenObserver( childrenObserver );

			this.okOperation.setDialog(null);
			this.cancelOperation.setDialog(null);
		} else {
			this.epilogue(childContext, false);
		}
		childContext.finish();
	}
}
