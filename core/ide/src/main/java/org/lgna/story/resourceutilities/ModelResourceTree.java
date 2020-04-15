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
package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.tweedle.file.ModelManifest;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.story.SModel;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.ModelResource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ModelResourceTree {
  ModelResourceTree(List<Class<? extends ModelResource>> classes) {
    this.galleryTree = this.createClassTree(classes);
  }

  public GalleryResourceTreeNode getTree() {
    return this.galleryTree;
  }

  Collection<TypeDefinedGalleryTreeNode> getDynamicNodes() {
    return dynamicResources.values();
  }

  List<ManifestDefinedGalleryTreeNode> addUserModels(List<ModelManifest> userModels) {
    List<ManifestDefinedGalleryTreeNode> newNodes = new ArrayList<>();
    for (ModelManifest userModel : userModels) {
      newNodes.add(addUserModel(userModel));
    }
    return newNodes;
  }

  private ManifestDefinedGalleryTreeNode addUserModel(ModelManifest userModel) {
    TypeDefinedGalleryTreeNode parentNode = dynamicResources.get(userModel.parentClass);
    if (parentNode == null) {
      Dialogs.showInfo("Unable to find parent class " + userModel.parentClass);
    }

    ManifestDefinedGalleryTreeNode manifestNode = new ManifestDefinedGalleryTreeNode(userModel);
    manifestNode.setParent(parentNode);
    return manifestNode;
  }

  TypeDefinedGalleryTreeNode getGalleryResourceTreeNodeForJavaType(AbstractType<?, ?, ?> type) {
    return this.galleryTree.getDescendantOfJavaType(type);
  }

  private static UserPackage getAlicePackage(Class<?> resourceClass, Class<?> rootClass) {
    String resourcePackage = resourceClass.getPackage().getName();
    String rootPackage = rootClass.getPackage().getName();
    int rootIndex = resourcePackage.indexOf(rootPackage);
    if (rootIndex != -1) {
      resourcePackage = resourcePackage.substring(rootIndex + rootPackage.length());
      if (resourcePackage.startsWith(".")) {
        resourcePackage = resourcePackage.substring(1);
      }
    }
    resourcePackage = AliceResourceClassUtilities.DEFAULT_PACKAGE + resourcePackage;
    return new UserPackage(resourcePackage);
  }

  //The Stack<Class<?>> classes is a stack of classes representing the hierarchy of the classes, with the parent class at the top of the stack
  private void addNodes(TypeDefinedGalleryTreeNode root, Stack<Class<? extends ModelResource>> classes) {
    Class<?> rootClass = null;
    TypeDefinedGalleryTreeNode currentNode = root;
    while (!classes.isEmpty()) {
      Class<? extends ModelResource> currentClass = classes.pop();
      if (currentClass.isAnnotationPresent(Deprecated.class)) {
        continue;
      }

      //The root class is the one at the top of the stack, so grab it the first time around
      if (rootClass == null) {
        rootClass = currentClass;
      }
      TypeDefinedGalleryTreeNode parentNode = currentNode;
      TypeDefinedGalleryTreeNode classNode = null;
      if (resourceClassToNodeMap.containsKey(currentClass)) {
        classNode = resourceClassToNodeMap.get(currentClass);
      } else {
        //Build a new TypeDefinedGalleryTreeNode for the current class
        classNode = createNode(rootClass, currentClass, parentNode);
        if (root == null) { //if the root node passed in is null, assign it to be the node from the first class we process
          root = classNode;
        }
      }
      currentNode = classNode;
    }
  }

  private TypeDefinedGalleryTreeNode createNode(Class<?> rootClass, Class<? extends ModelResource> currentClass, TypeDefinedGalleryTreeNode parentNode) {
    String aliceClassName = AliceResourceClassUtilities.getAliceClassName(currentClass);
    UserPackage packageName = getAlicePackage(currentClass, rootClass);

    UserMethod[] noMethods = {};
    UserField[] noFields = {};
    Field[] resourceConstants = AliceResourceClassUtilities.getFieldsOfType(currentClass, ModelResource.class);
    Class<? extends SModel> modelClass = AliceResourceClassUtilities.getModelClassForResourceClass(currentClass);
    AbstractType parentType = null;
    if ((parentNode == null) || (parentNode.getUserType() == null)) {
      parentType = JavaType.getInstance(modelClass);
    } else {
      parentType = parentNode.getUserType();
    }
    NamedUserConstructor[] noConstructors = {};
    NamedUserType aliceType = new NamedUserType(aliceClassName, packageName, parentType, noConstructors, noMethods, noFields);

    TypeDefinedGalleryTreeNode classNode = new TypeDefinedGalleryTreeNode(aliceType, currentClass, modelClass);
    classNode.setParent(parentNode);
    resourceClassToNodeMap.put(currentClass, classNode);
    registerDynamicClassIfFound(currentClass, classNode);
    if (resourceConstants.length != 0) {
      for (Field f : resourceConstants) {
        String fieldClassName = AliceResourceClassUtilities.getClassNameFromName(f.getName()) + aliceClassName;
        NamedUserType subParentType = classNode.getUserType();
        NamedUserType fieldType = new NamedUserType(fieldClassName, packageName, subParentType, noConstructors, noMethods, noFields);
        TypeDefinedGalleryTreeNode fieldNode = new TypeDefinedGalleryTreeNode(fieldType, currentClass, null);
        try {
          ModelResource resource = (ModelResource) f.get(null);
          JavaField javaField = JavaField.getInstance(f);
          fieldNode.setJavaField(javaField);
        } catch (Exception e) {
          e.printStackTrace();
        }
        fieldNode.setParent(classNode);
        resourceClassToNodeMap.put(f, fieldNode);
      }
    }
    return classNode;
  }

  private void registerDynamicClassIfFound(Class<? extends ModelResource> resourceClass, TypeDefinedGalleryTreeNode classNode) {
    try {
      Class dynamicResourceClass = Class.forName("org.lgna.story.resources.Dynamic" + resourceClass.getSimpleName());
      if (resourceClass.isAssignableFrom(dynamicResourceClass)) {
        dynamicResources.put(classNode.getUserType().getName(), classNode);
        classNode.setDynamicResource(dynamicResourceClass);
      }
    } catch (ClassNotFoundException ignored) {
    }
  }

  private TypeDefinedGalleryTreeNode createClassTree(List<Class<? extends ModelResource>> classes) {
    TypeDefinedGalleryTreeNode topNode = new TypeDefinedGalleryTreeNode(null, null, null);

    for (Class<? extends ModelResource> cls : classes) {
      Class<? extends ModelResource> currentClass = cls;
      Stack<Class<? extends ModelResource>> classStack = new Stack<Class<? extends ModelResource>>();
      Class<?>[] interfaces = null;
      while (currentClass != null) {
        classStack.push(currentClass);
        boolean isTopLevelResource = AliceResourceClassUtilities.isTopLevelResource(currentClass);
        if (isTopLevelResource) {
          break;
        }

        interfaces = currentClass.getInterfaces();
        currentClass = null;
        if ((interfaces != null) && (interfaces.length > 0)) {
          for (Class<?> intrfc : interfaces) {
            if (ModelResource.class.isAssignableFrom(intrfc)) {
              currentClass = (Class<? extends ModelResource>) intrfc;
              break;
            }
          }
        }
      }
      addNodes(topNode, classStack);
    }
    return topNode;
  }

  private final TypeDefinedGalleryTreeNode galleryTree;
  private final Map<Object, TypeDefinedGalleryTreeNode> resourceClassToNodeMap = Maps.newHashMap();
  private final Map<String, TypeDefinedGalleryTreeNode> dynamicResources = Maps.newHashMap();

  public TypeDefinedGalleryTreeNode getNodeForResource(Object resource) {
    return resourceClassToNodeMap.get(resource);
  }
}
