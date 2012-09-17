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
package org.lgna.story.implementation.alice;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

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

/**
 * @author dculyba
 * 
 */
public class AliceResourceClassUtilities {

	public static String DEFAULT_PACKAGE = "";

	public static String RESOURCE_SUFFIX = "Resource";

	public static Class<? extends org.lgna.story.SModel> getModelClassForResourceClass( Class<? extends org.lgna.story.resources.ModelResource> resourceClass )
	{
		if( resourceClass.isAnnotationPresent( org.lgna.project.annotations.ResourceTemplate.class ) ) {
			org.lgna.project.annotations.ResourceTemplate resourceTemplate = resourceClass.getAnnotation( org.lgna.project.annotations.ResourceTemplate.class );
			Class<?> cls = resourceTemplate.modelClass();
			if( org.lgna.story.SModel.class.isAssignableFrom( cls ) )
			{
				return (Class<? extends org.lgna.story.SModel>)cls;
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

	public static String getAliceMethodNameForEnum( String enumName )
	{
		StringBuilder sb = new StringBuilder();
		String[] nameParts = enumName.split( "_" );
		for( String s : nameParts )
		{
			sb.append( uppercaseFirstLetter( s ) );
		}
		return sb.toString();
	}

	public static String getAliceClassName( String name )
	{
		int resourceIndex = name.indexOf( RESOURCE_SUFFIX );
		if( resourceIndex != -1 )
		{
			name = name.substring( 0, resourceIndex );
		}
		name = getClassNameFromName( name );
		return name;
	}

	public static String getAliceClassName( Class<?> resourceClass )
	{
		return getAliceClassName( resourceClass.getSimpleName() );
	}

	public static List<String> splitOnCapitalsAndNumbers( String s )
	{
		StringBuilder sb = new StringBuilder();
		List<String> split = new LinkedList<String>();
		boolean isOnNumber = false;
		for( int i = 0; i < s.length(); i++ )
		{
			boolean shouldRestart = false;
			if( Character.isDigit( s.charAt( i ) ) ) {
				if( !isOnNumber ) {
					shouldRestart = true;
				}
				isOnNumber = true;
			}
			else {
				if( isOnNumber ) {
					shouldRestart = true;
				}
				isOnNumber = false;
			}
			if( Character.isUpperCase( s.charAt( i ) ) )
			{
				shouldRestart = true;

			}
			if( shouldRestart ) {
				split.add( sb.toString() );
				sb = new StringBuilder();
			}
			sb.append( s.charAt( i ) );
		}
		if( sb.length() > 0 )
		{
			split.add( sb.toString() );
		}
		return split;
	}

	public static String uppercaseFirstLetter( String s )
	{
		if( s == null )
		{
			return null;
		}
		if( s.length() <= 1 )
		{
			return s.toUpperCase();
		}
		return s.substring( 0, 1 ).toUpperCase() + s.substring( 1 ).toLowerCase();
	}

	public static String[] fullStringSplit( String name )
	{
		List<String> strings = new LinkedList<String>();
		String[] nameParts = name.split( "[_ -]" );
		for( String s : nameParts )
		{
			List<String> capitalSplit = splitOnCapitalsAndNumbers( s );
			for( String subS : capitalSplit )
			{
				if( subS.length() > 0 )
				{
					strings.add( subS );
				}
			}
		}
		return strings.toArray( new String[ strings.size() ] );
	}

	public static String getClassNameFromName( String name )
	{
		StringBuilder sb = new StringBuilder();
		String[] nameParts = fullStringSplit( name );
		for( String s : nameParts )
		{
			sb.append( uppercaseFirstLetter( s ) );
		}
		return sb.toString();
	}

	public static Field[] getFieldsOfType( Class<?> ownerClass, Class<?> ofType )
	{
		Field[] fields = ownerClass.getFields();
		List<Field> fieldsOfType = new LinkedList<Field>();
		for( Field f : fields )
		{
			boolean matchesType = ofType.isAssignableFrom( f.getType() );
			boolean matchesOwner = f.getDeclaringClass() == ownerClass;
			if( matchesType && matchesOwner )
			{
				fieldsOfType.add( f );
			}
		}
		return fieldsOfType.toArray( new Field[ fieldsOfType.size() ] );
	}

	public static UserPackage getAlicePackage( Class<?> resourceClass, Class<?> rootClass )
	{
		String resourcePackage = resourceClass.getPackage().getName();
		String rootPackage = rootClass.getPackage().getName();
		int rootIndex = resourcePackage.indexOf( rootPackage );
		if( rootIndex != -1 )
		{
			resourcePackage = resourcePackage.substring( rootIndex + rootPackage.length() );
			if( resourcePackage.startsWith( "." ) )
			{
				resourcePackage = resourcePackage.substring( 1 );
			}
		}
		resourcePackage = DEFAULT_PACKAGE + resourcePackage;
		return new UserPackage( resourcePackage );
	}

	public static NamedUserConstructor createConstructorForResourceClass( Class<?> resourceClass, ConstructorParameterPair constructorAndParameter )
	{
		UserParameter parameter = new UserParameter( "modelResource", resourceClass );
		ParameterAccess parameterAccessor = new ParameterAccess( parameter );
		SimpleArgument superArgument = new SimpleArgument( constructorAndParameter.getParameter(), parameterAccessor );
		ConstructorInvocationStatement superInvocation = new SuperConstructorInvocationStatement( constructorAndParameter.getConstructor(), superArgument );
		ConstructorBlockStatement blockStatement = new ConstructorBlockStatement( superInvocation );
		UserParameter[] parameters = { parameter };
		NamedUserConstructor constructor = new NamedUserConstructor( parameters, blockStatement );
		return constructor;
	}

	public static NamedUserConstructor createConstructorForResourceField( Field resourceField, ConstructorParameterPair constructorAndParameter )
	{
		JavaField javaField = JavaField.getInstance( resourceField );
		FieldAccess fieldAccess = org.lgna.project.ast.AstUtilities.createStaticFieldAccess( javaField );
		SimpleArgument superArgument = new SimpleArgument( constructorAndParameter.getParameter(), fieldAccess );
		ConstructorInvocationStatement superInvocation = new SuperConstructorInvocationStatement( constructorAndParameter.getConstructor(), superArgument );
		ConstructorBlockStatement blockStatement = new ConstructorBlockStatement( superInvocation );
		UserParameter[] parameters = {};
		NamedUserConstructor constructor = new NamedUserConstructor( parameters, blockStatement );
		return constructor;
	}

	public static ConstructorParameterPair getConstructorAndParameterForAliceClass( NamedUserType aliceType )
	{
		for( int i = 0; i < aliceType.constructors.size(); i++ )
		{
			NamedUserConstructor constructor = aliceType.constructors.get( i );
			if( constructor.requiredParameters.size() == 1 )
			{
				UserParameter parameter = constructor.requiredParameters.get( 0 );
				if( parameter.getValueType().isAssignableTo( ModelResource.class ) )
				{
					return new ConstructorParameterPair( constructor, parameter );
				}
			}
		}
		return null;
	}

	public static ConstructorParameterPair getConstructorAndParameterForJavaClass( Class<?> javaClass )
	{
		JavaType javeType = JavaType.getInstance( javaClass );
		List<JavaConstructor> constructors = javeType.getDeclaredConstructors();
		for( JavaConstructor constructor : constructors )
		{
			List<JavaConstructorParameter> parameters = (List<JavaConstructorParameter>)constructor.getRequiredParameters();
			if( parameters.size() == 1 )
			{
				JavaConstructorParameter parameter = parameters.get( 0 );
				JavaType javaType = parameter.getValueTypeDeclaredInJava();
				if( javaType.isAssignableTo( ModelResource.class ) )
				{
					return new ConstructorParameterPair( constructor, parameter );
				}
			}
		}
		return null;
	}

	public static UserMethod getPartAccessorMethod( Field partField )
	{
		String methodName = "get" + getAliceMethodNameForEnum( partField.getName() );
		Class<?> returnClass = org.lgna.story.SJoint.class;
		UserParameter[] parameters = {};
		org.lgna.project.ast.JavaType jointIdType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.JointId.class );
		org.lgna.project.ast.TypeExpression typeExpression = new org.lgna.project.ast.TypeExpression( org.lgna.story.SJoint.class );
		Class<?>[] methodParameterClasses = { org.lgna.story.SJointedModel.class, org.lgna.story.resources.JointId.class };
		org.lgna.project.ast.JavaMethod methodExpression = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SJoint.class, "getJoint", methodParameterClasses );

		org.lgna.project.ast.SimpleArgument thisArgument = new org.lgna.project.ast.SimpleArgument( methodExpression.getRequiredParameters().get( 0 ), new org.lgna.project.ast.ThisExpression() );

		org.lgna.project.ast.FieldAccess jointField = new org.lgna.project.ast.FieldAccess(
				new org.lgna.project.ast.TypeExpression( jointIdType ),
				org.lgna.project.ast.JavaField.getInstance( partField.getDeclaringClass(), partField.getName() )
				);

		org.lgna.project.ast.SimpleArgument jointArgument = new org.lgna.project.ast.SimpleArgument( methodExpression.getRequiredParameters().get( 1 ), jointField );

		org.lgna.project.ast.SimpleArgument[] methodArguments = { thisArgument, jointArgument };
		org.lgna.project.ast.MethodInvocation getJointMethodInvocation = new org.lgna.project.ast.MethodInvocation( typeExpression, methodExpression, methodArguments );
		ReturnStatement returnStatement = new ReturnStatement( jointIdType, getJointMethodInvocation );
		UserMethod newMethod = new UserMethod( methodName, returnClass, parameters, new BlockStatement( returnStatement ) );
		return newMethod;
	}

	public static UserMethod[] getPartAccessorMethods( Class<? extends org.lgna.story.resources.ModelResource> forClass )
	{
		Field[] jointFields = AliceResourceClassUtilities.getFieldsOfType( forClass, org.lgna.story.resources.JointId.class );
		List<UserMethod> methods = new LinkedList<UserMethod>();
		for( Field f : jointFields )
		{
			boolean visible = true;
			if( f.isAnnotationPresent( org.lgna.project.annotations.FieldTemplate.class ) ) {
				org.lgna.project.annotations.FieldTemplate propertyFieldTemplate = f.getAnnotation( org.lgna.project.annotations.FieldTemplate.class );
				visible = propertyFieldTemplate.visibility() != org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN;
			}
			if( visible ) {
				methods.add( getPartAccessorMethod( f ) );
			}
		}
		return methods.toArray( new UserMethod[ methods.size() ] );
	}
}
