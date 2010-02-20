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
package org.alice.ide.resource.prompter;


//todo: rename
/**
 * @author Dennis Cosgrove
 */
public class AudioResourcePrompter extends ResourcePrompter< org.alice.virtualmachine.resources.AudioResource> {
	private static AudioResourcePrompter singleton = new AudioResourcePrompter();
	public static AudioResourcePrompter getSingleton() {
		return singleton;
	}
	private AudioResourcePrompter() {
	}
	@Override
	protected String getInitialFileText() {
		return "*.mp3;*.wav;*.au";
	}
	
	@Override
	protected org.alice.virtualmachine.resources.AudioResource createResourceFromFile( java.io.File file ) throws java.io.IOException {
		return edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton().createAudioResource( file );
	}
}
