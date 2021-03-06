package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 10.07.15.
 */
public class AddRolesToUserTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private AdministrationPage administrationPage;
    private ManageUserPage manageUserPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);

        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        administrationPage = new AdministrationPage(driver);
        manageUserPage = new ManageUserPage(driver);
    }


    private void reLoginAsUser() throws InterruptedException {
        manageUserPage.clickOnHomeLink();
        headerPage.logOut();
        loginPage.login("super_nurse","Nurse321");
    }

    private void reLoginAsAdmin() throws InterruptedException {
        headerPage.logOut();
        loginPage.loginAsAdmin();
    }

    @Ignore
    @Test
    public void addRolesToUserTest() throws InterruptedException {
        homePage.goToAdministration();
        administrationPage.clickOnManageUsers();

        Map<String,Integer> roleModules = new HashMap();

        fillInRoleModules(roleModules);
        if(!manageUserPage.userExists("super_nurse")) {
            manageUserPage.clickOnAddUser();
            manageUserPage.createNewPerson();
            manageUserPage.fillInPersonName("Super", "Nurse", "super_nurse", "Nurse321");
        }
        String oldRole = null;
        for(Entry<String, Integer> role : roleModules.entrySet()) {
            manageUserPage.assignRolesToUser(oldRole,role.getKey(),"super_nurse");
            reLoginAsUser();
            assertThat(homePage.numberOfAppsPresent(), is(role.getValue()));
            reLoginAsAdmin();
            oldRole = role.getKey();
            homePage.goToAdministration();
            administrationPage.clickOnManageUsers();

        }

    }

    private void fillInRoleModules(Map<String, Integer> roleModules) {
        roleModules.put("roleStrings.Anonymous",1);
        roleModules.put("roleStrings.Application:ConfiguresAppointmentScheduling",2);
        roleModules.put("roleStrings.Application:EntersADTEvents",3);
        roleModules.put("roleStrings.Application:HasSuperUserPrivileges",10);
        roleModules.put("roleStrings.Application:RegistersPatients",4);
        roleModules.put("roleStrings.Application:SchedulesAndOverbooksAppointments",2);
        roleModules.put("roleStrings.Application:SeesAppointmentSchedule",2);
        roleModules.put("roleStrings.Application:UsesPatientSummary",2);
        roleModules.put("roleStrings.Authenticated",1);
        roleModules.put("roleStrings.Organizational:SystemAdministrator",3);
        roleModules.put("roleStrings.SystemDeveloper",10);
        roleModules.put("roleStrings.Application:AdministersSystem",2);
        roleModules.put("roleStrings.Application:ConfiguresForms",1);
        roleModules.put("roleStrings.Application:EntersVitals",3);
        roleModules.put("roleStrings.Application:ManagesAtlas",2);
        roleModules.put("roleStrings.Application:ConfiguresMetadata",1);
        roleModules.put("roleStrings.Application:ManagesProviderSchedules",2);
        roleModules.put("roleStrings.Application:RequestsAppointments",1);
        roleModules.put("roleStrings.Application:SchedulesAppointments",2);
        roleModules.put("roleStrings.Application:UsesCaptureVitalsApp",2);
        roleModules.put("roleStrings.Application:WritesClinicalNotes", 3);
        roleModules.put("roleStrings.Organizational:Doctor", 4);
        roleModules.put("roleStrings.Organizational:Nurse", 5);
        roleModules.put("roleStrings.Organizational:RegistrationClerk", 5);
    }
    @After
    public void tearDown() throws Exception {
        manageUserPage.removeUser("super_nurse");
        manageUserPage.clickOnHomeLink();
        headerPage.logOut();
    }
}
