package net.skhu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by ds on 2018-03-26.
 */

@Data
@Entity
public class USER implements Serializable{

    private static final long serialVersionUID = 1062898914988042848L;

    @Id
    private String id;
    private String pw;
    private String name;
}
