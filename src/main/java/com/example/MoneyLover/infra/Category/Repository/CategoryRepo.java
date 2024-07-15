package com.example.MoneyLover.infra.Category.Repository;

import com.example.MoneyLover.infra.Category.Dto.CategoryResponse;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,String> {
    @Query("select new com.example.MoneyLover.infra.Category.Dto.CategoryResponse(c.id, c.name, c.categoryIcon, c.categoryType) " +
            "from Category c where c.categoryType = ?1 and (c.user = ?2 or c.user is null)")
    List<CategoryResponse> allCategoryDefault(CategoryType type,User user);

    @Query("select new com.example.MoneyLover.infra.Category.Dto.CategoryResponse(c.id, c.name, c.categoryIcon, c.categoryType) " +
            "from Category c where (c.user = ?1 or c.user is null)")
    List<CategoryResponse> allCategoryDefaultUser(User user);

    Category findCategoriesById(String id);

    Category findCategoriesByDefaultIncomeTrue();
}
