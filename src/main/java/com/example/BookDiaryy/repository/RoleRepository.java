package com.example.BookDiaryy.repository;

import com.example.BookDiaryy.model.entity.Role;
import com.example.BookDiaryy.model.enums.UserRoleENUM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRoleENUM name);
}
