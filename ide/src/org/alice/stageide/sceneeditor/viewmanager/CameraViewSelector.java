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
package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.Composite;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.OrthographicCameraMarker;
import org.alice.apis.moveandturn.PerspectiveCameraMarker;
import org.alice.interact.AffineMatrix4x4TargetBasedAnimation;
import org.alice.interact.OrthographicCameraController;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

/**
 * @author David Culyba
 */
public class CameraViewSelector extends JPanel implements ItemListener, AbsoluteTransformationListener, PropertyListener{
	
	private MoveAndTurnSceneEditor sceneEditor;
	private JComboBox cameraViewComboBox;
	private List<edu.cmu.cs.dennisc.alice.ast.AbstractField> cameraFields = new LinkedList<edu.cmu.cs.dennisc.alice.ast.AbstractField>();
	private org.alice.apis.moveandturn.CameraMarker activeMarker;
	private SymmetricPerspectiveCamera perspectiveCamera;
	private OrthographicCamera orthographicCamera;
	private Animator animator;
	private int selectedIndex = -1;
	private AffineMatrix4x4TargetBasedAnimation cameraAnimation = null;
	
	private List<OrthographicCameraMarker> orthographicMarkers = new LinkedList<OrthographicCameraMarker>();
	
	private JPanel orthographicControlPanel;
	
	
	public CameraViewSelector(MoveAndTurnSceneEditor sceneEditor, Animator animator)
	{
		super();
		this.animator = animator;
		this.sceneEditor = sceneEditor;
		this.cameraViewComboBox = new JComboBox();
		this.add(this.cameraViewComboBox);
		this.cameraViewComboBox.addItemListener( this );
		initializeOrthographicControls();
		createInitialOrthographicCameraMarkers();
		refreshFields();
	}
	
	public void addOrthographicMarkersToScene(org.alice.apis.moveandturn.Scene scene )
	{
		for (OrthographicCameraMarker marker : this.orthographicMarkers)
		{
			if (marker.getVehicle() != null)
			{
				marker.setVehicle(null);
			}
			scene.addComponent(marker);
		}
	}
	
	private void createInitialOrthographicCameraMarkers()
	{
		orthographicMarkers.clear();
		org.alice.apis.moveandturn.OrthographicCameraMarker topMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		topMarker.setName( "TOP" );
		AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
		topTransform.translation.y = 10;
		topTransform.orientation.up.set( 0, 0, 1 );
		topTransform.orientation.right.set( 1, 0, 0 );
		topTransform.orientation.backward.set( 0, 1, 0 );
		assert topTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		topMarker.setLocalTransformation( topTransform );
		ClippedZPlane picturePlane = new ClippedZPlane();
		picturePlane.setCenter(0, 0);
		picturePlane.setHeight(1);
		topMarker.setPicturePlane(picturePlane);
		orthographicMarkers.add(topMarker);

		org.alice.apis.moveandturn.OrthographicCameraMarker sideMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		sideMarker.setName( "SIDE" );
		AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
		sideTransform.translation.x = 10;
		sideTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis()) );
		assert sideTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		sideMarker.setLocalTransformation( sideTransform );
		sideMarker.setPicturePlane(picturePlane);
		orthographicMarkers.add(sideMarker);

		org.alice.apis.moveandturn.OrthographicCameraMarker frontMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		frontMarker.setName( "FRONT" );
		AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
		frontTransform.translation.z = 10;
		frontTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessNegativeZAxis(), Vector3.accessPositiveYAxis()) );
		assert frontTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		frontMarker.setLocalTransformation( frontTransform );
		frontMarker.setPicturePlane(picturePlane);
		orthographicMarkers.add(frontMarker);
	}
	
	private void initializeOrthographicControls()
	{
		final double MOVEMENT_AMOUNT = .1d;
		this.orthographicControlPanel = new JPanel();
		this.orthographicControlPanel.setLayout( new GridBagLayout() );
		
		JButton leftButton = new JButton("<-");
		leftButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ) {
				moveOrthographicCamera( new Point3(-MOVEMENT_AMOUNT, 0, 0) );
			}
		});
		JButton rightButton = new JButton("->");
		rightButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ) {
				moveOrthographicCamera( new Point3(MOVEMENT_AMOUNT, 0, 0) );
			}
		});
		JButton upButton = new JButton("^");
		upButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ) {
				moveOrthographicCamera( new Point3(0, MOVEMENT_AMOUNT, 0) );
			}
		});
		JButton downButton = new JButton("v");
		downButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ) {
				moveOrthographicCamera( new Point3(0, -MOVEMENT_AMOUNT, 0) );
			}
		});
		JButton backButton = new JButton("-");
		backButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ) {
				zoomOrthographicCamera( -MOVEMENT_AMOUNT );
			}
		});
		JButton forwardButton = new JButton("+");
		forwardButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ) {
				zoomOrthographicCamera( MOVEMENT_AMOUNT );
			}
		});
		
		this.orthographicControlPanel.add(upButton, new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.orthographicControlPanel.add(leftButton, new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.orthographicControlPanel.add(rightButton, new GridBagConstraints( 
				2, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.orthographicControlPanel.add(downButton, new GridBagConstraints( 
				1, //gridX
				2, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.orthographicControlPanel.add(forwardButton, new GridBagConstraints( 
				3, //gridX
				1, //gridY
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
		this.orthographicControlPanel.add(backButton, new GridBagConstraints( 
				3, //gridX
				2, //gridY
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
		
		
	}
	
	//TODO: remove
	private void zoomOrthographicCamera(double zoom)
	{
		if (this.orthographicCamera != null)
		{
			this.orthographicCamera.nearClippingPlaneDistance.setValue(this.orthographicCamera.nearClippingPlaneDistance.getValue() + zoom);
		}
	}
	
	//TODO: remove
	private void moveOrthographicCamera(Point3 direction)
	{
		if (this.orthographicCamera != null)
		{
			Transformable cameraParent = (Transformable)CameraViewSelector.this.orthographicCamera.getParent();
			AffineMatrix4x4 localTransform = cameraParent.localTransformation.getValue();
			localTransform.applyTranslation( direction );
			cameraParent.localTransformation.setValue( localTransform );
		}
	}
	
	public void setPersespectiveCamera(SymmetricPerspectiveCamera camera)
	{
		if (this.perspectiveCamera != null)
		{
			this.perspectiveCamera.removeAbsoluteTransformationListener( this );
		}
		this.perspectiveCamera = camera;
		if (this.perspectiveCamera != null)
		{
			this.perspectiveCamera.addAbsoluteTransformationListener( this );
		}
	}
	
	public void setOrthographicCamera(OrthographicCamera camera)
	{
		if (this.orthographicCamera != null)
		{
			this.orthographicCamera.removeAbsoluteTransformationListener( this );
			this.orthographicCamera.picturePlane.removePropertyListener(this);
		}
		this.orthographicCamera = camera;
		if (this.orthographicCamera != null)
		{
			this.orthographicCamera.addAbsoluteTransformationListener( this );
			this.orthographicCamera.picturePlane.addPropertyListener(this);
		}
	}
	
	public void refreshFields()
	{
		cameraFields.clear();
		this.cameraViewComboBox.removeAllItems();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : this.sceneEditor.getDeclaredFields()) 
		{
			if (field.getValueType().isAssignableTo( org.alice.apis.moveandturn.CameraMarker.class ))
			{
				cameraFields.add( field );
				this.cameraViewComboBox.addItem( field.getName() );
			}
		}
		for (OrthographicCameraMarker marker : this.orthographicMarkers)
		{
			this.cameraViewComboBox.addItem(marker.getName());
		}
		
		if (selectedIndex == -1 && this.cameraFields.size() > 0)
		{
			this.cameraViewComboBox.setSelectedIndex( 0 );
		}
	}

	public org.alice.apis.moveandturn.CameraMarker getActiveMarker()
	{
		return this.activeMarker;
	}
	
	private org.alice.apis.moveandturn.CameraMarker retrieveActiveMarker()
	{
		if (this.selectedIndex > -1)
		{
			if (this.selectedIndex < this.cameraFields.size())
			{
				AbstractField selectedField = this.cameraFields.get( this.selectedIndex );
				CameraMarker marker = this.sceneEditor.getInstanceInJavaForField(selectedField, org.alice.apis.moveandturn.CameraMarker.class);
				return marker;
			}
			else
			{
				int orthoIndex = this.selectedIndex - this.cameraFields.size();
				OrthographicCameraMarker orthoMarker = this.orthographicMarkers.get(orthoIndex);
				return orthoMarker;
			}
		}
		return null;
	}
	
	
	private synchronized boolean isAnimatingCamera()
	{
		return (this.cameraAnimation != null && !this.cameraAnimation.isDone());
	}
	
	private void animateToTargetView()
	{
		if (this.cameraAnimation == null)
		{
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTransform = CameraViewSelector.this.perspectiveCamera.getAbsoluteTransformation();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransform = CameraViewSelector.this.activeMarker.getTransformation( AsSeenBy.SCENE );
			this.cameraAnimation = new AffineMatrix4x4TargetBasedAnimation(currentTransform, targetTransform)
			{

				@Override
				protected void updateValue( AffineMatrix4x4 value ) {
					Transformable cameraParent = (Transformable)CameraViewSelector.this.perspectiveCamera.getParent();
					cameraParent.setTransformation( value, CameraViewSelector.this.perspectiveCamera.getRoot() );
				}

			};
			this.animator.addFrameObserver( this.cameraAnimation );
		}
		else
		{
			this.cameraAnimation.setCurrentValue( CameraViewSelector.this.perspectiveCamera.getAbsoluteTransformation() );
			this.cameraAnimation.setTarget( CameraViewSelector.this.activeMarker.getTransformation( AsSeenBy.SCENE ) );
		}
	}
	
	private void switchToOrthographicView()
	{
		this.sceneEditor.switchToOthographicCamera();
		if (this.orthographicControlPanel.getParent() != this)
		{
			this.add( this.orthographicControlPanel );
			this.revalidate();
		}
	}
	
	private void switchToPerspectiveView()
	{
		this.sceneEditor.switchToPerspectiveCamera();
		if (this.orthographicControlPanel.getParent() == this)
		{
			this.remove( this.orthographicControlPanel );
			this.revalidate();
		}
	}
	
	private void printOrthoViews()
	{
		for (OrthographicCameraMarker marker : this.orthographicMarkers)
		{
			System.out.println(marker.getName() + ": <("+marker.getPicturePlane().getXMaximum()+", "+marker.getPicturePlane().getXMinimum()+"), ("+marker.getPicturePlane().getYMaximum()+", "+marker.getPicturePlane().getYMinimum()+")>");
		}
	}
	
	private void setSelectedView(int index)
	{
		this.selectedIndex = index;
		this.activeMarker = retrieveActiveMarker();
		if (this.perspectiveCamera != null && this.activeMarker != null)
		{
			if (this.activeMarker instanceof OrthographicCameraMarker)
			{
				OrthographicCameraMarker orthoMarker = (OrthographicCameraMarker)this.activeMarker;
				switchToOrthographicView();
				Transformable cameraParent = (Transformable)CameraViewSelector.this.orthographicCamera.getParent();
				CameraViewSelector.this.orthographicCamera.picturePlane.setValue(new ClippedZPlane(orthoMarker.getPicturePlane()) );
				cameraParent.setTransformation( CameraViewSelector.this.activeMarker.getTransformation( AsSeenBy.SCENE ), CameraViewSelector.this.orthographicCamera.getRoot() );
			}
			else
			{
				switchToPerspectiveView();
				animateToTargetView();
			}
			
		}
	}

	public void itemStateChanged( ItemEvent e ) {
		if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
			this.setSelectedView( this.cameraViewComboBox.getSelectedIndex() );
		}
	}

	
	private String printTransformable( AffineMatrix4x4 t )
	{
		String s = "\n";
		s += t.orientation.right.toString() + "\n";
		s += t.orientation.up.toString() + "\n";
		s += t.orientation.backward.toString() + "\n";
		s += t.translation.toString() + "\n";
		return s;
	}
	
	private boolean doesMarkerMatchCamera(org.alice.apis.moveandturn.CameraMarker marker, AbstractCamera camera)
	{
		if (camera instanceof OrthographicCamera)
		{
			return marker instanceof OrthographicCameraMarker;
		}
		else if (camera instanceof SymmetricPerspectiveCamera)
		{
			return marker instanceof PerspectiveCameraMarker;
		}
		return false;
	}
	
	public void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) 
	{
		AbstractCamera cameraSource = (AbstractCamera)absoluteTransformationEvent.getSource();
		if (doesMarkerMatchCamera( this.activeMarker, cameraSource ))
		{
			if (cameraSource instanceof OrthographicCamera)
			{
				AffineMatrix4x4 cameraTransform = this.orthographicCamera.getAbsoluteTransformation();
				Component root = this.orthographicCamera.getRoot();
				this.activeMarker.getSGTransformable().setTransformation( cameraTransform, root );
			}
			else if (cameraSource instanceof SymmetricPerspectiveCamera)
			{
				if (!isAnimatingCamera())
				{
					this.activeMarker.getSGTransformable().setTransformation( this.perspectiveCamera.getAbsoluteTransformation(), this.perspectiveCamera.getRoot() );
				}
				
			}
		}
	}

	public void propertyChanged(PropertyEvent e) {
		if (e.getOwner() == this.orthographicCamera && e.getTypedSource() == this.orthographicCamera.picturePlane)
		{		
			if (doesMarkerMatchCamera( this.activeMarker, this.orthographicCamera ))
			{
				((OrthographicCameraMarker)this.activeMarker).setPicturePlane(this.orthographicCamera.picturePlane.getValue());
			}
		}		
	}

	public void propertyChanging(PropertyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
