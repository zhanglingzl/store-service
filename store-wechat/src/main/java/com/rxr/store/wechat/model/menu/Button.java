package com.rxr.store.wechat.model.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rxr.store.wechat.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Button {
    private Type type;
    private String name;
    private String key;
    private String url;
    @JsonProperty("sub_button")
    private Button[] subButton;

    public Button(Type type, String name, String url) {
        this.type = type;
        this.name = name;
        this.url = url;
    }

    public enum Type{
        click,view,scancode_push,scancode_waitmsg,pic_sysphoto,pic_photo_or_album,
        pic_weixin,location_select,media_id,view_limited;
    }


}
