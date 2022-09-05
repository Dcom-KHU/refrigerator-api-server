package dcom.refrigerator.api.domain.foodImage.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.foodImage.repository.FoodImageRepository;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientRepository;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class FoodImageService {
    private  final FoodImageRepository foodImageRepository;
    private  final FoodRepository foodRepository;

    public FoodImage getFoodImageById(Integer foodImageId) {
        return foodImageRepository.findById(foodImageId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"image 를 찾을 수 없습니다."));

    }
    public List<FoodImage> getAllFoodImageByFoodId(Integer foodId) {

        Optional<Food> foodOptional= foodRepository.findById(foodId);
        if(!foodOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 foodId 에 대한 이미지가 없습니다");
        else {
        List<FoodImage> foodImages=foodImageRepository.findAllByFood(foodOptional.get());

        if(foodImages.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 recipe id 에 대한 image 를 찾을 수 없습니다.");

        else return  foodImages;
        }
    }



    public FoodImage getFoodImageByOriginFileName(String originFileName) {
        return foodImageRepository.findFoodImageByOriginFileName(originFileName).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"image 를 찾을 수 없습니다."));
    }


    public String updateImages(Food food,String imageDescription,MultipartFile ... images )throws Exception{
        String url="";
        if(images.length !=0) {
            String[] imageDescriptions = imageDescription
                    .replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");

            if (images.length != imageDescriptions.length)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "각 이미지에는 설명이 반드시 필요합니다.");

            Iterator<String> iter = Arrays.stream(imageDescriptions).iterator();

            for (MultipartFile multipartFile : images) {
                if (iter.hasNext()) {
                    String imagePath = null;
                    String absolutePath = new File("").getAbsolutePath() + "/";
                    String path = "images";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    if (!multipartFile.isEmpty()) {
                        String contentType = multipartFile.getContentType();
                        String originalFileExtension;
                        if (ObjectUtils.isEmpty(contentType)) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 파일은 jpg, png 만 가능합니다.");
                        } else {
                            if (contentType.contains("image/jpeg")) {
                                originalFileExtension = ".jpg";
                            } else if (contentType.contains("image/png")) {
                                originalFileExtension = ".png";
                            } else {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 파일은 jpg, png 만 가능합니다.");
                            }
                        }

                        imagePath = path + "/" + UUID.randomUUID() + originalFileExtension;
                        file = new File(absolutePath + imagePath);
                        multipartFile.transferTo(file);

                        FoodImage foodImage = FoodImage.builder()
                                .food(foodRepository.findByName(food.getName()).get())
                                .originFileName(multipartFile.getOriginalFilename())
                                .filePath(absolutePath + imagePath)
                                .description(iter.next().strip()).build();
                        url=foodImage.getFilePath();
                        foodImageRepository.save(foodImage);
                    }
                }
            }
        }
        return url;
    }

    public String registerMainImage(MultipartFile image,Food food) throws  Exception{
        String imagePath = null;
        String absolutePath = new File("").getAbsolutePath() + "/";
        String path = "images";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }


        if (!image.isEmpty()) {
            String contentType = image.getContentType();
            String originalFileExtension;
            if (ObjectUtils.isEmpty(contentType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미지 파일은 jpg, png 만 가능합니다.");
            } else {
                if (contentType.contains("image/jpeg")) {
                    originalFileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    originalFileExtension = ".png";
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미지 파일은 jpg, png 만 가능합니다.");
                }
            }

            imagePath = path + "/" + UUID.randomUUID() + originalFileExtension;
            file = new File(absolutePath + imagePath);
            image.transferTo(file);

            FoodImage foodImage = FoodImage.builder()
                    .food(foodRepository.findByName(food.getName()).get())
                    .originFileName(image.getOriginalFilename())
                    .filePath(absolutePath + imagePath)
                    .description(food.getDescription()).build();

            foodImageRepository.save(foodImage);


            return foodImage.getFilePath();

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미지 파일이 비어있습니다. ");
        }
    }

    public void registerImages(List<MultipartFile>images, String imageDescription,Food food) throws  Exception {

        if (!images.isEmpty()) {

            String[] imageDescriptions = imageDescription
                    .replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");

            if (images.size()!=imageDescriptions.length)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"각 이미지에는 설명이 반드시 필요합니다.");




            Iterator<String> iter = Arrays.stream(imageDescriptions).iterator();

            for (MultipartFile multipartFile:images) {

                log.info(multipartFile.getContentType().toString());

                if (iter.hasNext()) {


                    String imagePath = null;
                    String absolutePath = new File("").getAbsolutePath() + "/";
                    String path = "images";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    if (!multipartFile.isEmpty()) {
                        String contentType = multipartFile.getContentType();
                        String originalFileExtension;
                        if (ObjectUtils.isEmpty(contentType)) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미지 파일은 jpg, png 만 가능합니다.");
                        } else {
                            if (contentType.contains("image/jpeg")) {
                                originalFileExtension = ".jpg";
                            } else if (contentType.contains("image/png")) {
                                originalFileExtension = ".png";
                            } else {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미지 파일은 jpg, png 만 가능합니다.");
                            }
                        }

                        imagePath = path + "/" + UUID.randomUUID() + originalFileExtension;
                        file = new File(absolutePath + imagePath);
                        multipartFile.transferTo(file);

                        FoodImage foodImage = FoodImage.builder()
                                .food(foodRepository.findByName(food.getName()).get())
                                .originFileName(multipartFile.getOriginalFilename())
                                .filePath(absolutePath + imagePath)
                                .description(iter.next().strip()).build();

                        foodImageRepository.save(foodImage);

                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미지 파일이 비어있습니다. ");
                    }
                }
            }
        }
    }
    public void deleteAllFoodImagesByFoodId(Integer foodId) {
        Optional<Food> foodOptional=foodRepository.findById(foodId);
        if(foodOptional.isPresent()){
            List<FoodImage> foodImages= foodImageRepository.findAllByFood(foodOptional.get());
            foodImageRepository.deleteAll(foodImages);
        }
    }



}