<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>${projectName}</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.granite.builder.granitebuilder</name>
			<arguments>
			</arguments>
		</buildCommand><%
if (flexBuilder) { %>
		<buildCommand>
			<name>com.adobe.flexbuilder.project.flexbuilder</name>
			<arguments>
			</arguments>
		</buildCommand><%
} %>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
		<nature>org.granite.builder.granitenature</nature><%
if (flexBuilder) { %>
		<nature>com.adobe.flexbuilder.project.flexnature</nature>
		<nature>com.adobe.flexbuilder.project.actionscriptnature</nature><%
} %>
	</natures>
</projectDescription>
