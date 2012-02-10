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
public class CreateFieldFromPersonResourceOperation extends org.alice.ide.croquet.models.declaration.InitializerManagedFieldDeclarationOperation {
	private static class SingletonHolder {
		private static CreateFieldFromPersonResourceOperation instance = new CreateFieldFromPersonResourceOperation();
	}
	public static CreateFieldFromPersonResourceOperation getInstance() {
		return SingletonHolder.instance;
	}
	private CreateFieldFromPersonResourceOperation() {
		super( 
				java.util.UUID.fromString( "3ac22606-3b37-4b75-9613-89994c873782" ),
				null
		);
	}
	
	@Override
	protected org.alice.ide.croquet.components.declaration.DeclarationPanel< ? > prologue( org.lgna.croquet.history.OperationStep step ) {
		org.lgna.croquet.history.OperationStep subStep = CreatePersonResourceOperation.getInstance().fire();
		if( subStep.containsEphemeralDataFor( PersonResourceOperation.VALUE_KEY ) ) {
			org.lgna.story.resources.sims2.PersonResource personResource = subStep.getEphemeralDataFor( PersonResourceOperation.VALUE_KEY );
			try {
				org.lgna.project.ast.InstanceCreation argumentExpression = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSims2PersonRecourseInstanceCreation( personResource );
				
				org.lgna.project.ast.NamedUserType type = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromPersonResource(personResource);
				
				org.lgna.project.ast.InstanceCreation expression = org.lgna.project.ast.AstUtilities.createInstanceCreation( 
						type.getDeclaredConstructors().get(0), 
//						new Class<?>[] { org.lgna.story.resources.BipedResource.class }, 
						argumentExpression 
				);
				org.alice.ide.croquet.components.declaration.DeclarationPanel< ? > rv = super.prologue( step );				
				this.getInitializerState().setValueTransactionlessly( expression );
				return rv;
			} catch (org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee) {
				throw new RuntimeException( ccee );
			}
		} else {
			return null;
		}
	}
}
