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
public abstract class Node extends edu.cmu.cs.dennisc.pattern.DefaultInstancePropertyOwner/* implements java.io.Serializable*/implements edu.cmu.cs.dennisc.pattern.Crawlable {
	private java.util.UUID m_uuid = java.util.UUID.randomUUID();
	//public edu.cmu.cs.dennisc.property.InstanceProperty< java.util.UUID > uuid = new edu.cmu.cs.dennisc.property.InstanceProperty< java.util.UUID >( this, java.util.UUID.randomUUID() );
	//public edu.cmu.cs.dennisc.property.ListProperty< java.util.UUID > uuidHistory = new edu.cmu.cs.dennisc.property.ListProperty< java.util.UUID >( this );

	
	public java.util.UUID getUUID() {
		return m_uuid;
	}
	
	@Override
	public boolean isComposedOfGetterAndSetterProperties() {
		return false;
	}

	private static void acceptIfCrawlable( java.util.Set< edu.cmu.cs.dennisc.pattern.Crawlable > alreadyVisited, Object value, edu.cmu.cs.dennisc.pattern.Crawler crawler ) {
		if( value instanceof edu.cmu.cs.dennisc.pattern.Crawlable ) {
			edu.cmu.cs.dennisc.pattern.Crawlable crawlable = (edu.cmu.cs.dennisc.pattern.Crawlable)value;
			crawlable.accept( alreadyVisited, crawler );
		}
	}

	public void accept( java.util.Set< edu.cmu.cs.dennisc.pattern.Crawlable > alreadyVisited, edu.cmu.cs.dennisc.pattern.Crawler crawler ) {
		if( alreadyVisited.contains( this ) ) {
			//pass
		} else {
			alreadyVisited.add( this );
			crawler.visit( this );
			for( edu.cmu.cs.dennisc.property.Property< ? > property : this.getProperties() ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( property.getName() );
				Object value = property.getValue( this );
				if( value instanceof Iterable ) {
					Iterable iterable = (Iterable)value;
					for( Object item : iterable ) {
						acceptIfCrawlable( alreadyVisited, item, crawler );
					}
				} else if( value instanceof Object[] ) {
					Object[] array = (Object[])value;
					for( Object item : array ) {
						acceptIfCrawlable( alreadyVisited, item, crawler );
					}
				} else {
					acceptIfCrawlable( alreadyVisited, value, crawler );
				}
			}
		}
	}
	
	public final void crawl( edu.cmu.cs.dennisc.pattern.Crawler crawler ) {
		accept( new java.util.HashSet< edu.cmu.cs.dennisc.pattern.Crawlable >(), crawler );
	}

	//	protected void crawl( java.util.Set< AbstractType > types, edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
	//		visitor.visit( this );
	//	}
	//	public void crawl( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
	//		java.util.Set< AbstractType > types = new java.util.HashSet< AbstractType >();
	//		crawl( types, visitor );
	//	}

	private static final String KEY_ATTRIBUTE = "key";
	private static final String TYPE_ATTRIBUTE = "type";
	private static final String UUID_ATTRIBUTE = "uuid";

	private static org.w3c.dom.Element encodeValue( Object value, org.w3c.dom.Document xmlDocument, java.util.Set< AbstractDeclaration > set ) {
		org.w3c.dom.Element rv;
		if( value instanceof Node ) {
			Node node = (Node)value;
			rv = node.encode( xmlDocument, set );
		} else if( value instanceof java.util.Collection ) {
			rv = xmlDocument.createElement( "collection" );
			rv.setAttribute( TYPE_ATTRIBUTE, value.getClass().getName() );
			java.util.Collection< ? > collection = (java.util.Collection< ? >)value;
			for( Object item : collection ) {
				rv.appendChild( encodeValue( item, xmlDocument, set ) );
			}
		} else {
			rv = xmlDocument.createElement( "value" );
			if( value != null ) {
				rv.setAttribute( TYPE_ATTRIBUTE, value.getClass().getName() );
				rv.appendChild( xmlDocument.createTextNode( value.toString() ) );
			} else {
				rv.setAttribute( "isNull", "true" );
			}
		}
		return rv;
	}

	protected final org.w3c.dom.Element encodeProperty( org.w3c.dom.Document xmlDocument, edu.cmu.cs.dennisc.property.Property property, java.util.Set< AbstractDeclaration > set ) {
		org.w3c.dom.Element xmlProperty = xmlDocument.createElement( "property" );
		xmlProperty.setAttribute( "name", property.getName() );
		Object value = property.getValue( this );
		xmlProperty.appendChild( encodeValue( value, xmlDocument, set ) );
		return xmlProperty;
	}
	private static org.w3c.dom.Element encodeType( org.w3c.dom.Document xmlDocument, String nodeName, Class< ? > type ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( nodeName );
		rv.setAttribute( "name", type.getName() );
		return rv;
	}
	private static org.w3c.dom.Element encodeDeclaringClass( org.w3c.dom.Document xmlDocument, java.lang.reflect.Member mmbr ) {
		return encodeType( xmlDocument, "declaringClass", mmbr.getDeclaringClass() );
	}
	private static org.w3c.dom.Element encodeParameters( org.w3c.dom.Document xmlDocument, Class< ? >[] parameterTypes ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( "parameters" );
		for( Class< ? > parameterType : parameterTypes ) {
			rv.appendChild( encodeType( xmlDocument, "type", parameterType ) );
		}
		return rv;
	}
	private static org.w3c.dom.Element encodeMember( org.w3c.dom.Document xmlDocument, String nodeName, java.lang.reflect.Member mmbr ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( nodeName );
		String name = mmbr.getName();
		if( name == null ) {
			assert mmbr instanceof java.lang.reflect.Constructor< ? >;
		} else {
			rv.setAttribute( "name", name );
		}
		rv.appendChild( encodeDeclaringClass( xmlDocument, mmbr ) );
		return rv;
	}
	private static org.w3c.dom.Element encodeField( org.w3c.dom.Document xmlDocument, String nodeName, java.lang.reflect.Field fld ) {
		org.w3c.dom.Element rv = encodeMember( xmlDocument, nodeName, fld );
		return rv;
	}
	private static org.w3c.dom.Element encodeConstructor( org.w3c.dom.Document xmlDocument, String nodeName, java.lang.reflect.Constructor cnstrctr ) {
		org.w3c.dom.Element rv = encodeMember( xmlDocument, nodeName, cnstrctr );
		rv.appendChild( encodeParameters( xmlDocument, cnstrctr.getParameterTypes() ) );
		return rv;
	}
	private static org.w3c.dom.Element encodeMethod( org.w3c.dom.Document xmlDocument, String nodeName, java.lang.reflect.Method mthd ) {
		org.w3c.dom.Element rv = encodeMember( xmlDocument, nodeName, mthd );
		rv.appendChild( encodeParameters( xmlDocument, mthd.getParameterTypes() ) );
		return rv;
	}
	private org.w3c.dom.Element encode( org.w3c.dom.Document xmlDocument, java.util.Set< AbstractDeclaration > set ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( "node" );
		if( this instanceof AbstractDeclaration ) {
			AbstractDeclaration abstractDeclaration = (AbstractDeclaration)this;
			rv.setAttribute( KEY_ATTRIBUTE, Integer.toHexString( abstractDeclaration.hashCode() ) );
			if( set.contains( this ) ) {
				return rv;
			} else {
				set.add( abstractDeclaration );
			}
		}
		rv.setAttribute( UUID_ATTRIBUTE, m_uuid.toString() );
		rv.setAttribute( TYPE_ATTRIBUTE, getClass().getName() );
		if( this instanceof TypeDeclaredInJava ) {
			TypeDeclaredInJava typeDeclaredInJava = (TypeDeclaredInJava)this;
			Class< ? > cls = typeDeclaredInJava.getCls();
			rv.appendChild( encodeType( xmlDocument, "type", cls ) );
		} else if( this instanceof ArrayTypeDeclaredInAlice ) {
			ArrayTypeDeclaredInAlice arrayTypeDeclaredInAlice = (ArrayTypeDeclaredInAlice)this;

			org.w3c.dom.Element xmlLeafType = xmlDocument.createElement( "leafType" );
			xmlLeafType.appendChild( encodeValue( arrayTypeDeclaredInAlice.getLeafType(), xmlDocument, set ) );
			rv.appendChild( xmlLeafType );
			
			org.w3c.dom.Element xmlDimensionCount = xmlDocument.createElement( "dimensionCount" );
			xmlDimensionCount.appendChild( xmlDocument.createTextNode( Integer.toString( arrayTypeDeclaredInAlice.getDimensionCount() ) ) );
			rv.appendChild( xmlDimensionCount );
		
		} else if( this instanceof ConstructorDeclaredInJava ) {
			ConstructorDeclaredInJava constructorDeclaredInJava = (ConstructorDeclaredInJava)this;
			java.lang.reflect.Constructor< ? > cnstrctr = constructorDeclaredInJava.getCnstrctr();
			rv.appendChild( encodeConstructor( xmlDocument, "constructor", cnstrctr ) );
		} else if( this instanceof MethodDeclaredInJava ) {
			MethodDeclaredInJava methodDeclaredInJava = (MethodDeclaredInJava)this;
			java.lang.reflect.Method mthd = methodDeclaredInJava.getMthd();
			rv.appendChild( encodeMethod( xmlDocument, "method", mthd ) );
		} else if( this instanceof FieldDeclaredInJavaWithField ) {
			FieldDeclaredInJavaWithField fieldDeclaredInJavaWithField = (FieldDeclaredInJavaWithField)this;
			java.lang.reflect.Field fld = fieldDeclaredInJavaWithField.getFld();
			rv.appendChild( encodeField( xmlDocument, "field", fld ) );
		} else if( this instanceof FieldDeclaredInJavaWithGetterAndSetter ) {
			FieldDeclaredInJavaWithGetterAndSetter fieldDeclaredInJavaWithGetterAndSetter = (FieldDeclaredInJavaWithGetterAndSetter)this;
			java.lang.reflect.Method gttr = fieldDeclaredInJavaWithGetterAndSetter.getGttr();
			java.lang.reflect.Method sttr = fieldDeclaredInJavaWithGetterAndSetter.getSttr();
			rv.appendChild( encodeMethod( xmlDocument, "getter", gttr ) );
			rv.appendChild( encodeMethod( xmlDocument, "setter", sttr ) );
		} else if( this instanceof ParameterDeclaredInJavaConstructor ) {
			ParameterDeclaredInJavaConstructor parameterDeclaredInJavaConstructor = (ParameterDeclaredInJavaConstructor)this;
			ConstructorDeclaredInJava constructor = parameterDeclaredInJavaConstructor.getConstructor();
			rv.appendChild( encodeValue( constructor, xmlDocument, set ) );

			org.w3c.dom.Element xmlIndex = xmlDocument.createElement( "index" );
			xmlIndex.appendChild( xmlDocument.createTextNode( Integer.toString( parameterDeclaredInJavaConstructor.getIndex() ) ) );
			rv.appendChild( xmlIndex );
		} else if( this instanceof ParameterDeclaredInJavaMethod ) {
			ParameterDeclaredInJavaMethod parameterDeclaredInJavaMethod = (ParameterDeclaredInJavaMethod)this;
			MethodDeclaredInJava method = parameterDeclaredInJavaMethod.getMethod();
			rv.appendChild( encodeValue( method, xmlDocument, set ) );

			org.w3c.dom.Element xmlIndex = xmlDocument.createElement( "index" );
			xmlIndex.appendChild( xmlDocument.createTextNode( Integer.toString( parameterDeclaredInJavaMethod.getIndex() ) ) );
			rv.appendChild( xmlIndex );
		}
		for( edu.cmu.cs.dennisc.property.Property property : getProperties() ) {
			rv.appendChild( encodeProperty( xmlDocument, property, set ) );
		}
		return rv;
	}

	private static final double VERSION = 3.0;

	public final org.w3c.dom.Document encode( java.util.Set< AbstractDeclaration > set ) {
		org.w3c.dom.Document rv = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
		org.w3c.dom.Element xmlElement = encode( rv, set );
		xmlElement.setAttribute( "version", Double.toString( VERSION ) );
		rv.appendChild( xmlElement );
		return rv;
	}
	public final org.w3c.dom.Document encode() {
		return encode( new java.util.HashSet< AbstractDeclaration >() );
	}

	private static Object decodeValue( org.w3c.dom.Element xmlValue, java.util.Map< Integer, AbstractDeclaration > map ) {
		Object rv;
		if( xmlValue.hasAttribute( "isNull" ) ) {
			rv = null;
		} else {
			String tagName = xmlValue.getTagName();
			if( tagName.equals( "node" ) ) {
				rv = Node.decode( xmlValue, map );
			} else if( tagName.equals( "collection" ) ) {
				java.util.Collection collection = (java.util.Collection)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( xmlValue.getAttribute( TYPE_ATTRIBUTE ) );
				org.w3c.dom.NodeList nodeList = xmlValue.getChildNodes();
				for( int i = 0; i < nodeList.getLength(); i++ ) {
					org.w3c.dom.Element xmlItem = (org.w3c.dom.Element)nodeList.item( i );
					collection.add( decodeValue( xmlItem, map ) );
				}
				rv = collection;
			} else if( tagName.equals( "value" ) ) {
				Class< ? > cls = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( xmlValue.getAttribute( TYPE_ATTRIBUTE ) );
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

	protected void postDecode() {
	}
	protected final void decodeNode( org.w3c.dom.Element xmlElement, java.util.Map< Integer, AbstractDeclaration > map ) {
		org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
		for( int i = 0; i < nodeList.getLength(); i++ ) {
			org.w3c.dom.Node xmlNode = nodeList.item( i );
			assert xmlNode instanceof org.w3c.dom.Element : xmlNode;
			org.w3c.dom.Element xmlProperty = (org.w3c.dom.Element)xmlNode;
			if( xmlProperty.getTagName().equals( "property" ) ) {
				edu.cmu.cs.dennisc.property.Property property = this.getPropertyNamed( xmlProperty.getAttribute( "name" ) );
				Object value = decodeValue( (org.w3c.dom.Element)xmlProperty.getFirstChild(), map );
				property.setValue( this, value );
			}
		}
		postDecode();
	}
	private static Class< ? > decodeType( org.w3c.dom.Element xmlElement, String nodeName ) {
		org.w3c.dom.Element xmlClass = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, nodeName );
		String clsName = xmlClass.getAttribute( "name" );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( clsName );
	}

	private static ArrayTypeDeclaredInAlice decodeArrayTypeDeclaredInAlice( org.w3c.dom.Element xmlElement, java.util.Map< Integer, AbstractDeclaration > map ) {
		org.w3c.dom.Element xmlLeafType = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "leafType" );
		org.w3c.dom.Element xmlDimensionCount = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "dimensionCount" );
		org.w3c.dom.Element xmlLeafTypeNode = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "node" );
		TypeDeclaredInAlice leafType = (TypeDeclaredInAlice)decode( xmlLeafTypeNode, map );
		int dimensionCount = Integer.parseInt( xmlDimensionCount.getTextContent() );
		return ArrayTypeDeclaredInAlice.get( leafType, dimensionCount );
	}

	
	private static Class< ? > decodeDeclaringClass( org.w3c.dom.Element xmlElement ) {
		return decodeType( xmlElement, "declaringClass" );
	}
	private static Class< ? >[] decodeParameters( org.w3c.dom.Element xmlElement ) {
		org.w3c.dom.Element xmlParameters = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlElement, "parameters" );
		org.w3c.dom.Element[] xmlTypes = edu.cmu.cs.dennisc.xml.XMLUtilities.getElementsByTagNameAsArray( xmlParameters, "type" );
		Class< ? >[] rv = new Class< ? >[ xmlTypes.length ];
		for( int i = 0; i < xmlTypes.length; i++ ) {
			rv[ i ] = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( xmlTypes[ i ].getAttribute( "name" ) );
		}
		return rv;
	}
	private static java.lang.reflect.Field decodeField( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlField = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlParent, nodeName );
		Class< ? > declaringCls = decodeDeclaringClass( xmlField );
		String name = xmlField.getAttribute( "name" );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getDeclaredField( declaringCls, name );
	}
	private static java.lang.reflect.Constructor decodeConstructor( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlConstructor = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlParent, nodeName );
		Class< ? > declaringCls = decodeDeclaringClass( xmlConstructor );
		Class< ? >[] parameterTypes = decodeParameters( xmlConstructor );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getDeclaredConstructor( declaringCls, parameterTypes );
	}
	private static java.lang.reflect.Method decodeMethod( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlMethod = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleElementByTagName( xmlParent, nodeName );
		Class< ? > declaringCls = decodeDeclaringClass( xmlMethod );
		String name = xmlMethod.getAttribute( "name" );
		Class< ? >[] parameterTypes = decodeParameters( xmlMethod );
		java.lang.reflect.Method rv = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getDeclaredMethod( declaringCls, name, parameterTypes );
		assert rv != null;
		return rv;
	}

	private static Node decode( org.w3c.dom.Element xmlElement, java.util.Map< Integer, AbstractDeclaration > map ) {
		Node rv;
		if( xmlElement.hasAttribute( TYPE_ATTRIBUTE ) ) {
			String clsName = xmlElement.getAttribute( TYPE_ATTRIBUTE );
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
				String typeName = xmlElement.getAttribute( TYPE_ATTRIBUTE );
				rv = (Node)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( typeName );
				assert rv != null;
			}
			if( rv instanceof AbstractDeclaration ) {
				int key = Integer.parseInt( xmlElement.getAttribute( KEY_ATTRIBUTE ), 16 );
				map.put( key, (AbstractDeclaration)rv );
			}
			rv.decodeNode( xmlElement, map );
			if( xmlElement.hasAttribute( UUID_ATTRIBUTE ) ) {
				rv.m_uuid = java.util.UUID.fromString( xmlElement.getAttribute( UUID_ATTRIBUTE ) );
			}
		} else {
			int key = Integer.parseInt( xmlElement.getAttribute( KEY_ATTRIBUTE ), 16 );
			rv = map.get( key );
			assert rv != null;
		}
		return rv;
	}

	protected java.util.Set< AbstractDeclaration > fillInDeclarationSet( java.util.Set< AbstractDeclaration > rv, java.util.Set< Node > nodes ) {
		nodes.add( this );
		for( edu.cmu.cs.dennisc.property.Property< ? > property : this.getProperties() ) {
			Object value = property.getValue( this );
			if( value instanceof Node ) {
				if( nodes.contains( value ) ) {
					//pass
				} else {
					((Node)value).fillInDeclarationSet( rv, nodes );
				}
			} else if( value instanceof Iterable ) {
				for( Object item : (Iterable)value ) {
					if( item instanceof Node ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							((Node)item).fillInDeclarationSet( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}
	public java.util.Set< AbstractDeclaration > createDeclarationSet() {
		java.util.Set< AbstractDeclaration > rv = new java.util.HashSet< AbstractDeclaration >();
		fillInDeclarationSet( rv, new java.util.HashSet< Node >() );
		return rv;
	}
	private java.util.Set< AbstractDeclaration > removeDeclarationsThatNeedToBeCopied( java.util.Set< AbstractDeclaration > rv, java.util.Set< Node > nodes ) {
		nodes.add( this );
		for( edu.cmu.cs.dennisc.property.Property< ? > property : this.getProperties() ) {
			if( property instanceof DeclarationProperty ) {
				DeclarationProperty< ? extends AbstractDeclaration > declarationProperty = (DeclarationProperty< ? extends AbstractDeclaration >)property;
				if( declarationProperty.isReference() ) { 
					//pass
				} else {
					rv.remove( declarationProperty.getValue() );
				}
			}
			Object value = property.getValue( this );
			if( value instanceof Node ) {
				if( nodes.contains( value ) ) {
					//pass
				} else {
					((Node)value).removeDeclarationsThatNeedToBeCopied( rv, nodes );
				}
			} else if( value instanceof Iterable ) {
				for( Object item : (Iterable)value ) {
					if( item instanceof Node ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							((Node)item).removeDeclarationsThatNeedToBeCopied( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}
	public java.util.Set< AbstractDeclaration > removeDeclarationsThatNeedToBeCopied( java.util.Set< AbstractDeclaration > rv ) {
		return removeDeclarationsThatNeedToBeCopied( rv, new java.util.HashSet< Node >() );
	}
	public static java.util.Map< Integer, AbstractDeclaration > createMapOfDeclarationsThatShouldNotBeCopied( java.util.Set< AbstractDeclaration > set ) {
		java.util.Map< Integer, AbstractDeclaration > rv = new java.util.HashMap< Integer, AbstractDeclaration >();
		for( AbstractDeclaration abstractDeclaration : set ) {
			rv.put( abstractDeclaration.hashCode(), abstractDeclaration );
		}
		return rv;
	}

	public static Node decode( org.w3c.dom.Document xmlDocument, java.util.Map< Integer, AbstractDeclaration > map ) {
		org.w3c.dom.Element xmlElement = xmlDocument.getDocumentElement();
		double version = Double.parseDouble( xmlElement.getAttribute( "version" ) );
		assert version == VERSION;
		return decode( xmlElement, map );
	}
	public static Node decode( org.w3c.dom.Document xmlDocument ) {
		return decode( xmlDocument, new java.util.HashMap< Integer, AbstractDeclaration >() );
	}

	protected void appendInternal( StringBuffer sb, java.util.Set< Node > set ) {
		sb.append( "name=" );
		sb.append( this.getName() );
		sb.append( ";" );
		sb.append( "@" );
		sb.append( this.hashCode() );
		sb.append( ";" );
		String separator = "";
		for( edu.cmu.cs.dennisc.property.Property< ? > property : getProperties() ) {
			sb.append( separator );
			sb.append( property.getName() );
			sb.append( "=" );
			Object value = property.getValue( this );
			if( value instanceof Node ) {
				Node node = (Node)value;
				if( set.contains( node ) ) {
					sb.append( "referenced@" );
					sb.append( node.hashCode() );
				} else {
					node.append( sb, set );
				}
			} else if( value instanceof java.util.Collection ) {
				java.util.Collection collection = (java.util.Collection)value;
				sb.append( "cls:" );
				sb.append( collection.getClass().getName() );
				sb.append( ";size:" );
				sb.append( collection.size() );
			} else {
				sb.append( value );
			}
			separator = ",";
		}
	}

	protected void append( StringBuffer sb, java.util.Set< Node > set ) {
		set.add( this );
		sb.append( this.getClass().getName() );
		sb.append( "[" );
		appendInternal( sb, set );
		sb.append( "]" );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		append( sb, new java.util.HashSet< Node >() );
		return sb.toString();
	}
}
