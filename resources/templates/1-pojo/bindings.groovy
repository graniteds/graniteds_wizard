import org.granite.wizard.bindings.Eclipse;
import org.granite.wizard.bindings.ControlType;

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
/*
flexBinDir = [
	label: "Flex binary directory",
	tooltip: "Name of the project directory for Flex binaries (compiled swf)",
	controlType: ControlType.TEXT,
	type: String.class,
	value: { "bin-" + flexSrcDir.value },
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
*/
flexBuilder = [
	label: "Configure Flash Builder",
	tooltip: "Create Flash Builder configuration files and add Flex nature and builder",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	disabled: !Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder"),
	value: Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder")
]
antBuild = [
	label: "Create an Ant build file",
	tooltip: "Should we create a build.xml file (Flex compilation, WAR archive, deployment configuration)?",
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
