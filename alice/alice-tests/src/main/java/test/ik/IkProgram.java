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

import org.lgna.story.EmployeesOnly;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SProgram;
import org.lgna.story.SSphere;

/**
 * @author Dennis Cosgrove
 */
class IkProgram extends SProgram {
	private final SCamera camera = new SCamera();
	private final SBiped ogre = new SBiped( org.lgna.story.resources.biped.OgreResource.BROWN );
	private final SSphere target = new SSphere();
	private final IkScene scene = new IkScene( camera, ogre, target );
	private final edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	private final edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter modelManipulationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter();

	private final org.lgna.croquet.event.ValueListener<Boolean> linearAngularEnabledListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			IkProgram.this.handleChainChanged();
		}
	};
	private final org.lgna.croquet.event.ValueListener<org.lgna.story.resources.JointId> jointIdListener = new org.lgna.croquet.event.ValueListener<org.lgna.story.resources.JointId>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.story.resources.JointId> e ) {
			IkProgram.this.handleChainChanged();
		}
	};
	private final org.lgna.croquet.event.ValueListener<org.lgna.ik.Bone> boneListener = new org.lgna.croquet.event.ValueListener<org.lgna.ik.Bone>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.ik.Bone> e ) {
			IkProgram.this.handleBoneChanged();
		}
	};
	private final edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener targetTransformListener = new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener() {
		@Override
		public void absoluteTransformationChanged( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent ) {
			IkProgram.this.handleTargetTransformChanged();
		}
	};

	private org.lgna.story.implementation.SphereImp getTargetImp() {
		return EmployeesOnly.getImplementation( this.target );
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
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getTargetImp().getTransformation( this.getAnchorImp() );
		edu.cmu.cs.dennisc.print.PrintUtilities.printlns( m );
		this.updateInfo();
	}

	private void updateInfo() {
		org.lgna.ik.Bone bone = test.ik.croquet.BonesState.getInstance().getValue();

		StringBuilder sb = new StringBuilder();
		if( bone != null ) {
			org.lgna.story.implementation.JointImp a = bone.getA();
			org.lgna.story.implementation.JointImp b = bone.getB();
			sb.append( a.getJointId() );
			sb.append( ":\n" );
			edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, a.getLocalTransformation() );
			sb.append( "\n" );
			sb.append( b.getJointId() );
			sb.append( ":\n" );
			edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, b.getLocalTransformation() );
		}
		sb.append( "\n" );
		sb.append( "target:\n" );
		edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, this.getTargetImp().getLocalTransformation() );
		sb.append( "\n" );

		test.ik.croquet.InfoState.getInstance().setValueTransactionlessly( sb.toString() );
	}

	private org.lgna.ik.Chain createChain() {
		boolean isLinearEnabled = test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
		boolean isAngularEnabled = test.ik.croquet.IsAngularEnabledState.getInstance().getValue();
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		return org.lgna.ik.Chain.createInstance( this.getSubjectImp(), anchorId, endId, isLinearEnabled, isAngularEnabled );
	}

	private void handleChainChanged() {
		org.lgna.ik.Chain chain = createChain();
		test.ik.croquet.BonesState.getInstance().setChain( chain );
		this.updateInfo();
	}

	private void initializeTest() {
		this.setActiveScene( this.scene );
		this.modelManipulationDragAdapter.setOnscreenRenderTarget( EmployeesOnly.getImplementation( this ).getOnscreenRenderTarget() );
		this.cameraNavigationDragAdapter.setOnscreenRenderTarget( EmployeesOnly.getImplementation( this ).getOnscreenRenderTarget() );
		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		this.cameraNavigationDragAdapter.requestDistance( 8.0 );

		test.ik.croquet.AnchorJointIdState.getInstance().addNewSchoolValueListener( this.jointIdListener );
		test.ik.croquet.EndJointIdState.getInstance().addNewSchoolValueListener( this.jointIdListener );
		test.ik.croquet.BonesState.getInstance().addNewSchoolValueListener( this.boneListener );
		test.ik.croquet.IsLinearEnabledState.getInstance().addNewSchoolValueListener( this.linearAngularEnabledListener );
		test.ik.croquet.IsAngularEnabledState.getInstance().addNewSchoolValueListener( this.linearAngularEnabledListener );

		this.getTargetImp().setTransformation( this.getEndImp() );
		this.getTargetImp().getSgComposite().addAbsoluteTransformationListener( this.targetTransformListener );
		this.handleChainChanged();
	}

	private void handleBoneChanged() {
		this.updateInfo();
	}

	public static void main( String[] args ) {
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );

		org.lgna.croquet.views.Frame frame = app.getDocumentFrame().getFrame();
		frame.setMainComposite( new test.ik.croquet.IkSplitComposite() );

		org.lgna.story.resources.JointId initialAnchor = org.lgna.story.resources.BipedResource.LEFT_CLAVICLE;
		org.lgna.story.resources.JointId initialEnd = org.lgna.story.resources.BipedResource.LEFT_WRIST;

		test.ik.croquet.AnchorJointIdState.getInstance().setValueTransactionlessly( initialAnchor );
		test.ik.croquet.EndJointIdState.getInstance().setValueTransactionlessly( initialEnd );

		IkProgram program = new IkProgram();

		test.ik.croquet.SceneComposite.getInstance().getView().initializeInAwtContainer( program );
		program.initializeTest();

		frame.setSize( 1200, 800 );
		frame.setVisible( true );
	}
}
