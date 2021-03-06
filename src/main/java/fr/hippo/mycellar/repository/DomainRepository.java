package fr.hippo.mycellar.repository;

import fr.hippo.mycellar.domain.Appellation;
import fr.hippo.mycellar.domain.Domain;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Domain entity.
 */
public interface DomainRepository extends JpaRepository<Domain,Long> {

    @Query("from Domain d " + "left join fetch d.appellation")
    List<Domain> findAllWithAppellation();

    List<Domain> findAllByAppellation(Appellation appellation);
}
