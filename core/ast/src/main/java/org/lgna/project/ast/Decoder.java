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

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public class Decoder {
	private static class ClassReflectionProxyAndMethodName {
		private final ClassReflectionProxy classReflectionProxy;
		private final String name;

		public ClassReflectionProxyAndMethodName( ClassReflectionProxy classReflectionProxy, String name ) {
			this.classReflectionProxy = classReflectionProxy;
			this.name = name;
		}

		public ClassReflectionProxy getClassReflectionProxy() {
			return this.classReflectionProxy;
		}

		public String getName() {
			return this.name;
		}
	}

	private static final edu.cmu.cs.dennisc.map.MapToMap<ClassReflectionProxy, String, ClassReflectionProxyAndMethodName> betweenClassesMethodMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private static final edu.cmu.cs.dennisc.map.MapToMap<ClassReflectionProxy, String, String> intraClassMethodMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private static final java.util.Map<String, String> mapClassNameToClassName = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static void addMethodFilterWithinClass( ClassReflectionProxy classReflectionProxy, String prevName, String nextName ) {
		Decoder.intraClassMethodMap.put( classReflectionProxy, prevName, nextName );
	}

	public static void addMethodFilterWithinClass( Class<?> cls, String prevName, String nextName ) {
		addMethodFilterWithinClass( JavaType.getInstance( cls ).getClassReflectionProxy(), prevName, nextName );
	}

	public static void addMethodFilterBetweenClasses( ClassReflectionProxy prevClassReflectionProxy, String prevName, ClassReflectionProxy nextClassReflectionProxy, String nextName ) {
		Decoder.betweenClassesMethodMap.put( prevClassReflectionProxy, prevName, new ClassReflectionProxyAndMethodName( nextClassReflectionProxy, nextName ) );
	}

	public static void addMethodFilterBetweenClasses( Class<?> prevCls, String prevName, Class<?> nextCls, String nextName ) {
		addMethodFilterBetweenClasses( JavaType.getInstance( prevCls ).getClassReflectionProxy(), prevName, JavaType.getInstance( nextCls ).getClassReflectionProxy(), nextName );
	}

	public static void addClassFilter( ClassReflectionProxy prevClassReflectionProxy, ClassReflectionProxy nextClassReflectionProxy ) {
		Decoder.mapClassNameToClassName.put( prevClassReflectionProxy.getName(), nextClassReflectionProxy.getName() );
	}

	public static void addClassFilter( ClassReflectionProxy prevClassReflectionProxy, Class<?> nextCls ) {
		addClassFilter( prevClassReflectionProxy, JavaType.getInstance( nextCls ).getClassReflectionProxy() );
	}

	public static void addClassFilter( Class<?> prevCls, Class<?> nextCls ) {
		addClassFilter( JavaType.getInstance( prevCls ).getClassReflectionProxy(), JavaType.getInstance( nextCls ).getClassReflectionProxy() );
	}

	private static String filterClassNameIfNecessary( String clsName ) {
		String rv = Decoder.mapClassNameToClassName.get( clsName );
		if( rv != null ) {
			//pass
		} else {
			rv = clsName;
		}
		return rv;
	}

	//	private static String filterMethodNameIfNecessary( ClassReflectionProxy classReflectionProxy, String name ) {
	//		String rv = Decoder.intraClassMethodMap.get( classReflectionProxy, name );
	//		if( rv != null ) {
	//			//pass
	//		} else {
	//			rv = name;
	//		}
	//		return rv;
	//	}

	public Decoder( org.lgna.project.Version srcVersion, org.lgna.project.Version dstVersion, DecodeIdPolicy policy ) {
		this.srcVersion = srcVersion;
		this.dstVersion = dstVersion;
		this.policy = policy;
	}

	private static ClassReflectionProxy createClassReflectionProxy( String clsName ) {
		return new ClassReflectionProxy( filterClassNameIfNecessary( clsName ) );
	}

	private String getClassName( org.w3c.dom.Element xmlElement ) {
		String rv = xmlElement.getAttribute( CodecConstants.TYPE_ATTRIBUTE );
		return rv;
	}

	private ClassReflectionProxy getJavaClassInfo( org.w3c.dom.Element xmlElement ) {
		return createClassReflectionProxy( getClassName( xmlElement ) );
	}

	//todo: investigate
	private Class<?> getCls( org.w3c.dom.Element xmlElement ) {
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( getClassName( xmlElement ) );
	}

	//todo: investigate
	private Object newInstance( org.w3c.dom.Element xmlElement ) {
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( getClassName( xmlElement ) );
	}

	public Object decodeValue( org.w3c.dom.Element xmlValue, java.util.Map<Integer, AbstractDeclaration> map ) {
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
					//rv = new NullLiteral();
					rv = null;
				}
			} else if( tagName.equals( "collection" ) ) {
				java.util.Collection collection = (java.util.Collection)newInstance( xmlValue );
				org.w3c.dom.NodeList nodeList = xmlValue.getChildNodes();
				for( int i = 0; i < nodeList.getLength(); i++ ) {
					org.w3c.dom.Element xmlItem = (org.w3c.dom.Element)nodeList.item( i );
					collection.add( decodeValue( xmlItem, map ) );
				}
				rv = collection;
				//			} else if( tagName.equals( "resource" ) ) {
				//				String uuidText = xmlValue.getAttribute( CodecConstants.UUID_ATTRIBUTE );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "uuidText", uuidText );
				//				java.util.UUID uuid = java.util.UUID.fromString( uuidText );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "uuid", uuid );
				//				rv = org.alice.virtualmachine.Resource.get( uuid );
			} else if( tagName.equals( "value" ) ) {
				Class<?> cls = getCls( xmlValue );
				String textContent = xmlValue.getTextContent();
				if( cls.equals( String.class ) ) {
					rv = textContent;
				} else {
					try {
						rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.valueOf( cls, textContent );
					} catch( RuntimeException re ) {
						if( "DIVIDE".equals( textContent ) ) {
							rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.valueOf( cls, "REAL_DIVIDE" );
						} else if( "REMAINDER".equals( textContent ) ) {
							rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.valueOf( cls, "REAL_REMAINDER" );
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

	private ClassReflectionProxy decodeType( org.w3c.dom.Element xmlElement, String nodeName ) {
		org.w3c.dom.Element xmlClass = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlElement, nodeName );
		String clsName = xmlClass.getAttribute( "name" );
		return createClassReflectionProxy( clsName );
	}

	private UserArrayType decodeUserArrayType( org.w3c.dom.Element xmlElement, java.util.Map<Integer, AbstractDeclaration> map ) {
		org.w3c.dom.Element xmlLeafType = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlElement, "leafType" );
		org.w3c.dom.Element xmlDimensionCount = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlElement, "dimensionCount" );
		org.w3c.dom.Element xmlLeafTypeNode = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlLeafType, "node" );

		org.w3c.dom.Node xmlLeafTypeFirstChild = xmlLeafType.getFirstChild();
		if( xmlLeafTypeFirstChild instanceof org.w3c.dom.Element ) {
			org.w3c.dom.Element xmlLeafTypeFirstChildElement = (org.w3c.dom.Element)xmlLeafTypeFirstChild;
			if( xmlLeafTypeFirstChildElement.hasAttribute( CodecConstants.UNIQUE_KEY_ATTRIBUTE ) ) {
				int arrayTypeUniqueKey = getUniqueKey( xmlElement );
				int leafTypeUniqueKey = getUniqueKey( xmlLeafTypeFirstChildElement );
				EPIC_HACK_mapArrayTypeKeyToLeafTypeKey.put( arrayTypeUniqueKey, leafTypeUniqueKey );
			}
		}

		NamedUserType leafType = (NamedUserType)decode( xmlLeafTypeNode, map );
		int dimensionCount = Integer.parseInt( xmlDimensionCount.getTextContent() );
		return UserArrayType.getInstance( leafType, dimensionCount );
	}

	private AnonymousUserConstructor decodeAnonymousConstructor( org.w3c.dom.Element xmlElement, java.util.Map<Integer, AbstractDeclaration> map ) {
		org.w3c.dom.Element xmlLeafType = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlElement, "anonymousType" );
		org.w3c.dom.Element xmlLeafTypeNode = (org.w3c.dom.Element)xmlLeafType.getChildNodes().item( 0 );
		AnonymousUserType anonymousType = (AnonymousUserType)decode( xmlLeafTypeNode, map );
		return AnonymousUserConstructor.get( anonymousType );
	}

	private ClassReflectionProxy decodeDeclaringClass( org.w3c.dom.Element xmlElement ) {
		return decodeType( xmlElement, "declaringClass" );
	}

	private ClassReflectionProxy[] decodeParameters( org.w3c.dom.Element xmlElement ) {
		org.w3c.dom.Element xmlParameters = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlElement, "parameters" );
		java.util.List<org.w3c.dom.Element> xmlTypes = edu.cmu.cs.dennisc.xml.XMLUtilities.getChildElementsByTagName( xmlParameters, "type" );
		ClassReflectionProxy[] rv = new ClassReflectionProxy[ xmlTypes.size() ];
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = createClassReflectionProxy( xmlTypes.get( i ).getAttribute( "name" ) );
		}
		return rv;
	}

	private FieldReflectionProxy decodeField( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlField = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlParent, nodeName );
		ClassReflectionProxy declaringCls = decodeDeclaringClass( xmlField );
		String name = xmlField.getAttribute( "name" );
		return new FieldReflectionProxy( declaringCls, name );
	}

	private ConstructorReflectionProxy decodeConstructor( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlConstructor = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlParent, nodeName );
		ClassReflectionProxy declaringCls = decodeDeclaringClass( xmlConstructor );
		ClassReflectionProxy[] parameterClses = decodeParameters( xmlConstructor );
		boolean isVarArgs = Boolean.parseBoolean( xmlConstructor.getAttribute( "isVarArgs" ) );
		return new ConstructorReflectionProxy( declaringCls, parameterClses, isVarArgs );
	}

	private MethodReflectionProxy decodeMethod( org.w3c.dom.Element xmlParent, String nodeName ) {
		org.w3c.dom.Element xmlMethod = edu.cmu.cs.dennisc.xml.XMLUtilities.getSingleChildElementByTagName( xmlParent, nodeName );
		ClassReflectionProxy declaringCls = decodeDeclaringClass( xmlMethod );
		String name = xmlMethod.getAttribute( "name" );

		String potentialReplacement = Decoder.intraClassMethodMap.get( declaringCls, name );
		if( potentialReplacement != null ) {
			name = potentialReplacement;
		} else {
			ClassReflectionProxyAndMethodName classReflectionProxyAndMethodName = Decoder.betweenClassesMethodMap.get( declaringCls, name );
			if( classReflectionProxyAndMethodName != null ) {
				declaringCls = classReflectionProxyAndMethodName.getClassReflectionProxy();
				name = classReflectionProxyAndMethodName.getName();
			}
		}

		//		name = filterMethodNameIfNecessary( declaringCls, name );
		ClassReflectionProxy[] parameterClses = decodeParameters( xmlMethod );
		boolean isVarArgs = Boolean.parseBoolean( xmlMethod.getAttribute( "isVarArgs" ) );
		return new MethodReflectionProxy( declaringCls, name, parameterClses, isVarArgs );
	}

	private static int getUniqueKey( org.w3c.dom.Element xmlElement ) {
		return Integer.parseInt( xmlElement.getAttribute( CodecConstants.UNIQUE_KEY_ATTRIBUTE ), 16 );
	}

	public AbstractNode decode( org.w3c.dom.Element xmlElement, java.util.Map<Integer, AbstractDeclaration> map ) {
		AbstractNode rv;
		if( xmlElement.hasAttribute( CodecConstants.TYPE_ATTRIBUTE ) ) {
			String clsName = getClassName( xmlElement );
			if( clsName.equals( JavaType.class.getName() ) ) {
				rv = JavaType.getInstance( decodeType( xmlElement, "type" ) );
			} else if( clsName.equals( UserArrayType.class.getName() ) ) {
				rv = decodeUserArrayType( xmlElement, map );
			} else if( clsName.equals( JavaConstructor.class.getName() ) ) {
				rv = JavaConstructor.getInstance( decodeConstructor( xmlElement, "constructor" ) );
			} else if( clsName.equals( JavaMethod.class.getName() ) ) {
				MethodReflectionProxy methodReflectionProxy = decodeMethod( xmlElement, "method" );
				MethodReflectionProxy varArgsReplacement = MethodReflectionProxy.getReplacementIfNecessary( methodReflectionProxy );
				if( varArgsReplacement != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "replacing", methodReflectionProxy, "with", varArgsReplacement );
					methodReflectionProxy = varArgsReplacement;
				}
				rv = JavaMethod.getInstance( methodReflectionProxy );
			} else if( clsName.equals( Getter.class.getName() ) || clsName.equals( Setter.class.getName() ) ) {

				org.w3c.dom.Node xmlFirstChild = xmlElement.getFirstChild();
				if( xmlFirstChild instanceof org.w3c.dom.Element ) {
					org.w3c.dom.Element xmlFirstChildElement = (org.w3c.dom.Element)xmlFirstChild;
					if( xmlFirstChildElement.hasAttribute( CodecConstants.UNIQUE_KEY_ATTRIBUTE ) ) {
						int getterOrSetterUniqueKey = getUniqueKey( xmlElement );
						int fieldUniqueKey = getUniqueKey( xmlFirstChildElement );
						java.util.Map<Integer, Integer> mapToFieldKey;
						if( clsName.equals( Getter.class.getName() ) ) {
							mapToFieldKey = EPIC_HACK_mapGetterKeyToFieldKey;
						} else {
							mapToFieldKey = EPIC_HACK_mapSetterKeyToFieldKey;
						}
						mapToFieldKey.put( getterOrSetterUniqueKey, fieldUniqueKey );
					}
				}

				org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
				assert nodeList.getLength() == 1;
				org.w3c.dom.Element xmlField = (org.w3c.dom.Element)nodeList.item( 0 );
				UserField field = (UserField)decode( xmlField, map );
				if( clsName.equals( Getter.class.getName() ) ) {
					rv = field.getGetter();
				} else {
					rv = field.getSetter();
				}
			} else if( clsName.equals( SetterParameter.class.getName() ) ) {
				org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
				assert nodeList.getLength() == 1;
				org.w3c.dom.Element xmlSetter = (org.w3c.dom.Element)nodeList.item( 0 );
				Setter setter = (Setter)decode( xmlSetter, map );
				rv = setter.getRequiredParameters().get( 0 );
			} else if( clsName.equals( JavaField.class.getName() ) ) {
				rv = JavaField.getInstance( decodeField( xmlElement, "field" ) );
			} else if( clsName.equals( AnonymousUserConstructor.class.getName() ) ) {
				rv = decodeAnonymousConstructor( xmlElement, map );
			} else if( clsName.equals( JavaConstructorParameter.class.getName() ) ) {
				org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
				assert nodeList.getLength() == 2;
				org.w3c.dom.Element xmlConstructor = (org.w3c.dom.Element)nodeList.item( 0 );
				JavaConstructor constructorDeclaredInJava = (JavaConstructor)decodeValue( xmlConstructor, map );
				org.w3c.dom.Element xmlIndex = (org.w3c.dom.Element)nodeList.item( 1 );
				int index = Integer.parseInt( xmlIndex.getTextContent() );

				final int REQUIRED_N = constructorDeclaredInJava.getRequiredParameters().size();
				if( index < REQUIRED_N ) {
					rv = constructorDeclaredInJava.getRequiredParameters().get( index );
				} else {
					if( index == REQUIRED_N ) {
						rv = constructorDeclaredInJava.getVariableLengthParameter();
						if( rv != null ) {
							//pass;
						} else {
							rv = constructorDeclaredInJava.getKeyedParameter();
						}
					} else {
						rv = null;
					}
				}
			} else if( clsName.equals( JavaMethodParameter.class.getName() ) ) {
				org.w3c.dom.NodeList nodeList = xmlElement.getChildNodes();
				assert nodeList.getLength() == 2;
				org.w3c.dom.Element xmlMethod = (org.w3c.dom.Element)nodeList.item( 0 );
				JavaMethod methodDeclaredInJava = (JavaMethod)decodeValue( xmlMethod, map );
				org.w3c.dom.Element xmlIndex = (org.w3c.dom.Element)nodeList.item( 1 );
				int index = Integer.parseInt( xmlIndex.getTextContent() );
				final int REQUIRED_N = methodDeclaredInJava.getRequiredParameters().size();
				if( index < REQUIRED_N ) {
					rv = methodDeclaredInJava.getRequiredParameters().get( index );
				} else {
					if( index == REQUIRED_N ) {
						rv = methodDeclaredInJava.getVariableLengthParameter();
						if( rv != null ) {
							//pass;
						} else {
							rv = methodDeclaredInJava.getKeyedParameter();
						}
					} else {
						rv = null;
					}
				}
			} else {
				rv = (AbstractNode)newInstance( xmlElement );
				assert rv != null;
			}
			if( rv instanceof AbstractDeclaration ) {
				map.put( getUniqueKey( xmlElement ), (AbstractDeclaration)rv );
			}
			rv.decodeNode( this, xmlElement, map );
			if( xmlElement.hasAttribute( CodecConstants.ID_ATTRIBUTE ) ) {
				if( this.policy.isIdPreserved() ) {
					rv.setId( java.util.UUID.fromString( xmlElement.getAttribute( CodecConstants.ID_ATTRIBUTE ) ) );
				}
			}
		} else {
			int key = getUniqueKey( xmlElement );
			rv = map.get( key );
			if( rv != null ) {
				//pass
			} else {
				if( EPIC_HACK_mapArrayTypeKeyToLeafTypeKey.containsKey( key ) ) {
					int leafTypeKey = EPIC_HACK_mapArrayTypeKeyToLeafTypeKey.get( key );
					AbstractDeclaration leafDeclaration = map.get( leafTypeKey );
					if( leafDeclaration instanceof UserType<?> ) {
						UserType<?> leafType = (UserType<?>)leafDeclaration;
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( leafTypeKey, leafType );
						rv = leafType.getArrayType();
					} else {
						assert false : leafDeclaration;
					}
				} else if( EPIC_HACK_mapGetterKeyToFieldKey.containsKey( key ) ) {
					int fieldKey = EPIC_HACK_mapGetterKeyToFieldKey.get( key );
					AbstractDeclaration fieldDeclaration = map.get( fieldKey );
					if( fieldDeclaration instanceof UserField ) {
						UserField userField = (UserField)fieldDeclaration;
						rv = userField.getGetter();
					}
				} else if( EPIC_HACK_mapSetterKeyToFieldKey.containsKey( key ) ) {
					int fieldKey = EPIC_HACK_mapSetterKeyToFieldKey.get( key );
					AbstractDeclaration fieldDeclaration = map.get( fieldKey );
					if( fieldDeclaration instanceof UserField ) {
						UserField userField = (UserField)fieldDeclaration;
						rv = userField.getSetter();
					}
				} else {
					assert false : Integer.toHexString( key ) + " " + map;
				}
			}
		}
		return rv;
	}

	private final java.util.Map<Integer, Integer> EPIC_HACK_mapArrayTypeKeyToLeafTypeKey = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Integer, Integer> EPIC_HACK_mapGetterKeyToFieldKey = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Integer, Integer> EPIC_HACK_mapSetterKeyToFieldKey = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private final org.lgna.project.Version srcVersion;
	private final org.lgna.project.Version dstVersion;
	private final DecodeIdPolicy policy;
}
