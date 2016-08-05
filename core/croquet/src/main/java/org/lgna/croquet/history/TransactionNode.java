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

/**
 * @author Dennis Cosgrove
 */
public abstract class TransactionNode<P extends TransactionNode<?>> {
	private final java.util.List<org.lgna.croquet.history.event.Listener> listeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private P owner;

	public TransactionNode( P owner ) {
		this.setOwner( owner );
	}

	public P getOwner() {
		return this.owner;
	}

	/* package-private */void setOwner( P owner ) {
		this.owner = owner;
	}

	protected abstract void appendContexts( java.util.List<org.lgna.croquet.Context> out );

	private <N extends TransactionNode<?>> N findNodeAssignableTo( Class<N> cls, boolean isThisIncludedInSearch ) {
		TransactionNode<?> rv;
		if( isThisIncludedInSearch ) {
			rv = this;
		} else {
			rv = this.getOwner();
		}
		while( rv != null ) {
			if( cls.isAssignableFrom( rv.getClass() ) ) {
				break;
			}
			rv = rv.getOwner();
		}
		return (N)rv;
	}

	private Step<?> findAcceptableStep( edu.cmu.cs.dennisc.pattern.Criterion<Step<?>> criterion, boolean isThisIncludedInSearch ) {
		TransactionNode<?> rv;
		if( isThisIncludedInSearch ) {
			rv = this;
		} else {
			rv = this.getOwner();
		}
		while( rv != null ) {
			if( rv instanceof Step ) {
				Step<?> step = (Step<?>)rv;
				if( criterion.accept( step ) ) {
					return step;
				}
			} else if( rv instanceof Transaction ) {
				Transaction transaction = (Transaction)rv;
				final int N = transaction.getPrepStepCount();
				for( int i = 0; i < N; i++ ) {
					PrepStep<?> prepStep = transaction.getPrepStepAt( N - 1 - i );
					if( criterion.accept( prepStep ) ) {
						return prepStep;
					}
				}
			}
			rv = rv.getOwner();
		}
		return null;
	}

	public final <N extends TransactionNode<?>> N getFirstAssignableTo( Class<N> cls ) {
		return this.findNodeAssignableTo( cls, true );
	}

	public final <N extends TransactionNode<?>> N getFirstAncestorAssignableTo( Class<N> cls ) {
		return this.findNodeAssignableTo( cls, false );
	}

	public org.lgna.croquet.DropSite findDropSite() {
		Step<?> step = this.findAcceptableStep( new edu.cmu.cs.dennisc.pattern.Criterion<Step<?>>() {
			@Override
			public boolean accept( Step<?> step ) {
				org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
				if( trigger instanceof org.lgna.croquet.triggers.DropTrigger ) {
					org.lgna.croquet.triggers.DropTrigger dropTrigger = (org.lgna.croquet.triggers.DropTrigger)trigger;
					return true;
				}
				return false;
			}
		}, true );
		if( step != null ) {
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			if( trigger instanceof org.lgna.croquet.triggers.DropTrigger ) {
				org.lgna.croquet.triggers.DropTrigger dropTrigger = (org.lgna.croquet.triggers.DropTrigger)trigger;
				return dropTrigger.getDropSite();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	protected <S extends Step<? super M>, M extends org.lgna.croquet.Model> S findStepOfEquivalentModel( M model, Class<S> stepCls, boolean isThisIncludedInSearch ) {
		S step = this.findNodeAssignableTo( stepCls, isThisIncludedInSearch );
		if( step != null ) {
			if( edu.cmu.cs.dennisc.java.util.Objects.equals( step.getModel(), model ) ) {
				return step;
			} else {
				return step.findStepOfEquivalentModel( model, stepCls, false );
			}
		} else {
			return null;
		}
	}

	public final <S extends Step<? super M>, M extends org.lgna.croquet.Model> S getFirstAncestorStepOfEquivalentModel( M model, Class<S> stepCls ) {
		return this.findStepOfEquivalentModel( model, stepCls, false );
	}

	public final <S extends Step<? super M>, M extends org.lgna.croquet.Model> S getFirstStepOfEquivalentModel( M model, Class<S> stepCls ) {
		return this.findStepOfEquivalentModel( model, stepCls, true );
	}

	protected final <S extends Step<? super M>, M extends org.lgna.croquet.Model> S findStepOfModelAssignableTo( Class<M> modelCls, Class<S> stepCls, boolean isThisIncludedInSearch ) {
		S step = this.findNodeAssignableTo( stepCls, isThisIncludedInSearch );
		if( step != null ) {
			org.lgna.croquet.Model m = step.getModel();
			if( m != null ) {
				if( modelCls.isAssignableFrom( m.getClass() ) ) {
					return step;
				} else {
					return step.findStepOfModelAssignableTo( modelCls, stepCls, false );
				}
			} else {
				return step.findStepOfModelAssignableTo( modelCls, stepCls, false );
			}
		} else {
			return null;
		}
	}

	public final <S extends Step<? super M>, M extends org.lgna.croquet.Model> S getFirstAncestorStepOfModelAssignableTo( Class<M> modelCls, Class<S> stepCls ) {
		return this.findStepOfModelAssignableTo( modelCls, stepCls, false );
	}

	public final <S extends Step<? super M>, M extends org.lgna.croquet.Model> S getFirstStepOfModelAssignableTo( Class<M> modelCls, Class<S> stepCls ) {
		return this.findStepOfModelAssignableTo( modelCls, stepCls, true );
	}

	public void addListener( org.lgna.croquet.history.event.Listener listener ) {
		this.listeners.add( listener );
	}

	public void removeListener( org.lgna.croquet.history.event.Listener listener ) {
		this.listeners.remove( listener );
	}

	public boolean isListening( org.lgna.croquet.history.event.Listener listener ) {
		return this.listeners.contains( listener );
	}

	protected void fireChanging( org.lgna.croquet.history.event.Event<?> e ) {
		if( this.owner != null ) {
			this.owner.fireChanging( e );
		}
		for( org.lgna.croquet.history.event.Listener listener : this.listeners ) {
			listener.changing( e );
		}
	}

	protected void fireChanged( org.lgna.croquet.history.event.Event<?> e ) {
		if( this.owner != null ) {
			this.owner.fireChanged( e );
		}
		for( org.lgna.croquet.history.event.Listener listener : this.listeners ) {
			listener.changed( e );
		}
	}
}
