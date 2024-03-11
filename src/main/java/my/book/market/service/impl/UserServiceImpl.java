package my.book.market.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.user.UserRegistrationRequestDto;
import my.book.market.dto.user.UserResponseDto;
import my.book.market.exception.RegistrationException;
import my.book.market.mapper.UserMapper;
import my.book.market.model.Role;
import my.book.market.model.User;
import my.book.market.repository.user.UserRepository;
import my.book.market.service.RoleService;
import my.book.market.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("The user with this email is already registered "
                    + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleService.getRoleByRoleName(Role.RoleName.USER);
        user.setRoles(new HashSet<>(Set.of(role)));
        return userMapper.toDto(userRepository.save(user));
    }
}
