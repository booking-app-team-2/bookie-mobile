package ftn.booking_app_team_2.bookie.model;

import java.util.Set;

import ftn.booking_app_team_2.bookie.model.AccommodationType;
import ftn.booking_app_team_2.bookie.model.Amenities;
import ftn.booking_app_team_2.bookie.model.AvailabilityPeriod;
import ftn.booking_app_team_2.bookie.model.Image;
import ftn.booking_app_team_2.bookie.model.Location;

public class AccommodationDTO {
    private Long id = null;
    private String name;
    private String description;
    private int minimumGuests;
    private int maximumGuests;
    private Location location;
    private Set<Amenities> amenities;
    private Set<AvailabilityPeriod> availabilityPeriods;
    private Set<Image> images;
    private int reservationCancellationDeadline;
    private AccommodationType type;
    private boolean isReservationAutoAccepted;

    public AccommodationDTO(Long id, String name, String description, int minimumGuests, int maximumGuests, Location location, Set<Amenities> amenities, Set<AvailabilityPeriod> availabilityPeriods, Set<Image> images, int reservationCancellationDeadline, AccommodationType type, boolean isReservationAutoAccepted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minimumGuests = minimumGuests;
        this.maximumGuests = maximumGuests;
        this.location = location;
        this.amenities = amenities;
        this.availabilityPeriods = availabilityPeriods;
        this.images = images;
        this.reservationCancellationDeadline = reservationCancellationDeadline;
        this.type = type;
        this.isReservationAutoAccepted = isReservationAutoAccepted;
    }

    public AccommodationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinimumGuests() {
        return minimumGuests;
    }

    public void setMinimumGuests(int minimumGuests) {
        this.minimumGuests = minimumGuests;
    }

    public int getMaximumGuests() {
        return maximumGuests;
    }

    public void setMaximumGuests(int maximumGuests) {
        this.maximumGuests = maximumGuests;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Amenities> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenities> amenities) {
        this.amenities = amenities;
    }

    public Set<AvailabilityPeriod> getAvailabilityPeriods() {
        return availabilityPeriods;
    }

    public void setAvailabilityPeriods(Set<AvailabilityPeriod> availabilityPeriods) {
        this.availabilityPeriods = availabilityPeriods;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public int getReservationCancellationDeadline() {
        return reservationCancellationDeadline;
    }

    public void setReservationCancellationDeadline(int reservationCancellationDeadline) {
        this.reservationCancellationDeadline = reservationCancellationDeadline;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public boolean isReservationAutoAccepted() {
        return isReservationAutoAccepted;
    }

    public void setReservationAutoAccepted(boolean reservationAutoAccepted) {
        isReservationAutoAccepted = reservationAutoAccepted;
    }

    @Override
    public String toString() {
        return "AccommodationDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minimumGuests=" + minimumGuests +
                ", maximumGuests=" + maximumGuests +
                ", location=" + location +
                ", amenities=" + amenities +
                ", availabilityPeriods=" + availabilityPeriods +
                ", images=" + images +
                ", reservationCancellationDeadline=" + reservationCancellationDeadline +
                ", type=" + type +
                ", isReservationAutoAccepted=" + isReservationAutoAccepted +
                '}';
    }
}
