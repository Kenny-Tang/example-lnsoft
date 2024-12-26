package com.redjujubetree.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Table to store alarm messages and service response time rules.
 * </p>
 *
 * @author kenny
 * @since 2024-12-24
 */
@Getter
@Setter
@TableName("sw_alarm_message")
public class SwAlarmMessage {

    /**
     * Primary key ID for the alarm message, manually assigned.
     */
    private Long id;

    /**
     * Scope identifier. Defined in org.apache.skywalking.oap.server.core.source.DefaultScopeDefine.
     */
    private Integer scopeId;

    /**
     * Scope type. All scopes are defined in DefaultScopeDefine.
     */
    private String scope;

    /**
     * Target scope entity name. Follow entity name definitions.
     */
    private String name;

    /**
     * ID of the scope entity matching with the name. For relation scopes, this is the source entity ID.
     */
    private String id0;

    /**
     * Destination entity ID for relation scopes. Empty for other scopes.
     */
    private String id1;

    /**
     * Rule name as configured in alarm-settings.yml.
     */
    private String ruleName;

    /**
     * Alarm text message.
     */
    private String alarmMessage;

    /**
     * Alarm time in milliseconds since January 1, 1970 UTC.
     */
    private Long startTime;

    /**
     * Alarm level, e.g., CRITICAL, MAJOR, etc.
     */
    private String level;

    /**
     * Record creation timestamp.
     */
    private Date createTime;

    /**
     * Record last update timestamp.
     */
    private Date updateTime;

    /**
     * Record version number, used for optimistic locking.
     */
    @Version
    private Integer version;

    /**
     * Username or identifier of the last person who updated the record.
     */
    private String updatedBy;
}
