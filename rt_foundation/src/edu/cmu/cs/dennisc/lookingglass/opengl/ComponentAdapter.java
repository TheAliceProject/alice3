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

/**
 * @author Dennis Cosgrove
 */
public abstract class ComponentAdapter< E extends edu.cmu.cs.dennisc.scenegraph.Component > extends ElementAdapter< E > implements edu.cmu.cs.dennisc.pattern.Visitable {
	private static edu.cmu.cs.dennisc.math.AffineMatrix4x4 s_buffer = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN();
	private double[] m_absolute = new double[ 16 ];
	private double[] m_inverseAbsolute = new double[ 16 ];
	private java.nio.DoubleBuffer m_absoluteBuffer = java.nio.DoubleBuffer.wrap( m_absolute );
	private java.nio.DoubleBuffer m_inverseAbsoluteBuffer = java.nio.DoubleBuffer.wrap( m_inverseAbsolute );

	private SceneAdapter m_sceneAdapter = null;

	protected ComponentAdapter() {
		handleAbsoluteTransformationChanged();
	}

	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		visitor.visit( this );
	}
//	@Override
//	protected void propertyChanged( String propertyName ) {
//		if( propertyName == edu.cmu.cs.dennisc.scenegraph.Component.VEHICLE_PROPERTY_NAME ) {
//			//pass
//		} else {
//			super.propertyChanged( propertyName );
//		}
//	}
	protected void handleAbsoluteTransformationChanged() {
		synchronized( m_absolute ) {
			m_absolute[ 0 ] = Double.NaN;
		}
		synchronized( m_inverseAbsolute ) {
			m_inverseAbsolute[ 0 ] = Double.NaN;
		}
	}
	protected void handleHierarchyChanged() {
		SceneAdapter sceneAdapter;
		edu.cmu.cs.dennisc.scenegraph.Composite sgRoot = m_element.getRoot();
		if( sgRoot instanceof edu.cmu.cs.dennisc.scenegraph.Scene ) {
			//edu.cmu.cs.dennisc.scenegraph.Scene sgScene = (edu.cmu.cs.dennisc.scenegraph.Scene)sgRoot;
			sceneAdapter = AdapterFactory.getAdapterFor( (edu.cmu.cs.dennisc.scenegraph.Scene)sgRoot );
		} else {
			sceneAdapter = null;
		}
		
		if( m_sceneAdapter != sceneAdapter ) {
			if( m_sceneAdapter != null ) {
				m_sceneAdapter.removeDescendant( this );
			}
			m_sceneAdapter = sceneAdapter;
			if( m_sceneAdapter != null ) {
				m_sceneAdapter.addDescendant( this );
			}
		}

	}

	public static void handleAbsoluteTransformationChanged( edu.cmu.cs.dennisc.scenegraph.Component component ) {
		ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > componentAdapter = AdapterFactory.getAdapterFor( component );
		componentAdapter.handleAbsoluteTransformationChanged();
	}

	public static void handleHierarchyChanged( edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent e ) {
		ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > componentAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		componentAdapter.handleHierarchyChanged();
	}

	public SceneAdapter getSceneAdapter() {
		edu.cmu.cs.dennisc.scenegraph.Composite sgRoot = m_element.getRoot();
		if( sgRoot instanceof edu.cmu.cs.dennisc.scenegraph.Scene ) {
			return AdapterFactory.getAdapterFor( (edu.cmu.cs.dennisc.scenegraph.Scene)sgRoot );
		} else {
			return null;
		}
	}

	private void updateAbsoluteTransformationIfNecessary() {
		synchronized( s_buffer ) {
			synchronized( m_absolute ) {
				if( Double.isNaN( m_absolute[ 0 ] ) ) {
					m_element.getAbsoluteTransformation( s_buffer );
					assert s_buffer.isNaN() == false;
					s_buffer.getAsColumnMajorArray16( m_absolute );
				}
			}
		}
	}
	private void updateInverseAbsoluteTransformationIfNecessary() {
		synchronized( s_buffer ) {
			synchronized( m_inverseAbsolute ) {
				if( Double.isNaN( m_inverseAbsolute[ 0 ] ) ) {
					m_element.getInverseAbsoluteTransformation( s_buffer );
					assert s_buffer.isNaN() == false;
					s_buffer.getAsColumnMajorArray16( m_inverseAbsolute );
				}
			}
		}
	}
	protected double[] getAbsoluteTransformation( double[] rv ) {
		updateAbsoluteTransformationIfNecessary();
		synchronized( m_absolute ) {
			System.arraycopy( m_absolute, 0, rv, 0, rv.length );
		}
		return rv;
	}
	protected double[] getInverseAbsoluteTransformation( double[] rv ) {
		updateInverseAbsoluteTransformationIfNecessary();
		synchronized( m_inverseAbsolute ) {
			System.arraycopy( m_inverseAbsolute, 0, rv, 0, rv.length );
		}
		return rv;
	}
	public java.nio.DoubleBuffer accessAbsoluteTransformationAsBuffer() {
		updateAbsoluteTransformationIfNecessary();
		return m_absoluteBuffer;
	}
	public java.nio.DoubleBuffer accessInverseAbsoluteTransformationAsBuffer() {
		updateInverseAbsoluteTransformationIfNecessary();
		return m_inverseAbsoluteBuffer;
	}
	public abstract void renderGhost( RenderContext rc, GhostAdapter root );
	public abstract void renderOpaque( RenderContext rc );
	public abstract void setup( RenderContext rc );
	public abstract void pick( PickContext pc, PickParameters pickParameters );

}
