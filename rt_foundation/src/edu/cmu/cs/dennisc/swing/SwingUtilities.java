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
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class SwingUtilities {
	public static void doLayout( java.awt.Component c ) {
		//c.doLayout();
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				doLayout( component );
			}
		}
		c.doLayout();
		//c.getPreferredSize();
	}
	private static void paint( java.awt.Graphics g, java.awt.Component c, java.awt.Container p, int x, int y ) {
		java.awt.Dimension size = c.getPreferredSize();
//		java.awt.Rectangle rect = new java.awt.Rectangle();
//		javax.swing.SwingUtilities.calculateInnerArea( (javax.swing.JComponent)c, rect );
		g.translate( x, y );
		javax.swing.SwingUtilities.paintComponent( g, c, p, 0, 0, size.width, size.height );
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				paint( g, component, p, component.getX(), component.getY() );
			}
		}
		g.translate( -x, -y );
	}
	public static void paint( java.awt.Graphics g, java.awt.Component c, java.awt.Container p ) {
		//doLayout( c );
		paint( g, c, p, 0, 0 );
	}

	public static javax.swing.Icon createIcon( java.awt.Component component, java.awt.Container p ) {
		javax.swing.Icon rv;
		java.awt.Dimension size = component.getPreferredSize();
		if( size.width > 0 && size.height > 0 ) {
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics g = image.getGraphics();
//			g.setColor( java.awt.Color.RED );
//			g.fillRect( 0, 0, size.width, size.height );
			SwingUtilities.paint( g, component, p );
			g.dispose();
			rv = new javax.swing.ImageIcon( image );
		} else {
			rv = new javax.swing.Icon() {
				public int getIconHeight() {
					return 100;
				}
				public int getIconWidth() {
					return 100;
				}
				public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
					g.setColor( java.awt.Color.RED );
					g.fillOval( x, y, 100, 100 );
				}
			};
		}
		return rv;
	}

	public static java.awt.Frame getRootFrame( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof java.awt.Frame ) {
			return (java.awt.Frame)root;
		} else {
			return null;
		}
	}
	public static javax.swing.JFrame getRootJFrame( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof javax.swing.JFrame ) {
			return (javax.swing.JFrame)root;
		} else {
			return null;
		}
	}

	public static java.awt.Dialog getRootDialog( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof java.awt.Dialog ) {
			return (java.awt.Dialog)root;
		} else {
			return null;
		}
	}
	public static javax.swing.JDialog getRootJDialog( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof javax.swing.JDialog ) {
			return (javax.swing.JDialog)root;
		} else {
			return null;
		}
	}

	private static boolean s_isWindows;
	static {
		s_isWindows = System.getProperty( "os.name" ).contains( "indows" );
	}

	public static boolean isControlDown( java.awt.event.InputEvent e ) {
		if( s_isWindows ) {
			return e.isControlDown();
		} else {
			return e.isAltDown();
		}
	}

	private static java.awt.image.BufferedImage s_bufferedImage = null;

	public static java.awt.Graphics getGraphics() {
		if( s_bufferedImage != null ) {
			//pass
		} else {
			s_bufferedImage = new java.awt.image.BufferedImage( 1, 1, java.awt.image.BufferedImage.TYPE_3BYTE_BGR );
		}
		return s_bufferedImage.getGraphics();
	}

	public static void showMessageDialogInScrollableUneditableTextArea( java.awt.Component owner, String text, String title, int messageType, final int maxPreferredWidth, final int maxPreferredHeight ) {
		assert messageType == javax.swing.JOptionPane.ERROR_MESSAGE || messageType == javax.swing.JOptionPane.INFORMATION_MESSAGE || messageType == javax.swing.JOptionPane.WARNING_MESSAGE || messageType == javax.swing.JOptionPane.PLAIN_MESSAGE;
		javax.swing.JTextArea textArea = new javax.swing.JTextArea( text );
		textArea.setEditable( false );
		javax.swing.JOptionPane.showMessageDialog( owner, new javax.swing.JScrollPane( textArea ) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				java.awt.Dimension rv = super.getPreferredSize();
				rv.width = Math.min( rv.width, maxPreferredWidth );
				rv.height = Math.min( rv.height, maxPreferredHeight );
				return rv;
			}
		}, title, messageType );
	}
	public static void showMessageDialogInScrollableUneditableTextArea( java.awt.Component owner, String text, String title, int messageType ) {
		showMessageDialogInScrollableUneditableTextArea( owner, text, title, messageType, 640, 480 );
	}
}
