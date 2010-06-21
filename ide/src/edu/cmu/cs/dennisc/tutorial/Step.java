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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
public abstract class Step {
	/*package-private*/ static final boolean IS_NOTE_OVERLAPPING_DESIRED = true;
	private class StepLayoutManager implements java.awt.LayoutManager {
		private java.util.Set<java.awt.Component> set = edu.cmu.cs.dennisc.java.util.Collections.newHashSet(); 
		public void addLayoutComponent( java.lang.String name, java.awt.Component comp ) {
		}
		public void removeLayoutComponent( java.awt.Component comp ) {
		}
		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
			return parent.getMinimumSize();
		}
		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
			return parent.getPreferredSize();
		}
		public void layoutContainer( java.awt.Container parent ) {
			java.awt.Point prevLocation = null;
			java.awt.Dimension prevSize = null;
			java.awt.Dimension parentSize = parent.getSize();
			for( java.awt.Component awtComponent : parent.getComponents() ) {
				java.awt.Dimension childSize = awtComponent.getPreferredSize();
				awtComponent.setSize( childSize );
				if( set.contains( awtComponent ) ) {
					//pass
				} else {
					java.awt.Point p;
					if( prevLocation != null ) {
						if( IS_NOTE_OVERLAPPING_DESIRED ) {
							p = new java.awt.Point( prevLocation.x + 48, prevLocation.y - 33 );
						} else {
							p = new java.awt.Point( prevLocation.x + prevSize.width - 64, prevLocation.y - 33 );
						}
					} else {
						if (awtComponent instanceof Note.JNote) {
							if( Step.this.layoutHint != null ) {
								p = Step.this.layoutHint;
								if( p.x < 0 ) {
									p.x = parentSize.width - childSize.width + p.x;
								}
								if( p.y < 0 ) {
									p.y = parentSize.height - childSize.height + p.y;
								}
							} else {
								p = new java.awt.Point( Step.this.calculateLocationOfFirstNote() );
							}
						} else {
							p = new java.awt.Point( 10, 10 );
						}
					}
					
					if( parentSize.width > 0 && parentSize.height > 0 ) {
						final int BORDER = 32;
						p.x = Math.max( p.x, BORDER );
						p.x = Math.min( p.x, parentSize.width-childSize.width-BORDER );
						p.y = Math.max( p.y, BORDER );
						p.y = Math.min( p.y, parentSize.height-childSize.height-BORDER );
					}
					
					awtComponent.setLocation( p );

					set.add( awtComponent );
				}
				prevLocation = awtComponent.getLocation();
				prevSize = awtComponent.getSize();
			}
		}
	}
	protected int getIndexOfFirstActiveNote() { 
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			Note note = this.getNoteAt( i );
			if( note.isActive() ) {
				return i;
			}
		}
		return -1;
	}
	protected void setActiveNote( int activeIndex ) {
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			this.getNoteAt( i ).setActive( i==activeIndex );
		}
	}
	
	protected java.awt.Point calculateLocationForNoteAt( edu.cmu.cs.dennisc.croquet.Container< ? > container, int index ) {
		return this.notes.get( 0 ).calculateLocation( container );
	}

	protected java.awt.Point calculateLocationOfFirstNote( edu.cmu.cs.dennisc.croquet.Container< ? > container ) {
		return this.calculateLocationForNoteAt( container, 0 );
	}

	private java.awt.Point calculateLocationOfFirstNote() {
		return this.calculateLocationOfFirstNote( this.stepPanel );
	}

	private class StepPanel extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JPanel > {
		@Override
		protected javax.swing.JPanel createAwtComponent() {
			javax.swing.JPanel rv = new javax.swing.JPanel() {
				private boolean isSortingDesired = false;
				private java.util.ArrayList<java.awt.Component> sortedComponents = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
				private boolean isActiveNote( java.awt.Component component ) {
					boolean rv;
					if( component instanceof Note.JNote ) {
						Note.JNote jNote = (Note.JNote)component;
						rv = jNote.isActive();
					} else {
						rv = false;
					}
					return rv;
				}
				private void updateSortedComponents() {
					java.awt.Component[] components = this.getComponents();
					this.sortedComponents.ensureCapacity( components.length );
					this.sortedComponents.clear();
					for( java.awt.Component component : components ) {
						if( this.isActiveNote(component) ) {
							this.sortedComponents.add( component );
						} else {
							//pass 
						}
					}
					for( java.awt.Component component : components ) {
						if( this.isActiveNote(component) ) {
							//pass 
						} else {
							this.sortedComponents.add( component );
						}
					}
					assert this.sortedComponents.size()==components.length;
				}
				@Override
				public java.awt.Component getComponent(int index) {
					if( this.isSortingDesired ) {
						return this.sortedComponents.get( index );
					} else {
						return super.getComponent( index );
					}
				}
				@Override
				protected void paintChildren(java.awt.Graphics g) {
					if( this.getComponentCount() > 1 ) {
						this.updateSortedComponents();
						this.isSortingDesired = true;
					}
					try {
						super.paintChildren(g);
					} finally {
						this.isSortingDesired = false;
					}
				}
			};
			rv.setLayout( new StepLayoutManager() );
			rv.setOpaque( false );
			return rv;
		}
		@Override
		protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			for( Note note : Step.this.getNotes() ) {
				this.internalAddComponent( note );
			}
			super.handleAddedTo( parent );
		}
		@Override
		protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			this.removeAllComponents();
			super.handleRemovedFrom( parent );
		}
	}

	private java.util.UUID id = java.util.UUID.randomUUID();
	private TutorialStencil tutorialStencil;
	private StepPanel stepPanel = new StepPanel();
	private java.util.List< Note > notes = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private String title;
	private java.awt.Point layoutHint = null;
	
	private int[] historyIndices = null;
	
	public Step( String title, String text ) {
		this.title = title;
		this.addNote( new Note( text ) );
	}
	public TutorialStencil getTutorialStencil() {
		return this.tutorialStencil;
	}
	protected abstract void complete( edu.cmu.cs.dennisc.croquet.ModelContext<?> context );
	/*package-private*/ void setTutorialStencil( TutorialStencil tutorialStencil ) {
		this.tutorialStencil = tutorialStencil;
		for( Note note : this.notes ) {
			note.setTutorialStencil( this.tutorialStencil );
		}
	}
	
	/*package-private*/ int[] getHistoryIndices() {
		return this.historyIndices;
	}
	/*package-private*/ void setHistoryIndices( int[] historyIndices ) {
		this.historyIndices = historyIndices;
	}
	
	public java.util.UUID getId() {
		return this.id;
	}
	
	public java.awt.Point getLayoutHint() {
		return this.layoutHint;
	}
	public void setLayoutHint(java.awt.Point layoutHint) {
		this.layoutHint = layoutHint;
	}
	public final void setLayoutHint( int x, int y ) {
		this.setLayoutHint( new java.awt.Point( x, y ) );
	}
	
	public void addNote( Note note ) {
		this.notes.add( note );
		note.setTutorialStencil( this.tutorialStencil );
	}
	public Note getNoteAt( int index ) {
		return this.notes.get( index );
	}
	public int getNoteCount() {
		return this.notes.size();
	}
	public Iterable< Note > getNotes() {
		return this.notes;
	}
	public edu.cmu.cs.dennisc.croquet.Component< ? > getCard() {
		return this.stepPanel;
	}
	public boolean isAutoAdvanceDesired() {
		return true;
	}

	public void reset() {
	}
		
	@Override
	public String toString() {
		return this.title;
	}
}
