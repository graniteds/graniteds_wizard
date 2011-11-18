<?xml version="1.0" encoding="UTF-8"?>

<web-app
	xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	version="2.5">

	<description>${projectName} Web Application Description</description>
	<display-name>${projectName} Web Application</display-name>

	<listener>
		<listener-class>org.granite.config.GraniteConfigListener</listener-class>
	</listener>

	<filter>
		<filter-name>AMFMessageFilter</filter-name>
		<filter-class>org.granite.messaging.webapp.AMFMessageFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>AMFMessageFilter</filter-name>
		<url-pattern>/graniteamf/*</url-pattern>
	</filter-mapping>

	<servlet>
		<servlet-name>AMFMessageServlet</servlet-name>
		<servlet-class>org.granite.messaging.webapp.AMFMessageServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet><%

if (gravityEnabled) { %>
	<servlet>
		<servlet-name>GravityServlet</servlet-name>
		<servlet-class>org.granite.gravity.jbossweb.GravityJBossWebServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet><%
} %>

	<servlet-mapping>
		<servlet-name>AMFMessageServlet</servlet-name>
		<url-pattern>/graniteamf/*</url-pattern>
	</servlet-mapping><%

if (gravityEnabled) { %>
	<servlet-mapping>
		<servlet-name>GravityServlet</servlet-name>
		<url-pattern>/gravityamf/*</url-pattern>
	</servlet-mapping><%
} %>

	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>${projectName}.swf</welcome-file>
	</welcome-file-list>
</web-app>
