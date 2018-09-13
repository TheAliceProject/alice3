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

package org.lgna.croquet;

import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.croquet.triggers.IterationTrigger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class ValueCreator<T> extends AbstractCompletionModel implements Triggerable {
	//todo: edits are never created by value creators.  allow group specification anyways?
	public static final Group VALUE_CREATOR_GROUP = Group.getInstance( UUID.fromString( "4bef663b-1474-40ec-9731-4e2a2cb49333" ), "VALUE_CREATOR_GROUP" );

	private static final class InternalFillIn<F> extends ImmutableCascadeFillIn<F, Void> {
		private final ValueCreator<F> valueCreator;
		private String text;

		private InternalFillIn( ValueCreator<F> valueCreator ) {
			super( UUID.fromString( "258797f2-c1b6-4887-b6fc-42702493d573" ) );
			this.valueCreator = valueCreator;
		}

		@Override
		protected void localize() {
			super.localize();
			this.text = this.findDefaultLocalizedText();
		}

		public ValueCreator<F> getValueCreator() {
			return this.valueCreator;
		}

		@Override
		protected Class<? extends Element> getClassUsedForLocalization() {
			return this.valueCreator.getClassUsedForLocalization();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.valueCreator.getSubKeyForLocalization();
		}

		@Override
		protected JComponent createMenuItemIconProxy( ItemNode<? super F, Void> step ) {
			return new JLabel( this.text );
		}

		@Override
		public final F createValue( ItemNode<? super F, Void> node ) {
			return this.valueCreator.fireAndGetValue();
		}

		@Override
		public F getTransientValue( ItemNode<? super F, Void> node ) {
			return null;
		}
	}

	private InternalFillIn<T> fillIn = new InternalFillIn<T>( this );

	public ValueCreator( UUID migrationId ) {
		super( VALUE_CREATOR_GROUP, migrationId );
	}

	@Override
	protected void localize() {
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return Collections.emptyList();
	}

	public CascadeFillIn<T, Void> getFillIn() {
		return this.fillIn;
	}

	protected abstract T createValue( UserActivity transaction );

	@Override
	protected void performInActivity( UserActivity activity ) {
		T value = this.createValue( activity );
		activity.setProducedValue(value);
	}

	public T fireAndGetValue() throws CancelException {
		final UserActivity activity = Application.getActiveInstance().acquireOpenActivity().getActivityWithoutTrigger();
		IterationTrigger.createUserInstance( activity );
		fire( activity );
		if( activity.isSuccessfullyCompleted() ) {
			return (T)activity.getProducedValue();
		} else {
			throw new CancelException();
		}
	}

	@Override
	public void fire( UserActivity activity ) {
		if( this.isEnabled() ) {
			this.initializeIfNecessary();
			this.performInActivity( activity );
		}
	}
}
