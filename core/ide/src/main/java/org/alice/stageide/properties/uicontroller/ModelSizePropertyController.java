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

package org.alice.stageide.properties.uicontroller;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.alice.ide.properties.uicontroller.AbstractAdapterController;
import org.alice.ide.properties.uicontroller.DoubleTextField;
import org.alice.stageide.properties.IsAllScaleLinkedState;
import org.alice.stageide.properties.IsXYScaleLinkedState;
import org.alice.stageide.properties.IsXZScaleLinkedState;
import org.alice.stageide.properties.IsYZScaleLinkedState;
import org.alice.stageide.properties.LinkScaleButton;
import org.alice.stageide.properties.ModelSizeAdapter;
import org.lgna.croquet.views.BooleanStateButton;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingAdapter;
import org.lgna.story.implementation.ModelImp;

import edu.cmu.cs.dennisc.math.Dimension3;

public class ModelSizePropertyController extends AbstractAdapterController<Dimension3> {

	private org.lgna.croquet.State.ValueListener<Boolean> linkStateValueObserver = new org.lgna.croquet.State.ValueListener<Boolean>() {
		@Override
		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			ModelSizePropertyController.this.updateUIFromLinkState( state, prevValue, nextValue );
		}
	};

	private ActionListener valueChangeListener;

	private DoubleTextField widthField;
	private DoubleTextField heightField;
	private DoubleTextField depthField;

	private Label widthLabel;
	private Label heightLabel;
	private Label depthLabel;

	private Button resetButton;

	private BooleanStateButton<javax.swing.AbstractButton> linkAllButton;
	private BooleanStateButton<javax.swing.AbstractButton> linkXYButton;
	private BooleanStateButton<javax.swing.AbstractButton> linkXZButton;
	private BooleanStateButton<javax.swing.AbstractButton> linkYZButton;

	private boolean isUpdatingState = false;
	private boolean doUpdateOnAdapter = true;

	private final int GLUE_X_POS = 8;
	private final int RESET_X_POS = 7;
	private final int SCALE_ALL_X_POS = 6;
	private final int SCALE_XZ_X_POS = 5;
	private final int SCALE_YZ_X_POS = 4;
	private final int SCALE_XY_X_POS = 3;

	public ModelSizePropertyController( ModelSizeAdapter propertyAdapter ) {
		super( propertyAdapter );
		IsAllScaleLinkedState.getInstance().addValueListener( linkStateValueObserver );
		IsXYScaleLinkedState.getInstance().addValueListener( linkStateValueObserver );
		IsXZScaleLinkedState.getInstance().addValueListener( linkStateValueObserver );
		IsYZScaleLinkedState.getInstance().addValueListener( linkStateValueObserver );
	}

	@Override
	public Class<?> getPropertyType() {
		return org.lgna.story.SModel.class;
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
		this.valueChangeListener = new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				ModelSizePropertyController.this.updateAdapterFromUI( e );
			}
		};

		String widthString = AbstractPropertyAdapter.getLocalizedString( "Width" );
		String heightString = AbstractPropertyAdapter.getLocalizedString( "Height" );
		String depthString = AbstractPropertyAdapter.getLocalizedString( "Depth" );
		this.widthLabel = new Label( widthString + ":" );
		this.heightLabel = new Label( heightString + ":" );
		this.depthLabel = new Label( depthString + ":" );

		this.widthField = new DoubleTextField( 3 );
		this.widthField.addActionListener( this.valueChangeListener );

		this.heightField = new DoubleTextField( 3 );
		this.heightField.addActionListener( this.valueChangeListener );

		this.depthField = new DoubleTextField( 3 );
		this.depthField.addActionListener( this.valueChangeListener );

		//		this.linkXYButton = new LinkScaleButton( IsXYScaleLinkedState.getInstance(), IsXYScaleLinkedState.class.getResource( "images/subScaleLinked.png" ), IsXYScaleLinkedState.class.getResource( "images/subScaleUnlinked.png" ) );
		//		this.linkXZButton = new LinkScaleButton( IsXZScaleLinkedState.getInstance(), IsXZScaleLinkedState.class.getResource( "images/subScaleLinked_long.png" ), IsXZScaleLinkedState.class.getResource( "images/subScaleUnlinked_long.png" ) );
		//		this.linkYZButton = new LinkScaleButton( IsYZScaleLinkedState.getInstance(), IsYZScaleLinkedState.class.getResource( "images/subScaleLinked.png" ), IsYZScaleLinkedState.class.getResource( "images/subScaleUnlinked.png" ) );
		//		this.linkAllButton = new LinkScaleButton( IsAllScaleLinkedState.getInstance() );
		this.linkXYButton = new LinkScaleButton( IsXYScaleLinkedState.getInstance() );
		this.linkXZButton = new LinkScaleButton( IsXZScaleLinkedState.getInstance() );
		this.linkYZButton = new LinkScaleButton( IsYZScaleLinkedState.getInstance() );
		this.linkAllButton = new LinkScaleButton( IsAllScaleLinkedState.getInstance() );

		this.addComponent( this.widthLabel, new GridBagConstraints(
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( new SwingAdapter( this.widthField ), new GridBagConstraints(
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( this.heightLabel, new GridBagConstraints(
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( new SwingAdapter( this.heightField ), new GridBagConstraints(
				1, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( this.depthLabel, new GridBagConstraints(
				0, //gridX
				2, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( new SwingAdapter( this.depthField ), new GridBagConstraints(
				1, //gridX
				2, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( BoxUtilities.createHorizontalGlue(), new GridBagConstraints(
				GLUE_X_POS, //gridX
				0, //gridY
				1, //gridWidth
				3, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor
				GridBagConstraints.BOTH, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
		);

	}

	@Override
	protected void updateUIFromNewAdapter() {
		super.updateUIFromNewAdapter();

		this.removeComponent( this.linkAllButton );
		this.removeComponent( this.linkXYButton );
		this.removeComponent( this.linkXZButton );
		this.removeComponent( this.linkYZButton );

		boolean hasLinkAll = false;
		boolean hasLinkXY = false;
		boolean hasLinkXZ = false;
		boolean hasLinkYZ = false;
		boolean hasX = false;
		boolean hasY = false;
		boolean hasZ = false;
		boolean hasIndependentX = false;
		boolean hasIndependentY = false;
		boolean hasIndependentZ = false;

		if( ( this.propertyAdapter != null ) && ( this.propertyAdapter.getInstance() != null ) ) {
			ModelImp baseModel = (ModelImp)this.propertyAdapter.getInstance();
			for( edu.cmu.cs.dennisc.scenegraph.scale.Resizer r : baseModel.getResizers() ) {
				if( r == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.UNIFORM ) {
					hasLinkAll = true;
					hasX = true;
					hasY = true;
					hasZ = true;
				} else if( r == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.XY_PLANE ) {
					hasLinkXY = true;
					hasX = true;
					hasY = true;
				} else if( r == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.XZ_PLANE ) {
					hasLinkXZ = true;
					hasX = true;
					hasZ = true;
				} else if( r == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.YZ_PLANE ) {
					hasLinkYZ = true;
					hasY = true;
					hasZ = true;
				} else if( r == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.X_AXIS ) {
					hasX = true;
					hasIndependentX = true;
				} else if( r == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Y_AXIS ) {
					hasY = true;
					hasIndependentY = true;
				} else if( r == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Z_AXIS ) {
					hasZ = true;
					hasIndependentZ = true;
				}
			}
		}

		if( hasLinkAll ) {
			this.addComponent( this.linkAllButton, new GridBagConstraints(
					SCALE_ALL_X_POS, //gridX
					0, //gridY
					1, //gridWidth
					3, //gridHeight
					0.0, //weightX
					0.0, //weightY
					GridBagConstraints.WEST, //anchor
					GridBagConstraints.NONE, //fill
					new Insets( 2, 2, 2, 2 ), //insets
					0, //ipadX
					0 ) //ipadY
			);
		}
		if( hasLinkXY ) {
			this.addComponent( this.linkXYButton, new GridBagConstraints(
					SCALE_XY_X_POS, //gridX
					0, //gridY
					1, //gridWidth
					3, //gridHeight
					0.0, //weightX
					0.0, //weightY
					GridBagConstraints.NORTHWEST, //anchor
					GridBagConstraints.NONE, //fill
					new Insets( 16, 2, 2, 2 ), //insets //16
					0, //ipadX
					0 ) //ipadY
			);
		}
		if( hasLinkXZ ) {
			this.addComponent( this.linkXZButton, new GridBagConstraints(
					SCALE_XZ_X_POS, //gridX
					0, //gridY
					1, //gridWidth
					3, //gridHeight
					0.0, //weightX
					0.0, //weightY
					GridBagConstraints.WEST, //anchor
					GridBagConstraints.NONE, //fill
					new Insets( 2, 2, 2, 2 ), //insets
					0, //ipadX
					0 ) //ipadY
			);
		}
		if( hasLinkYZ ) {
			this.addComponent( this.linkYZButton, new GridBagConstraints(
					SCALE_YZ_X_POS, //gridX
					0, //gridY
					1, //gridWidth
					3, //gridHeight
					0.0, //weightX
					0.0, //weightY
					GridBagConstraints.NORTHWEST, //anchor
					GridBagConstraints.NONE, //fill
					new Insets( 48, 2, 2, 2 ), //insets
					0, //ipadX
					0 ) //ipadY
			);
		}
		isUpdatingState = true;

		widthField.setEnabled( hasX );
		widthField.setEditable( hasX );
		heightField.setEnabled( hasY );
		heightField.setEditable( hasY );
		depthField.setEnabled( hasZ );
		depthField.setEditable( hasZ );

		IsAllScaleLinkedState.getInstance().setValueTransactionlessly( hasLinkAll );
		boolean enableLinkAll = ( hasIndependentX && hasLinkYZ ) ||
				( hasIndependentY && hasLinkXZ ) ||
				( hasIndependentZ && hasLinkXY ) ||
				( hasIndependentX && hasIndependentY && hasIndependentZ );
		IsAllScaleLinkedState.getInstance().setEnabled( enableLinkAll );

		boolean enableXYLink = hasIndependentX && hasIndependentY;
		IsXYScaleLinkedState.getInstance().setEnabled( enableXYLink );
		IsXYScaleLinkedState.getInstance().setValueTransactionlessly( hasLinkXY && ( !hasLinkAll || !enableXYLink ) );

		boolean enableXZLink = hasIndependentX && hasIndependentZ;
		IsXZScaleLinkedState.getInstance().setEnabled( enableXZLink );
		IsXZScaleLinkedState.getInstance().setValueTransactionlessly( hasLinkXZ && ( !hasLinkAll || !enableXZLink ) );

		boolean enableYZLink = hasIndependentY && hasIndependentZ;
		IsYZScaleLinkedState.getInstance().setEnabled( enableYZLink );
		IsYZScaleLinkedState.getInstance().setValueTransactionlessly( hasLinkYZ && ( !hasLinkAll || !enableYZLink ) );

		isUpdatingState = false;
		setResetButton();
	}

	private void setResetButton() {
		if( ( this.resetButton != null ) && ( this.resetButton.getAwtComponent().getParent() != null ) ) {
			this.removeComponent( this.resetButton );
		}
		if( this.propertyAdapter != null ) {

			org.lgna.croquet.Operation operation = new org.alice.ide.properties.adapter.croquet.ModelSizePropertyValueOperation( this.propertyAdapter, getOriginalSize() );
			operation.setName( "Reset" );
			this.resetButton = operation.createButton();

		}
		boolean usesReset = false;
		if( ( this.propertyAdapter != null ) && ( this.propertyAdapter.getInstance() != null ) ) {
			ModelImp baseModel = (ModelImp)this.propertyAdapter.getInstance();
			if( ( baseModel instanceof org.lgna.story.implementation.JointedModelImp ) || ( baseModel instanceof org.lgna.story.implementation.BillboardImp ) ) {
				usesReset = true;
			}
		}
		if( ( this.resetButton != null ) && usesReset ) {
			this.addComponent( this.resetButton, new GridBagConstraints(
					RESET_X_POS, //gridX
					0, //gridY
					1, //gridWidth
					3, //gridHeight
					0.0, //weightX
					0.0, //weightY
					GridBagConstraints.WEST, //anchor
					GridBagConstraints.NONE, //fill
					new Insets( 2, 2, 2, 2 ), //insets
					0, //ipadX
					0 ) //ipadY
			);
		}
	}

	private Dimension3 getOriginalSize() {
		ModelImp baseModel = (ModelImp)this.propertyAdapter.getInstance();
		Dimension3 scale = baseModel.getScale();
		Dimension3 size = baseModel.getSize();
		return new Dimension3( size.x / scale.x, size.y / scale.y, size.z / scale.z );
	}

	private Dimension3 getModelSize() {
		ModelImp baseModel = (ModelImp)this.propertyAdapter.getInstance();
		return baseModel.getSize();
	}

	@Override
	protected void setValueOnUI( Dimension3 value ) {
		if( value != null ) {
			this.doUpdateOnAdapter = false;
			this.widthField.setValue( value.x );
			this.heightField.setValue( value.y );
			this.depthField.setValue( value.z );
			this.doUpdateOnAdapter = true;
			return;
		}
		//If we haven't set the scale value, set it to null
		this.widthField.setValue( null );
		this.heightField.setValue( null );
		this.depthField.setValue( null );
	}

	private Dimension3 getSizeFromUI( Object source ) {
		double desiredWidth = widthField.getValue();
		double desiredHeight = heightField.getValue();
		double desiredDepth = depthField.getValue();
		if( Double.isNaN( desiredWidth ) || Double.isNaN( desiredHeight ) || Double.isNaN( desiredDepth ) ) {
			return null;
		}
		double width = desiredWidth;
		double height = desiredHeight;
		double depth = desiredDepth;
		if( source != null ) {
			Dimension3 size = this.getModelSize();
			if( source == widthField ) {
				double relativeXScale = width / size.x;
				if( IsAllScaleLinkedState.getInstance().getValue() ) {
					height = relativeXScale * size.y;
					depth = relativeXScale * size.z;
				} else if( IsXYScaleLinkedState.getInstance().getValue() ) {
					height = relativeXScale * size.y;
				} else if( IsXZScaleLinkedState.getInstance().getValue() ) {
					depth = relativeXScale * size.z;
				}
			} else if( source == heightField ) {
				double relativeYScale = height / size.y;
				if( IsAllScaleLinkedState.getInstance().getValue() ) {
					width = relativeYScale * size.x;
					depth = relativeYScale * size.z;
				} else if( IsXYScaleLinkedState.getInstance().getValue() ) {
					width = relativeYScale * size.x;
				} else if( IsYZScaleLinkedState.getInstance().getValue() ) {
					depth = relativeYScale * size.z;
				}
			} else if( source == depthField ) {
				double relativeZScale = depth / size.z;
				if( IsAllScaleLinkedState.getInstance().getValue() ) {
					width = relativeZScale * size.x;
					height = relativeZScale * size.y;
				} else if( IsXZScaleLinkedState.getInstance().getValue() ) {
					width = relativeZScale * size.x;
				} else if( IsYZScaleLinkedState.getInstance().getValue() ) {
					height = relativeZScale * size.y;
				}
			}
		}
		Dimension3 newSize = new Dimension3( width, height, depth );
		return newSize;
	}

	private void updateUIFromLinkState( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue ) {
		if( !isUpdatingState && ( nextValue != prevValue ) ) {
			isUpdatingState = true;
			if( nextValue ) {
				if( state == IsAllScaleLinkedState.getInstance() ) {
					IsXYScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsXYScaleLinkedState.getInstance().isEnabled() );
					IsXZScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsXZScaleLinkedState.getInstance().isEnabled() );
					IsYZScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsYZScaleLinkedState.getInstance().isEnabled() );
				} else if( state == IsXYScaleLinkedState.getInstance() ) {
					IsAllScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsAllScaleLinkedState.getInstance().isEnabled() );
					IsXZScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsXZScaleLinkedState.getInstance().isEnabled() );
					IsYZScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsYZScaleLinkedState.getInstance().isEnabled() );
				} else if( state == IsXZScaleLinkedState.getInstance() ) {
					IsAllScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsAllScaleLinkedState.getInstance().isEnabled() );
					IsXYScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsXYScaleLinkedState.getInstance().isEnabled() );
					IsYZScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsYZScaleLinkedState.getInstance().isEnabled() );
				} else if( state == IsYZScaleLinkedState.getInstance() ) {
					IsAllScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsAllScaleLinkedState.getInstance().isEnabled() );
					IsXZScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsXZScaleLinkedState.getInstance().isEnabled() );
					IsXYScaleLinkedState.getInstance().setValueTransactionlessly( false || !IsXYScaleLinkedState.getInstance().isEnabled() );
				}
			}
			isUpdatingState = false;
		}
	}

	protected void updateAdapterFromUI( ActionEvent e ) {
		if( this.doUpdateOnAdapter ) {
			Dimension3 newScale = getSizeFromUI( e.getSource() );
			if( newScale != null ) {
				if( !newScale.equals( this.propertyAdapter.getValue() ) ) {
					if( ( this.propertyAdapter.getLastSetValue() == null ) || !this.propertyAdapter.getLastSetValue().equals( newScale ) ) {
						org.lgna.croquet.Operation operation = new org.alice.ide.properties.adapter.croquet.ModelSizePropertyValueOperation( this.propertyAdapter, newScale );
						operation.fire( org.lgna.croquet.triggers.ActionEventTrigger.createUserInstance( e ) );
					}
				}
			}
		}
	}
}
