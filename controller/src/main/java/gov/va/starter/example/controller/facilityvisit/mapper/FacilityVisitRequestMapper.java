package gov.va.starter.example.controller.facilityvisit.mapper;

import gov.va.starter.example.api.facilityvisit.requests.FacilityVisitRequest;
import gov.va.starter.example.api.facilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.facilityvisit.responses.FacilityVisitResponse;
import gov.va.starter.example.api.facilityvisit.responses.SubFacilityVisitResponse;
import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
import gov.va.starter.example.service.spi.facilityvisit.model.SubFacilityVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface FacilityVisitRequestMapper {

  @Mapping(constant = "UNKNOWN_ID", target = "id")
  FacilityVisit toModel(FacilityVisitRequest request);

  @Mapping(constant = "UNKNOWN_ID", target = "id")
  SubFacilityVisit toModel(SubFacilityVisitRequest request);

  @Mapping(
      target = "fullName",
      expression = "java(String.format(\"%s %s\",src.getFirstName(),src.getLastName()))")
  FacilityVisitResponse toFacilityVisitResponse(FacilityVisit src);

  default FacilityVisitResponse toFacilityVisitResponse(Optional<FacilityVisit> src) {
    return toFacilityVisitResponse(src.orElse(null));
  }

  /**
   * convert to PagedResponse<>.
   *
   * @param src Page<> object
   * @return PagedResponse<>
   */
  default PagedResponse<FacilityVisitResponse> toFacilityVisitResponsePage(
      Page<FacilityVisit> src) {
    return new PagedResponse<>(
        toFacilityVisitResponseList(src.getContent()),
        src.getTotalPages(),
        src.getTotalElements(),
        src.getNumber(),
        src.getNumberOfElements());
  }

  List<FacilityVisitResponse> toFacilityVisitResponseList(List<FacilityVisit> src);

  SubFacilityVisitResponse toSubFacilityVisitResponse(SubFacilityVisit src);

  default SubFacilityVisitResponse toSubFacilityVisitResponse(Optional<SubFacilityVisit> src) {
    return toSubFacilityVisitResponse(src.orElse(null));
  }

  List<SubFacilityVisitResponse> toSubFacilityVisitResponseList(List<SubFacilityVisit> src);

  /**
   * convert to PagedResponse<>.
   *
   * @param src Page<> object
   * @return PagedResponse<>
   */
  default PagedResponse<SubFacilityVisitResponse> toSubFacilityVisitResponsePage(
      Page<SubFacilityVisit> src) {
    return new PagedResponse<>(
        toSubFacilityVisitResponseList(src.getContent()),
        src.getTotalPages(),
        src.getTotalElements(),
        src.getNumber(),
        src.getNumberOfElements());
  }
}
