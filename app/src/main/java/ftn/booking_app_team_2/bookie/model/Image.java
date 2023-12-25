package ftn.booking_app_team_2.bookie.model;

public class Image {
    private Long id = null;

    private String path;

    private String name;

    private String type;

    private boolean isDeleted;

    public Image(Long id, String path, String name, String type, boolean isDeleted) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.type = type;
        this.isDeleted = isDeleted;
    }

    public Image() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
