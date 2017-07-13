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
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

//import static com.jogamp.opengl.GL.*;
//import static com.jogamp.opengl.GL2.*;

import edu.cmu.cs.dennisc.scenegraph.Indices;


public class Mesh extends Node
{
    private List<ShaderIndicesPair> shaderIndicesPairs;
    private float[] textureCoordinates;
    private VerticesAndNormals verticesAndNormals;
    private List<BlendShape> blendShapes;
    private int triangleMode = 0x4; //GL.GL_TRIANGLES;
    private FloatBuffer vertexBuffer = null;
    private FloatBuffer normalBuffer = null;
    private FloatBuffer texCoords = null;
    private IntBuffer indexBuffer = null;
    
    private Texture texture = null;
    private TextureGroup textureGroup = null;
    
    private int[] edgeVertices;
    
    protected MeshType meshType = MeshType.MAYA_BASED;
    
    public enum MeshType
    {
        MAYA_BASED,
        COLLADA_BASED;
        
        public static MeshType getMeshType(int intVal)
        {
            return MeshType.values()[intVal];
        }
    }
    
    public Mesh()
    {
        this.shaderIndicesPairs = new LinkedList<ShaderIndicesPair>();
        this.textureCoordinates = new float[0];
        this.verticesAndNormals = null;
    }
    
    @Override
    public String toString() {
    	return this.getName();
    }
    
    protected Indices[] createSGIndices()
    {
        Indices[] sgIndices = new Indices[this.shaderIndicesPairs.size()];
        for (int i=0; i<this.shaderIndicesPairs.size(); i++)
        {
            sgIndices[i] = new Indices();
            ShaderIndicesPair sip = this.shaderIndicesPairs.get(i);
            sip.adjustIndicesIfNecessary();
            sgIndices[i].setIndices(sip.getIndices(), sip.needsAdjustment());
        }
        return sgIndices;
    }
    
    protected List<VertexMapData> vertexInfo = new LinkedList<VertexMapData>();
    
    protected edu.cmu.cs.dennisc.scenegraph.Mesh initializeSceneGraphMesh(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh)
    {
        if (sgMesh == null)
        {
            sgMesh = new edu.cmu.cs.dennisc.scenegraph.Mesh();
        }
        sgMesh.setName(this.getName());
        int textureId = -1;
        if (this.getTextureGroup() != null )
        {
        	if (this.getTextureGroup().getActiveTexture() == null) {
        		System.out.println("PROBLEM");
        	}
        	else
        	{
        		System.out.println("Setting "+this.getName()+" to texture "+this.getTextureGroup().getActiveTexture().getName()+" ("+this.getTextureGroup().getID()+")");
        	}
        	textureId = this.getTextureGroup().getID();
        }
        sgMesh.textureId.setValue(textureId);
        if (this.meshType == MeshType.MAYA_BASED)
        {
//            sgMesh.meshType.setValue(edu.cmu.cs.dennisc.scenegraph.Mesh.MeshType.ALICE_BASED);
            //Copy over the indices
            Indices[] sgIndices = createSGIndices();
//            sgMesh.indices.setValue(sgIndices);
            //Copy over the vertices
            double[] sgVertices = new double[this.verticesAndNormals.getVertexCount()*3]; //the count is how many vertices, but we store 3 floats per vertex
            for (int i=0; i<sgVertices.length; i++)
            {
                sgVertices[i] = this.verticesAndNormals.getVertices()[i];
            }
//            sgMesh.vertices.setValue(sgVertices);
            //Copy over the normals
            float[] sgNormals = new float[this.verticesAndNormals.getNormalCount()*3];  //the count is how many normals, but we store 3 floats per normal
            for (int i=0; i<sgNormals.length; i++)
            {
                sgNormals[i] = this.verticesAndNormals.getNormals()[i];
            }
            
//            sgMesh.normals.setValue(sgNormals);
            //Copy over the texture coordinates
            float[] sgTextureCoordinates = new float[this.textureCoordinates.length];
            for (int i=0; i<sgTextureCoordinates.length; i++)
            {
                sgTextureCoordinates[i] = this.textureCoordinates[i];
            }
            
//            sgMesh.textureCoordinates.setValue(sgTextureCoordinates);
            
            int totalIndices = 0;
            for( Indices ip : sgIndices ) 
            {
                ip.adjustIndicesIfNecessary();
                totalIndices += (ip.getIndices().length / (3)); //The indices actually pack 3 indices (normal index, vertex index, and texcoord index)
            }
            
            int[] indexData = new int[totalIndices];
            int itemTotal = 0;
            this.vertexInfo = new LinkedList<VertexMapData>();
            for( Indices ip : sgIndices ) 
            {
                ip.adjustIndicesIfNecessary();
                int[] pnIndices = ip.getIndices();
                int nIndexCount = ip.getIndexCount();
                for( int i=0; i<nIndexCount;  ) {
                    int texIndex = pnIndices[ i++ ]; 
                    int normalIndex = pnIndices[ i++ ];
                    int vertexIndex = pnIndices[ i++ ];
                    VertexMapData vert = new VertexMapData(vertexIndex, normalIndex, texIndex);
                    int vertIndex = this.vertexInfo.indexOf(vert); 
                    if (vertIndex == -1)
                    {
                        this.vertexInfo.add(vert);
                        vertIndex = this.vertexInfo.size() - 1;
                        vert.newIndex = vertIndex;
                    }
                    indexData[itemTotal] = vertIndex;
                    itemTotal++;
                }
            }
            
            double[] vertexData = new double[this.vertexInfo.size()*3];
            float[] normalData = new float[this.vertexInfo.size()*3];
            float[] textureData = new float[this.vertexInfo.size()*2];
            
            for (VertexMapData vert : this.vertexInfo)
            {
                int texIndex = vert.oldTexIndex;
                textureData[vert.newIndex*2] = sgTextureCoordinates[texIndex];
                textureData[vert.newIndex*2+1] = sgTextureCoordinates[texIndex+1];
                int normalIndex = vert.oldNormalIndex;
                if (normalIndex < 0)
                {
                	System.out.println("?");
                }
                else
                {
//                	System.out.println("normal index: "+normalIndex);
                }
                normalData[vert.newIndex*3] = sgNormals[normalIndex];
                normalData[vert.newIndex*3+1] = sgNormals[normalIndex+1];
                normalData[vert.newIndex*3+2] = sgNormals[normalIndex+2];
                int vertexIndex = vert.oldVertexIndex;
                vertexData[vert.newIndex*3] = sgVertices[vertexIndex];
                vertexData[vert.newIndex*3+1] = sgVertices[vertexIndex+1];
                vertexData[vert.newIndex*3+2] = sgVertices[vertexIndex+2];
            }
            
            sgMesh.indexBuffer.setValue(Utilities.createIntBuffer(indexData));
            sgMesh.normalBuffer.setValue(Utilities.createFloatBuffer(normalData));
            sgMesh.vertexBuffer.setValue(Utilities.createDoubleBuffer(vertexData));
            sgMesh.textCoordBuffer.setValue(Utilities.createFloatBuffer(textureData));
            
//            int oldIndicesCount = 0;
//            for( Indices ip : sgIndices ) 
//            {
//                oldIndicesCount += ip.getIndices().length;
//            }
//            int byteTotal = 0;
//            System.out.println("Old data:");
//            System.out.println("  vertices: "+sgVertices.length+" doubles = "+sgVertices.length*(Double.SIZE/8)+" bytes");
//            byteTotal += sgVertices.length*(Double.SIZE/8);
//            System.out.println("  normals: "+sgNormals.length+" floats = "+sgNormals.length*(Float.SIZE/8)+" bytes");
//            byteTotal += sgNormals.length*(Float.SIZE/8);
//            System.out.println("  texture coordinates: "+sgTextureCoordinates.length+" floats = "+sgTextureCoordinates.length*(Float.SIZE/8)+" bytes");
//            byteTotal += sgTextureCoordinates.length*(Float.SIZE/8);
//            System.out.println("  indices: "+oldIndicesCount+" ints = "+oldIndicesCount*(Integer.SIZE/8)+" bytes");
//            byteTotal += oldIndicesCount*(Integer.SIZE/8);
//            System.out.println(" Total: "+byteTotal+" bytes"); 
//            int oldTotal = byteTotal;
//            byteTotal = 0;
//            System.out.println("New data:");
//            System.out.println("  vertices: "+sgMesh.vertexBuffer.getValue().capacity()+" doubles = "+sgMesh.vertexBuffer.getValue().capacity()*(Double.SIZE/8)+" bytes");
//            byteTotal += sgMesh.vertexBuffer.getValue().capacity()*(Double.SIZE/8);
//            System.out.println("  normals: "+sgMesh.normalBuffer.getValue().capacity()+" floats = "+sgMesh.normalBuffer.getValue().capacity()*(Float.SIZE/8)+" bytes");
//            byteTotal += sgMesh.normalBuffer.getValue().capacity()*(Float.SIZE/8);
//            System.out.println("  texture coordinates: "+sgMesh.textCoordBuffer.getValue().capacity()+" floats = "+sgMesh.textCoordBuffer.getValue().capacity()*(Float.SIZE/8)+" bytes");
//            byteTotal += sgMesh.textCoordBuffer.getValue().capacity()*(Float.SIZE/8);
//            System.out.println("  indices: "+sgMesh.indexBuffer.getValue().capacity()+" ints = "+sgMesh.indexBuffer.getValue().capacity()*(Integer.SIZE/8)+" bytes");
//            byteTotal += sgMesh.indexBuffer.getValue().capacity()*(Integer.SIZE/8);
//            System.out.println(" Total: "+byteTotal+" bytes"); 
//            int newTotal = byteTotal;
//            System.out.println("Inflated by " +((double)newTotal)/oldTotal);
//            System.out.println();
        }
        else if (this.meshType == MeshType.COLLADA_BASED)
        {
            sgMesh.indexBuffer.setValue(this.indexBuffer);
            sgMesh.normalBuffer.setValue(this.normalBuffer);
            DoubleBuffer vertexBufferd = Utilities.createDoubleBuffer(this.vertexBuffer);
            sgMesh.vertexBuffer.setValue(vertexBufferd);
            sgMesh.textCoordBuffer.setValue(this.texCoords);
        }
        
        return sgMesh;
    }
    
    public edu.cmu.cs.dennisc.scenegraph.Mesh createSceneGraphMesh()
    {
        return initializeSceneGraphMesh(null);
    }
    
	@Override
	public int getClassID()
	{
		return Element.MESH;
	}
	
	public void setMeshType( MeshType meshType )
	{
	    this.meshType = meshType;
	}
	
	public void setMeshType( int meshTypeVal )
	{
	    this.setMeshType(MeshType.getMeshType(meshTypeVal));
	}
	
	public MeshType getMeshType()
	{
	    return this.meshType;
	}
	
	public void setTexture( Texture texture )
	{
	    this.texture = texture;
	}
	
	public Texture getTexture()
	{
	    return this.texture;
	}
	
	public void setTextureGroup( TextureGroup textureGroup )
	{
	    this.textureGroup = textureGroup;
	}
	
	public TextureGroup getTextureGroup()
	{
	    return this.textureGroup;
	}
	
	public void setTriangleMode(int triangleMode)
	{
	    this.triangleMode = triangleMode;
	}
    
    public FloatBuffer getVertexBuffer()
    {
        return this.vertexBuffer;
    }
    
    public FloatBuffer getNormalBuffer()
    {
        return this.normalBuffer;
    }
    
    public FloatBuffer getTexCoords()
    {
        return this.texCoords;
    }
	
    public IntBuffer getIndexBuffer()
    {
        return this.indexBuffer;
    }
    
	public ShaderIndicesPair getShaderIndicesPairAt( int nIndex )
	{
        return this.shaderIndicesPairs.get(nIndex);
    }
	
    public void addShaderIndicesPair( ShaderIndicesPair poShaderIndicesPair ) 
    {
        this.shaderIndicesPairs.add( poShaderIndicesPair );
    }
    
    public void addTextureCoordinates( double fU, double fV ) 
    {
        this.textureCoordinates = Utilities.addFloatToArray(this.textureCoordinates, (float)fU);
        this.textureCoordinates = Utilities.addFloatToArray(this.textureCoordinates, (float)fV);
    }

    public int getShaderIndicesPairCount()
    {
        return this.shaderIndicesPairs.size();
    }
    
    public List<ShaderIndicesPair> getShaderIndicesPairs()
    {
        return this.shaderIndicesPairs;
    }
    
    public int getTextureCoordinateCount()
    {
        return this.textureCoordinates.length / 2;
    }
    
    public float[] getTextureCoordinates()
    {
        return this.textureCoordinates;
    }
    
    public VerticesAndNormals getVerticesAndNormals()
    {
        return this.verticesAndNormals;
    }
    
    public void setVerticesAndNormals( VerticesAndNormals poVerticesAndNormals ) 
    {
        this.verticesAndNormals = poVerticesAndNormals;
    }
	
    public void setIndexBuffer( IntBuffer indexBuffer )
    {
        this.indexBuffer = indexBuffer;
    }
    
//    public void renderMesh( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, FloatBuffer vertices, FloatBuffer normals )
//    {
//        rc.gl.glEnableClientState(GL_VERTEX_ARRAY);
//        vertices.rewind();
//        rc.gl.glVertexPointer(3, GL_FLOAT, 0, vertices);
//        rc.gl.glEnableClientState(GL_NORMAL_ARRAY);
//        normals.rewind();
//        rc.gl.glNormalPointer(GL_FLOAT, 0, normals);
//        rc.gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
//        this.texCoords.rewind();
//        rc.gl.glTexCoordPointer(2, GL_FLOAT, 0, this.texCoords);
//        
//        this.indexBuffer.rewind();
//
//        rc.gl.glDrawElements(this.triangleMode, this.indexBuffer.remaining(), GL_UNSIGNED_INT, this.indexBuffer);
//    }
//    
//    public void renderMesh( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, VerticesAndNormals verticesAndNormals )
//    {
//        float[] vfTextureCoordinates = this.getTextureCoordinates();
//        float[] vfVertices = verticesAndNormals.getVertices();
//        float[] vfNormals = verticesAndNormals.getNormals();
//
//        for( ShaderIndicesPair sip : this.shaderIndicesPairs ) 
//        {
//            sip.adjustIndicesIfNecessary();
//            int[] pnIndices = sip.getIndices();
//
//            rc.gl.glBegin( this.triangleMode );
//            int nIndexCount = sip.getIndexCount();
//            int nIndex;
//            
//            for( int i=0; i<nIndexCount;  ) {
//                nIndex = pnIndices[ i++ ];
//                rc.gl.glTexCoord2fv(vfTextureCoordinates, nIndex);
//                nIndex = pnIndices[ i++ ];
//                rc.gl.glNormal3fv(vfNormals, nIndex);
//                nIndex = pnIndices[ i++ ];
//                rc.gl.glVertex3fv( vfVertices, nIndex );
//            }
//            rc.gl.glEnd();
//        }
//    }
//    
//    public void pickMesh( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, FloatBuffer vertices)
//    {
//        pc.gl.glPushName( -1 );
//        pc.gl.glEnableClientState(GL_VERTEX_ARRAY);
//        vertices.rewind();
//        pc.gl.glVertexPointer(3, GL_FLOAT, 0, vertices);
//        this.indexBuffer.rewind();
//        pc.gl.glDrawElements(this.triangleMode, this.indexBuffer.remaining(), GL_UNSIGNED_INT, this.indexBuffer);
//        pc.gl.glPopName();
//    }
//    
//    public void pickMesh( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, VerticesAndNormals verticesAndNormals )
//    {
//        pc.gl.glPushName( -1 );
//        if (this.normalBuffer != null && this.vertexBuffer != null && this.texCoords != null && this.indexBuffer != null)
//        {
//            pc.gl.glEnableClientState(GL_VERTEX_ARRAY);
//            this.vertexBuffer.rewind();
//            pc.gl.glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);
//            this.indexBuffer.rewind();
//            pc.gl.glDrawElements(this.triangleMode, this.indexBuffer.remaining(), GL_UNSIGNED_INT, this.indexBuffer);
//        }
//        else
//        {
//            float[] vfVertices = verticesAndNormals.getVertices();
//            for( ShaderIndicesPair sip : this.shaderIndicesPairs ) 
//            {
//                sip.adjustIndicesIfNecessary();
//                int[] pnIndices = sip.getIndices();
//                pc.gl.glBegin( this.triangleMode );
//                int nIndexCount = sip.getIndexCount();
//                int nIndex;
//                for( int i=0; i<nIndexCount;  ) {
//                    i++;
//                    i++;
//                    nIndex = pnIndices[ i++ ];
//                    pc.gl.glVertex3fv( vfVertices, nIndex );
//                }
//                pc.gl.glEnd();
//            }
//        }
//        pc.gl.glPopName();
//    }
    
    public void setVertexBuffer( FloatBuffer vertexBuffer )
    {
        this.vertexBuffer = vertexBuffer;
    }
    
    public void setNormalBuffer( FloatBuffer normalBuffer )
    {
        this.normalBuffer = normalBuffer;
    }
    
    public void setTexCoordsBuffer( FloatBuffer texCoords )
    {
        this.texCoords = texCoords;
    }
    
//    public void renderMesh(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc)
//    {
//        renderMesh( rc, this.verticesAndNormals );
//    }
//    public void pickMesh(edu.cmu.cs.dennisc.render.gl.imp.PickContext pc)
//    {
//        pickMesh( pc, this.verticesAndNormals );
//    }
    
    @Override
    protected void readInternal(InputStream iStream, int nVersion) throws IOException 
    {
        super.readInternal(iStream, nVersion);
        if (nVersion < MIN_COLLADA_BASED_VERSION)
        {
            this.meshType = MeshType.MAYA_BASED;
        }
        else
        {
            int meshTypeVal = Utilities.readInt(iStream);
            this.setMeshType(meshTypeVal);
        }
        if (this.meshType == MeshType.MAYA_BASED)
        {
            this.triangleMode = 0x4; //GL.GL_TRIANGLES;
            int numshaderIndexPairs = Utilities.readInt(iStream);
            this.shaderIndicesPairs = new LinkedList<ShaderIndicesPair>();
            for (int i=0; i<numshaderIndexPairs; i++)
            {
                ShaderIndicesPair sip = new ShaderIndicesPair();
                sip.read(iStream, nVersion);
                this.shaderIndicesPairs.add(sip);
            }
            this.textureCoordinates = Utilities.readFloatArray(iStream);
                
            boolean hasVerticesAndNormals = Utilities.readBool(iStream);
            if (hasVerticesAndNormals)
            {
                Element van = Element.construct(iStream, nVersion);
                assert van instanceof VerticesAndNormals;
                this.verticesAndNormals = (VerticesAndNormals)van;
            }
            if (nVersion >= 4)
            {
            	boolean hasEdges = Utilities.readBool(iStream);
                if (hasEdges)
                {
                	this.edgeVertices = Utilities.readIntArray(iStream);
                }
                else
                {
                	this.edgeVertices = new int[0];
                }
            }
            this.blendShapes = new LinkedList<BlendShape>();
            if (nVersion >= 5)
            {
            	boolean hasBlendShapes = Utilities.readBool(iStream);
                if (hasBlendShapes)
                {
                	 int numBlendShapes = Utilities.readInt(iStream);
                     for (int i=0; i<numBlendShapes; i++)
                     {
                         BlendShape blendShape = new BlendShape();
                         blendShape.read(iStream, nVersion);
                         this.blendShapes.add(blendShape);
                     }
                }
            }
        }
        else if (this.meshType == MeshType.COLLADA_BASED)
        {
            this.triangleMode = Utilities.readInt(iStream);
            this.vertexBuffer = Utilities.createFloatBuffer(Utilities.readFloatArray(iStream));
            this.normalBuffer = Utilities.createFloatBuffer(Utilities.readFloatArray(iStream));
            this.texCoords = Utilities.createFloatBuffer(Utilities.readFloatArray(iStream));
            this.indexBuffer = Utilities.createIntBuffer(Utilities.readIntArray(iStream));
        }
    }
    
    @Override
    protected void writeInternal(OutputStream oStream) throws IOException 
    {
        super.writeInternal(oStream);
        Utilities.writeInt(this.meshType.ordinal(), oStream);
        if (this.meshType == MeshType.MAYA_BASED)
        {
            Utilities.writeInt(this.shaderIndicesPairs.size(), oStream);
            for (ShaderIndicesPair sip : this.shaderIndicesPairs)
            {
                sip.write(oStream);
            }
            Utilities.writeFloatArray(oStream, this.textureCoordinates);
            if (this.verticesAndNormals != null)
            {
                oStream.write(1);
                this.verticesAndNormals.writeWithoutVersion(oStream);
            }
            else
            {
                oStream.write(0);
            }
        }
        else if (this.meshType == MeshType.COLLADA_BASED)
        {
            Utilities.writeInt(this.triangleMode, oStream);
            Utilities.writeFloatArray(oStream, Utilities.convertFloatBufferToArray(this.vertexBuffer));
            Utilities.writeFloatArray(oStream, Utilities.convertFloatBufferToArray(this.normalBuffer));
            Utilities.writeFloatArray(oStream, Utilities.convertFloatBufferToArray(this.texCoords));
            Utilities.writeIntArray(oStream, Utilities.convertIntBufferToArray(this.indexBuffer));
        }
    }
    
    @Override
    protected void renderSelfVisualization(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc) 
    {
        //Do nothing
    }

	
}
