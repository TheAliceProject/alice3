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

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.ide.croquet.edits.ast.DeclareNonGalleryFieldEdit;
import org.alice.ide.name.validators.FieldNameValidator;
import org.alice.stageide.ast.ExpressionCreator;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.OperationInputDialogCoreComposite;
import org.lgna.croquet.StringState;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.poser.view.NameYourPoseView;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;

/**
 * @author Matt May
 */
public class NameYourPoseComposite extends OperationInputDialogCoreComposite<NameYourPoseView> {

	private FieldNameValidator validator;
	private PoserControlComposite parent;
	private StringState poseName = createStringState( createKey( "poseName" ) );

	public NameYourPoseComposite( PoserControlComposite parent ) {
		super( java.util.UUID.fromString( "4c5873b3-7c3b-440f-8511-0203fd145c7f" ), null );
		this.parent = parent;
	}

	@Override
	protected NameYourPoseView createView() {
		return new NameYourPoseView( this );
	}

	public StringState getPoseName() {
		return this.poseName;
	}

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		if( validator != null ) {
			//pass
		} else {
			this.validator = new FieldNameValidator( parent.getIkPoser().getDeclaringType() );
		}
		ErrorStatus errorStatus = this.createErrorStatus( this.createKey( "errorStatus" ) );
		String candidate = poseName.getValue();
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
	protected DeclareNonGalleryFieldEdit createEdit( CompletionStep<?> completionStep ) {
		UserField field = createPoseField( poseName.getValue() );
		boolean IS_READY_FOR_PRIME_TIME = false;
		if( IS_READY_FOR_PRIME_TIME ) {
			UserType<?> declaringType = this.parent.getIkPoser().getDeclaringType();
			return new DeclareNonGalleryFieldEdit( completionStep, declaringType, field );

		} else {
			org.lgna.project.ast.NamedUserType bogusType = new org.lgna.project.ast.NamedUserType();
			bogusType.name.setValue( "Bogus" );
			bogusType.superType.setValue( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SBiped.class ) );
			bogusType.fields.add( field );
			String javaCode = bogusType.generateJavaCode( true );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( javaCode );
			return null;
		}
	}

	private UserField createPoseField( String value ) {
		try {
			Pose pose = parent.ikPoser.getPose();
			Expression rhSide = new ExpressionCreator().createExpression( pose );
			UserField rv = new UserField( "name", JavaType.getInstance( Pose.class ), rhSide );
			return rv;
		} catch( CannotCreateExpressionException e ) {
			throw new CancelException();
		}
	}

}
