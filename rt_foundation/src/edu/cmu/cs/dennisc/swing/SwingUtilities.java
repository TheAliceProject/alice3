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
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class SwingUtilities {
	public static void doLayoutTree( java.awt.Component c ) {
//		c.doLayout();
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				doLayoutTree( component );
			}
		}
		c.doLayout();
		//c.getPreferredSize();
	}
	public static void invalidateTree( java.awt.Component c ) {
		c.invalidate();
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				invalidateTree( component );
			}
		}
	}
	public static void validateTree( java.awt.Component c ) {
		c.invalidate();
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				validateTree( component );
			}
		}
	}
	public static void revalidateTree( java.awt.Component c ) {
		if( c instanceof javax.swing.JComponent ) {
			javax.swing.JComponent jc = (javax.swing.JComponent)c;
			jc.revalidate();
		}
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				validateTree( component );
			}
		}
	}
	private static void paint( java.awt.Graphics g, java.awt.Component c, java.awt.Container p, int x, int y ) {
		java.awt.Dimension size = c.getPreferredSize();
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
	private static void paint( java.awt.Graphics g, java.awt.Component c, java.awt.Container p ) {
		//doLayout( c );
		paint( g, c, p, 0, 0 );
	}

	private static java.awt.Container container = new java.awt.Container();
	public static javax.swing.Icon createIcon( java.awt.Component component ) {
		javax.swing.Icon rv;
		java.awt.Dimension size = component.getPreferredSize();
		if( size.width > 0 && size.height > 0 ) {
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics g = image.getGraphics();
//			g.setColor( java.awt.Color.RED );
//			g.fillRect( 0, 0, size.width, size.height );
			SwingUtilities.paint( g, component, container );
			g.dispose();
			rv = new javax.swing.ImageIcon( image );
		} else {
			rv = new javax.swing.Icon() {
				public int getIconWidth() {
					return 24;
				}
				public int getIconHeight() {
					return 12;
				}
				public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
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

	public static boolean isQuoteControlUnquoteDown( java.awt.event.InputEvent e ) {
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
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
	
	public static java.awt.event.MouseEvent convertMouseEvent(java.awt.Component source, java.awt.event.MouseEvent sourceEvent, java.awt.Component destination) {
		int modifiers = sourceEvent.getModifiers();
		int modifiersEx = sourceEvent.getModifiersEx();
		int modifiersComplete = modifiers | modifiersEx;
		java.awt.event.MouseEvent me = javax.swing.SwingUtilities.convertMouseEvent( source, sourceEvent, destination );
		if( me instanceof java.awt.event.MouseWheelEvent ) {
			java.awt.event.MouseWheelEvent mwe = (java.awt.event.MouseWheelEvent)me;
			return new java.awt.event.MouseWheelEvent( me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation() );
		} else if( me instanceof javax.swing.event.MenuDragMouseEvent ){
			javax.swing.event.MenuDragMouseEvent mdme = (javax.swing.event.MenuDragMouseEvent)me;
			return new javax.swing.event.MenuDragMouseEvent( me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), mdme.getPath(), mdme.getMenuSelectionManager() );
		} else {
			return new java.awt.event.MouseEvent( me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger() );
		}
	}
}
