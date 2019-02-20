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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.draganddrop.statement.AssignmentTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.CommentTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.ConditionalStatementTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.CountLoopTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.DeclareLocalDragModel;
import org.alice.ide.ast.draganddrop.statement.DoInOrderTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.DoTogetherTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.EachInArrayTogetherTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.ForEachInArrayLoopTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.ReturnStatementTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.WhileLoopTemplateDragModel;
import org.alice.ide.controlflow.components.ControlFlowPanel;
import org.lgna.croquet.Model;
import org.lgna.croquet.SimpleComposite;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.UserMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ControlFlowComposite extends SimpleComposite<ControlFlowPanel> {

	private static Map<AbstractCode, ControlFlowComposite> map = Maps.newHashMap();

	public static synchronized ControlFlowComposite getInstance( AbstractCode code ) {
		ControlFlowComposite rv = map.get( code );
		if( rv != null ) {
			//pass
		} else {
			rv = new ControlFlowComposite( code );
			map.put( code, rv );
		}
		return rv;
	}

	private final List<StatementTemplateDragModel> models = Lists.newLinkedList();
	private final AbstractCode code;

	private ControlFlowComposite( AbstractCode code ) {
		super( UUID.fromString( "27ff6dfc-2519-4378-bb4f-d8c2c2fb19e9" ) );
		this.code = code;
	}

	@Override
	public boolean contains( Model model ) {
		if( super.contains( model ) ) {
			return true;
		} else {
			return this.models.contains( model );
		}
	}

	public List<StatementTemplateDragModel> getModels() {
		return this.models;
	}

	@Override
	protected ControlFlowPanel createView() {
		return new ControlFlowPanel( this );
	}

	@Override
	protected void initialize() {
		super.initialize();

		Collections.addAll( this.models,
				DoInOrderTemplateDragModel.getInstance(),
				null,
				CountLoopTemplateDragModel.getInstance(),
				WhileLoopTemplateDragModel.getInstance(),
				ForEachInArrayLoopTemplateDragModel.getInstance(),
				null,
				ConditionalStatementTemplateDragModel.getInstance(),
				null,
				DoTogetherTemplateDragModel.getInstance(),
				EachInArrayTogetherTemplateDragModel.getInstance(),
				null,
				DeclareLocalDragModel.getInstance(),
				AssignmentTemplateDragModel.getInstance(),
				null,
				CommentTemplateDragModel.getInstance()
				);
		if( code instanceof UserMethod ) {
			UserMethod method = (UserMethod)code;
			if( method.getReturnType() == JavaType.VOID_TYPE ) {
				//pass
			} else {
				this.models.add( null );
				this.models.add( ReturnStatementTemplateDragModel.getInstance( method ) );
			}
		}
	}
}
