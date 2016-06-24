/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public final class InstanceCreationFillInWithPredeterminedFieldAccessArgument extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks<org.lgna.project.ast.InstanceCreation> {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.lgna.project.ast.AbstractConstructor, org.lgna.project.ast.AbstractField, InstanceCreationFillInWithPredeterminedFieldAccessArgument> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static synchronized InstanceCreationFillInWithPredeterminedFieldAccessArgument getInstance( org.lgna.project.ast.AbstractConstructor constructor, org.lgna.project.ast.AbstractField field ) {
		return mapToMap.getInitializingIfAbsent( constructor, field, new edu.cmu.cs.dennisc.map.MapToMap.Initializer<org.lgna.project.ast.AbstractConstructor, org.lgna.project.ast.AbstractField, InstanceCreationFillInWithPredeterminedFieldAccessArgument>() {
			@Override
			public InstanceCreationFillInWithPredeterminedFieldAccessArgument initialize( org.lgna.project.ast.AbstractConstructor constructor, org.lgna.project.ast.AbstractField field ) {
				return new InstanceCreationFillInWithPredeterminedFieldAccessArgument( constructor, field );
			}
		} );
	}

	private final org.lgna.project.ast.AbstractField field;
	private final org.lgna.project.ast.InstanceCreation transientValue;

	private static org.lgna.project.ast.InstanceCreation createInstanceCreation( org.lgna.project.ast.AbstractConstructor constructor, org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.InstanceCreation rv = new org.lgna.project.ast.InstanceCreation( constructor );
		rv.requiredArguments.add( new org.lgna.project.ast.SimpleArgument( constructor.getRequiredParameters().get( 0 ), new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.TypeExpression( field.getDeclaringType() ), field ) ) );
		return rv;
	}

	private InstanceCreationFillInWithPredeterminedFieldAccessArgument( org.lgna.project.ast.AbstractConstructor constructor, org.lgna.project.ast.AbstractField field ) {
		super( java.util.UUID.fromString( "72f4ff66-0652-4682-8e2e-84298732128b" ) );
		this.field = field;
		this.transientValue = createInstanceCreation( constructor, field );
	}

	@Override
	public org.lgna.project.ast.InstanceCreation createValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.InstanceCreation, Void> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return createInstanceCreation( this.transientValue.constructor.getValue(), this.field );
	}

	@Override
	public org.lgna.project.ast.InstanceCreation getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.InstanceCreation, Void> node ) {
		return this.transientValue;
	}
}
