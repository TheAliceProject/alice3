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
package edu.cmu.cs.dennisc.alice;

/**
 * @author Dennis Cosgrove
 */
public class License {
	public static final String TEXT;
	static {
//		StringBuffer sb = new StringBuffer();
//		sb.append( "Alice 3 End User License Agreement\n\n" );
//		sb.append( "Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.\n\n" );
//		sb.append( "Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n\n" );
//		sb.append( "1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n\n" );
//		sb.append( "2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n\n" );
//		sb.append( "3. Products derived from the software may not be called \"Alice\", nor may \"Alice\" appear in their name, without prior written permission of Carnegie Mellon University.\n\n" );
//		sb.append( "4. All advertising materials mentioning features or use of this software must display the following acknowledgement: \"This product includes software developed by Carnegie Mellon University\"\n\n" );
//		sb.append( "5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for academic, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.\n\n" );
//		sb.append( "DISCLAIMER:\n\n" );
//		sb.append( "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n" );
//		TEXT = sb.toString();
		TEXT = edu.cmu.cs.dennisc.io.TextFileUtilities.read( License.class.getResourceAsStream( "License.txt" ) );
	}
}
