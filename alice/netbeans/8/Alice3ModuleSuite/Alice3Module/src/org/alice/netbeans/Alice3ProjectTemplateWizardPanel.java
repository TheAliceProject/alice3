/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alice.netbeans;

import java.awt.Component;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * Panel just asking for basic info.
 */
public class Alice3ProjectTemplateWizardPanel implements WizardDescriptor.Panel, WizardDescriptor.ValidatingPanel, WizardDescriptor.FinishablePanel {

	public Alice3ProjectTemplateWizardPanel() {
	}

	public Component getComponent() {
		if (this.component == null) {
			this.component = new Alice3ProjectTemplatePanelVisual(this);
			this.component.setName(NbBundle.getMessage(Alice3ProjectTemplateWizardPanel.class, "LBL_CreateProjectStep"));
		}
		return this.component;
	}

	public HelpCtx getHelp() {
		return new HelpCtx(Alice3ProjectTemplateWizardPanel.class);
	}

	public boolean isValid() {
		getComponent();
		return this.component.valid(this.wizardDescriptor);
	}

	public final void addChangeListener(ChangeListener l) {
		synchronized (this.listeners) {
			this.listeners.add(l);
		}
	}

	public final void removeChangeListener(ChangeListener l) {
		synchronized (this.listeners) {
			this.listeners.remove(l);
		}
	}

	protected final void fireChangeEvent() {
		ChangeEvent ev = new ChangeEvent(this);
		for (ChangeListener l : listeners) {
			l.stateChanged(ev);
		}
	}

	public void readSettings(Object settings) {
		this.wizardDescriptor = (WizardDescriptor) settings;
		this.component.read(wizardDescriptor);
	}

	public void storeSettings(Object settings) {
		WizardDescriptor d = (WizardDescriptor) settings;
		this.component.store(d);
	}

	public boolean isFinishPanel() {
		return true;
	}

	public void validate() throws WizardValidationException {
		getComponent();
		this.component.validate(wizardDescriptor);
	}

	private final List<ChangeListener> listeners = new CopyOnWriteArrayList<ChangeListener>();
	private WizardDescriptor wizardDescriptor;
	private Alice3ProjectTemplatePanelVisual component;
}
