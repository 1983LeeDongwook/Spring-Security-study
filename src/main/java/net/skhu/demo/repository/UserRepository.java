package net.skhu.demo.repository;

import net.skhu.demo.domain.USER;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ds on 2018-04-09.
 */
public interface UserRepository extends CrudRepository<USER, String> {
}
