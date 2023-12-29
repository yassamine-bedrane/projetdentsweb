package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.entities.MyUserDetails;
import ma.projet.dents.entities.Role;
import ma.projet.dents.entities.User;
import ma.projet.dents.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/users")
public class UserController {
    @Autowired
    UserRepository userRepository;




    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", new User());
        return "indexUser";
    }


    @GetMapping("/{id}")
    public User findById(@PathVariable int id) throws Exception {
        return this.userRepository.findById(id).orElseThrow(() -> new Exception("User inexistant"));
    }

    @PostMapping
    public String addUser(@Valid User user, BindingResult result, Model model){
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/app/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model){
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid user Id:"+ id));
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editUser";
        }

        userRepository.save(user);
        return "redirect:/app/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, Model model) {
        userRepository.deleteById(id);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", new User());
        return "indexUser";
    }


    @GetMapping("/role")
    public String role(Authentication authentication) {
        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();

        for (Role role : user.getRoles()) {
            System.out.println(role.getName());
            if (role.getName().equals("ADMIN")) {
                return "redirect:/app/professors";
            } else if (role.getName().equals("PROF")) {
                return "redirect:/profile";
            } else if (role.getName().equals("STUDENT")) {
                return "redirect:/app/home";
            }
        }

        return "redirect:/app/home";
    }




}

