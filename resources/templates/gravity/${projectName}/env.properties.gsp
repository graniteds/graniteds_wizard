<%
	if (!antBuild)
		throw new org.granite.wizard.CancelFileGenerationException();

%>flex.home = ${flexSdkDir}
flex.src.dir = ${flexSrcDir}
flex.bin.dir = ${flexBinDir}
build.dir = build
deploy.dir = ${deployDir}

