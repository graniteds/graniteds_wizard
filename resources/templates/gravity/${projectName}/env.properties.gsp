<%
	if (!antBuild)
		throw new org.granite.wizard.CancelFileGenerationException();

%>project.name = ${projectName}
flex.home = ${flexSdkDir}
flex.src.dir = ${flexSrcDir}
flex.bin.dir = ${flexBinDir}
build.dir = ${buildDir}
deploy.dir = ${deployDir}

