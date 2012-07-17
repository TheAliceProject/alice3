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
package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddManagedFieldComposite extends AddFieldComposite {
	private final org.lgna.croquet.BooleanState initialPropertyValuesExpandedState = this.createBooleanState( this.createKey( "initialPropertyValuesExpandedState" ), true );
	private java.util.List<org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression>> initialPropertyValueExpressionStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	public AddManagedFieldComposite( java.util.UUID migrationId, Details details ) {
		super( migrationId, details );
	}
	public org.lgna.croquet.BooleanState getInitialPropertyValuesExpandedState() {
		return this.initialPropertyValuesExpandedState;
	}
	public java.util.List<org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression>> getInitialPropertyValueExpressionStates() {
		return this.initialPropertyValueExpressionStates;
	}
	
	@Override
	protected boolean isFieldFinal() {
		return false;
	}
	@Override
	protected org.lgna.project.ast.ManagementLevel getManagementLevel() {
		return org.lgna.project.ast.ManagementLevel.MANAGED;
	}
	@Override
	public org.lgna.project.ast.UserType<?> getDeclaringType() {
		return org.alice.ide.IDE.getActiveInstance().getSceneType();
	}
	protected static class EditCustomization {
		private final java.util.List< org.lgna.project.ast.Statement > doStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private final java.util.List< org.lgna.project.ast.Statement > undoStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private final java.util.List< org.lgna.common.Resource > resources = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		public void addDoStatement( org.lgna.project.ast.Statement statement ) {
			this.doStatements.add( statement );
		}
		public org.lgna.project.ast.Statement[] getDoStatements() {
			return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.doStatements, org.lgna.project.ast.Statement.class );
		}
		public void addUndoStatement( org.lgna.project.ast.Statement statement ) {
			this.undoStatements.add( statement );
		}
		public org.lgna.project.ast.Statement[] getUndoStatements() {
			return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.undoStatements, org.lgna.project.ast.Statement.class );
		}
		public void addResource( org.lgna.common.Resource resource ) {
			this.resources.add( resource );
		}
		public org.lgna.common.Resource[] getResources() {
			return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.resources, org.lgna.common.Resource.class );
		}
	}

	
	protected EditCustomization customize( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field, EditCustomization rv ) {
		rv.addDoStatement(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetVehicleStatement( field, null, true));
		rv.addUndoStatement(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetVehicleStatement( field, null, false));
		return rv;
	}
	@Override
	protected org.alice.ide.croquet.edits.ast.DeclareFieldEdit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep, org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field ) {
		EditCustomization customization = new EditCustomization();
		this.customize( completionStep, declaringType, field, customization );
		return new org.alice.ide.croquet.edits.ast.DeclareGalleryFieldEdit( completionStep, this.getDeclaringType(), field, customization.getDoStatements(), customization.getUndoStatements() );
	}

	private static class ExpressionCustomizer implements ItemStateCustomizer<org.lgna.project.ast.Expression> {
		private final org.lgna.project.ast.AbstractType<?,?,?> type;
		private org.lgna.project.annotations.ValueDetails valueDetails;
		public ExpressionCustomizer( org.lgna.project.ast.AbstractType<?,?,?> type, org.lgna.project.annotations.ValueDetails valueDetails ) {
			this.type = type;
			this.valueDetails = valueDetails;
		}
		public org.lgna.croquet.CascadeFillIn getFillInFor( org.lgna.project.ast.Expression value ) {
			//todo
			return null;
		}
		public void appendBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
			org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().appendItems( rv, blankNode, this.type, this.valueDetails );
		}
	}	
	protected org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> createExpressionState( Key key, org.lgna.project.ast.Expression initialValue, org.lgna.project.ast.AbstractType<?,?,?> type, org.lgna.project.annotations.ValueDetails valueDetails ) {
		org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> rv = this.createCustomItemState( key, org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Expression.class ), initialValue, new ExpressionCustomizer( type, valueDetails ) );
		this.initialPropertyValueExpressionStates.add( rv );
		return rv;
	}
	protected org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> createExpressionState( Key key, org.lgna.project.ast.Expression initialValue, org.lgna.project.ast.AbstractType<?,?,?> type ) {
		return this.createExpressionState( key, initialValue, type, null );
	}
	protected <T> org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> createExpressionState( Key key, T initialValue, Class<T> cls, org.lgna.project.annotations.ValueDetails<T> valueDetails ) {
		org.alice.ide.ast.ExpressionCreator expressionCreator = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
		try {
			org.lgna.project.ast.Expression expression = expressionCreator.createExpression( initialValue );
			return this.createExpressionState( key, expression, org.lgna.project.ast.JavaType.getInstance( cls ), valueDetails );
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			throw new RuntimeException( ccee );
		}
	}	
	protected <T> org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> createExpressionState( Key key, T initialValue, Class<T> cls ) {
		return this.createExpressionState( key, initialValue, cls, null );
	}	
	@Override
	protected org.alice.ide.ast.declaration.views.AddManagedFieldView createView() {
		return new org.alice.ide.ast.declaration.views.AddManagedFieldView( this );
	}
}
