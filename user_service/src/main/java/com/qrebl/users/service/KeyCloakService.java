package com.qrebl.users.service;

import com.qrebl.users.Credentials;
import com.qrebl.users.DTO.UserDTO;
import com.qrebl.users.KeycloakConfig;
import lombok.AllArgsConstructor;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class KeyCloakService {

    private static final String REALM = "myrealm";

    public String createUser(UserDTO userDTO) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource usersResource = getUserInstance(REALM);
        usersResource.create(user);
        return "Create USer Successfully.";
    }

    public UserResource getUserByUserId(String userId) {
        UsersResource usersResource = getUserInstance(REALM);
        return usersResource.get(userId);
    }

    public List<UserRepresentation> getUser(String userName) {
        UsersResource usersResource = getUserInstance(REALM);
        return usersResource.search(userName, true);

    }

    public void updateUser(String userId, UserDTO userDTO) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getUserInstance(REALM);
        usersResource.get(userId).update(user);
    }

    public void deleteUser(String userId) {
        UsersResource usersResource = getUserInstance(REALM);
        usersResource.get(userId)
                .remove();
    }

    public void setVerificationEmail(String userId, Boolean verificationStatus) {
        UsersResource usersResource = getUserInstance(REALM);
        UserResource userResource = usersResource.get(userId);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEmailVerified(verificationStatus);

        userResource.update(userRepresentation);
    }

    public void setUserEnabled(String userId, Boolean enableStatus) {
        UsersResource usersResource = getUserInstance(REALM);
        UserResource userResource = usersResource.get(userId);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(enableStatus);

        userResource.update(userRepresentation);
    }

    public RealmResource getRealmInstance(String realm) {
        return KeycloakConfig.getInstance().realm(realm);
    }

    public UsersResource getUserInstance(String realm) {
        return getRealmInstance(realm).users();
    }

    public void addRoleToUser(String username, String rolename) {
        RealmResource realmResource = getRealmInstance(REALM);
        UsersResource usersResource = realmResource.users();
        // Retrieve the user
        UserRepresentation user = usersResource.search(username).get(0);

        // Retrieve the role
        RoleRepresentation role = realmResource.roles().get(rolename).toRepresentation();

        // Add the role to the user
        usersResource.get(user.getId()).roles().realmLevel().add(Collections.singletonList(role));
    }

    public void createRole(String rolename) {
        RealmResource realmResource = getRealmInstance(REALM);
        RolesResource roleResource = realmResource.roles();

        RoleRepresentation role = new RoleRepresentation();
        role.setName(rolename);

        roleResource.create(role);
    }

    public void resetPassword(String userId, String password) {
        UsersResource usersResource = getUserInstance(REALM);

        UserResource userResource = usersResource.get(userId);

        userResource.resetPassword(Credentials.createPasswordCredentials(password));

    }

}
