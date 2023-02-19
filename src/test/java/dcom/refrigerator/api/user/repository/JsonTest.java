package dcom.refrigerator.api.user.repository;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JsonTest {

    @Test
    public void Test() throws Exception{

        String str="{\"ingredientAmount\": \"['한 마리', '한개']\"}";

        JSONParser jsonParser = new JSONParser();
        JSONObject ingredientAmountJsonObject = (JSONObject) jsonParser.parse(str);
        System.out.println(ingredientAmountJsonObject);

    }
}
