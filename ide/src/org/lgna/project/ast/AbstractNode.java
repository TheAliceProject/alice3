/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractNode extends edu.cmu.cs.dennisc.pattern.DefaultInstancePropertyOwner implements Node, edu.cmu.cs.dennisc.pattern.Crawlable {
	private static final double CURRENT_VERSION = 3.1;
	private static final double MINIMUM_ACCEPTABLE_VERSION = CURRENT_VERSION;

	private java.util.UUID m_uuid = java.util.UUID.randomUUID();
	private AbstractNode parent;
	
	public java.util.UUID getId() {
		return m_uuid;
	}
	/*package-private*/ void setUUID( java.util.UUID uuid ) {
		m_uuid = uuid;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	private void setParent( AbstractNode parent ) {
		if( this.parent != parent ) {
			if( this.parent != null ) {
				if( parent != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "previous not null", this );
				}
			}
			this.parent = parent;
		}
	}

	
	public <N extends Node> N getFirstAncestorAssignableTo( Class<N> cls, boolean isThisIncludedInSearch ) {
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
		return (N)rv;
	}
	public final <N extends Node> N getFirstAncestorAssignableTo( Class<N> cls ) {
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
				AbstractNode node = (AbstractNode)nodeProperty.getValue();
				if( node != null ) {
					node.setParent( null );
				}
			}
		}
	}
	@Override
	public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
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
				AbstractNode node = (AbstractNode)nodeProperty.getValue();
				if( node != null ) {
					node.setParent( this );
				}
			}
		}
		super.firePropertyChanged( e );
	}
	@Override
	public void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		super.fireClearing( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( AbstractNode node : nodeListProperty ) {
				if( node != null ) {
					node.setParent( null );
				}
			}
		}
	}
	@Override
	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		super.fireRemoving( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					((AbstractNode)o).setParent( null );
				}
			}
		}
	}
	@Override
	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		super.fireSetting( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					((AbstractNode)o).setParent( null );
				}
			}
		}
	}
	@Override
	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					((AbstractNode)o).setParent( this );
				}
			}
		}
		super.fireSet( e );
	}
	@Override
	public void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		super.fireAdding( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					((AbstractNode)o).setParent( null );
				}
			}
		}
	}
	@Override
	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty< ? > ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					((AbstractNode)o).setParent( this );
				}
			}
		}
		super.fireAdded( e );
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
				if( AbstractNode.isReferencedDeclarationPropertyInclusionDesired ) {
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
				if( value instanceof Iterable<?> ) {
					Iterable<?> iterable = (Iterable<?>)value;
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
		AbstractNode.isReferencedDeclarationPropertyInclusionDesired = isReferencedDeclarationPropertyInclusionDesired;
		accept( new java.util.HashSet< edu.cmu.cs.dennisc.pattern.Crawlable >(), crawler );
	}

	//	protected void crawl( java.util.Set< AbstractType > types, edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
	//		visitor.visit( this );
	//	}
	//	public void crawl( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
	//		java.util.Set< AbstractType > types = new java.util.HashSet< AbstractType >();
	//		crawl( types, visitor );
	//	}

	private static org.w3c.dom.Element encodeValue( Object value, org.w3c.dom.Document xmlDocument, java.util.Set< AbstractDeclaration > set ) {
		org.w3c.dom.Element rv;
		if( value instanceof AbstractNode ) {
			AbstractNode node = (AbstractNode)value;
			rv = node.encode( xmlDocument, set );
		} else if( value instanceof java.util.Collection ) {
			rv = xmlDocument.createElement( "collection" );
			rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, value.getClass().getName() );
			java.util.Collection< ? > collection = (java.util.Collection< ? >)value;
			for( Object item : collection ) {
				rv.appendChild( encodeValue( item, xmlDocument, set ) );
			}
//		} else if( value instanceof org.alice.virtualmachine.Resource ) {
//			org.alice.virtualmachine.Resource resource = (org.alice.virtualmachine.Resource)value;
//			rv = xmlDocument.createElement( "resource" );
//			java.util.UUID uuid = resource.getUUID();
//			assert uuid != null;
//			rv.setAttribute( CodecConstants.UUID_ATTRIBUTE, uuid.toString() );
		} else {
			rv = xmlDocument.createElement( "value" );
			if( value != null ) {
				rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, value.getClass().getName() );
				String text;
				if( value instanceof org.lgna.common.Resource ) {
					org.lgna.common.Resource resource = (org.lgna.common.Resource)value;
					text = resource.getId().toString();
				} else {
					text = value.toString();
				}
				rv.appendChild( xmlDocument.createTextNode( text ) );
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
	private static org.w3c.dom.Element encodeType( org.w3c.dom.Document xmlDocument, String nodeName, ClassReflectionProxy classReflectionProxy ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( nodeName );
		rv.setAttribute( "name", classReflectionProxy.getName() );
		return rv;
	}
	private static org.w3c.dom.Element encodeDeclaringClass( org.w3c.dom.Document xmlDocument, MemberReflectionProxy memberReflectionProxy ) {
		return encodeType( xmlDocument, "declaringClass", memberReflectionProxy.getDeclaringClassReflectionProxy() );
	}
	private static org.w3c.dom.Element encodeParameters( org.w3c.dom.Document xmlDocument, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( "parameters" );
		for( ClassReflectionProxy parameterClassReflectionProxy : parameterClassReflectionProxies ) {
			rv.appendChild( encodeType( xmlDocument, "type", parameterClassReflectionProxy ) );
		}
		return rv;
	}
	private static org.w3c.dom.Element encodeMember( org.w3c.dom.Document xmlDocument, String nodeName, MemberReflectionProxy memberReflectionProxy ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( nodeName );
//		String name = mmbr.getName();
//		if( name == null ) {
//			assert mmbr instanceof java.lang.reflect.Constructor< ? >;
//		} else {
//			rv.setAttribute( "name", name );
//		}
		rv.appendChild( encodeDeclaringClass( xmlDocument, memberReflectionProxy ) );
		return rv;
	}
	private static org.w3c.dom.Element encodeField( org.w3c.dom.Document xmlDocument, String nodeName, FieldReflectionProxy fieldReflectionProxy ) {
		org.w3c.dom.Element rv = encodeMember( xmlDocument, nodeName, fieldReflectionProxy );
		rv.setAttribute( "name", fieldReflectionProxy.getName() );
		return rv;
	}
	private static org.w3c.dom.Element encodeConstructor( org.w3c.dom.Document xmlDocument, String nodeName, ConstructorReflectionProxy constructorReflectionProxy ) {
		org.w3c.dom.Element rv = encodeMember( xmlDocument, nodeName, constructorReflectionProxy );
		rv.setAttribute( "isVarArgs", Boolean.toString( constructorReflectionProxy.isVarArgs() ) );
		rv.appendChild( encodeParameters( xmlDocument, constructorReflectionProxy.getParameterClassReflectionProxies() ) );
		return rv;
	}
	private static org.w3c.dom.Element encodeMethod( org.w3c.dom.Document xmlDocument, String nodeName, MethodReflectionProxy methodReflectionProxy ) {
		org.w3c.dom.Element rv = encodeMember( xmlDocument, nodeName, methodReflectionProxy );
		rv.setAttribute( "name", methodReflectionProxy.getName() );
		rv.setAttribute( "isVarArgs", Boolean.toString( methodReflectionProxy.isVarArgs() ) );
		rv.appendChild( encodeParameters( xmlDocument, methodReflectionProxy.getParameterClassReflectionProxies() ) );
		return rv;
	}
	
	//todo: reduce visibility?
	public org.w3c.dom.Element encode( org.w3c.dom.Document xmlDocument, java.util.Set< AbstractDeclaration > set ) {
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
		if( this instanceof JavaType ) {
			JavaType javaType = (JavaType)this;
			rv.appendChild( encodeType( xmlDocument, "type", javaType.getClassReflectionProxy() ) );
		} else if( this instanceof UserArrayType ) {
			UserArrayType userArrayType = (UserArrayType)this;

			org.w3c.dom.Element xmlLeafType = xmlDocument.createElement( "leafType" );
			xmlLeafType.appendChild( encodeValue( userArrayType.getLeafType(), xmlDocument, set ) );
			rv.appendChild( xmlLeafType );
			
			org.w3c.dom.Element xmlDimensionCount = xmlDocument.createElement( "dimensionCount" );
			xmlDimensionCount.appendChild( xmlDocument.createTextNode( Integer.toString( userArrayType.getDimensionCount() ) ) );
			rv.appendChild( xmlDimensionCount );
		
		} else if( this instanceof JavaConstructor ) {
			JavaConstructor constructorDeclaredInJava = (JavaConstructor)this;
			rv.appendChild( encodeConstructor( xmlDocument, "constructor", constructorDeclaredInJava.getConstructorReflectionProxy() ) );
		} else if( this instanceof JavaMethod ) {
			JavaMethod methodDeclaredInJava = (JavaMethod)this;
			rv.appendChild( encodeMethod( xmlDocument, "method", methodDeclaredInJava.getMethodReflectionProxy() ) );
		} else if( this instanceof JavaField ) {
			JavaField fieldDeclaredInJavaWithField = (JavaField)this;
			rv.appendChild( encodeField( xmlDocument, "field", fieldDeclaredInJavaWithField.getFieldReflectionProxy() ) );
		} else if( this instanceof AnonymousUserConstructor ) {
			AnonymousUserConstructor anonymousConstructor = (AnonymousUserConstructor)this;
			org.w3c.dom.Element xmlType = xmlDocument.createElement( "anonymousType" );
			xmlType.appendChild( encodeValue( anonymousConstructor.getDeclaringType(), xmlDocument, set ) );
			rv.appendChild( xmlType );
		} else if( this instanceof JavaConstructorParameter ) {
			JavaConstructorParameter parameterDeclaredInJavaConstructor = (JavaConstructorParameter)this;
			JavaConstructor constructor = parameterDeclaredInJavaConstructor.getConstructor();
			rv.appendChild( encodeValue( constructor, xmlDocument, set ) );

			org.w3c.dom.Element xmlIndex = xmlDocument.createElement( "index" );
			xmlIndex.appendChild( xmlDocument.createTextNode( Integer.toString( parameterDeclaredInJavaConstructor.getIndex() ) ) );
			rv.appendChild( xmlIndex );
		} else if( this instanceof JavaMethodParameter ) {
			JavaMethodParameter parameterDeclaredInJavaMethod = (JavaMethodParameter)this;
			JavaMethod method = parameterDeclaredInJavaMethod.getMethod();
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
		xmlElement.setAttribute( "version", Double.toString( CURRENT_VERSION ) );
		rv.appendChild( xmlElement );
		return rv;
	}
	public final org.w3c.dom.Document encode() {
		return encode( new java.util.HashSet< AbstractDeclaration >() );
	}


	protected java.util.Set< AbstractDeclaration > fillInDeclarationSet( java.util.Set< AbstractDeclaration > rv, java.util.Set< AbstractNode > nodes ) {
		nodes.add( this );
		for( edu.cmu.cs.dennisc.property.Property< ? > property : this.getProperties() ) {
			Object value = property.getValue( this );
			if( value instanceof AbstractNode ) {
				if( nodes.contains( value ) ) {
					//pass
				} else {
					((AbstractNode)value).fillInDeclarationSet( rv, nodes );
				}
			} else if( value instanceof Iterable<?> ) {
				for( Object item : (Iterable<?>)value ) {
					if( item instanceof AbstractNode ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							((AbstractNode)item).fillInDeclarationSet( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}
	public java.util.Set< AbstractDeclaration > createDeclarationSet() {
		java.util.Set< AbstractDeclaration > rv = new java.util.HashSet< AbstractDeclaration >();
		fillInDeclarationSet( rv, new java.util.HashSet< AbstractNode >() );
		return rv;
	}
	private java.util.Set< AbstractDeclaration > removeDeclarationsThatNeedToBeCopied( java.util.Set< AbstractDeclaration > rv, java.util.Set< AbstractNode > nodes ) {
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
			if( value instanceof AbstractNode ) {
				if( nodes.contains( value ) ) {
					//pass
				} else {
					((AbstractNode)value).removeDeclarationsThatNeedToBeCopied( rv, nodes );
				}
			} else if( value instanceof Iterable<?> ) {
				for( Object item : (Iterable<?>)value ) {
					if( item instanceof AbstractNode ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							((AbstractNode)item).removeDeclarationsThatNeedToBeCopied( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}
	public java.util.Set< AbstractDeclaration > removeDeclarationsThatNeedToBeCopied( java.util.Set< AbstractDeclaration > rv ) {
		return removeDeclarationsThatNeedToBeCopied( rv, new java.util.HashSet< AbstractNode >() );
	}
	public static java.util.Map< Integer, AbstractDeclaration > createMapOfDeclarationsThatShouldNotBeCopied( java.util.Set< AbstractDeclaration > set ) {
		java.util.Map< Integer, AbstractDeclaration > rv = new java.util.HashMap< Integer, AbstractDeclaration >();
		for( AbstractDeclaration abstractDeclaration : set ) {
			rv.put( abstractDeclaration.hashCode(), abstractDeclaration );
		}
		return rv;
	}
	
	protected void handleMissingProperty( String propertyName, Object value ) {
		throw new RuntimeException( propertyName );
	}

	protected Object convertPropertyValueIfNecessary( edu.cmu.cs.dennisc.property.Property property, Object value ) {
		return value;
	}
	
	protected final void decodeNode( Decoder decoder, org.w3c.dom.Element xmlElement, java.util.Map< Integer, AbstractDeclaration > map ) {
		org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
		for( int i = 0; i < nodeList.getLength(); i++ ) {
			org.w3c.dom.Node xmlNode = nodeList.item( i );
			assert xmlNode instanceof org.w3c.dom.Element : xmlNode;
			org.w3c.dom.Element xmlProperty = (org.w3c.dom.Element)xmlNode;
			if( xmlProperty.getTagName().equals( "property" ) ) {
				String propertyName = xmlProperty.getAttribute( "name" );
				edu.cmu.cs.dennisc.property.Property property = this.getPropertyNamed( propertyName );
				Object value = decoder.decodeValue( (org.w3c.dom.Element)xmlProperty.getFirstChild(), map );
				if( property != null ) {
					value = this.convertPropertyValueIfNecessary( property, value );
					property.setValue( this, value );
				} else {
					this.handleMissingProperty( propertyName, value );
				}
			}
		}
		this.postDecode();
	}

	protected void postDecode() {
	}

	public static AbstractNode decode( org.w3c.dom.Document xmlDocument, String projectVersion, java.util.Map< Integer, AbstractDeclaration > map, boolean isUUIDDecodingDesired ) throws org.lgna.project.VersionNotSupportedException {
		org.w3c.dom.Element xmlElement = xmlDocument.getDocumentElement();
		double astVersion = Double.parseDouble( xmlElement.getAttribute( "version" ) );
		if( astVersion >= MINIMUM_ACCEPTABLE_VERSION ) {
			Decoder decoder = new Decoder( projectVersion, org.lgna.project.Version.getCurrentVersionText(), isUUIDDecodingDesired );
			return decoder.decode( xmlElement, map );
		} else {
			throw new org.lgna.project.VersionNotSupportedException( MINIMUM_ACCEPTABLE_VERSION, astVersion );
		}
	}
	public static AbstractNode decode( org.w3c.dom.Document xmlDocument, String projectVersion, java.util.Map< Integer, AbstractDeclaration > map ) throws org.lgna.project.VersionNotSupportedException {
		return decode( xmlDocument, projectVersion, map, true );
	}
	public static AbstractNode decode( org.w3c.dom.Document xmlDocument, String projectVersion ) throws org.lgna.project.VersionNotSupportedException {
		return decode( xmlDocument, projectVersion, new java.util.HashMap< Integer, AbstractDeclaration >() );
	}

	public void assignUUIDs( java.util.Map< Integer, AbstractDeclaration > map ) {
	}

//	protected void appendInternal( StringBuffer sb, java.util.Set< Node > set ) {
//		sb.append( "name=" );
//		sb.append( this.getName() );
//		sb.append( ";" );
//		sb.append( "@" );
//		sb.append( this.hashCode() );
//		sb.append( ";" );
//		String separator = "";
//		for( edu.cmu.cs.dennisc.property.Property< ? > property : getProperties() ) {
//			sb.append( separator );
//			sb.append( property.getName() );
//			sb.append( "=" );
//			Object value = property.getValue( this );
//			if( value instanceof Node ) {
//				Node node = (Node)value;
//				if( set.contains( node ) ) {
//					sb.append( "referenced@" );
//					sb.append( node.hashCode() );
//				} else {
//					node.append( sb, set );
//				}
//			} else if( value instanceof java.util.Collection ) {
//				java.util.Collection collection = (java.util.Collection)value;
//				sb.append( "cls:" );
//				sb.append( collection.getClass().getName() );
//				sb.append( ";size:" );
//				sb.append( collection.size() );
//			} else {
//				sb.append( value );
//			}
//			separator = ",";
//		}
//	}

//	protected void append( StringBuffer sb, java.util.Set< Node > set ) {
//		set.add( this );
//		sb.append( this.getClass().getName() );
//		sb.append( "[" );
//		appendInternal( sb, set );
//		sb.append( "]" );
//	}
		
//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		append( sb, new java.util.HashSet< Node >() );
//		return sb.toString();
//	}

	//todo: i18n
	//protected abstract StringBuffer appendRepr( StringBuffer rv, java.util.Locale locale ); 
	protected StringBuilder appendRepr( StringBuilder rv, java.util.Locale locale ) {
		rv.append( this.getClass().getSimpleName() );
		return rv;
	}
	public final String getRepr( java.util.Locale locale ) {
		StringBuilder sb = new StringBuilder();
		this.appendRepr( sb, locale );
		return sb.toString();
	}
	
	protected StringBuilder appendStringDetails( StringBuilder rv ) {
		return rv;
	}
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		this.appendStringDetails( sb );
		sb.append( "]" );
		return sb.toString();
	}
}
