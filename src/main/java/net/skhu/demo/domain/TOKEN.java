package net.skhu.demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ds on 2018-04-10.
 */

@Data
@Entity
public class TOKEN implements Serializable {

    private static final long serialVersionUID = 839035049585559894L;

    @Id
    private String username;
    private String series;
    private String token;
    private Date lastUsed;
}
