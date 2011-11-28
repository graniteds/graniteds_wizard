<% if (javaFramework != "Seam2")
	throw new org.granite.generator.CancelFileGenerationException()
%><?xml version="1.0" encoding="UTF-8"?>

<components xmlns="http://jboss.com/products/seam/components"
            xmlns:core="http://jboss.com/products/seam/core"
            xmlns:security="http://jboss.com/products/seam/security"
            xmlns:transaction="http://jboss.com/products/seam/transaction"
            xmlns:persistence="http://jboss.com/products/seam/persistence"
            xmlns:framework="http://jboss.com/products/seam/framework"
            xmlns:bpm="http://jboss.com/products/seam/bpm"
            xmlns:jms="http://jboss.com/products/seam/jms"
            xmlns:web="http://jboss.com/products/seam/web"
            xmlns:graniteds="http://www.graniteds.org/config"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation=
                "http://jboss.com/products/seam/core http://jboss.com/products/seam/core-2.2.xsd
                 http://jboss.com/products/seam/transaction http://jboss.com/products/seam/transaction-2.2.xsd
                 http://jboss.com/products/seam/persistence http://jboss.com/products/seam/persistence-2.2.xsd
                 http://jboss.com/products/seam/web http://jboss.com/products/seam/web-2.2.xsd
                 http://jboss.com/products/seam/jms http://jboss.com/products/seam/jms-2.2.xsd
                 http://jboss.com/products/seam/security http://jboss.com/products/seam/security-2.2.xsd
                 http://jboss.com/products/seam/bpm http://jboss.com/products/seam/bpm-2.2.xsd
                 http://jboss.com/products/seam/components http://jboss.com/products/seam/components-2.2.xsd
                 http://jboss.com/products/seam/framework http://jboss.com/products/seam/framework-2.2.xsd
                 http://www.graniteds.org/config http://www.graniteds.org/public/dtd/2.3.0/granite-config-2.3.xsd">


    <core:init debug="true"/>

    <core:manager concurrent-request-timeout="500" conversation-timeout="120000"/>

    <% if (targetPlatform.startsWith("JB6")) {%>
    <persistence:managed-persistence-context name="entityManager" auto-create="true">
		<persistence:persistence-unit-jndi-name>java:/${projectName}EMF</persistence:persistence-unit-jndi-name>
	</persistence:managed-persistence-context><%
    } else {%>
    <persistence:entity-manager-factory name="${projectName}emf" persistence-unit-name="${projectName}-pu"/>

    <persistence:managed-persistence-context name="entityManager" entity-manager-factory="#{${projectName}emf}"/>

    <transaction:entity-transaction entity-manager="#{entityManager}"/><%
    }%>

    <security:identity authenticate-method="#{authenticator.authenticate}"/>

    
    <graniteds:flex-filter url-pattern="/graniteamf/*" tide="true"/>
    
    <graniteds:messaging-destination id="welcomeTopic" no-local="true" session-selector="true"/>
	
</components>
