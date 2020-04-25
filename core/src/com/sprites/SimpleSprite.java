package com.sprites;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.misc.Constants.TILE_DIMS;

import com.badlogic.gdx.graphics.Texture;

// Class import
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.misc.Constants;
import com.misc.ResourceBar;
import com.misc.Constants.Direction;

/**
 * Simplify and add functionality to the sprite object provided by libGDX.
 *
 * @author Archie
 * @since 17/12/2019
 */
public class SimpleSprite extends Sprite {

    // Private values to be used in this class only
    protected Texture texture;
    private float width, height, internalTime;
    private ResourceBar healthBar;

    // hit boxes, only not equal for fire truck
    protected Polygon movementHitBox;
    private Polygon damageHitBox;
    protected TiledMapTileLayer blockedTilesLayer;

    // center of the sprite
    private Vector2 centre;

    /**
     * Constructor that creates a sprite at a given position using a given texture..
     * Creates a sprite at (0,0) using a given texture.
     *
     * @param spriteTexture  The texture the sprite should use.
     */
    public SimpleSprite(Texture spriteTexture, TiledMapTileLayer blockedTilesLayer) {
        super(spriteTexture);
        this.texture = spriteTexture;
        this.blockedTilesLayer = blockedTilesLayer;
        this.centre = new Vector2(this.getCentreX(), this.getCentreY());
        this.create();
    }

    /*
     *  =======================================================================
     *                          Modified for Assessment 3
     *  =======================================================================
     */
    /**
     * Creates a healthbar and hitbox for the sprite.
     */
    private void create() {
        // Use the longest side of the sprite as the bar width
        this.healthBar = new ResourceBar(Math.max(this.getWidth(), this.getHeight()), Math.min(this.getWidth(), this.getHeight()));
        // {0f, 0f, 0f, height / 2, 0f, this.height, this.width / 2, this.height, this.width, this.height, width, height/2,this.width, 0f, width/2, 0}
        this.movementHitBox = new Polygon(new float[] {0f, 0f, 0f, this.height, this.width, this.height, this.width, 0f});
        this.movementHitBox.setOrigin(this.width / 2, this.height / 2);
        this.damageHitBox = new Polygon(new float[]{0,0,this.getWidth(),0,this.getWidth(),this.getHeight(),0,this.getHeight()});
        // Start internal time at 150, used for animations/timeouts
        this.internalTime = 150;
    }

    /*
     *  =======================================================================
     *                          Modified for Assessment 3
     *  =======================================================================
     */
    /**
     * Update the sprite position, hitbox and health bar.
     * Must be called every frame in order to draw the sprite.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        // Keep the healthbar and hitbox located on the sprite
        this.healthBar.setPosition(this.getX(), this.getY());
        this.movementHitBox.setPosition(this.getX(), this.getY());
        this.damageHitBox.setPosition(this.getX(), this.getY());
        this.centre = new Vector2(this.getCentreX(), this.getCentreY());
        this.healthBar.update(batch);
        // Draw the sprite and update the healthbar
        batch.draw(new TextureRegion(this.texture), this.getX(), this.getY(), this.getWidth() / 2, this.getHeight() / 2,
                this.getWidth(), this.getHeight(), 1, 1, this.getRotation(), true);
        // Decrease internal time
        this.decreaseInternalTime();
    }

    /**
     * Decrement or reset the internal time of the sprite
     * used for timing of attacks
     */
    public void decreaseInternalTime() {
        if (this.internalTime > 0) {
            this.internalTime -= 1;
        } else if (this.internalTime <= 0) {
            this.internalTime = 150;
        }
    }

    /**
     * Enables drawing of the hitbox to it can be seen. 
     * @param renderer   The shape renderer to draw onto.
     */
    public void drawDebug(ShapeRenderer renderer) {
//        renderer.polygon(this.damageHitBox.getTransformedVertices());
        renderer.polygon(this.movementHitBox.getTransformedVertices());
    }

    /**
     * Enables drawing of the hitbox to it can be seen.
     * @param destroyedTexture The flooded texture to replace the sprite with
     */
    public void removeSprite(Texture destroyedTexture) {
        this.texture = destroyedTexture;
    }

    /**
     * Overrides Sprite class method to keep seperate values for width and height.
     * Also updates the sprites width and height and creates a new hitbox and health bar.
     * @param width    The width the sprite should be set to.
     * @param height   The height the sprite should be set to.
     */
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.width = width;
        this.height = height;
        this.create();
        this.movementHitBox.setOrigin(width/2, height/2);
        this.damageHitBox.setOrigin(width/2, height/2);
    }

    /*
     *  =======================================================================
     *                          Added for Assessment 3
     *  =======================================================================
     */
    /**
     * Changes the movement hit box to a triangle for better movement and collisions
     *
     * @param rotation  amount in degrees to rotate the hitbox by
     */
    public void setMovementHitBox(float rotation) {
        this.movementHitBox = new Polygon(new float[] {0f, 0f, 0f, this.height, this.width, this.height, this.width, 0f});
        this.movementHitBox.setOrigin(width/2, height/2);
        this.movementHitBox.rotate(rotation);
    }
    
    
    protected Array<Vector2> getPolygonVertices(Polygon polygon) {
        float[] vertices = polygon.getTransformedVertices();
        Array<Vector2> result = new Array<>();
        for (int i = 0; i < vertices.length/2; i++) {
            float x = vertices[i * 2];
            float y = vertices[i * 2 + 1];
            result.add(new Vector2(x, y));
        }
        return result;
    }
    
    
    protected int collidesWithBlockedTile(TiledMapTileLayer layer) {
        int collisions = 0;
        if (layer != null) {
            for (Vector2 vertex : getPolygonVertices(movementHitBox)) {
                if (layer.getCell(((int) (vertex.x / TILE_DIMS)), ((int) (vertex.y / TILE_DIMS))) != null) {
                    collisions++;
                }
            }
        }
        return collisions;
    }
    
    protected Constants.Direction collisionDirection(TiledMapTileLayer layer) {
        if (layer != null) {
            Array<Vector2> vertices = getPolygonVertices(movementHitBox);
            for (int i = 0; i < vertices.size; i++) {
                Vector2 vertex = vertices.get(i);
                if (layer.getCell(((int) (vertex.x / TILE_DIMS)), ((int) (vertex.y / TILE_DIMS))) != null) {
                    int prev = Math.floorMod(i - 1, vertices.size);
                    int next = Math.floorMod(i + 1, vertices.size);
                    if (vertices.get(prev).x <= vertex.x && vertices.get(next).x <= vertex.x) {
                        return Direction.RIGHT;
                    } else if (vertices.get(prev).x >= vertex.x && vertices.get(next).x >= vertex.x) {
                        return Direction.LEFT;
                    } else if (vertices.get(prev).y <= vertex.y && vertices.get(next).y <= vertex.y) {
                        return Direction.DOWN;
                    } else {
                        return Direction.UP;
                    }
                }
            }
        }
        return Constants.Direction.NONE;
    }

    /**
     * Overrides Sprite class method to rotate the sprite and hitbox at once.
     * @param degrees   The degree to rotate to.
     */
    @Override
    public void rotate(float degrees) {
        this.movementHitBox.rotate(degrees);
        if (this.blockedTilesLayer == null || collidesWithBlockedTile(this.blockedTilesLayer) == 0) {
            super.rotate(degrees);
            this.damageHitBox.rotate(degrees);
        } else {
            this.movementHitBox.setRotation(this.getRotation());
        }
    }

    /**
     * Resets the rotation of the sprite,
     * but also the hit boxes
     *
     * @param degrees   what the starting rotation
     *                  should be
     */
    public void resetRotation(float degrees) {
        super.setRotation(degrees);
        this.movementHitBox.setRotation(degrees);
        this.damageHitBox.setRotation(degrees);
    }

    /**
     * Get the health bar of the sprite.
     * @return The health bar of the sprite.
     */
    public ResourceBar getHealthBar() {
        return this.healthBar;
    }

    /**
     * Get the internal time of the sprite.
     * @return The internal time of the sprite.
     */
    public float getInternalTime() {
        return this.internalTime;
    }

    /**
     * Set the internal time of the sprite.
     * @param time new internal time
     */
    public void setInternalTime(int time) {
        this.internalTime = time;
    }

    /**
     * Get the movement hit box of the sprite.
     * @return The hit box of the sprite.
     */
    public Polygon getMovementHitBox() {
        return this.movementHitBox;
    }

    /**
     * Get the damage hit box of the sprite.
     * @return The hit box of the sprite.
     */
    public Polygon getDamageHitBox() {
        return this.damageHitBox;
    }

    /**
     * Get the current width of the sprite.
     * @return The width of the sprite.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Get the current height of the sprite.
     * @return The height of the sprite.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Get the value of the centre x-coordinate of the sprite.
     * @return The cente X-coordinate of the sprite.
     */
    public float getCentreX() {
        // Add half sprite width to get centre
        return this.getX() + this.getWidth() / 2;
    }

    /**
     * Get the value of the centre y-coordinate of the sprite.
     * @return The centre y-coordinate of the sprite.
     */
    public float getCentreY() {
        //Add half sprite height to get centre
        return this.getY() + this.getHeight() / 2;
    }

    public Vector2 getCentre() {
        return this.centre;
    }
    
    public void setCenter(Vector2 cent) {
        this.setCenter(cent.x, cent.y);
    }

    /**
     * Dispose of assets used by the class.
     */
    public void dispose() {
        texture.dispose();
    }
}