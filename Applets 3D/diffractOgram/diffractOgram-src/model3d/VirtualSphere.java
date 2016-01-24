package model3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;

import diffrac.DefaultValues;

public class VirtualSphere extends BranchGroup implements ColorConstants {
	TransformGroup sPositioned;
	public Point3d center;
	public double radius;
	public DefaultValues defaultValues;
	
	public VirtualSphere(DefaultValues defaultValues, double lambda) {
		this.defaultValues = defaultValues;
		center = new Point3d();  
		sPositioned = new TransformGroup();
		sPositioned.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		setLambda(lambda);
		Sphere s = new Sphere(1f, Sphere.GENERATE_NORMALS, 100, app(new Color(1f, .7f, .7f, .5f)));
		sPositioned.addChild(s);
		
		createLegend();
		createRadius();
		createRepere();
		
		addChild(sPositioned);
	}

	
	public static Appearance app(Color c)
	{
		Appearance app=new Appearance();
		
		BufferedImage i = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics g = i.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, 64, 64);
		ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGBA, i, false, false);
		
		Texture2D texture=new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,image.getWidth(),image.getHeight());
		texture.setImage(0, image);
		texture.setEnable(true);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);

		app.setTexture(texture);
		app.setTextureAttributes(new TextureAttributes());

	  app.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST,0.8f));		
		
	  PolygonAttributes pa = new PolygonAttributes();
	  pa.setCullFace(PolygonAttributes.CULL_NONE);
	  pa.setBackFaceNormalFlip(true);
	  app.setPolygonAttributes(pa);
		
		return app;
	}
	
	
	private void createLegend() {
		Appearance app=new Appearance();
		app.setMaterial(new Material(magenta, black, magenta, white, 128));
	  app.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f));
		Group l = Utils3d.createFixedLegend("Ewald sphere", new Point3d(0, -.9, 0), .05f, app, true);
		sPositioned.addChild(l);
	}
	private void createRadius() {
		Transform3D t3v1 = new Transform3D();
		Transform3D t3v2 = new Transform3D();
		t3v1.rotZ(3*Math.PI/4);
		t3v2.rotY(-Math.PI/4);
		t3v1.mul(t3v2);
		TransformGroup tgv = new TransformGroup(t3v1);
		tgv.addChild(Utils3d.createNamedVector("1/lambda", new Point3d(0, 0, 0), new Point3d(-.98, 0, 0), new Point3d(-.5, 0, .02), .2f, magenta, magenta));
		sPositioned.addChild(tgv);
	}
	private void createRepere() {
		Transform3D t3dRepere = new Transform3D();
		t3dRepere.set(.3);
		TransformGroup tgRepere = new TransformGroup(t3dRepere);
		tgRepere.addChild(Utils3d.createRepere(cyan, green, green, new String[] {"x", "y", "z"}, .15f, .03f, 0, 0, new Vector3d(1, 0, 0), new Vector3d(0, 1, 0), new Vector3d(0, 0, 1)));
		sPositioned.addChild(tgRepere);
	}
	
	
	public double lambdaToRadius(double l) {
		return defaultValues.scale/l;
	}
	
	public void setLambda(double lambda) {
		center.set(0, -lambdaToRadius(lambda), 0);
		Transform3D t = new Transform3D();
		radius = lambdaToRadius(lambda); 
		t.set(radius, new Vector3d(center));
		sPositioned.setTransform(t);
	}
}


