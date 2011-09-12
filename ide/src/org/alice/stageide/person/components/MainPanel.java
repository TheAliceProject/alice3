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

package org.alice.stageide.person.components;

/**
 * @author Dennis Cosgrove
 */
public class MainPanel extends org.lgna.croquet.components.BorderPanel {
	public static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	public static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR, 1.0, 0.9, 0.8 );

	private final org.lgna.croquet.components.FolderTabbedPane<?> tabbedPane;
	private final org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseSkinTone> baseSkinToneObserver = new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseSkinTone>() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseSkinTone > state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseSkinTone > state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
			tabbedPane.repaint();
		}
	};
	private org.lgna.croquet.State.ValueObserver<org.lgna.croquet.PredeterminedTab> tabChangeAdapter = new org.lgna.croquet.State.ValueObserver<org.lgna.croquet.PredeterminedTab>() {
		public void changing( org.lgna.croquet.State< org.lgna.croquet.PredeterminedTab > state, org.lgna.croquet.PredeterminedTab prevValue, org.lgna.croquet.PredeterminedTab nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.croquet.PredeterminedTab > state, org.lgna.croquet.PredeterminedTab prevValue, org.lgna.croquet.PredeterminedTab nextValue, boolean isAdjusting ) {
			//todo
		}
	};
	private final PersonViewer personViewer;
	public MainPanel( PersonViewer personViewer ) {
		this.personViewer = personViewer;
		this.tabbedPane = org.alice.stageide.person.models.BodyHeadTabSelectionModel.getInstance().createDefaultFolderTabbedPane();
		this.tabbedPane.scaleFont( 1.5f );

		org.lgna.croquet.components.BorderPanel northPane = new org.lgna.croquet.components.BorderPanel();
		northPane.addComponent( org.alice.stageide.person.models.RandomizeOperation.getInstance().createButton(), Constraint.PAGE_START );
		org.lgna.croquet.components.RowsSpringPanel ubiquitousPane = new org.lgna.croquet.components.RowsSpringPanel( 8, 8 ) {
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "life stage:", new org.alice.stageide.person.components.LifeStageList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "gender:", new org.alice.stageide.person.components.GenderList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "skin tone:", new org.alice.stageide.person.components.BaseSkinToneList() ) );
				return rv;
			}
		};
		ubiquitousPane.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		northPane.addComponent( ubiquitousPane, Constraint.CENTER );

		org.lgna.croquet.components.BorderPanel ingredientsPanel = new org.lgna.croquet.components.BorderPanel();
		ingredientsPanel.addComponent( northPane, Constraint.PAGE_START );
		ingredientsPanel.addComponent( this.tabbedPane, Constraint.CENTER );
		ingredientsPanel.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );

		org.lgna.croquet.components.HorizontalSplitPane splitPane = new org.lgna.croquet.components.HorizontalSplitPane( personViewer, ingredientsPanel );
		splitPane.setDividerLocation( 400 );
		this.addComponent( splitPane, Constraint.CENTER );
		org.alice.stageide.person.models.BaseSkinToneState.getInstance().addValueObserver( this.baseSkinToneObserver );
	}
	public PersonViewer getPersonViewer() {
		return this.personViewer;
	}
}
