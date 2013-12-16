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

import java.util.UUID;

import org.alice.ide.croquet.edits.ast.ChangeMethodBodyEdit;
import org.alice.ide.croquet.edits.ast.DeclareMethodEdit;
import org.alice.ide.name.validators.MethodNameValidator;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.CompositeView;
import org.lgna.ik.poser.animation.composites.AnimatorControlComposite;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SJointedModel;
import org.lgna.story.SQuadruped;
import org.lgna.story.SetPose;

/**
 * @author Matt May
 */
public abstract class AbstractAnimatorInputDialogComposite<M extends SJointedModel> extends AbstractPoserOrAnimatorInputDialogComposite<AnimatorControlComposite<M>, M> {

	public static final JavaMethod SET_POSE = JavaMethod.getInstance( SBiped.class, "setPose", org.lgna.ik.poser.pose.Pose.class, SetPose.Detail[].class );
	private MethodNameValidator validator;
	private UserMethod method;

	public AbstractAnimatorInputDialogComposite( NamedUserType valueType, UserMethod editedMethod, UUID uuid ) {
		super( valueType, uuid );
		this.method = editedMethod;
	}

	@Override
	protected AnimatorControlComposite<M> createControlComposite() {
		return new AnimatorControlComposite<M>( this );
	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		AnimatorControlComposite<M> controlComposite = this.getControlComposite();
		BlockStatement body = controlComposite.createMethodBody();
		if( method != null ) {
			return new ChangeMethodBodyEdit( completionStep, method, body );
		} else {
			return new DeclareMethodEdit( completionStep, getDeclaringType(), controlComposite.getNameState().getValue(), org.lgna.project.ast.JavaType.VOID_TYPE, body );
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		if( validator != null ) {
			//pass
		} else {
			this.validator = new MethodNameValidator( getDeclaringType() );
		}
		ErrorStatus errorStatus = this.createErrorStatus( this.createKey( "errorStatus" ) );
		String candidate = getControlComposite().getNameState().getValue();
		String explanation = validator.getExplanationIfOkButtonShouldBeDisabled( candidate );
		if( explanation != null ) {
			errorStatus.setText( explanation );
			return errorStatus;
		} else {
			return IS_GOOD_TO_GO_STATUS;
		}
	}

	@Override
	protected CompositeView createView() {
		CompositeView splitPane = super.createView();
		BorderPanel panel = new BorderPanel();
		panel.addCenterComponent( splitPane );
		panel.addPageEndComponent( this.getControlComposite().getSouthViewForDialog() );
		return panel;
	}

	public static boolean isStrictlyAnimation( UserMethod candidate ) {
		if( candidate != null ) {
			if( !( candidate.getDeclaringType() instanceof NamedUserType ) ) {
				return false;
			}
			if( !candidate.getDeclaringType().isAssignableTo( SBiped.class ) ) {
				return false;
			}
			return CheckIfAnimationCrawler.initiateAndCheckMethod( candidate );
		} else {
			return true;
		}
	}

	public UserMethod getMethod() {
		return this.method;
	}

	public static AbstractAnimatorInputDialogComposite<?> getDialogForUserType( UserType<?> declaringType, UserMethod method ) {
		if( declaringType != null ) {
			if( ( declaringType instanceof NamedUserType ) && AbstractAnimatorInputDialogComposite.isStrictlyAnimation( method ) ) {
				NamedUserType namedUserType = (NamedUserType)declaringType;
				if( namedUserType.isAssignableTo( SBiped.class ) ) {
					return new BipedAnimatorInputDialog( namedUserType, method );
				} else if( namedUserType.isAssignableTo( SQuadruped.class ) ) {
					return new QuadrupedAnimatorInputDialog( namedUserType, method );
				} else if( namedUserType.isAssignableTo( SFlyer.class ) ) {
					return new FlyerAnimatorInputDialog( namedUserType, method );
				}
			}
		}
		return null;
	}
}
