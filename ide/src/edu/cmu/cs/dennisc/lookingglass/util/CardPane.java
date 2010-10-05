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
package edu.cmu.cs.dennisc.lookingglass.util;

/**
 * @author Dennis Cosgrove
 */
class SnapshotPane extends javax.swing.JComponent {
	public java.awt.image.BufferedImage bufferedImage;

	public SnapshotPane() {
		setBackground( java.awt.Color.GREEN );
		setFont( new java.awt.Font( null, java.awt.Font.ITALIC, 12 ) );
		//setOpaque( true );
	}
	public void setSnapshotImage( java.awt.image.BufferedImage bufferedImage ) {
		this.bufferedImage = bufferedImage;
		repaint();
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		if( this.bufferedImage != null ) {
			prepareImage( this.bufferedImage, this );
			g.drawImage( this.bufferedImage, 0, 0, this );
		}
		String text = "live rendering removed for performance considerations";
		int x = getWidth() / 8;
		int y = getHeight() / 2;

		((java.awt.Graphics2D)g).setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

		g.setColor( java.awt.Color.BLACK );
		g.drawString( text, x + 1, y + 1 );
		g.setColor( java.awt.Color.YELLOW );
		g.drawString( text, x, y );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CardPane extends javax.swing.JPanel {
	private SnapshotPane snapshotPane = new SnapshotPane();
	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
	public java.awt.image.BufferedImage bufferedImage = null;

	private java.awt.CardLayout cardLayout = new java.awt.CardLayout();
	private static final String LIVE_KEY = "live";
	private static final String SNAPSHOT_KEY = "snapshot";
	private String currentKey = null;

	public CardPane( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass, java.awt.Component awtComponent ) {
		setLayout( this.cardLayout );
		this.onscreenLookingGlass = onscreenLookingGlass;
		add( awtComponent, LIVE_KEY );
		add( this.snapshotPane, SNAPSHOT_KEY );
		showLive();
		//setBackground( java.awt.Color.BLUE );
		//setOpaque( false );
	}
	public CardPane( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		this( onscreenLookingGlass, onscreenLookingGlass.getAWTComponent() );
	}
	
	private void showCard( String key ) {
		this.cardLayout.show( this, key );
		this.currentKey = key;
	}
	public void showSnapshot() {
		if( isLive() ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "showSnapshot" );
			int desiredImageWidth = this.onscreenLookingGlass.getWidth();
			int desiredImageHeight = this.onscreenLookingGlass.getHeight();
			if( this.bufferedImage != null ) {
				if( this.bufferedImage.getWidth() != desiredImageWidth || this.bufferedImage.getHeight() != desiredImageHeight ) {
					this.bufferedImage = null;
				}
			}
			if( this.bufferedImage != null ) {
				//pass
			} else {
				if( desiredImageWidth > 0 && desiredImageHeight > 0 ) {
					this.bufferedImage = this.onscreenLookingGlass.createBufferedImageForUseAsColorBuffer();
				}
			}
			if( this.bufferedImage != null ) {
				this.onscreenLookingGlass.getColorBuffer( this.bufferedImage );
				this.snapshotPane.setSnapshotImage( this.bufferedImage );
				//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				//				public void run() {
				showCard( CardPane.SNAPSHOT_KEY );
				this.onscreenLookingGlass.setRenderingEnabled( false );
				//				}
				//			} );
			}
		} else {
			//pass
		}
	}
	public void showLive() {
		if( isLive() ) {
			//pass
		} else {
			//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
			//				public void run() {
			this.onscreenLookingGlass.setRenderingEnabled( true );
			showCard( CardPane.LIVE_KEY );
			//				}
			//			} );
		}
	}
	
	public boolean isLive() {
		return CardPane.LIVE_KEY.equals( this.currentKey );
	}

	public void handleLiveChange() {
		if( isLive() ) {
			//pass
		} else {
			showLive();
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					showSnapshot();
				}
			} );
		}
	}
}
