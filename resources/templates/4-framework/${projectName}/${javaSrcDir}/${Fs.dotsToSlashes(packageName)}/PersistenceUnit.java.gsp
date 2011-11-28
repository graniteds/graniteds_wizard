<% if (javaFramework != "CDI")
	throw new org.granite.generator.CancelFileGenerationException();
%>package ${packageName};

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class PersistenceUnit {
	
	@Produces @PersistenceContext
	EntityManager entityManager;
}
