<% if (javaFramework != "CDI")
	throw new org.granite.generator.CancelFileGenerationException()
%><?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/beans_1_0.xsd">
	
  	<interceptors>
  		<class>${packageName}.TransactionInterceptor</class>
  	</interceptors>
</beans>