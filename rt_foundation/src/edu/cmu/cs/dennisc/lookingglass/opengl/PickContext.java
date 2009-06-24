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

import javax.media.opengl.GL;

/**
 * @author Dennis Cosgrove
 */
public class PickContext extends Context {
	private java.util.HashMap< Integer, VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> > m_pickNameMap = new java.util.HashMap< Integer, VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> >();
	private PickParameters m_pickParameters;

	public void pick( PickParameters pickParameters ) {
		m_pickParameters = pickParameters;
//		javax.media.opengl.Threading.invokeOnOpenGLThread( new Runnable() {
//			public void run() {
				m_pickParameters.getGLAutoDrawable().display();
//			}
//		} );
	}
	public int getPickNameForVisualAdapter( VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter ) {
		synchronized( m_pickNameMap ) {
			int name = m_pickNameMap.size();
			m_pickNameMap.put( new Integer( name ), visualAdapter );
			return name;
		}
	}
	public VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> getPickVisualAdapterForName( int name ) {
		synchronized( m_pickNameMap ) {
			return m_pickNameMap.get( name );
		}
	}

	protected void pickVertex( edu.cmu.cs.dennisc.scenegraph.Vertex vertex ) {
		gl.glVertex3d( vertex.position.x, vertex.position.y, vertex.position.z );
	}
	public void pickScene( AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter, SceneAdapter sceneAdapter, PickParameters pickParameters ) {
		gl.glMatrixMode( GL.GL_MODELVIEW );
		synchronized( cameraAdapter ) {
			gl.glLoadMatrixd( cameraAdapter.accessInverseAbsoluteTransformationAsBuffer() );
		}
		m_pickNameMap.clear();
		sceneAdapter.pick( this, pickParameters );
	}

	@Override
	protected void handleGLChange() {
	}
	
	//todo: remove?
	@Override
	public void setAppearanceIndex( int index ) {
	}
}
