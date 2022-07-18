package gov.va.starter.example.service.spi.subfacilityvisit;

import gov.va.starter.boot.exception.RequestValidationException;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubFacilityVisitService {

  SubFacilityVisit add(SubFacilityVisit resource) throws RequestValidationException;

  Page<SubFacilityVisit> findByLastName(String lastName, Pageable pageable);

  Optional<SubFacilityVisit> findByUserName(String userName);

  Optional<SubFacilityVisit> findById(String id);

  Page<SubFacilityVisit> findAll(Pageable pageable);

  Optional<SubFacilityVisit> updateById(String id, SubFacilityVisit record)
      throws RequestValidationException;

  Optional<SubFacilityVisit> deleteById(String id);
}
