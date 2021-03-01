package Games.Pong;

import ArcodeEngine.Engine.Controller;
import ArcodeEngine.Engine.Geometry.Text;
import ArcodeEngine.Engine.Util.Direction;
import org.joml.*;

import ArcodeEngine.Engine.GFX.*;
import ArcodeEngine.Engine.Window;
import ArcodeEngine.Engine.GameState;
import ArcodeEngine.Engine.Geometry.Rectangle;

public class PongExample extends GameState {
    private final Window window;

    private Rectangle leftPaddle = null;
    private Rectangle rightPaddle = null;
    private Rectangle ball = null;

    private Text leftScoreText = null;
    private Text rightScoreText = null;

    private int leftScore = 0;
    private int rightScore = 0;

    private final float BALL_H_SPEED = 0.16f;
    private final float BALL_V_SPEED = 0.14f;
    private final Vector2f ballVelocity = new Vector2f(BALL_H_SPEED, 0f);

    private int ticks = 0;

    public PongExample(Window window) {
        super("Pong", window);
        this.window = window;
    }

    @Override
    public void Init() {
        leftPaddle = new Rectangle(1f, 10f, 2f, 9f);
        rightPaddle = new Rectangle(window.GetMaxWidth() - 3, 10f, 2f, 9f);
        ball = new Rectangle(window.GetMaxWidth() / 2, window.GetMaxHeight() / 2, 2f, 2f);

        leftScoreText = new Text(0f, window.GetMaxHeight() - 6f, Integer.toString(leftScore), 2f);
        rightScoreText = new Text(window.GetMaxWidth() - 8f, window.getMaxHeight() - 6f, Integer.toString(rightScore), 2f);
    }

    @Override
    public void Tick() {
        float leftJoystick = Controller.GetJoystickState(1).GetY() * -1f * 0.3f;
        float rightJoystick = Controller.GetJoystickState(2).GetY() * -0.3f;

        leftPaddle.Move(Direction.UP, leftJoystick);
        rightPaddle.Move(Direction.UP, rightJoystick);

        if(ball.IsColliding(0f, window.GetMaxWidth(), 0f, window.GetMaxHeight())) {
            ballVelocity.y *= -1;
        }

        if(leftPaddle.IsColliding(ball)) {
            if(ball.GetY() > leftPaddle.GetY() + (leftPaddle.GetHeight() * (2f / 3f))) {
                ballVelocity.y = BALL_V_SPEED;
                ballVelocity.x = BALL_H_SPEED / 2;
            } else if(ball.GetY() < leftPaddle.GetY() + (leftPaddle.GetHeight() / 3f)) {
                ballVelocity.y = -BALL_V_SPEED;
                ballVelocity.x = BALL_H_SPEED / 2;
            } else {
                ballVelocity.y = 0f;
                ballVelocity.x = BALL_H_SPEED;
            }

        } else if(ball.GetX() < leftPaddle.GetX() + leftPaddle.GetWidth() - 0.5f) {
            ball = new Rectangle(window.GetMaxWidth() / 2, window.GetMaxHeight() / 2, 2f, 2f);
            ballVelocity.x = BALL_H_SPEED;
            ballVelocity.y = 0f;
            rightScoreText.SetMsg(Integer.toString(++rightScore));
        }

        if(rightPaddle.IsColliding(ball)) {
            if(ball.GetY() > rightPaddle.GetY() + (rightPaddle.GetHeight() * (2f / 3f))) {
                ballVelocity.y = BALL_V_SPEED;
                ballVelocity.x = -BALL_H_SPEED / 2;
            } else if(ball.GetY() < rightPaddle.GetY() + (rightPaddle.GetHeight() / 3f)) {
                ballVelocity.y = -BALL_V_SPEED;
                ballVelocity.x = -BALL_H_SPEED / 2;
            } else {
                ballVelocity.y = 0f;
                ballVelocity.x = -BALL_H_SPEED;
            }

        } else if(ball.GetX() + ball.GetWidth() > rightPaddle.GetX() + 0.5f) {
            ball = new Rectangle(window.GetMaxWidth() / 2, window.GetMaxHeight() / 2, 2f, 2f);
            ballVelocity.x = -BALL_H_SPEED;
            ballVelocity.y = 0f;
            leftScoreText.SetMsg(Integer.toString(++leftScore));
        }

        if(leftPaddle.GetY() < 0) {
            leftPaddle.SetY(0);
        } else if((leftPaddle.GetY() + rightPaddle.GetHeight()) > window.GetMaxHeight()) {
            leftPaddle.SetY(window.GetMaxHeight() - leftPaddle.GetHeight());
        }

        if(rightPaddle.GetY() < 0) {
            rightPaddle.SetY(0);
        } else if((rightPaddle.GetY() + rightPaddle.GetHeight()) > window.GetMaxHeight()) {
            rightPaddle.SetY(window.GetMaxHeight() - rightPaddle.GetHeight());
        }

        ball.Move(ballVelocity);
        ticks++;
    }

    @Override
    public void Render() {
        Renderer.DrawColoredRect(window, leftPaddle, new Vector3f(0f, 1f, 1f));
        Renderer.DrawColoredRect(window, rightPaddle, new Vector3f(0f, 1f, 1f));
        Renderer.DrawColoredRect(window, ball, new Vector3f(1f, 0f, 0f));

        TextRenderer.DrawString(window, leftScoreText);
        TextRenderer.DrawString(window, rightScoreText);
    }

}