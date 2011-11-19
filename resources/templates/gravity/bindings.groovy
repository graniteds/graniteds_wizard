import org.granite.wizard.bindings.Eclipse;
import org.granite.wizard.bindings.ControlType;

gravityServlet = [
	label: "Target Platform",
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
antBuild = [
	label: "Create an Ant build file",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	value: true
]
flexSdkDir = [
	label: "Flex SDK directory",
	controlType: ControlType.DIRECTORY,
	type: File.class,
	disabled: { !antBuild.value },	
	value: new File("/FlexSDK4"),
	validate: { value -> value instanceof File && value.isDirectory() },
	errorMessage: "Must be a valid directory"
]
flexBuilder = [
	label: "Configure FlashBuilder",
	controlType: ControlType.CHECKBOX,
	type: Boolean.class,
	disabled: !Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder"),
	value: Eclipse.hasExtension("com.adobe.flexbuilder.project.flexbuilder")
]
