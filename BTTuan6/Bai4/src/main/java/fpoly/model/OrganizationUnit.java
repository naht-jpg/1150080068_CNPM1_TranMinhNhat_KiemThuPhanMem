package fpoly.model;

public class OrganizationUnit {
    private int unitId;
    private String name;
    private String description;

    public OrganizationUnit() {
    }

    public OrganizationUnit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public OrganizationUnit(int unitId, String name, String description) {
        this.unitId = unitId;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
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

    @Override
    public String toString() {
        return "OrganizationUnit{" +
                "unitId=" + unitId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
