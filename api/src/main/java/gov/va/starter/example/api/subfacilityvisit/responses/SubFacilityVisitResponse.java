package gov.va.starter.example.api.subfacilityvisit.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
// @RequiredArgsConstructor
@Getter
@Schema(
    name = "SubFacilityVisitResponse",
    description = "Metadata describing an SubFacilityVisit resource and unique identifier")
public class SubFacilityVisitResponse {

  @NonNull
  @Schema(
      description = "unique id of the SubFacilityVisit resource",
      example = "dd373780-79fb-4285-8c9b-bf48a8014a68")
  private final String id;

  @NonNull
  @Schema(description = "username of the SubFacilityVisit holder", example = "lvanpelt")
  private final String userName;

  @NonNull
  @Schema(
      description = "Representative PII of the SubFacilityVisit holder",
      example = "123-456-7890")
  private final String pii;

  @NonNull
  @Schema(description = "Given name of the SubFacilityVisit holder", example = "Lucy")
  private final String firstName;

  @NonNull
  @Schema(description = "Family name of the SubFacilityVisit holder", example = "van Pelt")
  private final String lastName;

  @NonNull
  @Schema(
      description = "Constructed full name (given + family) of the SubFacilityVisit holder",
      example = "Lucy van Pelt")
  private final String fullName;
}
