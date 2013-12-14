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
public abstract class AbstractComposite<V extends org.lgna.croquet.views.View<?, ?>> extends AbstractElement implements Composite<V> {
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
				return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.composite, key.composite ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.localizationKey, key.localizationKey );
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

	public static abstract class KeyResolver<M extends Model> implements org.lgna.croquet.resolvers.Resolver<M> {
		private org.lgna.croquet.resolvers.Resolver<AbstractComposite<?>> compositeResolver;
		private final String localizationKey;

		public KeyResolver( Key key ) {
			this.compositeResolver = key.composite.getResolver();
			this.localizationKey = key.localizationKey;
		}

		public KeyResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.compositeResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
			this.localizationKey = binaryDecoder.decodeString();
		}

		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			binaryEncoder.encode( this.compositeResolver );
			binaryEncoder.encode( this.localizationKey );
		}

		protected abstract M getResolved( Key key );

		public final M getResolved() {
			AbstractComposite<?> composite = this.compositeResolver.getResolved();
			Key key = new Key( composite, this.localizationKey );
			return this.getResolved( key );
		}

		public void retarget( org.lgna.croquet.Retargeter retargeter ) {
			this.compositeResolver.retarget( retargeter );
		}
	}

	public static final class BooleanStateKeyResolver extends KeyResolver<BooleanState> {
		public BooleanStateKeyResolver( Key key ) {
			super( key );
		}

		public BooleanStateKeyResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected BooleanState getResolved( Key key ) {
			return key.getComposite().mapKeyToBooleanState.get( key );
		}
	}

	public static final class PreferenceBooleanStateKeyResolver extends KeyResolver<org.lgna.croquet.preferences.PreferenceBooleanState> {
		public PreferenceBooleanStateKeyResolver( Key key ) {
			super( key );
		}

		public PreferenceBooleanStateKeyResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected org.lgna.croquet.preferences.PreferenceBooleanState getResolved( Key key ) {
			return key.getComposite().mapKeyToPreferenceBooleanState.get( key );
		}
	}

	public static final class StringStateKeyResolver extends KeyResolver<StringState> {
		public StringStateKeyResolver( Key key ) {
			super( key );
		}

		public StringStateKeyResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected StringState getResolved( Key key ) {
			return key.getComposite().mapKeyToStringState.get( key );
		}
	}

	public static final class PreferenceStringStateKeyResolver extends KeyResolver<StringState> {
		public PreferenceStringStateKeyResolver( Key key ) {
			super( key );
		}

		public PreferenceStringStateKeyResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected org.lgna.croquet.preferences.PreferenceStringState getResolved( Key key ) {
			return key.getComposite().mapKeyToPreferenceStringState.get( key );
		}
	}

	public static final class ItemStateKeyResolver<T> extends KeyResolver<ItemState<T>> {
		public ItemStateKeyResolver( Key key ) {
			super( key );
		}

		public ItemStateKeyResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected ItemState<T> getResolved( Key key ) {
			return key.getComposite().mapKeyToItemState.get( key );
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
		protected StringStateKeyResolver createResolver() {
			return new StringStateKeyResolver( this.getKey() );
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
		protected PreferenceStringStateKeyResolver createResolver() {
			return new PreferenceStringStateKeyResolver( this.getKey() );
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
		protected BooleanStateKeyResolver createResolver() {
			return new BooleanStateKeyResolver( this.getKey() );
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
		protected PreferenceBooleanStateKeyResolver createResolver() {
			return new PreferenceBooleanStateKeyResolver( this.getKey() );
		}

		@Override
		protected void appendRepr( java.lang.StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";key=" );
			sb.append( this.key );
		}
	}

	private static final class InternalImmutableListSelectionState<T> extends ImmutableDataListSelectionState<T> {
		private final Key key;

		private InternalImmutableListSelectionState( ItemCodec<T> codec, T[] data, int selectionIndex, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "091d5251-d278-4eb1-8214-a27c154f5378" ), codec, data, selectionIndex );
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

	private static final class InternalRefreshableListSelectionState<T> extends RefreshableDataListSelectionState<T> {
		private final Key key;

		private InternalRefreshableListSelectionState( org.lgna.croquet.data.RefreshableListData<T> data, int selectionIndex, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "4d7ef91c-a8ae-4b17-9d8a-91ffac4ba12e" ), data, selectionIndex );
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

	private static final class InternalMutableListSelectionState<T> extends MutableDataListSelectionState<T> {
		private final Key key;

		private InternalMutableListSelectionState( ItemCodec<T> codec, int selectionIndex, T[] data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "6cc16988-0fc8-476b-9026-b19fd15748ea" ), codec, selectionIndex, data );
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

	private static final class InternalTabSelectionState<T extends SimpleTabComposite<?>> extends SimpleTabSelectionState<T> {
		private final Key key;

		public InternalTabSelectionState( Class<T> cls, int selectionIndex, T[] data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "bea99c2f-45ad-40a8-a99c-9c125a72f0be" ), cls, data, selectionIndex );
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
		protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep, T[] values ) {
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
		public org.lgna.croquet.history.Transaction addGeneratedStateChangeTransaction( org.lgna.croquet.history.TransactionHistory history, T prevValue, T nextValue ) throws org.lgna.croquet.UnsupportedGenerationException {
			org.lgna.croquet.history.Transaction rv = super.addGeneratedStateChangeTransaction( history, prevValue, nextValue );
			CascadeFillIn<T, ?> fillIn = this.customizer.getFillInFor( nextValue );
			if( fillIn != null ) {
				org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( rv, null, new MenuItemPrepModel[] { fillIn }, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance() );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "cannot find fillin for", nextValue );
			}
			return rv;
		}

		@Override
		protected void updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<T> blankNode ) {
			this.customizer.appendBlankChildren( blankChildren, blankNode );
		}

		@Override
		protected ItemStateKeyResolver<T> createResolver() {
			return new ItemStateKeyResolver<T>( this.getKey() );
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

	public final org.lgna.croquet.views.ScrollPane getScrollPaneIfItExists() {
		return this.scrollPane;
	}

	public final org.lgna.croquet.views.JComponent<?> getRootComponent() {
		V view = this.getView();
		if( this.scrollPane != null ) {
			return this.scrollPane;
		} else {
			return view;
		}
	}

	public void releaseView() {
		this.view = null;
	}

	private final java.util.List<Composite<?>> subComposites = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	protected <C extends Composite<?>> C registerSubComposite( C subComposite ) {
		this.subComposites.add( subComposite );
		return subComposite;
	}

	protected void unregisterSubComposite( Composite<?> subComposite ) {
		this.subComposites.remove( subComposite );
	}

	protected void registerTabSelectionState( TabSelectionState<?> tabSelectionState ) {
		this.registeredTabSelectionStates.add( tabSelectionState );
	}

	protected void unregisterTabSelectionState( TabSelectionState<?> tabSelectionState ) {
		this.registeredTabSelectionStates.remove( tabSelectionState );
	}

	public void handlePreActivation() {
		this.initializeIfNecessary();
		this.getView().handleCompositePreActivation();
		for( Composite<?> subComposite : this.subComposites ) {
			subComposite.handlePreActivation();
		}
		for( TabSelectionState<?> tabSelectionState : this.mapKeyToTabSelectionState.values() ) {
			tabSelectionState.handlePreActivation();
		}
		for( TabSelectionState<?> tabSelectionState : this.registeredTabSelectionStates ) {
			tabSelectionState.handlePreActivation();
		}
	}

	public void handlePostDeactivation() {
		this.getView().handleCompositePostDeactivation();
		for( TabSelectionState<?> tabSelectionState : this.registeredTabSelectionStates ) {
			tabSelectionState.handlePostDeactivation();
		}
		for( TabSelectionState<?> tabSelectionState : this.mapKeyToTabSelectionState.values() ) {
			tabSelectionState.handlePostDeactivation();
		}
		for( Composite<?> subComposite : this.subComposites ) {
			subComposite.handlePostDeactivation();
		}
	}

	private java.util.Map<Key, AbstractInternalStringValue> mapKeyToStringValue = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalBooleanState> mapKeyToBooleanState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalPreferenceBooleanState> mapKeyToPreferenceBooleanState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalStringState> mapKeyToStringState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalPreferenceStringState> mapKeyToPreferenceStringState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalImmutableListSelectionState> mapKeyToImmutableListSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalRefreshableListSelectionState> mapKeyToRefreshableListSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalMutableListSelectionState> mapKeyToMutableListSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalTabSelectionState> mapKeyToTabSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalBoundedIntegerState> mapKeyToBoundedIntegerState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalBoundedDoubleState> mapKeyToBoundedDoubleState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalActionOperation> mapKeyToActionOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalCascadeWithInternalBlank> mapKeyToCascade = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key, InternalCustomItemState> mapKeyToItemState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	private java.util.Set<TabSelectionState> registeredTabSelectionStates = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();

	//	private java.util.Map<Key, InternalCardOwnerComposite> mapKeyToCardOwnerComposite = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

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
				this.mapKeyToImmutableListSelectionState,
				this.mapKeyToRefreshableListSelectionState,
				this.mapKeyToMutableListSelectionState,
				this.mapKeyToTabSelectionState,
				this.mapKeyToPreferenceStringState,
				this.mapKeyToStringState );
	}

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
		for( Key key : this.mapKeyToImmutableListSelectionState.keySet() ) {
			InternalImmutableListSelectionState state = this.mapKeyToImmutableListSelectionState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToRefreshableListSelectionState.keySet() ) {
			InternalRefreshableListSelectionState state = this.mapKeyToRefreshableListSelectionState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToMutableListSelectionState.keySet() ) {
			InternalMutableListSelectionState state = this.mapKeyToMutableListSelectionState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToTabSelectionState.keySet() ) {
			InternalTabSelectionState state = this.mapKeyToTabSelectionState.get( key );
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

	protected PlainStringValue createStringValue( Key key ) {
		InternalStringValue rv = new InternalStringValue( key );
		this.registerStringValue( rv );
		return rv;
	}

	protected StringState createStringState( Key key, String initialValue ) {
		InternalStringState rv = new InternalStringState( initialValue, key );
		this.mapKeyToStringState.put( key, rv );
		return rv;
	}

	protected StringState createStringState( Key key ) {
		return createStringState( key, "" );
	}

	protected org.lgna.croquet.preferences.PreferenceStringState createPreferenceStringState( Key key, String initialValue, BooleanState isStoringPreferenceDesiredState ) {
		return createPreferenceStringState( key, initialValue, isStoringPreferenceDesiredState, null );
	}

	protected org.lgna.croquet.preferences.PreferenceStringState createPreferenceStringState( Key key, String initialValue, BooleanState isStoringPreferenceDesiredState, java.util.UUID encryptionId ) {
		InternalPreferenceStringState rv = new InternalPreferenceStringState( initialValue, key, isStoringPreferenceDesiredState, encryptionId );
		this.mapKeyToPreferenceStringState.put( key, rv );
		return rv;
	}

	protected BooleanState createBooleanState( Key key, boolean initialValue ) {
		InternalBooleanState rv = new InternalBooleanState( initialValue, key );
		this.mapKeyToBooleanState.put( key, rv );
		return rv;
	}

	protected org.lgna.croquet.preferences.PreferenceBooleanState createPreferenceBooleanState( Key key, boolean initialValue ) {
		InternalPreferenceBooleanState rv = new InternalPreferenceBooleanState( initialValue, key );
		this.mapKeyToPreferenceBooleanState.put( key, rv );
		return rv;
	}

	protected BoundedIntegerState createBoundedIntegerState( Key key, BoundedIntegerState.Details details ) {
		InternalBoundedIntegerState rv = new InternalBoundedIntegerState( details, key );
		this.mapKeyToBoundedIntegerState.put( key, rv );
		return rv;
	}

	protected BoundedDoubleState createBoundedDoubleState( Key key, BoundedDoubleState.Details details ) {
		InternalBoundedDoubleState rv = new InternalBoundedDoubleState( details, key );
		this.mapKeyToBoundedDoubleState.put( key, rv );
		return rv;
	}

	protected ActionOperation createActionOperation( Key key, Action action ) {
		InternalActionOperation rv = new InternalActionOperation( action, key );
		this.mapKeyToActionOperation.put( key, rv );
		return rv;
	}

	protected <T> Cascade<T> createCascadeWithInternalBlank( Key key, Class<T> cls, CascadeCustomizer<T> customizer ) {
		InternalCascadeWithInternalBlank<T> rv = new InternalCascadeWithInternalBlank<T>( customizer, cls, key );
		this.mapKeyToCascade.put( key, rv );
		return rv;
	}

	protected <T> CustomItemState<T> createCustomItemState( Key key, ItemCodec<T> itemCodec, T initialValue, ItemStateCustomizer<T> customizer ) {
		InternalCustomItemState<T> rv = new InternalCustomItemState<T>( customizer, itemCodec, initialValue, key );
		this.mapKeyToItemState.put( key, rv );
		return rv;
	}

	protected <T> MutableDataListSelectionState<T> createListSelectionState( Key key, Class<T> valueCls, org.lgna.croquet.ItemCodec<T> codec, int selectionIndex, T... values ) {
		InternalMutableListSelectionState<T> rv = new InternalMutableListSelectionState<T>( codec, selectionIndex, values, key );
		this.mapKeyToMutableListSelectionState.put( key, rv );
		return rv;
	}

	protected <T extends Enum<T>> ImmutableDataListSelectionState<T> createListSelectionStateForEnum( Key key, Class<T> valueCls, org.lgna.croquet.codecs.EnumCodec.LocalizationCustomizer<T> localizationCustomizer, T initialValue ) {
		T[] constants = valueCls.getEnumConstants();
		int selectionIndex = java.util.Arrays.asList( constants ).indexOf( initialValue );
		org.lgna.croquet.codecs.EnumCodec<T> enumCodec = localizationCustomizer != null ? org.lgna.croquet.codecs.EnumCodec.createInstance( valueCls, localizationCustomizer ) : org.lgna.croquet.codecs.EnumCodec.getInstance( valueCls );
		InternalImmutableListSelectionState<T> rv = new InternalImmutableListSelectionState<T>( enumCodec, constants, selectionIndex, key );
		this.mapKeyToImmutableListSelectionState.put( key, rv );
		return rv;
	}

	protected <T extends Enum<T>> ImmutableDataListSelectionState<T> createListSelectionStateForEnum( Key key, Class<T> valueCls, T initialValue ) {
		return this.createListSelectionStateForEnum( key, valueCls, null, initialValue );
	}

	protected <T> RefreshableDataListSelectionState<T> createListSelectionState( Key key, org.lgna.croquet.data.RefreshableListData<T> data, int selectionIndex ) {
		InternalRefreshableListSelectionState<T> rv = new InternalRefreshableListSelectionState<T>( data, selectionIndex, key );
		this.mapKeyToRefreshableListSelectionState.put( key, rv );
		return rv;
	}

	protected <C extends SimpleTabComposite<?>> TabSelectionState<C> createTabSelectionState( Key key, Class<C> cls, int selectionIndex, C... tabComposites ) {
		InternalTabSelectionState<C> rv = new InternalTabSelectionState<C>( cls, selectionIndex, tabComposites, key );
		this.mapKeyToTabSelectionState.put( key, rv );
		return rv;
	}

	protected TabSelectionState<SimpleTabComposite> createTabSelectionState( Key key, int selectionIndex, SimpleTabComposite... tabComposites ) {
		return this.createTabSelectionState( key, SimpleTabComposite.class, selectionIndex, tabComposites );
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
