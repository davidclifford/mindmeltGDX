package mindmelt.game.maps;

public interface ITileAccess {
    TileType getTile(int x, int y, int level);
}
