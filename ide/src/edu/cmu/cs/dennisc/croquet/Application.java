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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Application {
//	private static Application singleton;
//	public static Application getInstance() {
//		return singleton;
//	}
//	protected void setInstance( Application singleton ) {
//		Application.singleton = singleton;
//	}
//	
//	/*package-private*/ Context<Operation> getCurrentContext() {
//		return null;
//	}

	public Context<? extends Operation> getCurrentContext() {
		return null;
	}
	private java.util.Set<Operation> operations = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
	protected <O extends Operation > O registerOperation( O rv ) { 
		this.operations.add( rv );
		return rv;
	}
	protected void createAndRegisterOperations() { 
	}
	
	protected static class MenuBuilder {
		private String name;
		private Operation[] operations;
		public MenuBuilder( String name, Operation... operations ) {
			this.name = name;
			this.operations = operations;
		}
		public javax.swing.JMenu createMenu() {
			javax.swing.JMenu rv = new javax.swing.JMenu( this.name );
			for( Operation operation : this.operations ) {
				if( operation != null ) {
					javax.swing.JMenuItem menuItem = operation.createMenuItem();
					assert menuItem != null;
					rv.add( menuItem );
				} else {
					rv.addSeparator();
				}
			}
			return rv;
		}
	}
	protected java.util.List< MenuBuilder > updateMenuBuilders( java.util.List< MenuBuilder > rv ) {
		return rv;
	}
	protected final java.util.List< MenuBuilder > createMenuBuilders() {
		java.util.List< MenuBuilder > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		return updateMenuBuilders( rv );
	}
	protected java.awt.Container initializeContentPane( java.awt.Container rv ) {
		return rv;
	}
	public void initialize( String[] args ) {
		KFrame frame = new KFrame();
		javax.swing.JFrame jFrame = (javax.swing.JFrame)frame.getAWTFrame();

		this.createAndRegisterOperations();
		
		java.util.List< MenuBuilder > menuBuilders = this.createMenuBuilders();
		if( menuBuilders.size() > 0 ) {
			javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
			for( MenuBuilder menuBuilder : menuBuilders ) {
				menuBar.add( menuBuilder.createMenu() );
			}
			jFrame.setJMenuBar( menuBar );
		}
		
		initializeContentPane( jFrame.getContentPane() );
		jFrame.pack();
		jFrame.setVisible( true );
	}
	
	public static final boolean CANCEL_IS_WORTHWHILE = true;
	public static final boolean CANCEL_IS_FUTILE = false;
	
	private static java.util.List< edu.cmu.cs.dennisc.croquet.event.ManagerListener > managerListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.croquet.event.ManagerListener >();
	private static edu.cmu.cs.dennisc.croquet.event.ManagerListener[] managerListenerArray = null;
	public static void addManagerListener( edu.cmu.cs.dennisc.croquet.event.ManagerListener l ) {
		synchronized( Application.managerListeners ) {
			Application.managerListeners.add( l );
			Application.managerListenerArray = null;
		}
	}
	public static void removeManagerListener( edu.cmu.cs.dennisc.croquet.event.ManagerListener l ) {
		synchronized( Application.managerListeners ) {
			Application.managerListeners.remove( l );
			Application.managerListenerArray = null;
		}
	}

	private static edu.cmu.cs.dennisc.croquet.event.ManagerListener[] getManagerListenerArray() {
		synchronized( Application.managerListeners ) {
			if( Application.managerListenerArray != null ) {
				//pass
			} else {
				Application.managerListenerArray = edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( Application.managerListeners, edu.cmu.cs.dennisc.croquet.event.ManagerListener.class );
			}
			return Application.managerListenerArray;
		}
	}
	/*package-private*/ static void fireOperationCancelling( edu.cmu.cs.dennisc.croquet.event.CancelEvent e ) {
		for( edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray() ) {
			l.operationCancelling( e );
		}
	}
	/*package-private*/ static void fireOperationCancelled( edu.cmu.cs.dennisc.croquet.event.CancelEvent e ) {
		for( edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray() ) {
			l.operationCancelled( e );
		}
	}
	/*package-private*/ static void fireOperationCommitting( edu.cmu.cs.dennisc.croquet.event.CommitEvent e ) {
		for( edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray() ) {
			l.operationCommitting( e );
		}
	}
	/*package-private*/ static void fireOperationCommitted( edu.cmu.cs.dennisc.croquet.event.CommitEvent e ) {
		for( edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray() ) {
			l.operationCommitted( e );
		}
	}

	public static BooleanStateContext performIfAppropriate( BooleanStateOperation stateOperation, java.util.EventObject e, boolean isCancelWorthwhile, Boolean previousValue, Boolean nextValue ) {
		assert stateOperation != null;
		BooleanStateContext rv = new BooleanStateContext( stateOperation, e, isCancelWorthwhile, previousValue, nextValue );
		stateOperation.performStateChange( rv );
		return rv;
	}
	public static BoundedRangeContext performIfAppropriate( BoundedRangeOperation boundedRangeOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		assert boundedRangeOperation != null;
		BoundedRangeContext rv = new BoundedRangeContext( boundedRangeOperation, e, isCancelWorthwhile );
		boundedRangeOperation.perform( rv );
		return rv;
	}
	public static ActionContext performIfAppropriate( ActionOperation actionOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		assert actionOperation != null;
		ActionContext rv = new ActionContext( actionOperation, e, isCancelWorthwhile );
		actionOperation.perform( rv );
		return rv;
	}
	public static < E > ItemSelectionContext< E > performIfAppropriate( ItemSelectionOperation< E > itemSelectionOperation, java.util.EventObject e, boolean isCancelWorthwhile, E previousSelection, E nextSelection ) {
		assert itemSelectionOperation != null;
		ItemSelectionContext< E > rv = new ItemSelectionContext( itemSelectionOperation, e, isCancelWorthwhile, previousSelection, nextSelection );
		itemSelectionOperation.performSelectionChange( rv );
		return rv;
	}
}
