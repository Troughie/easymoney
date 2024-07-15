package com.example.MoneyLover.infra.Category;

import com.example.MoneyLover.infra.Category.Dto.CateConstant;
import com.example.MoneyLover.infra.Category.Dto.CategoryAdd;
import com.example.MoneyLover.infra.Category.Service.CategoryService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {
    private final ResponseException _res;
    private final CategoryService categoryService;

    @GetMapping("categories")
    public ResponseEntity<?> index(@AuthenticationPrincipal User user,
        @RequestParam(required = false,defaultValue = CateConstant.TYPE) String type
    )
    {
        var result =categoryService.allCategory(user,type);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("category/add")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @RequestBody CategoryAdd categoryAdd)
    {
        var result =categoryService.addCategory(user,categoryAdd);
        return _res.responseEntity(result,result.getCode());
    }

    @GetMapping("categories/all")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user)
    {
        var result =categoryService.allCategory(user);
        return _res.responseEntity(result,result.getCode());
    }
}
