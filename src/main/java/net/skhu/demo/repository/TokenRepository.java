package net.skhu.demo.repository;

import net.skhu.demo.domain.TOKEN;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by ds on 2018-04-10.
 */

public interface TokenRepository extends CrudRepository<TOKEN, String> {
    Optional<TOKEN> findBySeries(final String series);
}
