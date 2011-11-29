<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>${projectName}</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec><%
if (flexBuilder) { %>
		<buildCommand>
			<name>com.adobe.flexbuilder.project.flexbuilder</name>
			<arguments>
			</arguments>
		</buildCommand><%
} %>
	</buildSpec>
	<natures><%
if (flexBuilder) { %>
		<nature>com.adobe.flexbuilder.project.flexnature</nature>
		<nature>com.adobe.flexbuilder.project.actionscriptnature</nature><%
} %>
	</natures>
</projectDescription>
