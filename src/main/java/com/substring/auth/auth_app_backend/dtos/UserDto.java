package com.substring.auth.auth_app_backend.dtos;

import com.substring.auth.auth_app_backend.models.Provider;
import com.substring.auth.auth_app_backend.models.Role;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {          //if we have to transfer data, we use dto and if we save data we use entities

    private UUID id;
    private String name;
    private String email;
    private String password;
    private String image;
    private boolean enable = true;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private Provider provider = Provider.LOCAL;
    private Set<RoleDto> roles = new HashSet<>();


}
