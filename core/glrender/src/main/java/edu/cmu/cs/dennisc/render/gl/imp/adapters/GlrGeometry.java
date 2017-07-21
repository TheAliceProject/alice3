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

import static com.jogamp.opengl.GL2.GL_COMPILE_AND_EXECUTE;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrGeometry<T extends edu.cmu.cs.dennisc.scenegraph.Geometry> extends GlrElement<T> {
	public void addRenderContext( RenderContext rc ) {
		this.renderContexts.add( rc );
	}

	public void removeRenderContext( RenderContext rc ) {
		this.renderContexts.remove( rc );
	}

	@Override
	protected void handleReleased() {
		super.handleReleased();
		if( this.renderContexts.size() > 0 ) {
			RenderContext[] renderContexts = new RenderContext[ this.renderContexts.size() ];
			this.renderContexts.toArray( renderContexts );
			for( RenderContext rc : renderContexts ) {
				rc.forgetGeometryAdapter( this, true );
			}
		}
	}

	public abstract boolean isAlphaBlended();

	public boolean hasOpaque() {
		return !isAlphaBlended();
	}

	protected boolean isGeometryChanged() {
		return this.isGeometryChanged;
	}

	protected boolean isDisplayListDesired() {
		return true;
	}

	protected boolean isDisplayListInNeedOfRefresh( RenderContext rc ) {
		return isGeometryChanged();
	}

	protected void setIsGeometryChanged( boolean isGeometryChanged ) {
		this.isGeometryChanged = isGeometryChanged;
	}

	//todo: better name
	protected abstract void renderGeometry( RenderContext rc, GlrVisual.RenderType renderType );

	protected abstract void pickGeometry( PickContext pc, boolean isSubElementRequired );

	public final void render( RenderContext rc, GlrVisual.RenderType renderType ) {
		if( isDisplayListDesired() ) {
			Integer id = rc.getDisplayListID( this );
			if( id == null ) {
				id = rc.generateDisplayListID( this );
				setIsGeometryChanged( true );
			}
			if( isDisplayListInNeedOfRefresh( rc ) || ( rc.gl.glIsList( id ) == false ) ) {
				rc.gl.glNewList( id, GL_COMPILE_AND_EXECUTE );
				renderGeometry( rc, renderType );
				rc.gl.glEndList();
				{
					int error = rc.gl.glGetError();
					if( error != com.jogamp.opengl.GL.GL_NO_ERROR ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( rc.glu.gluErrorString( error ), error, this );
						//throw new com.jogamp.opengl.GLException( rc.glu.gluErrorString( error ) + " " + error + " " + this.toString() );
					}
				}
				setIsGeometryChanged( false );
			} else {
				if( rc.gl.glIsList( id ) ) {
					rc.gl.glCallList( id );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
				}
			}
		} else {
			renderGeometry( rc, renderType );
		}
	}

	public final void pick( PickContext pc, boolean isSubElementRequired ) {
		//todo: display lists?
		pickGeometry( pc, isSubElementRequired );
	}

	protected static edu.cmu.cs.dennisc.math.Point3 getIntersectionInSourceFromPlaneInLocal( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, double px, double py, double pz, double nx, double ny, double nz ) {
		edu.cmu.cs.dennisc.math.Point3 position = new edu.cmu.cs.dennisc.math.Point3( px, py, pz );
		edu.cmu.cs.dennisc.math.Vector3 direction = new edu.cmu.cs.dennisc.math.Vector3( nx, ny, nz );
		m.transform( position );
		m.transform( direction );
		edu.cmu.cs.dennisc.math.Plane plane = edu.cmu.cs.dennisc.math.Plane.createInstance( position, direction );
		if( plane.isNaN() ) {
			rv.setNaN();
		} else {
			double t = plane.intersect( ray );
			ray.getPointAlong( rv, t );
		}
		return rv;
	}

	protected static edu.cmu.cs.dennisc.math.Point3 getIntersectionInSourceFromPlaneInLocal( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, edu.cmu.cs.dennisc.math.Point3 planePosition, edu.cmu.cs.dennisc.math.Vector3 planeDirection ) {
		return getIntersectionInSourceFromPlaneInLocal( rv, ray, m, planePosition.x, planePosition.y, planePosition.x, planeDirection.x, planeDirection.y, planeDirection.z );
	}

	public abstract edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement );

	private final java.util.List<RenderContext> renderContexts = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private boolean isGeometryChanged;
}
