public class NamedObject {
    private String name;
    private String description;
    private String value;

    NamedObject(String name, String description, String value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public String getValue() {
        return value;
    }
}