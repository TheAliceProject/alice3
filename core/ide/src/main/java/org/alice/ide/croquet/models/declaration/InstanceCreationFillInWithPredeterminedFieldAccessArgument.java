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

import edu.cmu.cs.dennisc.map.MapToMap;
import org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.TypeExpression;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class InstanceCreationFillInWithPredeterminedFieldAccessArgument extends ExpressionFillInWithoutBlanks<InstanceCreation> {
	private static MapToMap<AbstractConstructor, AbstractField, InstanceCreationFillInWithPredeterminedFieldAccessArgument> mapToMap = MapToMap.newInstance();

	public static synchronized InstanceCreationFillInWithPredeterminedFieldAccessArgument getInstance( AbstractConstructor constructor, AbstractField field ) {
		return mapToMap.getInitializingIfAbsent( constructor, field, new MapToMap.Initializer<AbstractConstructor, AbstractField, InstanceCreationFillInWithPredeterminedFieldAccessArgument>() {
			@Override
			public InstanceCreationFillInWithPredeterminedFieldAccessArgument initialize( AbstractConstructor constructor, AbstractField field ) {
				return new InstanceCreationFillInWithPredeterminedFieldAccessArgument( constructor, field );
			}
		} );
	}

	private final AbstractField field;
	private final InstanceCreation transientValue;

	private static InstanceCreation createInstanceCreation( AbstractConstructor constructor, AbstractField field ) {
		InstanceCreation rv = new InstanceCreation( constructor );
		rv.requiredArguments.add( new SimpleArgument( constructor.getRequiredParameters().get( 0 ), new FieldAccess( new TypeExpression( field.getDeclaringType() ), field ) ) );
		return rv;
	}

	private InstanceCreationFillInWithPredeterminedFieldAccessArgument( AbstractConstructor constructor, AbstractField field ) {
		super( UUID.fromString( "72f4ff66-0652-4682-8e2e-84298732128b" ) );
		this.field = field;
		this.transientValue = createInstanceCreation( constructor, field );
	}

	@Override
	public InstanceCreation createValue( ItemNode<? super InstanceCreation, Void> node ) {
		return createInstanceCreation( this.transientValue.constructor.getValue(), this.field );
	}

	@Override
	public InstanceCreation getTransientValue( ItemNode<? super InstanceCreation, Void> node ) {
		return this.transientValue;
	}
}
