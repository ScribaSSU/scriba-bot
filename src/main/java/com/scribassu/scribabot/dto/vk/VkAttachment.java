package com.scribassu.scribabot.dto.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class VkAttachment {
    @JsonProperty("type")
    private String type;

    @JsonProperty("photo")
    private Object photo;
    @JsonProperty("video")
    private Object video;
    @JsonProperty("audio")
    private Object audio;
    @JsonProperty("doc")
    private Object doc;
    @JsonProperty("link")
    private Object link;
    @JsonProperty("market")
    private Object market;
    @JsonProperty("market_album")
    private Object marketAlbum;
    @JsonProperty("wall")
    private Object wall;
    @JsonProperty("wall_reply")
    private Object wallReply;
    @JsonProperty("sticker")
    private Object sticker;
    @JsonProperty("gift")
    private Object gift;

    // todo value is not parsed correctly as a string
    public static VkAttachment fromType(String type, Object value) {
        switch (type) {
            case "photo": return photo(type, value);
            case "video": return video(type, value);
            case "audio": return audio(type, value);
            case "doc": return doc(type, value);
            case "link": return link(type, value);
            case "market": return market(type, value);
            case "market_album": return marketAlbum(type, value);
            case "wall": return wall(type, value);
            case "wall_reply": return wallReply(type, value);
            case "sticker": return sticker(type, value);
            default: return gift(type, value);
        }
    }

    public static VkAttachment photo(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setPhoto(value);
        return vkAttachment;
    }

    public static VkAttachment video(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setVideo(value);
        return vkAttachment;
    }

    public static VkAttachment audio(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setAudio(value);
        return vkAttachment;
    }

    public static VkAttachment doc(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setDoc(value);
        return vkAttachment;
    }

    public static VkAttachment link(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setLink(value);
        return vkAttachment;
    }

    public static VkAttachment market(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setMarket(value);
        return vkAttachment;
    }

    public static VkAttachment marketAlbum(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setMarketAlbum(value);
        return vkAttachment;
    }

    public static VkAttachment wall(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setWall(value);
        return vkAttachment;
    }

    public static VkAttachment wallReply(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setWallReply(value);
        return vkAttachment;
    }

    public static VkAttachment sticker(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setSticker(value);
        return vkAttachment;
    }

    public static VkAttachment gift(String type, Object value) {
        var vkAttachment = new VkAttachment();
        vkAttachment.setType(type);
        vkAttachment.setGift(value);
        return vkAttachment;
    }
}