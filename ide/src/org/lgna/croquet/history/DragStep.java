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
package org.lgna.croquet.history;

/**
 * @author Dennis Cosgrove
 */
public class DragStep extends PrepStep< org.lgna.croquet.DragModel > {
//	public static abstract class DragAndDropEvent extends ModelEvent< DragAndDropContext > {
//		private java.awt.event.MouseEvent mouseEvent;
//		public DragAndDropEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private DragAndDropEvent( java.awt.event.MouseEvent mouseEvent ) {
//			this.mouseEvent = mouseEvent;
//		}
//		public java.awt.event.MouseEvent getMouseEvent() {
//			return this.mouseEvent;
//		}
//		@Override
//		public State getState() {
//			return null;
//		}
//	}
//	public static abstract class DropReceptorEvent extends DragAndDropEvent {
//		private org.lgna.croquet.DropReceptor dropReceptor;
//		private CodableResolver< org.lgna.croquet.DropReceptor > dropReceptorResolver;
//		public DropReceptorEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private DropReceptorEvent( java.awt.event.MouseEvent mouseEvent, org.lgna.croquet.DropReceptor dropReceptor ) {
//			super( mouseEvent );
//			this.dropReceptor = dropReceptor;
//			if( this.dropReceptor != null ) {
//				this.dropReceptorResolver = this.dropReceptor.getCodableResolver();
//			} else {
//				this.dropReceptorResolver = null;
//			}
//		}
//		public org.lgna.croquet.DropReceptor getDropReceptor() {
//			org.lgna.croquet.DropReceptor rv;
//			if( this.dropReceptor != null ) {
//				rv = this.dropReceptor;
//			} else {
//				if( this.dropReceptorResolver != null ) {
//					rv = this.dropReceptorResolver.getResolved(); 
//					if( this.dropReceptorResolver instanceof RetargetableResolver< ? > ) {
//						//pass
//					} else {
//						this.dropReceptor = rv;
//					}
//				} else {
//					rv = null;
//				}
//			}
//			return rv;
//		}
//		@Override
//		public State getState() {
//			return null;
//		}
//		@Override
//		protected void decodeInternal(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
//			super.decodeInternal( binaryDecoder );
//			this.dropReceptorResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
//		}
//		@Override
//		protected void encodeInternal(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
//			super.encodeInternal( binaryEncoder );
//			binaryEncoder.encode( this.dropReceptorResolver );
//		}
//
//		@Override
//		public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
//			super.retarget( retargeter );
//			if( this.dropReceptorResolver instanceof RetargetableResolver< ? > ) {
//				RetargetableResolver< ? > retargetableResolver = (RetargetableResolver< ? >)this.dropReceptorResolver;
//				retargetableResolver.retarget( retargeter );
//			}
//		}
//	}
//	public static class EnteredDropReceptorEvent extends DropReceptorEvent {
//		public EnteredDropReceptorEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private EnteredDropReceptorEvent( java.awt.event.MouseEvent mouseEvent, org.lgna.croquet.DropReceptor dropReceptor ) {
//			super( mouseEvent, dropReceptor );
//		}
//		public EnteredDropReceptorEvent( org.lgna.croquet.DropReceptor dropReceptor ) {
//			this( null, dropReceptor );
//		}
//	}
//	public static class ExitedDropReceptorEvent extends DropReceptorEvent {
//		public ExitedDropReceptorEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private ExitedDropReceptorEvent( java.awt.event.MouseEvent mouseEvent, org.lgna.croquet.DropReceptor dropReceptor ) {
//			super( mouseEvent, dropReceptor );
//		}
//		public ExitedDropReceptorEvent( org.lgna.croquet.DropReceptor dropReceptor ) {
//			this( null, dropReceptor );
//		}
//	}
//
//	
//	public static abstract class PotentialDropSiteEvent extends DropReceptorEvent {
//		private DropSite potentialDropSite;
//		public PotentialDropSiteEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private PotentialDropSiteEvent( java.awt.event.MouseEvent mouseEvent, org.lgna.croquet.DropReceptor dropReceptor, DropSite potentialDropSite ) {
//			super( mouseEvent, dropReceptor );
//			this.potentialDropSite = potentialDropSite;
//		}
//		public DropSite getPotentialDropSite() {
//			return this.potentialDropSite;
//		}
//		@Override
//		public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
//			super.retarget( retargeter );
//			if( this.potentialDropSite instanceof RetargetableDropSite ) {
//				RetargetableDropSite retargetablePotentialDropSite = (RetargetableDropSite)this.potentialDropSite;
//				//System.err.println( "pretarget: " + this.potentialDropSite );
//				RetargetableDropSite replacement = retargetablePotentialDropSite.createReplacement( retargeter );
//				retargeter.addKeyValuePair( this.potentialDropSite, replacement );
//				this.potentialDropSite = replacement;
//				//System.err.println( "psttarget: " + this.potentialDropSite );
//			}
//		}
//		@Override
//		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super.decodeInternal( binaryDecoder );
//			this.potentialDropSite = binaryDecoder.decodeBinaryEncodableAndDecodable();
//		}
//		@Override
//		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
//			super.encodeInternal( binaryEncoder );
//			binaryEncoder.encode( this.potentialDropSite );
//		}
//	}
//	public static class EnteredPotentialDropSiteEvent extends PotentialDropSiteEvent {
//		public EnteredPotentialDropSiteEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private EnteredPotentialDropSiteEvent( java.awt.event.MouseEvent mouseEvent, org.lgna.croquet.DropReceptor dropReceptor, DropSite potentialDropSite ) {
//			super( mouseEvent, dropReceptor, potentialDropSite );
//		}
//		public EnteredPotentialDropSiteEvent( org.lgna.croquet.DropReceptor dropReceptor, DropSite potentialDropSite ) {
//			this( null, dropReceptor, potentialDropSite );
//		}
//	}
//	public static class ExitedPotentialDropSiteEvent extends PotentialDropSiteEvent {
//		public ExitedPotentialDropSiteEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private ExitedPotentialDropSiteEvent( java.awt.event.MouseEvent mouseEvent, org.lgna.croquet.DropReceptor dropReceptor, DropSite potentialDropSite ) {
//			super( mouseEvent, dropReceptor, potentialDropSite );
//		}
//		public ExitedPotentialDropSiteEvent( org.lgna.croquet.DropReceptor dropReceptor, DropSite potentialDropSite ) {
//			this( null, dropReceptor, potentialDropSite );
//		}
//	}
//
//	public static class DroppedEvent extends DropReceptorEvent {
//		public DroppedEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//			super( binaryDecoder );
//		}
//		private DroppedEvent( java.awt.event.MouseEvent mouseEvent, org.lgna.croquet.DropReceptor dropReceptor ) {
//			super( mouseEvent, dropReceptor );
//		}
//		public DroppedEvent( org.lgna.croquet.DropReceptor dropReceptor ) {
//			this( null, dropReceptor );
//		}
//	}

	private static class DropReceptorInfo {
		private org.lgna.croquet.DropReceptor dropReceptor;
		private java.awt.Rectangle bounds;
		public DropReceptorInfo( org.lgna.croquet.DropReceptor dropReceptor, java.awt.Rectangle bounds ) {
			this.dropReceptor = dropReceptor;
			this.bounds = bounds;
		}
		public boolean contains( int x, int y ) {
			return this.bounds.contains( x, y );
		}
		public boolean intersects( java.awt.Rectangle rectangle ) {
			return this.bounds.intersects( rectangle );
		}
		public org.lgna.croquet.DropReceptor getDropReceptor() {
			return this.dropReceptor;
		}
		public void setDropReceptor( org.lgna.croquet.DropReceptor dropReceptor ) {
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
	private org.lgna.croquet.DropReceptor currentDropReceptor;
	private org.lgna.croquet.DropSite currentPotentialDropSite;
	private java.awt.event.MouseEvent latestMouseEvent;
	public static DragStep createAndAddToTransaction( Transaction parent, org.lgna.croquet.DragModel model, org.lgna.croquet.triggers.Trigger trigger ) {
		return new DragStep( parent, model, trigger );
	}
	private DragStep( Transaction parent, org.lgna.croquet.DragModel model, org.lgna.croquet.triggers.Trigger trigger ) {
		super( parent, model, trigger );
		org.lgna.croquet.components.DragComponent dragSource = this.getDragSource();
		java.util.List< ? extends org.lgna.croquet.DropReceptor > potentialDropReceptors = model.createListOfPotentialDropReceptors( dragSource );
		this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
		int i = 0;
		for( org.lgna.croquet.DropReceptor dropReceptor : potentialDropReceptors ) {
			org.lgna.croquet.components.Component<?> dropComponent = dropReceptor.getViewController();
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
	public DragStep( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	
	public org.lgna.croquet.components.DragComponent getDragSource() {
		return (org.lgna.croquet.components.DragComponent)this.getViewController();
	}
	public java.awt.event.MouseEvent getLatestMouseEvent() {
		return this.latestMouseEvent;
	}
	public void setLatestMouseEvent( java.awt.event.MouseEvent e ) {
		this.latestMouseEvent = e;
	}

//	public java.awt.event.MouseEvent getOriginalMouseEvent() {
//		return (java.awt.event.MouseEvent)this.getAwtEvent();
//	}

	public org.lgna.croquet.DropReceptor getCurrentDropReceptor() {
		return this.currentDropReceptor;
	}
	public void setCurrentDropReceptor( org.lgna.croquet.DropReceptor currentDropReceptor ) {
		this.currentDropReceptor = currentDropReceptor;
	}
	private org.lgna.croquet.DropReceptor getDropReceptorUnder( int x, int y ) {
		org.lgna.croquet.DropReceptor rv = null;
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
	private org.lgna.croquet.DropReceptor getDropReceptorUnder( java.awt.Rectangle bounds ) {
		org.lgna.croquet.DropReceptor rv = null;
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
	protected org.lgna.croquet.DropReceptor getDropReceptorUnder( java.awt.event.MouseEvent e ) {
		org.lgna.croquet.DropReceptor rv = getDropReceptorUnder( e.getX(), e.getY() );
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
		org.lgna.croquet.DropReceptor nextDropReceptor = getDropReceptorUnder( e );
		if( this.currentDropReceptor != nextDropReceptor ) {
			if( this.currentPotentialDropSite != null ) {
				if( this.currentDropReceptor != null ) {
					//this.addChild( new ExitedPotentialDropSiteEvent( e, this.currentDropReceptor, this.currentPotentialDropSite ) );
				}
			}
			if( this.currentDropReceptor != null ) {
				this.getModel().handleDragExitedDropReceptor( this );
				this.currentDropReceptor.dragExited( this, false );
				//this.addChild( new ExitedDropReceptorEvent( e, this.currentDropReceptor ) );
			}
			this.currentDropReceptor = nextDropReceptor;
			if( this.currentDropReceptor != null ) {
				this.currentDropReceptor.dragEntered( this );
				this.getModel().handleDragEnteredDropReceptor( this );
				//this.addChild( new EnteredDropReceptorEvent( e, this.currentDropReceptor ) );
			}
		}
		if( this.currentDropReceptor != null ) {
			org.lgna.croquet.DropSite nextPotentialDropSite = this.currentDropReceptor.dragUpdated( this );
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.currentPotentialDropSite, nextPotentialDropSite ) ) {
				//pass
			} else {
				if( this.currentPotentialDropSite != null ) {
					//this.addChild( new ExitedPotentialDropSiteEvent( e, this.currentDropReceptor, this.currentPotentialDropSite ) );
				}
				this.currentPotentialDropSite = nextPotentialDropSite;
				if( this.currentPotentialDropSite != null ) {
					//this.addChild( new EnteredPotentialDropSiteEvent( e, this.currentDropReceptor, this.currentPotentialDropSite ) );
				}
			}
		}

		if( this.getDragSource().getDragProxy() != null ) {
			this.getDragSource().getDragProxy().setOverDropAcceptor( this.currentDropReceptor != null );
		}
	}
	
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		org.lgna.croquet.triggers.Trigger trigger = new org.lgna.croquet.triggers.MouseEventTrigger( this.getDragSource(), e );
		if( this.isCanceled() ) {
			this.cancel( trigger );
		} else {
			this.setLatestMouseEvent( e );
			if( this.currentDropReceptor != null ) {
				org.lgna.croquet.Model model = this.currentDropReceptor.dragDropped( this );

//				org.lgna.croquet.history.TransactionManager.pendDrop( model, this.currentDropReceptor, this.currentPotentialDropSite );
//				
				if( model != null ) {
					//this.addChild( new DroppedEvent( e, this.currentDropReceptor ) );
					org.lgna.croquet.components.JComponent<?> component = this.currentDropReceptor.getViewController();
					org.lgna.croquet.components.ViewController<?,?> viewController;
					if( component instanceof org.lgna.croquet.components.ViewController<?,?> ) {
						viewController = (org.lgna.croquet.components.ViewController<?,?>)component;
					} else {
						viewController = null;
					}
					try {
						org.lgna.croquet.history.Step< ? > step = model.fire( new org.lgna.croquet.triggers.DropTrigger( viewController, this.getLatestMouseEvent(), this.currentDropReceptor, this.currentPotentialDropSite ) );
					} catch( org.lgna.croquet.CancelException ce ) {
						this.cancelTransaction();
					}
				} else {
					this.cancel( trigger );
				}
				this.currentDropReceptor.dragExited( this, true );
			} else {
				this.cancel( trigger );
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				dropReceptorInfo.getDropReceptor().dragStopped( this );
			}
			this.getModel().handleDragStopped( this );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
			this.hideProxies();
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
		this.cancel( null );
		this.hideProxies();
	}

	private void hideProxies() {
		this.getDragSource().hideDragProxy();
		this.getDragSource().hideDropProxyIfNecessary();
	}
	
	public boolean isCanceled() {
		//todo
		return false;
	}
	public void cancel( org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.TransactionManager.addCancelCompletionStep( null, trigger );
	}
}
