<%
	if (!antBuild)
		throw new org.granite.generator.CancelFileGenerationException();

%>project.name = ${Fs.escapeBackslashes(projectName)}
flex.home = ${Fs.escapeBackslashes(flexSdkDir)}
flex.src.dir = ${Fs.escapeBackslashes(flexSrcDir)}
build.dir = ${Fs.escapeBackslashes(buildDir)}
deploy.dir = ${Fs.escapeBackslashes(deployDir)}

