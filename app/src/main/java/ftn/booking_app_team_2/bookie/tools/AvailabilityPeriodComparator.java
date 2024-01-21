package ftn.booking_app_team_2.bookie.tools;

import java.util.Comparator;

import ftn.booking_app_team_2.bookie.model.AvailabilityPeriod;

public class AvailabilityPeriodComparator implements Comparator<AvailabilityPeriod> {
    @Override
    public int compare(AvailabilityPeriod firstAvailabilityPeriod,
                       AvailabilityPeriod secondAvailabilityPeriod) {
        return firstAvailabilityPeriod.getPrice().compareTo(secondAvailabilityPeriod.getPrice());
    }
}
