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
package org.alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class FieldInitializerInstanceCreationArgument0State extends org.alice.ide.croquet.models.StandardExpressionState {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.UserField, FieldInitializerInstanceCreationArgument0State> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static FieldInitializerInstanceCreationArgument0State getInstance( org.lgna.project.ast.UserField field ) {
		if( field != null ) {
			org.lgna.project.ast.Expression initializer = field.initializer.getValue();
			if( initializer instanceof org.lgna.project.ast.InstanceCreation ) {
				final org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)initializer;
				org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
				java.util.List<? extends org.lgna.project.ast.AbstractParameter> requiredParameters = constructor.getRequiredParameters();
				if( requiredParameters.size() > 0 ) {
					return map.getInitializingIfAbsent( field, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.UserField, FieldInitializerInstanceCreationArgument0State>() {
						@Override
						public FieldInitializerInstanceCreationArgument0State initialize( org.lgna.project.ast.UserField field ) {
							return new FieldInitializerInstanceCreationArgument0State( field, instanceCreation );
						}
					} );
				}
			}
		}
		return null;
	}

	private final org.lgna.project.ast.UserField field;
	private final org.lgna.project.ast.InstanceCreation instanceCreation;

	private FieldInitializerInstanceCreationArgument0State( org.lgna.project.ast.UserField field, org.lgna.project.ast.InstanceCreation instanceCreation ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, java.util.UUID.fromString( "5743343c-c7e5-43aa-9ccc-51d766f3a683" ), instanceCreation.requiredArguments.get( 0 ).expression.getValue() );
		this.field = field;
		this.instanceCreation = instanceCreation;
	}

	private org.lgna.project.ast.AbstractParameter getRequiredParameter0() {
		return this.instanceCreation.constructor.getValue().getRequiredParameters().get( 0 );
	}

	@Override
	protected org.lgna.project.annotations.ValueDetails<?> getValueDetails() {
		return this.getRequiredParameter0().getDetails();
	}

	@Override
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getType() {
		return this.getRequiredParameter0().getValueType();
	}

	@Override
	protected void handleTruthAndBeautyValueChange( org.lgna.project.ast.Expression nextValue ) {
		super.handleTruthAndBeautyValueChange( nextValue );
		// update AST
		this.instanceCreation.requiredArguments.get( 0 ).expression.setValue( nextValue );

		// update Scene Editor
		org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor();
		org.lgna.story.SJointedModel model = sceneEditor.getInstanceInJavaVMForField( this.field, org.lgna.story.SJointedModel.class );
		org.lgna.story.implementation.JointedModelImp<?, ?> imp = org.lgna.story.EmployeesOnly.getImplementation( model );

		org.lgna.project.virtualmachine.VirtualMachine vm = org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().getVirtualMachine();
		org.lgna.project.virtualmachine.UserInstance userInstance = null;
		Object[] array = vm.ENTRY_POINT_evaluate( userInstance, new org.lgna.project.ast.Expression[] { nextValue } );
		org.lgna.story.resources.JointedModelResource resource = (org.lgna.story.resources.JointedModelResource)array[ 0 ];
		imp.setNewResource( resource );
	}
}
