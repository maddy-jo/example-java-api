package gov.va.starter.example.service.spi.facilityvisit;

import gov.va.starter.boot.exception.RequestValidationException;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
import gov.va.starter.example.service.spi.facilityvisit.model.SubFacilityVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FacilityVisitService {

  FacilityVisit add(FacilityVisit resource) throws RequestValidationException;

  Page<FacilityVisit> findByLastName(String lastName, Pageable pageable);

  Optional<FacilityVisit> findByUserName(String userName);

  Optional<FacilityVisit> findById(String id);

  Page<FacilityVisit> findAll(Pageable pageable);

  Optional<FacilityVisit> updateById(String id, FacilityVisit record)
      throws RequestValidationException;

  Optional<FacilityVisit> deleteById(String id);

  SubFacilityVisit addSubFacilityVisit(String id, SubFacilityVisit subResource)
      throws RequestValidationException;

  Page<SubFacilityVisit> getSubFacilityVisits(String id, Pageable pageable);

  Optional<SubFacilityVisit> getSubFacilityVisit(String id, String subResourceId);

  // CSOFF: LineLength
  Optional<SubFacilityVisit> updateSubFacilityVisit(
      String id, String subResourceId, SubFacilityVisit subResource)
      throws RequestValidationException;
  // CSON: LineLength

  Optional<SubFacilityVisit> deleteSubFacilityVisit(String id, String subResourceId);
}
