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
public abstract class InputPanelOperation extends AbstractActionOperation {
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
		protected void perform(edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton<?> button) {
			assert this.dialog != null;
			InputPanelOperation.this.isOk = this.isOk;
//			if (this.isOk) {
//				context.finish();
//			} else {
//				context.cancel();
//			}
			this.dialog.setVisible( false );
		}
	}
	public static interface Validator {
		public boolean isInputValid();
	}

	private java.util.List<Validator> validators = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private ButtonOperation okOperation;
	private ButtonOperation cancelOperation;
	private boolean isOk = false;
	
	public InputPanelOperation(java.util.UUID groupUUID, java.util.UUID individualUUID, String name, boolean isCancelDesired) {
		super(groupUUID, individualUUID);
		this.setName( name );
		this.okOperation = new ButtonOperation(java.util.UUID.fromString("f6019ff0-cf2b-4d6c-8c8d-14cac8154ebc"), "OK", true);
		if( isCancelDesired ) {
			this.cancelOperation = new ButtonOperation(java.util.UUID.fromString( "2a7e61c8-119a-45b1-830c-f59edda720a0"), "Cancel", false);
		} else {
			this.cancelOperation = null;
		}
	}
	public InputPanelOperation(java.util.UUID groupUUID, java.util.UUID individualUUID, String title) {
		this(groupUUID, individualUUID, title, true);
	}


	protected abstract edu.cmu.cs.dennisc.croquet.Component<?> prologue( edu.cmu.cs.dennisc.croquet.Context context );
	protected abstract void epilogue( edu.cmu.cs.dennisc.croquet.Context context, boolean isOk );

	public void addValidator(Validator validator) {
		this.validators.add(validator);
	}

	public void removeValidator(Validator validator) {
		this.validators.remove(validator);
	}

	@Override
	protected final void perform(edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton<?> button) {
		Component<?> contentPane = this.prologue(context);
		if( contentPane != null ) {
			class BottomPanel extends Panel {
				private Button okButton = okOperation.createButton();
				public BottomPanel() {
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
						this.internalAddComponent( okButton );
						if( cancelOperation != null ) {
							this.internalAddComponent( BoxUtilities.createHorizontalStrut( 4 ) );
							this.internalAddComponent( cancelOperation.createButton() );
						}
					} else {
						this.internalAddComponent( BoxUtilities.createHorizontalGlue() );
						if( cancelOperation != null ) {
							this.internalAddComponent( cancelOperation.createButton() );
							this.internalAddComponent( BoxUtilities.createHorizontalStrut( 4 ) );
						}
						this.internalAddComponent( okButton );
					}
					this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
				}
				@Override
				protected java.awt.LayoutManager createLayoutManager(javax.swing.JPanel jPanel) {
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
						return new java.awt.FlowLayout();
					} else {
						return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.LINE_AXIS );
					}
				}
				public Button getOkButton() {
					return this.okButton;
				}
			};
			
			BottomPanel bottomPanel = new BottomPanel();
			bottomPanel.setOpaque( false );

			final Dialog dialog = new Dialog(button);
			dialog.setTitle( this.getName() );
			dialog.setDefaultButton( bottomPanel.getOkButton() );
			dialog.setDefaultCloseOperation( edu.cmu.cs.dennisc.croquet.Dialog.DefaultCloseOperation.DISPOSE );
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
			BorderPanel borderPanel = dialog.getContentPanel();
			borderPanel.setBackgroundColor( contentPane.getBackgroundColor() );
			borderPanel.addComponent( contentPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
			borderPanel.addComponent( bottomPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.SOUTH );
			
			dialog.pack();
			//edu.cmu.cs.dennisc.java.awt.WindowUtilties.setLocationOnScreenToCenteredWithin(dialog.getAwtWindow(), button.getRoot().getAwtWindow());

			dialog.setVisible( true );
			this.epilogue(context, this.isOk);
			this.okOperation.setDialog(null);
			this.cancelOperation.setDialog(null);
		} else {
			this.epilogue(context, false);
		}
	}
}
