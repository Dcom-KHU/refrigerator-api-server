package dcom.refrigerator.api.domain.refrigerator.service;

import dcom.refrigerator.api.domain.recipe.dto.RecipeResponseDto;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.refrigerator.dto.RefrigeratorResponseDto;
import dcom.refrigerator.api.domain.refrigerator.repository.RefrigeratorRepository;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class RefrigeratorService {
    private final RefrigeratorRepository refrigeratorRepository;
    private final UserService userService;

    public List<RefrigeratorResponseDto.WithIngredient> getRefrigerator() {
        User currentUser = userService.getCurrentUser();
        return RefrigeratorResponseDto.WithIngredient.of(refrigeratorRepository.findByUser(currentUser));
    }

    @Scheduled(cron="0 0 12 * * *")
    public void expiredDateNotification() {
        List<Refrigerator> refrigerator = refrigeratorRepository.findOlds();
    }
}
