package com.bustrack.util;

import com.bustrack.model.user.BusOperator;
import com.bustrack.model.user.Customer;
import com.bustrack.model.user.User;
import com.bustrack.model.vehicle.LimousineBus;
import com.bustrack.model.vehicle.SeaterBus;
import com.bustrack.model.vehicle.SleeperBus;
import com.bustrack.model.vehicle.Vehicle;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tiện ích cấu hình Google Gson hỗ trợ LocalDateTime và đa hình (Polymorphism)
 */
public class GsonUtils {
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final JsonSerializer<LocalDateTime> LOCAL_DATE_TIME_SERIALIZER = 
            (src, typeOfSrc, context) -> new JsonPrimitive(src.format(ISO_FORMATTER));

    private static final JsonDeserializer<LocalDateTime> LOCAL_DATE_TIME_DESERIALIZER = 
            (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), ISO_FORMATTER);

    // Xử lý đa hình cho User (Customer / BusOperator)
    private static final JsonDeserializer<User> USER_DESERIALIZER = (json, typeOfT, context) -> {
        JsonObject jsonObject = json.getAsJsonObject();
        String roleStr = jsonObject.has("role") ? jsonObject.get("role").getAsString() : "CUSTOMER";
        if ("OPERATOR".equalsIgnoreCase(roleStr)) {
            return context.deserialize(jsonObject, BusOperator.class);
        } else {
            return context.deserialize(jsonObject, Customer.class);
        }
    };

    // Xử lý đa hình cho Vehicle (SeaterBus / SleeperBus / LimousineBus)
    private static final JsonDeserializer<Vehicle> VEHICLE_DESERIALIZER = (json, typeOfT, context) -> {
        JsonObject jsonObject = json.getAsJsonObject();
        String typeStr = jsonObject.has("vehicleType") ? jsonObject.get("vehicleType").getAsString() : "SEATER";
        if ("SLEEPER".equalsIgnoreCase(typeStr)) {
            return context.deserialize(jsonObject, SleeperBus.class);
        } else if ("LIMOUSINE".equalsIgnoreCase(typeStr)) {
            return context.deserialize(jsonObject, LimousineBus.class);
        } else {
            return context.deserialize(jsonObject, SeaterBus.class);
        }
    };

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_SERIALIZER)
            .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_DESERIALIZER)
            .registerTypeAdapter(User.class, USER_DESERIALIZER)
            .registerTypeAdapter(Vehicle.class, VEHICLE_DESERIALIZER)
            .setPrettyPrinting()
            .create();

    public static Gson getGson() {
        return GSON;
    }
}
