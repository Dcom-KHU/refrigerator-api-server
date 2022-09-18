package dcom.refrigerator.api.domain.ingredient.repository;

import dcom.refrigerator.api.domain.ingredient.IngredientDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientDocumentRepository extends ElasticsearchRepository<IngredientDocument, Integer> {
}
