<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE granite-config PUBLIC "-//Granite Data Services//DTD granite-config internal//EN"
    "http://www.graniteds.org/public/dtd/2.0.0/granite-config.dtd">

<granite-config scan="true">
	
	<!-- Optional configuration to allow proper serialization of BigInteger/BigDecimal
	<externalizers>
	   	<externalizer type="org.granite.messaging.amf.io.util.externalizer.BigIntegerExternalizer">
	   		<include instance-of="java.math.BigInteger"/>
	   	</externalizer>
	   	<externalizer type="org.granite.messaging.amf.io.util.externalizer.BigDecimalExternalizer">
	   		<include instance-of="java.math.BigDecimal"/>
	   	</externalizer>
	   	<externalizer type="org.granite.messaging.amf.io.util.externalizer.LongExternalizer">
	   		<include instance-of="java.lang.Long"/>
	   	</externalizer>
	</externalizers>
	 -->
	
	<!-- Security service for JBoss (same as Tomcat)
	<security type="org.granite.messaging.service.security.TomcatSecurityService"/>
	 --><%

if (tideEnabled && "ejb".equals(tideFramework))	{ %>

	<!-- Enable EJB3 services annotated with @RemoteDestination for Tide -->
	<tide-components>
		<tide-component instance-of="org.granite.tide.ejb.EjbIdentity"/>
		<tide-component annotated-with="org.granite.messaging.service.annotations.RemoteDestination"/>
	</tide-components><%
} %>

</granite-config>
