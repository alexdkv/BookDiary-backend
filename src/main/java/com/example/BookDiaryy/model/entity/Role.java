package com.example.BookDiaryy.model.entity;


import com.example.BookDiaryy.model.enums.UserRoleENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private UserRoleENUM name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRoleENUM getName() {
        return name;
    }

    public void setName(UserRoleENUM name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
