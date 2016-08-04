/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.netbeans.aliceprojectwizard;

import edu.cmu.cs.dennisc.java.util.logging.ConsoleFormatter;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * Panel just asking for basic info.
 */
public class AliceProjectTemplateWizardPanel implements WizardDescriptor.Panel, WizardDescriptor.ValidatingPanel, WizardDescriptor.FinishablePanel {

	private WizardDescriptor wizardDescriptor;
	private AliceProjectTemplatePanelVisual component;
	private boolean isValid = false;

	public AliceProjectTemplateWizardPanel() {
		IOLoggingHandler ioLoggingHandler = new IOLoggingHandler();
		ioLoggingHandler.setFormatter(new ConsoleFormatter());
		Logger.getInstance().addHandler(ioLoggingHandler);
		Logger.setLevel(Level.INFO);
		Logger.info("start");
	}

	public Component getComponent() {
		if (component == null) {
			component = new AliceProjectTemplatePanelVisual(this);
			component.setName(NbBundle.getMessage(AliceProjectTemplateWizardPanel.class, "LBL_CreateProjectStep"));
		}
		return component;
	}

	public HelpCtx getHelp() {
		return new HelpCtx(AliceProjectTemplateWizardPanel.class);
	}

	public boolean isValid() {
		getComponent();
		isValid = component.valid(wizardDescriptor);
		return isValid;
	}
	private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0

	public final void addChangeListener(ChangeListener l) {
		synchronized (listeners) {
			listeners.add(l);
		}
	}

	public final void removeChangeListener(ChangeListener l) {
		synchronized (listeners) {
			listeners.remove(l);
		}
	}

	protected final void fireChangeEvent() {
		Set<ChangeListener> ls;
		synchronized (listeners) {
			ls = new HashSet<ChangeListener>(listeners);
		}
		ChangeEvent ev = new ChangeEvent(this);
		for (ChangeListener l : ls) {
			l.stateChanged(ev);
		}
	}

	public void readSettings(Object settings) {
		wizardDescriptor = (WizardDescriptor) settings;
		component.read(wizardDescriptor);
	}

	public void storeSettings(Object settings) {
		WizardDescriptor d = (WizardDescriptor) settings;
		component.store(d);
	}

	public boolean isFinishPanel() {
		return isValid;
	}

	public void validate() throws WizardValidationException {
		getComponent();
		component.validate(wizardDescriptor);
	}
}
