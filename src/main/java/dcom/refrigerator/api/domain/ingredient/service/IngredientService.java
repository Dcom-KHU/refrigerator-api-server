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
                HttpStatus.NOT_FOUND, "해당하는 Name 을 가진 재료가 존재하지 않습니다."
        ));
    }



    public void joinIngredient(Ingredient ingredient){

        Optional<Ingredient> ingredientOptionalByName=ingredientRepository.findByName(ingredient.getName());

        if ( !ingredientOptionalByName.isPresent()) {

            Ingredient temp= ingredientRepository.save(ingredient);

        }
    }


    public void deleteIngredient(Integer id){
        Ingredient ingredient=ingredientRepository.findById(id).orElseThrow(()->new ResponseStatusException(
                HttpStatus.NOT_FOUND,"id에 해당되는 재료가 없습니다."));
        ingredientRepository.delete(ingredient);
    }


}
