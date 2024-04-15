package org.aurora.base.entity.sys;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;

/**
 * 系统数据字典表
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_system_dict", uniqueConstraints = @UniqueConstraint(columnNames = {"dict_code", "dict_key"}))
public class SysDict extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "dict_code", length = 50)
    private String dictCode;

    @Basic(optional = false)
    @Column(name = "dict_key", length = 20)
    private String dictKey;

    @Basic(optional = false)
    @Column(name = "dict_value", length = 50)
    private String dictValue;

    @Basic(optional = false)
    @Column(name = "order_by", length = 20)
    private String orderBy;

    @Basic(optional = false)
    @Column(length = 20)
    private String status;
}
