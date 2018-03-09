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

package org.lgna.story.implementation.alice;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lgna.story.resources.BasicResource;
import org.lgna.story.resourceutilities.ModelResourceInfo;
import org.lgna.story.resourceutilities.StorytellingResources;
import org.w3c.dom.Document;

import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

/**
 * @author Dennis Cosgrove
 */
public class AliceResourceUtilties {
	public static final String MODEL_RESOURCE_EXTENSION = "a3r";
	public static final String TEXTURE_RESOURCE_EXTENSION = "a3t";

	private static final Map<URL, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual> urlToVisualMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static final Map<URL, TexturedAppearance[]> urlToTextureMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static final Map<String, org.lgna.story.resourceutilities.ModelResourceInfo> classToInfoMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static final Map<ResourceIdentifier, ResourceNames> resourceIdentifierToResourceNamesMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static final class ResourceNames {
		public final String visualName;
		public final String textureName;

		public ResourceNames( String visualName, String textureName ) {
			this.visualName = visualName;
			this.textureName = textureName;
		}
	}

	private static final class ResourceIdentifier {
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

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( "ResourceIdentifier[" );
			sb.append( this.key );
			sb.append( "]" );
			return sb.toString();
		}

	}

	/*private*/protected AliceResourceUtilties() {
		throw new AssertionError();
	}

	private static String findLocalizedText( String bundleName, String key, Locale locale ) {
		if( ( bundleName != null ) && ( key != null ) ) {
			try {
				java.util.ResourceBundle resourceBundle = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( bundleName, locale );
				String rv = resourceBundle.getString( key );
				return rv;
			} catch( java.util.MissingResourceException mre ) {
				//Logger.errln( bundleName, key );
				return null;
			}
		} else {
			return null;
		}
	}

	private static String CLASS_NAME_LOCALIZATION_BUNDLE = BasicResource.class.getPackage().getName() + ".GalleryNames";
	private static String GROUP_TAGS_LOCALIZATION_BUNDLE = BasicResource.class.getPackage().getName() + ".GalleryTags";
	private static String THEME_TAGS_LOCALIZATION_BUNDLE = BasicResource.class.getPackage().getName() + ".GalleryTags";
	private static String TAGS_LOCALIZATION_BUNDLE = BasicResource.class.getPackage().getName() + ".GalleryTags";

	private static String getClassNameLocalizationBundleName() {
		return CLASS_NAME_LOCALIZATION_BUNDLE;
	}

	private static String getGroupTagsLocalizationBundleName() {
		return GROUP_TAGS_LOCALIZATION_BUNDLE;
	}

	private static String getThemeTagsLocalizationBundleName() {
		return THEME_TAGS_LOCALIZATION_BUNDLE;
	}

	private static String getTagsLocalizationBundleName() {
		return TAGS_LOCALIZATION_BUNDLE;
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual decodeVisual( URL url ) {
		try {
			java.io.InputStream is = url.openStream();
			edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
			return decoder.decodeReferenceableBinaryEncodableAndDecodable( new java.util.HashMap<Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable>() );
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static TexturedAppearance[] decodeTexture( URL url ) {
		try {
			java.io.InputStream is = url.openStream();
			edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
			TexturedAppearance[] rv = decoder.decodeReferenceableBinaryEncodableAndDecodableArray( TexturedAppearance.class, new java.util.HashMap<Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable>() );
			for( TexturedAppearance ta : rv ) {
				( (edu.cmu.cs.dennisc.texture.BufferedImageTexture)ta.diffuseColorTexture.getValue() ).directSetMipMappingDesired( false );
			}
			return rv;
		} catch( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	public static void encodeVisual( final SkeletonVisual toSave, File file ) throws IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		if( !file.exists() ) {
			file.createNewFile();
		}
		java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( fos );
		encoder.encode( toSave, new java.util.HashMap<ReferenceableBinaryEncodableAndDecodable, Integer>() );
		encoder.flush();
		fos.close();
	}

	public static void encodeTexture( final TexturedAppearance[] toSave, File file ) throws IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		if( !file.exists() ) {
			file.createNewFile();
		}
		java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( fos );
		encoder.encode( toSave, new java.util.HashMap<ReferenceableBinaryEncodableAndDecodable, Integer>() );
		encoder.flush();
		fos.close();
	}

	public static InputStream getAliceResourceAsStream( Class<?> cls, String resourceString ) {
		return StorytellingResources.INSTANCE.getAliceResourceAsStream( cls.getPackage().getName().replace( ".", "/" ) + "/" + resourceString );
	}

	public static URL getAliceResource( Class<?> cls, String resourceString ) {
		return StorytellingResources.INSTANCE.getAliceResource( cls.getPackage().getName().replace( ".", "/" ) + "/" + resourceString );
	}

	public static String enumToCamelCase( String enumName, boolean startWithLowerCase ) {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < enumName.length(); i++ ) {
			if( i == 0 ) {
				if( startWithLowerCase ) {
					sb.append( Character.toLowerCase( enumName.charAt( i ) ) );
				} else {
					sb.append( Character.toUpperCase( enumName.charAt( i ) ) );
				}
			} else if( enumName.charAt( i - 1 ) == '_' ) {
				sb.append( Character.toUpperCase( enumName.charAt( i ) ) );
			} else if( enumName.charAt( i ) != '_' ) {
				sb.append( Character.toLowerCase( enumName.charAt( i ) ) );
			}
		}
		return sb.toString();
	}

	public static String enumToCamelCase( String enumName ) {
		return enumToCamelCase( enumName, false );
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
		} else {
			return camelCaseToEnum( name );
		}
	}

	public static String makeLocalizationKey( String key ) {
		return key.replace( ' ', '_' );
	}

	public static String arrayToCamelCase( String[] nameArray, int start, int end ) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for( int i = start; i < end; i++ ) {
			if( nameArray[ i ].length() > 0 ) {
				if( isFirst ) {
					sb.append( nameArray[ i ].toLowerCase() );
					isFirst = false;
				} else {
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
				} else {
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
	 * Visual and Texture info is encoded into the enumeration like this: public
	 * enum BaseVisualName { TEXTURE_NAME_1, TEXTURE_NAME_2,
	 * DIFFERENT_VISUAL_NAME_TEXTURE_NAME_1,
	 * DIFFERENT_VISUAL_NAME_TEXTURE_NAME_2 }
	 *
	 * Both 'BaseVisualName' and DIFFERENT_VISUAL_NAME are potentially the names
	 * of visual resources. If the resource uses the base visual, then the enum
	 * name is just the name of the texture (like the entries TEXTURE_NAME_1 and
	 * TEXTURE_NAME_2) If the resource uses a different visual resource, then
	 * the visual resource name is the first half of the enum constant (like the
	 * entries DIFFERENT_VISUAL_NAME_TEXTURE_NAME_1 and
	 * DIFFERENT_VISUAL_NAME_TEXTURE_NAME_2)
	 **/

	private static void findAndStoreResourceNames( Class<?> resourceClass, String resourceName ) {
		String[] splitName = resourceName.split( "_" );
		StringBuilder modelName = new StringBuilder();
		//Set up to try the simple approach first (that the visual is the class name and the texture is the resource name)
		String visualName = AliceResourceClassUtilities.getAliceClassName( resourceClass.getSimpleName() );
		String textureName = resourceName;

		//Check the simple case (visual name is class name and texture name is resource name) and if it fails, iterate through the resource name to find a visual name that resolves to a valid url
		//Use that as the visual name and the remaining name as the texture name
		boolean found = false;
		if( checkVisualAndTextureName( resourceClass, visualName, textureName ) ) {
			found = true;
		}
		//Try using the resourceName as the visual name and assume no specified texture
		else if( checkVisualAndTextureName( resourceClass, enumToCamelCase( resourceName ), "" ) ) {
			visualName = enumToCamelCase( resourceName );
			textureName = "";
			found = true;
		} else {
			for( int i = 0; i < splitName.length; i++ ) {
				if( splitName[ i ].length() > 0 ) {
					if( i != 0 ) {
						modelName.append( "_" );
					}
					modelName.append( splitName[ i ] );
					visualName = enumToCamelCase( modelName.toString() );
					textureName = arrayToEnum( splitName, i + 1, splitName.length );
					if( checkVisualAndTextureName( resourceClass, visualName, textureName ) ) {
						checkVisualAndTextureName( resourceClass, visualName, textureName );
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
		if( resourceIdentifierToResourceNamesMap.get( identifier ) != null ) {
			return resourceIdentifierToResourceNamesMap.get( identifier ).visualName;
		} else {
			Logger.severe( "Failed to find resource names for '" + resourceClass + "' and '" + resourceName + "'" );
			return null;
		}
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
		ResourceNames resourceNames = resourceIdentifierToResourceNamesMap.get( identifier );
		if( resourceNames != null ) {
			return resourceNames.textureName;
		} else {
			Logger.severe( resourceClass, resourceName, identifier );
			return null;
		}
	}

	public static String getTextureResourceFileName( Class<?> resourceClass, String resourceName ) {
		String modelName = getModelNameFromClassAndResource( resourceClass, resourceName );
		String textureName = getTextureNameFromClassAndResource( resourceClass, resourceName );
		return getTextureResourceFileName( modelName, textureName );
	}

	public static String getTextureResourceFileName( Object resource ) {
		return getTextureResourceFileName( resource.getClass(), resource.toString() );
	}

	public static String getVisualResourceFileName( Class<?> resourceClass, String resourceName ) {
		String modelName = getModelNameFromClassAndResource( resourceClass, resourceName );
		return getVisualResourceFileNameFromModelName( modelName );
	}

	public static String getVisualResourceName( Object resource ) {
		return getModelNameFromClassAndResource( resource.getClass(), resource.toString() );
	}

	public static String getTextureResourceName( Object resource ) {
		return getTextureNameFromClassAndResource( resource.getClass(), resource.toString() );
	}

	public static String getVisualResourceFileName( Object resource ) {
		return getVisualResourceFileName( resource.getClass(), resource.toString() );
	}

	public static String getThumbnailResourceFileName( Class<?> resourceClass, String resourceName ) {
		String modelName = getModelNameFromClassAndResource( resourceClass, resourceName );
		String textureName = getTextureNameFromClassAndResource( resourceClass, resourceName );
		if( modelName != null ) {
			return getThumbnailResourceFileName( modelName, textureName );
		} else {
			Logger.severe( resourceClass, resourceName, modelName, textureName );
			return null;
		}
	}

	public static String getThumbnailResourceFileName( Object resource ) {
		return getThumbnailResourceFileName( resource.getClass(), resource.toString() );
	}

	public static String getDefaultTextureEnumName( String resourceName ) {
		return "DEFAULT";
		//		return AliceResourceUtilties.makeEnumName( resourceName );
	}

	private static String createTextureBaseName( String modelName, String textureName ) {
		if( modelName == null ) {
			return null;
		}
		if( textureName == null ) {
			textureName = "_cls";
		} else if( textureName.equalsIgnoreCase( getDefaultTextureEnumName( modelName ) ) || modelName.equalsIgnoreCase( enumToCamelCase( textureName ) ) || textureName.equalsIgnoreCase( AliceResourceUtilties.makeEnumName( modelName ) ) ) {
			textureName = "";
		} else if( textureName.length() > 0 ) {
			textureName = "_" + makeEnumName( textureName );
		}
		return ( modelName != null ? modelName.toLowerCase( java.util.Locale.ENGLISH ) : null ) + textureName;
	}

	public static String getThumbnailResourceFileName( String modelName, String textureName ) {
		return createTextureBaseName( modelName, textureName ) + ".png";
	}

	public static String getTextureResourceFileName( String modelName, String textureName ) {
		return createTextureBaseName( modelName, textureName ) + "." + TEXTURE_RESOURCE_EXTENSION;
	}

	public static String getVisualResourceFileNameFromModelName( String modelName ) {
		return modelName.toLowerCase( java.util.Locale.ENGLISH ) + "." + MODEL_RESOURCE_EXTENSION;
	}

	/*private*/protected static java.net.URL getThumbnailURLInternal( Class<?> modelResource, String resourceName ) {
		String thumbnailName = getThumbnailResourceFileName( modelResource, resourceName );
		return getThumbnailURLInternalFromFilename( modelResource, thumbnailName );
	}

	private static java.net.URL getThumbnailURLInternalFromFilename( Class<?> modelResource, String thumbnailFilename ) {
		return getAliceResource( modelResource, ModelResourceIoUtilities.getResourceSubDirWithSeparator( modelResource.getSimpleName() ) + thumbnailFilename );
	}

	public static URL getTextureURL( Object resource ) {
		return getTextureURL( resource.getClass(), getTextureResourceFileName( resource ) );
	}

	public static URL getTextureURL( Class<?> resourceClass, String visualResourceFileName ) {
		return getAliceResource( resourceClass, ModelResourceIoUtilities.getResourceSubDirWithSeparator( resourceClass.getSimpleName() ) + visualResourceFileName );
	}

	public static URL getVisualURL( Object resource ) {
		return getVisualURL( resource.getClass(), getVisualResourceFileName( resource ) );
	}

	public static URL getVisualURL( Class<?> resourceClass, String visualResourceFileName ) {
		return getAliceResource( resourceClass, ModelResourceIoUtilities.getResourceSubDirWithSeparator( resourceClass.getSimpleName() ) + visualResourceFileName );
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getVisual( Object resource ) {
		URL resourceURL = getVisualURL( resource );
		if( urlToVisualMap.containsKey( resourceURL ) ) {
			return urlToVisualMap.get( resourceURL );
		} else {
			edu.cmu.cs.dennisc.scenegraph.SkeletonVisual visual = decodeVisual( resourceURL );
			java.util.List<edu.cmu.cs.dennisc.scenegraph.qa.Problem> problems = edu.cmu.cs.dennisc.scenegraph.qa.QualityAssuranceUtilities.inspect( visual );
			if( problems.size() > 0 ) {
				Logger.errln( resourceURL );
				for( edu.cmu.cs.dennisc.scenegraph.qa.Problem problem : problems ) {
					Logger.errln( problem );
				}
			}
			urlToVisualMap.put( resourceURL, visual );
			return visual;
		}
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getVisualCopy( Object resource ) {
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual original = getVisual( resource );
		return createCopy( original );
	}

	public static TexturedAppearance[] getTexturedAppearances( Object resource ) {
		URL resourceURL = getTextureURL( resource );
		if( urlToTextureMap.containsKey( resourceURL ) ) {
			return urlToTextureMap.get( resourceURL );
		} else {
			TexturedAppearance[] texture = decodeTexture( resourceURL );
			urlToTextureMap.put( resourceURL, texture );
			return texture;
		}
	}

	public static edu.cmu.cs.dennisc.scenegraph.SkeletonVisual createCopy( edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgOriginal ) {
		edu.cmu.cs.dennisc.scenegraph.Geometry[] sgGeometries = sgOriginal.geometries.getValue();
		edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] sgTextureAppearances = sgOriginal.textures.getValue();
		edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] sgWeightedMeshes = sgOriginal.weightedMeshes.getValue();
		edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] sgDefaultPoseWeightedMeshes = sgOriginal.defaultPoseWeightedMeshes.getValue();
		boolean hasDefaultPoseWeightedMeshes = sgOriginal.hasDefaultPoseWeightedMeshes.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = sgOriginal.baseBoundingBox.getValue();
		edu.cmu.cs.dennisc.math.Matrix3x3 scaleCopy = new edu.cmu.cs.dennisc.math.Matrix3x3( sgOriginal.scale.getValue() );
		edu.cmu.cs.dennisc.scenegraph.Appearance sgFrontAppearanceCopy;
		if( sgOriginal.frontFacingAppearance.getValue() != null ) {
			sgFrontAppearanceCopy = (edu.cmu.cs.dennisc.scenegraph.Appearance)sgOriginal.frontFacingAppearance.getValue().newCopy();
		} else {
			sgFrontAppearanceCopy = null;
		}
		edu.cmu.cs.dennisc.scenegraph.Appearance sgBackAppearanceCopy;
		if( sgOriginal.backFacingAppearance.getValue() != null ) {
			sgBackAppearanceCopy = (edu.cmu.cs.dennisc.scenegraph.Appearance)sgOriginal.backFacingAppearance.getValue().newCopy();
		} else {
			sgBackAppearanceCopy = null;
		}

		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual rv = new edu.cmu.cs.dennisc.scenegraph.SkeletonVisual();
		final edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRootCopy;
		if( sgSkeletonRoot != null ) {
			sgSkeletonRootCopy = (edu.cmu.cs.dennisc.scenegraph.Joint)sgSkeletonRoot.newCopy();
		} else {
			sgSkeletonRootCopy = null;
		}

		rv.skeleton.setValue( sgSkeletonRootCopy );
		rv.geometries.setValue( sgGeometries );
		rv.weightedMeshes.setValue( sgWeightedMeshes );
		rv.defaultPoseWeightedMeshes.setValue( sgDefaultPoseWeightedMeshes );
		rv.hasDefaultPoseWeightedMeshes.setValue( hasDefaultPoseWeightedMeshes );
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
		if( sgNewSkeletonRoot != null ) {
			sgNewSkeleton = (edu.cmu.cs.dennisc.scenegraph.Joint)sgNewSkeletonRoot.newCopy();
		} else {
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

	public static String getName( Class<?> modelResource ) {
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

	/*private*/protected static BufferedImage getThumbnailInternal( Class<?> modelResource, String resourceName ) {
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

	public static BufferedImage getThumbnail( Class<?> modelResource, String instanceName ) {
		return getThumbnailInternal( modelResource, instanceName );
	}

	public static BufferedImage getThumbnail( Class<?> modelResource ) {
		return getThumbnailInternal( modelResource, null );
	}

	public static java.net.URL getThumbnailURL( Class<?> modelResource, String instanceName ) {
		return getThumbnailURLInternal( modelResource, instanceName );
	}

	/*private*/protected static String getKey( Class<?> modelResource, String resourceName ) {
		if( resourceName != null ) {
			return modelResource.getName() + resourceName;
		}
		return modelResource.getName();
	}

	public static org.lgna.story.resourceutilities.ModelResourceInfo getModelResourceInfo( Class<?> modelResource, String resourceName ) {
		if( modelResource == null ) {
			return null;
		}
		String key = getKey( modelResource, resourceName );
		//Return the info if we have it cached
		if( classToInfoMap.containsKey( key ) ) {
			return classToInfoMap.get( key );
		} else {
			String parentKey = getKey( modelResource, null );
			//If we don't have the parent info cached, load it from disk
			ModelResourceInfo parentInfo = null;
			if( !classToInfoMap.containsKey( parentKey ) ) {
				String name = getName( modelResource );
				try {
					//xml files are not referenced off the
					InputStream is = getAliceResourceAsStream( modelResource, ModelResourceIoUtilities.getResourceSubDirWithSeparator( "" ) + name + ".xml" );
					if( is != null ) {
						Document doc = XMLUtilities.read( is );
						parentInfo = new ModelResourceInfo( doc );
						classToInfoMap.put( parentKey, parentInfo );
					} else {
						//This is an acceptable case because classes like Biped don't have class infos
						classToInfoMap.put( parentKey, null );
					}
				} catch( Exception e ) {
					Logger.severe( "Failed to parse class info for " + name + ": " + e );
					classToInfoMap.put( parentKey, null );
				}
			} else {
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

	public static AxisAlignedBox getBoundingBox( Class<?> modelResource, String resourceName ) {
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			return info.getBoundingBox();
		}
		//TODO: implement better solution for getting bounding boxes for general resources (like Swimmer, Flyer, etc.)
		if( modelResource != null ) {
			if( org.lgna.story.resources.SwimmerResource.class.isAssignableFrom( modelResource ) ) {
				return new AxisAlignedBox( new Point3( -.5, -.5, -.5 ), new Point3( .5, .5, .5 ) );
			}
		}
		return new AxisAlignedBox( new Point3( -.5, 0, -.5 ), new Point3( .5, 1, .5 ) );
	}

	public static AxisAlignedBox getBoundingBox( Class<?> modelResource ) {
		return getBoundingBox( modelResource, null );
	}

	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getDefaultInitialTransform( Class<?> modelResource ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
		AxisAlignedBox bbox = getBoundingBox( modelResource );
		if( ( bbox != null ) && !bbox.isNaN() ) {
			boolean placeOnGround = getPlaceOnGround( modelResource );
			if( placeOnGround ) {
				rv.translation.y = -bbox.getYMinimum();
			}
		}
		return rv;
	}

	public static boolean getPlaceOnGround( Class<?> modelResource, String resourceName ) {
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			return info.getPlaceOnGround();
		}
		//TODO: implement better solution for getting placeOnGround for general resources (like Swimmer, Flyer, etc.)
		if( modelResource != null ) {
			if( org.lgna.story.resources.SwimmerResource.class.isAssignableFrom( modelResource ) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean getPlaceOnGround( Class<?> modelResource ) {
		return getPlaceOnGround( modelResource, null );
	}

	/*private*/protected static String getModelName( Class<?> modelResource, String resourceName, Locale locale ) {
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			if( locale == null ) {
				return info.getModelName();
			} else {
				String packageName = modelResource.getPackage().getName();
				return findLocalizedText( getClassNameLocalizationBundleName(), packageName + "." + info.getModelName(), locale );
			}
		}
		return null;
	}

	public static String getModelClassName( Class<?> modelResource, String resourceName, Locale locale ) {
		ModelResourceInfo info = getModelResourceInfo( modelResource, null );
		String className;
		String packageName = modelResource.getPackage().getName();
		if( info != null ) {
			className = info.getModelName();
		} else {
			className = modelResource.getSimpleName().replace( "Resource", "" );
		}

		if( locale == null ) {
			return className;
		} else {
			String localizedText = findLocalizedText( getClassNameLocalizationBundleName(), packageName + "." + className, locale );
			if( localizedText != null ) {
				//pass
			} else {
				localizedText = className;
			}
			return localizedText;
		}
	}

	private static String[] getLocalizedTags( String[] tags, String localizerBundleName, Locale locale, boolean acceptNull ) {
		//		if( Locale.ENGLISH.getLanguage().equals( locale.getLanguage() ) )
		{
			java.util.List<String> localizedTags = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
			for( String tag : tags ) {
				String[] splitTags = tag.split( ":" );
				StringBuilder finalTag = new StringBuilder();
				for( int i = 0; i < splitTags.length; i++ ) {
					String t = splitTags[ i ];
					boolean hasStar = t.startsWith( "*" );
					String stringToUse;
					if( hasStar ) {
						stringToUse = t.substring( 1 );
					} else {
						stringToUse = t;
					}
					String localizationKey = makeLocalizationKey( stringToUse );
					String localizedTag = findLocalizedText( localizerBundleName, localizationKey, locale );
					if( acceptNull && ( localizedTag == null ) ) {
						localizedTag = stringToUse;
					}
					if( localizedTag != null ) {
						if( i > 0 ) {
							finalTag.append( ":" );
						}
						if( hasStar ) {
							finalTag.append( "*" );
						}
						finalTag.append( localizedTag );
					}
				}
				if( finalTag.length() > 0 ) {
					localizedTags.add( finalTag.toString() );
				}
			}
			return localizedTags.toArray( new String[ localizedTags.size() ] );
		}
		//		else {
		//			return new String[ 0 ];
		//		}
	}

	public static String[] getTags( Class<?> modelResource, String resourceName, Locale locale ) {
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			if( locale == null ) {
				return info.getTags();
			} else {
				boolean acceptNull = locale.getLanguage().equals( "en" );
				return getLocalizedTags( info.getTags(), getTagsLocalizationBundleName(), locale, acceptNull );
			}
		} else {
			return null;
		}
	}

	public static String[] getGroupTags( Class<?> modelResource, String resourceName, Locale locale ) {
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			if( ( locale == null ) || true ) {
				return info.getGroupTags();
			} else {
				return getLocalizedTags( info.getGroupTags(), getGroupTagsLocalizationBundleName(), locale, true );
			}
		} else {
			return null;
		}
	}

	public static String getLocalizedTag( String tag, Locale locale ) {
		if( locale == null ) {
			return tag;
		}
		if( tag.contains( " " ) ) {
			tag = tag.replace( " ", "_" );
		}
		String result = findLocalizedText( getTagsLocalizationBundleName(), tag, locale );
		if( result != null ) {
			return result;
		} else {
			Logger.severe( "No localization for gallery tag '" + tag + "' for locale " + locale );
			return tag;
		}
	}

	public static String[] getThemeTags( Class<?> modelResource, String resourceName, Locale locale ) {
		ModelResourceInfo info = getModelResourceInfo( modelResource, resourceName );
		if( info != null ) {
			if( ( locale == null ) || true ) {
				return info.getThemeTags();
			} else {
				return getLocalizedTags( info.getThemeTags(), getThemeTagsLocalizationBundleName(), locale, true );
			}
		} else {
			return null;
		}
	}
}
