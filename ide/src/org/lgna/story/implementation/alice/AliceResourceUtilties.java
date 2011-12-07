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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ConstructorBlockStatement;
import org.lgna.project.ast.ConstructorInvocationStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaConstructor;
import org.lgna.project.ast.JavaConstructorParameter;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.ReturnStatement;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.SuperConstructorInvocationStatement;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resourceutilities.ConstructorParameterPair;
import org.lgna.story.resourceutilities.ModelResourceBuilderUtilities;
import org.lgna.story.resourceutilities.ModelResourceExporter;
import org.lgna.story.resourceutilities.ModelResourceInfo;
import org.lgna.story.resourceutilities.StorytellingResources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.xml.XMLUtilities;


/**
 * @author Dennis Cosgrove
 */
public class AliceResourceUtilties {
	
	public static String MODEL_RESOURCE_EXTENSION = "a3r";
	public static String TEXTURE_RESOURCE_EXTENSION = "a3t";
	
	
	private static Map<URL, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual> urlToVisualMap = new HashMap<URL, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual>();
	private static Map<URL, TexturedAppearance[]> urlToTextureMap = new HashMap<URL, TexturedAppearance[]>();
	private static Map<Class<?>, org.lgna.story.resourceutilities.ModelResourceInfo> classToInfoMap = new HashMap<Class<?>, org.lgna.story.resourceutilities.ModelResourceInfo>();
	
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

	public static InputStream getAliceResourceAsStream(Class<?> cls, String resourceString) {
		return StorytellingResources.getInstance().getAliceResourceAsStream(cls.getPackage().getName().replace(".", "/")+"/"+resourceString);
	}
	
	public static URL getAliceResource(Class<?> cls, String resourceString) {
		return StorytellingResources.getInstance().getAliceResource(cls.getPackage().getName().replace(".", "/")+"/"+resourceString);
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
	
	private static java.net.URL getThumbnailURLInternal(Class<?> modelResource, String name) {
		return getAliceResource(modelResource, ModelResourceExporter.getResourceSubDirWithSeparator()+ name+".png");
	}
	
	public static URL getTextureURL(Object resource)
	{
		return getAliceResource(resource.getClass(), ModelResourceExporter.getResourceSubDirWithSeparator()+getTextureResourceName(resource));
	}
	
	public static URL getVisualURL(Object resource)
	{
		return getAliceResource(resource.getClass(), ModelResourceExporter.getResourceSubDirWithSeparator()+getVisualResourceName(resource));
	}
	
	public static URL getModelResourceURL(Object resource)
	{
		return getAliceResource(resource.getClass(), ModelResourceExporter.getResourceSubDirWithSeparator()+getTextureResourceName(resource));
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
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = sgOriginal.baseBoundingBox.getValue();
		edu.cmu.cs.dennisc.math.Matrix3x3 scaleCopy = new edu.cmu.cs.dennisc.math.Matrix3x3(sgOriginal.scale.getValue());
		edu.cmu.cs.dennisc.scenegraph.Appearance sgFrontAppearanceCopy;
		if (sgOriginal.frontFacingAppearance.getValue() != null){
			sgFrontAppearanceCopy = (edu.cmu.cs.dennisc.scenegraph.Appearance)sgOriginal.frontFacingAppearance.getValue().newCopy();
		}
		else{
			sgFrontAppearanceCopy = null;
		}
		edu.cmu.cs.dennisc.scenegraph.Appearance sgBackAppearanceCopy;
		if (sgOriginal.backFacingAppearance.getValue() != null){
			sgBackAppearanceCopy = (edu.cmu.cs.dennisc.scenegraph.Appearance)sgOriginal.backFacingAppearance.getValue().newCopy();
		}
		else{
			sgBackAppearanceCopy = null;
		}
		
	    edu.cmu.cs.dennisc.scenegraph.SkeletonVisual rv = new edu.cmu.cs.dennisc.scenegraph.SkeletonVisual();
	    final edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRootCopy;
	    if (sgSkeletonRoot != null)
	    {
	    	sgSkeletonRootCopy = (edu.cmu.cs.dennisc.scenegraph.Joint)sgSkeletonRoot.newCopy();
	    }
	    else
	    {
	    	sgSkeletonRootCopy = null;
	    }
	    
    	rv.skeleton.setValue( sgSkeletonRootCopy );
		rv.geometries.setValue( sgGeometries );
		rv.weightedMeshes.setValue( sgWeightedMeshes );
		rv.textures.setValue( sgTextureAppearances );
		rv.frontFacingAppearance.setValue(sgFrontAppearanceCopy);
		rv.backFacingAppearance.setValue(sgBackAppearanceCopy);
		rv.baseBoundingBox.setValue(bbox);
		rv.isShowing.setValue(sgOriginal.isShowing.getValue());
		rv.scale.setValue(scaleCopy);
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointedModelResource resource, org.lgna.story.resources.JointId jointId ) {
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal = getVisual( resource );
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgJoint = sgSkeletonRoot.getJoint( jointId.toString() );
		return sgJoint.getLocalTransformation().orientation.createUnitQuaternion();
	}
	
	public static String getName(Class<?> modelResource)
	{
		return modelResource.getSimpleName();
	}
	
	private static BufferedImage getThumbnailInternal(Class<?> modelResource, String name)
	{
		URL resourceURL = getThumbnailURLInternal(modelResource, name);
		if (resourceURL != null)
		{
			try
			{
				BufferedImage image = ImageIO.read(resourceURL);
				return image;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			return null;
		}
		return null;
	}
	
	public static BufferedImage getThumbnail(Class<?> modelResource, String instanceName)
	{
		return getThumbnailInternal(modelResource, getName(modelResource)+"_"+instanceName);
	}
	
	public static BufferedImage getThumbnail(Class<?> modelResource)
	{
		return getThumbnailInternal(modelResource, getName(modelResource));
	}
	
	public static java.net.URL getThumbnailURL(Class<?> modelResource)
	{
		return getThumbnailURLInternal(modelResource, getName(modelResource));
	}
	
	public static java.net.URL getThumbnailURL(Class<?> modelResource, String instanceName)
	{
		return getThumbnailURLInternal(modelResource, getName(modelResource)+"_"+instanceName);
	}
	
	public static org.lgna.story.resourceutilities.ModelResourceInfo getModelResourceInfo(Class<?> modelResource) {
		if (modelResource == null)
		{
			return null;
		}
		if( classToInfoMap.containsKey(modelResource) ) {
			return classToInfoMap.get(modelResource);
		}
		else {
			String name = getName(modelResource);
			try {
				InputStream is = getAliceResourceAsStream(modelResource, ModelResourceExporter.getResourceSubDirWithSeparator()+name+".xml");
				if (is != null) {
					Document doc = XMLUtilities.read(is);
					ModelResourceInfo info = new ModelResourceInfo(doc);
					classToInfoMap.put(modelResource, info);
					return info;
				}
				else {
					return null;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public static AxisAlignedBox getBoundingBox(Class<?> modelResource)
	{
		ModelResourceInfo info = getModelResourceInfo(modelResource);
		if (info != null) {
			return info.getBoundingBox();
		}
		return null;
	}
	
	public static String getModelName(Class<?> modelResource)
	{
		ModelResourceInfo info = getModelResourceInfo(modelResource);
		if (info != null) {
			return info.getName();
		}
		return null;
	}
	
	public static String getCreator(Class<?> modelResource)
	{
		ModelResourceInfo info = getModelResourceInfo(modelResource);
		if (info != null) {
			return info.getCreator();
		}
		return null;
	}
	
	public static int getCreationYear(Class<?> modelResource)
	{
		ModelResourceInfo info = getModelResourceInfo(modelResource);
		if (info != null) {
			return info.getCreationYear();
		}
		return -1;
	}
	
	public static String[] getTags(Class<?> modelResource)
	{
		ModelResourceInfo info = getModelResourceInfo(modelResource);
		if (info != null) {
			return info.getTags();
		}
		return null;
	}
	
	
	
	
	
	
}
