#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float time;
uniform vec2 resolution;
const int num=2;
uniform vec2 cloud_pos[num];
uniform vec2 cloud_ratio[num];

float intersect_circle(vec2 ray_origin, vec2 ray_dir, vec2 ellp_pos, vec2 ellp_rad){
	ray_origin*=ellp_rad;
	ray_dir*=ellp_rad;
	ellp_pos*=ellp_rad;

	ray_dir=normalize(ray_dir);

	vec2 ray_nor=ray_dir.yx*vec2(1.,-1.);
	float ray_shift=dot(ray_nor, ray_origin);

	float ellp_dis=abs(dot(ray_nor, ellp_pos)-ray_shift);
	
	float opacity=smoothstep(0.,.25,max(1.-ellp_dis,0.));

	if(opacity==0.){
		return 0.;
	}

	float ellp_shift=dot(ray_dir, ellp_pos);

	if(dot(ray_dir, ray_origin)<ellp_shift){
		float ray_dis=length(ray_origin-ellp_pos);
		return smoothstep(0.,.25,max(1.-ray_dis,0.));
	}
	else{
		return opacity;
	}
}

void main(){
	/*
	vec2 xy = gl_FragCoord.xy/resolution;
	xy.y*=-1.;
	xy.x*=resolution.x/resolution.y;
	*/
	
	vec2 uv = gl_FragCoord.xy/resolution;
	uv-=.5;
	uv.x*=resolution.x/resolution.y;
	/*
	vec2 uv = -v_texCoords *.25;
	uv.x*=aspect;
	*/
	/*
	vec2 cloud_pos[num];
	vec2 cloud_ratio[num];

	cloud_pos[0]=vec2(time*.05-.1,.1);
	cloud_ratio[0]=vec2(5,15);
	cloud_pos[1]=vec2(time*.1-1.,0);
	cloud_ratio[1]=vec2(10,20);
	*/

	vec2 sun_pos=vec2(-.3,.6);
	//vec2 sun_dir=vec2(.3,-1);
	
	vec3 col=vec3(1);
	
	float sun_ray=0.;

	sun_ray=(1./pow(1.+abs(length(uv-sun_pos)-.25),1.5));
	//sun_ray=(1./pow(2.-uv.y,1.5));

	col=mix(col,vec3(1,1,.5),sun_ray);
	
	float shadow=1.;

	for(int i=0;i<num;i++){
		shadow*=1.-intersect_circle(uv, uv-sun_pos, cloud_pos[i], cloud_ratio[i]);
		//sun_ray*=1.-intersect_circle(uv, sun_dir, cloud_pos[i], cloud_ratio[i]);
	}
	
	//col=mix(col,vec3(.45),1.-shadow);
	//col*=min(shadow+.75,1.);
	shadow=.3*(1.-shadow);
	col*=1.-shadow;
	//vec4 col=vec4(shadow);
	
	float sun=smoothstep(0.,.1,max(.3-length(uv-sun_pos),0.));
	col=mix(col,vec3(1,1,0),sun);
	/*
	for(int i=1;i<num;i++){
		float cloud=smoothstep(0.,.25,max(1.-length(cloud_ratio[i]*(uv-cloud_pos[i])),0.));
		col=mix(col,vec3(.5),cloud);
	}
	*/

	//vec2 xy=uv*vec2(1,-.7);
	//vec2 xy=uv;


	//xy-=cloud_pos[0]+vec2(-.5,.3);
	//xy-=cloud_pos[0];
	//xy*=vec2(1,-.7);
	//xy-=cloud_pos[1]+vec2(-.5,.5);
	//xy.y*=-1.;
	//xy+=vec2(.5,-.45);
	/*
	if(max(5.*abs(xy.x-.5),15.*abs(xy.y+.5))<1.){
		vec4 cl_tex=texture2D(iChannel1, vec2(2.5,7.5)*(xy+vec2(.25/2.5,.25/7.5)));
		col=mix(col, cl_tex.rgb, cl_tex.a);
	}
	*/
	//col*=cl_tex.a;

	gl_FragColor=vec4(col,1);

}
