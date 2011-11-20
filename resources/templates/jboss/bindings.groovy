import org.granite.wizard.bindings.Eclipse;
import org.granite.wizard.bindings.ControlType;

jbossDir = [
	label: "JBoss installation directory",
	controlType: ControlType.DIRECTORY,
	type: File.class,
	value: "/jboss-5.1.0.GA",
	validate: { value -> value.isDirectory() },
	errorMessage: "Must be a valid directory"
]
jbossDeployDir = [
	label: "JBoss deploy directory",
	controlType: ControlType.TEXT,
	type: String.class,
	value: "server/default/deploy",
	validate: { value -> new File(jbossDir.value, value).isDirectory() },
	errorMessage: "Must be a valid directory" 
]
flexBuilder = [
	label: "Configure FlashBuilder",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	disabled: !Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder"),
	value: Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder")
]
/*
antBuildFile = [
	label: "Create an Ant build file",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	value: false
]
flexSdkDir = [
	label: "Flex SDK directory",
	controlType: ControlType.DIRECTORY,
	type: File.class,
	value: "/flex_sdk_4",
	disabled: { !antBuildFile.value },
	validate: { value -> value.isDirectory() },
	errorMessage: "Must be a valid directory" 
]
*/
graniteBuilder = [
	label: "Configure GraniteBuilder",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	disabled: !Eclipse.hasExtension("org.granite.builder.granitebuilder"),
	value: Eclipse.hasExtension("org.granite.builder.granitebuilder")
]
graniteBuilderPatterns = [
	label: "GraniteBuilder Java patterns",
	controlType: ControlType.TEXT,
	type: String.class,
	disabled: { !graniteBuilder.value },
	value: "**/entities/*.java;**/services/*.java"
]
gravityEnabled = [
	label: "Configure Gravity",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	value: true
]
tideEnabled = [
	label: "Configure Tide",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	value: true
]
tideFramework = [
	label: "Tide framework",
	controlType: ControlType.COMBO,
	type: String.class,
	disabled: { !tideEnabled.value },
	possibleValues: ["Ejb": "ejb", "Seam": "seam", "Spring": "spring"],
	value: "ejb"
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
	controlType: ControlType.TEXT,
	type: String.class,
	value: "flex",
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
flexBinDir = [
	label: "Java binary directory",
	controlType: ControlType.TEXT,
	type: String.class,
	value: { "bin-" + flexSrcDir.value },
	validate: { value -> value != null && value.matches("^[a-zA-Z0-9_\\-]+\$") },
	errorMessage: "Must be a valid directory name"
]
