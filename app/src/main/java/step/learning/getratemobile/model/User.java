package step.learning.getratemobile.model;

public class User {
    public String name;
    public String email;
    public String password;
    public String avatarUri;

    public User(String name, String email, String password, String avatarUri) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatarUri = avatarUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
