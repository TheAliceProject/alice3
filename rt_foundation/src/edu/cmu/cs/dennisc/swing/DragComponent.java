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
public class DragComponent extends javax.swing.JComponent {
	private java.awt.image.BufferedImage m_bufferedImage = null;
	private javax.swing.JLayeredPane m_layeredPane = null;

	private java.util.List< DropTarget > m_dropTargets = new java.util.LinkedList< DropTarget >();

	public void addDropTarget( DropTarget dropTarget ) {
		m_dropTargets.add( dropTarget );
	}
	public void removeDropTarget( DropTarget dropTarget ) {
		m_dropTargets.remove( dropTarget );
	}
	
	//private DragSource m_dragSource = null;
	private DropTarget m_dropTarget = null;
	private void setDropTarget( DropTarget dropTarget, edu.cmu.cs.dennisc.swing.event.DragEvent e ) {
		if( m_dropTarget != dropTarget ) {
			if( m_dropTarget != null ) {
				m_dropTarget.dragExited( e );
			}
			m_dropTarget = dropTarget;
			if( m_dropTarget != null ) {
				m_dropTarget.dragEntered( e );
			}
		}
	}	
	public void handleMousePressed( DragSource dragSource, java.awt.event.MouseEvent me ) {
		edu.cmu.cs.dennisc.swing.event.DragEvent e = new edu.cmu.cs.dennisc.swing.event.DragEvent( dragSource, this, me );
		for( DropTarget dropTarget : m_dropTargets ) {
			dropTarget.dragStarted( e );
		}
		for( DropTarget dropTarget : m_dropTargets ) {
			if( dropTarget.isAccepting( e ) ) {
				setDropTarget( dropTarget, e );
			}
		}
	}
	public void handleMouseDragged( DragSource dragSource, java.awt.event.MouseEvent me ) {
		edu.cmu.cs.dennisc.swing.event.DragEvent e = new edu.cmu.cs.dennisc.swing.event.DragEvent( dragSource, this, me );
		for( DropTarget dropTarget : m_dropTargets ) {
			if( dropTarget.isAccepting( e ) ) {
				setDropTarget( dropTarget, e );
				dropTarget.dragUpdated( e );
				break;
			}
		}
	}
	public void handleMouseReleased( DragSource dragSource, java.awt.event.MouseEvent me ) {
		edu.cmu.cs.dennisc.swing.event.DragEvent e = new edu.cmu.cs.dennisc.swing.event.DragEvent( dragSource, this, me );
		for( DropTarget dropTarget : m_dropTargets ) {
			if( dropTarget.isAccepting( e ) ) {
				dropTarget.dragDropped( e );
				break;
			}
		}
		setDropTarget( null, e );
		for( DropTarget dropTarget : m_dropTargets ) {
			dropTarget.dragStopped( e );
		}
	}
	

	public javax.swing.JLayeredPane getLayeredPane() {
		return m_layeredPane;
	}
	public void setLayeredPane( javax.swing.JLayeredPane layeredPane ) {
		if( m_layeredPane != layeredPane ) {
			m_layeredPane = layeredPane;
		}
	}
	
	public java.awt.image.BufferedImage accessBufferedImage( int width, int height ) {
		if( m_bufferedImage != null && m_bufferedImage.getWidth() >= width && m_bufferedImage.getHeight() >= height ) {
			//pass
		} else {
			m_bufferedImage = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
		}
		setSize( width, height );
		return m_bufferedImage;
	}
	
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		if( m_bufferedImage != null ) {
			g.drawImage( m_bufferedImage, 0, 0, this );
		}
	}
	public void removeFromLayeredPane() {
		if( m_layeredPane != null ) {
			m_layeredPane.remove( this );
			m_layeredPane.repaint( getBounds() );
		}
	}
	
}
