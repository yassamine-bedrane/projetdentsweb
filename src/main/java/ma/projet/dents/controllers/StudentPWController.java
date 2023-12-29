package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.config.SecurityConfiguration;
import ma.projet.dents.entities.*;
import ma.projet.dents.repositories.PWRepository;
import ma.projet.dents.repositories.StudentPWRepository;
import ma.projet.dents.repositories.RoleRepository;
import ma.projet.dents.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
@RequestMapping("/app/studentpws")
public class StudentPWController {
    @Autowired
    StudentPWRepository studentpwRepository;

    @Autowired
    SecurityConfiguration securityConfiguration;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    PWRepository pwRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    public String findAll(Model model, Authentication authentication) {
        model.addAttribute("studentpws", studentpwRepository.findAll());
        model.addAttribute("studentpw", new StudentPW());
        return "detailsGroupe";
    }


    @GetMapping("/{id}")
    public StudentPW findById(@PathVariable StudentPWPrimaire id) throws Exception {
        return this.studentpwRepository.findById(id).orElseThrow(() -> new Exception("StudentPW inexistant"));
    }

    @PostMapping
    public String addStudentPW(@Valid StudentPW studentpw, BindingResult result, Model model,
                           @RequestParam("image") MultipartFile image) {
        Optional<Role> optionalStudentPWRole = roleRepository.findByName("ADMIN");
        Role studentpwRole = optionalStudentPWRole.orElseThrow(() -> new RuntimeException("Role not found: "));

        studentpwRepository.save(studentpw);

        model.addAttribute("studentpws", studentpwRepository.findAll());
        return "redirect:/app/studentpws";
    }

    @GetMapping("/edit/{id}")
    public String editStudentPW(@PathVariable("id") StudentPWPrimaire id, Model model) {
        StudentPW studentpw = studentpwRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid studentpw Id:" + id));
        model.addAttribute("studentpw", studentpw);
        return "detailsGroupe";
    }

    @PostMapping("/update/{studentId}/{pwId}")
    public String updateStudentPW(@PathVariable int studentId, @PathVariable int pwId,
                                  @RequestParam(value = "note") Long newNote,
                                  Model model, RedirectAttributes redirectAttributes) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Student Id: " + studentId));

        PW pw = pwRepository.findById(pwId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PW Id: " + pwId));

        StudentPWPrimaire studentPWPrimaire = new StudentPWPrimaire(student, pw);

        StudentPW existingStudentPW = studentpwRepository.findById(studentPWPrimaire)
                .orElseThrow(() -> new IllegalArgumentException("Invalid StudentPW Ids: " + studentId + ", " + pwId));

        existingStudentPW.setNote(newNote);
        redirectAttributes.addFlashAttribute("studentpw",studentpwRepository.findAll());
        studentpwRepository.save(existingStudentPW);

        return "redirect:/app/groupes/details/" + student.getGroupe().getId();
    }


    @GetMapping("/delete/{id}")
    public String deleteStudentPW(@PathVariable StudentPWPrimaire id, Model model) {
        studentpwRepository.deleteById(id);
        model.addAttribute("studentpws", studentpwRepository.findAll());
        model.addAttribute("studentpw", new StudentPW());
        return "detailsGroupe";
    }

}

