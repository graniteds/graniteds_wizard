<%
	if (!targetPlatform.startsWith("GF"))
		throw new org.granite.generator.CancelFileGenerationException();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sun-web-app 
	PUBLIC '-//Sun Microsystems, Inc.//DTD Application Server 9.0 Servlet 2.5//EN' 
		'http://www.sun.com/software/appserver/dtds/sun-web-app_2_5-0.dtd'>
	
<sun-web-app>
    <security-role-mapping>
        <role-name>user</role-name>
        <group-name>user</group-name>
    </security-role-mapping>
    <security-role-mapping>
        <role-name>admin</role-name>
        <group-name>admin</group-name>
    </security-role-mapping>
</sun-web-app>
