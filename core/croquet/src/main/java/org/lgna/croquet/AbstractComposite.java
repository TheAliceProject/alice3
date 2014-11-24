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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractComposite<V extends org.lgna.croquet.views.CompositeView<?, ?>> extends AbstractElement implements Composite<V> {
	protected static final class Key {
		private final AbstractComposite<?> composite;
		private final String localizationKey;

		private Key( AbstractComposite<?> composite, String localizationKey ) {
			this.composite = composite;
			this.localizationKey = localizationKey;
		}

		public AbstractComposite<?> getComposite() {
			return this.composite;
		}

		public String getLocalizationKey() {
			return this.localizationKey;
		}

		public String getPreferenceKey() {
			StringBuilder sb = new StringBuilder();
			sb.append( this.composite.getClass().getName() );
			sb.append( "_" );
			sb.append( this.localizationKey );
			return sb.toString();
		}

		@Override
		public boolean equals( Object o ) {
			if( o == this ) {
				return true;
			}
			if( o instanceof Key ) {
				Key key = (Key)o;
				return edu.cmu.cs.dennisc.java.util.Objects.equals( this.composite, key.composite ) && edu.cmu.cs.dennisc.java.util.Objects.equals( this.localizationKey, key.localizationKey );
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			int rv = 17;
			if( this.composite != null ) {
				rv = ( 37 * rv ) + this.composite.hashCode();
			}
			if( this.localizationKey != null ) {
				rv = ( 37 * rv ) + this.localizationKey.hashCode();
			}
			return rv;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( this.getClass().getSimpleName() );
			sb.append( "[" );
			sb.append( this.composite );
			sb.append( ";" );
			sb.append( this.localizationKey );
			sb.append( "]" );
			return sb.toString();
		}
	}

	protected static abstract class AbstractInternalStringValue extends PlainStringValue {
		private final Key key;

		public AbstractInternalStringValue( java.util.UUID id, Key key ) {
			super( id );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalStringValue extends AbstractInternalStringValue {
		private InternalStringValue( Key key ) {
			super( java.util.UUID.fromString( "142b66a2-0b95-42d0-8ea4-a22a79c8ff8c" ), key );
		}
	}

	private static final class InternalStringState extends StringState {
		private final Key key;

		private InternalStringState( String initialValue, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "ed65869f-8d26-48b1-8240-cf74ba403a2f" ), initialValue );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalPreferenceStringState extends org.lgna.croquet.preferences.PreferenceStringState {
		private final Key key;
		private final BooleanState isStoringPreferenceDesiredState;

		private InternalPreferenceStringState( String initialValue, Key key, BooleanState isStoringPreferenceDesiredState, java.util.UUID encryptionId ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "ad98acf0-db41-43a6-8619-269a7d8cbc2d" ), initialValue, key.getPreferenceKey(), getEncryptionKey( encryptionId != null ? encryptionId.toString() : null ) );
			this.key = key;
			this.isStoringPreferenceDesiredState = isStoringPreferenceDesiredState;
		}

		@Override
		protected boolean isStoringPreferenceDesired() {
			return ( this.isStoringPreferenceDesiredState == null ) || this.isStoringPreferenceDesiredState.getValue();
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalBooleanState extends BooleanState {
		private final Key key;

		private InternalBooleanState( boolean initialValue, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "5053e40f-9561-41c8-835d-069bd106723c" ), initialValue );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalPreferenceBooleanState extends org.lgna.croquet.preferences.PreferenceBooleanState {
		private final Key key;

		private InternalPreferenceBooleanState( boolean initialValue, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "034f99f7-74ec-4a89-8396-3c187d9684a2" ), initialValue, key.getPreferenceKey() );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalSingleSelectListState<T> extends SingleSelectListState<T, org.lgna.croquet.data.ListData<T>> {
		private final Key key;

		private InternalSingleSelectListState( int selectionIndex, org.lgna.croquet.data.ListData<T> data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "4f0640c9-eceb-4801-a8bb-bf8e282cef0f" ), selectionIndex, data );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalImmutableDataSingleSelectListState<T> extends ImmutableDataSingleSelectListState<T> {
		private final Key key;

		private InternalImmutableDataSingleSelectListState( int selectionIndex, ItemCodec<T> codec, T[] values, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "091d5251-d278-4eb1-8214-a27c154f5378" ), selectionIndex, codec, values );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalRefreshableDataSingleSelectListState<T> extends RefreshableDataSingleSelectListState<T> {
		private final Key key;

		private InternalRefreshableDataSingleSelectListState( int selectionIndex, org.lgna.croquet.data.RefreshableListData<T> data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "4d7ef91c-a8ae-4b17-9d8a-91ffac4ba12e" ), selectionIndex, data );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalMutableDataSingleSelectListState<T> extends MutableDataSingleSelectListState<T> {
		private final Key key;

		private InternalMutableDataSingleSelectListState( int selectionIndex, org.lgna.croquet.data.MutableListData<T> data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "6cc16988-0fc8-476b-9026-b19fd15748ea" ), selectionIndex, data );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalTabState<T extends SimpleTabComposite<?>> extends SimpleTabState<T> {
		private final Key key;

		public InternalTabState( int selectionIndex, Class<T> cls, T[] values, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "bea99c2f-45ad-40a8-a99c-9c125a72f0be" ), selectionIndex, cls, values );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	public static class BoundedIntegerDetails extends BoundedIntegerState.Details {
		public BoundedIntegerDetails() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "3cb7dfc5-de8c-442c-9e9a-deab2eff38e8" ) );
		}
	}

	private static final class InternalBoundedIntegerState extends BoundedIntegerState {
		private final Key key;

		private InternalBoundedIntegerState( BoundedIntegerState.Details details, Key key ) {
			super( details );
			this.key = key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	public static class BoundedDoubleDetails extends BoundedDoubleState.Details {
		public BoundedDoubleDetails() {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "603d4a60-cc60-41df-b5a5-992966128b41" ) );
		}
	}

	private static final class InternalBoundedDoubleState extends BoundedDoubleState {
		private final Key key;

		private InternalBoundedDoubleState( BoundedDoubleState.Details details, Key key ) {
			super( details );
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	protected static interface Action {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, InternalActionOperation source ) throws CancelException;
	}

	protected static final class InternalActionOperation extends ActionOperation {
		private final Action action;
		private final Key key;

		private InternalActionOperation( Action action, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "2c311356-2bf2-4a57-b06b-f6cdb39b0d78" ) );
			this.action = action;
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			try {
				org.lgna.croquet.edits.Edit edit = this.action.perform( step, this );
				if( edit != null ) {
					step.commitAndInvokeDo( edit );
				} else {
					step.finish();
				}
			} catch( CancelException ce ) {
				step.cancel();
			}
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	protected static interface CascadeCustomizer<T> {
		public void appendBlankChildren( java.util.List<CascadeBlankChild> rv, org.lgna.croquet.imp.cascade.BlankNode<T> blankNode );

		public org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep, T[] values );
	}

	protected static final class InternalCascadeWithInternalBlank<T> extends CascadeWithInternalBlank<T> {
		private final CascadeCustomizer<T> customizer;
		private final Key key;

		private InternalCascadeWithInternalBlank( CascadeCustomizer<T> customizer, Class<T> componentType, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "165e65a4-fd9b-4a09-921d-ecc3cc808de0" ), componentType );
			this.customizer = customizer;
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		@Override
		protected java.lang.Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}

		@Override
		protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<Cascade<T>> completionStep, T[] values ) {
			return this.customizer.createEdit( completionStep, values );
		}

		@Override
		protected java.util.List<CascadeBlankChild> updateBlankChildren( java.util.List<CascadeBlankChild> rv, org.lgna.croquet.imp.cascade.BlankNode<T> blankNode ) {
			this.customizer.appendBlankChildren( rv, blankNode );
			return rv;
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	protected static interface ItemStateCustomizer<T> {
		public CascadeFillIn<T, ?> getFillInFor( T value );

		public void appendBlankChildren( java.util.List<CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<T> blankNode );

		public void prologue( org.lgna.croquet.triggers.Trigger trigger );

		public void epilogue();
	}

	protected static final class InternalCustomItemState<T> extends DefaultCustomItemState<T> {
		private final ItemStateCustomizer<T> customizer;
		private final Key key;

		private InternalCustomItemState( ItemStateCustomizer<T> customizer, ItemCodec<T> itemCodec, T initialValue, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "eac974ec-8b09-4f1a-9a4c-7bae8f0780f1" ), itemCodec, initialValue );
			this.customizer = customizer;
			this.key = key;
		}

		public Key getKey() {
			return this.key;
		}

		public ItemStateCustomizer<T> getCustomizer() {
			return this.customizer;
		}

		@Override
		protected void prologue( org.lgna.croquet.triggers.Trigger trigger ) {
			super.prologue( trigger );
			this.customizer.prologue( trigger );
		}

		@Override
		protected void epilogue() {
			this.customizer.epilogue();
			super.epilogue();
		}

		@Override
		protected void updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<T> blankNode ) {
			this.customizer.appendBlankChildren( blankChildren, blankNode );
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalSplitComposite extends SplitComposite {
		private final boolean isHorizontal;
		private final double resizeWeight;

		private InternalSplitComposite( Composite<?> leadingComposite, Composite<?> trailingComposite, boolean isHorizontal, double resizeWeight ) {
			super( java.util.UUID.fromString( "0a7dee81-a213-4168-b71c-99b9e364dcf3" ), leadingComposite, trailingComposite );
			this.isHorizontal = isHorizontal;
			this.resizeWeight = resizeWeight;
		}

		@Override
		protected org.lgna.croquet.views.SplitPane createView() {
			org.lgna.croquet.views.SplitPane rv;
			if( this.isHorizontal ) {
				rv = this.createHorizontalSplitPane();
			} else {
				rv = this.createVerticalSplitPane();
			}
			rv.setResizeWeight( this.resizeWeight );
			return rv;
		}
	}

	private static final class InternalCardOwnerComposite extends CardOwnerComposite {
		private InternalCardOwnerComposite( Composite<?>... cards ) {
			super( java.util.UUID.fromString( "3a6b3b22-9c35-473b-96cf-f69640176948" ), cards );
		}
	}

	private java.util.UUID cardId;

	private V view;

	private final org.lgna.croquet.views.ScrollPane scrollPane;

	public AbstractComposite( java.util.UUID id ) {
		super( id );
		this.scrollPane = this.createScrollPaneIfDesired();
		Manager.registerComposite( this );
	}

	@Override
	public synchronized java.util.UUID getCardId() {
		if( this.cardId != null ) {

		} else {
			this.cardId = java.util.UUID.randomUUID();
		}
		return this.cardId;
	}

	protected abstract org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired();

	protected abstract V createView();

	protected V peekView() {
		return this.view;
	}

	@Override
	public synchronized final V getView() {
		if( this.view != null ) {
			//pass
		} else {
			this.view = this.createView();
			assert this.view != null : this;
			if( this.scrollPane != null ) {
				this.scrollPane.setViewportView( this.view );
			}
		}
		return this.view;
	}

	@Override
	public final org.lgna.croquet.views.ScrollPane getScrollPaneIfItExists() {
		return this.scrollPane;
	}

	@Override
	public final org.lgna.croquet.views.SwingComponentView<?> getRootComponent() {
		V view = this.getView();
		if( this.scrollPane != null ) {
			return this.scrollPane;
		} else {
			return view;
		}
	}

	@Override
	public void releaseView() {
		this.view = null;
	}

	private final java.util.List<Composite<?>> subComposites = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	protected <C extends Composite<?>> C registerSubComposite( C subComposite ) {
		this.subComposites.add( subComposite );
		return subComposite;
	}

	protected void unregisterSubComposite( Composite<?> subComposite ) {
		this.subComposites.remove( subComposite );
	}

	protected void registerTabState( TabState<?, ?> tabState ) {
		this.registeredTabStates.add( tabState );
	}

	protected void unregisterTabState( TabState<?, ?> tabState ) {
		this.registeredTabStates.remove( tabState );
	}

	@Override
	public void handlePreActivation() {
		this.initializeIfNecessary();
		this.getView().handleCompositePreActivation();
		for( Composite<?> subComposite : this.subComposites ) {
			subComposite.handlePreActivation();
		}
		for( TabState<?, ?> tabSelectionState : this.mapKeyToTabState.values() ) {
			tabSelectionState.handlePreActivation();
		}
		for( TabState<?, ?> tabSelectionState : this.registeredTabStates ) {
			tabSelectionState.handlePreActivation();
		}
	}

	@Override
	public void handlePostDeactivation() {
		this.getView().handleCompositePostDeactivation();
		for( TabState<?, ?> tabSelectionState : this.registeredTabStates ) {
			tabSelectionState.handlePostDeactivation();
		}
		for( TabState<?, ?> tabSelectionState : this.mapKeyToTabState.values() ) {
			tabSelectionState.handlePostDeactivation();
		}
		for( Composite<?> subComposite : this.subComposites ) {
			subComposite.handlePostDeactivation();
		}
	}

	private final java.util.Map<Key, AbstractInternalStringValue> mapKeyToStringValue = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalBooleanState> mapKeyToBooleanState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalPreferenceBooleanState> mapKeyToPreferenceBooleanState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalStringState> mapKeyToStringState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalPreferenceStringState> mapKeyToPreferenceStringState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalSingleSelectListState> mapKeyToSingleSelectListState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalImmutableDataSingleSelectListState> mapKeyToImmutableSingleSelectListState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalRefreshableDataSingleSelectListState> mapKeyToRefreshableSingleSelectListState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalMutableDataSingleSelectListState> mapKeyToMutableSingleSelectListState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalTabState> mapKeyToTabState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalBoundedIntegerState> mapKeyToBoundedIntegerState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalBoundedDoubleState> mapKeyToBoundedDoubleState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalActionOperation> mapKeyToActionOperation = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalCascadeWithInternalBlank> mapKeyToCascade = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<Key, InternalCustomItemState> mapKeyToItemState = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private final java.util.Set<TabState> registeredTabStates = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();

	//	private java.util.Map<Key, InternalCardOwnerComposite> mapKeyToCardOwnerComposite = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static final String SIDEKICK_LABEL_EPILOGUE = ".sidekickLabel";

	private void localizeSidekicks( java.util.Map<Key, ? extends AbstractCompletionModel>... maps ) {
		for( java.util.Map<Key, ? extends AbstractCompletionModel> map : maps ) {
			for( Key key : map.keySet() ) {
				AbstractCompletionModel model = map.get( key );
				String text = this.findLocalizedText( key.getLocalizationKey() + SIDEKICK_LABEL_EPILOGUE );
				if( text != null ) {
					StringValue sidekickLabel = model.getSidekickLabel();
					text = this.modifyLocalizedText( sidekickLabel, text );
					sidekickLabel.setText( text );
				} else {
					StringValue sidekickLabel = model.peekSidekickLabel();
					//todo: it is probably to early for this check as we don't know if it will be accessed by the composite's view later.  hmm...
					if( sidekickLabel != null ) {
						Class<?> cls = this.getClassUsedForLocalization();
						String localizationKey = cls.getSimpleName() + "." + key.getLocalizationKey() + SIDEKICK_LABEL_EPILOGUE;
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "WARNING: could not find localization for sidekick label" );
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "looking for:" );
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "   ", localizationKey );
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "in croquet.properties file in package:", cls.getPackage().getName() );
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln();
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( localizationKey, "has been copied to the clipboard for your convenience." );
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "if this does not solve your problem please feel free to ask dennis for help." );
						edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( localizationKey );
					}
				}
			}
		}
	}

	protected String modifyLocalizedText( Element element, String localizedText ) {
		return localizedText;
	}

	@Override
	protected void localize() {
		for( Key key : this.mapKeyToStringValue.keySet() ) {
			AbstractInternalStringValue stringValue = this.mapKeyToStringValue.get( key );
			stringValue.setText( this.modifyLocalizedText( stringValue, this.findLocalizedText( key.getLocalizationKey() ) ) );
		}
		this.localizeSidekicks(
				this.mapKeyToActionOperation,
				this.mapKeyToBooleanState,
				this.mapKeyToPreferenceBooleanState,
				this.mapKeyToBoundedDoubleState,
				this.mapKeyToBoundedIntegerState,
				this.mapKeyToCascade,
				this.mapKeyToItemState,
				this.mapKeyToImmutableSingleSelectListState,
				this.mapKeyToRefreshableSingleSelectListState,
				this.mapKeyToMutableSingleSelectListState,
				this.mapKeyToTabState,
				this.mapKeyToPreferenceStringState,
				this.mapKeyToStringState );
	}

	@Override
	public boolean contains( Model model ) {
		for( Key key : this.mapKeyToBooleanState.keySet() ) {
			InternalBooleanState state = this.mapKeyToBooleanState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToPreferenceBooleanState.keySet() ) {
			InternalPreferenceBooleanState state = this.mapKeyToPreferenceBooleanState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToStringState.keySet() ) {
			InternalStringState state = this.mapKeyToStringState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToPreferenceStringState.keySet() ) {
			InternalPreferenceStringState state = this.mapKeyToPreferenceStringState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToImmutableSingleSelectListState.keySet() ) {
			InternalImmutableDataSingleSelectListState state = this.mapKeyToImmutableSingleSelectListState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToRefreshableSingleSelectListState.keySet() ) {
			InternalRefreshableDataSingleSelectListState state = this.mapKeyToRefreshableSingleSelectListState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToMutableSingleSelectListState.keySet() ) {
			InternalMutableDataSingleSelectListState state = this.mapKeyToMutableSingleSelectListState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToTabState.keySet() ) {
			InternalTabState state = this.mapKeyToTabState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToBoundedIntegerState.keySet() ) {
			InternalBoundedIntegerState state = this.mapKeyToBoundedIntegerState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToBoundedDoubleState.keySet() ) {
			InternalBoundedDoubleState state = this.mapKeyToBoundedDoubleState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToActionOperation.keySet() ) {
			InternalActionOperation operation = this.mapKeyToActionOperation.get( key );
			if( model == operation ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToCascade.keySet() ) {
			InternalCascadeWithInternalBlank cascade = this.mapKeyToCascade.get( key );
			if( model == cascade ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToItemState.keySet() ) {
			InternalCustomItemState itemState = this.mapKeyToItemState.get( key );
			if( model == itemState ) {
				return true;
			}
		}
		return false;
	}

	protected void registerStringValue( AbstractInternalStringValue stringValue ) {
		this.mapKeyToStringValue.put( stringValue.getKey(), stringValue );
	}

	protected Key createKey( String localizationKey ) {
		return new Key( this, localizationKey );
	}

	protected PlainStringValue createStringValue( String keyText ) {
		Key key = this.createKey( keyText );
		InternalStringValue rv = new InternalStringValue( key );
		this.registerStringValue( rv );
		return rv;
	}

	protected StringState createStringState( String keyText, String initialValue ) {
		Key key = this.createKey( keyText );
		InternalStringState rv = new InternalStringState( initialValue, key );
		this.mapKeyToStringState.put( key, rv );
		return rv;
	}

	protected StringState createStringState( String keyText ) {
		return createStringState( keyText, "" );
	}

	protected org.lgna.croquet.preferences.PreferenceStringState createPreferenceStringState( String keyText, String initialValue, BooleanState isStoringPreferenceDesiredState, java.util.UUID encryptionId ) {
		Key key = this.createKey( keyText );
		InternalPreferenceStringState rv = new InternalPreferenceStringState( initialValue, key, isStoringPreferenceDesiredState, encryptionId );
		this.mapKeyToPreferenceStringState.put( key, rv );
		return rv;
	}

	protected org.lgna.croquet.preferences.PreferenceStringState createPreferenceStringState( String keyText, String initialValue, BooleanState isStoringPreferenceDesiredState ) {
		return createPreferenceStringState( keyText, initialValue, isStoringPreferenceDesiredState, null );
	}

	protected BooleanState createBooleanState( String keyText, boolean initialValue ) {
		Key key = this.createKey( keyText );
		InternalBooleanState rv = new InternalBooleanState( initialValue, key );
		this.mapKeyToBooleanState.put( key, rv );
		return rv;
	}

	protected org.lgna.croquet.preferences.PreferenceBooleanState createPreferenceBooleanState( String keyText, boolean initialValue ) {
		Key key = this.createKey( keyText );
		InternalPreferenceBooleanState rv = new InternalPreferenceBooleanState( initialValue, key );
		this.mapKeyToPreferenceBooleanState.put( key, rv );
		return rv;
	}

	protected BoundedIntegerState createBoundedIntegerState( String keyText, BoundedIntegerState.Details details ) {
		Key key = this.createKey( keyText );
		InternalBoundedIntegerState rv = new InternalBoundedIntegerState( details, key );
		this.mapKeyToBoundedIntegerState.put( key, rv );
		return rv;
	}

	protected BoundedDoubleState createBoundedDoubleState( String keyText, BoundedDoubleState.Details details ) {
		Key key = this.createKey( keyText );
		InternalBoundedDoubleState rv = new InternalBoundedDoubleState( details, key );
		this.mapKeyToBoundedDoubleState.put( key, rv );
		return rv;
	}

	protected ActionOperation createActionOperation( String keyText, Action action ) {
		Key key = this.createKey( keyText );
		InternalActionOperation rv = new InternalActionOperation( action, key );
		this.mapKeyToActionOperation.put( key, rv );
		return rv;
	}

	protected <T> Cascade<T> createCascadeWithInternalBlank( String keyText, Class<T> cls, CascadeCustomizer<T> customizer ) {
		Key key = this.createKey( keyText );
		InternalCascadeWithInternalBlank<T> rv = new InternalCascadeWithInternalBlank<T>( customizer, cls, key );
		this.mapKeyToCascade.put( key, rv );
		return rv;
	}

	protected <T> CustomItemState<T> createCustomItemState( String keyText, ItemCodec<T> itemCodec, T initialValue, ItemStateCustomizer<T> customizer ) {
		Key key = this.createKey( keyText );
		InternalCustomItemState<T> rv = new InternalCustomItemState<T>( customizer, itemCodec, initialValue, key );
		this.mapKeyToItemState.put( key, rv );
		return rv;
	}

	protected <T> SingleSelectListState<T, org.lgna.croquet.data.ListData<T>> createGenericListState( String keyText, org.lgna.croquet.data.ListData<T> data, int selectionIndex ) {
		Key key = this.createKey( keyText );
		InternalSingleSelectListState<T> rv = new InternalSingleSelectListState<T>( selectionIndex, data, key );
		this.mapKeyToSingleSelectListState.put( key, rv );
		return rv;
	}

	protected <T> ImmutableDataSingleSelectListState<T> createImmutableListState( String keyText, Class<T> valueCls, org.lgna.croquet.ItemCodec<T> codec, int selectionIndex, T... values ) {
		Key key = this.createKey( keyText );
		InternalImmutableDataSingleSelectListState<T> rv = new InternalImmutableDataSingleSelectListState<T>( selectionIndex, codec, values, key );
		this.mapKeyToImmutableSingleSelectListState.put( key, rv );
		return rv;
	}

	protected <T extends Enum<T>> ImmutableDataSingleSelectListState<T> createImmutableListStateForEnum( String keyText, Class<T> valueCls, org.lgna.croquet.codecs.EnumCodec.LocalizationCustomizer<T> localizationCustomizer, T initialValue ) {
		Key key = this.createKey( keyText );
		T[] constants = valueCls.getEnumConstants();
		int selectionIndex = java.util.Arrays.asList( constants ).indexOf( initialValue );
		org.lgna.croquet.codecs.EnumCodec<T> enumCodec = localizationCustomizer != null ? org.lgna.croquet.codecs.EnumCodec.createInstance( valueCls, localizationCustomizer ) : org.lgna.croquet.codecs.EnumCodec.getInstance( valueCls );
		InternalImmutableDataSingleSelectListState<T> rv = new InternalImmutableDataSingleSelectListState<T>( selectionIndex, enumCodec, constants, key );
		this.mapKeyToImmutableSingleSelectListState.put( key, rv );
		return rv;
	}

	protected <T extends Enum<T>> ImmutableDataSingleSelectListState<T> createImmutableListStateForEnum( String keyText, Class<T> valueCls, T initialValue ) {
		return this.createImmutableListStateForEnum( keyText, valueCls, null, initialValue );
	}

	protected <T> RefreshableDataSingleSelectListState<T> createRefreshableListState( String keyText, org.lgna.croquet.data.RefreshableListData<T> data, int selectionIndex ) {
		Key key = this.createKey( keyText );
		InternalRefreshableDataSingleSelectListState<T> rv = new InternalRefreshableDataSingleSelectListState<T>( selectionIndex, data, key );
		this.mapKeyToRefreshableSingleSelectListState.put( key, rv );
		return rv;
	}

	protected <T> MutableDataSingleSelectListState<T> createMutableListState( String keyText, Class<T> valueCls, org.lgna.croquet.ItemCodec<T> codec, int selectionIndex, T... values ) {
		Key key = this.createKey( keyText );
		InternalMutableDataSingleSelectListState<T> rv = new InternalMutableDataSingleSelectListState<T>( selectionIndex, new org.lgna.croquet.data.MutableListData<T>( codec, values ), key );
		this.mapKeyToMutableSingleSelectListState.put( key, rv );
		return rv;
	}

	protected <C extends SimpleTabComposite<?>> ImmutableDataTabState<C> createImmutableTabState( String keyText, int selectionIndex, Class<C> cls, C... tabComposites ) {
		Key key = this.createKey( keyText );
		InternalTabState<C> rv = new InternalTabState<C>( selectionIndex, cls, tabComposites, key );
		this.mapKeyToTabState.put( key, rv );
		return rv;
	}

	protected ImmutableDataTabState<SimpleTabComposite<?>> createImmutableTabState( String keyText, int selectionIndex, SimpleTabComposite<?>... tabComposites ) {
		return (ImmutableDataTabState)this.createImmutableTabState( keyText, selectionIndex, SimpleTabComposite.class, tabComposites );
	}

	protected SplitComposite createHorizontalSplitComposite( Composite<?> leadingComposite, Composite<?> trailingComposite, double resizeWeight ) {
		return this.registerSubComposite( new InternalSplitComposite( leadingComposite, trailingComposite, true, resizeWeight ) );
	}

	protected SplitComposite createVerticalSplitComposite( Composite<?> leadingComposite, Composite<?> trailingComposite, double resizeWeight ) {
		return this.registerSubComposite( new InternalSplitComposite( leadingComposite, trailingComposite, false, resizeWeight ) );
	}

	protected CardOwnerComposite createAndRegisterCardOwnerComposite( Composite<?>... cards ) {
		return this.registerSubComposite( this.createCardOwnerCompositeButDoNotRegister( cards ) );
	}

	protected CardOwnerComposite createCardOwnerCompositeButDoNotRegister( Composite<?>... cards ) {
		return new InternalCardOwnerComposite( cards );
	}
}
