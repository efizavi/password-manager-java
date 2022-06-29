package com.hit.dao;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hit.dm.AbstractSerializable;

public class JSONAbstractAdapter<T extends AbstractSerializable>
        implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonEle = jsonSerializationContext.serialize(t, t.getClass());
        return jsonEle;
    }

    // Get actual object type from serialized type member, to avoid getting abstract objects
    @Override
    public final T deserialize(final JsonElement elem, final Type interfaceType, final JsonDeserializationContext context)
            throws JsonParseException
    {
        // Get actual type from serialized "type" member
        final JsonObject member = (JsonObject) elem;
        final JsonElement typeString = get(member, "type");
        final Type actualType = typeForName(typeString);

        return context.deserialize(elem, actualType);
    }

    private Type typeForName(final JsonElement typeElem)
    {
        try
        {
            return Class.forName(typeElem.getAsString());
        }
        catch (ClassNotFoundException e)
        {
            throw new JsonParseException(e);
        }
    }

    private JsonElement get(final JsonObject wrapper, final String memberName)
    {
        final JsonElement elem = wrapper.get(memberName);

        if (elem == null)
        {
            throw new JsonParseException(
                    "no '" + memberName + "' member found in json file.");
        }
        return elem;
    }
}