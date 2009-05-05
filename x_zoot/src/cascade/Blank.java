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
package cascade;

import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public abstract class Blank extends Node {
	private edu.cmu.cs.dennisc.task.TaskObserver taskObserver;
	private FillIn selectedFillIn;

	public FillIn getFillInAt( int index ) {
		return (FillIn)getChildren().get( index );
	}
	public FillIn getParentFillIn() {
		return (FillIn)getParent();
	}
	
	public void addFillIn( cascade.FillIn fillIn ) {
		super.addChild( fillIn );
	}

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
		this.addFillIn( new SeparatorFillIn( text ) );
	}
	public void showPopupMenu( java.awt.Component invoker, int x, int y, final edu.cmu.cs.dennisc.task.TaskObserver< ? extends Object > taskObserver ) {
		this.taskObserver = taskObserver;
		final javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
		
		//
		//popupMenu.setLightWeightPopupEnabled( false );
		//
		
		class EventConsumer extends javax.swing.JComponent {
			private java.awt.event.MouseListener mouseAdapter = new java.awt.event.MouseListener() {
				public void mouseEntered( MouseEvent e ) {
				}
				public void mouseExited( MouseEvent e ) {
				}
				public void mousePressed( MouseEvent e ) {
				}
				public void mouseReleased( MouseEvent e ) {
					EventConsumer.this.removeFromParentJustInCaseAnExceptionWasThrownSomewhere();
				}
				public void mouseClicked( MouseEvent e ) {
				}
			};
			private void removeFromParentJustInCaseAnExceptionWasThrownSomewhere() {
				java.awt.Container parent = this.getParent();
				if( parent != null ) {
					parent.remove( this );
				}
			}
			@Override
			public void addNotify() {
				super.addNotify();
				this.setLocation( 0, 0 );
				java.awt.Component parent = this.getParent();
				if( parent != null ) {
					this.setSize( parent.getSize() );
				}
				this.addMouseListener( this.mouseAdapter );
			}
			@Override
			public void removeNotify() {
				this.removeMouseListener( this.mouseAdapter );
				super.removeNotify();
			}

		}
		final EventConsumer eventConsumer = new EventConsumer();
		
		
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( invoker );
		final javax.swing.JLayeredPane layeredPane;
		if( root instanceof javax.swing.JFrame ) {
			javax.swing.JFrame window = (javax.swing.JFrame)root;
			layeredPane = window.getLayeredPane();
		} else if( root instanceof javax.swing.JDialog ) {
			javax.swing.JDialog window = (javax.swing.JDialog)root;
			layeredPane = window.getLayeredPane();
		} else if( root instanceof javax.swing.JWindow ) {
			javax.swing.JWindow window = (javax.swing.JWindow)root;
			layeredPane = window.getLayeredPane();
		} else {
			layeredPane = null;
		}
		popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
				if( layeredPane != null ) {
					layeredPane.add( eventConsumer, new Integer( javax.swing.JLayeredPane.MODAL_LAYER ) );
				}
				Blank.this.addNextNodeMenuItems( popupMenu );
			}
			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
				if( layeredPane != null ) {
					layeredPane.remove( eventConsumer );
				}
				popupMenu.removeAll();
			}
			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				Blank.this.handleCancel( e );
			}
		} );
		popupMenu.show( invoker, x, y );
	}

	protected void handleActionPerformed() {
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
