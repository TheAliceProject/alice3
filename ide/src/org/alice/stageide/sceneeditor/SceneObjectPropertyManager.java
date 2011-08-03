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
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import org.alice.ide.IDE;
import org.alice.ide.properties.uicontroller.AdapterControllerUtilities;
import org.alice.ide.properties.uicontroller.PropertyAdapterController;
import org.alice.stageide.croquet.models.sceneditor.AreExtraPropertiesShownState;
import org.alice.stageide.properties.FieldNameAdapter;
import org.alice.stageide.properties.MarkerColorAdapter;
import org.alice.stageide.properties.MarkerOpacityAdapter;
import org.alice.stageide.properties.ModelColorAdapter;
import org.alice.stageide.properties.ModelOpacityAdapter;
import org.alice.stageide.properties.ModelScaleAdapter;
import org.alice.stageide.properties.TransformableTranslationAdapter;
import org.alice.stageide.properties.TransformableVehicleAdapter;
import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridBagPanel;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.ToolPalette;
import org.lgna.project.ast.AbstractField;


public class SceneObjectPropertyManager extends GridBagPanel implements org.lgna.croquet.ListSelectionState.ValueObserver<org.lgna.project.ast.Accessible>
{
	private AbstractField selectedField;
	private Object selectedObject;
	
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
	
	private List<LabelValueControllerPair> labelControllerList = new LinkedList<LabelValueControllerPair>();
	private List<LabelValueControllerPair> activeControllers = new LinkedList<LabelValueControllerPair>();
	private Label classLabel;
	private Label classNameLabel;
	private GridBagPanel morePropertiesPanel;
	private ToolPalette extraPropertiesPalette;
	
	public SceneObjectPropertyManager()
	{
		super();
		this.classLabel = new Label("NO CLASS");
		this.classNameLabel = createLabel("Class = ");
		this.morePropertiesPanel = new GridBagPanel();
		this.extraPropertiesPalette = AreExtraPropertiesShownState.getInstance().createToolPalette(this.morePropertiesPanel);
	}
	
	@Override
    public void setBackgroundColor( java.awt.Color color )
	{
	    super.setBackgroundColor(color);
	    this.extraPropertiesPalette.setBackgroundColor(color);
	}
	
	private static Class<?>[] PROPERTY_ADAPTER_CLASSES= {
		FieldNameAdapter.class,
		MarkerColorAdapter.class,
		MarkerOpacityAdapter.class,
		ModelColorAdapter.class,
		ModelOpacityAdapter.class,
		TransformableTranslationAdapter.class,
		TransformableVehicleAdapter.class,
		ModelScaleAdapter.class,
	};

	public static List<org.alice.ide.properties.adapter.PropertyAdapter<?,?>> getPropertyAdaptersForObject(Object object)
	{
		List<org.alice.ide.properties.adapter.PropertyAdapter<?,?>> propertyList = new LinkedList<org.alice.ide.properties.adapter.PropertyAdapter<?,?>>();
		if (object != null)
		{
			for (Class<?> adapterClass : PROPERTY_ADAPTER_CLASSES)
			{
				Constructor<?>[] constructors = adapterClass.getConstructors();
				for (Constructor<?> constructor : constructors)
				{
					if (constructor.getParameterTypes().length == 1)
					{
						if (constructor.getParameterTypes()[0].isAssignableFrom(object.getClass()))
						{
							try
							{
								org.alice.ide.properties.adapter.PropertyAdapter<?,?> propertyAdapter = (org.alice.ide.properties.adapter.PropertyAdapter<?,?>)constructor.newInstance(object);
								propertyList.add(propertyAdapter);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return propertyList;
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
            1.0, //weightX
            0.0, //weightY
            GridBagConstraints.WEST, //anchor 
            GridBagConstraints.HORIZONTAL, //fill
            new Insets(2,2,2,2), // insets (top, left, bottom, right)
            0, //ipadX
            0 ) //ipadY
       );
	}
	
	private void addPropertyToPanel(LabelValueControllerPair propertyPair, GridBagPanel panel, int index )
	{
	    this.addNameAndControllerToPanel(propertyPair.label, propertyPair.controller.getPanel(), panel, index);
	}

	public void changing( org.lgna.croquet.State< org.lgna.project.ast.Accessible > state, org.lgna.project.ast.Accessible prevValue, org.lgna.project.ast.Accessible nextValue, boolean isAdjusting ) {
	}
	public void changed( org.lgna.croquet.State< org.lgna.project.ast.Accessible > state, org.lgna.project.ast.Accessible prevValue, org.lgna.project.ast.Accessible nextValue, boolean isAdjusting ) {
		this.selectedField = null;
		this.selectedObject = null;
		if( nextValue instanceof AbstractField ) {
			AbstractField field = (AbstractField)nextValue;
			this.selectedField = field;
			Object instance = IDE.getActiveInstance().getSceneEditor().getInstanceInAliceVMForField( field );
			if( instance instanceof org.lgna.project.virtualmachine.UserInstance ) {
				org.lgna.project.virtualmachine.UserInstance instanceInAlice = (org.lgna.project.virtualmachine.UserInstance)instance;
				instance = instanceInAlice.getInstanceInJava();
			}
			this.selectedObject = instance;
		}
		
		for (LabelValueControllerPair activeController : this.activeControllers)
		{
			if (activeController.controller != null)
			{
				activeController.controller.setPropertyAdapter(null);
			}
		}
		this.activeControllers.clear();
		
		this.removeAllComponents();
		this.morePropertiesPanel.removeAllComponents();
		
		
		List<org.alice.ide.properties.adapter.PropertyAdapter<?,?>> propertyAdapters = new LinkedList<org.alice.ide.properties.adapter.PropertyAdapter<?,?>>();
		propertyAdapters.addAll(getPropertyAdaptersForObject(this.selectedField));
		propertyAdapters.addAll(getPropertyAdaptersForObject(this.selectedObject));
		LabelValueControllerPair fieldNamePair = null;
		if (propertyAdapters.size() != 0)
		{
			int mainPropertyCount = 0;
			int extraPropertyCount = 0;
			//Add all the extra properties to the extra panel and find the name property adapter
			for (org.alice.ide.properties.adapter.PropertyAdapter propertyAdapter : propertyAdapters)
			{
				LabelValueControllerPair matchingLabelController = null;
				for (LabelValueControllerPair labelController : this.labelControllerList)
				{
					if (labelController.controller.getPropertyType().equals(propertyAdapter.getPropertyType()))
					{
						matchingLabelController = labelController;
						matchingLabelController.label.setText(propertyAdapter.getRepr()+ " = ");
						matchingLabelController.controller.setPropertyAdapter(propertyAdapter);
						break;
					}
				}
				if (matchingLabelController == null)
				{
					PropertyAdapterController<?> propertyController = AdapterControllerUtilities.getValuePanelForPropertyAdapter(propertyAdapter);
					assert propertyController != null;
					matchingLabelController = new LabelValueControllerPair(createLabel(propertyAdapter.getRepr()+ " = "), propertyController);
					this.labelControllerList.add(matchingLabelController);
				}
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
			//Setup the primary properties
			if (this.selectedField != null)
            {
                this.classLabel.setText(this.selectedField.getDesiredValueType().getName());
            }
            else
            {
                this.classLabel.setText("NO FIELD, NO CLASS");
            }
			
			//Add the object's name
			if (fieldNamePair != null)
			{
			    this.addPropertyToPanel(fieldNamePair, this, mainPropertyCount++);
			}
			//Add the object's class
			this.addNameAndControllerToPanel(this.classNameLabel, this.classLabel, this, mainPropertyCount++);
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
		this.revalidateAndRepaint();
	}

}
