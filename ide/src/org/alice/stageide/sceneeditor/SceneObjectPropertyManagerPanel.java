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

package org.alice.stageide.sceneeditor;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.properties.adapter.ColorPropertyAdapter;
import org.alice.ide.properties.adapter.DoublePropertyAdapter;
import org.alice.ide.properties.uicontroller.AdapterControllerUtilities;
import org.alice.ide.properties.uicontroller.PropertyAdapterController;
import org.alice.stageide.croquet.models.sceneditor.AreExtraPropertiesShownState;
import org.alice.stageide.properties.BillboardBackPaintPropertyAdapter;
import org.alice.stageide.properties.BillboardFrontPaintPropertyAdapter;
import org.alice.stageide.properties.FieldNameAdapter;
import org.alice.stageide.properties.GroundOpacityAdapter;
import org.alice.stageide.properties.ModelOpacityAdapter;
import org.alice.stageide.properties.ModelSizeAdapter;
import org.alice.stageide.properties.MoveableTurnableTranslationAdapter;
import org.alice.stageide.properties.MutableRiderVehicleAdapter;
import org.alice.stageide.properties.PaintPropertyAdapter;
import org.alice.stageide.properties.TextFontPropertyAdapter;
import org.alice.stageide.properties.TextValuePropertyAdapter;
import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridBagPanel;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.ToolPalette;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.UserField;
import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.JointedModel;
import org.lgna.story.Model;
import org.lgna.story.MovableTurnable;
import org.lgna.story.MutableRider;
import org.lgna.story.implementation.BillboardImp;
import org.lgna.story.implementation.ConeImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.GroundImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.ModelImp;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.implementation.SphereImp;
import org.lgna.story.implementation.TextImp;
import org.lgna.story.resources.JointedModelResource;


public class SceneObjectPropertyManagerPanel extends GridBagPanel
{
	private UserField selectedField;
	private Object selectedObject;
	private Entity selectedEntity;
	private EntityImp selectedImp;
	
	private org.lgna.project.virtualmachine.UserInstance sceneInstance;
	
	private ShowJointedModelJointAxesState showJointsState;
	
	private org.lgna.croquet.State.ValueObserver<Boolean> showJointsStateObserver = new org.lgna.croquet.State.ValueObserver<Boolean>() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			assert (state instanceof ShowJointedModelJointAxesState);
			SceneObjectPropertyManagerPanel.this.setShowJointsOfField(((ShowJointedModelJointAxesState)state).getField(), nextValue );
		}
	};
	
	private class LabelValueControllerPair
	{
		public Label label;
		public PropertyAdapterController<?> controller;
		
		public LabelValueControllerPair(Label label, PropertyAdapterController<?> controller)
		{
			this.label = label;
			this.controller = controller;
		} 
	}
	
	private List<LabelValueControllerPair> activeControllers = new LinkedList<LabelValueControllerPair>();
	private Label classNameLabel;
	private GridBagPanel morePropertiesPanel;
	private ToolPalette extraPropertiesPalette;
	
	public SceneObjectPropertyManagerPanel()
	{
		super();
		this.classNameLabel = createLabel("Class: ");
		this.morePropertiesPanel = new GridBagPanel();
		this.extraPropertiesPalette = AreExtraPropertiesShownState.getInstance().createToolPalette(this.morePropertiesPanel);
	}
	
	private void setShowJointsOfField(org.lgna.project.ast.AbstractField field, boolean showJoints) {
		JointedModelImp<? extends JointedModel, ? extends JointedModelResource> imp = IDE.getActiveInstance().getSceneEditor().getImplementation( field );
		if (imp != null) {
			imp.setJointAxisVisibility(showJoints);
		}
	}
	
	
	@Override
    public void setBackgroundColor( java.awt.Color color )
	{
	    super.setBackgroundColor(color);
	    this.extraPropertiesPalette.setBackgroundColor(color);
	}

	public void setSceneInstance(org.lgna.project.virtualmachine.UserInstance sceneInstance)
	{
		this.sceneInstance = sceneInstance;
	}
	
	private Label createLabel(String labelText)
	{
		return new Label(labelText, 1.2f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD);
	}
	
	private void addNameAndControllerToPanel(Component< ? > label, Component< ? > controllerPanel, GridBagPanel panel, int index)
	{
	    panel.addComponent( label, new GridBagConstraints( 
            0, //gridX
            index, //gridY
            1, //gridWidth
            1, //gridHeight
            0.0, //weightX
            0.0, //weightY
            GridBagConstraints.EAST, //anchor 
            GridBagConstraints.NONE, //fill
            new Insets(2,2,2,2), // insets (top, left, bottom, right)
            0, //ipadX
            0 ) //ipadY
       );
	   panel.addComponent( controllerPanel, new GridBagConstraints( 
            1, //gridX
            index, //gridY 
            1, //gridWidth
            1, //gridHeight
            0.0, //weightX
            0.0, //weightY
            GridBagConstraints.WEST, //anchor 
            GridBagConstraints.NONE, //fill
            new Insets(2,2,2,2), // insets (top, left, bottom, right)
            0, //ipadX
            0 ) //ipadY
       );
	   panel.addComponent( BoxUtilities.createHorizontalGlue(), new GridBagConstraints( 
	            2, //gridX
	            index, //gridY 
	            1, //gridWidth
	            1, //gridHeight
	            1.0, //weightX
	            0.0, //weightY
	            GridBagConstraints.WEST, //anchor 
	            GridBagConstraints.HORIZONTAL, //fill
	            new Insets(0,0,0,0), // insets (top, left, bottom, right)
	            0, //ipadX
	            0 ) //ipadY
	       );
	}
	
	private void addPropertyToPanel(LabelValueControllerPair propertyPair, GridBagPanel panel, int index )
	{
	    this.addNameAndControllerToPanel(propertyPair.label, propertyPair.controller.getPanel(), panel, index);
	}
	
	private org.alice.ide.properties.adapter.AbstractPropertyAdapter<?, ?> getPropertyAdapterForGetter(org.lgna.project.ast.JavaMethod getter, JavaType declaringType, EntityImp entityImp)
	{
		org.lgna.project.ast.JavaMethod setter = org.lgna.project.ast.AstUtilities.getSetterForGetter( getter, declaringType );
		org.alice.ide.croquet.models.StandardExpressionState state = org.alice.ide.croquet.models.ast.PropertyState.getInstanceForSetter( IDE.PROJECT_GROUP, setter );
		boolean isVisible = setter == null || setter.getVisibility() == null || setter.getVisibility() == Visibility.PRIME_TIME;
		if (setter != null && isVisible)
		{
			if (setter.getName().equalsIgnoreCase("setOpacity"))
			{
				if (entityImp instanceof ModelImp)
				{
					return new ModelOpacityAdapter((ModelImp)entityImp, state);
				}
				else if (entityImp instanceof GroundImp)
				{
					return new GroundOpacityAdapter((GroundImp)entityImp, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setPaint"))
			{
				if (entityImp instanceof GroundImp)
				{
					return new PaintPropertyAdapter<GroundImp>("Paint", (GroundImp)entityImp, ((GroundImp)entityImp).paint, state);
				}
				else if (entityImp instanceof ModelImp)
				{
					return new PaintPropertyAdapter<ModelImp>("Paint", (ModelImp)entityImp, ((ModelImp)entityImp).paint, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setVehicle"))
			{
				if (entityImp.getAbstraction() instanceof MutableRider)
				{
					return new MutableRiderVehicleAdapter((MutableRider)entityImp.getAbstraction(), state, this.sceneInstance);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setAtmosphereColor"))
			{
				if (entityImp instanceof SceneImp)
				{
					return new ColorPropertyAdapter<SceneImp>("Atmosphere Color", (SceneImp)entityImp, ((SceneImp)entityImp).atmosphereColor, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setAmbientLightColor"))
			{
				if (entityImp instanceof SceneImp)
				{
					return new ColorPropertyAdapter<SceneImp>("Light Color", (SceneImp)entityImp, ((SceneImp)entityImp).ambientLightColor, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setBackPaint"))
			{
				if (entityImp instanceof BillboardImp)
				{
					return new BillboardBackPaintPropertyAdapter((BillboardImp)entityImp, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setFrontPaint"))
			{
				if (entityImp instanceof BillboardImp)
				{
					return new BillboardFrontPaintPropertyAdapter((BillboardImp)entityImp, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setFont"))
			{
				if (entityImp instanceof TextImp)
				{
					return new TextFontPropertyAdapter((TextImp)entityImp, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setValue"))
			{
				if (entityImp instanceof TextImp)
				{
					return new TextValuePropertyAdapter((TextImp)entityImp, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setRadius"))
			{
				if (entityImp instanceof SphereImp)
				{
					return new DoublePropertyAdapter<SphereImp>("Opacity", (SphereImp)entityImp, ((SphereImp)entityImp).radius, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setBaseRadius"))
			{
				if (entityImp instanceof ConeImp)
				{
					return new DoublePropertyAdapter<ConeImp>("Base Radius", (ConeImp)entityImp, ((ConeImp)entityImp).baseRadius, state);
				}
			}
			else if (setter.getName().equalsIgnoreCase("setLength"))
			{
				if (entityImp instanceof ConeImp)
				{
					return new DoublePropertyAdapter<ConeImp>("Length", (ConeImp)entityImp, ((ConeImp)entityImp).length, state);
				}
			}
			else
			{
				System.out.println("Unknown setter: "+setter.getName());
			}
		}
		return null;
	}
	
	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		
		this.removeAllComponents();
		this.morePropertiesPanel.removeAllComponents();
		if( this.selectedField != null ) {
			List<org.alice.ide.properties.adapter.AbstractPropertyAdapter<?,?>> propertyAdapters = new LinkedList<org.alice.ide.properties.adapter.AbstractPropertyAdapter<?,?>>();
			
			Iterable< org.lgna.project.ast.JavaMethod > getterMethods = org.lgna.project.ast.AstUtilities.getPersistentPropertyGetters(this.selectedField.getValueType());
			JavaType declaringType = this.selectedField.getValueType().getFirstEncounteredJavaType();
			boolean isScene = this.selectedImp instanceof SceneImp;
			propertyAdapters.add(new FieldNameAdapter(this.selectedField, (StandardExpressionState)null, !isScene));
			
			for (org.lgna.project.ast.JavaMethod getter : getterMethods )
			{
				org.alice.ide.properties.adapter.AbstractPropertyAdapter<?, ?> adapter = getPropertyAdapterForGetter(getter, declaringType, this.selectedImp);
				if (adapter != null)
				{
					propertyAdapters.add(adapter);
				}
			}
			
			if (this.selectedEntity instanceof MovableTurnable)
			{
				propertyAdapters.add( new MoveableTurnableTranslationAdapter((MovableTurnable)this.selectedEntity, null));
			}
			if (this.selectedEntity instanceof Model && this.selectedImp instanceof ModelImp)
			{
				propertyAdapters.add( new ModelSizeAdapter((ModelImp)this.selectedImp, null));
			}
			
			LabelValueControllerPair fieldNamePair = null;
			
			
			if (propertyAdapters.size() != 0)
			{
				int mainPropertyCount = 0;
				int extraPropertyCount = 0;
				//Add all the extra properties to the extra panel and find the name property adapter
				for (org.alice.ide.properties.adapter.AbstractPropertyAdapter propertyAdapter : propertyAdapters)
				{

					PropertyAdapterController<?> propertyController = AdapterControllerUtilities.getValuePanelForPropertyAdapter(propertyAdapter);
					assert propertyController != null;
					LabelValueControllerPair matchingLabelController = new LabelValueControllerPair(createLabel(propertyAdapter.getRepr()+ " = "), propertyController);
					assert matchingLabelController != null;
					if (propertyAdapter instanceof FieldNameAdapter)
					{
					    //Don't add the fieldNameAdapter, just hold onto it so we can add it to the main panel later
					    fieldNamePair = matchingLabelController;
					    //TODO: Localize this
					    fieldNamePair.label.setText("Selected: ");
					}
					else
					{
					   this.addPropertyToPanel(matchingLabelController, this.morePropertiesPanel, extraPropertyCount);
					   extraPropertyCount++;
					}
	                this.activeControllers.add(matchingLabelController);
				}
				
				org.lgna.project.ast.AbstractType< ?,?,? > valueType;
				//Setup the primary properties
				if (this.selectedField != null)
	            {
					valueType = this.selectedField.getValueType();
	            }
	            else
	            {
					valueType = null;
	            }
				
				//Add the object's name
				if (fieldNamePair != null)
				{
				    this.addPropertyToPanel(fieldNamePair, this, mainPropertyCount++);
				}
				//Add the object's class
				this.addNameAndControllerToPanel(this.classNameLabel, org.alice.ide.common.TypeComponent.createInstance( valueType ), this, mainPropertyCount++);
				
				if (this.selectedImp instanceof JointedModelImp) {
					if (this.showJointsState != null) {
						this.showJointsState.removeValueObserver(this.showJointsStateObserver);
					}
					this.showJointsState = ShowJointedModelJointAxesState.getInstance(this.selectedField);
					this.showJointsState.addValueObserver(this.showJointsStateObserver);
					this.addNameAndControllerToPanel(createLabel("Show Joints: "), this.showJointsState.createCheckBox(), this, mainPropertyCount++);
				}
				
				//Lastly, add the extra palette if there are any extra properties
	            if (extraPropertyCount > 0)
	            {
	                this.addComponent( this.extraPropertiesPalette , new GridBagConstraints( 
	                        0, //gridX
	                        mainPropertyCount++, //gridY
	                        2, //gridWidth
	                        1, //gridHeight
	                        1.0, //weightX
	                        0.0, //weightY
	                        GridBagConstraints.WEST, //anchor 
	                        GridBagConstraints.HORIZONTAL, //fill
	                        new Insets(4,0,0,0), // insets (top, left, bottom, right)
	                        0, //ipadX
	                        0 ) //ipadY
	                );
	            }
	            this.addComponent( BoxUtilities.createVerticalGlue() , new GridBagConstraints( 
	                    0, //gridX
	                    mainPropertyCount++, //gridY
	                    2, //gridWidth
	                    1, //gridHeight
	                    1.0, //weightX
	                    1.0, //weightY
	                    GridBagConstraints.CENTER, //anchor 
	                    GridBagConstraints.VERTICAL, //fill
	                    new Insets(0,0,0,0), // insets (top, left, bottom, right)
	                    0, //ipadX
	                    0 ) //ipadY
	            );
			}
		}
	}
	
	public void setField( UserField field )
	{
		this.selectedField = field;
		
		Object instance = IDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForField( field );
		if( instance instanceof org.lgna.story.Entity ) {
			this.selectedEntity = (org.lgna.story.Entity)instance;
			this.selectedImp = ImplementationAccessor.getImplementation(this.selectedEntity);
		}
		else if (instance instanceof org.lgna.story.implementation.EntityImp)
		{
			this.selectedImp = (org.lgna.story.implementation.EntityImp)instance;
			this.selectedEntity = this.selectedImp.getAbstraction();
		}
		this.selectedObject = instance;
		for (LabelValueControllerPair activeController : this.activeControllers)
		{
			if (activeController.controller != null)
			{
				activeController.controller.getPropertyAdapter().stopListening();
//				activeController.controller.getPropertyAdapter().clearListeners();
				activeController.controller.setPropertyAdapter(null);
			}
		}
		this.activeControllers.clear();
		this.refreshLater();
	}
}
