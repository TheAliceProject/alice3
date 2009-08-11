/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.gallerybrowser;

import edu.cmu.cs.dennisc.browser.BrowserProgressDialog;

/**
 * @author Dennis Cosgrove
 */
class CreateFieldFromPersonPane extends org.alice.ide.createdeclarationpanes.CreateLargelyPredeterminedFieldPane {
	public CreateFieldFromPersonPane(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, org.alice.apis.stage.Person person) {
		super(declaringType, person.getClass(), null);
	}
}

class CreatePersonActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	public CreatePersonActionOperation() {
		this.putValue(javax.swing.Action.NAME, "Create Person...");
	}

	public void perform(zoot.ActionContext actionContext) {
		class PersonCreatorDialog extends edu.cmu.cs.dennisc.progress.ProgressDialog {
			public PersonCreatorDialog(javax.swing.JDialog owner) {
				super(owner);
			}

			public PersonCreatorDialog(javax.swing.JFrame owner) {
				super(owner);
			}

			@Override
			protected edu.cmu.cs.dennisc.progress.ProgressDialog.Worker createWorker() {
				class PersonCreatorWorker extends edu.cmu.cs.dennisc.progress.ProgressDialog.Worker {
					@Override
					protected Boolean doInBackground() throws Exception {
						this.publish("opening person creator...");
						org.alice.stageide.personeditor.PersonEditorInputPane personEditorInputPane = new org.alice.stageide.personeditor.PersonEditorInputPane(null) {
							@Override
							public void addNotify() {
								super.addNotify();
								PersonCreatorDialog.this.setVisible( false );
							}
						};
						org.alice.apis.stage.Person person = personEditorInputPane.showInJDialog(getIDE());
						if (person != null) {
							edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = getIDE().getSceneType();
							CreateFieldFromPersonPane createFieldFromPersonPane = new CreateFieldFromPersonPane(declaringType, person);
							edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromPersonPane.showInJDialog(getIDE());
							if (field != null) {
								getIDE().getSceneEditor().handleFieldCreation(declaringType, field, person);
							}
						}
						return true;
					}
				}
				return new PersonCreatorWorker();
			}

			@Override
			protected void handleDone(Boolean result) {
//				if (result) {
//					this.setVisible(false);
//				}
			}

			@Override
			protected boolean isProgressBarDesired() {
				return false;
			}
		}
		PersonCreatorDialog dialog = new PersonCreatorDialog(this.getIDE());
		dialog.pack();
		dialog.setVisible(true);
		dialog.createAndExecuteWorker();
	}
}

class CreateTextActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	public CreateTextActionOperation() {
		this.putValue(javax.swing.Action.NAME, "Create Text...");
	}

	public void perform(zoot.ActionContext actionContext) {
		CreateTextPane createTextPane = new CreateTextPane();
		org.alice.apis.moveandturn.Text text = createTextPane.showInJDialog(getIDE(), "Create Text", true);
		if (text != null) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getIDE().getTypeDeclaredInAliceFor(org.alice.apis.moveandturn.Text.class);
			edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation(type);
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice(text.getName(), type, initializer);
			field.finalVolatileOrNeither.setValue(edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL);
			field.access.setValue(edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE);
			getIDE().getSceneEditor().handleFieldCreation(getIDE().getSceneType(), field, text);

			edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: handle text", text);
			actionContext.put(org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, true);
			actionContext.commit();
		} else {
			actionContext.cancel();
		}

	}
}

abstract class CreateInstanceFromFileActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	protected abstract java.io.File getInitialDirectory();

	private void showMessageDialog(java.io.File file, boolean isValidZip) {
		StringBuffer sb = new StringBuffer();
		sb.append("Unable to create instance from file ");
		sb.append(edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible(file));
		sb.append(".\n\n");
		sb.append(getIDE().getApplicationName());
		sb.append(" is able to create instances from class files saved by ");
		sb.append(getIDE().getApplicationName());
		sb.append(".\n\nLook for files with an ");
		sb.append(edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION);
		sb.append(" extension.");
		javax.swing.JOptionPane.showMessageDialog(org.alice.ide.IDE.getSingleton(), sb.toString(), "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE);
	}

	public void perform(zoot.ActionContext actionContext) {
		java.io.File directory = this.getInitialDirectory();
		java.io.File file = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showOpenFileDialog(this.getIDE(), directory, null, edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION, false);
		if (file != null) {
			String lcFilename = file.getName().toLowerCase();
			if (lcFilename.endsWith(".a2c")) {
				javax.swing.JOptionPane.showMessageDialog(org.alice.ide.IDE.getSingleton(), "Alice3 does not load Alice2 characters", "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE);
				actionContext.cancel();
			} else if (lcFilename.endsWith(edu.cmu.cs.dennisc.alice.io.FileUtilities.PROJECT_EXTENSION.toLowerCase())) {
				javax.swing.JOptionPane.showMessageDialog(this.getIDE(), file.getAbsolutePath() + " appears to be a project file and not a class file.\n\nLook for files with an " + edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION + " extension.", "Incorrect File Type",
						javax.swing.JOptionPane.INFORMATION_MESSAGE);
			} else {
				boolean isWorthyOfException = lcFilename.endsWith(edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION.toLowerCase());
				java.util.zip.ZipFile zipFile;
				try {
					zipFile = new java.util.zip.ZipFile(file);
				} catch (java.io.IOException ioe) {
					if (isWorthyOfException) {
						throw new RuntimeException(file.getAbsolutePath(), ioe);
					} else {
						this.showMessageDialog(file, false);
						zipFile = null;
					}
				}
				if (zipFile != null) {
					edu.cmu.cs.dennisc.alice.ast.AbstractType type;
					try {
						type = edu.cmu.cs.dennisc.alice.io.FileUtilities.readType(zipFile);
					} catch (java.io.IOException ioe) {
						if (isWorthyOfException) {
							throw new RuntimeException(file.getAbsolutePath(), ioe);
						} else {
							this.showMessageDialog(file, true);
							type = null;
						}
					}
					if (type != null) {
						edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = getIDE().getSceneType();
						org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane(declaringType, type);
						edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldPane.showInJDialog(getIDE(), "Create New Instance", true);
						if (field != null) {
							getIDE().getSceneEditor().handleFieldCreation(declaringType, field, createFieldPane.createInstanceInJava());
							actionContext.put(org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, true);
							actionContext.commit();
						} else {
							actionContext.cancel();
						}
					} else {
						actionContext.cancel();
					}
				} else {
					actionContext.cancel();
				}
			}
		} else {
			actionContext.cancel();
		}
	}
}

class CreateMyInstance extends CreateInstanceFromFileActionOperation {
	public CreateMyInstance() {
		this.putValue(javax.swing.Action.NAME, "My Classes...");
	}

	@Override
	protected java.io.File getInitialDirectory() {
		return this.getIDE().getMyTypesDirectory();
	}
}

class CreateTextbookInstance extends CreateInstanceFromFileActionOperation {
	public CreateTextbookInstance() {
		this.putValue(javax.swing.Action.NAME, "Textbook Classes...");
	}

	@Override
	protected java.io.File getInitialDirectory() {
		return new java.io.File(this.getIDE().getApplicationRootDirectory(), "classes/textbook");
	}
}

class GalleryFileActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	private java.io.File file;

	public GalleryFileActionOperation(java.io.File file) {
		this.file = file;
	}

	public void perform(zoot.ActionContext actionContext) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = getIDE().getSceneType();
		org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane(declaringType, this.file);
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldPane.showInJDialog(getIDE(), "Create New Instance", true);
		if (field != null) {
			getIDE().getSceneEditor().handleFieldCreation(declaringType, field, createFieldPane.createInstanceInJava());
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class GalleryBrowser extends org.alice.ide.gallerybrowser.AbstractGalleryBrowser {
	private java.util.Map<String, String> map;

	public GalleryBrowser(java.io.File thumbnailRoot, java.util.Map<String, String> map) {
		this.map = map;
		this.initialize(thumbnailRoot);
		zoot.ZButton createPersonButton = new zoot.ZButton(new CreatePersonActionOperation());
		zoot.ZButton createTextButton = new zoot.ZButton(new CreateTextActionOperation());
		zoot.ZButton createMyInstanceButton = new zoot.ZButton(new CreateMyInstance());
		zoot.ZButton createTextbookInstanceButton = new zoot.ZButton(new CreateTextbookInstance());

		java.io.InputStream is = GalleryBrowser.class.getResourceAsStream("images/create_person.png");
		java.awt.Image image = edu.cmu.cs.dennisc.image.ImageUtilities.read(edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, is);
		createPersonButton.setIcon(new javax.swing.ImageIcon(image));
		createPersonButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		createPersonButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

		swing.Pane fromFilePane = new swing.Pane();
		fromFilePane.setLayout(new java.awt.GridLayout(2, 1, 0, 4));
		fromFilePane.add(createMyInstanceButton);
		fromFilePane.add(createTextbookInstanceButton);

		swing.BorderPane buttonPane = new swing.BorderPane();
		buttonPane.add(fromFilePane, java.awt.BorderLayout.NORTH);
		buttonPane.add(createTextButton, java.awt.BorderLayout.SOUTH);

		// this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4,
		// 4 ) );
		this.setBackground(new java.awt.Color(220, 220, 255));
		this.add(createPersonButton, java.awt.BorderLayout.WEST);
		this.add(buttonPane, java.awt.BorderLayout.EAST);
	}

	@Override
	protected String getAdornedTextFor(String name, boolean isDirectory, boolean isRequestedByPath) {
		if (this.map != null) {
			if (this.map.containsKey(name)) {
				name = this.map.get(name);
			}
		}
		return super.getAdornedTextFor(name, isDirectory, isRequestedByPath);
	}

	@Override
	protected void handleFileActivation(java.io.File file) {
		assert file.isFile();
		zoot.ZManager.performIfAppropriate(new GalleryFileActionOperation(file), null, zoot.ZManager.CANCEL_IS_WORTHWHILE);
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight(super.getPreferredSize(), 256);
	}

	public static void main(String[] args) {

		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();

		java.io.File thumbnailRoot = new java.io.File(org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory(), "thumbnails");
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened(java.awt.event.WindowEvent e) {
			}

			@Override
			protected void handleQuit(java.util.EventObject e) {
				System.exit(0);
			}
		};
		frame.setSize(new java.awt.Dimension(1024, 256));
		frame.getContentPane().add(new GalleryBrowser(thumbnailRoot, null));
		frame.setVisible(true);
	}
}
