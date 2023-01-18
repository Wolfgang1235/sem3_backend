package dtos;

import java.util.List;

public class TenantDTO {
    private final Integer id;
    private final String name;
    private final Integer phone;
    private final String job;
    private final Integer user_id;
    private final List<String> rentals;
    private final List<Integer> rental_ids;

    private TenantDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.job = builder.job;
        this.user_id = builder.userId;
        this.rentals = builder.rentals;
        this.rental_ids = builder.rentalIds;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getRentals() {
        return rentals;
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Integer phone;
        private String job;
        private Integer userId;
        private List<String> rentals;
        private List<Integer> rentalIds;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPhone(Integer phone) {
            this.phone = phone;
            return this;
        }

        public Builder setJob(String job) {
            this.job = job;
            return this;
        }

        public Builder setUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder setRentals(List<String> rentals) {
            this.rentals = rentals;
            return this;
        }

        public TenantDTO build() {
            return new TenantDTO(this);
        }
    }
}
