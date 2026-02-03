package fpoly.model;

/**
 * Model class cho Organization Unit
 * - unitId: String (max 20 ký tự, optional, alphanumeric + underscore)
 * - name: String (max 100 ký tự, required)
 * - description: String (max 255 ký tự, optional)
 */
public class OrganizationUnit {
    private String unitId;
    private String name;
    private String description;

    public OrganizationUnit() {
    }

    public OrganizationUnit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public OrganizationUnit(String unitId, String name, String description) {
        this.unitId = unitId;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
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
                "unitId='" + unitId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
