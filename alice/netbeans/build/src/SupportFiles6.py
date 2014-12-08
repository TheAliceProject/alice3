MANIFEST_TEXT = """
Manifest-Version: 1.0
OpenIDE-Module: org.alice.netbeans.aliceprojectwizard
OpenIDE-Module-Layer: org/alice/netbeans/aliceprojectwizard/layer.xml
OpenIDE-Module-Install: org/alice/netbeans/aliceprojectwizard/AliceWizardInstaller.class
OpenIDE-Module-Localizing-Bundle: org/alice/netbeans/aliceprojectwizard/Bundle.properties
OpenIDE-Module-Specification-Version: ___ALICE_VERSION___
"""

ALICE_3_LIBRARY_XML_TEXT = """
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE library PUBLIC "-//NetBeans//DTD Library Declaration 1.0//EN" "http://www.netbeans.org/dtds/library-declaration-1_0.dtd">
<library version="1.0">
	<name>Alice3Library</name>
	<type>j2se</type>
	<localizing-bundle>org.alice.netbeans.aliceprojectwizard.Bundle</localizing-bundle>
	<volume>
		<type>classpath</type>

		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/gluegen-rt-___JOGL_VERSION___.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/jogl-all-___JOGL_VERSION___.jar!/</resource>

		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/jmf-2.1.1e.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/javamp3-1.0.jar!/</resource>

		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/util-0.0.1-SNAPSHOT.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/scenegraph-0.0.1-SNAPSHOT.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/story-api-0.0.1-SNAPSHOT.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar!/</resource>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar!/</resource>


		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/ast-0.0.1-SNAPSHOT.jar!/</resource>
        <!--<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/modules/ext/story-api-migration-0.0.1-SNAPSHOT.jar!/</resource>-->

	</volume>
	<volume>
		<type>src</type>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/src/aliceSource.jar!/</resource>
	</volume>
	<volume>
		<type>javadoc</type>
		<resource>jar:nbinst://org.alice.netbeans.aliceprojectwizard/doc/aliceDocs.zip!/</resource>
	</volume>
</library>
"""

PROJECT_XML_TEXT = """
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://www.netbeans.org/ns/project/1">
	<type>org.netbeans.modules.apisupport.project</type>
	<configuration>
		<data xmlns="http://www.netbeans.org/ns/nb-module-project/3">
			<code-name-base>org.alice.netbeans.aliceprojectwizard</code-name-base>
			<suite-component/>
			<module-dependencies>
				<dependency>
					<code-name-base>org.netbeans.libs.javacapi</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>0.6.0.1</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.netbeans.modules.java.source</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>0.34.0.3.6</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.netbeans.modules.projectapi</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<release-version>1</release-version>
						<specification-version>1.14</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.netbeans.modules.projectuiapi</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<release-version>1</release-version>
						<specification-version>1.29.0.6</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.netbeans.spi.palette</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<release-version>1</release-version>
						<specification-version>1.14.1</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.awt</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>7.1.0.1</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.dialogs</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>7.6</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.filesystems</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>7.8</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.io</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>1.16.1</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.loaders</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>6.9</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.modules</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>7.6</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.nodes</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>7.7.1.1</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.text</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>6.21.1</specification-version>
					</run-dependency>
				</dependency>
				<dependency>
					<code-name-base>org.openide.util</code-name-base>
					<build-prerequisite/>
					<compile-dependency/>
					<run-dependency>
						<specification-version>7.12.0.1</specification-version>
					</run-dependency>
				</dependency>
			</module-dependencies>
			<public-packages/>
			<class-path-extension>
				<runtime-relative-path>ext/scenegraph-0.0.1-SNAPSHOT.jar</runtime-relative-path>
				<binary-origin>release/modules/ext/scenegraph-0.0.1-SNAPSHOT.jar</binary-origin>
			</class-path-extension>
			<class-path-extension>
				<runtime-relative-path>ext/ast-0.0.1-SNAPSHOT.jar</runtime-relative-path>
				<binary-origin>release/modules/ext/ast-0.0.1-SNAPSHOT.jar</binary-origin>
			</class-path-extension>
			<class-path-extension>
				<runtime-relative-path>ext/story-api-migration-0.0.1-SNAPSHOT.jar</runtime-relative-path>
				<binary-origin>release/modules/ext/story-api-migration-0.0.1-SNAPSHOT.jar</binary-origin>
			</class-path-extension>
			<class-path-extension>
				<runtime-relative-path>ext/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar</runtime-relative-path>
				<binary-origin>release/modules/ext/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar</binary-origin>
			</class-path-extension>
			<class-path-extension>
				<runtime-relative-path>ext/story-api-0.0.1-SNAPSHOT.jar</runtime-relative-path>
				<binary-origin>release/modules/ext/story-api-0.0.1-SNAPSHOT.jar</binary-origin>
			</class-path-extension>
			<class-path-extension>
				<runtime-relative-path>ext/util-0.0.1-SNAPSHOT.jar</runtime-relative-path>
				<binary-origin>release/modules/ext/util-0.0.1-SNAPSHOT.jar</binary-origin>
			</class-path-extension>
			<class-path-extension>
				<runtime-relative-path>ext/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar</runtime-relative-path>
				<binary-origin>release/modules/ext/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar</binary-origin>
			</class-path-extension>
		</data>
	</configuration>
</project>
"""