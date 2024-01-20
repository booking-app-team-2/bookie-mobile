package ftn.booking_app_team_2.bookie.tools;

import java.util.Comparator;
import java.util.Optional;

import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.model.AvailabilityPeriod;

public class AccommodationComparator implements Comparator<AccommodationDTO> {
    @Override
    public int compare(AccommodationDTO firstAccommodation, AccommodationDTO secondAccommodation) {
        Optional<AvailabilityPeriod> firstMinAvailabilityPeriodOptional =
                firstAccommodation
                        .getAvailabilityPeriods()
                        .stream()
                        .min(new AvailabilityPeriodComparator());
        Optional<AvailabilityPeriod> secondMinAvailabilityPeriodOptional =
                secondAccommodation
                        .getAvailabilityPeriods()
                        .stream()
                        .min(new AvailabilityPeriodComparator());

        return firstMinAvailabilityPeriodOptional
                .map(firstMinAvailabilityPeriod ->
                        secondMinAvailabilityPeriodOptional
                                .map(secondMinAvailabilityPeriod ->
                                        firstMinAvailabilityPeriod
                                                .getPrice()
                                                .compareTo(secondMinAvailabilityPeriod.getPrice())
                                )
                                .orElse(-1)
                ).orElse(1);

    }
}
