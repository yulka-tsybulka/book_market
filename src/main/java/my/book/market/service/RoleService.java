package my.book.market.service;

import my.book.market.model.Role;

public interface RoleService {
    Role getRoleByRoleName(Role.RoleName roleName);
}
