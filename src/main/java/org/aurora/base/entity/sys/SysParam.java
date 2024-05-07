package org.aurora.base.entity.sys;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;

/**
 * 系统参数表
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_system_param")
public class SysParam extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "param_code", length = 50, unique = true)
    private String paramCode;

    @Basic(optional = false)
    @Column(name = "param_desc", length = 50, unique = true)
    private String paramDesc;

    @Basic(optional = false)
    @Column(name = "param_value")
    private String paramValue;
}
