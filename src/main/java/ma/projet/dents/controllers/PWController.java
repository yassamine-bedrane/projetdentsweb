package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.entities.Groupe;
import ma.projet.dents.entities.PW;
import ma.projet.dents.entities.Tooth;
import ma.projet.dents.repositories.GroupeRepository;
import ma.projet.dents.repositories.PWRepository;
import ma.projet.dents.repositories.ToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/pws")
public class PWController {
    @Autowired
    PWRepository pwRepository;

    @Autowired
    private ToothRepository toothRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("pws", pwRepository.findAll());
        model.addAttribute("pw", new PW());
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("groupes", groupeRepository.findAll());

        return "indexPW";
    }


    @GetMapping("/{id}")
    public PW findById(@PathVariable int id) throws Exception {
        return this.pwRepository.findById(id).orElseThrow(() -> new Exception("PW inexistant"));

    }

    @PostMapping
    public String addPW(@Valid PW pw, BindingResult result, Model model, @RequestParam("groups") List<Integer> groupIds) {
        List<Groupe> selectedGroups = groupeRepository.findAllById(groupIds);
        System.out.println("Group IDs: " + groupIds);
        pw.setGroups(selectedGroups);
        for (Groupe group : selectedGroups) {
            group.getPws().add(pw);
        }
        pwRepository.save(pw);
        model.addAttribute("pws", pwRepository.findAll());
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("groupes", groupeRepository.findAll());

        return "redirect:/app/pws";
    }

    @GetMapping("/edit/{id}")
    public String editPW(@PathVariable("id") int id, Model model) {
        PW pw = pwRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid pw Id:" + id));
        model.addAttribute("pw", pw);
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("groupes", groupeRepository.findAll());


        return "editPW";
    }

    @PostMapping("/update/{id}")
    public String updatePW(@PathVariable("id") int id, @Valid PW pw, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editPW";
        }

        Tooth  selectedTooth = toothRepository.findById(pw.getTooth().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Tooth Id: "+ pw.getTooth().getId()));
        pw.setTooth(selectedTooth);

        PW existingPW = pwRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PW Id: "+ id));

        List<Groupe> selectedGroups = groupeRepository.findAllById(pw.getGroups().stream().map(Groupe::getId).collect(Collectors.toList()));
        System.out.println("Group IDs: " + selectedGroups);

        for (Groupe group : existingPW.getGroups()) {
            group.getPws().remove(existingPW);
        }

        for (Groupe group : selectedGroups) {
            group.getPws().add(existingPW);
        }

        pw.setGroups(selectedGroups);

        pwRepository.save(pw);
        model.addAttribute("groupes", groupeRepository.findAll());

        return "redirect:/app/pws";
    }


    @GetMapping("/delete/{id}")
    public String deletePW(@PathVariable int id, Model model) {
        try {
            PW pwToDelete = pwRepository.findById(id).orElse(null);

            if (pwToDelete != null) {
                boolean isGroupNotLinkedToStudent = pwToDelete.getGroups().stream()
                        .allMatch(group -> group.getStudents().isEmpty());

                if (pwToDelete.getStudentpws().isEmpty() && isGroupNotLinkedToStudent) {
                    for (Groupe groupe : pwToDelete.getGroups()) {
                        groupe.getPws().remove(pwToDelete);
                        groupeRepository.save(groupe);
                    }

                    pwRepository.delete(pwToDelete);
                    model.addAttribute("success", "Suppression réussie");
                } else {
                    model.addAttribute("error", "Impossible de supprimer ce PW car le groupe associé contient déjà des étudaints ");
                }
            }
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Impossible de supprimer le PW");
        }

        model.addAttribute("pws", pwRepository.findAll());
        model.addAttribute("pw", new PW());
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("groupes", groupeRepository.findAll());

        return "indexPW";
    }
}

