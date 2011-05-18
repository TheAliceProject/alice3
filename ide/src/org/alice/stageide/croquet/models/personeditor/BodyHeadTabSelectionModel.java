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

package org.alice.stageide.croquet.models.personeditor;

/*package-private*/ abstract class ContentTab extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
	public ContentTab(java.util.UUID id, String title) {
		super(id);
		this.setTitleText( title );
	}
	@Override
	public org.lgna.croquet.components.ScrollPane createScrollPane() {
		return null;
	}
}

/*package-private*/ class BodyTab extends ContentTab {
	public BodyTab() {
		super( java.util.UUID.fromString( "10c0d057-a5d7-4a36-8cd7-c30f46f5aac2" ), "Body" );
	}
	@Override
	protected org.lgna.croquet.components.JComponent<?> createMainComponent() {
		org.lgna.croquet.components.List< ? > list = org.alice.stageide.croquet.models.personeditor.FullBodyOutfitSelectionState.getInstance().createList();
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( list );
		scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 66 );
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );

		org.lgna.croquet.components.Slider slider = FitnessModel.getInstance().createSlider();
		slider.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		
		org.lgna.croquet.components.BorderPanel fitnessLevelPane = new org.lgna.croquet.components.BorderPanel();
		fitnessLevelPane.addComponent( SetFitnessToOutOfShapeOperation.getInstance().createButton(), org.lgna.croquet.components.BorderPanel.Constraint.LINE_START );
		fitnessLevelPane.addComponent( slider, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		fitnessLevelPane.addComponent( SetFitnessToInShapeOperation.getInstance().createButton(), org.lgna.croquet.components.BorderPanel.Constraint.LINE_END );

		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel( 8, 8 );
		rv.addComponent( scrollPane, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		rv.addComponent( fitnessLevelPane, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_END );
		rv.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		rv.getAwtComponent().setOpaque( true );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
		return rv;
	}
}
/*package-private*/ class HeadTab extends ContentTab {
	public HeadTab() {
		super( java.util.UUID.fromString( "1e1d604d-974f-4666-91e0-ccf5adec0e4d" ), "Head" );
	}
	@Override
	protected org.lgna.croquet.components.JComponent<?> createMainComponent() {
		org.lgna.croquet.components.RowsSpringPanel rv = new org.lgna.croquet.components.RowsSpringPanel( 8, 8 ) {
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "hair:", HairColorSelectionState.getInstance().createList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createRow( null, HairSelectionState.getInstance().createList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "eye color:", BaseEyeColorSelectionState.getInstance().createList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createRow( null, org.lgna.croquet.components.BoxUtilities.createGlue() ) );
				return rv;
			}
		};
		rv.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
		return rv;
	}
};

/**
 * @author Dennis Cosgrove
 */
public class BodyHeadTabSelectionModel extends edu.cmu.cs.dennisc.croquet.PredeterminedTabSelectionState< ContentTab >{
	private static class SingletonHolder {
		private static BodyHeadTabSelectionModel instance = new BodyHeadTabSelectionModel();
	}
	public static BodyHeadTabSelectionModel getInstance() {
		return SingletonHolder.instance;
	}
	private BodyHeadTabSelectionModel() {
		super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "d525f0c5-9f39-4807-a9d3-f66775f9eb2d" ), null, 0, new BodyTab(), new HeadTab() );
	}
}
