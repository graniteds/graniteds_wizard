import org.granite.wizard.bindings.Eclipse;
import org.granite.wizard.bindings.ControlType;

javaFramework = [
    label: "Framework",
	tooltip: "Type of framework technology",
	controlType: ControlType.COMBO,
	type: String.class,
	possibleValues: [
		"Spring 3": "Spring3",
		"Seam 2": "Seam2",
		"EJB 3.1": "EJB31",
		"CDI": "CDI"
	],
	value: "Spring3"
]
targetPlatform = [
	label: "Target platform",
	tooltip: "Type of application server technology",
	controlType: ControlType.COMBO,
	type: String.class,
	possibleValues: {
		def v
		if (javaFramework.value == "Spring3") {
			v = [
				"Tomcat 6.x": "TC6",
				"Tomcat 7.x": "TC7(S3)",
				"Jetty 6.x": "JY6",
				"Jetty 8.x": "JY8(S3)",
				"JBoss 4.2.x": "JB4(E3)",
				"JBoss 5.1.x": "JB5(E3)",
				"JBoss 6.x": "JB6(S3)(E31)(CDI)(EE6)",				
				"JBoss AS 7.x": "JB7(S3)(E31)(CDI)(EE6)",
				"GlassFish v3": "GF3(S3)(E31)(CDI)(EE6)"
			]
		}
		else if (javaFramework.value == "Seam2") {
			v = [
				"Tomcat 6.x": "TC6",
				"Tomcat 7.x": "TC7(S3)",
				"Jetty 6.x": "JY6",
				"Jetty 8.x": "JY8(S3)",
				"JBoss 4.2.x": "JB4(E3)",
				"JBoss 5.1.x": "JB5(E3)",
				"JBoss 6.x": "JB6(S3)(E31)(CDI)(EE6)"
			]
		}
		else {
			v = [ 
				"JBoss 6.x": "JB6(S3)(E31)(CDI)(EE6)",				
				"JBoss AS 7.x": "JB7(S3)(E31)(CDI)(EE6)",
				"GlassFish v3": "GF3(S3)(E31)(CDI)(EE6)"
			]
		}
		return v
	},
	value: "TC6"
]
javaPersistence = [
	label: "JPA provider",
	tooltip: "Type of persistence technology",
	controlType: ControlType.COMBO,
	type: String.class,
	possibleValues: {
		def v
		if (targetPlatform.value.startsWith("GF3"))
			v = [ "EclipseLink": "EclipseLink" ]
		else {
			v = [ "Hibernate": "Hibernate" ]
			if ((targetPlatform.value.startsWith("TC") || targetPlatform.value.startsWith("JY")) && javaFramework.value == "Spring3") {
				v["OpenJPA"] = "OpenJPA"
				v["DataNucleus"] = "DataNucleus"
			}
		}
		return v
	},
	value: "Hibernate"
]
packageName = [
	label: "Package name",
	controlType: ControlType.TEXT,
	type: String.class,
	value: "org.example",
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\$") },
	errorMessage: "Must be a valid package name"
]
javaSrcDir = [
	label: "Java source directory",
	controlType: ControlType.TEXT,
	type: String.class,
	value: "java",
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
javaBinDir = [
	label: "Java binary directory",
	controlType: ControlType.TEXT,
	type: String.class,
	value: { "bin-" + javaSrcDir.value },
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
flexSrcDir = [
	label: "Flex source directory",
	tooltip: "Name of the project directory for Flex sources",
	controlType: ControlType.TEXT,
	type: String.class,
	value: "flex",
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
flexBinDir = [
	label: "Flex binary directory",
	tooltip: "Name of the project directory for Flex binaries (compiled swf)",
	controlType: ControlType.TEXT,
	type: String.class,
	value: { "bin-" + flexSrcDir.value },
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
flexBuilder = [
	label: "Configure Flash Builder",
	tooltip: "Create Flash Builder configuration files and add Flex nature and builder",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	disabled: !Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder"),
	value: Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder")
]
antBuild = [
	label: "Create an Ant+Ivy build file",
	tooltip: "Should we create a build.xml/ivy.xml file (Flex compilation, WAR archive, deployment and dependencies configuration)?",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	value: true
]
flexSdkDir = [
	label: "|- Flex SDK directory",
	tooltip: "Root directory of the Flex SDK used when compiling Flex sources without Flash Builder",
	controlType: ControlType.DIRECTORY,
	type: File.class,
	disabled: { !antBuild.value },	
	value: "/path/to/flex_sdk_4",
	validate: { value -> value.isDirectory() },
	errorMessage: "Must be a valid directory"
]
buildDir = [
	label: "|- Build directory",
	tooltip: "Directory where the WAR file will be created (relative to the project directory)",
	controlType: ControlType.TEXT,
	type: String.class,
	disabled: { !antBuild.value },	
	value: "build",
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
deployDir = [
	label: "|- Deployment directory",
	tooltip: "Directory where the WAR file will be deployed",
	controlType: ControlType.DIRECTORY,
	type: File.class,
	disabled: { !antBuild.value },	
	value: "/path/to/as/deploy",
	validate: { value -> value.isDirectory() },
	errorMessage: "Must be a valid directory"
]
