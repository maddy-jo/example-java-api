package gov.va.starter.example.controller.facilityvisit.mapper;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.starter.example.api.facilityvisit.requests.FacilityVisitRequest;
import gov.va.starter.example.api.facilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.facilityvisit.responses.FacilityVisitResponse;
import gov.va.starter.example.api.facilityvisit.responses.SubFacilityVisitResponse;
import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.facilityvisit.factory.FacilityVisitFactory;
import gov.va.starter.example.facilityvisit.model.FacilityVisitData;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
import gov.va.starter.example.service.spi.facilityvisit.model.SubFacilityVisit;
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

public class FacilityVisitRequestMapperTest {

  private FacilityVisitRequestMapper mapper;
  private FacilityVisitFactory resourceFactory = new FacilityVisitFactory();
  private FacilityVisitData defaultResourceData = resourceFactory.createBySpec(DEFAULT_SPEC);
  private SubFacilityVisitFactory subResourceFactory = new SubFacilityVisitFactory();
  private SubFacilityVisitData defaultSubResourceData =
      subResourceFactory.createBySpec(DEFAULT_SPEC);

  @BeforeEach
  public void setup() {
    mapper = Mappers.getMapper(FacilityVisitRequestMapper.class);
  }

  @Test
  public void mapperNewResourceTest() {
    FacilityVisitRequest resource = createResourceRequest();

    FacilityVisit response = mapper.toModel(resource);

    verifyResource(response);
  }

  @Test
  public void mapperResourceResponseTest() {
    FacilityVisit resource = createResource(defaultResourceData.getId());

    FacilityVisitResponse response = mapper.toFacilityVisitResponse(resource);

    verifyResourceResponse(response);
  }

  @Test
  public void mapperOptionalTest() {
    Optional<FacilityVisit> resource = Optional.of(createResource(defaultResourceData.getId()));

    FacilityVisitResponse response = mapper.toFacilityVisitResponse(resource);

    assertThat(response).isNotNull();
    verifyResourceResponse(response);
  }

  @Test
  public void mapperOptionalNullTest() {
    Optional<FacilityVisit> resource = Optional.ofNullable(null);

    FacilityVisitResponse response = mapper.toFacilityVisitResponse(resource);

    assertThat(response).isNull();
  }

  @Test
  public void mapperOptionalEmptyTest() {
    Optional<FacilityVisit> resource = Optional.empty();

    FacilityVisitResponse response = mapper.toFacilityVisitResponse(resource);

    assertThat(response).isNull();
  }

  @Test
  public void mapperEntityListTest() {
    List<FacilityVisit> resources =
        Arrays.asList(
            createResource(defaultResourceData.getId()),
            createResource(defaultResourceData.getId()));

    List<FacilityVisitResponse> response = mapper.toFacilityVisitResponseList(resources);

    assertThat(response.size()).isEqualTo(2);
    verifyResourceResponse(response.get(0));
    verifyResourceResponse(response.get(1));
  }

  @Test
  public void mapperEntityPageTest() {
    Pageable pageable = PageRequest.of(0, 1);
    Page<FacilityVisit> resources =
        new PageImpl<>(Arrays.asList(createResource(defaultResourceData.getId())), pageable, 100);
    PagedResponse<FacilityVisitResponse> response = mapper.toFacilityVisitResponsePage(resources);

    assertThat(response.getItems().size()).isEqualTo(1);
    assertThat(response.getTotalItems()).isEqualTo(100);
    assertThat(response.getPageNumber()).isEqualTo(0);
    assertThat(response.getPageSize()).isEqualTo(1);
    assertThat(response.getTotalPages()).isEqualTo(100);
    verifyResourceResponse(response.getItems().get(0));
  }

  @Test
  public void mapperNewSubResourceTest() {
    SubFacilityVisitRequest resource = createSubResourceRequest();

    SubFacilityVisit response = mapper.toModel(resource);

    verifySubResource(response);
  }

  @Test
  public void mapperSubResourceResponseTest() {
    SubFacilityVisit resource = createSubResource(defaultSubResourceData.getId());

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    verifySubResourceResponse(response);
  }

  @Test
  public void mapperOptionalSubResourceTest() {
    Optional<SubFacilityVisit> resource =
        Optional.of(createSubResource(defaultSubResourceData.getId()));

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    assertThat(response).isNotNull();
    verifySubResourceResponse(response);
  }

  @Test
  public void mapperOptionalSubResourceNullTest() {
    Optional<SubFacilityVisit> resource = Optional.ofNullable(null);

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    assertThat(response).isNull();
  }

  @Test
  public void mapperOptionalSubResourceEmptyTest() {
    Optional<SubFacilityVisit> resource = Optional.empty();

    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(resource);

    assertThat(response).isNull();
  }

  @Test
  public void mapperSubEntityListTest() {
    List<SubFacilityVisit> resources =
        Arrays.asList(
            createSubResource(defaultSubResourceData.getId()),
            createSubResource(defaultSubResourceData.getId()));

    List<SubFacilityVisitResponse> response = mapper.toSubFacilityVisitResponseList(resources);

    assertThat(response.size()).isEqualTo(2);
    verifySubResourceResponse(response.get(0));
    verifySubResourceResponse(response.get(1));
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
        defaultResourceData.getUserName(),
        defaultResourceData.getPii(),
        defaultResourceData.getFirstName(),
        defaultResourceData.getLastName());
  }

  /**
   * convenience function to create subresource object.
   *
   * @param id whether to create with identifier (null if not)
   * @return SubFacilityVisit object
   */
  private SubFacilityVisit createSubResource(String id) {
    return new SubFacilityVisit(
        id,
        defaultSubResourceData.getUserName(),
        defaultSubResourceData.getFirstName(),
        defaultSubResourceData.getLastName());
  }

  /**
   * convenience function to create resource request object.
   *
   * @return FacilityVisitRequest object
   */
  private FacilityVisitRequest createResourceRequest() {
    return new FacilityVisitRequest(
        defaultResourceData.getUserName(),
        defaultResourceData.getPii(),
        defaultResourceData.getFirstName(),
        defaultResourceData.getLastName());
  }

  /**
   * convenience function to create subresource request object.
   *
   * @return SubFacilityVisitRequest object
   */
  private SubFacilityVisitRequest createSubResourceRequest() {
    return new SubFacilityVisitRequest(
        defaultSubResourceData.getUserName(),
        defaultSubResourceData.getFirstName(),
        defaultSubResourceData.getLastName());
  }

  /**
   * helper function to validate standard values.
   *
   * @param resource the object to validate
   */
  protected void verifyResource(FacilityVisit resource) {
    assertThat(resource.getUserName().equals(defaultResourceData.getUserName()));
    assertThat(resource.getPii().equals(defaultResourceData.getPii()));
    assertThat(resource.getFirstName().equals(defaultResourceData.getFirstName()));
    assertThat(resource.getLastName().equals(defaultResourceData.getLastName()));
    assertThat(resource.getId()).isNotEqualTo(defaultResourceData.getId());
  }

  /**
   * helper function to validate standard values.
   *
   * @param resource the object to validate
   */
  protected void verifySubResource(SubFacilityVisit resource) {
    assertThat(resource.getUserName().equals(defaultSubResourceData.getUserName()));
    assertThat(resource.getFirstName().equals(defaultSubResourceData.getFirstName()));
    assertThat(resource.getLastName().equals(defaultSubResourceData.getLastName()));
    assertThat(resource.getId()).isNotEqualTo(defaultSubResourceData.getId());
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  private void verifyResourceResponse(FacilityVisitResponse response) {
    assertThat(response.getUserName().equals(defaultResourceData.getUserName()));
    assertThat(response.getPii().equals(defaultResourceData.getPii()));
    assertThat(response.getFullName().equals(defaultResourceData.getFullName()));
    assertThat(response.getId()).isEqualTo(defaultResourceData.getId());
  }

  /**
   * helper function to validate standard values.
   *
   * @param response the object to validate
   */
  protected void verifySubResourceResponse(SubFacilityVisitResponse response) {
    assertThat(response.getId()).isEqualTo(defaultSubResourceData.getId());
  }
}
