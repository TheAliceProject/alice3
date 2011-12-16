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

import org.lgna.story.*;
import org.lgna.story.implementation.alice.JointImplementation;

/**
 * @author Dennis Cosgrove
 */
class IkProgram extends Program {
	private final Camera camera = new Camera();
	private final Biped ogre = new Biped( org.lgna.story.resources.biped.Ogre.BROWN_OGRE );
	private final IkScene scene = new IkScene( camera, ogre );
	private final edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	private final edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter modelManipulationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter();

	private static final void printChainOfJoints( org.lgna.story.implementation.JointedModelImp<?,?> imp, org.lgna.story.resources.JointId aId, org.lgna.story.resources.JointId bId ) {
		java.util.List< org.lgna.story.implementation.JointImp > chain = imp.getInclusiveListOfJointsBetween( aId, bId );
		System.out.println( aId + " " + bId );
		for( org.lgna.story.implementation.JointImp jointImp : chain ) {
			System.out.println( "\t" + jointImp + " " );
			if (jointImp instanceof JointImplementation) {
				JointImplementation ji = (JointImplementation) jointImp;
				System.out.println(ji.getSgComposite().isFreeInX.getValue() + " " + ji.getSgComposite().isFreeInY.getValue() + " " + ji.getSgComposite().isFreeInZ.getValue());
			}
			//not there for nebulous joint
//			if (jointImp instanceof org.lgna.story.implementation.sims2.JointImplementation) {
//				org.lgna.story.implementation.sims2.JointImplementation ji = (org.lgna.story.implementation.sims2.JointImplementation) jointImp;
//				System.out.println(ji.getSgComposite().isFreeInX.getValue() + " " + ji.getSgComposite().isFreeInY.getValue() + " " + ji.getSgComposite().isFreeInZ.getValue());
//			}
		}
	}
	public void runTest() {
		org.lgna.story.implementation.JointedModelImp<?,?> imp = ImplementationAccessor.getImplementation( ogre );
		printChainOfJoints( imp, org.lgna.story.resources.BipedResource.LEFT_ANKLE, org.lgna.story.resources.BipedResource.RIGHT_ELBOW );
		printChainOfJoints( imp, org.lgna.story.resources.BipedResource.SPINE_MIDDLE, org.lgna.story.resources.BipedResource.RIGHT_ELBOW );
		printChainOfJoints( imp, org.lgna.story.resources.BipedResource.RIGHT_ELBOW, org.lgna.story.resources.BipedResource.SPINE_MIDDLE );
		
		this.setActiveScene( this.scene );
		this.modelManipulationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		this.cameraNavigationDragAdapter.requestDistance( 8.0 );
	}

	public static void main( String[] args ) {
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );
		app.setPerspective( new test.ik.croquet.IkPerspective() );

		IkProgram program = new IkProgram();
		
		test.ik.croquet.SceneComposite.getInstance().getView().initializeInAwtContainer( program );
		program.runTest();

		app.getFrame().setSize( 1200, 800 );
		app.getFrame().setVisible( true );
	}
}
