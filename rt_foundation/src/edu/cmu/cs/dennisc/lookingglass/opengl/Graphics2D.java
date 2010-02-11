/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.lookingglass.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 * @author Dennis Cosgrove
 */
public class Graphics2D extends edu.cmu.cs.dennisc.lookingglass.Graphics2D {
	private static edu.cmu.cs.dennisc.math.SineCosineCache s_sineCosineCache = new edu.cmu.cs.dennisc.math.SineCosineCache( 8 );

	private static final java.awt.Paint DEFAULT_PAINT = java.awt.Color.BLACK;
	private static final java.awt.Color DEFAULT_BACKGROUND = java.awt.Color.WHITE;
	private static final java.awt.Font DEFAULT_FONT = new java.awt.Font( null, java.awt.Font.PLAIN, 12 );
	private static final java.awt.Stroke DEFAULT_STROKE = new java.awt.BasicStroke( 1 );

	private RenderContext m_renderContext;
	private java.awt.Paint m_paint = DEFAULT_PAINT;
	private java.awt.Color m_background = DEFAULT_BACKGROUND;
	private java.awt.Font m_font = DEFAULT_FONT;
	private java.awt.Stroke m_stroke = DEFAULT_STROKE;

	private java.awt.RenderingHints m_renderingHints;

	private java.awt.geom.AffineTransform m_affineTransform = new java.awt.geom.AffineTransform();
	private double[] m_glTransform = new double[ 16 ];
	private java.nio.DoubleBuffer m_glTransformBuffer = java.nio.DoubleBuffer.wrap( m_glTransform );

	private int m_width = -1;
	private int m_height = -1;

	public Graphics2D( RenderContext renderContext ) {
		assert renderContext != null;
		m_renderContext = renderContext;
		java.util.Map< java.awt.RenderingHints.Key, Object > map = new java.util.HashMap< java.awt.RenderingHints.Key, Object >();
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
		map.put( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT );
		m_renderingHints = new java.awt.RenderingHints( map );
	}

	public void initialize( int width, int height ) {
		assert m_renderContext.gl != null;
		assert m_renderContext.glu != null;
		m_width = width;
		m_height = height;

		setPaint( DEFAULT_PAINT );
		setBackground( DEFAULT_BACKGROUND );
		setFont( DEFAULT_FONT );

		m_renderContext.gl.glMatrixMode( GL.GL_PROJECTION );
		m_renderContext.gl.glPushMatrix();
		m_renderContext.gl.glLoadIdentity();
		m_renderContext.gl.glOrtho( 0, m_width - 1, m_height - 1, 0, -1, 1 );
		//m_renderContext.gl.glViewport( 0, 0, width, height );
		m_renderContext.gl.glMatrixMode( GL.GL_MODELVIEW );
		m_renderContext.gl.glPushMatrix();
		m_renderContext.gl.glLoadIdentity();

		m_affineTransform.setToIdentity();

		m_renderContext.gl.glDisable( GL.GL_DEPTH_TEST );
		m_renderContext.gl.glDisable( GL.GL_LIGHTING );
		m_renderContext.gl.glDisable( GL.GL_CULL_FACE );

		m_renderContext.setDiffuseColorTextureAdapter( null, false );
		m_renderContext.setBumpTextureAdapter( null );

		m_renderContext.gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_FILL );
	}

	// java.awt.Graphics

	@Override
	public void dispose() {
		m_renderContext.gl.glFlush();
		if( isValid() ) {
			m_renderContext.gl.glMatrixMode( GL.GL_MODELVIEW );
			m_renderContext.gl.glPopMatrix();
			m_renderContext.gl.glMatrixMode( GL.GL_PROJECTION );
			m_renderContext.gl.glPopMatrix();
			m_width = -1;
			m_height = -1;
		}
	}

	@Override
	public boolean isValid() {
		return m_width != -1 && m_height != -1;
	}

	@Override
	public java.awt.Graphics create() {
		throw new RuntimeException( "not implemented" );
	}
	@Override
	public java.awt.Color getColor() {
		if( m_paint instanceof java.awt.Color ) {
			return (java.awt.Color)m_paint;
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
		return m_font;
	}
	@Override
	public void setFont( java.awt.Font font ) {
		m_font = font;
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
		m_renderContext.gl.glBegin( GL.GL_LINES );
		m_renderContext.gl.glVertex2i( x1, y1 );
		m_renderContext.gl.glVertex2i( x2, y2 );
		m_renderContext.gl.glEnd();
	}
	@Override
	public void fillRect( int x, int y, int width, int height ) {
		m_renderContext.gl.glBegin( GL.GL_POLYGON );
		m_renderContext.gl.glVertex2i( x, y );
		m_renderContext.gl.glVertex2i( x + width, y );
		m_renderContext.gl.glVertex2i( x + width, y + height );
		m_renderContext.gl.glVertex2i( x, y + height );
		m_renderContext.gl.glEnd();
	}
	@Override
	public void clearRect( int x, int y, int width, int height ) {
		glSetColor( m_background );
		m_renderContext.gl.glBegin( GL.GL_POLYGON );
		m_renderContext.gl.glVertex2i( x, y );
		m_renderContext.gl.glVertex2i( x + width, y );
		m_renderContext.gl.glVertex2i( x + width, y + height );
		m_renderContext.gl.glVertex2i( x, y + height );
		m_renderContext.gl.glEnd();
		glSetPaint( m_paint );
	}

	private void glQuarterOval( double centerX, double centerY, double radiusX, double radiusY, int quadrant ) {
		int n = s_sineCosineCache.cosines.length;
		int max = n - 1;
		for( int lcv = 0; lcv < n; lcv++ ) {
			int i = max - lcv;
			double cos = s_sineCosineCache.getCosine( quadrant, i );
			double sin = s_sineCosineCache.getSine( quadrant, i );
			m_renderContext.gl.glVertex2d( centerX + cos * radiusX, centerY + sin * radiusY );
		}
	}

	private void glRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		//int x0 = x;
		int x1 = x + arcWidth;
		int x2 = x + width - arcWidth;
		//int x3 = x+width;

		//int y0 = y;
		int y1 = y + arcHeight;
		int y2 = y + height - arcHeight;
		//int y3 = y+height;

		glQuarterOval( x1, y2, arcWidth, arcHeight, 1 );
		glQuarterOval( x2, y2, arcWidth, arcHeight, 0 );
		glQuarterOval( x2, y1, arcWidth, arcHeight, 3 );
		glQuarterOval( x1, y1, arcWidth, arcHeight, 2 );
	}
	@Override
	public void drawRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		m_renderContext.gl.glBegin( GL.GL_LINE_LOOP );
		glRoundRect( x, y, width, height, arcWidth, arcHeight );
		m_renderContext.gl.glEnd();
	}
	@Override
	public void fillRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		m_renderContext.gl.glBegin( GL.GL_TRIANGLE_FAN );
		glRoundRect( x, y, width, height, arcWidth, arcHeight );
		m_renderContext.gl.glEnd();
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
		m_renderContext.gl.glBegin( GL.GL_LINE_LOOP );
		glOval( x, y, width, height );
		m_renderContext.gl.glEnd();
	}
	@Override
	public void fillOval( int x, int y, int width, int height ) {
		m_renderContext.gl.glBegin( GL.GL_TRIANGLE_FAN );
		glOval( x, y, width, height );
		m_renderContext.gl.glEnd();
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
			m_renderContext.gl.glVertex2i( xPoints[ i ], yPoints[ i ] );
		}
	}
	@Override
	public void drawPolyline( int xPoints[], int yPoints[], int nPoints ) {
		m_renderContext.gl.glBegin( GL.GL_LINE_STRIP );
		glPoly( xPoints, yPoints, nPoints );
		m_renderContext.gl.glEnd();
	}
	@Override
	public void drawPolygon( int xPoints[], int yPoints[], int nPoints ) {
		m_renderContext.gl.glBegin( GL.GL_LINE_LOOP );
		glPoly( xPoints, yPoints, nPoints );
		m_renderContext.gl.glEnd();
	}
	@Override
	public void fillPolygon( int xPoints[], int yPoints[], int nPoints ) {
		m_renderContext.gl.glBegin( GL.GL_POLYGON );
		glPoly( xPoints, yPoints, nPoints );
		m_renderContext.gl.glEnd();
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
			this.paint( m_imageToImageGeneratorMap.get( image ), x, y, 1.0f );
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
		ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > referencedObject = m_activeFontToTextRendererMap.get( m_font );
		//todo?
		if( referencedObject == null ) {
			remember( m_font );
			referencedObject = m_activeFontToTextRendererMap.get( m_font );
		}
		assert referencedObject != null;
		com.sun.opengl.util.j2d.TextRenderer glTextRenderer = referencedObject.getObject();
		glTextRenderer.beginRendering( m_width, m_height );
		if( m_paint instanceof java.awt.Color ) {
			java.awt.Color color = (java.awt.Color)m_paint;
			glTextRenderer.setColor( color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f );
		} else {
			//todo?
		}
		int xPixel = (int)(x + m_affineTransform.getTranslateX());
		int yPixel = (int)(y + m_affineTransform.getTranslateY());
		glTextRenderer.draw( text, xPixel, m_height - yPixel );
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
		for( int i=0; i<n; i++ ) {
			java.awt.Shape shapeI = g.getGlyphOutline( i );
			this.fill( shapeI );
		}
		this.translate( -x, -y );
		//throw new RuntimeException( "todo: use drawString( String, float, float ) for now" );
	}

	//	private static java.awt.Stroke s_stroke = new java.awt.BasicStroke( 0 );
	private static final double FLATNESS = 0.01;

	private void fill( java.awt.geom.PathIterator pi ) {
		
		class MyTessAdapter implements javax.media.opengl.glu.GLUtessellatorCallback {
			private GL m_gl;

			public MyTessAdapter( GL gl ) {
				m_gl = gl;
			}
			public void begin( int primitiveType ) {
				m_gl.glBegin( primitiveType );
			}
			public void beginData( int primitiveType, Object data ) {
			}
			public void vertex( Object data ) {
				double[] a = (double[])data;
				m_gl.glVertex2d( a[ 0 ], a[ 1 ] );
			}
			public void vertexData( Object arg0, Object arg1 ) {
			}
			public void end() {
				m_gl.glEnd();
			}
			public void endData( Object arg0 ) {
			}

			public void edgeFlag( boolean value ) {
			}
			public void edgeFlagData( boolean arg0, Object arg1 ) {
			}

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
			public void combineData( double[] arg0, Object[] arg1, float[] arg2, Object[] arg3, Object arg4 ) {
			}
			
			public void error(int n) {
				
			}
			public void errorData( int n, Object data ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "tesselator error" );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tn:", n );
				try {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tgluErrorString:", m_renderContext.glu.gluErrorString( n ) );
				} catch( ArrayIndexOutOfBoundsException aioobe ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tgluErrorString: unknown" );
				}
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "\tdata:", data );
			}
		}

		javax.media.opengl.glu.GLUtessellatorCallback adapter = new MyTessAdapter( m_renderContext.gl );
		javax.media.opengl.glu.GLUtessellator tesselator = m_renderContext.glu.gluNewTess();
		try {
			m_renderContext.glu.gluTessCallback( tesselator, GLU.GLU_TESS_BEGIN, adapter );
			m_renderContext.glu.gluTessCallback( tesselator, GLU.GLU_TESS_VERTEX, adapter );
			m_renderContext.glu.gluTessCallback( tesselator, GLU.GLU_TESS_END, adapter );
			m_renderContext.glu.gluTessCallback( tesselator, GLU.GLU_TESS_EDGE_FLAG, adapter );
			m_renderContext.glu.gluTessCallback( tesselator, GLU.GLU_TESS_COMBINE, adapter );
			m_renderContext.glu.gluTessCallback( tesselator, GLU.GLU_TESS_ERROR, adapter );

			double[] segment = new double[ 6 ];

//			m_renderContext.gl.glDisable( GL.GL_CULL_FACE );
//			try {
				m_renderContext.glu.gluBeginPolygon( tesselator );
				try {
					while( !pi.isDone() ) {
						double[] xyz = new double[ 3 ];
						switch( pi.currentSegment( segment ) ) {
						case java.awt.geom.PathIterator.SEG_MOVETO:
							m_renderContext.glu.gluTessBeginContour( tesselator );
							//note: no break
						case java.awt.geom.PathIterator.SEG_LINETO:
							xyz[ 0 ] = segment[ 0 ];
							xyz[ 1 ] = segment[ 1 ];
							m_renderContext.glu.gluTessVertex( tesselator, xyz, 0, xyz );
							break;
						case java.awt.geom.PathIterator.SEG_CLOSE:
							m_renderContext.glu.gluTessEndContour( tesselator );
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
					m_renderContext.glu.gluTessEndPolygon( tesselator );
				}
//			} finally {
//				m_renderContext.gl.glEnable( GL.GL_CULL_FACE );
//			}
		} finally {
			m_renderContext.glu.gluDeleteTess( tesselator );
		}
	}

	private static final java.awt.Stroke LINE_STROKE = new java.awt.BasicStroke( 0 );

	@Override
	public void draw( java.awt.Shape s ) {
		//boolean isLine = m_stroke.equals( DEFAULT_STROKE );
		boolean isLine;
		if( m_stroke instanceof java.awt.BasicStroke ) {
			java.awt.BasicStroke basicStroke = (java.awt.BasicStroke)m_stroke;
			if( basicStroke.getDashArray() != null ) {
				//todo
				m_renderContext.gl.glLineStipple( 1, (short)0x00FF );
				m_renderContext.gl.glEnable( GL.GL_LINE_STIPPLE );
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
			m_renderContext.gl.glLineWidth( ((java.awt.BasicStroke)m_stroke).getLineWidth() );
			try {
				while( !pi.isDone() ) {
					switch( pi.currentSegment( segment ) ) {
					case java.awt.geom.PathIterator.SEG_MOVETO:
						m_renderContext.gl.glBegin( GL.GL_LINE_STRIP );
					case java.awt.geom.PathIterator.SEG_LINETO:
						m_renderContext.gl.glVertex2f( segment[ 0 ], segment[ 1 ] );
						break;
					case java.awt.geom.PathIterator.SEG_CLOSE:
						m_renderContext.gl.glEnd();
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
				m_renderContext.gl.glDisable( GL.GL_LINE_STIPPLE );
				m_renderContext.gl.glLineWidth( 1 );
			}
		} else {
			//todo: investigate
			java.awt.Shape outlinesShape = m_stroke.createStrokedShape( s );
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
		return m_background;
	}
	@Override
	public void setBackground( java.awt.Color color ) {
		m_background = color;
	}

	private void glSetColor( java.awt.Color color ) {
		assert color != null;
		//m_renderContext.gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
		m_renderContext.gl.glColor4ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue(), (byte)color.getAlpha() );
		if( color.getAlpha() != 255 ) {
			m_renderContext.gl.glEnable( javax.media.opengl.GL.GL_BLEND );
			m_renderContext.gl.glBlendFunc( javax.media.opengl.GL.GL_SRC_ALPHA, javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA );
			m_renderContext.gl.glPixelTransferf( GL.GL_ALPHA_SCALE, color.getAlpha() / 255.0f );
		} else {
			m_renderContext.gl.glDisable( javax.media.opengl.GL.GL_BLEND );
			m_renderContext.gl.glPixelTransferf( GL.GL_ALPHA_SCALE, 1.0f );
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
		return m_paint;
	}
	@Override
	public void setPaint( java.awt.Paint paint ) {
		if( paint instanceof java.awt.Color ) {
			glSetColor( (java.awt.Color)paint );
			m_paint = paint;
		} else {
			throw new RuntimeException( "not implemented" );
		}
	}

	@Override
	public java.awt.Stroke getStroke() {
		return m_stroke;
	}
	@Override
	public void setStroke( java.awt.Stroke stroke ) {
		m_stroke = stroke;
	}

	@Override
	public Object getRenderingHint( java.awt.RenderingHints.Key hintKey ) {
		return m_renderingHints.get( hintKey );
	}
	@Override
	public java.awt.RenderingHints getRenderingHints() {
		return m_renderingHints;
	}
	@Override
	public void addRenderingHints( java.util.Map hints ) {
		m_renderingHints.add( new java.awt.RenderingHints( hints ) );
	}
	@Override
	public void setRenderingHint( java.awt.RenderingHints.Key hintKey, Object hintValue ) {
		m_renderingHints.put( hintKey, hintValue );
	}
	@Override
	public void setRenderingHints( java.util.Map hints ) {
		m_renderingHints = new java.awt.RenderingHints( hints );
	}

	private static double[] s_matrix = new double[ 6 ];

	private void glUpdateTransform() {
		synchronized( s_matrix ) {
			m_affineTransform.getMatrix( s_matrix );

			if( s_matrix[ 4 ] != m_affineTransform.getTranslateX() ) {
				System.err.println( "WARNING: translate x: " + s_matrix[ 4 ] + " != " + m_affineTransform.getTranslateX() );
			}
			if( s_matrix[ 5 ] != m_affineTransform.getTranslateY() ) {
				System.err.println( "WARNING: translate y: " + s_matrix[ 5 ] + " != " + m_affineTransform.getTranslateY() );
			}

			m_glTransform[ 0 ] = s_matrix[ 0 ];
			m_glTransform[ 4 ] = s_matrix[ 2 ];
			m_glTransform[ 8 ] = 0;
			m_glTransform[ 12 ] = s_matrix[ 4 ];
			m_glTransform[ 1 ] = s_matrix[ 1 ];
			m_glTransform[ 5 ] = s_matrix[ 3 ];
			m_glTransform[ 9 ] = 0;
			m_glTransform[ 13 ] = s_matrix[ 5 ];
			m_glTransform[ 2 ] = 0;
			m_glTransform[ 6 ] = 0;
			m_glTransform[ 10 ] = 1;
			m_glTransform[ 14 ] = 0;
			m_glTransform[ 3 ] = 0;
			m_glTransform[ 7 ] = 0;
			m_glTransform[ 11 ] = 0;
			m_glTransform[ 15 ] = 1;
		}
		m_renderContext.gl.glLoadMatrixd( m_glTransformBuffer );
	}
	@Override
	public void translate( int x, int y ) {
		m_affineTransform.translate( x, y );
		glUpdateTransform();
	}
	@Override
	public void translate( double x, double y ) {
		m_affineTransform.translate( x, y );
		glUpdateTransform();
	}

	@Override
	public void rotate( double theta ) {
		m_affineTransform.rotate( theta );
		glUpdateTransform();
	}
	@Override
	public void rotate( double theta, double x, double y ) {
		m_affineTransform.rotate( theta, x, y );
		glUpdateTransform();
	}

	@Override
	public void scale( double sx, double sy ) {
		m_affineTransform.scale( sx, sy );
		glUpdateTransform();
	}
	@Override
	public void shear( double shx, double shy ) {
		m_affineTransform.shear( shx, shy );
		glUpdateTransform();
	}

	@Override
	public void transform( java.awt.geom.AffineTransform Tx ) {
		m_affineTransform.concatenate( Tx );
		glUpdateTransform();
	}
	@Override
	public java.awt.geom.AffineTransform getTransform() {
		return new java.awt.geom.AffineTransform( m_affineTransform );
	}
	@Override
	public void setTransform( java.awt.geom.AffineTransform Tx ) {
		m_affineTransform.setTransform( Tx );
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

	// edu.cmu.cs.dennisc.lookingglass.Graphics2D

	class ReferencedObject<E extends Object> {
		private E m_object;
		private int m_referenceCount;

		public ReferencedObject( E object, int referenceCount ) {
			m_object = object;
			m_referenceCount = referenceCount;
		}
		public E getObject() {
			return m_object;
		}
		public boolean isReferenced() {
			return m_referenceCount > 0;
		}
		public void addReference() {
			m_referenceCount++;
		}
		public void removeReference() {
			m_referenceCount--;
		}
	}

	private java.util.Map< java.awt.Font, ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > > m_activeFontToTextRendererMap = new java.util.HashMap< java.awt.Font, ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > >();
	private java.util.Map< java.awt.Font, ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > > m_forgottenFontToTextRendererMap = new java.util.HashMap< java.awt.Font, ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > >();

	@Override
	public boolean isRemembered( java.awt.Font font ) {
		return m_activeFontToTextRendererMap.containsKey( font );
	}
	@Override
	public void remember( java.awt.Font font ) {
		ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > referencedObject = m_activeFontToTextRendererMap.get( font );
		if( referencedObject != null ) {
			//pass
		} else {
			referencedObject = m_forgottenFontToTextRendererMap.get( font );
			if( referencedObject != null ) {
				m_forgottenFontToTextRendererMap.remove( font );
			} else {
				com.sun.opengl.util.j2d.TextRenderer glTextRenderer = new com.sun.opengl.util.j2d.TextRenderer( font );
				referencedObject = new ReferencedObject< com.sun.opengl.util.j2d.TextRenderer >( glTextRenderer, 0 );
			}
			m_activeFontToTextRendererMap.put( font, referencedObject );
		}
		referencedObject.addReference();
	}
	@Override
	public java.awt.geom.Rectangle2D getBounds( String text, java.awt.Font font ) {
		ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > referencedObject = m_activeFontToTextRendererMap.get( font );
		assert referencedObject != null;
		assert referencedObject.isReferenced();
		java.awt.geom.Rectangle2D bounds = referencedObject.getObject().getBounds( text );
		assert bounds != null;
		return bounds;
	}

	@Override
	public void forget( java.awt.Font font ) {
		ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > referencedObject = m_activeFontToTextRendererMap.get( font );
		assert referencedObject != null;
		assert referencedObject.isReferenced();
		referencedObject.removeReference();
		if( referencedObject.isReferenced() ) {
			//pass
		} else {
			m_activeFontToTextRendererMap.remove( font );
			m_forgottenFontToTextRendererMap.put( font, referencedObject );
		}
	}
	@Override
	public void disposeForgottenFonts() {
		synchronized( m_forgottenFontToTextRendererMap ) {
			for( java.awt.Font font : m_forgottenFontToTextRendererMap.keySet() ) {
				ReferencedObject< com.sun.opengl.util.j2d.TextRenderer > referencedObject = m_forgottenFontToTextRendererMap.get( font );
				referencedObject.getObject().dispose();
			}
			m_forgottenFontToTextRendererMap.clear();
		}
	}

	private java.util.Map< java.awt.Image, edu.cmu.cs.dennisc.image.ImageGenerator > m_imageToImageGeneratorMap = new java.util.HashMap< java.awt.Image, edu.cmu.cs.dennisc.image.ImageGenerator >();

	private java.util.Map< edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject< Pixels > > m_activeImageGeneratorToPixelsMap = new java.util.HashMap< edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject< Pixels > >();
	private java.util.Map< edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject< Pixels > > m_forgottenImageGeneratorToPixelsMap = new java.util.HashMap< edu.cmu.cs.dennisc.image.ImageGenerator, ReferencedObject< Pixels > >();

	@Override
	public boolean isRemembered( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		return m_activeImageGeneratorToPixelsMap.containsKey( imageGenerator );
	}
	@Override
	public void remember( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		assert imageGenerator != null;
		ReferencedObject< Pixels > referencedObject = m_activeImageGeneratorToPixelsMap.get( imageGenerator );
		if( referencedObject != null ) {
			//pass
		} else {
			referencedObject = m_forgottenImageGeneratorToPixelsMap.get( imageGenerator );
			if( referencedObject != null ) {
				m_forgottenImageGeneratorToPixelsMap.remove( imageGenerator );
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
						((edu.cmu.cs.dennisc.texture.CustomTexture)texture).layoutIfNecessary( this );
					}

					Pixels pixels = new Pixels( (edu.cmu.cs.dennisc.texture.Texture)imageGenerator );
					referencedObject = new ReferencedObject< Pixels >( pixels, 0 );

				} else {
					throw new RuntimeException( "TODO" );
				}
			}
			m_activeImageGeneratorToPixelsMap.put( imageGenerator, referencedObject );
		}
		referencedObject.addReference();
	}

	@Override
	public void paint( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator, float x, float y, float alpha ) {
		ReferencedObject< Pixels > referencedObject = m_activeImageGeneratorToPixelsMap.get( imageGenerator );
		assert referencedObject != null;
		assert referencedObject.isReferenced();

		Pixels pixels = referencedObject.getObject();
		if( imageGenerator.isAnimated() ) {
			pixels.textureChanged( null );
		}
		m_renderContext.gl.glEnable( javax.media.opengl.GL.GL_BLEND );
		m_renderContext.gl.glBlendFunc( javax.media.opengl.GL.GL_SRC_ALPHA, javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA );
		m_renderContext.gl.glPixelTransferf( GL.GL_ALPHA_SCALE, alpha );

		int xPixel = (int)x;
		int yPixel = (int)y + pixels.getHeight();
		m_renderContext.gl.glRasterPos2i( 0, 0 );
		m_renderContext.gl.glBitmap( 0, 0, 0, 0, xPixel, -yPixel, null );
		m_renderContext.gl.glDrawPixels( pixels.getWidth(), pixels.getHeight(), javax.media.opengl.GL.GL_RGBA, javax.media.opengl.GL.GL_UNSIGNED_BYTE, pixels.getRGBA() );
		m_renderContext.gl.glDisable( javax.media.opengl.GL.GL_BLEND );
		m_renderContext.gl.glPixelTransferf( GL.GL_ALPHA_SCALE, 1.0f );
	}
	@Override
	public void forget( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		ReferencedObject< Pixels > referencedObject = m_activeImageGeneratorToPixelsMap.get( imageGenerator );
		assert referencedObject != null;
		assert referencedObject.isReferenced();
		referencedObject.removeReference();
		if( referencedObject.isReferenced() ) {
			//pass
		} else {
			m_activeImageGeneratorToPixelsMap.remove( imageGenerator );
			m_forgottenImageGeneratorToPixelsMap.put( imageGenerator, referencedObject );
		}
	}

	private void disposeImageGenerator( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ) {
		ReferencedObject< Pixels > referencedObject = m_forgottenImageGeneratorToPixelsMap.get( imageGenerator );
		referencedObject.getObject().release();
	}

	@Override
	public void disposeForgottenImageGenerators() {
		synchronized( m_forgottenImageGeneratorToPixelsMap ) {
			for( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator : m_forgottenImageGeneratorToPixelsMap.keySet() ) {
				disposeImageGenerator( imageGenerator );
			}
			m_forgottenFontToTextRendererMap.clear();
		}
	}

	//	class DefaultImageGenerator implements edu.cmu.cs.dennisc.image.ImageGenerator {
	//		private java.awt.Image m_image;
	//		public DefaultImageGenerator( java.awt.Image image ) {
	//			m_image = image;
	//		}
	//		@Override
	//		public int getWidth() {
	//			return edu.cmu.cs.dennisc.image.ImageUtilities.getWidth( m_image );
	//		}
	//		@Override
	//		public int getHeight() {
	//			return edu.cmu.cs.dennisc.image.ImageUtilities.getHeight( m_image );
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
	//			g.drawImage( m_image, 0, 0, null );
	//		}
	//	}

	@Override
	public boolean isRemembered( java.awt.Image image ) {
		edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator = m_imageToImageGeneratorMap.get( image );
		if( imageGenerator != null ) {
			return isRemembered( imageGenerator );
		} else {
			return false;
		}
	}
	@Override
	public void remember( java.awt.Image image ) {
		edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator = m_imageToImageGeneratorMap.get( image );
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
			m_imageToImageGeneratorMap.put( image, imageGenerator );
		}
		remember( imageGenerator );
	}
	@Override
	public void forget( java.awt.Image image ) {
		edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator = m_imageToImageGeneratorMap.get( image );
		forget( imageGenerator );
	}
	@Override
	public void disposeForgottenImages() {
		//todo
		for( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator : m_imageToImageGeneratorMap.values() ) {
			if( m_forgottenImageGeneratorToPixelsMap.containsKey( imageGenerator ) ) {
				disposeImageGenerator( imageGenerator );
				m_forgottenImageGeneratorToPixelsMap.remove( imageGenerator );
			}
		}
	}

	// edu.cmu.cs.dennisc.lookingglass.opengl.Graphics2D

	public javax.media.opengl.GL getGL() {
		return m_renderContext.gl;
	}
}
