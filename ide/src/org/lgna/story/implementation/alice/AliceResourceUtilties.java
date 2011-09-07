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

package org.lgna.story.implementation.alice;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;


/**
 * @author Dennis Cosgrove
 */
public class AliceResourceUtilties {
	
	public static String MODEL_RESOURCE_EXTENSION = "a3r";
	public static String TEXTURE_RESOURCE_EXTENSION = "a3t";
	
	
	private static Map<URL, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual> urlToVisualMap = new HashMap<URL, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual>();
	private static Map<URL, TexturedAppearance[]> urlToTextureMap = new HashMap<URL, TexturedAppearance[]>();
	
	private AliceResourceUtilties() {
	}
	
	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual decodeVisual( URL url ) {
		try
		{
	    	java.io.InputStream is = url.openStream();
	    	edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
	    	return decoder.decodeReferenceableBinaryEncodableAndDecodable( new java.util.HashMap< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable >() );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static TexturedAppearance[] decodeTexture( URL url ) {
		try
		{
	    	java.io.InputStream is = url.openStream();
	    	edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
	    	return decoder.decodeReferenceableBinaryEncodableAndDecodableArray( TexturedAppearance.class, new java.util.HashMap< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable >() );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void encodeVisual(final SkeletonVisual toSave, File file) throws IOException
	{
	    edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
	    if (!file.exists())
        {
	        file.createNewFile();
        }
        java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
        edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( fos );
        encoder.encode(toSave, new java.util.HashMap< ReferenceableBinaryEncodableAndDecodable, Integer >());
        encoder.flush();
        fos.close();
	}
	
	public static void encodeTexture(final TexturedAppearance[] toSave, File file) throws IOException
	{
	    edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
	    if (!file.exists())
        {
	        file.createNewFile();
        }
        java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
        edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( fos );
        encoder.encode(toSave, new java.util.HashMap< ReferenceableBinaryEncodableAndDecodable, Integer >());
        encoder.flush();
        fos.close();
        
	}

	public static String getTextureResourceName(Object resource)
	{
		return getTextureResourceName(resource.getClass().getSimpleName(), resource.toString());
	}
	
	public static String getTextureResourceName(String modelName, String textureName)
	{
		textureName = textureName.toUpperCase();
		return modelName.toLowerCase()+"_"+textureName+"."+TEXTURE_RESOURCE_EXTENSION;
	}

	public static String getVisualResourceName(Object resource)
	{
		return getVisualResourceName(resource.getClass().getSimpleName());
	}

	public static String getVisualResourceName(String modelName)
	{
		return modelName.toLowerCase()+"."+MODEL_RESOURCE_EXTENSION;
	}
	
	public static URL getTextureURL(Object resource)
	{
		Class textureClass = resource.getClass();
		String resourceString = "resources/"+getTextureResourceName(resource);
		return textureClass.getResource(resourceString);
	}
	
	public static URL getVisualURL(Object resource)
	{
		Class visualClass = resource.getClass();
		String resourceString = "resources/"+getVisualResourceName(resource);
		return visualClass.getResource(resourceString);
	}
	
	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getVisual(Object resource)
	{
		URL resourceURL = getVisualURL(resource);
		if (urlToVisualMap.containsKey(resourceURL))
		{
			return urlToVisualMap.get(resourceURL);
		}
		else
		{
			edu.cmu.cs.dennisc.scenegraph.SkeletonVisual visual = decodeVisual(resourceURL);
			urlToVisualMap.put(resourceURL, visual);
			return visual;
		}
	}
	
	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getVisualCopy(Object resource)
	{
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual original = getVisual(resource);
		return createCopy(original);
	}
	
	public static TexturedAppearance[] getTexturedAppearances(Object resource)
	{
		URL resourceURL = getTextureURL(resource);
		if (urlToTextureMap.containsKey(resourceURL))
		{
			return urlToTextureMap.get(resourceURL);
		}
		else
		{
			TexturedAppearance[] texture = decodeTexture(resourceURL);
			urlToTextureMap.put(resourceURL, texture);
			return texture;
		}
	}
	
	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual createCopy( edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal ) {
	    edu.cmu.cs.dennisc.scenegraph.Geometry[] sgGeometries = sgOriginal.geometries.getValue();
	    edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] sgTextureAppearances = sgOriginal.textures.getValue();
	    edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] sgWeightedMeshes = sgOriginal.weightedMeshes.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();

	    edu.cmu.cs.dennisc.scenegraph.SkeletonVisual rv = new edu.cmu.cs.dennisc.scenegraph.SkeletonVisual();
	    edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRootCopy = null;
	    if (sgSkeletonRoot != null)
	    {
	    	sgSkeletonRoot = (edu.cmu.cs.dennisc.scenegraph.Joint)sgSkeletonRoot.newCopy();
	    }

    	rv.skeleton.setValue( sgSkeletonRootCopy );
		rv.geometries.setValue( sgGeometries );
		rv.weightedMeshes.setValue( sgWeightedMeshes );
		rv.textures.setValue( sgTextureAppearances );
		return rv;
	}
}
