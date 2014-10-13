#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float time;

void main(){
	//vec2 uv=gl_FragCoord.xy/resolution;
	vec2 uv=vec2(1)-v_texCoords;
	//uv-=.5;
	//uv.x/=resolution.y/resolution.x;
	
	float phase=abs(mod(time*.1,.2)-.1)-.05;
	
	uv.y+=-.25+.125*sin(time);
	uv.x+=.5*sin(time);
	
	float col=10.*max(uv.y+phase*sin(uv.x*10.),0.);
	
	gl_FragColor=vec4(mix(vec4(0,.5,1,0.75),vec4(0),col));
	
}
