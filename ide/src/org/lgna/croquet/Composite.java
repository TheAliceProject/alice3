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
	
	protected static interface Action {
		public void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger );
	}
		
	private static class InternalStringState extends StringState {
		private final Key key;
		public InternalStringState( String initialValue, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "ed65869f-8d26-48b1-8240-cf74ba403a2f" ), initialValue );
			this.key = key;
		}
		@Override
		protected void localize() {
		}
	}
	private static class InternalBooleanState extends BooleanState {
		private final Key key;
		public InternalBooleanState( boolean initialValue, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "5053e40f-9561-41c8-835d-069bd106723c" ), initialValue );
			this.key = key;
		}
		@Override
		protected void localize() {
		}
	}
	private static class InternalListSelectionState<T> extends ListSelectionState<T> {
		private final Key key;
		public InternalListSelectionState( ItemCodec< T > codec, int selectionIndex, T[] data, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "6cc16988-0fc8-476b-9026-b19fd15748ea" ), codec, selectionIndex, data );
			this.key = key;
		}
		@Override
		protected void localize() {
		}
	}
	private static class InternalActionOperation extends ActionOperation {
		private final Action action;
		private final Key key;
		public InternalActionOperation( Action action, Key key ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "2c311356-2bf2-4a57-b06b-f6cdb39b0d78" ) );
			this.action = action;
			this.key = key;
		}
		@Override
		protected void localize() {
		}
		@Override
		protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			this.action.perform( transaction, trigger );
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
	}
	public void handlePostDectivation() {
	}
	
	private java.util.Map<Key,InternalBooleanState> mapKeyToBooleanState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalStringState> mapKeyToStringState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalListSelectionState> mapKeyToListSelectionState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.Map<Key,InternalActionOperation> mapKeyToActionOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	@Override
	protected void localize() {
		for( Key key : this.mapKeyToBooleanState.keySet() ) {
			InternalBooleanState booleanState = this.mapKeyToBooleanState.get( key );
			booleanState.setTextForBothTrueAndFalse( this.getLocalizedText( key.getLocalizationKey() ) );
		}
		for( Key key : this.mapKeyToActionOperation.keySet() ) {
			InternalActionOperation operation = this.mapKeyToActionOperation.get( key );
			operation.setName( this.getLocalizedText( key.getLocalizationKey() ) );
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
		for( Key key : this.mapKeyToActionOperation.keySet() ) {
			InternalActionOperation operation = this.mapKeyToActionOperation.get( key );
			if( model == operation ) {
				return true;
			}
		}
		return false;
	}

	protected Key createKey( String localizationKey ) {
		return new Key( this, localizationKey );
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
