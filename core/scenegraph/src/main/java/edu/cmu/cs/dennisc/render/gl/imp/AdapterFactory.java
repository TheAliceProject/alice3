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

package edu.cmu.cs.dennisc.render.gl.imp;

import edu.cmu.cs.dennisc.render.gl.imp.adapters.AbstractCameraAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AbstractElementAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AbstractTransformableAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AppearanceAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.BackgroundAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.BufferedImageTextureAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.ComponentAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.CompositeAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.ElementAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.FrustumPerspectiveCameraAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GeometryAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GhostAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GraphicAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.LayerAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.MultipleAppearanceAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.OrthographicCameraAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.ProjectionCameraAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.SceneAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.SymmetricPerspectiveCameraAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.TextureAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.TexturedAppearanceAdapter;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.TransformableAdapter;

//public class AdapterFactory {
//	private static AdapterFactory s_singleton = new AdapterFactory();
//	private AdapterFactory() {
//	}
//	public static AdapterFactory getSingleton() {
//		if( s_singleton != null ) {
//			//pass
//		} else {
//			s_singleton = new AdapterFactory();
//		}
//		return s_singleton;
//	}

/**
 * @author Dennis Cosgrove
 */
public abstract class AdapterFactory {
	private static java.util.Map<edu.cmu.cs.dennisc.pattern.AbstractElement, AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>> s_elementToAdapterMap = new java.util.HashMap<edu.cmu.cs.dennisc.pattern.AbstractElement, AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>>();
	private static java.util.Map<Class<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>, Class<? extends AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>>> s_classToAdapterClassMap = new java.util.HashMap<Class<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>, Class<? extends AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>>>();
	private static final String SCENEGRAPH_PACKAGE_NAME = edu.cmu.cs.dennisc.scenegraph.Element.class.getPackage().getName();
	private static final String RENDERER_PACKAGE_NAME = ElementAdapter.class.getPackage().getName();
	private static final String SCENEGRAPH_GRAPHICS_PACKAGE_NAME = edu.cmu.cs.dennisc.scenegraph.graphics.Text.class.getPackage().getName();
	private static final String RENDERER_GRAPHICS_PACKAGE_NAME = edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics.TextAdapter.class.getPackage().getName();

	static {
		register( edu.cmu.cs.dennisc.texture.BufferedImageTexture.class, BufferedImageTextureAdapter.class );
	}

	public static void register( Class<? extends edu.cmu.cs.dennisc.pattern.AbstractElement> sgClass, Class<? extends AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>> adapterClass ) {
		s_classToAdapterClassMap.put( sgClass, adapterClass );
	}

	private static AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement> createAdapterFor( edu.cmu.cs.dennisc.pattern.AbstractElement sgElement ) {
		Class sgClass = sgElement.getClass();
		Class cls = s_classToAdapterClassMap.get( sgClass );
		if( cls != null ) {
			//pass
		} else {
			StringBuffer sb = new StringBuffer();
			while( sgClass != null ) {
				Package sgPackage = sgClass.getPackage();
				if( ( sgPackage != null ) && sgPackage.getName().equals( SCENEGRAPH_PACKAGE_NAME ) ) {
					sb.append( RENDERER_PACKAGE_NAME );
					break;
				} else if( sgClass == edu.cmu.cs.dennisc.texture.CustomTexture.class ) {
					sb.append( RENDERER_PACKAGE_NAME );
					break;
				} else if( ( sgPackage != null ) && sgPackage.getName().equals( SCENEGRAPH_GRAPHICS_PACKAGE_NAME ) ) {
					sb.append( RENDERER_GRAPHICS_PACKAGE_NAME );
					break;
				} else {
					sgClass = sgClass.getSuperclass();
				}
			}
			assert sgClass != null;
			sb.append( '.' );
			sb.append( sgClass.getSimpleName() );
			sb.append( "Adapter" );
			cls = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( sb.toString() );
			if( cls != null ) {
				register( sgClass, cls );
			}
		}
		AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement> rv;
		if( cls != null ) {
			try {
				rv = (AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement>)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cls );
			} catch( Throwable t ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t, cls );
				rv = null;
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "cannot find adapter for", sgClass );
			rv = null;
		}
		return rv;
	}

	public static void forget( edu.cmu.cs.dennisc.pattern.AbstractElement sgElement ) {
		synchronized( s_elementToAdapterMap ) {
			s_elementToAdapterMap.remove( sgElement );
		}
	}

	public static AbstractElementAdapter<? extends edu.cmu.cs.dennisc.pattern.AbstractElement> getAdapterForElement( edu.cmu.cs.dennisc.pattern.AbstractElement sgElement ) {
		AbstractElementAdapter rv;
		if( sgElement != null ) {
			synchronized( s_elementToAdapterMap ) {
				rv = s_elementToAdapterMap.get( sgElement );
				if( rv == null ) {
					rv = createAdapterFor( sgElement );
					if( rv != null ) {
						s_elementToAdapterMap.put( sgElement, rv );
						rv.initialize( sgElement );
						ChangeHandler.addListenersAndObservers( sgElement );
						createNecessaryProxies( sgElement );
					} else {
						// todo
						// edu.cmu.cs.dennisc.pattern.AbstractElement.warnln( "warning: could
						// not create rv for: " + sgElement );
					}
				} else {
					if( rv.getElement() == null ) {
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

	public static AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return (AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera>)getAdapterForElement( sgCamera );
	}

	public static ProjectionCameraAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.ProjectionCamera sgProjectionCamera ) {
		return (ProjectionCameraAdapter)getAdapterForElement( sgProjectionCamera );
	}

	public static OrthographicCameraAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera ) {
		return (OrthographicCameraAdapter)getAdapterForElement( sgOrthographicCamera );
	}

	public static FrustumPerspectiveCameraAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera sgFrustumPerspectiveCamera ) {
		return (FrustumPerspectiveCameraAdapter)getAdapterForElement( sgFrustumPerspectiveCamera );
	}

	public static SymmetricPerspectiveCameraAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		return (SymmetricPerspectiveCameraAdapter)getAdapterForElement( sgSymmetricPerspectiveCamera );
	}

	public static BackgroundAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Background sgBackground ) {
		return (BackgroundAdapter)getAdapterForElement( sgBackground );
	}

	public static ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Component sgComponent ) {
		return (ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component>)getAdapterForElement( sgComponent );
	}

	public static CompositeAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Composite> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Composite sgComposite ) {
		return (CompositeAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Composite>)getAdapterForElement( sgComposite );
	}

	public static AppearanceAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Appearance> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Appearance sgAppearance ) {
		return (AppearanceAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Appearance>)getAdapterForElement( sgAppearance );
	}

	public static TexturedAppearanceAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgSingleAppearance ) {
		return (TexturedAppearanceAdapter)getAdapterForElement( sgSingleAppearance );
	}

	public static MultipleAppearanceAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.MultipleAppearance sgMultipleAppearance ) {
		return (MultipleAppearanceAdapter)getAdapterForElement( sgMultipleAppearance );
	}

	public static GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry ) {
		return (GeometryAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Geometry>)getAdapterForElement( sgGeometry );
	}

	public static SceneAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Scene sgScene ) {
		return (SceneAdapter)getAdapterForElement( sgScene );
	}

	public static AbstractTransformableAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractTransformable> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable ) {
		return (AbstractTransformableAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractTransformable>)getAdapterForElement( sgTransformable );
	}

	public static TransformableAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Transformable> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable ) {
		return (TransformableAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Transformable>)getAdapterForElement( sgTransformable );
	}

	public static GhostAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Ghost sgGhost ) {
		return (GhostAdapter)getAdapterForElement( sgGhost );
	}

	public static TextureAdapter<? extends edu.cmu.cs.dennisc.texture.Texture> getAdapterFor( edu.cmu.cs.dennisc.texture.Texture texture ) {
		return (TextureAdapter)getAdapterForElement( texture );
	}

	public static LayerAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Layer sgLayer ) {
		return (LayerAdapter)getAdapterForElement( sgLayer );
	}

	public static GraphicAdapter<?> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Graphic sgGraphic ) {
		return (GraphicAdapter<?>)getAdapterForElement( sgGraphic );
	}

	public static edu.cmu.cs.dennisc.render.gl.imp.adapters.SilhouetteAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Silhouette sgSilhouette ) {
		return (edu.cmu.cs.dennisc.render.gl.imp.adapters.SilhouetteAdapter)getAdapterForElement( sgSilhouette );
	}

	public static <E extends AbstractElementAdapter> E[] getAdaptersFor( edu.cmu.cs.dennisc.pattern.AbstractElement[] sgElements, Class<? extends E> componentType ) {
		if( sgElements != null ) {
			E[] proxies = (E[])java.lang.reflect.Array.newInstance( componentType, sgElements.length );
			for( int i = 0; i < sgElements.length; i++ ) {
				proxies[ i ] = (E)getAdapterForElement( sgElements[ i ] );
			}
			return proxies;
		} else {
			return null;
		}
	}

	public static void createNecessaryProxies( edu.cmu.cs.dennisc.pattern.AbstractElement sgElement ) {
		getAdapterForElement( sgElement );
		if( sgElement instanceof edu.cmu.cs.dennisc.scenegraph.Composite ) {
			edu.cmu.cs.dennisc.scenegraph.Composite sgComposite = (edu.cmu.cs.dennisc.scenegraph.Composite)sgElement;
			for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : sgComposite.getComponents() ) {
				createNecessaryProxies( sgComponent );
			}
		}
	}
}
