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
package org.lgna.croquet.undo;

import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.Stacks;
import org.lgna.croquet.Group;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.undo.event.HistoryClearEvent;
import org.lgna.croquet.undo.event.HistoryInsertionIndexEvent;
import org.lgna.croquet.undo.event.HistoryListener;
import org.lgna.croquet.undo.event.HistoryPushEvent;

import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class UndoHistory {

	private DStack<Edit> stack = Stacks.newStack();
	private int insertionIndex = 0;
	private Group group;

	public UndoHistory( Group group ) {
		this.group = group;
	}

	public Group getGroup() {
		return this.group;
	}

	public DStack<Edit> getStack() {
		return this.stack;
	}

	public void push( Edit edit ) {
		if( Objects.equals( edit.getGroup(), this.group ) ) {
			HistoryPushEvent historyPushEvent = new HistoryPushEvent( this, edit );
			this.fireOperationPushing( historyPushEvent );
			this.stack.setSize( this.insertionIndex );
			this.stack.push( edit );
			this.setInsertionIndex( this.stack.size(), false );
			this.fireOperationPushed( historyPushEvent );
		}
	}

	private static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}

	private void undo() {
		if( this.insertionIndex > 0 ) {
			Edit edit = this.stack.get( this.insertionIndex - 1 );
			if( edit.canUndo() ) {
				edit.undo();
				this.insertionIndex--;
			} else {
				beep();
			}
		} else {
			beep();
		}
	}

	private void redo() {
		if( this.insertionIndex < this.stack.size() ) {
			Edit edit = this.stack.get( this.insertionIndex );
			if( edit != null ) {
				if( edit.canRedo() ) {
					edit.doOrRedo( false );
					this.insertionIndex++;
				} else {
					beep();
				}
			} else {
				beep();
			}
		} else {
			beep();
		}
	}

	public void performUndo() {
		int nextIndex = this.insertionIndex - 1;
		int actualIndex = this.setInsertionIndex( nextIndex );
		//todo
	}

	public void performRedo() {
		int nextIndex = this.insertionIndex + 1;
		int actualIndex = this.setInsertionIndex( nextIndex );
		//todo
	}

	public int getInsertionIndex() {
		return this.insertionIndex;
	}

	private int setInsertionIndex( int nextInsertionIndex, boolean isActionDesired ) {
		if( ( nextInsertionIndex >= 0 ) && ( nextInsertionIndex <= this.stack.size() ) ) {
			if( this.insertionIndex != nextInsertionIndex ) {
				HistoryInsertionIndexEvent e = new HistoryInsertionIndexEvent( this, this.insertionIndex, nextInsertionIndex );
				this.fireInsertionIndexChanging( e );

				final int N = Math.abs( nextInsertionIndex - this.insertionIndex );
				if( nextInsertionIndex < this.insertionIndex ) {
					for( int i = 0; i < N; i++ ) {
						if( isActionDesired ) {
							this.undo();
						} else {
							this.insertionIndex--;
						}
					}
				} else {
					for( int i = 0; i < N; i++ ) {
						if( isActionDesired ) {
							this.redo();
						} else {
							this.insertionIndex++;
						}
					}
				}
				if( this.insertionIndex == nextInsertionIndex ) {
					this.fireInsertionIndexChanged( e );
				}
			}
		}
		return this.insertionIndex;
	}

	public int setInsertionIndex( int nextInsertionIndex ) {
		return this.setInsertionIndex( nextInsertionIndex, true );
	}

	private List<HistoryListener> historyListeners = new LinkedList<HistoryListener>();

	public void addHistoryListener( HistoryListener l ) {
		synchronized( this.historyListeners ) {
			this.historyListeners.add( l );
		}
	}

	public void removeHistoryListener( HistoryListener l ) {
		synchronized( this.historyListeners ) {
			this.historyListeners.remove( l );
		}
	}

	private void fireOperationPushing( HistoryPushEvent e ) {
		synchronized( this.historyListeners ) {
			for( HistoryListener l : this.historyListeners ) {
				l.operationPushing( e );
			}
		}
	}

	private void fireOperationPushed( HistoryPushEvent e ) {
		synchronized( this.historyListeners ) {
			for( HistoryListener l : this.historyListeners ) {
				l.operationPushed( e );
			}
		}
	}

	private void fireInsertionIndexChanging( HistoryInsertionIndexEvent e ) {
		synchronized( this.historyListeners ) {
			for( HistoryListener l : this.historyListeners ) {
				l.insertionIndexChanging( e );
			}
		}
	}

	private void fireInsertionIndexChanged( HistoryInsertionIndexEvent e ) {
		synchronized( this.historyListeners ) {
			for( HistoryListener l : this.historyListeners ) {
				l.insertionIndexChanged( e );
			}
		}
	}

	private void fireClearing( HistoryClearEvent e ) {
		synchronized( this.historyListeners ) {
			for( HistoryListener l : this.historyListeners ) {
				l.clearing( e );
			}
		}
	}

	private void fireCleared( HistoryClearEvent e ) {
		synchronized( this.historyListeners ) {
			for( HistoryListener l : this.historyListeners ) {
				l.cleared( e );
			}
		}
	}

	public Edit createDoIgnoringCompositeEdit( String presentation ) {
		//		synchronized( this.stack ) {
		//			final int N = this.insertionIndex;
		//			if( N > 0 ) {
		//				edu.cmu.cs.dennisc.croquet.Edit[] edits = new edu.cmu.cs.dennisc.croquet.Edit[ N ];
		//				for( int i=0; i<N; i++ ) {
		//					edits[ i ] = this.stack.get( i );
		//				}
		//				return new edu.cmu.cs.dennisc.croquet.CompositeEdit( edits, true, presentation );
		//			} else {
		//				return null;
		//			}
		//		}
		throw new RuntimeException( "todo" );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( "[" );
		sb.append( this.getGroup() );
		sb.append( "]" );
		return sb.toString();
	}
}
