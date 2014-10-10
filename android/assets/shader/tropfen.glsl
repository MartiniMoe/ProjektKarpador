uniform vec2 resolution;
uniform float time;

void main(void){
	vec2 uv=gl_FragCoord.xy/resolution;
	uv-=.5;
	uv.x*=resolution.x/resolution.y;

	uv*=10.;
	
	uv.y+=time*5.;
	uv-=.25+2.*floor(uv/2.)+1.25*vec2(.5+.5*sin(floor(uv.y/2.)*5407.109840914+845.04285),.5+.5*sin(floor(uv.x/2.)*324.941380708+5987.148957));

	//uv=mod(uv,vec2(2.))-.5;
	//uv.y+=time*2.;
	
	float thinness=smoothstep(0.,.7,max(uv.y,-.1)+.1);
	//float thinness=max(uv.y,0.);

	float col=smoothstep(0.,.05,max(length(uv*vec2(pow(1.+thinness,1.25),1./pow(1.+thinness,1.0125)))-.2,0.));
	//float col=smoothstep(0.,.05,max(length(uv*vec2(pow(1.+thinness,3.),1.))-.2,0.));

	//if(abs(uv.y+.05)<.01)
	//	col=0.;

	gl_FragColor=vec4(mix(vec3(0,0,1),vec3(1),col),1);
}
