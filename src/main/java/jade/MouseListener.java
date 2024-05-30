package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;

    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];

    private boolean isDragging;

    private MouseListener() {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        MouseListener m = get();
        m.lastX = m.xPos;
        m.lastY = m.yPos;
        m.xPos = xpos;
        m.yPos = ypos;

        m.isDragging = m.mouseButtonPressed[0] || m.mouseButtonPressed[1] || m.mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        MouseListener m = get();

        if (action == GLFW_PRESS) {
            if (button < m.mouseButtonPressed.length) {
                m.mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            m.mouseButtonPressed[button] = false;
            m.isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        MouseListener m = get();

        m.scrollX = xOffset;
        m.scrollY = yOffset;
    }

    public static void endFrame() {
        MouseListener m = get();

        m.scrollX = 0;
        m.scrollY = 0;
        m.lastX = m.xPos;
        m.lastY = m.yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDX() {
        double current = get().xPos;
        return (float) (instance.lastX - current);
    }

    public static float getDY() {
        double current = get().yPos;
        return (float) (instance.lastY - current);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        MouseListener m = get();

        if (button < m.mouseButtonPressed.length) {
            return  m.mouseButtonPressed[button];
        }
        return false;
    }

}
