package snakegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Sprite {

	private Image image;
	private ImageView theImageView;
	private double positionX;
	private double positionY;
	private double bgPositionX;
	private double bgPositionY;
	private double velocityX;
	private double velocityY;
	private double bgVelocityX;
	private double bgVelocityY;
	private double width;
	private double height;
	private double angle;
	ImageView head;


	public Sprite() {
		positionX = 0.0;
		positionY = 0.0;
		velocityX = 0.0;
		velocityY = 0.0;
		angle = 0.0;
	    head  = new ImageView(new Image("snake_head_red.png"));
		// head.setSmooth(true);
	}
        
        public double getPosX(){
            return positionX;
        }
        
        public double getPosY(){
            return positionY;
        }
        
	public void setImage(Image i) {
		image = i;
		width = i.getWidth();
		height = i.getHeight();
	}

	public Image getImage() {
		return image;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double newAngle, GraphicsContext gc) {
		angle = newAngle;

		// Doesn't work
		// theImageView.setImage(image);
		// theImageView.setRotate(newAngle);
		
		// Doesn't work
		// Rotate rotate = new Rotate();
		// rotate.setAngle(newAngle);
		// rotate.setPivotX(0.0);
		// rotate.setPivotY(0.0);
		// rotate.setPivotZ(0.0);
		// head.getTransforms().addAll(rotate);

		// Doesn't work
		// head.getTransforms().add(new Rotate(newAngle, 0, 0));

		// Doesn't work
		// head.setRotate(newAngle);
		// gc.drawImage(image, positionX, positionY);
		
		// Rotate the head to the new angle
		// head.setRotate(newAngle);
		// gc.drawImage(head.getImage(), WindowWidth/2, WindowHeight/2);

		// Doesn't work
		// Translate t = new Translate();
		// Translate p = new Translate();
		// Translate ip = new Translate();
		// Rotate rx = new Rotate();
		// {
		//     rx.setAxis(Rotate.X_AXIS);
		// }
		// Rotate ry = new Rotate();
		// {
		//     ry.setAxis(Rotate.Y_AXIS);
		// }
		// Rotate rz = new Rotate();
		// {
		//     rz.setAxis(Rotate.Z_AXIS);
		// }
		// Scale s = new Scale();
		// rx.setAngle(x);
		// ry.setAngle(y);
		// rz.setAngle(90);
        // Rotate rotate = new Rotate();
		// rotate.setAngle(newAngle-90);
		// rotate.setPivotX(0.0);
		// rotate.setPivotY(0.0);
		// rotate.setPivotZ(0.0);
		// head.getTransforms().addAll(rotate);
    	// head.getTransforms().addAll(rx, ry, rz, t, p, ip, s);
		// head.rotateProperty() = newAngle;
		// head.getTransforms().add(new Rotate(newAngle, 0, 0));
		// head.getTransforms().add(new Rotate(newAngle, 32, 32, 0));
		// ((head.getImage().getWidth()/2)), ((head.getImage().getHeight()/2))));
		// head.getTransformas().add(new Affine());
        // System.out.println(head.getTransforms());
		// System.out.println(t);
		// System.exit(0);
		// head.getTransforms().set(angle, new Rotate(newAngle));

		// head.getTransforms().add(new Rotate(Math.toRadians(newAngle), 0.0, 0.0, 0.0));
		// Only thing that works and it has a problem -- talk to AScott
        // Rotate the head to the new angle



		// head.setRotate(newAngle);
		// head.setPreserveRatio(true);
		// SnapshotParameters params = new SnapshotParameters();
		// params.setFill(Color.TRANSPARENT);
		// Image rotatedImage = head.snapshot(params, null);
		// setImage(rotatedImage);
		// render(gc);
	




	}

	public void setImage(String filename) {
		Image i = new Image(filename);

		setImage(i);
	}

	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}

	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}

	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	public void update(double time, Scene scene) {
		positionX += velocityX * time;
		positionY += velocityY * time;
	}

	public void render(GraphicsContext gc) {
		// gc.drawImage(image, positionX, positionY);
		drawRotatedImage(gc, image, angle, positionX, positionY);
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

	public String toString() {
		return " Position: [" + positionX + "," + positionY + "]"
			+ " Velocity: [" + velocityX + "," + velocityY + "]";
	}
	// Following two functions borrowed from:
	// http://stackoverflow.com/questions/18260421/how-to-draw-image-rotated-on-javafx-canvas
	/**
	 * Sets the transform for the GraphicsContext to rotate around a pivot
	 * point.
	 *
	 * @param gc the graphics context the transform to applied to.
	 * @param angle the angle of rotation.
	 * @param px the x pivot co-ordinate for the rotation (in canvas
	 * co-ordinates).
	 * @param py the y pivot co-ordinate for the rotation (in canvas
	 * co-ordinates).
	 */
	private void rotate(GraphicsContext gc, double angle, double px, double py) {
		Rotate r = new Rotate(angle, px, py);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	/**
	 * Draws an image on a graphics context.
	 *
	 * The image is drawn at (tlpx, tlpy) rotated by angle pivoted around the
	 * point: (tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2)
	 *
	 * @param gc the graphics context the image is to be drawn on.
	 * @param angle the angle of rotation.
	 * @param tlpx the top left x co-ordinate where the image will be plotted
	 * (in canvas co-ordinates).
	 * @param tlpy the top left y co-ordinate where the image will be plotted
	 * (in canvas co-ordinates).
	 */
	private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
		gc.save(); // saves the current state on stack, including the current transform
		rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
		gc.drawImage(image, tlpx, tlpy);
		gc.restore(); // back to original state (before rotation)
	}

}
