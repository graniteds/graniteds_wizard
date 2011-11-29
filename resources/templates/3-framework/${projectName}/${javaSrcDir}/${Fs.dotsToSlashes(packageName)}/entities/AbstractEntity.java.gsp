package ${packageName}.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
<% if (targetPlatform.startsWith("JB7")) { %>import javax.persistence.GenerationType;<% } %>
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import org.granite.tide.data.DataPublishListener;



@MappedSuperclass
@EntityListeners({AbstractEntity.AbstractEntityListener.class, DataPublishListener.class})
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@Id @GeneratedValue<% if (targetPlatform.startsWith("JB7")) { %>(strategy=GenerationType.SEQUENCE)<% } %>
    private Long id;

    /* "UUID" and "UID" are Oracle reserved keywords -> "ENTITY_UID" */
    @Column(name="ENTITY_UID", unique=true, nullable=false, updatable=false, length=36)
    private String uid;

    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this || (o instanceof AbstractEntity && uid().equals(((AbstractEntity)o).uid())));
    }

    @Override
    public int hashCode() {
        return uid().hashCode();
    }

    public static class AbstractEntityListener {

        @PrePersist
        public void onPrePersist(AbstractEntity abstractEntity) {
            abstractEntity.uid();
        }
    }

    private String uid() {
        if (uid == null)
            uid = UUID.randomUUID().toString();
        return uid;
    }
}
