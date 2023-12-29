package ma.projet.dents.controllers;

import jakarta.validation.Valid;
import ma.projet.dents.entities.Professor;
import ma.projet.dents.entities.User;
import ma.projet.dents.repositories.AdminRepository;
import ma.projet.dents.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AccessController {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ProfessorRepository professorRepository;



    @GetMapping("/login")
    public String login(Model model, User user ) {
        model.addAttribute("user", user);
        return "login";
    }


    @GetMapping("/app/home")
    public String successPage() {
        return "home";
    }



    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        String professorUsername = authentication.getName();
        Professor professor = professorRepository.findByUserName(professorUsername);
        if (professor == null) {
            throw new RuntimeException("Professor not found with username: " + professorUsername);
        }
        model.addAttribute("professor", professor);
        return "profilProf";
    }

    @GetMapping("/profile/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id, Authentication authentication) {
        try {
            String professorUsername = authentication.getName();
            Professor professor = professorRepository.findByUserName(professorUsername);

            byte[] imageBytes = professor.getImage();

            return ResponseEntity.ok()
                    .body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

        @PostMapping("/profile/updateProf/{id}")
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
            model.addAttribute("msg","Les changements ont été enregistrés avec succès.");
            return "profilProf";
        }


}
