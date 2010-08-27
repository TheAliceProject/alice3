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

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.Marker;
import org.alice.apis.moveandturn.Model;
import org.alice.apis.moveandturn.Transformable;
import org.alice.ide.IDE;
import org.alice.ide.properties.uicontroller.PropertyAdapterController;
import org.alice.ide.properties.uicontroller.PropertyAdapterUIController;
import org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor;
import org.alice.stageide.properties.FieldNameAdapter;
import org.alice.stageide.properties.MarkerColorAdapter;
import org.alice.stageide.properties.MarkerOpacityAdapter;
import org.alice.stageide.properties.ModelColorAdapter;
import org.alice.stageide.properties.ModelOpacityAdapter;
import org.alice.stageide.properties.TransformableTranslationAdapter;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.croquet.GridBagPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.pattern.event.NameEvent;
import edu.cmu.cs.dennisc.pattern.event.NameListener;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public class SceneObjectPropertyManager extends GridBagPanel implements edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.Accessible>
{
	private AbstractField selectedField;
	private Object selectedObject;
	
	private class LabelValueControllerPair
	{
		public Label label;
		public PropertyAdapterController controller;
		
		public LabelValueControllerPair(Label label, PropertyAdapterController controller)
		{
			this.label = label;
			this.controller = controller;
		}
	}
	
	private PropertyAdapterUIController nameController;
	private PropertyAdapterUIController colorController;
	private PropertyAdapterUIController opacityController;
	private PropertyAdapterUIController positionController;
	private Label classLabel;
	
	public SceneObjectPropertyManager()
	{
		super();
		int rowCount = 0;
		this.nameController = new PropertyAdapterUIController(null);
		this.addComponent( this.nameController, new GridBagConstraints( 
				0, //gridX
				rowCount, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		rowCount++;
		
		this.classLabel = new Label("NO CLASS");
		this.addComponent(new Label("Class:"), new GridBagConstraints( 
				0, //gridX
				rowCount, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( this.classLabel, new GridBagConstraints( 
				1, //gridX
				rowCount, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		rowCount++;
		
		this.colorController = new PropertyAdapterUIController(null);
		
		this.addComponent(this.colorController, new GridBagConstraints( 
				0, //gridX
				rowCount, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		
		rowCount++;
		
		this.opacityController = new PropertyAdapterUIController(null);
		this.addComponent( this.opacityController, new GridBagConstraints( 
				0, //gridX
				rowCount, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		rowCount++;
		this.positionController = new PropertyAdapterUIController(null);
		this.addComponent( this.positionController, new GridBagConstraints( 
				0, //gridX
				rowCount, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
	}
	
	private static Class<?>[] PROPERTY_ADAPTER_CLASSES= {
		FieldNameAdapter.class,
		MarkerColorAdapter.class,
		MarkerOpacityAdapter.class,
		ModelColorAdapter.class,
		ModelOpacityAdapter.class,
		TransformableTranslationAdapter.class,
	};
	
	private static List<Class<?>> getClassHierarchy(Class<?> startingClass)
	{
		List<Class<?>> classes = new LinkedList<Class<?>>();
		if (startingClass != null)
		{
			classes.add(startingClass);
			Class<?> superClass = startingClass.getSuperclass();
			while (superClass != null)
			{
				classes.add(superClass);
				superClass = superClass.getSuperclass();
			}
		}
		return classes;
	}

	public static List<org.alice.ide.properties.adapter.PropertyAdapter<?,?>> getPropertyAdaptersForObject(Object object)
	{
		List<org.alice.ide.properties.adapter.PropertyAdapter<?,?>> propertyList = new LinkedList<org.alice.ide.properties.adapter.PropertyAdapter<?,?>>();
		if (object != null)
		{
			PrintUtilities.println("Trying to get adapters for "+object.getClass().getSimpleName());
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
		if (propertyList.size() > 0)
		{
			PrintUtilities.println("Found adapters for "+object.getClass().getSimpleName()+":");
			for (org.alice.ide.properties.adapter.PropertyAdapter<?,?> adapter : propertyList)
			{
				PrintUtilities.println("  "+adapter.getRepr());
			}
		}
		else
		{
			PrintUtilities.println("Found no adapters for "+object);
		}
		return propertyList;
	}
	
	public void changed(edu.cmu.cs.dennisc.alice.ast.Accessible nextValue) 
	{
		this.nameController.setPropertyAdapter(null);
		this.colorController.setPropertyAdapter(null);
		this.opacityController.setPropertyAdapter(null);
		this.positionController.setPropertyAdapter(null);
		this.selectedField = null;
		this.selectedObject = null;
		if( nextValue instanceof AbstractField ) {
			AbstractField field = (AbstractField)nextValue;
			this.selectedField = field;
			Object instance = ((MoveAndTurnSceneEditor)(IDE.getSingleton().getSceneEditor())).getInstanceForField( field );
			if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
				edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
				instance = instanceInAlice.getInstanceInJava();
			}
			this.selectedObject = instance;
		}
		List<org.alice.ide.properties.adapter.PropertyAdapter<?,?>> propertyAdapters = new LinkedList<org.alice.ide.properties.adapter.PropertyAdapter<?,?>>();
		propertyAdapters.addAll(getPropertyAdaptersForObject(this.selectedField));
		propertyAdapters.addAll(getPropertyAdaptersForObject(this.selectedObject));
		
		for (org.alice.ide.properties.adapter.PropertyAdapter<?,?> adapter : propertyAdapters)
		{
			PrintUtilities.println(adapter.getRepr());
		}
		
		if (this.selectedField != null)
		{
			this.classLabel.setText(this.selectedField.getDesiredValueType().getName());
		}
		else
		{
			this.classLabel.setText("NO FIELD, NO CLASS");
		}
	}

}
