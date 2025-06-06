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

package org.alice.serialization.xml;

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.xml.XMLUtilities;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
class Decoder {

  private Decoder(DecodeIdPolicy policy) {
    this.policy = policy;
  }

  public static AbstractNode copy(Document xmlDocument, Set<AbstractDeclaration> terminals) throws VersionNotSupportedException {
    Map<Integer, AbstractDeclaration> map = Decoder.createMapOfDeclarationsThatShouldNotBeCopied(terminals);
    return decode(xmlDocument, map, DecodeIdPolicy.NEW_IDS);
  }

  public static AbstractNode decode(Document xmlDocument) throws VersionNotSupportedException {
    return decode(xmlDocument, new HashMap<>(), DecodeIdPolicy.PRESERVE_IDS);
  }

  private static ClassReflectionProxy createClassReflectionProxy(String clsName) {
    return new ClassReflectionProxy(clsName);
  }

  private String getClassName(Element xmlElement) {
    return xmlElement.getAttribute(CodecConstants.TYPE_ATTRIBUTE);
  }

  //todo: investigate
  private Class<?> getCls(Element xmlElement) {
    return ReflectionUtilities.getClassForName(getClassName(xmlElement));
  }

  //todo: investigate
  private Object newInstance(Element xmlElement) {
    return ReflectionUtilities.newInstance(getClassName(xmlElement));
  }

  private Object decodeValue(Element xmlValue, Map<Integer, AbstractDeclaration> map) {
    Object rv;
    if (xmlValue.hasAttribute("isNull")) {
      rv = null;
    } else {
      String tagName = xmlValue.getTagName();
      if (tagName.equals("node")) {
        try {
          rv = decode(xmlValue, map);
        } catch (RuntimeException re) {
          re.printStackTrace();
          //rv = new NullLiteral();
          rv = null;
        }
      } else if (tagName.equals("collection")) {
        Collection collection = (Collection) newInstance(xmlValue);
        NodeList nodeList = xmlValue.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
          Element xmlItem = (Element) nodeList.item(i);
          collection.add(decodeValue(xmlItem, map));
        }
        rv = collection;
        //      } else if( tagName.equals( "resource" ) ) {
        //        String uuidText = xmlValue.getAttribute( CodecConstants.UUID_ATTRIBUTE );
        //        edu.cmu.cs.dennisc.print.PrintUtilities.println( "uuidText", uuidText );
        //        java.util.UUID uuid = java.util.UUID.fromString( uuidText );
        //        edu.cmu.cs.dennisc.print.PrintUtilities.println( "uuid", uuid );
        //        rv = org.alice.virtualmachine.Resource.get( uuid );
      } else if (tagName.equals("value")) {
        Class<?> cls = getCls(xmlValue);
        String textContent = xmlValue.getTextContent();
        if (cls.equals(String.class)) {
          rv = textContent;
        } else {
          try {
            rv = ReflectionUtilities.valueOf(cls, textContent);
          } catch (RuntimeException re) {
            if ("DIVIDE".equals(textContent)) {
              rv = ReflectionUtilities.valueOf(cls, "REAL_DIVIDE");
            } else if ("REMAINDER".equals(textContent)) {
              rv = ReflectionUtilities.valueOf(cls, "REAL_REMAINDER");
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

  private ClassReflectionProxy decodeType(Element xmlElement, String nodeName) {
    Element xmlClass = XMLUtilities.getSingleChildElementByTagName(xmlElement, nodeName);
    String clsName = xmlClass.getAttribute("name");
    return createClassReflectionProxy(clsName);
  }

  private UserArrayType decodeUserArrayType(Element xmlElement, Map<Integer, AbstractDeclaration> map) {
    Element xmlLeafType = XMLUtilities.getSingleChildElementByTagName(xmlElement, "leafType");
    Element xmlDimensionCount = XMLUtilities.getSingleChildElementByTagName(xmlElement, "dimensionCount");
    Element xmlLeafTypeNode = XMLUtilities.getSingleChildElementByTagName(xmlLeafType, "node");

    Node xmlLeafTypeFirstChild = xmlLeafType.getFirstChild();
    if (xmlLeafTypeFirstChild instanceof Element) {
      Element xmlLeafTypeFirstChildElement = (Element) xmlLeafTypeFirstChild;
      if (xmlLeafTypeFirstChildElement.hasAttribute(CodecConstants.UNIQUE_KEY_ATTRIBUTE)) {
        int arrayTypeUniqueKey = getUniqueKey(xmlElement);
        int leafTypeUniqueKey = getUniqueKey(xmlLeafTypeFirstChildElement);
        EPIC_HACK_mapArrayTypeKeyToLeafTypeKey.put(arrayTypeUniqueKey, leafTypeUniqueKey);
      }
    }

    NamedUserType leafType = (NamedUserType) decode(xmlLeafTypeNode, map);
    int dimensionCount = Integer.parseInt(xmlDimensionCount.getTextContent());
    return UserArrayType.getInstance(leafType, dimensionCount);
  }

  private AnonymousUserConstructor decodeAnonymousConstructor(Element xmlElement, Map<Integer, AbstractDeclaration> map) {
    Element xmlLeafType = XMLUtilities.getSingleChildElementByTagName(xmlElement, "anonymousType");
    Element xmlLeafTypeNode = (Element) xmlLeafType.getChildNodes().item(0);
    AnonymousUserType anonymousType = (AnonymousUserType) decode(xmlLeafTypeNode, map);
    return AnonymousUserConstructor.get(anonymousType);
  }

  private ClassReflectionProxy decodeDeclaringClass(Element xmlElement) {
    return decodeType(xmlElement, "declaringClass");
  }

  private ClassReflectionProxy[] decodeParameters(Element xmlElement) {
    Element xmlParameters = XMLUtilities.getSingleChildElementByTagName(xmlElement, "parameters");
    List<Element> xmlTypes = XMLUtilities.getChildElementsByTagName(xmlParameters, "type");
    ClassReflectionProxy[] rv = new ClassReflectionProxy[xmlTypes.size()];
    for (int i = 0; i < rv.length; i++) {
      rv[i] = createClassReflectionProxy(xmlTypes.get(i).getAttribute("name"));
    }
    return rv;
  }

  private FieldReflectionProxy decodeField(Element xmlParent, String nodeName) {
    Element xmlField = XMLUtilities.getSingleChildElementByTagName(xmlParent, nodeName);
    ClassReflectionProxy declaringCls = decodeDeclaringClass(xmlField);
    String name = xmlField.getAttribute("name");
    return new FieldReflectionProxy(declaringCls, name);
  }

  private ConstructorReflectionProxy decodeConstructor(Element xmlParent, String nodeName) {
    Element xmlConstructor = XMLUtilities.getSingleChildElementByTagName(xmlParent, nodeName);
    ClassReflectionProxy declaringCls = decodeDeclaringClass(xmlConstructor);
    ClassReflectionProxy[] parameterClses = decodeParameters(xmlConstructor);
    boolean isVarArgs = Boolean.parseBoolean(xmlConstructor.getAttribute("isVarArgs"));
    return new ConstructorReflectionProxy(declaringCls, parameterClses, isVarArgs);
  }

  private MethodReflectionProxy decodeMethod(Element xmlParent, String nodeName) {
    Element xmlMethod = XMLUtilities.getSingleChildElementByTagName(xmlParent, nodeName);
    ClassReflectionProxy declaringCls = decodeDeclaringClass(xmlMethod);
    String name = xmlMethod.getAttribute("name");

    ClassReflectionProxy[] parameterClses = decodeParameters(xmlMethod);
    boolean isVarArgs = Boolean.parseBoolean(xmlMethod.getAttribute("isVarArgs"));
    return new MethodReflectionProxy(declaringCls, name, parameterClses, isVarArgs);
  }

  private static int getUniqueKey(Element xmlElement) {
    return Integer.parseInt(xmlElement.getAttribute(CodecConstants.UNIQUE_KEY_ATTRIBUTE), 16);
  }

  private AbstractNode decode(Element xmlElement, Map<Integer, AbstractDeclaration> map) {
    AbstractNode rv;
    if (xmlElement.hasAttribute(CodecConstants.TYPE_ATTRIBUTE)) {
      String clsName = getClassName(xmlElement);
      if (clsName.equals(JavaType.class.getName())) {
        rv = JavaType.getInstance(decodeType(xmlElement, "type"));
      } else if (clsName.equals(UserArrayType.class.getName())) {
        rv = decodeUserArrayType(xmlElement, map);
      } else if (clsName.equals(JavaConstructor.class.getName())) {
        rv = JavaConstructor.getInstance(decodeConstructor(xmlElement, "constructor"));
      } else if (clsName.equals(JavaMethod.class.getName())) {
        MethodReflectionProxy methodReflectionProxy = decodeMethod(xmlElement, "method");
        MethodReflectionProxy varArgsReplacement = MethodReflectionProxy.getReplacementIfNecessary(methodReflectionProxy);
        if (varArgsReplacement != null) {
          Logger.errln("replacing", methodReflectionProxy, "with", varArgsReplacement);
          methodReflectionProxy = varArgsReplacement;
        }
        rv = JavaMethod.getInstance(methodReflectionProxy);
      } else if (clsName.equals(ArrayItemGetter.class.getName())) {
        Element xmlField = (Element) xmlElement.getFirstChild();
        UserField field = (UserField) decode(xmlField, map);
        rv = field.getArrayItemGetter();
      } else if (clsName.equals(ArrayItemSetter.class.getName())) {
        Element xmlField = (Element) xmlElement.getFirstChild();
        UserField field = (UserField) decode(xmlField, map);
        rv = field.getArrayItemSetter();
      } else if (clsName.equals(Getter.class.getName()) || clsName.equals(Setter.class.getName())) {

        Node xmlFirstChild = xmlElement.getFirstChild();
        if (xmlFirstChild instanceof Element) {
          Element xmlFirstChildElement = (Element) xmlFirstChild;
          if (xmlFirstChildElement.hasAttribute(CodecConstants.UNIQUE_KEY_ATTRIBUTE)) {
            int getterOrSetterUniqueKey = getUniqueKey(xmlElement);
            int fieldUniqueKey = getUniqueKey(xmlFirstChildElement);
            Map<Integer, Integer> mapToFieldKey;
            if (clsName.equals(Getter.class.getName())) {
              mapToFieldKey = EPIC_HACK_mapGetterKeyToFieldKey;
            } else {
              mapToFieldKey = EPIC_HACK_mapSetterKeyToFieldKey;
            }
            mapToFieldKey.put(getterOrSetterUniqueKey, fieldUniqueKey);
          }
        }

        NodeList nodeList = xmlElement.getChildNodes();
        assert nodeList.getLength() == 1;
        Element xmlField = (Element) nodeList.item(0);
        UserField field = (UserField) decode(xmlField, map);
        if (clsName.equals(Getter.class.getName())) {
          rv = field.getGetter();
        } else {
          rv = field.getSetter();
        }
      } else if (clsName.equals(SetterParameter.class.getName())) {
        NodeList nodeList = xmlElement.getChildNodes();
        assert nodeList.getLength() == 1;
        Element xmlSetter = (Element) nodeList.item(0);
        Setter setter = (Setter) decode(xmlSetter, map);
        rv = setter.getRequiredParameters().get(0);
      } else if (clsName.equals(JavaField.class.getName())) {
        rv = JavaField.getInstance(decodeField(xmlElement, "field"));
      } else if (clsName.equals(AnonymousUserConstructor.class.getName())) {
        rv = decodeAnonymousConstructor(xmlElement, map);
      } else if (clsName.equals(JavaConstructorParameter.class.getName())) {
        NodeList nodeList = xmlElement.getChildNodes();
        assert nodeList.getLength() == 2;
        Element xmlConstructor = (Element) nodeList.item(0);
        JavaConstructor constructorDeclaredInJava = (JavaConstructor) decodeValue(xmlConstructor, map);
        Element xmlIndex = (Element) nodeList.item(1);
        int index = Integer.parseInt(xmlIndex.getTextContent());

        final int REQUIRED_N = constructorDeclaredInJava.getRequiredParameters().size();
        if (index < REQUIRED_N) {
          rv = constructorDeclaredInJava.getRequiredParameters().get(index);
        } else {
          if (index == REQUIRED_N) {
            rv = constructorDeclaredInJava.getVariableLengthParameter();
            if (rv == null) {
              rv = constructorDeclaredInJava.getKeyedParameter();
            }
          } else {
            rv = null;
          }
        }
      } else if (clsName.equals(JavaMethodParameter.class.getName())) {
        NodeList nodeList = xmlElement.getChildNodes();
        assert nodeList.getLength() == 2;
        Element xmlMethod = (Element) nodeList.item(0);
        JavaMethod methodDeclaredInJava = (JavaMethod) decodeValue(xmlMethod, map);
        Element xmlIndex = (Element) nodeList.item(1);
        int index = Integer.parseInt(xmlIndex.getTextContent());
        final int REQUIRED_N = methodDeclaredInJava.getRequiredParameters().size();
        if (index < REQUIRED_N) {
          rv = methodDeclaredInJava.getRequiredParameters().get(index);
        } else {
          if (index == REQUIRED_N) {
            rv = methodDeclaredInJava.getVariableLengthParameter();
            if (rv == null) {
              rv = methodDeclaredInJava.getKeyedParameter();
            }
          } else {
            rv = null;
          }
        }
      } else {
        rv = (AbstractNode) newInstance(xmlElement);
        assert rv != null;
      }
      if (rv instanceof AbstractDeclaration) {
        map.put(getUniqueKey(xmlElement), (AbstractDeclaration) rv);
      }
      decodeNode(rv, xmlElement, map);
      if (xmlElement.hasAttribute(CodecConstants.ID_ATTRIBUTE)) {
        if (this.policy.isIdPreserved()) {
          rv.setId(UUID.fromString(xmlElement.getAttribute(CodecConstants.ID_ATTRIBUTE)));
        }
      }
    } else {
      int key = getUniqueKey(xmlElement);
      rv = map.get(key);
      if (rv == null) {
        if (EPIC_HACK_mapArrayTypeKeyToLeafTypeKey.containsKey(key)) {
          int leafTypeKey = EPIC_HACK_mapArrayTypeKeyToLeafTypeKey.get(key);
          AbstractDeclaration leafDeclaration = map.get(leafTypeKey);
          if (leafDeclaration instanceof UserType<?>) {
            UserType<?> leafType = (UserType<?>) leafDeclaration;
            Logger.outln(leafTypeKey, leafType);
            rv = leafType.getArrayType();
          } else {
            assert false : leafDeclaration;
          }
        } else if (EPIC_HACK_mapGetterKeyToFieldKey.containsKey(key)) {
          int fieldKey = EPIC_HACK_mapGetterKeyToFieldKey.get(key);
          AbstractDeclaration fieldDeclaration = map.get(fieldKey);
          if (fieldDeclaration instanceof UserField) {
            UserField userField = (UserField) fieldDeclaration;
            rv = userField.getGetter();
          }
        } else if (EPIC_HACK_mapSetterKeyToFieldKey.containsKey(key)) {
          int fieldKey = EPIC_HACK_mapSetterKeyToFieldKey.get(key);
          AbstractDeclaration fieldDeclaration = map.get(fieldKey);
          if (fieldDeclaration instanceof UserField) {
            UserField userField = (UserField) fieldDeclaration;
            rv = userField.getSetter();
          }
        } else {
          assert false : Integer.toHexString(key) + " " + map;
        }
      }
    }
    return rv;
  }

  private void decodeNode(AbstractNode node, Element xmlElement, Map<Integer, AbstractDeclaration> map) {
    NodeList nodeList = xmlElement.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node xmlNode = nodeList.item(i);
      assert xmlNode instanceof Element : xmlNode;
      Element xmlProperty = (Element) xmlNode;
      if (xmlProperty.getTagName().equals("property")) {
        String propertyName = xmlProperty.getAttribute("name");
        InstanceProperty property = node.getPropertyNamed(propertyName);
        Object value = decodeValue((Element) xmlProperty.getFirstChild(), map);
        if (property != null) {
          if (node instanceof NamedUserConstructor) {
            value = convertPropertyValueIfNecessary((NamedUserConstructor) node, property, value);
          }
          property.setValue(value);
        } else {
          handleMissingProperty(node, propertyName, value);
        }
      }
    }
  }

  private void handleMissingProperty(AbstractNode node, String propertyName, Object value) {
    if (node instanceof ConditionalInfixExpression && propertyName.equals("expressionType")) {
      return;
    }

    if (node instanceof RelationalInfixExpression && propertyName.equals("expressionType")) {
      RelationalInfixExpression rie = (RelationalInfixExpression) node;
      AbstractType<?, ?, ?> operand = value == JavaType.DOUBLE_OBJECT_TYPE ? JavaType.getInstance(Number.class) : (AbstractType<?, ?, ?>) value;
      rie.leftOperandType.setValue(operand);
      rie.rightOperandType.setValue(operand);
      return;
    }
    // Anything else is an error
    throw new RuntimeException(propertyName);
  }

  private Object convertPropertyValueIfNecessary(NamedUserConstructor node, InstanceProperty property, Object value) {
    if (property == node.body && value instanceof BlockStatement && !(value instanceof ConstructorBlockStatement)) {
      BlockStatement prevBlockStatement = (BlockStatement) value;
      Statement[] buffer = new Statement[prevBlockStatement.statements.size()];
      ConstructorBlockStatement constructorBlockStatement = new ConstructorBlockStatement(new SuperConstructorInvocationStatement(), prevBlockStatement.statements.toArray(buffer));
      constructorBlockStatement.isEnabled.setValue(prevBlockStatement.isEnabled.getValue());
      return constructorBlockStatement;
    }
    return value;
  }

  private static AbstractNode decode(Document xmlDocument, Map<Integer, AbstractDeclaration> map, DecodeIdPolicy policy) throws VersionNotSupportedException {
    Element xmlElement = xmlDocument.getDocumentElement();
    double astVersion = Double.parseDouble(xmlElement.getAttribute("version"));
    if (astVersion >= CodecConstants.MINIMUM_ACCEPTABLE_VERSION) {
      Decoder decoder = new Decoder(policy);
      return decoder.decode(xmlElement, map);
    } else {
      throw new VersionNotSupportedException(CodecConstants.MINIMUM_ACCEPTABLE_VERSION, astVersion);
    }
  }

  private static int createUniqueKey(Map<?, ?> map) {
    return map.size() + 1;
  }

  private static void getUniqueKeyAndPutInDecodeMap(AbstractDeclaration declaration, Map<Integer, AbstractDeclaration> map) {
    map.put(createUniqueKey(map), declaration);
  }

  private static Map<Integer, AbstractDeclaration> createMapOfDeclarationsThatShouldNotBeCopied(Set<AbstractDeclaration> set) {
    Map<Integer, AbstractDeclaration> rv = new HashMap<Integer, AbstractDeclaration>();
    for (AbstractDeclaration abstractDeclaration : set) {
      getUniqueKeyAndPutInDecodeMap(abstractDeclaration, rv);
    }
    return rv;
  }

  private final Map<Integer, Integer> EPIC_HACK_mapArrayTypeKeyToLeafTypeKey = Maps.newHashMap();
  private final Map<Integer, Integer> EPIC_HACK_mapGetterKeyToFieldKey = Maps.newHashMap();
  private final Map<Integer, Integer> EPIC_HACK_mapSetterKeyToFieldKey = Maps.newHashMap();

  private final DecodeIdPolicy policy;
}
