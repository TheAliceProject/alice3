/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
	protected void handleActionOperationPerformed( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		this.select();
		super.handleActionOperationPerformed( actionContext );
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
