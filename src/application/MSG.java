package application;

import javafx.beans.property.SimpleStringProperty;

public class MSG {

    private SimpleStringProperty id;

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getContent() {
        return content.get();
    }

    public SimpleStringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    private SimpleStringProperty content;
    private SimpleStringProperty email;

    public MSG(String id, String name, String email){
        this.id = new SimpleStringProperty(id);
        this.content = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
    }
}
