<%
	if (!graniteBuilder)
		throw new org.granite.wizard.CancelFileGenerationException();

%><?xml version="1.0" encoding="UTF-8"?>
<graniteProperties version="2.0">
  <gas3 uid="uid" as3TypeFactory="org.granite.generator.as3.DefaultAs3TypeFactory" entityFactory="org.granite.generator.as3.BVEntityFactory" remoteDestinationFactory="org.granite.generator.as3.DefaultRemoteDestinationFactory" debugEnabled="false" flexConfig="false" externalizeLong="false" externalizeBigInteger="true" externalizeBigDecimal="true">
    <source path="${javaSrcDir}" includes="${graniteBuilderPatterns}" excludes="" output="${flexSrcDir};"/>
    <template kind="ENUM" uris="class:org/granite/generator/template/enum.gsp"/><%

if (tideEnabled) { %>
    <template kind="ENTITY" uris="class:org/granite/generator/template/entity.gsp;class:org/granite/generator/template/tideEntityBase.gsp"/>
    <template kind="REMOTE_DESTINATION" uris="class:org/granite/generator/template/remote.gsp;class:org/granite/generator/template/tideRemoteBase.gsp"/><%
}
else { %>
    <template kind="ENTITY" uris="class:org/granite/generator/template/entity.gsp;class:org/granite/generator/template/entityBase.gsp"/>
    <template kind="REMOTE_DESTINATION" uris="class:org/granite/generator/template/remote.gsp;class:org/granite/generator/template/remoteBase.gsp"/><%
}
%>
    <template kind="BEAN" uris="class:org/granite/generator/template/bean.gsp;class:org/granite/generator/template/beanBase.gsp"/>
    <template kind="INTERFACE" uris="class:org/granite/generator/template/interface.gsp"/>
    <transformer type="org.granite.generator.as3.JavaAs3GroovyTransformer"/>
  </gas3>
</graniteProperties>