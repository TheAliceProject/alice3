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
package org.alice.ide.localize.review.croquet;

import org.alice.ide.localize.review.core.Item;

/**
 * @author Dennis Cosgrove
 */
public class LocalizeReviewFrame extends org.lgna.croquet.FrameComposite<org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView> {
	private static final String SUFFIX = ".properties";
	private static final java.util.Map<java.util.Locale, java.util.Locale> mapLocaleToLocale;
	static {
		java.util.Map<java.util.Locale, java.util.Locale> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		map.put( new java.util.Locale( "pt" ), new java.util.Locale( "pt", "PT" ) );
		mapLocaleToLocale = java.util.Collections.unmodifiableMap( map );
	}

	public LocalizeReviewFrame() {
		super( java.util.UUID.fromString( "2652798a-f27d-4658-8907-3f0b6bb0aac2" ) );
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

	public String getBundleName( int row ) {
		return this.tableModel.getItems().get( row ).getBundleName();
	}

	public java.net.URI createUri( int row ) {
		java.util.Locale locale = this.getLocaleState().getValue();
		if( mapLocaleToLocale.containsKey( locale ) ) {
			locale = mapLocaleToLocale.get( locale );
		}
		String localeTag = edu.cmu.cs.dennisc.java.util.LocaleUtilities.toLanguageTag( locale );
		return this.tableModel.getItems().get( row ).createUri( localeTag );
	}

	private final org.lgna.croquet.ImmutableDataSingleSelectListState<java.util.Locale> localeState = this.createImmutableListState( "localeState", java.util.Locale.class, org.alice.ide.croquet.codecs.LocaleCodec.SINGLETON, 13,
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
			new java.util.Locale( "ko" ) );

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

		public LocalizationTableModel() {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			this.allItems = org.alice.ide.localize.review.core.LocalizeUtils.getItems( ide.getClass(), "alice-ide" );
		}

		public void setLocale( java.util.Locale locale ) {
			java.util.List<Item> _translatedItems = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( Item item : this.allItems ) {
				java.util.ResourceBundle resourceBundleB = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( item.getBundleName(), locale );
				try {
					item.setLocalizedValue( resourceBundleB.getString( item.getKey() ) );
				} catch( java.util.MissingResourceException mre ) {
					item.setLocalizedValue( "MissingResourceException" );
					mre.printStackTrace();
				}
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
					if( edu.cmu.cs.dennisc.java.util.Objects.equals( item.getDefaultValue(), item.getLocalizedValue() ) ) {
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
				return item.getKey();
			case 2:
				return item.getDefaultValue();
			case 3:
				if( edu.cmu.cs.dennisc.java.util.Objects.equals( item.getDefaultValue(), item.getLocalizedValue() ) ) {
					return null;
				} else {
					return item.getLocalizedValue();
				}
			case 4:
				return item;
			default:
				throw new Error();
			}
		}

		private final java.util.List<Item> allItems;
		private java.util.List<Item> translatedItems;
		private boolean isIncludingUntranslated;
	}
}
