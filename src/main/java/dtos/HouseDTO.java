package dtos;

import entities.Rental;

import java.util.List;

public class HouseDTO {
    private final Integer id;
    private final String address;
    private final String city;
    private final Integer number_of_rooms;
    private final List<Rental> rentals;

    private HouseDTO(Builder builder) {
        this.id = builder.id;
        this.address = builder.address;
        this.city = builder.city;
        this.number_of_rooms = builder.numberOfRooms;
        this.rentals = builder.rentals;
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public Integer getNumber_of_rooms() {
        return number_of_rooms;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public static class Builder {
        private Integer id;
        private String address;
        private String city;
        private Integer numberOfRooms;
        private List<Rental> rentals;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setNumberOfRooms(Integer numberOfRooms) {
            this.numberOfRooms = numberOfRooms;
            return this;
        }

        public Builder setRentals(List<Rental> rentals) {
            this.rentals = rentals;
            return this;
        }

        public HouseDTO build() {
            return new HouseDTO(this);
        }
    }
}
