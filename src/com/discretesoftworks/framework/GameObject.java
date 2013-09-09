package com.discretesoftworks.framework;


// Game object class
// All visible objects in the game should extend this class
public abstract class GameObject extends GriddedObject{

	private float life;
	private boolean solid;
	private boolean depthChanged;
	
	private RenderModel myModel;
	
	private Sprite sprite;
	
	private boolean init;
	
	public GameObject(float x, float y, float z, float width, float height, Sprite sprite){
		super(x,y,z,width,height);
		this.life = 1;
		this.sprite = sprite;
		depthChanged = false;
		init = false;
		myModel = null;
		solid = false;
		init();
		addSelf();
	}
	
	public void init(){
		myModel = GameRenderer.s_instance.getNewModel(getX(),getY(),sprite);
		setDimensions(getWidth(),getHeight());
		float w = getWidth()/2f;
		float h = getHeight()/2f;
		float squareCoords[] = {-w,  h, 0f,
				 				-w, -h, 0f,
				 				 w, -h, 0f,
				 				 w,  h, 0f } ;
		getModel().remake3DModel(squareCoords);
		sprite.setMask(getWidth(),getHeight());
		init = true;
	}
	
	@Override
	public void setDepth(int depth){
		if (depth != getDepth())
			depthChanged = true;
		super.setDepth(depth);
	}
	
	public boolean getDepthChanged(){
		if (depthChanged){
			depthChanged = false;
			return true;
		}
		return false;
	}
	
	public void addSelf(){
		GameRenderer.s_instance.add(this);
	}
	
	public void setHudElement(boolean h){
		myModel.setHudElement(h);
	}
	
	public GameObject clear(){
		init = false;
		GameRenderer.s_instance.freeModel(myModel);
		return this;
	}
	
	public void setSolid(boolean solid){
		this.solid = solid;
		if (solid)
			GameRenderer.s_instance.addSolid(this);
		else
			GameRenderer.s_instance.removeSolid(this);
	}
	
	public boolean getSolid(){
		return solid;
	}
	
	public void setLife(float life){
		this.life = life;
	}
	
	public void changeLife(float dLife){
		this.life += dLife;
	}
	
	public int getLife(){
		return (int)life;
	}
	
	public void setVisible(boolean v){
		myModel.setVisible(v);
	}
	
	public void setColor(float r, float g, float b, float a){
		myModel.setColor(r,g,b,a);
	}
	
	public RenderModel getModel(){
		return myModel;
	}
	
	public void updateModel(){
		myModel.setX(getX());
		myModel.setY(getY());
		myModel.setZ(getZ());
		myModel.setWidth(getWidth());
		myModel.setHeight(getHeight());
	}
	
	public boolean placeFree(float x, float y){
		float dx = x-getX();
		float dy = y-getY();
		changeX(dx);
		changeY(dy);
		
		boolean noCollide = true;
		
		if (Directional.checkAllCollisions(this, GameRenderer.s_instance.solidObjects) != null)
			noCollide = false;
		
		changeX(-dx);
		changeY(-dy);
		return noCollide;
	}
	
	//Recommended override and super call
	public void draw(float[] vpMatrix, float centerX, float centerY, float centerZ){
		if (init)
			myModel.draw(vpMatrix, centerX, centerY, centerZ);
		else
			init();
	}
	
	// Override and super call
	public void update(float deltaTime){
		if (init)
			updateModel();
		else
			init();
	}
	
}
