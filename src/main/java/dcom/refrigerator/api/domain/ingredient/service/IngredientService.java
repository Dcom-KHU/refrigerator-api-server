package dcom.refrigerator.api.domain.ingredient.service;


import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.IngredientDocument;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientDocumentRepository;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientDocumentRepository ingredientDocumentRepository;
    private final ElasticsearchOperations elasticsearchOperations;

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
        Optional<Ingredient> ingredientOptionalByName = ingredientRepository.findByName(ingredient.getName());

        if (ingredientOptionalByName.isEmpty()) {
            Ingredient temp = ingredientRepository.save(ingredient);
            ingredientDocumentRepository.save(new IngredientDocument(temp));
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 재료가 생성 되어 있습니다.");
        }
    }

    public void deleteIngredient(Integer id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"id에 해당되는 재료가 없습니다."));
        ingredientRepository.delete(ingredient);
    }

    public List<IngredientDocument> searchIngredient(String query) {
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("name", query);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery).withPageable(Pageable.ofSize(20)).build();
        return elasticsearchOperations.search(searchQuery, IngredientDocument.class).stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
