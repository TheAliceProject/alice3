/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.walkandtouch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.lgna.ik.poser.JointSelectionSphere;
import org.lgna.ik.poser.PoserControllerAdapter;
import org.lgna.ik.poser.PoserDragAdapter;
import org.lgna.ik.poser.PoserEvent;
import org.lgna.ik.poser.PoserSphereManipulatorListener;
import org.lgna.ik.walkandtouch.IKMagicWand.Limb;
import org.lgna.story.AddMouseClickOnObjectListener;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SGround;
import org.lgna.story.SJoint;
import org.lgna.story.SScene;
import org.lgna.story.SSun;
import org.lgna.story.SpatialRelation;
import org.lgna.story.event.MouseClickEvent;
import org.lgna.story.event.MouseClickOnObjectEvent;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author Matt May
 */
public class PoserScene extends SScene {
	private final SSun sun = new SSun();
	private final SGround snow = new SGround();
	private final SCamera camera;
	public final SBiped ogre;
	private ArrayList<JointSelectionSphere> jssArr;
	private ArrayList<JointId> anchorPoints = Collections.newArrayList();
	private PoserControllerAdapter adapter;
	private PoserDragAdapter dragAdapter = new PoserDragAdapter();
	private Map<IKMagicWand.Limb, List<JointSelectionSphere>> limbToJointMap = Collections.newHashMap();
	private Map<JointImp, IKMagicWand.Limb> jointToLimbMap = Collections.newHashMap();
	private JointImp currentlyShowingRotationHandles = null;

	public PoserScene( SCamera camera, SBiped ogre ) {
		this.camera = camera;
		this.ogre = ogre;
		JointSelectionSphere pelvis = createJSS( ogre.getPelvis(), null );
		JointSelectionSphere a = createJSS( ogre.getRightHip(), pelvis );
		JointSelectionSphere b = createJSS( ogre.getRightKnee(), a );
		JointSelectionSphere c = createJSS( ogre.getRightAnkle(), b );
		limbToJointMap.put( Limb.RIGHT_LEG, Collections.newArrayList( pelvis, a, b, c ) );
		JointSelectionSphere d = createJSS( ogre.getRightClavicle(), null );
		JointSelectionSphere e = createJSS( ogre.getRightShoulder(), d );
		JointSelectionSphere f = createJSS( ogre.getRightElbow(), e );
		JointSelectionSphere g = createJSS( ogre.getRightWrist(), f );
		limbToJointMap.put( Limb.RIGHT_ARM, Collections.newArrayList( d, e, f, g ) );
		JointSelectionSphere h = createJSS( ogre.getLeftHip(), pelvis );
		JointSelectionSphere i = createJSS( ogre.getLeftKnee(), h );
		JointSelectionSphere j = createJSS( ogre.getLeftAnkle(), i );
		limbToJointMap.put( Limb.LEFT_LEG, Collections.newArrayList( pelvis, h, i, j ) );
		JointSelectionSphere k = createJSS( ogre.getLeftClavicle(), null );
		JointSelectionSphere l = createJSS( ogre.getLeftShoulder(), k );
		JointSelectionSphere m = createJSS( ogre.getLeftElbow(), l );
		JointSelectionSphere n = createJSS( ogre.getLeftWrist(), m );
		limbToJointMap.put( Limb.LEFT_ARM, Collections.newArrayList( k, l, m, n ) );

		for( IKMagicWand.Limb limb : limbToJointMap.keySet() ) {
			for( JointSelectionSphere sphere : limbToJointMap.get( limb ) ) {
				jointToLimbMap.put( sphere.getJoint(), limb );
			}
		}

		this.jssArr = Collections.newArrayList( a, b, c, d, e, f, g, h, i, j, k, l, m, n );
		ArrayList<SJoint> sJointList = Collections.newArrayList( ogre.getRightClavicle(), ogre.getLeftClavicle(), ogre.getRightHip(), ogre.getLeftHip() );
		for( SJoint joint : sJointList ) {
			anchorPoints.add( ( (JointImp)ImplementationAccessor.getImplementation( joint ) ).getJointId() );
		}
	}

	private JointSelectionSphere createJSS( SJoint joint, JointSelectionSphere child ) {
		return new JointSelectionSphere( (JointImp)ImplementationAccessor.getImplementation( joint ), child );
	}

	private void performGeneratedSetup() {
		this.snow.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		this.ogre.setVehicle( this );

		this.ogre.place( SpatialRelation.ABOVE, this.snow );
		this.snow.setPaint( SGround.SurfaceAppearance.SNOW );

		//		target.setPositionRelativeToVehicle(new Position(1, 0, 0));

		//camera vantage point taken care of by camera navigator
		//this.camera.moveAndOrientToAGoodVantagePointOf( this.ogre );
		performInitializeEvents();
	}

	private void performCustomSetup() {
		//if you want the skeleton visualization to be co-located
		//		this.ogre.setOpacity( 0.25 );

		//		org.lgna.story.implementation.JointedModelImp impl = ImplementationAccessor.getImplementation( this.ogre );
		//		impl.showVisualization();
	}

	private void performInitializeEvents() {
		addCustomDragAdapter();
		addMouseClickOnObjectListener( new MouseClickOnObjectListener() {

			public void mouseClicked( MouseClickOnObjectEvent e ) {

				if( MouseClickEvent.isThisRightClick( e ) ) {
					if( e.getModelAtMouseLocation() instanceof JointSelectionSphere ) {
						JointSelectionSphere sphere = (JointSelectionSphere)e.getModelAtMouseLocation();
						adapter.updateSphere( jointToLimbMap.get( sphere.getJoint() ), sphere );
						toggleRotationHandles( sphere.getJoint() );
					}
				}
			}

		}, AddMouseClickOnObjectListener.setOfVisuals( ArrayUtilities.createArray( jssArr, JointSelectionSphere.class ) ) );
	}

	public void addCustomDragAdapter() {
		SceneImp scene = (SceneImp)ImplementationAccessor.getImplementation( this );
		OnscreenLookingGlass lookingGlass = scene.getProgram().getOnscreenLookingGlass();
		SymmetricPerspectiveCamera camera = (SymmetricPerspectiveCamera)scene.findFirstCamera().getSgCamera();
		this.dragAdapter.setOnscreenLookingGlass( lookingGlass );
		this.dragAdapter.addCameraView( CameraView.MAIN, camera, null );
		this.dragAdapter.makeCameraActive( camera );
		dragAdapter.addListener( new PoserSphereManipulatorListener() {

			public void fireStart( PoserEvent poserEvent ) {
				JointSelectionSphere jss = poserEvent.getJSS();
				jss.setVehicle( PoserScene.this );
				JointImp end = jss.getJoint();
				JointId anchor = getAnchorForEndJoint( end );
				if( anchor != null ) {
					JointImp anchor2 = (JointImp)ImplementationAccessor.getImplementation( ogre.getJoint( anchor ) );
					IKMagicWand.moveChainToPointInSceneSpace( anchor2, end, ImplementationAccessor.getImplementation( jss ).getAbsoluteTransformation().translation );
				}
				jss.setVehicle( end.getAbstraction() );

			}

			public void fireFinish( PoserEvent poserEvent ) {
				JointSelectionSphere jss = poserEvent.getJSS();
				jss.moveAndOrientTo( jss.getJoint().getAbstraction() );
			}
		} );
	}

	private JointId getAnchorForEndJoint( JointImp joint ) {
		Limb limb = jointToLimbMap.get( joint );
		JointId anchorJointID = adapter.getAnchorJointID( limb, joint );
		if( isAParentOfB( anchorJointID, joint.getJointId() ) ) {
			return anchorJointID;
		}
		return null;//joint.getJointedModelImplementation().getJointImplementation( joint.getJointId().getParent() ).getJointId();
	}

	private boolean isAParentOfB( JointId parent, JointId joint ) {
		if( parent.equals( joint ) ) {
			return true;
		}
		JointId rv = joint.getParent();
		if( rv != null ) {
			return isAParentOfB( parent, rv );
		}
		return false;
	}

	private void toggleRotationHandles( JointImp joint ) {
		if( this.currentlyShowingRotationHandles == joint ) {
			hideRotationHandles();
		} else {
			showRotationHandles( joint );
		}
	}

	private void hideRotationHandles() {
	}

	private void showRotationHandles( JointImp joint ) {
	}

	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
		if( isActive ) {
			if( activeCount == 1 ) {
				this.performGeneratedSetup();
				this.performCustomSetup();
			} else {
				this.restoreStateAndEventListeners();
			}
		} else {
			this.restoreStateAndEventListeners();
		}
	}

	public ArrayList<JointSelectionSphere> getJointSelectionSheres() {
		return this.jssArr;
	}

	public void setAdapter( PoserControllerAdapter adapter ) {
		this.adapter = adapter;
	}

	public List<JointSelectionSphere> getJointsForLimb( Limb key ) {
		return limbToJointMap.get( key );
	}

	public JointSelectionSphere getDefaultAnchorJoint( Limb key ) {
		return limbToJointMap.get( key ).get( 1 );
	}

	public PoserDragAdapter getDragAdapter() {
		return this.dragAdapter;
	}
}
