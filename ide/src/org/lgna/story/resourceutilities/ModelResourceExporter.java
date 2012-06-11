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
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lgna.project.License;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.w3c.dom.Document;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

public class ModelResourceExporter {

	private static String COPYRIGHT_COMMENT = null;
	private static final String ROOT_IDS_FIELD_NAME = "JOINT_ID_ROOTS";
	
	public static final String RESOURCE_SUB_DIR = "";
	
	public static String getResourceSubDirWithSeparator(String className) {
		String classDir = className != null && className.length() > 0 ? className.toLowerCase()+"/" : "";
		if (RESOURCE_SUB_DIR == null || RESOURCE_SUB_DIR.length() == 0) {
			return classDir;
		}
		else {
			return RESOURCE_SUB_DIR + "/"+classDir;
		}
	}
	
	private static String getCopyrightComment()
	{
		if (COPYRIGHT_COMMENT == null)
		{
			String copyright = License.TEXT.replace("\n", "\n * ");
			COPYRIGHT_COMMENT = "/*\n * " + copyright + "\n */\n";
		}
		return COPYRIGHT_COMMENT;
	}
	
	private class NamedFile
	{
		public String name;
		public File file;
		public NamedFile(String name, File file)
		{
			this.name = name;
			this.file = file;
		}
	}
	
	private String resourceName;
	private String className;
	private List<String> tags = new LinkedList<String>();
	private Map<String, AxisAlignedBox> boundingBoxes = new HashMap<String, AxisAlignedBox>();
	private File xmlFile;
	private File javaFile;
	private Map<ModelSubResourceExporter, Image> thumbnails = new HashMap<ModelSubResourceExporter, Image>();
	private Map<String, File> existingThumbnails = null;
	private List<ModelSubResourceExporter> subResources = new LinkedList<ModelSubResourceExporter>();
	private boolean isSims = false;
	private boolean hasNewData = false;
	private boolean forceRebuildCode = false;
	private boolean forceRebuildXML = false;
	private Date lastEdited = null;
	
	private String attributionName;
	private String attributionYear;
	
	private Class<?> jointAndVisualFactory = org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory.class;
	
	private ModelClassData classData;
	private List<Tuple2<String, String>> jointList;
	
	public ModelResourceExporter(String className) 
	{
		this.className = className;
		if (Character.isLowerCase(this.className.charAt(0)))
		{
			this.className = this.className.substring(0, 1).toUpperCase() + this.className.substring(1);
		}
	}
	
	public ModelResourceExporter(String className, String resourceName)
	{
		this(className);
		this.resourceName = resourceName;
	}
	
	public ModelResourceExporter(String className, ModelClassData classData)
	{
		this(className);
		this.classData = classData;
	}
	
	public ModelResourceExporter(String className, String resourceName, ModelClassData classData)
	{
		this(className, resourceName);
		this.classData = classData;
	}
	
	public ModelResourceExporter(String className, ModelClassData classData, Class<?> jointAndVisualFactoryClass)
	{
		this(className, classData);
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}
	
	public ModelResourceExporter(String className, String resourceName, ModelClassData classData, Class<?> jointAndVisualFactoryClass)
	{
		this(className, resourceName, classData);
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}
	
	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}
	
	public void setHasNewData(boolean hasNewData) {
		this.hasNewData = hasNewData;
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
		}
		else if (date.after(this.lastEdited)) {
			this.lastEdited = date;
		}
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
	
	public String getResourceName()
	{
		return this.resourceName;
	}
	
	public ModelClassData getClassData()
	{
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
	
	public void setJointAndVisualFactory(Class<?> jointAndVisualFactoryClass)
	{
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}

	public boolean hasJointMap() {
		return this.jointList != null;
	}

	public List<Tuple2<String, String>> getJointMap() {
		return this.jointList;
	}

	public void setJointMap(List<Tuple2<String, String>> jointList)
	{
		this.jointList = jointList;
//		if (this.classData == null)
//		{
//			this.classData = ModelResourceExporter.getBestClassDataForJointList(jointList);
//		}
	}
	
	public void addSubResource(ModelSubResourceExporter subResource) {
		this.subResources.add(subResource);
	}
	
	public void addResource(String modelName, String textureName) {
		this.addSubResource(new ModelSubResourceExporter(modelName, textureName));
	}
	
	public void addSubResourceTags(String modelName, String textureName, String... tags) {
		if (tags != null && tags.length > 0) {
			for (ModelSubResourceExporter subResource : this.subResources) {
				if (subResource.getModelName().equalsIgnoreCase(modelName)) {
					if (textureName == null || subResource.getTextureName().equalsIgnoreCase(textureName)) {
						subResource.addTags(tags);
					}
				}
			}
		}
	}
	
	public void addAttribution(String name, String year)
	{
		this.attributionName = name;
		this.attributionYear = year;
	}
	
	public void addTags(String... tags) {
		if (tags != null) {
			for (String s : tags){
				this.tags.add(s);
			}
		}
	}
	
	public boolean isSims() {
		return this.isSims;
	}
	
	public void setIsSims(boolean isSims) {
		this.isSims = isSims;
	}
	
	public String getClassName()
	{
		return this.className;
	}
	
	public String getPackageString()
	{
		return this.classData.packageString;
	}
	
	public void setBoundingBox( String modelName, AxisAlignedBox boundingBox )
	{
		this.boundingBoxes.put(modelName, boundingBox);
	}
	
	public void addThumbnail( String modelName, String textureName, Image thumbnail )
	{
		this.thumbnails.put(new ModelSubResourceExporter(modelName, textureName), thumbnail);
	}

	public void addExistingThumbnail(String name, File thumbnailFile) {
		if (thumbnailFile != null && thumbnailFile.exists()) {
			if (this.existingThumbnails == null) {
				this.existingThumbnails = new HashMap<String, File>();
			}
			this.existingThumbnails.put(name, thumbnailFile);
		}
		else {
			System.err.println("FAILED TO ADDED THUMBAIL: "+thumbnailFile+" does not exist.");
		}
	}
	
	public void setXMLFile(File xmlFile)
	{
		this.xmlFile = xmlFile;
	}

	public void setJavaFile(File javaFile)
	{
		this.javaFile = javaFile;
	}
	
	private static org.w3c.dom.Element createBoundingBoxElement(Document doc, AxisAlignedBox bbox)
	{
		org.w3c.dom.Element bboxElement = doc.createElement("BoundingBox");
		org.w3c.dom.Element minElement = doc.createElement("Min");
		minElement.setAttribute("x", Double.toString(bbox.getXMinimum()));
		minElement.setAttribute("y", Double.toString(bbox.getYMinimum()));
		minElement.setAttribute("z", Double.toString(bbox.getZMinimum()));
		org.w3c.dom.Element maxElement = doc.createElement("Max");
		maxElement.setAttribute("x", Double.toString(bbox.getXMaximum()));
		maxElement.setAttribute("y", Double.toString(bbox.getYMaximum()));
		maxElement.setAttribute("z", Double.toString(bbox.getZMaximum()));
		
		bboxElement.appendChild(minElement);
		bboxElement.appendChild(maxElement);
		
		return bboxElement;
	}
	
	private static org.w3c.dom.Element createTagsElement(Document doc, List<String> tagList)
	{
		org.w3c.dom.Element tagsElement = doc.createElement("Tags");
		for (String tag : tagList) {
			org.w3c.dom.Element tagElement = doc.createElement("Tag");
			tagElement.setTextContent(tag);
			tagsElement.appendChild(tagElement);
		}
		return tagsElement;
	}
	
	private static org.w3c.dom.Element createSubResourceElement(Document doc, ModelSubResourceExporter subResource, ModelResourceExporter parentMRE)
	{
		org.w3c.dom.Element resourceElement = doc.createElement("Resource");
		resourceElement.setAttribute("name", AliceResourceUtilties.makeEnumName(subResource.getTextureName()));
		if (subResource.getModelName() != null) {
			resourceElement.setAttribute("model", subResource.getModelName());
		}
		if (subResource.getBbox() != null){
			resourceElement.appendChild(createBoundingBoxElement(doc, subResource.getBbox()));
		}
		if (subResource.getTags().size() > 0) {
			List<String> uniqueTags = new ArrayList<String>();
			for (String t : subResource.getTags()) {
				if (parentMRE.tags == null || !parentMRE.tags.contains(t)) {
					uniqueTags.add(t);
				}
			}
			if (!uniqueTags.isEmpty()) {
				resourceElement.appendChild(createTagsElement(doc, uniqueTags));
			}
		}
		return resourceElement;
	}
	
	private Document createXMLDocument()
	{
        try
        {
            Document doc = XMLUtilities.createDocument();
            org.w3c.dom.Element modelRoot = doc.createElement("AliceModel");
            modelRoot.setAttribute("name", this.className);
            if (this.attributionName != null && this.attributionName.length() > 0)
            {
            	modelRoot.setAttribute("creator", this.attributionName);
            }
            if (this.attributionYear != null && this.attributionYear.length() > 0)
            {
            	modelRoot.setAttribute("creationYear", this.attributionYear);
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
            
            for (ModelSubResourceExporter subResource : this.subResources) {
            	if (!subResource.getModelName().equalsIgnoreCase(this.className) && this.boundingBoxes.containsKey(subResource.getModelName())) {
            		subResource.setBbox(this.boundingBoxes.get(subResource.getModelName()));
            	}
            	modelRoot.appendChild(createSubResourceElement(doc, subResource, this));
            }
            
            return doc;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        return null;
	}
	
	private static List<ModelClassData> POTENTIAL_MODEL_CLASS_DATA_OPTIONS = null;
	
	public static ModelClassData getBestClassDataForJointList( List<Tuple2<String, String>> jointList )
	{
		if (POTENTIAL_MODEL_CLASS_DATA_OPTIONS == null)
		{
			POTENTIAL_MODEL_CLASS_DATA_OPTIONS = new LinkedList<ModelClassData>();
			Field[] dataFields = AliceResourceClassUtilities.getFieldsOfType(ModelClassData.class, ModelClassData.class);
			for (Field f : dataFields)
			{
				ModelClassData data = null;
				try {
					Object o = f.get(null);
					if (o != null && o instanceof ModelClassData)
					{
						data = (ModelClassData)o;
					}
				}
				catch (Exception e) {}
				if (data != null)
				{
					POTENTIAL_MODEL_CLASS_DATA_OPTIONS.add(data);
				}
			}
		}
		
		int highScore = Integer.MIN_VALUE;
		ModelClassData bestFit = null;
		for (ModelClassData mcd : POTENTIAL_MODEL_CLASS_DATA_OPTIONS)
		{
			List<Tuple2<String, String>> modelDataJoints = getExistingJointIdPairs(mcd.superClass);
			int score = -Math.abs(modelDataJoints.size() - jointList.size());
			for (Tuple2<String, String> inputPair : jointList)
			{
				for (Tuple2<String, String> testPair : modelDataJoints)
				{
					if (inputPair.equals(testPair))
					{
						score++;
						break;
					}
				}
			}
			if (score > highScore)
			{
				highScore = score;
				bestFit = mcd;
			}
		}
		return bestFit;
	}
	
	public static String getDirectoryStringForPackage(String packageString)
	{
		StringBuilder sb = new StringBuilder();
		String[] splitString = packageString.split("\\.");
		for (String s : splitString)
		{
			sb.append(s);
			sb.append(File.separator);
		}
		return sb.toString();
	}
	
	private static List<Tuple2<String, String>> getExistingJointIdPairs(Class<?> resourceClass)
	{
		List<Tuple2<String, String>> ids = new LinkedList<Tuple2<String, String>>();
		Field[] fields = resourceClass.getDeclaredFields();
		for (Field f : fields)
		{
			if (org.lgna.story.resources.JointId.class.isAssignableFrom(f.getType()))
			{
				String fieldName = f.getName();
				String parentName = null;
				org.lgna.story.resources.JointId fieldData = null;
				try {
					Object o = f.get(null);
					if (o != null && o instanceof org.lgna.story.resources.JointId)
					{
						fieldData = (org.lgna.story.resources.JointId)o;
					}
				}
				catch (Exception e) {}
				if (fieldData != null && fieldData.getParent() != null) {
					parentName = fieldData.getParent().toString();
				}
				
				ids.add(Tuple2.createInstance(fieldName, parentName));
			}
		}
		Class<?>[] interfaces = resourceClass.getInterfaces();
		for (Class<?> i : interfaces)
		{
			ids.addAll(getExistingJointIdPairs(i));
		}
		return ids;
	}
	
	private static List<String> getExistingJointIds(Class<?> resourceClass)
	{
		List<String> ids = new LinkedList<String>();
		Field[] fields = resourceClass.getDeclaredFields();
		for (Field f : fields)
		{
			if (org.lgna.story.resources.JointId.class.isAssignableFrom(f.getType()))
			{
				String fieldName = f.getName();
				ids.add(fieldName);
			}
		}
		Class<?>[] interfaces = resourceClass.getInterfaces();
		for (Class<?> i : interfaces)
		{
			ids.addAll(getExistingJointIds(i));
		}
		return ids;
	}
	
	private java.lang.reflect.Field getJointRootsField( Class<?> cls )
	{
		if (cls == null)
		{
			return null;
		}
		java.lang.reflect.Field[] rootFields = AliceResourceClassUtilities.getFieldsOfType(cls, org.lgna.story.resources.JointId[].class);
		if (rootFields.length == 1)
		{
			return rootFields[0];
		}
		else{
			Class[] interfaces = cls.getInterfaces();
			for (Class i : interfaces)
			{
				java.lang.reflect.Field rootField = getJointRootsField(i);
				if (rootField != null)
				{
					return rootField;
				}
			}
		}
		return null;
	}
	
	private boolean needsToDefineRootsMethod( Class<?> cls )
	{
		if (cls == null)
		{
			return false;
		}
		java.lang.reflect.Method[] methods = cls.getMethods();
		for (java.lang.reflect.Method m : methods)
		{
			if (org.lgna.story.resources.JointId[].class.isAssignableFrom(m.getReturnType()))
			{
				return true;
			}
		}
		Class[] interfaces = cls.getInterfaces();
		for (Class i : interfaces)
		{
			boolean needToDefineMethod = needsToDefineRootsMethod(i);
			if (needToDefineMethod)
			{
				return needToDefineMethod;
			}
		}
		return false;
	}
	
	public static String getAccessorMethodsForResourceClass( Class<? extends org.lgna.story.resources.JointedModelResource> resourceClass )
	{
		StringBuilder sb = new StringBuilder();
		List<String> jointIds = getExistingJointIds(resourceClass);
		for (String id : jointIds)
		{
			sb.append( "public Joint get"+AliceResourceClassUtilities.getAliceMethodNameForEnum(id)+"() {\n");
			sb.append( "\t return org.lgna.story.Joint.getJoint( this, "+resourceClass.getCanonicalName()+"."+id+");\n");
			sb.append( "}\n");
		}
		return sb.toString();
	}
	
	private String createResourceEnumName(String modelName, String textureName) {
		if (modelName.equalsIgnoreCase(this.getClassName())) {
			return AliceResourceUtilties.makeEnumName(textureName);
		}
		else {
			return AliceResourceUtilties.makeEnumName(modelName) + "__" + AliceResourceUtilties.makeEnumName(textureName);
		}
	}
	
	private String createResourceEnumName(ModelSubResourceExporter resource) {
		return createResourceEnumName(resource.getModelName(), resource.getTextureName());
	}
	
	public String createJavaCode()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(getCopyrightComment());
		sb.append("\n");
		sb.append("package "+this.classData.packageString+";\n\n");
		
		sb.append("public enum "+this.className+" implements "+this.classData.superClass.getCanonicalName()+" {\n");
		for (int i=0; i<this.subResources.size(); i++)
		{
			ModelSubResourceExporter resource = this.subResources.get(i);
			String resourceEnumName = createResourceEnumName(resource);
			sb.append("\t"+resourceEnumName);
			if (i < this.subResources.size() - 1)
			{
				sb.append(",\n");
			}
			else
			{
				sb.append(";\n");
			}
		}
		List<String> existingIds = getExistingJointIds(this.classData.superClass);
		boolean addedRoots = false;
		if (this.jointList != null)
		{
			List<String> rootJoints = new LinkedList<String>();
			sb.append("\n");
			for (Tuple2<String, String> entry : this.jointList)
			{
				String jointString = entry.getA();
				String parentString = entry.getB();
				if (existingIds.contains(jointString))
				{
					continue;
				}
				if (parentString == null)
				{
					parentString = "null";
					rootJoints.add(jointString);
					addedRoots = true;
				}
				sb.append("\tpublic static final org.lgna.story.resources.JointId "+jointString+" = new org.lgna.story.resources.JointId( "+parentString+", "+this.className+".class );\n");
			}
			
			if (addedRoots)
			{
				sb.append("\n\tpublic static org.lgna.story.resources.JointId[] "+ROOT_IDS_FIELD_NAME+" = { ");
				for (int i=0; i<rootJoints.size(); i++){
					sb.append(rootJoints.get(i));
					if (i < rootJoints.size()-1) { 
						sb.append(", ");
					}
				}
				sb.append(" };\n");
			}
		}
		sb.append("\n");
		if (needsToDefineRootsMethod(this.classData.superClass))
		{
			sb.append("\tpublic org.lgna.story.resources.JointId[] getRootJointIds(){\n");
			if (addedRoots)
			{
				sb.append("\t\treturn "+this.className+"."+ROOT_IDS_FIELD_NAME+";\n");
			}
			else
			{
				java.lang.reflect.Field rootsField = getJointRootsField(this.classData.superClass);
				if (rootsField != null)
				{
					sb.append("\t\treturn "+rootsField.getDeclaringClass().getCanonicalName()+"."+rootsField.getName()+";\n");
				}
				else
				{
					sb.append("\t\treturn new org.lgna.story.resources.JointId[0];\n");
				}
			}
			sb.append("\t}\n");
		}
		sb.append("\n\tpublic org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {\n");
		sb.append("\t\treturn "+this.jointAndVisualFactory.getCanonicalName()+".getInstance( this );\n");
		sb.append("\t}\n");
		sb.append("\tpublic "+this.classData.implementationClass.getCanonicalName()+" createImplementation( "+this.classData.abstractionClass.getCanonicalName()+" abstraction ) {\n");
		sb.append("\t\treturn new "+this.classData.implementationClass.getCanonicalName() +"( abstraction, "+this.jointAndVisualFactory.getCanonicalName()+".getInstance( this ) );\n");
		sb.append("\t}\n");
		sb.append("}\n");
		
		
		return sb.toString();
	}
	
	private void add(File source, JarOutputStream target, boolean recursive) throws IOException
	{
		String root = source.getAbsolutePath().replace("\\", "/")+"/";
		this.add(source, target, root, recursive);
	}
	
	private void add(File source, JarOutputStream target, String root, boolean recursive) throws IOException
	{
	  BufferedInputStream in = null;
	  try
	  {
	    if (source.isDirectory())
	    {
	      String name = source.getPath().replace("\\", "/");
	      if (name.length() > 0 )
	      {
	        if (!name.endsWith("/"))
	          name += "/";
	        name = name.substring(root.length());
			if (name.startsWith("/")) {
				name = name.substring(1);
			}
	        if (name.length() > 0)
	        {
		        JarEntry entry = new JarEntry(name);
		        entry.setTime(source.lastModified());
		        try
		        {
		        	System.out.println("   Adding: "+name);
			        target.putNextEntry(entry);
			        target.closeEntry();
		        }
		        catch (ZipException ze)
		        {
		        	System.err.println(ze.getMessage());
		        }
	        }
	      }
	      for (File nestedFile: source.listFiles())
	      {
	    	  if (!nestedFile.isDirectory() || recursive)
	    	  {
	    		  add(nestedFile, target, root, recursive);
	    	  }
	      }
	      return;
	    }

	    String entryName = source.getPath().replace("\\", "/");
	    entryName = entryName.substring(root.length());
		if (entryName.startsWith("/") || entryName.startsWith("\\")) {
			entryName = entryName.substring(1);
		}
	    JarEntry entry = new JarEntry(entryName);
	    entry.setTime(source.lastModified());
	    target.putNextEntry(entry);
	    in = new BufferedInputStream(new FileInputStream(source));

	    byte[] buffer = new byte[1024];
	    while (true)
	    {
	      int count = in.read(buffer);
	      if (count == -1)
	        break;
	      target.write(buffer, 0, count);
	    }
	    target.closeEntry();
	  }
	  finally
	  {
	    if (in != null)
	      in.close();
	  }
	}

	private File getJavaCodeDir(String root)
	{
		String packageDirectory = getDirectoryStringForPackage(this.classData.packageString);
		return new File(root+packageDirectory);
	}
	
	private File getJavaClassFile(String root)
	{
		String filename = getDirectoryStringForPackage(this.classData.packageString)+this.className+".class";
		return new File(root+filename);
	}

	private File getJavaFile(String root)
	{
		String filename = getDirectoryStringForPackage(this.classData.packageString)+this.className+".java";
		return new File(root+filename);
	}
	
	private File createJavaCode(String root)
	{
		String packageDirectory = getDirectoryStringForPackage(this.classData.packageString);
		System.out.println(packageDirectory);
		String javaCode = createJavaCode();
		System.out.println(javaCode);
		System.out.println(System.getProperty("java.class.path"));
		File javaFile = getJavaFile(root);
		TextFileUtilities.write(javaFile, javaCode);
		return javaFile;
	}
	
	 private String createXMLString()
    {
        Document doc = this.createXMLDocument();
        if (doc != null)
        {
            try
            {
                TransformerFactory transfac = TransformerFactory.newInstance();
                transfac.setAttribute("indent-number", new Integer(4));
                Transformer trans = transfac.newTransformer();
//	                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");

                //create string from xml tree
                StringWriter sw = new StringWriter();
                StreamResult result = new StreamResult(sw);
                DOMSource source = new DOMSource(doc);
                trans.transform(source, result);
                String xmlString = sw.toString();

                return xmlString;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
	   
	private File getXMLFile(String root) {
		if (!root.endsWith("/") && !root.endsWith("\\")) {
			root += "/";
		}
		String resourceDirectory = root + getDirectoryStringForPackage(this.classData.packageString)+ModelResourceExporter.getResourceSubDirWithSeparator("");
        File xmlFile = new File(resourceDirectory, this.className+".xml");
		return xmlFile;
	}

	private File createXMLFile(String root, boolean forceRebuild)
	{
        File outputFile = getXMLFile(root);
        try
        {
	        if (!forceRebuild && this.xmlFile != null && this.xmlFile.exists())
	        {
				if (!outputFile.exists())
	            {
	            	FileUtilities.createParentDirectoriesIfNecessary(outputFile);
	                outputFile.createNewFile();
	            }
	        	FileUtilities.copyFile(this.xmlFile, outputFile);
	        	return outputFile;
	        }
	        else	
	        {
	        	//This path does not indent the xml
//	        	Document doc = this.createXMLDocument();
//	        	XMLUtilities.write(doc, outputFile);
	        	
	        	//This path does indenting
	        	String xmlString = this.createXMLString();	
	            if (!outputFile.exists())
	            {
	            	FileUtilities.createParentDirectoriesIfNecessary(outputFile);
	                outputFile.createNewFile();
	            }
	            FileWriter fw = new FileWriter(outputFile);
	            fw.write(xmlString);
	            fw.close();
	            
	            
	            return outputFile;
	        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
		
	}
	
	private File saveImageToFile(String fileName, Image image)
	{
		try {
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			if (width == 0 || height == 0) {
				return null;
			}
		}
		catch (Exception e) {
			return null;
		}
		File outputFile = new File(fileName);
        try{
            if (!outputFile.exists()){
            	FileUtilities.createParentDirectoriesIfNecessary(outputFile);
                outputFile.createNewFile();
            }
            ImageUtilities.write(outputFile, image);
            return outputFile;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
	}

	public String getThumbnailPath(String rootPath, String thumbnailName) {
		if (!rootPath.endsWith("/") && !rootPath.endsWith("\\")) {
			rootPath += "/";
		}
		String resourceDirectory = rootPath + getDirectoryStringForPackage(this.classData.packageString)+ModelResourceExporter.getResourceSubDirWithSeparator(this.className);
		return resourceDirectory+thumbnailName;
	}

	private List<File> saveThumbnailsToDir(String root)
	{
		List<File> thumbnailFiles = new LinkedList<File>();
		List<String> thumbnailsCreated = new LinkedList<String>();
		boolean gotBase = false;
		if (this.existingThumbnails != null && !this.existingThumbnails.isEmpty()) {
			for(Entry < String, File > entry : this.existingThumbnails.entrySet())
			{
				if (!gotBase) {
					String thumbName = AliceResourceUtilties.getThumbnailResourceName(this.getClassName(), null);
					File thumb = new File(getThumbnailPath(root, thumbName));
					try {
						FileUtilities.copyFile(entry.getValue(), thumb);
						thumbnailFiles.add(thumb);
					}
					catch (IOException e) {
						e.printStackTrace();
						return null;
					}
					gotBase = true;
				}
				if (entry.getValue().exists() ){
					thumbnailFiles.add(entry.getValue());
					thumbnailsCreated.add(entry.getKey());
				}
				else {
					System.err.println("FAILED TO FIND THUMBNAIL FILE '"+entry.getValue()+"'");
					return null;
				}
			}
		}
		for(Entry < ModelSubResourceExporter, Image > entry : this.thumbnails.entrySet())
		{
			if (!thumbnailsCreated.contains(entry.getKey())) {
				if (!gotBase)
				{
					String thumbName = AliceResourceUtilties.getThumbnailResourceName(this.getClassName(), null);
					File f = saveImageToFile(getThumbnailPath(root, thumbName), entry.getValue());
					if (f != null ){
						thumbnailFiles.add(f);
					}
					gotBase = true;
				}
				String thumbnailName = AliceResourceUtilties.getThumbnailResourceName(entry.getKey().getModelName(), entry.getKey().getTextureName());
				File f = saveImageToFile(getThumbnailPath(root, thumbnailName), entry.getValue());
				if (f != null ){
					thumbnailsCreated.add(thumbnailName);
					thumbnailFiles.add(f);
				}
			}
		}
        return thumbnailFiles;
	}
	
	public boolean addToJar(String sourceDirectory, String resourceDirectory, JarOutputStream resourceJarStream, JarOutputStream sourceJarStream, boolean rebuildJavaFile, boolean rebuildXmlFile) throws IOException
	{
		if (!sourceDirectory.endsWith("/") && !sourceDirectory.endsWith("\\")) {
			sourceDirectory += File.separator;
        }
		xmlFile = getXMLFile(resourceDirectory);
		if ((rebuildXmlFile || !xmlFile.exists() || xmlFile.length() == 0 || isMoreRecentThan(this.lastEdited, xmlFile)) && resourceJarStream != null) {
			xmlFile = createXMLFile(resourceDirectory, rebuildXmlFile);
		}
		boolean shouldAddResources = resourceJarStream != null;
		boolean addResources = false;
		File resourceDir = null;
		if (resourceJarStream != null && xmlFile != null) {
			addResources = true;
			resourceDir = xmlFile.getParentFile();
			List<File> thumbnailFiles = saveThumbnailsToDir(resourceDirectory);
		}
		else if (shouldAddResources){
			throw new IOException("FAILED TO MAKE XML FILE FOR "+this.getClassName()+"--NOT ADDING IT TO JARS.");
		}
		boolean addClassData = false;
		File sourceDir = null;
		boolean shouldAddClassData = this.classData != null && sourceJarStream != null;
		if (shouldAddClassData) {
			sourceDir = getJavaCodeDir(sourceDirectory);
			javaFile = getJavaFile(sourceDirectory);
			File classFile = getJavaClassFile(sourceDirectory);
			if (rebuildJavaFile || !javaFile.exists() || !classFile.exists() || javaFile.length() == 0 || isMoreRecentThan(this.lastEdited, javaFile)) {
				try {
					javaFile = createJavaCode(sourceDirectory);
				}
				catch (Exception e) {
					throw new IOException("FAILED TO MAKE JAVA FILE FOR "+this.getClassName()+"--NOT ADDING IT TO JARS.\n"+e.toString());
				}
			}
			
			String[] args = new String[]{javaFile.getAbsolutePath(), "-target", "1.5", "-classpath", System.getProperty("java.class.path")};
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(baos);
			int status = com.sun.tools.javac.Main.compile(args, pw);
			
			String compileOutput = baos.toString("UTF-8");
			if (status != 0)
			{
				System.err.println(compileOutput);
				throw new IOException("Java code for "+this.getClassName()+" failed to compile: "+ compileOutput);
			}
			else {
				addClassData = true;
			}
			
		}
		if (shouldAddResources && addResources) {
			try
			{
				System.out.println("Adding "+resourceDir);
				add(resourceDir, resourceJarStream, resourceDirectory, true);
			}
			catch (Exception e)
			{
				throw new IOException("FAILED ADDING RESROUCES TO RESOURCE JAR."+e);
			}
		}
		if (shouldAddClassData && addClassData) {
			try
			{
				System.out.println("Adding "+sourceDir);
				add(sourceDir, sourceJarStream, sourceDirectory, false);
			}
			catch (Exception e)
			{
				throw new IOException("FAILED ADDING RESROUCES TO SOURCE JAR."+e);
			}
		}
		return true;
	}
	
	public boolean addToJar(String sourceDirectory, String resourceDirectory, JarOutputStream jos) throws IOException
	{
		return addToJar(sourceDirectory, resourceDirectory, jos, jos, true, true);
	}

	public boolean addToJar(String sourceDirectory, String resourceDirectory, JarOutputStream jos, boolean rebuildFiles) throws IOException
	{
		return addToJar(sourceDirectory, resourceDirectory, jos, jos, rebuildFiles, rebuildFiles);
	}
	
	public File export(String sourceDirectory, String resourceDirectory, String outputDir)
	{
		
		File outputFile = new File(outputDir+this.className+".jar");
		try
		{
			FileUtilities.createParentDirectoriesIfNecessary(outputFile);
			FileOutputStream fos = new FileOutputStream(outputFile);
			JarOutputStream jos = new JarOutputStream(fos);
			addToJar(sourceDirectory, resourceDirectory, jos);
			jos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return outputFile;
	}
	
}
 