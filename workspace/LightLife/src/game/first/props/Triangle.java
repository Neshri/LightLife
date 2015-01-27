package game.first.props;

import android.opengl.GLES20;
import game.first.graphics.GLRenderer;
import game.first.math.Vect3;
import util.InvalidFormatException;

public class Triangle extends Shape {

	public static final int COORDS_PER_VERTEX = 3;
	
	private int shaderProgram, positionHandle, colorHandle, mMVPMatrixHandle;
	
	
	private float color[];
	
	public Triangle(float base, float height, float color[], Vect3 pos) throws InvalidFormatException {
		super(pos);
		
		if (color.length != 4) {
			throw new InvalidFormatException(
					"A color should consist of 4 float values(r,g,b,a)");
		} else {
			super.installVertices(createVertices(base, height));
			this.color = color;
			
		}
	}
	
	private float[] createVertices(float base, float height) {
		float[] vertices = new float[9];
		float[] pos = this.getPos().getArray();
		for (int i = 0; i < 3; i++) {
			vertices[i] = pos[i];
		}
		vertices[3] = pos[0] + base;
		for (int i = 4; i < 6; i++) {
			vertices[i] = pos[i-3];
		}
		vertices[6] = pos[0] + base/2;
		vertices[7] = pos[1] + height;
		vertices[8] = pos[2];
		
		return vertices;
	}
	
	@Override
	public void draw(float[] mvpMatrix) {
		 // Add program to OpenGL ES environment
	    GLES20.glUseProgram(shaderProgram);

	    // get handle to vertex shader's vPosition member
	    positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");

	    // Enable a handle to the triangle vertices
	    GLES20.glEnableVertexAttribArray(positionHandle);
	    
	    //GLES20.glVertexAttrib3fv(positionHandle, super.modelMatrix, 0);

	    // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
	                                 GLES20.GL_FLOAT, false,
	                                 12, super.vertexBuffer);

	    
	    // get handle to fragment shader's vColor member
	    colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");

	    // Set color for drawing the triangle
	    GLES20.glUniform4fv(colorHandle, 1, color, 0);
	    
	    // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
        

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

	    // Draw the triangle
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(positionHandle);
	    
	    
	}

	@Override
	public void installGraphics() {
		int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				super.vertexShaderCode);
		
		int fragmentShader = GLRenderer.loadShader(
				GLES20.GL_FRAGMENT_SHADER, super.fragmentShaderCode);
		
		shaderProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(shaderProgram, fragmentShader);
		GLES20.glAttachShader(shaderProgram, vertexShader);
		GLES20.glLinkProgram(shaderProgram);
		
	}
}
