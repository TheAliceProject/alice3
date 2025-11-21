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
package org.alice.ide.icons;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities;
import org.lgna.croquet.icon.*;
import org.lgna.project.ast.*;
import org.lgna.story.Color;
import org.lgna.story.Visual;
import org.lgna.story.implementation.alice.AliceResourceUtilities;
import org.lgna.story.resources.*;

import javax.swing.Icon;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class IconFactoryManager {
  private interface ResourceDeclaration {
    IconFactory createIconFactory();
  }

  // @formatter:off
  private static final Set<Class<? extends JointedModelResource>> setOfClassesWithIcons = Sets.newHashSet(
      BipedResource.class,
      FishResource.class,
      FlyerResource.class,
      PropResource.class,
      QuadrupedResource.class,
      SwimmerResource.class,
      SlithererResource.class,
      MarineMammalResource.class,
      TransportResource.class,
      AutomobileResource.class,
      AircraftResource.class,
      WatercraftResource.class,
      TrainResource.class);

  // @formatter:on

  public static Set<Class<? extends JointedModelResource>> getSetOfClassesWithIcons() {
    return setOfClassesWithIcons;
  }

  private abstract static class UrlResourceDeclaration implements ResourceDeclaration {
    protected abstract Class<? extends ModelResource> getModelResourceClass();

    protected abstract String getModelResourceName();

    protected abstract URL getThumbnailUrl();

    @Override
    public final IconFactory createIconFactory() {
      Class<? extends ModelResource> cls = this.getModelResourceClass();
      String modelResourceName = this.getModelResourceName();
      if (modelResourceName == null && setOfClassesWithIcons.contains(cls)) {
        String sb = "images/resources/" + cls.getSimpleName() + ".svg";
        return new SVGIconFactory(Icons.class.getResource(sb));
      }
      return createIconFactoryFromUrl(cls != null ? getThumbnailUrl() : null);
    }

    private IconFactory createIconFactoryFromUrl(URL url) {
      if (url != null) {
        return new TrimmedImageIconFactory(url, 160, 120);
      } else {
        Logger.severe(this, this.getClass(), getModelResourceClass(), getModelResourceName());
        return EmptyIconFactory.getInstance();
      }
    }
  }

  private static final class ResourceEnumConstant extends UrlResourceDeclaration {
    private final ModelResource enm;

    public ResourceEnumConstant(ModelResource enm) {
      assert enm != null;
      this.enm = enm;
    }

    @Override
    protected Class<? extends ModelResource> getModelResourceClass() {
      //todo?
      return enm.getClass();
    }

    @Override
    protected String getModelResourceName() {
      Enum<? extends ModelResource> e = (Enum<? extends ModelResource>) enm;
      return e.name();
    }

    @Override
    protected URL getThumbnailUrl() {
      return AliceResourceUtilities.getThumbnailURL(enm, getModelResourceName());
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj instanceof ResourceEnumConstant other) {
        return this.enm.equals(other.enm);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return this.enm.hashCode();
    }
  }

  private static final class ResourceType extends UrlResourceDeclaration {
    private final Class<? extends ModelResource> cls;

    public ResourceType(Class<? extends ModelResource> cls) {
      assert cls != null;
      this.cls = cls;
    }

    @Override
    protected Class<? extends ModelResource> getModelResourceClass() {
      return this.cls;
    }

    @Override
    protected String getModelResourceName() {
      return null;
    }

    @Override
    protected URL getThumbnailUrl() {
      return AliceResourceUtilities.getThumbnailURL(cls);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj instanceof ResourceType other) {
        return this.cls.equals(other.cls);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return this.cls.hashCode();
    }
  }

  private static final class ResourceInstance implements ResourceDeclaration {
    private final ModelResource instance;

    public ResourceInstance(ModelResource instance) {
      assert instance != null;
      this.instance = instance;
    }

    @Override
    public IconFactory createIconFactory() {
      if (instance instanceof ModelStructure<?, ?> structure) {
        return getIconFactoryForModelStructure(structure);
      } else {
        return NebulousIde.nonfree.createIconFactory(this.instance);
      }
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj instanceof ResourceInstance other) {
        return this.instance.equals(other.instance);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return this.instance.hashCode();
    }
  }

  private static final Map<JavaType, IconFactory> mapTypeToIconFactory = Maps.newHashMap();
  private static final Map<ResourceDeclaration, IconFactory> mapResourceDeclarationToIconFactory = Maps.newHashMap();
  private static final Map<ModelStructure<?, ?>, IconFactory> mapModelStructureToIconFactory = Maps.newHashMap();
  private static final Map<Color, IconFactory> mapColorToObjectMarkerIconFactory = Maps.newHashMap();

  private IconFactoryManager() {
  }

  public static void registerIconFactory(JavaType javaType, IconFactory iconFactory) {
    mapTypeToIconFactory.put(javaType, iconFactory);
  }

  public static void registerIconFactory(Class<?> cls, IconFactory iconFactory) {
    registerIconFactory(JavaType.getInstance(cls), iconFactory);
  }

  private static ResourceDeclaration createResourceDeclarationFromRequiredArguments(SimpleArgumentListProperty requiredArguments) {
    if (requiredArguments.size() == 1) {
      SimpleArgument arg0 = requiredArguments.get(0);
      Expression expression0 = arg0.expression.getValue();
      if (expression0 instanceof InstanceCreation) {
        Object instance = StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForExpression(expression0);
        if (instance instanceof ModelResource modelResource) {
          return new ResourceInstance(modelResource);
        }
      }
    }
    JavaField argumentField = requiredArguments.getJavaField();
    if (argumentField != null) {
      if (argumentField.isStatic()) {
        Field fld = argumentField.getFieldReflectionProxy().getReification();
        try {
          Object o = fld.get(null);
          if (o != null) {
            if (o instanceof JointedModelResource resource) {
              if (o.getClass().isEnum()) {
                return new ResourceEnumConstant(resource);
              }
            }
          }
        } catch (IllegalAccessException iae) {
          iae.printStackTrace();
          return null;
        }
      }
    }

    return null;
  }

  private static int getRequiredArgumentsInInitializer(UserField userField) {
    Expression initializer = userField.initializer.getValue();
    if (initializer instanceof InstanceCreation instanceCreation) {
      return instanceCreation.requiredArguments.size();
    }
    return -1;
  }

  private static ResourceDeclaration createResourceDeclarationFromField(UserField userField) {
    Expression initializer = userField.initializer.getValue();
    if (initializer instanceof InstanceCreation instanceCreation) {
      return createResourceDeclarationFromRequiredArguments(instanceCreation.requiredArguments);
    }
    return null;
  }

  public static IconFactory getRegisteredIconFactory(AbstractType<?, ?, ?> type) {
    if (type != null) {
      JavaType javaType = type.getFirstEncounteredJavaType();
      if (mapTypeToIconFactory.containsKey(javaType)) {
        return mapTypeToIconFactory.get(javaType);
      }
    }
    return null;
  }

  public static IconFactory getIconFactoryForResourceCls(Class<? extends ModelResource> cls) {
    ResourceType resourceType = new ResourceType(cls);
    IconFactory iconFactory = mapResourceDeclarationToIconFactory.get(resourceType);
    if (iconFactory == null) {
      iconFactory = resourceType.createIconFactory();
      mapResourceDeclarationToIconFactory.put(resourceType, iconFactory);
    }
    return iconFactory;
  }

  public static IconFactory getIconFactoryForModelStructure(ModelStructure<?, ?> modelStructure) {
    IconFactory iconFactory = mapModelStructureToIconFactory.get(modelStructure);
    if (iconFactory == null) {
      URL url = null;
      final URI iconURI = modelStructure.getIconURI();
      if (iconURI == null) {
        Logger.severe("Null icon URL for " + modelStructure.getModelClassName());
      } else {
        try {
          url = iconURI.toURL();
        } catch (MalformedURLException e) {
          Logger.severe("Malformed URL: " + iconURI);
        }
      }
      if (url != null) {
        iconFactory = new TrimmedImageIconFactory(url, 160, 120);
      } else {
        iconFactory = EmptyIconFactory.getInstance();
      }
      mapModelStructureToIconFactory.put(modelStructure, iconFactory);
    }
    return iconFactory;
  }

  public static IconFactory getIconFactoryForResourceInstance(ModelResource modelResource) {
    ResourceDeclaration resourceDeclaration;
    if (modelResource.getClass().isEnum()) {
      resourceDeclaration = new ResourceEnumConstant(modelResource);
    } else {
      resourceDeclaration = new ResourceInstance(modelResource);
    }
    return getIconFactoryForResourceDeclaration(resourceDeclaration);
  }

  private static IconFactory getIconFactoryForResourceDeclaration(ResourceDeclaration resourceDeclaration) {
    IconFactory iconFactory = mapResourceDeclarationToIconFactory.get(resourceDeclaration);
    if (iconFactory == null) {
      iconFactory = resourceDeclaration.createIconFactory();
      mapResourceDeclarationToIconFactory.put(resourceDeclaration, iconFactory);
    }
    return iconFactory;
  }

  public static IconFactory getIconFactoryForType(AbstractType<?, ?, ?> type) {
    IconFactory iconFactory = getRegisteredIconFactory(type);
    if (iconFactory != null) {
      return iconFactory;
    } else {
      ResourceDeclaration resourceDeclaration = null;
      AbstractConstructor constructor0 = type != null ? type.getFirstDeclaredConstructor() : null;
      if (constructor0 != null) {
        List<? extends AbstractParameter> parameters = constructor0.getRequiredParameters();
        switch (parameters.size()) {
        case 0:
          if (constructor0 instanceof UserConstructor) {
            NamedUserConstructor userConstructor0 = (NamedUserConstructor) constructor0;
            ConstructorInvocationStatement constructorInvocationStatement = userConstructor0.body.getValue().constructorInvocationStatement.getValue();
            resourceDeclaration = createResourceDeclarationFromRequiredArguments(constructorInvocationStatement.requiredArguments);
          }
          break;
        case 1:
          AbstractParameter parameter0 = parameters.getFirst();
          AbstractType<?, ?, ?> parameter0Type = parameter0.getValueType();
          if (parameter0Type != null) {
            if (parameter0Type.isAssignableTo(ModelResource.class)) {
              Class<? extends ModelResource> cls = (Class<? extends ModelResource>) parameter0Type.getFirstEncounteredJavaType().getClassReflectionProxy().getReification();
              resourceDeclaration = new ResourceType(cls);
            }
          }
          break;
        }
        if (resourceDeclaration != null) {
          iconFactory = getIconFactoryForResourceDeclaration(resourceDeclaration);
          return iconFactory;
        }
      }
    }
    return EmptyIconFactory.getInstance();
  }

  // There is a chance that we're dynamically rendering our icons for our fields, so we have extra logic here for managing that
  private static final Map<UserField, FieldIconFactory> mapFieldToIconFactory = Maps.newWeakHashMap();

  public static void markDynamicIconFactoryForFieldDirty(UserField field) {
    FieldIconFactory iconFactory = mapFieldToIconFactory.get(field);
    if (iconFactory != null) {
      iconFactory.markAllIconsDirty();
    }
  }

  public static IconFactory getDynamicIconFactoryForField(UserField field, IconFactory fallbackIconFactory) {
    AbstractType<?, ?, ?> type = field.getValueType();
    if (type.isAssignableTo(Visual.class)) { //type.isAssignableTo( org.lgna.story.SShape.class ) || type.isAssignableFrom( org.lgna.story.SRoom.class ) || type.isAssignableFrom( org.lgna.story.SGround.class ) ) {
      synchronized (mapFieldToIconFactory) {
        FieldIconFactory iconFactory = mapFieldToIconFactory.get(field);
        if (iconFactory == null) {
          iconFactory = new FieldIconFactory(field, fallbackIconFactory);
          mapFieldToIconFactory.put(field, iconFactory);
        }
        return iconFactory;
      }
    } else {
      return fallbackIconFactory;
    }
  }

  public static IconFactory getDynamicIconFactoryForField(UserField field) {
    return getDynamicIconFactoryForField(field, getIconFactoryForField(field));
  }

  public static IconFactory getIconFactoryForField(UserField userField) {
    if (userField != null) {
      IconFactory iconFactory = getRegisteredIconFactory(userField.getValueType());
      if (iconFactory != null) {
        return iconFactory;
      }
      ResourceDeclaration resourceDeclaration = createResourceDeclarationFromField(userField);
      if (resourceDeclaration != null) {
        iconFactory = getIconFactoryForResourceDeclaration(resourceDeclaration);
        return iconFactory;
      }
      int requiredArgumentCount = getRequiredArgumentsInInitializer(userField);
      if (requiredArgumentCount != 0) {
        Logger.outln("Note: non-zero initializer detected, but no resource specific icon found. Falling back to type for icon for", userField);
      }
      return getIconFactoryForType(userField.getValueType());
    }
    return EmptyIconFactory.getInstance();
  }

  public static IconFactory getIconFactoryForObjectMarker(Color color) {
    IconFactory rv = mapColorToObjectMarkerIconFactory.get(color);
    if (rv == null) {
      Icon imageIcon = MarkerUtilities.getObjectMarkIconForColor(color);
      //todo
      rv = new ImageIconFactory(imageIcon);
      mapColorToObjectMarkerIconFactory.put(color, rv);
    }
    return rv;
  }
}
