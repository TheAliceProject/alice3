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
package org.alice.ide.x.croquet.edits;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import org.alice.ide.croquet.edits.ast.ExpressionPropertyEdit;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionProperty;
import org.lgna.project.ast.UserField;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.project.virtualmachine.VirtualMachine;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointedModelResource;

/**
 * @author Dennis Cosgrove
 */
public class SceneEditorUpdatingExpressionPropertyEdit extends ExpressionPropertyEdit {
	private final UserField field;

	public SceneEditorUpdatingExpressionPropertyEdit( UserActivity userActivity, ExpressionProperty expressionProperty, Expression prevExpression, Expression nextExpression, UserField field ) {
		super( userActivity, expressionProperty, prevExpression, nextExpression );
		this.field = field;
	}

	public SceneEditorUpdatingExpressionPropertyEdit( BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.field = null; //todo
	}

	@Override
	protected void setValue( Expression expression ) {
		super.setValue( expression );
		// update Scene Editor
		StorytellingSceneEditor sceneEditor = StageIDE.getActiveInstance().getSceneEditor();
		SJointedModel model = sceneEditor.getInstanceInJavaVMForField( this.field, SJointedModel.class );
		JointedModelImp<?, ?> imp = EmployeesOnly.getImplementation( model );

		VirtualMachine vm = StorytellingSceneEditor.getInstance().getVirtualMachine();
		UserInstance userInstance = null;
		Object[] array = vm.ENTRY_POINT_evaluate( userInstance, new Expression[] { expression } );
		JointedModelResource resource = (JointedModelResource)array[ 0 ];
		imp.setNewResource( resource );
	}

}
