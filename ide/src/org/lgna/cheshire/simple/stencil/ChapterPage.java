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

package org.lgna.cheshire.simple.stencil;

/**
 * @author Dennis Cosgrove
 */
public class ChapterPage implements org.lgna.cheshire.simple.Page {
	private static java.util.Map< org.lgna.cheshire.simple.Chapter, ChapterPage > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized ChapterPage getInstance( org.lgna.cheshire.simple.Chapter chapter ) {
		ChapterPage rv = map.get( chapter );
		if( rv != null ) {
			//pass
		} else {
			rv = new ChapterPage( chapter );
			map.put( chapter, rv );
		}
		return rv;
	}
	private final org.lgna.cheshire.simple.Chapter chapter;
	private final java.util.List< Note > notes = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public ChapterPage( org.lgna.cheshire.simple.Chapter chapter ) {
		this.chapter = chapter;
		this.refreshNotes();
	}
	public org.lgna.cheshire.simple.Chapter getChapter() {
		return this.chapter;
	}
	public Iterable< ? extends org.lgna.cheshire.simple.Note > getNotes() {
		return this.notes;
	}
	
	public void refreshNotes() {
		this.notes.clear();
		if( chapter instanceof org.lgna.cheshire.simple.MessageChapter ) {
			org.lgna.cheshire.simple.MessageChapter messageChapter = (org.lgna.cheshire.simple.MessageChapter)chapter;
			this.notes.add( new MessageNote( messageChapter.getText() ) );
		} else if( chapter instanceof org.lgna.cheshire.simple.TransactionChapter ) {
			org.lgna.cheshire.simple.TransactionChapter transactionChapter = (org.lgna.cheshire.simple.TransactionChapter)chapter;
			org.lgna.croquet.history.Transaction transaction = transactionChapter.getTransaction();
			for( org.lgna.croquet.history.Step< ? > step : transaction.getDescendantSteps() ) {
				this.notes.add( org.lgna.cheshire.simple.stencil.stepnotes.StepNoteFactory.createNote( step ) );
			}
		}
		if( this.notes.size() > 1 ) {
			final int N = this.notes.size();
			for( int i=0; i<N; i++ ) {
				this.notes.get( i ).setLabel( Integer.toString( i+1 ) );
			}
		}
		this.reset();
	}

	private static final boolean IS_STENCIL_RENDERING_DESIRED_BY_DEFAULT = true;
	/*package-private*/ static final boolean IS_NOTE_OVERLAPPING_DESIRED = true;
	private class PageLayoutManager implements java.awt.LayoutManager {
		private java.util.Set<java.awt.Component> set = edu.cmu.cs.dennisc.java.util.Collections.newHashSet(); 
		public void addLayoutComponent( String name, java.awt.Component comp ) {
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
			java.awt.Dimension parentSize = parent.getSize();
			final int X_OFFSET = 64;
			final int Y_OFFSET = 33;
			if( parentSize.width > 0 && parentSize.height > 0 ) {
				java.awt.Point prevLocation = null;
				java.awt.Dimension prevSize = null;
				for( java.awt.Component awtComponent : parent.getComponents() ) {
					java.awt.Dimension childSize = awtComponent.getPreferredSize();
					awtComponent.setSize( childSize );
					if( set.contains( awtComponent ) ) {
						//pass
					} else {
						final int BORDER = 16;
						java.awt.Point p;
						if( prevLocation != null ) {
							if( IS_NOTE_OVERLAPPING_DESIRED ) {
								p = new java.awt.Point( prevLocation.x + X_OFFSET, prevLocation.y - Y_OFFSET );
							} else {
								p = new java.awt.Point( prevLocation.x + prevSize.width - X_OFFSET, prevLocation.y - Y_OFFSET );
							}
						} else {
							if (awtComponent instanceof org.lgna.cheshire.simple.Note.JNote) {
								if( ChapterPage.this.layoutHint != null ) {
									p = ChapterPage.this.layoutHint;
									if( p.x < 0 ) {
										p.x = parentSize.width - childSize.width + p.x;
									}
									if( p.y < 0 ) {
										p.y = parentSize.height - childSize.height + p.y;
									}
								} else {
									final boolean IS_BASED_ON_FRAME_SIZE = true;
									if( IS_BASED_ON_FRAME_SIZE ) {
										int x = parentSize.width - BORDER - X_OFFSET*(parent.getComponentCount()-1) - childSize.width;
										int y = parentSize.height - BORDER - childSize.height;
										p = new java.awt.Point( x, y );
									} else {
										p = new java.awt.Point( ChapterPage.this.calculateLocationOfFirstNote() );
									}
								}
							} else {
								p = new java.awt.Point( 10, 10 );
							}
						}
						
						if( parentSize.width > 0 && parentSize.height > 0 ) {
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
	}
	private class PagePanel extends org.lgna.croquet.components.JComponent< javax.swing.JPanel > {
		@Override
		protected javax.swing.JPanel createAwtComponent() {
			javax.swing.JPanel rv = new javax.swing.JPanel() {
				private boolean isSortingDesired = false;
				private java.util.ArrayList<java.awt.Component> sortedComponents = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
				private boolean isActiveNote( java.awt.Component component ) {
					boolean rv;
					if( component instanceof org.lgna.cheshire.simple.Note.JNote ) {
						org.lgna.cheshire.simple.Note.JNote jNote = (org.lgna.cheshire.simple.Note.JNote)component;
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
					//todo: use setComponentZOrder?
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
			rv.setLayout( new PageLayoutManager() );
			rv.setOpaque( false );
			return rv;
		}
//		@Override
//		protected void handleDisplayabilityChanged( java.awt.event.HierarchyEvent e ) {
//			super.handleDisplayabilityChanged( e );
//			if( this.getComponentCount() == 0 ) {
//				for( Note note : Page.this.getNotes() ) {
//					this.internalAddComponent( note );
//				}
//			}
//		}
		@Override
		protected void handleDisplayable() {
			for( org.lgna.cheshire.simple.Note note : ChapterPage.this.getNotes() ) {
				this.internalAddComponent( note );
			}
			super.handleDisplayable();
		}
		@Override
		protected void handleUndisplayable() {
			this.internalRemoveAllComponents();
			super.handleUndisplayable();
		}
	}

	private java.util.UUID id = java.util.UUID.randomUUID();
	private PagePanel stepPanel = new PagePanel();
	private java.awt.Point layoutHint = null;
	
	private int[] historyIndices = null;
	private boolean isStencilRenderingDesired = IS_STENCIL_RENDERING_DESIRED_BY_DEFAULT;
	

	protected int getIndexOfFirstActiveNote() { 
		final int N = this.notes.size();
		for( int i=0; i<N; i++ ) {
			org.lgna.cheshire.simple.Note note = this.notes.get( i );
			if( note.isActive() ) {
				return i;
			}
		}
		return -1;
	}
	private Note getFirstActiveNote() {
		for( Note note : this.notes ) {
			if( note.isActive() ) {
				return note;
			}
		}
		return null;
	}
	protected void setActiveNote( int activeIndex ) {
		final int N = this.notes.size();
		for( int i=0; i<N; i++ ) {
			this.notes.get( i ).setActive( i==activeIndex );
		}
	}
	
//	protected java.awt.Point calculateLocationForNoteAt( org.lgna.croquet.components.Container< ? > container, int index ) {
//		return this.notes.get( 0 ).calculateLocation( container );
//	}

	public void adjustIfNecessary( org.lgna.croquet.history.event.Event<?> event ) {
//		Note note = this.getFirstActiveNote();
//		if( note != null ) {
//			note.adjustIfNecessary( event );
//		}
		int activeNoteIndex = this.getIndexOfFirstActiveNote();
		if( activeNoteIndex != -1 ) {
			final int N = this.notes.size();
			for( int i=activeNoteIndex; i<N; i++ ) {
				Note iNote = this.notes.get( i );
				boolean isMoved = iNote.moveOutOfTheWayIfNecessary( event );
				if( isMoved ) {
					for( int j=i+1; j<N; j++ ) {
						java.awt.Point prevLocation = this.notes.get( j-1 ).getAwtComponent().getLocation();
						Note jNote = this.notes.get( j );
						jNote.setLocation( prevLocation.x+48, prevLocation.y-33 );
					}
					break;
				}
			}
		}
	}
	public boolean isAutoAdvanceDesired() {
		return this.chapter.isAutoAdvanceDesired();
	}
	public boolean isWhatWeveBeenWaitingFor( org.lgna.croquet.history.event.Event<?> event ) {
		final int NOTE_COUNT = this.notes.size();
		int activeNoteIndex = this.getIndexOfFirstActiveNote();
		activeNoteIndex = Math.max( activeNoteIndex, 0 );
		if( activeNoteIndex < NOTE_COUNT ) {
			Note activeNote = this.notes.get( activeNoteIndex );
//			try {
				if( activeNote.isWhatWeveBeenWaitingFor( event ) ) {
					activeNoteIndex ++;
					if( activeNoteIndex == NOTE_COUNT ) {
						return true;
					} else {
						this.setActiveNote( activeNoteIndex );
					}
				}
//			} catch( org.lgna.croquet.CancelException ce ) {
//				org.alice.ide.IDE.getActiveInstance().getSimplePresentation().restoreHistoryIndicesDueToCancel();
//				this.reset();
//			}
		}
		return false;
	}
	
//	public abstract edu.cmu.cs.dennisc.croquet.ReplacementAcceptability getReplacementAcceptability();
//
//	public org.lgna.stencil.Note getNoteAt( int index ) {
//		return this.getNotes().get( index );
//	}
//	public int getNoteCount() {
//		return this.getNotes().size();
//	}

	protected java.awt.Point calculateLocationOfFirstNote( org.lgna.croquet.components.Container< ? > container ) {
		return this.notes.get( 0 ).calculateLocation( container );
	}

	private java.awt.Point calculateLocationOfFirstNote() {
		return this.calculateLocationOfFirstNote( this.stepPanel );
	}

	public boolean isEventInterceptable( java.awt.event.MouseEvent e ) {
		Note note = this.getFirstActiveNote();
		if( note != null ) {
			return note.isEventInterceptable( e );
		} else {
			return true;
		}
	}
	public void resetStencilRenderingDesiredToDefault() {
		this.isStencilRenderingDesired = IS_STENCIL_RENDERING_DESIRED_BY_DEFAULT;
	}
	public boolean isStencilRenderingDesired() {
		return this.isStencilRenderingDesired;
	}
	public void setStencilRenderingDesired( boolean isStencilRenderingDesired ) {
		this.isStencilRenderingDesired = isStencilRenderingDesired;
	}
	
	public boolean isGoodToGo() {
		if( this.notes.size() > 0 ) {
			Note note0 = this.notes.get( 0 );
			return note0.isGoodToGo();
		} else {
			//todo?
			return false;
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
	
	public org.lgna.croquet.components.JComponent< ? > getCard() {
		return this.stepPanel;
	}
	
	public void reset() {
		java.awt.LayoutManager layoutManager = this.stepPanel.getAwtComponent().getLayout();
		if( layoutManager instanceof PageLayoutManager ) {
			PageLayoutManager stepLayoutManager = (PageLayoutManager)layoutManager;
			stepLayoutManager.set.clear();
			this.stepPanel.revalidateAndRepaint();
		}
		for( org.lgna.cheshire.simple.Note note : this.getNotes() ) {
			note.reset();
		}
		this.setActiveNote( 0 );
	}
	
	public String getDetailedReport() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getChapter().getTitle() );
		sb.append( "\n" );
		for( Note note : this.notes ) {
			note.appendDetailedReport( sb );
		}
		return sb.toString();
	}
}
