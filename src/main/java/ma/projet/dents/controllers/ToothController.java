package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.entities.Tooth;
import ma.projet.dents.repositories.ToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/tooths")
public class ToothController {
    @Autowired
    ToothRepository toothRepository;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("tooth", new Tooth());
        return "indexTooth";
    }


    @GetMapping("/{id}")
    public Tooth findById(@PathVariable int id) throws Exception {
        return this.toothRepository.findById(id).orElseThrow(() -> new Exception("Tooth inexistant"));
    }

    @PostMapping
    public String addTooth(@Valid Tooth tooth, BindingResult result, Model model){
        toothRepository.save(tooth);
        model.addAttribute("tooths", toothRepository.findAll());
        return "redirect:/app/tooths";
    }

    @GetMapping("/edit/{id}")
    public String editTooth(@PathVariable("id") int id, Model model){
        Tooth tooth = toothRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid tooth Id:"+ id));
        model.addAttribute("tooth", tooth);
        return "editTooth";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid Tooth tooth, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editTooth";
        }

        toothRepository.save(tooth);
        return "redirect:/app/tooths";
    }

    @GetMapping("/delete/{id}")
    public String deleteTooth(@PathVariable int id, Model model) {
        toothRepository.deleteById(id);
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("tooth", new Tooth());
        return "indexTooth";
    }
}

