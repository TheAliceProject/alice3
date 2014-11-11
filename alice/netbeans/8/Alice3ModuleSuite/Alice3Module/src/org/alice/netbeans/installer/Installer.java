/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alice.netbeans.installer;

import org.alice.netbeans.logging.IoLoggingHandler;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

	@Override
	public void restored() {
		IoLoggingHandler.initialize();
	}

}
