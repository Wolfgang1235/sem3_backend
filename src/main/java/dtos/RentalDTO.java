package dtos;

import java.util.List;

public class RentalDTO {
    private final Integer id;
    private final String start_date;
    private final String end_date;
    private final Integer price_annual;
    private final Integer deposit;
    private final String contact_person;
    private final HouseDTO house;
    private final List<String> tenants;
    private final Integer house_id;
    private final List<Integer> tenant_ids;

    private RentalDTO(Builder builder) {
        this.id = builder.id;
        this.start_date = builder.startDate;
        this.end_date = builder.endDate;
        this.price_annual = builder.priceAnnual;
        this.deposit = builder.deposit;
        this.contact_person = builder.contactPerson;
        this.house = builder.house;
        this.tenants = builder.tenants;
        this.house_id = builder.houseId;
        this.tenant_ids = builder.tenantIds;
    }

    public Integer getId() {
        return id;
    }

    public String getStartDate() {
        return start_date;
    }

    public String getEndDate() {
        return end_date;
    }

    public Integer getPriceAnnual() {
        return price_annual;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public String getContactPerson() {
        return contact_person;
    }

    public HouseDTO getHouse() {
        return house;
    }

    public List<String> getTenants() {
        return tenants;
    }

    public Integer getHouseId() {
        return house_id;
    }

    public List<Integer> getTenantIds() {
        return tenant_ids;
    }

    public static class Builder {
        private Integer id;
        private String startDate;
        private String endDate;
        private Integer priceAnnual;
        private Integer deposit;
        private String contactPerson;
        private HouseDTO house;
        private List<String> tenants;
        private Integer houseId;
        private List<Integer> tenantIds;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setPriceAnnual(Integer priceAnnual) {
            this.priceAnnual = priceAnnual;
            return this;
        }

        public Builder setDeposit(Integer deposit) {
            this.deposit = deposit;
            return this;
        }

        public Builder setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
            return this;
        }

        public Builder setHouse(HouseDTO house) {
            this.house = house;
            return this;
        }

        public Builder setTenants(List<String> tenants) {
            this.tenants = tenants;
            return this;
        }

        public Builder setHouseId(Integer houseId) {
            this.houseId = houseId;
            return this;
        }

        public Builder setTenantIds(List<Integer> tenantIds) {
            this.tenantIds = tenantIds;
            return this;
        }

        public RentalDTO build() {
            return new RentalDTO(this);
        }
    }
}
