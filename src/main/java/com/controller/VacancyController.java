package com.controller;

import com.db.dao.CompanyDAO;
import com.db.dao.VacancyDAO;
import com.db.entity.Account;
import com.db.entity.Company;
import com.db.entity.Vacancy;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vacancies")
public class VacancyController {

    @Autowired
    private VacancyDAO vacancyDAO;

    @Autowired
    private CompanyDAO companyDAO;

    @GetMapping("/add")
    public String addVacancyForm(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account.isEmployer()) {
            return "redirect:/profile";
        }
        model.addAttribute("title", "Add Vacancy");
        addUserInfoToModel(model, session);
        return "addVacancy";
    }

    @PostMapping("/add")
    public String addVacancy(@RequestParam String position, @RequestParam Integer salary,
                             @RequestParam String requirements, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && !account.isEmployer()) {
            List<Company> companies = companyDAO.findByAccount(account);
            if (!companies.isEmpty()) {
                Company company = companies.getFirst();
                Vacancy vacancy = new Vacancy(null, position, salary, requirements, company, null);
                vacancyDAO.save(vacancy);
            }
        }
        return "redirect:/profile";
    }

    @GetMapping("/edit/{id}")
    public String editVacancyForm(@PathVariable Long id, Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account.isEmployer()) {
            return "redirect:/profile";
        }
        Vacancy vacancy = vacancyDAO.findById(id);
        model.addAttribute("vacancy", vacancy);
        model.addAttribute("title", "Edit Vacancy");
        addUserInfoToModel(model, session);
        return "editVacancy";
    }

    @PostMapping("/edit/{id}")
    public String editVacancy(@PathVariable Long id, @RequestParam String position,
                              @RequestParam Integer salary, @RequestParam String requirements,
                              HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account.isEmployer()) {
            return "redirect:/profile";
        }
        Vacancy vacancy = vacancyDAO.findById(id);
        vacancy.setPosition(position);
        vacancy.setSalary(salary);
        vacancy.setRequirements(requirements);
        vacancyDAO.update(vacancy);
        return "redirect:/profile";
    }

    @GetMapping("/delete/{id}")
    public String deleteVacancy(@PathVariable Long id, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account.isEmployer()) {
            return "redirect:/profile";
        }
        vacancyDAO.deleteById(id);
        return "redirect:/profile";
    }

    @GetMapping("/view/{id}")
    public String viewVacancy(@PathVariable Long id, Model model, HttpSession session) {
        Vacancy vacancy = vacancyDAO.findById(id);
        model.addAttribute("vacancy", vacancy);
        model.addAttribute("title", "Vacancy Details");
        Account account = (Account) session.getAttribute("loggedInUser");
        model.addAttribute("isEmployer", account != null && account.isEmployer());
        addUserInfoToModel(model, session);
        return "viewVacancy";
    }

    @GetMapping("/search")
    public String searchVacancies(@RequestParam(required = false) String company,
                                  @RequestParam(required = false) String position,
                                  @RequestParam(required = false) Integer minSalary,
                                  @RequestParam(required = false) Integer maxSalary, Model model, HttpSession session) {
        List<String> companies = companyDAO.findAll().stream()
                .map(Company::getName)
                .collect(Collectors.toList());
        List<String> positions = vacancyDAO.findAll().stream()
                .map(Vacancy::getPosition)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("companies", companies);
        model.addAttribute("positions", positions);
        model.addAttribute("title", "Search Vacancies");
        addUserInfoToModel(model, session);

        List<Vacancy> vacancies = vacancyDAO.findAll();

        if (company != null && !company.isEmpty()) {
            vacancies = vacancies.stream()
                    .filter(v -> v.getCompany().getName().equals(company))
                    .collect(Collectors.toList());
        }
        if (position != null && !position.isEmpty()) {
            vacancies = vacancies.stream()
                    .filter(v -> v.getPosition().equals(position))
                    .collect(Collectors.toList());
        }
        if (minSalary != null) {
            vacancies = vacancies.stream()
                    .filter(v -> v.getSalary() >= minSalary)
                    .collect(Collectors.toList());
        }
        if (maxSalary != null) {
            vacancies = vacancies.stream()
                    .filter(v -> v.getSalary() <= maxSalary)
                    .collect(Collectors.toList());
        }

        model.addAttribute("vacancies", vacancies);
        return "searchVacancies";
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
