package entities;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "rentals")
public class Rental implements entities.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 75)
    @NotNull
    @Column(name = "startDate", nullable = false, length = 75)
    private String startDate;

    @Size(max = 75)
    @NotNull
    @Column(name = "endDate", nullable = false, length = 75)
    private String endDate;

    @NotNull
    @Column(name = "priceAnnual", nullable = false)
    private Integer priceAnnual;

    @NotNull
    @Column(name = "deposit", nullable = false)
    private Integer deposit;

    @Size(max = 45)
    @NotNull
    @Column(name = "contactPerson", nullable = false, length = 45)
    private String contactPerson;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "houses_id", nullable = false)
    private House houses;

    @ManyToMany
    @JoinTable(name = "tenants_rentals",
            joinColumns = @JoinColumn(name = "rental_id"),
            inverseJoinColumns = @JoinColumn(name = "tenant_id"))
    private List<Tenant> tenants = new ArrayList<>();

    public Rental() {
    }

    public Rental(String startDate, String endDate, Integer priceAnnual, Integer deposit,
                  String contactPerson, House houses, List<Tenant> tenants) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceAnnual = priceAnnual;
        this.deposit = deposit;
        this.contactPerson = contactPerson;
        this.houses = houses;
        this.tenants = tenants;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getPriceAnnual() {
        return priceAnnual;
    }

    public void setPriceAnnual(Integer priceAnnual) {
        this.priceAnnual = priceAnnual;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public House getHouse() {
        return houses;
    }

    public void setHouses(House houses) {
        this.houses = houses;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    public List<Integer> getTenantIds() {
        List<Integer> tenantIds = new ArrayList<>();
        tenants.forEach((tenant) -> tenantIds.add(tenant.getId()));
        return tenantIds;
    }

    public List<String> getTenantsAsStringList() {
        List<String> tenantsAsStrings = new ArrayList<>();
        tenants.forEach((tenant) -> tenantsAsStrings.add(tenant.getName()));
        return tenantsAsStrings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rental)) return false;
        Rental rental = (Rental) o;
        return Objects.equals(getId(), rental.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}