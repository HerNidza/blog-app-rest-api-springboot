package com.nikola.blog.controller;

import com.nikola.blog.entity.Category;
import com.nikola.blog.payload.CategoryDto;
import com.nikola.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "CRUD REST APIs for Category Resource")
public class CategoryController {
    private CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService theCategoryService) {
        categoryService = theCategoryService;
    }

    // Blog -> Category REST Api
    // Create -> Post REST Api
    @Operation(summary = "Create Category REST API",description = "Create Category REST API is used to save post into the database")
    @ApiResponse(responseCode = "201",description = "Http Status 201 CREATED")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Get Category by ID -> Category REST Api
    @Operation(summary = "Get Category by ID REST API",description = "Get Category by ID REST API is used to get single post from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long categoryId){
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    // Get All -> Category REST Api
    @Operation(summary = "Get All Categories REST API",description = "Get All Categories REST API is used to fetch all posts from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
       return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Update -> Category REST Api
    @Operation(summary = "Update Category REST API",description = "Update Category REST API is used to update a particular post in the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable("id") Long categoryId){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, categoryId));
    }

    // Delete -> Category REST Api
    @Operation(summary = "Delete Category REST API",description = "Delete Category REST API is used to delete a particular post from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId){
        categoryService.deleteById(categoryId);
        return ResponseEntity.ok("Category deleted successfully!");
    }

}
