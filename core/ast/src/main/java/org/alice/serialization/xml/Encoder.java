package org.alice.serialization.xml;

import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.xml.XMLUtilities;
import org.lgna.common.Resource;
import org.lgna.project.ast.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Encoder {

	public static Document encode( AbstractNode node ) {
		return encode( node, new HashSet<>() );
	}

	public static Document encode( AbstractNode node, Set<AbstractDeclaration> terminals ) {
		Document document = XMLUtilities.createDocument();
		Element xmlElement = encode( node, document, terminals );
		xmlElement.setAttribute( "version", Double.toString( CodecConstants.CURRENT_VERSION ) );
		document.appendChild( xmlElement );
		return document;
	}

	private static Element encode( AbstractNode node, Document xmlDocument, Set<AbstractDeclaration> set ) {
		return encode( node, xmlDocument, createEncodeMapFromDeclarationSet( set ) );
	}

	private static Map<AbstractDeclaration, Integer> createEncodeMapFromDeclarationSet( Set<AbstractDeclaration> set ) {
		Map<AbstractDeclaration, Integer> map = new HashMap<>();
		for( AbstractDeclaration declaration : set ) {
			getUniqueKeyAndPutInEncodeMap( declaration, map );
		}
		return map;
	}


	private static int createUniqueKey( Map<?, ?> map ) {
		return map.size() + 1;
	}

	private static int getUniqueKeyAndPutInEncodeMap( AbstractDeclaration declaration, Map<AbstractDeclaration, Integer> map ) {
		int uniqueKey = createUniqueKey( map );
		map.put( declaration, uniqueKey );
		return uniqueKey;
	}



	private static Element encode( AbstractNode node, Document xmlDocument, Map<AbstractDeclaration, Integer> map ) {
		Element rv = xmlDocument.createElement( "node" );
		if( node instanceof AbstractDeclaration ) {
			AbstractDeclaration abstractDeclaration = (AbstractDeclaration)node;
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
		rv.setAttribute( CodecConstants.ID_ATTRIBUTE, node.getId().toString() );
		rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, node.getClass().getName() );
		if( node instanceof JavaType) {
			JavaType javaType = (JavaType)node;
			rv.appendChild( encodeType( xmlDocument, "type", javaType.getClassReflectionProxy() ) );
		} else if( node instanceof UserArrayType) {
			UserArrayType userArrayType = (UserArrayType)node;

			Element xmlLeafType = xmlDocument.createElement( "leafType" );
			xmlLeafType.appendChild( encodeValue( userArrayType.getLeafType(), xmlDocument, map ) );
			rv.appendChild( xmlLeafType );

			Element xmlDimensionCount = xmlDocument.createElement( "dimensionCount" );
			xmlDimensionCount.appendChild( xmlDocument.createTextNode( Integer.toString( userArrayType.getDimensionCount() ) ) );
			rv.appendChild( xmlDimensionCount );

		} else if( node instanceof JavaConstructor) {
			JavaConstructor constructorDeclaredInJava = (JavaConstructor)node;
			rv.appendChild( encodeConstructor( xmlDocument, constructorDeclaredInJava.getConstructorReflectionProxy() ) );
		} else if( node instanceof JavaMethod) {
			JavaMethod methodDeclaredInJava = (JavaMethod)node;
			rv.appendChild( encodeMethod( xmlDocument, methodDeclaredInJava.getMethodReflectionProxy() ) );
		} else if( node instanceof AbstractMethodContainedByUserField) {
			AbstractMethodContainedByUserField getterOrSetter = (AbstractMethodContainedByUserField)node;
			UserField field = getterOrSetter.getField();
			rv.appendChild( encodeValue( field, xmlDocument, map ) );
		} else if( node instanceof JavaField ) {
			JavaField fieldDeclaredInJavaWithField = (JavaField)node;
			rv.appendChild( encodeField( xmlDocument, fieldDeclaredInJavaWithField.getFieldReflectionProxy() ) );
		} else if( node instanceof AnonymousUserConstructor ) {
			AnonymousUserConstructor anonymousConstructor = (AnonymousUserConstructor)node;
			Element xmlType = xmlDocument.createElement( "anonymousType" );
			xmlType.appendChild( encodeValue( anonymousConstructor.getDeclaringType(), xmlDocument, map ) );
			rv.appendChild( xmlType );
		} else if( node instanceof JavaConstructorParameter ) {
			JavaConstructorParameter parameterDeclaredInJavaConstructor = (JavaConstructorParameter)node;
			JavaConstructor constructor = parameterDeclaredInJavaConstructor.getCode();
			rv.appendChild( encodeValue( constructor, xmlDocument, map ) );

			Element xmlIndex = xmlDocument.createElement( "index" );
			xmlIndex.appendChild( xmlDocument.createTextNode( Integer.toString( parameterDeclaredInJavaConstructor.getIndex() ) ) );
			rv.appendChild( xmlIndex );
		} else if( node instanceof JavaMethodParameter ) {
			JavaMethodParameter parameterDeclaredInJavaMethod = (JavaMethodParameter)node;
			JavaMethod method = parameterDeclaredInJavaMethod.getCode();
			rv.appendChild( encodeValue( method, xmlDocument, map ) );

			Element xmlIndex = xmlDocument.createElement( "index" );
			xmlIndex.appendChild( xmlDocument.createTextNode( Integer.toString( parameterDeclaredInJavaMethod.getIndex() ) ) );
			rv.appendChild( xmlIndex );
		} else if( node instanceof SetterParameter ) {
			SetterParameter setterParameter = (SetterParameter)node;
			Setter setter = setterParameter.getCode();
			rv.appendChild( encodeValue( setter, xmlDocument, map ) );
		}
		for( InstanceProperty property : node.getProperties() ) {
			rv.appendChild( encodeProperty( xmlDocument, property, map ) );
		}
		return rv;
	}

	private static Element encodeType( Document xmlDocument, String nodeName, ClassReflectionProxy classReflectionProxy ) {
		Element element = xmlDocument.createElement( nodeName );
		element.setAttribute( "name", classReflectionProxy.getName() );
		return element;
	}

	private static Element encodeValue( Object value, Document xmlDocument, Map<AbstractDeclaration, Integer> map ) {
		Element rv;
		if( value instanceof AbstractNode ) {
			AbstractNode node = (AbstractNode)value;
			rv = encode( node, xmlDocument, map );
		} else if( value instanceof Collection) {
			rv = xmlDocument.createElement( "collection" );
			rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, value.getClass().getName() );
			Collection<?> collection = (Collection<?>)value;
			for( Object item : collection ) {
				rv.appendChild( encodeValue( item, xmlDocument, map ) );
			}
		} else {
			rv = xmlDocument.createElement( "value" );
			if( value != null ) {
				rv.setAttribute( CodecConstants.TYPE_ATTRIBUTE, value.getClass().getName() );
				String text;
				if( value instanceof Resource ) {
					Resource resource = (Resource)value;
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

	private static Element encodeProperty( Document xmlDocument, InstanceProperty<?> property,
					Map<AbstractDeclaration, Integer> map ) {
		Element xmlProperty = xmlDocument.createElement( "property" );
		xmlProperty.setAttribute( "name", property.getName() );
		Object value = property.getValue();
		xmlProperty.appendChild( encodeValue( value, xmlDocument, map ) );
		return xmlProperty;
	}

	private static Element encodeDeclaringClass( Document xmlDocument, MemberReflectionProxy memberReflectionProxy ) {
		return encodeType( xmlDocument, "declaringClass", memberReflectionProxy.getDeclaringClassReflectionProxy() );
	}

	private static Element encodeParameters( Document xmlDocument, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		Element parameters = xmlDocument.createElement( "parameters" );
		for( ClassReflectionProxy parameterClassReflectionProxy : parameterClassReflectionProxies ) {
			parameters.appendChild( encodeType( xmlDocument, "type", parameterClassReflectionProxy ) );
		}
		return parameters;
	}

	private static Element encodeMember( Document xmlDocument, String nodeName, MemberReflectionProxy memberReflectionProxy ) {
		Element element = xmlDocument.createElement( nodeName );
		element.appendChild( encodeDeclaringClass( xmlDocument, memberReflectionProxy ) );
		return element;
	}

	private static Element encodeField( Document xmlDocument, FieldReflectionProxy fieldReflectionProxy ) {
		Element field = encodeMember( xmlDocument, "field", fieldReflectionProxy );
		field.setAttribute( "name", fieldReflectionProxy.getName() );
		return field;
	}

	private static Element encodeConstructor( Document xmlDocument, ConstructorReflectionProxy constructorReflectionProxy ) {
		Element constructor = encodeMember( xmlDocument, "constructor", constructorReflectionProxy );
		constructor.setAttribute( "isVarArgs", Boolean.toString( constructorReflectionProxy.isVarArgs() ) );
		constructor.appendChild( encodeParameters( xmlDocument, constructorReflectionProxy.getParameterClassReflectionProxies() ) );
		return constructor;
	}

	private static Element encodeMethod( Document xmlDocument, MethodReflectionProxy methodReflectionProxy ) {
		Element method = encodeMember( xmlDocument, "method", methodReflectionProxy );
		method.setAttribute( "name", methodReflectionProxy.getName() );
		method.setAttribute( "isVarArgs", Boolean.toString( methodReflectionProxy.isVarArgs() ) );
		method.appendChild( encodeParameters( xmlDocument, methodReflectionProxy.getParameterClassReflectionProxies() ) );
		return method;
	}
}
