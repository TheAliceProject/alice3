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
public abstract class InputDialogOperation<J extends Component<?>> extends AbstractDialogOperation<InputDialogOperationContext<J>> {
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
			//this.dialog.getAwtWindow().dispose();
		}
	}


	private static final String NULL_EXPLANATION = "good to go";
	private ButtonOperation okOperation;
	private ButtonOperation cancelOperation;
	private Label explanationLabel = new Label( NULL_EXPLANATION ) {
		@Override
		protected javax.swing.JLabel createAwtComponent() {
			return new javax.swing.JLabel() {
				@Override
				protected void paintComponent(java.awt.Graphics g) {
					if( this.getText() == NULL_EXPLANATION ) {
						//pass
					} else {
						super.paintComponent( g );
					}
				}
			};
		};
	};
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
		this.explanationLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 16, 0, 0 ) );
		this.explanationLabel.setForegroundColor( java.awt.Color.RED.darker().darker() );
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


	protected String getExplanationIfOkButtonShouldBeDisabled( InputDialogOperationContext<J> context ) {
		return null;
	}
	protected abstract J prologue( InputDialogOperationContext<J> context );
	protected abstract void epilogue( InputDialogOperationContext<J> context, boolean isOk );

	protected void updateOkOperationAndExplanation( InputDialogOperationContext<J> context ) {
		if( context != null ) {
			String explanation = this.getExplanationIfOkButtonShouldBeDisabled( context );
			this.okOperation.setEnabled( explanation == null );
			if( explanation != null ) {
				this.explanationLabel.setText( explanation );
			} else {
				this.explanationLabel.setText( NULL_EXPLANATION );
			}
		} else {
			this.explanationLabel.setText( "todo: updateOkOperationAndExplanation context==null" );
			this.okOperation.setEnabled( true );
		}
	}

	private ModelContext.ChildrenObserver childrenObserver = new ModelContext.ChildrenObserver() {
		public void addingChild(HistoryNode child) {
		}
		public void addedChild(HistoryNode child) {
			InputDialogOperation.this.updateOkOperationAndExplanation( (InputDialogOperationContext<J>)child.findContextFor( InputDialogOperation.this ) );
		}
	};

	@Override
	protected final Container<?> createContentPane(InputDialogOperationContext<J> context, Dialog dialog) {
		J mainPane = this.prologue(context);
		assert mainPane != null;
		context.setMainPanel( mainPane );
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
				this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,0,4,0 ) );
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

		PageAxisPanel southPanel = new PageAxisPanel();
		southPanel.addComponent( this.explanationLabel );
		southPanel.addComponent( okCancelPanel );

		java.awt.Color backgroundColor = mainPane.getBackgroundColor();
		this.explanationLabel.setBackgroundColor( backgroundColor );
		
		BorderPanel borderPanel = dialog.getContentPanel();
		borderPanel.setBackgroundColor( backgroundColor );
		borderPanel.addComponent( mainPane, BorderPanel.Constraint.CENTER );
		borderPanel.addComponent( southPanel, BorderPanel.Constraint.SOUTH );
		
		dialog.setDefaultButton( okCancelPanel.getOkButton() );

		this.okOperation.setDialog(dialog);
		this.cancelOperation.setDialog(dialog);
		this.isOk = false;

		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate.  observer should not need to be added to the root" );
		Application.getSingleton().getRootContext().addChildrenObserver( this.childrenObserver );
		this.updateOkOperationAndExplanation( context );

		return borderPanel;
	}
	@Override
	protected final void releaseContentPane(InputDialogOperationContext<J> context, Dialog dialog, Container<?> contentPane) {
		this.epilogue(context, this.isOk);
		
		Application.getSingleton().getRootContext().removeChildrenObserver( this.childrenObserver );

		this.okOperation.setDialog(null);
		this.cancelOperation.setDialog(null);
		if( this.isOk ) {
			//pass
		} else {
			assert context.isCanceled();
		}
	}
}
