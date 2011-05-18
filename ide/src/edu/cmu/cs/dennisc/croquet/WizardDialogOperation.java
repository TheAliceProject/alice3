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

import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.CardPanel;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.components.GridBagPanel;
import org.lgna.croquet.components.HorizontalSeparator;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.List;
import org.lgna.croquet.components.PageAxisPanel;
import org.lgna.croquet.components.RowsSpringPanel;
import org.lgna.croquet.components.SpringUtilities;
import org.lgna.croquet.edits.OperationEdit;

/**
 * @author Dennis Cosgrove
 */
public abstract class WizardDialogOperation extends GatedCommitDialogOperation<org.lgna.croquet.steps.WizardDialogOperationStep> {
	protected static class FinishOperation extends CompleteOperation {
		private static class SingletonHolder {
			private static FinishOperation instance = new FinishOperation();
		}
		public static FinishOperation getInstance() {
			return SingletonHolder.instance;
		}
		private FinishOperation() {
			super( java.util.UUID.fromString( "687d90a2-4bdd-4b85-83f8-11b8a3cb0f6a" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "Finish" );
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.GatedCommitDialogOperation.CompleteOperation getCompleteOperation() {
		return FinishOperation.getInstance();
	}

	private static abstract class WizardOperation extends ActionOperation {
		public WizardOperation( java.util.UUID id ) {
			super( DIALOG_IMPLEMENTATION_GROUP, id );
		}
		protected ListSelectionState< Card > getCardSelectionState(  org.lgna.croquet.steps.ActionOperationStep step ) {
			//todo: fix
			return ((WizardDialogOperation)step.getParent().getParent().getParent().getModel()).cardSelectionState;
		}
	}
	private static class NextOperation extends WizardOperation {
		private static class SingletonHolder {
			private static NextOperation instance = new NextOperation();
		}
		public static NextOperation getInstance() {
			return SingletonHolder.instance;
		}
		private NextOperation() {
			super( java.util.UUID.fromString( "e1239539-1eb0-411d-b808-947d0b1c1e94" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "Next >" );
		}
		@Override
		protected void perform( org.lgna.croquet.steps.ActionOperationStep step ) {
			ListSelectionState< Card > cardSelectionState = this.getCardSelectionState(step);
			int index = cardSelectionState.getSelectedIndex();
			final int N = cardSelectionState.getItemCount();
			if( index < N-1 ) {
				cardSelectionState.setSelectedIndex( index+1 );
			}
		}
	}
	private static class PreviousOperation extends WizardOperation {
		private static class SingletonHolder {
			private static PreviousOperation instance = new PreviousOperation();
		}
		public static PreviousOperation getInstance() {
			return SingletonHolder.instance;
		}
		private PreviousOperation() {
			super( java.util.UUID.fromString( "2b1ff0fd-8d8a-4d23-9d95-6203e9abff9c" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "< Back" );
		}
		@Override
		protected void perform( org.lgna.croquet.steps.ActionOperationStep step ) {
			ListSelectionState< Card > cardSelectionState = this.getCardSelectionState(step);
			int index = cardSelectionState.getSelectedIndex();
			if( index > 0 ) {
				cardSelectionState.setSelectedIndex( index-1 );
			}
		}
	}

	private Label title = new Label( "Name/Description", edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
	private CardPanel cardPanel = new CardPanel();
	
	private class Card {
		private WizardStage step;
		private CardPanel.Key key;
		public Card( WizardStage step ) {
			this.step = step;
			this.key = cardPanel.createKey( this.step.getComponent(), java.util.UUID.randomUUID() );
			cardPanel.addComponent( key );
		}
		@Override
		public String toString() {
			return this.step.getTitle();
		}
	};
	
	private static class CardSelectionState extends DefaultListSelectionState< Card > {
		public CardSelectionState() {
			super( DIALOG_IMPLEMENTATION_GROUP, java.util.UUID.fromString( "2382103d-a67e-4a35-baa2-9a612fd2d8f2" ), new Codec< Card >() {
				public Class<Card> getValueClass() {
					return Card.class;
				}
				public Card decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
					throw new RuntimeException( "todo" );
				}
				public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Card card ) {
					throw new RuntimeException( "todo" );
				}
				public StringBuilder appendRepresentation( StringBuilder rv, Card value, java.util.Locale locale ) {
					rv.append( value );
					return rv;
				}
			} );
		}
	}
	
	private final CardSelectionState cardSelectionState = new CardSelectionState();
	
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
	protected String getExplanation( org.lgna.croquet.steps.WizardDialogOperationStep step ) {
		Card card = this.cardSelectionState.getSelectedItem();
		if( card != null ) {
			return card.step.getExplanationIfProcedeButtonShouldBeDisabled();
		} else {
			//todo?
			return null;
		}
	}
	@Override
	public void handleFiredEvent( org.lgna.cheshire.events.Event event ) {
		super.handleFiredEvent( event );
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
		PreviousOperation.getInstance().setEnabled( index > 0 );
		WizardStage step = nextValue.step;
		String explanation = step.getExplanationIfProcedeButtonShouldBeDisabled();
		NextOperation.getInstance().setEnabled( explanation==null && index < this.cardSelectionState.getItemCount()-1 );
		this.getCompleteOperation().setEnabled( explanation==null && step.isFinishPotentiallyEnabled() );
		
		//todo
		this.updateExplanation( null );
	}
	@Override
	public org.lgna.croquet.steps.WizardDialogOperationStep createAndPushStep( org.lgna.croquet.Trigger trigger ) {
		return org.lgna.croquet.steps.TransactionManager.addWizardDialogOperationStep( this, trigger );
	}

	protected abstract WizardStage[] createSteps( org.lgna.croquet.steps.WizardDialogOperationStep step );
	
	@Override
	protected Component< ? > createControlsPanel( org.lgna.croquet.steps.WizardDialogOperationStep step, Dialog dialog ) {
		Button finishButton = this.getCompleteOperation().createButton();
		LineAxisPanel rv = new LineAxisPanel();
		rv.addComponent( BoxUtilities.createHorizontalGlue() );
		rv.addComponent( PreviousOperation.getInstance().createButton() );
		rv.addComponent( NextOperation.getInstance().createButton() );
		rv.addComponent( BoxUtilities.createHorizontalSliver( 8 ) );
		rv.addComponent( finishButton );
		rv.addComponent( BoxUtilities.createHorizontalSliver( 8 ) );
		rv.addComponent( this.getCancelOperation().createButton() );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );		
		dialog.setDefaultButton( finishButton );
		return rv;
	}
	@Override
	protected Component< ? > createMainPanel( org.lgna.croquet.steps.WizardDialogOperationStep step, Dialog dialog, Label explanationLabel ) {
		WizardStage[] stages = this.createSteps( step );

		java.util.ArrayList< Card > cards = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		cards.ensureCapacity( stages.length );
		for( WizardStage stage : stages ) {
			Card card = new Card( stage );
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
			private StringState name = new StringState( Application.INHERIT_GROUP, java.util.UUID.fromString( "63245276-7fba-4905-8ac1-34629ed258e5" ), "" );
			private StringState description = new StringState( Application.INHERIT_GROUP, java.util.UUID.fromString( "18c58e94-7155-45c3-b158-82d299a0f5c9" ), "" );
			@Override
			protected java.awt.Dimension getDesiredDialogSize( Dialog dialog ) {
				return new java.awt.Dimension( 640, 480 );
			}
			@Override
			protected void localize() {
				super.localize();
				this.setName( "Action Script" );
			}
			@Override
			protected WizardStage[] createSteps( org.lgna.croquet.steps.WizardDialogOperationStep step ) {
				class ReviewPanel extends PageAxisPanel implements WizardStage {
					public ReviewPanel() {
						this.addComponent( new Label( "please review your animation" ) );
					}
					public String getTitle() {
						return "Review";
					}
					public Component< ? > getComponent() {
						return this;
					}
					public String getExplanationIfProcedeButtonShouldBeDisabled() {
						return null;
					}
					public boolean isFinishPotentiallyEnabled() {
						return false;
					}
				};
				class NamePanel extends RowsSpringPanel implements WizardStage {
					public String getTitle() {
						return "Name/Description";
					}
					@Override
					protected java.util.List< Component< ? >[] > updateComponentRows( java.util.List< Component< ? >[] > rv ) {
						rv.add( SpringUtilities.createLabeledRow( "name:", name.createTextField() ) );
						rv.add( SpringUtilities.createTopLabeledRow( "description:", description.createTextArea() ) );
						return rv;
					}
					public Component< ? > getComponent() {
						return this;
					}
					public String getExplanationIfProcedeButtonShouldBeDisabled() {
						boolean isNameAcceptable = name.getValue().length() > 0;
						boolean isDescriptionAcceptable = description.getValue().length() > 0;
						if( isNameAcceptable ) {
							if( isDescriptionAcceptable ) {
								return null;
							} else {
								return "enter description";
							}
						} else {
							if( isDescriptionAcceptable ) {
								return "enter name";
							} else {
								return "enter name and description";
							}
						}
					}
					public boolean isFinishPotentiallyEnabled() {
						return true;
					}
				};
				class CharactersPanel extends BorderPanel implements WizardStage {
					public CharactersPanel() {
						this.addComponent( new Label( "todo" ), Constraint.CENTER );
					}
					public String getTitle() {
						return "Characters";
					}
					public Component< ? > getComponent() {
						return this;
					}
					public String getExplanationIfProcedeButtonShouldBeDisabled() {
						return null;
					}
					public boolean isFinishPotentiallyEnabled() {
						return true;
					}
				};
				return new WizardStage[] {
					new ReviewPanel(),
					new NamePanel(), 
					new CharactersPanel()
				};
			}
			@Override
			protected void release( org.lgna.croquet.steps.WizardDialogOperationStep step, org.lgna.croquet.components.Dialog dialog, boolean isCompleted ) {
				if( isCompleted ) {
					step.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
						@Override
						protected void doOrRedoInternal( boolean isDo ) {
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "do", name.getValue() );
						}
						@Override
						protected void undoInternal() {
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "undo", name.getValue() );
						}
						@Override
						protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
							rv.append( "todo" );
							return rv;
						}
					} );
				} else {
					step.cancel();
				}
			}
		};
		wizardDialogOperation.fire();
		System.exit( 0 );
	}
}
