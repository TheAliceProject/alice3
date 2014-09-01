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
package org.alice.ide.cascade.fillerinners;

import java.util.List;

import org.alice.ide.custom.PoseCustomExpressionCreatorComposite;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryState;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.ik.core.pose.Pose;
import org.lgna.ik.poser.PoserGenerationException;
import org.lgna.ik.poser.input.AbstractPoserInputDialogComposite;
import org.lgna.ik.poser.input.BipedPoserInputDialog;
import org.lgna.ik.poser.input.FlyerPoserInputDialog;
import org.lgna.ik.poser.input.QuadrupedPoserInputDialog;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SQuadruped;

/**
 * @author Matt May
 */
public class PoseFillerInner extends ExpressionFillerInner {

	public PoseFillerInner() {
		super( Pose.class );
	}

	@Override
	public void appendItems( List<CascadeBlankChild> items, ValueDetails<?> details, boolean isTop, Expression prevExpression ) {
		//come back to this later, how to fill in more poses for parent classes
		//		System.out.println( items.size() );
		//		for( CascadeBlankChild child : items ) {
		//			System.out.println( child );
		//			if( child instanceof ParameterNameSeparator ) {
		//				ParameterNameSeparator sep = (ParameterNameSeparator)child;
		//				System.out.println( sep.getItemCount() );
		//				for( int i = 0; i != sep.getItemCount(); ++i ) {
		//					System.out.println( "  " + sep.getItemAt( i ) );
		//				}
		//			}
		//		}
		AbstractType<?, ?, ?> type = null;
		System.out.println( "BOOOGA" );
		System.out.println( "BOOOGA" );
		System.out.println( "BOOOGA" );
		System.out.println( getType() );
		if( prevExpression == null ) {
			InstanceFactory value = InstanceFactoryState.getInstance().getValue();
			type = value.getValueType();
		} else {
			MethodInvocation methodInv = prevExpression.getFirstAncestorAssignableTo( MethodInvocation.class );
			type = methodInv.expression.getValue().getType();
		}
		System.out.println( type );
		AbstractPoserInputDialogComposite dialog = getDialogForType( type );
		dialog.getCommitOperation().getMenuItemPrepModel();
		dialog.getLaunchOperation().getMenuItemPrepModel();
		items.add( new PoseCustomExpressionCreatorComposite( dialog ).getValueCreator().getFillIn() );
	}

	private AbstractPoserInputDialogComposite getDialogForType( AbstractType<?, ?, ?> type ) {
		AbstractPoserInputDialogComposite rv;
		if( type instanceof NamedUserType ) {
			NamedUserType userType = (NamedUserType)type;
			if( type.isAssignableTo( SBiped.class ) ) {
				rv = new BipedPoserInputDialog( userType );
			} else if( type.isAssignableTo( SQuadruped.class ) ) {
				rv = new QuadrupedPoserInputDialog( userType );
			} else if( type.isAssignableTo( SFlyer.class ) ) {
				rv = new FlyerPoserInputDialog( userType );
			} else {
				throw new PoserGenerationException( "We don't yet have poses for this type: " + type );
			}
		} else {
			throw new PoserGenerationException( "We only handle NamedUserTypes" );
		}
		return rv;
	}
}
