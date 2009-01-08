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
		//		String text = "live rendering removed for performance considerations";
		//		int x = getWidth() / 8;
		//		int y = getHeight() / 2;
		//		
		//		((java.awt.Graphics2D)g).setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		//		
		//		g.setColor( java.awt.Color.BLACK );
		//		g.drawString( text, x+1, y+1 );
		//		g.setColor( java.awt.Color.YELLOW );
		//		g.drawString( text, x, y );
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
			if( this.bufferedImage != null ) {
				if( this.bufferedImage.getWidth() != getWidth() || this.bufferedImage.getHeight() != getHeight() ) {
					this.bufferedImage = null;
				}
			}
			if( this.bufferedImage != null ) {
				//pass
			} else {
				this.bufferedImage = this.onscreenLookingGlass.createBufferedImageForUseAsColorBuffer();
			}
			this.onscreenLookingGlass.getColorBuffer( this.bufferedImage );
			this.snapshotPane.setSnapshotImage( this.bufferedImage );
			//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
			//				public void run() {
			showCard( CardPane.SNAPSHOT_KEY );
			//				}
			//			} );
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
