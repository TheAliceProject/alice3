package org.alice.ide.declarationseditor.events.components;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

import org.alice.ide.codeeditor.CodeEditor;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.components.PopupButton;
import org.lgna.project.ast.AbstractCode;

public class EventListenersView extends DeclarationView {

	public EventListenersView(DeclarationComposite composite) {
		super(composite);
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();

		this.addComponent( button, Constraint.PAGE_START);
		this.addComponent( new CodeEditor((AbstractCode)composite.getDeclaration()), Constraint.CENTER );
	}

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPrintSupported() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
