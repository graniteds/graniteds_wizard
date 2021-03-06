<?xml version="1.0" encoding="UTF-8"?>
<graniteProperties version="2.0">
  <gas3 uid="uid" 
	as3TypeFactory="org.granite.generator.as3.DefaultAs3TypeFactory" <%
	if (javaFramework == "Seam2" || (targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5"))) {%>
	entityFactory="org.granite.generator.as3.HVEntityFactory" <%
	} else {%>
	entityFactory="org.granite.generator.as3.BVEntityFactory" <%
	}%>
	remoteDestinationFactory="org.granite.generator.as3.DefaultRemoteDestinationFactory" 
	debugEnabled="false" flexConfig="false" 
	externalizeLong="false" externalizeBigInteger="false" externalizeBigDecimal="false">
    <source path="java" includes="${Fs.dotsToSlashes(packageName)}/entities/*.java;${Fs.dotsToSlashes(packageName)}/services/*Service.java" excludes="" output="flex;"/>
    <template kind="INTERFACE" uris="class:org/granite/generator/template/interface.gsp"/>
    <template kind="BEAN" uris="class:org/granite/generator/template/bean.gsp;class:org/granite/generator/template/tideBeanBase.gsp"/>
    <template kind="ENTITY" uris="class:org/granite/generator/template/entity.gsp;class:org/granite/generator/template/tideEntityBase.gsp"/>
    <template kind="ENUM" uris="class:org/granite/generator/template/enum.gsp"/>
    <template kind="REMOTE_DESTINATION" uris="class:org/granite/generator/template/remote.gsp;class:org/granite/generator/template/tideRemoteBase.gsp"/>
    <transformer type="org.granite.generator.as3.JavaAs3GroovyTransformer"/>
  </gas3>
</graniteProperties>