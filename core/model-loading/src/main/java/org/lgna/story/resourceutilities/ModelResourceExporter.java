/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.resourceutilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lgna.story.BipedPose;
import org.lgna.story.BipedPoseBuilder;
import org.lgna.story.FlyerPose;
import org.lgna.story.FlyerPoseBuilder;
import org.lgna.story.JointedModelPose;
import org.lgna.story.JointedModelPoseBuilder;
import org.lgna.story.Pose;
import org.lgna.story.QuadrupedPose;
import org.lgna.story.QuadrupedPoseBuilder;
import org.lgna.story.SlithererPose;
import org.lgna.story.SlithererPoseBuilder;
import org.lgna.story.SwimmerPose;
import org.lgna.story.SwimmerPoseBuilder;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.implementation.alice.AliceResourceUtilities;
import org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory;
import org.lgna.story.implementation.alice.ModelResourceIoUtilities;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointArrayId;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.QuadrupedResource;
import org.lgna.story.resources.SlithererResource;
import org.lgna.story.resources.SwimmerResource;
import org.w3c.dom.Document;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.xml.XMLUtilities;
import org.w3c.dom.Element;

public class ModelResourceExporter {

  private static boolean REMOVE_ROOT_JOINTS = false;

  private static final String ROOT_IDS_FIELD_NAME = "JOINT_ID_ROOTS";
  private static final String ROOT_IDS_METHOD_NAME = "getRootJointIds";

  private class NamedFile {
    public String name;
    public File file;

    public NamedFile(String name, File file) {
      this.name = name;
      this.file = file;
    }
  }

  private String resourceName;
  private String className;
  private List<String> tags = new LinkedList<String>();
  private List<String> groupTags = new LinkedList<String>();
  private List<String> themeTags = new LinkedList<String>();
  private Map<String, AxisAlignedBox> boundingBoxes = new HashMap<String, AxisAlignedBox>();
  private File xmlFile;
  private File javaFile;
  private List<String> jointIdsToSuppress = new ArrayList<String>();
  private List<String> arraysToExposeFirstElementOf = new ArrayList<String>();
  private List<String> arraysToHideElementsOf = new ArrayList<String>();
  private Map<ModelSubResourceExporter, Image> thumbnails = new HashMap<ModelSubResourceExporter, Image>();
  private Map<String, File> existingThumbnails = null;
  private List<ModelSubResourceExporter> subResources = new LinkedList<ModelSubResourceExporter>();
  private boolean isSims = false;
  private boolean hasNewData = false;
  private boolean forceRebuildCode = false;
  private boolean forceRebuildXML = false;
  private Date lastEdited = null;
  private boolean shouldRecenter = false;
  private boolean recenterXZ = false;
  private boolean moveCenterToBottom = true;
  private List<String> forcedOverridingEnumNames = new ArrayList<String>();
  private Map<String, List<String>> forcedEnumNamesMap = new HashMap<String, List<String>>();
  private Map<String, String> customArrayNameMap = new HashMap<String, String>();
  private Map<String, Map<String, AffineMatrix4x4>> poses = new HashMap<String, Map<String, AffineMatrix4x4>>();
  private String[] arrayNamesToSkip = null;
  private boolean exportGalleryResources = true;
  private boolean isDeprecated = false;
  private boolean placeOnGround = false;
  private boolean validData = false;

  private boolean enableArraySupport = true;

  private String attributionName;
  private String attributionYear;

  private Class<?> jointAndVisualFactory = JointImplementationAndVisualDataFactory.class;

  private ModelClassData classData;
  private List<Tuple2<String, String>> jointList;

  public ModelResourceExporter(String className) {
    this.className = className;
    if (Character.isLowerCase(this.className.charAt(0))) {
      this.className = this.className.substring(0, 1).toUpperCase() + this.className.substring(1);
    }
  }

  public ModelResourceExporter(String className, String resourceName) {
    this(className);
    this.resourceName = resourceName;
  }

  public ModelResourceExporter(String className, ModelClassData classData) {
    this(className);
    this.classData = classData;
  }

  public ModelResourceExporter(String className, String resourceName, ModelClassData classData) {
    this(className, resourceName);
    this.classData = classData;
  }

  public ModelResourceExporter(String className, ModelClassData classData, Class<?> jointAndVisualFactoryClass) {
    this(className, classData);
    this.jointAndVisualFactory = jointAndVisualFactoryClass;
  }

  public ModelResourceExporter(String className, String resourceName, ModelClassData classData, Class<?> jointAndVisualFactoryClass) {
    this(className, resourceName, classData);
    this.jointAndVisualFactory = jointAndVisualFactoryClass;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public void setHasNewData(boolean hasNewData) {
    this.hasNewData = hasNewData;
  }

  public void setIsDeprecated(boolean isDeprecated) {
    this.isDeprecated = isDeprecated;
  }

  public void setPlaceOnGround(boolean placeOnGround) {
    this.placeOnGround = placeOnGround;
  }

  public void setShouldRecenter(boolean shouldRecenter) {
    this.shouldRecenter = shouldRecenter;
    if (!this.shouldRecenter) {
      this.moveCenterToBottom = false;
      this.recenterXZ = false;
    }
  }

  public void addPose(String poseName, Map<String, AffineMatrix4x4> pose) {
    this.poses.put(poseName, pose);
  }

  public void setEnableArrays(boolean enableArrays) {
    this.enableArraySupport = enableArrays;
  }

  public boolean shouldRecenter() {
    return this.shouldRecenter;
  }

  public void setMoveCenterToBottom(boolean moveCenter) {
    this.moveCenterToBottom = moveCenter;
  }

  public boolean shouldMoveCenterToBottom() {
    return this.moveCenterToBottom;
  }

  public void setRecenterXZ(boolean recenterXZ) {
    this.recenterXZ = recenterXZ;
  }

  public boolean shouldRecenterXZ() {
    return this.recenterXZ;
  }

  public boolean hasValidData() {
    return this.validData;
  }

  public void setHasValidData(boolean validData) {
    this.validData = validData;
  }

  public boolean hasNewData() {
    return this.hasNewData;
  }

  public void setLastEdited(Date lastEdited) {
    this.lastEdited = lastEdited;
  }

  public void addLastEditedDate(Date date) {
    if (date == null) {
      return;
    }
    if (this.lastEdited == null) {
      this.lastEdited = date;
    } else if (date.after(this.lastEdited)) {
      this.lastEdited = date;
    }
  }

  public void setExportGalleryResources(boolean exportResources) {
    this.exportGalleryResources = exportResources;
  }

  public boolean getExportGalleryResources() {
    return this.exportGalleryResources;
  }

  public static boolean isMoreRecentThan(Date dataDate, File file) {
    if (dataDate == null) {
      return false;
    }
    Date fileDate = new Date(file.lastModified());
    boolean isNewer = dataDate.after(fileDate);
    return isNewer;
  }

  public static boolean isMoreRecentThan(Date dataData, Date otherDate) {
    if (dataData == null) {
      return false;
    }
    return dataData.after(otherDate);
  }

  public String getResourceName() {
    return this.resourceName;
  }

  public ModelClassData getClassData() {
    return this.classData;
  }

  public void setForceRebuildCode(boolean rebuildCode) {
    this.forceRebuildCode = rebuildCode;
  }

  public void setForceRebuildXML(boolean rebuildXML) {
    this.forceRebuildXML = rebuildXML;
  }

  public boolean getForceRebuildCode() {
    return this.forceRebuildCode;
  }

  public boolean getForceRebuildXML() {
    return this.forceRebuildXML;
  }

  public void setJointAndVisualFactory(Class<?> jointAndVisualFactoryClass) {
    this.jointAndVisualFactory = jointAndVisualFactoryClass;
  }

  public boolean hasJointMap() {
    return this.jointList != null;
  }

  public boolean hasJoints() {
    return hasJointMap() && !this.jointList.isEmpty();
  }

  public List<Tuple2<String, String>> getJointMap() {
    return this.jointList;
  }

  private boolean hasParent(List<Tuple2<String, String>> listToCheck, String parent) {
    if ((parent == null) || (parent.length() == 0)) {
      return true;
    }
    for (Tuple2<String, String> entry : listToCheck) {
      if (entry.getA().equalsIgnoreCase(parent)) {
        return true;
      }
    }
    return false;
  }

  public void addArrayNamesToExposeFirstElementOf(List<String> arrayNames) {
    for (String arrayName : arrayNames) {
      if (!this.arraysToExposeFirstElementOf.contains(arrayName)) {
        this.arraysToExposeFirstElementOf.add(arrayName);
      }
    }
  }

  public void addArrayNamesToExposeFirstElementOf(String[] arrayNames) {
    for (String arrayName : arrayNames) {
      if (!this.arraysToExposeFirstElementOf.contains(arrayName)) {
        this.arraysToExposeFirstElementOf.add(arrayName);
      }
    }
  }

  public void addArrayNamesToHideElementsOf(List<String> arrayNames) {
    for (String arrayName : arrayNames) {
      if (!this.arraysToHideElementsOf.contains(arrayName)) {
        this.arraysToHideElementsOf.add(arrayName);
      }
    }
  }

  public void addArrayNamesToHideElementsOf(String[] arrayNames) {
    for (String arrayName : arrayNames) {
      if (!this.arraysToHideElementsOf.contains(arrayName)) {
        this.arraysToHideElementsOf.add(arrayName);
      }
    }
  }

  public void addJointIdsToSuppress(List<String> jointIds) {
    for (String jointId : jointIds) {
      if (!this.jointIdsToSuppress.contains(jointId)) {
        this.jointIdsToSuppress.add(jointId);
      }
    }
  }

  public void addJointIdsToSuppress(String[] jointIds) {
    for (String jointId : jointIds) {
      if (!this.jointIdsToSuppress.contains(jointId)) {
        this.jointIdsToSuppress.add(jointId);
      }
    }
  }

  static final Pattern arrayPattern = Pattern.compile("(_\\d*$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

  public static int getArrayIndexForJoint(String jointName) {
    Matcher match = arrayPattern.matcher(jointName);
    if (match.find()) {
      String indexStr = jointName.substring(jointName.lastIndexOf('_') + 1);
      //Remove all leading 0s
      indexStr = indexStr.replaceAll("^0+", "");
      //If we've removed all the leading 0s and ended up with an empty string, return the number 0
      if (indexStr.length() == 0) {
        return 0;
      }
      try {
        return Integer.decode(indexStr);
      } catch (NumberFormatException e) {
        System.err.println("Error decoding array index in joint " + jointName);
        return -1;
      }
    } else {
      return -1;
    }
  }

  public static boolean hasArray(String arrayName, List<Tuple2<String, String>> jointList) {
    for (Tuple2<String, String> joint : jointList) {
      if (arrayName.equals(getArrayNameForJoint(joint.getA(), null, null))) {
        return true;
      }
    }
    return false;
  }

  public static String getArrayNameForJoint(String jointName, Map<String, String> customArrayNameMap, String[] namesToSkip) {
    Matcher match = arrayPattern.matcher(jointName);
    if (match.find()) {
      String nameStr = jointName.substring(0, jointName.lastIndexOf('_'));
      if ((customArrayNameMap != null) && customArrayNameMap.containsKey(nameStr)) {
        nameStr = customArrayNameMap.get(nameStr);
      }
      if (namesToSkip != null) {
        for (String toSkip : namesToSkip) {
          if (nameStr.equalsIgnoreCase(toSkip)) {
            return null;
          }
        }
      }
      return nameStr;
    }
    //    int index = jointName.lastIndexOf( '_' );
    //    if( index != -1 ) {
    //      String nameStr = jointName.substring( 0, index );
    //      if( ( customArrayNameMap != null ) && customArrayNameMap.containsKey( nameStr ) ) {
    //        nameStr = customArrayNameMap.get( nameStr );
    //        return nameStr;
    //      }
    //
    //    }
    return null;
  }

  public static Map<String, List<String>> getArrayEntriesFromJointList(List<Tuple2<String, String>> jointList, Map<String, String> customArrayNameMap, List<String> jointsToSuppress, String[] arrayNamesToSkip) throws DataFormatException {
    List<String> jointNames = new LinkedList<String>();
    for (Tuple2<String, String> joint : jointList) {
      jointNames.add(joint.getA());
    }
    return getArrayEntries(jointNames, customArrayNameMap, jointsToSuppress, arrayNamesToSkip);
  }

  public static Map<String, List<String>> getArrayEntries(List<String> jointNames, Map<String, String> customArrayNameMap, List<String> jointsToSuppress, String[] arrayNamesToSkip) throws DataFormatException {
    //Array name, joint name entries
    Map<String, List<String>> arrayEntries = new HashMap<String, List<String>>();
    for (String jointName : jointNames) {
      if ((jointsToSuppress == null) || !jointsToSuppress.contains(jointName)) {
        String arrayName = getArrayNameForJoint(jointName, customArrayNameMap, arrayNamesToSkip);
        if (arrayName != null) {
          if (arrayEntries.containsKey(arrayName)) {
            arrayEntries.get(arrayName).add(jointName);
          } else {
            List<String> arrayElements = new ArrayList<String>();
            arrayElements.add(jointName);
            arrayEntries.put(arrayName, arrayElements);
          }
        }
      }
    }
    //How to sort array entry names that are not really comparable?
    // Like entries with different names (ENGINE_WHEEL_1 vs COAL_BOX_WHEEL_1) but have a clear spatial ordering
    // We don't handle cases like this
    for (Entry<String, List<String>> arrayEntry : arrayEntries.entrySet()) {
      try {
        Collections.sort(arrayEntry.getValue(), new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            int index1 = getArrayIndexForJoint(o1);
            int index2 = getArrayIndexForJoint(o2);
            if (index1 == index2) {
              System.err.println("ERROR COMPARING ARRAY NAME INDICES: " + o1 + " == " + o2);
              //We don't handle cases where the index is the same.
              throw new RuntimeException("ERROR COMPARING ARRAY NAME INDICES: " + o1 + " == " + o2 + ". These names resolve to the same index (" + index1 + ") and that is not allowed.");
            }
            return index1 - index2;
          }
        });
      } catch (RuntimeException e) {
        e.printStackTrace();
        throw new DataFormatException(e.toString());
      }
    }

    return arrayEntries;
  }

  private static List<Tuple2<String, String>> removeEntry(List<Tuple2<String, String>> sourceList, String toRemove) {
    Tuple2<String, String> entryToRemove = null;
    //Find the entry to remove
    for (Tuple2<String, String> entry : sourceList) {
      if (entry.getA().equalsIgnoreCase(toRemove)) {
        entryToRemove = entry;
        break;
      }
    }
    if (entryToRemove != null) {
      //Remap any existing joints that are children of the joint to remove to be children of the parent of the joint to remove
      for (Tuple2<String, String> entry : sourceList) {
        if ((entry.getB() != null) && entry.getB().equalsIgnoreCase(entryToRemove.getA())) {
          entry.setB(entryToRemove.getB());
        }
      }
      //Remove the joint
      sourceList.remove(entryToRemove);
    }
    return sourceList;
  }

  private static boolean isRootJoint(String jointName) {
    return jointName.equalsIgnoreCase("root");
  }

  private List<Tuple2<String, String>> makeCodeReadyTree(List<Tuple2<String, String>> sourceList) {
    if (sourceList != null) {
      List<Tuple2<String, String>> cleaned = new ArrayList<Tuple2<String, String>>();
      for (Tuple2<String, String> entry : sourceList) {
        if (REMOVE_ROOT_JOINTS) {
          if (isRootJoint(entry.getA()) && ((entry.getB() == null) || (entry.getB().length() == 0))) {
            continue;
          } else if ((entry.getB() != null) && isRootJoint(entry.getB())) {
            entry.setB(null);
          }
        }
        cleaned.add(entry);
      }
      List<Tuple2<String, String>> sorted = new ArrayList<Tuple2<String, String>>();
      while (sorted.size() != cleaned.size()) {
        for (Tuple2<String, String> entry : cleaned) {
          if (!sorted.contains(entry) && hasParent(sorted, entry.getB())) {
            sorted.add(entry);
          }
        }
      }

      //Remove joints that are in the "to suppress" list
      //      for (String toSuppress : this.jointIdsToSuppress) {
      //        sorted = removeEntry(sorted, toSuppress);
      //      }

      return sorted;
    }
    return null;
  }

  public void setJointMap(List<Tuple2<String, String>> jointList) {
    this.jointList = jointList;
    //    if (this.classData == null)
    //    {
    //      this.classData = ModelResourceExporter.getBestClassDataForJointList(jointList);
    //    }
  }

  public void addSubResource(ModelSubResourceExporter subResource) {
    this.subResources.add(subResource);
  }

  public void addResource(String modelName, String textureName, String resourceType, String attributionName, String attributionYear) {
    String attributionNameToUse = null;
    String attributionYearToUse = null;
    if ((attributionName != null) && !attributionName.equals(this.attributionName)) {
      attributionNameToUse = attributionName;
    }
    if ((attributionYear != null) && !attributionYear.equals(this.attributionYear)) {
      attributionYearToUse = attributionYear;
    }
    if ((attributionNameToUse != null) && (attributionNameToUse.length() == 0)) {
      attributionNameToUse = null;
    }
    if ((attributionYearToUse != null) && (attributionYearToUse.length() == 0)) {
      attributionYearToUse = null;
    }
    this.addSubResource(new ModelSubResourceExporter(modelName, textureName, resourceType, attributionNameToUse, attributionYearToUse));
  }

  public void addSubResourceTags(String modelName, String textureName, String... tags) {
    if ((tags != null) && (tags.length > 0)) {
      for (ModelSubResourceExporter subResource : this.subResources) {
        if (subResource.getModelName().equalsIgnoreCase(modelName)) {
          if ((textureName == null) || subResource.getTextureName().equalsIgnoreCase(textureName)) {
            subResource.addTags(tags);
          }
        }
      }
    }
  }

  public void addSubResourceGroupTags(String modelName, String textureName, String... tags) {
    if ((tags != null) && (tags.length > 0)) {
      for (ModelSubResourceExporter subResource : this.subResources) {
        if (subResource.getModelName().equalsIgnoreCase(modelName)) {
          if ((textureName == null) || subResource.getTextureName().equalsIgnoreCase(textureName)) {
            subResource.addGroupTags(tags);
          }
        }
      }
    }
  }

  public void addSubResourceThemeTags(String modelName, String textureName, String... tags) {
    if ((tags != null) && (tags.length > 0)) {
      for (ModelSubResourceExporter subResource : this.subResources) {
        if (subResource.getModelName().equalsIgnoreCase(modelName)) {
          if ((textureName == null) || subResource.getTextureName().equalsIgnoreCase(textureName)) {
            subResource.addThemeTags(tags);
          }
        }
      }
    }
  }

  public void addAttribution(String name, String year) {
    this.attributionName = name;
    this.attributionYear = year;
  }

  public void addTags(String... tags) {
    if (tags != null) {
      Collections.addAll(this.tags, tags);
    }
  }

  public void addGroupTags(String... tags) {
    if (tags != null) {
      Collections.addAll(this.groupTags, tags);
    }
  }

  public void addThemeTags(String... tags) {
    if (tags != null) {
      Collections.addAll(this.themeTags, tags);
    }
  }

  public boolean isSims() {
    return this.isSims;
  }

  public void setIsSims(boolean isSims) {
    this.isSims = isSims;
  }

  public String getClassName() {
    return this.className;
  }

  public String getPackageString() {
    return this.classData.packageString;
  }

  public void setBoundingBox(String modelName, AxisAlignedBox boundingBox) {
    this.boundingBoxes.put(modelName, boundingBox);
  }

  //  public void addThumbnail( String modelName, String textureName, String resourceType, String attributionName, String attributionYear, Image thumbnail )
  //  {
  //    this.thumbnails.put( new ModelSubResourceExporter( modelName, textureName, resourceType, attributionName, attributionYear ), thumbnail );
  //  }

  public void addExistingThumbnail(String name, File thumbnailFile) {
    if ((thumbnailFile != null) && thumbnailFile.exists()) {
      if (this.existingThumbnails == null) {
        this.existingThumbnails = new HashMap<String, File>();
      }
      this.existingThumbnails.put(name, thumbnailFile);
    } else {
      System.err.println("FAILED TO ADDED THUMBAIL: " + thumbnailFile + " does not exist.");
    }
  }

  public void setXMLFile(File xmlFile) {
    this.xmlFile = xmlFile;
  }

  public void setJavaFile(File javaFile) {
    this.javaFile = javaFile;
  }

  private static Element createBoundingBoxElement(Document doc, AxisAlignedBox bbox) {
    Element bboxElement = doc.createElement("BoundingBox");
    Element minElement = doc.createElement("Min");
    minElement.setAttribute("x", Double.toString(bbox.getXMinimum()));
    minElement.setAttribute("y", Double.toString(bbox.getYMinimum()));
    minElement.setAttribute("z", Double.toString(bbox.getZMinimum()));
    Element maxElement = doc.createElement("Max");
    maxElement.setAttribute("x", Double.toString(bbox.getXMaximum()));
    maxElement.setAttribute("y", Double.toString(bbox.getYMaximum()));
    maxElement.setAttribute("z", Double.toString(bbox.getZMaximum()));

    bboxElement.appendChild(minElement);
    bboxElement.appendChild(maxElement);

    return bboxElement;
  }

  private static Element createTagsElement(Document doc, List<String> tagList) {
    Element tagsElement = doc.createElement("Tags");
    for (String tag : tagList) {
      Element tagElement = doc.createElement("Tag");
      tagElement.setTextContent(tag);
      tagsElement.appendChild(tagElement);
    }
    return tagsElement;
  }

  private static Element createGroupTagsElement(Document doc, List<String> tagList) {
    Element tagsElement = doc.createElement("GroupTags");
    for (String tag : tagList) {
      Element tagElement = doc.createElement("GroupTag");
      tagElement.setTextContent(tag);
      tagsElement.appendChild(tagElement);
    }
    return tagsElement;
  }

  private static Element createThemeTagsElement(Document doc, List<String> tagList) {
    Element tagsElement = doc.createElement("ThemeTags");
    for (String tag : tagList) {
      Element tagElement = doc.createElement("ThemeTag");
      tagElement.setTextContent(tag);
      tagsElement.appendChild(tagElement);
    }
    return tagsElement;
  }

  private static Element createSubResourceElement(Document doc, ModelSubResourceExporter subResource, ModelResourceExporter parentMRE) {
    Element resourceElement = doc.createElement("Resource");
    resourceElement.setAttribute("textureName", AliceResourceUtilities.makeEnumName(subResource.getTextureName()));
    resourceElement.setAttribute("resourceName", createResourceEnumName(parentMRE, subResource));
    if (subResource.getModelName() != null) {
      resourceElement.setAttribute("modelName", subResource.getModelName());
    }
    if (subResource.getAttributionName() != null) {
      resourceElement.setAttribute("creator", subResource.getAttributionName());
    }
    if (subResource.getAttributionYear() != null) {
      resourceElement.setAttribute("creationYear", subResource.getAttributionYear());
    }
    if (subResource.getModelName() != null) {
      resourceElement.setAttribute("modelName", subResource.getModelName());
    }
    if (subResource.getBbox() != null) {
      resourceElement.appendChild(createBoundingBoxElement(doc, subResource.getBbox()));
    }
    if (subResource.getTags().size() > 0) {
      List<String> uniqueTags = new ArrayList<String>();
      for (String t : subResource.getTags()) {
        if ((parentMRE.tags == null) || !parentMRE.tags.contains(t)) {
          uniqueTags.add(t);
        }
      }
      if (!uniqueTags.isEmpty()) {
        resourceElement.appendChild(createTagsElement(doc, uniqueTags));
      }
    }

    if (subResource.getGroupTags().size() > 0) {
      List<String> uniqueTags = new ArrayList<String>();
      for (String t : subResource.getGroupTags()) {
        if ((parentMRE.groupTags == null) || !parentMRE.groupTags.contains(t)) {
          uniqueTags.add(t);
        }
      }
      if (!uniqueTags.isEmpty()) {
        resourceElement.appendChild(createGroupTagsElement(doc, uniqueTags));
      }
    }

    if (subResource.getThemeTags().size() > 0) {
      List<String> uniqueTags = new ArrayList<String>();
      for (String t : subResource.getThemeTags()) {
        if ((parentMRE.themeTags == null) || !parentMRE.themeTags.contains(t)) {
          uniqueTags.add(t);
        }
      }
      if (!uniqueTags.isEmpty()) {
        resourceElement.appendChild(createThemeTagsElement(doc, uniqueTags));
      }
    }
    return resourceElement;
  }

  private Document createXMLDocument() {
    try {
      Document doc = XMLUtilities.createDocument();
      Element modelRoot = doc.createElement("AliceModel");
      modelRoot.setAttribute("name", this.className);
      if ((this.attributionName != null) && (this.attributionName.length() > 0)) {
        modelRoot.setAttribute("creator", this.attributionName);
      }
      if ((this.attributionYear != null) && (this.attributionYear.length() > 0)) {
        modelRoot.setAttribute("creationYear", this.attributionYear);
      }
      if (this.isDeprecated) {
        modelRoot.setAttribute("deprecated", "TRUE");
      }
      if (this.placeOnGround) {
        modelRoot.setAttribute("placeOnGround", "TRUE");
      }
      doc.appendChild(modelRoot);
      if (this.boundingBoxes.get(this.className) == null) {
        AxisAlignedBox superBox = new AxisAlignedBox();
        for (Entry<String, AxisAlignedBox> entry : this.boundingBoxes.entrySet()) {
          superBox.union(entry.getValue());
        }
        this.boundingBoxes.put(this.className, superBox);
      }
      modelRoot.appendChild(createBoundingBoxElement(doc, this.boundingBoxes.get(this.className)));
      modelRoot.appendChild(createTagsElement(doc, this.tags));
      modelRoot.appendChild(createGroupTagsElement(doc, this.groupTags));
      modelRoot.appendChild(createThemeTagsElement(doc, this.themeTags));

      for (ModelSubResourceExporter subResource : this.subResources) {
        if (!subResource.getModelName().equalsIgnoreCase(this.className) && this.boundingBoxes.containsKey(subResource.getModelName())) {
          subResource.setBbox(this.boundingBoxes.get(subResource.getModelName()));
        }
        modelRoot.appendChild(createSubResourceElement(doc, subResource, this));
      }

      return doc;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static List<ModelClassData> POTENTIAL_MODEL_CLASS_DATA_OPTIONS = null;

  public static ModelClassData getBestClassDataForJointList(List<Tuple2<String, String>> jointList) {
    if (POTENTIAL_MODEL_CLASS_DATA_OPTIONS == null) {
      POTENTIAL_MODEL_CLASS_DATA_OPTIONS = new LinkedList<ModelClassData>();
      Field[] dataFields = AliceResourceClassUtilities.getFieldsOfType(ModelClassData.class, ModelClassData.class);
      for (Field f : dataFields) {
        ModelClassData data = null;
        try {
          Object o = f.get(null);
          if ((o != null) && (o instanceof ModelClassData)) {
            data = (ModelClassData) o;
          }
        } catch (Exception e) {
        }
        if (data != null) {
          POTENTIAL_MODEL_CLASS_DATA_OPTIONS.add(data);
        }
      }
    }

    int highScore = Integer.MIN_VALUE;
    ModelClassData bestFit = null;
    for (ModelClassData mcd : POTENTIAL_MODEL_CLASS_DATA_OPTIONS) {
      List<Tuple2<String, String>> modelDataJoints = getExistingJointIdPairs(mcd.superClass);
      int score = -Math.abs(modelDataJoints.size() - jointList.size());
      for (Tuple2<String, String> inputPair : jointList) {
        for (Tuple2<String, String> testPair : modelDataJoints) {
          if (inputPair.equals(testPair)) {
            score++;
            break;
          }
        }
      }
      if (score > highScore) {
        highScore = score;
        bestFit = mcd;
      }
    }
    return bestFit;
  }

  private static List<Tuple2<String, String>> getExistingJointIdPairs(Class<?> resourceClass) {
    List<Tuple2<String, String>> ids = new LinkedList<Tuple2<String, String>>();
    Field[] fields = resourceClass.getDeclaredFields();
    for (Field f : fields) {
      if (JointId.class.isAssignableFrom(f.getType())) {
        String fieldName = f.getName();
        String parentName = null;
        JointId fieldData = null;
        try {
          Object o = f.get(null);
          if ((o != null) && (o instanceof JointId)) {
            fieldData = (JointId) o;
          }
        } catch (Exception e) {
        }
        if ((fieldData != null) && (fieldData.getParent() != null)) {
          parentName = fieldData.getParent().toString();
        }

        ids.add(Tuple2.createInstance(fieldName, parentName));
      }
    }
    Class<?>[] interfaces = resourceClass.getInterfaces();
    for (Class<?> i : interfaces) {
      ids.addAll(getExistingJointIdPairs(i));
    }
    return ids;
  }

  private static List<String> getExistingJointIds(Class<?> resourceClass) {
    List<String> ids = new LinkedList<String>();
    Field[] fields = resourceClass.getDeclaredFields();
    for (Field f : fields) {
      if (JointId.class.isAssignableFrom(f.getType())) {
        String fieldName = f.getName();
        ids.add(fieldName);
      }
    }
    Class<?>[] interfaces = resourceClass.getInterfaces();
    for (Class<?> i : interfaces) {
      ids.addAll(getExistingJointIds(i));
    }
    return ids;
  }

  private Field getJointRootsField(Class<?> cls) {
    if (cls == null) {
      return null;
    }
    Field[] rootFields = AliceResourceClassUtilities.getFieldsOfType(cls, JointId[].class);
    if (rootFields.length == 1) {
      return rootFields[0];
    } else {
      Class[] interfaces = cls.getInterfaces();
      for (Class i : interfaces) {
        Field rootField = getJointRootsField(i);
        if (rootField != null) {
          return rootField;
        }
      }
    }
    return null;
  }

  private boolean needsToDefineRootsMethod(Class<?> cls) {
    if (cls == null) {
      return false;
    }
    Method[] methods = cls.getMethods();
    for (Method m : methods) {
      if (JointId[].class.isAssignableFrom(m.getReturnType())) {
        return true;
      }
    }
    Class[] interfaces = cls.getInterfaces();
    for (Class i : interfaces) {
      boolean needToDefineMethod = needsToDefineRootsMethod(i);
      if (needToDefineMethod) {
        return needToDefineMethod;
      }
    }
    return false;
  }

  public static String getAccessorMethodsForResourceClass(Class<? extends JointedModelResource> resourceClass) {
    StringBuilder sb = new StringBuilder();
    List<String> jointIds = getExistingJointIds(resourceClass);
    for (String id : jointIds) {
      sb.append("public Joint get" + AliceResourceClassUtilities.getAliceMethodNameForEnum(id) + "() {\n");
      sb.append("\t return org.lgna.story.Joint.getJoint( this, " + resourceClass.getCanonicalName() + "." + id + ");\n");
      sb.append("}\n");
    }
    return sb.toString();
  }

  private static String createResourceEnumName(ModelResourceExporter parentExporter, String modelName, String textureName) {
    if (modelName.equalsIgnoreCase(parentExporter.getClassName())) {
      return AliceResourceUtilities.makeEnumName(textureName);
    } else if (modelName.equalsIgnoreCase(textureName) || textureName.equalsIgnoreCase(AliceResourceUtilities.getDefaultTextureEnumName(modelName)) || textureName.equalsIgnoreCase(AliceResourceUtilities.makeEnumName(modelName))) {
      return AliceResourceUtilities.makeEnumName(modelName);
    } else {
      return AliceResourceUtilities.makeEnumName(modelName) + "_" + AliceResourceUtilities.makeEnumName(textureName);
    }
  }

  private static String createResourceEnumName(ModelResourceExporter parentExporter, ModelSubResourceExporter resource) {
    return createResourceEnumName(parentExporter, resource.getModelName(), resource.getTextureName());
  }

  public String createResourceEnumName(String modelName, String textureName) {
    return createResourceEnumName(this, modelName, textureName);
  }

  private boolean shouldSuppressJoint(String jointString) {
    if (this.jointIdsToSuppress.contains(jointString)) {
      return true;
    }
    if (isRootJoint(jointString)) {
      return true;
    }
    return false;
  }

  private String getArrayNameFromMapForJoint(String jointString, Map<String, List<String>> arrayEntries) {
    for (Entry<String, List<String>> entry : arrayEntries.entrySet()) {
      if (entry.getValue().contains(jointString)) {
        return entry.getKey();
      }
    }
    return null;
  }

  private boolean shouldSuppressJointInArray(String jointString, Map<String, List<String>> arrayEntries) {
    for (Entry<String, List<String>> entry : arrayEntries.entrySet()) {
      //We don't suppress the first element in the array so we can generate an accessor for it in Alice
      if (entry.getValue().contains(jointString)) {
        if (this.arraysToExposeFirstElementOf.contains(entry.getKey()) && (getArrayIndexForJoint(jointString) == 0)) {
          return false;
        } else {
          return true;
        }
      }
    }
    return false;
  }

  private boolean shouldHideJointInArray(String jointString, Map<String, List<String>> arrayEntries) {
    for (Entry<String, List<String>> entry : arrayEntries.entrySet()) {
      if (entry.getValue().contains(jointString)) {
        if (this.arraysToHideElementsOf.contains(entry.getKey())) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean shouldHideJointsOfArray(String arrayName) {
    if (this.arraysToHideElementsOf.contains(arrayName)) {
      return true;
    }
    return false;
  }

  private String getJointAccessMethodNameForArrayJoint(String jointName) {
    String arrayName = getArrayNameForJoint(jointName, null, null);
    return getAccessorMethodName(arrayName);
  }

  private String getAccessorMethodName(String arrayName) {
    return "get" + AliceResourceUtilities.enumToCamelCase(arrayName);
  }

  //If a parent interface has declared an accessor for a given field, return true
  // otherwisse return false
  private boolean needsAccessorMethodForFieldName(ModelClassData classData, String fieldName) {
    String fieldAccessorMethodName = getAccessorMethodName(fieldName);
    Method m = null;
    try {
      m = classData.superClass.getMethod(fieldAccessorMethodName);
      if ((m != null) && m.getDeclaringClass().isInterface()) {
        return true;
      }
    } catch (NoSuchMethodException me) {
    }
    return false;
  }

  private List<Method> getMandatoryMethods(Class<?> superClass, Class<?> returnType) {
    List<Method> methods = new LinkedList<Method>();
    for (Method method : superClass.getMethods()) {
      if (returnType.isAssignableFrom(method.getReturnType()) && method.getDeclaringClass().isInterface()) {
        methods.add(method);
      }
    }
    return methods;
  }

  private List<String> getMandatoryJointArrayNames(Class<?> superClass) {
    List<String> methodNames = new LinkedList<String>();
    for (Method method : getMandatoryMethods(superClass, JointId[].class)) {
      methodNames.add(method.getName());
    }
    List<String> arrayNames = new LinkedList<String>();
    for (String methodName : methodNames) {
      int index = methodName.indexOf("get");
      if ((index == 0)) {
        if (!methodName.equals(ROOT_IDS_METHOD_NAME)) {
          String newName = methodName.substring(3);
          int arrayIndex = newName.indexOf("Array");
          if (arrayIndex != -1) {
            newName = newName.substring(0, arrayIndex);
          }
          newName = AliceResourceUtilities.makeEnumName(newName);
          arrayNames.add(newName);
        }
      } else {
        System.err.println("FROM " + superClass + ": UNABLE TO CONVERT " + methodName + " INTO AN ARRAY NAME.");
      }

    }
    return arrayNames;
  }

  //  We don't support defining poses ahead of time in classes.
  //  All poses are either declared as functions in the interface to be implemented by the generated code
  //  Or they're defined by the model resource and added as part of the generative process
  //  private List<String> getAlreadyDeclaredPoseNames( Class<?> superClass ) {
  //    List<String> fieldNames = new LinkedList<String>();
  //    for( Field field : ReflectionUtilities.getPublicStaticFinalFields( superClass, org.lgna.story.Pose.class ) ) {
  //      fieldNames.add( field.getName() );
  //    }
  //    return fieldNames;
  //  }

  private List<String> getMandatoryPoseNames(Class<?> superClass) {
    List<String> methodNames = new LinkedList<String>();
    for (Method method : getMandatoryMethods(superClass, Pose.class)) {
      methodNames.add(method.getName());
    }
    List<String> poseNames = new LinkedList<String>();
    for (String methodName : methodNames) {
      int index = methodName.indexOf("get");
      if ((index == 0)) {
        String newName = methodName.substring(3);
        int arrayIndex = newName.indexOf("Pose");
        if (arrayIndex != -1) {
          newName = newName.substring(0, arrayIndex);
        }
        newName = AliceResourceUtilities.makeEnumName(newName);
        poseNames.add(newName);
      } else {
        System.err.println("FROM " + superClass + ": UNABLE TO CONVERT " + methodName + " INTO POSE NAME.");
      }

    }
    return poseNames;
  }

  private Class getPoseBuilderTypeForSuperClass(Class<?> superClass) {
    if (FlyerResource.class.isAssignableFrom(superClass)) {
      return FlyerPoseBuilder.class;
    } else if (BipedResource.class.isAssignableFrom(superClass)) {
      return BipedPoseBuilder.class;
    } else if (QuadrupedResource.class.isAssignableFrom(superClass)) {
      return QuadrupedPoseBuilder.class;
    } else if (SwimmerResource.class.isAssignableFrom(superClass)) {
      return SwimmerPoseBuilder.class;
    } else if (SlithererResource.class.isAssignableFrom(superClass)) {
      return SlithererPoseBuilder.class;
    }
    return JointedModelPoseBuilder.class;
  }

  private Class getPoseTypeForSuperClass(Class<?> superClass) {
    if (FlyerResource.class.isAssignableFrom(superClass)) {
      return FlyerPose.class;
    } else if (BipedResource.class.isAssignableFrom(superClass)) {
      return BipedPose.class;
    } else if (QuadrupedResource.class.isAssignableFrom(superClass)) {
      return QuadrupedPose.class;
    } else if (SwimmerResource.class.isAssignableFrom(superClass)) {
      return SwimmerPose.class;
    } else if (SlithererResource.class.isAssignableFrom(superClass)) {
      return SlithererPose.class;
    }
    return JointedModelPose.class;

  }

  private List<String> getAlreadyDeclaredJointArrayNames(Class<?> superClass) {
    List<String> fieldNames = new LinkedList<String>();
    for (Field field : ReflectionUtilities.getPublicStaticFinalFields(superClass, JointArrayId.class)) {
      fieldNames.add(field.getName());
    }
    return fieldNames;
  }

  public String createJavaCode() throws DataFormatException {
    StringBuilder sb = new StringBuilder();

    sb.append(JavaCodeUtilities.getCopyrightComment());
    sb.append(JavaCodeUtilities.LINE_RETURN);
    sb.append("package " + this.classData.packageString + ";" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN);
    sb.append("import org.lgna.project.annotations.*;" + JavaCodeUtilities.LINE_RETURN);
    sb.append("import org.lgna.story.implementation.JointIdTransformationPair;" + JavaCodeUtilities.LINE_RETURN);
    sb.append("import org.lgna.story.Orientation;" + JavaCodeUtilities.LINE_RETURN);
    sb.append("import org.lgna.story.Position;" + JavaCodeUtilities.LINE_RETURN);
    sb.append("import org.lgna.story.resources.ImplementationAndVisualType;" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN);
    if (this.isDeprecated) {
      sb.append("@Deprecated" + JavaCodeUtilities.LINE_RETURN);
    }
    sb.append("public enum " + this.getJavaClassName() + " implements " + this.classData.superClass.getCanonicalName() + " {" + JavaCodeUtilities.LINE_RETURN);
    assert this.subResources.size() > 0;
    boolean isFirst = true;
    for (int i = 0; i < this.subResources.size(); i++) {
      ModelSubResourceExporter resource = this.subResources.get(i);
      String resourceEnumName = createResourceEnumName(this, resource);
      if (isValidEnumName(resource.getModelName(), resourceEnumName)) {
        if (!isFirst) {
          sb.append("," + JavaCodeUtilities.LINE_RETURN);
        }
        String typeString = "";
        if (!resource.getTypeString().equals(ImplementationAndVisualType.ALICE.toString())) {
          typeString = "( ImplementationAndVisualType." + resource.getTypeString() + " )";
        }
        sb.append("\t" + resourceEnumName + typeString);
        isFirst = false;
      } else {
        System.out.println("SKIPPING ENUM NAME: " + resourceEnumName);
      }
    }
    sb.append(";" + JavaCodeUtilities.LINE_RETURN);
    List<String> existingIds = getExistingJointIds(this.classData.superClass);
    boolean addedRoots = false;
    List<Tuple2<String, String>> trimmedSkeleton = makeCodeReadyTree(this.jointList);
    if (trimmedSkeleton != null) {
      Map<String, List<String>> arrayEntries;
      if (this.enableArraySupport) {
        arrayEntries = getArrayEntriesFromJointList(trimmedSkeleton, this.customArrayNameMap, this.jointIdsToSuppress, this.arrayNamesToSkip);
      } else {
        arrayEntries = new HashMap<String, List<String>>();
      }

      Map<String, Map<String, AffineMatrix4x4>> poseEntries = new HashMap<String, Map<String, AffineMatrix4x4>>(this.poses);
      List<String> rootJoints = new LinkedList<String>();
      sb.append(JavaCodeUtilities.LINE_RETURN);
      for (Tuple2<String, String> entry : trimmedSkeleton) {
        String jointString = entry.getA();
        String parentString = entry.getB();
        if (existingIds.contains(jointString)) {
          continue;
        }
        if (!shouldHideJointInArray(jointString, arrayEntries)) {
          if ((parentString == null) || (parentString.length() == 0)) {
            parentString = "null";
            rootJoints.add(jointString);
            addedRoots = true;
          }
          if (shouldSuppressJoint(jointString) || shouldSuppressJointInArray(jointString, arrayEntries)) {
            sb.append("@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)" + JavaCodeUtilities.LINE_RETURN);
          } else {
            String arrayName = getArrayNameFromMapForJoint(jointString, arrayEntries);
            if (arrayName != null) {
              sb.append("@FieldTemplate(visibility=Visibility.PRIME_TIME, methodNameHint=\"" + getJointAccessMethodNameForArrayJoint(jointString) + "\")" + JavaCodeUtilities.LINE_RETURN);
            } else {
              sb.append("@FieldTemplate(visibility=Visibility.PRIME_TIME)" + JavaCodeUtilities.LINE_RETURN);
            }

          }
          sb.append("\tpublic static final org.lgna.story.resources.JointId " + jointString + " = new org.lgna.story.resources.JointId( " + parentString + ", " + this.getJavaClassName() + ".class );" + JavaCodeUtilities.LINE_RETURN);
        }
      }

      if (addedRoots) {
        sb.append("\n@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )");
        sb.append("\n\tpublic static final org.lgna.story.resources.JointId[] " + ROOT_IDS_FIELD_NAME + " = { ");
        for (int i = 0; i < rootJoints.size(); i++) {
          sb.append(rootJoints.get(i));
          if (i < (rootJoints.size() - 1)) {
            sb.append(", ");
          }
        }
        sb.append(" };" + JavaCodeUtilities.LINE_RETURN);
      }
      //Handle pose code
      List<String> mandatoryPoseNames = getMandatoryPoseNames(classData.superClass);
      if (!poseEntries.isEmpty() || (mandatoryPoseNames.size() != 0)) {
        for (String mandatoryPose : mandatoryPoseNames) {
          if (!poseEntries.containsKey(mandatoryPose)) {
            throw new DataFormatException("Missing pose definition for " + mandatoryPose + " on class " + classData.superClass);
          }
        }
        for (Entry<String, Map<String, AffineMatrix4x4>> poseEntry : poseEntries.entrySet()) {
          Map<String, AffineMatrix4x4> poseData = poseEntry.getValue();
          if (poseData.isEmpty()) {
            throw new DataFormatException("No pose data for " + poseEntry.getKey() + " on class " + classData.superClass);
          }
          String fullPoseName = poseEntry.getKey() + "_POSE";

          boolean needsAccessor = needsAccessorMethodForFieldName(classData, fullPoseName);

          //If an accessor is needed, add a "COMPLETELY_HIDDEN" annotation.
          // The accessor is used to retrieve the pose via a parent class and therefore the pose itself is essentially already handled
          // If there is no accessor, then we want the code generation system to create an Alice level accessor at runtime (which this annotation prevents)
          if (needsAccessor) {
            sb.append("\n\t@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )");
          }

          //          Class poseType = getPoseTypeForSuperClass( classData.superClass );
          Class poseType = JointedModelPose.class;
          Class poseBuilderType = getPoseBuilderTypeForSuperClass(classData.superClass);
          String poseTypeString = poseType.getName();
          sb.append("\n\tpublic static final " + poseTypeString + " " + fullPoseName + " = new " + poseTypeString + "( ");
          sb.append(JavaCodeUtilities.LINE_RETURN);
          int count = 0;
          for (Entry<String, AffineMatrix4x4> poseDataEntry : poseData.entrySet()) {
            count++;
            UnitQuaternion quat = poseDataEntry.getValue().orientation.createUnitQuaternion();
            Point3 pos = poseDataEntry.getValue().translation;
            sb.append("\t\tnew JointIdTransformationPair( " + poseDataEntry.getKey() + ", new Orientation(" + quat.x + ", " + quat.y + ", " + quat.z + ", " + quat.w + "), new Position(" + pos.x + ", " + pos.y + ", " + pos.z + ") )");
            if (count != poseData.size()) {
              sb.append(",");
            }
            sb.append(JavaCodeUtilities.LINE_RETURN);
          }
          sb.append("\t);" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN);
          if (needsAccessor) {
            String poseAccessorName = getAccessorMethodName(fullPoseName);
            sb.append("\tpublic " + poseType.getName() + " " + poseAccessorName + "(){" + JavaCodeUtilities.LINE_RETURN);
            sb.append("\t\treturn " + this.getJavaClassName() + "." + fullPoseName + ";" + JavaCodeUtilities.LINE_RETURN);
            sb.append("\t}" + JavaCodeUtilities.LINE_RETURN);
          }
        }
      }
      //Handle array code
      List<String> mandatoryArrayNames = getMandatoryJointArrayNames(classData.superClass);
      List<String> declaredArrays = getAlreadyDeclaredJointArrayNames(classData.superClass);
      if (!arrayEntries.isEmpty() || (mandatoryArrayNames.size() != 0)) {
        //Loop through and remove any existing arrays from the mandatory array list
        // This should leave only the mandatory arrays that need an empty list defined
        for (Entry<String, List<String>> arrayEntry : arrayEntries.entrySet()) {
          if (mandatoryArrayNames.contains(arrayEntry.getKey())) {
            mandatoryArrayNames.remove(arrayEntry.getKey());
          }
        }
        for (String mandatoryArray : mandatoryArrayNames) {
          arrayEntries.put(mandatoryArray, new LinkedList<String>());
        }
        for (Entry<String, List<String>> arrayEntry : arrayEntries.entrySet()) {
          List<String> arrayElements = arrayEntry.getValue();
          String fullArrayName = arrayEntry.getKey() + "_ARRAY";

          if (declaredArrays.contains(fullArrayName) || declaredArrays.contains(arrayEntry.getKey())) {
            //If the array is already declared, skip it and trust that the previous declaration will capture the data
            continue;
          }

          boolean needsAccessor = needsAccessorMethodForFieldName(classData, fullArrayName);

          //If an accessor is needed, add a "COMPLETELY_HIDDEN" annotation.
          // The accessor is used to retrieve the array via a parent class and therefore the array itself is essentially already handled
          // If there is no accessor, then we want the code generation system to create an Alice level accessor at runtime (which this annotation prevents)
          if (needsAccessor) {
            sb.append("\n\t@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )");
          }

          //If the array is one in the "hide all the elements of this array" list, then declare it as an arrayId rather than an array of joint ids
          if (this.arraysToHideElementsOf.contains(fullArrayName) || this.arraysToHideElementsOf.contains(arrayEntry.getKey())) {
            String firstEntry = arrayElements.get(0);
            String parentString = "null";
            for (Tuple2<String, String> entry : trimmedSkeleton) {
              if (entry.getA().equals(firstEntry)) {
                parentString = entry.getB();
                break;
              }
            }
            sb.append("@FieldTemplate(visibility=Visibility.PRIME_TIME)" + JavaCodeUtilities.LINE_RETURN);
            sb.append("\tpublic static final org.lgna.story.resources.JointArrayId " + fullArrayName + " = new org.lgna.story.resources.JointArrayId( \"" + arrayEntry.getKey() + "\", " + parentString + ", " + this.getJavaClassName() + ".class );" + JavaCodeUtilities.LINE_RETURN);
          } else {
            sb.append("\n\tpublic static final org.lgna.story.resources.JointId[] " + fullArrayName + " = { ");
            for (int i = 0; i < arrayElements.size(); i++) {
              sb.append(arrayElements.get(i));
              if (i < (arrayElements.size() - 1)) {
                sb.append(", ");
              }
            }
            sb.append(" };" + JavaCodeUtilities.LINE_RETURN);
          }
          if (needsAccessor) {
            String arrayAccessorName = getAccessorMethodName(fullArrayName);
            sb.append("\tpublic org.lgna.story.resources.JointId[] " + arrayAccessorName + "(){" + JavaCodeUtilities.LINE_RETURN);
            sb.append("\t\treturn " + this.getJavaClassName() + "." + fullArrayName + ";" + JavaCodeUtilities.LINE_RETURN);
            sb.append("\t}" + JavaCodeUtilities.LINE_RETURN);
          }
        }
      }
    }
    sb.append(JavaCodeUtilities.LINE_RETURN);
    sb.append("\tprivate final ImplementationAndVisualType resourceType;" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\tprivate " + this.getJavaClassName() + "() {" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t\tthis( ImplementationAndVisualType.ALICE );" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t}" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN);
    sb.append("\tprivate " + this.getJavaClassName() + "( ImplementationAndVisualType resourceType ) {" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t\tthis.resourceType = resourceType;" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t}" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN);
    if (needsToDefineRootsMethod(this.classData.superClass)) {
      sb.append("\tpublic org.lgna.story.resources.JointId[] " + ROOT_IDS_METHOD_NAME + "(){" + JavaCodeUtilities.LINE_RETURN);
      if (addedRoots) {
        sb.append("\t\treturn " + this.getJavaClassName() + "." + ROOT_IDS_FIELD_NAME + ";" + JavaCodeUtilities.LINE_RETURN);
      } else {
        Field rootsField = getJointRootsField(this.classData.superClass);
        if (rootsField != null) {
          sb.append("\t\treturn " + rootsField.getDeclaringClass().getCanonicalName() + "." + rootsField.getName() + ";" + JavaCodeUtilities.LINE_RETURN);
        } else {
          sb.append("\t\treturn new org.lgna.story.resources.JointId[0];" + JavaCodeUtilities.LINE_RETURN);
        }
      }
      sb.append("\t}" + JavaCodeUtilities.LINE_RETURN);
    }
    sb.append("\n\tpublic org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t\treturn this.resourceType.getFactory( this );" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t}" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\tpublic " + this.classData.implementationClass.getCanonicalName() + " createImplementation( " + this.classData.abstractionClass.getCanonicalName() + " abstraction ) {" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t\treturn new " + this.classData.implementationClass.getCanonicalName() + "( abstraction, this.resourceType.getFactory( this ) );" + JavaCodeUtilities.LINE_RETURN);
    sb.append("\t}" + JavaCodeUtilities.LINE_RETURN);
    sb.append("}" + JavaCodeUtilities.LINE_RETURN);

    return sb.toString();
  }

  private void add(File source, JarOutputStream target, String destPathPrefix, boolean recursive) throws IOException {
    if (destPathPrefix == null) {
      destPathPrefix = "";
    }
    if ((destPathPrefix != null) && (destPathPrefix.length() > 0)) {
      destPathPrefix = destPathPrefix.replace("\\", "/");
      if (!destPathPrefix.endsWith("/")) {
        destPathPrefix += "/";
      }
      if (destPathPrefix.startsWith("/") || destPathPrefix.startsWith("\\")) {
        destPathPrefix = destPathPrefix.substring(1);
      }
    }

    String root = source.getAbsolutePath().replace("\\", "/") + "/";
    this.add(source, target, root, destPathPrefix, recursive);
  }

  private void add(File source, JarOutputStream target, String root, String destPathPrefix, boolean recursive) throws IOException {
    BufferedInputStream in = null;
    try {
      if (source.isDirectory()) {
        String name = source.getPath().replace("\\", "/");
        name = name.replace("//", "/");
        if (name.length() > 0) {
          if (!name.endsWith("/")) {
            name += "/";
          }
          name = name.substring(root.length());
          if (name.startsWith("/")) {
            name = name.substring(1);
          }
          if (name.length() > 0) {
            name = destPathPrefix + name;
            JarEntry entry = new JarEntry(name);
            entry.setTime(source.lastModified());
            try {
              System.out.println("   Adding: " + name);
              target.putNextEntry(entry);
              target.closeEntry();
            } catch (ZipException ze) {
              System.err.println(ze.getMessage());
            }
          }
        }
        for (File nestedFile : source.listFiles()) {
          if (!nestedFile.isDirectory() || recursive) {
            add(nestedFile, target, root, destPathPrefix, recursive);
          }
        }
        return;
      }

      String entryName = source.getPath().replace("\\", "/");
      entryName = entryName.substring(root.length());
      if (entryName.startsWith("/") || entryName.startsWith("\\")) {
        entryName = entryName.substring(1);
      }
      entryName = destPathPrefix + entryName;
      JarEntry entry = new JarEntry(entryName);
      entry.setTime(source.lastModified());
      target.putNextEntry(entry);
      in = new BufferedInputStream(new FileInputStream(source));

      byte[] buffer = new byte[1024];
      while (true) {
        int count = in.read(buffer);
        if (count == -1) {
          break;
        }
        target.write(buffer, 0, count);
      }
      target.closeEntry();
    } finally {
      if (in != null) {
        in.close();
      }
    }
  }

  private String getJavaClassName() {
    return this.className + AliceResourceClassUtilities.RESOURCE_SUFFIX;
  }

  private File getJavaCodeDir(String root) {
    String packageDirectory = JavaCodeUtilities.getDirectoryStringForPackage(this.classData.packageString);
    return new File(root + packageDirectory);
  }

  private File getJavaClassFile(String root) {
    String filename = JavaCodeUtilities.getDirectoryStringForPackage(this.classData.packageString) + this.getJavaClassName() + ".class";
    return new File(root + filename);
  }

  private File getJavaFile(String root) {
    String filename = JavaCodeUtilities.getDirectoryStringForPackage(this.classData.packageString) + this.getJavaClassName() + ".java";
    return new File(root + filename);
  }

  private File createJavaCode(String root) throws DataFormatException {
    String packageDirectory = JavaCodeUtilities.getDirectoryStringForPackage(this.classData.packageString);
    System.out.println(packageDirectory);
    String javaCode = createJavaCode();
    System.out.println(javaCode);
    System.out.println(System.getProperty("java.class.path"));
    File javaFile = getJavaFile(root);
    TextFileUtilities.write(javaFile, javaCode);
    return javaFile;
  }

  private String createXMLString() {
    Document doc = this.createXMLDocument();
    if (doc != null) {
      try {
        TransformerFactory transfac = TransformerFactory.newInstance();
        transfac.setAttribute("indent-number", new Integer(4));
        Transformer trans = transfac.newTransformer();
        //                  trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String xmlString = sw.toString();

        return xmlString;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private File getXMLFile(String root) {
    if (!root.endsWith("/") && !root.endsWith("\\")) {
      root += "/";
    }
    String resourceDirectory = root + JavaCodeUtilities.getDirectoryStringForPackage(this.classData.packageString) + ModelResourceIoUtilities.getResourceSubDirWithSeparator("");
    File xmlFile = new File(resourceDirectory, this.className + ".xml");
    return xmlFile;
  }

  private File createXMLFile(String root, boolean forceRebuild) {
    File outputFile = getXMLFile(root);
    try {
      if (!forceRebuild && (this.xmlFile != null) && this.xmlFile.exists()) {
        if (!outputFile.exists()) {
          FileUtilities.createParentDirectoriesIfNecessary(outputFile);
          outputFile.createNewFile();
        }
        FileUtilities.copyFile(this.xmlFile, outputFile);
        return outputFile;
      } else {
        //This path does not indent the xml
        //            Document doc = this.createXMLDocument();
        //            XMLUtilities.write(doc, outputFile);

        //This path does indenting
        String xmlString = this.createXMLString();
        if (!outputFile.exists()) {
          FileUtilities.createParentDirectoriesIfNecessary(outputFile);
          outputFile.createNewFile();
        }
        FileWriter fw = new FileWriter(outputFile);
        fw.write(xmlString);
        fw.close();

        return outputFile;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  private File saveImageToFile(String fileName, Image image) {
    try {
      int width = image.getWidth(null);
      int height = image.getHeight(null);
      if ((width == 0) || (height == 0)) {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
    File outputFile = new File(fileName);
    try {
      if (!outputFile.exists()) {
        FileUtilities.createParentDirectoriesIfNecessary(outputFile);
        outputFile.createNewFile();
      }
      ImageUtilities.write(outputFile, image);
      return outputFile;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getThumbnailPath(String rootPath, String thumbnailName) {
    if (!rootPath.endsWith("/") && !rootPath.endsWith("\\")) {
      rootPath += "/";
    }
    String resourceDirectory = rootPath + JavaCodeUtilities.getDirectoryStringForPackage(this.classData.packageString) + ModelResourceIoUtilities.getResourceSubDirWithSeparator(this.className);
    return resourceDirectory + thumbnailName;
  }

  public static BufferedImage createClassThumb(BufferedImage imgSrc) {
    //    ColorConvertOp colorConvert =
    //        new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null );
    //    if( imgSrc == null ) {
    //      System.out.println( "NULL!" );
    //    }
    //    try {
    //      colorConvert.filter( imgSrc, imgSrc );
    //    } catch( NullPointerException e ) {
    //      e.printStackTrace();
    //      throw e;
    //    }
    return imgSrc;
  }

  private List<File> saveThumbnailsToDir(String root) {
    List<File> thumbnailFiles = new LinkedList<File>();
    List<String> thumbnailsCreated = new LinkedList<String>();
    if ((this.existingThumbnails != null) && !this.existingThumbnails.isEmpty()) {
      for (Entry<String, File> entry : this.existingThumbnails.entrySet()) {
        if (entry.getValue().exists()) {
          thumbnailFiles.add(entry.getValue());
          thumbnailsCreated.add(entry.getKey());
        } else {
          System.err.println("FAILED TO FIND THUMBNAIL FILE '" + entry.getValue() + "'");
          return null;
        }
      }
    }
    for (Entry<ModelSubResourceExporter, Image> entry : this.thumbnails.entrySet()) {
      if (!thumbnailsCreated.contains(entry.getKey())) {
        String thumbnailName = AliceResourceUtilities.getThumbnailResourceFileName(entry.getKey().getModelName(), entry.getKey().getTextureName());
        File f = saveImageToFile(getThumbnailPath(root, thumbnailName), entry.getValue());
        if (f != null) {
          thumbnailsCreated.add(thumbnailName);
          thumbnailFiles.add(f);
        }
      }
    }
    if (this.subResources.size() == 0) {
      System.err.println("NO SUB RESOURCES ON " + this.resourceName);
    }
    ModelSubResourceExporter firstSubResource = this.subResources.get(0);
    String firstThumbName = AliceResourceUtilities.getThumbnailResourceFileName(firstSubResource.getModelName(), firstSubResource.getTextureName());
    String classThumbName = AliceResourceUtilities.getThumbnailResourceFileName(this.getClassName(), null);
    File firstThumbFile = new File(getThumbnailPath(root, firstThumbName));
    File classThumbFile = new File(getThumbnailPath(root, classThumbName));

    //TODO: Handle this error in a better way
    try {
      BufferedImage classThumb = createClassThumb(ImageUtilities.read(firstThumbFile));
      ImageUtilities.write(classThumbFile, classThumb);
      thumbnailFiles.add(classThumbFile);
    } catch (IOException ioe) {
      ioe.printStackTrace();
      System.err.println("Error reading thumbnail " + firstThumbFile + ", Deleting it...");
      firstThumbFile.delete();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error reading thumbnail " + firstThumbFile + ", Deleting it...");
      firstThumbFile.delete();
    }

    return thumbnailFiles;
  }

  public boolean isValidEnumName(String modelName, String enumName) {
    if (this.forcedEnumNamesMap.containsKey(modelName)) {
      List<String> validEnums = this.forcedEnumNamesMap.get(modelName);
      for (String e : validEnums) {
        String otherToCheck = modelName.toUpperCase() + "_" + e;
        if (e.equalsIgnoreCase(enumName) || otherToCheck.equalsIgnoreCase(enumName)) {
          return true;
        }
      }
      return false;
    }
    //If we aren't forcing any enum names then all enum names are valid
    if (this.forcedOverridingEnumNames.isEmpty()) {
      return true;
    }
    for (String e : this.forcedOverridingEnumNames) {
      if (e.equalsIgnoreCase(enumName)) {
        return true;
      }
    }
    return false;
  }

  public void setArrayNamesToSkip(String[] arrayNamesToSkip) {
    this.arrayNamesToSkip = arrayNamesToSkip;
  }

  public void addCustomArrayName(String arrayName, String customName) {
    this.customArrayNameMap.put(arrayName, customName);
  }

  public void addCustomArrayNames(Map<String, String> customArrayNames) {
    for (Entry<String, String> entry : customArrayNames.entrySet()) {
      this.customArrayNameMap.put(entry.getKey(), entry.getValue());
    }
  }

  public void addForcedEnumNames(String resourceName, List<String> enumNames) {
    if ((enumNames != null) && (enumNames.size() > 0)) {
      if (resourceName == null) {
        this.forcedOverridingEnumNames.addAll(enumNames);
      } else {
        List<String> nameList;
        if (!this.forcedEnumNamesMap.containsKey(resourceName)) {
          nameList = new ArrayList<String>();
          this.forcedEnumNamesMap.put(resourceName, nameList);
        } else {
          nameList = this.forcedEnumNamesMap.get(resourceName);
        }
        nameList.addAll(enumNames);
      }
    }
  }

  /*
   * public Joint getRightWrist() {
   * return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_WRIST );
   * }
   */

  public static String getJointAccessCodeForClass(Class<?> resourceClass) {
    List<String> ids = getExistingJointIds(resourceClass);
    StringBuilder sb = new StringBuilder();
    for (String id : ids) {
      sb.append("public org.lgna.story.Joint get" + AliceResourceClassUtilities.getAliceMethodNameForEnum(id) + "() {\n");
      sb.append("\treturn org.lgna.story.Joint.getJoint( this, " + resourceClass.getName() + "." + id + " );\n");
      sb.append("}\n");
    }

    return sb.toString();

  }

  public static void main(String[] args) {
    System.out.println(getJointAccessCodeForClass(BipedResource.class));
  }
}
