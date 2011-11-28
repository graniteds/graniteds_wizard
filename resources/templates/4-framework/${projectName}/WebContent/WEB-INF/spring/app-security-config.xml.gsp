<% if (javaFramework != "Spring3")
		throw new org.granite.generator.CancelFileGenerationException();
%><?xml version="1.0" encoding="UTF-8"?>

<beans
	xmlns="http://www.springframework.org/schema/beans"
	xmlns:security="http://www.springframework.org/schema/security"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:context="http://www.springframework.org/schema/context"
  	xmlns:graniteds="http://www.graniteds.org/config"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       	   http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
    	   http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
           http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-3.0.xsd
       	   http://www.graniteds.org/config http://www.graniteds.org/public/dtd/2.1.0/granite-config-2.1.xsd"
           default-autowire="byName"
    	   default-lazy-init="true">
          
    <security:authentication-manager alias="authenticationManager">
     	<security:authentication-provider>
         	<security:user-service>
            	<security:user name="admin" password="admin" authorities="ROLE_USER,ROLE_ADMIN" />
                <security:user name="user" password="user" authorities="ROLE_USER" />
        	</security:user-service>
        </security:authentication-provider>
    </security:authentication-manager>
     
    <security:global-method-security secured-annotations="enabled" jsr250-annotations="enabled"/>
  	
</beans>
