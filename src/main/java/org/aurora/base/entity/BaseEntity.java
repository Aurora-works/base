package org.aurora.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

    @Setter(value = AccessLevel.PRIVATE)
    @Id
    @GeneratedValue(generator = "hibernate_default_generator")
    @GenericGenerator(name = "hibernate_default_generator")
    private Long id;

    @Column
    private String description;

    @Setter(value = AccessLevel.PRIVATE)
    @Basic(optional = false)
    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createTime;

    @Setter(value = AccessLevel.PRIVATE)
    @Basic(optional = false)
    @Column(name = "create_user_id", updatable = false)
    private Long createUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id", insertable = false, updatable = false)
    @JsonSerialize(using = JsonSysUserSerializer.class)
    private SysUser createUser;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "last_time", insertable = false)
    @UpdateTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastTime;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "last_user_id", insertable = false)
    private Long lastUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_user_id", insertable = false, updatable = false)
    @JsonSerialize(using = JsonSysUserSerializer.class)
    private SysUser lastUser;

    @Setter(value = AccessLevel.PRIVATE)
    @Version
    private Integer version;

    @PrePersist
    private void prePersist() {
        this.setCreateUserId(ShiroUtils.getCurrentUserId());
    }

    @PreUpdate
    private void PreUpdate() {
        this.setLastUserId(ShiroUtils.getCurrentUserId());
    }
}
