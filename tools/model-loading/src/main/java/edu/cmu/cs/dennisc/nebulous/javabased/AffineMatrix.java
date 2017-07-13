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
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

public class AffineMatrix
{
	public static float[] IDENTITY_AFFINE_MATRIX = { 1f, 0f, 0f,
	                                                 0f, 1f, 0f,
	                                                 0f, 0f, 1f,
	                                                 0f, 0f, 0f };
	
	public static float[] ZERO_AFFINE_MATRIX = { 0f, 0f, 0f,
                                                 0f, 0f, 0f,
                                                 0f, 0f, 0f,
                                                 0f, 0f, 0f };
	
	public static boolean isWithinReasonableEpsilonOfZero( float fOther ) {
	    return Math.abs( fOther ) < 0.001f;
	}
	public static boolean isWithinReasonableEpsilonOfZero( double dOther ) {
	    return Math.abs( dOther ) < 0.001;
	}
	
	public static AffineMatrix setReturnValueToInverse( AffineMatrix rv, AffineMatrix a ) 
	{
	    AffineMatrix4x4 aliceInverse = AffineMatrix4x4.createInverse(createAliceMatrix(a));
	    rv.set(aliceInverse);
	    return rv;
	}
	
	public static AffineMatrix createInverse( AffineMatrix a ) {
        return setReturnValueToInverse( new AffineMatrix(), a );
    }
    public void setToInverse( AffineMatrix a ) {
        setReturnValueToInverse( this, a );
    }
    public void invert() {
        setToInverse( this );
    }
	
	public static AffineMatrix4x4 createAliceMatrix(AffineMatrix matrix)
	{
	    return new AffineMatrix4x4(
	            matrix.matrix[rc00],
	            matrix.matrix[rc01],
	            matrix.matrix[rc02],
	            matrix.matrix[rc03],
	            matrix.matrix[rc10],
                matrix.matrix[rc11],
                matrix.matrix[rc12],
                matrix.matrix[rc13],
                matrix.matrix[rc20],
                matrix.matrix[rc21],
                matrix.matrix[rc22],
                matrix.matrix[rc23],
                0,
                0,
                0,
                1
	            );
	}
	public static AffineMatrix createXRotationMatrix(double rotationAngle)
	{
	    AffineMatrix rotationMatrix = new AffineMatrix();
	    float[] rotationValues = new float[9];
	    rotationValues[0] = 1;
	    rotationValues[1] = 0;
	    rotationValues[2] = 0;

	    rotationValues[3] = 0;
	    rotationValues[4] = (float)Math.cos(rotationAngle);
	    rotationValues[5] = (float)Math.sin(rotationAngle);

	    rotationValues[6] = 0;
	    rotationValues[7] = -(float)Math.sin(rotationAngle);
	    rotationValues[8] = (float)Math.cos(rotationAngle);
	    rotationMatrix.setRightUpBackward(rotationValues);
	    return rotationMatrix;
	}
	
	private static int rc00 = 0;
	private static int rc10 = 1;
	private static int rc20 = 2;
	private static int rc01 = 3;
	private static int rc11 = 4;
	private static int rc21 = 5;
	private static int rc02 = 6;
	private static int rc12 = 7;
	private static int rc22 = 8;
	private static int rc03 = 9;
	private static int rc13 = 10;
	private static int rc23 = 11;
	
	private float[] matrix = new float[12]; 
	private float[] scale = {1,1,1};
	
	public AffineMatrix()
	{
	    setIdentity();
	}
	
	public AffineMatrix( AffineMatrix other )
	{
	    this.set(other);
	}
	
	public AffineMatrix( AffineMatrix4x4 other )
	{
	    this.set(other);
	}
	
	public void set( AffineMatrix other )
	{
	    System.arraycopy(other.matrix, 0, this.matrix, 0, 12);
	}
	
	public void set( AffineMatrix4x4 other )
	{
	    this.matrix[rc00] = (float)other.orientation.right.x;
        this.matrix[rc10] = (float)other.orientation.right.y;
        this.matrix[rc20] = (float)other.orientation.right.z;
        
        this.matrix[rc01] = (float)other.orientation.up.x;
        this.matrix[rc11] = (float)other.orientation.up.y;
        this.matrix[rc21] = (float)other.orientation.up.z;
        
        this.matrix[rc02] = (float)other.orientation.backward.x;
        this.matrix[rc12] = (float)other.orientation.backward.y;
        this.matrix[rc22] = (float)other.orientation.backward.z;
        
        this.matrix[rc03] = (float)other.translation.x;
        this.matrix[rc13] = (float)other.translation.y;
        this.matrix[rc23] = (float)other.translation.z;
	}
	
	public void setZero()
	{
	    System.arraycopy(ZERO_AFFINE_MATRIX, 0, matrix, 0, matrix.length);
	}
	
	public void setIdentity()
	{
	    System.arraycopy(IDENTITY_AFFINE_MATRIX, 0, matrix, 0, matrix.length);
	}
	
	public boolean isOrientationEquivalentTo( float[] af )
	{
        if (af.length < 9)
        {
            return false;
        }
        for (int i=0; i<9; i++)
        {
            if (af[i] != this.matrix[i])
            {
                return false;
            }
        }
        return true;
    }
	
	public boolean isTranslationEquivalentTo( float[] af )
	{
	    if (af.length < 3)
        {
            return false;
        }
        for (int i=0; i<3; i++)
        {
            if (af[i] != this.matrix[i+9])
            {
                return false;
            }
        }
        return true;
    }
	
	public boolean isTranslationEquivalentTo( float x, float y, float z )
    {
	    return this.matrix[9] == x && this.matrix[10] == y && this.matrix[11] == z;
    }
	
	public float[] GetQuaternion() 
	{
        float a00 = this.matrix[rc00];
        float a10 = this.matrix[rc10];
        float a20 = this.matrix[rc20];
        float a01 = this.matrix[rc01];
        float a11 = this.matrix[rc11];
        float a21 = this.matrix[rc21];
        float a02 = this.matrix[rc02];
        float a12 = this.matrix[rc12];
        float a22 = this.matrix[rc22];
        final float M_EPSILON = 0.001f;
        float trace = a00 + a11 + a22 + 1.0f;
        float q_w;
        float q_x;
        float q_y;
        float q_z;
        if( trace > M_EPSILON ) {
            float s = 0.5f / (float)Math.sqrt(trace);
            q_w = 0.25f / s;
            q_x = ( a21 - a12 ) * s;
            q_y = ( a02 - a20 ) * s;
            q_z = ( a10 - a01 ) * s;
        } else {
            if ( a00 > a11 && a00 > a22 ) {
                float s = 2.0f * (float)Math.sqrt( 1.0f + a00 - a11 - a22);
                q_w = (a21 - a12 ) / s;
                q_x = 0.25f * s;
                q_y = (a01 + a10 ) / s;
                q_z = (a02 + a20 ) / s;
            } else if (a11 > a22) {
                float s = 2.0f * (float)Math.sqrt( 1.0f + a11 - a00 - a22);
                q_w = (a02 - a20 ) / s;
                q_x = (a01 + a10 ) / s;
                q_y = 0.25f * s;
                q_z = (a12 + a21 ) / s;
            } else {
                float s = 2.0f * (float)Math.sqrt( 1.0f + a22 - a00 - a11 );
                q_w = (a10 - a01 ) / s;
                q_x = (a02 + a20 ) / s;
                q_y = (a12 + a21 ) / s;
                q_z = 0.25f * s;
            }
        }
        float[] afQuaternion = new float[4];
        afQuaternion[ 0 ] = q_w;
        afQuaternion[ 1 ] = q_x;
        afQuaternion[ 2 ] = q_y;
        afQuaternion[ 3 ] = q_z;
        return afQuaternion;
    }
	
	public void setQuaternion( float[] afQuaternion ) 
	{
        float q_w = afQuaternion[ 0 ];
        float q_x = afQuaternion[ 1 ];
        float q_y = afQuaternion[ 2 ];
        float q_z = afQuaternion[ 3 ];

        float wx = q_w * q_x;
        float wy = q_w * q_y;
        float wz = q_w * q_z;

        float xx = q_x * q_x;
        float xy = q_x * q_y;
        float xz = q_x * q_z;

        float yy = q_y * q_y;
        float yz = q_y * q_z;

        float zz = q_z * q_z;

        float right_x = 1 - 2 * ( yy + zz );   float up_x = 2 * ( xy - wz );       float backward_x = 2 * ( xz + wy );
        float right_y = 2 * ( xy + wz );       float up_y = 1 - 2 * ( xx + zz );   float backward_y = 2 * ( yz - wx );
        float right_z = 2 * ( xz - wy );       float up_z = 2 * ( yz + wx );       float backward_z = 1 - 2 * ( xx + yy );

        this.matrix[ rc00 ] = right_x;
        this.matrix[ rc10 ] = right_y;
        this.matrix[ rc20 ] = right_z;

        this.matrix[ rc01 ] = up_x;
        this.matrix[ rc11 ] = up_y;
        this.matrix[ rc21 ] = up_z;

        this.matrix[ rc02 ] = backward_x;
        this.matrix[ rc12 ] = backward_y;
        this.matrix[ rc22 ] = backward_z;
    }
	
	public void setRightUpBackward( float[] af ) 
	{
	    System.arraycopy(af, 0, this.matrix, 0, 9);
    }
	
    public void setTranslation( float[] af ) 
    {
        System.arraycopy(af, 0, this.matrix, 9, 3);
    }

    public void setScale( float[] scale )
    {
        this.setScale(scale[0], scale[1], scale[2]);
    }
    
    public void setScale( float sX, float sY, float sZ )
    {
        this.scale[0] = sX;
        this.scale[1] = sY;
        this.scale[2] = sZ;
    }
    
    public boolean isWithinReasonableEpsilonOf( AffineMatrix oOther )
    {
        for( int i=0; i<12; i++ ) {
            if( isWithinReasonableEpsilonOfZero( this.matrix[ i ] - oOther.matrix[ i ] ) ) {
                //pass
            } else {
                return false;
            }
        }
        return true;
    }
    
    public void add( AffineMatrix other )
    {
        for (int i=0; i<this.matrix.length; i++)
        {
            this.matrix[i] += other.matrix[i];
        }
    }
    
    //Generally as setToProduct(preTransformed, localTransform)
    public void setToProduct( AffineMatrix a, AffineMatrix b )
    {
//        AffineMatrix _a = createFromProduct(a, b.scale);
        float sX = a.scale[0] * b.scale[0];
        float sY = a.scale[1] * b.scale[1];
        float sZ = a.scale[2] * b.scale[2];
        
        float bX = b.matrix[rc03];// * a.scale[0];
        float bY = b.matrix[rc13];// * a.scale[1];
        float bZ = b.matrix[rc23];// * a.scale[2];
        
        
        float _rc00 = a.matrix[rc00]*b.matrix[rc00] + a.matrix[rc01]*b.matrix[rc10] + a.matrix[rc02]*b.matrix[rc20];
        float _rc01 = a.matrix[rc00]*b.matrix[rc01] + a.matrix[rc01]*b.matrix[rc11] + a.matrix[rc02]*b.matrix[rc21];
        float _rc02 = a.matrix[rc00]*b.matrix[rc02] + a.matrix[rc01]*b.matrix[rc12] + a.matrix[rc02]*b.matrix[rc22];
        float _rc03 = a.matrix[rc00]*bX + a.matrix[rc01]*bY + a.matrix[rc02]*bZ + a.matrix[rc03];

        float _rc10 = a.matrix[rc10]*b.matrix[rc00] + a.matrix[rc11]*b.matrix[rc10] + a.matrix[rc12]*b.matrix[rc20];
        float _rc11 = a.matrix[rc10]*b.matrix[rc01] + a.matrix[rc11]*b.matrix[rc11] + a.matrix[rc12]*b.matrix[rc21];
        float _rc12 = a.matrix[rc10]*b.matrix[rc02] + a.matrix[rc11]*b.matrix[rc12] + a.matrix[rc12]*b.matrix[rc22];
        float _rc13 = a.matrix[rc10]*bX + a.matrix[rc11]*bY + a.matrix[rc12]*bZ + a.matrix[rc13];

        float _rc20 = a.matrix[rc20]*b.matrix[rc00] + a.matrix[rc21]*b.matrix[rc10] + a.matrix[rc22]*b.matrix[rc20];
        float _rc21 = a.matrix[rc20]*b.matrix[rc01] + a.matrix[rc21]*b.matrix[rc11] + a.matrix[rc22]*b.matrix[rc21];
        float _rc22 = a.matrix[rc20]*b.matrix[rc02] + a.matrix[rc21]*b.matrix[rc12] + a.matrix[rc22]*b.matrix[rc22];
        float _rc23 = a.matrix[rc20]*bX + a.matrix[rc21]*bY + a.matrix[rc22]*bZ + a.matrix[rc23];

        this.matrix[rc00] = _rc00;
        this.matrix[rc01] = _rc01;
        this.matrix[rc02] = _rc02;
        this.matrix[rc10] = _rc10;
        this.matrix[rc11] = _rc11;
        this.matrix[rc12] = _rc12;
        this.matrix[rc20] = _rc20;
        this.matrix[rc21] = _rc21;
        this.matrix[rc22] = _rc22;
        this.matrix[rc03] = _rc03;
        this.matrix[rc13] = _rc13;
        this.matrix[rc23] = _rc23;
        
        this.scale[0] = a.scale[0] * b.scale[0];
        this.scale[1] = a.scale[1] * b.scale[1];
        this.scale[2] = a.scale[2] * b.scale[2];
    }
    
    public void multiply( AffineMatrix other )
    {
        this.setToProduct(this, other);
    }
    
    public void multiply( AffineMatrix4x4 other )
    {
        this.setToProduct(this, new AffineMatrix(other));
    }
    
    public void applyScale( float scale[] )
    {
        this.matrix[rc00] *= scale[0];
        this.matrix[rc01] *= scale[0];
        this.matrix[rc02] *= scale[0];
        this.matrix[rc03] *= scale[0];
        this.matrix[rc10] *= scale[1];
        this.matrix[rc11] *= scale[1];
        this.matrix[rc12] *= scale[1];
        this.matrix[rc13] *= scale[1];
        this.matrix[rc20] *= scale[2];
        this.matrix[rc21] *= scale[2];
        this.matrix[rc22] *= scale[2];
        this.matrix[rc23] *= scale[2];
    }
    
    public static AffineMatrix createFromProduct( AffineMatrix a, AffineMatrix b )
    {
        AffineMatrix result = new AffineMatrix();
        result.setToProduct(a, b);
        return result;
    }
    
    public static AffineMatrix createFromProduct( AffineMatrix a, float scalar )
    {
        AffineMatrix result = new AffineMatrix(a);
        result.multiply(scalar);
        return result;
    }
    
    public static AffineMatrix createFromProduct( AffineMatrix a, float[] scale )
    {
        AffineMatrix result = new AffineMatrix(a);
        result.applyScale(scale);
        return result;
    }
    
    public void multiply( float scalar )
    {
        for (int i=0; i<this.matrix.length; i++)
        {
            this.matrix[i] *= scalar;
        }
        for (int i=0; i<this.scale.length; i++)
        {
            this.scale[i] *= scalar;
        }
    }
    
    public float[] getXAxis()
    {
        return new float[] { this.matrix[0], this.matrix[1], this.matrix[2] };
    }
    
    public float[] getYAxis()
    {
        return new float[] { this.matrix[3], this.matrix[4], this.matrix[5] };
    }
    
    public float[] getZAxis()
    {
        return new float[] { this.matrix[6], this.matrix[7], this.matrix[8] };
    }
    
    public float[] getTranslationAxis()
    {
        return new float[] { this.matrix[9], this.matrix[10], this.matrix[11] };
    }
    
    public float[] getScale()
    {
        return new float[] { this.scale[0], this.scale[1], this.scale[2] };
    }
    
    public float[] transformVertex( float[] afRV, float[] afSrc )
    {
        return transformVertex( afRV, 0, afSrc, 0);
    }
    
    public float[] transformVertex( float[] afRV, int offsetDest, float[] afSrc, int offsetSrc )
    {
        if (afRV == null)
        {
            afRV = new float[3];
            offsetDest = 0;
        }
        transformVector( afRV, offsetDest, afSrc, offsetSrc );
        afRV[ 0 ] += this.matrix[rc03] * this.scale[0];
        afRV[ 1 ] += this.matrix[rc13] * this.scale[1];
        afRV[ 2 ] += this.matrix[rc23] * this.scale[2];
        return afRV;
    }
    
    public float[] transformNormal( float[] afRV, float[] afSrc )
    {
        return transformNormal( afRV, 0, afSrc, 0 );
    }
    
    public float[] transformNormal( float[] afRV, int offsetDest, float[] afSrc, int offsetSrc )
    {
        return transformVector( afRV, offsetDest, afSrc, offsetSrc );
    }
    
    private float[] transformVector( float[] afRV, int offsetDest, float[] afSrc, int offsetSrc )
    {
        if (afRV == null)
        {
            afRV = new float[3];
            offsetDest = 0;
        }
        afRV[ offsetDest ] = this.matrix[rc00]*afSrc[offsetSrc] + this.matrix[rc01]*afSrc[offsetSrc+1] + this.matrix[rc02]*afSrc[offsetSrc+2];
        afRV[ offsetDest + 1 ] = this.matrix[rc10]*afSrc[offsetSrc] + this.matrix[rc11]*afSrc[offsetSrc+1] + this.matrix[rc12]*afSrc[offsetSrc+2];
        afRV[ offsetDest + 2 ] = this.matrix[rc20]*afSrc[offsetSrc] + this.matrix[rc21]*afSrc[offsetSrc+1] + this.matrix[rc22]*afSrc[offsetSrc+2];
        return afRV;
    }
    
    public void setXAxis( double fX, double fY, double fZ ) 
    {
        this.matrix[ 0 ] = (float)fX;
        this.matrix[ 1 ] = (float)fY;
        this.matrix[ 2 ] = (float)fZ;
    }
    
    public void setYAxis( double fX, double fY, double fZ ) 
    {
        this.matrix[ 3 ] = (float)fX;
        this.matrix[ 4 ] = (float)fY;
        this.matrix[ 5 ] = (float)fZ;
    }
    
    public void setZAxis( double fX, double fY, double fZ ) 
    {
        this.matrix[ 6 ] = (float)fX;
        this.matrix[ 7 ] = (float)fY;
        this.matrix[ 8 ] = (float)fZ;
    }
    
    public void setTranslationAxis( double fX, double fY, double fZ ) 
    {
        this.matrix[ 9 ] = (float)fX;
        this.matrix[ 10 ] = (float)fY;
        this.matrix[ 11 ] = (float)fZ;
    }
    
    public void addTranslation( double fX, double fY, double fZ )
    {
        this.matrix[ 9 ] += (float)fX;
        this.matrix[ 10 ] += (float)fY;
        this.matrix[ 11 ] += (float)fZ;
    }
    
    private static final float[] BUFFER_FOR_GL_LOAD_MATRIX = { 0,0,0,0,  0,0,0,0,  0,0,0,0,  0,0,0,1};
    
//    public void glMult(RenderContext rc) 
//    {
//        float[] dst = BUFFER_FOR_GL_LOAD_MATRIX;
//        float[] src = this.matrix;
//        int srcIndex = 0;
//        int dstIndex = 0;
//        for( int c=0; c<4; c++ ) {
//            dst[dstIndex++] = src[srcIndex++];
//            dst[dstIndex++] = src[srcIndex++];
//            dst[dstIndex++] = src[srcIndex++];
//            dstIndex++;
//        }
//        for (int i=0; i<BUFFER_FOR_GL_LOAD_MATRIX.length; i++)
//        {
//            System.out.print(BUFFER_FOR_GL_LOAD_MATRIX[i]+", ");
//        }
//        System.out.println();
//        
//        rc.gl.glMultMatrixf( BUFFER_FOR_GL_LOAD_MATRIX, 0 );
//    }
    
    public void read( InputStream iStream, int versionNum ) throws IOException 
    {
        this.matrix = Utilities.readRawFloatArray(iStream, 12);
        if (versionNum >= Element.MIN_COLLADA_BASED_VERSION)
        {
            this.scale = Utilities.readRawFloatArray(iStream, 3);
        }
    }
    
    public void write( OutputStream oStream ) throws IOException 
    {
        Utilities.writeRawFloatArray(oStream, this.matrix);
        Utilities.writeRawFloatArray(oStream, this.scale);
    }
    
    
	
}
