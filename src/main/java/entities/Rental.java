package entities;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "rentals")
public class Rental {
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
    private Set<Tenant> tenants = new LinkedHashSet<>();

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

    public House getHouses() {
        return houses;
    }

    public void setHouses(House houses) {
        this.houses = houses;
    }

    public Set<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(Set<Tenant> tenants) {
        this.tenants = tenants;
    }

}