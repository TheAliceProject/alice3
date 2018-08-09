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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.project.annotations.ResourceTemplate;
import org.lgna.story.SModel;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.ModelResource;

/**
 * @author dculyba
 * 
 */
public class AliceResourceClassUtilities {
	public static final String DEFAULT_PACKAGE = "";
	public static final String RESOURCE_SUFFIX = "Resource";

	private AliceResourceClassUtilities() {
		throw new AssertionError();
	}

	public static boolean isTopLevelResource( Class<? extends ModelResource> resourceClass ) {
		if( resourceClass.isAnnotationPresent( ResourceTemplate.class ) ) {
			ResourceTemplate resourceTemplate = resourceClass.getAnnotation( ResourceTemplate.class );
			return resourceTemplate.isTopLevelResource();
		} else {
			return false;
		}
	}

	public static Class<? extends SModel> getModelClassForResourceClass( Class<? extends ModelResource> resourceClass ) {
		if( resourceClass.isAnnotationPresent( ResourceTemplate.class ) ) {
			ResourceTemplate resourceTemplate = resourceClass.getAnnotation( ResourceTemplate.class );
			Class<?> cls = resourceTemplate.modelClass();
			if( SModel.class.isAssignableFrom( cls ) ) {
				return (Class<? extends SModel>)cls;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static Class<? extends ModelResource> getResourceClassForModelClass( Class<? extends SModel> modelClass ) {
		Constructor<?>[] constructors = modelClass.getConstructors();
		Constructor<?> firstConstructor = ( constructors != null ) && ( constructors.length > 0 ) ? constructors[ 0 ] : null;
		if( firstConstructor != null ) {
			Class<?>[] parameterTypes = firstConstructor.getParameterTypes();
			if( ( parameterTypes != null ) ) {
				for( Class<?> parameterType : parameterTypes ) {
					if( ModelResource.class.isAssignableFrom( parameterType ) ) {
						return (Class<? extends ModelResource>)parameterType;
					}
				}
			}
		}
		return null;
	}

	public static Class<? extends ModelResource> getResourceClassForAliceName( String aliceClassName ) {
		String className = "org.lgna.story.resources." + aliceClassName + RESOURCE_SUFFIX;
		try {
			return (Class<? extends ModelResource>)Class.forName(className);
		} catch (ClassNotFoundException e) {
			Logger.warning("No class found for "+className);
			return null;
		}
	}

	public static String getAliceMethodNameForEnum( String enumName ) {
		StringBuilder sb = new StringBuilder();
		String[] nameParts = enumName.split( "_" );
		for( String s : nameParts ) {
			sb.append( uppercaseFirstLetter( s ) );
		}
		return sb.toString();
	}

	public static String getAliceClassName( String name ) {
		int resourceIndex = name.indexOf( RESOURCE_SUFFIX );
		if( resourceIndex != -1 ) {
			name = name.substring( 0, resourceIndex );
		}
		name = getClassNameFromName( name );
		return name;
	}

	public static String getAliceClassName( Class<?> resourceClass ) {
		return getAliceClassName( resourceClass.getSimpleName() );
	}

	public static List<String> splitOnCapitalsAndNumbers( String s ) {
		StringBuilder sb = new StringBuilder();
		List<String> split = Lists.newLinkedList();
		boolean isOnNumber = false;
		for( int i = 0; i < s.length(); i++ ) {
			boolean shouldRestart = false;
			if( Character.isDigit( s.charAt( i ) ) ) {
				if( !isOnNumber ) {
					shouldRestart = true;
				}
				isOnNumber = true;
			} else {
				if( isOnNumber ) {
					shouldRestart = true;
				}
				isOnNumber = false;
			}
			if( Character.isUpperCase( s.charAt( i ) ) ) {
				shouldRestart = true;
			}
			if( shouldRestart ) {
				split.add( sb.toString() );
				sb = new StringBuilder();
			}
			sb.append( s.charAt( i ) );
		}
		if( sb.length() > 0 ) {
			split.add( sb.toString() );
		}
		return split;
	}

	public static String uppercaseFirstLetter( String s ) {
		if( s == null ) {
			return null;
		}
		if( s.length() <= 1 ) {
			return s.toUpperCase();
		}
		return s.substring( 0, 1 ).toUpperCase( Locale.ENGLISH ) + s.substring( 1 ).toLowerCase( Locale.ENGLISH );
	}

	public static String[] fullStringSplit( String name ) {
		List<String> strings = Lists.newLinkedList();
		String[] nameParts = name.split( "[_ -]" );
		for( String s : nameParts ) {
			List<String> capitalSplit = splitOnCapitalsAndNumbers( s );
			for( String subS : capitalSplit ) {
				if( subS.length() > 0 ) {
					strings.add( subS );
				}
			}
		}
		return strings.toArray( new String[ strings.size() ] );
	}

	public static String getClassNameFromName( String name ) {
		StringBuilder sb = new StringBuilder();
		String[] nameParts = fullStringSplit( name );
		for( String s : nameParts ) {
			sb.append( uppercaseFirstLetter( s ) );
		}
		return sb.toString();
	}

	public static Field[] getFieldsOfType( Class<?> ownerClass, Class<?> ofType ) {
		Field[] fields = ownerClass.getFields();
		List<Field> fieldsOfType = Lists.newLinkedList();
		for( Field f : fields ) {
			boolean matchesType = ofType.isAssignableFrom( f.getType() );
			boolean matchesOwner = f.getDeclaringClass() == ownerClass;
			if( matchesType && matchesOwner ) {
				fieldsOfType.add( f );
			}
		}
		return fieldsOfType.toArray( new Field[ fieldsOfType.size() ] );
	}


	public static List<JointId> getJoints(Class<? extends ModelResource> resourceClass) {
		Field[] jointFields = getFieldsOfType(resourceClass, JointId.class);
		List<JointId> baseJoints = new ArrayList<>();
		for (Field f : jointFields) {
			try {
				JointId id = (JointId) f.get(null);
				baseJoints.add(id);
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
			}
		}
		return baseJoints;
	}

}
