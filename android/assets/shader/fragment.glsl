#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
//uniform mat4 u_projTrans;
uniform float time;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb;
        float gray = (color.r + color.g + color.b) / 3.0;
        vec3 grayscale = vec3(gray);
		vec2 uv=v_texCoords;
		
		float phase=abs(mod(time*.1,.2)-.1)-.05;
		
		uv.y+=.125*sin(time)*1;
		uv.x+=.5*sin(time);
		
		float col=10.*max(uv.y+phase*sin(uv.x*10.),0.);
		
        gl_FragColor = vec4(grayscale, 1.0)*col;
		
		
}