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
package edu.cmu.cs.dennisc.ui;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.java.awt.EventInterceptor;
import edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities;
import edu.cmu.cs.dennisc.java.awt.event.KeyEventUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class DragAdapter implements MouseListener, MouseMotionListener, KeyListener {
	private Animator m_animator;
	private UndoRedoManager m_undoRedoManager = null;
	private Component m_awtComponent = null;
	private DragStyle m_dragStyleCurrent;
	private Point m_current = new Point();
	private Point m_previous = new Point();
	private Point m_press = new Point();

	//	private int m_modifierMask = java.awt.event.MouseEvent.BUTTON1_MASK | java.awt.event.MouseEvent.BUTTON2_MASK | java.awt.event.MouseEvent.BUTTON3_MASK;
	private boolean m_isActive = false;

	private List<EventInterceptor> m_eventInterceptors = new LinkedList<EventInterceptor>();

	public void addEventInterceptor( EventInterceptor eventInterceptor ) {
		m_eventInterceptors.add( eventInterceptor );
	}

	public void removeEventInterceptor( EventInterceptor eventInterceptor ) {
		m_eventInterceptors.add( eventInterceptor );
	}

	public Iterable<EventInterceptor> accessEventInterceptors() {
		return m_eventInterceptors;
	}

	//	private static final java.awt.Cursor NULL_CURSOR;
	//
	//	static {
	//
	//		java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
	//		final int WIDTH = 16;
	//		final int HEIGHT = 16;
	//		int[] pixels = new int[ WIDTH * HEIGHT ];
	//		java.awt.image.MemoryImageSource memoryImageSource = new java.awt.image.MemoryImageSource( WIDTH, HEIGHT, pixels, 0, WIDTH );
	//		java.awt.Image image = toolkit.createImage( memoryImageSource );
	//		NULL_CURSOR = toolkit.createCustomCursor( image, new java.awt.Point( 0,0 ), "NULL_CURSOR" );
	//	}
	//
	//	private java.awt.Cursor m_prevCursor;
	//	protected void showCursor() {
	//		m_awtComponent.setCursor( m_prevCursor );
	//		m_prevCursor = null;
	//	}
	//	protected void hideCursor() {
	//		m_prevCursor = m_awtComponent.getCursor();
	//		m_awtComponent.setCursor( NULL_CURSOR );
	//	}

	protected Component getAWTComponent() {
		return m_awtComponent;
	}

	public void setAWTComponent( Component awtComponent ) {
		if( m_awtComponent != null ) {
			removeListeners( m_awtComponent );
		}
		m_awtComponent = awtComponent;
		if( m_awtComponent != null ) {
			addListeners( awtComponent );
		}
	}

	//	public int getModifierMask() {
	//		return m_modifierMask;
	//	}
	//	public void setModifierMask( int modifierMask ) {
	//		m_modifierMask = modifierMask;
	//	}
	//	protected boolean isModifierMaskTestPassed( int modifierMask ) {
	//		return (m_modifierMask & modifierMask) != 0;
	//	}

	protected boolean isAcceptable( MouseEvent e ) {
		return ( e.getModifiers() & ( MouseEvent.BUTTON1_MASK | MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK ) ) != 0;
	}

	public Animator getAnimator() {
		return m_animator;
	}

	public void setAnimator( Animator animator ) {
		m_animator = animator;
	}

	public UndoRedoManager getUndoRedoManager() {
		return m_undoRedoManager;
	}

	public void setUndoRedoManager( UndoRedoManager undoRedoManager ) {
		m_undoRedoManager = undoRedoManager;
	}

	protected void addListeners( Component component ) {
		component.addMouseListener( this );
		component.addMouseMotionListener( this );
		component.addKeyListener( this );
	}

	protected void removeListeners( Component component ) {
		component.removeMouseListener( this );
		component.removeMouseMotionListener( this );
		component.removeKeyListener( this );
	}

	protected abstract void handleMousePress( Point current, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange );

	protected abstract void handleMouseDrag( Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, DragStyle dragStyle );

	protected abstract Point handleMouseRelease( Point rvCurrent, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange );

	private boolean m_isDragInProgress = false;

	public boolean isDragInProgress() {
		return m_isDragInProgress;
	}

	private void invokeHandleMousePress( boolean isOriginalAsOpposedToStyleChange ) {
		m_isDragInProgress = true;
		m_press.x = m_previous.x = m_current.x;
		m_press.y = m_previous.y = m_current.y;
		handleMousePress( m_current, m_dragStyleCurrent, isOriginalAsOpposedToStyleChange );
	}

	private void invokeHandleMouseDrag() {
		handleMouseDrag( m_current, m_current.x - m_press.x, m_current.y - m_press.y, m_current.x - m_previous.x, m_current.y - m_previous.y, m_dragStyleCurrent );
	}

	private Point invokeHandleMouseRelease( boolean isOriginalAsOpposedToStyleChange ) {
		m_isDragInProgress = false;
		return handleMouseRelease( m_current, m_dragStyleCurrent, isOriginalAsOpposedToStyleChange );
	}

	@Override
	public final void mouseClicked( MouseEvent e ) {
	}

	@Override
	public final void mouseEntered( MouseEvent e ) {
	}

	@Override
	public final void mouseExited( MouseEvent e ) {
	}

	@Override
	public final void mousePressed( MouseEvent e ) {
		boolean isIntercepted = false;
		for( EventInterceptor eventInterceptor : m_eventInterceptors ) {
			if( eventInterceptor.isEventIntercepted( e ) ) {
				isIntercepted = true;
				break;
			}
		}
		if( isIntercepted ) {
			m_isActive = false;
		} else {
			e.getComponent().requestFocus();
			m_isActive = this.isAcceptable( e );
		}
		if( m_isActive ) {
			if( InputEventUtilities.isQuoteControlUnquoteDown( e ) ) {
				if( e.isShiftDown() ) {
					m_dragStyleCurrent = DragStyle.CONTROL_SHIFT;
				} else {
					m_dragStyleCurrent = DragStyle.CONTROL;
				}
			} else {
				if( e.isShiftDown() ) {
					m_dragStyleCurrent = DragStyle.SHIFT;
				} else {
					m_dragStyleCurrent = DragStyle.NORMAL;
				}
			}
			m_current.x = e.getX();
			m_current.y = e.getY();
			invokeHandleMousePress( true );
			invokeHandleMouseDrag();
		} else {
			m_dragStyleCurrent = null;
		}
	}

	@Override
	public final void mouseReleased( MouseEvent e ) {
		if( m_isActive ) {
			m_previous.x = m_current.x;
			m_previous.y = m_current.y;
			m_current.x = e.getX();
			m_current.y = e.getY();
			invokeHandleMouseRelease( true );
		}
		//		m_cursorWarps.clear();
	}

	//	private void handleMouseWarp( java.awt.event.MouseEvent e ) {
	//		java.awt.Point p = m_cursorWarps.peek();
	//		if( p.x == e.getX() && p.y == e.getY() ) {
	//			m_cursorWarps.remove();
	//			System.err.println( "received warp" );
	//		} else {
	//			System.err.println( "skipping mouseDragged " + e.getPoint() + " while waiting for cursor warp: " + p );
	//		}
	//	}
	@Override
	public final void mouseDragged( MouseEvent e ) {
		//		if( m_cursorWarps.size() > 0 ) {
		//			handleMouseWarp( e );
		//		} else {
		if( m_isActive ) {
			m_previous.x = m_current.x;
			m_previous.y = m_current.y;
			m_current.x = e.getX();
			m_current.y = e.getY();
			invokeHandleMouseDrag();
		}
		//		}
	}

	@Override
	public final void mouseMoved( MouseEvent e ) {
		//the mac does not report dragged events if control is pressed
		if( m_isActive ) {
			if( SystemUtilities.isMac() ) {
				if( e.isControlDown() ) {
					mouseDragged( e );
				}
			}
		}
	}

	protected void handleDragStyleChange( DragStyle from, DragStyle to ) {
		Point p = invokeHandleMouseRelease( false );
		m_current.x = p.x;
		m_current.y = p.y;
		m_dragStyleCurrent = to;
		invokeHandleMousePress( false );
		invokeHandleMouseDrag();
	}

	//	private void handleDragStyleChangeIfNecessary( DragStyle from, DragStyle to ) {
	//		if( from != to ) {
	//			handleDragStyleChange( from, to );
	//		}
	//	}

	private void addToDragStyleIfNecessary( int keyCode ) {
		if( m_dragStyleCurrent != null ) {
			if( keyCode == KeyEventUtilities.getQuoteControlUnquoteKey() ) {
				if( m_dragStyleCurrent.isControlDown() ) {
					//note: this should happen plenty, as key press events keep coming
					//pass
				} else {
					if( m_dragStyleCurrent.isShiftDown() ) {
						handleDragStyleChange( m_dragStyleCurrent, DragStyle.CONTROL_SHIFT );
					} else {
						handleDragStyleChange( m_dragStyleCurrent, DragStyle.CONTROL );
					}
				}
			} else if( keyCode == KeyEvent.VK_SHIFT ) {
				if( m_dragStyleCurrent.isControlDown() ) {
					handleDragStyleChange( m_dragStyleCurrent, DragStyle.CONTROL_SHIFT );
				} else {
					if( m_dragStyleCurrent.isShiftDown() ) {
						//note: this should happen plenty, as key press events keep coming
						//pass
					} else {
						handleDragStyleChange( m_dragStyleCurrent, DragStyle.SHIFT );
					}
				}
			}
		}
	}

	private void removeFromDragStyleIfNecessary( int keyCode ) {
		if( m_dragStyleCurrent != null ) {
			if( keyCode == KeyEventUtilities.getQuoteControlUnquoteKey() ) {
				if( m_dragStyleCurrent.isControlDown() ) {
					if( m_dragStyleCurrent.isShiftDown() ) {
						handleDragStyleChange( m_dragStyleCurrent, DragStyle.SHIFT );
					} else {
						handleDragStyleChange( m_dragStyleCurrent, DragStyle.NORMAL );
					}
				} else {
					//note: this should not happen, as one only gets one key release event
					//pass
				}
			} else if( keyCode == KeyEvent.VK_SHIFT ) {
				if( m_dragStyleCurrent.isControlDown() ) {
					handleDragStyleChange( m_dragStyleCurrent, DragStyle.CONTROL );
				} else {
					if( m_dragStyleCurrent.isShiftDown() ) {
						handleDragStyleChange( m_dragStyleCurrent, DragStyle.NORMAL );
					} else {
						//note: this should not happen, as one only gets one key release event
						//pass
					}
				}
			}
		}
	}

	@Override
	public void keyPressed( KeyEvent e ) {
		addToDragStyleIfNecessary( e.getKeyCode() );
	}

	@Override
	public void keyReleased( KeyEvent e ) {
		removeFromDragStyleIfNecessary( e.getKeyCode() );
	}

	@Override
	public void keyTyped( KeyEvent e ) {
	}
}
