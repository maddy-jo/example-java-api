package gov.va.starter.example.controller.facilityvisit;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.va.starter.boot.exception.ResourceNotFoundException;
import gov.va.starter.boot.notifier.lifecycle.entity.provider.MemoizedTimestampProvider;
import gov.va.starter.boot.notifier.lifecycle.entity.provider.NoopEntityLifecycleNotifier;
import gov.va.starter.boot.notifier.lifecycle.entity.spi.EntityLifecycleNotifier;
import gov.va.starter.example.api.facilityvisit.requests.FacilityVisitRequest;
import gov.va.starter.example.api.facilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.facilityvisit.responses.FacilityVisitResponse;
import gov.va.starter.example.api.facilityvisit.responses.SubFacilityVisitResponse;
import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.controller.facilityvisit.mapper.FacilityVisitRequestMapper;
import gov.va.starter.example.facilityvisit.factory.FacilityVisitFactory;
import gov.va.starter.example.facilityvisit.model.FacilityVisitData;
import gov.va.starter.example.service.spi.facilityvisit.FacilityVisitService;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
import gov.va.starter.example.service.spi.facilityvisit.model.SubFacilityVisit;
import gov.va.starter.example.subfacilityvisit.factory.SubFacilityVisitFactory;
import gov.va.starter.example.subfacilityvisit.model.SubFacilityVisitData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FacilityVisitControllerTest {

  private FacilityVisitController controller;

  @Mock private FacilityVisitService manager;
  @Mock private FacilityVisitRequestMapper mapper;
  private EntityLifecycleNotifier notifier =
      new NoopEntityLifecycleNotifier(new MemoizedTimestampProvider(ZonedDateTime.now()));

  private FacilityVisit resource;
  private FacilityVisit output;
  private FacilityVisitData defaultResourceData;
  private FacilityVisitData bogusResourceData;
  private FacilityVisitFactory resourceFactory = new FacilityVisitFactory();
  private FacilityVisitRequest request;
  private FacilityVisitResponse response;
  private Optional<FacilityVisit> emptyResource = Optional.empty();
  private Optional<FacilityVisitResponse> emptyResponse = Optional.empty();
  private Optional<FacilityVisitResponse> optionalResponse;
  private Optional<FacilityVisit> optionalOutput;
  private List<FacilityVisitResponse> responseList;
  private List<FacilityVisit> outputList;
  private List<FacilityVisitResponse> emptyResponseList = Arrays.asList();
  private List<FacilityVisit> emptyOutputList = Arrays.asList();
  private PagedResponse<FacilityVisitResponse> responsePage;
  private PagedResponse<FacilityVisitResponse> emptyResponsePage;
  private Page<FacilityVisit> outputPage;
  private Page<FacilityVisit> emptyOutputPage;
  private Pageable pageable = Pageable.unpaged();

  private SubFacilityVisit subResource;
  private SubFacilityVisit subOutput;
  private SubFacilityVisitData defaultSubResourceData;
  private SubFacilityVisitData bogusSubResourceData;
  private SubFacilityVisitFactory subResourceFactory = new SubFacilityVisitFactory();
  private SubFacilityVisitRequest subRequest;
  private SubFacilityVisitResponse subResponse;
  private Optional<SubFacilityVisit> emptySubResource = Optional.empty();
  private Optional<SubFacilityVisitResponse> emptySubResponse = Optional.empty();
  private Optional<SubFacilityVisitResponse> optionalSubResponse;
  private Optional<SubFacilityVisit> optionalSubOutput;
  private List<SubFacilityVisitResponse> subResponseList;
  private List<SubFacilityVisit> subOutputList;
  private List<SubFacilityVisitResponse> emptySubResponseList = Arrays.asList();
  private List<SubFacilityVisit> emptySubOutputList = Arrays.asList();
  private PagedResponse<SubFacilityVisitResponse> subResponsePage;
  private PagedResponse<SubFacilityVisitResponse> emptySubResponsePage;
  private Page<SubFacilityVisit> subOutputPage;
  private Page<SubFacilityVisit> emptySubOutputPage;

  /** setup data for each test. */
  @BeforeEach
  public void setup() {

    controller = new FacilityVisitController(manager, mapper, notifier);

    defaultResourceData = resourceFactory.createBySpec(DEFAULT_SPEC);
    bogusResourceData = resourceFactory.createBySpec("bogus");
    defaultSubResourceData = subResourceFactory.createBySpec(DEFAULT_SPEC);
    bogusSubResourceData = subResourceFactory.createBySpec("bogus");

    // use the real mapper to generate consistent objects to use in mapper stubs
    FacilityVisitRequestMapper real = Mappers.getMapper(FacilityVisitRequestMapper.class);

    request =
        new FacilityVisitRequest(
            defaultResourceData.getUserName(),
            defaultResourceData.getPii(),
            defaultResourceData.getFirstName(),
            defaultResourceData.getLastName());
    resource = real.toModel(request);
    output =
        new FacilityVisit(
            defaultResourceData.getId(),
            resource.getUserName(),
            resource.getPii(),
            resource.getFirstName(),
            resource.getLastName());
    response = real.toFacilityVisitResponse(output);
    optionalResponse = Optional.of(response);
    optionalOutput = Optional.of(output);
    responseList = Arrays.asList(response, response);
    outputList = Arrays.asList(output, output);
    responsePage = new PagedResponse<>(responseList, 10, (long) 100, 1, 10);
    emptyResponsePage = new PagedResponse<>(emptyResponseList, 0, (long) 0, 0, 0);
    outputPage = new PageImpl<>(outputList);
    emptyOutputPage = new PageImpl<>(emptyOutputList);

    subRequest =
        new SubFacilityVisitRequest(
            defaultSubResourceData.getUserName(),
            defaultSubResourceData.getFirstName(),
            defaultSubResourceData.getLastName());
    subResource = real.toModel(subRequest);
    subOutput =
        new SubFacilityVisit(
            defaultSubResourceData.getId(),
            subResource.getUserName(),
            subResource.getFirstName(),
            subResource.getLastName());
    subResponse = real.toSubFacilityVisitResponse(subOutput);
    optionalSubResponse = Optional.of(subResponse);
    optionalSubOutput = Optional.of(subOutput);
    subResponseList = Arrays.asList(subResponse, subResponse);
    subOutputList = Arrays.asList(subOutput, subOutput);
    subResponsePage = new PagedResponse<>(subResponseList, 10, (long) 100, 1, 10);
    emptySubResponsePage = new PagedResponse<>(emptySubResponseList, 0, (long) 0, 0, 0);
    subOutputPage = new PageImpl<>(subOutputList);
    emptySubOutputPage = new PageImpl<>(emptySubOutputList);
  }

  private void createMapperStubs() {
    Mockito.when(mapper.toModel(request)).thenReturn(resource);
  }

  private void createResponseMapperStubs() {
    Mockito.when(mapper.toFacilityVisitResponse(output)).thenReturn(response);
  }

  private void createOptionalMapperStubs() {
    Mockito.when(mapper.toFacilityVisitResponse(optionalOutput)).thenReturn(response);
  }

  private void createListMapperStubs() {
    Mockito.when(mapper.toFacilityVisitResponsePage(outputPage)).thenReturn(responsePage);
  }

  private void createEmptyListMapperStubs() {
    Mockito.when(mapper.toFacilityVisitResponsePage(emptyOutputPage)).thenReturn(emptyResponsePage);
  }

  private void createSubFacilityVisitMapperStubs() {
    Mockito.when(mapper.toModel(subRequest)).thenReturn(subResource);
  }

  private void createSubFacilityVisitResponseMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponse(subOutput)).thenReturn(subResponse);
  }

  private void createOptionalSubFacilityVisitMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponse(optionalSubOutput)).thenReturn(subResponse);
  }

  private void createSubFacilityVisitListMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponsePage(subOutputPage)).thenReturn(subResponsePage);
  }

  private void createEmptySubFacilityVisitListMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponsePage(emptySubOutputPage))
        .thenReturn(emptySubResponsePage);
  }

  @Test
  public void findByResourceIdFailTest() throws Exception {

    Mockito.when(manager.findById(bogusResourceData.getId())).thenReturn(emptyResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<FacilityVisitResponse> response =
              controller.findEntityById(bogusResourceData.getId());
        });
  }

  @Test
  public void addResourceTest() throws Exception {

    createMapperStubs();
    createResponseMapperStubs();
    Mockito.when(manager.add(resource)).thenReturn(output);

    ResponseEntity<FacilityVisitResponse> response = controller.addEntity(request);

    assertThat(response.getStatusCodeValue()).isEqualTo(201);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getPii()).isEqualTo(defaultResourceData.getPii());
    assertThat(response.getBody().getFullName()).isEqualTo(defaultResourceData.getFullName());
    assertThat(response.getBody().getId()).isEqualTo(defaultResourceData.getId());
  }

  @Test
  public void findByIdTest() throws Exception {

    createResponseMapperStubs();
    Mockito.when(manager.findById(defaultResourceData.getId())).thenReturn(optionalOutput);

    ResponseEntity<FacilityVisitResponse> response =
        controller.findEntityById(defaultResourceData.getId());

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getPii()).isEqualTo(defaultResourceData.getPii());
    assertThat(response.getBody().getFullName()).isEqualTo(defaultResourceData.getFullName());
    assertThat(response.getBody().getId()).isEqualTo(defaultResourceData.getId());
  }

  @Test
  public void findByIdFailedTest() throws Exception {

    Mockito.when(manager.findById(bogusResourceData.getId())).thenReturn(emptyResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<FacilityVisitResponse> response =
              controller.findEntityById(bogusResourceData.getId());
        });
  }

  @Test
  public void findAllTest() throws Exception {

    createListMapperStubs();
    Mockito.when(manager.findAll(pageable)).thenReturn(outputPage);

    ResponseEntity<PagedResponse<FacilityVisitResponse>> response =
        controller.findEntities(pageable);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getItems().size()).isEqualTo(2);
    // Todo: check contents of the list objects
  }

  @Test
  public void findAllEmptyTest() throws Exception {

    createEmptyListMapperStubs();
    Mockito.when(manager.findAll(pageable)).thenReturn(emptyOutputPage);

    ResponseEntity<PagedResponse<FacilityVisitResponse>> response =
        controller.findEntities(pageable);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getItems().size()).isEqualTo(0);
  }

  @Test
  public void updateTest() throws Exception {

    createMapperStubs();
    createResponseMapperStubs();
    Mockito.when(manager.updateById(defaultResourceData.getId(), resource))
        .thenReturn(optionalOutput);

    ResponseEntity<FacilityVisitResponse> response =
        controller.updateEntityById(defaultResourceData.getId(), request);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getPii()).isEqualTo(defaultResourceData.getPii());
    assertThat(response.getBody().getFullName()).isEqualTo(defaultResourceData.getFullName());
    assertThat(response.getBody().getId()).isEqualTo(defaultResourceData.getId());
  }

  @Test
  public void updateFailedTest() throws Exception {

    createMapperStubs();
    Mockito.when(manager.updateById(bogusResourceData.getId(), resource)).thenReturn(emptyResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<FacilityVisitResponse> response =
              controller.updateEntityById(bogusResourceData.getId(), request);
        });
  }

  @Test
  public void deleteTest() throws Exception {

    createResponseMapperStubs();
    Mockito.when(manager.deleteById(defaultResourceData.getId())).thenReturn(optionalOutput);

    ResponseEntity<FacilityVisitResponse> response =
        controller.deleteEntityById(defaultResourceData.getId());

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getPii()).isEqualTo(defaultResourceData.getPii());
    assertThat(response.getBody().getFullName()).isEqualTo(defaultResourceData.getFullName());
    assertThat(response.getBody().getId()).isEqualTo(defaultResourceData.getId());
  }

  @Test
  public void deleteFailedTest() throws Exception {

    Mockito.when(manager.deleteById(bogusResourceData.getId())).thenReturn(emptyResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<FacilityVisitResponse> response =
              controller.deleteEntityById(bogusResourceData.getId());
        });
  }

  @Test
  public void findBySubResourceIdFailTest() throws Exception {

    Mockito.when(
            manager.getSubFacilityVisit(bogusResourceData.getId(), bogusSubResourceData.getId()))
        .thenReturn(emptySubResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.getSubFacilityVisit(
                  bogusResourceData.getId(), bogusSubResourceData.getId());
        });
  }

  @Test
  public void addSubResourceTest() throws Exception {

    createSubFacilityVisitMapperStubs();
    createSubFacilityVisitResponseMapperStubs();
    Mockito.when(manager.addSubFacilityVisit(defaultResourceData.getId(), subResource))
        .thenReturn(subOutput);

    ResponseEntity<SubFacilityVisitResponse> response =
        controller.addSubFacilityVisit(defaultResourceData.getId(), subRequest);

    assertThat(response.getStatusCodeValue()).isEqualTo(201);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(defaultSubResourceData.getId());
  }

  @Test
  public void findSubResourceByIdTest() throws Exception {

    createSubFacilityVisitResponseMapperStubs();
    Mockito.when(
            manager.getSubFacilityVisit(
                defaultResourceData.getId(), defaultSubResourceData.getId()))
        .thenReturn(optionalSubOutput);

    ResponseEntity<SubFacilityVisitResponse> response =
        controller.getSubFacilityVisit(defaultResourceData.getId(), defaultSubResourceData.getId());

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getId()).isEqualTo(defaultSubResourceData.getId());
  }

  @Test
  public void findSubResourceByIdFailedTest() throws Exception {

    Mockito.when(
            manager.getSubFacilityVisit(bogusResourceData.getId(), bogusSubResourceData.getId()))
        .thenReturn(emptySubResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.getSubFacilityVisit(
                  bogusResourceData.getId(), bogusSubResourceData.getId());
        });
  }

  @Test
  public void findAllSubResourceTest() throws Exception {

    createSubFacilityVisitListMapperStubs();
    Mockito.when(manager.getSubFacilityVisits(defaultResourceData.getId(), pageable))
        .thenReturn(subOutputPage);

    ResponseEntity<PagedResponse<SubFacilityVisitResponse>> response =
        controller.getSubFacilityVisits(defaultResourceData.getId(), pageable);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getItems().size()).isEqualTo(2);
    // Todo: check contents of the list objects
  }

  @Test
  public void findAllSubResourceEmptyTest() throws Exception {

    createEmptySubFacilityVisitListMapperStubs();
    Mockito.when(manager.getSubFacilityVisits(defaultResourceData.getId(), pageable))
        .thenReturn(emptySubOutputPage);

    ResponseEntity<PagedResponse<SubFacilityVisitResponse>> response =
        controller.getSubFacilityVisits(defaultResourceData.getId(), pageable);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getItems().size()).isEqualTo(0);
  }

  @Test
  public void updateSubResourceTest() throws Exception {

    createSubFacilityVisitMapperStubs();
    createSubFacilityVisitResponseMapperStubs();
    Mockito.when(
            manager.updateSubFacilityVisit(
                defaultResourceData.getId(), defaultSubResourceData.getId(), subResource))
        .thenReturn(optionalSubOutput);

    ResponseEntity<SubFacilityVisitResponse> response =
        controller.updateSubFacilityVisit(
            defaultResourceData.getId(), defaultSubResourceData.getId(), subRequest);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getId()).isEqualTo(defaultSubResourceData.getId());
  }

  @Test
  public void updateSubResourceFailedTest() throws Exception {

    createSubFacilityVisitMapperStubs();
    Mockito.when(
            manager.updateSubFacilityVisit(
                bogusResourceData.getId(), bogusSubResourceData.getId(), subResource))
        .thenReturn(emptySubResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.updateSubFacilityVisit(
                  bogusResourceData.getId(), bogusSubResourceData.getId(), subRequest);
        });
  }

  @Test
  public void deleteSubResourceTest() throws Exception {

    createSubFacilityVisitResponseMapperStubs();
    Mockito.when(
            manager.deleteSubFacilityVisit(
                defaultResourceData.getId(), defaultSubResourceData.getId()))
        .thenReturn(optionalSubOutput);

    ResponseEntity<SubFacilityVisitResponse> response =
        controller.deleteSubFacilityVisit(
            defaultResourceData.getId(), defaultSubResourceData.getId());

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getId()).isEqualTo(defaultSubResourceData.getId());
  }

  @Test
  public void deleteSubResourceFailedTest() throws Exception {

    Mockito.when(
            manager.deleteSubFacilityVisit(bogusResourceData.getId(), bogusSubResourceData.getId()))
        .thenReturn(emptySubResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.deleteSubFacilityVisit(
                  bogusResourceData.getId(), bogusSubResourceData.getId());
        });
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
