<% if (javaFramework != "Spring3")
		throw new org.granite.generator.CancelFileGenerationException();
%><?xml version="1.0" encoding="UTF-8"?>

<beans
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">


    <!-- Annotation scan -->
    <context:component-scan base-package="${packageName}.services"/>
  
    <!-- Spring MVC configuration -->
    <bean class="org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping"/>
    <bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter"/>

<%
	if (!(targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5"))) {%>    
    <!-- Configuration of Spring 3 Bean Validation -->
    <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/><%
	}%>
    
</beans>
