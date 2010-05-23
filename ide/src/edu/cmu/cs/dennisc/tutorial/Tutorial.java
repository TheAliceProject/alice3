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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
public class Tutorial {
	/*package-private*/ static java.util.UUID TUTORIAL_GROUP = java.util.UUID.fromString( "7bfa86e3-234e-4bd1-9177-d4acac0b12d9" );
	
	private static java.awt.Color CONTROL_COLOR = new java.awt.Color(255, 255, 191);

	private abstract static class TutorialOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
		public TutorialOperation(java.util.UUID individualId, String name) {
			super(Tutorial.TUTORIAL_GROUP, individualId);
			this.setName(name);
		}

		@Override
		public edu.cmu.cs.dennisc.croquet.Button createButton() {
			edu.cmu.cs.dennisc.croquet.Button rv = super.createButton();
			rv.setBackgroundColor(CONTROL_COLOR);
			return rv;
		}
	}

	private static class PreviousStepOperation extends TutorialOperation {
		private StepsComboBoxModel stepsComboBoxModel;

		public PreviousStepOperation(StepsComboBoxModel stepsComboBoxModel) {
			super(java.util.UUID.fromString("dbb7a622-95b4-48b9-ad86-db9350503aee"), "\u2190 Previous");
			this.stepsComboBoxModel = stepsComboBoxModel;
			// this.setName( "\u21E6 Previous" );
		}

		@Override
		protected void perform(edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component<?> component) {
			this.stepsComboBoxModel.decrementSelectedIndex();
			context.finish();
		}
	}

	private static class NextStepOperation extends TutorialOperation {
		private StepsComboBoxModel stepsComboBoxModel;

		public NextStepOperation(StepsComboBoxModel stepsComboBoxModel) {
			super(java.util.UUID.fromString("114060ef-1231-433b-9084-48faa024d1ba"), "Next \u2192");
			this.stepsComboBoxModel = stepsComboBoxModel;
			// this.setName( "Next \u21E8" );
		}

		@Override
		protected void perform(edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component<?> component) {
			this.stepsComboBoxModel.incrementSelectedIndex();
			context.finish();
		}
	}

	private static class ExitOperation extends TutorialOperation {
		public ExitOperation() {
			super(java.util.UUID.fromString("5393bf32-899a-49a5-b6c9-1e6ebc1daddf"), "Exit Tutorial");
		}

		@Override
		protected void perform(edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component<?> component) {
			Object optionExit = "Exit Tutorial";
			Object optionDisable = "Disable Stencil";
			Object optionCancel = "Cancel";
			Object value = edu.cmu.cs.dennisc.croquet.Application.getSingleton().showOptionDialog("Would you like to exit the tutorial or temporarily disable the stencil to work around a problem?", "Exit Tutorial or Disable Stencil",
					edu.cmu.cs.dennisc.croquet.MessageType.QUESTION, null, optionExit, optionDisable, optionCancel, -1);
			if (value == optionExit) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: exit");
			} else if (value == optionDisable) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: disable");
			}
			context.finish();
		}
	}

	private StepsComboBoxModel stepsComboBoxModel = new StepsComboBoxModel();
	private PreviousStepOperation previousStepOperation = new PreviousStepOperation( this.stepsComboBoxModel );
	private NextStepOperation nextStepOperation = new NextStepOperation( this.stepsComboBoxModel );
	private ExitOperation exitOperation = new ExitOperation();

	class StepsComboBox extends edu.cmu.cs.dennisc.croquet.JComponent<javax.swing.JComboBox> {
		@Override
		protected javax.swing.JComboBox createAwtComponent() {
			javax.swing.JComboBox rv = new javax.swing.JComboBox(stepsComboBoxModel);
			rv.setRenderer(new StepCellRenderer( Tutorial.this.stepsComboBoxModel ));
			rv.setBackground(CONTROL_COLOR);
			return rv;
		}
	};


	private class TutorialStencil extends Stencil {
		private edu.cmu.cs.dennisc.croquet.CardPanel cardPanel = new edu.cmu.cs.dennisc.croquet.CardPanel();
		private StepsComboBox comboBox = new StepsComboBox();
		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
					TutorialStencil.this.handleStepChanged((Step) e.getItem());
				} else {
					// pass
				}
			}
		};
		public TutorialStencil() {
			edu.cmu.cs.dennisc.croquet.FlowPanel controlPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.CENTER, 2, 0);
			controlPanel.addComponent(Tutorial.this.previousStepOperation.createButton());
			controlPanel.addComponent(comboBox);
			controlPanel.addComponent(Tutorial.this.nextStepOperation.createButton());

			edu.cmu.cs.dennisc.croquet.BorderPanel panel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
			panel.addComponent(controlPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER);
			panel.addComponent(Tutorial.this.exitOperation.createButton(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST);
			panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 0, 4));

			this.internalAddComponent(panel, java.awt.BorderLayout.NORTH);
			this.internalAddComponent(this.cardPanel, java.awt.BorderLayout.CENTER);
		}
		private void handleStepChanged(Step step) {
			String cardLayoutKey = step.getCardLayoutKey();
			edu.cmu.cs.dennisc.croquet.CardPanel.Key key = this.cardPanel.getKey(cardLayoutKey);
			if (key != null) {
				// pass
			} else {
				key = this.cardPanel.createKey(step.getCard(), cardLayoutKey);
				this.cardPanel.addComponent(key);
			}
			this.cardPanel.show(key);
			this.revalidateAndRepaint();

			int selectedIndex = stepsComboBoxModel.getSelectedIndex();
			Tutorial.this.nextStepOperation.setEnabled(0 <= selectedIndex && selectedIndex < stepsComboBoxModel.getSize() - 1);
			Tutorial.this.previousStepOperation.setEnabled(1 <= selectedIndex);
			this.revalidateAndRepaint();
		}
		@Override
		protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			super.handleAddedTo(parent);
			this.comboBox.getAwtComponent().addItemListener(this.itemListener);
			this.handleStepChanged((Step) stepsComboBoxModel.getSelectedItem());
		}

		@Override
		protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			this.comboBox.getAwtComponent().addItemListener(this.itemListener);
			super.handleRemovedFrom(parent);
		}
		
		@Override
		protected Step getCurrentStep() {
			return (Step)stepsComboBoxModel.getSelectedItem();
		}
	}
	private final TutorialStencil stencil = new TutorialStencil();
	
	private void addStep( Step step ) {
		this.stepsComboBoxModel.addStep( step );
	}
	public void setSelectedIndex( int index ) {
		this.stepsComboBoxModel.setSelectedIndex( index );
	}
	public void createAndAddMessageStep( String title, String text ) {
		Step step = NoteStep.createMessageNoteStep( this, title, text );
		this.addStep( step );
	}
	public void createAndAddSpotlightStep( String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > componentToSpotlight ) {
		Step step = NoteStep.createSpotlightMessageNoteStep( this, title, text, componentToSpotlight );
		this.addStep( step );
	}
	public void createAndAddActionStep( String title, String text, edu.cmu.cs.dennisc.croquet.AbstractActionOperation operation ) {
		edu.cmu.cs.dennisc.croquet.Component< ? > component = operation.getFirstComponent();
		Step step = NoteStep.createActionMessageNoteStep( this, title, text, component );
		this.addStep( step );
	}
	
	/*package-private*/ edu.cmu.cs.dennisc.croquet.ActionOperation getNextOperation() {
		return this.nextStepOperation;
	}
	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			class StencilRepaintManager extends javax.swing.RepaintManager {
				@Override
				public void addDirtyRegion(javax.swing.JComponent c, int x, int y, int w, int h) {
					super.addDirtyRegion(c, x, y, w, h);
					final javax.swing.JComponent jStencil = stencil.getAwtComponent();
					if( jStencil == c || jStencil.isAncestorOf( c ) ) {
						//pass
					} else {
						final java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle( c, new java.awt.Rectangle(x,y,w,h), jStencil );
//						javax.swing.SwingUtilities.invokeLater(new Runnable() {
//							public void run() {
								jStencil.repaint( rect.x, rect.y, rect.width, rect.height );
								//StencilRepaintManager.super.addDirtyRegion( jStencil, rect.x, rect.y, rect.width, rect.height);
//							}
//						} );
					}
				}
			};
			javax.swing.RepaintManager repaintManager = new StencilRepaintManager();
			javax.swing.RepaintManager.setCurrentManager( repaintManager );
			
			java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener() {
				public void componentResized( java.awt.event.ComponentEvent e ) {
					stencil.getAwtComponent().setBounds( e.getComponent().getBounds() );
					stencil.revalidateAndRepaint();
				}
				public void componentMoved( java.awt.event.ComponentEvent e ) {
				}
				public void componentShown( java.awt.event.ComponentEvent e ) {
				}
				public void componentHidden( java.awt.event.ComponentEvent e ) {
				}
			};
			
			javax.swing.JFrame frame = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getAwtWindow();
			javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();

			stencil.getAwtComponent().setBounds( layeredPane.getBounds() );
			layeredPane.addComponentListener( componentListener );
			
			layeredPane.add( stencil.getAwtComponent(), null );
			layeredPane.setLayer( stencil.getAwtComponent(), javax.swing.JLayeredPane.POPUP_LAYER - 1 );

			final int PAD = 4;
			frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder(PAD+32,PAD,0,PAD));
			((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder(0,PAD,PAD,PAD));
			
			stencil.getAwtComponent().repaint();
		}
	}
}
