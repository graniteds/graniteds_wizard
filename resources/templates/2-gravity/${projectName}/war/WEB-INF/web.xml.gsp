<%
	boolean servlet3 = gravityServlet.indexOf(".servlet3.") != -1;

%><?xml version="1.0" encoding="UTF-8"?>

<%
if (servlet3) { %>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0"><%
}
else { %>
<web-app
	xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	version="2.5"><%
} %>

	<description>${projectName} Application Description</description>
	<display-name>${projectName} Application</display-name>

	<!--
	Load configuration files and setup this application.
	-->
	<listener>
		<listener-class>org.granite.config.GraniteConfigListener</listener-class>
	</listener>
    
	<!--
	Optional: expose GraniteDS configuration mbeans in the JMX console.
    <context-param>
        <param-name>registerGraniteMBeans</param-name>
        <param-value>true</param-value>
    </context-param>
	-->

	<!--
	Gravity asynchronous (Comet) servlet. Must be used with a valid APR (Apache
	Portable Runtime) installation and configuration.
	-->
    <servlet>
        <servlet-name>GravityServlet</servlet-name>
        <servlet-class>${gravityServlet}</servlet-class>
        <load-on-startup>1</load-on-startup><%
if (servlet3) { %>
        <async-supported>true</async-supported><%
} %>
    </servlet>
    <servlet-mapping>
        <servlet-name>GravityServlet</servlet-name>
        <url-pattern>/gravity/*</url-pattern>
    </servlet-mapping>

	<!--
	The default HTML welcome files, that will load the Flex application.
	-->
	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>${projectName}.html</welcome-file>
		<welcome-file>${projectName}.swf</welcome-file>
	</welcome-file-list>

</web-app>
