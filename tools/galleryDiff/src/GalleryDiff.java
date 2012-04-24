import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */

/**
 * @author dculyba
 *
 */
public class GalleryDiff {
	
	private static class CustomClassLoader extends java.net.URLClassLoader {
		
		public CustomClassLoader(URL[] urls) {
			super(urls);
		}

		//Workaround for a bug where the class loader holds onto a reference to a loaded jar file and doesn't let us delete the file
		public void close() {
			try {
				Class clazz = java.net.URLClassLoader.class;
				java.lang.reflect.Field ucp = clazz.getDeclaredField("ucp");
				ucp.setAccessible(true);
				Object sun_misc_URLClassPath = ucp.get(this);
				java.lang.reflect.Field loaders = sun_misc_URLClassPath
						.getClass().getDeclaredField("loaders");
				loaders.setAccessible(true);
				Object java_util_Collection = loaders
						.get(sun_misc_URLClassPath);
				for (Object sun_misc_URLClassPath_JarLoader : ((java.util.Collection) java_util_Collection)
						.toArray()) {
					try {
						java.lang.reflect.Field loader = sun_misc_URLClassPath_JarLoader
								.getClass().getDeclaredField("jar");
						loader.setAccessible(true);
						Object java_util_jar_JarFile = loader
								.get(sun_misc_URLClassPath_JarLoader);
						((java.util.jar.JarFile) java_util_jar_JarFile).close();
					} catch (Throwable t) {
						// if we got this far, this is probably not a JAR loader
						// so skip it
					}
				}
			} catch (Throwable t) {
				// probably not a SUN VM
			}
			return;
		}
	}

	private org.lgna.project.Version prevVersion;
	private org.lgna.project.Version curVersion;
	
	private java.util.List<String> prevSymbols;
	private java.util.List<String> curSymbols;
	
	private java.util.List<String> unMatchedSymbols;
	private java.util.List<String> conversionMap;
	
	private static final int SCORE_THRESHOLD = 10;
	
	private GalleryDiff(org.lgna.project.Version prevVersion, org.lgna.project.Version curVersion) {
		this.prevVersion = prevVersion;
		this.curVersion = curVersion;
	}
	
	public GalleryDiff(org.lgna.project.Version prevVersion, org.lgna.project.Version curVersion, java.io.File[] prevJars, java.io.File[] curJars) {
		this(prevVersion, curVersion);
		
		prevSymbols = new java.util.ArrayList<String>();
		curSymbols = new java.util.ArrayList<String>();
		try {
			for (File jar : prevJars) {
				prevSymbols.addAll(loadResourceSymbols(jar));
			}
			for (File jar : curJars) {
				curSymbols.addAll(loadResourceSymbols(jar));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		doMatch();
	}
	
	public GalleryDiff(org.lgna.project.Version prevVersion, org.lgna.project.Version curVersion, java.io.File prevDataFile, java.io.File curDataFile, String... jarFileNames) {
		this(prevVersion, curVersion);
		
		prevSymbols = new java.util.ArrayList<String>();
		curSymbols = new java.util.ArrayList<String>();
		try {
			for (String fileName : jarFileNames) {
				byte[] prevData = getBytesFromDataZip(prevDataFile, fileName);
				prevSymbols.addAll(loadResourceSymbols(prevData));
				
				byte[] curData = getBytesFromDataZip(curDataFile, fileName);
				curSymbols.addAll(loadResourceSymbols(curData));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		doMatch();
	}
	
	private void doMatch()
	{
		this.unMatchedSymbols = new java.util.ArrayList<String>();
		this.conversionMap = new java.util.ArrayList<String>();
		for (String symbol : prevSymbols) {
			List<String> matches = getMatchingSymbol(symbol, curSymbols);
			if (matches.size() > 0) {
				int score = computeLevenshteinDistance(symbol, matches.get(0));
				if (score == 0) {
					System.out.println("No change found for symbol '"+symbol+"'");
				}
				else if (score < SCORE_THRESHOLD) {
					if (matches.size() == 1) {
						conversionMap.add(symbol);
						conversionMap.add(matches.get(0));
					}
					else {
						System.err.println("Found multiple matches for '"+symbol+"' :");
						for (String m : matches) {
							System.err.println("  "+m);
						}
						System.err.println("Too many matches, marking as unmatched\n");
						this.unMatchedSymbols.add(symbol);
					}
				}
				else {
					System.err.println("No matches withing "+SCORE_THRESHOLD+" found for '"+symbol+"'\n");
					this.unMatchedSymbols.add(symbol);
				}
			}
			else {
				System.err.println("No matches found for '"+symbol+"'\n");
				this.unMatchedSymbols.add(symbol);
			}
		}
		
		System.out.println("\n\nResults:");
		
		for (int i=0; i<conversionMap.size(); i+=2) {
			System.out.println("  "+conversionMap.get(i)+" -> "+conversionMap.get(i+1));
		}
		System.out.println("\nNo matches:");
		if (this.unMatchedSymbols.size() > 0) {
			for (String s : this.unMatchedSymbols) {
				System.err.println("  "+s);
			}
		}
		else {
			System.out.println("  NO UNMATCHED SYMBOLS");
		}
		System.out.println();
	}
	
	public String getMigrationCode() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("new org.lgna.project.migration.TextMigration(\n");
		sb.append("\tnew org.lgna.project.Version( \""+ this.prevVersion.toString() +"\" ),\n");
		sb.append("\tnew org.lgna.project.Version( \""+ this.curVersion.toString() +"\" ),\n");
		
		for (int i=0; i<conversionMap.size(); i+=2) {
			sb.append("\n\t\""+conversionMap.get(i)+"\",\n");
			String lastComma = i < conversionMap.size()-2 ? "," : "";
			sb.append("\t\""+conversionMap.get(i+1)+"\""+lastComma+"\n");
		}
		sb.append(")\n");
		
		for (String s : this.unMatchedSymbols) {
			sb.append("UNMATCHED: "+s+"\n");
		}
		
		return sb.toString();
	}
	
	private static byte[] getBytesFromDataZip(File dataZip, String fileName) {
		try
		{
			java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(new java.io.FileInputStream(dataZip));
			ZipEntry entry = zis.getNextEntry();
			while (entry != null)
			{
				if (entry.getName().endsWith(fileName))
				{
					byte[] data = edu.cmu.cs.dennisc.zip.ZipUtilities.extractBytes(zis, entry);
					zis.close();
					return data;
				}
				entry = zis.getNextEntry();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static java.io.InputStream getJarStreamFromDataZip(File dataZip, String fileName) {
		try
		{
			java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(new java.io.FileInputStream(dataZip));
			ZipEntry entry = zis.getNextEntry();
			while (entry != null)
			{
				if (entry.getName().endsWith(fileName))
				{
					byte[] data = edu.cmu.cs.dennisc.zip.ZipUtilities.extractBytes(zis, entry);
					zis.close();
					return new java.io.ByteArrayInputStream(data);
				}
				entry = zis.getNextEntry();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<String> getMatchingSymbol(String symbol, List<String> symbols) {
		int minScore = Integer.MAX_VALUE;
		List<String> best = new java.util.ArrayList<String>();
		boolean isSymbolEnum = isEnum(symbol);
		for (String s : symbols) {
			if (isEnum(s) == isSymbolEnum) {
				int score = computeLevenshteinDistance(symbol, s);
				if (score < minScore) {
					best.clear();
					best.add(s);
					minScore = score;
				}
				else if (score == minScore) {
					best.add(s);
				}
			}
		}
		return best;
	}
	
	public static java.util.List<String> getClassNamesFromStream(java.io.InputStream stream) {
		List<String> classNames = new java.util.ArrayList<String>();
		try
		{
			java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(stream);
			ZipEntry entry = zis.getNextEntry();
			while (entry != null)
			{
				if (entry.getName().endsWith(".java") && !entry.getName().contains("$"))
				{
					String className = entry.getName().replace('/', '.');
					int lastDot = className.lastIndexOf(".");
					String baseName = className.substring(0, lastDot);
					if (baseName.startsWith(".")) {
						baseName = baseName.substring(1);
					}
					
					classNames.add(baseName);
				}
				entry = zis.getNextEntry();
			}
			zis.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return classNames;
	}
	
	public static java.util.List<String> getClassNamesFromJar(java.io.File jar) {
		
		List<String> classNames = new java.util.ArrayList<String>();
		try
		{
			classNames = getClassNamesFromStream(new java.io.FileInputStream(jar));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return classNames;
	}
	
	public static boolean isEnum(String s) {
		if (s == null) {
			return false;
		}
		String[] split = s.split("\\.");
		String enumName;
		if (split.length > 0) {
			enumName = split[split.length-1];
		}
		else {
			enumName = s;
		}
		if (enumName == null || enumName.length() == 0) {
			return false;
		}
		for (int i=0; i<enumName.length(); i++) {
			char c = enumName.charAt(i);
			if (!Character.isUpperCase(c) && !Character.isDigit(c) && c != '_') {
				return false;
			}
		}
		return true;
	}
	
	public static java.util.List<String> loadResourceSymbols(byte[] inputData) throws MalformedURLException, java.io.IOException {
		
		File tempFile = File.createTempFile("aliceGalleryDiff", ".jar");
		writeDataToFile(inputData, tempFile);
		
		List<String> symbols = new java.util.ArrayList<String>();
		List<String> classNames = getClassNamesFromJar(tempFile);
		java.net.URL jarURL = tempFile.toURI().toURL();
		CustomClassLoader cl = new CustomClassLoader(new java.net.URL[]{jarURL});
		
		for (String className : classNames)
		{
			try {
				Class<?> cls = cl.loadClass(className);
				if (org.lgna.story.resources.ModelResource.class.isAssignableFrom(cls) && cls.isEnum())
				{
					org.lgna.story.resources.ModelResource[] enums = (org.lgna.story.resources.ModelResource[])cls.getEnumConstants();
					for (org.lgna.story.resources.ModelResource resource : enums) {
						symbols.add(cls.getName()+"."+resource.toString());
					}
				}
				symbols.add(cls.getName());
			}
			catch (ClassNotFoundException cnfe) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe("FAILED TO LOAD GALLERY CLASS: "+className);
			}
		}
		jarURL = null;
		cl.close();
		cl = null;
		System.gc();
		if (!tempFile.delete())
		{
			System.out.println("Failed to delete. Deleting on exit.");
		}
		return symbols;
	}
	
	public static java.util.List<String> loadResourceSymbols(java.io.File jar) throws MalformedURLException {
		List<String> symbols = new java.util.ArrayList<String>();
		List<String> classNames = getClassNamesFromJar(jar);
		java.net.URL jarURL = jar.toURI().toURL();
		java.net.URLClassLoader cl = new java.net.URLClassLoader(new java.net.URL[]{jarURL});
		
		for (String className : classNames)
		{
			try {
				Class<?> cls = cl.loadClass(className);
				if (org.lgna.story.resources.ModelResource.class.isAssignableFrom(cls) && cls.isEnum())
				{
					org.lgna.story.resources.ModelResource[] enums = (org.lgna.story.resources.ModelResource[])cls.getEnumConstants();
					for (org.lgna.story.resources.ModelResource resource : enums) {
						symbols.add(cls.getName()+"."+resource.toString());
					}
				}
				symbols.add(cls.getName());
			}
			catch (ClassNotFoundException cnfe) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe("FAILED TO LOAD GALLERY CLASS: "+className);
			}
		}
		return symbols;
	}
	
	private static void writeDataToFile(byte[] data, File destFile) {
		java.io.FileOutputStream fos = null;
		try {
			fos = new java.io.FileOutputStream(destFile);
			fos.write(data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			fos = null;
			System.gc();
		}
		
	}
	
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public static int computeLevenshteinDistance(CharSequence str1,
			CharSequence str2) {
		int[][] distance = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++)
			distance[i][0] = i;
		for (int j = 0; j <= str2.length(); j++)
			distance[0][j] = j;

		for (int i = 1; i <= str1.length(); i++)
			for (int j = 1; j <= str2.length(); j++)
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]
								+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
										: 1));

		return distance[str1.length()][str2.length()];
	}
	
	public static void main( String[] args ) throws Exception {
		
		if (args.length < 4 || args.length % 4 != 0) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe("Proper use: GalleryDiff <prevVersion> <curVersion> <prevJar1> <prevJar2> ... <curJar1> <curJar2> ...");
			return;
		}
		File baseDir = new File(System.getProperty( "user.dir" ));
		File baseAliceDir = baseDir;
		if (baseAliceDir.exists() && baseAliceDir.getParentFile() != null) {
			baseAliceDir = baseAliceDir.getParentFile();
			if (baseAliceDir.exists() && baseAliceDir.getParent() != null) {
				baseAliceDir = baseAliceDir.getParentFile();
				if (baseAliceDir.exists()) {
					baseAliceDir = new File(baseAliceDir, "ide/lib/alice");
					if (!baseAliceDir.exists()) {
						baseAliceDir = null;
					}
				}
				else {
					baseAliceDir = null;
				}
			}
			else {
				baseAliceDir = null;
			}
		}
		else {
			baseAliceDir = null;
		}
		File baseTestDir = new File(baseDir, "test");
		if (!baseTestDir.exists()) {
			baseTestDir = null;
		}
		int argsOffset = 2;
		int fileCount = args.length - argsOffset;
		File[] files = new File[fileCount];
		for (int i=0; i<files.length; i++) {
			files[i] = new File(args[argsOffset+i]);
			if (!files[i].exists() && baseDir != null) {
				files[i] = new File(baseDir, args[argsOffset+i]);
			}
			if (!files[i].exists() && baseTestDir != null) {
				files[i] = new File(baseTestDir, args[argsOffset+i]);
			}
			if (!files[i].exists()) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe("Failed to find file '"+args[argsOffset+i]+"'");
				return;
			}
		}
		int toDiffCount = files.length /2;
		File[] prevFiles = new File[toDiffCount];
		System.arraycopy(files, 0, prevFiles, 0, toDiffCount);
		File[] curFiles = new File[toDiffCount];
		System.arraycopy(files, toDiffCount, curFiles, 0, toDiffCount);
		
		org.lgna.project.Version previousVersion = new org.lgna.project.Version(args[0]);
		org.lgna.project.Version currentVersion = new org.lgna.project.Version(args[1]);
		boolean areZips = false;
		for (File f : files) {
			if (edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension(f).equalsIgnoreCase("zip")) {
				areZips = true;
				break;
			}
		}
		GalleryDiff differ;
		if (areZips) {
			differ = new GalleryDiff(previousVersion, currentVersion, prevFiles[0], curFiles[0], new String[] {"aliceModelSource.jar", "nebulousModelSource.jar"});
		}
		else {
			differ = new GalleryDiff(previousVersion, currentVersion, prevFiles, curFiles);
		}
		String code = differ.getMigrationCode();
		edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents(code);
		System.out.println(code);
		System.out.println();
	}
	
}
