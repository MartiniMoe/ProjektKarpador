#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float time;

void main(){
	vec2 uv=vec2(1)-v_texCoords;
	float phase=abs(mod(time*.1,.2)-.1)-.05;
	
	uv.y+=-.5;
	uv+=vec2(.5,.125)*sin(time);
	
	float wave=uv.y+phase*sin(uv.x*10.);
	vec4 col=mix(vec4(0,0,1,.5),vec4(0),step(0.,wave));
	col=mix(vec4(1),col,smoothstep(0.,.015,abs(wave)));
	
	gl_FragColor=col;
}
