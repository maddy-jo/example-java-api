package gov.va.starter.example.service.provider.facilityvisit;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;

import gov.va.starter.example.facilityvisit.factory.FacilityVisitFactory;
import gov.va.starter.example.facilityvisit.model.FacilityVisitData;
import gov.va.starter.example.persistence.model.FacilityVisitEntity;
import gov.va.starter.example.persistence.model.FacilityVisitEntityRepository;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntity;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntityRepository;
import gov.va.starter.example.service.provider.facilityvisit.mapper.FacilityVisitEntityMapper;
import gov.va.starter.example.service.spi.facilityvisit.model.FacilityVisit;
import gov.va.starter.example.service.spi.facilityvisit.model.SubFacilityVisit;
import gov.va.starter.example.subfacilityvisit.factory.SubFacilityVisitFactory;
import gov.va.starter.example.subfacilityvisit.model.SubFacilityVisitData;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FacilityVisitServiceImplTest {

  private FacilityVisitServiceImpl manager;

  @Mock private FacilityVisitEntityRepository repository;
  @Mock private FacilityVisitEntityMapper mapper;
  @Mock private SubFacilityVisitEntityRepository subResourceRepository;

  private FacilityVisitFactory resourceFactory = new FacilityVisitFactory();
  private FacilityVisitData defaultResourceData;
  private FacilityVisitData bogusResourceData;
  private FacilityVisit resource;
  private FacilityVisit output;
  private FacilityVisitEntity entity;
  private FacilityVisitEntity added;
  private Optional<FacilityVisit> emptyResource = Optional.empty();
  private Optional<FacilityVisitEntity> emptyEntity = Optional.empty();
  private Optional<FacilityVisitEntity> optionalEntity;
  private Optional<FacilityVisitEntity> optionalAdded;
  private Optional<FacilityVisit> optionalOutput;
  private List<FacilityVisitEntity> entityList;
  private List<FacilityVisit> outputList;
  private List<FacilityVisitEntity> emptyEntityList = Arrays.asList();
  private List<FacilityVisit> emptyOutputList = Arrays.asList();
  private Page<FacilityVisitEntity> entityPage;
  private Page<FacilityVisit> outputPage;
  private Page<FacilityVisitEntity> emptyEntityPage;
  private Page<FacilityVisit> emptyOutputPage;
  private Pageable pageable = Pageable.unpaged();

  private SubFacilityVisitFactory subResourceFactory = new SubFacilityVisitFactory();
  private SubFacilityVisitData defaultSubResourceData;
  private SubFacilityVisit subResource;
  private SubFacilityVisit subOutput;
  private SubFacilityVisitEntity subEntity;
  private SubFacilityVisitEntity subAdded;
  private Optional<SubFacilityVisit> emptySubResource = Optional.empty();
  private Optional<SubFacilityVisitEntity> emptySubEntity = Optional.empty();
  private Optional<SubFacilityVisitEntity> optionalSubEntity;
  private Optional<SubFacilityVisitEntity> optionalSubAdded;
  private Optional<SubFacilityVisit> optionalSubOutput;
  private List<SubFacilityVisitEntity> subEntityList;
  private List<SubFacilityVisit> subOutputList;
  private List<SubFacilityVisitEntity> emptySubEntityList = Arrays.asList();
  private List<SubFacilityVisit> emptySubOutputList = Arrays.asList();
  private Page<SubFacilityVisitEntity> subEntityPage;
  private Page<SubFacilityVisit> subOutputPage;
  private Page<SubFacilityVisitEntity> emptySubEntityPage;
  private Page<SubFacilityVisit> emptySubOutputPage;

  /** setup data for each test. */
  @BeforeEach
  public void setup() {

    manager = new FacilityVisitServiceImpl(repository, mapper, subResourceRepository);

    defaultResourceData = resourceFactory.createBySpec(DEFAULT_SPEC);
    bogusResourceData = resourceFactory.createBySpec("bogus");
    defaultSubResourceData = subResourceFactory.createBySpec(DEFAULT_SPEC);

    // use the real mapper to generate consistent objects to use in mapper stubs
    FacilityVisitEntityMapper real = Mappers.getMapper(FacilityVisitEntityMapper.class);

    resource =
        FacilityVisit.builder()
            .userName(defaultResourceData.getUserName())
            .pii(defaultResourceData.getPii())
            .firstName(defaultResourceData.getFirstName())
            .lastName(defaultResourceData.getLastName())
            .build();
    entity = real.toEntity(resource);
    added =
        new FacilityVisitEntity(
            defaultResourceData.getId(),
            entity.getUserName(),
            entity.getPii(),
            entity.getFirstName(),
            entity.getLastName());
    output = real.toModel(added);
    optionalEntity = Optional.of(entity);
    optionalAdded = Optional.of(added);
    optionalOutput = Optional.of(output);
    entityList = Arrays.asList(added, added);
    outputList = Arrays.asList(output, output);
    entityPage = new PageImpl<>(entityList);
    outputPage = new PageImpl<>(outputList);
    emptyEntityPage = new PageImpl<>(emptyEntityList);
    emptyOutputPage = new PageImpl<>(emptyOutputList);

    subResource =
        SubFacilityVisit.builder()
            .userName(defaultSubResourceData.getUserName())
            .firstName(defaultSubResourceData.getFirstName())
            .lastName(defaultSubResourceData.getLastName())
            .build();
    subEntity = real.toSubFacilityVisitEntity(subResource);
    subAdded =
        new SubFacilityVisitEntity(
            defaultSubResourceData.getId(),
            subEntity.getUserName(),
            subEntity.getPii(),
            subEntity.getFirstName(),
            subEntity.getLastName(),
            defaultResourceData.getId());
    subOutput = real.toSubFacilityVisitModel(subAdded);
    optionalSubEntity = Optional.of(subEntity);
    optionalSubAdded = Optional.of(subAdded);
    optionalSubOutput = Optional.of(subOutput);
    subEntityList = Arrays.asList(subAdded, subAdded);
    subOutputList = Arrays.asList(subOutput, subOutput);
    subEntityPage = new PageImpl<>(subEntityList);
    subOutputPage = new PageImpl<>(subOutputList);
    emptySubEntityPage = new PageImpl<>(emptySubEntityList);
    emptySubOutputPage = new PageImpl<>(emptySubOutputList);
  }

  private void createMapperStubs() {
    Mockito.when(mapper.toEntity(resource)).thenReturn(entity);
    Mockito.when(mapper.toModel(added)).thenReturn(output);
  }

  private void createOptionalMapperStubs() {
    Mockito.when(mapper.toModel(optionalAdded)).thenReturn(optionalOutput);
  }

  private void createEmptyMapperStubs() {
    Mockito.when(mapper.toModel(emptyEntity)).thenReturn(emptyResource);
  }

  private void createListMapperStubs() {
    Mockito.when(mapper.toModelPage(entityPage)).thenReturn(outputPage);
  }

  private void createEmptyListMapperStubs() {
    Mockito.when(mapper.toModelPage(emptyEntityPage)).thenReturn(emptyOutputPage);
  }

  private void createSubFacilityVisitMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitEntity(subResource)).thenReturn(subEntity);
    Mockito.when(mapper.toSubFacilityVisitModel(subAdded)).thenReturn(subOutput);
  }

  private void createOptionalSubFacilityVisitMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitModel(optionalSubAdded)).thenReturn(optionalSubOutput);
  }

  private void createEmptySubFacilityVisitMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitModel(emptySubEntity)).thenReturn(emptySubResource);
  }

  private void createSubFacilityVisitListMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitModelPage(subEntityPage)).thenReturn(subOutputPage);
  }

  private void createEmptySubFacilityVisitListMapperStubs() {
    Mockito.when(mapper.toSubFacilityVisitModelPage(emptySubEntityPage))
        .thenReturn(emptySubOutputPage);
  }

  @Test
  public void findByFacilityVisitIdFailTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(Mockito.any())).thenReturn(emptyEntity);

    Optional<FacilityVisit> result = manager.findById(bogusResourceData.getId());
    Assertions.assertThat(!result.isPresent()).isTrue();
  }

  @Test
  public void addFacilityVisitTest() {

    createMapperStubs();
    Mockito.when(repository.save(entity)).thenReturn(added);

    FacilityVisit response = manager.add(resource);

    Assertions.assertThat(response.getFirstName()).isEqualTo(resource.getFirstName());
    Assertions.assertThat(response.getId()).isEqualTo(added.getId());
  }

  @Test
  public void findByUserNameTest() {

    createOptionalMapperStubs();
    Mockito.when(repository.findByUserName(defaultResourceData.getUserName()))
        .thenReturn(optionalAdded);

    Optional<FacilityVisit> response = manager.findByUserName(defaultResourceData.getUserName());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(added.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(added.getId());
  }

  @Test
  public void findByUserNameFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findByUserName(bogusResourceData.getUserName()))
        .thenReturn(emptyEntity);

    Optional<FacilityVisit> response = manager.findByUserName(bogusResourceData.getUserName());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void findByLastNameTest() {

    createListMapperStubs();
    Mockito.when(repository.findByLastName(defaultResourceData.getLastName(), pageable))
        .thenReturn(entityPage);

    Page<FacilityVisit> response =
        manager.findByLastName(defaultResourceData.getLastName(), pageable);

    Assertions.assertThat(response.getContent().isEmpty()).isFalse();
    Assertions.assertThat(response.getContent().get(0).getFirstName())
        .isEqualTo(added.getFirstName());
    Assertions.assertThat(response.getContent().get(0).getId()).isEqualTo(added.getId());
  }

  @Test
  public void findByLastNameFailedTest() {

    createEmptyListMapperStubs();
    Mockito.when(repository.findByLastName(bogusResourceData.getLastName(), pageable))
        .thenReturn(emptyEntityPage);

    Page<FacilityVisit> response =
        manager.findByLastName(bogusResourceData.getLastName(), pageable);

    Assertions.assertThat(response.getContent().isEmpty()).isTrue();
  }

  @Test
  public void findByIdTest() {

    createOptionalMapperStubs();
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(optionalAdded);

    Optional<FacilityVisit> response = manager.findById(defaultResourceData.getId());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(added.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(added.getId());
  }

  @Test
  public void findByIdFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(bogusResourceData.getId())).thenReturn(emptyEntity);

    Optional<FacilityVisit> response = manager.findById(bogusResourceData.getId());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void findAllTest() {

    createListMapperStubs();
    Mockito.when(repository.findAll(pageable)).thenReturn(entityPage);

    Page<FacilityVisit> response = manager.findAll(pageable);

    Assertions.assertThat(response.getContent().size()).isEqualTo(2);
  }

  @Test
  public void findAllEmptyTest() {

    createEmptyListMapperStubs();
    Mockito.when(repository.findAll(pageable)).thenReturn(emptyEntityPage);

    Page<FacilityVisit> response = manager.findAll(pageable);

    Assertions.assertThat(response.getContent().size()).isEqualTo(0);
  }

  @Test
  public void updateTest() {

    createOptionalMapperStubs();
    Mockito.when(mapper.updateMetadata(resource, added)).thenReturn(added);
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(optionalAdded);
    Mockito.when(repository.save(added)).thenReturn(added);

    Optional<FacilityVisit> response = manager.updateById(defaultResourceData.getId(), resource);

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(resource.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(defaultResourceData.getId());
  }

  @Test
  public void updateFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(emptyEntity);

    Optional<FacilityVisit> response = manager.updateById(defaultResourceData.getId(), resource);

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void deleteTest() {

    createOptionalMapperStubs();
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(optionalAdded);

    Optional<FacilityVisit> response = manager.deleteById(defaultResourceData.getId());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(added.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(added.getId());
  }

  @Test
  public void deleteFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(bogusResourceData.getId())).thenReturn(emptyEntity);

    Optional<FacilityVisit> response = manager.deleteById(bogusResourceData.getId());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void findBySubFacilityVisitIdFailTest() {

    createEmptySubFacilityVisitMapperStubs();
    Mockito.when(subResourceRepository.findById(Mockito.any())).thenReturn(emptySubEntity);

    Optional<SubFacilityVisit> result =
        manager.getSubFacilityVisit(defaultResourceData.getId(), "bogus");
    Assertions.assertThat(!result.isPresent()).isTrue();
  }

  @Test
  public void addSubFacilityVisitTest() {

    createSubFacilityVisitMapperStubs();
    Mockito.when(subResourceRepository.save(subEntity)).thenReturn(subAdded);

    SubFacilityVisit response =
        manager.addSubFacilityVisit(defaultResourceData.getId(), subResource);

    Assertions.assertThat(response.getFirstName()).isEqualTo(subResource.getFirstName());
    Assertions.assertThat(response.getId()).isEqualTo(subAdded.getId());
    Assertions.assertThat(response.getId()).isEqualTo(defaultSubResourceData.getId());
  }

  @Test
  public void findSubFacilityVisitByIdTest() {

    createOptionalSubFacilityVisitMapperStubs();
    Mockito.when(subResourceRepository.findById(defaultSubResourceData.getId()))
        .thenReturn(optionalSubAdded);

    Optional<SubFacilityVisit> response =
        manager.getSubFacilityVisit(defaultResourceData.getId(), defaultSubResourceData.getId());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(subAdded.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(subAdded.getId());
  }

  @Test
  public void findSubFacilityVisitByIdFailedTest() {

    createEmptySubFacilityVisitMapperStubs();
    Mockito.when(subResourceRepository.findById(bogusResourceData.getId()))
        .thenReturn(emptySubEntity);

    Optional<SubFacilityVisit> response =
        manager.getSubFacilityVisit(defaultResourceData.getId(), bogusResourceData.getId());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void findAllSubFacilityVisitTest() {

    createSubFacilityVisitListMapperStubs();
    Mockito.when(
            subResourceRepository.findAllByFacilityVisitId(defaultResourceData.getId(), pageable))
        .thenReturn(subEntityPage);

    Page<SubFacilityVisit> response =
        manager.getSubFacilityVisits(defaultResourceData.getId(), pageable);

    Assertions.assertThat(response.getContent().size()).isEqualTo(2);
  }

  @Test
  public void findAllSubFacilityVisitEmptyTest() {

    createEmptySubFacilityVisitListMapperStubs();
    Mockito.when(
            subResourceRepository.findAllByFacilityVisitId(defaultResourceData.getId(), pageable))
        .thenReturn(emptySubEntityPage);

    Page<SubFacilityVisit> response =
        manager.getSubFacilityVisits(defaultResourceData.getId(), pageable);

    Assertions.assertThat(response.getContent().size()).isEqualTo(0);
  }

  @Test
  public void updateSubFacilityVisitTest() {

    createOptionalSubFacilityVisitMapperStubs();
    Mockito.when(mapper.updateSubFacilityVisitMetadata(subResource, subAdded)).thenReturn(subAdded);
    Mockito.when(subResourceRepository.findById(defaultSubResourceData.getId()))
        .thenReturn(optionalSubAdded);
    Mockito.when(subResourceRepository.save(subAdded)).thenReturn(subAdded);

    Optional<SubFacilityVisit> response =
        manager.updateSubFacilityVisit(
            defaultResourceData.getId(), defaultSubResourceData.getId(), subResource);

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(subResource.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(defaultSubResourceData.getId());
  }

  @Test
  public void updateSubFacilityVisitFailedTest() {

    createEmptySubFacilityVisitMapperStubs();
    Mockito.when(subResourceRepository.findById(defaultSubResourceData.getId()))
        .thenReturn(emptySubEntity);

    Optional<SubFacilityVisit> response =
        manager.updateSubFacilityVisit(
            defaultResourceData.getId(), defaultSubResourceData.getId(), subResource);

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void deleteSubFacilityVisitTest() {

    createOptionalSubFacilityVisitMapperStubs();
    Mockito.when(subResourceRepository.findById(defaultSubResourceData.getId()))
        .thenReturn(optionalSubAdded);

    Optional<SubFacilityVisit> response =
        manager.deleteSubFacilityVisit(defaultResourceData.getId(), defaultSubResourceData.getId());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(subAdded.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(subAdded.getId());
  }

  @Test
  public void deleteSubFacilityVisitFailedTest() {

    createEmptySubFacilityVisitMapperStubs();
    Mockito.when(subResourceRepository.findById(bogusResourceData.getId()))
        .thenReturn(emptySubEntity);

    Optional<SubFacilityVisit> response =
        manager.deleteSubFacilityVisit(defaultResourceData.getId(), bogusResourceData.getId());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }
}
