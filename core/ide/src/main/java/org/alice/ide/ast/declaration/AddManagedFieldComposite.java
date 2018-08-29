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

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import org.alice.ide.IDE;
import org.alice.ide.ast.ExpressionCreator;
import org.alice.ide.ast.declaration.views.AddManagedFieldView;
import org.alice.ide.croquet.codecs.NodeCodec;
import org.alice.ide.croquet.edits.ast.DeclareFieldEdit;
import org.alice.ide.croquet.edits.ast.DeclareGalleryFieldEdit;
import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.stageide.StageIDE;
import org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptIncludingTypeAndInitializerState;
import org.alice.stageide.sceneeditor.SetUpMethodGenerator;
import org.alice.stageide.sceneeditor.draganddrop.SceneDropSite;
import org.lgna.common.Resource;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;
import org.lgna.story.DurationAnimationStyleSetDimensionPolicyArgumentFactory;
import org.lgna.story.SModel;
import org.lgna.story.SetDepth;
import org.lgna.story.SetDimensionPolicy;
import org.lgna.story.SetHeight;
import org.lgna.story.SetWidth;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.implementation.alice.AliceResourceUtilties;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddManagedFieldComposite extends AddFieldComposite {
	private final InitialPropertyValuesToolPaletteCoreComposite initialPropertyValuesToolPaletteCoreComposite = new InitialPropertyValuesToolPaletteCoreComposite();

	public AddManagedFieldComposite( UUID migrationId, Details details ) {
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
	protected ManagementLevel getManagementLevel() {
		return ManagementLevel.MANAGED;
	}

	@Override
	public UserType<?> getDeclaringType() {
		StageIDE ide = StageIDE.getActiveInstance();
		if( ide != null ) {
			return ide.getSceneType();
		} else {
			return null;
		}
	}

	@Override
	public boolean isValueComponentTypeDisplayed() {
		return super.isValueComponentTypeDisplayed() && IsPromptIncludingTypeAndInitializerState.getInstance().getValue();
	}

	@Override
	public boolean isInitializerDisplayed() {
		return super.isInitializerDisplayed() && IsPromptIncludingTypeAndInitializerState.getInstance().getValue();
	}

	protected static class EditCustomization {
		private final List<Statement> doStatements = Lists.newLinkedList();
		private final List<Statement> undoStatements = Lists.newLinkedList();
		private final List<Resource> resources = Lists.newLinkedList();

		public void addDoStatement( Statement statement ) {
			this.doStatements.add( statement );
		}

		public Statement[] getDoStatements() {
			return ArrayUtilities.createArray( this.doStatements, Statement.class );
		}

		public void addUndoStatement( Statement statement ) {
			this.undoStatements.add( statement );
		}

		public Statement[] getUndoStatements() {
			return ArrayUtilities.createArray( this.undoStatements, Statement.class );
		}

		public void addResource( Resource resource ) {
			this.resources.add( resource );
		}

		public Resource[] getResources() {
			return ArrayUtilities.createArray( this.resources, Resource.class );
		}
	}

	private static class InitialPropertyValueExpressionCustomizer implements ItemStateCustomizer<Expression> {
		private final JavaMethod setter;
		private static final Collection<JavaMethod> setDimensionPolicyMethods = Sets.newHashSet();

		public InitialPropertyValueExpressionCustomizer( JavaMethod setter ) {
			this.setter = setter;
		}

		@Override
		public void prologue( Trigger trigger ) {
		}

		@Override
		public void epilogue() {
		}

		@Override
		public CascadeFillIn getFillInFor( Expression value ) {
			//todo
			return null;
		}

		@Override
		public void appendBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode ) {
			AbstractParameter parameter = this.setter.getRequiredParameters().get( 0 );
			AbstractType<?, ?, ?> valueType = parameter.getValueType();
			ValueDetails<?> valueDetails = parameter.getDetails();

			IDE.getActiveInstance().getExpressionCascadeManager().appendItems( blankChildren, blankNode, valueType, valueDetails );
		}

		public void appendDoStatements( EditCustomization editCustomization, UserField field, Expression expression ) {
			MethodInvocation setterInvocation = SetUpMethodGenerator.createSetterInvocation( false, field, setter, expression );
			if( setDimensionPolicyMethods.size() == 0 ) {
				setDimensionPolicyMethods.add( JavaMethod.getInstance( SModel.class, "setWidth", Number.class, SetWidth.Detail[].class ) );
				setDimensionPolicyMethods.add( JavaMethod.getInstance( SModel.class, "setHeight", Number.class, SetHeight.Detail[].class ) );
				setDimensionPolicyMethods.add( JavaMethod.getInstance( SModel.class, "setDepth", Number.class, SetDepth.Detail[].class ) );
			}
			if( setDimensionPolicyMethods.contains( this.setter ) ) {
				ExpressionCreator expressionCreator = IDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
				JavaMethod policyKeyMethod = JavaMethod.getInstance( DurationAnimationStyleSetDimensionPolicyArgumentFactory.class, "policy", SetDimensionPolicy.class );
				Expression preserveNothingExpression = expressionCreator.createEnumExpression( SetDimensionPolicy.PRESERVE_NOTHING );
				setterInvocation.keyedArguments.add( new JavaKeyedArgument( setterInvocation.method.getValue().getKeyedParameter(), policyKeyMethod, preserveNothingExpression ) );
			}
			editCustomization.addDoStatement( new ExpressionStatement( setterInvocation ) );
		}
	}

	protected AffineMatrix4x4 updateInitialTransformIfNecessary( AffineMatrix4x4 initialTransform ) {
		return initialTransform;
	}

	protected EditCustomization customize( UserActivity userActivity, UserType<?> declaringType, UserField field, EditCustomization rv ) {
		AffineMatrix4x4 initialTransform = null;
		DropSite dropSite = userActivity.findDropSite();
		if( dropSite instanceof SceneDropSite ) {
			SceneDropSite sceneDropSite = (SceneDropSite)dropSite;
			initialTransform = sceneDropSite.getTransform();
		} else {
			AbstractType<?, ?, ?> type = field.getValueType();
			JavaType javaType = type.getFirstEncounteredJavaType();
			Class<?> cls = javaType.getClassReflectionProxy().getReification();
			if( SModel.class.isAssignableFrom( cls ) ) {
				initialTransform = AliceResourceUtilties.getDefaultInitialTransform( AliceResourceClassUtilities.getResourceClassForModelClass( (Class<? extends SModel>)cls ) );
			}
			else {
				initialTransform = null;
			}
		}
		initialTransform = this.updateInitialTransformIfNecessary( initialTransform );
		AbstractSceneEditor sceneEditor = IDE.getActiveInstance().getSceneEditor();
		Statement[] doStatements = sceneEditor.getDoStatementsForAddField( field, initialTransform );
		for( Statement s : doStatements ) {
			rv.addDoStatement( s );
		}
		Statement[] undoStatements = sceneEditor.getUndoStatementsForAddField( field );
		for( Statement s : undoStatements ) {
			rv.addUndoStatement( s );
		}
		for( CustomItemState<Expression> initialPropertyValueExpressionState : this.initialPropertyValuesToolPaletteCoreComposite.getInitialPropertyValueExpressionStates() ) {
			InitialPropertyValueExpressionCustomizer customizer = (InitialPropertyValueExpressionCustomizer)( (InternalCustomItemState<Expression>)initialPropertyValueExpressionState ).getCustomizer();
			customizer.appendDoStatements( rv, field, initialPropertyValueExpressionState.getValue() );
		}
		return rv;
	}

	@Override
	protected DeclareFieldEdit createEdit( UserActivity userActivity, UserType<?> declaringType, UserField field ) {
		EditCustomization customization = new EditCustomization();
		this.customize( userActivity, declaringType, field, customization );
		AbstractSceneEditor sceneEditor = IDE.getActiveInstance().getSceneEditor();
		return new DeclareGalleryFieldEdit( userActivity, sceneEditor, this.getDeclaringType(), field, customization.getDoStatements(), customization.getUndoStatements() );
	}

	protected <T> CustomItemState<Expression> createInitialPropertyValueExpressionState( String keyText, T initialValue, Class<?> declaringCls, String setterName, Class<T> valueCls, Class<?> variableLengthCls ) {
		Method mthd;
		if( variableLengthCls != null ) {
			mthd = ReflectionUtilities.getMethod( declaringCls, setterName, valueCls, variableLengthCls );
		} else {
			mthd = ReflectionUtilities.getMethod( declaringCls, setterName, valueCls );
		}
		JavaMethod method = JavaMethod.getInstance( mthd );
		ExpressionCreator expressionCreator = IDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
		try {
			Expression expression = expressionCreator.createExpression( initialValue );
			CustomItemState<Expression> rv = this.createCustomItemState( keyText, NodeCodec.getInstance( Expression.class ), expression, new InitialPropertyValueExpressionCustomizer( method ) );
			this.initialPropertyValuesToolPaletteCoreComposite.addInitialPropertyValueExpressionState( rv );
			return rv;
		} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
			throw new RuntimeException( ccee );
		}
	}

	@Override
	protected AddManagedFieldView createView() {
		return new AddManagedFieldView( this );
	}
}
