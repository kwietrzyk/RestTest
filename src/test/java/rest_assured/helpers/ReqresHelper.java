package rest_assured.helpers;

import com.github.javafaker.Faker;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Logger;

final public class ReqresHelper {

    private ReqresHelper() {
        logger.info("Private constructor. Creating objects forbidden");
    }

    private static final Logger logger = Logger.getLogger(ReqresHelper.class.getName());
    public static final Map<Integer, String> userMap;
    public static final Map<Integer, String> unknownMap;
    public static final Map<Integer, String> userEmailMap;
    public static Faker faker = new Faker();
    public static final JSONObject exampleUser;

    static {
        logger.info("Inicjalizing data");

        userMap = Map.of(
                1, "Bluth",
                2, "Weaver",
                3, "Wong",
                4, "Holt",
                5, "Morris",
                6, "Ramos" );

        unknownMap = Map.of(
                1, "cerulean",
                2, "fuchsia rose",
                3, "true red",
                4, "aqua sky",
                5, "tigerlily",
                6, "blue turquoise" );

        userEmailMap = Map.of(
                1, "george.bluth@reqres.in",
                2, "janet.weaver@reqres.in",
                3, "emma.wong@reqres.in",
                4, "eve.holt@reqres.in",
                5, "charles.morris@reqres.in",
                6, "tracey.ramos@reqres.in" );

        exampleUser = new JSONObject();
        exampleUser.put("email", faker.internet().emailAddress());
        exampleUser.put("first_name", faker.name().firstName());
        exampleUser.put("last_name", faker.name().lastName());
    }
}
