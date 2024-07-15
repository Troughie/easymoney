package com.example.MoneyLover.infra.Icon.ServiceImpl;

import com.example.MoneyLover.infra.Category.ServiceImpl.ICategoryService;
import com.example.MoneyLover.infra.Icon.Dto.Icon_dto;
import com.example.MoneyLover.infra.Icon.Dto.Icon_url;
import com.example.MoneyLover.infra.Icon.Entity.Icon;
import com.example.MoneyLover.infra.Icon.Mapper.IconMapper;
import com.example.MoneyLover.infra.Icon.Repository.IconRepo;
import com.example.MoneyLover.infra.Icon.Service.IconService;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.shares.Service.FileUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IIconService implements IconService {
    private final ResponseException _res;
    private final IconRepo iconRepo;
    private final Logger logger = LoggerFactory.getLogger(ICategoryService.class);

    public ApiResponse<?> saveImgIcon(Icon_dto iconDto) {
        List<Icon> icons =new ArrayList<>();
        try {
            for(MultipartFile i : iconDto.getIcon()){

                String fileName = StringUtils.cleanPath(Objects.requireNonNull(i.getOriginalFilename()));

                String fileCode = FileUtil.saveFile(fileName, i);

                Icon response = new Icon();
                response.setPath("icon/" + fileCode);
                icons.add(response);
            }
            iconRepo.saveAll(icons);
            return _res.createSuccessResponse("successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> saveUrlIcon(List<Icon_url> iconUrlDto) {
        try {
            List<Icon> icons =iconUrlDto.stream().map(IconMapper.INSTANCE::toIcon).toList();
            iconRepo.saveAll(icons);
            return _res.createSuccessResponse("successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }

    }

    public ApiResponse<?> getAllIcon() {
        try {
            return _res.createSuccessResponse("successfully",200,iconRepo.findAll());
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

}
