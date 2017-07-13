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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.print.PrintUtilities;

public abstract class Element {
	public static final int MIN_COLLADA_BASED_VERSION = 255;
	public static final int VERSION = MIN_COLLADA_BASED_VERSION + 0;
	public static final int INVALID = 0; 
	public static final int ELEMENT = 1 << 0; 
	public		static final int NAMED = ELEMENT | ( 1 << 1 ); 
	public			static final int NODE = NAMED | ( 1 << 2 ); 
	public				static final int UNKNOWN = NODE | ( 1 << 3 ); 
	public				static final int MESH = NODE | ( 1 << 4 ); 
	public					static final int WEIGHTED_MESH = MESH | ( 1 << 5 ); 
	public				static final int TRANSFORM = NODE | ( 1 << 6 ); 
	public					static final int JOINT = TRANSFORM | ( 1 << 7 ); 
	
	public		static final int INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR = ELEMENT | ( 1 << 8 ); 
	public			static final int PLENTIFUL_INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR = INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR | ( 1 << 9 ); 
	public			static final int SPARSE_INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR = INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR | ( 1 << 10 ); 
	
	public		static final int ANIMATION = ELEMENT | ( 1 << 11 ); 
	public			static final int SKELETON_ANIMATION = ANIMATION | ( 1 << 12 ); 
	public				static final int BAKED_SKELETON_ANIMATION = SKELETON_ANIMATION | ( 1 << 13 ); 
	public				static final int FLEXIBLE_SKELETON_ANIMATION = SKELETON_ANIMATION | ( 1 << 14 ); 
	
	public		static final int TRANSFORM_CHANNEL = ELEMENT | ( 1 << 15 ); 
	public			static final int BAKED_TRANSFORM_CHANNEL = TRANSFORM_CHANNEL | ( 1 << 16 ); 
	public			static final int FLEXIBLE_TRANSFORM_CHANNEL = TRANSFORM_CHANNEL | ( 1 << 17 ); 
	
	public		static final int VERTICES_AND_NORMALS = ELEMENT | ( 1 << 18 ); 
	
	public		static final int SHADER = ELEMENT | ( 1 << 19 ); 
	public			static final int LAMBERT_SHADER = SHADER | ( 1 << 20 ); 
	public			static final int MATERIAL_SHADER = SHADER | ( 1 << 21 );
	public			static final int BLEND_WITH_SKIN_SHADER = LAMBERT_SHADER | ( 1 << 22 );
	public			static final int TEXTURE_SHADER = SHADER | ( 1 << 23 ); 
				
	public int getClassID()
	{
		return ELEMENT;
	}

	public boolean isA( int nClassID ) {
		return ( getClassID() & nClassID ) == nClassID;
	}

	protected abstract void readInternal( InputStream iStream, int nVersion ) throws IOException;
	protected abstract void writeInternal( OutputStream oStream) throws IOException;
	
	public void writeWithVersion(OutputStream oStream) throws IOException
	{
	    writeVersion(oStream);
        this.writeWithoutVersion(oStream);
	}
	
	public File writeWithVersion(File outFile) throws IOException, FileNotFoundException
    {
        if (!outFile.exists())
        {
            FileUtilities.createParentDirectoriesIfNecessary(outFile);
            if (!outFile.createNewFile())
            {
                return null;
            }
        }
        OutputStream oStream = new FileOutputStream(outFile);
        writeWithVersion(oStream);
        oStream.close();
        return outFile;
    }
	
	public File writeWithVersion(String outPath) throws IOException, FileNotFoundException
	{
		File outFile = new File(outPath);
		return writeWithVersion(outFile);
	}
	
	public static Element construct(InputStream iStream, int nVersion)
	{
		Element element = null;
		try
		{
			int classID = Utilities.readInt(iStream);
			switch (classID)
			{
//				case Element.NAMED: element = new Named(); break;
//				case Element.NODE: element = new Node(); break;
			    case Element.UNKNOWN: element = new Unknown(); break;
			    case Element.SHADER: element = new Shader(); break;
			    case Element.LAMBERT_SHADER: element = new LambertShader(); break;
			    case Element.TEXTURE_SHADER: element = new TextureShader(); break;
			    case Element.MATERIAL_SHADER: element = new MaterialShader(); break;
				case Element.MESH: element = new Mesh(); break;
				case Element.JOINT: element = new Joint(); break;
				case Element.TRANSFORM: element = new Transform(); break;
				case Element.WEIGHTED_MESH: element = new WeightedMesh(); break;
				case Element.VERTICES_AND_NORMALS: element = new VerticesAndNormals(); break;
				case Element.PLENTIFUL_INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR: element = new PlentifulInverseAbsoluteTransformationWeightsPair(); break;
				case Element.SPARSE_INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR: element = new SparseInverseAbsoluteTransformationWeightsPair(); break;
			}
			if (element != null)
			{
				element.readInternal(iStream, nVersion);
			}
			else
			{
			    PrintUtilities.println("FAILED TO CREATE ELEMENT FOR ID: "+classID);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return element;
	}
	
	protected void writeWithoutVersion(OutputStream oStream) throws IOException
	{
		oStream.write(Utilities.intToByteArray(this.getClassID()));
		this.writeInternal(oStream);
	}
	
	protected static int readVersion( InputStream iStream ) throws IOException
	{
		return Utilities.readInt(iStream);
	}
	
	protected static void writeVersion( OutputStream oStream ) throws IOException
	{
		oStream.write(Utilities.intToByteArray(VERSION));
	}
}

