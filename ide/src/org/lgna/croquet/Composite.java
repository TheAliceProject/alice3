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
public abstract class Composite< V extends org.lgna.croquet.components.View< ?, ? > > extends AbstractElement {
	protected static class Key {
		private final Composite<?> composite;
		private final String localizationKey;
		public Key( Composite<?> composite, String localizationKey ) {
			this.composite = composite;
			this.localizationKey = localizationKey;
		}
		public Composite<?> getComposite() {
			return this.composite;
		}
		public String getLocalizationKey() {
			return this.localizationKey;
		}
	}
	
	protected static abstract class AbstractInternalStringValue extends StringValue {
		private final Key key;
		public AbstractInternalStringValue( java.util.UUID id, Key key ) {
			super( id );
			this.key = key;
		}
		public Key getKey() {
			return this.key;
		}
		@Override
		protected void localize() {
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
		protected void localize() {
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
		protected void localize() {
		}
	}
	private static final class InternalListSelectionState<T> extends DefaultListSelectionState<T> {
		private final Key key;
		private InternalListSelectionState( ItemCodec< T > codec, int selectionIndex, T[] data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "6cc16988-0fc8-476b-9026-b19fd15748ea" ), codec, selectionIndex, data );
			this.key = key;
		}
		public Key getKey() {
			return this.key;
		}
		@Override
		protected void localize() {
		}
	}
	protected static class BoundedIntegerDetails extends BoundedIntegerState.Details {
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
		protected void localize() {
		}
	}
	protected static class BoundedDoubleDetails extends BoundedDoubleState.Details {
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
		protected void localize() {
		}
	}
	protected static interface Action {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) throws CancelException;
	}
	private static final class InternalActionOperation extends ActionOperation {
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
		protected void localize() {
		}
		@Override
		protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			try {
				org.lgna.croquet.edits.Edit edit = this.action.perform( transaction, trigger );
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
	
	

	
	public Composite( java.util.UUID id ) {
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
	private java.util.Map<Key,InternalListSelectionState> mapKeyToListSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalBoundedIntegerState> mapKeyToBoundedIntegerState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalBoundedDoubleState> mapKeyToBoundedDoubleState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalActionOperation> mapKeyToActionOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	@Override
	protected void localize() {
		for( Key key : this.mapKeyToStringValue.keySet() ) {
			AbstractInternalStringValue stringValue = this.mapKeyToStringValue.get( key );
			stringValue.setText( this.findLocalizedText( key.getLocalizationKey(), Composite.class ) );
		}
		for( Key key : this.mapKeyToBooleanState.keySet() ) {
			InternalBooleanState booleanState = this.mapKeyToBooleanState.get( key );
			//todo
			String trueAndFalseText = this.findLocalizedText( key.getLocalizationKey(), Composite.class );
			if( trueAndFalseText != null ) {
				booleanState.setTextForBothTrueAndFalse( trueAndFalseText );
			} else {
				String trueText = this.findLocalizedText( key.getLocalizationKey() + ".true", Composite.class );
				String falseText = this.findLocalizedText( key.getLocalizationKey() + ".false", Composite.class );
				booleanState.setTextForTrueAndTextForFalse( trueText, falseText );
			}
		}
		for( Key key : this.mapKeyToActionOperation.keySet() ) {
			InternalActionOperation operation = this.mapKeyToActionOperation.get( key );
			operation.setName( this.findLocalizedText( key.getLocalizationKey(), Composite.class ) );
		}
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
			InternalListSelectionState state = this.mapKeyToListSelectionState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToListSelectionState.keySet() ) {
			InternalBoundedIntegerState state = this.mapKeyToBoundedIntegerState.get( key );
			if( model == state ) {
				return true;
			}
		}
		for( Key key : this.mapKeyToListSelectionState.keySet() ) {
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
		return false;
	}

	protected void registerStringValue( AbstractInternalStringValue stringValue ) {
		this.mapKeyToStringValue.put( stringValue.getKey(), stringValue );
	}
	
	protected Key createKey( String localizationKey ) {
		return new Key( this, localizationKey );
	}
	protected StringValue createStringValue( Key key ) {
		InternalStringValue rv = new InternalStringValue( key );
		this.registerStringValue( rv );
		return rv;
	}
	protected StringState createStringState( String initialValue, Key key ) {
		InternalStringState rv = new InternalStringState( initialValue, key );
		this.mapKeyToStringState.put( key, rv );
		return rv;
	}
	protected BooleanState createBooleanState( boolean initialValue, Key key ) {
		InternalBooleanState rv = new InternalBooleanState( initialValue, key );
		this.mapKeyToBooleanState.put( key, rv );
		return rv;
	}
	protected BoundedIntegerState createBoundedIntegerState( BoundedIntegerState.Details details, Key key ) {
		InternalBoundedIntegerState rv = new InternalBoundedIntegerState( details, key );
		this.mapKeyToBoundedIntegerState.put( key, rv );
		return rv;
	}
	protected BoundedDoubleState createBoundedDoubleState( BoundedDoubleState.Details details, Key key ) {
		InternalBoundedDoubleState rv = new InternalBoundedDoubleState( details, key );
		this.mapKeyToBoundedDoubleState.put( key, rv );
		return rv;
	}
	protected ActionOperation createActionOperation( Action action, Key key ) {
		InternalActionOperation rv = new InternalActionOperation( action, key );
		this.mapKeyToActionOperation.put( key, rv );
		return rv;
	}
	
	
	
	protected <T extends Enum<T>> ListSelectionState<T> createListSelectionState( Class<T> valueCls, T initialValue, Key key ) {
		T[] constants = valueCls.getEnumConstants();
		int selectionIndex = java.util.Arrays.asList( constants ).indexOf( initialValue );
		InternalListSelectionState<T> rv = new InternalListSelectionState<T>( edu.cmu.cs.dennisc.toolkit.croquet.codecs.EnumCodec.getInstance( valueCls ), selectionIndex, constants, key );
		this.mapKeyToListSelectionState.put( key, rv );
		return rv;
	}
	
}
