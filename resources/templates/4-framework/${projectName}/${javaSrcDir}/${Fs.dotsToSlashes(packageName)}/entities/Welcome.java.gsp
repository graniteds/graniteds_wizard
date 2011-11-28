package ${packageName}.entities;

import javax.persistence.Entity;
import javax.persistence.Basic;


@Entity
public class Welcome extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
