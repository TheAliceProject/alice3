MANIFEST_TEXT = """
Manifest-Version: 1.0
OpenIDE-Module: org.alice.netbeans
OpenIDE-Module-Install: org/alice/netbeans/installer/Installer.class
OpenIDE-Module-Layer: org/alice/netbeans/layer.xml
OpenIDE-Module-Localizing-Bundle: org/alice/netbeans/Bundle.properties
OpenIDE-Module-Specification-Version: ___ALICE_VERSION___
"""

ALICE_3_LIBRARY_XML_TEXT = """
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE library PUBLIC "-//NetBeans//DTD Library Declaration 1.0//EN" "http://www.netbeans.org/dtds/library-declaration-1_0.dtd">
<library version="1.0">
    <name>Alice3Library</name>
    <type>j2se</type>
    <localizing-bundle>org.alice.netbeans.Bundle</localizing-bundle>
    <volume>
		<type>classpath</type>

		<resource>jar:nbinst://org.alice.netbeans/modules/ext/gluegen-rt-___JOGL_VERSION___.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans/modules/ext/jogl-all-___JOGL_VERSION___.jar!/</resource>

		<resource>jar:nbinst://org.alice.netbeans/modules/ext/jmf-2.1.1e.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans/modules/ext/javamp3-1.0.jar!/</resource>

		<resource>jar:nbinst://org.alice.netbeans/modules/ext/util-0.0.1-SNAPSHOT.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans/modules/ext/scenegraph-0.0.1-SNAPSHOT.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans/modules/ext/glrender-0.0.1-SNAPSHOT.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans/modules/ext/story-api-0.0.1-SNAPSHOT.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans/modules/ext/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans/modules/ext/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar!/</resource>



		<resource>jar:nbinst://org.alice.netbeans/modules/ext/ast-0.0.1-SNAPSHOT.jar!/</resource>
<!--		<resource>jar:nbinst://org.alice.netbeans/modules/ext/story-api-migration-0.0.1-SNAPSHOT.jar!/</resource>-->



	</volume>
    <volume>
		<type>src</type>
		<resource>jar:nbinst://org.alice.netbeans/src/aliceSource.jar!/</resource>
    </volume>
    <volume>
		<type>javadoc</type>
		<resource>jar:nbinst://org.alice.netbeans/doc/aliceDocs.zip!/</resource>
    </volume>
</library>
"""

PROJECT_XML_TEXT = """
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://www.netbeans.org/ns/project/1">
    <type>org.netbeans.modules.apisupport.project</type>
    <configuration>
        <data xmlns="http://www.netbeans.org/ns/nb-module-project/3">
            <code-name-base>org.alice.netbeans</code-name-base>
            <suite-component/>
            <module-dependencies>
                <dependency>
                    <code-name-base>org.netbeans.api.progress</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>1</release-version>
                        <specification-version>1.36.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.libs.javacapi</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>8.8.1.3</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.modules.editor.completion</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>1</release-version>
                        <specification-version>1.38.1.2</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.modules.editor.indent</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>2</release-version>
                        <specification-version>1.35.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.modules.editor.mimelookup</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>1</release-version>
                        <specification-version>1.34.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.modules.java.source</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>0.131.1.26.2.25.8</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.modules.options.api</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>1</release-version>
                        <specification-version>1.38.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.modules.projectapi</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>1</release-version>
                        <specification-version>1.56.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.modules.projectuiapi</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>1</release-version>
                        <specification-version>1.75.1.8</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.netbeans.spi.palette</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <release-version>1</release-version>
                        <specification-version>1.40.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.awt</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>7.60.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.dialogs</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>7.35.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.filesystems</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>8.9.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.io</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>1.43.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.loaders</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>7.54.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.modules</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>7.41.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.nodes</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>7.37.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.text</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>6.59.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.util</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>8.35.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.util.lookup</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>8.23.1</specification-version>
                    </run-dependency>
                </dependency>
                <dependency>
                    <code-name-base>org.openide.windows</code-name-base>
                    <build-prerequisite/>
                    <compile-dependency/>
                    <run-dependency>
                        <specification-version>6.67.1</specification-version>
                    </run-dependency>
                </dependency>
            </module-dependencies>
            <public-packages/>
            <class-path-extension>
                <runtime-relative-path>ext/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar</binary-origin>
            </class-path-extension>
            <class-path-extension>
                <runtime-relative-path>ext/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar</binary-origin>
            </class-path-extension>
            <class-path-extension>
                <runtime-relative-path>ext/ast-0.0.1-SNAPSHOT.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/ast-0.0.1-SNAPSHOT.jar</binary-origin>
            </class-path-extension>
            <class-path-extension>
                <runtime-relative-path>ext/story-api-0.0.1-SNAPSHOT.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/story-api-0.0.1-SNAPSHOT.jar</binary-origin>
            </class-path-extension>
            <class-path-extension>
                <runtime-relative-path>ext/story-api-migration-0.0.1-SNAPSHOT.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/story-api-migration-0.0.1-SNAPSHOT.jar</binary-origin>
            </class-path-extension>
            <class-path-extension>
                <runtime-relative-path>ext/util-0.0.1-SNAPSHOT.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/util-0.0.1-SNAPSHOT.jar</binary-origin>
            </class-path-extension>
            <class-path-extension>
                <runtime-relative-path>ext/scenegraph-0.0.1-SNAPSHOT.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/scenegraph-0.0.1-SNAPSHOT.jar</binary-origin>
            </class-path-extension>
            <class-path-extension>
                <runtime-relative-path>ext/glrender-0.0.1-SNAPSHOT.jar</runtime-relative-path>
                <binary-origin>release/modules/ext/glrender-0.0.1-SNAPSHOT.jar</binary-origin>
            </class-path-extension>
        </data>
    </configuration>
</project>
"""
