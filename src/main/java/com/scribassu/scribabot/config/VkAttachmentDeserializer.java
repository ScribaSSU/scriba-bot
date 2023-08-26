package com.scribassu.scribabot.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.scribassu.scribabot.dto.vk.VkAttachment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VkAttachmentDeserializer extends StdDeserializer<List<VkAttachment>> {

    public VkAttachmentDeserializer() {
        super(List.class);
    }

    @Override
    public List<VkAttachment> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<VkAttachment> result = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode element : node) {
                var type = element.get("type").asText();
                var someAttachment = element.get(type);
                result.add(VkAttachment.fromType(type, someAttachment));
            }
        } else {
            var type = node.get("type").asText();
            var someAttachment = node.get(type);
            result.add(VkAttachment.fromType(type, someAttachment));
        }
        return result;
    }
}
