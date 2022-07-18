package gov.va.starter.example.service.provider.subfacilityvisit.mapper;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.starter.example.facilityvisit.factory.FacilityVisitFactory;
import gov.va.starter.example.facilityvisit.model.FacilityVisitData;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntity;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
import gov.va.starter.example.subfacilityvisit.factory.SubFacilityVisitFactory;
import gov.va.starter.example.subfacilityvisit.model.SubFacilityVisitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SubFacilityVisitEntityMapperTest {

  private SubFacilityVisitFactory resourceFactory = new SubFacilityVisitFactory();
  private SubFacilityVisitData resourceData = resourceFactory.createBySpec(DEFAULT_SPEC);
  private FacilityVisitFactory parentFactory = new FacilityVisitFactory();
  private FacilityVisitData parentData = parentFactory.createBySpec(DEFAULT_SPEC);
  private SubFacilityVisitEntityMapper mapper;

  @BeforeEach
  public void setup() {
    mapper = Mappers.getMapper(SubFacilityVisitEntityMapper.class);
  }

  @Test
  public void mapperNewResourceTest() {
    SubFacilityVisit resource = createResource(null);

    SubFacilityVisitEntity response = mapper.toEntity(resource);

    verifyResourceEntity(response, false, false);
  }

  @Test
  public void mapperResourceTest() {
    SubFacilityVisit resource = createResource(resourceData.getId());

    SubFacilityVisitEntity response = mapper.toEntity(resource);

    verifyResourceEntity(response, true, false);
  }

  @Test
  public void mapperEntityTest() {
    SubFacilityVisitEntity entity = createResourceEntity();

    SubFacilityVisit response = mapper.toModel(entity);

    verifyResource(response);
  }

  @Test
  public void mapperOptionalEntityTest() {
    Optional<SubFacilityVisitEntity> entity = Optional.of(createResourceEntity());

    Optional<SubFacilityVisit> response = mapper.toModel(entity);

    assertThat(response.isPresent());
    verifyResource(response.get());
  }

  @Test
  public void mapperOptionalTest() {
    Optional<SubFacilityVisit> resource = Optional.of(createResource(null));

    Optional<SubFacilityVisitEntity> response = mapper.toEntity(resource);

    assertThat(response.isPresent());
    verifyResourceEntity(response.get(), false, false);
  }

  @Test
  public void mapperOptionalNullTest() {
    Optional<SubFacilityVisit> resource = Optional.ofNullable(null);

    Optional<SubFacilityVisitEntity> response = mapper.toEntity(resource);

    assertThat(response.isEmpty());
  }

  @Test
  public void mapperOptionalEmptyTest() {
    Optional<SubFacilityVisit> resource = Optional.empty();

    Optional<SubFacilityVisitEntity> response = mapper.toEntity(resource);

    assertThat(response.isEmpty());
  }

  @Test
  public void mapperEntityListTest() {
    List<SubFacilityVisitEntity> entities =
        Arrays.asList(createResourceEntity(), createResourceEntity());

    List<SubFacilityVisit> response = mapper.toModelList(entities);

    assertThat(response.size()).isEqualTo(2);
    verifyResource(response.get(0));
    verifyResource(response.get(1));
  }

  @Test
  public void mapperEntityPageTest() {
    Pageable pageable = PageRequest.of(0, 3);
    Page<SubFacilityVisitEntity> entities =
        new PageImpl<>(
            Arrays.asList(createResourceEntity(), createResourceEntity(), createResourceEntity()),
            pageable,
            100);

    Page<SubFacilityVisit> response = mapper.toModelPage(entities);

    assertThat(response.getContent().size()).isEqualTo(3);
    assertThat(response.getTotalElements()).isEqualTo(100);
    assertThat(response.getNumber()).isEqualTo(0);
    assertThat(response.getNumberOfElements()).isEqualTo(3);

    verifyResource(response.toList().get(0));
    verifyResource(response.toList().get(1));
    verifyResource(response.toList().get(2));
  }

  /**
   * convenience function to create resource object.
   *
   * @param id whether to create with identifier (null if not)
   * @return SubFacilityVisit object
   */
  private SubFacilityVisit createResource(String id) {
    return new SubFacilityVisit(
        id,
        resourceData.getUserName(),
        resourceData.getPii(),
        resourceData.getFirstName(),
        resourceData.getLastName());
  }

  /**
   * convenience function to create resource entity object.
   *
   * @return SubFacilityVisitEntity object
   */
  private SubFacilityVisitEntity createResourceEntity() {
    return new SubFacilityVisitEntity(
        resourceData.getId(),
        resourceData.getUserName(),
        resourceData.getPii(),
        resourceData.getFirstName(),
        resourceData.getLastName(),
        parentData.getId());
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  protected void verifyResource(SubFacilityVisit response) {
    assertThat(response.getUserName()).isEqualTo(resourceData.getUserName());
    assertThat(response.getPii()).isEqualTo(resourceData.getPii());
    assertThat(response.getFirstName()).isEqualTo(resourceData.getFirstName());
    assertThat(response.getLastName()).isEqualTo(resourceData.getLastName());
    assertThat(response.getId()).isEqualTo(resourceData.getId());
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  private void verifyResourceEntity(SubFacilityVisitEntity response) {
    verifyResourceEntity(response, true, true);
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  // CSOFF: LineLength
  private void verifyResourceEntity(
      SubFacilityVisitEntity response, boolean hasId, boolean hasParentId) {
    // CSON: LineLength
    assertThat(response.getUserName()).isEqualTo(resourceData.getUserName());
    assertThat(response.getPii()).isEqualTo(resourceData.getPii());
    assertThat(response.getFirstName()).isEqualTo(resourceData.getFirstName());
    assertThat(response.getLastName()).isEqualTo(resourceData.getLastName());
    if (hasParentId) {
      assertThat(response.getFacilityVisitId()).isEqualTo(parentData.getId());
    } else {
      assertThat(response.getFacilityVisitId()).isNotEqualTo(parentData.getId());
    }
    if (hasId) {
      assertThat(response.getId()).isEqualTo(resourceData.getId());
    } else {
      assertThat(response.getId()).isNotEqualTo(resourceData.getId());
    }
  }
}
