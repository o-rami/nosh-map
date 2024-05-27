package nosh.nosh_map_server.models;

import java.util.Objects;

public class Profile {


    private int appUserId;
    private String firstName;
    private String lastName;
    private String address;

    public Profile() {

    }

    public Profile(String firstName, String lastName, String address, int appUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.appUserId = appUserId;

    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile profile)) return false;
        return getAppUserId() == profile.getAppUserId() && Objects.equals(getFirstName(), profile.getFirstName())
                && Objects.equals(getLastName(), profile.getLastName())
                && Objects.equals(getAddress(), profile.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAppUserId(), getFirstName(), getLastName(), getAddress());
    }

    @Override
    public String toString() {
        return "Profile{" +
                "appUserId=" + appUserId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
