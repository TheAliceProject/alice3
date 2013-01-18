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

package org.lgna.ik.poser;

import java.util.ArrayList;

import org.lgna.ik.enforcer.TightPositionalIkEnforcer;
import org.lgna.ik.enforcer.TightPositionalIkEnforcer.PositionConstraint;
import org.lgna.ik.walkandtouch.PoserScene;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MoveDirection;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SProgram;
import org.lgna.story.TurnDirection;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.SphereImp;
import org.lgna.story.resources.JointId;

import test.ik.IkTestApplication;
import test.ik.NiceDragAdapter;

/**
 * @author Matt May
 */
class IkPoser extends SProgram {
	private final SCamera camera = new SCamera();
	private final SBiped ogre = new SBiped( org.lgna.story.resources.biped.OgreResource.BROWN );
	private final PoserScene scene = new PoserScene( camera, ogre );
	private PoserControllerAdapter adapter;
	private org.lgna.ik.enforcer.JointedModelIkEnforcer ikEnforcer;
	private TightPositionalIkEnforcer tightIkEnforcer;
	public final JointId DEFAULT_ANCHOR = ( (JointImp)ImplementationAccessor.getImplementation( ogre.getRightClavicle() ) ).getJointId();
	public final JointId DEFAULT_END = ( (JointImp)ImplementationAccessor.getImplementation( ogre.getRightWrist() ) ).getJointId();
	private final NiceDragAdapter modelManipulationDragAdapter = new NiceDragAdapter();

	//	private final org.lgna.croquet.State.ValueListener<Boolean> linearAngularEnabledListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
	//		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
	//		}
	//
	//		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
	//		}
	//	};
	//	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.JointId> jointIdListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.JointId>() {
	//		public void changing( org.lgna.croquet.State<org.lgna.story.resources.JointId> state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
	//			IkPoser.this.handleChainChanging();
	//		}
	//
	//		public void changed( org.lgna.croquet.State<org.lgna.story.resources.JointId> state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
	//			IkPoser.this.handleChainChanged();
	//		}
	//	};
	//SOLVER this is for printing out the chain
	//	private final org.lgna.croquet.State.ValueListener<org.lgna.ik.solver.Bone> boneListener = new org.lgna.croquet.State.ValueListener<org.lgna.ik.solver.Bone>() {
	//		public void changing( org.lgna.croquet.State<org.lgna.ik.solver.Bone> state, org.lgna.ik.solver.Bone prevValue, org.lgna.ik.solver.Bone nextValue, boolean isAdjusting ) {
	//		}
	//
	//		public void changed( org.lgna.croquet.State<org.lgna.ik.solver.Bone> state, org.lgna.ik.solver.Bone prevValue, org.lgna.ik.solver.Bone nextValue, boolean isAdjusting ) {
	//			IkPoser.this.handleBoneChanged();
	//		}
	//	};
	//	private final edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener targetTransformListener = new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener() {
	//		public void absoluteTransformationChanged( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent ) {
	//			IkPoser.this.handleTargetTransformChanged();
	//		}
	//	};
	//	private org.lgna.ik.solver.Chain chain;
	//	private org.lgna.ik.solver.Solver solver;

	//	class Constraints {
	////		List<Constraint> allActiveConstraints = new ArrayList<Constraint>();
	//		List<PositionConstraint> activePositionConstraints = new ArrayList<PositionConstraint>();
	//		List<OrientationConstraint> activeOrientationConstraints = new ArrayList<OrientationConstraint>();
	//	}
	////	private List<Constraint> activeConstraints = new ArrayList<Constraint>();
	//	Constraints constraints = new Constraints();

	private boolean useTightIkEnforcer = false;
	private PositionConstraint myPositionConstraint;
	private PoserSplitComposite composite = new PoserSplitComposite( this );

	//	protected java.util.Map<org.lgna.ik.solver.Bone.Axis, Double> currentSpeeds;

	//	private org.lgna.story.implementation.SphereImp getTargetImp() {
	//		return ImplementationAccessor.getImplementation( this.target );
	//	}

	//	private SModel createDragProp() {
	//		SSphere mainSphere = new SSphere();
	//
	//		mainSphere.setRadius( 0.13 );
	//		mainSphere.setPaint( Color.RED );
	//		mainSphere.setOpacity( 0.5 );
	//
	//		mainSphere.resizeHeight( .7 );
	//		mainSphere.resizeWidth( .5 );
	//
	//		SCone cone = new SCone();
	//		cone.setBaseRadius( .07 );
	//		cone.setLength( .2 );
	//		cone.setPaint( Color.ORANGE );
	//		//		cone.setOpacity(.5);
	//		cone.resizeWidth( .5 );
	//		cone.resize( .8 );
	//		cone.setVehicle( mainSphere );
	//		cone.move( MoveDirection.DOWN, .1 );
	//		cone.move( MoveDirection.RIGHT, .05 );
	//		cone.turn( TurnDirection.FORWARD, .25, Turn.asSeenBy( cone.getVehicle() ) );
	//
	//		return mainSphere;
	//	}

	private org.lgna.story.implementation.JointedModelImp<?, ?> getSubjectImp() {
		return ImplementationAccessor.getImplementation( this.ogre );
	}

	//	private org.lgna.story.implementation.JointImp getAnchorImp() {
	//		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
	//		return this.getSubjectImp().getJointImplementation( anchorId );
	//	}

	private org.lgna.story.implementation.JointImp getEndImp() {
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		return this.getSubjectImp().getJointImplementation( endId );
	}

	private void handleTargetTransformChanged() {
		//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getTargetImp().getTransformation( this.getAnchorImp() );
		//		edu.cmu.cs.dennisc.print.PrintUtilities.printlns( m );
		this.updateInfo();
	}

	//SOLVER this prints to the yellow area right under the chain display
	private void updateInfo() {
		org.lgna.ik.solver.Bone bone = test.ik.croquet.BonesState.getInstance().getValue();

		StringBuilder sb = new StringBuilder();
		if( bone != null ) {
			org.lgna.story.implementation.JointImp a = bone.getA();
			//			org.lgna.story.implementation.JointImp b = bone.getB();
			sb.append( a.getJointId() );
			sb.append( ":\n" );
			edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, a.getLocalTransformation() );
			//			sb.append( "\n" );
			//			sb.append( b.getJointId() );
			//			sb.append( ":\n" );
			//			edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, b.getLocalTransformation() );
		}
		sb.append( "\n" );
		sb.append( "target:\n" );
		edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, this.getTargetForJoint( adapter.getEndJointID() ).getLocalTransformation() );
		sb.append( "\n" );

		test.ik.croquet.InfoState.getInstance().setValueTransactionlessly( sb.toString() );
	}

	//	private org.lgna.ik.solver.Chain createChain() {
	//		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
	//		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
	//		return org.lgna.ik.solver.Chain.createInstance( this.getSubjectImp(), anchorId, endId );
	//	}

	protected void handleChainChanging() {
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();

		if( ( endId != null ) && ( anchorId != null ) ) {
			if( useTightIkEnforcer ) {
				//TODO 
				//should I remove? depends. when is this called?
			} else {
				ikEnforcer.clearChainBetween( anchorId, endId );
			}
		}
	}

	//	private void handleChainChanged() {
	//		org.lgna.story.resources.JointId endId = adapter.getEndJointID();
	//		org.lgna.story.resources.JointId anchorId = adapter.getAnchorJointID();
	//
	//		if( ( endId != null ) && ( anchorId != null ) ) {
	//			if( useTightIkEnforcer ) {
	//				//TODO 
	//			} else {
	//				ikEnforcer.setChainBetween( anchorId, endId );
	//			}
	//			setDragAdornmentsVisible( true );
	//			Point3 ap = getSubjectImp().getJointImplementation( anchorId ).getAbsoluteTransformation().translation;
	//			scene.anchor.setPositionRelativeToVehicle( new Position( ap.x, ap.y, ap.z ) );
	//		} else {
	//			setDragAdornmentsVisible( false );
	//		}
	//
	//		if( useTightIkEnforcer ) {
	//			//TODO
	//		} else {
	//			test.ik.croquet.BonesState.getInstance().setChain( ikEnforcer.getChainForPrinting( anchorId, endId ) );
	//		}
	//
	//		//		updateInfo();
	//	}

	//	private void handleChainChanged_old() {
	//		//this does not race with the thread. this creates a new one, it might use the old one one more time, which is fine. 
	//		
	//		if(chain != null) {
	//			ikEnforcer.removeChain(chain);
	//		}
	//		chain = createChain();
	//		
	//		if(chain != null) {
	//			setDragAdornmentsVisible(true);
	//			JointImp lastJointImp = chain.getLastJointImp();
	//			
	//			edu.cmu.cs.dennisc.math.AffineMatrix4x4 ltrans = lastJointImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
	//
	//			edu.cmu.cs.dennisc.math.Point3 eePos = new edu.cmu.cs.dennisc.math.Point3(ltrans.translation);
	//			eePos.add(edu.cmu.cs.dennisc.math.Point3.createMultiplication(ltrans.orientation.backward, -.2)); //can do something like this to drag fingertips. right now it results in jumping. 
	//			chain.setEndEffectorPosition(eePos);
	//			
	//			//assuming that all are parented to scene...
	//			this.getTargetImp().setLocalTransformation( new edu.cmu.cs.dennisc.math.AffineMatrix4x4(chain.getEndEffectorOrientation(), chain.getEndEffectorPosition()) );
	//			ikEnforcer.addChain(chain);
	//		} else {
	//			setDragAdornmentsVisible(false);
	//		}
	//		
	//		test.ik.croquet.BonesState.getInstance().setChain( chain );
	//		this.updateInfo();
	//	}

	protected void targetDragStarted() {
	}

	//	private void setDragAdornmentsVisible( boolean visible ) {
	//		if( visible ) {
	//			scene.anchor.setVehicle( scene );
	//			scene.ee.setVehicle( scene );
	//		} else {
	//			scene.anchor.setVehicle( null );
	//			scene.ee.setVehicle( null );
	//		}
	//	}

	private void initializeTest() {
		this.setActiveScene( this.scene );

		//		this.modelManipulationDragAdapter.setOnClickRunnable( new Runnable() {
		//			public void run() {
		//				targetDragStarted();
		//			}
		//		} );
		//
		//		this.modelManipulationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		//		this.cameraNavigationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		//		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		//		this.cameraNavigationDragAdapter.requestDistance( 8.0 );

		this.camera.turn( TurnDirection.RIGHT, .5 );
		this.camera.move( MoveDirection.BACKWARD, 8 );
		this.camera.move( MoveDirection.UP, 1 );
		//		test.ik.croquet.AnchorJointIdState.getInstance().addValueListener( this.jointIdListener );
		//		test.ik.croquet.EndJointIdState.getInstance().addValueListener( this.jointIdListener );
		//		test.ik.croquet.BonesState.getInstance().addValueListener( this.boneListener );
		//		test.ik.croquet.IsLinearEnabledState.getInstance().addValueListener( this.linearAngularEnabledListener );
		//		test.ik.croquet.IsAngularEnabledState.getInstance().addValueListener( this.linearAngularEnabledListener );

		//		this.getTargetImp().setTransformation( this.getEndImp() );
		//		this.getTargetImp().getSgComposite().addAbsoluteTransformationListener( this.targetTransformListener );

		//		calculateThread.start();
	}

	private SphereImp getTargetForJoint( JointId eeId ) {
		return ImplementationAccessor.getImplementation( JointSelectionSphere.findSphereForJoint( eeId, getJointSelectionSheres() ) );
	}

	private void handleBoneChanged() {
		this.updateInfo();
	}

	public static void main( String[] args ) {
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );
		IkPoser program = new IkPoser();
		app.getFrame().setMainComposite( program.getComposite() );

		test.ik.croquet.IsLinearEnabledState.getInstance().setValueTransactionlessly( true );
		test.ik.croquet.IsAngularEnabledState.getInstance().setValueTransactionlessly( false );

		test.ik.croquet.SceneComposite.getInstance().getView().initializeInAwtContainer( program );
		program.initializeTest();

		app.getFrame().setSize( 1200, 800 );
		app.getFrame().setVisible( true );
	}

	private PoserSplitComposite getComposite() {
		return composite;
	}

	public ArrayList<JointSelectionSphere> getJointSelectionSheres() {
		return scene.getJointSelectionSheres();
	}

	public void setAdapter( PoserControllerAdapter adapter ) {
		this.adapter = adapter;
		scene.setAdapter( adapter );
	}

	public JointSelectionSphere getDefaultAnchorJoint() {
		return JointSelectionSphere.findSphereForJoint( DEFAULT_ANCHOR, getJointSelectionSheres() );
	}

	public JointSelectionSphere getDefaultEndJoint() {
		return JointSelectionSphere.findSphereForJoint( DEFAULT_END, getJointSelectionSheres() );
	}

	public Pose getPose() {
		return new Pose( ogre );
	}

}
