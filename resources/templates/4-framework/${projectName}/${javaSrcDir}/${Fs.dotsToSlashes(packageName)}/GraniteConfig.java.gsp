<% if (javaFramework != "EJB31" && javaFramework != "CDI")
	throw new org.granite.generator.CancelFileGenerationException();
%>package ${packageName};


import org.granite.config.servlet3.FlexFilter;
import org.granite.gravity.config.AbstractMessagingDestination;
import org.granite.gravity.config.servlet3.MessagingDestination;
<% if (javaFramework == "EJB31") {%>
import org.granite.tide.ejb.EjbConfigProvider;

@FlexFilter(configProviderClass=EjbConfigProvider.class,
entityManagerJndiName="java:comp/env/${projectName}-pc"
)<%
} else if (javaFramework == "CDI") {%>
import org.granite.tide.cdi.CDIConfigProvider;

@FlexFilter(configProviderClass=CDIConfigProvider.class)<%
}%>
public class GraniteConfig {

	@MessagingDestination(noLocal=true, sessionSelector=true)
	AbstractMessagingDestination welcomeTopic;

}
