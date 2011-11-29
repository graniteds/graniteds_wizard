<% if (javaFramework != "Spring3" && javaFramework != "Seam2")
	throw new org.granite.generator.CancelFileGenerationException();
%>package ${packageName}.services;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.persistence.Query;
import javax.persistence.EntityManager;
<% if (javaFramework == "Spring3") {%>import javax.persistence.PersistenceContext;<% }%>
import javax.persistence.NoResultException;

import org.granite.tide.data.DataEnabled;
import org.granite.tide.data.DataEnabled.PublishMode;

<% if (javaFramework == "Spring3") {%>
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;<%
} else if (javaFramework == "Seam2") {%>
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Transactional;<%
}%>

import ${packageName}.entities.Welcome;


<% if (javaFramework == "Spring3") {%>@Service<%
} else if (javaFramework == "Seam2") {%>@Name("welcomeService")
@Scope(ScopeType.EVENT)<%
}%>
@DataEnabled(topic="welcomeTopic", publish=PublishMode.ON_SUCCESS)
public class WelcomeServiceImpl implements WelcomeService {

    <% if (javaFramework == "Spring3") { %>@PersistenceContext<% } else { %>@In<% } %>
    private EntityManager entityManager;
    

    @Transactional
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
    
    
    @Transactional<% if (javaFramework == "Spring3") {%>(readOnly=true)<% }
if (targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5")) {%>
	@SuppressWarnings("unchecked")<%
}%>
    public List<Welcome> findAll() {<%
if (targetPlatform.startsWith("JB4") || targetPlatform.startsWith("JB5")) {%>
        return entityManager.createQuery("select w from Welcome w order by w.name").getResultList();<%
} else {%>
        return entityManager.createQuery("select w from Welcome w order by w.name", Welcome.class).getResultList();<%
}%>
    }
}
