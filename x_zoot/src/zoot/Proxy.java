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
package zoot;

/**
 * @author Dennis Cosgrove
 */
abstract class Proxy extends javax.swing.JPanel {
	private static java.awt.image.BufferedImage image;
	private static java.awt.image.BufferedImage getOffscreenImage( int width, int height ) {
		if( image == null || image.getWidth() != width || image.getHeight() != height ) {
			image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
			//image = getGraphicsConfiguration().createCompatibleImage( width, height, java.awt.Transparency.TRANSLUCENT );
		}
		return image;
	}
	private java.awt.Component subject;
	private boolean isOverDropAcceptor = false;
	private boolean isCopyDesired = false;
	

	public Proxy( java.awt.Component subject ) {
		this.subject = subject;
		this.setOpaque( false );
	}
	
	protected java.awt.Component getSubject() {
		return this.subject;
	}

	public int getProxyWidth() {
		return this.subject.getWidth();
	}
	public int getProxyHeight() {
		return this.subject.getHeight();
	}
	
	protected void fillBounds( java.awt.Graphics2D g2 ) {
		java.awt.Component subject = this.getSubject();
		int x = 0;
		int y = 0;
		int width = subject.getWidth();
		int height = subject.getHeight();
		if( subject instanceof ZComponent ) {
			ZComponent component = (ZComponent)subject;
			component.fillBounds( g2, x, y, width, height );
		} else {
			g2.fillRect( x, y, width, height );
		}
	}
	
	protected abstract void paintProxy( java.awt.Graphics2D g2 );
	protected abstract float getAlpha();
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		int width = this.getProxyWidth();
		int height = this.getProxyHeight();
		if( width > 0 && height > 0 ) {
			java.awt.image.BufferedImage image = Proxy.getOffscreenImage( width, height );
			//todo: synchronize
			//if( LayeredPaneProxy.image == null || LayeredPaneProxy.image.getWidth() < width || LayeredPaneProxy.image.getHeight() < height ) {
			java.awt.Graphics2D g2Image = (java.awt.Graphics2D)image.getGraphics();
			this.paintProxy( g2Image );
			g2Image.dispose();

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			
			java.awt.Composite prevComposite = g2.getComposite();

			//g2.setComposite( java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.CLEAR, 0.0f ) );
			//g2.clearRect( 0, 0, width, height );
			float alpha = this.getAlpha();
			if( alpha < 0.99f ) {
				g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, this.getAlpha() ) );
			}
			//java.awt.Color bgColor = new java.awt.Color( 0, 0, 0, 0 );
			//g2.drawImage( LayeredPaneimage, 0, 0, width, height, 0, 0, width, height, bgColor, this );
			g2.drawImage( image, 0, 0, width, height, this );
			g2.dispose();
			g2.setComposite( prevComposite );
		}
	}

	public boolean isOverDropAcceptor() {
		return this.isOverDropAcceptor;
	}
	public void setOverDropAcceptor( boolean isOverDropAcceptor ) {
		if( this.isOverDropAcceptor != isOverDropAcceptor ) {
			this.isOverDropAcceptor = isOverDropAcceptor;
			this.repaint();
		}
	}
	public boolean isCopyDesired() {
		return this.isCopyDesired;
	}
	public void setCopyDesired( boolean isCopyDesired ) {
		if( this.isCopyDesired != isCopyDesired ) {
			this.isCopyDesired = isCopyDesired;
			this.repaint();
		}
	}

	public int getDropWidth() {
		return this.getWidth();
	}
	public int getDropHeight() {
		return this.getHeight();
	}
}
