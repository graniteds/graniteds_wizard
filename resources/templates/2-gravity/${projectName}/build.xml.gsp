<%
	if (!antBuild)
		throw new org.granite.wizard.CancelFileGenerationException();

%><?xml version="1.0" encoding="UTF-8"?>

<project name="${projectName}" default="build.war">
	
	<!--
	 ! Load configuration properties from the 'env.properties' file.
	 !-->
	<property file="env.properties"/>

	<property name="war.dir" value="\${build.dir}/\${project.name}"/>
	
	<!--
	 ! Build WAR.
	 !-->
	<target name="build.war"<% if (!flexBuilder) {%> depends="build.flex" <% } %>>

		<mkdir dir="\${war.dir}"/>
		<copy todir="\${war.dir}">
			<fileset dir="war" includes="**"/>
			<fileset dir="\${flex.bin.dir}" includes="**" excludes="**/*.cache"/>
		</copy>
		
		<zip destfile="\${war.dir}.war">
			<fileset dir="\${war.dir}" includes="**"/>
		</zip>
		
	</target>
	
	<!--
	 ! Deploy WAR.
	 !-->
	<target name="deploy.war" depends="build.war">

		<copy todir="\${deploy.dir}">
			<fileset dir="\${build.dir}" includes="\${project.name}.war"/>
		</copy>
		
	</target>
	
	<!--
	 ! Compile Flex sources and create a HTML wrapper.
	 !-->	
	<target name="build.flex">
		
		<property name="FLEX_HOME" value="\${flex.home}"/>
		<taskdef resource="flexTasks.tasks" classpath="\${flex.home}/ant/lib/flexTasks.jar" />
		
		<mkdir dir="\${flex.bin.dir}"/>
		<mxmlc
            file="\${flex.src.dir}/\${project.name}.mxml"
            output="\${flex.bin.dir}/\${project.name}.swf"
            services="war/WEB-INF/flex/services-config.xml"
            context-root="/\${project.name}"
            keep-generated-actionscript="false"
            debug="false"
            incremental="true"
            use-network="false">

            <source-path path-element="\${flex.home}/frameworks"/>
            <load-config filename="\${flex.home}/frameworks/flex-config.xml"/>

        	<!-- All granite-essentials.swc classes are included in the output swf -->
            <compiler.include-libraries dir="lib" append="true">
                <include name="granite-essentials.swc" />
            </compiler.include-libraries>

        	<!-- Only granite.swc actually used classes are included in the output swf -->
        	<compiler.library-path dir="lib" append="true">
        		<include name="granite.swc"/>
        	</compiler.library-path>
        </mxmlc>

		<html-wrapper
			title="\${project.name}"
		    output="\${flex.bin.dir}"
			file="\${project.name}.html"
		    application="\${project.name}"
		    swf="${projectName}"
		    version-major="10"
		    version-minor="0"
		    version-revision="0"
		    history="true"
		    height="100%"
			width="100%"
		    bgcolor="#ffffff"
		/>

	</target>

</project>