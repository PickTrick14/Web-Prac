package com.controller;

import com.db.dao.EducationDAO;
import com.db.dao.ExperienceDAO;
import com.db.dao.PersonDAO;
import com.db.dao.ResumeDAO;
import com.db.entity.Account;
import com.db.entity.Education;
import com.db.entity.Experience;
import com.db.entity.Person;
import com.db.entity.Resume;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/resumes")
public class ResumeController {

    @Autowired
    private ResumeDAO resumeDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private EducationDAO educationDAO;

    @Autowired
    private ExperienceDAO experienceDAO;

    @GetMapping("/add")
    public String addResumeForm(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (!account.isEmployer()) {
            return "redirect:/profile";
        }
        model.addAttribute("title", "Add Resume");
        addUserInfoToModel(model, session);
        return "addResume";
    }

    @PostMapping("/add")
    public String addResume(@RequestParam String wantedPosition, @RequestParam Integer wantedSalary,
                            @RequestParam String skills, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (account != null && account.isEmployer()) {
            List<Person> persons = personDAO.findByAccount(account);
            if (!persons.isEmpty()) {
                Person person = persons.get(0);
                Resume resume = new Resume(null, wantedPosition, wantedSalary, skills, person);
                resumeDAO.save(resume);
            }
        }
        return "redirect:/profile";
    }

    @GetMapping("/edit/{id}")
    public String editResumeForm(@PathVariable Long id, Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (!account.isEmployer()) {
            return "redirect:/profile";
        }
        Resume resume = resumeDAO.findById(id);
        model.addAttribute("resume", resume);
        model.addAttribute("title", "Edit Resume");
        addUserInfoToModel(model, session);
        return "editResume";
    }

    @PostMapping("/edit/{id}")
    public String editResume(@PathVariable Long id, @RequestParam String wantedPosition,
                             @RequestParam Integer wantedSalary, @RequestParam String skills,
                             HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (!account.isEmployer()) {
            return "redirect:/profile";
        }
        Resume resume = resumeDAO.findById(id);
        resume.setWantedPosition(wantedPosition);
        resume.setWantedSalary(wantedSalary);
        resume.setSkills(skills);
        resumeDAO.update(resume);
        return "redirect:/profile";
    }

    @GetMapping("/delete/{id}")
    public String deleteResume(@PathVariable Long id, HttpSession session) {
        Account account = (Account) session.getAttribute("loggedInUser");
        if (!account.isEmployer()) {
            return "redirect:/profile";
        }
        resumeDAO.deleteById(id);
        return "redirect:/profile";
    }

    @GetMapping("/view/{id}")
    public String viewResume(@PathVariable Long id, Model model, HttpSession session) {
        Resume resume = resumeDAO.findById(id);
        Person person = resume.getPerson();
        model.addAttribute("resume", resume);
        model.addAttribute("educations", educationDAO.findByPerson(person));
        model.addAttribute("experiences", experienceDAO.findByPerson(person));
        model.addAttribute("title", "Resume Details");
        addUserInfoToModel(model, session);
        return "viewResume";
    }

    @GetMapping("/search")
    public String searchResumes(@RequestParam(required = false) String institution,
                                @RequestParam(required = false) String specialization,
                                @RequestParam(required = false) String position,
                                @RequestParam(required = false) String company,
                                @RequestParam(required = false) Integer minSalary,
                                @RequestParam(required = false) Integer maxSalary, Model model, HttpSession session) {
        List<String> institutions = educationDAO.findAll().stream()
                .map(Education::getInstitution)
                .distinct()
                .collect(Collectors.toList());
        List<String> specializations = educationDAO.findAll().stream()
                .map(Education::getSpecialization)
                .distinct()
                .collect(Collectors.toList());
        List<String> positions = resumeDAO.findAll().stream()
                .map(Resume::getWantedPosition)
                .distinct()
                .collect(Collectors.toList());
        List<String> companies = experienceDAO.findAll().stream()
                .map(exp -> exp.getCompany().getName())
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("institutions", institutions);
        model.addAttribute("specializations", specializations);
        model.addAttribute("positions", positions);
        model.addAttribute("companies", companies);
        model.addAttribute("title", "Search Resumes");
        addUserInfoToModel(model, session);

        List<Resume> resumes = resumeDAO.findAll().stream()
                .filter(resume -> resume.getPerson().isSearching())
                .collect(Collectors.toList());

        if (institution != null && !institution.isEmpty()) {
            resumes = resumes.stream()
                    .filter(resume -> educationDAO.findByPerson(resume.getPerson()).stream()
                            .anyMatch(edu -> edu.getInstitution().equals(institution)))
                    .collect(Collectors.toList());
        }
        if (specialization != null && !specialization.isEmpty()) {
            resumes = resumes.stream()
                    .filter(resume -> educationDAO.findByPerson(resume.getPerson()).stream()
                            .anyMatch(edu -> edu.getSpecialization().equals(specialization)))
                    .collect(Collectors.toList());
        }
        if (position != null && !position.isEmpty()) {
            resumes = resumes.stream()
                    .filter(resume -> resume.getWantedPosition().equals(position))
                    .collect(Collectors.toList());
        }
        if (company != null && !company.isEmpty()) {
            resumes = resumes.stream()
                    .filter(resume -> experienceDAO.findByPerson(resume.getPerson()).stream()
                            .anyMatch(exp -> exp.getCompany().getName().equals(company)))
                    .collect(Collectors.toList());
        }
        if (minSalary != null) {
            resumes = resumes.stream()
                    .filter(resume -> resume.getWantedSalary() >= minSalary)
                    .collect(Collectors.toList());
        }
        if (maxSalary != null) {
            resumes = resumes.stream()
                    .filter(resume -> resume.getWantedSalary() <= maxSalary)
                    .collect(Collectors.toList());
        }

        model.addAttribute("resumes", resumes);
        return "searchResumes";
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