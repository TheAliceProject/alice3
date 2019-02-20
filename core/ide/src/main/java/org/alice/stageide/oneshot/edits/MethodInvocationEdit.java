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
package org.alice.stageide.oneshot.edits;

import org.alice.ide.IDE;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;

/**
 * @author Dennis Cosgrove
 */
public abstract class MethodInvocationEdit extends AbstractEdit {
	private final InstanceFactory instanceFactory;
	private final AbstractMethod method;
	private final Expression[] argumentExpressions;

	public MethodInvocationEdit( UserActivity userActivity, InstanceFactory instanceFactory, AbstractMethod method, Expression[] argumentExpressions ) {
		super( userActivity );
		this.instanceFactory = instanceFactory;
		this.method = method;
		this.argumentExpressions = argumentExpressions;
	}

	protected abstract void preserveUndoInfo( Object instance, boolean isDo );

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		IDE ide = IDE.getActiveInstance();
		AbstractSceneEditor sceneEditor = ide.getSceneEditor();
		Object instance = sceneEditor.getInstanceInJavaVMForExpression( this.instanceFactory.createExpression() );
		this.preserveUndoInfo( instance, isDo );
		Expression methodInvocation = AstUtilities.createMethodInvocation(
				this.instanceFactory.createExpression(),
				method,
				argumentExpressions
				);
		ExpressionStatement statement = new ExpressionStatement( methodInvocation );
		sceneEditor.executeStatements( statement );
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "one shot " );
		rv.append( this.method );
	}
}
