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
package test.math;

/**
 * @author Dennis Cosgrove
 */
public class MathTestProgram extends org.lgna.story.SProgram {
	private final MathTestScene scene = new MathTestScene();

	public static void main( final String[] args ) {
		org.lgna.story.SSphere feedback = new org.lgna.story.SSphere();
		feedback.setRadius( 0.1 );
		feedback.setPaint( org.lgna.story.Color.RED );

		MathTestProgram mathTestProgram = new MathTestProgram();
		mathTestProgram.initializeInFrame( args );
		mathTestProgram.setActiveScene( mathTestProgram.scene );

		final org.lgna.story.implementation.SceneImp sceneImp = org.lgna.story.EmployeesOnly.getImplementation( mathTestProgram.scene );
		final org.lgna.story.implementation.SphereImp feedbackImp = org.lgna.story.EmployeesOnly.getImplementation( feedback );
		final org.lgna.story.implementation.GroundImp groundImp = org.lgna.story.EmployeesOnly.getImplementation( mathTestProgram.scene.getGround() );
		final org.lgna.story.implementation.CameraImp<?> cameraImp = org.lgna.story.EmployeesOnly.getImplementation( mathTestProgram.scene.getCamera() );

		org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.EmployeesOnly.getImplementation( mathTestProgram );
		final edu.cmu.cs.dennisc.renderer.OnscreenRenderTarget<?> renderTarget = programImp.getOnscreenRenderTarget();
		java.awt.Component awtComponent = renderTarget.getAwtComponent();
		//		awtComponent.addMouseMotionListener( new java.awt.event.MouseMotionAdapter() {
		//			@Override
		//			public void mouseDragged( java.awt.event.MouseEvent e ) {
		//				edu.cmu.cs.dennisc.math.Ray rayInCameraSpace = renderTarget.getRayAtPixel( e.getX(), e.getY() );
		//				edu.cmu.cs.dennisc.math.Ray rayInSceneSpace = new edu.cmu.cs.dennisc.math.Ray( rayInCameraSpace );
		//				rayInSceneSpace.transform( cameraImp.getAbsoluteTransformation() );
		//
		//				//edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = groundImp.getTransformation( cameraImp );
		//				edu.cmu.cs.dennisc.math.Plane plane = edu.cmu.cs.dennisc.math.Plane.createInstance( new edu.cmu.cs.dennisc.math.Point3( 0, 0, 0 ), new edu.cmu.cs.dennisc.math.Vector3( 0, 1, 0 ) );
		//				//ray.transform( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createInverse( cameraImp.getAbsoluteTransformation() ) );
		//				double t = plane.intersect( rayInSceneSpace );
		//				if( Double.isNaN( t ) == false ) {
		//					edu.cmu.cs.dennisc.math.Point3 pInSceneSpace = rayInSceneSpace.getPointAlong( t );
		//					edu.cmu.cs.dennisc.math.Point3 pInCameraSpace = rayInCameraSpace.getPointAlong( t );
		//
		//					//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( ray );
		//					//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( t );
		//					//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( p );
		//					//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( pInGroundSpace );
		//					feedbackImp.setVehicle( sceneImp );
		//					//edu.cmu.cs.dennisc.math.Point3 pInGroundSpace = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createInverse( m ).createTransformed( pInCameraSpace );
		//					feedbackImp.setPositionOnly( cameraImp, pInCameraSpace );
		//					//feedbackImp.setPositionOnly( sceneImp, pInSceneSpace );
		//				} else {
		//					feedbackImp.setVehicle( null );
		//				}
		//			}
		//		} );
		awtComponent.addMouseMotionListener( new java.awt.event.MouseMotionAdapter() {
			@Override
			public void mouseDragged( java.awt.event.MouseEvent e ) {
				edu.cmu.cs.dennisc.math.Ray rayInCameraSpace = renderTarget.getRayAtPixel( e.getX(), e.getY() );

				edu.cmu.cs.dennisc.math.immutable.MAffineMatrix4x4 mCameraAbsoluteTransformation = cameraImp.getAbsoluteTransformation().createImmutable();
				edu.cmu.cs.dennisc.math.immutable.MRay mRayInCameraSpace = rayInCameraSpace.createImmutable();

				edu.cmu.cs.dennisc.math.immutable.MRay mRayInSceneSpace = mRayInCameraSpace.createTransformed( mCameraAbsoluteTransformation );

				edu.cmu.cs.dennisc.math.immutable.MPlane mPlaneInSceneSpace = edu.cmu.cs.dennisc.math.immutable.MPlane.createInstance( new edu.cmu.cs.dennisc.math.immutable.MPoint3( 0, 0, 0 ), new edu.cmu.cs.dennisc.math.immutable.MVector3( 0, 1, 0 ) );
				edu.cmu.cs.dennisc.math.immutable.MPlane mPlaneInCameraSpace = mPlaneInSceneSpace.createTransformed( mCameraAbsoluteTransformation.createInverse() );

				//double t = mPlaneInSceneSpace.intersect( mRayInSceneSpace );
				double t = mPlaneInCameraSpace.intersect( mRayInCameraSpace );
				if( Double.isNaN( t ) == false ) {
					edu.cmu.cs.dennisc.math.immutable.MPoint3 mPointInSceneSpace = mRayInSceneSpace.calculatePointAlong( t );
					edu.cmu.cs.dennisc.math.immutable.MPoint3 mPointInCameraSpace = mRayInCameraSpace.calculatePointAlong( t );
					feedbackImp.setVehicle( sceneImp );
					//feedbackImp.setPositionOnly( sceneImp, new edu.cmu.cs.dennisc.math.Point3( mPointInSceneSpace ) );
					feedbackImp.setPositionOnly( cameraImp, new edu.cmu.cs.dennisc.math.Point3( mPointInCameraSpace ) );
				} else {
					feedbackImp.setVehicle( null );
				}
			}
		} );
	}
}
