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

package org.alice.stageide.sceneeditor.views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.IDE;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.common.FieldDeclarationPane;
import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.croquet.models.ast.SceneEditorUpdatingPropertyState;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessFactory;
import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.alice.ide.properties.adapter.ColorPropertyAdapter;
import org.alice.ide.properties.adapter.DoublePropertyAdapter;
import org.alice.ide.properties.adapter.SceneFogDensityAdapter;
import org.alice.ide.properties.uicontroller.AdapterControllerUtilities;
import org.alice.ide.properties.uicontroller.PropertyAdapterController;
import org.alice.ide.x.SceneEditorUpdatingProjectEditorAstI18nFactory;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.properties.BillboardBackPaintPropertyAdapter;
import org.alice.stageide.properties.BillboardFrontPaintPropertyAdapter;
import org.alice.stageide.properties.GroundOpacityAdapter;
import org.alice.stageide.properties.ModelOpacityAdapter;
import org.alice.stageide.properties.ModelSizeAdapter;
import org.alice.stageide.properties.MoveableTurnableTranslationAdapter;
import org.alice.stageide.properties.MutableRiderVehicleAdapter;
import org.alice.stageide.properties.PaintPropertyAdapter;
import org.alice.stageide.properties.ResourcePropertyAdapter;
import org.alice.stageide.properties.SelectedInstanceAdapter;
import org.alice.stageide.properties.TextFontPropertyAdapter;
import org.alice.stageide.properties.TextValuePropertyAdapter;
import org.alice.stageide.sceneeditor.ShowJointedModelJointAxesState;
import org.lgna.croquet.State;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.GridBagPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.LocalAccess;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.UserField;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.MutableRider;
import org.lgna.story.SJointedModel;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.implementation.BillboardImp;
import org.lgna.story.implementation.ConeImp;
import org.lgna.story.implementation.CylinderImp;
import org.lgna.story.implementation.DiscImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.GroundImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.ModelImp;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.implementation.SphereImp;
import org.lgna.story.implementation.TextModelImp;
import org.lgna.story.resources.JointedModelResource;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class SceneObjectPropertyManagerPanel extends GridBagPanel {
  private InstanceFactory selectedInstance;
  private SThing selectedEntity;
  private EntityImp selectedImp;

  private UserInstance sceneInstance;

  private ShowJointedModelJointAxesState showJointsState;

  private State.ValueListener<Boolean> showJointsStateObserver = new State.ValueListener<Boolean>() {
    @Override
    public void changing(State<Boolean> state, Boolean prevValue, Boolean nextValue) {
    }

    @Override
    public void changed(State<Boolean> state, Boolean prevValue, Boolean nextValue) {
      assert (state instanceof ShowJointedModelJointAxesState);
      SceneObjectPropertyManagerPanel.this.setShowJointsOfField(((ShowJointedModelJointAxesState) state).getField(), nextValue);
    }
  };

  private class LabelValueControllerPair {
    public Label label;
    public PropertyAdapterController<?> controller;

    public LabelValueControllerPair(Label label, PropertyAdapterController<?> controller) {
      this.label = label;
      this.controller = controller;
    }
  }

  private List<LabelValueControllerPair> activeControllers = new LinkedList<LabelValueControllerPair>();
  private GridBagPanel morePropertiesPanel;

  public SceneObjectPropertyManagerPanel() {
    super();
    this.morePropertiesPanel = new GridBagPanel();
    this.setBackgroundColor(ThemeUtilities.getActiveTheme().getPrimaryBackgroundColor());
    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
  }

  private String findLocalizedText(String key, String defaultValue) {
    String bundleName = SceneObjectPropertyManagerPanel.class.getPackage().getName() + ".ScenePropertyManager";
    try {
      ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle(bundleName, JComponent.getDefaultLocale());
      String rv = resourceBundle.getString(key);
      return rv;
    } catch (MissingResourceException mre) {
      return defaultValue;
    }
  }

  private void setShowJointsOfField(AbstractField field, boolean showJoints) {
    JointedModelImp<? extends SJointedModel, ? extends JointedModelResource> imp = IDE.getActiveInstance().getSceneEditor().getImplementation(field);
    if (imp != null) {
      imp.setAllJointPivotsVisibile(showJoints);
    }
  }

  @Override
  public void setBackgroundColor(Color color) {
    super.setBackgroundColor(color);
    this.morePropertiesPanel.setBackgroundColor(color);
  }

  public void setSceneInstance(UserInstance sceneInstance) {
    this.sceneInstance = sceneInstance;
  }

  private Label createLabel(String labelText) {
    return new Label(labelText, 1.2f, TextWeight.BOLD);
  }

  private void addNameAndControllerToPanel(AwtComponentView<?> label, AwtComponentView<?> controllerPanel, GridBagPanel panel, int index) {
    panel.addComponent(label, new GridBagConstraints(0, //gridX
                                                     index, //gridY
                                                     1, //gridWidth
                                                     1, //gridHeight
                                                     0.0, //weightX
                                                     0.0, //weightY
                                                     GridBagConstraints.EAST, //anchor
                                                     GridBagConstraints.NONE, //fill
                                                     new Insets(2, 2, 2, 2), // insets (top, left, bottom, right)
                                                     0, //ipadX
                                                     0) //ipadY
    );
    panel.addComponent(controllerPanel, new GridBagConstraints(1, //gridX
                                                               index, //gridY
                                                               1, //gridWidth
                                                               1, //gridHeight
                                                               0.0, //weightX
                                                               0.0, //weightY
                                                               GridBagConstraints.WEST, //anchor
                                                               GridBagConstraints.NONE, //fill
                                                               new Insets(2, 2, 2, 2), // insets (top, left, bottom, right)
                                                               0, //ipadX
                                                               0) //ipadY
    );
    panel.addComponent(BoxUtilities.createHorizontalGlue(), new GridBagConstraints(2, //gridX
                                                                                   index, //gridY
                                                                                   1, //gridWidth
                                                                                   1, //gridHeight
                                                                                   1.0, //weightX
                                                                                   0.0, //weightY
                                                                                   GridBagConstraints.WEST, //anchor
                                                                                   GridBagConstraints.HORIZONTAL, //fill
                                                                                   new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
                                                                                   0, //ipadX
                                                                                   0) //ipadY
    );
  }

  private void addPropertyToPanel(LabelValueControllerPair propertyPair, GridBagPanel panel, int index) {
    this.addNameAndControllerToPanel(propertyPair.label, propertyPair.controller.getPanel(), panel, index);
  }

  private AbstractPropertyAdapter<?, ?> getPropertyAdapterForGetter(JavaMethod getter, JavaType declaringType, EntityImp entityImp, UserField field) {
    if (entityImp != null) {
      JavaMethod setter = AstUtilities.getSetterForGetter(getter, declaringType);
      StandardExpressionState state = SceneEditorUpdatingPropertyState.getInstanceForSetter(field, setter);
      boolean isVisible = (setter == null) || (setter.getVisibility() == null) || (setter.getVisibility() == Visibility.PRIME_TIME);
      if ((setter != null) && isVisible) {
        if (setter.getName().equalsIgnoreCase("setOpacity")) {
          if (entityImp instanceof ModelImp) {
            return new ModelOpacityAdapter((ModelImp) entityImp, state);
          } else if (entityImp instanceof GroundImp) {
            return new GroundOpacityAdapter((GroundImp) entityImp, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setFogDensity")) {
          if (entityImp instanceof SceneImp) {
            return new SceneFogDensityAdapter((SceneImp) entityImp, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setResource")) {
          if (entityImp instanceof JointedModelImp<?, ?>) {
            return new ResourcePropertyAdapter((JointedModelImp<?, ?>) entityImp, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setPaint")) {
          if (entityImp instanceof GroundImp) {
            return new PaintPropertyAdapter<GroundImp>("Paint", (GroundImp) entityImp, ((GroundImp) entityImp).paint, state);
          } else if (entityImp instanceof BillboardImp) {
            return new BillboardFrontPaintPropertyAdapter((BillboardImp) entityImp, state);
          } else if (entityImp instanceof ModelImp) {
            return new PaintPropertyAdapter<ModelImp>("Paint", (ModelImp) entityImp, ((ModelImp) entityImp).paint, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setVehicle")) {
          if (entityImp.getAbstraction() instanceof MutableRider) {
            return new MutableRiderVehicleAdapter((MutableRider) entityImp.getAbstraction(), state, this.sceneInstance);
          }
        } else if (setter.getName().equalsIgnoreCase("setFromAboveLightColor")) {
          if (entityImp instanceof SceneImp) {
            return new ColorPropertyAdapter<SceneImp>("Above Light Color", (SceneImp) entityImp, ((SceneImp) entityImp).fromAboveLightColor, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setFromBelowLightColor")) {
          if (entityImp instanceof SceneImp) {
            return new ColorPropertyAdapter<SceneImp>("Below Light Color", (SceneImp) entityImp, ((SceneImp) entityImp).fromBelowLightColor, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setAtmosphereColor")) {
          if (entityImp instanceof SceneImp) {
            return new ColorPropertyAdapter<SceneImp>("Atmosphere Color", (SceneImp) entityImp, ((SceneImp) entityImp).atmosphereColor, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setAmbientLightColor")) {
          if (entityImp instanceof SceneImp) {
            return new ColorPropertyAdapter<SceneImp>("Light Color", (SceneImp) entityImp, ((SceneImp) entityImp).fromAboveLightColor, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setBackPaint")) {
          if (entityImp instanceof BillboardImp) {
            return new BillboardBackPaintPropertyAdapter((BillboardImp) entityImp, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setFrontPaint")) {
          if (entityImp instanceof BillboardImp) {
            return new BillboardFrontPaintPropertyAdapter((BillboardImp) entityImp, state);
          }
        } else if (NebulousIde.nonfree.getPropertyAdapterForGetter(setter, state, entityImp) != null) {
          return NebulousIde.nonfree.getPropertyAdapterForGetter(setter, state, entityImp);
        } else if (setter.getName().equalsIgnoreCase("setFont")) {
          if (entityImp instanceof TextModelImp) {
            return new TextFontPropertyAdapter((TextModelImp) entityImp, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setValue")) {
          if (entityImp instanceof TextModelImp) {
            return new TextValuePropertyAdapter((TextModelImp) entityImp, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setRadius")) {
          if (entityImp instanceof CylinderImp) {
            return new DoublePropertyAdapter<CylinderImp>("Radius", (CylinderImp) entityImp, ((CylinderImp) entityImp).radius, state);
          } else if (entityImp instanceof SphereImp) {
            return new DoublePropertyAdapter<SphereImp>("Radius", (SphereImp) entityImp, ((SphereImp) entityImp).radius, state);
          } else if (entityImp instanceof DiscImp) {
            return new DoublePropertyAdapter<DiscImp>("Radius", (DiscImp) entityImp, ((DiscImp) entityImp).outerRadius, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setBaseRadius")) {
          if (entityImp instanceof ConeImp) {
            return new DoublePropertyAdapter<ConeImp>("Radius", (ConeImp) entityImp, ((ConeImp) entityImp).baseRadius, state);
          }
        } else if (setter.getName().equalsIgnoreCase("setLength")) {
          if (entityImp instanceof CylinderImp) {
            return new DoublePropertyAdapter<CylinderImp>("Length", (CylinderImp) entityImp, ((CylinderImp) entityImp).length, state);
          } else if (entityImp instanceof ConeImp) {
            return new DoublePropertyAdapter<ConeImp>("Length", (ConeImp) entityImp, ((ConeImp) entityImp).length, state);
          }
        } else {
          System.out.println("Unknown setter: " + setter.getName());
        }
      }
    }
    return null;
  }

  @Override
  protected void internalRefresh() {
    super.internalRefresh();

    this.removeAllComponents();
    this.morePropertiesPanel.removeAllComponents();
    if (this.selectedInstance != null) {
      List<AbstractPropertyAdapter<?, ?>> propertyAdapters = new LinkedList<AbstractPropertyAdapter<?, ?>>();

      AbstractType<?, ?, ?> instanceValueType = this.selectedInstance.getValueType();
      if (instanceValueType != null) {
        Iterable<JavaMethod> getterMethods = AstUtilities.getPersistentPropertyGetters(instanceValueType);
        JavaType declaringType = this.selectedInstance.getValueType().getFirstEncounteredJavaType();
        boolean isScene = this.selectedImp instanceof SceneImp;

        UserField selectedField = null;
        if ((this.selectedInstance instanceof ThisFieldAccessFactory)) {
          ThisFieldAccessFactory fieldAccessFactory = (ThisFieldAccessFactory) this.selectedInstance;
          selectedField = fieldAccessFactory.getField();
        }

        //propertyAdapters.add( new SelectedInstanceAdapter( this.selectedInstance, (StandardExpressionState)null ) );

        //        org.alice.ide.ast.FieldInitializerInstanceCreationArgument0State fieldInitializerState = org.alice.ide.ast.FieldInitializerInstanceCreationArgument0State.getInstance( selectedField );
        //        boolean isPerson = false;
        //        if( this.selectedImp instanceof JointedModelImp<?, ?> ) {
        //          JointedModelImp<?, ?> jointedModelImp = (JointedModelImp<?, ?>)this.selectedImp;
        //          if( jointedModelImp.getResource() instanceof org.lgna.story.resources.sims2.PersonResource )
        //          {
        //            isPerson = true;
        //          }
        //        }
        //        if( ( fieldInitializerState != null ) && !isPerson ) {
        //          propertyAdapters.add( new org.alice.stageide.properties.ResourcePropertyAdapter( (JointedModelImp<?, ?>)this.selectedImp, fieldInitializerState ) );
        //        }

        for (JavaMethod getter : getterMethods) {
          AbstractPropertyAdapter<?, ?> adapter = getPropertyAdapterForGetter(getter, declaringType, this.selectedImp, selectedField);
          if (adapter != null) {
            propertyAdapters.add(adapter);
          }
        }

        if (this.selectedEntity instanceof SMovableTurnable) {
          propertyAdapters.add(new MoveableTurnableTranslationAdapter((SMovableTurnable) this.selectedEntity, null));
        }
        if ((this.selectedEntity instanceof SModel) && (this.selectedImp instanceof ModelImp)) {
          propertyAdapters.add(new ModelSizeAdapter((ModelImp) this.selectedImp, null));
        }

        LabelValueControllerPair fieldNamePair = null;

        if (propertyAdapters.size() != 0) {
          int mainPropertyCount = 0;
          int extraPropertyCount = 0;
          //Add all the extra properties to the extra panel and find the name property adapter
          for (AbstractPropertyAdapter propertyAdapter : propertyAdapters) {

            PropertyAdapterController<?> propertyController = AdapterControllerUtilities.getValuePanelForPropertyAdapter(propertyAdapter);
            assert propertyController != null;
            LabelValueControllerPair matchingLabelController = new LabelValueControllerPair(createLabel(propertyAdapter.getLocalizedRepr() + " = "), propertyController);
            assert matchingLabelController != null;
            if (propertyAdapter instanceof SelectedInstanceAdapter) {
              //Don't add the fieldNameAdapter, just hold onto it so we can add it to the main panel later
              fieldNamePair = matchingLabelController;
              //TODO: Localize this
              fieldNamePair.label.setText(this.findLocalizedText("selected", "Selected:"));
            } else {
              this.addPropertyToPanel(matchingLabelController, this.morePropertiesPanel, extraPropertyCount);
              extraPropertyCount++;
            }
            this.activeControllers.add(matchingLabelController);
          }

          AbstractType<?, ?, ?> valueType;
          //Setup the primary properties
          if (this.selectedInstance != null) {
            valueType = this.selectedInstance.getValueType();
          } else {
            valueType = null;
          }

          //Add the object's name
          if (fieldNamePair != null) {
            this.addPropertyToPanel(fieldNamePair, this, mainPropertyCount++);
          }

          if (selectedField != null) {
            SwingComponentView<?> initializerComponent = new FieldDeclarationPane(SceneEditorUpdatingProjectEditorAstI18nFactory.getInstance(), selectedField, false, false);
            initializerComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            this.addComponent(initializerComponent, new GridBagConstraints(0, //gridX
                                                                           mainPropertyCount++, //gridY
                                                                           2, //gridWidth
                                                                           1, //gridHeight
                                                                           0.0, //weightX
                                                                           0.0, //weightY
                                                                           GridBagConstraints.WEST, //anchor
                                                                           GridBagConstraints.NONE, //fill
                                                                           new Insets(2, 2, 2, 2), // insets (top, left, bottom, right)
                                                                           0, //ipadX
                                                                           0) //ipadY
            );
          }

          //Lastly, add the extra palette if there are any extra properties
          if (extraPropertyCount > 0) {
            this.addComponent(this.morePropertiesPanel, new GridBagConstraints(0, //gridX
                                                                               mainPropertyCount++, //gridY
                                                                               2, //gridWidth
                                                                               1, //gridHeight
                                                                               1.0, //weightX
                                                                               0.0, //weightY
                                                                               GridBagConstraints.WEST, //anchor
                                                                               GridBagConstraints.HORIZONTAL, //fill
                                                                               new Insets(4, 0, 0, 0), // insets (top, left, bottom, right)
                                                                               0, //ipadX
                                                                               0) //ipadY
            );
          }

          if ((this.selectedImp instanceof JointedModelImp) && (this.selectedInstance instanceof ThisFieldAccessFactory)) {
            this.addComponent(BoxUtilities.createVerticalSliver(8), new GridBagConstraints(0, //gridX
                                                                                           mainPropertyCount++, //gridY
                                                                                           2, //gridWidth
                                                                                           1, //gridHeight
                                                                                           0.0, //weightX
                                                                                           0.0, //weightY
                                                                                           GridBagConstraints.WEST, //anchor
                                                                                           GridBagConstraints.NONE, //fill
                                                                                           new Insets(2, 2, 2, 2), // insets (top, left, bottom, right)
                                                                                           0, //ipadX
                                                                                           0) //ipadY
            );

            ThisFieldAccessFactory fieldAccessFactory = (ThisFieldAccessFactory) this.selectedInstance;
            if (this.showJointsState != null) {
              this.showJointsState.removeValueListener(this.showJointsStateObserver);
            }

            this.showJointsState = ShowJointedModelJointAxesState.getInstance(fieldAccessFactory.getField());
            this.showJointsState.addValueListener(this.showJointsStateObserver);
            this.addNameAndControllerToPanel(createLabel(this.findLocalizedText("showJoints", "Show Joints:")), this.showJointsState.createCheckBox(), this, mainPropertyCount++);
          }

          this.addComponent(BoxUtilities.createVerticalGlue(), new GridBagConstraints(0, //gridX
                                                                                      mainPropertyCount++, //gridY
                                                                                      2, //gridWidth
                                                                                      1, //gridHeight
                                                                                      1.0, //weightX
                                                                                      1.0, //weightY
                                                                                      GridBagConstraints.CENTER, //anchor
                                                                                      GridBagConstraints.VERTICAL, //fill
                                                                                      new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
                                                                                      0, //ipadX
                                                                                      0) //ipadY
          );
        }
      }
    }
  }

  public void setSelectedInstance(InstanceFactory instance) {
    this.selectedInstance = instance;

    if (instance != null) {
      Expression expression = this.selectedInstance.createExpression();
      if (expression instanceof LocalAccess) {
        //pass
      } else if (expression instanceof ParameterAccess) {
        //pass
      } else {
        Object instanceInJava = IDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForExpression(this.selectedInstance.createExpression());
        if (instanceInJava instanceof SThing) {
          this.selectedEntity = (SThing) instanceInJava;
          this.selectedImp = EmployeesOnly.getImplementation(this.selectedEntity);
        } else if (instanceInJava instanceof EntityImp) {
          this.selectedImp = (EntityImp) instanceInJava;
          this.selectedEntity = this.selectedImp.getAbstraction();
        }
        for (LabelValueControllerPair activeController : this.activeControllers) {
          if (activeController.controller != null) {
            activeController.controller.getPropertyAdapter().stopListening();
            //        activeController.controller.getPropertyAdapter().clearListeners();
            activeController.controller.setPropertyAdapter(null);
          }
        }
      }
    }
    this.activeControllers.clear();
    this.refreshLater();
  }
}
