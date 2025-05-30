package com.controller;

import com.db.dao.CompanyDAO;
import com.db.dao.ResponseDAO;
import com.db.dao.PersonDAO;
import com.db.dao.VacancyDAO;
import com.db.entity.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/responses")
public class ResponseController {

    @Autowired
    private ResponseDAO responseDAO;

    @Autowired
    private VacancyDAO vacancyDAO;

    @Autowired
    private PersonDAO personDAO;
    @Autowired
    private CompanyDAO companyDAO;

    @GetMapping("/apply/{id}")
    public String applyForVacancy(@PathVariable Long id, HttpSession session) {
        Vacancy vacancy = vacancyDAO.findById(id);
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && account.isEmployer()) {
            List<Person> persons = personDAO.findByAccount(account);
            if (!persons.isEmpty()) {
                Person person = persons.get(0);
                Response response = new Response(null, LocalDate.now(), "Pending", person, vacancy);
                responseDAO.save(response);
            }
        }
        return "redirect:/vacancies/view/" + id;
    }

    @PostMapping("/updateStatus")
    public String updateresponseStatus(@ModelAttribute("responseId") Long responseId, @RequestParam String status, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && !account.isEmployer()) {
            Response response = responseDAO.findById(responseId);
            response.setStatus(status);
            responseDAO.update(response);
        }
        return "redirect:/profile";
    }
}
