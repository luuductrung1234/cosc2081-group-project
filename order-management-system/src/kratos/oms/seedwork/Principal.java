package kratos.oms.seedwork;

import kratos.oms.domain.Membership;
import kratos.oms.domain.Role;

import java.util.UUID;

public class Principal {
    private UUID id;
    private String username;
    private Role role;
    private Membership membership;

    public Principal(UUID id, String username, Role role, Membership membership) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.membership = membership;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public Membership getMembership() {
        return membership;
    }
}
