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
 * 系统数据库表单表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"columns", "generateCodes"})
@EqualsAndHashCode(callSuper = true, exclude = {"columns", "generateCodes"})
@Entity
@Table(name = "t_system_table")
public class SysTable extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "table_desc", length = 50)
    private String tableDesc;

    @Basic(optional = false)
    @Column(name = "table_name", length = 50, unique = true)
    private String tableName;

    @Basic(optional = false)
    @Column(name = "entity_name", length = 50, unique = true)
    private String entityName;

    @Basic(optional = false)
    @Column(name = "is_real", length = 20)
    private String isReal;

    @OneToMany(mappedBy = "table", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysTableColumn> columns = new HashSet<>();

    @OneToMany(mappedBy = "table", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<SysGenerateCode> generateCodes = new HashSet<>();
}
