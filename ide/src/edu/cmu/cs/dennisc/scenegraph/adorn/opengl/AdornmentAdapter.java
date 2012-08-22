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
package edu.cmu.cs.dennisc.scenegraph.adorn.opengl;

import edu.cmu.cs.dennisc.lookingglass.opengl.ConformanceTestResults;

/**
 * @author Dennis Cosgrove
 */
public abstract class AdornmentAdapter extends edu.cmu.cs.dennisc.lookingglass.opengl.ComponentAdapter<edu.cmu.cs.dennisc.scenegraph.adorn.Adornment> {
	protected edu.cmu.cs.dennisc.lookingglass.opengl.CompositeAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Composite> m_adornmentRootAdapter = null;

	protected abstract void actuallyRender( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, edu.cmu.cs.dennisc.lookingglass.opengl.CompositeAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Composite> adornmentRootAdapter );

	@Override
	public void setup( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		//pass
	}

	@Override
	public void renderOpaque( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		if( m_adornmentRootAdapter != null ) {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( accessInverseAbsoluteTransformationAsBuffer() );
			actuallyRender( rc, m_adornmentRootAdapter );
			rc.gl.glPopMatrix();
		}
	}

	@Override
	public void renderGhost( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, edu.cmu.cs.dennisc.lookingglass.opengl.GhostAdapter root ) {
		//todo?
		//pass
	}

	@Override
	public void pick( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, edu.cmu.cs.dennisc.lookingglass.opengl.PickParameters pickParameters, ConformanceTestResults conformanceTestResults ) {
		//todo?
		//pass
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.adorningRoot ) {
			m_adornmentRootAdapter = edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory.getAdapterFor( m_element.adorningRoot.getValue() );
		} else {
			super.propertyChanged( property );
		}
	}
}
