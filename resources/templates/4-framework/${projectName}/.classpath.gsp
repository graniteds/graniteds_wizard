<% import org.granite.wizard.bindings.Eclipse %><?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="${javaSrcDir}"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
	<classpathentry kind="output" path="${javaBinDir}"/><% 
	if (javaFramework == "Spring3") {%>
	<classpathentry kind="lib" path="lib/spring-core.jar"/>
	<classpathentry kind="lib" path="lib/spring-context.jar"/>
	<classpathentry kind="lib" path="lib/spring-tx.jar"/>
	<classpathentry kind="lib" path="lib/hibernate-jpa-2.0-api.jar"/>
	<classpathentry kind="lib" path="lib/granite-core.jar"/><%
	} else if (javaFramework == "Seam2") {%>
	<classpathentry kind="lib" path="lib/jboss-seam.jar"/>
	<classpathentry kind="lib" path="lib/hibernate-jpa-2.0-api.jar"/>
	<classpathentry kind="lib" path="lib/granite-core.jar"/><%
	} else if (javaFramework == "EJB31" || javaFramework == "CDI") {%>
	<classpathentry kind="lib" path="lib/javaee-api.jar"/>
	<classpathentry kind="lib" path="lib/granite-core.jar"/><%
		if (javaFramework == "CDI") {%>
	<classpathentry kind="lib" path="lib/granite-cdi.jar"/><%
		}
	}
%>
</classpath>
