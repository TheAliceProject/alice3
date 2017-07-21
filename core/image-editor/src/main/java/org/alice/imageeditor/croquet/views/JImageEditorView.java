/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.imageeditor.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class JImageEditorView extends javax.swing.JComponent {
	private static final int CLICK_THRESHOLD = 3;
	private static final java.awt.Paint CROP_PAINT = new java.awt.Color( 0, 0, 0, 127 );

	private static java.awt.Shape createShape( java.awt.Point a, java.awt.Point b, double scale, java.awt.Rectangle cropRectangle ) {
		int x = Math.min( a.x, b.x );
		int y = Math.min( a.y, b.y );
		int width = Math.abs( b.x - a.x );
		int height = Math.abs( b.y - a.y );

		x -= 1;
		y -= 1;

		if( ( width > CLICK_THRESHOLD ) || ( height > CLICK_THRESHOLD ) ) {
			double sx = x / scale;
			double sy = y / scale;
			double sw = width / scale;
			double sh = height / scale;
			if( cropRectangle != null ) {
				sx += cropRectangle.x;
				sy += cropRectangle.y;
			}
			return new java.awt.geom.Rectangle2D.Double( sx, sy, sw, sh );
		} else {
			return null;
		}
	}

	private static final java.awt.Stroke SHAPE_STROKE = new java.awt.BasicStroke( 8.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	private static final java.awt.Stroke OUTLINE_STROKE = new java.awt.BasicStroke( 0.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND, 8.0f, new float[] { 8.0f }, 0.0f );
	private static final javax.swing.KeyStroke ESCAPE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ESCAPE, 0 );
	private static final javax.swing.KeyStroke CLEAR_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK );

	private static final java.awt.Stroke[] DROP_SHADOW_STROKES;
	static {
		final int N = 5;
		DROP_SHADOW_STROKES = new java.awt.Stroke[ N ];
		for( int i = 0; i < N; i++ ) {
			DROP_SHADOW_STROKES[ i ] = new java.awt.BasicStroke( ( i + 1 ) * 5.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		}
	};

	private static final java.awt.Paint DROP_SHADOW_PAINT = new java.awt.Color( 127, 127, 127, 15 );

	private static java.awt.Point getClampedPoint( java.awt.event.MouseEvent e ) {
		java.awt.Component awtComponent = e.getComponent();
		java.awt.Point rv = e.getPoint();
		rv.x = Math.min( Math.max( 1, e.getX() ), awtComponent.getWidth() - 2 );
		rv.y = Math.min( Math.max( 1, e.getY() ), awtComponent.getHeight() - 2 );
		return rv;
	}

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			if( e.getButton() == java.awt.event.MouseEvent.BUTTON1 ) {
				ptPressed = getClampedPoint( e );
			} else {
				ptPressed = null;
			}
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			if( ptPressed != null ) {
				handleMouseReleased( e );
				ptPressed = null;
				ptDragged = null;
				repaint();
			}
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}
	};

	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		@Override
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			if( ptPressed != null ) {
				ptDragged = e.getPoint();
				repaint();
			}
		}
	};

	private final java.awt.event.ActionListener escapeListener = new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			ptPressed = null;
			ptDragged = null;
			repaint();
		}
	};

	private java.awt.Point ptPressed;
	private java.awt.Point ptDragged;

	private final org.alice.imageeditor.croquet.ImageEditorFrame imageEditorFrame;

	public JImageEditorView( org.alice.imageeditor.croquet.ImageEditorFrame imageEditorFrame ) {
		this.imageEditorFrame = imageEditorFrame;
	}

	private void handleMouseReleased( final java.awt.event.MouseEvent e ) {
		java.awt.Point p = getClampedPoint( e );
		java.awt.Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
		java.awt.Shape shape = createShape( ptPressed, p, getScale(), crop );
		org.alice.imageeditor.croquet.Tool tool = this.imageEditorFrame.getToolState().getValue();
		if( tool == org.alice.imageeditor.croquet.Tool.ADD_RECTANGLE ) {
			if( shape != null ) {
				org.lgna.croquet.history.Transaction transaction = org.lgna.croquet.Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().acquireActiveTransaction();
				org.lgna.croquet.CompletionModel model = null;
				org.lgna.croquet.history.CompletionStep completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, model, org.lgna.croquet.triggers.MouseEventTrigger.createUserInstance( e ), null );
				org.alice.imageeditor.croquet.edits.AddShapeEdit edit = new org.alice.imageeditor.croquet.edits.AddShapeEdit( completionStep, shape, imageEditorFrame );
				completionStep.commitAndInvokeDo( edit );
			}
		} else if( tool == org.alice.imageeditor.croquet.Tool.CROP_SELECT ) {
			this.imageEditorFrame.getCropSelectHolder().setValue( shape != null ? shape.getBounds() : null );
		}
	}

	private void drawShapes( java.awt.Graphics2D g2, java.awt.geom.AffineTransform mOriginal ) {
		java.awt.Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();

		java.awt.Stroke prevStroke = g2.getStroke();
		Object prevAntialias = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

		if( imageEditorFrame.getDropShadowState().getValue() ) {
			final int DROP_SHADOW_OFFSET = 8;
			g2.translate( DROP_SHADOW_OFFSET, DROP_SHADOW_OFFSET );
			g2.setPaint( DROP_SHADOW_PAINT );
			for( java.awt.Stroke stroke : DROP_SHADOW_STROKES ) {
				g2.setStroke( stroke );
				for( java.awt.Shape shape : imageEditorFrame.getShapes() ) {
					g2.draw( shape );
				}
			}
			g2.translate( -DROP_SHADOW_OFFSET, -DROP_SHADOW_OFFSET );
		}

		g2.setPaint( java.awt.Color.RED );
		g2.setStroke( SHAPE_STROKE );
		for( java.awt.Shape shape : imageEditorFrame.getShapes() ) {
			g2.draw( shape );
		}

		java.awt.Shape cropShape = null;

		boolean isInTheMidstOfDragging = ( ptPressed != null ) && ( ptDragged != null );
		org.alice.imageeditor.croquet.Tool tool = this.imageEditorFrame.getToolState().getValue();
		if( isInTheMidstOfDragging ) {
			java.awt.Shape shape = createShape( ptPressed, ptDragged, this.getScale(), crop );
			if( shape != null ) {
				if( tool == org.alice.imageeditor.croquet.Tool.ADD_RECTANGLE ) {
					g2.draw( shape );
				} else if( tool == org.alice.imageeditor.croquet.Tool.CROP_SELECT ) {
					cropShape = shape;
				}
			}
		}

		java.awt.Rectangle selection = this.imageEditorFrame.getCropSelectHolder().getValue();
		if( selection != null ) {
			if( cropShape != null ) {
				// pass
			} else {
				cropShape = selection;
			}
		}

		if( cropShape != null ) {
			try {
				java.awt.geom.AffineTransform m = g2.getTransform();
				java.awt.geom.Point2D.Double ptA = new java.awt.geom.Point2D.Double( 0, 0 );
				java.awt.geom.Point2D.Double ptB = new java.awt.geom.Point2D.Double( this.getWidth(), this.getHeight() );
				mOriginal.transform( ptA, ptA );
				mOriginal.transform( ptB, ptB );
				m.inverseTransform( ptA, ptA );
				m.inverseTransform( ptB, ptB );

				java.awt.Shape bounds = new java.awt.geom.Rectangle2D.Double( ptA.getX(), ptA.getY(), ptB.getX() - ptA.getX(), ptB.getY() - ptA.getY() );
				java.awt.geom.Area area = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createSubtraction( bounds, cropShape );
				g2.setPaint( CROP_PAINT );
				g2.fill( area );
			} catch( java.awt.geom.NoninvertibleTransformException nte ) {
				throw new RuntimeException( nte );
			}
		}

		g2.setStroke( prevStroke );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialias );
	}

	private int getImageResolution() {
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: getImageResolution()" );
		return 300;
	}

	private int getScreenResolution() {
		return edu.cmu.cs.dennisc.java.awt.ToolkitUtilities.getScreenResolution( this );
	}

	private double getScale() {
		if( imageEditorFrame.getShowInScreenResolutionState().getValue() ) {
			return this.getScreenResolution() / (double)this.getImageResolution();
		} else {
			return 1.0;
		}
	}

	private int scaledImageWidth = -1;
	private int scaledImageHeight = -1;
	private java.awt.Image scaledImage = null;

	private java.awt.Image getScaledImage( java.awt.Image image, int width, int height ) {
		if( ( width != this.scaledImageWidth ) || ( height != this.scaledImageHeight ) ) {
			this.scaledImage = null;
		}
		if( this.scaledImage != null ) {
			//pass
		} else {
			this.scaledImageWidth = width;
			this.scaledImageHeight = height;
			this.scaledImage = image.getScaledInstance( this.scaledImageWidth, this.scaledImageHeight, java.awt.Image.SCALE_SMOOTH );
		}
		return this.scaledImage;
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

		java.awt.Image image = imageEditorFrame.getImageHolder().getValue();

		if( image != null ) {
			java.awt.geom.AffineTransform m = g2.getTransform();
			g2.translate( 1, 1 );
			java.awt.Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
			double scale = this.getScale();
			if( scale != 1.0 ) {
				int imageWidth = image.getWidth( this );
				int imageHeight = image.getHeight( this );
				int scaledImageWidth = (int)Math.ceil( imageWidth * scale );
				int scaledImageHeight = (int)Math.ceil( imageHeight * scale );
				if( crop != null ) {
					g2.scale( scale, scale );
					if( crop != null ) {
						g2.translate( -crop.x, -crop.y );
					}
					g2.drawImage( image, 0, 0, this );
				} else {
					g2.drawImage( this.getScaledImage( image, scaledImageWidth, scaledImageHeight ), 0, 0, this );
					g2.scale( scale, scale );
				}
			} else {
				if( crop != null ) {
					g2.translate( -crop.x, -crop.y );
				}
				g2.drawImage( image, 0, 0, this );
			}

			this.drawShapes( g2, m );
			g2.setTransform( m );
		}
		if( imageEditorFrame.getShowDashedBorderState().getValue() ) {
			java.awt.Stroke prevStroke = g2.getStroke();
			g2.setColor( java.awt.Color.DARK_GRAY );
			g2.setStroke( OUTLINE_STROKE );
			g2.drawRect( 0, 0, this.getWidth() - 1, this.getHeight() - 1 );
			g2.setStroke( prevStroke );
		}
	}

	/* package-private */void render( java.awt.Graphics2D g2 ) {
		java.awt.Image image = imageEditorFrame.getImageHolder().getValue();
		if( image != null ) {
			java.awt.Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
			if( crop != null ) {
				g2.translate( -crop.x, -crop.y );
			}
			g2.drawImage( image, 0, 0, this );
			this.drawShapes( g2, g2.getTransform() );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
		}
	}

	@Override
	public java.awt.Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Image image = imageEditorFrame.getImageHolder().getValue();
		if( image != null ) {
			java.awt.Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
			int srcWidth = image.getWidth( this );
			int srcHeight = image.getHeight( this );
			int width;
			int height;
			if( crop != null ) {
				srcWidth = crop.width;
				srcHeight = crop.height;
			}
			if( imageEditorFrame.getShowInScreenResolutionState().getValue() ) {
				double scale = this.getScale();
				width = (int)Math.ceil( srcWidth * scale );
				height = (int)Math.ceil( srcHeight * scale );
			} else {
				width = srcWidth;
				height = srcHeight;
			}
			return new java.awt.Dimension( width + 2, height + 2 );
		} else {
			return super.getPreferredSize();
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.addMouseListener( this.mouseListener );
		this.addMouseMotionListener( this.mouseMotionListener );
		this.registerKeyboardAction( this.escapeListener, ESCAPE_KEY_STROKE, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
	}

	@Override
	public void removeNotify() {
		this.unregisterKeyboardAction( CLEAR_KEY_STROKE );
		this.unregisterKeyboardAction( ESCAPE_KEY_STROKE );
		this.removeMouseMotionListener( this.mouseMotionListener );
		this.removeMouseListener( this.mouseListener );
		super.removeNotify();
	}
};
