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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

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
	private static java.util.Map<edu.cmu.cs.dennisc.pattern.AbstractReleasable, GlrObject<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable>> s_elementToAdapterMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<Class<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable>, Class<? extends GlrObject<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable>>> s_classToAdapterClassMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static final String SCENEGRAPH_PACKAGE_NAME = edu.cmu.cs.dennisc.scenegraph.Element.class.getPackage().getName();
	private static final String RENDERER_PACKAGE_NAME = GlrElement.class.getPackage().getName();
	private static final String SCENEGRAPH_GRAPHICS_PACKAGE_NAME = edu.cmu.cs.dennisc.scenegraph.graphics.Text.class.getPackage().getName();
	private static final String RENDERER_GRAPHICS_PACKAGE_NAME = edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics.TextAdapter.class.getPackage().getName();

	static {
		register( edu.cmu.cs.dennisc.texture.BufferedImageTexture.class, GlrBufferedImageTexture.class );
	}

	public static void register( Class<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable> sgClass, Class<? extends GlrObject<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable>> adapterClass ) {
		s_classToAdapterClassMap.put( sgClass, adapterClass );
	}

	private static GlrObject<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable> createAdapterFor( edu.cmu.cs.dennisc.pattern.AbstractReleasable sgElement ) {
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
			sb.append( "Glr" );
			sb.append( sgClass.getSimpleName() );
			cls = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( sb.toString() );
			if( cls != null ) {
				register( sgClass, cls );
			}
		}
		GlrObject<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable> rv;
		if( cls != null ) {
			try {
				rv = (GlrObject<? extends edu.cmu.cs.dennisc.pattern.AbstractReleasable>)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cls );
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

	public static void forget( edu.cmu.cs.dennisc.pattern.AbstractReleasable sgElement ) {
		synchronized( s_elementToAdapterMap ) {
			s_elementToAdapterMap.remove( sgElement );
		}
	}

	public static GlrObject<?> getAdapterForElement( edu.cmu.cs.dennisc.pattern.AbstractReleasable sgElement ) {
		GlrObject rv;
		if( sgElement != null ) {
			synchronized( s_elementToAdapterMap ) {
				rv = s_elementToAdapterMap.get( sgElement );
				if( rv == null ) {
					rv = createAdapterFor( sgElement );
					if( rv != null ) {
						s_elementToAdapterMap.put( sgElement, rv );
						rv.initialize( sgElement );
						ChangeHandler.addListeners( sgElement );
						createNecessaryProxies( sgElement );
					} else {
						// todo
						// edu.cmu.cs.dennisc.pattern.AbstractElement.warnln( "warning: could
						// not create rv for: " + sgElement );
					}
				} else {
					if( rv.getOwner() == null ) {
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

	public static GlrAbstractCamera<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return (GlrAbstractCamera<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera>)getAdapterForElement( sgCamera );
	}

	public static GlrProjectionCamera getAdapterFor( edu.cmu.cs.dennisc.scenegraph.ProjectionCamera sgProjectionCamera ) {
		return (GlrProjectionCamera)getAdapterForElement( sgProjectionCamera );
	}

	public static GlrOrthographicCamera getAdapterFor( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera ) {
		return (GlrOrthographicCamera)getAdapterForElement( sgOrthographicCamera );
	}

	public static GlrFrustumPerspectiveCamera getAdapterFor( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera sgFrustumPerspectiveCamera ) {
		return (GlrFrustumPerspectiveCamera)getAdapterForElement( sgFrustumPerspectiveCamera );
	}

	public static GlrSymmetricPerspectiveCamera getAdapterFor( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		return (GlrSymmetricPerspectiveCamera)getAdapterForElement( sgSymmetricPerspectiveCamera );
	}

	public static GlrBackground getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Background sgBackground ) {
		return (GlrBackground)getAdapterForElement( sgBackground );
	}

	public static GlrComponent<? extends edu.cmu.cs.dennisc.scenegraph.Component> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Component sgComponent ) {
		return (GlrComponent<? extends edu.cmu.cs.dennisc.scenegraph.Component>)getAdapterForElement( sgComponent );
	}

	public static GlrComposite<? extends edu.cmu.cs.dennisc.scenegraph.Composite> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Composite sgComposite ) {
		return (GlrComposite<? extends edu.cmu.cs.dennisc.scenegraph.Composite>)getAdapterForElement( sgComposite );
	}

	public static GlrAppearance<? extends edu.cmu.cs.dennisc.scenegraph.Appearance> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Appearance sgAppearance ) {
		return (GlrAppearance<? extends edu.cmu.cs.dennisc.scenegraph.Appearance>)getAdapterForElement( sgAppearance );
	}

	public static GlrTexturedAppearance getAdapterFor( edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgSingleAppearance ) {
		return (GlrTexturedAppearance)getAdapterForElement( sgSingleAppearance );
	}

	public static GlrMultipleAppearance getAdapterFor( edu.cmu.cs.dennisc.scenegraph.MultipleAppearance sgMultipleAppearance ) {
		return (GlrMultipleAppearance)getAdapterForElement( sgMultipleAppearance );
	}

	public static GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry ) {
		return (GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry>)getAdapterForElement( sgGeometry );
	}

	public static GlrScene getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Scene sgScene ) {
		return (GlrScene)getAdapterForElement( sgScene );
	}

	public static GlrAbstractTransformable<? extends edu.cmu.cs.dennisc.scenegraph.AbstractTransformable> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable ) {
		return (GlrAbstractTransformable<? extends edu.cmu.cs.dennisc.scenegraph.AbstractTransformable>)getAdapterForElement( sgTransformable );
	}

	public static GlrTransformable<? extends edu.cmu.cs.dennisc.scenegraph.Transformable> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable ) {
		return (GlrTransformable<? extends edu.cmu.cs.dennisc.scenegraph.Transformable>)getAdapterForElement( sgTransformable );
	}

	public static GlrGhost getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Ghost sgGhost ) {
		return (GlrGhost)getAdapterForElement( sgGhost );
	}

	public static GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> getAdapterFor( edu.cmu.cs.dennisc.texture.Texture texture ) {
		return (GlrTexture)getAdapterForElement( texture );
	}

	public static GlrLayer getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Layer sgLayer ) {
		return (GlrLayer)getAdapterForElement( sgLayer );
	}

	public static GlrGraphic<?> getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Graphic sgGraphic ) {
		return (GlrGraphic<?>)getAdapterForElement( sgGraphic );
	}

	public static edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSilhouette getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Silhouette sgSilhouette ) {
		return (edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSilhouette)getAdapterForElement( sgSilhouette );
	}

	public static <E extends GlrObject> E[] getAdaptersFor( edu.cmu.cs.dennisc.pattern.AbstractReleasable[] sgElements, Class<? extends E> componentType ) {
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

	public static void createNecessaryProxies( edu.cmu.cs.dennisc.pattern.AbstractReleasable sgElement ) {
		getAdapterForElement( sgElement );
		if( sgElement instanceof edu.cmu.cs.dennisc.scenegraph.Composite ) {
			edu.cmu.cs.dennisc.scenegraph.Composite sgComposite = (edu.cmu.cs.dennisc.scenegraph.Composite)sgElement;
			for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : sgComposite.getComponents() ) {
				createNecessaryProxies( sgComponent );
			}
		}
	}
}
