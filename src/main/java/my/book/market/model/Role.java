package my.book.market.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Override
    public String getAuthority() {
        return roleName.name();
    }

    public enum RoleName {
        USER,
        ADMIN
    }
}
