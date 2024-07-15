package com.example.MoneyLover.infra.Category.ServiceImpl;

import com.example.MoneyLover.config.Redis.RedisService;
import com.example.MoneyLover.infra.Category.Dto.CategoryAdd;
import com.example.MoneyLover.infra.Category.Dto.CategoryResponse;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Category.Mapper.CategoryMapper;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Category.Service.CategoryService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ICategoryService implements CategoryService {
    private final ResponseException _res;
    private final Logger logger = LoggerFactory.getLogger(ICategoryService.class);

    private final CategoryRepo categoryRepo;

    private final RedisService _redis;

    public void saveCateToCache(User user ,List<CategoryResponse> categoryResponses,String type)
    {
        if(type!=null)
        {
        _redis.setList("category"+user.getId()+type,categoryResponses,1, TimeUnit.MINUTES);
        }else{
            _redis.setList("category"+user.getId(),categoryResponses,1, TimeUnit.MINUTES);
        }
    }

    public List<CategoryResponse> getCateFromCache(User user ,String type)
    {
        if(type!=null)
        {
        return _redis.getList("category"+user.getId()+type, CategoryResponse.class);
        }else{
            return _redis.getList("category"+user.getId(), CategoryResponse.class);
        }
    }

    @Override
    public ApiResponse<?> allCategory(User user,String type) {
        List<CategoryResponse> categories=getCateFromCache(user,type);
        if(categories==null || categories.isEmpty())
        {
            List<CategoryResponse> categoryResponses = categoryRepo.allCategoryDefault(CategoryType.valueOf(type),user);
            saveCateToCache(user,categoryResponses,type);
            return _res.createSuccessResponse(200,categoryResponses);
        }
        return _res.createSuccessResponse(200,categories);
    }

    public ApiResponse<?> allCategory(User user)
    {
        List<CategoryResponse> categories=getCateFromCache(user,null);
        if(categories==null || categories.isEmpty())
        {
            List<CategoryResponse> categoryResponses = categoryRepo.allCategoryDefaultUser(user);
            saveCateToCache(user,categoryResponses,null);
            return _res.createSuccessResponse(200,categoryResponses);
        }
        return _res.createSuccessResponse(200,categories);

    }


    public ApiResponse<?> addCategory(User user, CategoryAdd categoryAdd) {
        try {
            Category category= CategoryMapper.INSTANCE.categoryAdd(categoryAdd);
            category.setCategoryType(CategoryType.valueOf(categoryAdd.getType()));
            category.setUser(user);
            categoryRepo.save(category);
            _redis.removeValue("category"+user.getId()+categoryAdd.getType());
            return _res.createSuccessResponse("Add category successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }


}
