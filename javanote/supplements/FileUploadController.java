package kybmig.ssm.controller;

import kybmig.ssm.Utility;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


@Controller
public class FileUploadController {
    static String uploadDir  = "images";

    @GetMapping("/upload/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView m = new ModelAndView("public/upload");
        return m;
    }

    @PostMapping("/upload")
    @ResponseBody
    public String singFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败";
        } else {
            String path = String.format("%s/%s", uploadDir, file.getOriginalFilename());

            try(FileOutputStream os = new FileOutputStream(path)) {
                byte fileContent[] = file.getBytes();
                os.write(fileContent);;
                return "上传成功";
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败";
            }
        }
    }

    @GetMapping("/avatar/{imageName}")
    public ResponseEntity<byte[]> avatar(@PathVariable String imageName) {
        String path = String.format("%s/%s", uploadDir, imageName);
        Utility.log("avatar %s", path);

        byte content[];
        try (FileInputStream is = new FileInputStream(path)) {
            content = is.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(content);
    }
}
