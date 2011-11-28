<% if (javaFramework != "CDI")
	throw new org.granite.generator.CancelFileGenerationException()

%>package ${packageName};

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;


@Transactional @Interceptor
public class TransactionInterceptor {

	@Inject
	protected UserTransaction userTransaction;

	@Inject
    protected EntityManager entityManager;

	
	@AroundInvoke
	public Object manageTransaction(InvocationContext context) {
		Object result = null;
		
    	try {
    		userTransaction.begin();
    		entityManager.joinTransaction();
    		
    		result = context.proceed();
    		
    		entityManager.flush();
    		userTransaction.commit();
    		
    		return result;
    	}
    	catch (Exception e) {
    		try {
    			userTransaction.rollback();
    		}
    		catch (Exception f) {
    		}
    		throw new RuntimeException("Transaction failed", e);
    	}
	}
}
