/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class MultipleSelectionListState<T> extends /*todo*/AbstractCompletionModel {
	private static class DataListModel<T> extends javax.swing.AbstractListModel {
		private final org.lgna.croquet.data.ListData<T> data;

		//todo handle refresh/mutation
		public DataListModel( org.lgna.croquet.data.ListData<T> data ) {
			this.data = data;
		}

		public org.lgna.croquet.data.ListData<T> getData() {
			return this.data;
		}

		@Override
		public Object getElementAt( int index ) {
			return data.getItemAt( index );
		}

		@Override
		public int getSize() {
			return data.getItemCount();
		}
	}

	public static class SwingModel<T> {
		private final DataListModel<T> listModel;
		private final javax.swing.ListSelectionModel listSelectionModel = new javax.swing.DefaultListSelectionModel();

		public SwingModel( final org.lgna.croquet.data.ListData<T> data ) {
			this.listModel = new DataListModel<T>( data );
		}

		public javax.swing.ListModel getListModel() {
			return this.listModel;
		}

		public javax.swing.ListSelectionModel getListSelectionModel() {
			return this.listSelectionModel;
		}
	}

	private final java.util.List<org.lgna.croquet.event.ValueListener<java.util.List<T>>> newSchoolValueListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final SwingModel<T> swingModel;

	private boolean isInTheMidstOfSettingSwingValue;
	private final javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		@Override
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			if( isInTheMidstOfSettingSwingValue ) {
				//pass
			} else {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					fireChanged( getValue() );
				}
			}
		}
	};

	public MultipleSelectionListState( Group group, java.util.UUID migrationId, org.lgna.croquet.data.ListData<T> data ) {
		super( group, migrationId );
		this.swingModel = new SwingModel<T>( data );
		this.swingModel.listSelectionModel.addListSelectionListener( this.listSelectionListener );
	}

	public org.lgna.croquet.data.ListData<T> getData() {
		return this.swingModel.listModel.getData();
	}

	public SwingModel<T> getSwingModel() {
		return this.swingModel;
	}

	public java.util.List<T> getValue() {
		java.util.List<T> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		final int N = this.swingModel.listModel.data.getItemCount();
		for( int i = 0; i < N; i++ ) {
			if( this.swingModel.listSelectionModel.isSelectedIndex( i ) ) {
				rv.add( this.swingModel.listModel.data.getItemAt( i ) );
			}
		}
		return rv;
	}

	public void setValue( java.util.List<T> list ) {
		this.isInTheMidstOfSettingSwingValue = true;
		try {
			this.swingModel.listSelectionModel.setValueIsAdjusting( true );
			this.swingModel.listSelectionModel.clearSelection();
			synchronized( this.swingModel.listModel.data ) {
				final int N = this.swingModel.listModel.data.getItemCount();
				for( int i = 0; i < N; i++ ) {
					T item = this.swingModel.listModel.data.getItemAt( i );
					if( list.contains( item ) ) {
						this.swingModel.listSelectionModel.addSelectionInterval( i, i );
					}
				}
			}
			this.swingModel.listSelectionModel.setValueIsAdjusting( false );
		} finally {
			this.isInTheMidstOfSettingSwingValue = false;
		}
	}

	public void addNewSchoolValueListener( org.lgna.croquet.event.ValueListener<java.util.List<T>> valueListener ) {
		if( this.newSchoolValueListeners.contains( valueListener ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "listener already contained", this, valueListener );
		}
		this.newSchoolValueListeners.add( valueListener );
	}

	public void addAndInvokeNewSchoolValueListener( org.lgna.croquet.event.ValueListener<java.util.List<T>> valueListener ) {
		this.addNewSchoolValueListener( valueListener );
		org.lgna.croquet.event.ValueEvent<java.util.List<T>> e = org.lgna.croquet.event.ValueEvent.createInstance( this.getValue() );
		valueListener.valueChanged( e );
	}

	public void removeNewSchoolValueListener( org.lgna.croquet.event.ValueListener<java.util.List<T>> valueListener ) {
		if( this.newSchoolValueListeners.contains( valueListener ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "listener not contained", this, valueListener );
		}
		this.newSchoolValueListeners.remove( valueListener );
	}

	//	private void fireChanged( java.util.List<T> prevValue, java.util.List<T> nextValue, IsAdjusting isAdjusting ) {
	//		if( this.newSchoolValueListeners.size() > 0 ) {
	//			org.lgna.croquet.event.ValueEvent<java.util.List<T>> e = org.lgna.croquet.event.ValueEvent.createInstance( prevValue, nextValue, isAdjusting.getValue() );
	//			for( org.lgna.croquet.event.ValueListener<java.util.List<T>> valueListener : this.newSchoolValueListeners ) {
	//				valueListener.valueChanged( e );
	//			}
	//		}
	//	}
	private void fireChanged( java.util.List<T> nextValue ) {
		if( this.newSchoolValueListeners.size() > 0 ) {
			org.lgna.croquet.event.ValueEvent<java.util.List<T>> e = org.lgna.croquet.event.ValueEvent.createInstance( nextValue );
			for( org.lgna.croquet.event.ValueListener<java.util.List<T>> valueListener : this.newSchoolValueListeners ) {
				valueListener.valueChanged( e );
			}
		}
	}

	@Override
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		return java.util.Collections.emptyList();
	}

	@Override
	protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
	}

	@Override
	protected void localize() {
	}

	public org.lgna.croquet.views.MultipleSelectionListView<T> createMultipleSelectionListView() {
		return new org.lgna.croquet.views.MultipleSelectionListView<T>( this );
	}
}
