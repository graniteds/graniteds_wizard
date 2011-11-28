<% if (javaFramework != "Seam2")
	throw new org.granite.generator.CancelFileGenerationException()
%>package ${packageName};

import static org.jboss.seam.ScopeType.SESSION;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Identity;

@SuppressWarnings("all")
@Name("authenticator")
public class Authenticator {
	
	@In
	private Identity identity;
	
	
	public boolean authenticate() {
		if ("admin".equals(identity.getCredentials().getUsername()) && "admin".equals(identity.getCredentials().getPassword())) {
			identity.addRole("admin");
			return true;
		}
		if ("user".equals(identity.getCredentials().getUsername()) && "user".equals(identity.getCredentials().getPassword())) {
			identity.addRole("user");
			return true;
		}
		return false;
   }
}
