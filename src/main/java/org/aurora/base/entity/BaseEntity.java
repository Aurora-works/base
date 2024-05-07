package org.aurora.base.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.jackson.JsonSysUserSerializer;
import org.aurora.base.shiro.ShiroUtils;
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

    @Setter(value = AccessLevel.PRIVATE)
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

    @Setter(value = AccessLevel.PRIVATE)
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
        if (this.getCreateUserId() == null) {
            this.setCreateUserId(ShiroUtils.getCurrentUserId());
        }
    }

    @PreUpdate
    private void PreUpdate() {
        if (this.getLastUserId() == null) {
            this.setLastUserId(ShiroUtils.getCurrentUserId());
        }
    }
}
