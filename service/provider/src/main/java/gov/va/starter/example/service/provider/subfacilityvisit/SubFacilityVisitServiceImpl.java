package gov.va.starter.example.service.provider.subfacilityvisit;

import gov.va.starter.boot.exception.RequestValidationException;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntityRepository;
import gov.va.starter.example.service.provider.subfacilityvisit.mapper.SubFacilityVisitEntityMapper;
import gov.va.starter.example.service.spi.subfacilityvisit.SubFacilityVisitService;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SubFacilityVisitServiceImpl implements SubFacilityVisitService {

  private SubFacilityVisitEntityRepository repository;
  private SubFacilityVisitEntityMapper mapper;

  SubFacilityVisitServiceImpl(
      SubFacilityVisitEntityRepository repository, SubFacilityVisitEntityMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /**
   * add a new SubFacilityVisit entity.
   *
   * @param resource resource info to add (id should be null)
   * @return new resource object with valid id
   */
  @Override
  public SubFacilityVisit add(SubFacilityVisit resource) throws RequestValidationException {
    SubFacilityVisit saved = mapper.toModel(repository.save(mapper.toEntity(resource)));
    return saved;
  }

  /**
   * find all resources matching last name.
   *
   * @param lastName criteria for match
   * @return list of matching SubFacilityVisit records
   */
  @Override
  public Page<SubFacilityVisit> findByLastName(String lastName, Pageable pageable) {
    log.info("looking up by lastname of:{}", lastName);
    Page<SubFacilityVisit> responseList =
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
  public Optional<SubFacilityVisit> findByUserName(String userName) {
    log.info("looking up by username:{}", userName);
    Optional<SubFacilityVisit> resource = mapper.toModel(repository.findByUserName(userName));
    return resource;
  }

  @Override
  public Optional<SubFacilityVisit> findById(String id) {
    Optional<SubFacilityVisit> resource = mapper.toModel(repository.findById(id));
    return resource;
  }

  @Override
  public Page<SubFacilityVisit> findAll(Pageable pageable) {
    Page<SubFacilityVisit> resource = mapper.toModelPage(repository.findAll(pageable));
    return resource;
  }

  @Override
  // CSOFF: LineLength
  public Optional<SubFacilityVisit> updateById(String id, SubFacilityVisit record)
      // CSON: LineLength
      throws RequestValidationException {
    Optional<SubFacilityVisit> resource =
        mapper.toModel(
            repository
                .findById(id)
                .map((obj) -> mapper.updateMetadata(record, obj))
                .map((obj) -> repository.save(obj)));

    return resource;
  }

  @Override
  public Optional<SubFacilityVisit> deleteById(String id) {
    Optional<SubFacilityVisit> resource = findById(id);
    repository.deleteById(id);
    return resource;
  }
}
