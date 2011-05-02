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

package org.alice.ide.croquet.models.templates;

//import org.alice.ide.croquet.models.members.FieldsTab;
//import org.alice.ide.croquet.models.members.BlocksTab;
//import org.alice.ide.croquet.models.members.FunctionsTab;
//import org.alice.ide.croquet.models.members.MembersTabSelectionState;
//import org.alice.ide.croquet.models.members.ProceduresTab;
//import org.alice.ide.croquet.models.members.MembersTabSelectionState.SingletonHolder;
//
///**
// * @author Dennis Cosgrove
// */
//public class TemplatesTabSelectionState extends edu.cmu.cs.dennisc.croquet.PredeterminedTabSelectionState {
//	private static class IndirectCurrentAccessibleTypeIcon implements javax.swing.Icon {
//		private javax.swing.Icon getCurrentAccessibleTypeIcon() {
//			edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
//			edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type; 
//			if( accessible != null ) {
//				type = accessible.getValueType();
//			} else {
//				type = null;
//			}
//			return org.alice.stageide.gallerybrowser.ResourceManager.getSmallIconForType( type );
//		}
//		public int getIconHeight() {
//			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
//			if( icon != null ) {
//				return icon.getIconHeight();
//			} else {
//				return 0;
//			}
//		}
//		public int getIconWidth() {
//			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
//			if( icon != null ) {
//				return icon.getIconWidth();
//			} else {
//				return 0;
//			}
//		}
//		public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
//			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
//			if( icon != null ) {
//				icon.paintIcon(c, g, x, y);
//			}
//		}
//	}
//	
//	private static javax.swing.Icon ICON = new IndirectCurrentAccessibleTypeIcon();
//
//	private static class SingletonHolder {
//		private static TemplatesTabSelectionState instance = new TemplatesTabSelectionState();
//	}
//	public static TemplatesTabSelectionState getInstance() {
//		return SingletonHolder.instance;
//	}
//	
//	private TemplatesTabSelectionState() {
//		super( 
//				org.alice.ide.IDE.UI_STATE_GROUP, 
//				java.util.UUID.fromString( "9509d1e1-4997-4add-aed9-b73aac0ae000" ), 
//				org.alice.ide.croquet.codecs.SingletonCodec.getInstance( edu.cmu.cs.dennisc.croquet.PredeterminedTab.class ), 
//				0,
//				ProceduresTab.getInstance(), FunctionsTab.getInstance(), FieldsTab.getInstance(), BlocksTab.getInstance()
//		);
//	}
//	@Override
//	protected void localize() {
//		super.localize();
//		ProceduresTab proceduresTab = ProceduresTab.getInstance();
//		proceduresTab.setTitleIcon( ICON );
//		proceduresTab.setTitleText( this.getLocalizedText( "procedures" ) );
//		FunctionsTab functionsTab = FunctionsTab.getInstance();
//		functionsTab.setTitleIcon( ICON );
//		functionsTab.setTitleText( this.getLocalizedText( "functions" ) );
//		FieldsTab fieldsTab = FieldsTab.getInstance();
//		fieldsTab.setTitleIcon( ICON );
//		fieldsTab.setTitleText( this.getLocalizedText( "fields" ) );
//		BlocksTab blocksTab = BlocksTab.getInstance();
//		blocksTab.setTitleIcon( new javax.swing.Icon() {
//			public int getIconHeight() {
//				return 24;
//			}
//			public int getIconWidth() {
//				return 0;//32;
//			}
//			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
//			}
//		} );
//		blocksTab.setTitleText( this.getLocalizedText( "blocks" ) );
//	}
//}

public class TemplatesTabSelectionState extends edu.cmu.cs.dennisc.croquet.TabSelectionState< TemplateComposite > {
	private static class SingletonHolder {
		private static TemplatesTabSelectionState instance = new TemplatesTabSelectionState();
	}
	public static TemplatesTabSelectionState getInstance() {
		return SingletonHolder.instance;
	}
	
	private TemplatesTabSelectionState() {
		super( 
				org.alice.ide.IDE.UI_STATE_GROUP, 
				java.util.UUID.fromString( "5bf41daf-eaf6-46f2-9165-99ccb6928936" ), 
				org.alice.ide.croquet.codecs.SingletonCodec.getInstance( org.alice.ide.croquet.models.templates.TemplateComposite.class ), 
				0,
				ProcedureTemplateComposite.getInstance(),
				FunctionTemplateComposite.getInstance(),
				FieldTemplateComposite.getInstance(),
				BlockTemplateComposite.getInstance()
		);
	}
	public edu.cmu.cs.dennisc.croquet.ToolPaletteTabbedPane< TemplateComposite > createToolPaletteTabbedPane() {
		return this.createToolPaletteTabbedPane( new TabCreator< TemplateComposite >() {
			public edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent( org.alice.ide.croquet.models.templates.TemplateComposite item ) {
				return item.createMainComponent();
			}
			public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane( org.alice.ide.croquet.models.templates.TemplateComposite item ) {
				edu.cmu.cs.dennisc.croquet.ScrollPane rv = new edu.cmu.cs.dennisc.croquet.ScrollPane();
				rv.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
				rv.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
				return rv;
			}
			public void customizeTitleComponent( edu.cmu.cs.dennisc.croquet.BooleanState booleanState, edu.cmu.cs.dennisc.croquet.AbstractButton< ?, edu.cmu.cs.dennisc.croquet.BooleanState > button, org.alice.ide.croquet.models.templates.TemplateComposite item ) {
				item.customizeTitleComponent( booleanState, button );
			}
			public java.util.UUID getId( org.alice.ide.croquet.models.templates.TemplateComposite item ) {
				return java.util.UUID.randomUUID();
			}
			public boolean isCloseable( org.alice.ide.croquet.models.templates.TemplateComposite item ) {
				return false;
			}
		} );
	}
	@Override
	protected void localize() {
		super.localize();
//		ProceduresTab proceduresTab = ProceduresTab.getInstance();
//		proceduresTab.setTitleIcon( ICON );
//		proceduresTab.setTitleText( this.getLocalizedText( "procedures" ) );
//		FunctionsTab functionsTab = FunctionsTab.getInstance();
//		functionsTab.setTitleIcon( ICON );
//		functionsTab.setTitleText( this.getLocalizedText( "functions" ) );
//		FieldsTab fieldsTab = FieldsTab.getInstance();
//		fieldsTab.setTitleIcon( ICON );
//		fieldsTab.setTitleText( this.getLocalizedText( "fields" ) );
//		BlocksTab blocksTab = BlocksTab.getInstance();
//		blocksTab.setTitleIcon( new javax.swing.Icon() {
//			public int getIconHeight() {
//				return 24;
//			}
//			public int getIconWidth() {
//				return 0;//32;
//			}
//			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
//			}
//		} );
//		blocksTab.setTitleText( this.getLocalizedText( "blocks" ) );
	}
}
