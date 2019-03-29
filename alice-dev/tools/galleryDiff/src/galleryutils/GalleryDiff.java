package galleryutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.project.ProjectVersion;
import org.lgna.project.Version;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
import org.lgna.story.resources.ModelResource;

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

	private static class CustomClassLoader extends URLClassLoader {

		public CustomClassLoader(URL[] urls) {
			super(urls);
		}

		// Workaround for a bug where the class loader holds onto a reference to
		// a loaded jar file and doesn't let us delete the file
		@Override
		public void close() {
			try {
				Class clazz = URLClassLoader.class;
				Field ucp = clazz.getDeclaredField("ucp");
				ucp.setAccessible(true);
				Object sun_misc_URLClassPath = ucp.get(this);
				Field loaders = sun_misc_URLClassPath
						.getClass().getDeclaredField("loaders");
				loaders.setAccessible(true);
				Object java_util_Collection = loaders
						.get(sun_misc_URLClassPath);
				for (Object sun_misc_URLClassPath_JarLoader : ((Collection) java_util_Collection)
						.toArray()) {
					try {
						Field loader = sun_misc_URLClassPath_JarLoader
								.getClass().getDeclaredField("jar");
						loader.setAccessible(true);
						Object java_util_jar_JarFile = loader
								.get(sun_misc_URLClassPath_JarLoader);
						((JarFile) java_util_jar_JarFile).close();
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

	private Version prevVersion;
	private Version curVersion;

	private List<String> prevSymbols;
	private List<String> curSymbols;

	private List<String> unMatchedSymbols;
	private List<String> alreadyMatchedSymbols;
	private List<String> conversionMap;

	private GalleryDiffDialog diffDialog = new GalleryDiffDialog();

	private static final int SCORE_THRESHOLD = 10;

	private static final String MIGRATION_FILE_NAME = "migrationData.txt";

	private GalleryDiff(Version prevVersion,
			Version curVersion) {
		initializeVersion(prevVersion, curVersion);
	}

	private void initializeVersion(Version prevVersion,
			Version curVersion) {
		this.prevVersion = prevVersion;
		this.curVersion = curVersion;
	}

	private GalleryDiff(String prevVersion, String curVersion) {
		this.prevVersion = new Version(prevVersion);
		this.curVersion = new Version(curVersion);
	}

	public GalleryDiff(Version prevVersion,
			Version curVersion, File[] prevJars,
			File[] curJars) {
		this(prevVersion, curVersion);

		prevSymbols = new ArrayList<String>();
		curSymbols = new ArrayList<String>();
		try {
			for (File jar : prevJars) {
				prevSymbols.addAll(loadResourceSymbols(jar));
			}
			for (File jar : curJars) {
				curSymbols.addAll(loadResourceSymbols(jar));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		doMatch();
	}

	public GalleryDiff(String prevVersion, String curVersion,
			String prevDataFile, String curDataFile, String... jarFileNames)
			throws IOException {
		this(new Version(prevVersion), new Version(curVersion), new File(
				prevDataFile), new File(curDataFile), jarFileNames);
	}

	public GalleryDiff(String prevVersion, String curVersion,
			File prevDataFile, File curDataFile,
			String... jarFileNames) throws IOException {
		this(new Version(prevVersion), new Version(curVersion), prevDataFile,
				curDataFile, jarFileNames);
	}

	public GalleryDiff(Version prevVersion,
			Version curVersion, File prevDataFile,
			File curDataFile, String... jarFileNames)
			throws IOException {
		this(prevVersion, curVersion);

		prevSymbols = new ArrayList<String>();
		curSymbols = new ArrayList<String>();
		for (String fileName : jarFileNames) {
			byte[] prevData = getBytesFromDataZip(prevDataFile, fileName);
			if (prevData == null) {
				throw new IOException(
						"Target jar file does not exist in zipData: "
								+ prevDataFile);
			}
			prevSymbols.addAll(loadResourceSymbols(prevData));

			byte[] curData = getBytesFromDataZip(curDataFile, fileName);
			if (curData == null) {
				throw new IOException(
						"Target jar file does not exist in zipData: "
								+ prevDataFile);
			}
			curSymbols.addAll(loadResourceSymbols(curData));
		}
		doMatch();
	}

	public GalleryDiff(File prevDataFile, File curDataFile)
			throws IOException {

		prevSymbols = new ArrayList<String>();
		curSymbols = new ArrayList<String>();

		this.prevVersion = loadGalleryInfo(prevSymbols, prevDataFile);
		this.curVersion = loadGalleryInfo(curSymbols, curDataFile);

		File migrationFile = new File(curDataFile.getParentFile(), MIGRATION_FILE_NAME);
		if (migrationFile.exists()) {
			this.loadMigrationInfo(migrationFile.getAbsoluteFile());
		}

		doMatch();
	}

	private Version loadGalleryInfo(List<String> symbolArray, File dataFile) {

		Version version = null;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(dataFile));
			String line = reader.readLine();
			version = new Version(line.trim());
			while ((line = reader.readLine()) != null) {
				symbolArray.add(line.trim());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return version;

	}

	private void loadMigrationInfo(File dataFile) {

		this.conversionMap = new ArrayList<String>();
		this.alreadyMatchedSymbols = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(dataFile));
			String line;
			boolean isSourceSymbol = true;
			while ((line = reader.readLine()) != null) {
				String symbol = line.trim();
				this.conversionMap.add(symbol);
				if (isSourceSymbol) {
					alreadyMatchedSymbols.add(symbol);
					isSourceSymbol = false;
				} else {
					isSourceSymbol = true;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File saveGalleryInfo(String version, String outputFilename,
			File... jars) {

		List<String> symbols = new ArrayList<String>();
		for (File jar : jars) {
			try {
				symbols.addAll(loadResourceSymbols(jar));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}

		File outputFile = new File(outputFilename);
		try {
			FileUtilities.createParentDirectoriesIfNecessary(outputFile);
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(outputFile));
			writer.write(version.toString() + "\n");
			for (String s : symbols) {
				writer.write(s + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return outputFile;
	}

	public static File saveGalleryInfo(Version version, String outputFilename,
			File... jars) {
		return saveGalleryInfo(version.toString(), outputFilename, jars);
	}

	private File saveMigrationInfo(String outputFilename) {
		File outputFile = new File(outputFilename);
		try {
			FileUtilities.createParentDirectoriesIfNecessary(outputFile);
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(outputFile));
			for (int i=0; i<this.conversionMap.size(); i++) {
				String s = this.conversionMap.get(i);
				if (this.unMatchedSymbols.contains(s)) {
					i++;
				} else {
					writer.write(s + "\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return outputFile;
	}

	private void doMatch() {
		this.unMatchedSymbols = new ArrayList<String>();
		if (this.conversionMap == null) {
			this.conversionMap = new ArrayList<String>();
		}
		boolean askUser = true;
		for (String symbol : prevSymbols) {
			if (this.alreadyMatchedSymbols != null && this.alreadyMatchedSymbols.contains(symbol)) {
				continue;
			}
			List<String> matches = getMatchingSymbol(symbol, curSymbols);
			if (matches.size() > 0) {
				int score = computeLevenshteinDistance(symbol, matches.get(0));
				if (score == 0) {
					// System.out.println("No change found for symbol '"+symbol+"'");
				} else {
					String match = null;
					if (matches.size() == 1) {
						match = matches.get(0);
					} else {
						if (askUser) {
							diffDialog.setOptionsAndShow(symbol, matches);
							match = diffDialog.getSelectedOption();
							if (match == null) {
								askUser = false;
							}
						}
						if (match != null) {
							//pass
						} else {
							System.err.println("Found multiple matches for '"
									+ symbol + "' :");
							for (String m : matches) {
								System.err.println("  " + m);
							}
							System.err
									.println("Too many matches, marking as unmatched\n");
							this.unMatchedSymbols.add(symbol);
						}
						//
					}
					if (match != null) {
						conversionMap.add(symbol);
						conversionMap.add(match);
					}
				}
				// else {
				// System.err.println("No matches withing "+SCORE_THRESHOLD+" found for '"+symbol+"'\n");
				// this.unMatchedSymbols.add(symbol);
				// }
			} else {
				System.err.println("No matches found for '" + symbol + "'\n");
				this.unMatchedSymbols.add(symbol);
			}
		}

		System.out.println("\n\nResults:");

		for (int i = 0; i < conversionMap.size(); i += 2) {
			System.out.println("  " + conversionMap.get(i) + " -> "
					+ conversionMap.get(i + 1));
		}
		System.out.println("\nNo matches:");
		if (this.unMatchedSymbols.size() > 0) {
			for (String s : this.unMatchedSymbols) {
				System.err.println("  " + s);
			}
		} else {
			System.out.println("  NO UNMATCHED SYMBOLS");
		}
		System.out.println();
	}

	private static String getResourceEnum(String symbol) {
		String[] split = symbol.split("\\.");
		for (String s : split) {
			if (isEnum(s)) {
				return s;
			}
		}
		return null;
	}

	private static String getMoreSpecificCodePattern(String symbol) {
		String enumSymbol = getResourceEnum(symbol);
		if (enumSymbol == null) {
			return null;
		}
		String classString = symbol.substring(0, symbol.length() - enumSymbol.length()-1);

		return "createMoreSpecificFieldPattern( \"" + enumSymbol + "\", \""
				+ classString + "\" )";
	}

	private static String getMoreSpecificCodeReplacement(String symbol) {
		String enumSymbol = getResourceEnum(symbol);
		if (enumSymbol == null) {
			return null;
		}
		String classString = symbol.substring(0, symbol.length() - enumSymbol.length()-1);

		return "createMoreSpecificFieldReplacement( \"" + enumSymbol + "\", \""
				+ classString + "\" )";
	}

	public String getMigrationCode() {
		StringBuilder sb = new StringBuilder();

		sb.append("new org.lgna.project.migration.TextMigration(\n");
		sb.append("\tnew org.lgna.project.Version( \""
				+ this.prevVersion.toString() + "\" ),\n");
		sb.append("\tnew org.lgna.project.Version( \""
				+ this.curVersion.toString() + "\" ),\n");

		for (int i = 0; i < conversionMap.size(); i += 2) {
			String firstSymbol = conversionMap.get(i);
			String secondSymbol = conversionMap.get(i + 1);

			String firstLine = getMoreSpecificCodePattern(firstSymbol);
			String secondLine = getMoreSpecificCodeReplacement(secondSymbol);
			if (firstLine == null || secondLine == null) {
				firstLine = "\"name=\\\"" + firstSymbol + "\"";
				secondLine = "\"name=\\\"" + secondSymbol + "\"";
			}
			sb.append("\n\t"+firstLine+",\n");
			String lastComma = i < conversionMap.size() - 2 ? "," : "";
			sb.append("\t"+secondLine + lastComma + "\n");
		}
		sb.append(")\n");

		for (String s : this.unMatchedSymbols) {
			sb.append("UNMATCHED: " + s + "\n");
		}

		return sb.toString();
	}

	private static byte[] getBytesFromDataZip(File dataZip, String fileName)
			throws IOException {
		ZipInputStream zis = new ZipInputStream(
				new FileInputStream(dataZip));
		ZipEntry entry = zis.getNextEntry();
		while (entry != null) {
			if (entry.getName().endsWith(fileName)) {
				byte[] data = ZipUtilities.extractBytes(zis, entry);
				zis.close();
				return data;
			}
			entry = zis.getNextEntry();
		}

		return null;
	}

	private static InputStream getJarStreamFromDataZip(File dataZip,
			String fileName) {
		try {
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(dataZip));
			ZipEntry entry = zis.getNextEntry();
			while (entry != null) {
				if (entry.getName().endsWith(fileName)) {
					byte[] data = ZipUtilities.extractBytes(zis, entry);
					zis.close();
					return new ByteArrayInputStream(data);
				}
				entry = zis.getNextEntry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<String> getMatchingSymbol(String symbol,
			List<String> symbols) {
		int minScore = Integer.MAX_VALUE;
		List<String> best = new ArrayList<String>();
		boolean isSymbolEnum = isEnum(symbol);
		for (String s : symbols) {
			if (isEnum(s) == isSymbolEnum) {
				int score = computeLevenshteinDistance(symbol, s);
				if (score < minScore) {
					best.clear();
					best.add(s);
					minScore = score;
				} else if (score == minScore) {
					best.add(s);
				}
			}
		}
		return best;
	}

	public static List<String> getClassNamesFromStream(
			InputStream stream) {
		List<String> classNames = new ArrayList<String>();
		try {
			ZipInputStream zis = new ZipInputStream(
					stream);
			ZipEntry entry = zis.getNextEntry();
			while (entry != null) {
				if (entry.getName().endsWith(".java")
						&& !entry.getName().contains("$")) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classNames;
	}

	public static List<String> getClassNamesFromJar(File jar) {

		List<String> classNames = new ArrayList<String>();
		try {
			classNames = getClassNamesFromStream(new FileInputStream(
					jar));
		} catch (Exception e) {
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
			enumName = split[split.length - 1];
		} else {
			enumName = s;
		}
		if (enumName == null || enumName.length() == 0) {
			return false;
		}
		for (int i = 0; i < enumName.length(); i++) {
			char c = enumName.charAt(i);
			if (!Character.isUpperCase(c) && !Character.isDigit(c) && c != '_') {
				return false;
			}
		}
		return true;
	}

	public static List<String> loadResourceSymbols(byte[] inputData)
			throws MalformedURLException, IOException {

		File tempFile = File.createTempFile("aliceGalleryDiff", ".jar");
		writeDataToFile(inputData, tempFile);

		List<String> symbols = new ArrayList<String>();
		List<String> classNames = getClassNamesFromJar(tempFile);
		URL jarURL = tempFile.toURI().toURL();
		CustomClassLoader cl = new CustomClassLoader(
				new URL[] { jarURL });

		for (String className : classNames) {
			try {
				Class<?> cls = cl.loadClass(className);
				if (ModelResource.class
						.isAssignableFrom(cls) && cls.isEnum()) {
					ModelResource[] enums = (ModelResource[]) cls
							.getEnumConstants();
					for (ModelResource resource : enums) {
						String resourceSymbol = resource.toString();
						symbols.add(resourceSymbol);
					}
				}
				symbols.add(cls.getName());
			} catch (ClassNotFoundException cnfe) {
				Logger
						.severe("FAILED TO LOAD GALLERY CLASS: " + className);
			}
		}
		jarURL = null;
		cl.close();
		cl = null;
		System.gc();
		if (!tempFile.delete()) {
			System.out.println("Failed to delete. Deleting on exit.");
		}
		return symbols;
	}

	public static List<String> loadResourceSymbols(File jar)
			throws MalformedURLException {
		List<String> symbols = new ArrayList<String>();
		List<String> classNames = getClassNamesFromJar(jar);
		URL jarURL = jar.toURI().toURL();
		URLClassLoader cl = new URLClassLoader(
				new URL[] { jarURL });

		for (String className : classNames) {
			try {
				Class<?> cls = cl.loadClass(className);
				if (ModelResource.class
						.isAssignableFrom(cls) && cls.isEnum()) {
					ModelResource[] enums = (ModelResource[]) cls.getEnumConstants();
					for (ModelResource resource : enums) {
						String resourceSymbol = resource.toString();
						symbols.add(cls.getName() + "." + resourceSymbol);
					}
				}
				symbols.add(cls.getName());
			} catch (ClassNotFoundException cnfe) {
				Logger
						.severe("FAILED TO LOAD GALLERY CLASS: " + className);
			}
		}
		return symbols;
	}

	private static void writeDataToFile(byte[] data, File destFile) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(destFile);
			fos.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (Exception e) {
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

	public static void main(String[] args) throws Exception {

		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);

		File jarDir = new File(curDir);
		jarDir = jarDir.getParentFile().getParentFile();
		jarDir = new File(jarDir, "ide/lib/alice");
//		File aliceJarDir = new File("C:/Users/alice/.m2/repository/org/alice/alice-model-source/2014.07.22");
//		File nebulousJarDir = new File("C:/Users/alice/.m2/repository/org/alice/nonfree/nebulous-model-source/2014.07.22");

		File aliceJarDir = new File("C:/batchOutput/mavenFilesZipped/alice/gallery/alice-model-source");
		File nebulousJarDir = new File("C:/batchOutput/mavenFilesZipped/nonfree/nebulous-model-source");

		System.out.println(jarDir);

//		File[] jarFiles = edu.cmu.cs.dennisc.java.io.FileUtilities
//				.listDescendants(jarDir, "jar");

		File[] aliceJarFiles = FileUtilities
				.listDescendants(aliceJarDir, "jar");
		File[] nebulousJarFiles = FileUtilities
				.listDescendants(nebulousJarDir, "jar");

		File[] jarFiles = ArrayUtilities.concatArrays(File.class, aliceJarFiles, nebulousJarFiles);

		final String DATA_LOCATIONS = "C:\\batchOutput\\galleryVersionData\\";
		final String FILE_NAME = "\\galleryData.txt";


		 GalleryDiff.saveGalleryInfo(ProjectVersion.getCurrentVersion(),
		 DATA_LOCATIONS+ProjectVersion.getCurrentVersion().toString()+FILE_NAME,
		 jarFiles);

		final String[] DATA_VERSIONS = {
				// "3.1.0.0.0", //Not supported
				// "3.1.1.0.0", //Not supported
				// "3.1.2.0.0", //Not supported
				// "3.1.3.0.0", //Not supported
				// "3.1.4.0.0", //Not supported
				// "3.1.5.0.0", //Not supported
				// "3.1.6.0.0", //Not supported
				// "3.1.29.0.0",
				// "3.1.46.0.0",
				// "3.1.47.0.0",
				// "3.1.70.0.0",
				"3.1.92.0.0", "3.1.93.0.0" };

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DATA_VERSIONS.length - 1; i++) {
			String prev = DATA_VERSIONS[i];
			String cur = DATA_VERSIONS[i + 1];
			GalleryDiff differ = new GalleryDiff(new File(DATA_LOCATIONS + prev
					+ FILE_NAME), new File(DATA_LOCATIONS + cur + FILE_NAME));
			differ.saveMigrationInfo(DATA_LOCATIONS
					+ cur
					+ "/"+MIGRATION_FILE_NAME);
			String code = differ.getMigrationCode();
			sb.append(code + "\n");
		}
		String finalCode = sb.toString();
		ClipboardUtilities
				.setClipboardContents(finalCode);
		System.out.println(finalCode);
		return;

		//
		// GalleryDiff differ = null;
		//
		// if (args.length < 4 || args.length % 4 != 0) {
		// final String ZIP_LOCATIONS =
		// "C:\\unstableBuild\\Data_AliceVersions\\";
		// final String ZIP_NAME = "\\aliceData.zip";
		// StringBuilder sb = new StringBuilder();
		// for (int i=0; i<DATA_VERSIONS.length-1; i++)
		// {
		// String prev = DATA_VERSIONS[i];
		// String cur = DATA_VERSIONS[i+1];
		// try {
		// differ = new GalleryDiff(prev, cur, ZIP_LOCATIONS+prev+ZIP_NAME,
		// ZIP_LOCATIONS+cur+ZIP_NAME, new String[] {"aliceModelSource.jar",
		// "nebulousModelSource.jar"});
		// String code = differ.getMigrationCode();
		// sb.append(code+"\n");
		// }
		// catch (IOException e) {
		// System.err.println("Failed to make diff for "+prev+" -> "+cur+": "+e);
		// }
		// }
		// String finalCode = sb.toString();
		// edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents(finalCode);
		// System.out.println(finalCode);
		// return;
		// }
		// else {
		// File baseDir = new File(System.getProperty( "user.dir" ));
		// File baseAliceDir = baseDir;
		// if (baseAliceDir.exists() && baseAliceDir.getParentFile() != null) {
		// baseAliceDir = baseAliceDir.getParentFile();
		// if (baseAliceDir.exists() && baseAliceDir.getParent() != null) {
		// baseAliceDir = baseAliceDir.getParentFile();
		// if (baseAliceDir.exists()) {
		// baseAliceDir = new File(baseAliceDir, "ide/lib/alice");
		// if (!baseAliceDir.exists()) {
		// baseAliceDir = null;
		// }
		// }
		// else {
		// baseAliceDir = null;
		// }
		// }
		// else {
		// baseAliceDir = null;
		// }
		// }
		// else {
		// baseAliceDir = null;
		// }
		// File baseTestDir = new File(baseDir, "test");
		// if (!baseTestDir.exists()) {
		// baseTestDir = null;
		// }
		// int argsOffset = 2;
		// int fileCount = args.length - argsOffset;
		// File[] files = new File[fileCount];
		// for (int i=0; i<files.length; i++) {
		// files[i] = new File(args[argsOffset+i]);
		// if (!files[i].exists() && baseDir != null) {
		// files[i] = new File(baseDir, args[argsOffset+i]);
		// }
		// if (!files[i].exists() && baseTestDir != null) {
		// files[i] = new File(baseTestDir, args[argsOffset+i]);
		// }
		// if (!files[i].exists()) {
		// edu.cmu.cs.dennisc.java.util.logging.Logger.severe("Failed to find file '"+args[argsOffset+i]+"'");
		// return;
		// }
		// }
		// int toDiffCount = files.length /2;
		// File[] prevFiles = new File[toDiffCount];
		// System.arraycopy(files, 0, prevFiles, 0, toDiffCount);
		// File[] curFiles = new File[toDiffCount];
		// System.arraycopy(files, toDiffCount, curFiles, 0, toDiffCount);
		//
		// org.lgna.project.Version previousVersion = new
		// org.lgna.project.Version(args[0]);
		// org.lgna.project.Version currentVersion = new
		// org.lgna.project.Version(args[1]);
		// boolean areZips = false;
		// for (File f : files) {
		// if
		// (edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension(f).equalsIgnoreCase("zip"))
		// {
		// areZips = true;
		// break;
		// }
		// }
		//
		// if (areZips) {
		// differ = new GalleryDiff(previousVersion, currentVersion,
		// prevFiles[0], curFiles[0], new String[] {"aliceModelSource.jar",
		// "nebulousModelSource.jar"});
		// }
		// else {
		// differ = new GalleryDiff(previousVersion, currentVersion, prevFiles,
		// curFiles);
		// }
		// }
		// if (differ != null) {
		// String code = differ.getMigrationCode();
		// edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents(code);
		// System.out.println(code);
		// System.out.println();
		// }
	}

}
