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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractContext< E extends Operation > implements Context< E > {
	private E operation;
	private java.util.Map< Object, Object > map = new java.util.HashMap< Object, Object >();
	private boolean isCommitted = false;
	private boolean isCancelled = false;
	private java.util.EventObject e;
	private boolean isCancelWorthwhile;

	public AbstractContext( E operation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		this.operation = operation;
		this.e = e;
		this.isCancelWorthwhile = isCancelWorthwhile;
	}
	public E getOperation() {
		return this.operation;
	}
	public boolean isCommitted() {
		return this.isCommitted;
	}
	public boolean isCancelled() {
		return this.isCancelled;
	}
	public boolean isPending() {
		return (this.isCommitted() || this.isCancelled()) == false;
	}
	public void commitAndInvokeDo( Edit edit ) {
		assert this.isPending();
		edu.cmu.cs.dennisc.zoot.event.CommitEvent e = new edu.cmu.cs.dennisc.zoot.event.CommitEvent( this.operation, this, edit );
		ZManager.fireOperationCommitting( e );
		if( edit != null ) {
			edit.doOrRedo( true );
		}
		this.isCommitted = true;
		ZManager.fireOperationCommitted( e );
	}
	public void commit() {
		this.commitAndInvokeDo( null );
	}
	public void pend(edu.cmu.cs.dennisc.zoot.Resolver<? extends Edit, ?> resolver) {
		class PendTaskObserver< E extends Edit,F > implements edu.cmu.cs.dennisc.task.TaskObserver< F > {
			private Context<? extends Operation> context;
			private Resolver<E,F> resolver;
			private E edit;
			public PendTaskObserver( Context<? extends Operation> context, Resolver<E,F> resolver ) {
				this.context = context;
				this.resolver = resolver;
				this.edit = this.resolver.createEdit();
				this.edit = this.resolver.initialize( this.edit, this.context, this );
			}
			public void handleCompletion(F f) {
				this.edit = this.resolver.handleCompletion( this.edit, f);
				this.context.commitAndInvokeDo( this.edit );
			}
			public void handleCancelation() {
				this.resolver.handleCancelation();
				this.context.cancel();
			}
		}
		new PendTaskObserver(this, resolver);
	}
	public void cancel() {
		assert this.isPending();
		edu.cmu.cs.dennisc.zoot.event.CancelEvent e = new edu.cmu.cs.dennisc.zoot.event.CancelEvent( this.operation, this );
		ZManager.fireOperationCancelling( e );
		this.isCancelled = true;
		ZManager.fireOperationCancelled( e );
	}
	public boolean isCancelWorthwhile() {
		return isCancelWorthwhile;
	}
	public <E extends Object> E get( Object key, Class< E > cls ) {
		Object rv = this.get( key );
		if( rv != null ) {
			assert cls.isAssignableFrom( rv.getClass() );
			return (E)rv;
		} else {
			return null;
		}
	}
	public Object get( Object key ) {
		return this.map.get( key );
	}
	public void put( Object key, Object value ) {
		this.map.put( key, value );
	}
	public java.util.EventObject getEvent() {
		return e;
	}
	public ActionContext perform( ActionOperation operation, java.util.EventObject o, boolean isCancelWorthwhile ) {
		return ZManager.performIfAppropriate( operation, o, isCancelWorthwhile );
	}
	public ItemSelectionContext perform( ItemSelectionOperation operation, java.util.EventObject o, boolean isCancelWorthwhile, Object prevSelection, Object nextSelection ) {
		return ZManager.performIfAppropriate( operation, o, isCancelWorthwhile, prevSelection, nextSelection );
	}
	public void execute( org.jdesktop.swingworker.SwingWorker< ?, ? > worker ) {
		worker.execute();
	}
}

//class MyStateContext<E> extends AbstractContext implements StateContext {
//	private E previousValue;
//	private E nextValue;
//	public MyStateContext( java.util.EventObject e, boolean isCancelWorthwhile, E previousValue, E nextValue ) {
//		super( e, isCancelWorthwhile );
//		this.previousValue = previousValue;
//		this.nextValue = nextValue;
//	}
//	public E getPreviousValue() {
//		return this.previousValue;
//	}
//	public E getNextValue() {
//		return this.nextValue;
//	}
//}

class MyActionContext extends AbstractContext< ActionOperation > implements ActionContext {
	public MyActionContext( ActionOperation operation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		super( operation, e, isCancelWorthwhile );
	}
}

class MyBooleanStateContext extends AbstractContext< BooleanStateOperation > implements BooleanStateContext {
	private Boolean previousValue;
	private Boolean nextValue;

	public MyBooleanStateContext( BooleanStateOperation operation, java.util.EventObject e, boolean isCancelWorthwhile, Boolean previousValue, Boolean nextValue ) {
		super( operation, e, isCancelWorthwhile );
		this.previousValue = previousValue;
		this.nextValue = nextValue;
	}
	public Boolean getPreviousValue() {
		return this.previousValue;
	}
	public Boolean getNextValue() {
		return this.nextValue;
	}
}

class MyBoundedRangeContext extends AbstractContext< BoundedRangeOperation > implements BoundedRangeContext {
	public MyBoundedRangeContext( BoundedRangeOperation operation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		super( operation, e, isCancelWorthwhile );
	}
}

class MyItemSelectionContext<E> extends AbstractContext< ItemSelectionOperation< E > > implements ItemSelectionContext< E > {
	private E previousSelection;
	private E nextSelection;

	//	private boolean isPreviousSelectionValid;
	public MyItemSelectionContext( ItemSelectionOperation< E > operation, java.util.EventObject e, boolean isCancelWorthwhile, E previousSelection, E nextSelection ) {
		super( operation, e, isCancelWorthwhile );
		this.previousSelection = previousSelection;
		this.nextSelection = nextSelection;
		//		this.isPreviousSelectionValid = true;
	}
	public E getPreviousSelection() {
		return this.previousSelection;
	}
	public E getNextSelection() {
		return this.nextSelection;
	}
	//	public boolean isPreviousSelectionValid() {
	//		return this.isPreviousSelectionValid;
	//	}
}

/**
 * @author Dennis Cosgrove
 */
public class ZManager {
	@Deprecated
	public static final java.util.UUID UNKNOWN_GROUP = java.util.UUID.fromString( "6e31d069-b4ed-4d10-a3af-bc009985c6e3" );
	
	private static java.util.List< edu.cmu.cs.dennisc.zoot.event.ManagerListener > managerListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.event.ManagerListener >();
	private static edu.cmu.cs.dennisc.zoot.event.ManagerListener[] managerListenerArray = null;
	public static void addManagerListener( edu.cmu.cs.dennisc.zoot.event.ManagerListener l ) {
		synchronized( ZManager.managerListeners ) {
			ZManager.managerListeners.add( l );
			ZManager.managerListenerArray = null;
		}
	}
	public static void removeManagerListener( edu.cmu.cs.dennisc.zoot.event.ManagerListener l ) {
		synchronized( ZManager.managerListeners ) {
			ZManager.managerListeners.remove( l );
			ZManager.managerListenerArray = null;
		}
	}

	private static edu.cmu.cs.dennisc.zoot.event.ManagerListener[] getManagerListenerArray() {
		synchronized( ZManager.managerListeners ) {
			if( ZManager.managerListenerArray != null ) {
				//pass
			} else {
				ZManager.managerListenerArray = edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( ZManager.managerListeners, edu.cmu.cs.dennisc.zoot.event.ManagerListener.class );
			}
			return ZManager.managerListenerArray;
		}
	}
	/*package-private*/ static void fireOperationCancelling( edu.cmu.cs.dennisc.zoot.event.CancelEvent e ) {
		for( edu.cmu.cs.dennisc.zoot.event.ManagerListener l : ZManager.getManagerListenerArray() ) {
			l.operationCancelling( e );
		}
	}
	/*package-private*/ static void fireOperationCancelled( edu.cmu.cs.dennisc.zoot.event.CancelEvent e ) {
		for( edu.cmu.cs.dennisc.zoot.event.ManagerListener l : ZManager.getManagerListenerArray() ) {
			l.operationCancelled( e );
		}
	}
	/*package-private*/ static void fireOperationCommitting( edu.cmu.cs.dennisc.zoot.event.CommitEvent e ) {
		for( edu.cmu.cs.dennisc.zoot.event.ManagerListener l : ZManager.getManagerListenerArray() ) {
			l.operationCommitting( e );
		}
	}
	/*package-private*/ static void fireOperationCommitted( edu.cmu.cs.dennisc.zoot.event.CommitEvent e ) {
		for( edu.cmu.cs.dennisc.zoot.event.ManagerListener l : ZManager.getManagerListenerArray() ) {
			l.operationCommitted( e );
		}
	}

//	private static java.util.List< zoot.event.EditListener > editListeners = new java.util.LinkedList< zoot.event.EditListener >();
//	public static void addEditListener( zoot.event.EditListener l ) {
//		synchronized( ZManager.editListeners ) {
//			ZManager.editListeners.add( l );
//		}
//	}
//	public static void removeEditListener( zoot.event.EditListener l ) {
//		synchronized( ZManager.editListeners ) {
//			ZManager.editListeners.remove( l );
//		}
//	}
//
//	protected static void fireEditCommitting( zoot.event.EditEvent e ) {
//		synchronized( ZManager.editListeners ) {
//			for( zoot.event.EditListener l : ZManager.editListeners ) {
//				l.editCommitting( e );
//			}
//		}
//	}
//	protected static void fireEditCommitted( zoot.event.EditEvent e ) {
//		synchronized( ZManager.editListeners ) {
//			for( zoot.event.EditListener l : ZManager.editListeners ) {
//				l.editCommitted( e );
//			}
//		}
//	}


	//	private static void addToHistory( Operation operation ) {
	//	}
	//	private static void handlePreparedOperation( CancellableOperation operation, java.util.EventObject e, java.util.List< java.util.EventObject > preparationUpdates, CancellableOperation.PreparationResult preparationResult ) {
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "ZManager.handlePreparedOperation", operation, preparationResult );
	//		if( preparationResult != null ) {
	//			if( preparationResult == CancellableOperation.PreparationResult.CANCEL ) {
	//				//pass
	//			} else {
	//				operation.perform();
	//				if( preparationResult == CancellableOperation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY ) {
	//					//addToHistory( operation );
	//				}
	//			}
	//		}
	//	}

	private static boolean isDragInProgress = false;

	public static boolean isDragInProgress() {
		return ZManager.isDragInProgress;
	}
	public static void setDragInProgress( boolean isDragInProgress ) {
		ZManager.isDragInProgress = isDragInProgress;
	}

	public static final boolean CANCEL_IS_WORTHWHILE = true;
	public static final boolean CANCEL_IS_FUTILE = false;

	public static BooleanStateContext performIfAppropriate( BooleanStateOperation stateOperation, java.util.EventObject e, boolean isCancelWorthwhile, Boolean previousValue, Boolean nextValue ) {
		assert stateOperation != null;
		BooleanStateContext rv = new MyBooleanStateContext( stateOperation, e, isCancelWorthwhile, previousValue, nextValue );
		stateOperation.performStateChange( rv );
		return rv;
	}

	//	public static BoundedRangeContext performIfAppropriate( BoundedRangeOperation boundedRangeOperation, java.util.EventObject e ) throws java.beans.PropertyVetoException {
	//		assert boundedRangeOperation != null;
	//		BoundedRangeContext rv = new MyBoundedRangeContext( e, CANCEL_IS_WORTHWHILE );
	//		boundedRangeOperation.perform( rv );
	//		return rv;
	//	}	
	public static BoundedRangeContext performIfAppropriate( BoundedRangeOperation boundedRangeOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		assert boundedRangeOperation != null;
		BoundedRangeContext rv = new MyBoundedRangeContext( boundedRangeOperation, e, isCancelWorthwhile );
		boundedRangeOperation.perform( rv );
		return rv;
	}
	public static ActionContext performIfAppropriate( ActionOperation actionOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		assert actionOperation != null;
		ActionContext rv = new MyActionContext( actionOperation, e, isCancelWorthwhile );
		actionOperation.perform( rv );
		return rv;
		//		if( operation instanceof CancellableOperation ) {
		//			CancellableOperation cancellableOperation = (CancellableOperation)operation;
		//			final java.util.List< java.util.EventObject > preparationUpdates = new java.util.LinkedList< java.util.EventObject >();
		//			CancellableOperation.PreparationResult preparationResult = cancellableOperation.prepare( e, new CancellableOperation.PreparationObserver() {
		//				public void update( java.util.EventObject e ) {
		//					preparationUpdates.add( e );
		//				}
		//			} );
		//			handlePreparedOperation( cancellableOperation, e, preparationUpdates, preparationResult );
		//		} else {
		//			if( operation instanceof ResponseOperation ) {
		//				((ResponseOperation)operation).respond( e );
		//			}
		//			operation.perform();
		//		}
	}

	public static ItemSelectionContext performIfAppropriate( ItemSelectionOperation< ? > itemSelectionOperation, java.util.EventObject e, boolean isCancelWorthwhile, Object previousSelection, Object nextSelection ) {
		assert itemSelectionOperation != null;
		ItemSelectionContext rv = new MyItemSelectionContext( itemSelectionOperation, e, isCancelWorthwhile, previousSelection, nextSelection );
		itemSelectionOperation.performSelectionChange( rv );
		return rv;
	}
	//public static ZMenu createMenu( StateOperation< java.util.ArrayList< E > > stateOperation )

	//	private static void setHeavyWeight( javax.swing.JPopupMenu popup ) {
	//		popup.setLightWeightPopupEnabled( false );
	//	}

	public static javax.swing.JPanel createRadioButtons( ItemSelectionOperation itemSelectionOperation ) {
		javax.swing.JPanel rv = new edu.cmu.cs.dennisc.croquet.swing.BoxPane( javax.swing.BoxLayout.PAGE_AXIS );
		javax.swing.ComboBoxModel comboBoxModel = itemSelectionOperation.getComboBoxModel();
		int N = comboBoxModel.getSize();
		for( int i = 0; i < N; i++ ) {
			javax.swing.Action actionI = itemSelectionOperation.getActionForConfiguringSwing( i );
			javax.swing.ButtonModel buttonModelI = itemSelectionOperation.getButtonModelForConfiguringSwing( i );
			javax.swing.JRadioButton radioI = new javax.swing.JRadioButton( actionI );
			radioI.setModel( buttonModelI );
			//			buttonModelI.setGroup( group );
			rv.add( radioI );
		}
		return rv;
	}
	public static javax.swing.JMenu createMenu( String name, int mnemonic, ItemSelectionOperation itemSelectionOperation ) {
		javax.swing.JMenu rv = new javax.swing.JMenu( name );
		rv.setMnemonic( mnemonic );
		//rv.getPopupMenu().setSelectionModel( singleSelectionOperation.getSingleSelectionModelForConfiguringSwingComponents() );
		//		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		javax.swing.ComboBoxModel comboBoxModel = itemSelectionOperation.getComboBoxModel();
		int N = comboBoxModel.getSize();
		for( int i = 0; i < N; i++ ) {
			javax.swing.Action actionI = itemSelectionOperation.getActionForConfiguringSwing( i );
			javax.swing.ButtonModel buttonModelI = itemSelectionOperation.getButtonModelForConfiguringSwing( i );
			javax.swing.JRadioButtonMenuItem itemI = new javax.swing.JRadioButtonMenuItem( actionI );
			itemI.setModel( buttonModelI );
			//			buttonModelI.setGroup( group );
			rv.add( itemI );
		}
		//todo?
		//setHeavyWeight( rv.getPopupMenu() );
		return rv;
	}
	
	public static java.awt.event.KeyListener createKeyListener( final ItemSelectionOperation itemSelectionOperation ) {
		class KeyAdapter implements java.awt.event.KeyListener {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				itemSelectionOperation.handleKeyPressed( e );
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		}
		return new KeyAdapter();
	}
	

	public static ActionOperation MENU_SEPARATOR = null;

	private static javax.swing.JMenuItem createMenuItemOrCheckBoxMenuItem( Operation operation ) {
		if( operation instanceof ActionOperation ) {
			return createMenuItem( (ActionOperation)operation );
		} else if( operation instanceof BooleanStateOperation ) {
			BooleanStateOperation booleanStateOperation = (BooleanStateOperation)operation;
			return createCheckBoxMenuItem( booleanStateOperation );
		} else {
			throw new RuntimeException();
		}
	}

	public static javax.swing.JMenu createMenu( String name, int mnemonic, Operation... operations ) {
		javax.swing.JMenu rv = new javax.swing.JMenu( name );
		rv.setMnemonic( mnemonic );
		for( Operation operation : operations ) {
			if( operation == MENU_SEPARATOR ) {
				rv.addSeparator();
			} else {
				rv.add( createMenuItemOrCheckBoxMenuItem( operation ) );
			}
		}
		//todo?
		//setHeavyWeight( rv.getPopupMenu() );
		return rv;
	}
	public static javax.swing.JMenu createMenu( String name, int mnemonic, java.util.Collection< ? extends Operation > operations ) {
		Operation[] array = new Operation[ operations.size() ];
		operations.toArray( array );
		return createMenu( name, mnemonic, array );
	}
	public static javax.swing.JPopupMenu createPopupMenu( Operation... operations ) {
		javax.swing.JPopupMenu rv = new javax.swing.JPopupMenu();
		for( Operation operation : operations ) {
			if( operation == MENU_SEPARATOR ) {
				rv.addSeparator();
			} else {
				rv.add( createMenuItemOrCheckBoxMenuItem( operation ) );
			}
		}
		//todo?
		//setHeavyWeight( rv );
		return rv;
	}
	public static javax.swing.JPopupMenu createPopupMenu( java.util.Collection< ? extends Operation > operations ) {
		Operation[] array = new Operation[ operations.size() ];
		operations.toArray( array );
		return createPopupMenu( array );
	}
	
	
	public static javax.swing.JButton createButton( ActionOperation actionOperation ) {
		assert actionOperation != null;
		return new ZButton(actionOperation);
	}
	public static javax.swing.JMenuItem createMenuItem( ActionOperation actionOperation ) {
		assert actionOperation != null;
		return new ZMenuItem(actionOperation);
	}
	public static javax.swing.AbstractButton createHyperlink( ActionOperation actionOperation ) {
		assert actionOperation != null;
		return new ZHyperlink(actionOperation);
	}


	public static javax.swing.JCheckBox createCheckBox( BooleanStateOperation booleanStateOperation ) {
		assert booleanStateOperation != null;
		return new ZCheckBox(booleanStateOperation);
	}
	public static javax.swing.JCheckBoxMenuItem createCheckBoxMenuItem( BooleanStateOperation booleanStateOperation ) {
		assert booleanStateOperation != null;
		return new ZCheckBoxMenuItem(booleanStateOperation);
	}


	public static <E> javax.swing.JComboBox createComboBox( ItemSelectionOperation< E > itemSelectionOperation ) {
		return new ZComboBox(itemSelectionOperation);
	}
}
