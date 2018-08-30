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
package org.lgna.croquet.history;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Criterion;
import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.event.AddEvent;
import org.lgna.croquet.history.event.CancelEvent;
import org.lgna.croquet.history.event.EditCommittedEvent;
import org.lgna.croquet.history.event.FinishedEvent;
import org.lgna.croquet.triggers.IterationTrigger;
import org.lgna.croquet.triggers.Trigger;

import java.util.List;
import java.util.ListIterator;

public class UserActivity extends TransactionNode<UserActivity> {

	enum ActivityStatus {
		PENDING,
		CANCELED,
		FINISHED
	}

	private Trigger trigger;
	private final List<PrepStep<?>> prepSteps;
	private final List<UserActivity> childActivities;
	private CompletionStep<?> completionStep;

	private Object producedValue;
	private ActivityStatus status = ActivityStatus.PENDING;

	public UserActivity() {
		this(null);
	}

	private UserActivity( UserActivity parent) {
		super( parent );
		this.prepSteps = Lists.newLinkedList();
		this.childActivities = Lists.newCopyOnWriteArrayList();
		this.completionStep = null;
	}

	public UserActivity newChildActivity() {
		UserActivity child = new UserActivity( this );
		childActivities.add( child );
		child.fireChanged( new AddEvent<>( child ) );
		return child;
	}

	public Trigger getTrigger() {
		if (trigger == null) {
			trigger = IterationTrigger.createUserInstance( this );
		}
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public List<UserActivity> getChildActivities() {
		return childActivities;
	}

	public int getIndexOfTransaction( UserActivity childActivity ) {
		return getPrepStepCount() + childActivities.indexOf( childActivity );
	}

	public UserActivity getLatestActivity() {
			final int n = childActivities.size();
			return n > 0 && childActivities.get( n - 1 ).isPending()
					? childActivities.get( n - 1 ).getLatestActivity()
					: this;
	}

	Step<?> findAcceptableStep( Criterion<Step<?>> criterion ) {
		final int N = getPrepStepCount();
		for( int i = 0; i < N; i++ ) {
			PrepStep<?> prepStep = this.prepSteps.get( N - 1 - i );
			if( criterion.accept( prepStep ) ) {
				return prepStep;
			}
		}
		return getOwner() == null ? null: getOwner().findAcceptableStep(criterion);
	}

	public DropSite findDropSite() {
		return completionStep == null ? null : completionStep.findDropSite();
	}

	public MenuItemSelectStep findFirstMenuSelectStep() {
		for ( PrepStep step : prepSteps) {
			if (step instanceof MenuItemSelectStep) {
				return (MenuItemSelectStep) step;
			}
		}
		return null;
	}

	public void addMenuSelection( MenuSelection menuSelection ) {
		ListIterator<PrepStep<?>> iterator = this.prepSteps.listIterator( this.prepSteps.size() );
		while( iterator.hasPrevious() ) {
			PrepStep<?> prepStep = iterator.previous();
			if( prepStep instanceof MenuItemSelectStep ) {
				MenuItemSelectStep prevMenuItemSelectStep = (MenuItemSelectStep)prepStep;
				if( menuSelection.isPrevious( prevMenuItemSelectStep.getMenuBarComposite(), prevMenuItemSelectStep.getMenuItemPrepModels() ) ) {
					break;
				} else {
					iterator.remove();
				}
			} else {
				break;
			}
		}
		MenuItemSelectStep.createAndAddToTransaction( this, menuSelection.getMenuBarComposite(), menuSelection.getMenuItemPrepModels(), menuSelection.getTrigger() );
	}

	public Edit getEdit() {
		return completionStep != null ? completionStep.getEdit() : null;
	}

	public int getChildStepCount() {
		return getPrepStepCount() + childActivities.size() + ( completionStep != null ? 1 : 0 );
	}

	Step<?> getChildStepAt( int index ) {
		if( index >= getPrepStepCount() ) {
			return getCompletionStep();
		} else {
			return prepSteps.get( index );
		}
	}

	public TransactionNode<?> getChildAt( int index ) {
		if( index >= getPrepStepCount() ) {
			int childIndex = index - getPrepStepCount();
			if (childIndex >= childActivities.size()) {
				return completionStep;
			} else {
				return childActivities.get( childIndex );
			}
		} else {
			return prepSteps.get( index );
		}
	}

	int getIndexOfChildStep( Step<?> step ) {
		if( step instanceof PrepStep<?> ) {
			return getIndexOfPrepStep( (PrepStep<?>)step );
		} else {
			if( step == completionStep ) {
				return prepSteps.size();
			} else {
				return -1;
			}
		}
	}

	public int getIndexOfPrepStep( PrepStep<?> prepStep ) {
		return prepSteps.indexOf( prepStep );
	}

	private int getPrepStepCount() {
		return prepSteps.size();
	}

	void addPrepStep( PrepStep<?> step ) {
		AddEvent<Step> e = new AddEvent<>( step );
		prepSteps.add( step );
		step.fireChanged( e );
	}

	public CompletionStep<?> getCompletionStep() {
		return completionStep;
	}

	private void setCompletionStep( CompletionStep<?> step ) {
		AddEvent<Step> e = new AddEvent<>( step );
		completionStep = step;
		step.fireChanged( e );
	}

	public void setCompletionModel( CompletionModel model) {
		if (completionStep != null && model == completionStep.getModel()) {
			return;
		}
		setCompletionStep( new CompletionStep<>( this, model, getTrigger() ) );
	}

	public CompletionModel getCompletionModel() {
		return completionStep != null ? completionStep.getModel() : null;
	}

	public boolean isPending() {
		return ActivityStatus.PENDING == status;
	}

	public boolean isSuccessfullyCompleted() {
		return ActivityStatus.FINISHED == status;
	}

	public boolean isCanceled() {
		return ActivityStatus.CANCELED == status;
	}

	public void commitAndInvokeDo( Edit edit ) {
		status = ActivityStatus.FINISHED;
		if( completionStep == null ) {
			setCompletionStep( new CompletionStep<>( this, null, null ) );
		}
		completionStep.setEdit(edit);
		edit.doOrRedo( true );
		fireChanged( new EditCommittedEvent( edit ) );
	}

	public void finish() {
		status = ActivityStatus.FINISHED;
		if (getChildStepCount() == 0) {
			getOwner().childActivities.remove( this );
		}
		fireChanged( new FinishedEvent() );
	}

	public void cancel() {
		if (getChildStepCount() == 0) {
			getOwner().childActivities.remove( this );
		}
		status = ActivityStatus.CANCELED;
		fireChanged( new CancelEvent() );
	}

	public Object getProducedValue() {
		return producedValue;
	}

	public void setProducedValue( Object value ) {
		producedValue = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( getClass().getSimpleName() )
			.append( ' ' )
			.append( status )
			.append( "[" );
		for( PrepStep prepStep : prepSteps ) {
			sb.append( prepStep );
			sb.append( "," );
		}
		if (childActivities.size() == 1) {
			sb.append( "1 child," );
		}
		if (childActivities.size() > 1) {
			sb.append( childActivities.size())
				.append( " children," );
		}
		sb.append( completionStep );
		sb.append( "]" );
		return sb.toString();
	}
}
