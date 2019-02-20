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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Releasable;
import edu.cmu.cs.dennisc.pattern.event.Event;
import edu.cmu.cs.dennisc.pattern.event.ReleaseEvent;
import edu.cmu.cs.dennisc.pattern.event.ReleaseListener;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.Layer;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;
import edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent;
import edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent;
import edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener;
import edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent;
import edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent;
import edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener;
import edu.cmu.cs.dennisc.texture.Texture;
import edu.cmu.cs.dennisc.texture.event.TextureEvent;
import edu.cmu.cs.dennisc.texture.event.TextureListener;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class ChangeHandler {
	public static void pushRenderingMode() {
		synchronized( renderingModeLock ) {
			renderingModeCount--;
		}
	}

	public static void popRenderingMode() {
		synchronized( renderingModeLock ) {
			if( renderingModeCount == -1 ) {
				handleBufferedChanges();
			}
			renderingModeCount++;
		}
	}

	private static boolean isInRenderingMode() {
		synchronized( renderingModeLock ) {
			return renderingModeCount < 0;
		}
	}

	public static int getEventCountSinceLastReset() {
		return eventCount;
	}

	public static void resetEventCount() {
		eventCount = 0;
	}

	private static void handleEvent( Event<?> event ) {
		if( event instanceof PropertyEvent ) {
			PropertyEvent propertyEvent = (PropertyEvent)event;
			GlrElement.handlePropertyChanged( propertyEvent.getTypedSource() );
		} else if( event instanceof ReleaseEvent ) {
			GlrObject.handleReleased( (ReleaseEvent)event );
		} else if( event instanceof AbsoluteTransformationEvent ) {
			AbsoluteTransformationEvent absoluteTransformationEvent = (AbsoluteTransformationEvent)event;
			GlrComponent.handleAbsoluteTransformationChanged( absoluteTransformationEvent.getTypedSource() );
		} else if( event instanceof HierarchyEvent ) {
			GlrComponent.handleHierarchyChanged( (HierarchyEvent)event );
		} else if( event instanceof ComponentAddedEvent ) {
			GlrComposite.handleComponentAdded( (ComponentAddedEvent)event );
		} else if( event instanceof ComponentRemovedEvent ) {
			GlrComposite.handleComponentRemoved( (ComponentRemovedEvent)event );
		} else if( event instanceof GraphicAddedEvent ) {
			GlrLayer.handleGraphicAdded( (GraphicAddedEvent)event );
		} else if( event instanceof GraphicRemovedEvent ) {
			GlrLayer.handleGraphicRemoved( (GraphicRemovedEvent)event );
		} else if( event instanceof TextureEvent ) {
			GlrTexture.handleTextureChanged( (TextureEvent)event );
		} else {
			Logger.warning( "UNHANDLED EVENT:", event );
		}
	}

	private static void handleOrBufferEvent( Event<?> event ) {
		if( isInRenderingMode() ) {
			synchronized( bufferedEvents ) {
				bufferedEvents.add( event );
			}
		} else {
			synchronized( renderingModeLock ) {
				handleEvent( event );
			}
			//			fireRepaint();			
		}
		ChangeHandler.eventCount++;
	}

	public static void handleBufferedChanges() {
		synchronized( bufferedEvents ) {
			for( Event<?> event : bufferedEvents ) {
				handleEvent( event );
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "handling buffered event", event );
			}
			bufferedEvents.clear();
		}
	}

	/*package-private*/static void addListeners( Releasable element ) {
		element.addReleaseListener( releaseListener );
		if( element instanceof Element ) {
			( (Element)element ).addPropertyListener( propertyListener );
			if( element instanceof Component ) {
				( (Component)element ).addAbsoluteTransformationListener( absoluteTransformationListener );
				( (Component)element ).addHierarchyListener( hierarchyListener );
				if( element instanceof Composite ) {
					( (Composite)element ).addChildrenListener( componentsListener );
				}
			} else if( element instanceof Layer ) {
				( (Layer)element ).addGraphicsListener( graphicsListener );
			}
		} else if( element instanceof Texture ) {
			( (Texture)element ).addTextureListener( textureListener );
		}
	}

	/*package-private*/static void removeListeners( Releasable element ) {
		element.removeReleaseListener( releaseListener );
		if( element instanceof Element ) {
			( (Element)element ).removePropertyListener( propertyListener );
			if( element instanceof Component ) {
				( (Component)element ).removeAbsoluteTransformationListener( absoluteTransformationListener );
				( (Component)element ).removeHierarchyListener( hierarchyListener );
				if( element instanceof Composite ) {
					( (Composite)element ).removeChildrenListener( componentsListener );
				}
			} else if( element instanceof Layer ) {
				( (Layer)element ).removeGraphicsListener( graphicsListener );
			}
		} else if( element instanceof Texture ) {
			( (Texture)element ).removeTextureListener( textureListener );
		}
	}

	private static List<Event<?>> bufferedEvents = Lists.newLinkedList();

	private static int eventCount = 0;
	private static Object renderingModeLock = new Object();
	private static int renderingModeCount;

	private static final ReleaseListener releaseListener = new ReleaseListener() {
		@Override
		public void releasing( ReleaseEvent e ) {
		}

		@Override
		public void released( ReleaseEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static final PropertyListener propertyListener = new PropertyListener() {
		@Override
		public void propertyChanging( PropertyEvent e ) {
		}

		@Override
		public void propertyChanged( PropertyEvent e ) {
			handleOrBufferEvent( e );
		}
	};

	private static final ComponentsListener componentsListener = new ComponentsListener() {
		@Override
		public void componentAdded( ComponentAddedEvent e ) {
			handleOrBufferEvent( e );
		}

		@Override
		public void componentRemoved( ComponentRemovedEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static final GraphicsListener graphicsListener = new GraphicsListener() {
		@Override
		public void graphicAdded( GraphicAddedEvent e ) {
			handleOrBufferEvent( e );
		}

		@Override
		public void graphicRemoved( GraphicRemovedEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static final AbsoluteTransformationListener absoluteTransformationListener = new AbsoluteTransformationListener() {
		@Override
		public void absoluteTransformationChanged( AbsoluteTransformationEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static final HierarchyListener hierarchyListener = new HierarchyListener() {
		@Override
		public void hierarchyChanged( HierarchyEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static final TextureListener textureListener = new TextureListener() {
		@Override
		public void textureChanged( TextureEvent e ) {
			handleOrBufferEvent( e );
		}
	};
}
