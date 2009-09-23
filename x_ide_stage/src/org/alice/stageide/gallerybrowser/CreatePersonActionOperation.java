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
/**
 * @author Dennis Cosgrove
 */
class CreateFieldFromPersonPane extends org.alice.ide.createdeclarationpanes.CreateLargelyPredeterminedFieldPane {
	public CreateFieldFromPersonPane(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, org.alice.apis.stage.Person person) {
		super(declaringType, person.getClass(), null);
	}
}

/**
 * @author Dennis Cosgrove
 */
class CreatePersonActionOperation extends AbstractDeclareFieldOperation {
	public CreatePersonActionOperation() {
		this.putValue(javax.swing.Action.NAME, "Create Person...");
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, java.lang.Object> createFieldAndInstance(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType) {
		final org.alice.ide.IDE ide = getIDE();
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
						org.alice.apis.stage.Person person = personEditorInputPane.showInJDialog(ide);
						if (person != null) {
							edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = ide.getSceneType();
							CreateFieldFromPersonPane createFieldFromPersonPane = new CreateFieldFromPersonPane(declaringType, person);
							edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromPersonPane.showInJDialog(ide);
							if (field != null) {
								ide.getSceneEditor().handleFieldCreation(declaringType, field, person);
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
		PersonCreatorDialog dialog = new PersonCreatorDialog(ide);
		dialog.pack();
		dialog.setVisible(true);
		dialog.createAndExecuteWorker();
		
		javax.swing.JOptionPane.showMessageDialog( null, "todo: CreatePersonActionOperation" );
		return null;
	}
}
