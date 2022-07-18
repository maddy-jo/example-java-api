package gov.va.starter.example.facilityvisit.factory;

import gov.va.starter.boot.test.data.provider.NamedDataFactory;
import gov.va.starter.example.facilityvisit.model.FacilityVisitData;

import java.util.Arrays;

public class FacilityVisitFactory extends NamedDataFactory<FacilityVisitData> {
  public FacilityVisitFactory() {
    data.put(
        DEFAULT_SPEC,
        new FacilityVisitData(
            "defaultId",
            "defaultUserName",
            "defaultPii",
            "defaultFirstName",
            "defaultLastName",
            "defaultFirstName defaultLastName"));
    collections.put(
        DEFAULT_SPEC,
        Arrays.asList(
            new FacilityVisitData(
                "defaultId",
                "defaultUserName",
                "defaultPii",
                "defaultFirstName",
                "defaultLastName",
                "defaultFirstName defaultLastName"),
            new FacilityVisitData(
                "defaultId2",
                "defaultUserName2",
                "defaultPii2",
                "defaultFirstName2",
                "defaultLastName2",
                "defaultFirstName2 defaultLastName2"),
            new FacilityVisitData(
                "defaultId3",
                "defaultUserName3",
                "defaultPii3",
                "defaultFirstName3",
                "defaultLastName3",
                "defaultFirstName3 defaultLastName3")));

    data.put(
        "bogus",
        new FacilityVisitData(
            "bogusId",
            "bogusUserName",
            "bogusPii",
            "bogusFirstName",
            "bogusLastName",
            "bogusFirstName bogusLastName"));
    collections.put(
        "bogus",
        Arrays.asList(
            new FacilityVisitData(
                "bogusId",
                "bogusUserName",
                "bogusPii",
                "bogusFirstName",
                "bogusLastName",
                "bogusFirstName bogusLastName"),
            new FacilityVisitData(
                "bogusId2",
                "bogusUserName2",
                "bogusPii2",
                "bogusFirstName2",
                "bogusLastName2",
                "bogusFirstName2 bogusLastName2"),
            new FacilityVisitData(
                "bogusId3",
                "bogusUserName3",
                "bogusPii3",
                "bogusFirstName3",
                "bogusLastName3",
                "bogusFirstName3 bogusLastName3")));

    data.put(
        "duplicateLastName",
        new FacilityVisitData(
            "dupId", "dupUserName", "dupPii", "dupFirstName", "Smith", "dupFirstName Smith"));
    collections.put(
        "duplicateLastName",
        Arrays.asList(
            new FacilityVisitData(
                "dupId", "dupUserName", "dupPii", "dupFirstName", "Smith", "dupFirstName Smith"),
            new FacilityVisitData(
                "dupId2",
                "dupUserName2",
                "dupPii2",
                "dupFirstName2",
                "Smith",
                "dupFirstName2 Smith"),
            new FacilityVisitData(
                "dupId3",
                "dupUserName3",
                "dupPii3",
                "dupFirstName3",
                "dupLastName3",
                "dupFirstName3 dupLastName3")));
  }
}
