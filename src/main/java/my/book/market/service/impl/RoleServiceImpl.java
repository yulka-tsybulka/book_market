package my.book.market.service.impl;

import lombok.RequiredArgsConstructor;
import my.book.market.model.Role;
import my.book.market.repository.role.RoleRepository;
import my.book.market.service.RoleService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(Role.RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(() ->
                new RuntimeException("Can't find role by roleName: " + roleName));
    }
}
