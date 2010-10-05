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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public abstract class ChangeHandler {
	//private static java.util.concurrent.Semaphore s_semaphore = new java.util.concurrent.Semaphore( 1 );
	private static java.util.List< edu.cmu.cs.dennisc.pattern.event.Event< ? > > s_events = new java.util.LinkedList< edu.cmu.cs.dennisc.pattern.event.Event< ? >>();

	private static Object s_object = new Object();
	private static int s_count;

	public static void pushRenderingMode() {
		synchronized( s_object ) {
			s_count--;
		}
	}

	public static void popRenderingMode() {
		if( s_count == -1 ) {
			synchronized( s_object ) {
				handleBufferedChanges();
				s_count++;
			}
		}
	}

	private static boolean isInRenderingMode() {
		//todo
		return false;
		//		synchronized( s_object ) {
		//			return s_count < 0;
		//		}
	}

	private static int eventCount = 0;
	
	public static int getEventCountSinceLastReset() {
		return ChangeHandler.eventCount;
	}
	public static void resetEventCount() {
		ChangeHandler.eventCount = 0;
	}
	
	private static void handleEvent( edu.cmu.cs.dennisc.pattern.event.Event< ? > event ) {
		if( event instanceof edu.cmu.cs.dennisc.property.event.PropertyEvent ) {
			edu.cmu.cs.dennisc.property.event.PropertyEvent propertyEvent = (edu.cmu.cs.dennisc.property.event.PropertyEvent)event;
			ElementAdapter.handlePropertyChanged( (edu.cmu.cs.dennisc.property.InstanceProperty<?>)propertyEvent.getTypedSource() );
		} else if( event instanceof edu.cmu.cs.dennisc.pattern.event.ReleaseEvent ) {
			ElementAdapter.handleReleased( (edu.cmu.cs.dennisc.pattern.event.ReleaseEvent)event );
		} else if( event instanceof edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent ) {
			edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent = (edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent)event;
			ComponentAdapter.handleAbsoluteTransformationChanged( absoluteTransformationEvent.getTypedSource() );
		} else if( event instanceof edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent ) {
			ComponentAdapter.handleHierarchyChanged( (edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent)event );
		} else if( event instanceof edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent ) {
			CompositeAdapter.handleComponentAdded( (edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent)event );
		} else if( event instanceof edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent ) {
			CompositeAdapter.handleComponentRemoved( (edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent)event );
		} else if( event instanceof edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent ) {
			LayerAdapter.handleGraphicAdded( (edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent)event );
		} else if( event instanceof edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent ) {
			LayerAdapter.handleGraphicRemoved( (edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent)event );
		} else if( event instanceof edu.cmu.cs.dennisc.texture.event.TextureEvent ) {
			TextureAdapter.handleTextureChanged( (edu.cmu.cs.dennisc.texture.event.TextureEvent)event );
		} else {
			System.err.println( "UNHANDLED EVENT: " + event );
		}
	}
	private static void handleOrBufferEvent( edu.cmu.cs.dennisc.pattern.event.Event< ? > event ) {
		if( isInRenderingMode() ) {
			synchronized( s_events ) {
				s_events.add( event );
			}
		} else {
			synchronized( s_object ) {
				handleEvent( event );
			}
			//			fireRepaint();			
		}
		ChangeHandler.eventCount ++;
	}

	public static void handleBufferedChanges() {
		synchronized( s_events ) {
			for( edu.cmu.cs.dennisc.pattern.event.Event<?> event : s_events ) {
				handleEvent( event );
			}
		}
	}

	private static edu.cmu.cs.dennisc.property.event.PropertyListener s_propertyListener = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			handleOrBufferEvent( e );
		}
	};

	private static edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener s_childrenListener = new edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener() {
		public void componentAdded( edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent e ) {
			handleOrBufferEvent( e );
		}
		public void componentRemoved( edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener s_graphicsListener = new edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener() {
		public void graphicAdded( edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent e ) {
			handleOrBufferEvent( e );
		}
		public void graphicRemoved( edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener s_absoluteTransformationListener = new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener() {
		public void absoluteTransformationChanged( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener s_hierarchyListener = new edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener() {
		public void hierarchyChanged( edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static edu.cmu.cs.dennisc.texture.event.TextureListener s_textureListener = new edu.cmu.cs.dennisc.texture.event.TextureListener() {
		public void textureChanged( edu.cmu.cs.dennisc.texture.event.TextureEvent e ) {
			handleOrBufferEvent( e );
		}
	};
	private static edu.cmu.cs.dennisc.pattern.event.ReleaseListener s_releaseListener = new edu.cmu.cs.dennisc.pattern.event.ReleaseListener() {
		public void releasing( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent e ) {
		}
		public void released( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent e ) {
			handleOrBufferEvent( e );
		}
	};

	public static void addListenersAndObservers( edu.cmu.cs.dennisc.pattern.AbstractElement element ) {
		element.addReleaseListener( s_releaseListener );
		if( element instanceof edu.cmu.cs.dennisc.scenegraph.Element ) {
			((edu.cmu.cs.dennisc.scenegraph.Element)element).addPropertyListener( s_propertyListener );
			if( element instanceof edu.cmu.cs.dennisc.scenegraph.Component ) {
				((edu.cmu.cs.dennisc.scenegraph.Component)element).addAbsoluteTransformationListener( s_absoluteTransformationListener );
				((edu.cmu.cs.dennisc.scenegraph.Component)element).addHierarchyListener( s_hierarchyListener );
				if( element instanceof edu.cmu.cs.dennisc.scenegraph.Composite ) {
					((edu.cmu.cs.dennisc.scenegraph.Composite)element).addChildrenListener( s_childrenListener );
				}
			} else if( element instanceof edu.cmu.cs.dennisc.scenegraph.Layer ) {
				((edu.cmu.cs.dennisc.scenegraph.Layer)element).addGraphicsListener( s_graphicsListener );
			}
		} else if( element instanceof edu.cmu.cs.dennisc.texture.Texture ) {
			((edu.cmu.cs.dennisc.texture.Texture)element).addTextureListener( s_textureListener );
		}
	}
	public static void removeListenersAndObservers( edu.cmu.cs.dennisc.pattern.AbstractElement element ) {
		element.removeReleaseListener( s_releaseListener );
		if( element instanceof edu.cmu.cs.dennisc.scenegraph.Element ) {
			((edu.cmu.cs.dennisc.scenegraph.Element)element).removePropertyListener( s_propertyListener );
			if( element instanceof edu.cmu.cs.dennisc.scenegraph.Component ) {
				((edu.cmu.cs.dennisc.scenegraph.Component)element).removeAbsoluteTransformationListener( s_absoluteTransformationListener );
				((edu.cmu.cs.dennisc.scenegraph.Component)element).removeHierarchyListener( s_hierarchyListener );
				if( element instanceof edu.cmu.cs.dennisc.scenegraph.Composite ) {
					((edu.cmu.cs.dennisc.scenegraph.Composite)element).removeChildrenListener( s_childrenListener );
				}
			} else if( element instanceof edu.cmu.cs.dennisc.scenegraph.Layer ) {
				((edu.cmu.cs.dennisc.scenegraph.Layer)element).removeGraphicsListener( s_graphicsListener );
			}
		} else if( element instanceof edu.cmu.cs.dennisc.texture.Texture ) {
			((edu.cmu.cs.dennisc.texture.Texture)element).removeTextureListener( s_textureListener );
		}
	}
}
