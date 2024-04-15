package org.aurora.base.entity.sys;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;

/**
 * 系统生成代码表
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"menu", "table"})
@EqualsAndHashCode(callSuper = true, exclude = {"menu", "table"})
@Entity
@Table(name = "t_system_generate_code", uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "table_id"}))
public class SysGenerateCode extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private SysMenu menu;

    @Basic(optional = false)
    @Column(name = "table_id")
    private Long tableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", insertable = false, updatable = false)
    private SysTable table;
}
