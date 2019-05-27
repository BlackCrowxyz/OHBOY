package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

public class TiledGameMap{
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMapTileSet tileSet;
    private TiledMapTileLayer.Cell cell;
    private Sound coinSound;
    private Array<TiledMapTileLayer.Cell> coinsCell;
    private int numberOfTilesBeenTakenByUser = 0;
    private int numberOfCoins;

    TiledGameMap(CubeGame cubeGame, SpriteBatch batch, OrthographicCamera camera, int level) {
        tiledMap = cubeGame.getAssetManager().get("level"+level+".tmx", TiledMap.class);
//        tiledMap = cubeGame.getAssetManager().get("levelTest.tmx", TiledMap.class);
        tileSet = tiledMap.getTileSets().getTileSet("Tiles");
        coinSound = cubeGame.getAssetManager().get("coin.wav");
        coinsCell = new Array<>();
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView(camera);
        numberOfCoins = getNumberOfCoinsInMap();
    }

    public void nextMap(CubeGame cubeGame, int level) {
        //TODO: change the code for supporting asset manager like the GameScreen
        tiledMap = cubeGame.getAssetManager().get("level" + level + ".tmx", TiledMap.class);
        orthogonalTiledMapRenderer.setMap(tiledMap);
        numberOfCoins = getNumberOfCoinsInMap();
        coinsCell.clear();
        //number of tiles for next level
        this.numberOfTilesBeenTakenByUser = 0;
    }

    public void render() {
        orthogonalTiledMapRenderer.render();
    }

    public void setOrthogonalTiledMapRendererView(OrthographicCamera camera) {
        orthogonalTiledMapRenderer.setView(camera);
    }

    public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
        for (int row = (int) y / TileType.TILE_SIZE; row < (int) Math.ceil(((y + height) / TileType.TILE_SIZE)); row++) {
            for (int col = (int) x / TileType.TILE_SIZE; col < (int) Math.ceil(((x + width) / TileType.TILE_SIZE)); col++) {
                /* TODO: does not get object layer!
                   TODO: WARNING, subtract object layers
                 */
                for (int layer = 0; layer < getLayers() - 3; layer++) {
                    TileType type = getTileTypeByCoordinate(layer, col, row);
                    if (type != null && type.isCollidable()) {
                        //System.out.println(type.getName()+", id = "+type.getId());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int getNumberOfCoinsInMap() {
        MapLayer mapLayer = tiledMap.getLayers().get("Coins_Object");//3 for coin layer
        if (mapLayer != null) {
            return mapLayer.getObjects().getCount();
        }
        return 0;
    }

    private TileType getTileTypeByCoordinate(int layer, int col, int row) {
        cell = ((TiledMapTileLayer)tiledMap.getLayers().get(layer)).getCell(col, row);
        //cell == null -> is out of the Map
        if (cell != null) {
            TiledMapTile tile = cell.getTile();
            //tile == null -> does not contain any tile in this part of map(Ex: sky)
            if (tile != null) {
                int id = tile.getId();
                removeCoin(); //remove coin's tile
                return TileType.getTileTypeById(id);
            }
        }
        return null;
    }

    private void removeCoin() {
        if (cell.getTile().getId() == TileType.BOOK1.getId() || cell.getTile().getId() == TileType.BOOK2.getId()) {
            /*
             * DO NOT CLEAR THIS
            //cell.setTile(tileSet.getTile(1));//1 is background color
             ***/
            coinSound.play(1.0F);
            cell.setTile(null);//is it good to make it null???
            coinsCell.add(cell);
            //System.out.println(col+" = x , y = "+ row);
            numberOfTilesBeenTakenByUser++;
        }
    }

    private void resetCoinsTexture() {
        /*
         * * DO NOT CLEAR THIS
         * ID: when you create a tileset and draw pics with it like pixel art,
         *      every pixel has an ID,
         *      be aware that ids are first base.
            ** if you have the cell, then you have it's position too for reusing it
            ** //layer.setCell(2, 13, cell); // x and y coordinates NOT in PIXEL or any other Unit!
        **/
        for (TiledMapTileLayer.Cell cell: coinsCell) {
            //coin id in pixel (pixel.png is the tileset)
            //cell.setTile(tileSet.getTile(3));
            cell.setTile(tileSet.getTile(73));
        }
    }

    public int getNumberOfTilesBeenTakenByUser() {
        return numberOfTilesBeenTakenByUser;
    }

    public int getWidth() {
        return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();
    }

    public int getHeight() {
        return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight();
    }

    private int getLayers() {
        return tiledMap.getLayers().getCount();
    }

    public void doRestart() {
        this.numberOfTilesBeenTakenByUser = 0;
        resetCoinsTexture();
        coinsCell.clear();
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    public void dispose() {
        tiledMap.dispose();
        orthogonalTiledMapRenderer.dispose();
        coinSound.dispose();
        coinsCell.clear();
    }
}