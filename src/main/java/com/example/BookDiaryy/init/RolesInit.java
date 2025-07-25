package com.example.BookDiaryy.init;

import com.example.BookDiaryy.model.entity.Role;
import com.example.BookDiaryy.model.enums.UserRoleENUM;
import com.example.BookDiaryy.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RolesInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RolesInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        long count =roleRepository.count();
        if (count == 0){
            List<Role> roles = new ArrayList<>();

            Arrays.stream(UserRoleENUM.values())
                    .forEach(roleName -> {
                        Role role = new Role();
                        role.setName(roleName);
                        roles.add(role);
                    });
            roleRepository.saveAll(roles);
        }
    }
}
