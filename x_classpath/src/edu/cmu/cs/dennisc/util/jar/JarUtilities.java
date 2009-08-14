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
	
	public static void main(String[] args) throws Exception {
		edu.cmu.cs.dennisc.print.PrintUtilities.printlns( edu.cmu.cs.dennisc.lang.ClassPathUtilities.getClassPath() );
		java.util.jar.JarFile jarFile = getJarFileOnClassPathNamed("mail.jar");
		javax.swing.tree.TreeModel treeModel = new JarTreeModel(jarFile);
	}
}
