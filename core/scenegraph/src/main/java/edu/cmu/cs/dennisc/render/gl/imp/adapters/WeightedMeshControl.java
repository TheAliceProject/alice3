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

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;

/*package-private*/class WeightedMeshControl {
	protected WeightedMesh weightedMesh;

	protected DoubleBuffer vertexBuffer;
	protected FloatBuffer normalBuffer;
	protected FloatBuffer textCoordBuffer;
	protected IntBuffer indexBuffer;

	protected AffineMatrix4x4[] affineMatrices;
	protected float[] weights;
	protected List<Integer>[] normalIndices;
	protected boolean needsInitialization = true;
	private boolean geometryIsChanged = true;

	public void initialize( WeightedMesh weightedMesh ) {
		this.weightedMesh = weightedMesh;
		internalInitialize();
	}

	public boolean isGeometryChanged() {
		return geometryIsChanged;
	}

	public void setIsGeometryChanged( boolean isGeometryChanged ) {
		geometryIsChanged = isGeometryChanged;
	}

	private void internalInitialize() {
		if( this.weightedMesh != null ) {
			this.textCoordBuffer = this.weightedMesh.textCoordBuffer.getValue();
			this.indexBuffer = this.weightedMesh.indexBuffer.getValue();

			this.normalBuffer = BufferUtilities.copyFloatBuffer( this.weightedMesh.normalBuffer.getValue() );
			this.vertexBuffer = BufferUtilities.copyDoubleBuffer( this.weightedMesh.vertexBuffer.getValue() );
			int nVertexCount = this.vertexBuffer.limit() / 3;

			this.affineMatrices = new AffineMatrix4x4[ nVertexCount ];
			this.weights = new float[ nVertexCount ];
			for( int i = 0; i < nVertexCount; i++ ) {
				this.affineMatrices[ i ] = new AffineMatrix4x4();
				this.weights[ i ] = 0f;
			}
			needsInitialization = false;
		}
	}

	public void preProcess() {
		for( int i = 0; i < this.affineMatrices.length; i++ ) {
			this.affineMatrices[ i ].setZero();
			this.weights[ i ] = 0f;
		}
	}

	public void process( edu.cmu.cs.dennisc.scenegraph.Joint joint, AffineMatrix4x4 oTransformation ) {
		InverseAbsoluteTransformationWeightsPair iatwp = this.weightedMesh.weightInfo.getValue().getMap().get( joint.jointID.getValue() );
		if( iatwp != null ) {
			//        	System.out.println("\n  Processing mesh "+this.weightedMesh.hashCode());
			//        	System.out.println("  On Joint "+joint.hashCode());
			//        	System.out.println("  Weight Info "+this.weightedMesh.weightInfo.getValue().hashCode());
			//        	System.out.println("  iatwp "+iatwp.hashCode());
			AffineMatrix4x4 oDelta = AffineMatrix4x4.createMultiplication( oTransformation, iatwp.getInverseAbsoluteTransformation() );
			iatwp.reset();
			while( !iatwp.isDone() ) {
				int vertexIndex = iatwp.getIndex();
				float weight = iatwp.getWeight();
				AffineMatrix4x4 transform = AffineMatrix4x4.createMultiplication( oDelta, weight );
				this.affineMatrices[ vertexIndex ].add( transform );
				this.weights[ vertexIndex ] += weight;
				iatwp.advance();
			}
		}
	}

	public void postProcess() {
		for( int i = 0; i < this.affineMatrices.length; i++ ) {
			float weight = this.weights[ i ];
			if( ( 0.999f < weight ) && ( weight < 1.001f ) ) {
				//pass
			} else if( weight != 0 ) {
				AffineMatrix4x4 am = this.affineMatrices[ i ];
				this.affineMatrices[ i ].multiply( 1.0 / weight );
			}
		}
		this.transformBuffers( this.affineMatrices, this.vertexBuffer, this.normalBuffer, this.weightedMesh.vertexBuffer.getValue(), this.weightedMesh.normalBuffer.getValue() );
		setIsGeometryChanged( true );
	}

	private void transformBuffers( AffineMatrix4x4[] voAffineMatrices, DoubleBuffer vertices, FloatBuffer normals, DoubleBuffer verticesSrc, FloatBuffer normalsSrc ) {
		double[] vertexSrc = new double[ 3 ];
		float[] normalSrc = new float[ 3 ];
		double[] vertexDst = new double[ 3 ];
		float[] normalDst = new float[ 3 ];
		vertices.rewind();
		normals.rewind();
		verticesSrc.rewind();
		normalsSrc.rewind();

		for( AffineMatrix4x4 voAffineMatrice : voAffineMatrices ) {
			vertexSrc[ 0 ] = verticesSrc.get();
			vertexSrc[ 1 ] = verticesSrc.get();
			vertexSrc[ 2 ] = verticesSrc.get();
			voAffineMatrice.transformVertex( vertexDst, 0, vertexSrc, 0 );
			vertices.put( vertexDst );

			normalSrc[ 0 ] = normalsSrc.get();
			normalSrc[ 1 ] = normalsSrc.get();
			normalSrc[ 2 ] = normalsSrc.get();
			voAffineMatrice.transformNormal( normalDst, 0, normalSrc, 0 );
			normals.put( normalDst );
		}
	}

	public void renderGeometry( RenderContext rc ) {
		if( this.needsInitialization ) {
			this.internalInitialize();
		}
		MeshAdapter.renderMesh( rc, vertexBuffer, normalBuffer, textCoordBuffer, indexBuffer );
	}

	public void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		if( this.needsInitialization ) {
			this.internalInitialize();
		}
		MeshAdapter.pickMesh( pc, vertexBuffer, indexBuffer );
	}
}
