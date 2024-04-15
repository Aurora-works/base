package org.aurora.base.entity.sys;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;

/**
 * 系统数据库表单字段表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"table", "foreignTable"})
@EqualsAndHashCode(callSuper = true, exclude = {"table", "foreignTable"})
@Entity
@Table(name = "t_system_table_column")
public class SysTableColumn extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "table_id")
    private Long tableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", insertable = false, updatable = false)
    private SysTable table;

    @Basic(optional = false)
    @Column(name = "column_name", length = 50)
    private String columnName;

    @Basic(optional = false)
    @Column(name = "display_name", length = 50)
    private String displayName;

    @Basic(optional = false)
    @Column(name = "entity_name", length = 50)
    private String entityName;

    @Basic(optional = false)
    @Column(name = "column_type", length = 20)
    private String columnType;

    @Column(name = "column_length", length = 50)
    private String columnLength;

    @Basic(optional = false)
    @Column(name = "is_primary", length = 20)
    private String isPrimary;

    @Basic(optional = false)
    @Column(name = "is_nullable", length = 20)
    private String isNullable;

    @Basic(optional = false)
    @Column(name = "is_unique", length = 20)
    private String isUnique;

    @Basic(optional = false)
    @Column(name = "is_insertable", length = 20)
    private String isInsertable;

    @Basic(optional = false)
    @Column(name = "is_updatable", length = 20)
    private String isUpdatable;

    @Basic(optional = false)
    @Column(name = "order_by", length = 20)
    private String orderBy;

    @Column(name = "foreign_table_id")
    private Long foreignTableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreign_table_id", insertable = false, updatable = false)
    private SysTable foreignTable;

    @Column(name = "dict_code", length = 50)
    private String dictCode;
}
