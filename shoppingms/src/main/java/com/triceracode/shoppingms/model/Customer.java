package com.triceracode.shoppingms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String photo;
    private Region region;
    private String status;
}
