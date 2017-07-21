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

import org.alice.ide.name.validators.MethodNameValidator;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.CompositeView;
import org.lgna.croquet.views.Panel;
import org.lgna.ik.poser.CheckIfAnimationCrawler;
import org.lgna.ik.poser.animation.composites.AnimatorControlComposite;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SJointedModel;
import org.lgna.story.SQuadruped;

/**
 * @author Matt May
 */
public abstract class AnimatorComposite<M extends SJointedModel> extends AbstractPoserOrAnimatorComposite<AnimatorControlComposite<M>, M> {
	private MethodNameValidator validator;
	private UserMethod method;

	public AnimatorComposite( UUID migrationId, NamedUserType valueType, UserMethod editedMethod ) {
		super( migrationId, valueType );
		this.method = editedMethod;
	}

	@Override
	protected AnimatorControlComposite<M> createControlComposite() {
		return new AnimatorControlComposite<M>( this );
	}

	@Override
	protected Panel createView() {
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
			return CheckIfAnimationCrawler.initiateAndCheckMethod( candidate );
		} else {
			return true;
		}
	}

	public UserMethod getMethod() {
		return this.method;
	}

	public static AnimatorComposite<?> getDialogForUserType( UserType<?> declaringType, UserMethod method ) {
		if( declaringType != null ) {
			if( ( declaringType instanceof NamedUserType ) && AnimatorComposite.isStrictlyAnimation( method ) ) {
				NamedUserType namedUserType = (NamedUserType)declaringType;
				if( namedUserType.isAssignableTo( SBiped.class ) ) {
					return new BipedAnimator( namedUserType, method );
				} else if( namedUserType.isAssignableTo( SQuadruped.class ) ) {
					return new QuadrupedAnimator( namedUserType, method );
				} else if( namedUserType.isAssignableTo( SFlyer.class ) ) {
					return new FlyerAnimator( namedUserType, method );
				}
			}
		}
		return null;
	}

	public static AnimatorComposite<?> getDialogForUserMethod( UserMethod method ) {
		return getDialogForUserType( method.getDeclaringType(), method );
	}
}
