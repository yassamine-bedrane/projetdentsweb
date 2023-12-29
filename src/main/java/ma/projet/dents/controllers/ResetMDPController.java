package ma.projet.dents.controllers;

import ma.projet.dents.entities.User;
import ma.projet.dents.repositories.UserRepository;
import ma.projet.dents.services.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;

@Controller
@RequestMapping("/mdprecuperation")
public class ResetMDPController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final int CODE_EXPIRATION_MINUTES = 15;

    @GetMapping
    public String show(Model model) {
        model.addAttribute("users",userRepository.findAll());
        return "mpdOublie";
    }

    @PostMapping
    public String validateEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String code = generateRandomCode();
            emailSender.sendEmail(email, "Code d'activation pour la récupération du mot de passe", "Votre Code est: " + code);
            redirectAttributes.addFlashAttribute("code", code);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("expirationMinutes", CODE_EXPIRATION_MINUTES);
            return "recupereMdp";
        } else {
            redirectAttributes.addFlashAttribute("error", "Adresse email invalide");
            return "redirect:/mdprecuperation";
        }
    }

    @PostMapping("/new")
    public String changePassword(@RequestParam("email") String email, @RequestParam("code") String code, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        if (isCodeValid(email, code)) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("success", "Mot de passe réinitialisé avec succès.");
                return "redirect:/login";
            }
        }

        return "redirect:/mdprecuperation?error=CodeInvalide";
    }

    private String generateRandomCode() {
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }

    private boolean isCodeValid(String email, String code) {
        return true;
    }
}
