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

abstract class AbstractContext implements Context {
	private java.util.Map< Object, Object > map = new java.util.HashMap< Object, Object >();
	private boolean isCommitted = false;
	private boolean isCancelled = false;
	private java.util.EventObject e;
	private boolean isCancelWorthwhile;

	public AbstractContext( java.util.EventObject e, boolean isCancelWorthwhile ) {
		this.e = e;
		this.isCancelWorthwhile = isCancelWorthwhile;
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

class MyActionContext extends AbstractContext implements ActionContext {
	public MyActionContext( java.util.EventObject e, boolean isCancelWorthwhile ) {
		super( e, isCancelWorthwhile );
	}
}

class MySingleSelectionContext<E> extends AbstractContext implements ItemSelectionContext< E > {
	private E previousSelection;
	private E nextSelection;

	//	private boolean isPreviousSelectionValid;
	public MySingleSelectionContext( java.util.EventObject e, boolean isCancelWorthwhile, E previousSelection, E nextSelection ) {
		super( e, isCancelWorthwhile );
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
	static {
	}

	//	public static void setHandler( Handler handler ) {
	//	}
	public static void handleTabSelection( ZTabbedPane tabbedPane, int index ) {
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

	public static ActionContext performIfAppropriate( ActionOperation actionOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		assert actionOperation != null;
		ActionContext rv = new MyActionContext( e, isCancelWorthwhile );
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

	public static ItemSelectionContext performIfAppropriate( ItemSelectionOperation< ? > singleSelectionOperation, java.util.EventObject e, boolean isCancelWorthwhile, Object previousSelection, Object nextSelection ) {
		assert singleSelectionOperation != null;
		ItemSelectionContext rv = new MySingleSelectionContext( e, isCancelWorthwhile, previousSelection, nextSelection );
		singleSelectionOperation.performSelectionChange( rv );
		return rv;
	}
	//public static ZMenu createMenu( StateOperation< java.util.ArrayList< E > > stateOperation )

	//	private static void setHeavyWeight( javax.swing.JPopupMenu popup ) {
	//		popup.setLightWeightPopupEnabled( false );
	//	}

	public static javax.swing.JMenu createMenu( String name, int mnemonic, ItemSelectionOperation itemSelectionOperation ) {
		javax.swing.JMenu rv = new javax.swing.JMenu( name );
		rv.setMnemonic( mnemonic );
		//rv.getPopupMenu().setSelectionModel( singleSelectionOperation.getSingleSelectionModelForConfiguringSwingComponents() );
		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		javax.swing.ListModel listModel = itemSelectionOperation.getListModel();
		int N = listModel.getSize();
		for( int i = 0; i < N; i++ ) {
			javax.swing.Action actionI = itemSelectionOperation.getActionForConfiguringSwing( i );
			javax.swing.JRadioButtonMenuItem item = new javax.swing.JRadioButtonMenuItem( actionI );
			item.getModel().setGroup( group );
			rv.add( item );
		}
		//todo?
		//setHeavyWeight( rv.getPopupMenu() );
		return rv;
	}

	public static ActionOperation MENU_SEPARATOR = null;

	public static javax.swing.JMenu createMenu( String name, int mnemonic, ActionOperation... actionOperations ) {
		javax.swing.JMenu rv = new javax.swing.JMenu( name );
		rv.setMnemonic( mnemonic );
		for( ActionOperation actionOperation : actionOperations ) {
			if( actionOperation == MENU_SEPARATOR ) {
				rv.addSeparator();
			} else {
				rv.add( new ZMenuItem( actionOperation ) );
			}
		}
		//todo?
		//setHeavyWeight( rv.getPopupMenu() );
		return rv;
	}
	public static javax.swing.JMenu createMenu( String name, int mnemonic, java.util.Collection< ActionOperation > actionOperations ) {
		ActionOperation[] array = new ActionOperation[ actionOperations.size() ];
		actionOperations.toArray( array );
		return createMenu( name, mnemonic, array );
	}
	public static javax.swing.JPopupMenu createPopupMenu( ActionOperation... actionOperations ) {
		javax.swing.JPopupMenu rv = new javax.swing.JPopupMenu();
		for( ActionOperation actionOperation : actionOperations ) {
			if( actionOperation == MENU_SEPARATOR ) {
				rv.addSeparator();
			} else {
				rv.add( new ZMenuItem( actionOperation ) );
			}
		}
		//todo?
		//setHeavyWeight( rv );
		return rv;
	}
	public static javax.swing.JPopupMenu createPopupMenu( java.util.Collection< ActionOperation > actionOperations ) {
		ActionOperation[] array = new ActionOperation[ actionOperations.size() ];
		actionOperations.toArray( array );
		return createPopupMenu( array );
	}
}
