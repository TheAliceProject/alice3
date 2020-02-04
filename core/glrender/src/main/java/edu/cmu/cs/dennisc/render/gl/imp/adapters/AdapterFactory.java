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

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Releasable;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.adorn.GlrStickFigure;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.Appearance;
import edu.cmu.cs.dennisc.scenegraph.Background;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Ghost;
import edu.cmu.cs.dennisc.scenegraph.Graphic;
import edu.cmu.cs.dennisc.scenegraph.Layer;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.ProjectionCamera;
import edu.cmu.cs.dennisc.scenegraph.Scene;
import edu.cmu.cs.dennisc.scenegraph.Silhouette;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.adorn.StickFigure;
import edu.cmu.cs.dennisc.scenegraph.graphics.Text;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.CustomTexture;
import edu.cmu.cs.dennisc.texture.Texture;

import java.lang.reflect.Array;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class AdapterFactory {
  private static final Map<Releasable, GlrObject<? extends Releasable>> s_elementToAdapterMap = Maps.newHashMap();
  private static final Map<Class<? extends Releasable>, Class<? extends GlrObject<? extends Releasable>>> s_classToAdapterClassMap = Maps.newHashMap();

  private static final String SCENEGRAPH_PACKAGE_NAME = Element.class.getPackage().getName();
  private static final String RENDERER_PACKAGE_NAME = GlrElement.class.getPackage().getName();
  private static final String SCENEGRAPH_GRAPHICS_PACKAGE_NAME = Text.class.getPackage().getName();
  private static final String RENDERER_GRAPHICS_PACKAGE_NAME = edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics.GlrText.class.getPackage().getName();
  private static final String SCENEGRAPH_ADORN_PACKAGE_NAME = StickFigure.class.getPackage().getName();
  private static final String RENDERER_ADORN_PACKAGE_NAME = GlrStickFigure.class.getPackage().getName();

  static {
    register(BufferedImageTexture.class, GlrBufferedImageTexture.class);
  }

  private AdapterFactory() {
    throw new AssertionError();
  }

  public static <SG extends Releasable, GLR extends GlrObject<SG>> void register(Class<SG> sgClass, Class<GLR> adapterClass) {
    s_classToAdapterClassMap.put(sgClass, adapterClass);
  }

  private static void createNecessaryProxies(Releasable sgElement) {
    GlrObject<?> unused = getAdapterForElement(sgElement);
    if (sgElement instanceof Composite) {
      Composite sgComposite = (Composite) sgElement;
      for (Component sgComponent : sgComposite.getComponents()) {
        createNecessaryProxies(sgComponent);
      }
    }
  }

  private static <SG extends Releasable, GLR extends GlrObject<SG>> GLR createAdapterFor(SG sgElement) {
    Class sgClass = sgElement.getClass();
    Class cls = s_classToAdapterClassMap.get(sgClass);
    if (cls != null) {
      //pass
    } else {
      StringBuffer sb = new StringBuffer();
      while (sgClass != null) {
        Package sgPackage = sgClass.getPackage();
        if ((sgPackage != null) && sgPackage.getName().equals(SCENEGRAPH_PACKAGE_NAME)) {
          sb.append(RENDERER_PACKAGE_NAME);
          break;
        } else if (sgClass == CustomTexture.class) {
          sb.append(RENDERER_PACKAGE_NAME);
          break;
        } else if ((sgPackage != null) && sgPackage.getName().equals(SCENEGRAPH_GRAPHICS_PACKAGE_NAME)) {
          sb.append(RENDERER_GRAPHICS_PACKAGE_NAME);
          break;
        } else if ((sgPackage != null) && sgPackage.getName().equals(SCENEGRAPH_ADORN_PACKAGE_NAME)) {
          sb.append(RENDERER_ADORN_PACKAGE_NAME);
          break;
        } else {
          sgClass = sgClass.getSuperclass();
        }
      }
      assert sgClass != null;
      sb.append('.');
      sb.append("Glr");
      sb.append(sgClass.getSimpleName());
      cls = ReflectionUtilities.getClassForName(sb.toString());
      if (cls != null) {
        register(sgClass, cls);
      }
    }
    GLR rv;
    if (cls != null) {
      try {
        rv = (GLR) ReflectionUtilities.newInstance(cls);
      } catch (Throwable t) {
        Logger.throwable(t, cls);
        rv = null;
      }
    } else {
      Logger.severe("cannot find adapter for", sgClass);
      rv = null;
    }
    return rv;
  }

  private static <SG extends Releasable, GLR extends GlrObject<SG>> GLR getAdapterForElement(SG sgElement) {
    GLR rv;
    if (sgElement != null) {
      synchronized (s_elementToAdapterMap) {
        rv = (GLR) s_elementToAdapterMap.get(sgElement);
        if (rv == null) {
          rv = createAdapterFor(sgElement);
          if (rv != null) {
            s_elementToAdapterMap.put(sgElement, rv);
            rv.initialize(sgElement);
            ChangeHandler.addListeners(sgElement);
            createNecessaryProxies(sgElement);
          } else {
            // todo
            // edu.cmu.cs.dennisc.pattern.AbstractElement.warnln( "warning: could
            // not create rv for: " + sgElement );
          }
        } else {
          if (rv.getOwner() == null) {
            rv = null;
            // todo
            // edu.cmu.cs.dennisc.pattern.AbstractElement.warnln( sgElement + "'s
            // rv has null for a sgElement" );
          }
        }
      }
    } else {
      rv = null;
    }
    return rv;
  }

  public static GlrObject<?> getAdapterFor(Releasable releasable) {
    return (GlrObject<?>) getAdapterForElement(releasable);
  }

  public static GlrElement<?> getAdapterFor(Element sgElement) {
    return (GlrElement<?>) getAdapterForElement(sgElement);
  }

  public static GlrAbstractCamera<?> getAdapterFor(AbstractCamera sgCamera) {
    return (GlrAbstractCamera<?>) getAdapterForElement(sgCamera);
  }

  public static GlrProjectionCamera getAdapterFor(ProjectionCamera sgProjectionCamera) {
    return (GlrProjectionCamera) getAdapterForElement(sgProjectionCamera);
  }

  public static GlrOrthographicCamera getAdapterFor(OrthographicCamera sgOrthographicCamera) {
    return (GlrOrthographicCamera) getAdapterForElement(sgOrthographicCamera);
  }

  public static GlrFrustumPerspectiveCamera getAdapterFor(FrustumPerspectiveCamera sgFrustumPerspectiveCamera) {
    return (GlrFrustumPerspectiveCamera) getAdapterForElement(sgFrustumPerspectiveCamera);
  }

  public static GlrSymmetricPerspectiveCamera getAdapterFor(SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera) {
    return (GlrSymmetricPerspectiveCamera) getAdapterForElement(sgSymmetricPerspectiveCamera);
  }

  public static GlrBackground getAdapterFor(Background sgBackground) {
    return (GlrBackground) getAdapterForElement(sgBackground);
  }

  public static GlrComponent<?> getAdapterFor(Component sgComponent) {
    return (GlrComponent<?>) getAdapterForElement(sgComponent);
  }

  public static GlrComposite<?> getAdapterFor(Composite sgComposite) {
    return (GlrComposite<?>) getAdapterForElement(sgComposite);
  }

  public static GlrAppearance<?> getAdapterFor(Appearance sgAppearance) {
    return (GlrAppearance<?>) getAdapterForElement(sgAppearance);
  }

  public static GlrTexturedAppearance getAdapterFor(TexturedAppearance sgSingleAppearance) {
    return (GlrTexturedAppearance) getAdapterForElement(sgSingleAppearance);
  }

  public static GlrGeometry<?> getAdapterFor(Geometry sgGeometry) {
    return (GlrGeometry<?>) getAdapterForElement(sgGeometry);
  }

  public static GlrScene getAdapterFor(Scene sgScene) {
    return (GlrScene) getAdapterForElement(sgScene);
  }

  public static GlrAbstractTransformable<?> getAdapterFor(AbstractTransformable sgTransformable) {
    return (GlrAbstractTransformable<?>) getAdapterForElement(sgTransformable);
  }

  public static GlrTransformable<?> getAdapterFor(Transformable sgTransformable) {
    return (GlrTransformable<?>) getAdapterForElement(sgTransformable);
  }

  public static GlrGhost getAdapterFor(Ghost sgGhost) {
    return (GlrGhost) getAdapterForElement(sgGhost);
  }

  public static GlrTexture<?> getAdapterFor(Texture texture) {
    return (GlrTexture<?>) getAdapterForElement(texture);
  }

  public static GlrLayer getAdapterFor(Layer sgLayer) {
    return (GlrLayer) getAdapterForElement(sgLayer);
  }

  public static GlrGraphic<?> getAdapterFor(Graphic sgGraphic) {
    return (GlrGraphic<?>) getAdapterForElement(sgGraphic);
  }

  public static GlrSilhouette getAdapterFor(Silhouette sgSilhouette) {
    return (GlrSilhouette) getAdapterForElement(sgSilhouette);
  }

  public static <E extends GlrObject> E[] getAdaptersFor(Releasable[] sgElements, Class<? extends E> componentType) {
    if (sgElements != null) {
      E[] proxies = (E[]) Array.newInstance(componentType, sgElements.length);
      for (int i = 0; i < sgElements.length; i++) {
        proxies[i] = (E) getAdapterForElement(sgElements[i]);
      }
      return proxies;
    } else {
      return null;
    }
  }

  public static void forget(Releasable sgElement) {
    synchronized (s_elementToAdapterMap) {
      s_elementToAdapterMap.remove(sgElement);
    }
  }
}
