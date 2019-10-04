package hyper.reports.entity;

import java.time.Instant;

public class Company extends BaseEntity {
    private String name;
    private Instant lastDocDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getLastDocDate() {
        return lastDocDate;
    }

    public void setLastDocDate(Instant lastDocDate) {
        this.lastDocDate = lastDocDate;
    }
}
