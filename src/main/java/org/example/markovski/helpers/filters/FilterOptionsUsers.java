package org.example.markovski.helpers.filters;

import lombok.Getter;

import java.util.Optional;

@Getter
public class FilterOptionsUsers {
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> email;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptionsUsers() {
        this(null, null, null, null, null);
    }

    public FilterOptionsUsers(String firstName,
                              String lastName, String email,
                              String sortBy, String sortOrder) {
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.email = Optional.ofNullable(email);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }
}
