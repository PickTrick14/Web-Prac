package com.controller;

import com.db.dao.*;
import com.db.entity.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private ResumeDAO resumeDAO;

    @Autowired
    private EducationDAO educationDAO;

    @Autowired
    private ExperienceDAO experienceDAO;

    @Autowired
    private ResponseDAO responseDAO;

    @Autowired
    private VacancyDAO vacancyDAO;
    @Autowired
    private CityDAO cityDAO;

    @GetMapping
    public String profile(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Profile");
        model.addAttribute("isEmployer", account.isEmployer());
        addUserInfoToModel(model, session);

        if (!account.isEmployer()) {
            List<Company> companies = companyDAO.findByAccount(account);
            if (!companies.isEmpty()) {
                Company company = companies.getFirst();
                List<Vacancy> vacancies = vacancyDAO.findByCompany(company);
                List<Response> responsies = new ArrayList<>();
                if (!vacancies.isEmpty()) {
                    for (Vacancy vacancy : vacancies) {
                        List<Response> temp = responseDAO.findByVacancy(vacancy);
                        responsies.addAll(temp);
                    }
                }
                model.addAttribute("company", company);
                model.addAttribute("vacancies", vacancies);
                model.addAttribute("employerResponses", responsies);
            } else {
                model.addAttribute("company", new Company());
            }
        } else {
            List<Person> persons = personDAO.findByAccount(account);
            if (!persons.isEmpty()) {
                Person person = persons.get(0);
                model.addAttribute("person", person);
                model.addAttribute("resumes", resumeDAO.findByPerson(person));
                model.addAttribute("educations", educationDAO.findByPerson(person));
                model.addAttribute("experiences", experienceDAO.findByPerson(person));
                model.addAttribute("seekerResponses", responseDAO.findByPerson(person));
            } else {
                model.addAttribute("person", new Person());
            }
        }
        return "profile";
    }

    @PostMapping("/updateCompany")
    public String updateCompany(@RequestParam String companyName, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && !account.isEmployer()) {
            List<Company> companies = companyDAO.findByName(companyName);
            if (!companies.isEmpty()) {
                Company company = companies.getFirst();
                company.setName(companyName);
                companyDAO.update(company);
            }
        }
        return "redirect:/profile";
    }

    @PostMapping("/updatePerson")
    public String updatePerson(@RequestParam String name, @RequestParam Integer age,
                               @RequestParam String city, @RequestParam String contactInfo,
                               @RequestParam Boolean isSearching, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && account.isEmployer()) {
            List<Person> persons = personDAO.findByAccount(account);
            if (!persons.isEmpty()) {
                Person person = persons.getFirst();
                person.setName(name);
                person.setAge(age);
                City cityObj = cityDAO.findByName(city).isEmpty() ? null : cityDAO.findByName(city).getFirst();
                person.setCity(cityObj);
                person.setContactInfo(contactInfo);
                person.setSearching(isSearching);
                personDAO.update(person);
            }
        }
        return "redirect:/profile";
    }

    @PostMapping("/addEducation")
    public String addEducation(@RequestParam String institution, @RequestParam String specialization,
                               @RequestParam Integer endYear, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && account.isEmployer()) {
            List<Person> persons = personDAO.findByAccount(account);
            if (!persons.isEmpty()) {
                Person person = persons.get(0);
                Education education = new Education(null, institution, specialization, endYear, person);
                educationDAO.save(education);
            }
        }
        return "redirect:/profile";
    }

    @PostMapping("/addExperience")
    public String addExperience(@RequestParam String company, @RequestParam String position,
                                @RequestParam(required = false) Double salary, @RequestParam String startDate,
                                @RequestParam String endDate, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && account.isEmployer()) {
            List<Person> persons = personDAO.findByAccount(account);
            if (!persons.isEmpty()) {
                Person person = persons.get(0);
                List<Company> companies = companyDAO.findByName(company);
                Company company11;
                if (companies.isEmpty()) {
                    Account DEFAULT = new Account(null, "DEFAULT", 0L, false);
                    accountDAO.save(DEFAULT);
                    company11 = new Company(null, company, DEFAULT);
                    companyDAO.save(company11);
                } else {
                    company11 = companies.get(0);
                }
                Experience experience = new Experience(null, position, salary != null ? salary : 0,
                        LocalDate.parse(startDate), LocalDate.parse(endDate), person, company11);
                experienceDAO.save(experience);
            }
        }
        return "redirect:/profile";
    }

    private void addUserInfoToModel(Model model, HttpSession session) {
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("userEmail", loggedInUser.getEmail());
            model.addAttribute("isEmployer", loggedInUser.isEmployer());
        } else {
            model.addAttribute("loggedIn", false);
        }
    }
}
