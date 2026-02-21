package com.substring.auth.auth_app_backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "User")     //if I donot pass the entity name -> (name = "User"), the class name becomes entity which is "User"
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID id;
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
    protected void onCreate(){
        Instant now = Instant.now();
        if(createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = Instant.now();
    }
}
