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

package test.ik;

import org.lgna.ik.core.IkConstants;
import org.lgna.ik.core.enforcer.TightPositionalIkEnforcer;
import org.lgna.ik.core.enforcer.TightPositionalIkEnforcer.PositionConstraint;
import org.lgna.story.Color;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.MoveDirection;
import org.lgna.story.Position;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SCone;
import org.lgna.story.SModel;
import org.lgna.story.SProgram;
import org.lgna.story.SSphere;
import org.lgna.story.Turn;
import org.lgna.story.TurnDirection;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Dennis Cosgrove
 */
class IkProgram extends SProgram {
	private final SCamera camera = new SCamera();
	private final SBiped ogre = new SBiped( org.lgna.story.resources.biped.OgreResource.BROWN );
	private final SSphere target = new SSphere();
	private final IkScene scene = new IkScene( camera, ogre, target );
	private final edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	private final NiceDragAdapter modelManipulationDragAdapter = new NiceDragAdapter();

	private final org.lgna.croquet.State.ValueListener<Boolean> linearAngularEnabledListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
		@Override
		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.JointId> jointIdListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.JointId>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.JointId> state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
			IkProgram.this.handleChainChanging();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.JointId> state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
			IkProgram.this.handleChainChanged();
		}
	};
	//SOLVER this is for printing out the chain
	private final org.lgna.croquet.State.ValueListener<org.lgna.ik.core.solver.Bone> boneListener = new org.lgna.croquet.State.ValueListener<org.lgna.ik.core.solver.Bone>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.ik.core.solver.Bone> state, org.lgna.ik.core.solver.Bone prevValue, org.lgna.ik.core.solver.Bone nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.ik.core.solver.Bone> state, org.lgna.ik.core.solver.Bone prevValue, org.lgna.ik.core.solver.Bone nextValue, boolean isAdjusting ) {
			IkProgram.this.handleBoneChanged();
		}
	};
	private final edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener targetTransformListener = new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener() {
		@Override
		public void absoluteTransformationChanged( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent ) {
			IkProgram.this.handleTargetTransformChanged();
		}
	};
	//	private org.lgna.ik.solver.Chain chain;
	//	private org.lgna.ik.solver.Solver solver;
	private org.lgna.ik.core.enforcer.JointedModelIkEnforcer ikEnforcer;
	private TightPositionalIkEnforcer tightIkEnforcer;

	//	class Constraints {
	////		List<Constraint> allActiveConstraints = new ArrayList<Constraint>();
	//		List<PositionConstraint> activePositionConstraints = new ArrayList<PositionConstraint>();
	//		List<OrientationConstraint> activeOrientationConstraints = new ArrayList<OrientationConstraint>();
	//	}
	////	private List<Constraint> activeConstraints = new ArrayList<Constraint>();
	//	Constraints constraints = new Constraints();

	private boolean useTightIkEnforcer = false;
	private PositionConstraint myPositionConstraint;

	//	protected java.util.Map<org.lgna.ik.solver.Bone.Axis, Double> currentSpeeds;

	private org.lgna.story.implementation.SphereImp getTargetImp() {
		return EmployeesOnly.getImplementation( this.target );
	}

	private SModel createDragProp() {
		SSphere mainSphere = new SSphere();

		mainSphere.setRadius( 0.13 );
		mainSphere.setPaint( Color.RED );
		mainSphere.setOpacity( 0.5 );

		mainSphere.resizeHeight( .7 );
		mainSphere.resizeWidth( .5 );

		SCone cone = new SCone();
		cone.setBaseRadius( .07 );
		cone.setLength( .2 );
		cone.setPaint( Color.ORANGE );
		//		cone.setOpacity(.5);
		cone.resizeWidth( .5 );
		cone.resize( .8 );
		cone.setVehicle( mainSphere );
		cone.move( MoveDirection.DOWN, .1 );
		cone.move( MoveDirection.RIGHT, .05 );
		cone.turn( TurnDirection.FORWARD, .25, Turn.asSeenBy( cone.getVehicle() ) );

		return mainSphere;
	}

	private org.lgna.story.implementation.JointedModelImp<?, ?> getSubjectImp() {
		return EmployeesOnly.getImplementation( this.ogre );
	}

	private org.lgna.story.implementation.JointImp getAnchorImp() {
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
		return this.getSubjectImp().getJointImplementation( anchorId );
	}

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
		org.lgna.ik.core.solver.Bone bone = test.ik.croquet.BonesState.getInstance().getValue();

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
		edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, this.getTargetImp().getLocalTransformation() );
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

	private void handleChainChanged() {
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();

		if( ( endId != null ) && ( anchorId != null ) ) {
			if( useTightIkEnforcer ) {
				//TODO
			} else {
				ikEnforcer.setChainBetween( anchorId, endId );
			}
			setDragAdornmentsVisible( true );
			Point3 ap = getSubjectImp().getJointImplementation( anchorId ).getAbsoluteTransformation().translation;
			scene.anchor.setPositionRelativeToVehicle( new Position( ap.x, ap.y, ap.z ) );
		} else {
			setDragAdornmentsVisible( false );
		}

		if( useTightIkEnforcer ) {
			//TODO
		} else {
			test.ik.croquet.BonesState.getInstance().setChain( ikEnforcer.getChainForPrinting( anchorId, endId ) );
		}

		//		updateInfo();
	}

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

	private void setDragAdornmentsVisible( boolean visible ) {
		if( visible ) {
			scene.anchor.setVehicle( scene );
			scene.ee.setVehicle( scene );
		} else {
			scene.anchor.setVehicle( null );
			scene.ee.setVehicle( null );
		}
	}

	private void initializeTest() {
		this.setActiveScene( this.scene );
		this.modelManipulationDragAdapter.setOnClickRunnable( new Runnable() {
			@Override
			public void run() {
				targetDragStarted();
			}
		} );

		this.modelManipulationDragAdapter.setOnscreenRenderTarget( EmployeesOnly.getImplementation( this ).getOnscreenRenderTarget() );
		this.cameraNavigationDragAdapter.setOnscreenRenderTarget( EmployeesOnly.getImplementation( this ).getOnscreenRenderTarget() );
		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		this.cameraNavigationDragAdapter.requestDistance( 8.0 );

		test.ik.croquet.AnchorJointIdState.getInstance().addValueListener( this.jointIdListener );
		test.ik.croquet.EndJointIdState.getInstance().addValueListener( this.jointIdListener );
		test.ik.croquet.BonesState.getInstance().addValueListener( this.boneListener );
		test.ik.croquet.IsLinearEnabledState.getInstance().addValueListener( this.linearAngularEnabledListener );
		test.ik.croquet.IsAngularEnabledState.getInstance().addValueListener( this.linearAngularEnabledListener );

		this.getTargetImp().setTransformation( this.getEndImp() );
		this.getTargetImp().getSgComposite().addAbsoluteTransformationListener( this.targetTransformListener );

		Thread calculateThread;

		if( useTightIkEnforcer ) {
			calculateThread = initializeTightIkEnforcer();
		} else {
			calculateThread = initializeOldIkEnforcer();
		}

		this.handleChainChanged();

		calculateThread.start();
	}

	private Thread initializeOldIkEnforcer() {
		//		solver = new org.lgna.ik.solver.Solver();
		ikEnforcer = new org.lgna.ik.core.enforcer.JointedModelIkEnforcer( getSubjectImp() );
		ikEnforcer.addFullBodyDefaultPoseUsingCurrentPose();

		//I'm setting joint weights here
		ikEnforcer.setDefaultJointWeight( 1 );
		ikEnforcer.setJointWeight( org.lgna.story.resources.BipedResource.RIGHT_ELBOW, 2 );

		//using ikEnforcer's methods rather than dealing with chains.

		Thread calculateThread = new Thread() {
			@Override
			public void run() {
				while( !interrupted() ) {
					//solver has the chain. can also have multiple chains.
					//I can tell solver, for this chain this is the linear target, etc.
					//it actually only needs the velocity, etc. then, I should say for this chain this is the desired velocity. ok.

					java.util.Map<org.lgna.ik.core.solver.Bone.Axis, Double> desiredSpeedForAxis = new java.util.HashMap<org.lgna.ik.core.solver.Bone.Axis, Double>();

					//not bad concurrent programming practice
					boolean isLinearEnabled = test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
					boolean isAngularEnabled = test.ik.croquet.IsAngularEnabledState.getInstance().getValue();

					//these could be multiple. in this app it is one pair.
					final JointId eeId = test.ik.croquet.EndJointIdState.getInstance().getValue();
					final JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();

					double maxLinearSpeedForEe = IkConstants.MAX_LINEAR_SPEED_FOR_EE;
					double maxAngularSpeedForEe = IkConstants.MAX_ANGULAR_SPEED_FOR_EE;

					double deltaTime = IkConstants.DESIRED_DELTA_TIME;

					if( ikEnforcer.hasActiveChain() && ( isLinearEnabled || isAngularEnabled ) ) {
						//I could make chain setter not race with this
						//However, racing is fine, as long as the old chain is still valid. It is.

						edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransformation = getTargetImp().getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE );
						if( isLinearEnabled ) {
							ikEnforcer.setEeDesiredPosition( eeId, targetTransformation.translation, maxLinearSpeedForEe );
						}

						if( isAngularEnabled ) {
							ikEnforcer.setEeDesiredOrientation( eeId, targetTransformation.orientation, maxAngularSpeedForEe );
						}

						ikEnforcer.advanceTime( deltaTime );

						Point3 ep = ikEnforcer.getEndEffectorPosition( eeId );
						Point3 ap = ikEnforcer.getAnchorPosition( anchorId );
						scene.anchor.setPositionRelativeToVehicle( new Position( ap.x, ap.y, ap.z ) );
						scene.ee.setPositionRelativeToVehicle( new Position( ep.x, ep.y, ep.z ) );

						//force bone reprint
						//this should be fine even if the chain is not valid anymore.
						//						javax.swing.SwingUtilities.invokeLater(new Runnable() {
						//							public void run() {
						//								//this would prevent me from selecting the list
						////								test.ik.croquet.BonesState.getInstance().setChain( ikEnforcer.getChainForPrinting(anchorId, eeId) );
						//								//this would throw java.lang.IllegalStateException: Attempt to mutate in notification
						////								updateInfo();
						//							}
						//						});
					}

					try {
						sleep( 10 );
					} catch( InterruptedException e ) {
						break;
					}
				}
			}
		};
		return calculateThread;
	}

	//TODO need to populate constraints

	private Thread initializeTightIkEnforcer() {
		tightIkEnforcer = new TightPositionalIkEnforcer( getSubjectImp() );

		Thread calculateThread = new Thread() {
			@Override
			public void run() {
				//				System.out.println("will start");
				//				try {
				//					System.in.read();
				//				} catch (IOException e1) {
				//					// TODO Auto-generated catch block
				//					e1.printStackTrace();
				//				}
				while( !interrupted() ) {

					//not bad concurrent programming practice
					boolean isLinearEnabled = test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
					boolean isAngularEnabled = test.ik.croquet.IsAngularEnabledState.getInstance().getValue();

					//these could be multiple. in this app it is one pair.
					final JointId eeId = test.ik.croquet.EndJointIdState.getInstance().getValue();
					final JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();

					double deltaTime = IkConstants.DESIRED_DELTA_TIME;

					edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransformation = getTargetImp().getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE );

					myPositionConstraint.setEeDesiredPosition( targetTransformation.translation );

					//					//this is a little weird. I'd better let the enforcer create and hold the constraint, and I should hold a pointer to it for myself.
					//					for(PositionConstraint positionConstraint: constraints.activePositionConstraints) {
					//						//should it be like this, or should constraints read them automatically?
					//							//IK system reads joint angles automatically anyway
					//							//but these desired position/orientations are not necessarily tied to scenegraph stuff. I should give them myself like this.
					//						positionConstraint.setEeDesiredPosition(targetTransformation.translation);
					//
					//						//force bone reprint
					//						//this should be fine even if the chain is not valid anymore.
					//						//this would prevent me from selecting the list
					////						javax.swing.SwingUtilities.invokeLater(new Runnable() {
					////							public void run() {
					////								test.ik.croquet.BonesState.getInstance().setChain( ikEnforcer.getChainForPrinting(anchorId, eeId) );
					////							}
					////						});
					//					}
					//
					//					for(OrientationConstraint orientationConstraint: constraints.activeOrientationConstraints) {
					//						System.out.println("orientaiton constraint!");
					//						orientationConstraint.setEeDesiredOrientation(targetTransformation.orientation);
					//					}
					//perhaps better ways of setting constraint values?

					//this enforces the constraints immediately right now. so, there is no talk about deltatime or speed
					//had I had a maximum rotational speed for joints, then having time would make sense
					tightIkEnforcer.enforceConstraints();

					try {
						sleep( 10 );
					} catch( InterruptedException e ) {
						break;
					}
				}
			}
		};
		//TODO do what's below to complete it
		// set its chain

		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();

		int level = 0;
		myPositionConstraint = tightIkEnforcer.createPositionConstraint( level, anchorId, endId );

		return calculateThread;
	}

	private void handleBoneChanged() {
		this.updateInfo();
	}

	public static void main( String[] args ) {

		test.ik.croquet.IkSplitComposite ikSplitComposite = new test.ik.croquet.IkSplitComposite();
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );
		app.getDocumentFrame().getFrame().setMainComposite( ikSplitComposite );

		org.lgna.story.resources.JointId initialAnchor = org.lgna.story.resources.BipedResource.RIGHT_CLAVICLE;
		org.lgna.story.resources.JointId initialEnd = org.lgna.story.resources.BipedResource.RIGHT_WRIST;

		test.ik.croquet.AnchorJointIdState.getInstance().setValueTransactionlessly( initialAnchor );
		test.ik.croquet.EndJointIdState.getInstance().setValueTransactionlessly( initialEnd );

		test.ik.croquet.IsLinearEnabledState.getInstance().setValueTransactionlessly( true );
		test.ik.croquet.IsAngularEnabledState.getInstance().setValueTransactionlessly( false );

		IkProgram program = new IkProgram();

		ikSplitComposite.getSceneComposite().getView().initializeInAwtContainer( program );
		program.initializeTest();

		app.getDocumentFrame().getFrame().setSize( 1200, 800 );
		app.getDocumentFrame().getFrame().setVisible( true );
	}
}
