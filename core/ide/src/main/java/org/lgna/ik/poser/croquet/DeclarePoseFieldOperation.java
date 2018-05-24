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

import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.IDE;
import org.lgna.croquet.Model;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.ValueCreator;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Step;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.NamedUserType;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Matt May
 */
public class DeclarePoseFieldOperation extends SingleThreadIteratingOperation {
	private static InitializingIfAbsentMap<NamedUserType, DeclarePoseFieldOperation> map = Maps.newInitializingIfAbsentHashMap();

	public static DeclarePoseFieldOperation getInstance( NamedUserType declaringType ) {
		if( PoserComposite.isPoseable( declaringType ) ) {
			return map.getInitializingIfAbsent( declaringType, new InitializingIfAbsentMap.Initializer<NamedUserType, DeclarePoseFieldOperation>() {
				@Override
				public DeclarePoseFieldOperation initialize( NamedUserType declaringType ) {
					return new DeclarePoseFieldOperation( declaringType );
				}
			} );
		} else {
			return null;
		}
	}

	private DeclarePoseFieldOperation( NamedUserType declaringType ) {
		super( IDE.PROJECT_GROUP, UUID.fromString( "c20e6e66-78dd-4bb7-9ed9-8cb2096f5e18" ) );
		this.declaringType = declaringType;
	}

	@Override
	protected boolean hasNext( CompletionStep<?> step, List<Step<?>> subSteps, Iterator<Model> iteratingData ) {
		return subSteps.size() < 2;
	}

	@Override
	protected Model getNext( CompletionStep<?> step, List<Step<?>> subSteps, Iterator<Model> iteratingData ) {
		switch( subSteps.size() ) {
		case 0:
			return PoseExpressionCreatorComposite.getInstance( this.declaringType ).getValueCreator();
		case 1:
			Step<?> prevSubStep = subSteps.get( 0 );
			if( prevSubStep.containsEphemeralDataFor( ValueCreator.VALUE_KEY ) ) {
				Expression expression = (Expression)prevSubStep.getEphemeralDataFor( ValueCreator.VALUE_KEY );
				AddUnmanagedPoseFieldComposite addUnmanagedPoseFieldComposite = AddUnmanagedPoseFieldComposite.getInstance( this.declaringType );
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
	protected void handleSuccessfulCompletionOfSubModels( CompletionStep<?> step, List<Step<?>> subSteps ) {
		//		UserField field = getControlComposite().createPoseField( getControlComposite().getNameState().getValue() );
		//		NamedUserType declaringType = this.getDeclaringType();
		//		return new DeclareNonGalleryFieldEdit( completionStep, declaringType, field );
	}

	private final NamedUserType declaringType;
}
