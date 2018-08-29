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
package org.alice.stageide.ast.declaration;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.AddManagedFieldComposite;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.cascade.ExpressionCascadeContext;
import org.alice.ide.typemanager.TypeManager;
import org.alice.stageide.personresource.PersonResourceComposite;
import org.lgna.croquet.AbstractComposite;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.ValueConverter;
import org.lgna.croquet.ValueCreator;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.NamedUserType;

import java.awt.Dimension;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class AddPersonResourceManagedFieldComposite extends AddManagedFieldComposite {
	private static class SingletonHolder {
		private static AddPersonResourceManagedFieldComposite instance = new AddPersonResourceManagedFieldComposite();
	}

	public static AddPersonResourceManagedFieldComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static AbstractType<?, ?, ?> getDeclaringTypeFromInitializer( Expression expression ) {
		if( expression instanceof InstanceCreation ) {
			InstanceCreation instanceCreation = (InstanceCreation)expression;
			return instanceCreation.constructor.getValue().getDeclaringType();
		} else {
			return null;
		}
	}

	private InstanceCreation initialInstanceCreation;

	private AddPersonResourceManagedFieldComposite() {
		super( UUID.fromString( "a3484b6d-bee1-40b6-9162-a8066bd9b015" ), new FieldDetailsBuilder()
				.valueComponentType( ApplicabilityStatus.DISPLAYED, null )
				.valueIsArrayType( ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, false )
				.initializer( ApplicabilityStatus.EDITABLE, null )
				.build() );
	}

	private static InstanceCreation createPersonInstanceCreationFromPersonResourceInstanceCreation( InstanceCreation personResourceInstanceCreation ) {
		if( personResourceInstanceCreation != null ) {
			NamedUserType type = TypeManager.getNamedUserTypeFromPersonResourceInstanceCreation( personResourceInstanceCreation );

			return AstUtilities.createInstanceCreation(
					type.getDeclaredConstructors().get( 0 ),
					personResourceInstanceCreation
					);
		} else {
			return null;
		}
	}

	public void setInitialPersonResourceInstanceCreation( InstanceCreation argumentExpression ) {
		this.initialInstanceCreation = createPersonInstanceCreationFromPersonResourceInstanceCreation( argumentExpression );
	}

	@Override
	protected Expression getInitializerInitialValue() {
		return this.initialInstanceCreation;
	}

	@Override
	protected AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
		return getDeclaringTypeFromInitializer( this.getInitializer() );
	}

	private static final class PersonResourceToPersonConverter extends ValueConverter<InstanceCreation, InstanceCreation> {
		public PersonResourceToPersonConverter( ValueCreator<InstanceCreation> valueCreator ) {
			super( UUID.fromString( "f04fd1b1-24a6-444d-af92-b885e18a3887" ), valueCreator );
		}

		@Override
		protected InstanceCreation convert( InstanceCreation value ) {
			return createPersonInstanceCreationFromPersonResourceInstanceCreation( value );
		}
	}

	private final ValueConverter<InstanceCreation, InstanceCreation> previousResourceExpressionValueConverter = new PersonResourceToPersonConverter( PersonResourceComposite.getInstance().getPreviousResourceExpressionValueConverter() );

	private class InitializerContext implements ExpressionCascadeContext {
		@Override
		public Expression getPreviousExpression() {
			//todo: investigate
			Expression initializer = getInitializer();
			if( initializer instanceof InstanceCreation ) {
				InstanceCreation instanceCreation = (InstanceCreation)initializer;
				if( instanceCreation.requiredArguments.size() == 1 ) {
					return instanceCreation.requiredArguments.get( 0 ).expression.getValue();
				}
			}
			return null;
		}

		@Override
		public BlockStatementIndexPair getBlockStatementIndexPair() {
			return null;
		}
	}

	private class PersonResourceInitializerCustomizer implements ItemStateCustomizer<Expression> {
		private ExpressionCascadeContext pushedContext;

		@Override
		public CascadeFillIn getFillInFor( Expression value ) {
			return null;
		}

		@Override
		public void prologue( Trigger trigger ) {
			this.pushedContext = new InitializerContext();
			IDE.getActiveInstance().getExpressionCascadeManager().pushContext( this.pushedContext );
		}

		@Override
		public void epilogue() {
			IDE.getActiveInstance().getExpressionCascadeManager().popAndCheckContext( this.pushedContext );
			this.pushedContext = null;
		}

		@Override
		public void appendBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode ) {
			blankChildren.add( previousResourceExpressionValueConverter.getFillIn() );
		}
	}

	@Override
	protected Dimension calculateWindowSize( AbstractWindow<?> window ) {
		return DimensionUtilities.constrainToMaximumHeight( super.calculateWindowSize( window ), 400 );
	}

	@Override
	protected AbstractComposite.ItemStateCustomizer<Expression> createInitializerCustomizer() {
		return new PersonResourceInitializerCustomizer();
	}

	@Override
	protected void handlePostHideDialog() {
		super.handlePostHideDialog();
		this.initialInstanceCreation = null;
	}
}
