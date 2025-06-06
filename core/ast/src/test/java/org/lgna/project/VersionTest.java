package org.lgna.project;

import org.junit.Test;

import edu.cmu.cs.dennisc.java.io.TextFileUtilities;

import static org.junit.Assert.*;

public class VersionTest {

    @Test
    public void testIsValid() {
        assertTrue(new Version("3.14.0.4-alpha.2+build.local").isValid());
        assertFalse(new Version("3vhs14.0.4-alpha.2+build.local").isValid());
    }

    @Test
    public void testToString() {
        String versionString = "3.14.0.4-alpha.2+build.local";
        Version v = new Version(versionString);

        assertEquals(versionString, v.toString());
    }

    @Test
    public void testCurrentVersion() {
        String currewntVersionString = TextFileUtilities.read(Version.class.getResourceAsStream("Version.txt")).trim();
        Version v = new Version(currewntVersionString);

        assertTrue(v.isValid());
        assertEquals(v.toString(), currewntVersionString);
    }
}