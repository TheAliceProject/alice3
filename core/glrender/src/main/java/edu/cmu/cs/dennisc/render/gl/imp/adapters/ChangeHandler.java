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
import edu.cmu.cs.dennisc.scenegraph.event.*;
import edu.cmu.cs.dennisc.texture.Texture;
import edu.cmu.cs.dennisc.texture.event.TextureEvent;
import edu.cmu.cs.dennisc.texture.event.TextureListener;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class ChangeHandler {
  public static void pushRenderingMode() {
    synchronized (renderingModeLock) {
      renderingModeCount--;
    }
  }

  public static void popRenderingMode() {
    synchronized (renderingModeLock) {
      if (renderingModeCount == -1) {
        handleBufferedChanges();
      }
      renderingModeCount++;
    }
  }

  private static boolean isInRenderingMode() {
    synchronized (renderingModeLock) {
      return renderingModeCount < 0;
    }
  }

  public static int getEventCountSinceLastReset() {
    return eventCount;
  }

  public static void resetEventCount() {
    eventCount = 0;
  }

  private static void handleEvent(Event<?> event) {
    switch (event) {
      case PropertyEvent propertyEvent -> GlrElement.handlePropertyChanged(propertyEvent.getTypedSource());
      case ReleaseEvent releaseEvent -> GlrObject.handleReleased(releaseEvent);
      case AbsoluteTransformationEvent absoluteTransformationEvent ->
          GlrComponent.handleAbsoluteTransformationChanged(absoluteTransformationEvent.getTypedSource());
      case HierarchyEvent hierarchyEvent -> GlrComponent.handleHierarchyChanged(hierarchyEvent);
      case ComponentAddedEvent componentAdded -> GlrComposite.handleComponentAdded(componentAdded);
      case ComponentRemovedEvent componentRemoved -> GlrComposite.handleComponentRemoved(componentRemoved);
      case GraphicAddedEvent graphicAdded -> GlrLayer.handleGraphicAdded(graphicAdded);
      case GraphicRemovedEvent graphicRemoved -> GlrLayer.handleGraphicRemoved(graphicRemoved);
      case TextureEvent textureEvent -> GlrTexture.handleTextureChanged(textureEvent);
      case null, default -> Logger.warning("UNHANDLED EVENT:", event);
    }
  }

  private static void handleOrBufferEvent(Event<?> event) {
    if (isInRenderingMode()) {
      synchronized (bufferedEvents) {
        bufferedEvents.add(event);
      }
    } else {
      synchronized (renderingModeLock) {
        handleEvent(event);
      }
      //      fireRepaint();
    }
    ChangeHandler.eventCount++;
  }

  public static void handleBufferedChanges() {
    synchronized (bufferedEvents) {
      for (Event<?> event : bufferedEvents) {
        handleEvent(event);
        //edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "handling buffered event", event );
      }
      bufferedEvents.clear();
    }
  }

  /*package-private*/
  static void addListeners(Releasable releasable) {
    releasable.addReleaseListener(releaseListener);
    if (releasable instanceof Element element) {
      element.addPropertyListener(propertyListener);
      if (releasable instanceof Component component) {
        component.addAbsoluteTransformationListener(absoluteTransformationListener);
        component.addHierarchyListener(hierarchyListener);
        if (releasable instanceof Composite composite) {
          composite.addChildrenListener(componentsListener);
        }
      } else if (releasable instanceof Layer layer) {
        layer.addGraphicsListener(graphicsListener);
      }
    } else if (releasable instanceof Texture texture) {
      texture.addTextureListener(textureListener);
    }
  }

  /*package-private*/
  static void removeListeners(Releasable releasable) {
    releasable.removeReleaseListener(releaseListener);
    if (releasable instanceof Element element) {
      element.removePropertyListener(propertyListener);
      if (releasable instanceof Component component) {
        component.removeAbsoluteTransformationListener(absoluteTransformationListener);
        component.removeHierarchyListener(hierarchyListener);
        if (releasable instanceof Composite composite) {
          composite.removeChildrenListener(componentsListener);
        }
      } else if (releasable instanceof Layer layer) {
        layer.removeGraphicsListener(graphicsListener);
      }
    } else if (releasable instanceof Texture texture) {
      texture.removeTextureListener(textureListener);
    }
  }

  private static final List<Event<?>> bufferedEvents = Lists.newLinkedList();

  private static int eventCount = 0;
  private static final Object renderingModeLock = new Object();
  private static int renderingModeCount;

  private static final ReleaseListener releaseListener = new ReleaseListener() {
    @Override
    public void releasing(ReleaseEvent e) {
    }

    @Override
    public void released(ReleaseEvent e) {
      handleOrBufferEvent(e);
    }
  };
  private static final PropertyListener propertyListener = ChangeHandler::handleOrBufferEvent;

  private static final ComponentsListener componentsListener = new ComponentsListener() {
    @Override
    public void componentAdded(ComponentAddedEvent e) {
      handleOrBufferEvent(e);
    }

    @Override
    public void componentRemoved(ComponentRemovedEvent e) {
      handleOrBufferEvent(e);
    }
  };
  private static final GraphicsListener graphicsListener = new GraphicsListener() {
    @Override
    public void graphicAdded(GraphicAddedEvent e) {
      handleOrBufferEvent(e);
    }

    @Override
    public void graphicRemoved(GraphicRemovedEvent e) {
      handleOrBufferEvent(e);
    }
  };
  private static final AbsoluteTransformationListener absoluteTransformationListener = ChangeHandler::handleOrBufferEvent;
  private static final HierarchyListener hierarchyListener = ChangeHandler::handleOrBufferEvent;
  private static final TextureListener textureListener = ChangeHandler::handleOrBufferEvent;
}
