package kratos.oms.seedwork;

import kratos.oms.domain.Membership;
import kratos.oms.domain.Role;

import java.util.UUID;

public class Principal {
    private final UUID id;
    private final String username;
    private final Role role;
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

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
