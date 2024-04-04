package org.example.markovski.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterDtoUser {
    private String firstName;
    private String lastName;
    private String email;
    private String sortBy;
    private String sortOrder;
}
