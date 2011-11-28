<%
	boolean servlet3 = targetPlatform.indexOf("(S3)") >= 0;
%><?xml version="1.0" encoding="UTF-8"?>
<%
if (servlet3) {%>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0"><%
}
else {%>
<web-app
	xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	version="2.5"><%
}%>

	<description>${projectName} Application Description</description>
	<display-name>${projectName} Application</display-name><%
	
	if (javaFramework == "Spring3") {%>
	<context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/app-*-config.xml</param-value>
    </context-param>
    
    <!-- Spring listeners -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <listener>
        <listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>
    </listener>
	
    <!-- Spring MVC dispatcher servlet that handles incoming AMF requests on the /graniteamf endpoint -->
	<servlet>
	    <servlet-name>dispatcher</servlet-name>
	    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	    <load-on-startup>1</load-on-startup>
	</servlet>	
	<servlet-mapping>
	    <servlet-name>dispatcher</servlet-name>
	    <url-pattern>/graniteamf/*</url-pattern>
	</servlet-mapping><%
	} else if (javaFramework == "Seam2") {%>
    
    <!-- Seam listeners -->
    <listener>
        <listener-class>org.jboss.seam.servlet.SeamListener</listener-class>
    </listener>
    
	<filter>
        <filter-name>SeamFilter</filter-name> 
        <filter-class>org.jboss.seam.servlet.SeamFilter</filter-class> 
    </filter>
    <filter-mapping>
        <filter-name>SeamFilter</filter-name> 
        <url-pattern>/graniteamf/*</url-pattern> 
    </filter-mapping>
    <filter-mapping>
        <filter-name>SeamFilter</filter-name> 
        <url-pattern>/web/*</url-pattern> 
    </filter-mapping><%
	}
	else if (!servlet3) {%>

	<!--
	GraniteDS' AMF3 filter. Must be used together with the servlet below. This filter is
	responsible to deserialize/serialize AMF3 requests/responses.
	-->
	<filter>
		<filter-name>AMFMessageFilter</filter-name>
		<filter-class>org.granite.messaging.webapp.AMFMessageFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>AMFMessageFilter</filter-name>
		<url-pattern>/graniteamf/*</url-pattern>
	</filter-mapping>

	<!--
	GraniteDS' AMF3 servlet. Must be used together with the filter above. This servlet is
	responsible to call the Pojo service according to the content of the AMF3 request.
	-->
	<servlet>
		<servlet-name>AMFMessageServlet</servlet-name>
		<servlet-class>org.granite.messaging.webapp.AMFMessageServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>AMFMessageServlet</servlet-name>
		<url-pattern>/graniteamf/*</url-pattern>
	</servlet-mapping><%
	}
	
	if (javaFramework == "Spring3" || javaFramework == "Seam2" || !servlet3) {%>
	
	<!--
	Load configuration files and setup this application.
	-->
	<listener>
		<listener-class>org.granite.config.GraniteConfigListener</listener-class>
	</listener>
	
	<!--
	Gravity asynchronous (Comet) servlet. Must be used with a valid installation and configuration
	(APR for Tomcat and JBoss).
	-->
    <servlet>
        <servlet-name>GravityServlet</servlet-name><%
		if (servlet3) {%>
        <servlet-class>org.granite.gravity.servlet3.GravityAsyncServlet</servlet-class><%
		} else if (targetPlatform.startsWith("TC") || targetPlatform.startsWith("JB4")) {%>
        <servlet-class>org.granite.gravity.tomcat.GravityTomcatServlet</servlet-class><%
		} else if (targetPlatform.startsWith("JB5")) {%>
        <servlet-class>org.granite.gravity.jbossweb.GravityJBossWebServlet</servlet-class><%
		} else if (targetPlatform.startsWith("JY")) {%>
        <servlet-class>org.granite.gravity.jetty.GravityJettyServlet</servlet-class><%
		}%>	
        <load-on-startup>1</load-on-startup><%
		if (servlet3) {%>
        <async-supported>true</async-supported><%
		}%>
    </servlet>
    <servlet-mapping>
        <servlet-name>GravityServlet</servlet-name>
        <url-pattern>/gravityamf/*</url-pattern>
    </servlet-mapping><%
	}%>

	<!--
	The default HTML welcome file, that will load the ${projectName}.swc Flex application.
	-->
	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>${projectName}.html</welcome-file>
		<welcome-file>${projectName}.swf</welcome-file>
	</welcome-file-list>
	
	<% if (javaFramework == "EJB31") {%> 	
 	<login-config>
  		<auth-method>BASIC</auth-method>
 	</login-config>
 	
 	<security-role>
  		<role-name>user</role-name>
 	</security-role>
 	<security-role>
  		<role-name>admin</role-name>
 	</security-role>
 	
 	<persistence-context-ref>
		<persistence-context-ref-name>${projectName}-pc</persistence-context-ref-name>
		<persistence-unit-name>${projectName}-pu</persistence-unit-name>	
 	</persistence-context-ref><%
 	} %>
</web-app>
