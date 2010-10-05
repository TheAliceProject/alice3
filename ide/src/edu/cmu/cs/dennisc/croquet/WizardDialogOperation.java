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

	protected abstract class DialogOperation extends ActionOperation {
		private Dialog dialog;
		public DialogOperation( java.util.UUID id ) {
			super( Application.INHERIT_GROUP, id );
		}
		public Dialog getDialog() {
			return this.dialog;
		}
		public void setDialog( Dialog dialog ) {
			this.dialog = dialog;
		}
	}

	protected class CompleteOperation extends DialogOperation {
		public CompleteOperation() {
			super( java.util.UUID.fromString( "6acdd95e-849c-4281-9779-994e9807b25b" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "Finish" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
			isCompleted = true;
			context.finish();
			this.getDialog().setVisible( false );
		}
	}
	protected class CancelOperation extends DialogOperation {
		public CancelOperation() {
			super( java.util.UUID.fromString( "3363c6f0-c8a2-48f2-aefc-c53894ec8a99" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "Cancel" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
			context.cancel();
			this.getDialog().setVisible( false );
		}
	}
	private CompleteOperation completeOperation = new CompleteOperation();
	private CancelOperation cancelOperation = new CancelOperation();

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
			DialogOperationWithControls.this.handleCroquetAddedChild( child );
		}
	};
	
	public DialogOperationWithControls( Group group, java.util.UUID id ) {
		super( group, id );
		this.explanationLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
		//this.explanationLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 16, 0, 0 ) );
		this.explanationLabel.setForegroundColor( java.awt.Color.RED.darker().darker() );
	}

	private boolean isCompleted;
	
	protected CompleteOperation getCompleteOperation() {
		return this.completeOperation;
	}
	protected CancelOperation getCancelOperation() {
		return this.cancelOperation;
	}
	
	protected abstract Component<?> createMainPanel( C context, Dialog dialog, Label explanationLabel );
	protected abstract Component<?> createControlsPanel( C context, Dialog dialog );
	protected abstract void release( C context, Dialog dialog, boolean isCompleted );
	
	protected abstract String getExplanation();
	protected void updateExplanation( C context ) {
		String explanation = this.getExplanation();
		if( explanation != null ) {
			//pass
		} else {
			explanation = NULL_EXPLANATION;
		}
		this.explanationLabel.setText( explanation );
	}
	
	protected void handleCroquetAddedChild(HistoryNode child) {
		this.updateExplanation( (C)child.findContextFor( DialogOperationWithControls.this ) );
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
		
		ContextManager.getRootContext().addChildrenObserver( this.childrenObserver );
		this.updateExplanation( context );
		
		this.completeOperation.setDialog( dialog );
		this.cancelOperation.setDialog( dialog );
		this.isCompleted = false;
		return rv;
	}
	@Override
	protected final void releaseContentPane(C context, Dialog dialog, Container<?> contentPane) {
		if( contentPane != null ) {
			ContextManager.getRootContext().removeChildrenObserver( this.childrenObserver );
			this.release( context, dialog, this.isCompleted );
		} else {
			context.cancel();
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class WizardDialogOperation extends DialogOperationWithControls<WizardDialogOperationContext> {
	protected static final Group ENCLOSING_WIZARD_DIALOG_GROUP = Group.getInstance( java.util.UUID.fromString( "100a8027-cf11-4070-abd5-450f8c5ab1cc" ), "ENCLOSING_WIZARD_DIALOG_GROUP" );
	private class NextOperation extends ActionOperation {
		public NextOperation() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "e1239539-1eb0-411d-b808-947d0b1c1e94" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "Next >" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
			int index = cardSelectionState.getSelectedIndex();
			final int N = cardSelectionState.getItemCount();
			if( index < N-1 ) {
				cardSelectionState.setSelectedIndex( index+1 );
			}
		}
	}
	private class PreviousOperation extends ActionOperation {
		public PreviousOperation() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "2b1ff0fd-8d8a-4d23-9d95-6203e9abff9c" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "< Back" );
		}
		@Override
		protected void perform( ActionOperationContext context ) {
			int index = cardSelectionState.getSelectedIndex();
			if( index > 0 ) {
				cardSelectionState.setSelectedIndex( index-1 );
			}
		}
	}

	private PreviousOperation prevOperation = new PreviousOperation();
	private NextOperation nextOperation = new NextOperation();
	
	private Label title = new Label( "Name/Description", edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
	private CardPanel cardPanel = new CardPanel();
	
	private class Card {
		private WizardStep step;
		private CardPanel.Key key;
		public Card( WizardStep step ) {
			this.step = step;
			this.key = cardPanel.createKey( this.step.getComponent(), java.util.UUID.randomUUID() );
			cardPanel.addComponent( key );
		}
		@Override
		public String toString() {
			return this.step.getTitle();
		}
	};
	
	private final ListSelectionState< Card > cardSelectionState = new ListSelectionState< Card >( Application.INFORMATION_GROUP, java.util.UUID.fromString( "2382103d-a67e-4a35-baa2-9a612fd2d8f2" ), new Codec< Card >() {
		public StringBuilder appendRepresentation( StringBuilder rv, Card value, java.util.Locale locale ) {
			rv.append( value );
			return rv;
		}
		public Card decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			throw new RuntimeException( "todo" );
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Card card ) {
			throw new RuntimeException( "todo" );
		}
	} );
	
	private class StepsProgressPanel extends PageAxisPanel {
		public StepsProgressPanel() {
			class ListCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< Card > {
				private final java.awt.Font selectedFont;
				private final java.awt.Font unselectedFont;
				public ListCellRenderer() {
					this.setOpaque( false );
					java.awt.Font font = this.getFont();
					this.selectedFont = edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont( font, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
					this.unselectedFont = edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont( font, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
				}
				@Override
				protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, Card value, int index, boolean isSelected, boolean cellHasFocus ) {
					this.setBorder( null );
					if( isSelected ) {
						rv.setForeground( java.awt.Color.BLACK );
						rv.setFont( this.selectedFont );
					} else {
						rv.setForeground( java.awt.Color.GRAY );
						rv.setFont( this.unselectedFont );
					}
					rv.setText( (index+1) + ".    " + value.step.getTitle() );
					return rv;
				}
			}
			
			//List< Card > list = cardSelectionState.createList();
			List< Card > list = new List< Card >( cardSelectionState ) {
				@Override
				protected javax.swing.JList createAwtComponent() {
					return new javax.swing.JList() {
						@Override
						public boolean contains( int x, int y ) {
							return false;
						}
					};
				}
			};
			
			list.setCellRenderer( new ListCellRenderer() );
			list.setAlignmentX( 0.0f );
			
			Label label = new Label( "Steps", edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
			this.addComponent( label );
			this.addComponent( new HorizontalSeparator() );
			this.addComponent( BoxUtilities.createVerticalSliver( 8 ) );
			this.addComponent( list );
			this.addComponent( BoxUtilities.createVerticalGlue() );
			this.getAwtComponent().setOpaque( true );
			this.setBackgroundColor( java.awt.Color.WHITE );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
			this.setMinimumPreferredWidth( 160 );
		}
	}
	
	private ListSelectionState.ValueObserver< Card > selectionObserver = new ListSelectionState.ValueObserver< Card >() {
		public void changed( WizardDialogOperation.Card nextValue ) {
			WizardDialogOperation.this.handleCardChange( nextValue );
		}
	};
	public WizardDialogOperation(Group group, java.util.UUID individualId, boolean isCancelDesired) {
		super(group, individualId);
	}
	public WizardDialogOperation(Group group, java.util.UUID individualId) {
		this(group, individualId, true);
	}

	@Override
	protected String getExplanation() {
		Card card = this.cardSelectionState.getSelectedItem();
		if( card != null ) {
			return card.step.getExplanationIfProcedeButtonShouldBeDisabled();
		} else {
			//todo?
			return null;
		}
	}
	@Override
	protected void handleCroquetAddedChild( HistoryNode child ) {
		super.handleCroquetAddedChild( child );
		this.handleCardChange( this.cardSelectionState.getSelectedItem() );
	}
	private void handleCardChange( WizardDialogOperation.Card nextValue ) {
		if( nextValue != null ) {
			this.cardPanel.show( nextValue.key );
			this.title.setText( nextValue.step.getTitle() );
		} else {
			this.cardPanel.show( null );
			this.title.setText( "" );
		}
		int index = this.cardSelectionState.getSelectedIndex();
		this.prevOperation.setEnabled( index > 0 );
		WizardStep step = nextValue.step;
		String explanation = step.getExplanationIfProcedeButtonShouldBeDisabled();
		this.nextOperation.setEnabled( explanation==null && index < this.cardSelectionState.getItemCount()-1 );
		this.getCompleteOperation().setEnabled( explanation==null && step.isFinishPotentiallyEnabled() );
		
		//todo
		this.updateExplanation( null );
	}
	@Override
	public WizardDialogOperationContext createContext( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return ContextManager.createAndPushWizardDialogOperationContext( this, e, viewController );
	}

	protected abstract WizardStep[] createSteps( WizardDialogOperationContext context );
	
	@Override
	protected Component< ? > createControlsPanel( WizardDialogOperationContext context, Dialog dialog ) {
		LineAxisPanel rv = new LineAxisPanel();
		rv.addComponent( BoxUtilities.createHorizontalGlue() );
		rv.addComponent( this.prevOperation.createButton() );
		rv.addComponent( this.nextOperation.createButton() );
		rv.addComponent( BoxUtilities.createHorizontalSliver( 8 ) );
		rv.addComponent( this.getCompleteOperation().createButton() );
		rv.addComponent( BoxUtilities.createHorizontalSliver( 8 ) );
		rv.addComponent( this.getCancelOperation().createButton() );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );		
		return rv;
	}
	@Override
	protected Component< ? > createMainPanel( WizardDialogOperationContext context, Dialog dialog, Label explanationLabel ) {
		WizardStep[] steps = this.createSteps( context );

		java.util.ArrayList< Card > cards = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		cards.ensureCapacity( steps.length );
		for( WizardStep step : steps ) {
			Card card = new Card( step );
			cards.add( card );
		}
		this.cardSelectionState.setListData( 0, cards );
		this.cardSelectionState.addAndInvokeValueObserver( this.selectionObserver );
		
		BorderPanel rv = new BorderPanel();
		GridBagPanel centerPanel = new GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		centerPanel.addComponent( this.title, gbc );
		centerPanel.addComponent( new HorizontalSeparator(), gbc );
		gbc.weighty = 1.0;
		centerPanel.addComponent( this.cardPanel, gbc );
		gbc.weighty = 0.0;
		centerPanel.addComponent( explanationLabel, gbc );
		
		centerPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
		rv.addComponent( new StepsProgressPanel(), BorderPanel.Constraint.LINE_START );
		rv.addComponent( centerPanel, BorderPanel.Constraint.CENTER );
		return rv;
	}
	
//	public static void main( String[] args ) {
//		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
//		if( lookAndFeelInfo != null ) {
//			try {
//				edu.cmu.cs.dennisc.javax.swing.plaf.nimbus.NimbusUtilities.installModifiedNimbus( lookAndFeelInfo );
//			} catch( Throwable t ) {
//				t.printStackTrace();
//			}
//		}
//		org.alice.stageide.StageIDE stageIDE = new org.alice.stageide.StageIDE();
//		WizardDialogOperation wizardDialogOperation = new WizardDialogOperation( null, null ) {
//			private StringState name = new StringState( Application.INHERIT_GROUP, java.util.UUID.fromString( "63245276-7fba-4905-8ac1-34629ed258e5" ), "" );
//			private StringState description = new StringState( Application.INHERIT_GROUP, java.util.UUID.fromString( "18c58e94-7155-45c3-b158-82d299a0f5c9" ), "" );
//			@Override
//			protected java.awt.Dimension getDesiredDialogSize( Dialog dialog ) {
//				return new java.awt.Dimension( 640, 480 );
//			}
//			@Override
//			/*package-private*/ void localize() {
//				super.localize();
//				this.setName( "Action Script" );
//			}
//			@Override
//			protected WizardStep[] createSteps( WizardDialogOperationContext context ) {
//				class ReviewPanel extends PageAxisPanel implements WizardStep {
//					public ReviewPanel() {
//						this.addComponent( new Label( "please review your animation" ) );
//					}
//					public String getTitle() {
//						return "Review";
//					}
//					public Component< ? > getComponent() {
//						return this;
//					}
//					public String getExplanationIfProcedeButtonShouldBeDisabled() {
//						return null;
//					}
//					public boolean isFinishPotentiallyEnabled() {
//						return false;
//					}
//				};
//				class NamePanel extends RowsSpringPanel implements WizardStep {
//					public String getTitle() {
//						return "Name/Description";
//					}
//					@Override
//					protected java.util.List< Component< ? >[] > updateComponentRows( java.util.List< Component< ? >[] > rv ) {
//						rv.add( SpringUtilities.createLabeledRow( "name:", name.createTextField() ) );
//						rv.add( SpringUtilities.createTopLabeledRow( "description:", description.createTextArea() ) );
//						return rv;
//					}
//					public Component< ? > getComponent() {
//						return this;
//					}
//					public String getExplanationIfProcedeButtonShouldBeDisabled() {
//						boolean isNameAcceptable = name.getValue().length() > 0;
//						boolean isDescriptionAcceptable = description.getValue().length() > 0;
//						if( isNameAcceptable ) {
//							if( isDescriptionAcceptable ) {
//								return null;
//							} else {
//								return "enter description";
//							}
//						} else {
//							if( isDescriptionAcceptable ) {
//								return "enter name";
//							} else {
//								return "enter name and description";
//							}
//						}
//					}
//					public boolean isFinishPotentiallyEnabled() {
//						return true;
//					}
//				};
//				class CharactersPanel extends BorderPanel implements WizardStep {
//					public CharactersPanel() {
//						this.addComponent( new Label( "todo" ), Constraint.CENTER );
//					}
//					public String getTitle() {
//						return "Characters";
//					}
//					public Component< ? > getComponent() {
//						return this;
//					}
//					public String getExplanationIfProcedeButtonShouldBeDisabled() {
//						return null;
//					}
//					public boolean isFinishPotentiallyEnabled() {
//						return true;
//					}
//				};
//				return new WizardStep[] {
//					new ReviewPanel(),
//					new NamePanel(), 
//					new CharactersPanel()
//				};
//			}
//			@Override
//			protected void release( edu.cmu.cs.dennisc.croquet.WizardDialogOperationContext context, edu.cmu.cs.dennisc.croquet.Dialog dialog, boolean isCompleted ) {
//				if( isCompleted ) {
//					context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
//						@Override
//						protected void doOrRedoInternal( boolean isDo ) {
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "do", name.getValue() );
//						}
//						@Override
//						protected void undoInternal() {
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "undo", name.getValue() );
//						}
//						@Override
//						protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
//							rv.append( "todo" );
//							return rv;
//						}
//					} );
//				} else {
//					context.cancel();
//				}
//			}
//		};
//		wizardDialogOperation.fire();
//		System.exit( 0 );
//	}
}
