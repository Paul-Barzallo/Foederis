package es.uned.foederis.upload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uned.foederis.Constantes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/chat")
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = ".//uploadFiles//";

    String Message_;
    
//    @GetMapping("/upload")
//    public String index() {
//        return "index";
//    }
    
    @PostMapping("/upload") 
    @ResponseBody
//    public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file,
//            RedirectAttributes redirectAttributes) {
        public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file,
                Model model) {

        if (file.isEmpty()) {
        	Message_ = "Please select a file to upload";
            //redirectAttributes.addFlashAttribute("message", Message_);
            model.addAttribute("message", Message_);

            return new ModelAndView("fragmentos :: resultUpload");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            Message_ = "You successfully uploaded '" + file.getOriginalFilename() + "'";
            
            //redirectAttributes.addFlashAttribute("message",Message_);
            model.addAttribute("message", Message_);

            return new ModelAndView("fragmentos :: resultUpload");
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("message", Message_);

        return new ModelAndView("fragmentos :: resultUpload");
    }

    @GetMapping("/uploadStatus")
    @ResponseBody
    public ModelAndView uploadStatus(Model model) {
    	model.addAttribute("message", Message_);

        return new ModelAndView("fragmentos :: resultUpload");
    }

}