package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.config.SecurityConfiguration;
import ma.projet.dents.entities.Admin;
import ma.projet.dents.entities.Role;
import ma.projet.dents.entities.Student;
import ma.projet.dents.repositories.AdminRepository;
import ma.projet.dents.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


@Controller
@RequestMapping("/app/admins")
public class AdminController {
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    SecurityConfiguration securityConfiguration;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    public String findAll(Model model, Authentication authentication) {
        model.addAttribute("admins", adminRepository.findAll());
        model.addAttribute("admin", new Admin());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("Authorities TEST : " + authorities);

        String userName = authentication.getName();

        model.addAttribute("userName", userName);

        return "indexAdmin";
    }


    @GetMapping("/{id}")
    public Admin findById(@PathVariable int id) throws Exception {
        return this.adminRepository.findById(id).orElseThrow(() -> new Exception("Admin inexistant"));
    }

    @PostMapping
    public String addAdmin(@Valid Admin admin, BindingResult result, Model model,
                           @RequestParam("image") MultipartFile image) {
        try {
            Optional<Role> optionalAdminRole = roleRepository.findByName("ADMIN");
            Role adminRole = optionalAdminRole.orElseThrow(() -> new RuntimeException("Role not found: ADMIN"));

            admin.setRoles(Collections.singleton(adminRole));

            String pass = admin.getPassword();
            String encodepass = securityConfiguration.passwordEncoder().encode(pass);
            admin.setPassword(encodepass);

            if (!image.isEmpty()) {
                admin.setImage(image.getBytes());
            }

            adminRepository.save(admin);

            model.addAttribute("admins", adminRepository.findAll());
            return "redirect:/app/admins";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/app/admins";
        }
    }

    @GetMapping("/edit/{id}")
    public String editAdmin(@PathVariable("id") int id, Model model) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid admin Id:" + id));
        model.addAttribute("admin", admin);
        return "editAdmin";
    }

    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable("id") int id, @Valid Admin admin, BindingResult result, Model model,
                              @RequestParam(value = "image", required = false) MultipartFile image) {
        Admin existingAdmin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Admin Id: " + id));
        admin.setPassword(existingAdmin.getPassword());
        admin.setRoles(existingAdmin.getRoles());



        if (image != null && !image.isEmpty()) {
            try {
                byte[] photoBytes = image.getBytes();
                admin.setImage(photoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (existingAdmin != null) {
                admin.setImage(existingAdmin.getImage());
            }
        }


        adminRepository.save(admin);

        return "redirect:/app/admins";
    }


    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable int id, Model model) {
        adminRepository.deleteById(id);
        model.addAttribute("admins", adminRepository.findAll());
        model.addAttribute("admin", new Admin());
        return "indexAdmin";
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        try {
            Admin admin = adminRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

            byte[] imageBytes = admin.getImage();

            return ResponseEntity.ok()
                    .body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}

