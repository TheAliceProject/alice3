package org.alice.ide.croquet;

public class PopupMenuButton extends edu.cmu.cs.dennisc.croquet.AbstractButton<javax.swing.AbstractButton, edu.cmu.cs.dennisc.croquet./*AbstractPopupMenu*/Operation> {
	private static final int AFFORDANCE_WIDTH = 6;
	private static final int AFFORDANCE_HALF_HEIGHT = 5;
	private static final java.awt.Color ARROW_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 191 );
	private class Border implements javax.swing.border.Border {
		protected java.awt.Insets getBorderInsets( java.awt.Insets rv, java.awt.Component c ) {
			rv.set( 1, 1, 1, 5 + AFFORDANCE_WIDTH );
			return rv;
		}
		private java.awt.Insets buffer = new java.awt.Insets( 0,0,0,0 );
		public java.awt.Insets getBorderInsets( java.awt.Component c ) {
			return getBorderInsets( buffer, c );
		}
		public boolean isBorderOpaque() {
			return false;
		}
		public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.ButtonModel buttonModel = button.getModel();
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			java.awt.Paint prevPaint = g2.getPaint();
			boolean isActive = buttonModel.isRollover();
//			if( isActive || this.isInactiveFeedbackDesired() ) {
//				java.awt.Paint prevPaint = g2.getPaint();
//				if( isActive ) {
//					g2.setColor( java.awt.Color.WHITE );
//				}
//				try {
//					this.fillBounds( g2, x, y, width, height );
//				} finally {
//					g2.setPaint( prevPaint );
//				}
//			}
			
			float x0 = x + width - 4 - AFFORDANCE_WIDTH;
			float x1 = x0 + AFFORDANCE_WIDTH;
			float xC = (x0 + x1) / 2;

			float yC = (y + height)/2;
			float y0 = yC-AFFORDANCE_HALF_HEIGHT;
			float y1 = yC+AFFORDANCE_HALF_HEIGHT;

			java.awt.Color triangleFill;
			java.awt.Color triangleOutline;
			if( isActive ) {
				triangleFill = java.awt.Color.YELLOW;
				triangleOutline = java.awt.Color.BLACK;
			} else {
				triangleFill = ARROW_COLOR;
				triangleOutline = null;
			}

			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

//			float[] xs = { x0, xC, x1 };
//			float[] ys = { y0, y1, y0 };
			
			java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
			path.moveTo( x0, y0 );
			path.lineTo( xC, y1 );
			path.lineTo( x1, y0 );
			path.closePath();
			
			
			g2.setColor( triangleFill );
			g2.fill( path );
//			g2.fillPolygon( xs, ys, 3 );
			if( triangleOutline != null ) {
				g2.setColor( triangleOutline );
				g2.draw( path );
//				g2.drawPolygon( xs, ys, 3 );
			}

			
			if( isActive ) {
				g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
				g2.setColor( java.awt.Color.BLUE );
				g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, width-3.0f, height-3.0f ) );
			} else {
				if( PopupMenuButton.this.isInactiveFeedbackDesired() ) {
					g2.setColor( java.awt.Color.WHITE );
					//g2.drawRect( x, y, width-1, height-1 );
					g2.drawLine( x+1, y+1, x+width-4, y+1 );
					g2.drawLine( x+1, y+1, x+1, y+height-4 );
				}
			}
			g2.setPaint( prevPaint );
		}
	}
	private edu.cmu.cs.dennisc.croquet.Component< ? > component;
	public PopupMenuButton( edu.cmu.cs.dennisc.croquet./*AbstractPopupMenu*/Operation model, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		super( model );
		this.component = component;
	}
	public PopupMenuButton( edu.cmu.cs.dennisc.croquet./*AbstractPopupMenu*/Operation model ) {
		this( model, null );
	}
	protected boolean isInactiveFeedbackDesired() {
		return true;
	}
	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		javax.swing.AbstractButton rv;
		javax.swing.border.Border border;
		if( this.component != null ) {
			rv = new javax.swing.AbstractButton() {
				@Override
				public java.awt.Dimension getMaximumSize() {
					return this.getPreferredSize();
				}
			};
			rv.setLayout(  new java.awt.BorderLayout() );
			rv.add( this.component.getAwtComponent(), java.awt.BorderLayout.CENTER );
			border = new Border();
		} else {
			rv = new javax.swing.JButton() {
				@Override
				public void updateUI() {
					this.setUI( new javax.swing.plaf.basic.BasicButtonUI() );
				}
			};
			border = new Border() {
				@Override
				protected java.awt.Insets getBorderInsets( java.awt.Insets rv, java.awt.Component c ) {
					rv = super.getBorderInsets( rv, c );
					rv.left += 3;
					return rv;
				}
			};
		}
		rv.setBorder( border );
		return rv;
	}
}
