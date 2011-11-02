/**
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.lgna.project.ast.SimpleArgument;
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
import org.lgna.project.ast.SuperConstructorInvocationStatement;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.resources.ModelResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

/**
 * @author dculyba
 *
 */
public class ModelResourceUtilities {
	
	public static String DEFAULT_PACKAGE = "";
	
	
	public static String getName(Class<?> modelResource)
	{
		return modelResource.getSimpleName();
	}
	
	private static java.net.URL getThumbnailURLInternal(Class<?> modelResource, String name) {
		return modelResource.getResource("resources/"+ name+".png");
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
	
	private static AxisAlignedBox getBoundingBoxFromXML(Document doc)
	{
		Element bboxElement = XMLUtilities.getSingleChildElementByTagName(doc.getDocumentElement(), "BoundingBox");
		if (bboxElement != null)
		{
			Element min = (Element)bboxElement.getElementsByTagName("Min").item(0);
			Element max = (Element)bboxElement.getElementsByTagName("Max").item(0);
			
			double minX = Double.parseDouble(min.getAttribute("x"));
			double minY = Double.parseDouble(min.getAttribute("y"));
			double minZ = Double.parseDouble(min.getAttribute("z"));
			
			double maxX = Double.parseDouble(max.getAttribute("x"));
			double maxY = Double.parseDouble(max.getAttribute("y"));
			double maxZ = Double.parseDouble(max.getAttribute("z"));
			
			AxisAlignedBox bbox = new AxisAlignedBox(minX, minY, minZ, maxX, maxY, maxZ);
			
			return bbox;
		}
		
		return null;
	}
	
	public static AxisAlignedBox getBoundingBox(Class<?> modelResource)
	{
		if (modelResource == null)
		{
			return null;
		}
		String name = getName(modelResource);
		try {
			InputStream is = modelResource.getResourceAsStream("resources/"+name+".xml");
			if (is != null) {
				Document doc = XMLUtilities.read(is);
				return getBoundingBoxFromXML(doc);
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
	
	public static List<Class<? extends org.lgna.story.resources.ModelResource>> loadResourceJarFile(File resourceJar)
	{
		List<Class<? extends org.lgna.story.resources.ModelResource>> classes = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();
		List<String> classNames = new LinkedList<String>();
		try
		{
			ZipFile zip = new ZipFile(resourceJar);
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class") && !entry.getName().contains("$"))
				{
					String className = entry.getName().replace('/', '.');
					int lastDot = className.lastIndexOf(".");
					String baseName = className.substring(0, lastDot);
					classNames.add(baseName);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			URL url = resourceJar.toURI().toURL();  
			URL[] urls = new URL[]{url};
			ClassLoader cl = new URLClassLoader(urls);
			
			for (String className : classNames)
			{
				Class<?> cls = cl.loadClass(className);
				if (org.lgna.story.resources.ModelResource.class.isAssignableFrom(cls))
				{
					classes.add((Class<? extends org.lgna.story.resources.ModelResource>)cls);
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return classes;
	}
	
	public static List<Class<? extends org.lgna.story.resources.ModelResource>> getAndLoadModelResourceClasses(List<File> resourcePaths)
	{
		List<Class<? extends org.lgna.story.resources.ModelResource>> galleryClasses = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();
		for (File modelPath : resourcePaths)
		{
			try {
				File[] jarFiles = FileUtilities.listDescendants(modelPath, "jar");
				for (File f : jarFiles)
				{
					galleryClasses.addAll( ModelResourceUtilities.loadResourceJarFile(f) );
				}
			}
			catch (Exception e)
			{
				System.err.println("Failed to load resources on path: '"+modelPath+"'");
			}
		}
		return galleryClasses;
	}
	
	public static Class<? extends org.lgna.story.Model> getModelClassForResourceClass(Class<? extends org.lgna.story.resources.ModelResource> resourceClass)
	{
		if( resourceClass.isAnnotationPresent( org.lgna.project.annotations.ResourceTemplate.class ) ) {
			org.lgna.project.annotations.ResourceTemplate resourceTemplate = resourceClass.getAnnotation( org.lgna.project.annotations.ResourceTemplate.class );
			Class<?> cls = resourceTemplate.modelClass();
			if (org.lgna.story.Model.class.isAssignableFrom(cls))
			{
				return (Class<? extends org.lgna.story.Model>)cls;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	public static String getAliceEnumNameForModelJoint( String modelJointName )
	{
		List<String> nameParts = new LinkedList<String>();
		String[] parts = fullStringSplit(modelJointName);
		boolean hasRight = false;
		boolean hasLeft = false;
		boolean hasBack = false;
		boolean hasFront = false;
		for (String part : parts)
		{
			if (part.equalsIgnoreCase("l"))
			{
				hasLeft = true;
			}
			else if (part.equalsIgnoreCase("r"))
			{
				hasRight = true;
			}
			else if (part.equalsIgnoreCase("f"))
			{
				hasFront = true;
			}
			else if (part.equalsIgnoreCase("b"))
			{
				hasBack = true;
			}
			else if (part.length() > 0)
			{
				nameParts.add(part);
			}
		}
		if (hasRight)
		{
			nameParts.add(0, "RIGHT");
		}
		else if (hasLeft)
		{
			nameParts.add(0, "LEFT");
		}
		if (hasFront)
		{
			nameParts.add(0, "FRONT");
		}
		else if (hasBack)
		{
			nameParts.add(0, "BACK");
		}
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<nameParts.size(); i++)
		{
			if (i != 0)
			{
				sb.append("_");
			}
			sb.append(nameParts.get(i).toUpperCase());
		}
		return sb.toString();
	}
	
	public static String getAliceMethodNameForEnum(String enumName)
	{
		StringBuilder sb = new StringBuilder();
		String[] nameParts = enumName.split("_");
		for (String s : nameParts)
		{
			sb.append(uppercaseFirstLetter(s));
		}
		return sb.toString();
	}
	
	public static String getAliceClassName(Class<?> resourceClass)
	{
		String name = resourceClass.getSimpleName();
		int resourceIndex = name.indexOf("Resource");
		if (resourceIndex != -1)
		{
			name = name.substring(0, resourceIndex);
		}
		name = getClassNameFromName(name);
		return name;
	}
	
	public static List<String> splitOnCapitalsAndNumbers(String s)
    {
        StringBuilder sb = new StringBuilder();
        List<String> split = new LinkedList<String>();
        boolean isOnNumber = false;
        for (int i=0; i<s.length(); i++)
        {
        	boolean shouldRestart = false;
        	if (Character.isDigit(s.charAt(i))) {
        		if (!isOnNumber) {
            		shouldRestart = true;   
            	}
        		isOnNumber = true;
            }
        	else {
        		if (isOnNumber) {
        			shouldRestart = true;
        		}
        		isOnNumber = false;
        	}
            if (Character.isUpperCase(s.charAt(i)))
            {
            	shouldRestart = true;
                
            }
            if (shouldRestart) {
            	split.add(sb.toString());
                sb = new StringBuilder();
            }
            sb.append(s.charAt(i));
        }
        if (sb.length() > 0)
        {
            split.add(sb.toString());
        }
        return split;
    }
	
    public static String uppercaseFirstLetter(String s)
    {
    	if (s == null)
    	{
    		return null;
    	}
    	if (s.length() <= 1)
    	{
    		return s.toUpperCase();
    	}
    	return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }
    
    public static String[] fullStringSplit(String name)
    {
    	List<String> strings = new LinkedList<String>();
    	String[] nameParts = name.split("[_ -]");
        for (String s : nameParts)
        {
            List<String> capitalSplit = splitOnCapitalsAndNumbers(s);
            for (String subS : capitalSplit)
            {
            	if (subS.length() > 0)
            	{
            		strings.add(subS);
            	}
            }
        }
        return strings.toArray(new String[strings.size()]);
    }
    
	public static String getClassNameFromName(String name)
	{
		StringBuilder sb = new StringBuilder();
		String[] nameParts = fullStringSplit(name);
        for (String s : nameParts)
        {
        	sb.append(uppercaseFirstLetter(s));
        }
		return sb.toString();
	}
	
	public static Field[] getFieldsOfType(Class<?> ownerClass, Class<?> ofType)
	{
		Field[] fields = ownerClass.getFields();
		List<Field> fieldsOfType = new LinkedList<Field>();
		for (Field f : fields)
		{
			boolean matchesType = ofType.isAssignableFrom(f.getType());
			boolean matchesOwner = f.getDeclaringClass() == ownerClass;
			if (matchesType && matchesOwner)
			{
				fieldsOfType.add(f);
			}
		}
		return fieldsOfType.toArray(new Field[fieldsOfType.size()]);
	}
	
	public static UserPackage getAlicePackage(Class<?> resourceClass, Class<?> rootClass)
	{
		String resourcePackage = resourceClass.getPackage().getName();
		String rootPackage = rootClass.getPackage().getName();
		int rootIndex = resourcePackage.indexOf(rootPackage);
		if (rootIndex != -1)
		{
			resourcePackage = resourcePackage.substring(rootIndex+rootPackage.length());
			if (resourcePackage.startsWith("."))
			{
				resourcePackage = resourcePackage.substring(1);
			}
		}
		resourcePackage = DEFAULT_PACKAGE + resourcePackage;
		return new UserPackage(resourcePackage);
	}
	
	public static NamedUserConstructor createConstructorForResourceClass(Class<?> resourceClass, ConstructorParameterPair constructorAndParameter)
	{
		UserParameter parameter = new UserParameter("modelResource", resourceClass);
		ParameterAccess parameterAccessor = new ParameterAccess(parameter);
		SimpleArgument superArgument = new SimpleArgument(constructorAndParameter.getParameter(), parameterAccessor);
		ConstructorInvocationStatement superInvocation = new SuperConstructorInvocationStatement(constructorAndParameter.getConstructor(), superArgument);
		ConstructorBlockStatement blockStatement = new ConstructorBlockStatement(superInvocation);
		UserParameter[] parameters = {parameter};
		NamedUserConstructor constructor = new NamedUserConstructor(parameters, blockStatement);
		return constructor ;
	}
	
	public static NamedUserConstructor createConstructorForResourceField(Field resourceField, ConstructorParameterPair constructorAndParameter)
	{
		JavaField javaField = JavaField.getInstance(resourceField);
		FieldAccess fieldAccess = org.lgna.project.ast.AstUtilities.createStaticFieldAccess(javaField);
		SimpleArgument superArgument = new SimpleArgument(constructorAndParameter.getParameter(), fieldAccess);
		ConstructorInvocationStatement superInvocation = new SuperConstructorInvocationStatement(constructorAndParameter.getConstructor(), superArgument);
		ConstructorBlockStatement blockStatement = new ConstructorBlockStatement(superInvocation);
		UserParameter[] parameters = {};
		NamedUserConstructor constructor = new NamedUserConstructor(parameters, blockStatement);
		return constructor ;
	}
	
	public static ConstructorParameterPair getConstructorAndParameterForAliceClass(NamedUserType aliceType)
	{
		for (int i=0; i<aliceType.constructors.size(); i++)
		{
			NamedUserConstructor constructor = aliceType.constructors.get(i);
			if (constructor.requiredParameters.size() == 1)
			{
				UserParameter parameter = constructor.requiredParameters.get(0);
				if (parameter.getValueType().isAssignableTo(ModelResource.class))
				{
					return new ConstructorParameterPair(constructor, parameter);
				}
			}
		}
		return null;
	}
	
	public static ConstructorParameterPair getConstructorAndParameterForJavaClass(Class<?> javaClass)
	{
 		JavaType javeType = JavaType.getInstance(javaClass);
		List<JavaConstructor> constructors = javeType.getDeclaredConstructors();
		for (JavaConstructor constructor : constructors)
		{
			List<JavaConstructorParameter> parameters = (List<JavaConstructorParameter>) constructor.getRequiredParameters();
			if (parameters.size() == 1)
			{
				JavaConstructorParameter parameter = parameters.get(0);
				JavaType javaType = parameter.getValueTypeDeclaredInJava();
				if (javaType.isAssignableTo(ModelResource.class))
				{
					return new ConstructorParameterPair(constructor, parameter);
				}
			}
		}
		return null;
	}

	public static UserMethod getPartAccessorMethod(Field partField)
	{
		String methodName = "get"+getAliceMethodNameForEnum(partField.getName());
		Class<?> returnClass = org.lgna.story.Joint.class;
		UserParameter[] parameters = {};
		org.lgna.project.ast.JavaType jointIdType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.JointId.class );
		org.lgna.project.ast.TypeExpression typeExpression = new org.lgna.project.ast.TypeExpression( org.lgna.story.Joint.class );
		Class< ? >[] methodParameterClasses = { org.lgna.story.JointedModel.class, org.lgna.story.resources.JointId.class };
		org.lgna.project.ast.JavaMethod methodExpression = org.lgna.project.ast.JavaMethod.getInstance(org.lgna.story.Joint.class, "getJoint", methodParameterClasses);
		
		org.lgna.project.ast.SimpleArgument thisArgument = new org.lgna.project.ast.SimpleArgument( methodExpression.getRequiredParameters().get(0), new org.lgna.project.ast.ThisExpression() );
		
		org.lgna.project.ast.FieldAccess jointField = new org.lgna.project.ast.FieldAccess( 
				new org.lgna.project.ast.TypeExpression( jointIdType ), 
				org.lgna.project.ast.JavaField.getInstance(partField.getDeclaringClass(), partField.getName() ) 
		);
		
		org.lgna.project.ast.SimpleArgument jointArgument = new org.lgna.project.ast.SimpleArgument( methodExpression.getRequiredParameters().get(1), jointField );
		
		org.lgna.project.ast.SimpleArgument[] methodArguments = { thisArgument, jointArgument};
		org.lgna.project.ast.MethodInvocation getJointMethodInvocation = new org.lgna.project.ast.MethodInvocation( typeExpression, methodExpression, methodArguments );
		ReturnStatement returnStatement = new ReturnStatement(jointIdType, getJointMethodInvocation);
		UserMethod newMethod = new UserMethod(methodName, returnClass, parameters, new BlockStatement(returnStatement));
		return newMethod;
	}
	
	
	public static UserMethod[] getPartAccessorMethods( Class<? extends org.lgna.story.resources.ModelResource> forClass )
	{
		Field[] jointFields = ModelResourceUtilities.getFieldsOfType(forClass, org.lgna.story.resources.JointId.class);
		List<UserMethod> methods = new LinkedList<UserMethod>();
		for (Field f : jointFields)
		{
			methods.add(getPartAccessorMethod(f));
		}
		return methods.toArray(new UserMethod[methods.size()]);
	}
	
}
