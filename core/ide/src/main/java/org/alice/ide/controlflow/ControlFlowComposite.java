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

package org.alice.ide.controlflow;

/**
 * @author Dennis Cosgrove
 */
public class ControlFlowComposite extends org.lgna.croquet.SimpleComposite<org.alice.ide.controlflow.components.ControlFlowPanel> {

	private static java.util.Map<org.lgna.project.ast.AbstractCode, ControlFlowComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized ControlFlowComposite getInstance( org.lgna.project.ast.AbstractCode code ) {
		ControlFlowComposite rv = map.get( code );
		if( rv != null ) {
			//pass
		} else {
			rv = new ControlFlowComposite( code );
			map.put( code, rv );
		}
		return rv;
	}

	private final java.util.List<org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel> models = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final org.lgna.project.ast.AbstractCode code;

	private ControlFlowComposite( org.lgna.project.ast.AbstractCode code ) {
		super( java.util.UUID.fromString( "27ff6dfc-2519-4378-bb4f-d8c2c2fb19e9" ) );
		this.code = code;
	}

	@Override
	public boolean contains( org.lgna.croquet.Model model ) {
		if( super.contains( model ) ) {
			return true;
		} else {
			return this.models.contains( model );
		}
	}

	public java.util.List<org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel> getModels() {
		return this.models;
	}

	@Override
	protected org.alice.ide.controlflow.components.ControlFlowPanel createView() {
		return new org.alice.ide.controlflow.components.ControlFlowPanel( this );
	}

	@Override
	protected void initialize() {
		super.initialize();

		java.util.Collections.addAll( this.models,
				org.alice.ide.ast.draganddrop.statement.DoInOrderTemplateDragModel.getInstance(),
				null,
				org.alice.ide.ast.draganddrop.statement.CountLoopTemplateDragModel.getInstance(),
				org.alice.ide.ast.draganddrop.statement.WhileLoopTemplateDragModel.getInstance(),
				org.alice.ide.ast.draganddrop.statement.ForEachInArrayLoopTemplateDragModel.getInstance(),
				null,
				org.alice.ide.ast.draganddrop.statement.ConditionalStatementTemplateDragModel.getInstance(),
				null,
				org.alice.ide.ast.draganddrop.statement.DoTogetherTemplateDragModel.getInstance(),
				org.alice.ide.ast.draganddrop.statement.EachInArrayTogetherTemplateDragModel.getInstance(),
				null,
				org.alice.ide.ast.draganddrop.statement.DeclareLocalDragModel.getInstance(),
				org.alice.ide.ast.draganddrop.statement.AssignmentTemplateDragModel.getInstance(),
				null,
				org.alice.ide.ast.draganddrop.statement.CommentTemplateDragModel.getInstance()
				);
		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( method.getReturnType() == org.lgna.project.ast.JavaType.VOID_TYPE ) {
				//pass
			} else {
				this.models.add( null );
				this.models.add( org.alice.ide.ast.draganddrop.statement.ReturnStatementTemplateDragModel.getInstance( method ) );
			}
		}
	}
}
