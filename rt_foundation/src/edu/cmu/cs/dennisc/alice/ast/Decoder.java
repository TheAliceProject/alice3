/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class Decoder {
	private String srcVersion;
	private String dstVersion;

	public Decoder( String srcVersion, String dstVersion ) {
		this.srcVersion = srcVersion;
		this.dstVersion = dstVersion;
	}

	private String getClassName( org.w3c.dom.Element xmlElement ) {
		String rv = xmlElement.getAttribute( CodecConstants.TYPE_ATTRIBUTE );
		if( this.srcVersion.contains( "alpha" ) ) {
			if( this.dstVersion.contains( "beta" ) ) {
				//todo
			}
		}
		return rv;
	}
	private Class< ? > getCls( org.w3c.dom.Element xmlElement ) {
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( getClassName( xmlElement ) );
	}
	private Object newInstance( org.w3c.dom.Element xmlElement ) {
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( getClassName( xmlElement ) );
	}

	public Object decodeValue( org.w3c.dom.Element xmlValue, java.util.Map< Integer, AbstractDeclaration > map ) {
		Object rv;
		if( xmlValue.hasAttribute( "isNull" ) ) {
			rv = null;
		} else {
			String tagName = xmlValue.getTagName();
			if( tagName.equals( "node" ) ) {
				try {
					rv = decode( xmlValue, map );
				} catch( RuntimeException re ) {
					re.printStackTrace();
					rv = new NullLiteral();
				}
			} else if( tagName.equals( "collection" ) ) {
				java.util.Collection collection = (java.util.Collection)newInstance( xmlValue );
				org.w3c.dom.NodeList nodeList = xmlValue.getChildNodes();
				for( int i = 0; i < nodeList.getLength(); i++ ) {
					org.w3c.dom.Element xmlItem = (org.w3c.dom.Element)nodeList.item( i );
					collection.add( decodeValue( xmlItem, map ) );
				}
				rv = collection;
			} else if( tagName.equals( "value" ) ) {
				Class< ? > cls = getCls( xmlValue );
				String textContent = xmlValue.getTextContent();
				if( cls.equals( String.class ) ) {
					rv = textContent;
				} else {
					try {
						rv = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.valueOf( cls, textContent );
					} catch( RuntimeException re ) {
						if( "DIVIDE".equals( textContent ) ) {
							rv = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.valueOf( cls, "REAL_DIVIDE" );
						} else if( "REMAINDER".equals( textContent ) ) {
							rv = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.valueOf( cls, "REAL_REMAINDER" );
						} else {
							throw re;
						}
					}
				}
			} else {
				throw new RuntimeException();
			}
		}
		return rv;
	}

	private Class< ? > decodeType( org.w3c.dom.Element xmlElement, String nodeName ) {
		org.w3c.dom.Element xmlClass = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, nodeName );
		String clsName = xmlClass.getAttribute( "name" );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( clsName );
	}

	private ArrayTypeDeclaredInAlice decodeArrayTypeDeclaredInAlice( org.w3c.dom.Element xmlElement, java.util.Map< Integer, AbstractDeclaration > map ) {
		org.w3c.dom.Element xmlLeafType = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "leafType" );
		org.w3c.dom.Element xmlDimensionCount = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "dimensionCount" );
		org.w3c.dom.Element xmlLeafTypeNode = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "node" );
		TypeDeclaredInAlice leafType = (TypeDeclaredInAlice)decode( xmlLeafTypeNode, map );
		int dimensionCount = Integer.parseInt( xmlDimensionCount.getTextContent() );
		return ArrayTypeDeclaredInAlice.get( leafType, dimensionCount );
	}

	private Class< ? > decodeDeclaringClass( org.w3c.dom.Element xmlElement ) {
		return decodeType( xmlElement, "declaringClass" );
	}
	private Class< ? >[] decodeParameters( org.w3c.dom.Element xmlElement ) {
		org.w3c.dom.Element xmlParameters = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "parameters" );
		org.w3c.dom.Element[] xmlTypes = edu.cmu.cs.dennisc.xml.XMLUtilities.getElementsByTagNameAsArray( xmlParameters, "type" );
		Class< ? >[] rv = new Class< ? >[ xmlTypes.length ];
		for( int i = 0; i < xmlTypes.length; i++ ) {
			rv[ i ] = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( xmlTypes[ i ].getAttribute( "name" ) );
		}
		return rv;
	}
	private java.lang.reflect.Field decodeField( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlField = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlParent, nodeName );
		Class< ? > declaringCls = decodeDeclaringClass( xmlField );
		String name = xmlField.getAttribute( "name" );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getDeclaredField( declaringCls, name );
	}
	private java.lang.reflect.Constructor< ? > decodeConstructor( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlConstructor = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlParent, nodeName );
		Class< ? > declaringCls = decodeDeclaringClass( xmlConstructor );
		Class< ? >[] parameterTypes = decodeParameters( xmlConstructor );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getDeclaredConstructor( declaringCls, parameterTypes );
	}
	
	
	private static edu.cmu.cs.dennisc.map.MapToMap< Class<?>, String, Class<?> > mapToMap = new edu.cmu.cs.dennisc.map.MapToMap< Class<?>, String, Class<?> >();
	public static void addMethodFilter( Class<?> prevCls, String name, Class<?> nextCls ) {
		Decoder.mapToMap.put( prevCls, name, nextCls );
	}
	private static Class<?> filterClsIfNecessary( Class<?> cls, String name ) {
		Class<?> rv = Decoder.mapToMap.get( cls, name );
		if( rv != null ) {
			//pass
		} else {
			rv = cls;
		}
		return rv;
	}
	
	private java.lang.reflect.Method decodeMethod( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlMethod = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlParent, nodeName );
		Class< ? > declaringCls = decodeDeclaringClass( xmlMethod );
		String name = xmlMethod.getAttribute( "name" );
		Class< ? >[] parameterTypes = decodeParameters( xmlMethod );
		java.lang.reflect.Method rv;
		try {
			rv = declaringCls.getDeclaredMethod( name, parameterTypes );
		} catch( NoSuchMethodException nsme1 ) {
			declaringCls = Decoder.filterClsIfNecessary( declaringCls, name );
//			if( declaringCls.getName().equals( "org.alice.apis.moveandturn.Transformable" ) ) {
//				declaringCls = edu.cmu.cs.dennisc.lang.ClassUtilities.forName( "org.alice.apis.moveandturn.Model" );
//			}
			try {
				rv = declaringCls.getDeclaredMethod( name, parameterTypes );
			} catch( NoSuchMethodException nsme2 ) {
				rv = null;
			}
		}
		assert rv != null : name;
		return rv;
	}

	public Node decode( org.w3c.dom.Element xmlElement, java.util.Map< Integer, AbstractDeclaration > map ) {
		Node rv;
		if( xmlElement.hasAttribute( CodecConstants.TYPE_ATTRIBUTE ) ) {
			String clsName = getClassName( xmlElement );
			if( clsName.equals( TypeDeclaredInJava.class.getName() ) ) {
				Class< ? > cls = decodeType( xmlElement, "type" );
				rv = TypeDeclaredInJava.get( cls );
			} else if( clsName.equals( ArrayTypeDeclaredInAlice.class.getName() ) ) {
				rv = decodeArrayTypeDeclaredInAlice( xmlElement, map );
			} else if( clsName.equals( ConstructorDeclaredInJava.class.getName() ) ) {
				java.lang.reflect.Constructor< ? > cnstrctr = decodeConstructor( xmlElement, "constructor" );
				rv = TypeDeclaredInJava.getConstructor( cnstrctr );
			} else if( clsName.equals( MethodDeclaredInJava.class.getName() ) ) {
				java.lang.reflect.Method mthd = decodeMethod( xmlElement, "method" );
				rv = TypeDeclaredInJava.getMethod( mthd );
			} else if( clsName.equals( FieldDeclaredInJavaWithField.class.getName() ) ) {
				java.lang.reflect.Field fld = decodeField( xmlElement, "field" );
				rv = TypeDeclaredInJava.getField( fld );
			} else if( clsName.equals( FieldDeclaredInJavaWithGetterAndSetter.class.getName() ) ) {
				java.lang.reflect.Method gttr = decodeMethod( xmlElement, "getter" );
				java.lang.reflect.Method sttr = decodeMethod( xmlElement, "setter" );
				rv = TypeDeclaredInJava.getField( gttr, sttr );
			} else if( clsName.equals( ParameterDeclaredInJavaConstructor.class.getName() ) ) {
				org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
				assert nodeList.getLength() == 2;
				org.w3c.dom.Element xmlConstructor = (org.w3c.dom.Element)nodeList.item( 0 );
				ConstructorDeclaredInJava constructorDeclaredInJava = (ConstructorDeclaredInJava)decodeValue( xmlConstructor, map );
				org.w3c.dom.Element xmlIndex = (org.w3c.dom.Element)nodeList.item( 1 );
				int index = Integer.parseInt( xmlIndex.getTextContent() );
				rv = constructorDeclaredInJava.getParameters().get( index );
			} else if( clsName.equals( ParameterDeclaredInJavaMethod.class.getName() ) ) {
				org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
				assert nodeList.getLength() == 2;
				org.w3c.dom.Element xmlMethod = (org.w3c.dom.Element)nodeList.item( 0 );
				MethodDeclaredInJava methodDeclaredInJava = (MethodDeclaredInJava)decodeValue( xmlMethod, map );
				org.w3c.dom.Element xmlIndex = (org.w3c.dom.Element)nodeList.item( 1 );
				int index = Integer.parseInt( xmlIndex.getTextContent() );
				rv = methodDeclaredInJava.getParameters().get( index );
			} else {
				rv = (Node)newInstance( xmlElement );
				assert rv != null;
			}
			if( rv instanceof AbstractDeclaration ) {
				int key = Integer.parseInt( xmlElement.getAttribute( CodecConstants.KEY_ATTRIBUTE ), 16 );
				map.put( key, (AbstractDeclaration)rv );
			}
			rv.decodeNode( this, xmlElement, map );
			if( xmlElement.hasAttribute( CodecConstants.UUID_ATTRIBUTE ) ) {
				rv.setUUID( java.util.UUID.fromString( xmlElement.getAttribute( CodecConstants.UUID_ATTRIBUTE ) ) );
			}
		} else {
			int key = Integer.parseInt( xmlElement.getAttribute( CodecConstants.KEY_ATTRIBUTE ), 16 );
			rv = map.get( key );
			assert rv != null;
		}
		return rv;
	}
}
