package cool.zzy.sems.context.model;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/29 19:18
 * @since 1.0
 */
@Data
public class User {
    private Integer id;
    private OffsetDateTime created;
    private OffsetDateTime modified;
    private String ukEmail;
    private String passwordHash;
    private String nickname;
    private Boolean gender;
    private String ip;
    private Boolean delete;
}
