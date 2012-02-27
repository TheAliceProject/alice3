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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
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
	
	public static String getResourceSubDirWithSeparator() {
		if (RESOURCE_SUB_DIR == null || RESOURCE_SUB_DIR.length() == 0) {
			return "";
		}
		else {
			return RESOURCE_SUB_DIR + "/";
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
	private String name;
	private List<String> textures = new LinkedList<String>();
	private List<String> tags = new LinkedList<String>();
	private AxisAlignedBox boundingBox;
	private File xmlFile;
	private File javaFile;
	private Map<String, Image> thumbnails = new HashMap<String, Image>();
	private Map<String, File> existingThumbnails = null;
	
	private String attributionName;
	private String attributionYear;
	
	private Class<?> jointAndVisualFactory = org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory.class;
	
	private ModelClassData classData;
	private List<Tuple2<String, String>> jointList;
	
	public ModelResourceExporter(String name, String resourceName)
	{
		this.name = name;
		if (Character.isLowerCase(this.name.charAt(0)))
		{
			this.name = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
		}
		this.resourceName = resourceName;
	}
	
	public ModelResourceExporter(String name, ModelClassData classData)
	{
		this(name, name);
		this.classData = classData;
	}
	
	public ModelResourceExporter(String name, String resourceName, ModelClassData classData)
	{
		this(name, resourceName);
		this.classData = classData;
	}
	
	public ModelResourceExporter(String name, ModelClassData classData, Class<?> jointAndVisualFactoryClass)
	{
		this(name, classData);
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}
	
	public ModelResourceExporter(String name, String resourceName, ModelClassData classData, Class<?> jointAndVisualFactoryClass)
	{
		this(name, resourceName, classData);
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}
	
	public void SetResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}
	
	public String getResourceName()
	{
		return this.resourceName;
	}
	
	public ModelClassData getClassData()
	{
		return this.classData;
	}
	
	public void setJointAndVisualFactory(Class<?> jointAndVisualFactoryClass)
	{
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}
	
	public void setJointMap(List<Tuple2<String, String>> jointList)
	{
		this.jointList = jointList;
//		if (this.classData == null)
//		{
//			this.classData = ModelResourceExporter.getBestClassDataForJointList(jointList);
//		}
	}
	
	public void addAttribution(String name, String year)
	{
		this.attributionName = name;
		this.attributionYear = year;
	}
	
	public void addTags(String... tags) {
		for (String s : tags){
			this.tags.add(s);
		}
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getPackageString()
	{
		return this.classData.packageString;
	}
	
	public void addTexture(String textureName)
	{
		this.textures.add(textureName);
	}
	
	public void setBoundingBox( AxisAlignedBox boundingBox )
	{
		this.boundingBox = boundingBox;
	}
	
	public void addThumbnail( String name, Image thumbnail )
	{
		this.thumbnails.put(name, thumbnail);
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
	
	private org.w3c.dom.Element createBoundingBoxElement(Document doc)
	{
		org.w3c.dom.Element bboxElement = doc.createElement("BoundingBox");
		org.w3c.dom.Element minElement = doc.createElement("Min");
		minElement.setAttribute("x", Double.toString(this.boundingBox.getXMinimum()));
		minElement.setAttribute("y", Double.toString(this.boundingBox.getYMinimum()));
		minElement.setAttribute("z", Double.toString(this.boundingBox.getZMinimum()));
		org.w3c.dom.Element maxElement = doc.createElement("Max");
		maxElement.setAttribute("x", Double.toString(this.boundingBox.getXMaximum()));
		maxElement.setAttribute("y", Double.toString(this.boundingBox.getYMaximum()));
		maxElement.setAttribute("z", Double.toString(this.boundingBox.getZMaximum()));
		
		bboxElement.appendChild(minElement);
		bboxElement.appendChild(maxElement);
		
		return bboxElement;
	}
	
	private org.w3c.dom.Element createTagsElement(Document doc)
	{
		org.w3c.dom.Element tagsElement = doc.createElement("Tags");
		for (String tag : this.tags) {
			org.w3c.dom.Element tagElement = doc.createElement("Tag");
			tagElement.setTextContent(tag);
			tagsElement.appendChild(tagElement);
		}
		return tagsElement;
	}
	
	private Document createXMLDocument()
	{
        try
        {
            Document doc = XMLUtilities.createDocument();
            org.w3c.dom.Element modelRoot = doc.createElement("AliceModel");
            modelRoot.setAttribute("name", this.name);
            if (this.attributionName != null && this.attributionName.length() > 0)
            {
            	modelRoot.setAttribute("creator", this.attributionName);
            }
            if (this.attributionYear != null && this.attributionYear.length() > 0)
            {
            	modelRoot.setAttribute("creationYear", this.attributionYear);
            }
            doc.appendChild(modelRoot);
            modelRoot.appendChild(this.createBoundingBoxElement(doc));
            modelRoot.appendChild(this.createTagsElement(doc));
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
	
	public String createJavaCode()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(getCopyrightComment());
		sb.append("\n");
		sb.append("package "+this.classData.packageString+";\n\n");
		
		sb.append("public enum "+this.name+" implements "+this.classData.superClass.getCanonicalName()+" {\n");
		for (int i=0; i<this.textures.size(); i++)
		{
			String texture = this.textures.get(i);
			sb.append("\t"+texture);
			if (i < this.textures.size() - 1)
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
				sb.append("\tpublic static final org.lgna.story.resources.JointId "+jointString+" = new org.lgna.story.resources.JointId( "+parentString+", "+this.name+".class );\n");
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
				sb.append("\t\treturn "+this.name+"."+ROOT_IDS_FIELD_NAME+";\n");
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

	private File getJavaFile(String root)
	{
		String filename = getDirectoryStringForPackage(this.classData.packageString)+this.name+".java";
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
		String resourceDirectory = root + getDirectoryStringForPackage(this.classData.packageString)+ModelResourceExporter.getResourceSubDirWithSeparator();
        File xmlFile = new File(resourceDirectory, this.name+".xml");
		return xmlFile;
	}

	private File createXMLFile(String root)
	{
        File outputFile = getXMLFile(root);
        try
        {
	        if (this.xmlFile != null && this.xmlFile.exists())
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

	public String getThumbnailPath(String rootPath, String textureName) {
		if (!rootPath.endsWith("/") && !rootPath.endsWith("\\")) {
			rootPath += "/";
		}
		String resourceDirectory = rootPath + getDirectoryStringForPackage(this.classData.packageString)+ModelResourceExporter.getResourceSubDirWithSeparator();
		if (textureName == null) {
			return resourceDirectory+this.name+".png";
		}
		else {
			return resourceDirectory+this.name + "_" + textureName + ".png";
		}
	}

	private List<File> createThumbnails(String root)
	{
		List<File> thumbnailFiles = new LinkedList<File>();
		List<String> thumbnailsCreated = new LinkedList<String>();
		boolean gotBase = false;
		if (this.existingThumbnails != null && !this.existingThumbnails.isEmpty()) {
			for(Entry < String, File > entry : this.existingThumbnails.entrySet())
			{
				if (!gotBase) {
					File thumb = new File(getThumbnailPath(root, null));
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
		for(Entry < String, Image > entry : this.thumbnails.entrySet())
		{
			if (!thumbnailsCreated.contains(entry.getKey())) {
				if (!gotBase)
				{
					File f = saveImageToFile(getThumbnailPath(root, null), entry.getValue());
					if (f != null ){
						thumbnailFiles.add(f);
					}
					gotBase = true;
				}
				File f = saveImageToFile(getThumbnailPath(root, entry.getKey()), entry.getValue());
				if (f != null ){
					thumbnailsCreated.add(entry.getKey());
					thumbnailFiles.add(f);
				}
			}
		}
        return thumbnailFiles;
	}
	
	public boolean addToJar(String sourceDirectory, String resourceDirectory, JarOutputStream resourceJarStream, JarOutputStream sourceJarStream, boolean rebuildJavaFile, boolean rebuildXmlFile)
	{
		if (!sourceDirectory.endsWith("/") && !sourceDirectory.endsWith("\\")) {
			sourceDirectory += File.separator;
        }
		xmlFile = getXMLFile(resourceDirectory);
		if (rebuildXmlFile || !xmlFile.exists()) {
			xmlFile = createXMLFile(resourceDirectory);
		}
		boolean addResources = false;
		File resourceDir = null;
		if (xmlFile != null) {
			addResources = true;
			resourceDir = xmlFile.getParentFile();
			List<File> thumbnailFiles = createThumbnails(resourceDirectory);
		}
		else {
			System.err.println("FAILED TO MAKE XML FILE FOR "+this.getName()+"--NOT ADDING IT TO JARS.");
			return false;
		}
		boolean addClassData = false;
		File sourceDir = null;
		boolean shouldAddClassData = this.classData != null;
		if (shouldAddClassData) {
			sourceDir = getJavaCodeDir(sourceDirectory);
			javaFile = getJavaFile(sourceDirectory);
			if (rebuildJavaFile || !javaFile.exists()) {
				try {
					javaFile = createJavaCode(sourceDirectory);
				}
				catch (Exception e) {
					System.err.println("FAILED TO MAKE JAVA FILE FOR "+this.getName()+"--NOT ADDING IT TO JARS.");
					return false;
				}
			}
			
			String[] args = new String[]{javaFile.getAbsolutePath(), "-target", "1.5", "-classpath", System.getProperty("java.class.path")};
			PrintWriter pw = new PrintWriter(System.out);
			int status = com.sun.tools.javac.Main.compile(args, pw);
			
			if (status != 0)
			{
				System.out.println("BOOM!");
			}
			else {
				addClassData = true;
			}
			
		}
		if (addResources && (shouldAddClassData && addClassData)) {
			try
			{
				System.out.println("Adding "+sourceDir);
				add(sourceDir, sourceJarStream, sourceDirectory, false);
				System.out.println("Adding "+resourceDir);
				add(resourceDir, resourceJarStream, resourceDirectory, true);
				return true;
			}
			catch (Exception e)
			{
				System.err.println("FAILED ADDING DATA TO JARS: ");
				e.printStackTrace();
				return false;
			}
		}
		else {
			System.err.println("NOT ADDING "+this.getName()+" TO JARS.");
			if (!addResources) {
				System.err.println("FAILED TO MAKE RESOURCES FOR "+this.getName());
			}
			if (shouldAddClassData && !addClassData) {
				System.err.println("FAILED TO MAKE JAVA CODE FOR "+this.getName());
			}
			return false;
		}
	}
	
	public boolean addToJar(String sourceDirectory, String resourceDirectory, JarOutputStream jos)
	{
		return addToJar(sourceDirectory, resourceDirectory, jos, jos, true, true);
	}

	public boolean addToJar(String sourceDirectory, String resourceDirectory, JarOutputStream jos, boolean rebuildFiles)
	{
		return addToJar(sourceDirectory, resourceDirectory, jos, jos, rebuildFiles, rebuildFiles);
	}
	
	public File export(String sourceDirectory, String resourceDirectory, String outputDir)
	{
		
		File outputFile = new File(outputDir+this.name+".jar");
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
 