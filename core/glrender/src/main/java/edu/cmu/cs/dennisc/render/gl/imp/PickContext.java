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

package edu.cmu.cs.dennisc.render.gl.imp;

import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractCamera;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual;

/**
 * @author Dennis Cosgrove
 */
public class PickContext extends Context {
	public static final long MAX_UNSIGNED_INTEGER = 0xFFFFFFFFL;

	public PickContext( boolean isSynchronous ) {
		this.isSynchronous = isSynchronous;
	}

	public int getPickNameForVisualAdapter( GlrVisual<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter ) {
		synchronized( m_pickNameMap ) {
			int name = m_pickNameMap.size();
			m_pickNameMap.put( new Integer( name ), visualAdapter );
			return name;
		}
	}

	public edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.PickDetails getConformanceTestResultsPickDetails() {
		if( this.isSynchronous ) {
			return edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SINGLETON.getSynchronousPickDetails();
		} else {
			return edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SINGLETON.getAsynchronousPickDetails();
		}
	}

	public GlrVisual<? extends edu.cmu.cs.dennisc.scenegraph.Visual> getPickVisualAdapterForName( int name ) {
		synchronized( m_pickNameMap ) {
			return m_pickNameMap.get( name );
		}
	}

	@Override
	protected void enableNormalize() {
	}

	@Override
	protected void disableNormalize() {
	}

	public void pickVertex( edu.cmu.cs.dennisc.scenegraph.Vertex vertex ) {
		gl.glVertex3d( vertex.position.x, vertex.position.y, vertex.position.z );
	}

	public void pickScene( GlrAbstractCamera<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter, GlrScene sceneAdapter, PickParameters pickParameters ) {
		gl.glMatrixMode( GL_MODELVIEW );
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

	private final java.util.Map<Integer, GlrVisual<? extends edu.cmu.cs.dennisc.scenegraph.Visual>> m_pickNameMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final boolean isSynchronous;
}
