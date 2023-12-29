package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.entities.Groupe;
import ma.projet.dents.entities.Professor;
import ma.projet.dents.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/app/groupes")
public class GroupeController {
    @Autowired
    GroupeRepository groupeRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private StudentPWRepository studentPWRepository;

    @GetMapping
    public String findAll(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        Professor professor = professorRepository.findByUserName(username);

        List<Groupe> groupes = groupeRepository.findByProfessor(professor);

        model.addAttribute("groupes", groupes);
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("professors", professorRepository.findAll());

        return "indexGroupe";
    }



    @GetMapping("/{id}")
    public Groupe findById(@PathVariable int id) throws Exception {
        return this.groupeRepository.findById(id).orElseThrow(() -> new Exception("Groupe inexistant"));

    }

    @PostMapping
    public String addGroupe(@Valid Groupe groupe, BindingResult result, Model model){
        groupeRepository.save(groupe);
        model.addAttribute("groupes", groupeRepository.findAll());
        model.addAttribute("professors", professorRepository.findAll());

        return "redirect:/app/groupes";
    }

    @GetMapping("/edit/{id}")
    public String editGroupe(@PathVariable("id") int id, Model model){
        Groupe groupe = groupeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid groupe Id:"+ id));
        model.addAttribute("groupe", groupe);
        model.addAttribute("professors", professorRepository.findAll());

        return "editGroupe";
    }

    @PostMapping("/update/{id}")
    public String updateGroupe(@PathVariable("id") int id, @Valid Groupe groupe, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editGroupe";
        }

        Professor selectedProfessor = professorRepository.findById(groupe.getProfessor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Professor Id:" + groupe.getProfessor().getId()));

        groupe.setProfessor(selectedProfessor);

        groupeRepository.save(groupe);

        return "redirect:/app/groupes";
    }

    @GetMapping("/delete/{id}")
    public String deleteGroupe(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            groupeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Suppression réussie");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer le groupe car il contient des étudiants");
        }
        model.addAttribute("groupes", groupeRepository.findAll());
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("professors", professorRepository.findAll());

        return "redirect:/app/groupes";
    }


    @GetMapping("/details/{id}")
    public String showDetailsGroupe(@PathVariable("id") int id, Model model) {
        try {
            Groupe groupe = groupeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid groupe Id:" + id));

            model.addAttribute("code", groupe.getCode());
            model.addAttribute("year", groupe.getYear());
            model.addAttribute("professor", groupe.getProfessor());
            model.addAttribute("students", groupe.getStudents()); 
            model.addAttribute("images",imagesRepository.findAll());
            model.addAttribute("groupe", groupe);
            model.addAttribute("studentspw",studentPWRepository.findAll());
            model.addAttribute("professors", professorRepository.findAll());

            return "detailsGroupe";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/app/groupes";
        }
    }

}

