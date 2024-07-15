package com.example.MoneyLover.infra.Category.Mapper;

import com.example.MoneyLover.infra.Category.Dto.CategoryAdd;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "name",source = "name")
    @Mapping(target = "categoryIcon",source = "icon")
    Category categoryAdd(CategoryAdd categoryAdd);

}
