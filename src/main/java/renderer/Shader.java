package renderer;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;


public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;

    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] split = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf(System.lineSeparator(), index);
            String pattern1 = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf(System.lineSeparator(), index);
            String pattern2 = source.substring(index, eol).trim();

            if (pattern1.equals("vertex")) {
                vertexSource = split[1];
            } else if (pattern1.equals("fragment")) {
                fragmentSource = split[1];
            } else {
                throw new IOException("Unexpected token '" + pattern1 + "'");
            }

            if (pattern2.equals("vertex")) {
                vertexSource = split[2];
            } else if (pattern2.equals("fragment")) {
                fragmentSource = split[2];
            } else {
                throw new IOException("Unexpected token '" + pattern2 + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open shader file '" + filepath + "'";
        }

    }

    public void compile() {
        int vertexID, fragmentID;

        // compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl' \n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "' \n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "' \n\tLinking shaders failed.");
            System.out.println(glGetProgramInfoLog(fragmentID, len));
            assert false : "";
        }

    }

    public void use() {
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}
