package cool.zzy.sems.context.model;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 15:43
 * @since 1.0
 */
@Data
public class Config {
    private OffsetDateTime created;
    private OffsetDateTime modified;
    private String passwordSalt;
    private Integer passwordHashCount;
    private Boolean delete;
}
