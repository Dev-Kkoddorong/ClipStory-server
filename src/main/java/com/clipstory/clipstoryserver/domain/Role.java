package com.clipstory.clipstoryserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER("ROLE_USER", "Normal User"),
    SUPER("ROLE_SUPER", "Super Administrator");

    private String key;

    private String description;

}
