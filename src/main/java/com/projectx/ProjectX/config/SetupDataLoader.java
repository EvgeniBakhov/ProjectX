package com.projectx.ProjectX.config;

import com.projectx.ProjectX.model.Privilege;
import com.projectx.ProjectX.model.Role;
import com.projectx.ProjectX.repository.PrivilegeRepository;
import com.projectx.ProjectX.repository.RoleRepository;
import com.projectx.ProjectX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) {
            return;
        }
        Privilege bookEstatePrivilege = createPrivilegeIfNotFound("BOOK_ESTATE");
        Privilege reserveEventPlacePrivilege = createPrivilegeIfNotFound("RESERVE_EVENT_PLACE");
        Privilege updateProfilePrivilege = createPrivilegeIfNotFound("UPDATE_PROFILE");
        Privilege publishEstatePrivilege = createPrivilegeIfNotFound("PUBLISH_PRIVILEGE");
        Privilege publishEventPrivilege = createPrivilegeIfNotFound("PUBLISH_EVENT");
        Privilege deleteUserPrivilege = createPrivilegeIfNotFound("DELETE_USER");
        Privilege editUserPrivilege = createPrivilegeIfNotFound("EDIT_USER");
        Privilege editForeignEventsPrivilege = createPrivilegeIfNotFound("EDIT_FOREIGN_EVENTS");
        Privilege editForeignEstatesPrivilege = createPrivilegeIfNotFound("EDIT_FOREIGN_ESTATES");

        List<Privilege> adminPrivileges = Arrays.asList(
                bookEstatePrivilege,
                reserveEventPlacePrivilege,
                updateProfilePrivilege,
                publishEstatePrivilege,
                publishEventPrivilege,
                deleteUserPrivilege,
                editForeignEstatesPrivilege,
                editForeignEventsPrivilege,
                editUserPrivilege);

        List<Privilege> normalPrivileges = Arrays.asList(
                bookEstatePrivilege,
                reserveEventPlacePrivilege,
                updateProfilePrivilege);

        List<Privilege> ownerPrivileges = Arrays.asList(
                bookEstatePrivilege,
                reserveEventPlacePrivilege,
                updateProfilePrivilege,
                publishEstatePrivilege);

        List<Privilege> organizerPrivileges = Arrays.asList(
                bookEstatePrivilege,
                reserveEventPlacePrivilege,
                updateProfilePrivilege,
                publishEventPrivilege);

        createRoleIfNotFound("ROLE_NORMAL", normalPrivileges);
        createRoleIfNotFound("ROLE_OWNER", ownerPrivileges);
        createRoleIfNotFound("ROLE_ORGANIZER", organizerPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Optional<Role> existingRole = roleRepository.findByName(name);
        if (existingRole.isPresent()) {
            return existingRole.get();
        }
        Role role = new Role();
        role.setName(name);
        role.setPrivileges(privileges);
        role.setCreatedBy("system");
        role.setModifiedBy("system");
        return roleRepository.save(role);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Optional<Privilege> existingPrivilege = privilegeRepository.findByName(name);
        if(existingPrivilege.isPresent()) {
            return existingPrivilege.get();
        }
        Privilege privilege = new Privilege();
        privilege.setName(name);

    }
}
