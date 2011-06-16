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

package org.lookingglassandalice.storytelling.resourceutilities;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import edu.cmu.cs.dennisc.alice.License;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.xml.XMLUtilities;
import edu.cmu.cs.dennisc.zip.ZipUtilities;

public class ModelResourceExporter {

	private static String COPYRIGHT_COMMENT = null;
	
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
	private String packageString;
	
	private Class implementationFactoryClass;
	private Class implementationClass;
	private Class rootabstractionClass;
	private Class superClass;
	
	public ModelResourceExporter(String name, String packageString, Class superClass, Class implementationFactoryClass, Class implementationClass, Class abstractionClass)
	{
		this.name = name;
		if (Character.isLowerCase(this.name.charAt(0)))
		{
			this.name = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
		}
		this.packageString = packageString;
		this.superClass = superClass;
		this.implementationFactoryClass = implementationFactoryClass;
		this.implementationClass = implementationClass;
		this.rootabstractionClass = abstractionClass;
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
	
	private String createJavaCode()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(getCopyrightComment());
		sb.append("\n");
		sb.append("package "+this.packageString+";\n\n");
		
		sb.append("public enum "+this.name+" implements "+this.superClass.getCanonicalName()+" {\n");
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
		sb.append("\tprivate final "+this.implementationFactoryClass.getCanonicalName()+" factory;\n");
		sb.append("\tprivate "+this.name+"() {\n");
		sb.append("\t\tthis.factory = "+this.implementationFactoryClass.getCanonicalName()+".getInstance(this);\n");
		sb.append("\t}\n");
		sb.append("\tpublic "+this.implementationClass.getCanonicalName()+" createImplementation( "+this.rootabstractionClass.getCanonicalName()+" abstraction ) {\n");
		sb.append("\t\treturn this.factory.createImplementation( abstraction );\n");
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
		String packageDirectory = getDirectoryStringForPackage(this.packageString);
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
		String resourceDirectory = root + getDirectoryStringForPackage(this.packageString)+"resources/";
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
			String resourceDirectory = root + getDirectoryStringForPackage(this.packageString)+"resources/";
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
		File javaFile = createJavaCode(sourceDirectory);
		
		String[] args = new String[]{javaFile.getAbsolutePath(), "-classpath", System.getProperty("java.class.path")};
		com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();
		PrintWriter pw = new PrintWriter(System.out);
		int status = com.sun.tools.javac.Main.compile(args, pw);
		
		File xmlFile = createXMLFile(sourceDirectory);
		File thumbnailFile = createThumbnail(sourceDirectory);
		File outputFile = new File(outputDir+this.name+".jar");
		try
		{
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
		System.out.println("status: "+status);
		return outputFile;
	}
	
}
 