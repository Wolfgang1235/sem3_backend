package dtos;

public class TenantDTO {
    private final Integer id;
    private final String name;
    private final Integer phone;
    private final String job;
    private final Integer user_id;

    private TenantDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.job = builder.job;
        this.user_id = builder.userId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPhone() {
        return phone;
    }

    public String getJob() {
        return job;
    }

    public Integer getUserId() {
        return user_id;
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Integer phone;
        private String job;
        private Integer userId;

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

        public TenantDTO build() {
            return new TenantDTO(this);
        }
    }
}
