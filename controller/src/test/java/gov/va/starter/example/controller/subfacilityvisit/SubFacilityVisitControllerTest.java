package gov.va.starter.example.controller.subfacilityvisit;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.va.starter.boot.exception.ResourceNotFoundException;
import gov.va.starter.boot.notifier.lifecycle.entity.provider.MemoizedTimestampProvider;
import gov.va.starter.boot.notifier.lifecycle.entity.provider.NoopEntityLifecycleNotifier;
import gov.va.starter.boot.notifier.lifecycle.entity.spi.EntityLifecycleNotifier;
import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.api.subfacilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.subfacilityvisit.responses.SubFacilityVisitResponse;
import gov.va.starter.example.controller.subfacilityvisit.mapper.SubFacilityVisitRequestMapper;
import gov.va.starter.example.service.spi.subfacilityvisit.SubFacilityVisitService;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
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
public class SubFacilityVisitControllerTest {

  private SubFacilityVisitController controller;

  @Mock private SubFacilityVisitService manager;
  @Mock private SubFacilityVisitRequestMapper mapper;
  private EntityLifecycleNotifier notifier =
      new NoopEntityLifecycleNotifier(new MemoizedTimestampProvider(ZonedDateTime.now()));

  private SubFacilityVisit resource;
  private SubFacilityVisit output;
  private SubFacilityVisitData defaultResourceData;
  private SubFacilityVisitData bogusResourceData;
  private SubFacilityVisitFactory resourceFactory = new SubFacilityVisitFactory();
  private SubFacilityVisitRequest request;
  private SubFacilityVisitResponse response;
  private Optional<SubFacilityVisit> emptyResource = Optional.empty();
  private Optional<SubFacilityVisitResponse> emptyResponse = Optional.empty();
  private Optional<SubFacilityVisitResponse> optionalResponse;
  private Optional<SubFacilityVisit> optionalOutput;
  private List<SubFacilityVisitResponse> responseList;
  private List<SubFacilityVisit> outputList;
  private List<SubFacilityVisitResponse> emptyResponseList = Arrays.asList();
  private List<SubFacilityVisit> emptyOutputList = Arrays.asList();
  private PagedResponse<SubFacilityVisitResponse> responsePage;
  private PagedResponse<SubFacilityVisitResponse> emptyResponsePage;
  private Page<SubFacilityVisit> outputPage;
  private Page<SubFacilityVisit> emptyOutputPage;
  private Pageable pageable = Pageable.unpaged();

  /** setup data for each test. */
  @BeforeEach
  public void setup() {

    controller = new SubFacilityVisitController(manager, mapper, notifier);

    defaultResourceData = resourceFactory.createBySpec(DEFAULT_SPEC);
    bogusResourceData = resourceFactory.createBySpec("bogus");

    // use the real mapper to generate consistent objects to use in mapper stubs
    SubFacilityVisitRequestMapper real = Mappers.getMapper(SubFacilityVisitRequestMapper.class);

    request =
        new SubFacilityVisitRequest(
            defaultResourceData.getUserName(),
            defaultResourceData.getPii(),
            defaultResourceData.getFirstName(),
            defaultResourceData.getLastName());
    resource = real.toModel(request);
    output =
        new SubFacilityVisit(
            defaultResourceData.getId(),
            resource.getUserName(),
            resource.getPii(),
            resource.getFirstName(),
            resource.getLastName());
    response = real.toSubFacilityVisitResponse(output);
    optionalResponse = Optional.of(response);
    optionalOutput = Optional.of(output);
    responseList = Arrays.asList(response, response);
    outputList = Arrays.asList(output, output);
    responsePage = new PagedResponse<>(responseList, 10, (long) 100, 1, 10);
    emptyResponsePage = new PagedResponse<>(emptyResponseList, 0, (long) 0, 0, 0);
    outputPage = new PageImpl<>(outputList);
    emptyOutputPage = new PageImpl<>(emptyOutputList);
  }

  private void createMapperStubs() {
    Mockito.when(mapper.toModel(request)).thenReturn(resource);
  }

  private void createResponseMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponse(output)).thenReturn(response);
  }

  private void createOptionalMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponse(optionalOutput)).thenReturn(response);
  }

  private void createListMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponsePage(outputPage)).thenReturn(responsePage);
  }

  private void createEmptyListMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitResponsePage(emptyOutputPage))
        .thenReturn(emptyResponsePage);
  }

  @Test
  public void findByResourceIdFailTest() throws Exception {

    Mockito.when(manager.findById(bogusResourceData.getId())).thenReturn(emptyResource);

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.findEntityById(bogusResourceData.getId());
        });
  }

  @Test
  public void addResourceTest() throws Exception {

    createMapperStubs();
    createResponseMapperStubs();
    Mockito.when(manager.add(resource)).thenReturn(output);

    ResponseEntity<SubFacilityVisitResponse> response = controller.addEntity(request);

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

    ResponseEntity<SubFacilityVisitResponse> response =
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
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.findEntityById(bogusResourceData.getId());
        });
  }

  @Test
  public void findAllTest() throws Exception {

    createListMapperStubs();
    Mockito.when(manager.findAll(pageable)).thenReturn(outputPage);

    ResponseEntity<PagedResponse<SubFacilityVisitResponse>> response =
        controller.findEntities(pageable);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody().getItems().size()).isEqualTo(2);
    // Todo: check contents of the list objects
  }

  @Test
  public void findAllEmptyTest() throws Exception {

    createEmptyListMapperStubs();
    Mockito.when(manager.findAll(pageable)).thenReturn(emptyOutputPage);

    ResponseEntity<PagedResponse<SubFacilityVisitResponse>> response =
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

    ResponseEntity<SubFacilityVisitResponse> response =
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
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.updateEntityById(bogusResourceData.getId(), request);
        });
  }

  @Test
  public void deleteTest() throws Exception {

    createResponseMapperStubs();
    Mockito.when(manager.deleteById(defaultResourceData.getId())).thenReturn(optionalOutput);

    ResponseEntity<SubFacilityVisitResponse> response =
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
          ResponseEntity<SubFacilityVisitResponse> response =
              controller.deleteEntityById(bogusResourceData.getId());
        });
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
