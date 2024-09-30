package org.aurora.base.entity.sys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.aurora.base.entity.BaseEntity;
import org.hibernate.Length;

/**
 * 系统操作日志表
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_system_request_log")
public class SysRequestLog extends BaseEntity {

    @Column(name = "request_controller")
    private String requestController;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "request_ip", length = 50)
    private String requestIp;

    @Column(name = "request_parameters", length = Length.LONG)
    private String requestParameters;
}
