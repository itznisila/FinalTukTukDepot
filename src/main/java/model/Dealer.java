package model;

public class Dealer {

    private String code;
    private String name;
    private String phone;
    private String location;

    public Dealer(String code, String name, String phone, String location) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.location = location;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
