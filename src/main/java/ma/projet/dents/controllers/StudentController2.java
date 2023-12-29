package ma.projet.dents.controllers;

import ma.projet.dents.entities.*;
import ma.projet.dents.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.projet.dents.repositories.PWRepository;
import ma.projet.dents.repositories.StudentPWRepository;
import ma.projet.dents.repositories.StudentRepository;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController2 {

    @Autowired
    private StudentRepository studentRepository ;

    @Autowired
    private PWRepository pwRepository ;

    @Autowired
    private StudentPWRepository studentPWRepository ;

    @Autowired
    private ImagesRepository imagesRepository;


    @GetMapping
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @GetMapping("/credentials")
    public List<Object[]> getStudentCredentials() {
        return studentRepository.findUsernamesAndPasswords();
    }

    @PostMapping("/createStudentPW")
    public ResponseEntity<String> createStudentPW(
            @RequestParam int studentId,
            @RequestParam String pwTitle,
            @RequestParam String imageFrontBase64,
            @RequestParam String imageSideBase64
    ) {
        try {
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            Student student = optionalStudent.orElseThrow(() -> new RuntimeException("Role not found: STUDENT"));

            List<PW> pws = pwRepository.findByTitle(pwTitle);
            if (pws.isEmpty()) {
                throw new ResourceNotFoundException("PW not found with title: " + pwTitle);
            }

            StudentPWPrimaire studentPWPrimaire = new StudentPWPrimaire(student, pws.get(0));
            String currentTimeString = new SimpleDateFormat("HH:mm:ss").format(new Date());

            byte[] imageFrontBytes = Base64.getDecoder().decode(imageFrontBase64);
            byte[] imageSideBytes = Base64.getDecoder().decode(imageSideBase64);

            return ResponseEntity.ok("StudentPW created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating StudentPW");
        }
    }
    @PostMapping("/create")
    public void createStudentPW(@RequestBody ListForPW listForPW) {


        Student student = studentRepository.findById(listForPW.getStudentId()).orElse(null);
        PW pw = pwRepository.findById(listForPW.getPwId()).orElse(null);

        if (student == null || pw == null) {
            return;
        }

        StudentPWPrimaire studentPWPrimaire = new StudentPWPrimaire(student, pw);

        StudentPW studentPW = new StudentPW(studentPWPrimaire, new Date(), new Date());

        studentPWRepository.save(studentPW);

        for (DataForPW dataForPW : listForPW.getList()) {

            byte[] decodedImage = Base64.getDecoder().decode(dataForPW.getImageFront());

            Images images = new Images(
                    dataForPW.getAlpha1(),
                    dataForPW.getAlpha2(),
                    dataForPW.getAlpha3(),
                    dataForPW.getBeta1(),
                    dataForPW.getBeta2(),
                    dataForPW.getBeta3(),
                    decodedImage,
                    studentPW
            );
            imagesRepository.save(images);
        }
    }

}