<% if (javaFramework != "EJB31" && javaFramework != "CDI")
	throw new org.granite.generator.CancelFileGenerationException();
%>package ${packageName}.services;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

<% if (javaFramework == "EJB31") {%>
import javax.ejb.Stateless;
import javax.ejb.Local;
import javax.persistence.PersistenceContext;<%
} else if (javaFramework == "CDI") {%>
import javax.inject.Inject;<%
}%>
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.granite.tide.data.DataEnabled;
import org.granite.tide.data.DataEnabled.PublishMode;

<% if (javaFramework == "CDI") { %>import ${packageName}.Transactional;<% } %>
import ${packageName}.entities.Welcome;


<% if (javaFramework == "EJB31") {%>
@Stateless
@Local(WelcomeService.class)<%
} else {%>
@Transactional<%
}%>
@DataEnabled(topic="welcomeTopic", publish=PublishMode.ON_SUCCESS)
public class WelcomeServiceBean implements WelcomeService {

    <% if (javaFramework == "CDI") {%>@Inject<% } else {%>@PersistenceContext<% }%>
    private EntityManager entityManager;
    

    public Welcome hello(String name) {
        if (name == null || name.trim().length() == 0)
            throw new RuntimeException("Name cannot be null or empty");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Welcome welcome = null;
        try {
            Query q = entityManager.createQuery("select w from Welcome w where w.name = :name");
            q.setParameter("name", name);
            welcome = (Welcome)q.getSingleResult();
            welcome.setMessage("Welcome " + name + " (" + sdf.format(new Date()) + ")");
        }
        catch (NoResultException e) {
            welcome = new Welcome();
            welcome.setName(name);
            welcome.setMessage("Welcome " + name + " (" + sdf.format(new Date()) + ")");
            entityManager.persist(welcome);
        }
        return welcome;
    }
    
    
	public List<Welcome> findAll() {
		return entityManager.createQuery("select w from Welcome w order by w.name", Welcome.class).getResultList();
    }
}
