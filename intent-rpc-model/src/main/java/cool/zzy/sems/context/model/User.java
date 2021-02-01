package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/29 19:18
 * @since 1.0
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -2826150668872375625L;
    private Integer id;
    private Long created;
    private Long modified;
    private String ukEmail;
    private String passwordHash;
    private String nickname;
    private Boolean gender;
    private String ip;
    private Boolean delete;
}
