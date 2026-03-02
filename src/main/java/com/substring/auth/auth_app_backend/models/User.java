package com.substring.auth.auth_app_backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "User")     //if I donot pass the entity name -> (name = "User"), the class name becomes entity which is "User"
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID id;                //UUID (Universally Unique Identifier) is a 128-bit unique value used to uniquely identify information in distributed systems without coordination between servers.
                                    //It is also called GUID (Globally Unique Identifier) in Microsoft ecosystems. 550e8400-e29b-41d4-a716-446655440000. Represented as 32 hexadecimal characters. Displayed in 5 groups separated by hyphens: 8-4-4-4-12
    @Column(name = "user_email", unique = true, length = 300)
    private String email;
    @Column(name = "user_name", unique = true, length = 500)
    private String name;
    private String password;
    private String image;
    private boolean enable = true;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

//    private String gender;
//    private Address address;

    private Provider provider = Provider.LOCAL;
    @ManyToMany(fetch = FetchType.EAGER)                    //In a Many-to-Many relationship, JPA needs a third table (called a join table) to connect both tables.
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))       //lest suppose User 1 ko 3 role diya ho, user 3 ko 5 role diya ho

    private Set<Role> roles = new HashSet<>();

    //    @PrePersist is a JPA lifecycle callback annotation. This method runs automatically before the entity is saved (INSERTED) into the database.So when you do: userRepository.save(user);
    @PrePersist
    protected void onCreate(){              //JPA requires the method to be non-private. @PrePersist is a JPA lifecycle callback. Hibernate uses reflection to call lifecycle methods.JPA specification says: Lifecycle callback methods must be: void, no agruments, not static, not final, not private.
                                            //Why NOT public? This method is not part of business API. No one outside entity should call it
        Instant now = Instant.now();
        if(createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate  //@PreUpdate is a JPA lifecycle callback annotation. It means “Execute this method automatically before the entity is updated in the database.”
    protected void onUpdate(){
        updatedAt = Instant.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {            //when spring calls authority, it should return authority kept in roles -> admin, user, support, guest, etc.
    //getAuthorities()?Converts your application roles into Spring Security authorities so that Spring can perform authorization checks. Spring Security does NOT understand your custom Role entity directly.
        List<SimpleGrantedAuthority> authorities = roles
                .stream()
                .map( role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();     //GrantedAuthority is interface and SimpleGrantedAuthority is implementation class
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;          // when service does getUsername -> what should you return, what you wnat your username to be -> I want here as email
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return this.enable;
    }
}
