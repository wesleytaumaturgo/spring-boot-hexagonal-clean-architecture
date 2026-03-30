package com.wesleytaumaturgo.hexagonal.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

// REQ-6.EARS-1, REQ-6.EARS-2, REQ-6.EARS-3
@AnalyzeClasses(packages = "com.wesleytaumaturgo.hexagonal")
class ArchUnitTest {

    @ArchTest
    // REQ-6.EARS-1: domain não importa infrastructure
    static final ArchRule domainShouldNotDependOnInfrastructure =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

    @ArchTest
    // REQ-6.EARS-3: domain não importa application
    static final ArchRule domainShouldNotDependOnApplication =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAPackage("..application..");

    @ArchTest
    // REQ-6.EARS-2: domain não usa annotations Spring
    static final ArchRule domainShouldNotUseSpringStereotypes =
            noClasses().that().resideInAPackage("..domain..")
                    .should().beAnnotatedWith("org.springframework.stereotype.Component")
                    .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
                    .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
                    .orShould().beAnnotatedWith("org.springframework.stereotype.Controller");

    @ArchTest
    // REQ-6.EARS-4: application não importa infrastructure
    static final ArchRule applicationShouldNotDependOnInfrastructure =
            noClasses().that().resideInAPackage("..application..")
                    .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
}
