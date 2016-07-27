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
package edu.cmu.cs.dennisc.render.gl.imp;

import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.GL.GL_LINE_LOOP;
import static com.jogamp.opengl.GL.GL_LINE_STRIP;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_RGBA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_TRIANGLE_FAN;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;
import static com.jogamp.opengl.GL2.GL_LINE_STIPPLE;
import static com.jogamp.opengl.GL2.GL_POLYGON;
import static com.jogamp.opengl.GL2ES1.GL_ALPHA_SCALE;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import com.jogamp.opengl.glu.GLU;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class Graphics2D extends edu.cmu.cs.dennisc.render.Graphics2D {
	private static edu.cmu.cs.dennisc.math.SineCosineCache s_sineCosineCache = new edu.cmu.cs.dennisc.math.SineCosineCache( 8 );

	private static final java.awt.Paint DEFAULT_PAINT = java.awt.Color.BLACK;
	private static final java.awt.Color DEFAULT_BACKGROUND = java.awt.Color.WHITE;
	private static final java.awt.Font DEFAULT_FONT = new java.awt.Font( null, java.awt.Font.PLAIN, 12 );
	private static final java.awt.Stroke DEFAULT_STROKE = new java.awt.BasicStroke( 1 );

	private RenderContext renderContext;
	private java.awt.Paint paint = DEFAULT_PAINT;
	private java.awt.Color background = DEFAULT_BACKGROUND;
	private java.awt.Font font = DEFAULT_FONT;
	private java.awt.Stroke stroke = DEFAULT_STROKE;

	private java.awt.RenderingHints renderingHints;

	private java.awt.geom.AffineTransform affineTransform = new java.awt.geom.AffineTransform();
	private double[] glTransform = new double[ 16 ];
	private java.nio.DoubleBuffer glTransformBuffer = java.nio.DoubleBuffer.wrap( glTransform );

	private int width = -1;
	private int height = -1;

	public Graphics2D( RenderContext renderContext ) {
		assert renderContext != null;
		this.renderContext = renderContext;
		java.util.Map<java.awt.RenderingHints.Key, Object> map = new java.util.HashMap<java.awt.RenderingHints.Key, Object>();
		map.put( java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT );
		map.put( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_DEFAULT );
		map.put( java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_DEFAULT );
		map.put( java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_DEFAULT );
		map.put( java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT );

		//todo: investigate
		//map.put( java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_DEFAULT );
		map.put( java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR );

		map.put( java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_DEFAULT );
		map.put( java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_DEFAULT );
		Object antialiasTextValue;
		if( Boolean.getBoolean( "swing.aatext" ) ) {
			antialiasTextValue = java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
		} else {
			antialiasTextValue = java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
		}
		map.put( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, antialiasTextValue );
		this.setRenderingHints( map );
	}

	public void initialize( java.awt.Dimension surfaceSize ) {
		assert this.renderContext.gl != null;
		this.width = surfaceSize.width;
		this.height = surfaceSize.height;

		setPaint( DEFAULT_PAINT );
		setBackground( DEFAULT_BACKGROUND );
		setFont( DEFAULT_FONT );

		this.renderContext.gl.glMatrixMode( GL_PROJECTION );
		this.renderContext.gl.glPushMatrix();
		this.renderContext.gl.glLoadIdentity();
		this.renderContext.gl.glOrtho( 0, this.width - 1, this.height - 1, 0, -1, 1 );
		//this.renderContext.gl.glViewport( 0, 0, width, height );
		this.renderContext.gl.glMatrixMode( GL_MODELVIEW );
		this.renderContext.gl.glPushMatrix();
		this.renderContext.gl.glLoadIdentity();

		this.affineTransform.setToIdentity();

		this.renderContext.gl.glDisable( GL_DEPTH_TEST );
		this.renderContext.gl.glDisable( GL_LIGHTING );
		this.renderContext.gl.glDisable( GL_CULL_FACE );

		this.renderContext.setDiffuseColorTextureAdapter( null, false );
		this.renderContext.setBumpTextureAdapter( null );

		this.renderContext.gl.glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
	}

	private boolean isInTheMidstOfFinalization = false;

	@Override
	public void finalize() {
		this.isInTheMidstOfFinalization = true;
		try {
			super.finalize();
		} finally {
			this.isInTheMidstOfFinalization = false;
		}
	}

	// java.awt.Graphics

	@Override
	public void dispose() {
		if( this.isInTheMidstOfFinalization ) {
			//pass
		} else {
			this.renderContext.gl.glFlush();
			if( isValid() ) {
				this.renderContext.gl.glMatrixMode( GL_MODELVIEW );
				this.renderContext.gl.glPopMatrix();
				this.renderContext.gl.glMatrixMode( GL_PROJECTION );
				this.renderContext.gl.glPopMatrix();
				this.width = -1;
				this.height = -1;
			}
		}
	}

	@Override
	public boolean isValid() {
		return ( this.width != -1 ) && ( this.height != -1 );
	}

	@Override
	public java.awt.Graphics create() {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public java.awt.Color getColor() {
		if( this.paint instanceof java.awt.Color ) {
			return (java.awt.Color)this.paint;
		} else {
			throw new RuntimeException( "use getPaint()" );
		}
	}

	@Override
	public void setColor( java.awt.Color color ) {
		setPaint( color );
	}

	@Override
	public void setPaintMode() {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void setXORMode( java.awt.Color c1 ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public java.awt.Font getFont() {
		return this.font;
	}

	@Override
	public void setFont( java.awt.Font font ) {
		this.font = font;
	}

	@Override
	public java.awt.FontMetrics getFontMetrics( java.awt.Font f ) {
		return java.awt.Toolkit.getDefaultToolkit().getFontMetrics( f );
	}

	@Override
	public java.awt.Rectangle getClipBounds() {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void clipRect( int x, int y, int width, int height ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void setClip( int x, int y, int width, int height ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public java.awt.Shape getClip() {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void setClip( java.awt.Shape clip ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void copyArea( int x, int y, int width, int height, int dx, int dy ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void drawLine( int x1, int y1, int x2, int y2 ) {
		this.renderContext.gl.glBegin( GL_LINES );
		this.renderContext.gl.glVertex2i( x1, y1 );
		this.renderContext.gl.glVertex2i( x2, y2 );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void fillRect( int x, int y, int width, int height ) {
		this.renderContext.gl.glBegin( GL_POLYGON );
		this.renderContext.gl.glVertex2i( x, y );
		this.renderContext.gl.glVertex2i( x + width, y );
		this.renderContext.gl.glVertex2i( x + width, y + height );
		this.renderContext.gl.glVertex2i( x, y + height );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void clearRect( int x, int y, int width, int height ) {
		glSetColor( this.background );
		this.renderContext.gl.glBegin( GL_POLYGON );
		this.renderContext.gl.glVertex2i( x, y );
		this.renderContext.gl.glVertex2i( x + width, y );
		this.renderContext.gl.glVertex2i( x + width, y + height );
		this.renderContext.gl.glVertex2i( x, y + height );
		this.renderContext.gl.glEnd();
		glSetPaint( this.paint );
	}

	private void glQuarterOval( double centerX, double centerY, double radiusX, double radiusY, int quadrant ) {
		int n = s_sineCosineCache.cosines.length;
		int max = n - 1;
		for( int lcv = 0; lcv < n; lcv++ ) {
			int i = max - lcv;
			double cos = s_sineCosineCache.getCosine( quadrant, i );
			double sin = s_sineCosineCache.getSine( quadrant, i );
			this.renderContext.gl.glVertex2d( centerX + ( cos * radiusX ), centerY + ( sin * radiusY ) );
		}
	}

	private void glRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		//int x0 = x;
		int x1 = x + arcWidth;
		int x2 = ( x + width ) - arcWidth;
		//int x3 = x+width;

		//int y0 = y;
		int y1 = y + arcHeight;
		int y2 = ( y + height ) - arcHeight;
		//int y3 = y+height;

		glQuarterOval( x1, y2, arcWidth, arcHeight, 1 );
		glQuarterOval( x2, y2, arcWidth, arcHeight, 0 );
		glQuarterOval( x2, y1, arcWidth, arcHeight, 3 );
		glQuarterOval( x1, y1, arcWidth, arcHeight, 2 );
	}

	@Override
	public void drawRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		this.renderContext.gl.glBegin( GL_LINE_LOOP );
		glRoundRect( x, y, width, height, arcWidth, arcHeight );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void fillRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		this.renderContext.gl.glBegin( GL_TRIANGLE_FAN );
		glRoundRect( x, y, width, height, arcWidth, arcHeight );
		this.renderContext.gl.glEnd();
	}

	private void glOval( int x, int y, int width, int height ) {
		double radiusX = width * 0.5;
		double radiusY = height * 0.5;
		double centerX = x + radiusX;
		double centerY = y + radiusY;
		glQuarterOval( centerX, centerY, radiusX, radiusY, 3 );
		glQuarterOval( centerX, centerY, radiusX, radiusY, 2 );
		glQuarterOval( centerX, centerY, radiusX, radiusY, 1 );
		glQuarterOval( centerX, centerY, radiusX, radiusY, 0 );
	}

	@Override
	public void drawOval( int x, int y, int width, int height ) {
		this.renderContext.gl.glBegin( GL_LINE_LOOP );
		glOval( x, y, width, height );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void fillOval( int x, int y, int width, int height ) {
		this.renderContext.gl.glBegin( GL_TRIANGLE_FAN );
		glOval( x, y, width, height );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void drawArc( int x, int y, int width, int height, int startAngle, int arcAngle ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void fillArc( int x, int y, int width, int height, int startAngle, int arcAngle ) {
		throw new RuntimeException( "not implemented" );
	}

	private void glPoly( int xPoints[], int yPoints[], int nPoints ) {
		for( int i = 0; i < nPoints; i++ ) {
			this.renderContext.gl.glVertex2i( xPoints[ i ], yPoints[ i ] );
		}
	}

	@Override
	public void drawPolyline( int xPoints[], int yPoints[], int nPoints ) {
		this.renderContext.gl.glBegin( GL_LINE_STRIP );
		glPoly( xPoints, yPoints, nPoints );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void drawPolygon( int xPoints[], int yPoints[], int nPoints ) {
		this.renderContext.gl.glBegin( GL_LINE_LOOP );
		glPoly( xPoints, yPoints, nPoints );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void fillPolygon( int xPoints[], int yPoints[], int nPoints ) {
		this.renderContext.gl.glBegin( GL_POLYGON );
		glPoly( xPoints, yPoints, nPoints );
		this.renderContext.gl.glEnd();
	}

	@Override
	public void drawString( String str, int x, int y ) {
		drawString( str, (float)x, (float)y );
	}

	@Override
	public void drawString( java.text.AttributedCharacterIterator iterator, int x, int y ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void drawChars( char[] data, int offset, int length, int x, int y ) {
		drawString( new String( data, offset, length ), x, y );
	}

	@Override
	public void drawBytes( byte[] data, int offset, int length, int x, int y ) {
		drawString( new String( data, offset, length ), x, y );
	}

	@Override
	public boolean drawImage( java.awt.Image image, int x, int y, java.awt.image.ImageObserver observer ) {
		boolean isRemembered = isRemembered( image );
		if( isRemembered ) {
			//pass
		} else {
			remember( image );
		}
		try {
			this.paint( this.imageToImageGeneratorMap.get( image ), x, y, 1.0f );
		} finally {
			if( isRemembered ) {
				//pass
			} else {
				forget( image );
			}
		}
		return true;
	}

	@Override
	public boolean drawImage( java.awt.Image image, int x, int y, int width, int height, java.awt.image.ImageObserver observer ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public boolean drawImage( java.awt.Image image, int x, int y, java.awt.Color bgcolor, java.awt.image.ImageObserver observer ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public boolean drawImage( java.awt.Image image, int x, int y, int width, int height, java.awt.Color bgcolor, java.awt.image.ImageObserver observer ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public boolean drawImage( java.awt.Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.image.ImageObserver observer ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public boolean drawImage( java.awt.Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.Color bgcolor, java.awt.image.ImageObserver observer ) {
		throw new RuntimeException( "not implemented" );
	}

	// java.awt.Graphics2D

	@Override
	public void draw3DRect( int x, int y, int width, int height, boolean raised ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void fill3DRect( int x, int y, int width, int height, boolean raised ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public boolean drawImage( java.awt.Image img, java.awt.geom.AffineTransform xform, java.awt.image.ImageObserver obs ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void drawImage( java.awt.image.BufferedImage img, java.awt.image.BufferedImageOp op, int x, int y ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void drawRenderedImage( java.awt.image.RenderedImage img, java.awt.geom.AffineTransform xform ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void drawRenderableImage( java.awt.image.renderable.RenderableImage img, java.awt.geom.AffineTransform xform ) {
		throw new RuntimeException( "not implemented" );
	}

	//	@Override
	//	public void drawString( String str, int x, int y ) {
	//		throw new RuntimeException( "not implemented" );
	//	}

	@Override
	public void drawString( String text, float x, float y ) {
		ReferencedObject<TextRendererHolder> referencedObject = this.activeFontToTextRendererMap.get( this.font );
		//todo?
		if( referencedObject == null ) {
			remember( this.font );
			referencedObject = this.activeFontToTextRendererMap.get( this.font );
		}
		assert referencedObject != null;
		com.jogamp.opengl.util.awt.TextRenderer glTextRenderer = referencedObject.getObject().getTextRenderer( this.font, this.renderContext.gl );
		glTextRenderer.beginRendering( this.width, this.height );
		if( this.paint instanceof java.awt.Color ) {
			java.awt.Color color = (java.awt.Color)this.paint;
			glTextRenderer.setColor( color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f );
		} else {
			//todo?
		}
		int xPixel = (int)( x + this.affineTransform.getTranslateX() );
		int yPixel = (int)( y + this.affineTransform.getTranslateY() );
		glTextRenderer.draw( text, xPixel, this.height - yPixel );
		glTextRenderer.endRendering();
	}

	//	@Override
	//	public void drawString( java.text.AttributedCharacterIterator iterator, int x, int y ) {
	//		throw new RuntimeException( "not implemented" );
	//	}

	@Override
	public void drawString( java.text.AttributedCharacterIterator iterator, float x, float y ) {
		throw new RuntimeException( "todo: use drawString( String, float, float ) for now" );
	}

	@Override
	public void drawGlyphVector( java.awt.font.GlyphVector g, float x, float y ) {
		int n = g.getNumGlyphs();
		this.translate( x, y );
		for( int i = 0; i < n; i++ ) {
			java.awt.Shape shapeI = g.getGlyphOutline( i );
			this.fill( shapeI );
		}
		this.translate( -x, -y );
		//throw new RuntimeException( "todo: use drawString( String, float, float ) for now" );
	}

	//	private static java.awt.Stroke s_stroke = new java.awt.BasicStroke( 0 );
	private static final double FLATNESS = 0.01;

	private void fill( java.awt.geom.PathIterator pi ) {

		class MyTessAdapter implements com.jogamp.opengl.glu.GLUtessellatorCallback {
			private com.jogamp.opengl.GL2 gl;

			public MyTessAdapter( com.jogamp.opengl.GL2 gl ) {
				this.gl = gl;
			}

			@Override
			public void begin( int primitiveType ) {
				this.gl.glBegin( primitiveType );
			}

			@Override
			public void beginData( int primitiveType, Object data ) {
			}

			@Override
			public void vertex( Object data ) {
				double[] a = (double[])data;
				this.gl.glVertex2d( a[ 0 ], a[ 1 ] );
			}

			@Override
			public void vertexData( Object arg0, Object arg1 ) {
			}

			@Override
			public void end() {
				this.gl.glEnd();
			}

			@Override
			public void endData( Object arg0 ) {
			}

			@Override
			public void edgeFlag( boolean value ) {
			}

			@Override
			public void edgeFlagData( boolean arg0, Object arg1 ) {
			}

			@Override
			public void combine( double[] coords, Object[] data, float[] weight, Object[] outData ) {
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: handle combine" );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "coords:", coords );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "data:", data );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "weight:", weight );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "outData:", outData );
				assert outData != null;
				assert outData.length > 0;
				double[] out = new double[ 3 ];
				out[ 0 ] = coords[ 0 ];
				out[ 1 ] = coords[ 1 ];
				out[ 2 ] = coords[ 2 ];
				outData[ 0 ] = out;
			}

			@Override
			public void combineData( double[] arg0, Object[] arg1, float[] arg2, Object[] arg3, Object arg4 ) {
			}

			@Override
			public void error( int n ) {

			}

			@Override
			public void errorData( int n, Object data ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "tesselator error" );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tn:", n );
				try {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tgluErrorString:", Graphics2D.this.renderContext.glu.gluErrorString( n ) );
				} catch( ArrayIndexOutOfBoundsException aioobe ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tgluErrorString: unknown" );
				}
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tdata:", data );
			}
		}

		com.jogamp.opengl.glu.GLUtessellatorCallback adapter = new MyTessAdapter( this.renderContext.gl );
		com.jogamp.opengl.glu.GLUtessellator tesselator = com.jogamp.opengl.glu.GLU.gluNewTess();
		try {
			com.jogamp.opengl.glu.GLU.gluTessCallback( tesselator, GLU.GLU_TESS_BEGIN, adapter );
			com.jogamp.opengl.glu.GLU.gluTessCallback( tesselator, GLU.GLU_TESS_VERTEX, adapter );
			com.jogamp.opengl.glu.GLU.gluTessCallback( tesselator, GLU.GLU_TESS_END, adapter );
			com.jogamp.opengl.glu.GLU.gluTessCallback( tesselator, GLU.GLU_TESS_EDGE_FLAG, adapter );
			com.jogamp.opengl.glu.GLU.gluTessCallback( tesselator, GLU.GLU_TESS_COMBINE, adapter );
			com.jogamp.opengl.glu.GLU.gluTessCallback( tesselator, GLU.GLU_TESS_ERROR, adapter );

			double[] segment = new double[ 6 ];

			//			this.renderContext.gl.glDisable( GL_CULL_FACE );
			//			try {
			com.jogamp.opengl.glu.GLU.gluBeginPolygon( tesselator );
			try {
				while( !pi.isDone() ) {
					double[] xyz = new double[ 3 ];
					switch( pi.currentSegment( segment ) ) {
					case java.awt.geom.PathIterator.SEG_MOVETO:
						com.jogamp.opengl.glu.GLU.gluTessBeginContour( tesselator );
						//note: no break
					case java.awt.geom.PathIterator.SEG_LINETO:
						xyz[ 0 ] = segment[ 0 ];
						xyz[ 1 ] = segment[ 1 ];
						com.jogamp.opengl.glu.GLU.gluTessVertex( tesselator, xyz, 0, xyz );
						break;
					case java.awt.geom.PathIterator.SEG_CLOSE:
						com.jogamp.opengl.glu.GLU.gluTessEndContour( tesselator );
						break;

					case java.awt.geom.PathIterator.SEG_QUADTO:
						throw new RuntimeException( "SEG_QUADTO: should not occur when shape.getPathIterator is passed a flatness argument" );
					case java.awt.geom.PathIterator.SEG_CUBICTO:
						throw new RuntimeException( "SEG_CUBICTO: should not occur when shape.getPathIterator is passed a flatness argument" );
					default:
						throw new RuntimeException( "unhandled segment: should not occur" );
					}
					pi.next();
				}
			} finally {
				com.jogamp.opengl.glu.GLU.gluTessEndPolygon( tesselator );
			}
			//			} finally {
			//				this.renderContext.gl.glEnable( GL_CULL_FACE );
			//			}
		} finally {
			com.jogamp.opengl.glu.GLU.gluDeleteTess( tesselator );
		}
	}

	private static final java.awt.Stroke LINE_STROKE = new java.awt.BasicStroke( 0 );

	@Override
	public void draw( java.awt.Shape s ) {
		//boolean isLine = this.stroke.equals( DEFAULT_STROKE );
		boolean isLine;
		if( this.stroke instanceof java.awt.BasicStroke ) {
			java.awt.BasicStroke basicStroke = (java.awt.BasicStroke)this.stroke;
			if( basicStroke.getDashArray() != null ) {
				//todo
				this.renderContext.gl.glLineStipple( 1, (short)0x00FF );
				this.renderContext.gl.glEnable( GL_LINE_STIPPLE );
			}
			isLine = true;
		} else {
			isLine = false;
		}

		//todo: remove
		//isLine = true;

		if( isLine ) {
			java.awt.Shape outlinesShape = LINE_STROKE.createStrokedShape( s );
			java.awt.geom.PathIterator pi = outlinesShape.getPathIterator( null, FLATNESS );
			float[] segment = new float[ 6 ];
			this.renderContext.gl.glLineWidth( ( (java.awt.BasicStroke)this.stroke ).getLineWidth() );
			try {
				while( !pi.isDone() ) {
					switch( pi.currentSegment( segment ) ) {
					case java.awt.geom.PathIterator.SEG_MOVETO:
						this.renderContext.gl.glBegin( GL_LINE_STRIP );
						//note: no break
					case java.awt.geom.PathIterator.SEG_LINETO:
						this.renderContext.gl.glVertex2f( segment[ 0 ], segment[ 1 ] );
						break;
					case java.awt.geom.PathIterator.SEG_CLOSE:
						this.renderContext.gl.glEnd();
						break;

					case java.awt.geom.PathIterator.SEG_QUADTO:
						throw new RuntimeException( "SEG_QUADTO: should not occur when shape.getPathIterator is passed a flatness argument" );
					case java.awt.geom.PathIterator.SEG_CUBICTO:
						throw new RuntimeException( "SEG_CUBICTO: should not occur when shape.getPathIterator is passed a flatness argument" );
					default:
						throw new RuntimeException( "unhandled segment: should not occur" );
					}
					pi.next();
				}
			} finally {
				this.renderContext.gl.glDisable( GL_LINE_STIPPLE );
				this.renderContext.gl.glLineWidth( 1 );
			}
		} else {
			//todo: investigate
			java.awt.Shape outlinesShape = this.stroke.createStrokedShape( s );
			java.awt.geom.PathIterator pi = outlinesShape.getPathIterator( null, FLATNESS );
			fill( pi );
		}
	}

	@Override
	public void fill( java.awt.Shape s ) {
		//		System.out.println( "fill: " + s );
		fill( s.getPathIterator( null, FLATNESS ) );
		//		System.out.println( "/fill: " + s );
	}

	@Override
	public boolean hit( java.awt.Rectangle rect, java.awt.Shape s, boolean onStroke ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public java.awt.GraphicsConfiguration getDeviceConfiguration() {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public java.awt.Composite getComposite() {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public void setComposite( java.awt.Composite comp ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public java.awt.Color getBackground() {
		return this.background;
	}

	@Override
	public void setBackground( java.awt.Color color ) {
		this.background = color;
	}

	private void glSetColor( java.awt.Color color ) {
		assert color != null;
		//this.renderContext.gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
		this.renderContext.gl.glColor4ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue(), (byte)color.getAlpha() );
		if( color.getAlpha() != 255 ) {
			this.renderContext.gl.glEnable( GL_BLEND );
			this.renderContext.gl.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
			this.renderContext.gl.glPixelTransferf( GL_ALPHA_SCALE, color.getAlpha() / 255.0f );
		} else {
			this.renderContext.gl.glDisable( GL_BLEND );
			this.renderContext.gl.glPixelTransferf( GL_ALPHA_SCALE, 1.0f );
		}
	}

	private void glSetPaint( java.awt.Paint paint ) {
		if( paint instanceof java.awt.Color ) {
			glSetColor( (java.awt.Color)paint );
		} else {
			throw new RuntimeException( "not implemented" );
		}
	}

	@Override
	public java.awt.Paint getPaint() {
		return this.paint;
	}

	@Override
	public void setPaint( java.awt.Paint paint ) {
		if( paint instanceof java.awt.Color ) {
			glSetColor( (java.awt.Color)paint );
			this.paint = paint;
		} else {
			throw new RuntimeException( "not implemented" );
		}
	}

	@Override
	public java.awt.Stroke getStroke() {
		return this.stroke;
	}

	@Override
	public void setStroke( java.awt.Stroke stroke ) {
		this.stroke = stroke;
	}

	@Override
	public Object getRenderingHint( java.awt.RenderingHints.Key hintKey ) {
		return this.renderingHints.get( hintKey );
	}

	@Override
	public java.awt.RenderingHints getRenderingHints() {
		return this.renderingHints;
	}

	@Override
	public void addRenderingHints( java.util.Map<?, ?> hints ) {
		this.renderingHints.add( new java.awt.RenderingHints( (java.util.Map<java.awt.RenderingHints.Key, ?>)hints ) );
	}

	@Override
	public void setRenderingHint( java.awt.RenderingHints.Key hintKey, Object hintValue ) {
		this.renderingHints.put( hintKey, hintValue );
	}

	@Override
	public void setRenderingHints( java.util.Map<?, ?> hints ) {
		this.renderingHints = new java.awt.RenderingHints( (java.util.Map<java.awt.RenderingHints.Key, ?>)hints );
	}

	private static double[] s_matrix = new double[ 6 ];

	private void glUpdateTransform() {
		synchronized( s_matrix ) {
			this.affineTransform.getMatrix( s_matrix );

			if( s_matrix[ 4 ] != this.affineTransform.getTranslateX() ) {
				System.err.println( "WARNING: translate x: " + s_matrix[ 4 ] + " != " + this.affineTransform.getTranslateX() );
			}
			if( s_matrix[ 5 ] != this.affineTransform.getTranslateY() ) {
				System.err.println( "WARNING: translate y: " + s_matrix[ 5 ] + " != " + this.affineTransform.getTranslateY() );
			}

			this.glTransform[ 0 ] = s_matrix[ 0 ];
			this.glTransform[ 4 ] = s_matrix[ 2 ];
			this.glTransform[ 8 ] = 0;
			this.glTransform[ 12 ] = s_matrix[ 4 ];
			this.glTransform[ 1 ] = s_matrix[ 1 ];
			this.glTransform[ 5 ] = s_matrix[ 3 ];
			this.glTransform[ 9 ] = 0;
			this.glTransform[ 13 ] = s_matrix[ 5 ];
			this.glTransform[ 2 ] = 0;
			this.glTransform[ 6 ] = 0;
			this.glTransform[ 10 ] = 1;
			this.glTransform[ 14 ] = 0;
			this.glTransform[ 3 ] = 0;
			this.glTransform[ 7 ] = 0;
			this.glTransform[ 11 ] = 0;
			this.glTransform[ 15 ] = 1;
		}
		this.renderContext.gl.glLoadMatrixd( this.glTransformBuffer );
	}

	@Override
	public void translate( int x, int y ) {
		this.affineTransform.translate( x, y );
		glUpdateTransform();
	}

	@Override
	public void translate( double x, double y ) {
		this.affineTransform.translate( x, y );
		glUpdateTransform();
	}

	@Override
	public void rotate( double theta ) {
		this.affineTransform.rotate( theta );
		glUpdateTransform();
	}

	@Override
	public void rotate( double theta, double x, double y ) {
		this.affineTransform.rotate( theta, x, y );
		glUpdateTransform();
	}

	@Override
	public void scale( double sx, double sy ) {
		this.affineTransform.scale( sx, sy );
		glUpdateTransform();
	}

	@Override
	public void shear( double shx, double shy ) {
		this.affineTransform.shear( shx, shy );
		glUpdateTransform();
	}

	@Override
	public void transform( java.awt.geom.AffineTransform Tx ) {
		this.affineTransform.concatenate( Tx );
		glUpdateTransform();
	}

	@Override
	public java.awt.geom.AffineTransform getTransform() {
		return new java.awt.geom.AffineTransform( this.affineTransform );
	}

	@Override
	public void setTransform( java.awt.geom.AffineTransform Tx ) {
		this.affineTransform.setTransform( Tx );
		glUpdateTransform();
	}

	@Override
	public void clip( java.awt.Shape s ) {
		throw new RuntimeException( "not implemented" );
	}

	@Override
	public java.awt.font.FontRenderContext getFontRenderContext() {
		boolean isAntiAliased = getRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING ) == java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
		boolean usesFractionalMetrics = getRenderingHint( java.awt.RenderingHints.KEY_FRACTIONALMETRICS ) == java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON;
		return new java.awt.font.FontRenderContext( getTransform(), isAntiAliased, usesFractionalMetrics );
	}

	// edu.cmu.cs.dennisc.renderer.Graphics2D

	private static final class ReferencedObject<E> {
		private final E object;
		private int referenceCount;

		public ReferencedObject( E object, int referenceCount ) {
			this.object = object;
			this.referenceCount = referenceCount;
		}

		public E getObject() {
			return this.object;
		}

		public boolean isReferenced() {
			return this.referenceCount > 0;
		}

		public void addReference() {
			this.referenceCount++;
		}

		public void removeReference() {
			this.referenceCount--;
		}
	}

	private static final class TextRendererHolder {
		private com.jogamp.opengl.util.awt.TextRenderer textRenderer;
		private com.jogamp.opengl.GL gl;

		public com.jogamp.opengl.util.awt.TextRenderer getTextRenderer( java.awt.Font font, com.jogamp.opengl.GL gl ) {
			if( this.textRenderer != null ) {
				if( this.gl == gl ) {
					//pass
				} else {
					this.textRenderer.dispose();
					this.textRenderer = null;
				}
			}
			if( this.textRenderer != null ) {
				//pass
			} else {
				this.textRenderer = new com.jogamp.opengl.util.awt.TextRenderer( font );
			}
			return this.textRenderer;
		}

		public void dispose() {
			if( this.textRenderer != null ) {
				this.textRenderer.dispose();
			}
			this.textRenderer = null;
			this.gl = null;
		}
	}

	private final java.util.Map<java.awt.Font, ReferencedObject<TextRendererHolder>> activeFontToTextRendererMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<java.awt.Font, ReferencedObject<TextRendererHolder>> forgottenFontToTextRendererMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	@Override
	public boolean isRemembered( java.awt.Font font ) {
		return this.activeFontToTextRendererMap.containsKey( font );
	}

	@Override
	public void remember( java.awt.Font font ) {
		ReferencedObject<TextRendererHolder> referencedObject = this.activeFontToTextRendererMap.get( font );
		if( referencedObject != null ) {
			//pass
		} else {
			referencedObject = this.forgottenFontToTextRendererMap.get( font );
			if( referencedObject != null ) {
				this.forgottenFontToTextRendererMap.remove( font );
			} else {
				referencedObject = new ReferencedObject<TextRendererHolder>( new TextRendererHolder(), 0 );
			}
			this.activeFontToTextRendererMap.put( font, referencedObject );
		}
		referencedObject.addReference();
	}

	@Override
	public java.awt.geom.Rectangle2D getBounds( String text, java.awt.Font font ) {
		ReferencedObject<TextRendererHolder> referencedObject = this.activeFontToTextRendererMap.get( font );
		assert referencedObject != null;
		assert referencedObject.isReferenced();
		java.awt.geom.Rectangle2D bounds = referencedObject.getObject().getTextRenderer( font, this.renderContext.gl ).getBounds( text );
		assert bounds != null;
		return bounds;
	}

	@Override
	public void forget( java.awt.Font font ) {
		ReferencedObject<TextRendererHolder> referencedObject = this.activeFontToTextRendererMap.get( font );
		assert referencedObject != null;
		assert referencedObject.isReferenced();
		referencedObject.removeReference();
		if( referencedObject.isReferenced() ) {
			//pass
		} else {
			this.activeFontToTextRendererMap.remove( font );
			this.forgottenFontToTextRendererMap.put( font, referencedObject );
		}
	}

	@Override
	public void disposeForgottenFonts() {
		synchronized( this.forgottenFontToTextRendererMap ) {
			for( java.awt.Font font : this.forgottenFontToTextRendererMap.keySet() ) {
				ReferencedObject<TextRendererHolder> referencedObject = this.forgottenFontToTextRendererMap.get( font );
				referencedObject.getObject().dispose();
			}
			this.forgottenFontToTextRendererMap.clear();
		}
	}

	private java.util.Map<java.awt.Image, edu.cmu.cs.dennisc.image.ImageGenerator> imageToImageGeneratorMap = new java.util.HashMap<java.awt.Image, edu.cmu.cs.dennisc.image.ImageGenerator>();

	private java.util.Map<edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject<Pixels>> activeImageGeneratorToPixelsMap = new java.util.HashMap<edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject<Pixels>>();
	private java.util.Map<edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject<Pixels>> forgottenImageGeneratorToPixelsMap = new java.util.HashMap<edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject<Pixels>>();

	@Override
	public boolean isRemembered( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		return this.activeImageGeneratorToPixelsMap.containsKey( imageGenerator );
	}

	@Override
	public void remember( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		assert imageGenerator != null;
		ReferencedObject<Pixels> referencedObject = this.activeImageGeneratorToPixelsMap.get( imageGenerator );
		if( referencedObject != null ) {
			//pass
		} else {
			referencedObject = this.forgottenImageGeneratorToPixelsMap.get( imageGenerator );
			if( referencedObject != null ) {
				this.forgottenImageGeneratorToPixelsMap.remove( imageGenerator );
			} else {
				if( imageGenerator instanceof edu.cmu.cs.dennisc.texture.Texture ) {
					//					sgTexture.addReleaseListener( new edu.cmu.cs.dennisc.pattern.event.ReleaseListener() {
					//					public void releasing( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
					//					}
					//					public void released( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
					//						forget( (edu.cmu.cs.dennisc.scenegraph.Texture)releaseEvent.getReleasableSource() );
					//					};
					//				} );
					edu.cmu.cs.dennisc.texture.Texture texture = (edu.cmu.cs.dennisc.texture.Texture)imageGenerator;

					if( texture instanceof edu.cmu.cs.dennisc.texture.CustomTexture ) {
						( (edu.cmu.cs.dennisc.texture.CustomTexture)texture ).layoutIfNecessary( this );
					}

					Pixels pixels = new Pixels( (edu.cmu.cs.dennisc.texture.Texture)imageGenerator );
					referencedObject = new ReferencedObject<Pixels>( pixels, 0 );

				} else {
					throw new RuntimeException( "TODO" );
				}
			}
			this.activeImageGeneratorToPixelsMap.put( imageGenerator, referencedObject );
		}
		referencedObject.addReference();
	}

	@Override
	public void paint( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator, float x, float y, float alpha ) {
		ReferencedObject<Pixels> referencedObject = this.activeImageGeneratorToPixelsMap.get( imageGenerator );
		assert referencedObject != null;
		assert referencedObject.isReferenced();

		Pixels pixels = referencedObject.getObject();
		if( imageGenerator.isAnimated() ) {
			pixels.textureChanged( null );
		}
		this.renderContext.gl.glEnable( GL_BLEND );
		this.renderContext.gl.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		this.renderContext.gl.glPixelTransferf( GL_ALPHA_SCALE, alpha );

		int xPixel = (int)x;
		int yPixel = (int)y + pixels.getHeight();
		this.renderContext.gl.glRasterPos2i( 0, 0 );
		this.renderContext.gl.glBitmap( 0, 0, 0, 0, xPixel, -yPixel, null );
		this.renderContext.gl.glDrawPixels( pixels.getWidth(), pixels.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, pixels.getRGBA() );
		this.renderContext.gl.glDisable( GL_BLEND );
		this.renderContext.gl.glPixelTransferf( GL_ALPHA_SCALE, 1.0f );
	}

	@Override
	public void forget( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		ReferencedObject<Pixels> referencedObject = this.activeImageGeneratorToPixelsMap.get( imageGenerator );
		assert referencedObject != null;
		assert referencedObject.isReferenced();
		referencedObject.removeReference();
		if( referencedObject.isReferenced() ) {
			//pass
		} else {
			this.activeImageGeneratorToPixelsMap.remove( imageGenerator );
			this.forgottenImageGeneratorToPixelsMap.put( imageGenerator, referencedObject );
		}
	}

	private void disposeImageGenerator( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		ReferencedObject<Pixels> referencedObject = this.forgottenImageGeneratorToPixelsMap.get( imageGenerator );
		referencedObject.getObject().release();
	}

	@Override
	public void disposeForgottenImageGenerators() {
		synchronized( this.forgottenImageGeneratorToPixelsMap ) {
			for( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator : this.forgottenImageGeneratorToPixelsMap.keySet() ) {
				disposeImageGenerator( imageGenerator );
			}
			this.forgottenFontToTextRendererMap.clear();
		}
	}

	//	class DefaultImageGenerator implements edu.cmu.cs.dennisc.image.ImageGenerator {
	//		private java.awt.Image this.image;
	//		public DefaultImageGenerator( java.awt.Image image ) {
	//			this.image = image;
	//		}
	//		@Override
	//		public int getWidth() {
	//			return edu.cmu.cs.dennisc.image.ImageUtilities.getWidth( this.image );
	//		}
	//		@Override
	//		public int getHeight() {
	//			return edu.cmu.cs.dennisc.image.ImageUtilities.getHeight( this.image );
	//		}		
	//		@Override
	//		public edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy getMipMapGenerationPolicy() {
	//			return edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy.PAINT_ONLY_HIGHEST_LEVEL_THEN_SCALE_REMAINING;
	//		}
	//		@Override
	//		public boolean isAnimated() {
	//			return false;
	//		}
	//		@Override
	//		public boolean isMipMappingDesired() {
	//			return false;
	//		}
	//		@Override
	//		public boolean isPotentiallyAlphaBlended() {
	//			return false;
	//		}
	//		@Override
	//		public void paint( java.awt.Graphics2D g, int width, int height ) {
	//			g.drawImage( this.image, 0, 0, null );
	//		}
	//	}

	@Override
	public boolean isRemembered( java.awt.Image image ) {
		edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator = this.imageToImageGeneratorMap.get( image );
		if( imageGenerator != null ) {
			return isRemembered( imageGenerator );
		} else {
			return false;
		}
	}

	@Override
	public void remember( java.awt.Image image ) {
		edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator = this.imageToImageGeneratorMap.get( image );
		if( imageGenerator != null ) {
			//pass
		} else {
			if( image instanceof java.awt.image.BufferedImage ) {
				java.awt.image.BufferedImage bufferedImage = (java.awt.image.BufferedImage)image;
				edu.cmu.cs.dennisc.texture.BufferedImageTexture bufferedImageTexture = new edu.cmu.cs.dennisc.texture.BufferedImageTexture();
				bufferedImageTexture.setBufferedImage( bufferedImage );
				bufferedImageTexture.setMipMappingDesired( false );
				imageGenerator = bufferedImageTexture;
			} else {
				throw new RuntimeException( "todo" );
			}
			this.imageToImageGeneratorMap.put( image, imageGenerator );
		}
		remember( imageGenerator );
	}

	@Override
	public void forget( java.awt.Image image ) {
		edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator = this.imageToImageGeneratorMap.get( image );
		forget( imageGenerator );
	}

	@Override
	public void disposeForgottenImages() {
		//todo
		for( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator : this.imageToImageGeneratorMap.values() ) {
			if( this.forgottenImageGeneratorToPixelsMap.containsKey( imageGenerator ) ) {
				disposeImageGenerator( imageGenerator );
				this.forgottenImageGeneratorToPixelsMap.remove( imageGenerator );
			}
		}
	}

	// edu.cmu.cs.dennisc.lookingglass.opengl.Graphics2D

	public com.jogamp.opengl.GL getGL() {
		return this.renderContext.gl;
	}
}
