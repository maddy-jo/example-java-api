package gov.va.starter.example.api.subfacilityvisit.resources;

import gov.va.starter.boot.exception.RequestValidationException;
import gov.va.starter.boot.exception.ResourceNotFoundException;
import gov.va.starter.example.api.responses.PagedResponse;
import gov.va.starter.example.api.subfacilityvisit.requests.SubFacilityVisitRequest;
import gov.va.starter.example.api.subfacilityvisit.responses.PagedSubFacilityVisitResponse;
import gov.va.starter.example.api.subfacilityvisit.responses.SubFacilityVisitResponse;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.Problem;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping(value = "/v1/example/subfacilityvisits", produces = "application/json")
@Tag(
    name = "SubFacilityVisit API",
    description = "Starter Kit template API, essentially CRUD access")
@SecurityRequirement(name = "bearer-jwt")
@Timed
public interface SubFacilityVisitResource {

  @Operation(
      summary = "Create SubFacilityVisit",
      description = "Create a new SubFacilityVisit from the demographic information provided")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Created a new SubFacilityVisit",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = SubFacilityVisitResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid data provided",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Not authorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class)))
      })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Timed(value = "example.subfacilityvisits.create")
  ResponseEntity<SubFacilityVisitResponse> addEntity(
      @Parameter(
              description = "metadata for new SubFacilityVisit resource. Cannot null or empty.",
              required = true,
              schema = @Schema(implementation = SubFacilityVisitRequest.class))
          @Valid
          @RequestBody
          SubFacilityVisitRequest request)
      throws RequestValidationException;

  @Operation(
      summary = "Find SubFacilityVisit",
      description = "Find a specific SubFacilityVisit by id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the SubFacilityVisit",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = SubFacilityVisitResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id supplied",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Not authorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class)))
      })
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Timed(value = "example.subfacilityvisits.findById")
  ResponseEntity<SubFacilityVisitResponse> findEntityById(
      @Parameter(
              description =
                  "unique identifier for SubFacilityVisit resource. Cannot null or empty.",
              example = "uuid",
              required = true)
          @NotNull
          @PathVariable(value = "id")
          String id)
      throws ResourceNotFoundException;

  @Operation(summary = "Get SubFacilityVisits", description = "Get all existing SubFacilityVisits")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found all existing SubFacilityVisits",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = PagedSubFacilityVisitResponse.class))
            }),
        @ApiResponse(
            responseCode = "401",
            description = "Not authorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class)))
      })
  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  @Timed(value = "example.subfacilityvisits.findAll")
  ResponseEntity<PagedResponse<SubFacilityVisitResponse>> findEntities(
      @Parameter(
              description = "Paging specification for retrieving a subset of the full list.",
              example = "{\"page\": 0, \"size\": 10, \"sort\":[\"id\"]}",
              required = false)
          Pageable pageable);

  @Operation(
      summary = "Update SubFacilityVisit",
      description = "Update info for an existing SubFacilityVisit")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Updated SubFacilityVisit info",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = SubFacilityVisitResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid entity",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Not authorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "404",
            description = "SubFacilityVisit not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class)))
      })
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Timed(value = "example.subfacilityvisits.update")
  ResponseEntity<SubFacilityVisitResponse> updateEntityById(
      @Parameter(
              description =
                  "unique identifier for SubFacilityVisit resource. Cannot be null or empty",
              example = "uuid",
              required = true)
          @NotNull
          @PathVariable(value = "id")
          String id,
      @Parameter(
              description = "updated metadata SubFacilityVisit resource. Cannot null or empty.",
              required = true,
              schema = @Schema(implementation = SubFacilityVisitRequest.class))
          @Valid
          @RequestBody
          SubFacilityVisitRequest request)
      throws ResourceNotFoundException, RequestValidationException;

  @Operation(
      summary = "Delete SubFacilityVisit",
      description = "Delete an existing SubFacilityVisit by id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Deleted SubFacilityVisit info",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = SubFacilityVisitResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id supplied",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Not authorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))),
        @ApiResponse(
            responseCode = "404",
            description = "SubFacilityVisit not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class)))
      })
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Timed(value = "example.subfacilityvisits.delete")
  ResponseEntity<SubFacilityVisitResponse> deleteEntityById(
      @Parameter(
              description =
                  "unique identifier for SubFacilityVisit resource. Cannot null or empty.",
              example = "uuid",
              required = true)
          @NotNull
          @PathVariable(value = "id")
          String id)
      throws ResourceNotFoundException;
}
