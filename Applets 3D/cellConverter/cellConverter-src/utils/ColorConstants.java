package utils;
import javax.vecmath.Color3f;

public interface ColorConstants {
	static final Color3f black = new Color3f(0f, 0f, 0f);
	static final Color3f white = new Color3f(1f, 1f, 1f);
	static final Color3f blue = new Color3f(0f, 0f, 1f);
	static final Color3f red = new Color3f(1f, 0f, 0f);
	static final Color3f yellow = new Color3f(1f, 1f, 0f);
	static final Color3f green = new Color3f(0f, 1f, 0f);
	static final Color3f cyan = new Color3f(0f, 1f, 1f);
	static final Color3f magenta = new Color3f(1f, 0f, 1f);
	static final Color3f orange = new Color3f(1f, .7f, 0f);

	static final Color3f[] colorList = {blue, red, yellow, orange, green, white, magenta, cyan};
	static final String[] colorListNames = {"blue", "red", "yellow", "orange", "green", "white", "magenta", "cyan"};
}
