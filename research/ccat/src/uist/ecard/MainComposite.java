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

package uist.ecard;


import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author Dennis Cosgrove
 */
public class MainComposite extends org.lgna.croquet.SimpleComposite< org.lgna.croquet.components.BorderPanel > {
	private static class SingletonHolder {
		private static MainComposite instance = new MainComposite();
	}
	public static MainComposite getInstance() {
		return SingletonHolder.instance;
	}
	private MainComposite() {
		super( java.util.UUID.fromString( "2cfdd500-0fd8-4f25-9bdf-791ac450cfcf" ) );
	}
	@Override
	protected void localize() {
	}
	@Override
	public boolean contains( org.lgna.croquet.Model model ) {
		return false;
	}
	@Override
	protected org.lgna.croquet.components.BorderPanel createView() {
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();

		ECardApplication app = ECardApplication.getActiveInstance();
		
		app.cardPanel = new ECardPanel(ECardPanel.CardState.PHOTO);
//		this.cardPanel = new ECardPanel(ECardPanel.CardState.EMPTY);
		rv.addComponent( new org.lgna.croquet.components.SwingAdapter(app.cardPanel), org.lgna.croquet.components.BorderPanel.Constraint.CENTER );

		if (app.isRibbonBased()) {
			org.lgna.croquet.components.FolderTabbedPane< ? > folderTabbedPane = uist.ecard.ribbon.ECardRibbonModel.getInstance().createFolderTabbedPane();
			folderTabbedPane.setBackgroundColor( java.awt.SystemColor.controlShadow );
			rv.addComponent( folderTabbedPane, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START );
		} else {
			app.getFrame().setMenuBarModel( uist.ecard.menu.MenuBarComposite.getInstance() );

			// Make mock toolbar
			javax.swing.JToolBar toolbar = new javax.swing.JToolBar();
			toolbar.setFloatable(false);
			toolbar.setRollover(true);

			JButton newButton = new JButton();
			newButton.setFocusable(false);
			newButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/document-new.png")));
			toolbar.add(newButton);

			JButton openButton = new JButton();
			openButton.setFocusable(false);
			openButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/document-open.png")));
			toolbar.add(openButton);

			JButton saveButton = new JButton();
			saveButton.setFocusable(false);
			saveButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/document-save.png")));
			toolbar.add(saveButton);

			toolbar.addSeparator();

			JButton printButton = new JButton();
			printButton.setFocusable(false);
			//printButton.setText("Print");
			printButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/document-print.png")));
			toolbar.add(printButton);

			toolbar.addSeparator();

			JButton undoButton = new JButton();
			undoButton.setFocusable(false);
			undoButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/edit-undo.png")));
			toolbar.add(undoButton);

			JButton redoButton = new JButton();
			redoButton.setFocusable(false);
			redoButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/edit-redo.png")));
			toolbar.add(redoButton);

			toolbar.addSeparator();

			JButton copyButton = new JButton();
			copyButton.setFocusable(false);
			copyButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/edit-copy.png")));
			toolbar.add(copyButton);	

			JButton cutButton = new JButton();
			cutButton.setFocusable(false);
			cutButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/edit-cut.png")));
			toolbar.add(cutButton);	

			JButton pasteButton = new JButton();
			pasteButton.setFocusable(false);
			pasteButton.setIcon(new ImageIcon(ECardApplication.class.getResource("resources/toolbar/edit-paste.png")));
			toolbar.add(pasteButton);	

			//toolbar.addSeparator();

			//JButton deleteJButton = DeletePictureModel.getInstance().createButton().getAwtComponent();
			//deleteJButton.setFocusable(false);
			//toolbar.add(deleteJButton);	

			rv.addComponent(new org.lgna.croquet.components.SwingAdapter(toolbar) , org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START );
		}

		return rv;
	}
}
