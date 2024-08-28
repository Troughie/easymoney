package com.example.MoneyLover.infra.User.Entity;

import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Friend.Entity.Friend;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.shares.Annotation.Column;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String username;
    @Column(unique = true)
    String email;

    String password;

    boolean is_enable;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonIgnore
    List<Wallet> wallets =new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonIgnore
    List<Category> categories =new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Friend> friends = new ArrayList<>();

    public User(String name, String password, List<GrantedAuthority> grantedAuths) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id); // Assuming `id` is the unique identifier for User
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
