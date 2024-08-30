package org.aurora.base.entity.sys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.BaseEntity;
import org.hibernate.Length;

/**
 * 系统错误记录表
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_system_error_log")
public class SysErrorLog extends BaseEntity {

    @Column(name = "error_name")
    private String errorName;

    @Column(name = "error_stack_trace", length = Length.LONG)
    private String errorStackTrace;

    @Column(name = "error_controller")
    private String errorController;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "request_ip", length = 50)
    private String requestIp;

    @Column(name = "request_parameters", length = Length.LONG)
    private String requestParameters;
}
