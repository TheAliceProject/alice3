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
	private static java.util.UUID TUTORIAL_GROUP = java.util.UUID.fromString( "7bfa86e3-234e-4bd1-9177-d4acac0b12d9" );
	private static java.awt.Color CONTROL_COLOR = new java.awt.Color( 255, 255, 191 );
	
	private abstract class TutorialOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
		public TutorialOperation( java.util.UUID individualId, String name ) {
			super( TUTORIAL_GROUP, individualId );
			this.setName( name );
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.Button createButton() {
			edu.cmu.cs.dennisc.croquet.Button rv = super.createButton();
			rv.setBackgroundColor( CONTROL_COLOR );
			return rv;
		}
	}
	private class PreviousStepOperation extends TutorialOperation {
		public PreviousStepOperation() {
			super( java.util.UUID.fromString( "dbb7a622-95b4-48b9-ad86-db9350503aee" ), "\u2190 Previous" );
			//this.setName( "\u21E6 Previous" );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
			stepsComboBoxModel.decrementSelectedIndex();
		}
	}
	private class NextStepOperation extends TutorialOperation {
		public NextStepOperation() {
			super( java.util.UUID.fromString( "114060ef-1231-433b-9084-48faa024d1ba" ), "Next \u2192" );
			//this.setName( "Next \u21E8" );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
			stepsComboBoxModel.incrementSelectedIndex();
		}
	}

	private class ExitOperation extends TutorialOperation {
		public ExitOperation() {
			super( java.util.UUID.fromString( "5393bf32-899a-49a5-b6c9-1e6ebc1daddf" ), "Exit Tutorial" );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
			Object optionExit = "Exit Tutorial";
			Object optionDisable = "Disable Stencil";
			Object optionCancel = "Cancel";
			Object value = edu.cmu.cs.dennisc.croquet.Application.getSingleton().showOptionDialog( "Would you like to exit the tutorial or temporarily disable the stencil to work around a problem?", "Exit Tutorial or Disable Stencil", edu.cmu.cs.dennisc.croquet.MessageType.QUESTION, null, optionExit, optionDisable, optionCancel, -1 );
			if( value == optionExit ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: exit" );
			} else if( value == optionDisable ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: disable" );
			}
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
	/*package-private*/ static final java.awt.Color STENCIL_BASE_COLOR =  new java.awt.Color( 127, 127, 255, 127 );
	/*package-private*/ static final java.awt.Color STENCIL_LINE_COLOR =  new java.awt.Color( 63, 63, 127, 31 );
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
			g2.setColor( STENCIL_BASE_COLOR );
			g2.fillRect( 0, 0, width, height );
			g2.setColor( STENCIL_LINE_COLOR );
			g2.drawLine( 0, height, width, 0 );
			g2.drawLine( 0, 0, 0, 0 );
			g2.dispose();
			Tutorial.stencilPaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return Tutorial.stencilPaint;
	}

	
	private class Stencil extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JPanel > {
		private PreviousStepOperation previousStepOperation = new PreviousStepOperation();
		private NextStepOperation nextStepOperation = new NextStepOperation();
		private ExitOperation exitOperation = new ExitOperation();
		
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
		
		class StepsComboBox extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComboBox > {
			@Override
			protected javax.swing.JComboBox createAwtComponent() {
				javax.swing.JComboBox rv = new javax.swing.JComboBox( stepsComboBoxModel ); 
				rv.setRenderer( new StepCellRenderer() );
				rv.setBackground( CONTROL_COLOR );
				return rv;
			}
		};
		private StepsComboBox comboBox = new StepsComboBox();
		public Stencil() {
			edu.cmu.cs.dennisc.croquet.FlowPanel controlPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel( edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.CENTER, 2, 0 );
			controlPanel.addComponent( this.previousStepOperation.createButton() );
			controlPanel.addComponent( comboBox );
			controlPanel.addComponent( this.nextStepOperation.createButton() );

			edu.cmu.cs.dennisc.croquet.BorderPanel panel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
			panel.addComponent( controlPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
			panel.addComponent( this.exitOperation.createButton(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );
			panel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,4,4,4 ) );

			this.internalAddComponent( panel, java.awt.BorderLayout.SOUTH );
			this.internalAddComponent( this.cardPanel, java.awt.BorderLayout.CENTER );
		}
		
		@Override
		protected javax.swing.JPanel createAwtComponent() {
			class JStencil extends javax.swing.JPanel {
				@Override
				protected void paintComponent( java.awt.Graphics g ) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					Step step = (Step)stepsComboBoxModel.getSelectedItem();
					
					java.awt.geom.Area area = new java.awt.geom.Area( g2.getClip() );
					if( step != null ) {
						step.subtractIfAppropriate( area, Stencil.this, false );
					}
					g2.setPaint( getStencilPaint() );
					g2.fill( area );
					super.paintComponent( g );
				}
				@Override
				public boolean contains( int x, int y ) {
					java.awt.geom.Area area = new java.awt.geom.Area( new java.awt.Rectangle( 0, 0, this.getWidth(), this.getHeight() ) );
					Step step = (Step)stepsComboBoxModel.getSelectedItem();
					if( step != null ) {
						step.subtractIfAppropriate( area, Stencil.this, true );
					}
					return area.contains( x, y );
				}
				private void redispatch( java.awt.event.MouseEvent e ) {
					java.awt.Point p = e.getPoint();
					if( this.contains( p.x, p.y ) ) {
						//pass
					} else {
						java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt( application.getSingleton().getFrame().getAwtWindow().getLayeredPane(), p.x, p.y );
						if( component != null ) {
							java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint( this, p, component );
							component.dispatchEvent( new java.awt.event.MouseEvent( component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger() ) );
						}
					}
				}
			};
			final JStencil rv = new JStencil();
			rv.addMouseListener( new java.awt.event.MouseListener() {
				public void mouseClicked( java.awt.event.MouseEvent e ) {
					rv.redispatch( e );
				}
				public void mouseEntered( java.awt.event.MouseEvent e ) {
					rv.redispatch( e );
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
					rv.redispatch( e );
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					rv.redispatch( e );
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
					rv.redispatch( e );
				}
			} );
			rv.addMouseMotionListener( new java.awt.event.MouseMotionListener() {
				public void mouseMoved( java.awt.event.MouseEvent e ) {
					rv.redispatch( e );
				}
				public void mouseDragged( java.awt.event.MouseEvent e ) {
					rv.redispatch( e );
				}
			} );
			rv.setLayout( new java.awt.BorderLayout() );
			rv.setOpaque( false );
			return rv;
		}
		
		@Override
		protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			super.handleAddedTo( parent );
			this.comboBox.getAwtComponent().addItemListener( this.itemListener );
			this.handleStepChanged( (Step)stepsComboBoxModel.getSelectedItem() );
		}
		@Override
		protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			this.comboBox.getAwtComponent().addItemListener( this.itemListener );
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
			this.revalidateAndRepaint();
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
		if( isVisible ) {
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
			
			javax.swing.JFrame frame = this.application.getFrame().getAwtWindow();
			javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();

			stencil.getAwtComponent().setBounds( layeredPane.getBounds() );
			layeredPane.addComponentListener( componentListener );
			
			layeredPane.add( stencil.getAwtComponent(), null );
			layeredPane.setLayer( stencil.getAwtComponent(), javax.swing.JLayeredPane.POPUP_LAYER - 1 );

			final int PAD = 4;
			frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder(PAD,PAD,0,PAD));
			((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder(0,PAD,PAD+32,PAD));
			
			stencil.getAwtComponent().repaint();
		}
	}
}
