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
package zoot;

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
	public void commit() {
		assert this.isPending();
		this.isCommitted = true;
	}
	public void commit( javax.swing.undo.UndoableEdit undoableEdit ) {
		zoot.event.EditEvent e = new zoot.event.EditEvent( this.getOperation(), this, undoableEdit );
		ZManager.fireEditCommitting( e );
		undoableEdit.redo();
		ZManager.fireEditCommitted( e );
	}
	
	public void cancel() {
		assert this.isPending();
		this.isCancelled = true;
	}
	public void setTaskObserver( edu.cmu.cs.dennisc.task.TaskObserver< ? > taskObserver ) {
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
	
	private static javax.swing.undo.UndoableEdit insignificantEdit = new javax.swing.undo.AbstractUndoableEdit() {
		@Override
		public boolean isSignificant() {
			return false;
		}
	};
	
	public static javax.swing.undo.UndoableEdit getInsignificantEdit() {
		return ZManager.insignificantEdit;
	}
	
	
	private static java.util.List< zoot.event.OperationListener > operationListeners = new java.util.LinkedList< zoot.event.OperationListener >();
	private static java.util.List< zoot.event.EditListener > editListeners = new java.util.LinkedList< zoot.event.EditListener >();

	public static void addOperationListener( zoot.event.OperationListener l ) {
		synchronized( ZManager.operationListeners ) {
			ZManager.operationListeners.add( l );
		}
	}
	public static void removeOperationListener( zoot.event.OperationListener l ) {
		synchronized( ZManager.operationListeners ) {
			ZManager.operationListeners.remove( l );
		}
	}

	protected static void fireOperationPerforming( zoot.event.OperationEvent e ) {
		synchronized( ZManager.operationListeners ) {
			for( zoot.event.OperationListener l : ZManager.operationListeners ) {
				l.operationPerforming( e );
			}
		}
	}
	protected static void fireOperationPerformed( zoot.event.OperationEvent e ) {
		synchronized( ZManager.operationListeners ) {
			for( zoot.event.OperationListener l : ZManager.operationListeners ) {
				l.operationPerformed( e );
			}
		}
	}

	public static void addEditListener( zoot.event.EditListener l ) {
		synchronized( ZManager.editListeners ) {
			ZManager.editListeners.add( l );
		}
	}
	public static void removeEditListener( zoot.event.EditListener l ) {
		synchronized( ZManager.editListeners ) {
			ZManager.editListeners.remove( l );
		}
	}

	protected static void fireEditCommitting( zoot.event.EditEvent e ) {
		synchronized( ZManager.editListeners ) {
			for( zoot.event.EditListener l : ZManager.editListeners ) {
				l.editCommitting( e );
			}
		}
	}
	protected static void fireEditCommitted( zoot.event.EditEvent e ) {
		synchronized( ZManager.editListeners ) {
			for( zoot.event.EditListener l : ZManager.editListeners ) {
				l.editCommitted( e );
			}
		}
	}


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
		zoot.event.OperationEvent operationEvent = new zoot.event.OperationEvent( stateOperation, rv );
		fireOperationPerforming( operationEvent );
		stateOperation.performStateChange( rv );
		fireOperationPerformed( operationEvent );
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
		zoot.event.OperationEvent operationEvent = new zoot.event.OperationEvent( boundedRangeOperation, rv );
		fireOperationPerforming( operationEvent );
		boundedRangeOperation.perform( rv );
		fireOperationPerformed( operationEvent );
		return rv;
	}
	public static ActionContext performIfAppropriate( ActionOperation actionOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		assert actionOperation != null;
		ActionContext rv = new MyActionContext( actionOperation, e, isCancelWorthwhile );
		zoot.event.OperationEvent operationEvent = new zoot.event.OperationEvent( actionOperation, rv );
		fireOperationPerforming( operationEvent );
		actionOperation.perform( rv );
		fireOperationPerformed( operationEvent );
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
		zoot.event.OperationEvent operationEvent = new zoot.event.OperationEvent( itemSelectionOperation, rv );
		fireOperationPerforming( operationEvent );
		itemSelectionOperation.performSelectionChange( rv );
		fireOperationPerformed( operationEvent );
		return rv;
	}
	//public static ZMenu createMenu( StateOperation< java.util.ArrayList< E > > stateOperation )

	//	private static void setHeavyWeight( javax.swing.JPopupMenu popup ) {
	//		popup.setLightWeightPopupEnabled( false );
	//	}

	public static javax.swing.JPanel createRadioButtons( ItemSelectionOperation itemSelectionOperation ) {
		javax.swing.JPanel rv = new swing.BoxPane( javax.swing.BoxLayout.PAGE_AXIS );
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

	private static javax.swing.JMenuItem createMenuItem( Operation operation ) {
		if( operation instanceof ActionOperation ) {
			ActionOperation actionOperation = (ActionOperation)operation;
			return new ZMenuItem( actionOperation );
		} else if( operation instanceof BooleanStateOperation ) {
			BooleanStateOperation booleanStateOperation = (BooleanStateOperation)operation;
			return new ZCheckBoxMenuItem( booleanStateOperation );
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
				rv.add( createMenuItem( operation ) );
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
				rv.add( createMenuItem( operation ) );
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
}
