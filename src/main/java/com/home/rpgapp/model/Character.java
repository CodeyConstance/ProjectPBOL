package com.home.rpgapp.model;

import java.util.List;

public class Character {
    private int characterId;
    private String name;
    private String charClass;
    private int level;
    private int hp;
    private int xp;
    private List<Item> items;

    public Character(int characterId, String name, String charClass, int level, int hp, int xp, List<Item> items) {
        this.characterId = characterId;
        this.name = name;
        this.charClass = charClass;
        this.level = level;
        this.hp = hp;
        this.xp = xp;
        this.items = items;
    }

    public int getCharacterId() {
        return characterId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharClass() {
        return charClass;
    }

    public void setCharClass(String charClass) {
        this.charClass = charClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
}
