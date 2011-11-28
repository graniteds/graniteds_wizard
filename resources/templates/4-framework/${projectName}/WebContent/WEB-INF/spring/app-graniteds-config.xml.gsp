<% if (javaFramework != "Spring3")
		throw new org.granite.generator.CancelFileGenerationException();
%><?xml version="1.0" encoding="UTF-8"?>

<beans
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:graniteds="http://www.graniteds.org/config"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.graniteds.org/config http://www.graniteds.org/public/dtd/2.3.0/granite-config-2.3.xsd">

    <!-- Configuration of GraniteDS -->
    <graniteds:flex-filter url-pattern="/*" tide="true"/>
    
    <!-- Simple messaging destination for data push -->
    <graniteds:messaging-destination id="welcomeTopic" no-local="true" session-selector="true"/>
  	
    <!-- Configuration for Tide/Spring authorization -->
  	<graniteds:tide-identity/>
    
    <!-- Uncomment when there are more than one authentication-manager :
    <graniteds:security-service authentication-manager="authenticationManager"/>
    -->
    
</beans>
