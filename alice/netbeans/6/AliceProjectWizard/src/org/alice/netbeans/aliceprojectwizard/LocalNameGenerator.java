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

import org.lgna.project.ast.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class LocalNameGenerator {

	private Map<UserLocal, String> map = new HashMap<UserLocal, String>();

	public LocalNameGenerator(AbstractCode code) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<LocalDeclarationStatement> ldsCrawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance(LocalDeclarationStatement.class);
		code.crawl(ldsCrawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY);

		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<CountLoop> clCrawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance(CountLoop.class);
		code.crawl(clCrawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY);

		Set<String> usedNames = new HashSet<String>();

		for (AbstractParameter parameter : code.getRequiredParameters()) {
			String name = parameter.getName();
			if (name != null) {
				usedNames.add(name);
			} else {
				//todo
			}
		}

		for (LocalDeclarationStatement localDeclarationStatement : ldsCrawler.getList()) {
			String name = localDeclarationStatement.local.getName();
			if (name != null) {
				usedNames.add(name);
			} else {
				//todo
			}
		}

		for (CountLoop countLoop : clCrawler.getList()) {
			UserLocal i = countLoop.variable.getValue();
			UserLocal n = countLoop.constant.getValue();

			String nName = n.getName();
			if (nName != null) {
				usedNames.add(nName);
			}

			String iName = i.getName();
			if (iName != null) {
				usedNames.add(iName);
			}

		}

		for (CountLoop countLoop : clCrawler.getList()) {
			UserLocal i = countLoop.variable.getValue();
			UserLocal n = countLoop.constant.getValue();

			String iName = i.getName();
			if (iName != null) {
				//pass
			} else {
				iName = this.generateNameForCountLoopVariable(usedNames);
				this.map.put(i, iName);
				usedNames.add(iName);
			}

			String nName = n.getName();
			if (nName != null) {
				//pass
			} else {
				nName = this.generateNameForCountLoopConstant(usedNames);
				this.map.put(n, nName);
				usedNames.add(nName);
			}
		}
	}

	public String getNameFor(UserLocal local, Node context) {
		String rv = local.name.getValue();
		if (rv != null) {
			//pass
		} else {
			rv = this.map.get(local);
			if (rv != null) {
				//pass
			} else {
				rv = local.getValidName(context);
			}
		}
		return rv;
	}

	private String generateNameForCountLoopLocal(Set<String> usedNames, char cFirst, char cLast) {
		char c = cFirst;
		while (usedNames.contains(c + "")) {
			if (c == cLast) {
				return null;
			} else {
				c += 1;
			}
		}
		return c + "";
	}

	private String generateNameForCountLoopVariable(Set<String> usedNames) {
		return generateNameForCountLoopLocal(usedNames, 'i', (char) ('i' + 'Z' - 'N'));
	}

	private String generateNameForCountLoopConstant(Set<String> usedNames) {
		return generateNameForCountLoopLocal(usedNames, 'N', 'Z');
	}
}
