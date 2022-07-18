package gov.va.starter.example.service.provider.subfacilityvisit;

import static gov.va.starter.boot.test.data.provider.NamedDataFactory.DEFAULT_SPEC;

import gov.va.starter.example.persistence.model.SubFacilityVisitEntity;
import gov.va.starter.example.persistence.model.SubFacilityVisitEntityRepository;
import gov.va.starter.example.service.provider.subfacilityvisit.mapper.SubFacilityVisitEntityMapper;
import gov.va.starter.example.service.spi.subfacilityvisit.model.SubFacilityVisit;
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
public class SubFacilityVisitServiceImplTest {

  private SubFacilityVisitServiceImpl manager;

  @Mock private SubFacilityVisitEntityRepository repository;
  @Mock private SubFacilityVisitEntityMapper mapper;

  private SubFacilityVisitFactory resourceFactory = new SubFacilityVisitFactory();
  private SubFacilityVisitData defaultResourceData;
  private SubFacilityVisitData bogusResourceData;
  private SubFacilityVisit resource;
  private SubFacilityVisit output;
  private SubFacilityVisitEntity entity;
  private SubFacilityVisitEntity added;
  private Optional<SubFacilityVisit> emptyResource = Optional.empty();
  private Optional<SubFacilityVisitEntity> emptyEntity = Optional.empty();
  private Optional<SubFacilityVisitEntity> optionalEntity;
  private Optional<SubFacilityVisitEntity> optionalAdded;
  private Optional<SubFacilityVisit> optionalOutput;
  private List<SubFacilityVisitEntity> entityList;
  private List<SubFacilityVisit> outputList;
  private List<SubFacilityVisitEntity> emptyEntityList = Arrays.asList();
  private List<SubFacilityVisit> emptyOutputList = Arrays.asList();
  private Page<SubFacilityVisitEntity> entityPage;
  private Page<SubFacilityVisit> outputPage;
  private Page<SubFacilityVisitEntity> emptyEntityPage;
  private Page<SubFacilityVisit> emptyOutputPage;
  private Pageable pageable = Pageable.unpaged();

  /** setup data for each test. */
  @BeforeEach
  public void setup() {

    manager = new SubFacilityVisitServiceImpl(repository, mapper);

    defaultResourceData = resourceFactory.createBySpec(DEFAULT_SPEC);
    bogusResourceData = resourceFactory.createBySpec("bogus");

    // use the real mapper to generate consistent objects to use in mapper stubs
    SubFacilityVisitEntityMapper real = Mappers.getMapper(SubFacilityVisitEntityMapper.class);

    resource =
        SubFacilityVisit.builder()
            .userName(defaultResourceData.getUserName())
            .pii(defaultResourceData.getPii())
            .firstName(defaultResourceData.getFirstName())
            .lastName(defaultResourceData.getLastName())
            .build();
    entity = real.toEntity(resource);
    added =
        new SubFacilityVisitEntity(
            defaultResourceData.getId(),
            entity.getUserName(),
            entity.getPii(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getFacilityVisitId());
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

  @Test
  public void findBySubFacilityVisitIdFailTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(Mockito.any())).thenReturn(emptyEntity);

    Optional<SubFacilityVisit> result = manager.findById(bogusResourceData.getId());
    Assertions.assertThat(!result.isPresent()).isTrue();
  }

  @Test
  public void addSubFacilityVisitTest() {

    createMapperStubs();
    Mockito.when(repository.save(entity)).thenReturn(added);

    SubFacilityVisit response = manager.add(resource);

    Assertions.assertThat(response.getFirstName()).isEqualTo(resource.getFirstName());
    Assertions.assertThat(response.getId()).isEqualTo(added.getId());
  }

  @Test
  public void findByUserNameTest() {

    createOptionalMapperStubs();
    Mockito.when(repository.findByUserName(defaultResourceData.getUserName()))
        .thenReturn(optionalAdded);

    Optional<SubFacilityVisit> response = manager.findByUserName(defaultResourceData.getUserName());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(added.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(added.getId());
  }

  @Test
  public void findByUserNameFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findByUserName(bogusResourceData.getUserName()))
        .thenReturn(emptyEntity);

    Optional<SubFacilityVisit> response = manager.findByUserName(bogusResourceData.getUserName());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void findByLastNameTest() {

    createListMapperStubs();
    Mockito.when(repository.findByLastName(defaultResourceData.getLastName(), pageable))
        .thenReturn(entityPage);

    Page<SubFacilityVisit> response =
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

    Page<SubFacilityVisit> response =
        manager.findByLastName(bogusResourceData.getLastName(), pageable);

    Assertions.assertThat(response.getContent().isEmpty()).isTrue();
  }

  @Test
  public void findByIdTest() {

    createOptionalMapperStubs();
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(optionalAdded);

    Optional<SubFacilityVisit> response = manager.findById(defaultResourceData.getId());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(added.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(added.getId());
  }

  @Test
  public void findByIdFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(bogusResourceData.getId())).thenReturn(emptyEntity);

    Optional<SubFacilityVisit> response = manager.findById(bogusResourceData.getId());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void findAllTest() {

    createListMapperStubs();
    Mockito.when(repository.findAll(pageable)).thenReturn(entityPage);

    Page<SubFacilityVisit> response = manager.findAll(pageable);

    Assertions.assertThat(response.getContent().size()).isEqualTo(2);
  }

  @Test
  public void findAllEmptyTest() {

    createEmptyListMapperStubs();
    Mockito.when(repository.findAll(pageable)).thenReturn(emptyEntityPage);

    Page<SubFacilityVisit> response = manager.findAll(pageable);

    Assertions.assertThat(response.getContent().size()).isEqualTo(0);
  }

  @Test
  public void updateTest() {

    createOptionalMapperStubs();
    Mockito.when(mapper.updateMetadata(resource, added)).thenReturn(added);
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(optionalAdded);
    Mockito.when(repository.save(added)).thenReturn(added);

    Optional<SubFacilityVisit> response = manager.updateById(defaultResourceData.getId(), resource);

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(resource.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(defaultResourceData.getId());
  }

  @Test
  public void updateFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(emptyEntity);

    Optional<SubFacilityVisit> response = manager.updateById(defaultResourceData.getId(), resource);

    Assertions.assertThat(response.isEmpty()).isTrue();
  }

  @Test
  public void deleteTest() {

    createOptionalMapperStubs();
    Mockito.when(repository.findById(defaultResourceData.getId())).thenReturn(optionalAdded);

    Optional<SubFacilityVisit> response = manager.deleteById(defaultResourceData.getId());

    Assertions.assertThat(response.isPresent()).isTrue();
    Assertions.assertThat(response.get().getFirstName()).isEqualTo(added.getFirstName());
    Assertions.assertThat(response.get().getId()).isEqualTo(added.getId());
  }

  @Test
  public void deleteFailedTest() {

    createEmptyMapperStubs();
    Mockito.when(repository.findById(bogusResourceData.getId())).thenReturn(emptyEntity);

    Optional<SubFacilityVisit> response = manager.deleteById(bogusResourceData.getId());

    Assertions.assertThat(response.isEmpty()).isTrue();
  }
}
