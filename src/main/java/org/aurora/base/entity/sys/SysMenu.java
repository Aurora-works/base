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
 * 系统功能菜单表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"parentMenu", "roleMenus", "generateCodes", "childMenus"})
@EqualsAndHashCode(callSuper = true, exclude = {"parentMenu", "roleMenus", "generateCodes", "childMenus"})
@Entity
@Table(name = "t_system_menu")
public class SysMenu extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "menu_code", length = 50, unique = true)
    private String menuCode;

    @Basic(optional = false)
    @Column(name = "menu_name", length = 50)
    private String menuName;

    @Column
    private String href;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonIgnore
    private SysMenu parentMenu;

    @Basic(optional = false)
    @Column(name = "order_by", length = 20)
    private String orderBy;

    @Column
    private String css;

    @Basic(optional = false)
    @Column(name = "is_open", length = 20)
    private String isOpen;

    @Basic(optional = false)
    @Column(length = 20)
    private String status;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysRoleMenu> roleMenus = new HashSet<>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysGenerateCode> generateCodes = new HashSet<>();

    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysMenu> childMenus = new HashSet<>();
}
