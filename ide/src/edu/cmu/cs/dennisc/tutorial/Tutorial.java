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

import edu.cmu.cs.dennisc.croquet.PaintUtilities;


/**
 * @author Dennis Cosgrove
 */
public class Tutorial {
	private static java.util.UUID TUTORIAL_GROUP = java.util.UUID.fromString( "7bfa86e3-234e-4bd1-9177-d4acac0b12d9" );
	private class PreviousStepOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
		public PreviousStepOperation() {
			super( TUTORIAL_GROUP, java.util.UUID.fromString( "dbb7a622-95b4-48b9-ad86-db9350503aee" ) );
			this.setName( "Previous" );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
			stepsComboBoxModel.decrementSelectedIndex();
		}
	}
	private class NextStepOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
		public NextStepOperation() {
			super( TUTORIAL_GROUP, java.util.UUID.fromString( "114060ef-1231-433b-9084-48faa024d1ba" ) );
			this.setName( "Next" );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
			stepsComboBoxModel.incrementSelectedIndex();
		}
	}
	
	private class StepCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< Step > {
		@Override
		protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.tutorial.Step value, int index, boolean isSelected, boolean cellHasFocus ) {
			assert list != null;
			if( value != null ) {
				StringBuilder sb = new StringBuilder();
				int i;
				if( index >= 0 ) {
					i = index;
				} else {
					i = stepsComboBoxModel.selectedIndex;
//					if( i >= 0 ) {
//						assert value == stepsComboBoxModel.getElementAt( i ) : stepsComboBoxModel.getElementAt( i );
//					}
				}
				sb.append( "Step " );
				sb.append( i+1 );
				sb.append( ": " );
				sb.append( value );
				rv.setText( sb.toString() );
			}
			return rv;
		}
	}
	private static java.awt.Paint stencilPaint = null;
	private static java.awt.Paint getStencilPaint() {
		if( Tutorial.stencilPaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setColor( new java.awt.Color( 127, 127, 255, 127 ) );
			g2.fillRect( 0, 0, width, height );
			g2.setColor( new java.awt.Color( 63, 63, 127, 31 ) );
			g2.drawLine( 0, height, width, 0 );
			g2.drawLine( 0, 0, 0, 0 );
			g2.dispose();
			Tutorial.stencilPaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return Tutorial.stencilPaint;
	}
	
	private class Stencil extends edu.cmu.cs.dennisc.croquet.BorderPanel {
		private PreviousStepOperation previousStepOperation = new PreviousStepOperation();
		private NextStepOperation nextStepOperation = new NextStepOperation();
		private edu.cmu.cs.dennisc.croquet.CardPanel cardPanel = new edu.cmu.cs.dennisc.croquet.CardPanel();
		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
					Stencil.this.handleStepChanged( (Step)e.getItem() );
				} else {
					//pass
				}
			}
		};
		private javax.swing.JComboBox comboBox = new javax.swing.JComboBox( stepsComboBoxModel );
		public Stencil() {
			this.comboBox.setRenderer( new StepCellRenderer() );
			edu.cmu.cs.dennisc.croquet.FlowPanel northPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel();
			northPanel.addComponent( this.previousStepOperation.createButton() );
			northPanel.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.comboBox ) );
			northPanel.addComponent( this.nextStepOperation.createButton() );
			this.addComponent( northPanel, Constraint.NORTH );
			this.addComponent( this.cardPanel, Constraint.CENTER );
		}
		
		@Override
		protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			super.handleAddedTo( parent );
			this.comboBox.addItemListener( this.itemListener );
			this.handleStepChanged( (Step)stepsComboBoxModel.getSelectedItem() );
		}
		@Override
		protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			this.comboBox.addItemListener( this.itemListener );
			super.handleRemovedFrom( parent );
		}
		private void handleStepChanged( Step step ) {
			String cardLayoutKey = step.getCardLayoutKey();
			edu.cmu.cs.dennisc.croquet.CardPanel.Key key = this.cardPanel.getKey( cardLayoutKey );
			if( key != null ) {
				//pass
			} else {
				key = this.cardPanel.createKey( step.getComponent(), cardLayoutKey );
				this.cardPanel.addComponent( key );
			}
			this.cardPanel.show( key );
			this.revalidateAndRepaint();
			
			int selectedIndex = stepsComboBoxModel.getSelectedIndex();
			this.nextStepOperation.setEnabled( 0 <= selectedIndex && selectedIndex < stepsComboBoxModel.getSize()-1 );
			this.previousStepOperation.setEnabled( 1 <= selectedIndex );
		}
		@Override
		protected boolean paintComponent( java.awt.Graphics2D g2 ) {
			Step step = (Step)stepsComboBoxModel.getSelectedItem();
			
			java.awt.Shape clip = g2.getClip();
			java.awt.geom.Area area = new java.awt.geom.Area( clip );
			if( step != null ) {
				step.subtractHoleIfAppropriate( area, this );
			}
			//g2.setPaint( new java.awt.Color( 127, 127, 255, 127 ) );
			g2.setPaint( getStencilPaint() );
			g2.fill( area );
			return super.paintComponent( g2 );
		}
	}
	private edu.cmu.cs.dennisc.croquet.Application application;
	private class StepsComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
		private int selectedIndex = -1;
		private java.util.List< Step > steps = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
		public Object getElementAt( int index ) {
			return this.steps.get( index );
		}
		public int getSize() {
			return this.steps.size();
		}
		public Object getSelectedItem() {
			if( this.selectedIndex >= 0 ) {
				return this.getElementAt( this.selectedIndex );
			} else {
				return null;
			}
		}
		
		private int getSelectedIndex() {
			return this.selectedIndex;
		}
		private void setSelectedIndex( int selectedIndex ) {
			this.selectedIndex = selectedIndex;
			this.fireContentsChanged( this, -1, -1 );
		}
		
		private void decrementSelectedIndex() {
			this.setSelectedIndex( this.selectedIndex - 1 );
		}
		private void incrementSelectedIndex() {
			this.setSelectedIndex( this.selectedIndex + 1 );
		}
		
		public void setSelectedItem( Object item ) {
			this.selectedIndex = -1;
			final int N = this.steps.size();
			for( int i=0; i<N; i++ ) {
				if( this.steps.get( i ) == item ) {
					this.selectedIndex = i;
					break;
				}
			}
		}
		private void addStep( Step step ) {
			this.steps.add( step );
		}
	}
	private StepsComboBoxModel stepsComboBoxModel = new StepsComboBoxModel();
	public Tutorial( edu.cmu.cs.dennisc.croquet.Application application ) {
		this.application = application;
	}
	public void createAndAddMessageStep( String title, String text ) {
		Step step = NoteStep.createMessageNoteStep( this, title, text );
		this.stepsComboBoxModel.addStep( step );
	}
	public void createAndAddSpotlightStep( String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > componentToSpotlight ) {
		Step step = NoteStep.createSpotlightMessageNoteStep( this, title, text, componentToSpotlight );
		this.stepsComboBoxModel.addStep( step );
	}
	public void createAndAddActionStep( String title, String text, edu.cmu.cs.dennisc.croquet.AbstractActionOperation operation ) {
		edu.cmu.cs.dennisc.croquet.Component< ? > component = operation.getFirstComponent();
		Step step = NoteStep.createActionMessageNoteStep( this, title, text, component );
		this.stepsComboBoxModel.addStep( step );
	}
	
	public void setSelectedIndex( int index ) {
		this.stepsComboBoxModel.setSelectedIndex( index );
	}
	
	private final Stencil stencil = new Stencil();
	/*package-private*/ edu.cmu.cs.dennisc.croquet.ActionOperation getNextOperation() {
		return stencil.nextStepOperation;
	}
	public void setVisible( boolean isVisible ) {

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
		javax.swing.JLayeredPane layeredPane = this.application.getFrame().getAwtWindow().getLayeredPane();

		stencil.getAwtComponent().setBounds( layeredPane.getBounds() );
		layeredPane.addComponentListener( componentListener );
		
		
		layeredPane.add( stencil.getAwtComponent(), null );
		layeredPane.setLayer( stencil.getAwtComponent(), javax.swing.JLayeredPane.POPUP_LAYER - 1 );
		
		stencil.getAwtComponent().repaint();
		
	}
}
