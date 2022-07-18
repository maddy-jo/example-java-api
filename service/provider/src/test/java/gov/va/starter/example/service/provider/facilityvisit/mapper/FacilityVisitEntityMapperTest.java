package gov.va.starter.example.service.provider.facilityvisit.mapper;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.starter.example.facilityvisit.factory.FacilityVisitFactory;
import gov.va.starter.example.facilityvisit.model.FacilityVisitData;
import gov.va.starter.example.persistence.model.FacilityVisitEntity;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
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

public class FacilityVisitEntityMapperTest {

  private FacilityVisitFactory resourceFactory = new FacilityVisitFactory();
  private FacilityVisitData resourceData = resourceFactory.createBySpec(DEFAULT_SPEC);
  private FacilityVisitEntityMapper mapper;

  @BeforeEach
  public void setup() {
    mapper = Mappers.getMapper(FacilityVisitEntityMapper.class);
  }

  @Test
  public void mapperNewResourceTest() {
    FacilityVisit resource = createResource(null);

    FacilityVisitEntity response = mapper.toEntity(resource);

    verifyResourceEntity(response, false);
  }

  @Test
  public void mapperResourceTest() {
    FacilityVisit resource = createResource(resourceData.getId());

    FacilityVisitEntity response = mapper.toEntity(resource);

    verifyResourceEntity(response);
  }

  @Test
  public void mapperEntityTest() {
    FacilityVisitEntity entity = createResourceEntity();

    FacilityVisit response = mapper.toModel(entity);

    verifyResource(response);
  }

  @Test
  public void mapperOptionalEntityTest() {
    Optional<FacilityVisitEntity> entity = Optional.of(createResourceEntity());

    Optional<FacilityVisit> response = mapper.toModel(entity);

    assertThat(response.isPresent());
    verifyResource(response.get());
  }

  @Test
  public void mapperOptionalTest() {
    Optional<FacilityVisit> resource = Optional.of(createResource(null));

    Optional<FacilityVisitEntity> response = mapper.toEntity(resource);

    assertThat(response.isPresent());
    verifyResourceEntity(response.get(), false);
  }

  @Test
  public void mapperOptionalNullTest() {
    Optional<FacilityVisit> resource = Optional.ofNullable(null);

    Optional<FacilityVisitEntity> response = mapper.toEntity(resource);

    assertThat(response.isEmpty());
  }

  @Test
  public void mapperOptionalEmptyTest() {
    Optional<FacilityVisit> resource = Optional.empty();

    Optional<FacilityVisitEntity> response = mapper.toEntity(resource);

    assertThat(response.isEmpty());
  }

  @Test
  public void mapperEntityListTest() {
    List<FacilityVisitEntity> entities =
        Arrays.asList(createResourceEntity(), createResourceEntity());

    List<FacilityVisit> response = mapper.toModelList(entities);

    assertThat(response.size()).isEqualTo(2);
    verifyResource(response.get(0));
    verifyResource(response.get(1));
  }

  @Test
  public void mapperEntityPageTest() {
    Pageable pageable = PageRequest.of(0, 3);
    Page<FacilityVisitEntity> entities =
        new PageImpl<>(
            Arrays.asList(createResourceEntity(), createResourceEntity(), createResourceEntity()),
            pageable,
            100);

    Page<FacilityVisit> response = mapper.toModelPage(entities);

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
   * @return FacilityVisit object
   */
  private FacilityVisit createResource(String id) {
    return new FacilityVisit(
        id,
        resourceData.getUserName(),
        resourceData.getPii(),
        resourceData.getFirstName(),
        resourceData.getLastName());
  }

  /**
   * convenience function to create resource entity object.
   *
   * @return FacilityVisitEntity object
   */
  private FacilityVisitEntity createResourceEntity() {
    return new FacilityVisitEntity(
        resourceData.getId(),
        resourceData.getUserName(),
        resourceData.getPii(),
        resourceData.getFirstName(),
        resourceData.getLastName());
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  protected void verifyResource(FacilityVisit response) {
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
  private void verifyResourceEntity(FacilityVisitEntity response) {
    verifyResourceEntity(response, true);
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  // CSOFF: LineLength
  private void verifyResourceEntity(FacilityVisitEntity response, boolean hasId) {
    // CSON: LineLength
    assertThat(response.getUserName()).isEqualTo(resourceData.getUserName());
    assertThat(response.getPii()).isEqualTo(resourceData.getPii());
    assertThat(response.getFirstName()).isEqualTo(resourceData.getFirstName());
    assertThat(response.getLastName()).isEqualTo(resourceData.getLastName());
    if (hasId) {
      assertThat(response.getId()).isEqualTo(resourceData.getId());
    } else {
      assertThat(response.getId()).isNotEqualTo(resourceData.getId());
    }
  }
}
