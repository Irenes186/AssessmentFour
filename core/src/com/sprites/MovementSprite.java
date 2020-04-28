package com.sprites;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.misc.Constants;
import com.misc.Constants.Direction;
//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

// Constants import

import static com.misc.Constants.TILE_DIMS;
import static com.misc.Constants.FIRETRUCK_ROTATIONRATE;

/**
 * MovementSprite adds movement facilities to a sprite.
 * @author Archie
 * @since 08/12/2019
 */
public class MovementSprite extends SimpleSprite {

    // physics values
    private float accelerationRate, decelerationRate, maxSpeed, rotationLockTime;
    private Vector2 speed;
    private float speedMagnitude;

    // layer that provides collisions
    private TiledMapTileLayer collisionLayer;

    /**
     * Creates a sprite capable of moving and colliding with the tiledMap and other sprites.
     * 
     * @param spriteTexture  The texture the sprite should use.
     * @param collisionLayer The layer of the map the sprite will collide with.
     */
    public MovementSprite(Texture spriteTexture, TiledMapTileLayer collisionLayer) {
        super(spriteTexture, collisionLayer);
        this.collisionLayer = collisionLayer;
        this.create();
    }

    /**
     * Creates a sprite capable of moving and but only colliding with other sprites.
     * 
     * @param spriteTexture  The texture the sprite should use.
     */
    public MovementSprite(Texture spriteTexture) {
        super(spriteTexture, null);
        this.create();
    }

    /**
     * Sets the initial values for all properties needed by the sprite.
     */
    private void create() {
        this.speed = new Vector2();
        this.speedMagnitude = 0;
        this.accelerationRate = 10;
        this.decelerationRate = 6;
        this.rotationLockTime = 0;
        this.maxSpeed = 200;
    }

    /**
     * Update the sprite position and direction based on acceleration and
     * boundaries. This is called every game frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        // Calculate the acceleration on the sprite and apply it
        accelerate();

        // Rotate sprite to face the direction its moving in
//        updateRotation();

        super.update(batch);
        // Update rotationLockout if set
        if (this.rotationLockTime >= 0) this.rotationLockTime -= 1; 
    }

    /**
     * Increases the speed of the sprite in the given direction.
     * @param direction The direction to accelerate in.
     */
    public void applyAcceleration(Direction direction) {
        if (speedMagnitude < maxSpeed && direction == Direction.UP) {
            speedMagnitude += this.accelerationRate;
            speed.x = MathUtils.cosDeg(getRotation()) * speedMagnitude;
            speed.y = MathUtils.sinDeg(getRotation()) * speedMagnitude;
//            speedMagnitude = (float) Math.sqrt(Math.pow(speed.x, 2) + Math.pow(speed.y, 2));
        }
        if (speedMagnitude > -this.maxSpeed && direction == Direction.DOWN) {
            speedMagnitude -= this.accelerationRate;
            speed.x = MathUtils.cosDeg(getRotation()) * speedMagnitude;
            speed.y = MathUtils.sinDeg(getRotation()) * speedMagnitude;
        }
        if (direction == Direction.RIGHT) {
            this.rotate(-FIRETRUCK_ROTATIONRATE * Gdx.graphics.getDeltaTime());
        }
        if (direction == Direction.LEFT) {
            this.rotate(FIRETRUCK_ROTATIONRATE * Gdx.graphics.getDeltaTime());
        }
    }


    /*
     *  =======================================================================
     *                          Modified for Assessment 3
     *  =======================================================================
     */
    /**
     * Apply acceleration to the sprite, based on collision boundaries and
     * existing acceleration.
     */
    private void accelerate() {
        float oldX = this.getX();
        float oldY = this.getY();
        this.movementHitBox.setPosition(oldX + speed.x * Gdx.graphics.getDeltaTime(), oldY + speed.y * Gdx.graphics.getDeltaTime());
        Constants.Direction collisiondirection = this.collisionDirection(this.collisionLayer);
        // Check if it collides with any tiles, then move the sprite
        switch (collisiondirection) {
            case LEFT:
            case RIGHT:
                speedMagnitude = speed.y;
                speed.x = 0;
                this.movementHitBox.setPosition(oldX, this.movementHitBox.getY());
                this.setY(oldY + speed.y * Gdx.graphics.getDeltaTime());
                break;
            case UP:
            case DOWN:
                speedMagnitude = speed.x;
                speed.y = 0;
                this.setX(oldX + speed.x * Gdx.graphics.getDeltaTime());
                this.movementHitBox.setPosition(this.movementHitBox.getX(), oldY);
                break;
            default:
                this.setX(oldX + speed.x * Gdx.graphics.getDeltaTime());
                this.setY(oldY + speed.y * Gdx.graphics.getDeltaTime());
        }
        if (this.decelerationRate != 0) decelerate();
    }

    /**
     * Decreases the speed of the sprite (direction irrelevant). Deceleration rate
     * is based upon the sprite's properties.
     */
    private void decelerate() {
        if (speedMagnitude > decelerationRate) {
            speedMagnitude -= decelerationRate;
            speed.x = MathUtils.cosDeg(getRotation()) * speedMagnitude;
            speed.y = MathUtils.sinDeg(getRotation()) * speedMagnitude;
        } else if (speedMagnitude < -decelerationRate) {
            speedMagnitude += decelerationRate;
            speed.x = MathUtils.cosDeg(getRotation()) * speedMagnitude;
            speed.y = MathUtils.sinDeg(getRotation()) * speedMagnitude;
        } else {
            speedMagnitude = 0;
            speed.x = 0;
            speed.y = 0;
        }
    }

    /**
     * Sets the rate at which the sprite will accelerate.
     * @param rate The acceleration rate for the sprite.
     */
    public void setAccelerationRate(float rate) {
        this.accelerationRate = rate;
    }
    
    public float getAccelerationRate() {
        return this.accelerationRate;
    }

    /**
     * Sets the rate at which the sprite will decelerate.
     * @param rate The deceleration rate for the sprite.
     */
    public void setDecelerationRate(float rate) {
        this.decelerationRate = rate;
    }

    /**
     * Sets the max speed the sprite can accelerate to.
     * @param amount The max speed value for the sprite.
     */
    public void setMaxSpeed(float amount) {
        this.maxSpeed = amount;
    }

     /**
     * Returns the max speed the sprite can accelerate to.
     * @return  The max speed value for the sprite.
     */
    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    /**
     * Sets the current speed of the sprite.
     * @param speed The speed the sprite should travel.
     */
    public void setSpeed(Vector2 speed) {
        this.speed.x = speed.x;
        this.speed.y = speed.y;
    }
}