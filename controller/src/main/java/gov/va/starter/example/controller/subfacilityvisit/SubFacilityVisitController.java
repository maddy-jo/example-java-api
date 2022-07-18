package gov.va.starter.example.controller.subfacilityvisit;

import gov.va.starter.boot.exception.RequestValidationException;
import gov.va.starter.boot.exception.ResourceNotFoundException;
import gov.va.starter.boot.notifier.lifecycle.entity.spi.EntityLifecycleNotifier;
import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.api.subfacilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.subfacilityvisit.resources.SubFacilityVisitResource;
import gov.va.starter.example.api.subfacilityvisit.responses.SubFacilityVisitResponse;
import gov.va.starter.example.controller.subfacilityvisit.mapper.SubFacilityVisitRequestMapper;
import gov.va.starter.example.service.spi.subfacilityvisit.SubFacilityVisitService;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController
public class SubFacilityVisitController implements SubFacilityVisitResource {

  private final SubFacilityVisitService manager;
  private final SubFacilityVisitRequestMapper mapper;
  private final EntityLifecycleNotifier notifier;
  // TODO: Need to find a better way to determine version of entity
  private final String entityVersion = "0.0.1";

  /**
   * constructor.
   *
   * @param manager instance of SubFacilityVisit manager
   * @param mapper instance of SubFacilityVisit request mappper
   */
  public SubFacilityVisitController(
      SubFacilityVisitService manager,
      SubFacilityVisitRequestMapper mapper,
      EntityLifecycleNotifier notifier) {
    this.manager = manager;
    this.mapper = mapper;
    this.notifier = notifier;
  }

  @Override
  public ResponseEntity<SubFacilityVisitResponse> addEntity(
      SubFacilityVisitRequest addEntityRequest) throws RequestValidationException {

    log.info("username->{}", addEntityRequest.getUserName());
    SubFacilityVisit resource = mapper.toModel(addEntityRequest);
    SubFacilityVisit saved = manager.add(resource);
    SubFacilityVisitResponse response = mapper.toSubFacilityVisitResponse(saved);
    notifier.created(saved, entityVersion, URI.create("user:anonymous"));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<SubFacilityVisitResponse> findEntityById(String id)
      throws ResourceNotFoundException {

    log.info("id->{}", id);
    Optional<SubFacilityVisit> found = manager.findById(id);
    return new ResponseEntity<>(
        found
            .map(r -> mapper.toSubFacilityVisitResponse(r))
            .orElseThrow(() -> new ResourceNotFoundException(id)),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PagedResponse<SubFacilityVisitResponse>> findEntities(Pageable pageable) {
    Page<SubFacilityVisit> resources = manager.findAll(pageable);

    return new ResponseEntity<>(mapper.toSubFacilityVisitResponsePage(resources), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<SubFacilityVisitResponse> updateEntityById(
      String id, SubFacilityVisitRequest request)
      throws ResourceNotFoundException, RequestValidationException {

    log.info("id->{}", id);
    Optional<SubFacilityVisit> found = manager.updateById(id, mapper.toModel(request));
    if (found.isPresent()) {
      notifier.updated(found.get(), entityVersion, URI.create("user:anonymous"));
    }
    return new ResponseEntity<>(
        found
            .map(r -> mapper.toSubFacilityVisitResponse(r))
            .orElseThrow(() -> new ResourceNotFoundException(id)),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<SubFacilityVisitResponse> deleteEntityById(String id)
      throws ResourceNotFoundException {

    log.info("id->{}", id);
    Optional<SubFacilityVisit> found = manager.deleteById(id);
    if (found.isPresent()) {
      notifier.deleted(found.get(), entityVersion, URI.create("user:anonymous"));
    }
    return new ResponseEntity<>(
        found
            .map(r -> mapper.toSubFacilityVisitResponse(r))
            .orElseThrow(() -> new ResourceNotFoundException(id)),
        HttpStatus.OK);
  }
}
