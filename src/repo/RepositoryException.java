package repo;

public class RepositoryException extends Exception {
    private String custom_message;
    public RepositoryException(String custom_message) {this.custom_message = custom_message;}

    public String getCustom_message() {return custom_message;}
}
