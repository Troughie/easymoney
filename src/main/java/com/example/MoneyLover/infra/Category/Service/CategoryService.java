package com.example.MoneyLover.infra.Category.Service;

import com.example.MoneyLover.infra.Category.Dto.CategoryAdd;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    ApiResponse<?> allCategory(User user, String type);

    ApiResponse<?> addCategory(User user, CategoryAdd categoryAdd);
}
