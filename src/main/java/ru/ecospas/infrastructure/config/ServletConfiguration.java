package ru.ecospas.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ecospas.web.asf.*;
import ru.ecospas.web.auth.CheckLoginServlet;
import ru.ecospas.web.object.*;
import ru.ecospas.web.organization.DeleteOrganizationServlet;
import ru.ecospas.web.organization.OrganizationServlet;
import ru.ecospas.web.organization.OrganizationsServlet;
import ru.ecospas.web.organization.SaveOrganizationServlet;
import ru.ecospas.web.plan.DownloadPlanServlet;
import ru.ecospas.web.plan.GeneratePlanServlet;
import ru.ecospas.web.region.CreateEmptyRegionServlet;
import ru.ecospas.web.region.DeleteRegionServlet;
import ru.ecospas.web.region.RegionServlet;
import ru.ecospas.web.region.SaveRegionServlet;
import ru.ecospas.web.substance.CreateEmptyHazardousSubstanceServlet;
import ru.ecospas.web.substance.DeleteHazardousSubstanceServlet;
import ru.ecospas.web.substance.HazardousSubstanceServlet;
import ru.ecospas.web.substance.SaveHazardousSubstanceServlet;
import ru.ecospas.web.type.CreateEmptyObjectTypeServlet;
import ru.ecospas.web.type.DeleteObjectTypeServlet;
import ru.ecospas.web.type.ObjectTypeServlet;
import ru.ecospas.web.type.SaveObjectTypeServlet;
import ru.ecospas.web.user.*;

@Configuration
@RequiredArgsConstructor
public class ServletConfiguration {

    private final GeneratePlanServlet generatePlanServlet;
    private final AsfServlet asfServlet;
    private final CreateEmptyAsfServlet createEmptyAsfServlet;
    private final DeleteAsfImageServlet deleteAsfImageServlet;
    private final GetAsfSignersServlet getAsfSignersServlet;
    private final SaveAsfServlet saveAsfServlet;
    private final UploadAsfImageServlet uploadAsfImageServlet;
    private final CreateEmptyObjectServlet createEmptyObjectServlet;
    private final DeleteObjectServlet deleteObjectServlet;
    private final ObjectServlet objectServlet;
    private final ObjectsServlet objectsServlet;
    private final SaveObjectServlet saveObjectServlet;
    private final UploadObjectImageServlet uploadObjectImageServlet;
    private final DeleteOrganizationServlet deleteOrganizationServlet;
    private final OrganizationServlet organizationServlet;
    private final OrganizationsServlet organizationsServlet;
    private final SaveOrganizationServlet saveOrganizationServlet;
    private final DownloadPlanServlet downloadPlanServlet;
    private final CreateEmptyRegionServlet createEmptyRegionServlet;
    private final DeleteRegionServlet deleteRegionServlet;
    private final RegionServlet regionServlet;
    private final SaveRegionServlet saveRegionServlet;
    private final DeleteObjectTypeServlet deleteObjectTypeServlet;
    private final CreateEmptyObjectTypeServlet createEmptyObjectTypeServlet;
    private final ObjectTypeServlet objectTypeServlet;
    private final SaveObjectTypeServlet saveObjectTypeServlet;
    private final CreateEmptyHazardousSubstanceServlet createEmptyHazardousSubstanceServlet;
    private final DeleteHazardousSubstanceServlet deleteHazardousSubstanceServlet;
    private final HazardousSubstanceServlet hazardousSubstanceServlet;
    private final SaveHazardousSubstanceServlet saveHazardousSubstanceServlet;
    private final CheckLoginServlet checkLoginServlet;
    private final UsersServlet usersServlet;
    private final UserServlet userServlet;
    private final SaveUserServlet saveUserServlet;
    private final DeleteUserServlet deleteUserServlet;
    private final CreateEmptyUserServlet createEmptyUserServlet;

    @Bean
    public ServletRegistrationBean<GeneratePlanServlet> generatePlanRegistration() {
        return new ServletRegistrationBean<>(generatePlanServlet, "/generate-plan");
    }

    @Bean
    public ServletRegistrationBean<AsfServlet> asfRegistration() {
        return new ServletRegistrationBean<>(asfServlet, "/asf");
    }

    @Bean
    public ServletRegistrationBean<CreateEmptyAsfServlet> createEmptyAsfRegistration() {
        return new ServletRegistrationBean<>(createEmptyAsfServlet, "/create-empty-asf");
    }

    @Bean
    public ServletRegistrationBean<DeleteAsfImageServlet> deleteAsfImageRegistration() {
        return new ServletRegistrationBean<>(deleteAsfImageServlet, "/delete-asf-image");
    }

    @Bean
    public ServletRegistrationBean<GetAsfSignersServlet> getAsfSignersRegistration() {
        return new ServletRegistrationBean<>(getAsfSignersServlet, "/get-asf-signers");
    }

    @Bean
    public ServletRegistrationBean<SaveAsfServlet> saveAsfRegistration() {
        return new ServletRegistrationBean<>(saveAsfServlet, "/save-asf");
    }

    @Bean
    public ServletRegistrationBean<UploadAsfImageServlet> uploadAsfImageRegistration() {
        return new ServletRegistrationBean<>(uploadAsfImageServlet, "/upload-asf-image");
    }

    @Bean
    public ServletRegistrationBean<CreateEmptyObjectServlet> createEmptyObjectRegistration() {
        return new ServletRegistrationBean<>(createEmptyObjectServlet, "/create-empty-object");
    }

    @Bean
    public ServletRegistrationBean<DeleteObjectServlet> deleteObjectRegistration() {
        return new ServletRegistrationBean<>(deleteObjectServlet, "/delete-object");
    }

    @Bean
    public ServletRegistrationBean<ObjectServlet> objectRegistration() {
        return new ServletRegistrationBean<>(objectServlet, "/object");
    }

    @Bean
    public ServletRegistrationBean<ObjectsServlet> objectsRegistration() {
        return new ServletRegistrationBean<>(objectsServlet, "/objects");
    }

    @Bean
    public ServletRegistrationBean<SaveObjectServlet> saveObjectRegistration() {
        return new ServletRegistrationBean<>(saveObjectServlet, "/save-object");
    }

    @Bean
    public ServletRegistrationBean<UploadObjectImageServlet> uploadObjectImageRegistration() {
        return new ServletRegistrationBean<>(uploadObjectImageServlet, "/upload-object-image");
    }

    @Bean
    public ServletRegistrationBean<DeleteOrganizationServlet> deleteOrganizationRegistration() {
        return new ServletRegistrationBean<>(deleteOrganizationServlet, "/delete-organization");
    }

    @Bean
    public ServletRegistrationBean<OrganizationServlet> organizationRegistration() {
        return new ServletRegistrationBean<>(organizationServlet, "/organization");
    }

    @Bean
    public ServletRegistrationBean<OrganizationsServlet> organizationsRegistration() {
        return new ServletRegistrationBean<>(organizationsServlet, "/organizations");
    }

    @Bean
    public ServletRegistrationBean<SaveOrganizationServlet> saveOrganizationRegistration() {
        return new ServletRegistrationBean<>(saveOrganizationServlet, "/save-organization");
    }

    @Bean
    public ServletRegistrationBean<DownloadPlanServlet> downloadPlanRegistration() {
        return new ServletRegistrationBean<>(downloadPlanServlet, "/download-plan");
    }

    @Bean
    public ServletRegistrationBean<CreateEmptyRegionServlet> createEmptyRegionRegistration() {
        return new ServletRegistrationBean<>(createEmptyRegionServlet, "/create-empty-region");
    }

    @Bean
    public ServletRegistrationBean<DeleteRegionServlet> deleteRegionRegistration() {
        return new ServletRegistrationBean<>(deleteRegionServlet, "/delete-region");
    }

    @Bean
    public ServletRegistrationBean<RegionServlet> regionRegistration() {
        return new ServletRegistrationBean<>(regionServlet, "/region");
    }

    @Bean
    public ServletRegistrationBean<SaveRegionServlet> saveRegionRegistration() {
        return new ServletRegistrationBean<>(saveRegionServlet, "/save-region");
    }

    @Bean
    public ServletRegistrationBean<DeleteObjectTypeServlet> deleteObjectTypeRegistration() {
        return new ServletRegistrationBean<>(deleteObjectTypeServlet, "/delete-object-type");
    }

    @Bean
    public ServletRegistrationBean<CreateEmptyObjectTypeServlet> createEmptyObjectTypeRegistration() {
        return new ServletRegistrationBean<>(createEmptyObjectTypeServlet, "/create-empty-object-type");
    }

    @Bean
    public ServletRegistrationBean<ObjectTypeServlet> objectTypeRegistration() {
        return new ServletRegistrationBean<>(objectTypeServlet, "/object-type");
    }

    @Bean
    public ServletRegistrationBean<SaveObjectTypeServlet> saveObjectTypeRegistration() {
        return new ServletRegistrationBean<>(saveObjectTypeServlet, "/save-object-type");
    }

    @Bean
    public ServletRegistrationBean<CreateEmptyHazardousSubstanceServlet> createEmptyHazardousSubstanceRegistration() {
        return new ServletRegistrationBean<>(createEmptyHazardousSubstanceServlet, "/create-empty-hazardous-substance");
    }

    @Bean
    public ServletRegistrationBean<DeleteHazardousSubstanceServlet> deleteHazardousSubstanceRegistration() {
        return new ServletRegistrationBean<>(deleteHazardousSubstanceServlet, "/delete-hazardous-substance");
    }

    @Bean
    public ServletRegistrationBean<HazardousSubstanceServlet> hazardousSubstanceRegistration() {
        return new ServletRegistrationBean<>(hazardousSubstanceServlet, "/hazardous-substance");
    }

    @Bean
    public ServletRegistrationBean<SaveHazardousSubstanceServlet> saveHazardousSubstanceRegistration() {
        return new ServletRegistrationBean<>(saveHazardousSubstanceServlet, "/save-hazardous-substance");
    }

    @Bean
    public ServletRegistrationBean<CheckLoginServlet> checkLoginRegistration() {
        return new ServletRegistrationBean<>(checkLoginServlet, "/check-login");
    }

    @Bean
    public ServletRegistrationBean<UsersServlet> usersRegistration() {
        return new ServletRegistrationBean<>(usersServlet, "/users");
    }

    @Bean
    public ServletRegistrationBean<UserServlet> userRegistration() {
        return new ServletRegistrationBean<>(userServlet, "/user");
    }

    @Bean
    public ServletRegistrationBean<SaveUserServlet> saveUserRegistration() {
        return new ServletRegistrationBean<>(saveUserServlet, "/save-user");
    }

    @Bean
    public ServletRegistrationBean<DeleteUserServlet> deleteUserRegistration() {
        return new ServletRegistrationBean<>(deleteUserServlet, "/delete-user");
    }

    @Bean
    public ServletRegistrationBean<CreateEmptyUserServlet> createEmptyUserRegistration() {
        return new ServletRegistrationBean<>(createEmptyUserServlet, "/create-empty-user");
    }
}