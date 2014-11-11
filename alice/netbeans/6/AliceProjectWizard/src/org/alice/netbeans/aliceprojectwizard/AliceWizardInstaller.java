/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alice.netbeans.aliceprojectwizard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.LogManager;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author Administrator
 */
public class AliceWizardInstaller extends ModuleInstall {

//    private final static String RELATIVE_LIB_PATH = "libs/src";
	//todo: find out if we are doing anything with these?
//    private final static String MAC_UNIVERSAL_DIR = "alicecore.jar-natives-macosx-universal";
//    private final static String MAC_10_4_DIR = "alicecore.jar-natives-macosx-10.4";
//    private final static String WINDOWS_64BIT_DIR = "alicecore.jar-natives-windows-amd64";
//    private final static String WINDOWS_32BIT_DIR = "alicecore.jar-natives-windows-i586";
//    private final static String LINUX_64BIT_DIR = "alicecore.jar-natives-linux-amd64";
//    private final static String LINUX_32BIT_DIR = "alicecore.jar-natives-linux-i586";
//    private final static String[] LIB_DIRS = {
//        MAC_UNIVERSAL_DIR,
//        MAC_10_4_DIR,
//        WINDOWS_64BIT_DIR,
//        WINDOWS_32BIT_DIR,
//        LINUX_64BIT_DIR,
//        LINUX_32BIT_DIR
//    };
//    private final static String NATIVE_DIR = "aliceSource.jar_root";
	@Override
	public void restored() {
		//This has been shifted to install all the native libs in their respective folders (windows-i586, etc.)
		//Alice now looks to the root native dir and determines which native libs to load at runtime and depends upon these directories existing
		//The code below is used to figure out which of the native libs to keep, rename that directory to the root native dir, and delete the rest
		//New note: on the mac the path can't have path separators, so the code below should onlt be used for mac installs
//        boolean isMac = System.getProperty( "os.name" ).toLowerCase().contains( "mac" );
//        if (isMac) {
		//Legacy variables to preserve old code behavior
		//TODO: remove this when path stuff settles down
//            boolean isMac104 = System.getProperty( "os.version" ).toLowerCase().contains( "10.4" );
//            boolean isWindows = System.getProperty( "os.name" ).toLowerCase().contains( "windows" );
//            boolean isLinux = System.getProperty( "os.name" ).toLowerCase().contains("linux");
//            boolean is64Bit = System.getProperty( "sun.arch.data.model" ).toLowerCase().contains("64");
//            InstalledFileLocator locator = InstalledFileLocator.getDefault();
//            String nativeDirString = null;
//            if (isMac)
//            {
//                if (isMac104)
//                {
//                    nativeDirString = MAC_10_4_DIR;
//                }
//                else
//                {
//                    nativeDirString = MAC_UNIVERSAL_DIR;
//                }
//            }
//            else if (isWindows && is64Bit)
//            {
//                nativeDirString = WINDOWS_64BIT_DIR;
//            }
//            else if (isWindows && !is64Bit)
//            {
//                nativeDirString = WINDOWS_32BIT_DIR;
//            }
//            else if (isLinux && is64Bit)
//            {
//                nativeDirString = LINUX_64BIT_DIR;
//            }
//            else if (isLinux && !is64Bit)
//            {
//                nativeDirString = LINUX_32BIT_DIR;
//            }
//            if (nativeDirString != null)
//            {
//                File nativeDir = locator.locate(RELATIVE_LIB_PATH + nativeDirString, "org.alice.netbeans.aliceprojectwizard", false);
//                if (nativeDir != null && nativeDir.exists())
//                {
//                    File parentFile = nativeDir.getParentFile();
//                    String genericDirName = parentFile.getAbsolutePath() + "/" + NATIVE_DIR;
//                    File genericNativeDir = new File(genericDirName);
//                    if (genericNativeDir.exists())
//                    {
//                        deleteDir(genericNativeDir);
//                    }
//                    nativeDir.renameTo(genericNativeDir);
//                    for (File file : parentFile.listFiles())
//                    {
//                        if (file.isDirectory())
//                        {
//                            for (String libDir : LIB_DIRS)
//                            {
//                                String dirName = file.getName();
//                                if (dirName.equals(libDir))
//                                {
//                                    deleteDir(file);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
	}

	@Override
	public void uninstalled() {
//        InstalledFileLocator locator = InstalledFileLocator.getDefault();
//        File nativeDir = locator.locate(RELATIVE_LIB_PATH + NATIVE_DIR, "org.alice.netbeans.aliceprojectwizard", false);
//        if (nativeDir != null && nativeDir.exists())
//        {
//            deleteDir(nativeDir);
//        }
	}
//    private boolean deleteDir(File dir)
//    {
//        if (dir.exists())
//        {
//            for (File file: dir.listFiles())
//            {
//                if (file.isDirectory())
//                {
//                    deleteDir(file);
//                }
//                else
//                {
//                    file.delete();
//                }
//            }
//            return dir.delete();
//        }
//        return false;
//    }
}
