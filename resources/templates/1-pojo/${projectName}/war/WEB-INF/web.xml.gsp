<?xml version="1.0" encoding="UTF-8"?>

<web-app
	xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	version="2.5">

	<description>${projectName} Application Description</description>
	<display-name>${projectName} Application</display-name>

	<!--
	Load configuration files and setup this application.
	-->
	<listener>
		<listener-class>org.granite.config.GraniteConfigListener</listener-class>
	</listener>

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
	</servlet-mapping>

	<!--
	The default HTML welcome file, that will load the ${projectName}.swc Flex application.
	-->
	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>${projectName}.html</welcome-file>
		<welcome-file>${projectName}.swf</welcome-file>
	</welcome-file-list>
</web-app>
