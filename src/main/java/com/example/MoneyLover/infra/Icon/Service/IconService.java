package com.example.MoneyLover.infra.Icon.Service;

import com.example.MoneyLover.infra.Icon.Dto.Icon_dto;
import com.example.MoneyLover.infra.Icon.Dto.Icon_url;
import com.example.MoneyLover.shares.Entity.ApiResponse;

import java.util.List;

public interface IconService {

    ApiResponse<?> saveImgIcon(Icon_dto iconDto);
    ApiResponse<?> saveUrlIcon(List<Icon_url> iconUrlDto);
    ApiResponse<?> getAllIcon();
}
