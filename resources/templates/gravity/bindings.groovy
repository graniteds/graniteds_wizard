import org.granite.wizard.bindings.Eclipse;
import org.granite.wizard.bindings.ControlType;

gravityServlet = [
	label: "Target platform",
	tooltip: "Type of application server asynchronous servlet technology",
	controlType: ControlType.COMBO,
	type: String.class,
	possibleValues: [
		"Servlet 3.0 (JBoss 7, Tomcat 7, Jetty 8, GlassFish 3)": "org.granite.gravity.servlet3.GravityAsyncServlet",
		"JBossWeb (JBoss 5)" : "org.granite.gravity.jbossweb.GravityJBossWebServlet",
		"Tomcat (Tomcat 6, JBoss 4, GlassFish 2)": "org.granite.gravity.tomcat.GravityTomcatServlet",
		"Jetty (Jetty 6 & 7)": "org.granite.gravity.jetty.GravityJettyServlet",
		"WebLogic (WebLogic 9, 10, 11)": "org.granite.gravity.weblogic.GravityWebLogicServlet"
	],
	value: "org.granite.gravity.servlet3.GravityAsyncServlet"
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
antBuild = [
	label: "Create an Ant build file",
	tooltip: "Should we create a build.xml file (Flex compilation, WAR archive, deployment configuration)?",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	value: true
]
deployDir = [
	label: "Deployement directory",
	tooltip: "Directory where the WAR file will be deployed",
	controlType: ControlType.DIRECTORY,
	type: File.class,
	disabled: { !antBuild.value },	
	value: "/path/to/as/deploy",
	validate: { value -> value.isDirectory() },
	errorMessage: "Must be a valid directory"
]
flexSdkDir = [
	label: "Flex SDK directory",
	tooltip: "Root directory of the Flex SDK used when compiling Flex sources without Flash Builder",
	controlType: ControlType.DIRECTORY,
	type: File.class,
	disabled: { !antBuild.value },	
	value: "/path/to/flex_sdk_4",
	validate: { value -> value.isDirectory() },
	errorMessage: "Must be a valid directory"
]
flexBuilder = [
	label: "Configure Flash Builder",
	tooltip: "Create Flash Builder configuration files and add Flex nature and builder",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	disabled: !Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder"),
	value: Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder")
]
