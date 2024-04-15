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
 * 系统用户表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = "userRoles")
@EqualsAndHashCode(callSuper = true, exclude = "userRoles")
@Entity
@Table(name = "t_system_user")
public class SysUser extends BaseEntity {

    @Basic(optional = false)
    @Column(length = 50, unique = true)
    private String username;

    @Column(length = 64)
    private String password;

    @Column(length = 32)
    private String salt;

    @Basic(optional = false)
    @Column(length = 50)
    private String nickname;

    @Column(length = 20)
    private String sex;

    @Column(length = 50)
    private String email;

    @Column(name = "mobile_phone_number", length = 11, unique = true)
    private String mobilePhoneNumber;

    @Column(name = "avatar_image_path")
    private String avatarImagePath;

    @Basic(optional = false)
    @Column(length = 20)
    private String status;

    @Basic(optional = false)
    @Column(name = "user_type", length = 20)
    private String userType;

    @Basic(optional = false)
    @Column(name = "is_deleted", length = 20)
    private String isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysUserRole> userRoles = new HashSet<>();
}
