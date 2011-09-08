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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lgna.project.License;
import org.w3c.dom.Document;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

public class ModelResourceExporter {

	private static String COPYRIGHT_COMMENT = null;
	private static String ROOT_IDS_FIELD_NAME = "JOINT_ID_ROOTS";
	
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
	
	private String name;
	private List<String> textures = new LinkedList<String>();
	private AxisAlignedBox boundingBox;
	private File xmlFile;
	private Image thumbnail;
	
	private String attributionName;
	private String attributionYear;
	
	private Class<?> jointAndVisualFactory = org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory.class;
	
	private ModelClassData classData;
	private List<Tuple2<String, String>> jointList;
	
	public ModelResourceExporter(String name)
	{
		this.name = name;
		if (Character.isLowerCase(this.name.charAt(0)))
		{
			this.name = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
		}
	}
	
	public ModelResourceExporter(String name, ModelClassData classData)
	{
		this(name);
		this.classData = classData;
	}
	
	public ModelResourceExporter(String name, ModelClassData classData, Class<?> jointAndVisualFactoryClass)
	{
		this(name, classData);
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
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
		if (this.classData == null)
		{
			this.classData = ModelResourceExporter.getBestClassDataForJointList(jointList);
		}
	}
	
	public void addAttribution(String name, String year)
	{
		this.attributionName = name;
		this.attributionYear = year;
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
	
	public void setThumbnail( Image thumbnail )
	{
		this.thumbnail = thumbnail;
	}
	
	public void setXMLFile(File xmlFile)
	{
		this.xmlFile = xmlFile;
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
			Field[] dataFields = ModelResourceUtilities.getFieldsOfType(ModelClassData.class, ModelClassData.class);
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
		java.lang.reflect.Field[] rootFields = ModelResourceUtilities.getFieldsOfType(cls, org.lgna.story.resources.JointId[].class);
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
			sb.append( "public Joint get"+ModelResourceUtilities.getAliceMethodNameForEnum(id)+"() {\n");
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
	
	private void add(File source, JarOutputStream target) throws IOException
	{
		String root = source.getAbsolutePath().replace("\\", "/")+"/";
		this.add(source, target, root);
	}
	private void add(File source, JarOutputStream target, String root) throws IOException
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
	        if (name.length() > 0)
	        {
		        JarEntry entry = new JarEntry(name);
		        entry.setTime(source.lastModified());
		        target.putNextEntry(entry);
		        target.closeEntry();
	        }
	      }
	      for (File nestedFile: source.listFiles())
	        add(nestedFile, target, root);
	      return;
	    }

	    String entryName = source.getPath().replace("\\", "/");
	    entryName = entryName.substring(root.length());
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
	
	private File createJavaCode(String root)
	{
		String packageDirectory = getDirectoryStringForPackage(this.classData.packageString);
		System.out.println(packageDirectory);
		String javaCode = createJavaCode();
		System.out.println(javaCode);
		System.out.println(System.getProperty("java.class.path"));
		File javaFile = new File(root+packageDirectory+this.name+".java");
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
	   
	
	private File createXMLFile(String root)
	{
		String resourceDirectory = root + getDirectoryStringForPackage(this.classData.packageString)+"resources/";
        File outputFile = new File(resourceDirectory, this.name+".xml");
        try
        {
	        if (this.xmlFile != null)
	        {
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
	
	private File createThumbnail(String root)
	{
		if (this.thumbnail != null)
		{
			String resourceDirectory = root + getDirectoryStringForPackage(this.classData.packageString)+"resources/";
	        File outputFile = new File(resourceDirectory, this.name+".png");
	        try
	        {
	            if (!outputFile.exists())
	            {
	            	FileUtilities.createParentDirectoriesIfNecessary(outputFile);
	                outputFile.createNewFile();
	            }
	            ImageUtilities.write(outputFile, this.thumbnail);
	            return outputFile;
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		}
        return null;
	}
	
	public File export(String sourceDirectory, String outputDir)
	{
		if (!sourceDirectory.endsWith("/") && !sourceDirectory.endsWith("\\")) {
			sourceDirectory += File.separator;
        }
		File javaFile = createJavaCode(sourceDirectory);
		
		String[] args = new String[]{javaFile.getAbsolutePath(), "-classpath", System.getProperty("java.class.path")};
		com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();
		PrintWriter pw = new PrintWriter(System.out);
		int status = com.sun.tools.javac.Main.compile(args, pw);
		
		if (status != 0)
		{
			System.out.println("BOOM!");
		}
		
		File xmlFile = createXMLFile(sourceDirectory);
		File thumbnailFile = createThumbnail(sourceDirectory);
		File outputFile = new File(outputDir+this.name+".jar");
		try
		{
			FileUtilities.createParentDirectoriesIfNecessary(outputFile);
			FileOutputStream fos = new FileOutputStream(outputFile);
			JarOutputStream jos = new JarOutputStream(fos);
			add(new File(sourceDirectory), jos);
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
 