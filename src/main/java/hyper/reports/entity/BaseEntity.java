package hyper.reports.entity;

public abstract class BaseEntity {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (object == this) return true;

        if (object == null) return false;

        if (object.getClass() != this.getClass()) return false;

        BaseEntity other = (BaseEntity) object;
        if (this.hashCode() == 0 || object.hashCode() == 0) return false;

        return this.getId().equals(other.getId());
    }
}


