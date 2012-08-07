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
public abstract class ValueCreatorInputDialogCoreComposite<V extends org.lgna.croquet.components.View<?,?>,T> extends InputDialogCoreComposite<V> implements ValueCreatorOwningComposite<V,T> {
	private final ValueCreator<T> valueCreator = new OwnedByCompositeValueCreator<T>( this );
	private String name;
	public ValueCreatorInputDialogCoreComposite( java.util.UUID migrationId ) {
		super( migrationId );
	}
	public ValueCreator<T> getValueCreator() {
		return this.valueCreator;
	}
	@Override
	protected void initialize() {
		super.initialize();
		this.valueCreator.initializeIfNecessary();
	}
	@Override
	protected void localize() {
		super.localize();
		this.name = this.findDefaultLocalizedText();
	}
	@Override
	protected String getName() {
		return this.name;
	}
	protected abstract T createValue();
	public T createValue( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		final Object[] buffer = { null };
		org.lgna.croquet.dialog.DialogUtilities.showDialog( new DialogOwner( this ) {
			@Override
			public void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
				Boolean isCommited = completionStep.getEphemeralDataFor( IS_COMMITED_KEY );
				if( isCommited != null ) { // close button condition
					if( isCommited ) {
						try {
							buffer[ 0 ] = createValue();
							completionStep.finish();
						} catch( CancelException ce ) {
							completionStep.cancel();
						}
					} else {
						completionStep.cancel();
					}
				} else {
					completionStep.cancel();
				}
			}
		}, completionStep );
		return (T)buffer[ 0 ];
	}
}
