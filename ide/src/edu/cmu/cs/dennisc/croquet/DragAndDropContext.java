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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public class DragAndDropContext extends ModelContext<DragAndDropOperation> {
	/**
	 * @author Dennis Cosgrove
	 */
	private static class DropReceptorInfo {
		private DropReceptor dropReceptor;
		private java.awt.Rectangle bounds;
		public DropReceptorInfo( DropReceptor dropReceptor, java.awt.Rectangle bounds ) {
			this.dropReceptor = dropReceptor;
			this.bounds = bounds;
		}
		public boolean contains( int x, int y ) {
			return this.bounds.contains( x, y );
		}
		public boolean intersects( java.awt.Rectangle rectangle ) {
			return this.bounds.intersects( rectangle );
		}
		public DropReceptor getDropReceptor() {
			return this.dropReceptor;
		}
		public void setDropReceptor( DropReceptor dropReceptor ) {
			this.dropReceptor = dropReceptor;
		}
		public java.awt.Rectangle getBounds() {
			return this.bounds;
		}
		public void setBounds( java.awt.Rectangle bounds ) {
			this.bounds = bounds;
		}
	}
	private DropReceptorInfo[] potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
	private DropReceptor currentDropReceptor;
	private java.awt.event.MouseEvent latestMouseEvent;

	public DragAndDropContext( ModelContext<?> parent, DragAndDropOperation dragAndDropOperation, java.awt.event.MouseEvent originalMouseEvent, java.awt.event.MouseEvent latestMouseEvent, DragComponent dragSource ) {
		super( parent, dragAndDropOperation, originalMouseEvent, dragSource );
		this.setLatestMouseEvent( latestMouseEvent );
		java.util.List< ? extends DropReceptor > potentialDropReceptors = dragAndDropOperation.createListOfPotentialDropReceptors( dragSource );
		this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
		int i = 0;
		for( DropReceptor dropReceptor : potentialDropReceptors ) {
			Component<?> dropComponent = dropReceptor.getComponent();
			java.awt.Rectangle bounds = dropComponent.getBounds();
			bounds = javax.swing.SwingUtilities.convertRectangle( dropComponent.getAwtComponent().getParent(), bounds, this.getDragSource().getAwtComponent() );
			this.potentialDropReceptorInfos[ i ] = new DropReceptorInfo( dropReceptor, bounds );
			i++;
		}
		for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
			//todo: pass original mouse pressed event?
			dropReceptorInfo.getDropReceptor().dragStarted( this );
		}
	}

	public java.awt.event.MouseEvent getOriginalMouseEvent() {
		return (java.awt.event.MouseEvent)this.getAwtEvent();
	}
	public java.awt.event.MouseEvent getLatestMouseEvent() {
		return this.latestMouseEvent;
	}
	public void setLatestMouseEvent( java.awt.event.MouseEvent latestMouseEvent ) {
		this.latestMouseEvent = latestMouseEvent;
	}

	public DragComponent getDragSource() {
		return (DragComponent)this.getViewController();
	}
	public DropReceptor getCurrentDropReceptor() {
		return this.currentDropReceptor;
	}
	public void setCurrentDropReceptor( DropReceptor currentDropReceptor ) {
		this.currentDropReceptor = currentDropReceptor;
	}
	private DropReceptor getDropReceptorUnder( int x, int y ) {
		DropReceptor rv = null;
		int prevHeight = Integer.MAX_VALUE;
		for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
			assert dropReceptorInfo != null;
			if( dropReceptorInfo.contains( x, y ) ) {
				java.awt.Rectangle bounds = dropReceptorInfo.getBounds();
				assert bounds != null;
				int nextHeight = bounds.height;
				if( nextHeight < prevHeight ) {
					rv = dropReceptorInfo.getDropReceptor();
					prevHeight = nextHeight;
				}
			}
		}
		return rv;
	}
	private DropReceptor getDropReceptorUnder( java.awt.Rectangle bounds ) {
		DropReceptor rv = null;
		int prevHeight = Integer.MAX_VALUE;
		for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
			if( dropReceptorInfo.intersects( bounds ) ) {
				int nextHeight = dropReceptorInfo.getBounds().height;
				if( nextHeight < prevHeight ) {
					rv = dropReceptorInfo.getDropReceptor();
					prevHeight = nextHeight;
				}
			}
		}
		return rv;
	}
	protected DropReceptor getDropReceptorUnder( java.awt.event.MouseEvent e ) {
		DropReceptor rv = getDropReceptorUnder( e.getX(), e.getY() );
		if( rv != null ) {
			//pass
		} else {
			if( this.getDragSource().getDragProxy() != null ) {
				java.awt.Rectangle dragBounds = this.getDragSource().getDragProxy().getBounds();
				dragBounds = javax.swing.SwingUtilities.convertRectangle( this.getDragSource().getDragProxy().getParent(), dragBounds, this.getDragSource().getAwtComponent() );
				int x = dragBounds.x;
				int y = dragBounds.y + dragBounds.height / 2;
				rv = getDropReceptorUnder( x, y );
				if( rv != null ) {
					//pass
				} else {
					rv = getDropReceptorUnder( dragBounds );
				}
			}
		}
		return rv;
	}
	public void handleMouseDragged( java.awt.event.MouseEvent e ) {
		this.setLatestMouseEvent( e );
		DropReceptor nextDropReceptor = getDropReceptorUnder( e );
		if( this.currentDropReceptor != nextDropReceptor ) {
			if( this.currentDropReceptor != null ) {
				this.getModel().handleDragExitedDropReceptor( this );
				this.currentDropReceptor.dragExited( this, false );
			}
			this.currentDropReceptor = nextDropReceptor;
			if( this.currentDropReceptor != null ) {
				this.currentDropReceptor.dragEntered( this );
				this.getModel().handleDragEnteredDropReceptor( this );
			}
		}
		if( this.getDragSource().getDragProxy() != null ) {
			this.getDragSource().getDragProxy().setOverDropAcceptor( this.currentDropReceptor != null );
		}
		if( this.currentDropReceptor != null ) {
			this.currentDropReceptor.dragUpdated( this );
		}
	}
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		this.setLatestMouseEvent( e );
		if( this.currentDropReceptor != null ) {
			this.currentDropReceptor.dragDropped( this );
			this.currentDropReceptor.dragExited( this, true );
		}
		for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
			dropReceptorInfo.getDropReceptor().dragStopped( this );
		}
		this.getModel().handleDragStopped( this );
		this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
	}
	public void handleCancel( java.util.EventObject e ) {
		if( this.currentDropReceptor != null ) {
			this.currentDropReceptor.dragExited( this, false );
		}
		for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
			dropReceptorInfo.getDropReceptor().dragStopped( this );
		}
		this.getModel().handleDragStopped( this );
		this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
		this.getDragSource().hideDropProxyIfNecessary();
	}
}
