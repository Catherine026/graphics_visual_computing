package com.mycompany.myapp;

import android.content.*;
import android.opengl.*;
import com.mycompany.myapp.util.*;
import java.nio.*;
import javax.microedition.khronos.opengles.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import static android.opengl.GLES20.*;
import android.opengl.GLSurfaceView.Renderer;


public class AirHockeyRenderer implements Renderer
{
	private static final int POSITION_COMPONENT_COUNT=2;
	private static final int BYTES_PER_FLOAT=4;
	private final FloatBuffer vertexData;
	private final Context context;
	private int program;
	private static final String U_COLOR="u_Color";
	private int uColorLocation;
	private static final String A_POSITION="a_Position";
	private int aPositionLocation;
	
	public AirHockeyRenderer(Context context)
	{
		this.context=context;
		
		float[] tableVerticesWithTriangles=
		{
			//big cube
			//Triangle 1
		   -0.0f,0.7f,
		   -0.7f,0.0f,
		   -0.0f,-0.7f,
			
			//Triangle 2
	       -0.0f,0.7f,
			0.7f,-0.0f,
		   -0.0f,-0.7f,
			
			//Triangle 3
		   -0.0f,0.7f,
			0.7f,0f,
			1.0f,0.0f,
			
			//Triangle 4
		   -0f,-0.7f,
			0.7f,-0.0f,
			1.0f,0.0f,
			
			//Triangle 7
			-0f,0.7f,
			0.6f,0.4f,
			0.35f,0.65f,

			//Triangle 8
			-0f,-0.7f,
			0.6f,-0.4f,
			0.35f,-0.65f,	
			
			//Triangle 5
			-0.0f, 0.7f,
			0.6f,0.4f,
			1.0f,0.0f,
			
			//Triangle 6
			-0.0f,-0.7f,
			0.6f,-0.4f,
			1.0f, 0.0f,
			
			//heart
			//Triangle1
			0.1f,0.1f,
			-0.2f,-0.2f,
			-0.21f,0.26f,
		
			//Triangle2
			0.1f,0.1f,
			-0.21f,0.26f,
			-0.18f,0.4f,
			
			//Triangle3
			0.1f,0.1f,
			-0.18f,0.4f,
			-0.1f,0.46f,
			
			//Triangle4
			0.1f,0.1f,
			-0.1f,0.46f,
			0.045f,0.43f,
			
			//Triangle5
			0.045f,0.43f,
			0.07f,0.28f,
			0.14f,0.35f,
		
			//Triangle6
			0.07f,0.28f,
			0.14f,0.35f,
			0.16f,0.23f,
			
			//Triangle7
			0.07f,0.28f,
			0.16f,0.23f,
			0.1f,0.1f,
			
			//Triangle8
			-0.2f,-0.2f,
			0.26f,-0.21f,
			0.1f,0.1f,
			
			//Triangle9
			0.4f,-0.18f,
			0.26f,-0.21f,
			0.1f,0.1f,
			
			//Triangle10
			0.4f,-0.18f,
			0.46f,-0.1f,
			0.1f,0.1f,
			
			//Triangle11
			0.46f,-0.1f,
			0.43f,0.045f,
			0.1f,0.1f,
			
			//Triangle12
			0.43f,0.045f,
			0.28f,0.07f,
			0.35f,0.14f,
			
			//Triangle13
			0.28f,0.07f,
			0.35f,0.14f,
			0.23f,0.16f,
			
			//Triangle14
			0.28f,0.07f,
			0.23f,0.16f,
			0.1f,0.1f,
			
			//smallcube
			//Triangle1
			-0.3f,-0.2f,
			-0.8f,-0.8f,
			-0.78f,-0.35f,
			
			//Triangle2
			-0.3f,-0.2f,
			-0.3f,-0.65f,
			-0.8f,-0.8f,
			
			//Triangle3
			-0.3f,-0.2f,
			-0.05f,-0.5f,
			-0.05f,-0.3f,
			
			//Triangle5
			-0.3f,-0.2f,
			-0.05f,-0.76f,
			-0.3f,-0.46f,
			
			//Triangle7
			-0.05f,-0.76f,
			-0.3f,-0.65f,
			-0.45f,-0.695f,

			//Triangle9
			-0.05f,-0.76f,
			-0.325f,-0.835f,
			-0.8f,-0.8f,
			
			//Triangle4
			-0.3f,-0.2f,
			-0.05f,-0.5f,
			-0.05f,-0.76f,
			
			//Triangle6
			-0.3f,-0.46f,
			-0.05f,-0.76f,
			-0.3f,-0.65f,
			
			//Triangle8
			-0.45f,-0.695f,
			-0.8f,-0.8f,
			-0.05f,-0.76f,
			
			//Triangle10
			-0.325f,-0.835f,
			-0.8f,-0.8f,
			-0.485f,-0.88f,
			
			//star
			//Triangle1
			-0.48f, -0.4f,
			-0.57f, -0.3f,
			-0.62f,-0.43f,
			
			//Triangle2
			-0.48f,-0.4f,
			-0.34f,-0.365f,
			-0.43f,-0.5f,
			
			//Triangle3
			-0.43f,-0.5f,
			-0.37f,-0.635f,
			-0.52f,-0.6f,

			//Triangle4
			-0.695f,-0.7f,
			-0.52f,-0.6f,
			-0.66f,-0.54f,
			
			//Triangle5
			-0.62f,-0.43f,
			-0.66f,-0.54f,
			-0.75f,-0.465f,
			
			//pentagon inside the star
			//Triangle1
			-0.48f,-0.4f,
			-0.62f,-0.43f,
			-0.52f,-0.6f,
			
			//Triangle2
			-0.48f,-0.4f,
			-0.43f,-0.5f,
			-0.52f,-0.6f,
			
			//Triabgle3
			-0.62f,-0.43f,
			-0.66f,-0.54f,
			-0.52f,-0.6f,
			
			//Letter - L
			//Triangle1
			-0.056f,0.15f,
			-0.15f,0.1f,
			-0.08f,0.18f,

			//Triangle2
			-0.056f,0.15f,
			-0.15f,0.1f,
			-0.09f,0.1f,

			//Triangle3
			-0.056f,0.08f,
			-0.15f,0.1f,
			-0.09f,0.1f,

			//Triangle4
			-0.15f,0.1f,
			-0.08f,0.04f,
			-0.056f,0.08f,
			
			//Letter - O
			//Triangle1
			0.01f,0.1f,
			0.02f,0.07f,
			-0.025f,0.1f,

			//Triangle2
			-0.025f,0.1f,
			0.02f,0.07f,
			-0.06f,0.07f,

			//Triangle3
			-0.06f,0.07f,
			-0.015f,0.0695f,
			-0.065f,0.03f,

			//Triangle4
			-0.04f,0.05f,
			-0.065f,0.03f,
			-0.025f,-0.01f,
			
			//Triangle5
			-0.03f,-0.01f,
			-0.035f,0.035f,
			0.01f,-0.01f,

			//Triangle6
			-0.02f,0.02f,
			0.05f,0.035f,
			0.01f,-0.01f,

			//Triangle7
			0.05f,0.07f,
			0.0155f,0.03f,
			0.05f,0.03f,

			//Triangle8
			0.01f,0.1f,
			0.05f,0.07f,
			0.025f,0.04f,
			
			//Letter - V
			//Triangle1
			0.08f,0.03f,
			0.05f,0.06f,
			0.01f,-0.05f,

			//Triangle2
			0.08f,0.03f,
			0.01f,-0.05f,
			0.06f,-0.03f,

			//Triangle3
			0.01f,-0.05f,
			0.06f,-0.03f,
			0.09f,-0.06f,

			//Triangle4
			0.01f,-0.05f,
			0.04f,-0.08f,
			0.09f,-0.06f,

			//Triangle5
			0.06f,-0.03f,
			0.15f, -0.035f,
			0.09f,-0.06f,

			//Triangle6
			0.06f,-0.03f,
			0.11f,-0.01f,
			0.15f,-0.035f,
			
			//Letter - E
			//Triangle1
			0.19f,-0.08f,
			0.16f,-0.1f,
			0.13f,-0.06f,


			//Triangle2
			0.19f,-0.08f,
			0.13f,-0.02f,
			0.13f,-0.06f,

			//Triangle3
			0.13f,-0.02f,
			0.13f,-0.06f,
			0.11f,-0.07f,

			//Triangle4
			0.13f,-0.03f,
			0.115f,-0.07f,
			0.085f,-0.06f,

			//Triangle5
			0.085f,-0.055f,
			0.14f,-0.09f,
			0.105f,-0.09f,

			//Triangle6
			0.14f,-0.09f,
			0.105f,-0.08f,
			0.115f,-0.11f,

			//Triangle7
			0.085f,-0.055f,
			0.105f,-0.09f,
			0.09f,-0.1f,

			//Triangle8
			0.085f,-0.06f,
			0.09f,-0.1f,
			0.04f,-0.09f,

			//Triangle9
			0.09f,-0.1f,
			0.04f,-0.09f,
			0.1f,-0.15f,

			//Triangle10
			0.09f,-0.1f,
			0.1f,-0.15f,
			0.13f,-0.135f,
			
			//Extra
			//Triangle1
			-0.465f,-0.235f,
			-0.57f,-0.285f,
			-0.61f,-0.09f,

			//triangle2
			-0.465f,-0.235f,
			-0.455f,-0.248f,
			-0.57f,-0.285f,

			//Triangle3
			-0.57f,-0.25f,
			-0.61f,-0.09f,
			-0.72f,-0.29f,

			//Triangle4
			-0.83f,-0.22f,
			-0.61f,-0.09f,
			-0.58f,-0.23f,

			//Triangle5
			-0.69f,-0.195f,
			-0.61f,-0.09f,
			-0.89f,-0.125f,

			//Triangle6
			-0.78f,-0.12f,
			-0.61f,-0.09f,
			-0.89f,-0.01f,

			//Triangle7
			-0.82f,0.129f,
			-0.61f,-0.09f,
			-0.795f,-0.04f,

			//Triangle8
			-0.7f,0.0f,
			-0.7f,0.23f,
			-0.765f,0.07f,

			//Triangle9
			-0.7f,0.0f,
			-0.6f,0.23f,
			-0.72f,0.1f,

			//Triangle10
			-0.7f,0.0f,
			-0.48f,0.22f,
			-0.66f,0.12f,

			//LETTER C
			//Triangle1
			-0.815f,0.77f,
			-0.815f,0.8f,
			-0.9f,0.8f,

			//Triangle2
			-0.815f,0.77f,
			-0.9f,0.77f,
			-0.9f,0.8f,

			//Triangle3
			-0.9f,0.8f,
			-0.9f,0.77f,
			-0.95f,0.8f,

			//Triangle4
			-0.9f,0.8f,
			-0.95f,0.8f,
			-0.9f,0.915f,

			//Triangle5
			-0.95f,0.8f,
			-0.95f,0.915f,
			-0.9f,0.915f,

			//Triangle6
			-0.95f,0.915f,
			-0.9f,0.915f,
			-0.9f,0.95f,

			//Triangle7
			-0.9f,0.95f,
			-0.9f,0.915f,
			-0.82f,0.915f,

			//Triangle8
			-0.9f,0.95f,
			-0.82f,0.95f,
			-0.82f,0.915f,

			//LETTER A
			//Triangle1
			-0.8f,0.77f,
			-0.755f,0.77f,
			-0.8f,0.915f,

			//Triangle2
			-0.8f,0.915f,
			-0.755f,0.77f,
			-0.755f,0.915f,

			//Triangle3
			-0.715f,0.77f,
			-0.665f,0.915f,
			-0.665f,0.77f,

			//Triangle4
			-0.715f,0.77f,
			-0.665f,0.915f,
			-0.715f,0.915f,

			//Triangle5
			-0.8f,0.915f,
			-0.755f,0.95f,
			-0.755f,0.915f,

			//Triangle6
			-0.665f,0.915f,
			-0.715f,0.95f,
			-0.715f,0.915f,

			//Triangle7
			-0.715f,0.95f,
			-0.715f,0.915f,
			-0.755f,0.915f,

			//Triangle8
			-0.715f,0.95f,
			-0.755f,0.915f,
			-0.755f,0.95f,

			//Triangle9
			-0.71f,0.87f,
			-0.71f,0.83f,
			-0.76f,0.83f,

			//Triangle10
			-0.71f,0.87f,
			-0.76f,0.83f,
			-0.76f,0.87f,

			//LETTER T
			//Triangle1
			-0.58f,0.77f,
			-0.62f,0.77f,
			-0.58f,0.92f,

			//Triangle2
			-0.62f,0.77f,
			-0.58f,0.92f,
			-0.62f,0.92f,

			//Triangle3
			-0.55f,0.92f,
			-0.55f,0.95f,
			-0.65f,0.92f,

			//Triangle4
			-0.55f,0.95f,
			-0.65f,0.92f,
			-0.65f,0.95f,

			//LETTER H
			//Triangle1
			-0.39f,0.77f,
			-0.39f,0.95f,
			-0.44f,0.77f,

			//Triangle2
			-0.39f,0.95f,
			-0.44f,0.95f,
			-0.44f,0.77f,

			//Triangle3
			-0.48f,0.77f,
			-0.48f,0.95f,
			-0.53f,0.95f,

			//Triangle4
			-0.48f,0.77f,
			-0.53f,0.77f,
			-0.53f,0.95f,

			//Triangle1
			-0.44f,0.89f,
			-0.48f,0.89f,
			-0.48f,0.85f,

			//Triangle2
			-0.44f,0.89f,
			-0.48f,0.85f,
			-0.44f,0.85f,

			//under CATH
			//BLUE
			//Triangle1
			-0.99f,0.75f,
			-0.94f,0.7f,
			-0.875f,0.75f,

			//Triangle3
			-0.925f,0.75f,
			-0.81f,0.7f,
			-0.75f,0.75f,

			//Triangle5
			-0.75f,0.75f,
			-0.7f,0.7f,
			-0.64f,0.75f,

			//Triangle7
			-0.64f,0.75f,
			-0.585f,0.7f,
			-0.525f,0.75f,

			//Triangle9
			-0.525f,0.75f,
			-0.455f,0.7f,
			-0.395f,0.75f,

			//PINK
			//Triangle2
			-0.94f,0.7f,
			-0.875f,0.75f,
			-0.81f,0.7f,

			//Triangle4
			-0.81f,0.7f,
			-0.75f,0.75f,
			-0.7f,0.7f,

			//Triangle6
			-0.7f,0.7f,
			-0.64f,0.75f,
			-0.585f,0.7f,

			//Triangle8
			-0.585f,0.7f,
			-0.525f,0.75f,
			-0.455f,0.7f,

			//Triangle10
			-0.455f,0.7f,
			-0.395f,0.75f,
			-0.33f,0.7f,

			//HeartBesideCATH
			//Triangle1
			-0.2f,0.85f,
			-0.215f,0.9f,
			-0.265f,0.9f,

			//Triangle2
			-0.215f,0.9f,
			-0.265f,0.9f,
			-0.25f,0.93f,

			//Triangle3
			-0.25f,0.93f,
			-0.265f,0.9f,
			-0.3f,0.93f,

			//Triangle4
			-0.2f,0.85f,
			-0.3f,0.93f,
			-0.33f,0.9f,

			//Triangle5
			-0.33f,0.9f,
			-0.2f,0.85f,
			-0.33f,0.87f,

			//Triangle6
			-0.3f,0.83f,
			-0.2f,0.85f,
			-0.2f,0.78f,

			//Triangle6
			-0.12f,0.82f,
			-0.2f,0.85f,
			-0.2f,0.78f,

			//Triangle6
			-0.07f,0.87f,
			-0.12f,0.82f,
			-0.2f,0.85f,

			//Triangle7
			-0.07f,0.87f,
			-0.2f,0.85f,
			-0.07f,0.9f,

			//Triangle8
			-0.2f,0.85f,
			-0.07f,0.9f,
			-0.1f,0.93f,

			//Triangle9
			-0.1f,0.93f,
			-0.135f,0.9f,
			-0.15f,0.93f,

			//Triangle10
			-0.15f,0.93f,
			-0.135f,0.9f,
			-0.185f,0.9f,

			//Triangle11
			-0.185f,0.9f,
			-0.2f,0.85f,
			-0.135f,0.9f,

			//Triangle12
			-0.2f,0.85f,
			-0.33f,0.87f,
			-0.3f,0.83f,
			
			//another heart
			//Triangle12
			-0.06f,0.87f,
			-0.07f,0.89f,
			-0.08f,0.87f,
			
			//Triangle2 
			-0.07f,0.89f,
			-0.08f,0.87f,
			-0.09f,0.9f,
			
			//Triangle3
			-0.08f,0.87f,
			-0.09f,0.9f,
			-0.11f,0.9f,
			
			//Triangle4
			-0.11f,0.9f,
			-0.06f,0.87f,
			-0.14f,0.87f,
			
			//Triangle5
			-0.13f,0.85f,
			-0.06f,0.87f,
			-0.120f,0.83f,
			
			//Triangle6
			-0.06f,0.87f,
			-0.14f,0.87f,
			-0.13f,0.85f,
			
			//Triangle7
			-0.120f,0.83f,
			-0.06f,0.87f,
			-0.06f,0.8f,
			
			//half heart
			//Triangle1
			-0.06f,0.87f,
			-0.06f,0.8f,
			-0.01f,0.83f,
			
			//Triangle2
			-0.06f,0.87f,
			0.005f,0.85f,
			-0.01f,0.83f,
			
			//Triangle3
			-0.06f,0.87f,
			0.005f,0.85f,
			0.013f,0.87f,
			
			//Triangle4
			0.013f,0.87f,
			-0.01f,0.9f,
			-0.03f,0.87f,
			
			//Triangle5
			-0.03f,0.87f,
			-0.01f,0.9f,
			-0.03f,0.9f,
			
			//Triangle6
			-0.03f,0.9f,
			-0.05f,0.89f,
			-0.03f,0.87f,
			
			//Triangle7
			-0.03f,0.87f,
			-0.06f,0.87f,
			-0.05f,0.89f,
			
			//I<3U
			//<3 HALF
			//Triangle1
			0.67f,-0.98f,
			0.67f,-0.9f,
			0.58f,-0.93f,
			
			//Triangle2
			0.58f,-0.93f,
			0.67f,-0.9f,
			0.56f,-0.9f,
			
			//Triangle3
			0.56f,-0.9f,
			0.67f,-0.9f,
			0.56f,-0.87f,
			
			//Triangle4
			0.56f,-0.87f,
			0.59f,-0.845f,
			0.67f,-0.9f,
			
			//Triangle5
			0.59f,-0.845f,
			0.63f,-0.845f,
			0.635f,-0.875f,
			
			//Triangle6
			0.635f,-0.875f,
			0.66f,-0.87f,
			0.63f,-0.845f,
			
			//Triangle7
			0.67f,-0.9f,
			0.66f,-0.87f,
			0.635f,-0.875f,
			
			//other half
			//Triangle1
			0.67f,-0.98f,
			0.67f,-0.9f,
			0.76f,-0.93f,
			
			//Triangle2
			0.76f,-0.93f,
			0.67f,-0.9f,
			0.79f,-0.9f,
			
			//Triangle3
			0.67f,-0.9f,
			0.79f,-0.9f,
			0.79f,-0.87f,
			
			//Triangle4
			0.67f,-0.9f,
			0.79f,-0.87f,
			0.76f,-0.845f,
		
			//Triangle5
			0.76f,-0.845f,
			0.725f,-0.845f,
			0.715f,-0.875f,
		
			//Triangle6
			0.715f,-0.875f,
			0.725f,-0.845f,
			0.68f,-0.87f,
			
			//Triangle7
			0.67f,-0.9f,
			0.68f,-0.87f,
			0.715f,-0.875f,
			
			//LETTER I
			//Triangle1
			0.49f,-0.875f,
			0.53f,-0.875f,
			0.49f,-0.94f,
			
			//Triangle2
			0.53f,-0.875f,
			0.49f,-0.94f,
			0.53f,-0.94f,
			
			//Triangle3
			0.46f,-0.935f,
			0.56f,-0.97f,
			0.46f,-0.97f,
			
			//Triangle4
			0.46f,-0.935f,
			0.56f,-0.935f,
			0.56f,-0.97f,
			
			//Triangle5
			0.46f,-0.875f,
			0.56f,-0.875f,
			0.56f,-0.84f,
			
			//Triangle6
			0.46f,-0.875f,
			0.46f,-0.84f,
			0.56f,-0.84f,
			
			//LETTER U
			//Triangle1
			0.8f,-0.935f,
			0.85f,-0.97f,
			0.85f,-0.935f,
			
			//Triangle2
			0.8f,-0.935f,
			0.85f,-0.84f,
			0.85f,-0.935f,
			
			//Triangle3
			0.8f,-0.84f,
			0.85f,-0.84f,
			0.8f,-0.935f,
			
			//Triangle4
			0.85f,-0.97f,
			0.85f,-0.935f,
			0.89f,-0.935f,
			
			//Triangle5
			0.89f,-0.935f,
			0.85f,-0.97f,
			0.89f,-0.97f,
			
			//Triangle
			0.89f,-0.935f,
			0.89f,-0.97f,
			0.94f,-0.935f,
			
			//Triangle
			0.89f,-0.935f,
			0.94f,-0.935f,
			0.89f,-0.84f,
			
			//Triangle
			0.89f,-0.84f,
			0.94f,-0.84f,
			0.94f,-0.935f,
		};
		vertexData=ByteBuffer
	    .allocateDirect(tableVerticesWithTriangles.length*BYTES_PER_FLOAT)
	    .order(ByteOrder.nativeOrder())
	    .asFloatBuffer();
	    vertexData.put(tableVerticesWithTriangles);
	}
		
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Set the background clear color to red. The first component is
        // red, the second is green, the third is blue, and the last
        // component is alpha, which we don't use in this lesson.
        glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
		
		String vertexShaderSource= TextResourceReader
		.readTextFileFromResource(context,R.raw.simple_vertex_shader);
		
		String fragmentShaderSource = TextResourceReader
		.readTextFileFromResource(context,R.raw.simple_fragment_shader);
		
		int vertexShader=ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader=ShaderHelper.compileFragmentShader(fragmentShaderSource);
		
		program=ShaderHelper.linkProgram(vertexShader,fragmentShader);
		
		if(LoggerConfig.ON)
		{
			ShaderHelper.validateProgram(program);
		}
		
		glUseProgram(program);
		
		uColorLocation=glGetUniformLocation(program,U_COLOR);
		
		aPositionLocation=glGetAttribLocation(program,A_POSITION);
		
		vertexData.position(0);
		
		glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,0,vertexData);
		
		glEnableVertexAttribArray(aPositionLocation);
		
    }

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one createdk.
     * 
     * @param width
     *            The new width, in pixels.
     * @param height
     *            The new height, in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    
	@Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
		
        glClear(GL_COLOR_BUFFER_BIT);
		
		glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,0,6);
		
		glUniform4f(uColorLocation,1.0f,1.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,6,6);
		
		//Draw the first mallet blue.
		glUniform4f(uColorLocation,1.0f,1.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,12,6);
		
		//Draw the second mallet red.
		glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,18,6);
		
		//heart
		glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,24,42);
		
		glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,66,6);

		glUniform4f(uColorLocation,0.2f,1.0f,1.0f,0.2f);
		glDrawArrays(GL_TRIANGLES,72,12);

		glUniform4f(uColorLocation,1.0f,0.3f,0.4f,0.2f);
		glDrawArrays(GL_TRIANGLES,84,12);

		glUniform4f(uColorLocation,1.0f,1.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,96,15);

		glUniform4f(uColorLocation,1.0f,0.3f,0.2f,1.0f);
		glDrawArrays(GL_TRIANGLES,111,12);

		glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,123,12);

		glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,135,24);

		glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,159,18);

		glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,177,27);

		//Extra
		glUniform4f(uColorLocation, 2.2f, 5.3f, 0.5f, 7.6f);
		glDrawArrays(GL_TRIANGLES, 204, 30);

		//Letter C
		glUniform4f(uColorLocation, 2.2f, 5.3f, 0.5f, 7.6f);
		glDrawArrays(GL_TRIANGLES, 234, 24);

		//Letter A
		glUniform4f(uColorLocation, 2.2f, 5.3f, 0.5f, 7.6f);
		glDrawArrays(GL_TRIANGLES, 258, 30);

		//Letter T
		glUniform4f(uColorLocation, 2.2f, 5.3f, 0.5f, 7.6f);
		glDrawArrays(GL_TRIANGLES, 288, 12);

		//Letter H
		glUniform4f(uColorLocation, 2.2f, 5.3f, 0.5f, 7.6f);
		glDrawArrays(GL_TRIANGLES, 300, 18);

		//underline CATH
		glUniform4f(uColorLocation, 0.2f, 1.0f, 1.0f, 0.2f);
		glDrawArrays(GL_TRIANGLES, 318, 15);

		glUniform4f(uColorLocation, 2.2f, 0.3f, 0.5f, 0.4f);
		glDrawArrays(GL_TRIANGLES, 333, 15);

		//HEARTbesideCATH
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_TRIANGLES, 348, 42);

		//HEARTbesideCATH
		glUniform4f(uColorLocation, 7.2f, 1.3f, 0.8f, .2f);
		glDrawArrays(GL_TRIANGLES, 390, 21);

		glUniform4f(uColorLocation, 7.2f, 1.3f, 0.8f, .2f);
		glDrawArrays(GL_TRIANGLES, 411, 21);

		//I<3U
		//heart
		glUniform4f(uColorLocation, 2.2f, 0.3f, 9.9f, 0.9f);
		glDrawArrays(GL_TRIANGLES, 432, 21);

		glUniform4f(uColorLocation, 2.2f, 0.3f, 9.9f, 0.9f);
		glDrawArrays(GL_TRIANGLES, 453, 21);

		//LetterI
		glUniform4f(uColorLocation, 2.2f, 0.3f, 9.9f, 0.9f);
		glDrawArrays(GL_TRIANGLES, 474, 18);

		//LetterU
		glUniform4f(uColorLocation, 2.2f, 0.3f, 9.9f, 0.9f);
		glDrawArrays(GL_TRIANGLES, 492, 24);
		
    }
}

