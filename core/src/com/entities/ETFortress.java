package com.entities;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

import org.json.simple.JSONObject;
import java.io.StringWriter;

// Custom class import
import com.misc.Constants.*;
import com.screens.GameScreen;
import com.sprites.SimpleSprite;

// Constants import
import static com.misc.Constants.ETFORTRESS_HEIGHT;
import static com.misc.Constants.ETFORTRESS_WIDTH;

/**
 * The ET Fortress implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class ETFortress extends SimpleSprite {

    // Private values for this class to use
    private final Texture destroyed;
    private boolean flooded;
    private final FortressType type;
    private final GameScreen gameScreen;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture           The texture used to draw the ETFortress with.
     * @param destroyedTexture  The texture used to draw the ETFortress with. when it has been destroyed.
     * @param scaleX            The scaling in the x-axis.
     * @param scaleY            The scaling in the y-axis.
     * @param xPos              The x-coordinate for the ETFortress.
     * @param yPos              The y-coordinate for the ETFortress.
     * @param type              {@link FortressType} given to fortress
     * @param gameScreen        GameScreen to send popup messages to
     */
    public ETFortress(Texture texture, Texture destroyedTexture, float scaleX, float scaleY, float xPos, float yPos, FortressType type, GameScreen gameScreen) {
        super(texture);
        this.gameScreen = gameScreen;
        this.destroyed = destroyedTexture;
        this.flooded = false;
        this.type = type;
        this.setScale(scaleX, scaleY);
        this.setPosition(xPos, yPos);
        this.setSize(ETFORTRESS_WIDTH * this.getScaleX(), ETFORTRESS_HEIGHT * this.getScaleY());
        this.getHealthBar().setMaxResource((int) (type.getHealth() * com.misc.Constants.getInstance().difficulty));
        super.resetRotation(90);
    }

    /**
     * Update the fortress so that it is drawn every frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        // If ETFortress is destroyed, change to flooded texture
        // If ETFortress is damaged, heal over time
        if (!flooded && this.getHealthBar().getCurrentAmount() <= 0) {
            this.removeSprite(this.destroyed);
            this.flooded = true;
            this.gameScreen.showPopupText("You have destroyed " + gameScreen.getETFortressesDestroyed()[0] + " / " + gameScreen.getETFortressesDestroyed()[1] +
                    " fortresses", 1, 7);
        } else if (!flooded && this.getInternalTime() % 150 == 0 && this.getHealthBar().getCurrentAmount() != this.getHealthBar().getMaxAmount()) {
            // Heal ETFortresses every second if not taking damage
			this.getHealthBar().addResourceAmount(type.getHealing());
        }
    }

    /**
     * Check to see if the ETFortress should fire another projectile. The pattern the ETFortress
     * uses to fire can be modified here. 
     * 
     * @return boolean  Whether the ETFortress is ready to fire again (true) or not (false)
     */
    public boolean canShootProjectile() {
        return this.getHealthBar().getCurrentAmount() > 0 && this.getInternalTime() < 120 && this.getInternalTime() % 30 == 0;
    }

    // ==============================================================
    //			          Edited for Assessment 3
    // ==============================================================
    /**
     * Simple method for detecting whether a vector, fire truck,
     * is within attacking distance of that fortress' range
     *
     * @param position  vector to check
     * @return          <code>true</code> position is within range
     *                  <code>false</code> otherwise
     */
    public boolean isInRadius(Vector2 position) {
        return this.getCentre().dst(position) <= type.getRange();
    }

    /**
     * Overloaded method for drawing debug information. Draws the hitbox as well
     * as the range circle.
     * 
     * @param renderer  The renderer used to draw the hitbox and range indicator with.
     */
    @Override
    public void drawDebug(ShapeRenderer renderer) {
        super.drawDebug(renderer);
        renderer.circle(this.getCentreX(), this.getCentreY(), this.type.getRange());
    }

    /**
     * Returns whether or not the fortess has been flooded
     * 
     * @return boolean true if the fortress has been flooded otherwise false
     */
    public boolean isFlooded() {
        return this.flooded;
    }

    /**
     * Return which type of fortress this is from the fortresstype enum
     * 
     * @return FortressType which fortress this is
     */
    public FortressType getType() {
        return this.type;
    }

    public String save() {

        JSONObject json = new JSONObject();

        json.put("FortType", this.getType().name());
        json.put("Health", this.getHealthBar().getCurrentAmount());

        StringWriter out = new StringWriter();

        try {
            json.writeJSONString(out);
        } catch (Exception e) {
            System.out.println(e);
        }

        return out.toString();
    }
}
