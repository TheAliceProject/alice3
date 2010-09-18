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
	protected static final Group ENCLOSING_INPUT_DIALOG_GROUP = Group.getInstance( java.util.UUID.fromString( "8dc8d3e5-9153-423e-bf1b-caa94597f57c" ), "ENCLOSING_INPUT_DIALOG_GROUP" );
	protected static final Group INPUT_DIALOG_IMPLEMENTATION_GROUP = Group.getInstance( java.util.UUID.fromString( "35b47d9d-d17b-4862-ac22-5ece4e317242" ), "INPUT_DIALOG_IMPLEMENTATION_GROUP" );
	private static abstract class ButtonOperation extends ActionOperation {
		private boolean isOk;
		private Dialog dialog;
		public ButtonOperation(java.util.UUID individualId, String name, boolean isOk) {
			super( INPUT_DIALOG_IMPLEMENTATION_GROUP, individualId );
			this.setName(name);
			this.isOk = isOk;
		}
		public void setDialog(Dialog dialog) {
			if( dialog != null ) {
				assert this.dialog == null;
			}
			this.dialog = dialog;
		}
		
		private InputDialogOperationContext< ? > getInputDialogOperationContext( ActionOperationContext context ) {
			System.err.println( "todo: getInputDialogOperationContext" );
			return (InputDialogOperationContext< ? >)context.getParent();
		}
		private InputDialogOperation< ? > getInputDialogOperation( ActionOperationContext context ) {
			return this.getInputDialogOperationContext( context ).getModel();
		}
		@Override
		protected final void perform(ActionOperationContext context) {
			assert this.dialog != null;
			InputDialogOperation< ? > inputDialogOperation = this.getInputDialogOperation( context );
			inputDialogOperation.isOk = this.isOk;
			this.dialog.setVisible( false );
			//this.dialog.getAwtWindow().dispose();
			context.finish();
		}
	}
	
	public static class CommitOperation extends ButtonOperation { 
		private static class SingletonHolder {
			private static CommitOperation instance = new CommitOperation();
		}
		public static CommitOperation getInstance() {
			return SingletonHolder.instance;
		}
		private CommitOperation() {
			super( java.util.UUID.fromString("f6019ff0-cf2b-4d6c-8c8d-14cac8154ebc"), "OK", true );
		}
	}
	public static class CancelOperation extends ButtonOperation { 
		private static class SingletonHolder {
			private static CancelOperation instance = new CancelOperation();
		}
		public static CancelOperation getInstance() {
			return SingletonHolder.instance;
		}
		private CancelOperation() {
			super( java.util.UUID.fromString("2a7e61c8-119a-45b1-830c-f59edda720a0"), "Cancel", false );
		}
	}
	private static final String NULL_EXPLANATION = "good to go";
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
	private boolean isCancelDesired;
	private boolean isOk = false;
	public InputDialogOperation(Group group, java.util.UUID individualId, boolean isCancelDesired) {
		super(group, individualId);
		this.isCancelDesired = isCancelDesired;
		this.explanationLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
		this.explanationLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 16, 0, 0 ) );
		this.explanationLabel.setForegroundColor( java.awt.Color.RED.darker().darker() );
	}
	public InputDialogOperation(Group group, java.util.UUID individualId) {
		this(group, individualId, true);
	}

	@Override
	protected InputDialogOperationContext<J> createContext( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return ContextManager.createAndPushInputDialogOperationContext( this, e, viewController );
	}

	
	public String getTutorialFinishNoteText( InputDialogOperationContext< ? > inputDialogOperationContext, UserInformation userInformation ) {
		return "When finished press the <strong>OK</strong> button.";
	}

	public static interface ExternalCommitButtonDisabler<J extends Component<?>> {
		public String getExplanationIfCommitButtonShouldBeDisabled( InputDialogOperationContext<J> context );
	}
	private ExternalCommitButtonDisabler<J> externalCommitButtonDisabler;
	public ExternalCommitButtonDisabler<J> getExternalCommitButtonDisabler() {
		return this.externalCommitButtonDisabler;
	}
	public void setExternalCommitButtonDisabler( ExternalCommitButtonDisabler<J> externalCommitButtonDisabler ) {
		this.externalCommitButtonDisabler = externalCommitButtonDisabler;
	}
	protected String getExplanationIfCommitButtonShouldBeDisabled( InputDialogOperationContext<J> context ) {
		return null;
	}
	protected abstract J prologue( InputDialogOperationContext<J> context );
	protected abstract void epilogue( InputDialogOperationContext<J> context, boolean isCommit );

	protected void updateOperationAndExplanation( HistoryNode<?> child ) {
		InputDialogOperationContext<J> context = (InputDialogOperationContext<J>)child.findContextFor( InputDialogOperation.this );
		String text;
		if( context != null ) {
			String explanation = this.getExplanationIfCommitButtonShouldBeDisabled( context );
			if( this.externalCommitButtonDisabler != null ) {
				String externalExplanation = this.externalCommitButtonDisabler.getExplanationIfCommitButtonShouldBeDisabled( context );
				if( externalExplanation != null ) {
					explanation = externalExplanation;
				}
			}
			if( explanation != null ) {
				text = explanation;
			} else {
				text = NULL_EXPLANATION;
			}
			CommitOperation.getInstance().setEnabled( text == NULL_EXPLANATION );
			this.explanationLabel.setText( text );
		} else {
			this.explanationLabel.setText( "todo: updateOperationAndExplanation context==null" );
			CommitOperation.getInstance().setEnabled( true );
		}
	}

	private ModelContext.ChildrenObserver childrenObserver = new ModelContext.ChildrenObserver() {
		public void addingChild(HistoryNode child) {
		}
		public void addedChild(HistoryNode child) {
			InputDialogOperation.this.updateOperationAndExplanation( child );
		}
	};

	public Edit< ? > createEdit( InputDialogOperationContext< J > inputDialogOperationContext ) {
		//todo
		return null;
	}
	
	@Override
	protected final Container<?> createContentPane(InputDialogOperationContext<J> context, Dialog dialog) {
		J mainPane = this.prologue(context);
		if( mainPane != null ) {
			assert mainPane != null;
			context.setMainPanel( mainPane );
			class OkCancelPanel extends Panel {
				private Button okButton = CommitOperation.getInstance().createButton();
				public OkCancelPanel() {
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
						this.internalAddComponent( okButton );
						if( isCancelDesired ) {
							//this.internalAddComponent( BoxUtilities.createHorizontalSliver( 2 ) );
							this.internalAddComponent( CancelOperation.getInstance().createButton() );
						}
					} else {
						this.internalAddComponent( BoxUtilities.createHorizontalGlue() );
						if( isCancelDesired ) {
							this.internalAddComponent( CancelOperation.getInstance().createButton() );
							//this.internalAddComponent( BoxUtilities.createHorizontalSliver( 2 ) );
						}
						this.internalAddComponent( okButton );
					}
					this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,0,4,0 ) );
				}
				@Override
				protected java.awt.LayoutManager createLayoutManager(javax.swing.JPanel jPanel) {
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
						return new java.awt.FlowLayout( java.awt.FlowLayout.TRAILING, 2, 0 );
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
			borderPanel.addComponent( southPanel, BorderPanel.Constraint.PAGE_END );
			
			dialog.setDefaultButton( okCancelPanel.getOkButton() );

			CommitOperation.getInstance().setDialog(dialog);
			CancelOperation.getInstance().setDialog(dialog);
			this.isOk = false;

			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate.  observer should not need to be added to the root" );
			ContextManager.getRootContext().addChildrenObserver( this.childrenObserver );
			this.updateOperationAndExplanation( context );

			return borderPanel;
		} else {
			this.isOk = false;
			return null;
		}
	}
	@Override
	protected final void releaseContentPane(InputDialogOperationContext<J> context, Dialog dialog, Container<?> contentPane) {
		if( contentPane != null ) {
			this.epilogue(context, this.isOk);
			
			ContextManager.getRootContext().removeChildrenObserver( this.childrenObserver );

			CommitOperation.getInstance().setDialog(null);
			CancelOperation.getInstance().setDialog(null);
			if( this.isOk ) {
				//pass
			} else {
				assert context.isCanceled();
			}
		} else {
			context.cancel();
		}
	}

}
