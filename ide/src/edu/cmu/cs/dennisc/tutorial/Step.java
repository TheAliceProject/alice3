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
	/*package-private*/ static final boolean IS_NOTE_OVERLAPPING_DESIRED = false;
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
			for( java.awt.Component awtComponent : parent.getComponents() ) {
				awtComponent.setSize( awtComponent.getPreferredSize() );
				
				if( set.contains( awtComponent ) ) {
					//pass
				} else {
					if( prevLocation != null ) {
						if( IS_NOTE_OVERLAPPING_DESIRED ) {
							awtComponent.setLocation( prevLocation.x + 48, prevLocation.y - 33 );
						} else {
							awtComponent.setLocation( prevLocation.x + prevSize.width - 64, prevLocation.y - 33 );
						}
					} else {
						if (awtComponent instanceof Note.JNote) {
//							edu.cmu.cs.dennisc.croquet.Component<?> component = edu.cmu.cs.dennisc.croquet.Component.lookup( awtComponent );
//							Note note = (Note) component;
//							note.setLocation( parent );
							awtComponent.setLocation( Step.this.calculateLocationOfFirstNote() );
						} else {
							awtComponent.setLocation( 10, 10 );
						}
					}
					set.add( awtComponent );
				}
				prevLocation = awtComponent.getLocation();
				prevSize = awtComponent.getSize();
			}
		}
	}

	protected java.awt.Point calculateLocationOfFirstNote() {
		return this.notes.get( 0 ).calculateLocation( this.stepPanel );
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
//	private class StepPanel extends edu.cmu.cs.dennisc.croquet.Panel {
//		@Override
//		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
//			return new StepLayoutManager();
//		}
		@Override
		protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			for( Note note : Step.this.getNotes() ) {
				this.internalAddComponent( note );
			}
//			for( Note note : Step.this.getNotes() ) {
//				if( note.isActive() ) {
//					//
//				} else {
//					this.internalAddComponent( note );
//				}
//			}
//			for( Note note : Step.this.getNotes() ) {
//				if( note.isActive() ) {
//					this.internalAddComponent( note );
//				} else {
//					//pass
//				}
//			}
			super.handleAddedTo( parent );
		}
		@Override
		protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			this.removeAllComponents();
			super.handleRemovedFrom( parent );
		}
	}

	private java.util.UUID id = java.util.UUID.randomUUID();
	private Tutorial tutorial;
	private StepPanel stepPanel = new StepPanel();
	private java.util.List< Note > notes = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private String title;
	
	private int[] historyIndices = null;
	
	public Step( String title, String text ) {
		this.title = title;
		this.addNote( new Note( text ) );
	}
	public Tutorial getTutorial() {
		return this.tutorial;
	}
	protected abstract void complete( edu.cmu.cs.dennisc.croquet.ModelContext<?> context );
	/*package-private*/ void setTutorial( Tutorial tutorial ) {
		this.tutorial = tutorial;
		for( Note note : this.notes ) {
			note.setTutorial( this.tutorial );
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
	public void addNote( Note note ) {
		this.notes.add( note );
		note.setTutorial( this.tutorial );
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
