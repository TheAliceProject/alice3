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
package edu.cmu.cs.dennisc.property;

//public class ListProperty<E> extends Property<E> implements java.util.List<E> {
/**
 * @author Dennis Cosgrove
 */
public class ListProperty<E> extends InstanceProperty< java.util.ArrayList< E > > implements Iterable< E > {
	private java.util.List< edu.cmu.cs.dennisc.property.event.ListPropertyListener< E >> m_listPropertyListeners = null;

	public ListProperty( InstancePropertyOwner owner ) {
		super( owner, new java.util.ArrayList< E >() );
	}

	public void addListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l ) {
		if( m_listPropertyListeners != null ) {
			//pass
		} else {
			m_listPropertyListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > >();
		}
		synchronized( m_listPropertyListeners ) {
			m_listPropertyListeners.add( l );
		}
	}
	public void removeListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l ) {
		assert m_listPropertyListeners != null;
		synchronized( m_listPropertyListeners ) {
			m_listPropertyListeners.remove( l );
		}
	}

	private void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.adding( e );
			}
		}
		getOwner().fireAdding( e );
	}
	private void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.added( e );
			}
		}
		getOwner().fireAdded( e );
	}
	private void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.clearing( e );
			}
		}
		getOwner().fireClearing( e );
	}
	private void fireCleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.cleared( e );
			}
		}
		getOwner().fireCleared( e );
	}
	private void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.removing( e );
			}
		}
		getOwner().fireRemoving( e );
	}
	private void fireRemoved( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.removed( e );
			}
		}
		getOwner().fireRemoved( e );
	}
	private void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.setting( e );
			}
		}
		getOwner().fireSetting( e );
	}
	private void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
		if( m_listPropertyListeners != null ) {
			for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > l : m_listPropertyListeners ) {
				l.set( e );
			}
		}
		getOwner().fireSet( e );
	}

	public Object[] toArray() {
		return getValue().toArray();
	}

	public <T> T[] toArray( T[] a ) {
		return getValue().toArray( a );
	}
	public boolean isEmpty() {
		return getValue().isEmpty();
	}
	public boolean contains( Object o ) {
		return getValue().contains( o );
	}
	public boolean containsAll( java.util.Collection< ? > c ) {
		return getValue().containsAll( c );
	}
	public int size() {
		return getValue().size();
	}
	public java.util.List< E > subList( int fromIndex, int toIndex ) {
		return getValue().subList( fromIndex, toIndex );
	}

	public int indexOf( Object element ) {
		return getValue().indexOf( element );
	}

	public int lastIndexOf( Object element ) {
		return getValue().lastIndexOf( element );
	}

	public E get( int index ) {
		return getValue().get( index );
	}


	//todo: investigate returning iterator that could allow modification when isLocked 
	public java.util.Iterator< E > iterator() {
		return getValue().iterator();
	}
	//	//todo: investigate returning iterator that could allow modification when isLocked 
	//	public java.util.ListIterator<E> listIterator() {
	//		return m_list.listIterator();
	//	}
	//
	//	//todo: investigate returning iterator that could allow modification when isLocked 
	//	public java.util.ListIterator<E> listIterator( int index ) {
	//		return m_list.listIterator( arg0 );
	//	}

	//todo
	//	public boolean remove( Object o ) {
	//		assert isLocked() == false;
	//		return getValue().remove( o );
	//	}
	//	public boolean removeAll( java.util.Collection<?> c ) {
	//		assert isLocked() == false;
	//		return getValue().removeAll( c );
	//	}
	//	public boolean retainAll( java.util.Collection<?> c ) {
	//		assert isLocked() == false;
	//		return getValue().retainAll( c );
	//	}



	public void add( int index, E... elements ) {
		//assert isLocked() == false;
		edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e = new edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E >( this, index, elements );
		fireAdding( e );
		getValue().ensureCapacity( size() + elements.length );
		int i = index;
		for( E element : elements ) {
			getValue().add( i++, element );
		}
		fireAdded( e );
	}
	public void add( E... elements ) {
		add( size(), elements );
	}
	

	public boolean addAll( int index, java.util.Collection< ? extends E > collection ) {
		//assert isLocked() == false;
//		E[] array = (E[])java.lang.reflect.Array.newInstance( E.class, collection.size() );
//		E[] array = new E[ collection.size() ];
//		collection.toArray( array );

		edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e = new edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E >( this, index, collection );
		fireAdding( e );
		boolean rv = getValue().addAll( index, collection );
		fireAdded( e );
		return rv;
	}
	public boolean addAll( java.util.Collection< ? extends E > c ) {
		return addAll( size(), c );
	}

	public void clear() {
		//assert isLocked() == false;
		edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e = new edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E >( this );
		fireClearing( e );
		getValue().clear();
		fireCleared( e );
	}

	public void remove( int fromIndex, int toIndex ) {
		//assert isLocked() == false;
		edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e = new edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E >( this, fromIndex, getValue().subList( fromIndex, toIndex ) );
		fireRemoving( e );
		for( int i=fromIndex; i<toIndex; i++ ) {
			getValue().remove( i );
		}
		fireRemoved( e );
	}
	public E remove( int index ) {
		E rv = get( index );
		remove( index, index+1 );
		return rv;
	}

	public void set( int index, E... elements ) {
		//assert isLocked() == false;
		edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e = new edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E >( this, index, elements );
		fireSetting( e );
		for( int i=0; i<elements.length; i++ ) {
			getValue().set( index+i, elements[ i ] );
		}
		fireSet( e );
	}

	@Override
	public void setValue( PropertyOwner owner, java.util.ArrayList< E > value ) {
		
		//todo?
		
		edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > eClear = new edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E >( this );
		edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > eAdd = new edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E >( this, 0, value );
		fireClearing( eClear );
		fireAdding( eAdd );
		super.setValue( owner, value );
		fireCleared( eClear );
		fireAdded( eAdd );
	}

	//	@Override
	//	public void encodeXML( org.w3c.dom.Document xmlDocument, org.w3c.dom.Element xmlElement ) {
	//		org.w3c.dom.Element xmlChild = xmlDocument.createElement( getName() );
	//		for( E e : getValue() ) {
	//			org.w3c.dom.Element xmlItem = xmlDocument.createElement( "item" );
	//			encodeXML( xmlDocument, xmlChild, xmlItem, e );
	//		}
	//		xmlElement.appendChild( xmlChild );
	//	}
	//	@Override
	//	public void decodeXML( org.w3c.dom.Document xmlDocument, org.w3c.dom.Element xmlElement ) {
	//
	//	}
	//
	//	@Override
	//	public void appendValue( StringBuffer sb ) {
	//		sb.append( "{" );
	//		boolean isFirst = true;
	//		for( E e : getValue() ) {
	//			if( isFirst ) {
	//				//pass
	//			} else {
	//				sb.append( ", " );
	//			}
	//			//todo
	//			if( e instanceof Node ) {
	//				sb.append( "instance of " );
	//				sb.append( e.getClass() );
	//			} else {
	//				sb.append( e );
	//			}
	//			isFirst = false;
	//		}
	//		sb.append( "}" );
	//	}
}
