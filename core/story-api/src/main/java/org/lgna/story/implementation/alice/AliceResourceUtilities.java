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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder;
import edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.scenegraph.Appearance;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;
import edu.cmu.cs.dennisc.scenegraph.qa.Problem;
import edu.cmu.cs.dennisc.scenegraph.qa.QualityAssuranceUtilities;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.Texture;
import org.lgna.story.resources.*;
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
public class AliceResourceUtilities {
  public static final String MODEL_RESOURCE_EXTENSION = "a3r";
  public static final String TEXTURE_RESOURCE_EXTENSION = "a3t";

  private static final Map<URL, SkeletonVisual> urlToVisualMap = Maps.newHashMap();
  private static final Map<URL, TexturedAppearance[]> urlToTextureMap = Maps.newHashMap();
  private static final Map<String, ModelResourceInfo> classToInfoMap = Maps.newHashMap();
  private static final Map<String, ResourceNames> resourceIdentifierToResourceNamesMap = Maps.newHashMap();

  private static final class ResourceNames {
    public final String visualName;
    public final String textureName;

    public ResourceNames(String visualName, String textureName) {
      this.visualName = visualName;
      this.textureName = textureName;
    }
  }

  /*private*/
  protected AliceResourceUtilities() {
    throw new AssertionError();
  }

  private static String findLocalizedText(String bundleName, String key, Locale locale) {
    if ((bundleName != null) && (key != null)) {
      try {
        ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle(bundleName, locale);
        String rv = resourceBundle.getString(key);
        return rv;
      } catch (MissingResourceException mre) {
        //Logger.errln( bundleName, key );
        return null;
      }
    } else {
      return null;
    }
  }

  private static String CLASS_NAME_LOCALIZATION_BUNDLE = ModelResource.class.getPackage().getName() + ".GalleryNames";
  private static String GROUP_TAGS_LOCALIZATION_BUNDLE = ModelResource.class.getPackage().getName() + ".GalleryTags";
  private static String THEME_TAGS_LOCALIZATION_BUNDLE = ModelResource.class.getPackage().getName() + ".GalleryTags";
  private static String TAGS_LOCALIZATION_BUNDLE = ModelResource.class.getPackage().getName() + ".GalleryTags";

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

  public static SkeletonVisual decodeVisual(URL url) {
    try {
      InputStream is = url.openStream();
      BinaryDecoder decoder = new InputStreamBinaryDecoder(is);
      return decoder.decodeReferenceableBinaryEncodableAndDecodable(new HashMap<Integer, ReferenceableBinaryEncodableAndDecodable>());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static TexturedAppearance[] decodeTexture(URL url) {
    try {
      InputStream is = url.openStream();
      BinaryDecoder decoder = new InputStreamBinaryDecoder(is);
      TexturedAppearance[] rv = decoder.decodeReferenceableBinaryEncodableAndDecodableArray(TexturedAppearance.class, new HashMap<Integer, ReferenceableBinaryEncodableAndDecodable>());
      for (TexturedAppearance ta : rv) {
        correctDimensions(ta);
        ((BufferedImageTexture) ta.diffuseColorTexture.getValue()).directSetMipMappingDesired(false);
      }
      return rv;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // Stretch images to power of two to fix rendering on certain Mac graphics configurations.
  // The problem was observed specifically with the Baby Penguin model.
  private static void correctDimensions(TexturedAppearance ta) {
    Texture texture = ta.diffuseColorTexture.getValue();
    if (texture instanceof BufferedImageTexture) {
      BufferedImageTexture buffTexture = (BufferedImageTexture) texture;
      buffTexture.setBufferedImage(ImageUtilities.stretchToPowersOfTwo(buffTexture.getBufferedImage()));
    }
  }

  public static void encodeVisual(final SkeletonVisual toSave, OutputStream os) throws IOException {
    BinaryEncoder encoder = new OutputStreamBinaryEncoder(os);
    encoder.encode(toSave, new HashMap<ReferenceableBinaryEncodableAndDecodable, Integer>());
    encoder.flush();
  }

  public static void encodeVisual(final SkeletonVisual toSave, File file) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileOutputStream fos = new FileOutputStream(file);
    encodeVisual(toSave, fos);
    fos.close();
  }

  public static void encodeTexture(final TexturedAppearance[] toSave, OutputStream os) throws IOException {
    BinaryEncoder encoder = new OutputStreamBinaryEncoder(os);
    encoder.encode(toSave, new HashMap<ReferenceableBinaryEncodableAndDecodable, Integer>());
    encoder.flush();
  }

  public static void encodeTexture(final TexturedAppearance[] toSave, File file) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileOutputStream fos = new FileOutputStream(file);
    encodeTexture(toSave, fos);
    fos.close();
  }

  public static InputStream getAliceResourceAsStream(Class<?> cls, String resourceString) {
    return StorytellingResources.INSTANCE.getAliceResourceAsStream(cls.getPackage().getName().replace(".", "/") + "/" + resourceString);
  }

  public static URL getAliceResource(Class<?> cls, String resourceString) {
    return StorytellingResources.INSTANCE.getAliceResource(cls.getPackage().getName().replace(".", "/") + "/" + resourceString);
  }

  public static String enumToCamelCase(String enumName, boolean startWithLowerCase) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < enumName.length(); i++) {
      if (i == 0) {
        if (startWithLowerCase) {
          sb.append(Character.toLowerCase(enumName.charAt(i)));
        } else {
          sb.append(Character.toUpperCase(enumName.charAt(i)));
        }
      } else if (enumName.charAt(i - 1) == '_') {
        sb.append(Character.toUpperCase(enumName.charAt(i)));
      } else if (enumName.charAt(i) != '_') {
        sb.append(Character.toLowerCase(enumName.charAt(i)));
      }
    }
    return sb.toString();
  }

  public static String enumToCamelCase(String enumName) {
    return enumToCamelCase(enumName, false);
  }

  public static String camelCaseToEnum(String name) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < name.length(); i++) {
      if ((i != 0) && Character.isUpperCase(name.charAt(i))) {
        sb.append('_');
      }
      sb.append(Character.toUpperCase(name.charAt(i)));
    }
    return sb.toString();
  }

  public static boolean isEnumName(String name) {
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if ((c != '_') && !(Character.isUpperCase(c) || Character.isDigit(c))) {
        return false;
      }
    }
    return true;
  }

  public static String makeEnumName(String name) {
    if (isEnumName(name)) {
      return name;
    }
    if (name.contains("_")) {
      return name.toUpperCase();
    } else {
      return camelCaseToEnum(name);
    }
  }

  public static String makeLocalizationKey(String key) {
    return key.replace(' ', '_');
  }

  public static String arrayToEnum(String[] nameArray, int start, int end) {
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    for (int i = start; i < end; i++) {
      if (nameArray[i].length() > 0) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append("_");
        }
        sb.append(nameArray[i].toUpperCase());
      }
    }
    return sb.toString();
  }

  //Check to see if a thumbnail exists for the given visual name and texture name
  private static boolean checkVisualAndTextureName(ModelResource resource, String visualName, String textureName) {
    String thumbnailFileName = getThumbnailResourceFileName(visualName, textureName);
    return getThumbnailURLInternalFromFilename(resource, thumbnailFileName) != null;
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

  private static void findAndStoreResourceNames(ModelResource resource, String resourceName) {
    String[] splitName = resourceName.split("_");
    StringBuilder modelName = new StringBuilder();
    //Set up to try the simple approach first (that the visual is the class name and the texture is the resource name)
    String visualName = AliceResourceClassUtilities.getAliceClassName(resource.getClass().getSimpleName());
    String textureName = resourceName;

    //Check the simple case (visual name is class name and texture name is resource name) and if it fails, iterate through the resource name to find a visual name that resolves to a valid url
    //Use that as the visual name and the remaining name as the texture name
    boolean found = false;
    if (checkVisualAndTextureName(resource, visualName, textureName)) {
      found = true;
    } else if (checkVisualAndTextureName(resource, enumToCamelCase(resourceName), "")) {
      //Try using the resourceName as the visual name and assume no specified texture
      visualName = enumToCamelCase(resourceName);
      textureName = "";
      found = true;
    } else {
      for (int i = 0; i < splitName.length; i++) {
        if (splitName[i].length() > 0) {
          if (i != 0) {
            modelName.append("_");
          }
          modelName.append(splitName[i]);
          visualName = enumToCamelCase(modelName.toString());
          textureName = arrayToEnum(splitName, i + 1, splitName.length);
          if (checkVisualAndTextureName(resource, visualName, textureName)) {
            checkVisualAndTextureName(resource, visualName, textureName);
            found = true;
            break;
          }
        }
      }
    }
    if (!found) {
      modelName = new StringBuilder();
      for (int i = 0; i < splitName.length; i++) {
        if (splitName[i].length() > 0) {
          if (i != 0) {
            modelName.append("_");
          }
          modelName.append(splitName[i]);
          visualName = enumToCamelCase(modelName.toString());
          textureName = arrayToEnum(splitName, i + 1, splitName.length);
          if (checkVisualAndTextureName(resource, visualName, textureName)) {
            Logger.warning("Initially failed to find resource names for '" + resource + "' and '" + resourceName + "' but did find for '" + modelName + "'");
            break;
          }
        }
      }
      return;
    }
    String identifier = resource.identifierFor(resourceName);
    resourceIdentifierToResourceNamesMap.put(identifier, new ResourceNames(visualName, textureName));
  }

  public static String getModelNameFromClassAndResource(ModelResource resource, String resourceName) {
    //If we're just using the class as a lookup, return the class name directly
    if (resourceName == null) {
      return getName(resource.getClass());
    }
    String identifier = resource.identifierFor(resourceName);
    if (!resourceIdentifierToResourceNamesMap.containsKey(identifier)) {
      findAndStoreResourceNames(resource, resourceName);
    }
    if (resourceIdentifierToResourceNamesMap.get(identifier) != null) {
      return resourceIdentifierToResourceNamesMap.get(identifier).visualName;
    } else {
      Logger.warning("Failed to find resource names for '" + resource + "' and '" + resourceName + "'");
      return null;
    }
  }

  public static String getTextureNameFromClassAndResource(ModelResource resource, String resourceName) {
    //If we're just using the class as a lookup, return null since the class name only distinguishes visuals
    if (resourceName == null) {
      return null;
    }
    String identifier = resource.identifierFor(resourceName);
    if (!resourceIdentifierToResourceNamesMap.containsKey(identifier)) {
      findAndStoreResourceNames(resource, resourceName);
    }
    ResourceNames resourceNames = resourceIdentifierToResourceNamesMap.get(identifier);
    if (resourceNames != null) {
      return resourceNames.textureName;
    } else {
      Logger.severe(resource, resourceName, identifier);
      return null;
    }
  }

  public static String getTextureResourceFileName(ModelResource resource, String resourceName) {
    String modelName = getModelNameFromClassAndResource(resource, resourceName);
    String textureName = getTextureNameFromClassAndResource(resource, resourceName);
    return getTextureResourceFileName(modelName, textureName);
  }

  public static String getTextureResourceFileName(ModelResource resource) {
    return getTextureResourceFileName(resource, resource.toString());
  }

  public static String getVisualResourceFileName(ModelResource resource, String resourceName) {
    String modelName = getModelNameFromClassAndResource(resource, resourceName);
    return getVisualResourceFileNameFromModelName(modelName);
  }

  public static String getVisualResourceName(ModelResource resource) {
    return getModelNameFromClassAndResource(resource, resource.toString());
  }

  public static String getTextureResourceName(ModelResource resource) {
    return getTextureNameFromClassAndResource(resource, resource.toString());
  }

  private static String getVisualResourceFileName(ModelResource resource) {
    return getVisualResourceFileName(resource, resource.toString());
  }

  public static String getThumbnailResourceFileName(ModelResource resource, String resourceName) {
    String modelName = getModelNameFromClassAndResource(resource, resourceName);
    if (modelName != null) {
      String textureName = getTextureNameFromClassAndResource(resource, resourceName);
      return getThumbnailResourceFileName(modelName, textureName);
    } else {
      return null;
    }
  }

  public static String getDefaultTextureEnumName(String resourceName) {
    return "DEFAULT";
  }

  private static String createTextureBaseName(String modelName, String textureName) {
    if (modelName == null) {
      return null;
    }
    if (textureName == null) {
      textureName = "_cls";
    } else if (textureName.equalsIgnoreCase(getDefaultTextureEnumName(modelName))
        || modelName.equalsIgnoreCase(enumToCamelCase(textureName))
        || textureName.equalsIgnoreCase(makeEnumName(modelName))) {
      textureName = "";
    } else if (textureName.length() > 0) {
      textureName = "_" + makeEnumName(textureName);
    }
    return (modelName != null ? modelName.toLowerCase(Locale.ENGLISH) : null) + textureName;
  }

  public static String getThumbnailResourceFileName(String modelName, String textureName) {
    return createTextureBaseName(modelName, textureName) + ".png";
  }

  public static String getTextureResourceFileName(String modelName, String textureName) {
    return createTextureBaseName(modelName, textureName) + "." + TEXTURE_RESOURCE_EXTENSION;
  }

  public static String getVisualResourceFileNameFromModelName(String modelName, String extension) {
    return modelName.toLowerCase(Locale.ENGLISH) + "." + extension;
  }

  public static String getVisualResourceFileNameFromModelName(String modelName) {
    return getVisualResourceFileNameFromModelName(modelName, MODEL_RESOURCE_EXTENSION);
  }

  private static URL getThumbnailURLInternalFromFilename(ModelResource modelResource, String thumbnailFilename) {
    return getAliceResource(modelResource.getClass(),
                            getResourceSubDirWithSeparator(modelResource.getClass()) + thumbnailFilename);
  }

  private static String getResourceSubDirWithSeparator(Class<?> resource) {
    return ModelResourceIoUtilities.getResourceSubDirWithSeparator(resource.getSimpleName());
  }

  public static URL getTextureURL(ModelResource resource) {
    if (resource instanceof DynamicResource) {
      final URI textureURI = ((DynamicResource) resource).getTextureURI();
      if (textureURI == null) {
        return null;
      }
      try {
        return textureURI.toURL();
      } catch (MalformedURLException e) {
        Logger.severe("Failed to get texture URL for " + textureURI, e);
        return null;
      }
    }
    return getUrl(resource, getTextureResourceFileName(resource));
  }

  public static URL getUrl(ModelResource resource, String visualResourceFileName) {
    return getAliceResource(resource.getClass(), getResourceSubDirWithSeparator(resource.getClass()) + visualResourceFileName);
  }

  private static URL getVisualURL(ModelResource resource) {
    if (resource instanceof DynamicResource) {
      final URI visualURI = ((DynamicResource) resource).getVisualURI();
      if (visualURI == null) {
        return null;
      }
      try {
        return visualURI.toURL();
      } catch (MalformedURLException e) {
        Logger.severe("Failed to get visual URL for " + visualURI, e);
        return null;
      }
    }
    return getUrl(resource, getVisualResourceFileName(resource));
  }

  public static SkeletonVisual getVisual(ModelResource resource) {
    URL resourceURL = getVisualURL(resource);
    if (urlToVisualMap.containsKey(resourceURL)) {
      return urlToVisualMap.get(resourceURL);
    } else {
      SkeletonVisual visual = decodeVisual(resourceURL);
      List<Problem> problems = QualityAssuranceUtilities.inspect(visual);
      if (problems.size() > 0) {
        Logger.errln(resourceURL);
        for (Problem problem : problems) {
          Logger.errln(problem);
        }
      }
      urlToVisualMap.put(resourceURL, visual);
      return visual;
    }
  }

  public static SkeletonVisual getVisualCopy(ModelResource resource) {
    SkeletonVisual original = getVisual(resource);
    return createCopy(original);
  }

  public static TexturedAppearance[] getTexturedAppearances(ModelResource resource) {
    URL resourceURL = getTextureURL(resource);
    if (urlToTextureMap.containsKey(resourceURL)) {
      return urlToTextureMap.get(resourceURL);
    } else {
      TexturedAppearance[] texture = decodeTexture(resourceURL);
      urlToTextureMap.put(resourceURL, texture);
      return texture;
    }
  }

  public static SkeletonVisual createCopy(SkeletonVisual sgOriginal) {
    Geometry[] sgGeometries = sgOriginal.geometries.getValue();
    TexturedAppearance[] sgTextureAppearances = sgOriginal.textures.getValue();
    WeightedMesh[] sgWeightedMeshes = sgOriginal.weightedMeshes.getValue();
    WeightedMesh[] sgDefaultPoseWeightedMeshes = sgOriginal.defaultPoseWeightedMeshes.getValue();
    boolean hasDefaultPoseWeightedMeshes = sgOriginal.hasDefaultPoseWeightedMeshes.getValue();
    Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
    AxisAlignedBox bbox = sgOriginal.baseBoundingBox.getValue();
    Matrix3x3 scaleCopy = new Matrix3x3(sgOriginal.scale.getValue());
    Appearance sgFrontAppearanceCopy;
    if (sgOriginal.frontFacingAppearance.getValue() != null) {
      sgFrontAppearanceCopy = (Appearance) sgOriginal.frontFacingAppearance.getValue().newCopy();
    } else {
      sgFrontAppearanceCopy = null;
    }
    Appearance sgBackAppearanceCopy;
    if (sgOriginal.backFacingAppearance.getValue() != null) {
      sgBackAppearanceCopy = (Appearance) sgOriginal.backFacingAppearance.getValue().newCopy();
    } else {
      sgBackAppearanceCopy = null;
    }

    SkeletonVisual rv = new SkeletonVisual();
    final Joint sgSkeletonRootCopy;
    if (sgSkeletonRoot != null) {
      sgSkeletonRootCopy = (Joint) sgSkeletonRoot.newCopy();
    } else {
      sgSkeletonRootCopy = null;
    }

    rv.skeleton.setValue(sgSkeletonRootCopy);
    rv.geometries.setValue(sgGeometries);
    rv.weightedMeshes.setValue(sgWeightedMeshes);
    rv.defaultPoseWeightedMeshes.setValue(sgDefaultPoseWeightedMeshes);
    rv.hasDefaultPoseWeightedMeshes.setValue(hasDefaultPoseWeightedMeshes);
    rv.textures.setValue(sgTextureAppearances);
    rv.frontFacingAppearance.setValue(sgFrontAppearanceCopy);
    rv.backFacingAppearance.setValue(sgBackAppearanceCopy);
    rv.baseBoundingBox.setValue(bbox);
    rv.isShowing.setValue(sgOriginal.isShowing.getValue());
    rv.scale.setValue(scaleCopy);
    return rv;
  }

  public static SkeletonVisual createReplaceVisualElements(SkeletonVisual sgOriginal, ModelResource resource) {
    SkeletonVisual sgToReplaceWith = getVisual(resource);
    Geometry[] sgGeometries = sgToReplaceWith.geometries.getValue();
    WeightedMesh[] sgWeightedMeshes = sgToReplaceWith.weightedMeshes.getValue();
    AxisAlignedBox bbox = sgToReplaceWith.baseBoundingBox.getValue();
    Joint sgNewSkeletonRoot = sgToReplaceWith.skeleton.getValue();
    final Joint sgNewSkeleton;
    if (sgNewSkeletonRoot != null) {
      sgNewSkeleton = (Joint) sgNewSkeletonRoot.newCopy();
    } else {
      sgNewSkeleton = null;
    }
    if (sgNewSkeleton != null) {
      sgNewSkeleton.setParent(sgOriginal.getParent());
    }

    //      if (sgOriginal.skeleton.getValue() != null) {
    //        sgOriginal.skeleton.getValue().setParent(null);
    //      }
    sgOriginal.skeleton.setValue(sgNewSkeleton);

    sgOriginal.geometries.setValue(sgGeometries);
    sgOriginal.weightedMeshes.setValue(sgWeightedMeshes);

    sgOriginal.baseBoundingBox.setValue(bbox);
    return sgOriginal;
  }

  public static AffineMatrix4x4 getOriginalJointTransformation(ModelResource resource, JointId jointId) {
    SkeletonVisual sgOriginal = getVisual(resource);
    Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
    Joint sgJoint = sgSkeletonRoot.getJoint(jointId.toString());
    return sgJoint.getLocalTransformation();
  }

  public static UnitQuaternion getOriginalJointOrientation(ModelResource resource, JointId jointId) {
    SkeletonVisual sgOriginal = getVisual(resource);
    Joint sgSkeletonRoot = sgOriginal.skeleton.getValue();
    Joint sgJoint = sgSkeletonRoot.getJoint(jointId.toString());
    return sgJoint.getLocalTransformation().orientation.createUnitQuaternion();
  }

  public static String getName(Class<?> modelResource) {
    return AliceResourceClassUtilities.getAliceClassName(modelResource);
  }

  public static String trimName(String name) {
    name = name.trim();
    while (name.contains("__")) {
      name = name.replace("__", "_");
    }
    while (name.startsWith("_")) {
      name = name.substring(1);
    }
    while (name.endsWith("_")) {
      name = name.substring(0, name.length() - 1);
    }
    return name;
  }

  public static URL getThumbnailURL(ModelResource modelResource, String instanceName) {
    String thumbnailName = getThumbnailResourceFileName(modelResource, instanceName);
    return getThumbnailURLInternalFromFilename(modelResource, thumbnailName);
  }

  public static URL getThumbnailURL(Class<?> modelResource) {
    String thumbnailFilename =  getThumbnailResourceFileName(getName(modelResource), null);
    return getAliceResource(modelResource, getResourceSubDirWithSeparator(modelResource) + thumbnailFilename);
  }

    /*private*/
  protected static String getKey(Class<?> modelResource, String resourceName) {
    if (resourceName != null) {
      return modelResource.getName() + resourceName;
    }
    return modelResource.getName();
  }

  public static ModelResourceInfo getModelResourceInfo(Class<?> modelResource, String resourceName) {
    if (modelResource == null) {
      return null;
    }
    String key = getKey(modelResource, resourceName);
    //Return the info if we have it cached
    if (classToInfoMap.containsKey(key)) {
      return classToInfoMap.get(key);
    } else {
      String parentKey = getKey(modelResource, null);
      //If we don't have the parent info cached, load it from disk
      ModelResourceInfo parentInfo = null;
      if (!classToInfoMap.containsKey(parentKey)) {
        String name = getName(modelResource);
        try {
          //xml files are not referenced off the
          InputStream is = getAliceResourceAsStream(modelResource, ModelResourceIoUtilities.getResourceSubDirWithSeparator("") + name + ".xml");
          if (is != null) {
            Document doc = XMLUtilities.read(is);
            parentInfo = new ModelResourceInfo(doc);
            classToInfoMap.put(parentKey, parentInfo);
          } else {
            //This is an acceptable case because classes like Biped don't have class infos
            classToInfoMap.put(parentKey, null);
          }
        } catch (Exception e) {
          Logger.severe("Failed to parse class info for " + name + ": " + e);
          classToInfoMap.put(parentKey, null);
        }
      } else {
        parentInfo = classToInfoMap.get(parentKey);
      }
      if (parentInfo != null) {
        //If the key we're looking for is the same as the parent key, then just return the parent info
        if (parentKey.equals(key)) {
          return parentInfo;
        }
        //Otherwise get the model and texture names and find the sub resource we're looking for
        ModelResourceInfo subResource = parentInfo.getSubResource(resourceName);
        if (subResource == null) {
          Logger.severe("Failed to find a resource for " + modelResource + " : " + resourceName);
        }
        //Cache the sub resource under the original main key
        classToInfoMap.put(key, subResource);
        return subResource;
      }
      return null;
    }
  }

  public static AxisAlignedBox getBoundingBox(Class<?> modelResource, String resourceName) {
    ModelResourceInfo info = getModelResourceInfo(modelResource, resourceName);
    if (info != null) {
      return info.getBoundingBox();
    }
    //TODO: implement better solution for getting bounding boxes for general resources (like Swimmer, Flyer, etc.)
    if (modelResource != null) {
      if (SwimmerResource.class.isAssignableFrom(modelResource)) {
        return new AxisAlignedBox(new Point3(-.5, -.5, -.5), new Point3(.5, .5, .5));
      }
    }
    return new AxisAlignedBox(new Point3(-.5, 0, -.5), new Point3(.5, 1, .5));
  }

  public static AxisAlignedBox getBoundingBox(Class<?> modelResource) {
    return getBoundingBox(modelResource, null);
  }

  public static AffineMatrix4x4 getDefaultInitialTransform(Class<?> modelResource) {
    AffineMatrix4x4 rv = AffineMatrix4x4.createIdentity();
    AxisAlignedBox bbox = getBoundingBox(modelResource);
    if ((bbox != null) && !bbox.isNaN()) {
      boolean placeOnGround = getPlaceOnGround(modelResource);
      if (placeOnGround) {
        rv.translation.y = -bbox.getYMinimum();
      }
    }
    return rv;
  }

  public static boolean getPlaceOnGround(Class<?> modelResource, String resourceName) {
    ModelResourceInfo info = getModelResourceInfo(modelResource, resourceName);
    if (info != null) {
      return info.getPlaceOnGround();
    }
    //TODO: implement better solution for getting placeOnGround for general resources (like Swimmer, Flyer, etc.)
    if (modelResource != null) {
      if (SwimmerResource.class.isAssignableFrom(modelResource)) {
        return true;
      }
    }
    return false;
  }

  public static boolean getPlaceOnGround(Class<?> modelResource) {
    return getPlaceOnGround(modelResource, null);
  }

  /*private*/
  protected static String getModelName(Class<?> modelResource, String resourceName, Locale locale) {
    ModelResourceInfo info = getModelResourceInfo(modelResource, resourceName);
    if (info != null) {
      if (locale == null) {
        return info.getModelName();
      } else {
        String packageName = modelResource.getPackage().getName();
        return findLocalizedText(getClassNameLocalizationBundleName(), packageName + "." + info.getModelName(), locale);
      }
    }
    return null;
  }

  public static String getModelClassName(Class<?> modelResource, String resourceName, Locale locale) {
    ModelResourceInfo info = getModelResourceInfo(modelResource, null);
    String className;
    String packageName = modelResource.getPackage().getName();
    if (info != null) {
      className = info.getModelName();
    } else {
      className = modelResource.getSimpleName().replace("Resource", "");
    }

    if (locale == null) {
      return className;
    } else {
      String localizedText = findLocalizedText(getClassNameLocalizationBundleName(), packageName + "." + className, locale);
      if (localizedText == null) {
        localizedText = className;
      }
      return localizedText;
    }
  }

  private static String[] getLocalizedTags(String[] tags, String localizerBundleName, Locale locale, boolean acceptNull) {
    //    if( Locale.ENGLISH.getLanguage().equals( locale.getLanguage() ) ) {
    List<String> localizedTags = Lists.newArrayList();
    for (String tag : tags) {
      String[] splitTags = tag.split(":");
      StringBuilder finalTag = new StringBuilder();
      for (int i = 0; i < splitTags.length; i++) {
        String t = splitTags[i];
        boolean hasStar = t.startsWith("*");
        String stringToUse;
        if (hasStar) {
          stringToUse = t.substring(1);
        } else {
          stringToUse = t;
        }
        String localizationKey = makeLocalizationKey(stringToUse);
        String localizedTag = findLocalizedText(localizerBundleName, localizationKey, locale);
        if (acceptNull && (localizedTag == null)) {
          localizedTag = stringToUse;
        }
        if (localizedTag != null) {
          if (i > 0) {
            finalTag.append(":");
          }
          if (hasStar) {
            finalTag.append("*");
          }
          finalTag.append(localizedTag);
        }
      }
      if (finalTag.length() > 0) {
        localizedTags.add(finalTag.toString());
      }
    }
    return localizedTags.toArray(new String[localizedTags.size()]);
    //    } else {
    //      return new String[ 0 ];
    //    }
  }

  public static String[] getTags(Class<?> modelResource, String resourceName, Locale locale) {
    ModelResourceInfo info = getModelResourceInfo(modelResource, resourceName);
    if (info != null) {
      if (locale == null) {
        return info.getTags();
      } else {
        boolean acceptNull = locale.getLanguage().equals("en");
        return getLocalizedTags(info.getTags(), getTagsLocalizationBundleName(), locale, acceptNull);
      }
    } else {
      return null;
    }
  }

  public static String[] getGroupTags(Class<?> modelResource, String resourceName, Locale locale) {
    ModelResourceInfo info = getModelResourceInfo(modelResource, resourceName);
    if (info != null) {
      if ((locale == null) || true) {
        return info.getGroupTags();
      } else {
        return getLocalizedTags(info.getGroupTags(), getGroupTagsLocalizationBundleName(), locale, true);
      }
    } else {
      return null;
    }
  }

  public static String getLocalizedTag(String tag, Locale locale) {
    if (locale == null) {
      return tag;
    }
    if (tag.contains(" ")) {
      tag = tag.replace(" ", "_");
    }
    String result = findLocalizedText(getTagsLocalizationBundleName(), tag, locale);
    if (result != null) {
      return result;
    } else {
      Logger.severe("No localization for gallery tag '" + tag + "' for locale " + locale);
      return tag;
    }
  }

  public static String[] getThemeTags(Class<?> modelResource, String resourceName, Locale locale) {
    ModelResourceInfo info = getModelResourceInfo(modelResource, resourceName);
    if (info != null) {
      if ((locale == null) || true) {
        return info.getThemeTags();
      } else {
        return getLocalizedTags(info.getThemeTags(), getThemeTagsLocalizationBundleName(), locale, true);
      }
    } else {
      return null;
    }
  }
}
