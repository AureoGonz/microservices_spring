package com.triceracode.shoppingms.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {

	private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String photo;
    private Region region;
    private String status;
}
