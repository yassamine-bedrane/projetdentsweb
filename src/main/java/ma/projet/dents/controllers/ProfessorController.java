package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.config.SecurityConfiguration;
import ma.projet.dents.entities.Professor;
import ma.projet.dents.entities.Role;
import ma.projet.dents.repositories.ProfessorRepository;
import ma.projet.dents.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/app/professors")
public class ProfessorController {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    SecurityConfiguration securityConfiguration;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("professor", new Professor());
        return "indexProf";
    }


    @GetMapping("/{id}")
    public Professor findById(@PathVariable int id) throws Exception {
        return this.professorRepository.findById(id).orElseThrow(() -> new Exception("Professor inexistant"));
    }

    @PostMapping
    public String addProfessor(@Valid Professor professor, BindingResult result, Model model, @RequestParam("image") MultipartFile image) {
        try {
            Optional<Role> optionalProfRole = roleRepository.findByName("PROF");
            Role profRole = optionalProfRole.orElseThrow(() -> new RuntimeException("Role not found: PROF"));

            professor.setRoles(Collections.singleton(profRole));

            String pass = professor.getPassword();
            String encodepass = securityConfiguration.passwordEncoder().encode(pass);
            professor.setPassword(encodepass);

            if (!image.isEmpty()) {
                professor.setImage(image.getBytes());
            }

            professorRepository.save(professor);

            model.addAttribute("professors", professorRepository.findAll());
            return "redirect:/app/professors";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/app/professors";
        }
    }

    @GetMapping("/edit/{id}")
    public String editProfessor(@PathVariable("id") int id, Model model){
        Professor professor = professorRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid professor Id:"+ id));
        model.addAttribute("professor", professor);
        return "editProf";
    }

    @PostMapping("/update/{id}")
    public String updateProfessor(@PathVariable("id") int id, @Valid Professor professor, BindingResult result, Model model, @RequestParam(value = "image", required = false) MultipartFile image) {


        Professor existingProf = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("prof not found with id: " + id));
        professor.setPassword(existingProf.getPassword());
        professor.setRoles(existingProf.getRoles());

        if (image != null && !image.isEmpty()) {
            try {
                byte[] photoBytes = image.getBytes();
                professor.setImage(photoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (existingProf != null) {
                professor.setImage(existingProf.getImage());
            }
        }

        professorRepository.save(professor);
        return "redirect:/app/professors";
    }

    @GetMapping("/delete/{id}")
    public String deleteProfessor(@PathVariable int id, Model model,  RedirectAttributes redirectAttributes) {
        try {
            professorRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Suppression réussie");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer le prof car il est lié à un(des) groupe(s)");
        }

        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("professor", new Professor());
        return "redirect:/app/professors";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        try {
            Professor prof = professorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prof not found with id: " + id));

            byte[] imageBytes = prof.getImage();

            return ResponseEntity.ok()
                    .body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


}

