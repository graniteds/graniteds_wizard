<%

	String tideFactoryId = null;
	String tideFactoryClass = null;
	if (tideEnabled) {
		String tideFrameworkUp = tideFramework.substring(0, 1).toUpperCase() + tideFramework.substring(1);
		tideFactoryId = "tide" + tideFrameworkUp + "Factory";
		tideFactoryClass = "org.granite.tide." + tideFramework + "." + tideFrameworkUp + "ServiceFactory";
	}

%><?xml version="1.0" encoding="UTF-8"?>

<services-config>

    <services>
    	<!-- Remoting service -->
        <service id="granite-service"
            class="flex.messaging.services.RemotingService"
            messageTypes="flex.messaging.messages.RemotingMessage"><%

if (tideEnabled) { %>
            <destination id="${tideFramework}">
                <channels>
                    <channel ref="graniteamf"/>
                </channels>
                <properties>
                    <factory>${tideFactoryId}</factory>
                    <entity-manager-factory-jndi-name>java:/DefaultEMF</entity-manager-factory-jndi-name>
                </properties>
            </destination><%
} %>
        </service><%

if (gravityEnabled) { %>

        <service id="gravity-service"
            class="flex.messaging.services.MessagingService"
            messageTypes="flex.messaging.messages.AsyncMessage">
            <adapters>
                <adapter-definition id="simple" class="org.granite.gravity.adapters.SimpleServiceAdapter"/>
            </adapters>

            <destination id="dummy">
                <properties>
                  <no-local>true</no-local>
                  <session-selector>true</session-selector>
                </properties>
                <channels>
                    <channel ref="gravityamf"/>
                </channels>
                <adapter ref="simple"/>
            </destination>
        </service><%
} %>
    </services><%

if (tideEnabled) { %>

    <factories>
        <factory id="${tideFactoryId}" class="${tideFactoryClass}">
            <properties>
                <lookup>${projectName}/{capitalized.component.name}Bean/local</lookup>
            </properties>
        </factory>
    </factories><%
} %>

    <!-- Channel definitions -->
    <channels>
        <channel-definition id="graniteamf" class="mx.messaging.channels.AMFChannel">
            <endpoint
                uri="http://{server.name}:{server.port}/{context.root}/graniteamf/amf"
                class="flex.messaging.endpoints.AMFEndpoint"/>
        </channel-definition><%

if (gravityEnabled) { %>
        <channel-definition id="gravityamf" class="org.granite.gravity.channels.GravityChannel">
            <endpoint
                uri="http://{server.name}:{server.port}/{context.root}/gravityamf/amf"
                class="flex.messaging.endpoints.AMFEndpoint"/>
        </channel-definition><%
} %>
    </channels>

</services-config>