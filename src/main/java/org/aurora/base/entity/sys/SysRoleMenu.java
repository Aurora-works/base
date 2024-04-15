package org.aurora.base.entity.sys;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;

/**
 * 系统角色对系统功能菜单的操作权限表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"role", "menu"})
@EqualsAndHashCode(callSuper = true, exclude = {"role", "menu"})
@Entity
@Table(name = "t_system_role_menu", uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "menu_id"}))
public class SysRoleMenu extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "create_op", length = 20)
    private String createOp;

    @Basic(optional = false)
    @Column(name = "update_op", length = 20)
    private String updateOp;

    @Basic(optional = false)
    @Column(name = "delete_op", length = 20)
    private String deleteOp;

    @Basic(optional = false)
    @Column(name = "read_op", length = 20)
    private String readOp;

    @Basic(optional = false)
    @Column(name = "role_id")
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private SysRole role;

    @Basic(optional = false)
    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private SysMenu menu;
}
