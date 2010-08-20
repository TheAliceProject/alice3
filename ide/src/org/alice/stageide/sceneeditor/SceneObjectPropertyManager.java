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

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.Marker;
import org.alice.apis.moveandturn.Model;
import org.alice.apis.moveandturn.Transformable;
import org.alice.ide.IDE;
import org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.croquet.GridBagPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.pattern.event.NameEvent;
import edu.cmu.cs.dennisc.pattern.event.NameListener;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public class SceneObjectPropertyManager extends GridBagPanel implements edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.Accessible>
{
	private AbstractField selectedField;
	private Transformable selectedObject;
	
	private Label nameLabel;
	private Label colorLabel;
	private Label opacityLabel;
	private Label positionLabel;
	private Label classLabel;
	
//	private NameListener nameListener = new NameListener()
//	{
//		public void nameChanging(NameEvent nameEvent) {}
//		
//		public void nameChanged(NameEvent nameEvent)
//		{
//			updateNameInformation();
//		}
//	};

	private PropertyListener nameListener = new PropertyListener()
	{
		public void propertyChanging(PropertyEvent e) {}
		
		public void propertyChanged(PropertyEvent e)
		{
			updateNameInformation();
		}
	};
	
	private AbsoluteTransformationListener transformationListener = new AbsoluteTransformationListener() 
	{	
		public void absoluteTransformationChanged(
				AbsoluteTransformationEvent absoluteTransformationEvent) 
		{
			updateTransformationInformation();
		}
	};
	
	private PropertyListener colorListener = new PropertyListener() 
	{
		public void propertyChanging(PropertyEvent e) {}
		
		public void propertyChanged(PropertyEvent e)
		{
			updateColorInformation();
		}
	};
	
	private PropertyListener opacityListener = new PropertyListener() 
	{
		public void propertyChanging(PropertyEvent e) {}
		
		public void propertyChanged(PropertyEvent e)
		{
			updateOpacityInformation();
		}
	};
	
	public SceneObjectPropertyManager()
	{
		super();
		int rowCount = 0;
		this.nameLabel = new Label("NO NAME");
		this.addComponent(new Label("Selected Object:"), new GridBagConstraints( 
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
		this.addComponent( this.nameLabel, new GridBagConstraints( 
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
		
		this.addComponent(new Label("Color:"), new GridBagConstraints( 
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
		this.colorLabel = new Label("NO COLOR");
		this.addComponent( this.colorLabel, new GridBagConstraints( 
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
		
		this.addComponent(new Label("Opacity:"), new GridBagConstraints( 
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
		this.opacityLabel = new Label("NO OPACITY");
		this.addComponent( this.opacityLabel, new GridBagConstraints( 
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
		
		this.addComponent(new Label("Position:"), new GridBagConstraints( 
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
		this.positionLabel = new Label("NO POSITION");
		this.addComponent( this.positionLabel, new GridBagConstraints( 
				1, //gridX
				rowCount, //gridY
				1, //gridWidth
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

	public void changed(edu.cmu.cs.dennisc.alice.ast.Accessible nextValue) 
	{
		if (this.selectedField != null && this.selectedField.getNamePropertyIfItExists() != null)
		{
			this.selectedField.getNamePropertyIfItExists().addPropertyListener(this.nameListener);
		}
		if (this.selectedObject != null)
		{
			this.selectedObject.getSGTransformable().removeAbsoluteTransformationListener(this.transformationListener);
			if (this.selectedObject instanceof Model)
			{
				((Model)this.selectedObject).getSGSingleAppearance().diffuseColor.removePropertyListener(this.colorListener);
				((Model)this.selectedObject).getSGSingleAppearance().opacity.removePropertyListener(this.opacityListener);
			}
			else if (this.selectedObject instanceof Marker)
			{
				((Marker)this.selectedObject).getSGSingleAppearance().diffuseColor.removePropertyListener(this.colorListener);
				((Marker)this.selectedObject).getSGSingleAppearance().opacity.removePropertyListener(this.opacityListener);
			}
		}
		this.selectedField = null;
		this.selectedObject = null;
		if( nextValue instanceof AbstractField ) {
			AbstractField field = (AbstractField)nextValue;
			this.selectedField = field;
			if (this.selectedField.getNamePropertyIfItExists() != null)
			{
				this.selectedField.getNamePropertyIfItExists().addPropertyListener(this.nameListener);
			}
			Object instance = ((MoveAndTurnSceneEditor)(IDE.getSingleton().getSceneEditor())).getInstanceForField( field );
			if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
				edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
				instance = instanceInAlice.getInstanceInJava();
			}
			if( instance instanceof org.alice.apis.moveandturn.Model ) {
				this.selectedObject = (org.alice.apis.moveandturn.Model)instance;
				((Model)this.selectedObject).getSGSingleAppearance().diffuseColor.addPropertyListener(this.colorListener);
				((Model)this.selectedObject).getSGSingleAppearance().opacity.addPropertyListener(this.opacityListener);
			}
			else if( instance instanceof org.alice.apis.moveandturn.Marker ) {
				this.selectedObject = (org.alice.apis.moveandturn.Marker)instance;
				((Marker)this.selectedObject).getSGSingleAppearance().diffuseColor.addPropertyListener(this.colorListener);
				((Marker)this.selectedObject).getSGSingleAppearance().opacity.addPropertyListener(this.opacityListener);
			}
			if (this.selectedObject != null)
			{
				this.selectedObject.getSGTransformable().addAbsoluteTransformationListener(this.transformationListener);
			}
		}
		setUIFromObject();
	}
	
	private static java.text.NumberFormat format = new java.text.DecimalFormat( "0.00" );
	
	private void updateNameInformation()
	{
		this.nameLabel.setText(this.selectedField.getName());
	}
	
	private void updateTransformationInformation()
	{
		Point3 position = selectedObject.getPosition(AsSeenBy.SCENE);
		this.positionLabel.setText("("+format.format(position.x)+", "+format.format(position.y)+","+format.format(position.z)+")");
	}
	
	private void updateColorInformation()
	{
		this.colorLabel.getAwtComponent().setOpaque(true);
		if( selectedObject instanceof org.alice.apis.moveandturn.Marker ) 
		{
			org.alice.apis.moveandturn.Marker marker = (org.alice.apis.moveandturn.Marker)selectedObject;
			this.colorLabel.setText("                  ");
			this.colorLabel.setBackgroundColor(marker.getMarkerColor().getAsAWTColor());
		}
		else if( selectedObject instanceof org.alice.apis.moveandturn.Model ) 
		{
			org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)selectedObject; 
			this.colorLabel.setText("                  ");
			this.colorLabel.setBackgroundColor(model.getColor().getInternal().getAsAWTColor());
		}
	}
	
	private void updateOpacityInformation()
	{
		if( selectedObject instanceof org.alice.apis.moveandturn.Marker ) 
		{
			org.alice.apis.moveandturn.Marker marker = (org.alice.apis.moveandturn.Marker)selectedObject;
			this.opacityLabel.setText(marker.getOpacity().toString());
		}
		else if( selectedObject instanceof org.alice.apis.moveandturn.Model ) 
		{
			org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)selectedObject; 
			this.opacityLabel.setText(model.getOpacity().toString());
		}
	}
	
	private void setUIFromObject()
	{
		if (this.selectedField != null)
		{
			updateNameInformation();
			this.classLabel.setText(this.selectedField.getDesiredValueType().getName());
		}
		else
		{
			this.nameLabel.setText("NO FIELD, NO NAME");
			this.classLabel.setText("NO FIELD, NO CLASS");
		}
		if (this.selectedObject != null)
		{
			updateTransformationInformation();
			updateColorInformation();
			updateOpacityInformation();
		}
		else
		{
			this.colorLabel.setText("NO OBJECT, NO COLOR");
			this.colorLabel.getAwtComponent().setOpaque(false);
			this.positionLabel.setText("NO OBJECT, NO POSITION");
			this.opacityLabel.setText("NO OBJECT, NO OPACITY");
		}
	}

}
