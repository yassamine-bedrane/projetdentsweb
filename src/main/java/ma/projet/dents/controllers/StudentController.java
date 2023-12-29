package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.config.SecurityConfiguration;
import ma.projet.dents.entities.*;
import ma.projet.dents.repositories.GroupeRepository;
import ma.projet.dents.repositories.ProfessorRepository;
import ma.projet.dents.repositories.RoleRepository;
import ma.projet.dents.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/app/students")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GroupeRepository groupeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SecurityConfiguration securityConfiguration;

    @Autowired
    ProfessorRepository professorRepository;

    @GetMapping
    public String findAll(Model model, Authentication authentication) {
        String professorUsername = authentication.getName();

        Professor professor = professorRepository.findByUserName(professorUsername);

        if (professor == null) {
            throw new RuntimeException("Professor not found with username: " + professorUsername);
        }

        List<Groupe> professorGroups = professor.getGroups();

        List<Student> students = studentRepository.findByGroupeIn(professorGroups);

        model.addAttribute("students", students);
        model.addAttribute("student", new Student());
        model.addAttribute("groupes", professorGroups);

        return "indexStudent";
    }



    @GetMapping("/{id}")
    public Student findById(@PathVariable int id) throws Exception {
        return this.studentRepository.findById(id).orElseThrow(() -> new Exception("Student inexistant"));
    }

    @PostMapping
    public String addStudent(@Valid Student student, BindingResult result, Model model,
                             @RequestParam("image") MultipartFile image) {
        try {
            Optional<Role> optionalStudentRole = roleRepository.findByName("STUDENT");
            Role studentRole = optionalStudentRole.orElseThrow(() -> new RuntimeException("Role not found: STUDENT"));

            student.setRoles(Collections.singleton(studentRole));

            String pass = student.getPassword();
            String encodepass = securityConfiguration.passwordEncoder().encode(pass);
            student.setPassword(encodepass);

            if (!image.isEmpty()) {
                student.setImage(image.getBytes());
            }

            studentRepository.save(student);

            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("groupes", groupeRepository.findAll());
            return "redirect:/app/students";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/app/students";
        }
    }
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable("id") int id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        model.addAttribute("student", student);
        model.addAttribute("groupes", groupeRepository.findAll());

        return "editStudent";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable("id") int id, @Valid Student student, BindingResult result, Model model, @RequestParam(value = "image", required = false) MultipartFile image) {

        Student existingStudent = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Student Id: " + id));


        if (image != null && !image.isEmpty()) {
            try {
                byte[] photoBytes = image.getBytes();
                student.setImage(photoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (existingStudent != null) {
                student.setImage(existingStudent.getImage());
            }
        }


        Groupe selectedGroupe = groupeRepository.findById(student.getGroupe().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid grp Id:" + student.getGroupe().getId()));
        student.setGroupe(selectedGroupe);
        student.setRoles(existingStudent.getRoles());

        student.setPassword(existingStudent.getPassword());
        studentRepository.save(student);
        return "redirect:/app/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            studentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Suppression réussie");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer cet étudiant");
        }
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("student", new Student());
        model.addAttribute("groupes", groupeRepository.findAll());
        return "redirect:/app/students";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

            byte[] imageBytes = student.getImage();

            return ResponseEntity.ok()
                    .body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/details/{id}")
    public String showDetailsStudent(@PathVariable("id") int id, Model model) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));

            model.addAttribute("lastName", student.getLastName());
            model.addAttribute("firstName", student.getFirstName());
            model.addAttribute("userName", student.getUserName());
            model.addAttribute("image", student.getImage());
            model.addAttribute("number", student.getNumber());
            model.addAttribute("groupe", student.getGroupe());

            model.addAttribute("student", student);
            model.addAttribute("groupes", groupeRepository.findAll());
            return "detailsStudent";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/app/students";
        }
    }


}

