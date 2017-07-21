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
package org.lgna.ik.poser.croquet;

import java.util.UUID;

import org.lgna.ik.poser.controllers.PoserControlComposite;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SJointedModel;
import org.lgna.story.SQuadruped;

/**
 * @author Matt May
 */
public abstract class PoserComposite<M extends SJointedModel> extends AbstractPoserOrAnimatorComposite<PoserControlComposite, M> {
	public PoserComposite( UUID migrationID, NamedUserType valueType ) {
		super( migrationID, valueType );
	}

	@Override
	protected PoserControlComposite createControlComposite() {
		PoserControlComposite rv = new PoserControlComposite( this );
		return rv;
	}

	public boolean isEmptyPose() {
		return getControlComposite().getParent().getUsedJoints().isEmpty();
	}

	//	@Override
	//	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
	//		if( getControlComposite().getParent().getUsedJoints().isEmpty() ) {
	//			return emptyPoseStatus;
	//		}
	//		if( validator != null ) {
	//			//pass
	//		} else {
	//			this.validator = new FieldNameValidator( getDeclaringType() );
	//		}
	//		String candidate = getControlComposite().getNameState().getValue();
	//		String explanation = validator.getExplanationIfOkButtonShouldBeDisabled( candidate );
	//		if( explanation != null ) {
	//			errorStatus.setText( explanation );
	//			return errorStatus;
	//		} else {
	//			return IS_GOOD_TO_GO_STATUS;
	//		}
	//	}

	public static boolean isPoseable( org.lgna.project.ast.NamedUserType declaringType ) {
		return getDialogForUserType( declaringType ) != null;
	}

	public static PoserComposite<?> getDialogForUserType( org.lgna.project.ast.NamedUserType declaringType ) {
		if( declaringType.isAssignableTo( SBiped.class ) ) {
			return new BipedPoser( declaringType );
		} else if( declaringType.isAssignableTo( SQuadruped.class ) ) {
			return new QuadrupedPoser( declaringType );
		} else if( declaringType.isAssignableTo( SFlyer.class ) ) {
			return new FlyerPoser( declaringType );
		} else {
			return null;
		}
	}
}
