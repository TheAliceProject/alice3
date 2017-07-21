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

package org.lgna.story.implementation.eventhandling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SScene;
import org.lgna.story.Visual;
import org.lgna.story.event.MouseClickEvent;
import org.lgna.story.event.MouseClickOnObjectEvent;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenEvent;
import org.lgna.story.event.MouseClickOnScreenListener;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

/**
 * @author Matt May
 */
public class MouseClickedHandler extends AbstractEventHandler<Object, MouseClickEvent> {

	public static final Visual[] ALL_VISUALS = new Visual[ 0 ];
	private final Map<Object, CopyOnWriteArrayList<Object>> map = new ConcurrentHashMap<Object, CopyOnWriteArrayList<Object>>();
	private final Object empty = new Object();

	private boolean isMouseButtonListenerInExistence() {
		//		if( this.mouseButtonListeners.size() > 0 ) {
		//			return true;
		//		} else {
		//			for( Transformable component : this.getComponents() ) {
		//				if( component instanceof Model ) {
		//					Model model = (Model)component;
		//					if( model.getMouseButtonListeners().size() > 0 ) {
		//						return true;
		//					}
		//				}
		//			}
		//			return false;
		//		}
		return true;
	}

	@Override
	protected void fire( Object listener, MouseClickEvent event ) {
		if( listener instanceof MouseClickOnObjectListener ) {
			if( event.getModelAtMouseLocation() != null ) {
				MouseClickOnObjectListener mouseCOOL = (MouseClickOnObjectListener)listener;
				mouseCOOL.mouseClicked( (MouseClickOnObjectEvent)event );
			}
		} else if( listener instanceof MouseClickOnScreenListener ) {
			MouseClickOnScreenListener mouseCOSL = (MouseClickOnScreenListener)listener;
			mouseCOSL.mouseClicked( (MouseClickOnScreenEvent)event );
		} else {
			Logger.severe( listener );
		}
	}

	public MouseClickedHandler() {
		map.put( empty, new CopyOnWriteArrayList<Object>() );
		map.put( ALL_VISUALS, new CopyOnWriteArrayList<Object>() );
	}

	public void handleMouseQuoteClickedUnquote( java.awt.event.MouseEvent e, /* int quoteClickCountUnquote, */SScene scene ) {
		if( this.isMouseButtonListenerInExistence() ) {
			final org.lgna.story.event.MouseClickEventImp mbe = new org.lgna.story.event.MouseClickEventImp( e, scene );
			//			SModel model = mbe.getModelAtMouseLocation();
			//todo
			//			if( model != null ) {
			this.fireAllTargeted( mbe );
			//
			//				for( final org.lgna.story.event.MouseButtonListener mouseButtonListener : this.mouseButtonListeners ) {
			//					Logger.todo( "use parent tracking thread" );
			//					new Thread() {
			//						@Override
			//						public void run() {
			//							ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
			//								public void run() {
			//									mouseButtonListener.mouseButtonClicked( mbe );
			//								}
			//							} );
			//						}
			//					}.start();
			//				}
			//				for( final org.alice.apis.moveandturn.event.MouseButtonListener mouseButtonListener : model.getMouseButtonListeners() ) {
			//					new Thread() {
			//						@Override
			//						public void run() {
			//							edu.cmu.cs.dennisc.alice.ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
			//								public void run() {
			//									mouseButtonListener.mouseButtonClicked( mbe );
			//								}
			//							} );
			//						}
			//					}.start();
			//				}
			//			}
		}
	}

	public void fireAllTargeted( org.lgna.story.event.MouseClickEventImp event ) {
		if( shouldFire ) {
			if( event != null ) {
				CopyOnWriteArrayList<Object> listeners = new CopyOnWriteArrayList<Object>();
				listeners.addAll( map.get( empty ) );
				SModel modelAtMouseLocation = event.getModelAtMouseLocation();
				if( modelAtMouseLocation != null ) {
					listeners.addAll( map.get( ALL_VISUALS ) );
					if( map.get( modelAtMouseLocation ) != null ) {
						listeners.addAll( map.get( modelAtMouseLocation ) );
					}
				}
				if( listeners != null ) {
					for( Object listener : listeners ) {
						if( listener instanceof MouseClickOnScreenListener ) {
							fireEvent( listener, new MouseClickOnScreenEvent( event ) );
						} else if( listener instanceof MouseClickOnObjectListener ) {
							fireEvent( listener, new MouseClickOnObjectEvent( event ), modelAtMouseLocation );
						}
					}
				}
			}
		}
	}

	public void addListener( Object listener, MultipleEventPolicy eventPolicy, Visual[] targets ) {
		registerIsFiringMap( listener, targets );
		registerPolicyMap( listener, eventPolicy );
		if( listener instanceof MouseClickOnScreenListener ) {
			map.get( empty ).add( listener );
		} else if( targets.length > 0 ) {//targets should not be null
			for( Visual target : targets ) {
				if( map.get( target ) != null ) {
					map.get( target ).add( listener );
				} else {
					CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<Object>();
					list.add( listener );
					map.put( target, list );
				}
			}
		} else {
			map.get( ALL_VISUALS ).add( listener );
		}
	}
}
