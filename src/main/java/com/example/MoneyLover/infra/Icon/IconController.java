package com.example.MoneyLover.infra.Icon;

import com.example.MoneyLover.infra.Icon.Dto.Icon_dto;
import com.example.MoneyLover.infra.Icon.Dto.Icon_url;
import com.example.MoneyLover.infra.Icon.Service.IconService;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.shares.Service.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class IconController {

    private final ResponseException _res;
    private final IconService iconService;

    @PostMapping("icon")
    public ResponseEntity<?> saveIcon(@ModelAttribute Icon_dto icon_dto){
        var result =iconService.saveImgIcon(icon_dto);
        return _res.responseEntity(result,result.getCode());
    }


    @GetMapping("icons")
    public ResponseEntity<?> getIcon(){
        var result =iconService.getAllIcon();
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("icon/url")
    public ResponseEntity<?> saveUrlIcon(@RequestBody List<Icon_url> iconUrlDto){
        var result =iconService.saveUrlIcon(iconUrlDto);
        return _res.responseEntity(result,result.getCode());
    }

    @GetMapping("icon/{filename}")
    public ResponseEntity<?> getIcon(@PathVariable("filename") String filename){
        Resource resource = null;
        try {
            resource = FileUtil.getFileAsResource(filename);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
