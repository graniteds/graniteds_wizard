<% if (javaFramework != "Spring3")
		throw new org.granite.generator.CancelFileGenerationException();
%><?xml version="1.0" encoding="UTF-8"?>

<beans
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:jee="http://www.springframework.org/schema/jee"    
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

    <tx:annotation-driven transaction-manager="transactionManager"/>
  
	<% if (targetPlatform.startsWith("JB7")) {%>
    <!--
    JNDI Data source
    -->
	<jee:jndi-lookup id="dataSource" jndi-name="java:jboss/datasources/ExampleDS"/><%
	} else if (!targetPlatform.startsWith("GF3")) {%>
    <!--
    Spring Data source
    -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName">
            <value>org.hsqldb.jdbcDriver</value>
        </property>
        <property name="url">
            <value>jdbc:hsqldb:mem:${projectName}-db</value>
        </property>
        <property name="username">
            <value>sa</value>
        </property>
        <property name="password">
            <value></value>
        </property>
    </bean><%
	}%>

    <!--
        Configuration for Hibernate/JPA
    --><%
	if (targetPlatform.startsWith("GF3")) {%>
	<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
	    <property name="persistenceUnitName" value="${projectName}-pu" />
	</bean>
	
	<tx:jta-transaction-manager id="transactionManager"/><%
	}
	else if (javaPersistence == "Hibernate") {%>
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="persistenceUnitName" value="${projectName}-pu" />
        <property name="dataSource" ref="dataSource" />
        <property name="jpaDialect">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaDialect" />
        </property>
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
                <property name="showSql" value="false" />
                <property name="generateDdl" value="true" /><%
	if (targetPlatform.startsWith("JB7")) {%>
                <property name="databasePlatform" value="org.hibernate.dialect.H2Dialect" /><%
	} else {%>
                <property name="databasePlatform" value="org.hibernate.dialect.HSQLDialect" /><%
	}%>
            </bean>
        </property>
    </bean><%
	} else if (javaPersistence == "OpenJPA" || javaPersistence == "DataNucleus") {%>
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
	    <property name="persistenceUnitName" value="${projectName}-pu" />
	    <property name="loadTimeWeaver">
	    	<bean class="org.springframework.instrument.classloading.SimpleLoadTimeWeaver"/>
	    </property>
	</bean><%		
	}
	
	if (!targetPlatform.startsWith("GF3")) {%>
    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory" />
        <property name="dataSource" ref="dataSource" />
    </bean><%
	}%>
    
</beans>
