package hello.entity.removeCascade.orphan;

import hello.entity.AbstractBaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "client_orphan")
public class ClientOrphan extends AbstractBaseEntity<Long> {

    @OneToMany(mappedBy = "clientOrphan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AccountOrphan> accountOrphanList;

    public List<AccountOrphan> getAccountOrphanList() {
        return accountOrphanList;
    }

    public void setAccountOrphanList(List<AccountOrphan> accountOrphanList) {
        this.accountOrphanList = accountOrphanList;
    }
}
