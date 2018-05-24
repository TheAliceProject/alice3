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
package org.lgna.croquet.history;

import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Model;
import org.lgna.croquet.triggers.DragTrigger;
import org.lgna.croquet.triggers.DropTrigger;
import org.lgna.croquet.triggers.MouseEventTrigger;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.ViewController;
import org.lgna.croquet.views.imp.JDropProxy;

import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class DragStep extends PrepStep<DragModel> {
	private static class DropReceptorInfo {
		private DropReceptor dropReceptor;
		private Rectangle bounds;

		public DropReceptorInfo( DropReceptor dropReceptor, Rectangle bounds ) {
			this.dropReceptor = dropReceptor;
			this.bounds = bounds;
		}

		public boolean contains( int x, int y ) {
			return this.bounds.contains( x, y );
		}

		public boolean intersects( Rectangle rectangle ) {
			return this.bounds.intersects( rectangle );
		}

		public DropReceptor getDropReceptor() {
			return this.dropReceptor;
		}

		public void setDropReceptor( DropReceptor dropReceptor ) {
			this.dropReceptor = dropReceptor;
		}

		public Rectangle getBounds() {
			return this.bounds;
		}

		public void setBounds( Rectangle bounds ) {
			this.bounds = bounds;
		}
	}

	private DropReceptorInfo[] potentialDropReceptorInfos;
	private DropReceptor currentDropReceptor;
	private DropSite currentPotentialDropSite;
	private MouseEvent latestMouseEvent;

	public static DragStep createAndAddToTransaction( Transaction parent, DragModel model, DragTrigger trigger ) {
		return new DragStep( parent, model, trigger );
	}

	private DragStep( Transaction parent, DragModel model, DragTrigger trigger ) {
		super( parent, model, trigger );
	}

	private DropReceptorInfo[] getPotentialDropReceptorInfos() {
		if( this.potentialDropReceptorInfos != null ) {
			//pass
		} else {
			DragModel dragModel = this.getModel();
			List<? extends DropReceptor> potentialDropReceptors = dragModel.createListOfPotentialDropReceptors();
			this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
			int i = 0;
			for( DropReceptor dropReceptor : potentialDropReceptors ) {
				AwtComponentView<?> dropComponent = dropReceptor.getViewController();
				Rectangle bounds = dropComponent.getBounds();
				bounds = SwingUtilities.convertRectangle( dropComponent.getAwtComponent().getParent(), bounds, this.getDragSource().getAwtComponent() );
				this.potentialDropReceptorInfos[ i ] = new DropReceptorInfo( dropReceptor, bounds );
				i++;
			}
		}
		return this.potentialDropReceptorInfos;
	}

	public DragComponent<?> getDragSource() {
		return (DragComponent<?>)this.getViewController();
	}

	public MouseEvent getLatestMouseEvent() {
		return this.latestMouseEvent;
	}

	public void setLatestMouseEvent( MouseEvent e ) {
		this.latestMouseEvent = e;
	}

	public void fireDragStarted() {
		for( DropReceptorInfo dropReceptorInfo : this.getPotentialDropReceptorInfos() ) {
			//todo: pass original mouse pressed event?
			dropReceptorInfo.getDropReceptor().dragStarted( this );
		}
	}

	//	public java.awt.event.MouseEvent getOriginalMouseEvent() {
	//		return (java.awt.event.MouseEvent)this.getAwtEvent();
	//	}

	public DropReceptor getCurrentDropReceptor() {
		return this.currentDropReceptor;
	}

	//	public void setCurrentDropReceptor( org.lgna.croquet.DropReceptor currentDropReceptor ) {
	//		this.currentDropReceptor = currentDropReceptor;
	//	}

	public DropSite getCurrentPotentialDropSite() {
		return this.currentPotentialDropSite;
	}

	private DropReceptor getDropReceptorUnder( int x, int y ) {
		DropReceptor rv = null;
		int prevHeight = Integer.MAX_VALUE;
		for( DropReceptorInfo dropReceptorInfo : this.getPotentialDropReceptorInfos() ) {
			assert dropReceptorInfo != null;
			if( dropReceptorInfo.contains( x, y ) ) {
				Rectangle bounds = dropReceptorInfo.getBounds();
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

	private DropReceptor getDropReceptorUnder( Rectangle bounds ) {
		DropReceptor rv = null;
		int prevHeight = Integer.MAX_VALUE;
		for( DropReceptorInfo dropReceptorInfo : this.getPotentialDropReceptorInfos() ) {
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

	protected DropReceptor getDropReceptorUnder( MouseEvent e ) {
		DropReceptor rv = getDropReceptorUnder( e.getX(), e.getY() );
		if( rv != null ) {
			//pass
		} else {
			if( this.getDragSource().getDragProxy() != null ) {
				Rectangle dragBounds = this.getDragSource().getDragProxy().getBounds();
				dragBounds = SwingUtilities.convertRectangle( this.getDragSource().getDragProxy().getParent(), dragBounds, this.getDragSource().getAwtComponent() );
				int x = dragBounds.x;
				int y = dragBounds.y + ( dragBounds.height / 2 );
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

	public void handleMouseDragged( MouseEvent e ) {
		this.setLatestMouseEvent( e );
		DropReceptor nextDropReceptor = getDropReceptorUnder( e );
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
			DropSite nextPotentialDropSite = this.currentDropReceptor.dragUpdated( this );
			if( Objects.equals( this.currentPotentialDropSite, nextPotentialDropSite ) ) {
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

	public void handleMouseReleased( MouseEvent e ) {
		if( this.isCanceled() ) {
			this.cancel( e );
		} else {
			JDropProxy.Hider dropProxyHider = null;

			this.setLatestMouseEvent( e );
			if( this.currentDropReceptor != null ) {
				Model model = this.currentDropReceptor.dragDropped( this );

				//				org.lgna.croquet.history.TransactionManager.pendDrop( model, this.currentDropReceptor, this.currentPotentialDropSite );
				//				
				if( model != null ) {
					//this.addChild( new DroppedEvent( e, this.currentDropReceptor ) );
					SwingComponentView<?> component = this.currentDropReceptor.getViewController();
					ViewController<?, ?> viewController;
					if( component instanceof ViewController<?, ?> ) {
						viewController = (ViewController<?, ?>)component;
					} else {
						viewController = null;
					}
					try {
						if( model instanceof JDropProxy.Hider ) {
							dropProxyHider = (JDropProxy.Hider)model;
							dropProxyHider.setDragSource( this.getDragSource() );
						} else {
							Logger.outln( "drop proxy hider:", model.getClass() );
						}
						Step<?> step = model.fire( DropTrigger.createUserInstance( viewController, this.getLatestMouseEvent(), this.currentPotentialDropSite ) );
					} catch( CancelException ce ) {
						this.cancel( e );
					}
				} else {
					this.cancel( e );
				}
				this.currentDropReceptor.dragExited( this, true );
			} else {
				this.cancel( e );
			}
			for( DropReceptorInfo dropReceptorInfo : this.getPotentialDropReceptorInfos() ) {
				dropReceptorInfo.getDropReceptor().dragStopped( this );
			}
			this.getModel().handleDragStopped( this );
			this.potentialDropReceptorInfos = null;
			this.hideProxies( dropProxyHider );
		}
	}

	public void handleCancel( EventObject e ) {
		if( this.currentDropReceptor != null ) {
			this.currentDropReceptor.dragExited( this, false );
		}
		for( DropReceptorInfo dropReceptorInfo : this.getPotentialDropReceptorInfos() ) {
			dropReceptorInfo.getDropReceptor().dragStopped( this );
		}
		this.getModel().handleDragStopped( this );
		this.potentialDropReceptorInfos = null;
		this.cancel( null );
		this.hideProxies( null );
	}

	private void hideProxies( JDropProxy.Hider dropProxyHider ) {
		this.getDragSource().hideDragProxy();
		if( dropProxyHider != null ) {
			//pass
		} else {
			this.getDragSource().hideDropProxyIfNecessary();
		}
	}

	public boolean isCanceled() {
		//todo
		return false;
	}

	public void cancel( MouseEvent e ) {
		TransactionManager.addCancelCompletionStep( null, MouseEventTrigger.createUserInstance( e ) );
	}
}
