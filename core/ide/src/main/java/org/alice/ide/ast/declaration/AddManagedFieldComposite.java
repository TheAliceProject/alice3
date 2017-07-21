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
package org.alice.ide.ast.declaration;

import org.lgna.story.SetDimensionPolicy;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddManagedFieldComposite extends AddFieldComposite {
	private final InitialPropertyValuesToolPaletteCoreComposite initialPropertyValuesToolPaletteCoreComposite = new InitialPropertyValuesToolPaletteCoreComposite();

	public AddManagedFieldComposite( java.util.UUID migrationId, Details details ) {
		super( migrationId, details );
	}

	public InitialPropertyValuesToolPaletteCoreComposite getInitialPropertyValuesToolPaletteCoreComposite() {
		return this.initialPropertyValuesToolPaletteCoreComposite;
	}

	@Override
	protected boolean isFieldFinal() {
		return true;
	}

	@Override
	protected org.lgna.project.ast.ManagementLevel getManagementLevel() {
		return org.lgna.project.ast.ManagementLevel.MANAGED;
	}

	@Override
	public org.lgna.project.ast.UserType<?> getDeclaringType() {
		org.alice.stageide.StageIDE ide = org.alice.stageide.StageIDE.getActiveInstance();
		if( ide != null ) {
			return ide.getSceneType();
		} else {
			return null;
		}
	}

	@Override
	public boolean isValueComponentTypeDisplayed() {
		return super.isValueComponentTypeDisplayed() && org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptIncludingTypeAndInitializerState.getInstance().getValue();
	}

	@Override
	public boolean isInitializerDisplayed() {
		return super.isInitializerDisplayed() && org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptIncludingTypeAndInitializerState.getInstance().getValue();
	}

	protected static class EditCustomization {
		private final java.util.List<org.lgna.project.ast.Statement> doStatements = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private final java.util.List<org.lgna.project.ast.Statement> undoStatements = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private final java.util.List<org.lgna.common.Resource> resources = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

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

	private static class InitialPropertyValueExpressionCustomizer implements ItemStateCustomizer<org.lgna.project.ast.Expression> {
		private final org.lgna.project.ast.JavaMethod setter;
		private static final java.util.Collection<org.lgna.project.ast.JavaMethod> setDimensionPolicyMethods = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();

		public InitialPropertyValueExpressionCustomizer( org.lgna.project.ast.JavaMethod setter ) {
			this.setter = setter;
		}

		@Override
		public void prologue( org.lgna.croquet.triggers.Trigger trigger ) {
		}

		@Override
		public void epilogue() {
		}

		@Override
		public org.lgna.croquet.CascadeFillIn getFillInFor( org.lgna.project.ast.Expression value ) {
			//todo
			return null;
		}

		@Override
		public void appendBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
			org.lgna.project.ast.AbstractParameter parameter = this.setter.getRequiredParameters().get( 0 );
			org.lgna.project.ast.AbstractType<?, ?, ?> valueType = parameter.getValueType();
			org.lgna.project.annotations.ValueDetails<?> valueDetails = parameter.getDetails();

			org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().appendItems( blankChildren, blankNode, valueType, valueDetails );
		}

		public void appendDoStatements( EditCustomization editCustomization, org.lgna.project.ast.UserField field, org.lgna.project.ast.Expression expression ) {
			org.lgna.project.ast.MethodInvocation setterInvocation = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetterInvocation( false, field, setter, expression );
			if( setDimensionPolicyMethods.size() == 0 ) {
				setDimensionPolicyMethods.add( org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SModel.class, "setWidth", Number.class, org.lgna.story.SetWidth.Detail[].class ) );
				setDimensionPolicyMethods.add( org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SModel.class, "setHeight", Number.class, org.lgna.story.SetHeight.Detail[].class ) );
				setDimensionPolicyMethods.add( org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SModel.class, "setDepth", Number.class, org.lgna.story.SetDepth.Detail[].class ) );
			}
			if( setDimensionPolicyMethods.contains( this.setter ) ) {
				org.alice.ide.ast.ExpressionCreator expressionCreator = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
				org.lgna.project.ast.JavaMethod policyKeyMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.DurationAnimationStyleSetDimensionPolicyArgumentFactory.class, "policy", SetDimensionPolicy.class );
				org.lgna.project.ast.Expression preserveNothingExpression = expressionCreator.createEnumExpression( SetDimensionPolicy.PRESERVE_NOTHING );
				setterInvocation.keyedArguments.add( new org.lgna.project.ast.JavaKeyedArgument( setterInvocation.method.getValue().getKeyedParameter(), policyKeyMethod, preserveNothingExpression ) );
			}
			editCustomization.addDoStatement( new org.lgna.project.ast.ExpressionStatement( setterInvocation ) );
		}
	}

	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 updateInitialTransformIfNecessary( edu.cmu.cs.dennisc.math.AffineMatrix4x4 initialTransform ) {
		return initialTransform;
	}

	protected EditCustomization customize( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field, EditCustomization rv ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 initialTransform = null;
		org.lgna.croquet.DropSite dropSite = step.findDropSite();
		if( dropSite instanceof org.alice.stageide.sceneeditor.draganddrop.SceneDropSite ) {
			org.alice.stageide.sceneeditor.draganddrop.SceneDropSite sceneDropSite = (org.alice.stageide.sceneeditor.draganddrop.SceneDropSite)dropSite;
			initialTransform = sceneDropSite.getTransform();
		} else {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = field.getValueType();
			org.lgna.project.ast.JavaType javaType = type.getFirstEncounteredJavaType();
			Class<?> cls = javaType.getClassReflectionProxy().getReification();
			if( org.lgna.story.SModel.class.isAssignableFrom( cls ) ) {
				initialTransform = org.lgna.story.implementation.alice.AliceResourceUtilties.getDefaultInitialTransform( org.lgna.story.implementation.alice.AliceResourceClassUtilities.getResourceClassForModelClass( (Class<? extends org.lgna.story.SModel>)cls ) );
			}
			else {
				initialTransform = null;
			}
		}
		initialTransform = this.updateInitialTransformIfNecessary( initialTransform );
		org.alice.ide.sceneeditor.AbstractSceneEditor sceneEditor = org.alice.ide.IDE.getActiveInstance().getSceneEditor();
		org.lgna.project.ast.Statement[] doStatements = sceneEditor.getDoStatementsForAddField( field, initialTransform );
		for( org.lgna.project.ast.Statement s : doStatements ) {
			rv.addDoStatement( s );
		}
		org.lgna.project.ast.Statement[] undoStatements = sceneEditor.getUndoStatementsForAddField( field );
		for( org.lgna.project.ast.Statement s : undoStatements ) {
			rv.addUndoStatement( s );
		}
		for( org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> initialPropertyValueExpressionState : this.initialPropertyValuesToolPaletteCoreComposite.getInitialPropertyValueExpressionStates() ) {
			InitialPropertyValueExpressionCustomizer customizer = (InitialPropertyValueExpressionCustomizer)( (InternalCustomItemState<org.lgna.project.ast.Expression>)initialPropertyValueExpressionState ).getCustomizer();
			customizer.appendDoStatements( rv, field, initialPropertyValueExpressionState.getValue() );
		}
		return rv;
	}

	@Override
	protected org.alice.ide.croquet.edits.ast.DeclareFieldEdit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep, org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field ) {
		EditCustomization customization = new EditCustomization();
		this.customize( completionStep, declaringType, field, customization );
		org.alice.ide.sceneeditor.AbstractSceneEditor sceneEditor = org.alice.ide.IDE.getActiveInstance().getSceneEditor();
		return new org.alice.ide.croquet.edits.ast.DeclareGalleryFieldEdit( completionStep, sceneEditor, this.getDeclaringType(), field, customization.getDoStatements(), customization.getUndoStatements() );
	}

	protected <T> org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> createInitialPropertyValueExpressionState( String keyText, T initialValue, Class<?> declaringCls, String setterName, Class<T> valueCls, Class<?> variableLengthCls ) {
		java.lang.reflect.Method mthd;
		if( variableLengthCls != null ) {
			mthd = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( declaringCls, setterName, valueCls, variableLengthCls );
		} else {
			mthd = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( declaringCls, setterName, valueCls );
		}
		org.lgna.project.ast.JavaMethod method = org.lgna.project.ast.JavaMethod.getInstance( mthd );
		org.alice.ide.ast.ExpressionCreator expressionCreator = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
		try {
			org.lgna.project.ast.Expression expression = expressionCreator.createExpression( initialValue );
			org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> rv = this.createCustomItemState( keyText, org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Expression.class ), expression, new InitialPropertyValueExpressionCustomizer( method ) );
			this.initialPropertyValuesToolPaletteCoreComposite.addInitialPropertyValueExpressionState( rv );
			return rv;
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			throw new RuntimeException( ccee );
		}
	}

	@Override
	protected org.alice.ide.ast.declaration.views.AddManagedFieldView createView() {
		return new org.alice.ide.ast.declaration.views.AddManagedFieldView( this );
	}
}
