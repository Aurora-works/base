package org.aurora.base.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.jackson.JsonSysUserSerializer;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.enums.TodoUser;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"createUser", "lastUser"})
@EqualsAndHashCode(exclude = {"createUser", "lastUser", "id"})
@MappedSuperclass
// public class BaseEntity implements Serializable {
public abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "hibernate_default_generator")
    @GenericGenerator(name = "hibernate_default_generator")
    private Long id;

    @Column
    private String description;

    @Basic(optional = false)
    @Column(name = "create_time", precision = 3, updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Basic(optional = false)
    @Column(name = "create_user_id", updatable = false)
    private Long createUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id", insertable = false, updatable = false)
    @JsonSerialize(using = JsonSysUserSerializer.class)
    private SysUser createUser;

    @Column(name = "last_time", precision = 3, insertable = false)
    @UpdateTimestamp
    private LocalDateTime lastTime;

    @Column(name = "last_user_id", insertable = false)
    private Long lastUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_user_id", insertable = false, updatable = false)
    @JsonSerialize(using = JsonSysUserSerializer.class)
    private SysUser lastUser;

    @Version
    private Integer version;

    @PrePersist
    private void prePersist() {
        Long currentUserId = ShiroUtils.getCurrentUserId();
        if (TodoUser.USER_NO_LOGIN.getUserId().equals(currentUserId)) {
            if (this.getCreateUserId() != null) {
                return;
            }
        }
        this.setCreateUserId(currentUserId);
    }

    @PreUpdate
    private void PreUpdate() {
        this.setLastUserId(ShiroUtils.getCurrentUserId());
    }
}
