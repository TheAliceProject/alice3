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
public class PrintHelper implements java.awt.print.Printable {
	public static class Builder {
		private final java.awt.Insets insets;
		private final java.awt.Paint clearPaint;
		private java.awt.Component lineStart;
		private java.awt.Component lineEnd;
		private java.awt.Component pageStart;
		private java.awt.Component pageEnd;
		private java.awt.Component center;

		public Builder( java.awt.Insets insets, java.awt.Paint clearPaint ) {
			this.insets = insets;
			this.clearPaint = clearPaint;
		}

		public Builder lineStart( java.awt.Component lineStart ) {
			this.lineStart = lineStart;
			return this;
		}

		public Builder lineEnd( java.awt.Component lineEnd ) {
			this.lineEnd = lineEnd;
			return this;
		}

		public Builder pageStart( java.awt.Component pageStart ) {
			this.pageStart = pageStart;
			return this;
		}

		public Builder pageEnd( java.awt.Component pageEnd ) {
			this.pageEnd = pageEnd;
			return this;
		}

		public Builder center( java.awt.Component center ) {
			this.center = center;
			return this;
		}

		public PrintHelper build() {
			return new PrintHelper( this );
		}
	}

	private final java.awt.Insets insets;
	private final java.awt.Paint clearPaint;
	private final java.awt.Component lineStart;
	private final java.awt.Component lineEnd;
	private final java.awt.Component pageStart;
	private final java.awt.Component pageEnd;
	private final java.awt.Component center;

	private static java.awt.Component getViewportViewIfNecessary( java.awt.Component component ) {
		if( component instanceof javax.swing.JScrollPane ) {
			javax.swing.JScrollPane jScrollPane = (javax.swing.JScrollPane)component;
			return jScrollPane.getViewport().getView();
		} else {
			return component;
		}
	}

	private static void printAll( java.awt.Graphics2D g2, java.awt.Component component ) {
		if( component != null ) {
			int x = component.getX();
			int y = component.getY();
			g2.translate( x, y );
			try {
				java.awt.Shape prevClip = g2.getClip();
				try {
					java.awt.Component c = getViewportViewIfNecessary( component );
					java.awt.Dimension size = c.getPreferredSize();
					g2.setClip( 0, 0, size.width, size.height );
					getViewportViewIfNecessary( component ).printAll( g2 );
				} finally {
					g2.setClip( prevClip );
				}
			} finally {
				g2.translate( -x, -y );
			}
		}
	}

	private PrintHelper( Builder builder ) {
		this.insets = builder.insets;
		this.clearPaint = builder.clearPaint;
		this.lineStart = builder.lineStart;
		this.lineEnd = builder.lineEnd;
		this.pageStart = builder.pageStart;
		this.pageEnd = builder.pageEnd;
		this.center = builder.center;
	}

	@Override
	public int print( java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex ) throws java.awt.print.PrinterException {
		if( pageIndex > 0 ) {
			return java.awt.print.Printable.NO_SUCH_PAGE;
		} else {
			java.awt.Dimension size = getViewportViewIfNecessary( this.center ).getPreferredSize();
			int width = size.width;
			int height = size.height;
			for( java.awt.Component component : new java.awt.Component[] { lineStart, lineEnd } ) {
				if( component != null ) {
					java.awt.Dimension componentSize = component.getPreferredSize();
					width += componentSize.width;
					height = Math.max( height, componentSize.height );
				}
			}
			for( java.awt.Component component : new java.awt.Component[] { pageStart, pageEnd } ) {
				if( component != null ) {
					java.awt.Dimension componentSize = component.getPreferredSize();
					width = Math.max( width, componentSize.width );
					height += componentSize.height;
				}
			}

			width += insets.left + insets.right;
			height += insets.top + insets.bottom;

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			java.awt.geom.AffineTransform prevTransform = g2.getTransform();
			try {
				double scale = edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities.calculateScale( pageFormat, width, height );
				g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
				if( scale > 1.0 ) {
					g2.scale( 1.0 / scale, 1.0 / scale );
				}

				g2.setPaint( this.clearPaint );
				g2.fillRect( 0, 0, width, height );

				printAll( g2, this.center );
				printAll( g2, this.pageStart );
				printAll( g2, this.lineStart );

				if( lineEnd != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( lineEnd );
				}

				if( pageEnd != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( pageEnd );
				}

			} finally {
				g2.setTransform( prevTransform );
			}
			return java.awt.print.Printable.PAGE_EXISTS;
		}
	}

}
