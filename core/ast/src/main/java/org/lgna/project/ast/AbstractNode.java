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

import org.lgna.project.ast.localizer.AstLocalizer;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractNode extends Element implements Node {
	private static final double CURRENT_VERSION = 3.10062;
	private static final double MINIMUM_ACCEPTABLE_VERSION = 3.1;

	private static org.lgna.project.ast.localizer.AstLocalizerFactory astLocalizerFactory = new org.lgna.project.ast.localizer.DefaultAstLocalizerFactory();

	public static org.lgna.project.ast.localizer.AstLocalizerFactory getAstLocalizerFactory() {
		return AbstractNode.astLocalizerFactory;
	}

	public static void setAstLocalizerFactory( org.lgna.project.ast.localizer.AstLocalizerFactory astLocalizerFactory ) {
		AbstractNode.astLocalizerFactory = astLocalizerFactory;
	}

	private java.util.UUID id = java.util.UUID.randomUUID();
	private AbstractNode parent;

	//todo
	public boolean isAppropriatelyIdenitifiedById() {
		return true;
	}

	@Override
	public final java.util.UUID getId() {
		return this.id;
	}

	/* package-private */final void setId( java.util.UUID id ) {
		this.id = id;
	}

	@Override
	public boolean contentEquals( Node other, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( other != null ) {
			Class<?> thisCls = this.getClass();
			Class<?> otherCls = other.getClass();
			return thisCls.equals( otherCls );
		} else {
			return false;
		}
	}

	@Override
	public final boolean contentEquals( org.lgna.project.ast.Node other, org.lgna.project.ast.ContentEqualsStrictness strictness ) {
		edu.cmu.cs.dennisc.property.PropertyFilter filter = null;
		return this.contentEquals( other, strictness, filter );
	}

	@Override
	public Node getParent() {
		return this.parent;
	}

	private void setParent( AbstractNode parent ) {
		if( this.parent != parent ) {
			if( this.parent != null ) {
				if( parent != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "previous not null", this, this.parent );
				}
			}
			this.parent = parent;
		}
	}

	@Override
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

	@Override
	public final <N extends Node> N getFirstAncestorAssignableTo( Class<N> cls ) {
		return getFirstAncestorAssignableTo( cls, false );
	}

	@Override
	public void firePropertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		super.firePropertyChanging( e );
		edu.cmu.cs.dennisc.property.InstanceProperty<?> property = e.getTypedSource();
		if( property instanceof NodeProperty<?> ) {
			NodeProperty<?> nodeProperty = (NodeProperty<?>)property;
			boolean isReference;
			if( nodeProperty instanceof DeclarationProperty<?> ) {
				isReference = ( (DeclarationProperty<?>)nodeProperty ).isReference();
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
		edu.cmu.cs.dennisc.property.InstanceProperty<?> property = e.getTypedSource();
		if( property instanceof NodeProperty<?> ) {
			NodeProperty<?> nodeProperty = (NodeProperty<?>)property;
			boolean isReference;
			if( nodeProperty instanceof DeclarationProperty<?> ) {
				isReference = ( (DeclarationProperty<?>)nodeProperty ).isReference();
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
		if( listProperty instanceof NodeListProperty<?> ) {
			NodeListProperty<?> nodeListProperty = (NodeListProperty<?>)listProperty;
			for( Node node : nodeListProperty ) {
				if( node instanceof AbstractNode ) {
					( (AbstractNode)node ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		super.fireRemoving( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		super.fireSetting( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( this );
				}
			}
		}
		super.fireSet( e );
	}

	@Override
	public void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		super.fireAdding( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( this );
				}
			}
		}
		super.fireAdded( e );
	}

	private static void acceptIfCrawlable( edu.cmu.cs.dennisc.pattern.Crawler crawler, java.util.Set<edu.cmu.cs.dennisc.pattern.Crawlable> visited, Object value, CrawlPolicy crawlPolicy, edu.cmu.cs.dennisc.pattern.Criterion<Declaration> declarationFilter ) {
		if( value instanceof AbstractNode ) {
			AbstractNode node = (AbstractNode)value;
			if( declarationFilter != null ) {
				if( node instanceof Declaration ) {
					Declaration declaration = (Declaration)node;
					if( declarationFilter.accept( declaration ) ) {
						//pass
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping", declaration );
						return;
					}
				}
			}
			node.accept( crawler, visited, crawlPolicy, declarationFilter );
		} else if( value instanceof edu.cmu.cs.dennisc.pattern.Crawlable ) {
			edu.cmu.cs.dennisc.pattern.Crawlable crawlable = (edu.cmu.cs.dennisc.pattern.Crawlable)value;
			crawlable.accept( crawler, visited );
		}
	}

	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Crawler crawler, java.util.Set<edu.cmu.cs.dennisc.pattern.Crawlable> visited ) {
		this.accept( crawler, visited, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY, null );
	}

	private void accept( edu.cmu.cs.dennisc.pattern.Crawler crawler, java.util.Set<edu.cmu.cs.dennisc.pattern.Crawlable> visited, CrawlPolicy crawlPolicy, edu.cmu.cs.dennisc.pattern.Criterion<Declaration> declarationFilter ) {
		if( visited.contains( this ) ) {
			//pass
		} else {
			visited.add( this );
			crawler.visit( this );

			// Look through this nodes properties to see if any have anything to crawl
			for( edu.cmu.cs.dennisc.property.InstanceProperty<?> property : this.getProperties() ) {
				// Check if this is a reference
				if( property instanceof DeclarationProperty<?> ) {
					DeclarationProperty<?> declarationProperty = (DeclarationProperty<?>)property;
					if( declarationProperty.isReference() ) {
						if( crawlPolicy.isReferenceTunneledInto() ) {
							//pass
						} else {
							if( crawlPolicy.isReferenceIncluded() ) {
								Declaration declaration = declarationProperty.getValue();
								if( visited.contains( declaration ) ) {
									//pass
								} else {
									visited.add( declaration );
									crawler.visit( declaration );
								}
							}
							continue;
						}
					}
				}

				Object value = property.getValue( this );
				if( value instanceof Iterable<?> ) {
					Iterable<?> iterable = (Iterable<?>)value;
					for( Object item : iterable ) {
						acceptIfCrawlable( crawler, visited, item, crawlPolicy, declarationFilter );
					}
				} else if( value instanceof Object[] ) {
					Object[] array = (Object[])value;
					for( Object item : array ) {
						acceptIfCrawlable( crawler, visited, item, crawlPolicy, declarationFilter );
					}
				} else {
					acceptIfCrawlable( crawler, visited, value, crawlPolicy, declarationFilter );
				}
			}
		}
	}

	@Override
	public final synchronized void crawl( edu.cmu.cs.dennisc.pattern.Crawler crawler, CrawlPolicy crawlPolicy, edu.cmu.cs.dennisc.pattern.Criterion<Declaration> criterion ) {
		this.accept( crawler, new java.util.HashSet<edu.cmu.cs.dennisc.pattern.Crawlable>(), crawlPolicy, criterion );
	}

	public final synchronized void crawl( edu.cmu.cs.dennisc.pattern.Crawler crawler, CrawlPolicy crawlPolicy ) {
		this.crawl( crawler, crawlPolicy, null );
	}

	//	@Deprecated
	//	public final synchronized void crawl( edu.cmu.cs.dennisc.pattern.Crawler crawler, boolean followReferences ) {
	//		CrawlPolicy crawlPolicy = followReferences ? CrawlPolicy.COMPLETE : CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY;
	//		this.crawl( crawler, crawlPolicy );
	//	}

	private static org.w3c.dom.Element encodeValue( Object value, org.w3c.dom.Document xmlDocument, java.util.Map<AbstractDeclaration, Integer> map ) {
		org.w3c.dom.Element rv;
		if( value instanceof AbstractNode ) {
			AbstractNode node = (AbstractNode)value;
			rv = node.encode( xmlDocument, map );
		} else if( value instanceof java.util.Collection ) {
			rv = xmlDocument.createElement( "collection" );
			rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, value.getClass().getName() );
			java.util.Collection<?> collection = (java.util.Collection<?>)value;
			for( Object item : collection ) {
				rv.appendChild( encodeValue( item, xmlDocument, map ) );
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

	protected final org.w3c.dom.Element encodeProperty( org.w3c.dom.Document xmlDocument, edu.cmu.cs.dennisc.property.InstanceProperty property, java.util.Map<AbstractDeclaration, Integer> map ) {
		org.w3c.dom.Element xmlProperty = xmlDocument.createElement( "property" );
		xmlProperty.setAttribute( "name", property.getName() );
		Object value = property.getValue( this );
		xmlProperty.appendChild( encodeValue( value, xmlDocument, map ) );
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

	// hashCode not terrible choice for "unique" key.
	//	private static int getNotGuaranteedToBeUniqueKey( AbstractDeclaration declaration ) {
	//		return System.identityHashCode( declaration );
	//	}

	private static int createUniqueKey( AbstractDeclaration declaration, java.util.Map<?, ?> map ) {
		return map.size() + 1;
	}

	private static int getUniqueKeyAndPutInEncodeMap( AbstractDeclaration declaration, java.util.Map<AbstractDeclaration, Integer> map ) {
		int uniqueKey = createUniqueKey( declaration, map );
		map.put( declaration, uniqueKey );
		return uniqueKey;
	}

	private static int getUniqueKeyAndPutInDecodeMap( AbstractDeclaration declaration, java.util.Map<Integer, AbstractDeclaration> map ) {
		int uniqueKey = createUniqueKey( declaration, map );
		map.put( uniqueKey, declaration );
		return uniqueKey;
	}

	public org.w3c.dom.Element encode( org.w3c.dom.Document xmlDocument, java.util.Map<AbstractDeclaration, Integer> map ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( "node" );
		if( this instanceof AbstractDeclaration ) {
			AbstractDeclaration abstractDeclaration = (AbstractDeclaration)this;
			boolean isDeclarationAlreadyEncoded = map.containsKey( abstractDeclaration );
			int key;
			if( isDeclarationAlreadyEncoded ) {
				key = map.get( abstractDeclaration );
			} else {
				key = getUniqueKeyAndPutInEncodeMap( abstractDeclaration, map );
			}
			rv.setAttribute( CodecConstants.UNIQUE_KEY_ATTRIBUTE, Integer.toHexString( key ) );
			if( isDeclarationAlreadyEncoded ) {
				return rv;
			}
		}
		//todo
		rv.setAttribute( CodecConstants.ID_ATTRIBUTE, this.id.toString() );
		rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, getClass().getName() );
		if( this instanceof JavaType ) {
			JavaType javaType = (JavaType)this;
			rv.appendChild( encodeType( xmlDocument, "type", javaType.getClassReflectionProxy() ) );
		} else if( this instanceof UserArrayType ) {
			UserArrayType userArrayType = (UserArrayType)this;

			org.w3c.dom.Element xmlLeafType = xmlDocument.createElement( "leafType" );
			xmlLeafType.appendChild( encodeValue( userArrayType.getLeafType(), xmlDocument, map ) );
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
		} else if( this instanceof AbstractMethodContainedByUserField ) {
			AbstractMethodContainedByUserField getterOrSetter = (AbstractMethodContainedByUserField)this;
			UserField field = getterOrSetter.getField();
			rv.appendChild( encodeValue( field, xmlDocument, map ) );
		} else if( this instanceof JavaField ) {
			JavaField fieldDeclaredInJavaWithField = (JavaField)this;
			rv.appendChild( encodeField( xmlDocument, "field", fieldDeclaredInJavaWithField.getFieldReflectionProxy() ) );
		} else if( this instanceof AnonymousUserConstructor ) {
			AnonymousUserConstructor anonymousConstructor = (AnonymousUserConstructor)this;
			org.w3c.dom.Element xmlType = xmlDocument.createElement( "anonymousType" );
			xmlType.appendChild( encodeValue( anonymousConstructor.getDeclaringType(), xmlDocument, map ) );
			rv.appendChild( xmlType );
		} else if( this instanceof JavaConstructorParameter ) {
			JavaConstructorParameter parameterDeclaredInJavaConstructor = (JavaConstructorParameter)this;
			JavaConstructor constructor = parameterDeclaredInJavaConstructor.getCode();
			rv.appendChild( encodeValue( constructor, xmlDocument, map ) );

			org.w3c.dom.Element xmlIndex = xmlDocument.createElement( "index" );
			xmlIndex.appendChild( xmlDocument.createTextNode( Integer.toString( parameterDeclaredInJavaConstructor.getIndex() ) ) );
			rv.appendChild( xmlIndex );
		} else if( this instanceof JavaMethodParameter ) {
			JavaMethodParameter parameterDeclaredInJavaMethod = (JavaMethodParameter)this;
			JavaMethod method = parameterDeclaredInJavaMethod.getCode();
			rv.appendChild( encodeValue( method, xmlDocument, map ) );

			org.w3c.dom.Element xmlIndex = xmlDocument.createElement( "index" );
			xmlIndex.appendChild( xmlDocument.createTextNode( Integer.toString( parameterDeclaredInJavaMethod.getIndex() ) ) );
			rv.appendChild( xmlIndex );
		} else if( this instanceof SetterParameter ) {
			SetterParameter setterParameter = (SetterParameter)this;
			Setter setter = setterParameter.getCode();
			rv.appendChild( encodeValue( setter, xmlDocument, map ) );
		}
		for( edu.cmu.cs.dennisc.property.InstanceProperty property : getProperties() ) {
			rv.appendChild( encodeProperty( xmlDocument, property, map ) );
		}
		return rv;
	}

	public static java.util.Map<AbstractDeclaration, Integer> createEncodeMapFromDeclarationSet( java.util.Set<AbstractDeclaration> set ) {
		java.util.Map<AbstractDeclaration, Integer> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		for( AbstractDeclaration declaration : set ) {
			getUniqueKeyAndPutInEncodeMap( declaration, map );
		}
		return map;
	}

	private org.w3c.dom.Element encode( org.w3c.dom.Document xmlDocument, java.util.Set<AbstractDeclaration> set ) {
		return this.encode( xmlDocument, createEncodeMapFromDeclarationSet( set ) );
	}

	public final org.w3c.dom.Document encode( java.util.Set<AbstractDeclaration> set ) {
		org.w3c.dom.Document rv = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
		org.w3c.dom.Element xmlElement = this.encode( rv, set );
		xmlElement.setAttribute( "version", Double.toString( CURRENT_VERSION ) );
		rv.appendChild( xmlElement );
		return rv;
	}

	public final org.w3c.dom.Document encode() {
		return encode( new java.util.HashSet<AbstractDeclaration>() );
	}

	protected java.util.Set<AbstractDeclaration> fillInDeclarationSet( java.util.Set<AbstractDeclaration> rv, java.util.Set<AbstractNode> nodes ) {
		nodes.add( this );
		for( edu.cmu.cs.dennisc.property.InstanceProperty<?> property : this.getProperties() ) {
			Object value = property.getValue( this );
			if( value instanceof AbstractNode ) {
				if( nodes.contains( value ) ) {
					//pass
				} else {
					( (AbstractNode)value ).fillInDeclarationSet( rv, nodes );
				}
			} else if( value instanceof Iterable<?> ) {
				for( Object item : (Iterable<?>)value ) {
					if( item instanceof AbstractNode ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							( (AbstractNode)item ).fillInDeclarationSet( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}

	public java.util.Set<AbstractDeclaration> createDeclarationSet() {
		java.util.Set<AbstractDeclaration> rv = new java.util.HashSet<AbstractDeclaration>();
		fillInDeclarationSet( rv, new java.util.HashSet<AbstractNode>() );
		return rv;
	}

	private java.util.Set<AbstractDeclaration> removeDeclarationsThatNeedToBeCopied( java.util.Set<AbstractDeclaration> rv, java.util.Set<AbstractNode> nodes ) {
		nodes.add( this );
		for( edu.cmu.cs.dennisc.property.InstanceProperty<?> property : this.getProperties() ) {
			if( property instanceof DeclarationProperty ) {
				DeclarationProperty<? extends AbstractDeclaration> declarationProperty = (DeclarationProperty<? extends AbstractDeclaration>)property;
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
					( (AbstractNode)value ).removeDeclarationsThatNeedToBeCopied( rv, nodes );
				}
			} else if( value instanceof Iterable<?> ) {
				for( Object item : (Iterable<?>)value ) {
					if( item instanceof AbstractNode ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							( (AbstractNode)item ).removeDeclarationsThatNeedToBeCopied( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}

	public java.util.Set<AbstractDeclaration> removeDeclarationsThatNeedToBeCopied( java.util.Set<AbstractDeclaration> rv ) {
		return removeDeclarationsThatNeedToBeCopied( rv, new java.util.HashSet<AbstractNode>() );
	}

	public static java.util.Map<Integer, AbstractDeclaration> createMapOfDeclarationsThatShouldNotBeCopied( java.util.Set<AbstractDeclaration> set ) {
		java.util.Map<Integer, AbstractDeclaration> rv = new java.util.HashMap<Integer, AbstractDeclaration>();
		for( AbstractDeclaration abstractDeclaration : set ) {
			getUniqueKeyAndPutInDecodeMap( abstractDeclaration, rv );
		}
		return rv;
	}

	protected void handleMissingProperty( String propertyName, Object value ) {
		throw new RuntimeException( propertyName );
	}

	protected Object convertPropertyValueIfNecessary( edu.cmu.cs.dennisc.property.InstanceProperty property, Object value ) {
		return value;
	}

	protected final void decodeNode( Decoder decoder, org.w3c.dom.Element xmlElement, java.util.Map<Integer, AbstractDeclaration> map ) {
		org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
		for( int i = 0; i < nodeList.getLength(); i++ ) {
			org.w3c.dom.Node xmlNode = nodeList.item( i );
			assert xmlNode instanceof org.w3c.dom.Element : xmlNode;
			org.w3c.dom.Element xmlProperty = (org.w3c.dom.Element)xmlNode;
			if( xmlProperty.getTagName().equals( "property" ) ) {
				String propertyName = xmlProperty.getAttribute( "name" );
				edu.cmu.cs.dennisc.property.InstanceProperty property = this.getPropertyNamed( propertyName );
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

	public static AbstractNode decode( org.w3c.dom.Document xmlDocument, org.lgna.project.Version projectVersion, java.util.Map<Integer, AbstractDeclaration> map, DecodeIdPolicy policy ) throws org.lgna.project.VersionNotSupportedException {
		org.w3c.dom.Element xmlElement = xmlDocument.getDocumentElement();
		double astVersion = Double.parseDouble( xmlElement.getAttribute( "version" ) );
		if( astVersion >= MINIMUM_ACCEPTABLE_VERSION ) {
			Decoder decoder = new Decoder( projectVersion, org.lgna.project.ProjectVersion.getCurrentVersion(), policy );
			return decoder.decode( xmlElement, map );
		} else {
			throw new org.lgna.project.VersionNotSupportedException( MINIMUM_ACCEPTABLE_VERSION, astVersion );
		}
	}

	public static AbstractNode decode( org.w3c.dom.Document xmlDocument, org.lgna.project.Version projectVersion, java.util.Map<Integer, AbstractDeclaration> map ) throws org.lgna.project.VersionNotSupportedException {
		return decode( xmlDocument, projectVersion, map, DecodeIdPolicy.PRESERVE_IDS );
	}

	public static AbstractNode decode( org.w3c.dom.Document xmlDocument, org.lgna.project.Version projectVersion ) throws org.lgna.project.VersionNotSupportedException {
		return decode( xmlDocument, projectVersion, new java.util.HashMap<Integer, AbstractDeclaration>() );
	}

	//	protected StringBuilder appendRepr( StringBuilder rv, java.util.Locale locale ) {
	//		rv.append( this.getClass().getSimpleName() );
	//		return rv;
	//	}
	//
	//	public final String getRepr( java.util.Locale locale ) {
	//		StringBuilder sb = new StringBuilder();
	//		this.appendRepr( sb, locale );
	//		return sb.toString();
	//	}

	public static void safeAppendRepr( AstLocalizer localizer, Node node ) {
		if( node instanceof AbstractNode ) {
			( (AbstractNode)node ).appendRepr( localizer );
		} else {
			if( node != null ) {
				localizer.appendText( node.getRepr() );
			} else {
				localizer.appendNull();
			}
		}
	}

	//protected abstract void appendRepr( AstLocalizer localizer );
	protected void appendRepr( AstLocalizer localizer ) {
		localizer.appendText( this.getClass().getSimpleName() );
	}

	@Override
	public final String getRepr() {
		final StringBuilder sb = new StringBuilder();
		this.appendRepr( astLocalizerFactory.createInstance( sb ) );
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
