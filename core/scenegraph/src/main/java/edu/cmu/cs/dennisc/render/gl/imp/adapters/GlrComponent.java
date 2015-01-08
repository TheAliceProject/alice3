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


/**
 * @author Dennis Cosgrove
 */
public abstract class GlrComponent<T extends edu.cmu.cs.dennisc.scenegraph.Component> extends GlrElement<T> implements edu.cmu.cs.dennisc.pattern.Visitable {
	private static edu.cmu.cs.dennisc.math.AffineMatrix4x4 s_buffer = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN();

	public GlrComponent() {
		handleAbsoluteTransformationChanged();
	}

	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		visitor.visit( this );
	}

	protected void handleAbsoluteTransformationChanged() {
		synchronized( m_absolute ) {
			m_absolute[ 0 ] = Double.NaN;
		}
		synchronized( m_inverseAbsolute ) {
			m_inverseAbsolute[ 0 ] = Double.NaN;
		}
	}

	protected void handleHierarchyChanged() {
		GlrScene sceneAdapter;
		edu.cmu.cs.dennisc.scenegraph.Composite sgRoot = owner.getRoot();
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

	/*package-private*/static void handleAbsoluteTransformationChanged( edu.cmu.cs.dennisc.scenegraph.Component component ) {
		GlrComponent<? extends edu.cmu.cs.dennisc.scenegraph.Component> componentAdapter = AdapterFactory.getAdapterFor( component );
		componentAdapter.handleAbsoluteTransformationChanged();
	}

	/*package-private*/static void handleHierarchyChanged( edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent e ) {
		GlrComponent<? extends edu.cmu.cs.dennisc.scenegraph.Component> componentAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		componentAdapter.handleHierarchyChanged();
	}

	public GlrScene getSceneAdapter() {
		edu.cmu.cs.dennisc.scenegraph.Composite sgRoot = owner.getRoot();
		if( sgRoot instanceof edu.cmu.cs.dennisc.scenegraph.Scene ) {
			return AdapterFactory.getAdapterFor( (edu.cmu.cs.dennisc.scenegraph.Scene)sgRoot );
		} else {
			return null;
		}
	}

	private void updateAbsoluteTransformationIfNecessary() {
		synchronized( m_absolute ) {
			if( Double.isNaN( m_absolute[ 0 ] ) ) {
				synchronized( s_buffer ) {
					owner.getAbsoluteTransformation( s_buffer );
					assert s_buffer.isNaN() == false;
					s_buffer.getAsColumnMajorArray16( m_absolute );
				}
			}
		}
	}

	private void updateInverseAbsoluteTransformationIfNecessary() {
		synchronized( m_inverseAbsolute ) {
			if( Double.isNaN( m_inverseAbsolute[ 0 ] ) ) {
				synchronized( s_buffer ) {
					owner.getInverseAbsoluteTransformation( s_buffer );
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

	private double[] m_absolute = new double[ 16 ];
	private double[] m_inverseAbsolute = new double[ 16 ];
	private java.nio.DoubleBuffer m_absoluteBuffer = java.nio.DoubleBuffer.wrap( m_absolute );
	private java.nio.DoubleBuffer m_inverseAbsoluteBuffer = java.nio.DoubleBuffer.wrap( m_inverseAbsolute );

	private GlrScene m_sceneAdapter = null;
}
