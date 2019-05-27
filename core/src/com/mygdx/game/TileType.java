package com.mygdx.game;

import java.util.HashMap;

public enum TileType {
    //SKY(1, false, "Sky"),
    BOOK1(72, false, "Enemy"),
    BOOK2(73, false, "Enemy"),
    //FLOOR(4, true, "Floor"),
    FLOOR1(66, true, "NewFloor1"),
    FLOOR2(67, true, "NewFloor2"),
    FLOOR3(68, true, "NewFloor3"),
    FLOOR4(79, true, "NewFloor3"),
    FLOOR5(80, true, "NewFloor3"),
    FLOOR6(81, true, "NewFloor3"),
    FLOOR7(82, true, "NewFloor3"),
    FLOOR8(83, true, "NewFloor3"),
    FLOOR9(84, true, "NewFloor3"),
    WALL1(23, true, "WALL1"),
    WALL2(24, true, "WALL2"),
    WALL3(36, true, "WALL3"),
    WALL4(37, true, "WALL4"),
    WALL5(49, true, "WALL5"),
    WALL6(50, true, "WALL6"),
    WALL7(62, true, "WALL7"),
    WALL8(63, true, "WALL8"),
    WALL9(75, true, "WALL9"),
    WALL10(76, true, "WALL10"),
    WALL11(85, true, "WALL11"),
    WALL12(86, true, "WALL12"),
    WALL13(10, true, "WALL13"),
    WALL14(11, true, "WALL14");
    //LAVA(5, false, "Lava");
//    LAVA1(5, false, "Lava");
//    LAVA2(5, false, "Lava");

    public static final int TILE_SIZE = 64;
    private static HashMap<Integer, TileType> tileMap;
    private int id;
    private boolean collidable;
    private String name;

    TileType(int id, boolean collidable, String name) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public String getName() {
        return name;
    }
    
    static {
        tileMap = new HashMap<>();
        for (TileType tileType : TileType.values()) {
            tileMap.put(tileType.getId(), tileType);
        }
    }

    public static TileType getTileTypeById(int id) {
        return tileMap.get(id);
    }

}
