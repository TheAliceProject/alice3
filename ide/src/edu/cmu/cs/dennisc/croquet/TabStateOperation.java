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
public abstract class TabStateOperation extends BooleanStateOperation {
	public TabStateOperation( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID individualUUID, boolean initialState ) {
		super( group, individualUUID, initialState );
	}
	public TabStateOperation( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID individualUUID, boolean initialState, String title ) {
		super( group, individualUUID, initialState, title );
	}
	private edu.cmu.cs.dennisc.croquet.ScrollPane singletonScrollPane;

	protected ScrollPane createSingletonScrollPane() {
		ScrollPane rv = new edu.cmu.cs.dennisc.croquet.ScrollPane( this.getSingletonView() );
		rv.setOpaque( false );
		rv.setBackgroundColor( this.getSingletonView().getBackgroundColor() );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		rv.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
		return rv;
	}
	public final ScrollPane getSingletonScrollPane() {
		if( this.singletonScrollPane != null ) {
			//pass
		} else {
			this.singletonScrollPane = this.createSingletonScrollPane();
		}
		return this.singletonScrollPane;
	}
	
	private JComponent<?> singletonView;
	public final JComponent<?> getSingletonView() {
		if( this.singletonView != null ) {
			//pass
		} else {
			this.singletonView = this.createSingletonView();
		}
		return this.singletonView;
	}
	protected abstract JComponent<?> createSingletonView();
	protected abstract boolean isCloseAffordanceDesired();

	private static class CloseButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
		private static final java.awt.Color BASE_COLOR = new java.awt.Color( 127, 63, 63 );
		private static final java.awt.Color HIGHLIGHT_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.shiftHSB( BASE_COLOR, 0, 0, +0.25f );
		private static final java.awt.Color PRESS_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.shiftHSB( BASE_COLOR, 0, 0, -0.125f );

		private int getIconWidth() {
			return 14;
		}
		private int getIconHeight() {
			return getIconWidth();
		}

		@Override
		public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.ButtonModel model = button.getModel();

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			float size = Math.min( this.getIconWidth(), this.getIconHeight() ) * 0.9f;

			float w = size;
			float h = size * 0.25f;
			float xC = -w * 0.5f;
			float yC = -h * 0.5f;
			java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( xC, yC, w, h, h, h );

			java.awt.geom.Area area0 = new java.awt.geom.Area( rr );
			java.awt.geom.Area area1 = new java.awt.geom.Area( rr );

			java.awt.geom.AffineTransform m0 = new java.awt.geom.AffineTransform();
			m0.rotate( Math.PI * 0.25 );
			area0.transform( m0 );

			java.awt.geom.AffineTransform m1 = new java.awt.geom.AffineTransform();
			m1.rotate( Math.PI * 0.75 );
			area1.transform( m1 );

			area0.add( area1 );

			int x0 = 0;
			int y0 = 0;
			
			java.awt.geom.AffineTransform m = new java.awt.geom.AffineTransform();
			m.translate( x0 + getIconWidth() / 2, y0 + getIconWidth() / 2 );
			area0.transform( m );

			java.awt.Paint prevPaint = g2.getPaint();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			if( model.isRollover() ) {
				if( model.isPressed() ) {
					g2.setPaint( PRESS_COLOR );
				} else {
					g2.setPaint( HIGHLIGHT_COLOR );
				}
			} else {
				g2.setPaint( java.awt.Color.WHITE );
			}
			javax.swing.AbstractButton parent = (javax.swing.AbstractButton)button.getParent();
			if( parent.isSelected() ) {
				g2.fill( area0 );
				g2.setPaint( java.awt.Color.BLACK );
			} else {
				g2.setPaint( java.awt.Color.DARK_GRAY );
			}
			g2.draw( area0 );
			g2.setPaint( prevPaint );
		}
		@Override
		public java.awt.Dimension getPreferredSize(javax.swing.JComponent c) {
			return new java.awt.Dimension( this.getIconWidth(), this.getIconHeight() );
		}
	}

	private static class JTabTitle extends javax.swing.AbstractButton {
		private java.awt.event.MouseListener mouseListener = new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				JTabTitle.this.select();
			}
		};
		private java.beans.PropertyChangeListener propertyChangeListener = new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent e) {
				JTabTitle.this.updateLabel();
			}
		};
		
		private void select() {
			this.setSelected( true );
			this.getParent().repaint();
			//this.getParent().repaint( this.getX(), this.getY(), this.getWidth() + EAST_TAB_PAD, this.getHeight() );
		}

		private javax.swing.JLabel label = new javax.swing.JLabel();
		private javax.swing.JButton closeButton = new javax.swing.JButton();
		private javax.swing.Action action;
		public JTabTitle( boolean isCloseAffordanceDesired ) {
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
			this.add( this.label );
			
			if( isCloseAffordanceDesired ) {
				this.closeButton = new javax.swing.JButton();
				this.closeButton.setUI( new CloseButtonUI() );
				this.closeButton.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
				this.closeButton.setOpaque( false );
				this.add( this.closeButton );
			}

			
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 12, 4, 4, 8 ) );
			this.setOpaque( false );
		}
		
		private void updateLabel() {
			if( this.action != null ) {
				this.label.setText( (String)action.getValue( javax.swing.Action.NAME ) );
			}
		}
		@Override
		public void setAction(javax.swing.Action action) {
			if( this.action != null ) {
				this.action.removePropertyChangeListener( this.propertyChangeListener );
			}
			super.setAction(action);
			this.action = action;
			this.updateLabel();
			if( this.action != null ) {
				this.action.addPropertyChangeListener( this.propertyChangeListener );
			}
		}
		@Override
		public void addNotify() {
			super.addNotify();
			this.addMouseListener( this.mouseListener );
		}
		@Override
		public void removeNotify() {
			this.removeMouseListener( this.mouseListener );
			super.removeNotify();
		}
	}

	private class TabTitle extends AbstractButton< javax.swing.AbstractButton > {
		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
//			javax.swing.AbstractButton rv = new JTabTitle( TabStateOperation.this.isCloseAffordanceDesired() );
//			rv.setModel( TabStateOperation.this.getButtonModel() );
//			return rv;
			return new JTabTitle( TabStateOperation.this.isCloseAffordanceDesired() );
		}
	}
	
	private TabTitle tabTitle;
	/*package-private*/AbstractButton<?> getSingletonTabTitle() {
		if( this.tabTitle != null ) {
			//pass
		} else {
			this.tabTitle = new TabTitle();
			register( this.tabTitle );
		}
		return this.tabTitle;
	}
}
