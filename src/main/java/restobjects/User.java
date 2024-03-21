package restobjects;

import restobjects.Address;

public record User(String firstName, String secondName, int age, String job, Address address) {
}
