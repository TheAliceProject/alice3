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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lgna.story.resourceutilities.ModelResourceExporter;
import org.lgna.story.resourceutilities.ModelResourceInfo;
import org.lgna.story.resourceutilities.StorytellingResources;
import org.w3c.dom.Document;

import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
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
	private static Map<String, org.lgna.story.resourceutilities.ModelResourceInfo> classToInfoMap = new HashMap<String, org.lgna.story.resourceutilities.ModelResourceInfo>();

	private static Map<ResourceIdentifier, ResourceNames> resourceIdentifierToResourceNamesMap = new HashMap<ResourceIdentifier, ResourceNames>();

	private static class ResourceNames {
		public final String visualName;
		public final String textureName;

		public ResourceNames( String visualName, String textureName ) {
			this.visualName = visualName;
			this.textureName = textureName;
		}
	}

	private static class ResourceIdentifier {
		public final Class<?> resourceClass;
		public final String resourceName;

		private final String key;

		public ResourceIdentifier( Class<?> resourceClass, String resourceName ) {
			this.resourceClass = resourceClass;
			this.resourceName = resourceName;
			this.key = this.resourceClass.getName() + this.resourceName;
		}

		@Override
		public boolean equals( Object arg0 ) {
			if( ( arg0 != null ) && ( arg0 instanceof ResourceIdentifier ) ) {
				return this.key.equals( ( (ResourceIdentifier)arg0 ).key );
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.key.hashCode();
		}

	}

	private AliceResourceUtilties() {
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual decodeVisual( URL url ) {
		try
		{
			java.io.InputStream is = url.openStream();
			edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
			return decoder.decodeReferenceableBinaryEncodableAndDecodable( new java.util.HashMap<Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable>() );
		} catch( Exception e )
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
			return decoder.decodeReferenceableBinaryEncodableAndDecodableArray( TexturedAppearance.class, new java.util.HashMap<Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable>() );
		} catch( Exception e )
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void encodeVisual( final SkeletonVisual toSave, File file ) throws IOException
	{
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		if( !file.exists() )
		{
			file.createNewFile();
		}
		java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( fos );
		encoder.encode( toSave, new java.util.HashMap<ReferenceableBinaryEncodableAndDecodable, Integer>() );
		encoder.flush();
		fos.close();
	}

	public static void encodeTexture( final TexturedAppearance[] toSave, File file ) throws IOException
	{
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		if( !file.exists() )
		{
			file.createNewFile();
		}
		java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( fos );
		encoder.encode( toSave, new java.util.HashMap<ReferenceableBinaryEncodableAndDecodable, Integer>() );
		encoder.flush();
		fos.close();

	}

	public static InputStream getAliceResourceAsStream( Class<?> cls, String resourceString ) {
		return StorytellingResources.getInstance().getAliceResourceAsStream( cls.getPackage().getName().replace( ".", "/" ) + "/" + resourceString );
	}

	public static URL getAliceResource( Class<?> cls, String resourceString ) {
		return StorytellingResources.getInstance().getAliceResource( cls.getPackage().getName().replace( ".", "/" ) + "/" + resourceString );
	}

	public static String enumToCamelCase( String enumName ) {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < enumName.length(); i++ ) {
			if( ( i == 0 ) || ( enumName.charAt( i - 1 ) == '_' ) ) {
				sb.append( Character.toUpperCase( enumName.charAt( i ) ) );
			}
			else if( enumName.charAt( i ) != '_' ) {
				sb.append( Character.toLowerCase( enumName.charAt( i ) ) );
			}
		}
		return sb.toString();
	}

	public static String camelCaseToEnum( String name ) {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < name.length(); i++ ) {
			if( ( i != 0 ) && Character.isUpperCase( name.charAt( i ) ) ) {
				sb.append( '_' );
			}
			sb.append( Character.toUpperCase( name.charAt( i ) ) );
		}
		return sb.toString();
	}

	public static boolean isEnumName( String name ) {
		for( int i = 0; i < name.length(); i++ ) {
			char c = name.charAt( i );
			if( ( c != '_' ) && !( Character.isUpperCase( c ) || Character.isDigit( c ) ) ) {
				return false;
			}
		}
		return true;
	}

	public static String makeEnumName( String name ) {
		if( isEnumName( name ) ) {
			return name;
		}
		if( name.contains( "_" ) ) {
			return name.toUpperCase();
		}
		else {
			return camelCaseToEnum( name );
		}
	}

	public static String arrayToCamelCase( String[] nameArray, int start, int end ) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for( int i = start; i < end; i++ ) {
			if( nameArray[ i ].length() > 0 ) {
				if( isFirst ) {
					sb.append( nameArray[ i ].toLowerCase() );
					isFirst = false;
				}
				else {
					sb.append( nameArray[ i ].substring( 0, 1 ).toUpperCase() + nameArray[ i ].substring( 1 ).toLowerCase() );
				}
			}
		}
		return sb.toString();
	}

	public static String arrayToEnum( String[] nameArray, int start, int end ) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for( int i = start; i < end; i++ ) {
			if( nameArray[ i ].length() > 0 ) {
				if( isFirst ) {
					isFirst = false;
				}
				else {
					sb.append( "_" );
				}
				sb.append( nameArray[ i ].toUpperCase() );
			}
		}
		return sb.toString();
	}

	//Check to see if a thumbnail exists for the given visual name and texture name
	private static boolean checkVisualAndTextureName( Class<?> resourceClass, String visualName, String textureName ) {
		String thubmnailFileName = getThumbnailResourceFileName( visualName, textureName );
		return getThumbnailURLInternalFromFilename( resourceClass, thubmnailFileName ) != null;
	}

	/**
	 * Visual and Texture info is encoded into the enumeration like this:
	 * public enum BaseVisualName {
	 * TEXTURE_NAME_1,
	 * TEXTURE_NAME_2,
	 * DIFFERENT_VISUAL_NAME_TEXTURE_NAME_1,
	 * DIFFERENT_VISUAL_NAME_TEXTURE_NAME_2
	 * }
	 * 
	 * Both 'BaseVisualName' and DIFFERENT_VISUAL_NAME are potentially the names of visual resources.
	 * If the resource uses the base visual, then the enum name is just the name of the texture
	 * (like the entries TEXTURE_NAME_1 and TEXTURE_NAME_2)
	 * If the resource uses a different visual resource, then the visual resource name is the first half of the enum constant
	 * (like the entries DIFFERENT_VISUAL_NAME_TEXTURE_NAME_1 and DIFFERENT_VISUAL_NAME_TEXTURE_NAME_2)
	 **/

	private static void findAndStoreResourceNames( Class<?> resourceClass, String resourceName ) {
		String[] splitName = resourceName.split( "_" );
		StringBuilder modelName = new StringBuilder();
		//Set up to try the simple approach first (that the visual is the class name and the texture is the resource name)
		String visualName = AliceResourceClassUtilities.getAliceClassName( resourceClass.getSimpleName() );
		String textureName = resourceName;

		//Check the simple case and if it fails, iterate through the resource name to find a visual name that resolves to a valid url
		//Use that as the visual name and the remaining name as the texture name
		boolean found = false;
		if( checkVisualAndTextureName( resourceClass, visualName, textureName ) ) {
			found = true;
		}
		else {
			for( int i = 0; i < splitName.length; i++ ) {
				if( splitName[ i ].length() > 0 ) {
					if( i != 0 ) {
						modelName.append( "_" );
					}
					modelName.append( splitName[ i ] );
					visualName = enumToCamelCase( modelName.toString() );
					textureName = arrayToEnum( splitName, i + 1, splitName.length );
					if( checkVisualAndTextureName( resourceClass, visualName, textureName ) ) {
						found = true;
						break;
					}
				}
			}
		}
		if( !found ) {
			Logger.severe( "Failed to find resource names for '" + resourceClass + "' and '" + resourceName + "'" );
			modelName = new StringBuilder();
			for( int i = 0; i < splitName.length; i++ ) {
				if( splitName[ i ].length() > 0 ) {
					if( i != 0 ) {
						modelName.append( "_" );
					}
					modelName.append( splitName[ i ] );
					visualName = enumToCamelCase( modelName.toString() );
					textureName = arrayToEnum( splitName, i + 1, splitName.length );
					if( checkVisualAndTextureName( resourceClass, visualName, textureName ) ) {
						found = true;
						break;
					}
				}
			}
			return;
		}
		ResourceIdentifier identifier = new ResourceIdentifier( resourceClass, resourceName );
		resourceIdentifierToResourceNamesMap.put( identifier, new ResourceNames( visualName, textureName ) );
	}

	public static String getModelNameFromClassAndResource( Class<?> resourceClass, String resourceName ) {
		//If we're just using the class as a lookup, return the class name directly
		if( resourceName == null ) {
			return AliceResourceClassUtilities.getAliceClassName( resourceClass );
		}
		ResourceIdentifier identifier = new ResourceIdentifier( resourceClass, resourceName );
		if( !resourceIdentifierToResourceNamesMap.containsKey( identifier ) ) {
			findAndStoreResourceNames( resourceClass, resourceName );
		}
		return resourceIdentifierToResourceNamesMap.get( identifier ).visualName;
	}

	public static String getTextureNameFromClassAndResource( Class<?> resourceClass, String resourceName ) {
		//If we're just using the class as a lookup, return null since the class name only distinguishes visuals
		if( resourceName == null ) {
			return null;
		}
		ResourceIdentifier identifier = new ResourceIdentifier( resourceClass, resourceName );
		if( !resourceIdentifierToResourceNamesMap.containsKey( identifier ) ) {
			findAndStoreResourceNames( resourceClass, resourceName );
		}
		return resourceIdentifierToResourceNamesMap.get( identifier ).textureName;
	}

	public static String getTextureResourceFileName( Class<?> resourceClass, String resourceName ) {
		String modelName = getModelNameFromClassAndResource( resourceClass, resourceName );
		String textureName = getTextureNameFromClassAndResource( resourceClass, resourceName );
		return getTextureResourceFileName( modelName, textureName );
	}

	public static String getTextureResourceFileName( Object resource )
	{
		return getTextureResourceFileName( resource.getClass(), resource.toString() );
	}

	public static String getVisualResourceFileName( Class<?> resourceClass, String resourceName )
	{
		String modelName = getModelNameFromClassAndResource( resourceClass, resourceName );
		return getVisualResourceFileNameFromModelName( modelName );
	}

	public static String getVisualResourceName( Object resource ) {
		return getModelNameFromClassAndResource( resource.getClass(), resource.toString() );
	}

	public static String getTextureResourceName( Object resource ) {
		return getTextureNameFromClassAndResource( resource.getClass(), resource.toString() );
	}

	public static String getVisualResourceFileName( Object resource )
	{
		return getVisualResourceFileName( resource.getClass(), resource.toString() );
	}

	public static String getThumbnailResourceFileName( Class<?> resourceClass, String resourceName ) {
		String modelName = getModelNameFromClassAndResource( resourceClass, resourceName );
		String textureName = getTextureNameFromClassAndResource( resourceClass, resourceName );
		return getThumbnailResourceFileName( modelName, textureName );
	}

	public static String getThumbnailResourceFileName( Object resource )
	{
		return getThumbnailResourceFileName( resource.getClass(), resource.toString() );
	}

	private static String createTextureBaseName( String modelName, String textureName ) {
		if( textureName == null ) {
			textureName = "_cls";
		}
		else if( modelName.equalsIgnoreCase( enumToCamelCase( textureName ) ) ) {
			textureName = "";
		}
		else if( textureName.length() > 0 ) {
			textureName = "_" + makeEnumName( textureName );
		}
		return modelName.toLowerCase() + textureName;
	}

	public static String getThumbnailResourceFileName( String modelName, String textureName ) {
		return createTextureBaseName( modelName, textureName ) + ".png";
	}

	public static String getTextureResourceFileName( String modelName, String textureName )
	{
		return createTextureBaseName( modelName, textureName ) + "." + TEXTURE_RESOURCE_EXTENSION;
	}

	public static String getVisualResourceFileNameFromModelName( String modelName )
	{
		return modelName.toLowerCase() + "." + MODEL_RESOURCE_EXTENSION;
	}

	private static java.net.URL getThumbnailURLInternal( Class<?> modelResource, String resourceName ) {
		String thumbnailName = getThumbnailResourceFileName( modelResource, resourceName );
		return getThumbnailURLInternalFromFilename( modelResource, thumbnailName );
	}

	private static java.net.URL getThumbnailURLInternalFromFilename( Class<?> modelResource, String thumbnailFilename ) {
		return getAliceResource( modelResource, ModelResourceExporter.getResourceSubDirWithSeparator( modelResource.getSimpleName() ) + thumbnailFilename );
	}

	public static URL getTextureURL( Object resource )
	{
		return getTextureURL( resource.getClass(), getTextureResourceFileName( resource ) );
	}

	public static URL getTextureURL( Class<?> resourceClass, String visualResourceFileName )
	{
		return getAliceResource( resourceClass, ModelResourceExporter.getResourceSubDirWithSeparator( resourceClass.getSimpleName() ) + visualResourceFileName );
	}

	public static URL getVisualURL( Object resource )
	{
		return getVisualURL( resource.getClass(), getVisualResourceFileName( resource ) );
	}

	public static URL getVisualURL( Class<?> resourceClass, String visualResourceFileName )
	{
		return getAliceResource( resourceClass, ModelResourceExporter.getResourceSubDirWithSeparator( resourceClass.getSimpleName() ) + visualResourceFileName );
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getVisual( Object resource )
	{
		URL resourceURL = getVisualURL( resource );
		if( urlToVisualMap.containsKey( resourceURL ) )
		{
			return urlToVisualMap.get( resourceURL );
		}
		else
		{
			edu.cmu.cs.dennisc.scenegraph.SkeletonVisual visual = decodeVisual( resourceURL );
			urlToVisualMap.put( resourceURL, visual );
			return visual;
		}
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getVisualCopy( Object resource )
	{
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual original = getVisual( resource );
		return createCopy( original );
	}

	public static TexturedAppearance[] getTexturedAppearances( Object resource )
	{
		URL resourceURL = getTextureURL( resource );
		if( urlToTextureMap.containsKey( resourceURL ) )
		{
			return urlToTextureMap.get( resourceURL );
		}
		else
		{
			TexturedAppearance[] texture = decodeTexture( resourceURL );
			urlToTextureMap.put( resourceURL, texture );
			return texture;
		}
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual createCopy( edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal ) {
		edu.cmu.cs.dennisc.scenegraph.Geometry[] sgGeometries = sgOriginal.geometries.getValue();
		edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] sgTextureAppearances = sgOriginal.textures.getValue();
		edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] sgWeightedMeshes = sgOriginal.weightedMeshes.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = sgOriginal.baseBoundingBox.getValue();
		edu.cmu.cs.dennisc.math.Matrix3x3 scaleCopy = new edu.cmu.cs.dennisc.math.Matrix3x3( sgOriginal.scale.getValue() );
		edu.cmu.cs.dennisc.scenegraph.Appearance sgFrontAppearanceCopy;
		if( sgOriginal.frontFacingAppearance.getValue() != null ) {
			sgFrontAppearanceCopy = (edu.cmu.cs.dennisc.scenegraph.Appearance)sgOriginal.frontFacingAppearance.getValue().newCopy();
		}
		else {
			sgFrontAppearanceCopy = null;
		}
		edu.cmu.cs.dennisc.scenegraph.Appearance sgBackAppearanceCopy;
		if( sgOriginal.backFacingAppearance.getValue() != null ) {
			sgBackAppearanceCopy = (edu.cmu.cs.dennisc.scenegraph.Appearance)sgOriginal.backFacingAppearance.getValue().newCopy();
		}
		else {
			sgBackAppearanceCopy = null;
		}

		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual rv = new edu.cmu.cs.dennisc.scenegraph.SkeletonVisual();
		final edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRootCopy;
		if( sgSkeletonRoot != null )
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
		rv.frontFacingAppearance.setValue( sgFrontAppearanceCopy );
		rv.backFacingAppearance.setValue( sgBackAppearanceCopy );
		rv.baseBoundingBox.setValue( bbox );
		rv.isShowing.setValue( sgOriginal.isShowing.getValue() );
		rv.scale.setValue( scaleCopy );
		return rv;
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual createReplaceVisualElements( edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal, Object resource ) {
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgToReplaceWith = getVisual( resource );
		edu.cmu.cs.dennisc.scenegraph.Geometry[] sgGeometries = sgToReplaceWith.geometries.getValue();
		edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] sgWeightedMeshes = sgToReplaceWith.weightedMeshes.getValue();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = sgToReplaceWith.baseBoundingBox.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgNewSkeletonRoot = sgToReplaceWith.skeleton.getValue();
		final edu.cmu.cs.dennisc.scenegraph.Joint sgNewSkeleton;
		if( sgNewSkeletonRoot != null )
		{
			sgNewSkeleton = (edu.cmu.cs.dennisc.scenegraph.Joint)sgNewSkeletonRoot.newCopy();
		}
		else
		{
			sgNewSkeleton = null;
		}
		if( sgNewSkeleton != null ) {
			sgNewSkeleton.setParent( sgOriginal.getParent() );
		}

		//	    if (sgOriginal.skeleton.getValue() != null) {
		//	    	sgOriginal.skeleton.getValue().setParent(null);
		//	    }
		sgOriginal.skeleton.setValue( sgNewSkeleton );

		sgOriginal.geometries.setValue( sgGeometries );
		sgOriginal.weightedMeshes.setValue( sgWeightedMeshes );

		sgOriginal.baseBoundingBox.setValue( bbox );
		return sgOriginal;
	}

	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalJointTransformation( org.lgna.story.resources.JointedModelResource resource, org.lgna.story.resources.JointId jointId ) {
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal = getVisual( resource );
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgJoint = sgSkeletonRoot.getJoint( jointId.toString() );
		return sgJoint.getLocalTransformation();
	}

	public static edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointedModelResource resource, org.lgna.story.resources.JointId jointId ) {
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal = getVisual( resource );
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgJoint = sgSkeletonRoot.getJoint( jointId.toString() );
		return sgJoint.getLocalTransformation().orientation.createUnitQuaternion();
	}

	public static String getName( Class<?> modelResource )
	{
		return AliceResourceClassUtilities.getAliceClassName( modelResource );
	}

	public static String trimName( String name ) {
		name = name.trim();
		while( name.contains( "__" ) ) {
			name = name.replace( "__", "_" );
		}
		while( name.startsWith( "_" ) ) {
			name = name.substring( 1 );
		}
		while( name.endsWith( "_" ) ) {
			name = name.substring( 0, name.length() - 1 );
		}
		return name;
	}

	private static BufferedImage getThumbnailInternal( Class<?> modelResource, String resourceName )
	{
		URL resourceURL = getThumbnailURLInternal( modelResource, resourceName );
		if( resourceURL == null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "Cannot load thumbnail for", modelResource, resourceName );
			resourceURL = getThumbnailURLInternal( modelResource, resourceName );
		}
		if( resourceURL != null ) {
			try {
				return ImageIO.read( resourceURL );
			} catch( Throwable t ) {
				t.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public static BufferedImage getThumbnail( Class<?> modelResource, String instanceName )
	{
		return getThumbnailInternal( modelResource, instanceName );
	}

	public static BufferedImage getThumbnail( Class<?> modelResource )
	{
		return getThumbnailInternal( modelResource, null );
	}

	public static java.net.URL getThumbnailURL( Class<?> modelResource )
	{
		return getThumbnailURLInternal( modelResource, null );
	}

	public static java.net.URL getThumbnailURL( Class<?> modelResource, String instanceName )
	{
		return getThumbnailURLInternal( modelResource, instanceName );
	}

	private static String getKey( Class<?> modelResource, String resourceName ) {
		if( resourceName != null ) {
			return modelResource.getName() + resourceName;
		}
		return modelResource.getName();
	}

	public static org.lgna.story.resourceutilities.ModelResourceInfo getModelResourceInfo( Class<?> modelResource, String resourceName ) {
		if( modelResource == null )
		{
			return null;
		}
		String key = getKey( modelResource, resourceName );
		//Return the info if we have it cached
		if( classToInfoMap.containsKey( key ) ) {
			return classToInfoMap.get( key );
		}
		else {
			String parentKey = getKey( modelResource, null );
			//If we don't have the parent info cached, load it from disk
			ModelResourceInfo parentInfo = null;
			if( !classToInfoMap.containsKey( parentKey ) ) {
				String name = getName( modelResource );
				try {
					//xml files are not referenced off the 
					InputStream is = getAliceResourceAsStream( modelResource, ModelResourceExporter.getResourceSubDirWithSeparator( "" ) + name + ".xml" );
					if( is != null ) {
						Document doc = XMLUtilities.read( is );
						parentInfo = new ModelResourceInfo( doc );
						classToInfoMap.put( parentKey, parentInfo );
					}
					else {
						Logger.severe( "Failed to find class info for " + name );
						classToInfoMap.put( parentKey, null );
					}
				} catch( Exception e )
				{
					Logger.severe( "Failed to parse class info for " + name + ": " + e );
					classToInfoMap.put( parentKey, null );
				}
			}
			else {
				parentInfo = classToInfoMap.get( parentKey );
			}
			if( parentInfo != null ) {
				//If the key we're looking for is the same as the parent key, then just return the parent info
				if( parentKey.equals( key ) ) {
					return parentInfo;
				}
				//Otherwise get the model and texture names and find the sub resource we're looking for
				ModelResourceInfo subResource = parentInfo.getSubResource( resourceName );
				if( subResource == null ) {
					Logger.severe( "Failed to find a resource for " + modelResource + " : " + resourceName );
				}
				//Cache the sub resource under the original main key
				classToInfoMap.put( key, subResource );
				return subResource;
			}
			return null;
		}
	}

	public static org.lgna.story.resourceutilities.ModelResourceInfo getModelResourceInfo( Class<?> modelResource ) {
		return getModelResourceInfo( modelResource, null );
	}

	public static AxisAlignedBox getBoundingBox( Class<?> modelResource, String resourceName )
	{
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			return info.getBoundingBox();
		}
		return null;
	}

	public static AxisAlignedBox getBoundingBox( Class<?> modelResource )
	{
		return getBoundingBox( modelResource, null );
	}

	public static String getModelName( Class<?> modelResource, String resourceName )
	{
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			return info.getModelName();
		}
		return null;
	}

	public static String getModelName( Class<?> modelResource )
	{
		return getModelName( modelResource, null );
	}

	public static String getJavaCode( Class<?> modelResource ) {
		String name = getName( modelResource );
		InputStream is = null;
		try {
			is = getAliceResourceAsStream( modelResource, name + ".java" );
			if( is != null ) {
				String javaCode = TextFileUtilities.read( is );
				return javaCode;
			}
			else {
				Logger.severe( "Failed to find java file for " + name );
			}
		} catch( Exception e )
		{
			Logger.severe( "Failed to find java file for " + name );
		} finally {
			if( is != null ) {
				try {
					is.close();
				} catch( IOException ioe ) {

				}
			}
		}
		return null;
	}

	public static String getCreator( Class<?> modelResource, String resourceName )
	{
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			return info.getCreator();
		}
		return null;
	}

	public static String getCreator( Class<?> modelResource )
	{
		return getCreator( modelResource, null );
	}

	public static int getCreationYear( Class<?> modelResource, String resourceName )
	{
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			return info.getCreationYear();
		}
		return -1;
	}

	public static int getCreationYear( Class<?> modelResource )
	{
		return getCreationYear( modelResource, null );
	}

	public static String[] getTags( Class<?> modelResource, String resourceName )
	{
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			return info.getTags();
		}
		return null;
	}

	public static String[] getTags( Class<?> modelResource )
	{
		return getTags( modelResource, null );
	}

}
