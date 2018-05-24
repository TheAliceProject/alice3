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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.LocaleUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.IDE;
import org.alice.ide.croquet.codecs.LocaleCodec;
import org.alice.ide.localize.review.core.Item;
import org.alice.ide.localize.review.core.LocalizeUtils;
import org.alice.ide.localize.review.croquet.views.LocalizeReviewFrameView;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.ImmutableDataSingleSelectListState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class LocalizeReviewFrame extends FrameComposite<LocalizeReviewFrameView> {
	private static final String SUFFIX = ".properties";
	private static final Map<Locale, Locale> mapLocaleToLocale;
	static {
		Map<Locale, Locale> map = Maps.newHashMap();
		map.put( new Locale( "pt" ), new Locale( "pt", "PT" ) );
		mapLocaleToLocale = Collections.unmodifiableMap( map );
	}

	public LocalizeReviewFrame() {
		super( UUID.fromString( "2652798a-f27d-4658-8907-3f0b6bb0aac2" ) );
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
	protected LocalizeReviewFrameView createView() {
		return new LocalizeReviewFrameView( this );
	}

	public ImmutableDataSingleSelectListState<Locale> getLocaleState() {
		return this.localeState;
	}

	public BooleanState getIsIncludingUntranslatedState() {
		return this.isIncludingUntranslatedState;
	}

	public TableModel getTableModel() {
		return this.tableModel;
	}

	public String getBundleName( int row ) {
		return this.tableModel.getItems().get( row ).getBundleName();
	}

	public URI createUri( int row ) {
		Locale locale = this.getLocaleState().getValue();
		if( mapLocaleToLocale.containsKey( locale ) ) {
			locale = mapLocaleToLocale.get( locale );
		}
		String localeTag = LocaleUtilities.toLanguageTag( locale );
		return this.tableModel.getItems().get( row ).createUri( localeTag );
	}

	private final ImmutableDataSingleSelectListState<Locale> localeState = this.createImmutableListState( "localeState", Locale.class, LocaleCodec.SINGLETON, 13,
			new Locale( "pt" ),
			new Locale( "pt", "BR" ),
			new Locale( "es" ),
			new Locale( "fr" ),
			new Locale( "fr", "BE" ),
			new Locale( "it" ),
			new Locale( "nl" ),
			new Locale( "de" ),
			new Locale( "el" ),
			new Locale( "ro" ),
			new Locale( "cs" ),
			new Locale( "sl" ),
			new Locale( "lt" ),
			new Locale( "ru" ),
			new Locale( "uk" ),
			new Locale( "tr" ),
			new Locale( "ar" ),
			new Locale( "iw" ),
			new Locale( "in" ),
			new Locale( "zh", "CN" ),
			new Locale( "zh", "TW" ),
			new Locale( "ko" ) );

	private final BooleanState isIncludingUntranslatedState = this.createBooleanState( "isIncludingUntranslatedState", false );

	private final ValueListener<Locale> localeListener = new ValueListener<Locale>() {
		@Override
		public void valueChanged( ValueEvent<Locale> e ) {
			tableModel.setLocale( e.getNextValue() );
		}
	};

	private final ValueListener<Boolean> isIncludingUntranslatedListener = new ValueListener<Boolean>() {
		@Override
		public void valueChanged( ValueEvent<Boolean> e ) {
			tableModel.setIncludingUntranslated( e.getNextValue() );
		}
	};

	private final LocalizationTableModel tableModel;

	private static class LocalizationTableModel extends AbstractTableModel {

		public LocalizationTableModel() {
			IDE ide = IDE.getActiveInstance();
			this.allItems = LocalizeUtils.getItems( ide.getClass(), "alice-ide" );
		}

		public void setLocale( Locale locale ) {
			List<Item> _translatedItems = Lists.newLinkedList();
			for( Item item : this.allItems ) {
				ResourceBundle resourceBundleB = ResourceBundleUtilities.getUtf8Bundle( item.getBundleName(), locale );
				try {
					item.setLocalizedValue( resourceBundleB.getString( item.getKey() ) );
				} catch( MissingResourceException mre ) {
					item.setLocalizedValue( "MissingResourceException" );
					mre.printStackTrace();
				}
			}
			this.translatedItems = Collections.unmodifiableList( _translatedItems );
			this.fireTableDataChanged();
		}

		public void setIncludingUntranslated( boolean isIncludingUntranslated ) {
			if( this.isIncludingUntranslated != isIncludingUntranslated ) {
				this.isIncludingUntranslated = isIncludingUntranslated;
				this.fireTableDataChanged();
			}
		}

		private List<Item> getItems() {
			if( this.isIncludingUntranslated ) {
				return this.allItems;
			} else {
				List<Item> _translatedItems = Lists.newLinkedList();
				for( Item item : this.allItems ) {
					if( Objects.equals( item.getDefaultValue(), item.getLocalizedValue() ) ) {
						//pass
					} else {
						_translatedItems.add( item );
					}
				}
				this.translatedItems = Collections.unmodifiableList( _translatedItems );
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
				if( Objects.equals( item.getDefaultValue(), item.getLocalizedValue() ) ) {
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

		private final List<Item> allItems;
		private List<Item> translatedItems;
		private boolean isIncludingUntranslated;
	}
}
