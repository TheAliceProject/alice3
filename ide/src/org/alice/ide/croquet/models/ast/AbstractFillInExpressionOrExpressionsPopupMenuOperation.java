/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractFillInExpressionOrExpressionsPopupMenuOperation extends edu.cmu.cs.dennisc.cascade.CascadingPopupMenuOperation {
	public AbstractFillInExpressionOrExpressionsPopupMenuOperation( java.util.UUID id ) {
		super( id );
	}
	public abstract edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression();
	protected edu.cmu.cs.dennisc.alice.ast.Statement getStatement() {
		edu.cmu.cs.dennisc.alice.ast.Expression prevExpression = this.getPreviousExpression();
		if( prevExpression != null ) {
			return prevExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.Statement.class );
		} else {
			return null;
		}
	}
	protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.BlockStatement, Integer > getBlockStatementAndIndex() {
		edu.cmu.cs.dennisc.alice.ast.Statement statement = getStatement();
		if( statement != null ) {
			edu.cmu.cs.dennisc.alice.ast.Node node = statement.getParent();
			if( node instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
				edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)node;
				int index = blockStatement.statements.indexOf( statement );
				if( index != -1 ) {
					return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( blockStatement, index );
				}
			}
		}
		return null;
	}

	private edu.cmu.cs.dennisc.cascade.Blank blank;
	protected abstract edu.cmu.cs.dennisc.cascade.Blank createCascadeBlank();
	@Override
	protected final edu.cmu.cs.dennisc.cascade.Blank getCascadeBlank() {
		if( this.blank != null ) {
			//pass
		} else {
			this.blank = this.createCascadeBlank();
		}
		return this.blank;
	}
	

	@Override
	protected void handlePopupMenuWillBecomeVisible( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu, javax.swing.event.PopupMenuEvent e ) {
		org.alice.ide.IDE.getSingleton().getCascadeManager().pushContext( this.getPreviousExpression(), this.getBlockStatementAndIndex() );
		super.handlePopupMenuWillBecomeVisible( popupMenu, e );
	}
	
	@Override
	protected void handlePopupMenuWillBecomeInvisible( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu, javax.swing.event.PopupMenuEvent e ) {
		super.handlePopupMenuWillBecomeInvisible( popupMenu, e );
		org.alice.ide.IDE.getSingleton().getCascadeManager().popContext();
	}
}
