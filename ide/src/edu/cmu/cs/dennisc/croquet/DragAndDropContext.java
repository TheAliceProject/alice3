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
public class DragAndDropContext extends ModelContext<DragAndDropModel> {
	public static abstract class DragAndDropEvent extends ModelEvent< DragAndDropContext > {
		private java.awt.event.MouseEvent mouseEvent;
		public DragAndDropEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private DragAndDropEvent( java.awt.event.MouseEvent mouseEvent ) {
			this.mouseEvent = mouseEvent;
		}
		public java.awt.event.MouseEvent getMouseEvent() {
			return this.mouseEvent;
		}
		@Override
		public State getState() {
			return null;
		}
	}
	public static abstract class DropReceptorEvent extends DragAndDropEvent {
		private DropReceptor dropReceptor;
		private CodableResolver< DropReceptor > dropReceptorResolver;
		public DropReceptorEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private DropReceptorEvent( java.awt.event.MouseEvent mouseEvent, DropReceptor dropReceptor ) {
			super( mouseEvent );
			this.dropReceptor = dropReceptor;
			if( this.dropReceptor != null ) {
				this.dropReceptorResolver = this.dropReceptor.getCodableResolver();
			} else {
				this.dropReceptorResolver = null;
			}
		}
		public DropReceptor getDropReceptor() {
			DropReceptor rv;
			if( this.dropReceptor != null ) {
				rv = this.dropReceptor;
			} else {
				if( this.dropReceptorResolver != null ) {
					rv = this.dropReceptorResolver.getResolved(); 
					if( this.dropReceptorResolver instanceof RetargetableResolver< ? > ) {
						//pass
					} else {
						this.dropReceptor = rv;
					}
				} else {
					rv = null;
				}
			}
			return rv;
		}
		@Override
		public State getState() {
			return null;
		}
		@Override
		protected void decodeInternal(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
			super.decodeInternal( binaryDecoder );
			this.dropReceptorResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
		}
		@Override
		protected void encodeInternal(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
			super.encodeInternal( binaryEncoder );
			binaryEncoder.encode( this.dropReceptorResolver );
		}

		@Override
		public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
			super.retarget( retargeter );
			if( this.dropReceptorResolver instanceof RetargetableResolver< ? > ) {
				RetargetableResolver< ? > retargetableResolver = (RetargetableResolver< ? >)this.dropReceptorResolver;
				retargetableResolver.retarget( retargeter );
			}
		}
	}
	public static class EnteredDropReceptorEvent extends DropReceptorEvent {
		public EnteredDropReceptorEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private EnteredDropReceptorEvent( java.awt.event.MouseEvent mouseEvent, DropReceptor dropReceptor ) {
			super( mouseEvent, dropReceptor );
		}
	}
	public static class ExitedDropReceptorEvent extends DropReceptorEvent {
		public ExitedDropReceptorEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private ExitedDropReceptorEvent( java.awt.event.MouseEvent mouseEvent, DropReceptor dropReceptor ) {
			super( mouseEvent, dropReceptor );
		}
	}

	
	public static abstract class PotentialDropSiteEvent extends DropReceptorEvent {
		private DropSite potentialDropSite;
		public PotentialDropSiteEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private PotentialDropSiteEvent( java.awt.event.MouseEvent mouseEvent, DropReceptor dropReceptor, DropSite potentialDropSite ) {
			super( mouseEvent, dropReceptor );
			this.potentialDropSite = potentialDropSite;
		}
		public DropSite getPotentialDropSite() {
			return this.potentialDropSite;
		}
		@Override
		public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
			super.retarget( retargeter );
			if( this.potentialDropSite instanceof RetargetableDropSite ) {
				RetargetableDropSite retargetablePotentialDropSite = (RetargetableDropSite)this.potentialDropSite;
				System.err.println( "pretarget: " + this.potentialDropSite );
				RetargetableDropSite replacement = retargetablePotentialDropSite.createReplacement( retargeter );
				retargeter.addKeyValuePair( this.potentialDropSite, replacement );
				this.potentialDropSite = replacement;
				System.err.println( "psttarget: " + this.potentialDropSite );
			}
		}
		@Override
		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super.decodeInternal( binaryDecoder );
			this.potentialDropSite = binaryDecoder.decodeBinaryEncodableAndDecodable();
		}
		@Override
		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			super.encodeInternal( binaryEncoder );
			binaryEncoder.encode( this.potentialDropSite );
		}
	}
	public static class EnteredPotentialDropSiteEvent extends PotentialDropSiteEvent {
		public EnteredPotentialDropSiteEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private EnteredPotentialDropSiteEvent( java.awt.event.MouseEvent mouseEvent, DropReceptor dropReceptor, DropSite potentialDropSite ) {
			super( mouseEvent, dropReceptor, potentialDropSite );
		}
	}
	public static class ExitedPotentialDropSiteEvent extends PotentialDropSiteEvent {
		public ExitedPotentialDropSiteEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private ExitedPotentialDropSiteEvent( java.awt.event.MouseEvent mouseEvent, DropReceptor dropReceptor, DropSite potentialDropSite ) {
			super( mouseEvent, dropReceptor, potentialDropSite );
		}
	}

	public static class DroppedEvent extends DropReceptorEvent {
		public DroppedEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		private DroppedEvent( DragAndDropContext parent, java.awt.event.MouseEvent mouseEvent, DropReceptor dropReceptor ) {
			super( mouseEvent, dropReceptor );
		}
	}

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
	private DropSite currentPotentialDropSite;
	private java.awt.event.MouseEvent latestMouseEvent;
	/*package-private*/ DragAndDropContext( DragAndDropModel dragAndDropOperation, java.awt.event.MouseEvent originalMouseEvent, java.awt.event.MouseEvent latestMouseEvent, DragComponent dragSource ) {
		super( dragAndDropOperation, originalMouseEvent, dragSource );
		this.setLatestMouseEvent( latestMouseEvent );
		java.util.List< ? extends DropReceptor > potentialDropReceptors = dragAndDropOperation.createListOfPotentialDropReceptors( dragSource );
		this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
		int i = 0;
		for( DropReceptor dropReceptor : potentialDropReceptors ) {
			Component<?> dropComponent = dropReceptor.getViewController();
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
	public DragAndDropContext( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
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
			if( this.currentPotentialDropSite != null ) {
				if( this.currentDropReceptor != null ) {
					this.addChild( new ExitedPotentialDropSiteEvent( e, this.currentDropReceptor, this.currentPotentialDropSite ) );
				}
			}
			if( this.currentDropReceptor != null ) {
				this.getModel().handleDragExitedDropReceptor( this );
				this.currentDropReceptor.dragExited( this, false );
				this.addChild( new ExitedDropReceptorEvent( e, this.currentDropReceptor ) );
			}
			this.currentDropReceptor = nextDropReceptor;
			if( this.currentDropReceptor != null ) {
				this.currentDropReceptor.dragEntered( this );
				this.getModel().handleDragEnteredDropReceptor( this );
				this.addChild( new EnteredDropReceptorEvent( e, this.currentDropReceptor ) );
			}
		}
		if( this.currentDropReceptor != null ) {
			DropSite nextPotentialDropSite = this.currentDropReceptor.dragUpdated( this );
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.currentPotentialDropSite, nextPotentialDropSite ) ) {
				//pass
			} else {
				if( this.currentPotentialDropSite != null ) {
					this.addChild( new ExitedPotentialDropSiteEvent( e, this.currentDropReceptor, this.currentPotentialDropSite ) );
				}
				this.currentPotentialDropSite = nextPotentialDropSite;
				if( this.currentPotentialDropSite != null ) {
					this.addChild( new EnteredPotentialDropSiteEvent( e, this.currentDropReceptor, this.currentPotentialDropSite ) );
				}
			}
		}

		if( this.getDragSource().getDragProxy() != null ) {
			this.getDragSource().getDragProxy().setOverDropAcceptor( this.currentDropReceptor != null );
		}
	}
	
	private void popContext( OperationContext< ? > childContext ) {
		System.err.println( "popContext" );
		ModelContext< ? > currentContext = ContextManager.getCurrentContext();
		if( childContext != null && childContext == currentContext ) {
			ContextManager.popParentContextWhenChildContextIsPopped( this, childContext );
		} else {
			ModelContext< ? > modelContext = ContextManager.popContext();
			assert modelContext == this;
		}
	}
	
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		if( this.isCanceled() ) {
			//pass
		} else {
			OperationContext< ? > childContext = null;
			this.setLatestMouseEvent( e );
			if( this.currentDropReceptor != null ) {
				Operation<?> operation = this.currentDropReceptor.dragDropped( this );
				if( operation != null ) {
					this.addChild( new DroppedEvent( this, e, this.currentDropReceptor ) );
					JComponent<?> component = this.currentDropReceptor.getViewController();
					ViewController<?,?> viewController; 
					if( component instanceof ViewController<?,?> ) {
						viewController = (ViewController<?,?>)component;
					} else {
						viewController = null;
					}
					childContext = operation.fire( this.getLatestMouseEvent(), viewController );
				} else {
					this.cancel();
				}
				this.currentDropReceptor.dragExited( this, true );
			} else {
				this.cancel();
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				dropReceptorInfo.getDropReceptor().dragStopped( this );
			}
			this.getModel().handleDragStopped( this );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
			this.popContext( childContext );
		}
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
		this.cancel();
		this.popContext( null );
	}

	@Override
	/*package-private*/ void popped() {
		super.popped();
		this.getDragSource().hideDropProxyIfNecessary();
	}
}
