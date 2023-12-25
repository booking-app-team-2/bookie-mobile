package ftn.booking_app_team_2.bookie.model;

import java.math.BigDecimal;

public class AvailabilityPeriod {
    private Long id = null;

    private BigDecimal price;

    private Period period;


    private boolean isDeleted = false;
    public AvailabilityPeriod(Long id, BigDecimal price, Period period, boolean isDeleted) {
        this.id = id;
        this.price = price;
        this.period = period;
        this.isDeleted = isDeleted;
    }

    public AvailabilityPeriod() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
