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
public class LocalizeReviewFrame extends org.lgna.croquet.FrameCompositeWithInternalIsShowingState<org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView> {
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
	protected void localize() {
		super.localize();
		this.localeState.getSidekickLabel().setText( "locale:" );
		this.isIncludingUntranslatedState.setTextForBothTrueAndFalse( "include untranslated?" );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.localeState.addAndInvokeNewSchoolValueListener( this.localeListener );
		this.isIncludingUntranslatedState.addAndInvokeNewSchoolValueListener( this.isIncludingUntranslatedListener );
	}

	@Override
	public void handlePostDeactivation() {
		this.isIncludingUntranslatedState.removeNewSchoolValueListener( this.isIncludingUntranslatedListener );
		this.localeState.removeNewSchoolValueListener( this.localeListener );
		super.handlePostDeactivation();
	}

	@Override
	protected org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView createView() {
		return new org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView( this );
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<java.util.Locale> getLocaleState() {
		return this.localeState;
	}

	public org.lgna.croquet.BooleanState getIsIncludingUntranslatedState() {
		return this.isIncludingUntranslatedState;
	}

	public javax.swing.table.TableModel getTableModel() {
		return this.tableModel;
	}

	private final org.lgna.croquet.ImmutableDataSingleSelectListState<java.util.Locale> localeState = this.createImmutableListState( "localeState", java.util.Locale.class, org.alice.ide.croquet.codecs.LocaleCodec.SINGLETON, 0,
			new java.util.Locale( "pt" ),
			new java.util.Locale( "pt", "BR" ),
			new java.util.Locale( "es" ),
			new java.util.Locale( "fr" ),
			new java.util.Locale( "fr", "BE" ),
			new java.util.Locale( "it" ),
			new java.util.Locale( "nl" ),
			new java.util.Locale( "de" ),
			new java.util.Locale( "el" ),
			new java.util.Locale( "ro" ),
			new java.util.Locale( "cs" ),
			new java.util.Locale( "sl" ),
			new java.util.Locale( "lt" ),
			new java.util.Locale( "ru" ),
			new java.util.Locale( "uk" ),
			new java.util.Locale( "tr" ),
			new java.util.Locale( "ar" ),
			new java.util.Locale( "iw" ),
			new java.util.Locale( "in" ),
			new java.util.Locale( "zh", "CN" ),
			new java.util.Locale( "zh", "TW" ),
			new java.util.Locale( "ko" )
			);

	private final org.lgna.croquet.BooleanState isIncludingUntranslatedState = this.createBooleanState( "isIncludingUntranslatedState", false );

	private final org.lgna.croquet.event.ValueListener<java.util.Locale> localeListener = new org.lgna.croquet.event.ValueListener<java.util.Locale>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.util.Locale> e ) {
			tableModel.setLocale( e.getNextValue() );
		}
	};

	private final org.lgna.croquet.event.ValueListener<Boolean> isIncludingUntranslatedListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			tableModel.setIncludingUntranslated( e.getNextValue() );
		}
	};

	private final LocalizationTableModel tableModel;

	private static class LocalizationTableModel extends javax.swing.table.AbstractTableModel {
		private static class Item {
			public Item( String bundleName, String key, String defaultValue ) {
				this.bundleName = bundleName;
				this.key = key;
				this.defaultValue = defaultValue;
			}

			private final String bundleName;
			private final String key;
			private final String defaultValue;
			private String localizedValue;
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

			java.util.List<Item> _allItems = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

			for( String classPathEntry : classPathEntries ) {
				String bundleName = classPathEntry.substring( 0, classPathEntry.length() - SUFFIX.length() );
				java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( bundleName );
				for( String key : resourceBundle.keySet() ) {
					_allItems.add( new Item( bundleName, key, resourceBundle.getString( key ) ) );
				}
			}

			this.allItems = java.util.Collections.unmodifiableList( _allItems );
		}

		public void setLocale( java.util.Locale locale ) {
			java.util.List<Item> _translatedItems = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( Item item : this.allItems ) {
				java.util.ResourceBundle resourceBundleB = java.util.ResourceBundle.getBundle( item.bundleName, locale );
				item.localizedValue = resourceBundleB.getString( item.key );
			}
			this.translatedItems = java.util.Collections.unmodifiableList( _translatedItems );
			this.fireTableDataChanged();
		}

		public void setIncludingUntranslated( boolean isIncludingUntranslated ) {
			if( this.isIncludingUntranslated != isIncludingUntranslated ) {
				this.isIncludingUntranslated = isIncludingUntranslated;
				this.fireTableDataChanged();
			}
		}

		private java.util.List<Item> getItems() {
			if( this.isIncludingUntranslated ) {
				return this.allItems;
			} else {
				java.util.List<Item> _translatedItems = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				for( Item item : this.allItems ) {
					if( edu.cmu.cs.dennisc.java.util.Objects.equals( item.defaultValue, item.localizedValue ) ) {
						//pass
					} else {
						_translatedItems.add( item );
					}
				}
				this.translatedItems = java.util.Collections.unmodifiableList( _translatedItems );
				return this.translatedItems;
			}
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public int getRowCount() {
			return this.getItems().size();
		}

		@Override
		public Object getValueAt( int rowIndex, int columnIndex ) {
			Item item = this.getItems().get( rowIndex );
			switch( columnIndex ) {
			case 0:
				return rowIndex;
			case 1:
				return item.key;
			case 2:
				return item.defaultValue;
			case 3:
				if( edu.cmu.cs.dennisc.java.util.Objects.equals( item.defaultValue, item.localizedValue ) ) {
					return null;
				} else {
					return item.localizedValue;
				}
			case 4:
				return item.key;
			default:
				throw new Error();
			}
		}

		private final java.util.List<Item> allItems;
		private java.util.List<Item> translatedItems;
		private boolean isIncludingUntranslated;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
				org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
				LocalizeReviewFrame.getInstance().getIsFrameShowingState().getImp().getSwingModel().getButtonModel().setSelected( true );
			}
		} );
	}
}
