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
package org.lgna.ik.poser;

import org.alice.ide.croquet.edits.ast.DeclareMethodEdit;
import org.alice.ide.name.validators.MethodNameValidator;
import org.lgna.croquet.OperationInputDialogCoreComposite;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.poser.view.AnimationNamingView;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;

import org.lgna.story.SBiped;import org.lgna.story.Orientation;import org.lgna.ik.poser.Pose.Builder;class Bogus extends SBiped{public Void a(){this.setPose(new Builder().rightArm(new Orientation(-0.334632,-0.82115,0.236049,0.397511),new Orientation(-0.468727,0.138878,0.183267,0.85289),new Orientation(0.0,0.00323889,0.0,0.999995),new Orientation(0.0,0.0,0.0,1.0)).leftArm(new Orientation(-0.707107,0.0,0.0,0.707107),new Orientation(0.037113,0.0,0.0,0.999311),new Orientation(-0.129062,0.0,0.0,0.991636),new Orientation(0.476142,0.0,0.0,0.879369)).rightLeg(new Orientation(-0.334631,0.82115,-0.23605,0.397511),new Orientation(-0.460776,-0.140577,-0.181968,0.857211),new Orientation(0.0,-0.00323889,0.0,0.999995),new Orientation(0.0,0.0,0.0,1.0)).leftLeg(new Orientation(-0.334631,0.82115,-0.23605,0.397511),new Orientation(0.0371134,0.0,0.0,0.999311),new Orientation(-0.129063,0.0,0.0,0.991636),new Orientation(0.476141,0.0,0.0,0.879369)).build());this.setPose(new Builder().rightArm(new Orientation(-0.334632,-0.82115,0.236049,0.397511),new Orientation(-0.468727,0.138878,0.183267,0.85289),new Orientation(0.0,0.00323889,0.0,0.999995),new Orientation(0.0,0.0,0.0,1.0)).leftArm(new Orientation(-0.707107,0.0,0.0,0.707107),new Orientation(0.037113,0.0,0.0,0.999311),new Orientation(-0.129062,0.0,0.0,0.991636),new Orientation(0.476142,0.0,0.0,0.879369)).rightLeg(new Orientation(-0.334631,0.82115,-0.23605,0.397511),new Orientation(-0.460776,-0.140577,-0.181968,0.857211),new Orientation(0.0,-0.00323889,0.0,0.999995),new Orientation(0.0,0.0,0.0,1.0)).leftLeg(new Orientation(-0.334631,0.82115,-0.23605,0.397511),new Orientation(0.0371134,0.0,0.0,0.999311),new Orientation(-0.129063,0.0,0.0,0.991636),new Orientation(0.476141,0.0,0.0,0.879369)).build());}}
/**
 * @author Matt May
 */
public class NameAndExportAnimationCompositeInHonorOfJenLapp extends OperationInputDialogCoreComposite<AnimationNamingView> {

	StringState animationName = createStringState( createKey( "animationName" ) );
	private final PoserControlComposite parent;
	private MethodNameValidator validator;

	public NameAndExportAnimationCompositeInHonorOfJenLapp( PoserControlComposite poserControlComposite ) {
		super( java.util.UUID.fromString( "c1a0cf60-a454-4b31-a4f1-718279d8e8e2" ), null );
		this.parent = poserControlComposite;
	}

	@Override
	protected AnimationNamingView createView() {
		return new AnimationNamingView( this );
	}

	public StringState getAnimationName() {
		return this.animationName;
	}

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		if( validator != null ) {
			//pass
		} else {
			this.validator = new MethodNameValidator( parent.getIkPoser().getDeclaringType() );
		}
		ErrorStatus errorStatus = this.createErrorStatus( this.createKey( "errorStatus" ) );
		String candidate = animationName.getValue();
		String explanation = validator.getExplanationIfOkButtonShouldBeDisabled( candidate );
		if( explanation != null ) {
			errorStatus.setText( explanation );
			return errorStatus;
		} else {
			return IS_GOOD_TO_GO_STATUS;
		}
	}

	@Override
	protected String getName() {
		return null;
	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		UserMethod method = parent.createUserMethod( animationName.getValue() );
		boolean IS_READY_FOR_PRIME_TIME = false;
		if( IS_READY_FOR_PRIME_TIME ) {
			UserType<?> declaringType = this.parent.getIkPoser().getDeclaringType();
			return new DeclareMethodEdit( completionStep, declaringType, method );
		} else {
			org.lgna.project.ast.NamedUserType bogusType = new org.lgna.project.ast.NamedUserType();
			bogusType.name.setValue( "Bogus" );
			bogusType.superType.setValue( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SBiped.class ) );
			bogusType.methods.add( method );
			String javaCode = bogusType.generateJavaCode( true );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( javaCode );
			return null;
		}
	}
}


