<?xml version="1.0" encoding="UTF-8"?>
<ivy-module version="2.0" 
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:noNamespaceSchemaLocation="http://ant.apache.org/ivy/schemas/ivy.xsd">
    <info organisation="${packageName}" module="${projectName}"/>
    <dependencies>
		<dependency org="org.graniteds" name="granite-core" rev="${GRANITEDS_VERSION}" transitive="false"/>
		<dependency org="org.graniteds" name="granite-essentials-swc" rev="${GRANITEDS_VERSION}" transitive="false"/>
		<dependency org="org.graniteds" name="granite-swc" rev="${GRANITEDS_VERSION}" transitive="false"/>
        <dependency org="javax.annotation" name="jsr250-api" rev="1.0" transitive="false"/><% 
	if (javaFramework == "Spring3") {%>
        <dependency org="org.springframework" name="spring-aop" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-asm" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-beans" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-context" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-core" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-expression" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-jdbc" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-jms" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-orm" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-tx" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-web" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework" name="spring-webmvc" rev="3.0.6.RELEASE" transitive="false"/>
        <dependency org="org.springframework.security" name="spring-security-core" rev="3.0.7.RELEASE" transitive="false"/>
        <dependency org="org.springframework.security" name="spring-security-config" rev="3.0.7.RELEASE" transitive="false"/>
        <dependency org="org.springframework.security" name="spring-security-web" rev="3.0.7.RELEASE" transitive="false"/>
        <dependency org="org.springframework.security" name="spring-security-acl" rev="3.0.7.RELEASE" transitive="false"/>
        <dependency org="commons-collections" name="commons-collections" rev="3.1" transitive="false"/>
        <dependency org="commons-logging" name="commons-logging" rev="1.1.1" transitive="false"/>
        <dependency org="aopalliance" name="aopalliance" rev="1.0" transitive="false"/>
        <dependency org="aspectj" name="aspectjweaver" rev="1.5.3" transitive="false"/>
        <dependency org="asm" name="asm" rev="3.3.1" transitive="false"/>
		<dependency org="org.graniteds" name="granite-spring" rev="${GRANITEDS_VERSION}" transitive="false"/><%
		if (!(targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5"))) {%>
        <dependency org="javassist" name="javassist" rev="3.12.1.GA" transitive="false"/><%
		}
	} else if (javaFramework == "Seam2") {%>
        <dependency org="commons-collections" name="commons-collections" rev="3.1" transitive="false"/>
        <dependency org="javax.faces" name="jsf-api" rev="1.2" transitive="false"/>
		<dependency org="org.graniteds" name="granite-seam21" rev="${GRANITEDS_VERSION}" transitive="false"/><%
		if (!(targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5"))) {%>
	    <dependency org="javassist" name="javassist" rev="3.12.1.GA" transitive="false"/><%
		}
	} else if (javaFramework == "EJB31" || javaFramework == "CDI") {%>
		<dependency org="javax" name="javaee-api" rev="6.0" transitive="false"/><%
	}
    if (javaFramework == "CDI") {%>
    	<dependency org="org.graniteds" name="granite-cdi" rev="${GRANITEDS_VERSION}" transitive="false"/><%
    }%>
		<dependency org="hsqldb" name="hsqldb" rev="1.8.0.10" transitive="false"/>
		<dependency org="org.hibernate.javax.persistence" name="hibernate-jpa-2.0-api" rev="1.0.1.Final" transitive="false"/>
		<dependency org="javax.transaction" name="transaction-api" rev="1.1" transitive="false"/><%
	if (javaPersistence == "Hibernate" && !targetPlatform.startsWith("JB")) {%>
        <dependency org="org.hibernate" name="hibernate-core" rev="3.6.8.Final" transitive="false"/>
        <dependency org="org.hibernate" name="hibernate-entitymanager" rev="3.6.8.Final" transitive="false"/>
        <dependency org="org.hibernate" name="hibernate-commons-annotations" rev="3.2.0.Final" transitive="false"/>
        <dependency org="antlr" name="antlr" rev="2.7.7" transitive="false"/>
        <dependency org="dom4j" name="dom4j" rev="1.6.1" transitive="false"/>
        <dependency org="cglib" name="cglib" rev="2.2.2" transitive="false"/><%
	}
	if (javaPersistence == "Hibernate") {
		if (targetPlatform.startsWith("JB7")) {%>
        <dependency org="org.graniteds" name="granite-hibernate4" rev="${GRANITEDS_VERSION}" transitive="false"/><%
		} else {%>
        <dependency org="org.graniteds" name="granite-hibernate" rev="${GRANITEDS_VERSION}" transitive="false"/><%
		}
	}
	else if (javaPersistence == "EclipseLink") {%>
		<dependency org="org.graniteds" name="granite-eclipselink" rev="${GRANITEDS_VERSION}" transitive="false"/><%	
	}
	else if (javaPersistence == "OpenJPA") {%>
		<dependency org="org.apache.openjpa" name="openjpa-all" rev="2.1.1" transitive="false"/>
		<dependency org="org.graniteds" name="granite-openjpa" rev="${GRANITEDS_VERSION}" transitive="false"/><%	
	}
	else if (javaPersistence == "DataNucleus") {%>
		<dependency org="org.datanucleus" name="datanucleus-core" rev="3.0.3" transitive="false"/>
		<dependency org="org.datanucleus" name="datanucleus-api-jpa" rev="3.0.3" transitive="false"/>
		<dependency org="org.datanucleus" name="datanucleus-rdbms" rev="3.0.3" transitive="false"/>
		<dependency org="org.datanucleus" name="datanucleus-enhancer" rev="3.0.1" transitive="false"/>
		<dependency org="javax.jdo" name="jdo2-api" rev="2.3-eb" transitive="false"/>
		<dependency org="org.graniteds" name="granite-datanucleus" rev="${GRANITEDS_VERSION}" transitive="false"/><%	
	}
	if (targetPlatform.startsWith("TC") || targetPlatform.startsWith("JY")) {%>
        <dependency org="log4j" name="log4j" rev="1.2.15" transitive="false"/><%
	}
	if (javaFramework == "Seam2" || (targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5"))) {%>
        <dependency org="org.hibernate" name="hibernate-validator" rev="3.1.0.GA" transitive="false"/><%
	} else {%>
		<dependency org="javax.validation" name="validation-api" rev="1.0.0.GA" transitive="false"/>
        <dependency org="org.graniteds" name="granite-beanvalidation" rev="2.3.0.GA" transitive="false"/>
        <dependency org="org.hibernate" name="hibernate-validator" rev="4.2.0.Final" transitive="false"/><%
	}%>
    	<dependency org="org.slf4j" name="slf4j-api" rev="1.6.4" transitive="false"/>
    	<dependency org="org.slf4j" name="slf4j-log4j12" rev="1.6.4" transitive="false"/>
    </dependencies>
    <conflicts>
    	<manager name="all"/>
    </conflicts>
</ivy-module>