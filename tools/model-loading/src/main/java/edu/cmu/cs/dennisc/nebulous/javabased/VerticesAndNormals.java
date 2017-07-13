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

package edu.cmu.cs.dennisc.nebulous.javabased;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;

public class VerticesAndNormals extends Element
{
	private float[] normals;
	private float[] vertices;
	
	
	public VerticesAndNormals()
	{
	    this.normals = new float[0];
	    this.vertices = new float[0];
	}
	
	@Override
	public int getClassID()
	{
		return Element.VERTICES_AND_NORMALS;
	}

	@Override
	protected void readInternal(InputStream iStream, int nVersion) throws IOException 
	{
		this.normals = Utilities.readFloatArray(iStream);
		this.vertices = Utilities.readFloatArray(iStream);

		for( int i=0; i<this.normals.length; i+=3 ) {
			float fI = this.normals[i];
			float fJ =this.normals[ i+1 ];
			float fK = this.normals[ i+2 ];
			float fMagnitudeSquared = fI*fI + fJ*fJ + fK*fK;
			if( 0.99f < fMagnitudeSquared && fMagnitudeSquared < 1.01f ) {
				//pass
			} else {
				float fMagnitude = (float)Math.sqrt( fMagnitudeSquared );
				this.normals[ i ] = fI / fMagnitude;
				this.normals[ i+1 ] = fJ / fMagnitude;
				this.normals[ i+2 ] = fK / fMagnitude;
			}
		}
		
	}

	@Override
	protected void writeInternal(OutputStream oStream) throws IOException 
	{
		Utilities.writeFloatArray(oStream, this.normals);
		Utilities.writeFloatArray(oStream, this.vertices);
	}
	
	public void resize( VerticesAndNormals other)
	{
	    resizeNormals(other.normals.length);
	    resizeVertices(other.vertices.length);
	}
	
	public void resizeNormals( int normalCount )
	{
	    this.normals = new float[normalCount];
	}
	
	public void resizeVertices( int vertexCount )
	{
	    this.vertices = new float[vertexCount];
	}
	
	public void interpolate( VerticesAndNormals vnA, VerticesAndNormals  vnB, double dPortion ) {
		float fPortion = (float)dPortion;
		for( int i=0; i<vertices.length; i++ ) {
			this.vertices[i] = vnA.vertices[i] + (vnB.vertices[i] - vnA.vertices[i])*fPortion;
		}
		for( int i=0; i<normals.length; i++ ) {
			this.normals[i] =  vnA.normals[i] + (vnB.normals[i] - vnA.normals[i])*fPortion;
		}
	}
	
	public void transform( AffineMatrix4x4[] voAffineMatrices, List< Integer >[] vnNormalIndices, VerticesAndNormals poOther) {
		int iTimes3 = 0;
		for( int i=0; i<voAffineMatrices.length; i++, iTimes3+=3 ) {
			voAffineMatrices[i].transformVertex( this.vertices, iTimes3, poOther.vertices, iTimes3 );
			for( int j=0; j<vnNormalIndices[i].size(); j++ ) {
				int kTimes3 = vnNormalIndices[i].get(j)*3;
				voAffineMatrices[i].transformNormal( this.normals, kTimes3, poOther.normals, kTimes3 );
			}
		}
	}
	
	public int getNormalCount()
	{
		return this.normals.length / 3;
	}
	
	public float[] getNormals()
	{
		return this.normals;
	}
	
	public int getVertexCount()
	{
		return this.vertices.length / 3;
	}
	
	public float[] getVertices()
	{
		return this.vertices;
	}

	public void addNormal( float i, float j, float k)
	{
		this.normals = Utilities.addFloatToArray(this.normals, i);
		this.normals = Utilities.addFloatToArray(this.normals, j);
		this.normals = Utilities.addFloatToArray(this.normals, k);
	}
	
	public void addVertex( float x, float y, float z)
	{
		this.vertices = Utilities.addFloatToArray(this.vertices, x);
		this.vertices = Utilities.addFloatToArray(this.vertices, y);
		this.vertices = Utilities.addFloatToArray(this.vertices, z);
	}
	
}
