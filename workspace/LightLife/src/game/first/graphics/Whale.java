//package game.first.graphics;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//import android.opengl.GLES20;
//
//import util.ObjReader;
//import util.ShaderReader;
//
//public class Whale {
//	
//	
//	FloatBuffer buffer;
//	
//	float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
//
//	public Whale() {
//		ObjReader reader = new ObjReader("./assets/Whale2");
//		float[] whaleCoords = reader.getVectorList();
//		ByteBuffer bb = ByteBuffer.allocateDirect(whaleCoords.length * 4);
//		bb.order(ByteOrder.nativeOrder());
//		buffer = bb.asFloatBuffer();
//		buffer.put(whaleCoords);
//		buffer.position(0);
//		
//		int vertexShader = 0;
//		int fragmentShader = 0;
//		ShaderReader vertexShaderCode = new ShaderReader("./assets/shaders/Whale2Vertex");
//		ShaderReader fragmentShaderCode = new ShaderReader("./assets/shaders/Whale2Fragment");
//		try {
//			String vertexCode = vertexShaderCode.getCode();
//			String fragmentCode = fragmentShaderCode.getCode();
//			vertexShader = FrameRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexCode);
//			fragmentShader = FrameRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentCode);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public void draw() {
//		
//	}
//}
