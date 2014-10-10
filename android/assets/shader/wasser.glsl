#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float time;
uniform vec2 resolution;
uniform float turbulence;

void main(){
	//vec2 uv=gl_FragCoord.xy/resolution;
	vec2 uv=vec2(0.5)-v_texCoords;
	//uv.x*aspect;
	//uv-=.5;
	//uv.x/=resolution.y/resolution.x;
	
	float phase=abs(mod(time*.1,.2)-.1)-.05;
	
	uv.y+=.125*sin(time)*turbulence;
	uv.x+=.5*sin(time);
	
	float col=10.*max(uv.y+phase*sin(uv.x*10.),0.);
	
	gl_FragColor=vec4(mix(vec4(0,1,0,1),vec4(1),length(uv));
	
}
