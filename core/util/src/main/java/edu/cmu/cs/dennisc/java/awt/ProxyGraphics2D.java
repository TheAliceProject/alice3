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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class ProxyGraphics2D extends java.awt.Graphics2D {
	public void setOther( java.awt.Graphics2D other ) {
		this.other = other;
	}

	@Override
	public void draw( java.awt.Shape s ) {
		this.other.draw( s );
	}

	@Override
	public boolean drawImage( java.awt.Image img, java.awt.geom.AffineTransform xform, java.awt.image.ImageObserver obs ) {
		return this.other.drawImage( img, xform, obs );
	}

	@Override
	public void drawImage( java.awt.image.BufferedImage img, java.awt.image.BufferedImageOp op, int x, int y ) {
		this.other.drawImage( img, op, x, y );
	}

	@Override
	public void drawRenderedImage( java.awt.image.RenderedImage img, java.awt.geom.AffineTransform xform ) {
		this.other.drawRenderedImage( img, xform );
	}

	@Override
	public void drawRenderableImage( java.awt.image.renderable.RenderableImage img, java.awt.geom.AffineTransform xform ) {
		this.other.drawRenderableImage( img, xform );
	}

	@Override
	public void drawString( String str, float x, float y ) {
		this.other.drawString( str, x, y );
	}

	@Override
	public void drawString( java.text.AttributedCharacterIterator iterator, float x, float y ) {
		this.other.drawString( iterator, x, y );
	}

	@Override
	public void drawGlyphVector( java.awt.font.GlyphVector g, float x, float y ) {
		this.other.drawGlyphVector( g, x, y );
	}

	@Override
	public void fill( java.awt.Shape s ) {
		this.other.fill( s );
	}

	@Override
	public boolean hit( java.awt.Rectangle rect, java.awt.Shape s, boolean onStroke ) {
		return this.other.hit( rect, s, onStroke );
	}

	@Override
	public java.awt.GraphicsConfiguration getDeviceConfiguration() {
		return this.other.getDeviceConfiguration();
	}

	@Override
	public void setComposite( java.awt.Composite comp ) {
		this.other.setComposite( comp );
	}

	@Override
	public void setPaint( java.awt.Paint paint ) {
		this.other.setPaint( paint );
	}

	@Override
	public void setStroke( java.awt.Stroke s ) {
		this.other.setStroke( s );
	}

	@Override
	public void setRenderingHint( java.awt.RenderingHints.Key hintKey, Object hintValue ) {
		this.other.setRenderingHint( hintKey, hintValue );
	}

	@Override
	public Object getRenderingHint( java.awt.RenderingHints.Key hintKey ) {
		return this.other.getRenderingHint( hintKey );
	}

	@Override
	public void setRenderingHints( java.util.Map<?, ?> hints ) {
		this.other.setRenderingHints( hints );
	}

	@Override
	public void addRenderingHints( java.util.Map<?, ?> hints ) {
		this.other.addRenderingHints( hints );
	}

	@Override
	public java.awt.RenderingHints getRenderingHints() {
		return this.other.getRenderingHints();
	}

	@Override
	public void translate( double tx, double ty ) {
		this.other.translate( tx, ty );
	}

	@Override
	public void rotate( double theta ) {
		this.other.rotate( theta );
	}

	@Override
	public void rotate( double theta, double x, double y ) {
		this.other.rotate( theta, x, y );
	}

	@Override
	public void scale( double sx, double sy ) {
		this.other.scale( sx, sy );
	}

	@Override
	public void shear( double shx, double shy ) {
		this.other.shear( shx, shy );
	}

	@Override
	public void transform( java.awt.geom.AffineTransform Tx ) {
		this.other.transform( Tx );
	}

	@Override
	public void setTransform( java.awt.geom.AffineTransform Tx ) {
		this.other.setTransform( Tx );
	}

	@Override
	public java.awt.geom.AffineTransform getTransform() {
		return this.other.getTransform();
	}

	@Override
	public java.awt.Paint getPaint() {
		return this.other.getPaint();
	}

	@Override
	public java.awt.Composite getComposite() {
		return this.other.getComposite();
	}

	@Override
	public void setBackground( java.awt.Color color ) {
		this.other.setBackground( color );
	}

	@Override
	public java.awt.Color getBackground() {
		return this.other.getBackground();
	}

	@Override
	public java.awt.Stroke getStroke() {
		return this.other.getStroke();
	}

	@Override
	public void clip( java.awt.Shape s ) {
		this.other.clip( s );
	}

	@Override
	public java.awt.font.FontRenderContext getFontRenderContext() {
		return this.other.getFontRenderContext();
	}

	@Override
	public java.awt.Graphics create() {
		return this.other.create();
	}

	@Override
	public void translate( int x, int y ) {
		this.other.translate( x, y );
	}

	@Override
	public java.awt.Color getColor() {
		return this.other.getColor();
	}

	@Override
	public void setColor( java.awt.Color c ) {
		this.other.setColor( c );
	}

	@Override
	public void setPaintMode() {
		this.other.setPaintMode();
	}

	@Override
	public void setXORMode( java.awt.Color c1 ) {
		this.other.setXORMode( c1 );
	}

	@Override
	public java.awt.Font getFont() {
		return this.other.getFont();
	}

	@Override
	public void setFont( java.awt.Font font ) {
		this.other.setFont( font );
	}

	@Override
	public java.awt.FontMetrics getFontMetrics( java.awt.Font f ) {
		return this.other.getFontMetrics( f );
	}

	@Override
	public java.awt.Rectangle getClipBounds() {
		return this.other.getClipBounds();
	}

	@Override
	public void clipRect( int x, int y, int width, int height ) {
		this.other.clipRect( x, y, width, height );
	}

	@Override
	public void setClip( int x, int y, int width, int height ) {
		this.other.setClip( x, y, width, height );
	}

	@Override
	public java.awt.Shape getClip() {
		return this.other.getClip();
	}

	@Override
	public void setClip( java.awt.Shape clip ) {
		this.other.setClip( clip );
	}

	@Override
	public void copyArea( int x, int y, int width, int height, int dx, int dy ) {
		this.other.copyArea( x, y, width, height, dx, dy );
	}

	@Override
	public void drawLine( int x1, int y1, int x2, int y2 ) {
		this.other.drawLine( x1, y1, x2, y2 );
	}

	@Override
	public void fillRect( int x, int y, int width, int height ) {
		this.other.fillRect( x, y, width, height );
	}

	@Override
	public void clearRect( int x, int y, int width, int height ) {
		this.other.clearRect( x, y, width, height );
	}

	@Override
	public void drawRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		this.other.drawRoundRect( x, y, width, height, arcWidth, arcHeight );
	}

	@Override
	public void fillRoundRect( int x, int y, int width, int height, int arcWidth, int arcHeight ) {
		this.other.fillRoundRect( x, y, width, height, arcWidth, arcHeight );
	}

	@Override
	public void drawOval( int x, int y, int width, int height ) {
		this.other.drawOval( x, y, width, height );
	}

	@Override
	public void fillOval( int x, int y, int width, int height ) {
		this.other.fillOval( x, y, width, height );
	}

	@Override
	public void drawArc( int x, int y, int width, int height, int startAngle, int arcAngle ) {
		this.other.drawArc( x, y, width, height, startAngle, arcAngle );
	}

	@Override
	public void fillArc( int x, int y, int width, int height, int startAngle, int arcAngle ) {
		this.other.fillArc( x, y, width, height, startAngle, arcAngle );
	}

	@Override
	public void drawPolyline( int[] xPoints, int[] yPoints, int nPoints ) {
		this.other.drawPolyline( xPoints, yPoints, nPoints );
	}

	@Override
	public void drawPolygon( int[] xPoints, int[] yPoints, int nPoints ) {
		this.other.drawPolygon( xPoints, yPoints, nPoints );
	}

	@Override
	public void fillPolygon( int[] xPoints, int[] yPoints, int nPoints ) {
		this.other.fillPolygon( xPoints, yPoints, nPoints );
	}

	@Override
	public void drawString( String str, int x, int y ) {
		this.other.drawString( str, x, y );
	}

	@Override
	public void drawString( java.text.AttributedCharacterIterator iterator, int x, int y ) {
		this.other.drawString( iterator, x, y );
	}

	@Override
	public boolean drawImage( java.awt.Image img, int x, int y, java.awt.image.ImageObserver observer ) {
		return this.other.drawImage( img, x, y, observer );
	}

	@Override
	public boolean drawImage( java.awt.Image img, int x, int y, int width, int height, java.awt.image.ImageObserver observer ) {
		return this.other.drawImage( img, x, y, width, height, observer );
	}

	@Override
	public boolean drawImage( java.awt.Image img, int x, int y, java.awt.Color bgcolor, java.awt.image.ImageObserver observer ) {
		return this.other.drawImage( img, x, y, bgcolor, observer );
	}

	@Override
	public boolean drawImage( java.awt.Image img, int x, int y, int width, int height, java.awt.Color bgcolor, java.awt.image.ImageObserver observer ) {
		return this.other.drawImage( img, x, y, width, height, bgcolor, observer );
	}

	@Override
	public boolean drawImage( java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.image.ImageObserver observer ) {
		return this.other.drawImage( img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer );
	}

	@Override
	public boolean drawImage( java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.Color bgcolor, java.awt.image.ImageObserver observer ) {
		return this.other.drawImage( img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer );
	}

	@Override
	public void dispose() {
		this.other.dispose();
	}

	private java.awt.Graphics2D other;

}
