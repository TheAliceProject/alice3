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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities;
import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Stacks;
import org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel;
import org.alice.ide.ast.draganddrop.statement.StatementDragModel;
import org.alice.ide.clipboard.components.ClipboardDragComponent;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.views.DragComponent;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.Statement;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public enum Clipboard {
	SINGLETON;
	//todo
	private static class ClipboardDropSite implements DropSite {
		public ClipboardDropSite() {
		}

		public ClipboardDropSite( BinaryDecoder binaryDecoder ) {
			//todo
		}

		@Override
		public void encode( BinaryEncoder binaryEncoder ) {
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
		public DropReceptor getOwningDropReceptor() {
			return Clipboard.SINGLETON.dragComponent.getDropReceptor();
		}
	}

	private static class ClipboardDragModel extends AbstractStatementDragModel {
		public ClipboardDragModel() {
			super( UUID.fromString( "d6c25f14-7ed2-4cb8-90dd-f621af830060" ) );
		}

		@Override
		public boolean isAddEventListenerLikeSubstance() {
			Node node = Clipboard.SINGLETON.peek();
			if( node instanceof Statement ) {
				Statement statement = (Statement)node;
				return AstUtilities.isAddEventListenerMethodInvocationStatement( statement );
			} else {
				return false;
			}
		}

		@Override
		public Triggerable getDropOperation( DragStep step, DropSite dropSite ) {
			DragModel dragModel = step.getModel();
			if( dragModel instanceof StatementDragModel ) {
				StatementDragModel statementDragModel = (StatementDragModel)dragModel;
				Statement statement = statementDragModel.getStatement();
				boolean isCopy = InputEventUtilities.isQuoteControlUnquoteDown( step.getLatestMouseEvent() );
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

	private final DStack<Node> stack = Stacks.newStack();
	private final ClipboardDropSite dropSite = new ClipboardDropSite();
	private final ClipboardDragModel dragModel = new ClipboardDragModel();
	private final ClipboardDragComponent dragComponent = new ClipboardDragComponent( dragModel );

	public DragComponent getDragComponent() {
		return this.dragComponent;
	}

	public DropReceptor getDropReceptor() {
		return this.dragComponent.getDropReceptor();
	}

	public DropSite getDropSite() {
		return this.dropSite;
	}

	public DragModel getDragModel() {
		return this.dragModel;
	}

	public boolean isStackEmpty() {
		return ( ( this.stack != null ) && ( this.stack.size() > 0 ) ) == false;
	}

	public int getStackSize() {
		return this.stack.size();
	}

	public Node peek() {
		if( this.stack.size() > 0 ) {
			return this.stack.peek();
		} else {
			return null;
		}
	}

	public void push( Node node ) {
		this.stack.push( node );
		this.dragComponent.refresh();
	}

	public Node pop() {
		Node rv = this.stack.pop();
		this.dragComponent.refresh();
		return rv;
	}
}
