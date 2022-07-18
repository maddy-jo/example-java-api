package gov.va.starter.example.persistence.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface SubFacilityVisitEntityRepository
    extends PagingAndSortingRepository<SubFacilityVisitEntity, String> {

  Optional<SubFacilityVisitEntity> findByUserName(String userName);

  Page<SubFacilityVisitEntity> findByLastName(String lastName, Pageable pageable);

  Page<SubFacilityVisitEntity> findAllByFacilityVisitId(String facilityVisitId, Pageable pageable);
}
