<%
	import org.granite.wizard.CancelFileGenerationException;
	import org.granite.wizard.bindings.Eclipse;

	if (!Eclipse.hasBundle("org.jboss.ide.eclipse.archives.core"))
		throw new CancelFileGenerationException();

%><?xml version="1.0" encoding="UTF-8"?>

<packages version="1.3">
   <package name="${projectName}.ear" type="org.jboss.ide.eclipse.as.core.packages.earPackageType" todir="${jbossDeployDir.canonicalPath}" exploded="true" inWorkspace="false">
      <package name="${projectName}.war" type="org.jboss.ide.eclipse.as.core.packages.warPackage" exploded="true" inWorkspace="true">
         <fileset dir="ear/war" includes="**" inWorkspace="true" flatten="false">
            <properties></properties>
         </fileset>
         <fileset dir="${flexBinDir}" includes="**" inWorkspace="true" flatten="false">
            <properties></properties>
         </fileset>
         <properties></properties>
      </package>
      <package name="${projectName}.jar" type="jar" exploded="true" inWorkspace="true">
         <fileset dir="ear/jar" includes="**" inWorkspace="true" flatten="false">
            <properties></properties>
         </fileset>
         <fileset dir="${javaBinDir}" includes="**" inWorkspace="true" flatten="false">
            <properties></properties>
         </fileset>
         <properties></properties>
      </package>
      <folder name="META-INF">
         <fileset dir="ear/META-INF" includes="**" inWorkspace="true" flatten="false">
            <properties></properties>
         </fileset>
         <properties></properties>
      </folder>
      <folder name="lib">
         <fileset dir="ear/lib" includes="**" inWorkspace="true" flatten="false">
            <properties></properties>
         </fileset>
         <properties></properties>
      </folder>
      <properties>
         <property name="org.jboss.ide.eclipse.as.core.packages.ModuleIDPropertyKey" value="${ System.currentTimeMillis() }"></property>
      </properties>
   </package>
   <properties></properties>
</packages>