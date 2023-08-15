package com.files.upload.controller;

import com.files.upload.message.ResponseMessage;
import com.files.upload.model.FileData;
import com.files.upload.service.FileDataService;
import com.files.upload.utils.GenericValidator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin("http://localhost:8081")
public class FilesValidationController {

    @Autowired
    private FileDataService fileDataService;

    Logger logger = org.slf4j.LoggerFactory.getLogger(FilesValidationController.class);


    @PostMapping("/upload/{module}")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestBody String jsonString, @PathVariable String module) throws Exception {

        List<Map<String, Object>> dataList = getMaps(jsonString);

        GenericValidator genericValidator = new GenericValidator();

        String message = genericValidator.validate(dataList);



        if (message != null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        } else {
            message = "File uploaded successfully: " + dataList.size() + " records";
        }

        fileDataService.store(dataList, module);


        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

    }

    private List<Map<String, Object>> getMaps(String jsonString) {
        Gson gson = new Gson();

        Type listType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> dataList = gson.fromJson(jsonString, listType);
        return dataList;
    }


}
