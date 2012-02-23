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

import java.util.Map;

import org.lgna.ik.Bone.Axis;
import org.lgna.story.Biped;
import org.lgna.story.Camera;
import org.lgna.story.Color;
import org.lgna.story.Cone;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Model;
import org.lgna.story.MoveDirection;
import org.lgna.story.Position;
import org.lgna.story.Program;
import org.lgna.story.Sphere;
import org.lgna.story.Turn;
import org.lgna.story.TurnDirection;
import org.lgna.story.implementation.JointImp;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisRotation;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author Dennis Cosgrove
 */
class IkProgram extends Program {
	
	private final Camera camera = new Camera();
	private final Biped ogre = new Biped( org.lgna.story.resources.biped.Ogre.BROWN_OGRE );
	private final Model target = createDragProp();;
	private final IkScene scene = new IkScene( camera, ogre, target );
	private final edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	private final NiceDragAdapter modelManipulationDragAdapter = new NiceDragAdapter();

	private final org.lgna.croquet.State.ValueObserver< Boolean > linearAngularEnabledListener = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			IkProgram.this.handleChainChanged();
		}
	};
	private final org.lgna.croquet.State.ValueObserver< org.lgna.story.resources.JointId > jointIdListener = new org.lgna.croquet.State.ValueObserver< org.lgna.story.resources.JointId >() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.JointId > state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.JointId > state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
			IkProgram.this.handleChainChanged();
		}
	};
	private final org.lgna.croquet.State.ValueObserver< org.lgna.ik.Bone > boneListener = new org.lgna.croquet.State.ValueObserver< org.lgna.ik.Bone >() {
		public void changing( org.lgna.croquet.State< org.lgna.ik.Bone > state, org.lgna.ik.Bone prevValue, org.lgna.ik.Bone nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.ik.Bone > state, org.lgna.ik.Bone prevValue, org.lgna.ik.Bone nextValue, boolean isAdjusting ) {
			IkProgram.this.handleBoneChanged();
		}
	};
	private final edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener targetTransformListener = new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener() {
		public void absoluteTransformationChanged(edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent) {
			IkProgram.this.handleTargetTransformChanged();
		}
	};
	private org.lgna.ik.Chain chain;
	private org.lgna.ik.Solver solver;
	protected Map<Axis, Double> currentSpeeds;
	
	private org.lgna.story.implementation.SphereImp getTargetImp() {
		return ImplementationAccessor.getImplementation( this.target );
	}
	private Model createDragProp() {
		Sphere mainSphere = new Sphere();
		
		mainSphere.setRadius( 0.13 );
		mainSphere.setPaint( Color.RED );
		mainSphere.setOpacity( 0.5 );
		
		mainSphere.resizeHeight(.7);
		mainSphere.resizeWidth(.5);
		
		Cone cone = new Cone();
		cone.setBaseRadius(.07);
		cone.setLength(.2);
		cone.setPaint(Color.ORANGE);
//		cone.setOpacity(.5);
		cone.resizeWidth(.5);
		cone.resize(.8);
		cone.setVehicle(mainSphere);
		cone.move(MoveDirection.DOWN, .1);
		cone.move(MoveDirection.RIGHT, .05);
		cone.turn(TurnDirection.FORWARD, .25, Turn.asSeenBy(cone.getVehicle()));
		
		return mainSphere;
	}
	private org.lgna.story.implementation.JointedModelImp< ?,? > getSubjectImp() {
		return ImplementationAccessor.getImplementation( this.ogre );
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
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getTargetImp().getTransformation( this.getAnchorImp() );
//		edu.cmu.cs.dennisc.print.PrintUtilities.printlns( m );
		this.updateInfo();
	}
	private void updateInfo() {
		org.lgna.ik.Bone bone = test.ik.croquet.BonesState.getInstance().getSelectedItem();
		
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
		
		test.ik.croquet.InfoState.getInstance().setValue( sb.toString() );
	}
	private org.lgna.ik.Chain createChain() {
		boolean isLinearEnabled = test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
		boolean isAngularEnabled = test.ik.croquet.IsAngularEnabledState.getInstance().getValue();
		
		if(!isLinearEnabled && !isAngularEnabled) return null;
		
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		return org.lgna.ik.Chain.createInstance( this.getSubjectImp(), anchorId, endId, isLinearEnabled, isAngularEnabled );
	}
	private void handleChainChanged() {//FIXME make this not race with the thread
		if(chain != null) {
			solver.removeChain(chain);
		}
		chain = createChain();
		
		if(chain != null) {
			setDragAdornmentsVisible(true);
			JointImp lastJointImp = chain.getLastJointImp();
			
			AffineMatrix4x4 ltrans = lastJointImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);

			edu.cmu.cs.dennisc.math.Point3 eePos = new edu.cmu.cs.dennisc.math.Point3(ltrans.translation);
			eePos.add(Point3.createMultiplication(ltrans.orientation.backward, -.2)); //can do something like this to drag fingertips. right now it results in jumping. 
			chain.setEndEffectorPosition(eePos);
			
//			this.getTargetImp().setTransformation( this.getEndImp() );
			//assuming that all are parented to scene...
			this.getTargetImp().setLocalTransformation( new AffineMatrix4x4(chain.getEndEffectorOrientation(), chain.getEndEffectorPosition()) );
			solver.addChain(chain);
		} else {
			setDragAdornmentsVisible(false);
		}
		
		test.ik.croquet.BonesState.getInstance().setChain( chain );
		this.updateInfo();
	}
	private void setDragAdornmentsVisible(boolean visible) {
		if(visible) {
			scene.anchor.setVehicle(scene);
			scene.ee.setVehicle(scene);
		} else {
			scene.anchor.setVehicle(null);
			scene.ee.setVehicle(null);
		}
	}
	private void initializeTest() {
		this.setActiveScene( this.scene );
		
		this.modelManipulationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		this.cameraNavigationDragAdapter.requestDistance( 8.0 );

		test.ik.croquet.AnchorJointIdState.getInstance().addValueObserver( this.jointIdListener );
		test.ik.croquet.EndJointIdState.getInstance().addValueObserver( this.jointIdListener );
		test.ik.croquet.BonesState.getInstance().addValueObserver( this.boneListener );
		test.ik.croquet.IsLinearEnabledState.getInstance().addValueObserver( this.linearAngularEnabledListener );
		test.ik.croquet.IsAngularEnabledState.getInstance().addValueObserver( this.linearAngularEnabledListener );
		

		this.getTargetImp().setTransformation( this.getEndImp() );
		this.getTargetImp().getSgComposite().addAbsoluteTransformationListener( this.targetTransformListener );
		
		solver = new org.lgna.ik.Solver();
		
		//did not find a way to perform a custom animation
//		Thread moveThread = new Thread() {
//			@Override
//			public void run() {
//				while(!interrupted()) {
//					//move all the joints
//					
//					movePls();
//					
//					try {
//						sleep(100);
//					} catch (InterruptedException e) {
//						break;
//					}
//				}
//			}
//
//		};
//		moveThread.start();
		
		Thread calculateThread = new Thread() {
			@Override
			public void run() {
				while(!interrupted()) {
					//making sure I only do it when UI says it's activated. 
					//FIXME these following two are not good because they are used when creating the chain.
					boolean linearActivated = test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
					boolean angularActivated = test.ik.croquet.IsAngularEnabledState.getInstance().getValue();
					if(chain != null && (linearActivated || angularActivated)) { //not good concurrent programming practice but temporary solution
						//make chain setter not race with this

						AffineMatrix4x4 targetTransformation = getTargetImp().getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
						if(linearActivated) {
							Point3 desiredLinearDistance = Point3.createSubtraction(targetTransformation.translation, chain.getEndEffectorPosition());
							
							//not going to use it directly because is likely to be too fast (linear is bad approximation for large steps)
							
							Vector3 linVelToUse;
							
							double maxLinearSpeed = 0.1;
							
							if(desiredLinearDistance.calculateMagnitude() > maxLinearSpeed) {
								desiredLinearDistance.normalize();
								linVelToUse = Vector3.createMultiplication(desiredLinearDistance, maxLinearSpeed);
							} else {
								linVelToUse = new Vector3(desiredLinearDistance);
							}
							while(chain.getDesiredEndEffectorLinearVelocity() == null); //preventing race condition temporarily...
							chain.setDesiredEndEffectorLinearVelocity(linVelToUse);
						}
						
						if(angularActivated) {
							//these both are not local, so it's good.
							OrthogonalMatrix3x3 desiredOrientation = targetTransformation.orientation;
							OrthogonalMatrix3x3 currentOrientation = chain.getEndEffectorOrientation();
							
							OrthogonalMatrix3x3 inverseCurrent = new OrthogonalMatrix3x3(currentOrientation);
							inverseCurrent.invert();
							
							OrthogonalMatrix3x3 diff = new OrthogonalMatrix3x3();
							diff.setToMultiplication(desiredOrientation, inverseCurrent);
							
							AxisRotation diffAxisRotation = new edu.cmu.cs.dennisc.math.AxisRotation(diff);
							
							Vector3 desiredAngularDistance = Vector3.createMultiplication(diffAxisRotation.axis, diffAxisRotation.angle.getAsRadians());
							
							//not going to use it directly because is likely to be too fast (linear is bad approximation for large steps)
							
							Vector3 angVelToUse;

							double maxAngularSpeed = Math.PI / 10.0;
							
							if(desiredAngularDistance.calculateMagnitude() > maxAngularSpeed) {
								desiredAngularDistance.normalize();
								angVelToUse = Vector3.createMultiplication(desiredAngularDistance, maxAngularSpeed);
							} else {
								angVelToUse = desiredAngularDistance;
							}
							while(chain.getDesiredEndEffectorAngularVelocity() == null); //preventing race condition temporarily...
							chain.setDesiredEndEffectorAngularVelocity(angVelToUse);
						}
						
						
						
						java.util.Map<org.lgna.ik.Bone.Axis, Double> speeds = solver.solve();
						
						if(speeds == null) {
//							System.out.println("speeds is null");
							continue;
						}
						
						currentSpeeds = speeds;
						
						//now apply these
						
						for(java.util.Map.Entry<org.lgna.ik.Bone.Axis, Double> e: currentSpeeds.entrySet()) {
							Axis axis = e.getKey();
							Double speed = e.getValue();
							
							axis.setDesiredAngleSpeed(speed);
						}
						
						javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								test.ik.croquet.BonesState.getInstance().setChain( chain );
							}
						});
						
						//force bone reprint
						
						//apply rotational velocities?
						//would this be better as an animation instead?
						
//						System.out.println("displayed");
					}
					
					movePls();
//					moveStraight();
					
					try {
						sleep(10);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		};
		calculateThread.start();
		
		this.handleChainChanged();
	}
//	private void moveStraight() {
//		if(chain != null) {
//			Bone[] bones = chain.getBones();
//			bones[0].getA().applyRotationInRadians(new Vector3(1, 1, 0), Math.PI / 100.0); //this is local. that's the issue... FIXME
//		}
//	}
	private void movePls() {
		if(chain != null) {
			
			if(!chain.isEmpty()) {
				Point3 ap = chain.getAnchorPosition();
				Point3 ep = chain.getEndEffectorPosition();
				//vehicle is scene, so it's not local. 
				scene.anchor.setPositionRelativeToVehicle(new Position(ap.x, ap.y, ap.z));
				scene.ee.setPositionRelativeToVehicle(new Position(ep.x, ep.y, ep.z));
			}
			
			double dt = .1; //FIXME
			chain.applyVelocitiesForDuration(dt);
		}
	}

	private void handleBoneChanged() {
		this.updateInfo();
	}
	public static void main( String[] args ) {
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );
		app.setPerspective( new test.ik.croquet.IkPerspective() );

		org.lgna.story.resources.JointId initialAnchor = org.lgna.story.resources.BipedResource.RIGHT_SHOULDER; 
		org.lgna.story.resources.JointId initialEnd = org.lgna.story.resources.BipedResource.RIGHT_WRIST; 

		test.ik.croquet.AnchorJointIdState.getInstance().setValue( initialAnchor );
		test.ik.croquet.EndJointIdState.getInstance().setValue( initialEnd );
		
		test.ik.croquet.IsLinearEnabledState.getInstance().setValue(true);
		test.ik.croquet.IsAngularEnabledState.getInstance().setValue(false);
		
		IkProgram program = new IkProgram();

		test.ik.croquet.SceneComposite.getInstance().getView().initializeInAwtContainer( program );
		program.initializeTest();

		app.getFrame().setSize( 1200, 800 );
		app.getFrame().setVisible( true );
	}
}
