package ArcodeEngine.Engine

import org.joml.Matrix4f
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.system.MemoryUtil
import java.lang.IllegalStateException

class Window(var dimensions: Pair<Int, Int>) {
    var resized: Boolean = false

    private var errorCallback: GLFWErrorCallback? = null
    private var keyCallback: GLFWKeyCallback? = null
    private var resizeCallback: GLFWFramebufferSizeCallback? = null

    private var window: Long = 0

    private var aspectRatio: Float = dimensions.first.toFloat() / dimensions.second;
    private var cameraZoom: Float = 1f;

    companion object {
        lateinit var projectionMatrix: Matrix4f
    }

    init {
        projectionMatrix = Matrix4f().identity().ortho((-aspectRatio * cameraZoom) * 10f, (aspectRatio * cameraZoom) * 10f, -cameraZoom * 10f, cameraZoom * 10f, -1f, 1f);
        init()
    }

    private fun init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        errorCallback = GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        // Configure our window
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)

        // Create the window
        window = GLFW.glfwCreateWindow(dimensions.first, dimensions.second, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        keyCallback = GLFW.glfwSetKeyCallback(window, object : GLFWKeyCallback() {
            override fun invoke(window: Long,
                                key: Int,
                                scancode: Int,
                                action: Int,
                                mods: Int) {

                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                    GLFW.glfwSetWindowShouldClose(window, true)
                }

            }
        })

        resizeCallback = GLFW.glfwSetFramebufferSizeCallback(window
        ) { _, width, height -> dimensions = Pair(width, height); resized = true }

        // Get the resolution of the primary monitor
        val vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

        // Center our window
        if (vidmode != null) {
            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - dimensions.first) / 2,
                    (vidmode.height() - dimensions.second) / 2
            )
        };

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window)

        // Enable v-sync (Change to 0/GLFW_FALSE to turn it off)
        GLFW.glfwSwapInterval(GLFW.GLFW_TRUE)

        // Make the window visible
        GLFW.glfwShowWindow(window)
    }

    fun destroy() {
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null)?.free();
    }

    fun GetWindowHandle(): Long {
        return window
    }
}