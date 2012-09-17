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
public abstract class VertexGeometryAdapter<E extends edu.cmu.cs.dennisc.scenegraph.VertexGeometry> extends GeometryAdapter<E> {
	private boolean m_isAlphaBlended;

	//    private boolean m_isVertexColored;
	private void updateVertices() {
		//	    edu.cmu.cs.dennisc.scenegraph.VertexGeometry vg = m_sgE;
		//	    int vertexCount = vg.getVertexCount();
		//	    if( vertexCount != m_vertices.length ) {
		//	        m_vertices = vg.getVertices();
		//	    } else {
		//	        m_vertices = vg.getVertices( m_vertices );
		//	    }
		setIsGeometryChanged( true );
		m_isAlphaBlended = false;
		//	    m_isVertexColored = false;
		for( edu.cmu.cs.dennisc.scenegraph.Vertex v : m_element.vertices.getValue() ) {
			if( v.diffuseColor.isNaN() == false ) {
				//m_isVertexColored = true;
				if( v.diffuseColor.alpha < 1.0f ) {
					m_isAlphaBlended = true;
					break;
				}
			}
		}
	}

	@Override
	public boolean isAlphaBlended() {
		return m_isAlphaBlended;
	}

	//    public boolean isVertexColored() {
	//    	return m_isVertexColored;
	//    }

	protected edu.cmu.cs.dennisc.scenegraph.Vertex accessVertexAt( int index ) {
		return m_element.vertices.getValue()[ index ];
	}

	public void renderPrimative( RenderContext rc, int mode ) {
		rc.gl.glBegin( mode );
		for( edu.cmu.cs.dennisc.scenegraph.Vertex vertex : m_element.vertices.getValue() ) {
			rc.renderVertex( vertex );
		}
		rc.gl.glEnd();
	}

	public void pickPrimative( PickContext pc, int mode ) {
		pc.gl.glPushName( -1 );
		pc.gl.glBegin( mode );
		for( edu.cmu.cs.dennisc.scenegraph.Vertex vertex : m_element.vertices.getValue() ) {
			pc.pickVertex( vertex );
		}
		pc.gl.glEnd();
		pc.gl.glPopName();
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.vertices ) {
			updateVertices();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
