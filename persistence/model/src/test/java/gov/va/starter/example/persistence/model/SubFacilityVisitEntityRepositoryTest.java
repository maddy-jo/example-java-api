package gov.va.starter.example.persistence.model;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.starter.example.subfacilityvisit.factory.SubFacilityVisitFactory;
import gov.va.starter.example.subfacilityvisit.model.SubFacilityVisitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SubFacilityVisitEntityRepositoryTest {

  @Autowired private SubFacilityVisitEntityRepository modelEntityRepository;

  private SubFacilityVisitEntity entity;

  private SubFacilityVisitFactory subFacilityVisitFactory = new SubFacilityVisitFactory();
  private List<SubFacilityVisitData> defaultSubFacilityVisitDataCollection =
      subFacilityVisitFactory.createCollectionBySpec("duplicateLastName");

  @BeforeEach
  public void setup() {
    entity =
        new SubFacilityVisitEntity(
            defaultSubFacilityVisitDataCollection.get(0).getUserName(),
            defaultSubFacilityVisitDataCollection.get(0).getPii(),
            defaultSubFacilityVisitDataCollection.get(0).getFirstName(),
            defaultSubFacilityVisitDataCollection.get(0).getLastName());
  }

  /**
   * populate the tables with some tests data.
   *
   * @return one of the saved entities
   */
  public SubFacilityVisitEntity populate() {
    SubFacilityVisitEntity result = modelEntityRepository.save(entity);
    SubFacilityVisitEntity agentSmith =
        new SubFacilityVisitEntity(
            defaultSubFacilityVisitDataCollection.get(1).getUserName(),
            defaultSubFacilityVisitDataCollection.get(1).getPii(),
            defaultSubFacilityVisitDataCollection.get(1).getFirstName(),
            defaultSubFacilityVisitDataCollection.get(1).getLastName());
    modelEntityRepository.save(agentSmith);
    SubFacilityVisitEntity maryQuiteContrary =
        new SubFacilityVisitEntity(
            defaultSubFacilityVisitDataCollection.get(2).getUserName(),
            defaultSubFacilityVisitDataCollection.get(2).getPii(),
            defaultSubFacilityVisitDataCollection.get(2).getFirstName(),
            defaultSubFacilityVisitDataCollection.get(2).getLastName());
    modelEntityRepository.save(maryQuiteContrary);

    return result;
  }

  @Test
  public void setupValid() {
    assertThat(modelEntityRepository != null);
  }

  @Test
  public void createAndGetTest() {
    modelEntityRepository.save(entity);

    Optional<SubFacilityVisitEntity> retrievedEntity =
        modelEntityRepository.findByUserName(
            defaultSubFacilityVisitDataCollection.get(0).getUserName());

    assertThat(retrievedEntity.isPresent());
    assertThat(retrievedEntity.get().getFirstName())
        .isEqualTo(defaultSubFacilityVisitDataCollection.get(0).getFirstName());
  }

  @Test
  public void testFindByLastName() {
    populate();

    Page<SubFacilityVisitEntity> retrievedSmiths =
        modelEntityRepository.findByLastName(
            defaultSubFacilityVisitDataCollection.get(0).getLastName(), Pageable.unpaged());

    assertThat(retrievedSmiths.getContent().size()).isEqualTo(2);
  }

  @Test
  public void testFindByLastNamePaged() {
    populate();

    Pageable pageable = PageRequest.of(0, 1);
    Page<SubFacilityVisitEntity> retrievedSmiths =
        modelEntityRepository.findByLastName(
            defaultSubFacilityVisitDataCollection.get(0).getLastName(), pageable);

    assertThat(retrievedSmiths.getContent().size()).isEqualTo(1);
  }

  @Test
  public void testUpdateRecord() {
    final String newName = "Contrary";
    SubFacilityVisitEntity saved = modelEntityRepository.save(entity);
    saved.setLastName(newName);

    SubFacilityVisitEntity updated = modelEntityRepository.save(saved);

    Page<SubFacilityVisitEntity> retrievedSmiths =
        modelEntityRepository.findByLastName(
            defaultSubFacilityVisitDataCollection.get(0).getLastName(), Pageable.unpaged());
    assertThat(retrievedSmiths.getContent().size()).isEqualTo(0);
    Page<SubFacilityVisitEntity> retrievedContrarians =
        modelEntityRepository.findByLastName(newName, Pageable.unpaged());
    assertThat(retrievedContrarians.getContent().size()).isEqualTo(1);
  }

  @Test
  public void testDeleteRecord() {
    SubFacilityVisitEntity saved = populate();

    modelEntityRepository.deleteById(saved.getId());

    Page<SubFacilityVisitEntity> retrievedSmiths =
        modelEntityRepository.findByLastName(
            defaultSubFacilityVisitDataCollection.get(0).getLastName(), Pageable.unpaged());
    assertThat(retrievedSmiths.getContent().size()).isEqualTo(1);
  }

  @Test
  public void testFindAll() {
    populate();
    Page<SubFacilityVisitEntity> retrieved = modelEntityRepository.findAll(Pageable.unpaged());
    assertThat(retrieved.getContent().size()).isEqualTo(3);
  }

  @Test
  public void testFindAllPaged() {
    populate();
    Pageable pageable = PageRequest.of(0, 2);
    Page<SubFacilityVisitEntity> retrieved = modelEntityRepository.findAll(pageable);
    assertThat(retrieved.getContent().size()).isEqualTo(2);
  }
}
