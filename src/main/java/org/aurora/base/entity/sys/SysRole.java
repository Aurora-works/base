package org.aurora.base.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * 系统角色表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"parentRole", "userRoles", "roleMenus", "childRoles"})
@EqualsAndHashCode(callSuper = true, exclude = {"parentRole", "userRoles", "roleMenus", "childRoles"})
@Entity
@Table(name = "t_system_role")
public class SysRole extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "role_code", length = 50, unique = true)
    private String roleCode;

    @Basic(optional = false)
    @Column(name = "role_name", length = 50)
    private String roleName;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonIgnore
    private SysRole parentRole;

    @Basic(optional = false)
    @Column(name = "order_by", length = 20)
    private String orderBy;

    @Basic(optional = false)
    @Column(length = 20)
    private String status;

    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysUserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysRoleMenu> roleMenus = new HashSet<>();

    @OneToMany(mappedBy = "parentRole", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysRole> childRoles = new HashSet<>();
}
