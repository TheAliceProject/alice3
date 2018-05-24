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

import edu.cmu.cs.dennisc.java.util.Lists;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class GitRepo {
	public GitRepo( Config config, String name ) {
		this.config = config;

		this.rootDir = new File( config.getRootDir(), name );
		assert this.rootDir.exists() : this.rootDir;
		assert this.rootDir.isDirectory() : this.rootDir;

		this.plugin8 = new Plugin8( this.config, this.rootDir );
		this.plugin6 = new Plugin6( this.config, this.rootDir );
	}

	public Config getConfig() {
		return this.config;
	}

	public File getRootDir() {
		return this.rootDir;
	}

	public List<Plugin> getPlugins() {
		List<Plugin> list = Lists.newLinkedList();
		if( this.config.isPlugin8Desired() ) {
			list.add( this.plugin8 );
		}
		if( this.config.isPlugin6Desired() ) {
			list.add( this.plugin6 );
		}
		return Collections.unmodifiableList( list );
	}

	public Plugin8 getPlugin8() {
		return this.plugin8;
	}

	private final Config config;
	private final File rootDir;
	private final Plugin8 plugin8;
	private final Plugin6 plugin6;
}
