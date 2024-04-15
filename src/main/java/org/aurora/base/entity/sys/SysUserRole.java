package org.aurora.base.entity.sys;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;

/**
 * 系统用户和系统角色的关系表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"user", "role"})
@EqualsAndHashCode(callSuper = true, exclude = {"user", "role"})
@Entity
@Table(name = "t_system_user_role", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class SysUserRole extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private SysUser user;

    @Basic(optional = false)
    @Column(name = "role_id")
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private SysRole role;
}
