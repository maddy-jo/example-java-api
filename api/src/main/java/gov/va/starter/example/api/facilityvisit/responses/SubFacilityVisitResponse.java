package gov.va.starter.example.api.facilityvisit.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
// @RequiredArgsConstructor
@Getter
@Schema(
    name = "SubFacilityVisitResponse",
    description = "Metadata describing an Account resource and unique identifier")
public class SubFacilityVisitResponse {

  @NonNull
  @Schema(description = "unique id of the SubFacilityVisit resource")
  private final String id;

  @NonNull
  @Schema(description = "username of the SubFacilityVisit holder")
  private final String userName;

  @NonNull
  @Schema(description = "Given name of the SubFacilityVisit holder")
  private final String firstName;

  @NonNull
  @Schema(description = "Family name of the SubFacilityVisit holder")
  private final String lastName;
}
