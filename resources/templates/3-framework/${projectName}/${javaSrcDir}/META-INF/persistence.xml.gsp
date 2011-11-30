<?xml version="1.0" encoding="UTF-8"?>

<persistence
	xmlns="http://java.sun.com/xml/ns/persistence"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	<% if (targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5")) { %>
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
	version="1.0">
	<% } else { %>
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
	version="2.0">
	<% }%>
	<% if (javaPersistence == "DataNucleus") { %>
    <persistence-unit name="${projectName}-pu" transaction-type="RESOURCE_LOCAL">
	    <provider>org.datanucleus.api.jpa.PersistenceProviderImpl</provider>
		<class>${packageName}.entities.AbstractEntity</class>
		<class>${packageName}.entities.Welcome</class>
		<exclude-unlisted-classes>false</exclude-unlisted-classes>
	    <properties>
	    	<property name="datanucleus.ConnectionDriverName" value="org.hsqldb.jdbcDriver"/>
	     	<property name="datanucleus.ConnectionURL" value="jdbc:hsqldb:mem:${projectName}-db"/>
	     	<property name="datanucleus.ConnectionUserName" value="sa"/>
	     	<property name="datanucleus.ConnectionPassword" value=""/>
	     	<property name="datanucleus.autoCreateTables" value="true"/>
	     	<property name="datanucleus.autoCreateColumns" value="true"/>
	     	<property name="datanucleus.storeManagerType" value="rdbms"/>
	     	<property name="datanucleus.DetachAllOnCommit" value="true"/>
	  	</properties>
    </persistence-unit><%
    } else if (javaPersistence == "OpenJPA") { %>
    <persistence-unit name="${projectName}-pu" transaction-type="RESOURCE_LOCAL">
        <provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>
		<class>${packageName}.entities.AbstractEntity</class>
		<class>${packageName}.entities.Welcome</class>
		<exclude-unlisted-classes>false</exclude-unlisted-classes>
        <properties>
        	<property name="openjpa.ConnectionDriverName" value="org.hsqldb.jdbcDriver"/>
        	<property name="openjpa.ConnectionURL" value="jdbc:hsqldb:mem:${projectName}-db"/>
        	<property name="openjpa.ConnectionUserName" value="sa"/>
        	<property name="openjpa.ConnectionPassword" value=""/>
            <property name="openjpa.jdbc.DBDictionary" value="org.apache.openjpa.jdbc.sql.HSQLDictionary"/> 
            <property name="openjpa.jdbc.SynchronizeMappings" value="buildSchema(ForeignKeys=true)"/>
        </properties>
    </persistence-unit><%
	} else if (targetPlatform.startsWith("JB5")) { %>
	<persistence-unit name="${projectName}-pu" transaction-type="RESOURCE_LOCAL">
    	<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<non-jta-data-source>java:/DefaultDS</non-jta-data-source>
		<class>${packageName}.entities.AbstractEntity</class>
		<class>${packageName}.entities.Welcome</class>
		<exclude-unlisted-classes>false</exclude-unlisted-classes>
	   	<properties>
      		<property name="hibernate.transaction.manager_lookup_class" value="org.hibernate.transaction.JBossTransactionManagerLookup"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
	     	<property name="jboss.entity.manager.factory.jndi.name" value="java:/${projectName}EMF"/>
        </properties>
 	</persistence-unit><%
	} else if (targetPlatform.startsWith("JB6")) {
		if (javaFramework == "Spring3") {%>
	<persistence-unit name="${projectName}-pu" transaction-type="RESOURCE_LOCAL">
		<non-jta-data-source>java:/DefaultDS</non-jta-data-source>
		<class>${packageName}.entities.AbstractEntity</class>
		<class>${packageName}.entities.Welcome</class>
		<exclude-unlisted-classes>false</exclude-unlisted-classes>
	</persistence-unit><%
		} else {%>
	<persistence-unit name="${projectName}-pu">
		<jta-data-source>java:/DefaultDS</jta-data-source>
        <properties>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
	     	<property name="jboss.entity.manager.factory.jndi.name" value="java:/${projectName}EMF"/>
        </properties>
	</persistence-unit><% 
		}
	} else if (targetPlatform.startsWith("JB7")) {
		if (javaFramework == "Spring3") {%>	
	<persistence-unit name="${projectName}-pu" transaction-type="RESOURCE_LOCAL">
		<non-jta-data-source>java:jboss/datasources/ExampleDS</non-jta-data-source>
		<class>${packageName}.entities.AbstractEntity</class>
		<class>${packageName}.entities.Welcome</class>
		<exclude-unlisted-classes>false</exclude-unlisted-classes>
	</persistence-unit><%
		} else {%>
	<persistence-unit name="${projectName}-pu">
		<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
        <properties>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
        </properties>
	</persistence-unit><%
		}
	} else if (targetPlatform.startsWith("GF3")) {
		if (javaFramework == "Spring3") { %>
    <persistence-unit name="${projectName}-pu" transaction-type="JTA">
    	<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
        <jta-data-source>jdbc/__default</jta-data-source>
		<class>${packageName}.entities.AbstractEntity</class>
		<class>${packageName}.entities.Welcome</class>
	    <exclude-unlisted-classes>false</exclude-unlisted-classes>
	    <properties>
	    	<property name="javax.persistence.jdbc.url" value="jdbc:derby://localhost:1527/sun-appserv-samples?create=true"/>
	        <property name="javax.persistence.jdbc.password" value="APP"/>
	        <property name="javax.persistence.jdbc.user" value="APP"/>
	        <property name="eclipselink.target-database" value="JavaDB"/>
	        <property name="eclipselink.ddl-generation" value="drop-and-create-tables"/>
	        <property name="eclipselink.ddl-generation.output-mode" value="database"/>
	        <property name="eclipselink.create-ddl-jdbc-file-name" value="create.sql"/>
      	</properties>
    </persistence-unit><%
    	} else {%>
 	<persistence-unit name="${projectName}-pu">
        <jta-data-source>jdbc/__default</jta-data-source>
	    <properties>
			<property name="eclipselink.ddl-generation" value="create-tables"/>
			<property name="eclipselink.ddl-generation.output-mode" value="database"/>
      	</properties>
 	</persistence-unit><%
 		}
 	} else if (javaFramework == "Seam2") {%>
    <persistence-unit name="${projectName}-pu" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.ejb.HibernatePersistence</provider>
        <properties>
            <property name="hibernate.dialect" value="org.hibernate.dialect.HSQLDialect"/>
            <property name="hibernate.connection.url" value="jdbc:hsqldb:mem:${projectName}-db"/>
            <property name="hibernate.connection.driver_class" value="org.hsqldb.jdbcDriver"/>
            <property name="hibernate.connection.username" value="sa"/>
            <property name="hibernate.connection.password" value=""/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
        </properties>
    </persistence-unit><%
	} else {%>
	<persistence-unit name="${projectName}-pu">
	</persistence-unit><%
	}%>
</persistence>
