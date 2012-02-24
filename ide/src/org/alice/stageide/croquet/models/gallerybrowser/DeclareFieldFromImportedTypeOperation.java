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
package org.alice.stageide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class DeclareFieldFromImportedTypeOperation extends org.lgna.croquet.IteratingOperation {
	private static final org.lgna.croquet.history.Step.Key< Stage > STAGE_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DeclareFieldFromImportedTypeOperation.STAGE_KEY" );
	private static enum Stage {
		REQUESTING_URI {
			@Override
			public Stage getNextStage() {
				return DECLARING_FIELD;
			}
			@Override
			public org.lgna.croquet.Model getModel( org.lgna.croquet.history.OperationStep step ) {
				return TypeFromUriProducer.getInstance();
			}
		},
		DECLARING_FIELD {
			@Override
			public Stage getNextStage() {
				return null;
			}
			@Override
			public org.lgna.croquet.Model getModel( org.lgna.croquet.history.OperationStep step ) {
				org.lgna.croquet.history.TransactionHistory transactionHistory = step.getTransactionHistory();
				org.lgna.croquet.history.Transaction transaction = transactionHistory.getTransactionAt( 0 );
				org.lgna.croquet.history.ValueProducerStep<org.lgna.project.ast.NamedUserType> valueProducerStep = (org.lgna.croquet.history.ValueProducerStep<org.lgna.project.ast.NamedUserType>)transaction.getCompletionStep();
				org.lgna.project.ast.NamedUserType type = valueProducerStep.getModel().getValue( valueProducerStep );
				if( type != null ) {
					org.lgna.project.ast.AbstractConstructor constructor = type.getDeclaredConstructors().get( 0 );
					java.util.ArrayList< ? extends org.lgna.project.ast.AbstractParameter > requiredParameters = constructor.getRequiredParameters();
					org.lgna.croquet.DropSite dropSite = null;
					if( requiredParameters.size() > 0 ) {
						org.lgna.project.ast.AbstractType< ?,?,? > parameterType = requiredParameters.get( 0 ).getValueType();
						return org.alice.ide.croquet.models.gallerybrowser.ResourceCascade.getInstance( parameterType, dropSite );
					} else {
						org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( constructor );
						return org.alice.ide.croquet.models.declaration.ArgumentFieldSpecifiedManagedFieldDeclarationOperation.getInstance( argumentField, dropSite );
					}
				} else {
					return null;
				}
			}
		};
		public abstract Stage getNextStage();
		public abstract org.lgna.croquet.Model getModel( org.lgna.croquet.history.OperationStep step );
	}
	
	private static class SingletonHolder {
		private static DeclareFieldFromImportedTypeOperation instance = new DeclareFieldFromImportedTypeOperation();
	}
	public static DeclareFieldFromImportedTypeOperation getInstance() {
		return SingletonHolder.instance;
	}
	private DeclareFieldFromImportedTypeOperation() {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "d5578a1f-2d43-4d42-802f-62016d82e92b" ) );
	}
	@Override
	protected boolean hasNext( org.lgna.croquet.history.OperationStep step ) {
		Stage nextStage;
		if( step.containsEphemeralDataFor( STAGE_KEY ) ) {
			Stage prevStage = step.getEphemeralDataFor( STAGE_KEY );
			nextStage = prevStage.getNextStage();
		} else {
			nextStage = Stage.REQUESTING_URI;
		}
		step.putEphemeralDataFor( STAGE_KEY, nextStage );
		return nextStage != null;
	}
	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.OperationStep step ) {
		Stage stage = step.getEphemeralDataFor( STAGE_KEY );
		if( stage != null ) {
			return stage.getModel( step );
		} else {
			return null;
		}
	}
}
