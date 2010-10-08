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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class FillIn< E > extends Node {
	public void addBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		super.addChild( blank );
	}

	@Override
	protected boolean isLast() {
		return this.getNextNode() == null;
	}
	
	public Blank getBlankAt( int index ) {
		return (Blank)getChildren().get( index );
	}
	public Blank getParentBlank() {
		return (Blank)getParent();
	}
	
	@Override
	protected Node getNextNode() {
		java.util.List< Node > children = this.getChildren();
		if( children.size() > 0 ) {
			return children.get( 0 );
		} else {
			return this.getNextBlank();
		}
	}
	public void select() {
		getNearestBlank().setSelectedFillIn( this );
	}
	public void deselect() {
		//todo?
		//getNearestBlank().setSelectedFillIn( null );
	}
	
	@Override
	public void menuSelected( javax.swing.event.MenuEvent e ) {
		this.select();
		super.menuSelected( e );
	}

	@Override
	public void menuDeselected( javax.swing.event.MenuEvent e ) {
		this.deselect();
		super.menuDeselected( e );
	}
	
	@Override
	protected void handleActionOperationPerformed() {
		this.select();
		super.handleActionOperationPerformed();
	}

	public abstract E getTransientValue();
	public abstract E getValue();
	public void showPopupMenu( java.awt.Component invoker, int x, int y, edu.cmu.cs.dennisc.task.TaskObserver< ? extends Object > taskObserver ) {
		class DefaultRootBlank extends Blank {
			@Override
			protected void addChildren() {
				this.addFillIn( FillIn.this );
			}
			
			@Override
			protected void addNextNodeMenuItems( javax.swing.JComponent component ) {
				FillIn.this.setParent( this );
				FillIn.this.getChildren().get( 0 ).addNextNodeMenuItems( component );
			}
		}
		if( this.getChildren().size() > 0 ) {
			new DefaultRootBlank().showPopupMenu( invoker, x, y, taskObserver );
		} else {
			throw new RuntimeException();
		}
	}
}
