package com.example.MoneyLover.infra.Icon.Mapper;

import com.example.MoneyLover.infra.Icon.Dto.Icon_url;
import com.example.MoneyLover.infra.Icon.Entity.Icon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IconMapper {
    IconMapper INSTANCE = Mappers.getMapper(IconMapper.class);

    @Mapping(target = "name",source = "name")
    @Mapping(target = "path",source = "url")
    Icon toIcon(Icon_url iconUrlDto);
}
