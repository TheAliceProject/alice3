package edu.cmu.cs.dennisc.toolkit.scenegraph;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener;

public class SceneGraphTreeModel implements TreeModel, HierarchyListener, PropertyListener {

	public SceneGraphTreeModel( Component root )
	{
		this.rootComponent = root;
		setUpListening( this.rootComponent, true );
	}

	private void setUpListening( Component toListenTo, boolean start )
	{
		if( toListenTo != null )
		{
			if( start )
			{
				toListenTo.addHierarchyListener( this );
			}
			else
			{
				toListenTo.removeHierarchyListener( this );
			}
			if( toListenTo instanceof Visual )
			{
				if( start )
				{
					( (Visual)toListenTo ).isShowing.addPropertyListener( this );
				}
				else
				{
					( (Visual)toListenTo ).isShowing.removePropertyListener( this );
				}
			}
			if( toListenTo instanceof Composite )
			{
				for( Component c : ( (Composite)toListenTo ).getComponents() )
				{
					setUpListening( c, start );
				}
			}
		}
	}

	public void setRoot( Component root )
	{
		if( this.rootComponent != null )
		{
			setUpListening( this.rootComponent, false );
		}
		Component oldRoot = this.rootComponent;
		this.rootComponent = root;
		setUpListening( this.rootComponent, true );
		fireTreeStructureChanged( oldRoot );
	}

	@Override
	public void addTreeModelListener( TreeModelListener l ) {
		this.treeModelListeners.add( l );
	}

	@Override
	public void removeTreeModelListener( TreeModelListener l ) {
		this.treeModelListeners.remove( l );
	}

	@Override
	public void hierarchyChanged( HierarchyEvent hierarchyEvent ) {
		System.out.println( hierarchyEvent );
	}

	/**
	 * The only event raised by this model is TreeStructureChanged with the root
	 * as path, i.e. the whole tree has changed.
	 */
	protected void fireTreeStructureChanged( Component oldRoot ) {
		TreeModelEvent e = new TreeModelEvent( this,
				new Object[] { oldRoot } );
		for( TreeModelListener tml : treeModelListeners ) {
			tml.treeStructureChanged( e );
		}
	}

	@Override
	public Object getChild( Object parent, int index ) {
		if( parent instanceof Composite )
		{
			Composite parentComposite = (Composite)parent;
			return parentComposite.getComponentAt( index );
		}
		return null;
	}

	@Override
	public int getChildCount( Object parent ) {
		if( parent instanceof Composite )
		{
			Composite parentComposite = (Composite)parent;
			return parentComposite.getComponentCount();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild( Object parent, Object child ) {
		if( parent instanceof Composite )
		{
			Composite parentComposite = (Composite)parent;
			for( int i = 0; i < parentComposite.getComponentCount(); i++ )
			{
				if( parentComposite.getComponentAt( i ) == child )
				{
					return i;
				}
			}
		}
		return -1;
	}

	private Component getComponentRoot( Component c )
	{
		if( c.getParent() == null )
		{
			return c;
		}
		else
		{
			return getComponentRoot( c.getParent() );
		}
	}

	@Override
	public Object getRoot() {
		if( this.rootComponent != null )
		{
			return this.getComponentRoot( this.rootComponent );
		}
		return null;
	}

	@Override
	public boolean isLeaf( Object node ) {
		return getChildCount( node ) == 0;
	}

	@Override
	public void valueForPathChanged( TreePath path, Object newValue ) {
		System.out.println( "*** valueForPathChanged : "
				+ path + " --> " + newValue );

	}

	@Override
	public void propertyChanged( PropertyEvent e ) {
		System.out.println( e );
	}

	@Override
	public void propertyChanging( PropertyEvent e ) {
		System.out.println( e );
	}

	private final java.util.List<TreeModelListener> treeModelListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private Component rootComponent;
}
