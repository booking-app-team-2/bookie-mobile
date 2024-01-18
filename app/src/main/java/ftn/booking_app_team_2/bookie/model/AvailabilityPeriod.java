package ftn.booking_app_team_2.bookie.model;

import java.math.BigDecimal;

public class AvailabilityPeriod {
    private Long id = null;

    private BigDecimal price;

    private PeriodDTO periodDTO;


    private boolean isDeleted = false;
    public AvailabilityPeriod(Long id, BigDecimal price, PeriodDTO periodDTO, boolean isDeleted) {
        this.id = id;
        this.price = price;
        this.periodDTO = periodDTO;
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

    public PeriodDTO getPeriod() {
        return periodDTO;
    }

    public void setPeriod(PeriodDTO periodDTO) {
        this.periodDTO = periodDTO;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
