package dcom.refrigerator.api.domain.refrigerator.controller;


import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import dcom.refrigerator.api.domain.refrigerator.service.RefrigeratorService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Recipe Controller"})
@Slf4j
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

}
