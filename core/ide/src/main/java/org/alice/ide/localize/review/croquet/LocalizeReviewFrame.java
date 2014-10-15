/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.localize.review.croquet;

/**
 * @author Dennis Cosgrove
 */
public class LocalizeReviewFrame extends org.lgna.croquet.FrameComposite<org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView> {
	private static final String SUFFIX = ".properties";

	private static class SingletonHolder {
		private static LocalizeReviewFrame instance = new LocalizeReviewFrame();
	}

	public static LocalizeReviewFrame getInstance() {
		return SingletonHolder.instance;
	}

	private LocalizeReviewFrame() {
		super( java.util.UUID.fromString( "2652798a-f27d-4658-8907-3f0b6bb0aac2" ), org.lgna.croquet.Application.INFORMATION_GROUP );
		this.tableModel = new LocalizationTableModel();
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.localeState.addAndInvokeNewSchoolValueListener( this.localeListener );
	}

	@Override
	public void handlePostDeactivation() {
		this.localeState.removeNewSchoolValueListener( this.localeListener );
		super.handlePostDeactivation();
	}

	@Override
	protected org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView createView() {
		return new org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView( this );
	}

	public org.lgna.croquet.SingleSelectListState<java.util.Locale> getLocaleState() {
		return this.localeState;
	}

	public javax.swing.table.TableModel getTableModel() {
		return this.tableModel;
	}

	private final org.lgna.croquet.SingleSelectListState<java.util.Locale> localeState = this.createSingleSelectListState( "localeState", java.util.Locale.class, org.alice.ide.croquet.codecs.LocaleCodec.SINGLETON, 0,
			new java.util.Locale( "ru" ),
			new java.util.Locale( "ar" ),
			new java.util.Locale( "es" ),
			new java.util.Locale( "pt" )
			);

	private final org.lgna.croquet.event.ValueListener<java.util.Locale> localeListener = new org.lgna.croquet.event.ValueListener<java.util.Locale>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.util.Locale> e ) {
			tableModel.setLocale( e.getNextValue() );
		}
	};

	private final LocalizationTableModel tableModel;

	private static class LocalizationTableModel extends javax.swing.table.AbstractTableModel {
		private static class Item {
			public Item( String bundleName, String key, String defaultValue ) {
				this.bundleName = bundleName;
				this.key = key;
				this.defaultValue = defaultValue;

				this.action.putValue( javax.swing.Action.NAME, "review" );
			}

			private final String bundleName;
			private final String key;
			private final String defaultValue;
			private String localizedValue;
			private final javax.swing.Action action = new javax.swing.AbstractAction() {
				@Override
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "http://alice.andrew.cmu.edu/localize/narro_project_text_list.php?l=ru&p=5&tf=1&st=3&s=" + key );
				}
			};
			private final javax.swing.JButton button = new javax.swing.JButton( action );
		}

		public LocalizationTableModel() {
			Class<?> cls = org.alice.ide.IDE.class;
			java.util.List<String> classPathEntries;
			try {
				classPathEntries = edu.cmu.cs.dennisc.classpath.ClassPathUtilities.getClassPathEntries( cls, new edu.cmu.cs.dennisc.pattern.Criterion<String>() {
					@Override
					public boolean accept( String path ) {
						if( path.endsWith( SUFFIX ) ) {
							return path.contains( "_" ) == false;
						} else {
							return false;
						}
					}
				} );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}

			java.util.List<Item> _items = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

			for( String classPathEntry : classPathEntries ) {
				String bundleName = classPathEntry.substring( 0, classPathEntry.length() - SUFFIX.length() );
				java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( bundleName );
				for( String key : resourceBundle.keySet() ) {
					_items.add( new Item( bundleName, key, resourceBundle.getString( key ) ) );
				}
			}

			this.items = java.util.Collections.unmodifiableList( _items );

		}

		public void setLocale( java.util.Locale locale ) {
			for( Item item : this.items ) {
				java.util.ResourceBundle resourceBundleB = java.util.ResourceBundle.getBundle( item.bundleName, locale );
				item.localizedValue = resourceBundleB.getString( item.key );
			}
			this.fireTableDataChanged();
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public int getRowCount() {
			return this.items.size();
		}

		@Override
		public Object getValueAt( int rowIndex, int columnIndex ) {
			switch( columnIndex ) {
			case 0:
				return rowIndex;
			case 1:
				return this.items.get( rowIndex ).key;
			case 2:
				return this.items.get( rowIndex ).defaultValue;
			case 3:
				Item item = this.items.get( rowIndex );
				if( java.util.Objects.equals( item.defaultValue, item.localizedValue ) ) {
					return null;
				} else {
					return item.localizedValue;
				}
			case 4:
				return this.items.get( rowIndex ).button;
			default:
				throw new Error();
			}
		}

		private final java.util.List<Item> items;
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		LocalizeReviewFrame.getInstance().getIsFrameShowingState().getImp().getSwingModel().getButtonModel().setSelected( true );
	}
}
