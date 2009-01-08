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
public abstract class Blank extends Node {
	private edu.cmu.cs.dennisc.task.TaskObserver< Object > taskObserver;
	private FillIn selectedFillIn;

	@Override
	protected Blank getNearestBlank() {
		return this;
	}
	@Override
	protected Node getNextNode() {
		return this;
	}
	public FillIn getSelectedFillIn() {
		return this.selectedFillIn;
	}
	public void setSelectedFillIn( FillIn fillIn ) {
		this.selectedFillIn = fillIn;
		Node parent = this.getParent();
		if( parent instanceof FillIn ) {
			FillIn parentFillIn = (FillIn)parent;
			for( Node child : parent.getChildren() ) {
				Blank blank = (Blank)child;
				if( blank.selectedFillIn != null ) {
					//pass
				} else {
					return;
				}
			}
			parentFillIn.select();
		}
	}

	public void addSeparator() {
		this.addSeparator( null );
	}
	public void addSeparator( String text ) {
		this.addChild( new SeparatorFillIn( text ) );
	}
	public void showPopupMenu( java.awt.Component invoker, int x, int y, final edu.cmu.cs.dennisc.task.TaskObserver< Object > taskObserver ) {
		this.taskObserver = taskObserver;
		final javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
		
		//
		//popupMenu.setLightWeightPopupEnabled( false );
		//
		
		
		popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
				Blank.this.addNextNodeMenuItems( popupMenu );
			}
			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
				popupMenu.removeAll();
			}
			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				Blank.this.handleCancel( e );
			}
		} );
		popupMenu.show( invoker, x, y );
	}

	protected void handleActionPerformed( java.awt.event.ActionEvent e ) {
		try {
			Object value = this.getSelectedFillIn().getValue();
			if( this.taskObserver != null ) {
				this.taskObserver.handleCompletion( value );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCompleted (no taskObserver):", this.getSelectedFillIn().getValue() );
			}
		} catch( CancelException ce ) {
			if( this.taskObserver != null ) {
				this.taskObserver.handleCancelation();
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCancelation (no taskObserver)" );
			}
		}
	}
	protected void handleCancel( javax.swing.event.PopupMenuEvent e ) {
		if( this.taskObserver != null ) {
			this.taskObserver.handleCancelation();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCancelation (no taskObserver)" );
		}
	}

}
