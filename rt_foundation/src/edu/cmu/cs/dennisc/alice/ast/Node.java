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
	private Node parent;
	
	public java.util.UUID getUUID() {
		return m_uuid;
	}
	/*package-private*/ void setUUID( java.util.UUID uuid ) {
		m_uuid = uuid;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	private void setParent( Node parent ) {
		if( this.parent != parent ) {
			if( this.parent != null ) {
				if( parent != null ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: setOwner previous not null", this );
				}
			}
			this.parent = parent;
		}
	}

	
	public <E extends Node> E getFirstAncestorAssignableTo( Class<E> cls, boolean isThisIncludedInSearch ) {
		Node rv;
		if( isThisIncludedInSearch ) {
			rv = this;
		} else {
			rv = this.getParent();
		}
		while( rv != null ) {
			if( cls.isAssignableFrom( rv.getClass() ) ) {
				break;
			}
			rv = rv.getParent();
		}
		return (E)rv;
	}
	public final <E extends Node> E getFirstAncestorAssignableTo( Class<E> cls ) {
		return getFirstAncestorAssignableTo( cls, false );
	}

	@Override
	public void firePropertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		super.firePropertyChanging( e );
		edu.cmu.cs.dennisc.property.Property< ? > property = e.getTypedSource();
		if( property instanceof NodeProperty< ? > ) {
			NodeProperty< ? > nodeProperty = (NodeProperty< ? >)property;
			boolean isReference;
			if( nodeProperty instanceof DeclarationProperty< ? > ) {
				isReference = ((DeclarationProperty< ? >)nodeProperty).isReference();
			} else {
				isReference = false;
			}
			if( isReference ) {
				//pass
			} else {
				Node node = nodeProperty.getValue();
				if( node != null ) {
					node.setParent( null );
				}
			}
		}
	}
	@Override
	public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		super.firePropertyChanged( e );
		edu.cmu.cs.dennisc.property.Property< ? > property = e.getTypedSource();
		if( property instanceof NodeProperty< ? > ) {
			NodeProperty< ? > nodeProperty = (NodeProperty< ? >)property;
			boolean isReference;
			if( nodeProperty instanceof DeclarationProperty< ? > ) {
				isReference = ((DeclarationProperty< ? >)nodeProperty).isReference();
			} else {
				isReference = false;
			}
			if( isReference ) {
				//pass
			} else {
				Node node = nodeProperty.getValue();
				if( node != null ) {
					node.setParent( this );
				}
			}
		}
	}
	@Override
	public void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		super.fireClearing( e );
		edu.cmu.cs.dennisc.property.ListProperty listProperty = (edu.cmu.cs.dennisc.property.ListProperty)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Node node : nodeListProperty ) {
				if( node != null ) {
					node.setParent( null );
				}
			}
		}
	}
	@Override
	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		super.fireRemoving( e );
		edu.cmu.cs.dennisc.property.ListProperty listProperty = (edu.cmu.cs.dennisc.property.ListProperty)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof Node ) {
					((Node)o).setParent( null );
				}
			}
		}
	}
	@Override
	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		super.fireSetting( e );
		edu.cmu.cs.dennisc.property.ListProperty listProperty = (edu.cmu.cs.dennisc.property.ListProperty)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof Node ) {
					((Node)o).setParent( null );
				}
			}
		}
	}
	@Override
	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		super.fireSet( e );
		edu.cmu.cs.dennisc.property.ListProperty listProperty = (edu.cmu.cs.dennisc.property.ListProperty)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof Node ) {
					((Node)o).setParent( this );
				}
			}
		}
	}
	@Override
	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		super.fireAdded( e );
		edu.cmu.cs.dennisc.property.ListProperty listProperty = (edu.cmu.cs.dennisc.property.ListProperty)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof Node ) {
					((Node)o).setParent( this );
				}
			}
		}
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

	//todo
	private static boolean isReferencedDeclarationPropertyInclusionDesired = false;
	public void accept( java.util.Set< edu.cmu.cs.dennisc.pattern.Crawlable > alreadyVisited, edu.cmu.cs.dennisc.pattern.Crawler crawler ) {
		if( alreadyVisited.contains( this ) ) {
			//pass
		} else {
			alreadyVisited.add( this );
			crawler.visit( this );
			for( edu.cmu.cs.dennisc.property.Property< ? > property : this.getProperties() ) {
				if( Node.isReferencedDeclarationPropertyInclusionDesired ) {
					//pass
				} else {
					if( property instanceof DeclarationProperty< ? > ) {
						DeclarationProperty< ? > declarationProperty = (DeclarationProperty< ? >)property;
						if( declarationProperty.isReference() ) {
							continue;
						}
					}
				}
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
	
	public final synchronized void crawl( edu.cmu.cs.dennisc.pattern.Crawler crawler, boolean isReferencedDeclarationPropertyInclusionDesired ) {
		Node.isReferencedDeclarationPropertyInclusionDesired = isReferencedDeclarationPropertyInclusionDesired;
		accept( new java.util.HashSet< edu.cmu.cs.dennisc.pattern.Crawlable >(), crawler );
	}

	//	protected void crawl( java.util.Set< AbstractType > types, edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
	//		visitor.visit( this );
	//	}
	//	public void crawl( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
	//		java.util.Set< AbstractType > types = new java.util.HashSet< AbstractType >();
	//		crawl( types, visitor );
	//	}


	private static final double VERSION = 3.0;

	private static org.w3c.dom.Element encodeValue( Object value, org.w3c.dom.Document xmlDocument, java.util.Set< AbstractDeclaration > set ) {
		org.w3c.dom.Element rv;
		if( value instanceof Node ) {
			Node node = (Node)value;
			rv = node.encode( xmlDocument, set );
		} else if( value instanceof java.util.Collection ) {
			rv = xmlDocument.createElement( "collection" );
			rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, value.getClass().getName() );
			java.util.Collection< ? > collection = (java.util.Collection< ? >)value;
			for( Object item : collection ) {
				rv.appendChild( encodeValue( item, xmlDocument, set ) );
			}
		} else {
			rv = xmlDocument.createElement( "value" );
			if( value != null ) {
				rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, value.getClass().getName() );
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
			rv.setAttribute( CodecConstants.KEY_ATTRIBUTE, Integer.toHexString( abstractDeclaration.hashCode() ) );
			if( set.contains( this ) ) {
				return rv;
			} else {
				set.add( abstractDeclaration );
			}
		}
		rv.setAttribute( CodecConstants.UUID_ATTRIBUTE, m_uuid.toString() );
		rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, getClass().getName() );
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

	protected final void decodeNode( Decoder decoder, org.w3c.dom.Element xmlElement, java.util.Map< Integer, AbstractDeclaration > map ) {
		org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
		for( int i = 0; i < nodeList.getLength(); i++ ) {
			org.w3c.dom.Node xmlNode = nodeList.item( i );
			assert xmlNode instanceof org.w3c.dom.Element : xmlNode;
			org.w3c.dom.Element xmlProperty = (org.w3c.dom.Element)xmlNode;
			if( xmlProperty.getTagName().equals( "property" ) ) {
				edu.cmu.cs.dennisc.property.Property property = this.getPropertyNamed( xmlProperty.getAttribute( "name" ) );
				Object value = decoder.decodeValue( (org.w3c.dom.Element)xmlProperty.getFirstChild(), map );
				property.setValue( this, value );
			}
		}
		postDecode();
	}

	protected void postDecode() {
	}

	public static Node decode( org.w3c.dom.Document xmlDocument, String projectVersion, java.util.Map< Integer, AbstractDeclaration > map ) {
		org.w3c.dom.Element xmlElement = xmlDocument.getDocumentElement();
		double xmlVersion = Double.parseDouble( xmlElement.getAttribute( "version" ) );
		assert xmlVersion == VERSION;
		
		Decoder decoder = new Decoder( projectVersion, edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() );
		return decoder.decode( xmlElement, map );
	}
	public static Node decode( org.w3c.dom.Document xmlDocument, String projectVersion ) {
		return decode( xmlDocument, projectVersion, new java.util.HashMap< Integer, AbstractDeclaration >() );
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
