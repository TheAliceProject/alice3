/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.lookingglass.opengl;

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
	private static java.util.Map< edu.cmu.cs.dennisc.pattern.AbstractElement, AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >> s_elementToAdapterMap = new java.util.HashMap< edu.cmu.cs.dennisc.pattern.AbstractElement, AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >>();
	private static java.util.Map< Class< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >, Class< ? extends AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >>> s_classToAdapterClassMap = new java.util.HashMap< Class< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >, Class< ? extends AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >>>();
	private static final String RENDERER_PACKAGE_NAME = ElementAdapter.class.getPackage().getName();
	private static final String SCENEGRAPH_PACKAGE_NAME = edu.cmu.cs.dennisc.scenegraph.Element.class.getPackage().getName();

	static {
		register( edu.cmu.cs.dennisc.texture.BufferedImageTexture.class, BufferedImageTextureAdapter.class );
	}
	public static void register( Class< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement > sgClass, Class< ? extends AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >> adapterClass ) {
		s_classToAdapterClassMap.put( sgClass, adapterClass );
	}

	private static AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement > createAdapterFor( edu.cmu.cs.dennisc.pattern.AbstractElement sgElement ) {
		Class sgClass = sgElement.getClass();
		Class cls = s_classToAdapterClassMap.get( sgClass );
		if( cls != null ) {
			//pass
		} else {
			while( sgClass != null ) {
				Package sgPackage = sgClass.getPackage();
				if( sgPackage != null && sgPackage.getName().equals( SCENEGRAPH_PACKAGE_NAME ) ) {
					break;
				} else if( sgClass == edu.cmu.cs.dennisc.texture.CustomTexture.class ) {
					break;
				} else {
					sgClass = sgClass.getSuperclass();
				}
			}
			assert sgClass != null;
			StringBuffer sb = new StringBuffer( RENDERER_PACKAGE_NAME );
			sb.append( '.' );
			sb.append( sgClass.getSimpleName() );
			sb.append( "Adapter" );
			cls = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( sb.toString() );
			if( cls != null ) {
				register( sgClass, cls );
			}
		}
		return (AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement >)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cls );
	}

	public static void forget( edu.cmu.cs.dennisc.pattern.AbstractElement sgElement ) {
		synchronized( s_elementToAdapterMap ) {
			s_elementToAdapterMap.remove( sgElement );
		}
	}

	public static AbstractElementAdapter< ? extends edu.cmu.cs.dennisc.pattern.AbstractElement > getAdapterForElement( edu.cmu.cs.dennisc.pattern.AbstractElement sgElement ) {
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
					if( rv.m_element == null ) {
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

	public static AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > getAdapterFor( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return (AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera >)getAdapterForElement( sgCamera );
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
	
	public static ComponentAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Component > getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Component sgComponent ) {
		return (ComponentAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Component >)getAdapterForElement( sgComponent );
	}
	
	public static CompositeAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Composite > getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Composite sgComposite ) {
		return (CompositeAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Composite >)getAdapterForElement( sgComposite );
	}
	
	public static AppearanceAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Appearance > getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Appearance sgAppearance ) {
		return (AppearanceAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Appearance >)getAdapterForElement( sgAppearance );
	}

	public static SingleAppearanceAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.SingleAppearance sgSingleAppearance ) {
		return (SingleAppearanceAdapter)getAdapterForElement( sgSingleAppearance );
	}

	public static MultipleAppearanceAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.MultipleAppearance sgMultipleAppearance ) {
		return (MultipleAppearanceAdapter)getAdapterForElement( sgMultipleAppearance );
	}

	public static GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry ) {
		return (GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry >)getAdapterForElement( sgGeometry );
	}

	public static SceneAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Scene sgScene ) {
		return (SceneAdapter)getAdapterForElement( sgScene );
	}
	public static TransformableAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Transformable > getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable ) {
		return (TransformableAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Transformable >)getAdapterForElement( sgTransformable );
	}

	public static GhostAdapter getAdapterFor( edu.cmu.cs.dennisc.scenegraph.Ghost sgGhost ) {
		return (GhostAdapter)getAdapterForElement( sgGhost );
	}

	public static TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > getAdapterFor( edu.cmu.cs.dennisc.texture.Texture texture ) {
		return (TextureAdapter)getAdapterForElement( texture );
	}

	public static <E extends AbstractElementAdapter> E[] getAdaptersFor( edu.cmu.cs.dennisc.pattern.AbstractElement[] sgElements, Class< ? extends E > componentType ) {
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
			for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : sgComposite.accessComponents() ) {
				createNecessaryProxies( sgComponent );
			}
		}
	}
}
