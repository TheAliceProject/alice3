package edu.cmu.cs.dennisc.util.jar;

public class JarUtilities {
	public static java.util.jar.JarFile getJarFileOnClassPathNamed( String jarFileName ) throws java.io.IOException {
		String[] paths = edu.cmu.cs.dennisc.lang.ClassPathUtilities.getClassPath();
		for( String path : paths ) {
			if( path.endsWith( jarFileName ) ) {
				return new java.util.jar.JarFile( path );
			}
		}
		return null;
	}
//	public static java.util.List<String> getEntryNames( java.util.jar.JarFile jarFile ) {
//		java.util.List<String> rv = new java.util.LinkedList<String>();
//		java.util.Enumeration< java.util.jar.JarEntry > e = jarFile.entries();
//		while( e.hasMoreElements() ) {
//			java.util.jar.JarEntry jarEntry = e.nextElement();
//			rv.add( jarEntry.getName() );
//		}
//		return rv;
//	}
	public static java.util.List< java.util.jar.JarEntry > getResources( java.util.jar.JarFile jarFile, String packageName, String extension, boolean isRecursionDesired ) {
		String suffix;
		if( extension.startsWith( "." ) ) {
			suffix = extension;
		} else {
			suffix = "." + extension;
		}
		String prefix = packageName.replace( '.', '/' );
		java.util.List< java.util.jar.JarEntry > rv = new java.util.LinkedList<java.util.jar.JarEntry>();
		java.util.Enumeration< java.util.jar.JarEntry > e = jarFile.entries();
		while( e.hasMoreElements() ) {
			java.util.jar.JarEntry jarEntry = e.nextElement();
			if( jarEntry.isDirectory() ) {
				//pass
			} else {
				String entryName = jarEntry.getName();
				if( entryName.startsWith( prefix ) ) {
					if( entryName.endsWith( suffix ) ) {
						if( isRecursionDesired || entryName.substring( prefix.length()+1 ).indexOf( '/' ) == -1 ) {
							rv.add( jarEntry );
						}
					}
				}
			}
		}
		return rv;
	}
	public static java.util.List< java.util.jar.JarEntry > getResources( java.util.jar.JarFile jarFile, Package pckg, String extension, boolean isRecursionDesired ) {
		return getResources(jarFile, pckg.getName(), extension, isRecursionDesired);
	}
	public static void main(String[] args) throws Exception {
//		edu.cmu.cs.dennisc.print.PrintUtilities.printlns( edu.cmu.cs.dennisc.lang.ClassPathUtilities.getClassPath() );
		java.util.jar.JarFile jarFile = getJarFileOnClassPathNamed("mail.jar");
		for( java.util.jar.JarEntry entry : getResources(jarFile, javax.mail.Flags.class.getPackage(), "class", false) ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( entry );
		}
	}
}
