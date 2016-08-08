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
package org.alice.stageide.sceneeditor.side.edits;

/**
 * @author Dennis Cosgrove
 */
public class MarkerColorIdEdit extends org.lgna.croquet.edits.AbstractEdit {
	private final org.lgna.project.ast.UserField field;
	private final org.lgna.project.ast.Expression nextArgumentExpression;
	private transient org.lgna.project.ast.Expression prevArgumentExpression;

	private static final org.lgna.project.ast.JavaMethod SET_COLOR_ID_METHOD = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SMarker.class, "setColorId", org.lgna.story.Color.class );

	public MarkerColorIdEdit( org.lgna.croquet.history.CompletionStep step, org.lgna.project.ast.UserField field, org.lgna.project.ast.Expression nextArgumentExpression ) {
		super( step );
		this.field = field;
		this.nextArgumentExpression = nextArgumentExpression;
	}

	private void set( org.lgna.project.ast.Expression argumentExpression ) {
		org.lgna.project.ast.Statement statement = org.lgna.project.ast.AstUtilities.createMethodInvocationStatement(
				org.lgna.project.ast.AstUtilities.createFieldAccess( new org.lgna.project.ast.ThisExpression(), field ),
				SET_COLOR_ID_METHOD,
				argumentExpression );
		org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor().executeStatements( statement );
	}

	@Override
	protected void doOrRedoInternal( boolean isDo ) {
		if( isDo ) {
			org.alice.stageide.StageIDE ide = org.alice.stageide.StageIDE.getActiveInstance();
			org.lgna.story.SMarker marker = ide.getSceneEditor().getInstanceInJavaVMForField( this.field, org.lgna.story.SMarker.class );
			if( marker != null ) {
				org.lgna.story.Color colorId = marker.getColorId();
				try {
					this.prevArgumentExpression = ide.getApiConfigurationManager().getExpressionCreator().createExpression( colorId );
				} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( ccee, colorId );
					this.prevArgumentExpression = new org.lgna.project.ast.NullLiteral();
				}
			} else {
				this.prevArgumentExpression = new org.lgna.project.ast.NullLiteral();
			}
		}
		this.set( this.nextArgumentExpression );
	}

	@Override
	protected void undoInternal() {
		this.set( this.prevArgumentExpression );
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "change " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.field );
		rv.append( " color id " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.prevArgumentExpression );
		rv.append( " ===> " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.nextArgumentExpression );
	}
}
