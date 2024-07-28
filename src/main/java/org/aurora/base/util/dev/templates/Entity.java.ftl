package org.aurora.${projectName}.entity.${package1};

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;
<#if hasBigDecimal || hasLocalDate || hasLocalDateTime || hasLocalTime>

<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if hasLocalDate>
import java.time.LocalDate;
</#if>
<#if hasLocalDateTime>
import java.time.LocalDateTime;
</#if>
<#if hasLocalTime>
import java.time.LocalTime;
</#if>
</#if>

/**
* ${tableDesc}
*/
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "${tableName}")
public class ${entityName} extends BaseEntity {
<#list columns as column>

    <#if column.isNullable == "0">
    @Basic(optional = false)
    </#if>
    @Column(name = "${column.columnName}"<#if column.columnLength??
        ><#if column.columnType == "BigDecimal">, precision = ${column.columnLength?split(",")?first}, scale = ${column.columnLength?split(",")?last}<#else>, length = ${column.columnLength}</#if></#if
        ><#if column.isUnique == "1">, unique = true</#if
        ><#if column.isInsertable == "0">, insertable = false</#if
        ><#if column.isUpdatable == "0">, updatable = false</#if>)
    private ${column.columnType} ${column.entityName};
    <#if column.foreignTableId??>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "${column.columnName}", insertable = false, updatable = false)
    private ${column.foreignTable.entityName} ${column.foreignTable.entityName?substring(0,1)?c_lower_case}${column.foreignTable.entityName?substring(1)};
    </#if>
</#list>
}
