package dcom.refrigerator.api.domain.ingredient.service;

import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public Ingredient getIngredientById(Integer id){
        return ingredientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 재료가 존재하지 않습니다."
        ));
    }

    public Ingredient getIngredientByName(String name){
        return ingredientRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 재료가 존재하지 않습니다."
        ));
    }


    public Integer joinIngredient(Ingredient ingredient){

        Optional<Ingredient> ingredientOptionalByName=ingredientRepository.findByName(ingredient.getName());

        if ( ingredientOptionalByName.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "이미 존제하는 재료 name 입니다.");
        }


         Ingredient temp= ingredientRepository.save(ingredient);

        return temp.getId();
    }


    public void deleteIngredient(Integer id){
        Ingredient ingredient=ingredientRepository.findById(id).orElseThrow(()->new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "잘못된 id 입니다."));
        ingredientRepository.delete(ingredient);
    }


}
