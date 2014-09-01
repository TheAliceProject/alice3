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
package org.lgna.ik.poser.scene;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.alice.interact.PoserAnimatorDragAdapter;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.ik.core.IKCore;
import org.lgna.ik.core.IKCore.Limb;
import org.lgna.ik.poser.PoserSphereManipulatorListener;
import org.lgna.ik.poser.controllers.PoserControllerAdapter;
import org.lgna.ik.poser.controllers.PoserEvent;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
import org.lgna.story.Duration;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SCamera;
import org.lgna.story.SGround;
import org.lgna.story.SJoint;
import org.lgna.story.SJointedModel;
import org.lgna.story.SScene;
import org.lgna.story.SSun;
import org.lgna.story.SpatialRelation;
import org.lgna.story.TurnDirection;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public abstract class AbstractPoserScene<T extends SJointedModel> extends SScene {
	private final SSun sun = new SSun();
	private final SGround snow = new SGround();
	public final SCamera camera;
	public T model;
	protected ArrayList<JointSelectionSphere> jssArr;
	protected final ArrayList<JointId> anchorPoints = Lists.newArrayList();
	private PoserControllerAdapter adapter;
	protected final Map<IKCore.Limb, List<JointSelectionSphere>> limbToJointMap = Maps.newHashMap();
	protected final Map<JointImp, IKCore.Limb> jointToLimbMap = Maps.newHashMap();
	protected final List<PoserSphereManipulatorListener> dragListeners = Lists.newCopyOnWriteArrayList();
	private PoserAnimatorDragAdapter poserAnimatorDragAdapter;

	public AbstractPoserScene( SCamera camera, T ogre ) {
		System.out.println( "ogre: " + ogre );
		this.camera = camera;
		this.camera.setVehicle( this );
		this.model = ogre;

		addDragListener( sphereDragListener );
		initializeJointSelectionSpheresAndLimbs();
		initializeLimbAnchors();
	}

	protected abstract void initializeJointSelectionSpheresAndLimbs();

	protected abstract void initializeLimbAnchors();

	private final ValueListener<Boolean> jointHandleVisibilityListener = new ValueListener<Boolean>() {

		@Override
		public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			poserAnimatorDragAdapter.setHandleVisibility( nextValue );
		}
	};
	private final PoserSphereManipulatorListener sphereDragListener = new PoserSphereManipulatorListener() {

		@Override
		public void fireStart( PoserEvent poserEvent ) {
			JointSelectionSphere jss = poserEvent.getJSS();
			jss.setVehicle( AbstractPoserScene.this );
			JointImp end = jss.getJoint();
			JointId anchor = getAnchorForEndJoint( end );
			if( anchor != null ) {
				JointImp anchor2 = (JointImp)EmployeesOnly.getImplementation( model.getJoint( anchor ) );
				IKCore.moveChainToPointInSceneSpace( anchor2, end, EmployeesOnly.getImplementation( jss ).getAbsoluteTransformation().translation );
			}
			jss.setVehicle( end.getAbstraction() );
		}

		@Override
		public void fireFinish( PoserEvent poserEvent ) {
			JointSelectionSphere jss = poserEvent.getJSS();
			jss.moveAndOrientTo( jss.getJoint().getAbstraction() );
			poserAnimatorDragAdapter.setSelectedImplementation( jss.getJoint() );
			poserAnimatorDragAdapter.setHandleVisibility( adapter.getJointRotationHandleVisibilityState().getValue() );

		}

		@Override
		public void fireAnchorUpdate( PoserEvent poserEvent ) {
			JointSelectionSphere jss = poserEvent.getJSS();
			Limb limb = AbstractPoserScene.this.jointToLimbMap.get( jss.getJoint() );
			assert limb != null;
			AbstractPoserScene.this.adapter.updateSphere( limb, jss );
		}
	};

	protected JointSelectionSphere createJSS( SJoint joint, JointSelectionSphere child ) {
		return new JointSelectionSphere( (JointImp)EmployeesOnly.getImplementation( joint ), child );
	}

	private void performGeneratedSetup() {
		this.snow.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		this.model.setVehicle( this );

		this.model.place( SpatialRelation.ABOVE, this.snow );
		this.snow.setPaint( SGround.SurfaceAppearance.SNOW );
		camera.turnToFace( model );
		performInitializeEvents();
	}

	private void performCustomSetup() {
	}

	private void performInitializeEvents() {
		addCustomDragAdapter();
	}

	private edu.cmu.cs.dennisc.renderer.OnscreenRenderTarget<?> getOnscreenRenderTarget() {
		return ( (SceneImp)EmployeesOnly.getImplementation( this ) ).getProgram().getOnscreenRenderTarget();
	}

	public void jointSelected( JointSelectionSphere sphere, MouseEvent e ) {

		if( e.getButton() == 3 ) {//MouseClickEvent.isThisRightClick( e ) ) {
			adapter.updateSphere( jointToLimbMap.get( sphere.getJoint() ), sphere );
		} else {
			//			dragAdapter.fireStart( poserEvent );
		}
	}

	public void addCustomDragAdapter() {
		synchronized( dragListeners ) {
			poserAnimatorDragAdapter = new PoserAnimatorDragAdapter( this );
			poserAnimatorDragAdapter.setAnimator( ( (SceneImp)EmployeesOnly.getImplementation( this ) ).getProgram().getAnimator() );
			poserAnimatorDragAdapter.setInteractionState( org.alice.interact.handle.HandleStyle.ROTATION );
			poserAnimatorDragAdapter.setTarget( model );
			poserAnimatorDragAdapter.setOnscreenRenderTarget( getOnscreenRenderTarget() );
			poserAnimatorDragAdapter.setHandleVisibility( adapter.getJointRotationHandleVisibilityState().getValue() );
			for( PoserSphereManipulatorListener sphereListener : dragListeners ) {
				poserAnimatorDragAdapter.addSphereDragListener( sphereListener );
			}
		}
		dragListeners.clear();
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
		adapter.getJointRotationHandleVisibilityState().addValueListener( jointHandleVisibilityListener );
	}

	public List<JointSelectionSphere> getJointsForLimb( Limb key ) {
		return limbToJointMap.get( key );
	}

	public JointSelectionSphere getDefaultAnchorJoint( Limb key ) {
		if( ( key == Limb.LEFT_ARM ) || ( key == Limb.RIGHT_ARM ) ) {
			return limbToJointMap.get( key ).get( 1 );
		}
		return limbToJointMap.get( key ).get( 0 );
	}

	public void addDragListener( PoserSphereManipulatorListener sphereDragListener ) {
		if( poserAnimatorDragAdapter != null ) {
			poserAnimatorDragAdapter.addSphereDragListener( sphereDragListener );
		} else {
			dragListeners.add( sphereDragListener );
		}
	}

	public void setNewModel( T model ) {
		this.model.setVehicle( null );
		this.model = model;
		this.model.turn( TurnDirection.RIGHT, .5, new Duration( 0 ) );
		model.setVehicle( this );
		poserAnimatorDragAdapter.setTarget( model );
		initializeJointSelectionSpheresAndLimbs();
		initializeLimbAnchors();
	}
}
