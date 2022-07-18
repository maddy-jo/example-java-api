package gov.va.starter.example.service.provider.subfacilityvisit.mapper;

import gov.va.starter.example.persistence.model.SubFacilityVisitEntity;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SubFacilityVisitEntityMapper {

  @Mapping(target = "facilityVisitId", ignore = true)
  SubFacilityVisitEntity toEntity(SubFacilityVisit src);

  default Optional<SubFacilityVisitEntity> toEntity(Optional<SubFacilityVisit> src) {
    return Optional.ofNullable(toEntity(src.orElse(null)));
  }

  List<SubFacilityVisitEntity> toEntityList(List<SubFacilityVisit> src);

  SubFacilityVisit toModel(SubFacilityVisitEntity src);

  default Optional<SubFacilityVisit> toModel(Optional<SubFacilityVisitEntity> src) {
    return Optional.ofNullable(toModel(src.orElse(null)));
  }

  default Page<SubFacilityVisit> toModelPage(Page<SubFacilityVisitEntity> src) {
    return src.map(this::toModel);
  }

  List<SubFacilityVisit> toModelList(Iterable<SubFacilityVisitEntity> src);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "facilityVisitId", ignore = true)
  SubFacilityVisitEntity updateMetadata(
      SubFacilityVisit src, @MappingTarget SubFacilityVisitEntity dst);
}
