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
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.event.ChangeEvent;
import org.lgna.croquet.history.event.CancelEvent;
import org.lgna.croquet.history.event.EditCommittedEvent;
import org.lgna.croquet.history.event.FinishedEvent;
import org.lgna.croquet.triggers.DragTrigger;
import org.lgna.croquet.triggers.DropTrigger;
import org.lgna.croquet.triggers.Trigger;

import java.util.List;
import java.util.ListIterator;

public class UserActivity extends ActivityNode<CompletionModel> {

	enum ActivityStatus {
		PENDING,
		CANCELED,
		FINISHED
	}
	private final List<PrepStep<?>> prepSteps;
	private final List<UserActivity> childActivities;
	private Edit edit;

	private Object producedValue;
	private ActivityStatus status = ActivityStatus.PENDING;

	public UserActivity() {
		this(null);
	}

	private UserActivity( UserActivity parent) {
		super( parent );
		this.prepSteps = Lists.newLinkedList();
		this.childActivities = Lists.newCopyOnWriteArrayList();
	}

	public UserActivity newChildActivity() {
		UserActivity child = new UserActivity( this );
		childActivities.add( child );
		child.fireChanged( new ChangeEvent<>( child ) );
		return child;
	}

	public UserActivity getActivityWithoutTrigger() {
		return getTrigger() != null ? newChildActivity() : this;
	}

	public UserActivity getActivityWithoutModel() {
		return getModel() != null ? newChildActivity() : this;
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

	private Trigger findAcceptableTrigger( Criterion<Trigger> criterion ) {
		if( criterion.accept( trigger ) ) {
			return trigger;
		}
		final int N = getPrepStepCount();
		for( int i = 0; i < N; i++ ) {
			PrepStep<?> prepStep = this.prepSteps.get( N - 1 - i );
			if( criterion.accept( prepStep.trigger ) ) {
				return prepStep.trigger;
			}
		}
		return getOwner() == null ? null: getOwner().findAcceptableTrigger(criterion);
	}

	public DropSite findDropSite() {
		DropTrigger dropTrigger = (DropTrigger) findAcceptableTrigger( trig -> trig instanceof DropTrigger );
		return dropTrigger != null ? dropTrigger.getDropSite() : null;
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
				if( menuSelection.isPrevious( prevMenuItemSelectStep.getMenuSelection() ) ) {
					break;
				} else {
					iterator.remove();
				}
			} else {
				break;
			}
		}
		MenuItemSelectStep.createAndAddToActivity( this, menuSelection, trigger );
	}

	public <M extends DragModel> DragStep addDragStep( DragModel model, DragTrigger trigger ) {
		return new DragStep( this, model, trigger );
	}

	public Edit getEdit() {
		return edit;
	}

	public int getChildStepCount() {
		return getPrepStepCount() + childActivities.size();
	}

	public ActivityNode getChildAt( int index ) {
		if( index >= getPrepStepCount() ) {
			return childActivities.get( index - getPrepStepCount() );
		} else {
			return prepSteps.get( index );
		}
	}

	public int getIndexOfPrepStep( PrepStep<?> prepStep ) {
		return prepSteps.indexOf( prepStep );
	}

	private int getPrepStepCount() {
		return prepSteps.size();
	}

	void addPrepStep( PrepStep<?> step ) {
		prepSteps.add( step );
		step.fireChanged( new ChangeEvent<PrepStep>( step ) );
	}

	public void setCompletionModel( CompletionModel model) {
		this.model = model;
		fireChanged( new ChangeEvent<UserActivity>( this ) );
	}

	public CompletionModel getCompletionModel() {
		return getModel();
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
		this.edit = edit;
		edit.doOrRedo( true );
		fireChanged( new EditCommittedEvent( edit ) );
	}

	public void finish() {
		status = ActivityStatus.FINISHED;
		removeIfEmpty();
		fireChanged( new FinishedEvent() );
	}

	public void cancel() {
		removeIfEmpty();
		status = ActivityStatus.CANCELED;
		fireChanged( new CancelEvent() );
	}

	private void removeIfEmpty() {
		if ( childActivities.isEmpty() && prepSteps.isEmpty() && model == null && edit == null) {
			getOwner().childActivities.remove( this );
			getOwner().fireChanged( new ChangeEvent<UserActivity>( getOwner() ) );
		}
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
			sb.append( "1 child" );
		}
		if (childActivities.size() > 1) {
			sb.append( childActivities.size())
				.append( " children" );
		}
		sb.append( "]" );
		return sb.toString();
	}
}