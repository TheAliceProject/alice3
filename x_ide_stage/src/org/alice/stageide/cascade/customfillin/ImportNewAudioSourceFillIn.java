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
package org.alice.stageide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public class ImportNewAudioSourceFillIn extends ImportNewSourceFillIn< org.alice.apis.moveandturn.AudioSource, org.alice.virtualmachine.resources.AudioResource > {
	@Override
	protected String getMenuText() {
		return "Import New Audio Source...";
	}
	@Override
	protected org.alice.ide.resource.ResourcePrompter<org.alice.virtualmachine.resources.AudioResource> getResourcePrompter() {
		return org.alice.ide.resource.AudioResourcePrompter.getSingleton();
	}
	@Override
	protected Class<org.alice.virtualmachine.resources.AudioResource> getResourceClass() {
		return org.alice.virtualmachine.resources.AudioResource.class;
	}
	@Override
	protected Class<org.alice.apis.moveandturn.AudioSource> getSourceClass() {
		return org.alice.apis.moveandturn.AudioSource.class;
	}
}
