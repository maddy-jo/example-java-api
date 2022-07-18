package gov.va.starter.example.controller.subfacilityvisit.mapper;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.api.subfacilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.subfacilityvisit.responses.SubFacilityVisitResponse;
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

public class SubFacilityVisitRequestMapperTest {

  private SubFacilityVisitRequestMapper mapper;
  private SubFacilityVisitFactory resourceFactory = new SubFacilityVisitFactory();
  private SubFacilityVisitData defaultResourceData = resourceFactory.createBySpec(DEFAULT_SPEC);

  @BeforeEach
  public void setup() {
    mapper = Mappers.getMapper(SubFacilityVisitRequestMapper.class);
  }

  @Test
  public void mapperNewResourceTest() {
    SubFacilityVisitRequest resource = createResourceRequest();

    SubFacilityVisit response = mapper.toModel(resource);

    verifyResource(response);
  }

  @Test
  public void mapperResourceResponseTest() {
    SubFacilityVisit resource = createResource(defaultResourceData.getId());

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    verifyResourceResponse(response);
  }

  @Test
  public void mapperOptionalTest() {
    Optional<SubFacilityVisit> resource = Optional.of(createResource(defaultResourceData.getId()));

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    assertThat(response).isNotNull();
    verifyResourceResponse(response);
  }

  @Test
  public void mapperOptionalNullTest() {
    Optional<SubFacilityVisit> resource = Optional.ofNullable(null);

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    assertThat(response).isNull();
  }

  @Test
  public void mapperOptionalEmptyTest() {
    Optional<SubFacilityVisit> resource = Optional.empty();

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    assertThat(response).isNull();
  }

  @Test
  public void mapperEntityListTest() {
    List<SubFacilityVisit> resources =
        Arrays.asList(
            createResource(defaultResourceData.getId()),
            createResource(defaultResourceData.getId()));

    List<SubFacilityVisitResponse> response = mapper.toSubFacilityVisitResponseList(resources);

    assertThat(response.size()).isEqualTo(2);
    verifyResourceResponse(response.get(0));
    verifyResourceResponse(response.get(1));
  }

  @Test
  public void mapperEntityPageTest() {
    Pageable pageable = PageRequest.of(0, 1);
    Page<SubFacilityVisit> resources =
        new PageImpl<>(Arrays.asList(createResource(defaultResourceData.getId())), pageable, 100);
    PagedResponse<SubFacilityVisitResponse> response =
        mapper.toSubFacilityVisitResponsePage(resources);

    assertThat(response.getItems().size()).isEqualTo(1);
    assertThat(response.getTotalItems()).isEqualTo(100);
    assertThat(response.getPageNumber()).isEqualTo(0);
    assertThat(response.getPageSize()).isEqualTo(1);
    assertThat(response.getTotalPages()).isEqualTo(100);
    verifyResourceResponse(response.getItems().get(0));
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
        defaultResourceData.getUserName(),
        defaultResourceData.getPii(),
        defaultResourceData.getFirstName(),
        defaultResourceData.getLastName());
  }

  /**
   * convenience function to create resource request object.
   *
   * @return SubFacilityVisitRequest object
   */
  private SubFacilityVisitRequest createResourceRequest() {
    return new SubFacilityVisitRequest(
        defaultResourceData.getUserName(),
        defaultResourceData.getPii(),
        defaultResourceData.getFirstName(),
        defaultResourceData.getLastName());
  }

  /**
   * helper function to validate standard values.
   *
   * @param resource the object to validate
   */
  protected void verifyResource(SubFacilityVisit resource) {
    assertThat(resource.getUserName().equals(defaultResourceData.getUserName()));
    assertThat(resource.getPii().equals(defaultResourceData.getPii()));
    assertThat(resource.getFirstName().equals(defaultResourceData.getFirstName()));
    assertThat(resource.getLastName().equals(defaultResourceData.getLastName()));
    assertThat(resource.getId()).isNotEqualTo(defaultResourceData.getId());
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  private void verifyResourceResponse(SubFacilityVisitResponse response) {
    assertThat(response.getUserName().equals(defaultResourceData.getUserName()));
    assertThat(response.getPii().equals(defaultResourceData.getPii()));
    assertThat(response.getFullName().equals(defaultResourceData.getFullName()));
    assertThat(response.getId()).isEqualTo(defaultResourceData.getId());
  }
}
