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
public abstract class AbstractComposite< V extends org.lgna.croquet.components.View< ?, ? > > extends AbstractElement implements Composite<V> {
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
		@Override
		public boolean equals( Object o ) {
			if( o == this )
				return true;
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
				rv = 37*rv + this.composite.hashCode();
			}
			if( this.localizationKey != null ) {
				rv = 37*rv + this.localizationKey.hashCode();
			}
			return rv;
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
	
	private static final class UnlocalizedPlainStringValue extends PlainStringValue { 
		public UnlocalizedPlainStringValue( String text ) {
			super( java.util.UUID.randomUUID() );
			this.setText( text );
		}
		@Override
		protected void localize() {
		}
	}
	private static final class UnlocalizedHtmlStringValue extends HtmlStringValue { 
		public UnlocalizedHtmlStringValue( String text ) {
			super( java.util.UUID.randomUUID() );
			this.setText( text );
		}
		@Override
		protected void localize() {
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
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}
		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
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
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
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
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}
		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}
	}
	private static final class InternalDefaultListSelectionState<T> extends DefaultListSelectionState<T> {
		private final Key key;
		private InternalDefaultListSelectionState( ItemCodec< T > codec, int selectionIndex, T[] data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "6cc16988-0fc8-476b-9026-b19fd15748ea" ), codec, selectionIndex, data );
			this.key = key;
		}
		public Key getKey() {
			return this.key;
		}
		@Override
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}
		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
		}
	}
	private static final class InternalTabSelectionState<T extends SimpleTabComposite<?>> extends SimpleTabSelectionState<T> {
		private final Key key;
		public InternalTabSelectionState( Class<T> cls, int selectionIndex, T[] data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "bea99c2f-45ad-40a8-a99c-9c125a72f0be" ), cls, selectionIndex, data );
			this.key = key;
		}
		public Key getKey() {
			return this.key;
		}
		@Override
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}
		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
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
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}
		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
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
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.key.composite.getClass();
		}
		@Override
		protected String getSubKeyForLocalization() {
			return this.key.localizationKey;
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
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
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
	}

	protected static interface CascadeCustomizer<T> {
		public void appendBlankChildren( java.util.List<CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<T> blankNode );
		public org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep, T[] values );
	}
	protected static final class InternalCascadeWithInternalBlank<T> extends CascadeWithInternalBlank<T> {
		private final CascadeCustomizer<T> customizer;
		private final Key key;
		private InternalCascadeWithInternalBlank( CascadeCustomizer<T> customizer, Class< T > componentType, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "165e65a4-fd9b-4a09-921d-ecc3cc808de0" ), componentType );
			this.customizer = customizer;
			this.key = key;
		}
		public Key getKey() {
			return this.key;
		}
		@Override
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
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
		protected java.util.List< CascadeBlankChild > updateBlankChildren( java.util.List< CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< T > blankNode ) {
			this.customizer.appendBlankChildren( rv, blankNode );
			return rv;
		}
	}
	
	protected static interface ItemStateCustomizer<T> {
		public CascadeFillIn<T,?> getFillInFor( T value );
		public void appendBlankChildren( java.util.List<CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<T> blankNode );
	}
	protected static final class InternalCustomItemState<T> extends DefaultCustomItemState<T> {
		private final ItemStateCustomizer<T> customizer;
		private final Key key;
		private InternalCustomItemState( ItemStateCustomizer<T> customizer, ItemCodec< T > itemCodec, T initialValue, Key key ) {
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
		public org.lgna.croquet.history.Transaction addGeneratedStateChangeTransaction(org.lgna.croquet.history.TransactionHistory history, T prevValue, T nextValue) {
			org.lgna.croquet.history.Transaction rv = super.addGeneratedStateChangeTransaction( history, prevValue, nextValue );
			CascadeFillIn<T,?> fillIn = this.customizer.getFillInFor( nextValue );
			if( fillIn != null ) {
				org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( rv, null, new MenuItemPrepModel[] { fillIn }, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance() );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "cannot find fillin for", nextValue );
			}
			return rv;
		}
		@Override
		protected java.util.List<org.lgna.croquet.CascadeBlankChild> updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<T> blankNode ) {
			this.customizer.appendBlankChildren( rv, blankNode );
			return rv;
		}
		@Override
		protected ItemStateKeyResolver<T> createResolver() {
			return new ItemStateKeyResolver<T>( this.getKey() );
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
		protected org.lgna.croquet.components.SplitPane createView() {
			org.lgna.croquet.components.SplitPane rv;
			if( this.isHorizontal ) {
				rv = this.createHorizontalSplitPane();
			} else {
				rv = this.createVerticalSplitPane();
			}
			rv.setResizeWeight( this.resizeWeight );
			return rv;
		}
	}
	
	public AbstractComposite( java.util.UUID id ) {
		super( id );
		Manager.registerComposite( this );
	}
	private V view;
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
		}
		return this.view;
	}
	public void releaseView() {
		this.view = null;
	}
	public void handlePreActivation() {
		this.initializeIfNecessary();
	}
	public void handlePostDeactivation() {
	}
	
	private java.util.Map<Key,AbstractInternalStringValue> mapKeyToStringValue = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalBooleanState> mapKeyToBooleanState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalStringState> mapKeyToStringState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalDefaultListSelectionState> mapKeyToListSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalTabSelectionState> mapKeyToTabSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalBoundedIntegerState> mapKeyToBoundedIntegerState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalBoundedDoubleState> mapKeyToBoundedDoubleState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalActionOperation> mapKeyToActionOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalCascadeWithInternalBlank> mapKeyToCascade = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalCustomItemState> mapKeyToItemState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private void localizeSidekicks( java.util.Map<Key,? extends AbstractCompletionModel>... maps ) {
		for( java.util.Map<Key,? extends AbstractCompletionModel> map : maps ) {
			for( Key key : map.keySet() ) {
				AbstractCompletionModel model = map.get( key );
				String text = this.findLocalizedText( key.getLocalizationKey() + ".sidekickLabel" );
				if( text != null ) {
					StringValue sidekickLabel = model.getSidekickLabel();
					sidekickLabel.setText( text );
				}
			}
		}
	}
	
	@Override
	protected void localize() {
		for( Key key : this.mapKeyToStringValue.keySet() ) {
			AbstractInternalStringValue stringValue = this.mapKeyToStringValue.get( key );
			stringValue.setText( this.findLocalizedText( key.getLocalizationKey() ) );
		}
		this.localizeSidekicks( this.mapKeyToActionOperation, this.mapKeyToBooleanState, this.mapKeyToBoundedDoubleState, this.mapKeyToBoundedIntegerState, this.mapKeyToCascade, this.mapKeyToItemState, this.mapKeyToListSelectionState, this.mapKeyToTabSelectionState, this.mapKeyToStringState );
	}
	public boolean contains( Model model ) {
		for( Key key : this.mapKeyToBooleanState.keySet() ) {
			InternalBooleanState state = this.mapKeyToBooleanState.get( key );
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
		for( Key key : this.mapKeyToListSelectionState.keySet() ) {
			InternalDefaultListSelectionState state = this.mapKeyToListSelectionState.get( key );
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
	protected PlainStringValue createUnlocalizedPlainStringValue( String text ) {
		UnlocalizedPlainStringValue rv = new UnlocalizedPlainStringValue( text );
		return rv;
	}
	protected HtmlStringValue createUnlocalizedHtmlStringValue( String text ) {
		UnlocalizedHtmlStringValue rv = new UnlocalizedHtmlStringValue( text );
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
	protected BooleanState createBooleanState( Key key, boolean initialValue ) {
		InternalBooleanState rv = new InternalBooleanState( initialValue, key );
		this.mapKeyToBooleanState.put( key, rv );
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
	
	protected <T> Cascade<T> createCascadeWithInternalBlank( Key key, Class<T> cls, CascadeCustomizer< T > customizer ) {
		InternalCascadeWithInternalBlank< T > rv = new InternalCascadeWithInternalBlank< T >( customizer, cls, key );
		this.mapKeyToCascade.put( key, rv );
		return rv;
	}
	
	protected <T> CustomItemState<T> createCustomItemState( Key key, ItemCodec<T> itemCodec, T initialValue, ItemStateCustomizer< T > customizer ) {
		InternalCustomItemState< T > rv = new InternalCustomItemState< T >( customizer, itemCodec, initialValue, key );
		this.mapKeyToItemState.put( key, rv );
		return rv;
	}

	protected <T> ListSelectionState<T> createListSelectionState( Key key, Class<T> valueCls, org.lgna.croquet.ItemCodec< T > codec, int selectionIndex, T... values ) {
		InternalDefaultListSelectionState<T> rv = new InternalDefaultListSelectionState<T>( codec, selectionIndex, values, key );
		this.mapKeyToListSelectionState.put( key, rv );
		return rv;
	}
	protected <T extends Enum<T>> ListSelectionState<T> createListSelectionStateForEnum( Key key, Class<T> valueCls, T initialValue ) {
		T[] constants = valueCls.getEnumConstants();
		int selectionIndex = java.util.Arrays.asList( constants ).indexOf( initialValue );
		return createListSelectionState( key, valueCls, edu.cmu.cs.dennisc.toolkit.croquet.codecs.EnumCodec.getInstance( valueCls ), selectionIndex, constants );
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
		return new InternalSplitComposite( leadingComposite, trailingComposite, true, resizeWeight );
	}
	protected SplitComposite createVerticalSplitComposite( Composite<?> leadingComposite, Composite<?> trailingComposite, double resizeWeight ) {
		return new InternalSplitComposite( leadingComposite, trailingComposite, false, resizeWeight );
	}
}
