package gov.va.starter.example.service.provider.facilityvisit;

import gov.va.starter.boot.exception.RequestValidationException;
import gov.va.starter.example.persistence.model.FacilityVisitEntityRepository;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntity;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntityRepository;
import gov.va.starter.example.service.provider.facilityvisit.mapper.FacilityVisitEntityMapper;
import gov.va.starter.example.service.spi.facilityvisit.FacilityVisitService;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
import gov.va.starter.example.service.spi.facilityvisit.model.SubFacilityVisit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class FacilityVisitServiceImpl implements FacilityVisitService {

  private FacilityVisitEntityRepository repository;
  private FacilityVisitEntityMapper mapper;
  private SubFacilityVisitEntityRepository subResourceRepository;

  FacilityVisitServiceImpl(
      FacilityVisitEntityRepository repository,
      FacilityVisitEntityMapper mapper,
      SubFacilityVisitEntityRepository subResourceRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.subResourceRepository = subResourceRepository;
  }

  /**
   * add a new FacilityVisit entity.
   *
   * @param resource resource info to add (id should be null)
   * @return new resource object with valid id
   */
  @Override
  public FacilityVisit add(FacilityVisit resource) throws RequestValidationException {
    FacilityVisit saved = mapper.toModel(repository.save(mapper.toEntity(resource)));
    return saved;
  }

  /**
   * find all resources matching last name.
   *
   * @param lastName criteria for match
   * @return list of matching FacilityVisit records
   */
  @Override
  public Page<FacilityVisit> findByLastName(String lastName, Pageable pageable) {
    log.info("looking up by lastname of:{}", lastName);
    Page<FacilityVisit> responseList =
        mapper.toModelPage(repository.findByLastName(lastName, pageable));
    log.info("Response list size:{}", responseList.getContent().size());
    return responseList;
  }

  /**
   * find resource by user name.
   *
   * @param userName username criteria to match
   * @return matching record, or null
   */
  @Override
  public Optional<FacilityVisit> findByUserName(String userName) {
    log.info("looking up by username:{}", userName);
    Optional<FacilityVisit> resource = mapper.toModel(repository.findByUserName(userName));
    return resource;
  }

  @Override
  public Optional<FacilityVisit> findById(String id) {
    Optional<FacilityVisit> resource = mapper.toModel(repository.findById(id));
    return resource;
  }

  @Override
  public Page<FacilityVisit> findAll(Pageable pageable) {
    Page<FacilityVisit> resource = mapper.toModelPage(repository.findAll(pageable));
    return resource;
  }

  @Override
  // CSOFF: LineLength
  public Optional<FacilityVisit> updateById(String id, FacilityVisit record)
      // CSON: LineLength
      throws RequestValidationException {
    Optional<FacilityVisit> resource =
        mapper.toModel(
            repository
                .findById(id)
                .map((obj) -> mapper.updateMetadata(record, obj))
                .map((obj) -> repository.save(obj)));

    return resource;
  }

  @Override
  public Optional<FacilityVisit> deleteById(String id) {
    Optional<FacilityVisit> resource = findById(id);
    repository.deleteById(id);
    return resource;
  }

  /**
   * add a new SubFacilityVisit entity.
   *
   * @param id FacilityVisit resource id
   * @param subResource resource info to add (id should be null)
   * @return new resource object with valid id
   */
  @Override
  // CSOFF: LineLength
  public SubFacilityVisit addSubFacilityVisit(String id, SubFacilityVisit subResource)
      // CSON: LineLength
      throws RequestValidationException {
    SubFacilityVisitEntity entity = mapper.toSubFacilityVisitEntity(subResource);
    entity.setFacilityVisitId(id);
    SubFacilityVisit saved = mapper.toSubFacilityVisitModel(subResourceRepository.save(entity));
    return saved;
  }

  /**
   * find a SubFacilityVisit resource by resource id.
   *
   * @param id FacilityVisit resource id
   * @param subResourceId id of the SubFacilityVisit
   * @return matching record, or null
   */
  @Override
  // CSOFF: LineLength
  public Optional<SubFacilityVisit> getSubFacilityVisit(String id, String subResourceId) {
    // CSON: LineLength
    Optional<SubFacilityVisit> resource =
        mapper.toSubFacilityVisitModel(subResourceRepository.findById(subResourceId));
    return resource;
  }

  /**
   * find all SubFacilityVisit resources related to FacilityVisit.
   *
   * @param id FacilityVisit resource id
   * @return list of SubFacilityVisit resources
   */
  @Override
  // CSOFF: LineLength
  public Page<SubFacilityVisit> getSubFacilityVisits(String id, Pageable pageable) {
    // CSON: LineLength
    Page<SubFacilityVisit> resources =
        mapper.toSubFacilityVisitModelPage(
            subResourceRepository.findAllByFacilityVisitId(id, pageable));
    return resources;
  }

  /**
   * update a SubFacilityVisit resource based on id.
   *
   * @param id FacilityVisit resource id
   * @param subResourceId SubFacilityVisit resource id
   * @param record SubFacilityVisit resource data
   * @return Optional<> reference to updated SubFacilityVisit resource
   */
  @Override
  // CSOFF: LineLength
  public Optional<SubFacilityVisit> updateSubFacilityVisit(
      String id, String subResourceId, SubFacilityVisit record)
      // CSON: LineLength
      throws RequestValidationException {
    Optional<SubFacilityVisit> resource =
        mapper.toSubFacilityVisitModel(
            subResourceRepository
                .findById(subResourceId)
                .map((obj) -> mapper.updateSubFacilityVisitMetadata(record, obj))
                .map((obj) -> subResourceRepository.save(obj)));

    return resource;
  }

  /**
   * delete a SubFacilityVisit resource based on id.
   *
   * @param id FacilityVisit resource id
   * @param subResourceId SubFacilityVisit resource id
   * @return subResource SubFacilityVisit resource data
   */
  @Override
  // CSOFF: LineLength
  public Optional<SubFacilityVisit> deleteSubFacilityVisit(String id, String subResourceId) {
    // CSON: LineLength
    Optional<SubFacilityVisit> result = getSubFacilityVisit(id, subResourceId);
    subResourceRepository.deleteById(subResourceId);
    return result;
  }
}
