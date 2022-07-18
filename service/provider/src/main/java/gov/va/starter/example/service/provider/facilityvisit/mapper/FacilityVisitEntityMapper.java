package gov.va.starter.example.service.provider.facilityvisit.mapper;

import gov.va.starter.example.persistence.model.FacilityVisitEntity;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntity;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
import gov.va.starter.example.service.spi.facilityvisit.model.SubFacilityVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface FacilityVisitEntityMapper {

  FacilityVisitEntity toEntity(FacilityVisit src);

  default Optional<FacilityVisitEntity> toEntity(Optional<FacilityVisit> src) {
    return Optional.ofNullable(toEntity(src.orElse(null)));
  }

  List<FacilityVisitEntity> toEntityList(List<FacilityVisit> src);

  FacilityVisit toModel(FacilityVisitEntity src);

  default Optional<FacilityVisit> toModel(Optional<FacilityVisitEntity> src) {
    return Optional.ofNullable(toModel(src.orElse(null)));
  }

  default Page<FacilityVisit> toModelPage(Page<FacilityVisitEntity> src) {
    return src.map(this::toModel);
  }

  List<FacilityVisit> toModelList(Iterable<FacilityVisitEntity> src);

  @Mapping(target = "id", ignore = true)
  FacilityVisitEntity updateMetadata(FacilityVisit src, @MappingTarget FacilityVisitEntity dst);

  @Mapping(target = "pii", constant = "FIXME")
  @Mapping(target = "facilityVisitId", ignore = true)
  SubFacilityVisitEntity toSubFacilityVisitEntity(SubFacilityVisit src);

  default Optional<SubFacilityVisitEntity> toSubFacilityVisitEntity(
      Optional<SubFacilityVisit> src) {
    return Optional.ofNullable(toSubFacilityVisitEntity(src.orElse(null)));
  }

  List<SubFacilityVisitEntity> toSubFacilityVisitEntityList(List<SubFacilityVisit> src);

  SubFacilityVisit toSubFacilityVisitModel(SubFacilityVisitEntity src);

  default Optional<SubFacilityVisit> toSubFacilityVisitModel(Optional<SubFacilityVisitEntity> src) {
    return Optional.ofNullable(toSubFacilityVisitModel(src.orElse(null)));
  }

  default Page<SubFacilityVisit> toSubFacilityVisitModelPage(Page<SubFacilityVisitEntity> src) {
    return src.map(this::toSubFacilityVisitModel);
  }

  List<SubFacilityVisit> toSubFacilityVisitModelList(Iterable<SubFacilityVisitEntity> src);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pii", ignore = true)
  @Mapping(target = "facilityVisitId", ignore = true)
  SubFacilityVisitEntity updateSubFacilityVisitMetadata(
      SubFacilityVisit src, @MappingTarget SubFacilityVisitEntity dst);
}
