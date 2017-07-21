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

import org.lgna.croquet.Model;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Step;

/**
 * @author Matt May
 */
public class DeclarePoseFieldOperation extends SingleThreadIteratingOperation {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.NamedUserType, DeclarePoseFieldOperation> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static DeclarePoseFieldOperation getInstance( org.lgna.project.ast.NamedUserType declaringType ) {
		if( PoserComposite.isPoseable( declaringType ) ) {
			return map.getInitializingIfAbsent( declaringType, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.NamedUserType, DeclarePoseFieldOperation>() {
				@Override
				public DeclarePoseFieldOperation initialize( org.lgna.project.ast.NamedUserType declaringType ) {
					return new DeclarePoseFieldOperation( declaringType );
				}
			} );
		} else {
			return null;
		}
	}

	private DeclarePoseFieldOperation( org.lgna.project.ast.NamedUserType declaringType ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "c20e6e66-78dd-4bb7-9ed9-8cb2096f5e18" ) );
		this.declaringType = declaringType;
	}

	@Override
	protected boolean hasNext( CompletionStep<?> step, java.util.List<Step<?>> subSteps, Object iteratingData ) {
		return subSteps.size() < 2;
	}

	@Override
	protected Model getNext( CompletionStep<?> step, java.util.List<Step<?>> subSteps, Object iteratingData ) {
		switch( subSteps.size() ) {
		case 0:
			return PoseExpressionCreatorComposite.getInstance( this.declaringType ).getValueCreator();
		case 1:
			org.lgna.croquet.history.Step<?> prevSubStep = subSteps.get( 0 );
			if( prevSubStep.containsEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY ) ) {
				org.lgna.project.ast.Expression expression = (org.lgna.project.ast.Expression)prevSubStep.getEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY );
				org.lgna.ik.poser.croquet.AddUnmanagedPoseFieldComposite addUnmanagedPoseFieldComposite = org.lgna.ik.poser.croquet.AddUnmanagedPoseFieldComposite.getInstance( this.declaringType );
				addUnmanagedPoseFieldComposite.setInitializerInitialValue( expression );
				return addUnmanagedPoseFieldComposite.getLaunchOperation();
			} else {
				return null;
			}

		default:
			throw new Error();
		}
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( CompletionStep<?> step, java.util.List<Step<?>> subSteps ) {
		//		UserField field = getControlComposite().createPoseField( getControlComposite().getNameState().getValue() );
		//		NamedUserType declaringType = this.getDeclaringType();
		//		return new DeclareNonGalleryFieldEdit( completionStep, declaringType, field );
	}

	private final org.lgna.project.ast.NamedUserType declaringType;
}
