package net.skhu.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ds on 2018-04-10.
 */

@Data
@RedisHash("token")
public class Token implements Serializable {

    private static final long serialVersionUID = 839035049585559894L;

    @Id
    private String series;
    @Indexed
    private String username;
    private String token;
    private Date lastUsed;
}
