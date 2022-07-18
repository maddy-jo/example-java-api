package gov.va.starter.example.controller.subfacilityvisit.mapper;

import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.api.subfacilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.subfacilityvisit.responses.SubFacilityVisitResponse;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SubFacilityVisitRequestMapper {

  @Mapping(constant = "UNKNOWN_ID", target = "id")
  SubFacilityVisit toModel(SubFacilityVisitRequest request);

  @Mapping(
      target = "fullName",
      expression = "java(String.format(\"%s %s\",src.getFirstName(),src.getLastName()))")
  SubFacilityVisitResponse toSubFacilityVisitResponse(SubFacilityVisit src);

  default SubFacilityVisitResponse toSubFacilityVisitResponse(Optional<SubFacilityVisit> src) {
    return toSubFacilityVisitResponse(src.orElse(null));
  }

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

  List<SubFacilityVisitResponse> toSubFacilityVisitResponseList(List<SubFacilityVisit> src);
}
