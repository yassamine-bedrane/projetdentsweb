package ma.projet.dents.controllers;


import jakarta.validation.Valid;
import ma.projet.dents.entities.Images;
import ma.projet.dents.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/imagess")
public class ImagesController {
    @Autowired
    ImagesRepository imagesRepository;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("imagess", imagesRepository.findAll());
        model.addAttribute("images", new Images());
        return "indexImages";
    }


    @GetMapping("/{id}")
    public Images findById(@PathVariable int id) throws Exception {
        return this.imagesRepository.findById(id).orElseThrow(() -> new Exception("Images inexistant"));
    }

    @PostMapping
    public String addImages(@Valid Images images, BindingResult result, Model model){
        imagesRepository.save(images);
        model.addAttribute("imagess", imagesRepository.findAll());
        return "redirect:/app/imagess";
    }

    @GetMapping("/edit/{id}")
    public String editImages(@PathVariable("id") int id, Model model){
        Images images = imagesRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid images Id:"+ id));
        model.addAttribute("images", images);
        return "editImages";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid Images images, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editImages";
        }

        imagesRepository.save(images);
        return "redirect:/app/imagess";
    }

    @GetMapping("/delete/{id}")
    public String deleteImages(@PathVariable int id, Model model) {
        imagesRepository.deleteById(id);
        model.addAttribute("imagess", imagesRepository.findAll());
        model.addAttribute("images", new Images());
        return "indexImages";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        try {
            Images img = imagesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Images not found with id: " + id));

            byte[] imageBytes = img.getImageFront();

            return ResponseEntity.ok()
                    .body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

}

