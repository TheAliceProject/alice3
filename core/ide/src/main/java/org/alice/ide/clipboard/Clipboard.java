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

package org.alice.ide.clipboard;

/**
 * @author Dennis Cosgrove
 */
public enum Clipboard {
	SINGLETON;
	//todo
	private static class ClipboardDropSite implements org.lgna.croquet.DropSite {
		public ClipboardDropSite() {
		}

		public ClipboardDropSite( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			//todo
		}

		@Override
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			//todo
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals( Object obj ) {
			return obj instanceof ClipboardDropSite;
		}

		@Override
		public org.lgna.croquet.DropReceptor getOwningDropReceptor() {
			return Clipboard.SINGLETON.dragComponent.getDropReceptor();
		}
	}

	private static class ClipboardDragModel extends org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel {
		public ClipboardDragModel() {
			super( java.util.UUID.fromString( "d6c25f14-7ed2-4cb8-90dd-f621af830060" ) );
		}

		@Override
		public boolean isAddEventListenerLikeSubstance() {
			org.lgna.project.ast.Node node = Clipboard.SINGLETON.peek();
			if( node instanceof org.lgna.project.ast.Statement ) {
				org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)node;
				return org.lgna.project.ast.AstUtilities.isAddEventListenerMethodInvocationStatement( statement );
			} else {
				return false;
			}
		}

		@Override
		public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
			org.lgna.croquet.DragModel dragModel = step.getModel();
			if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementDragModel ) {
				org.alice.ide.ast.draganddrop.statement.StatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.StatementDragModel)dragModel;
				org.lgna.project.ast.Statement statement = statementDragModel.getStatement();
				boolean isCopy = edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.isQuoteControlUnquoteDown( step.getLatestMouseEvent() );
				if( isCopy ) {
					return CopyToClipboardOperation.getInstance( statement );
				} else {
					return CutToClipboardOperation.getInstance( statement );
				}
			} else {
				//todo?
				return null;
			}
		}
	}

	private final edu.cmu.cs.dennisc.java.util.DStack<org.lgna.project.ast.Node> stack = edu.cmu.cs.dennisc.java.util.Stacks.newStack();
	private final ClipboardDropSite dropSite = new ClipboardDropSite();
	private final ClipboardDragModel dragModel = new ClipboardDragModel();
	private final org.alice.ide.clipboard.components.ClipboardDragComponent dragComponent = new org.alice.ide.clipboard.components.ClipboardDragComponent( dragModel );

	public org.lgna.croquet.views.DragComponent getDragComponent() {
		return this.dragComponent;
	}

	public org.lgna.croquet.DropReceptor getDropReceptor() {
		return this.dragComponent.getDropReceptor();
	}

	public org.lgna.croquet.DropSite getDropSite() {
		return this.dropSite;
	}

	public org.lgna.croquet.DragModel getDragModel() {
		return this.dragModel;
	}

	public boolean isStackEmpty() {
		return ( ( this.stack != null ) && ( this.stack.size() > 0 ) ) == false;
	}

	public int getStackSize() {
		return this.stack.size();
	}

	public org.lgna.project.ast.Node peek() {
		if( this.stack.size() > 0 ) {
			return this.stack.peek();
		} else {
			return null;
		}
	}

	public void push( org.lgna.project.ast.Node node ) {
		this.stack.push( node );
		this.dragComponent.refresh();
	}

	public org.lgna.project.ast.Node pop() {
		org.lgna.project.ast.Node rv = this.stack.pop();
		this.dragComponent.refresh();
		return rv;
	}
}
