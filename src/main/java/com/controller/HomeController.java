package com.controller;

import com.db.dao.AccountDAO;
import com.db.dao.CityDAO;
import com.db.dao.CompanyDAO;
import com.db.dao.PersonDAO;
import com.db.entity.Account;
import com.db.entity.City;
import com.db.entity.Company;
import com.db.entity.Person;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private CityDAO cityDAO;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("title", "Home");
        addUserInfoToModel(model, session);
        return "index";
    }

    @GetMapping("/login")
    public String loginForm(Model model, HttpSession session) {
        model.addAttribute("title", "Login");
        addUserInfoToModel(model, session);
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return "redirect:/profile";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        List<Account> accounts = accountDAO.findByEmail(email);
        if (!accounts.isEmpty() && accounts.get(0).getPassword().equals((long) password.hashCode())) {
            session.setAttribute("loggedInUser", accounts.get(0));
            return "redirect:/profile";
        } else {
            model.addAttribute("errorMessage", "Invalid email or password");
            model.addAttribute("title", "Login");
            addUserInfoToModel(model, session);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerForm(Model model, HttpSession session) {
        model.addAttribute("title", "Register");
        addUserInfoToModel(model, session);
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return "redirect:/profile";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password,
                           @RequestParam String name, @RequestParam String birthDate,
                           @RequestParam String city, @RequestParam String phone,
                           @RequestParam boolean isEmployer, Model model, HttpSession session) {
        LocalDate dob;
        int age = 19;
        dob = LocalDate.parse(birthDate);
        age = Period.between(dob, LocalDate.now()).getYears();

        if (age < 18) {
            model.addAttribute("errorMessage", "You must be at least 18 years old to register.");
            model.addAttribute("title", "Home");
            addUserInfoToModel(model, session);
            return "register";
        }

        List<Account> existingAccounts = accountDAO.findByEmail(email);
        if (!existingAccounts.isEmpty()) {
            model.addAttribute("errorMessage", "Email already registered.");
            model.addAttribute("title", "Home");
            addUserInfoToModel(model, session);
            return "register";
        }

        Account account = new Account(null, email, (long) password.hashCode(), isEmployer);
        accountDAO.save(account);
        session.setAttribute("loggedInUser", account);

        if (!isEmployer) {
            Company company = new Company(null, name, account);
            companyDAO.save(company);
        } else {
            City city1 = cityDAO.findByName(city).getFirst();
            if (city1 == null) {
                city1 = new City(null, city);
                cityDAO.save(city1);
            }
            Person person = new Person(null, city1, name, phone, true, age, account);
            personDAO.save(person);
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
