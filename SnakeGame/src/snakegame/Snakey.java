package snakegame;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class Snakey extends Application {

    int nextGrow = 1;
    ArrayList<Snake> snakes = new ArrayList<Snake>();
    
    int aiSnakeCount = 10; // Need to fetch this from menu
    String[] snakeHeads =  {"head_yellow.png", "snake_head_red.png", "snake_head_green.png"};
    String[] snakeBodies = {"snake_body_yellow.png", "snake_body_red.png", "snake_body_green.png"};
    int colorCount = 0;
    ArrayList<SnakeAI> SAIs = new ArrayList<SnakeAI>();
    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Menu Scene: Complete
        //Menu Layout: Complete
                Pane snakeMenu = (Pane) FXMLLoader.load(getClass().getResource("SnakeMenuLayout.fxml"));
                primaryStage.setScene(new Scene(snakeMenu));
                primaryStage.show(); 
    }

  
    public void beginGame(Stage primaryStage) {

        //Handles the speed and position of the window in relation to the game grid
        Background bg = new Background();
        //the root of the Window
        Group root = new Group();
        //The speed of the player Character Snake
        int Speed = 500;
        
        int WindowWidth = 640;
        int WindowHeight = 640;
        int GameGridWidth = 8192;
        int GameGridHeight = 8192;

        //The window view
        Scene theScene = new Scene(root, WindowWidth, WindowHeight);
        primaryStage.setTitle("Snakey!");
        primaryStage.setScene(theScene);
        //Drawable area inside of the scene
        Canvas canvas = new Canvas(WindowWidth, WindowHeight);

        //In x, y information from the mouse input is going to be stored here.
        ArrayList<Double> mouseInput = new ArrayList();

        //The image for the borders of the game
        Image cracked = new Image("stars5.jpg");
        //sets the thickness of the boarders
        theScene.setFill(new ImagePattern(cracked,
                ((theScene.getWidth() / 2) - 32),
                ((theScene.getHeight() / 2) - 32), .6, .6, true));
        root.getChildren().add(canvas);

        //it takes the information from the mouse and stores it into the ArrayList mouseInput
        //with the information from x first.
        theScene.setOnMouseMoved(
            new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                Double x = e.getX();
                Double y = e.getY();
                mouseInput.add(0, x);
                mouseInput.add(1, y);
            }
        });

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        
        // Primary Snake
        Snake playerSnake = new Snake();
        Sprite snakeHead = new Sprite();
        snakeHead.setImage("snake_head_purple.png");
        playerSnake.setBodyColor("snake_body_purple.png");
        snakeHead.setPosition((theScene.getWidth() / 2) - 32, (theScene.getHeight() / 2) - 32);
        playerSnake.setHead(snakeHead);
        snakes.add(playerSnake);

        
        for(int i=0; i<aiSnakeCount; i++) {
            Snake ai = new Snake();
            snakeHead = new Sprite();
            snakeHead.setImage(snakeHeads[colorCount]);
            ai.setBodyColor(snakeBodies[colorCount]);
            colorCount = (colorCount + 1) % snakeHeads.length;
            snakeHead.setPosition(200, 200);
            ai.setHead(snakeHead);
            SnakeAI SAI = new SnakeAI(ai, true);
            SAI.setHead(snakeHead);
            SAIs.add(SAI);
            snakes.add(ai);
        }
        
//        // AI Snake
//        //TODO: Make this a snake object and make the appropriate modifications to the code
//        Snake theSnake1 = new Snake();
//        snakeHead = new Sprite();
//        snakeHead.setImage("snake_head_red.png");
//        snakeHead.setPosition(200, 200);
//        theSnake1.setHead(snakeHead);
//        SnakeAI SAI1 = new SnakeAI(theSnake1, true);
//        SAI1.setHead(snakeHead);
//        snakes.add(theSnake1);
//
//        // AI Snake
//        //TODO: Make this a snake object and make the appropriate modifications to the code
//        Snake theSnake2 = new Snake();
//        snakeHead = new Sprite();
//        snakeHead.setImage("snake_head_green.png");
//        snakeHead.setPosition(200, 200);
//        theSnake2.setHead(snakeHead);
//        SnakeAI SAI2 = new SnakeAI(theSnake2, true);
//        SAI2.setHead(snakeHead);
//        snakes.add(theSnake2);
//
//        
//        // AI Snake
//        //TODO: Make this a snake object and make the appropriate modifications to the code
//        Snake theSnake3 = new Snake();
//        snakeHead = new Sprite();
//        snakeHead.setImage("head_yellow.png");
//        snakeHead.setPosition(200, 200);
//        theSnake3.setHead(snakeHead);
//        SnakeAI SAI3 = new SnakeAI(theSnake3, true);
//        SAI3.setHead(snakeHead);
//        snakes.add(theSnake3);
        
        //Set the initial velocity of the background.
        bg.setBGVelX(0);
        bg.setBGVelY(Speed);
        
        Apple apples = new Apple();
        Apple apples1 = new Apple();
        Apple apples2 = new Apple();
        Apple apples3 = new Apple();
        
        //Places the apples at the start of the game on the gameGrid
        //TODO: Make the apples respawn after they are eaten
        ArrayList<Sprite> appleList = new ArrayList<Sprite>();
        for (int i = 0; i < 50; i++) 
        {
            Sprite apple = new Sprite();
            apple.setImage("apple.png");
            double px = -GameGridWidth/2 * Math.random() + 50;
            double py = GameGridWidth/2 * Math.random() + 50;
            apple.setPosition(px, py);
            appleList.add(apple);

            apple = new Sprite();
            apple.setImage("apple.png");
            px = GameGridWidth/2 * Math.random() + 50;
            py = -GameGridWidth/2 * Math.random() + 50;
            apple.setPosition(px, py);
            appleList.add(apple);

            apple = new Sprite();
            apple.setImage("apple.png");
            px = -GameGridWidth/2 * Math.random() + 50;
            py = -GameGridWidth/2 * Math.random() + 50;
            apple.setPosition(px, py);
            appleList.add(apple);

            apple = new Sprite();
            apple.setImage("apple.png");
            px = GameGridWidth/2 * Math.random() + 50;
            py = GameGridWidth/2 * Math.random() + 50;
            apple.setPosition(px, py);
            appleList.add(apple);
        }

        // wall sprite
        ArrayList<Sprite> wallList = new ArrayList<Sprite>();
        for (int i = 0; i <= GameGridWidth + 400; i += 144) {
            // places the wall at the top
            Sprite wall = new Sprite();
            wall.setImage("cracked.png");
            wall.setPosition(i - (GameGridWidth / 2), ((WindowHeight / 2) - (GameGridWidth / 2)));
            wallList.add(wall);
            // places the wall at the bottom
            wall = new Sprite();
            wall.setImage("cracked.png");
            wall.setPosition(i - (GameGridWidth / 2), ((WindowHeight / 2) + (GameGridHeight / 2)));
            wallList.add(wall);
            // places the wall to the left
            wall = new Sprite();
            wall.setImage("cracked.png");
            wall.setPosition(((WindowHeight / 2) - (GameGridWidth / 2)), i - (GameGridWidth / 2));
            wallList.add(wall);
            // places the wall to the right
            wall = new Sprite();
            wall.setImage("cracked.png");
            wall.setPosition(((WindowWidth / 2) + (GameGridWidth / 2)), i - (GameGridWidth / 2));
            wallList.add(wall);

        }

        //The time of the last update
        LongValue lastNanoTime = new LongValue(System.nanoTime());
        //The score of the player
//        IntValue score = new IntValue(0);
//        //The score of the AI
//        IntValue aiScore1 = new IntValue(0);
//        IntValue aiScore2 = new IntValue(0);
//        IntValue aiScore3 = new IntValue(0);
        
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                double newAngle;
                // game logic
                if (!mouseInput.isEmpty()) {
                    double x;
                    double y;
                    double totX = 0.0;
                    double totY = 0.0;
                    // buffer up to 20 samples from the mouse
                    // so we can normalize input by averaging
                    // otherwise there is lots of jitter
                    if (mouseInput.size() > 20) {
                        for (int i = 0; i < 19; i += 2) {
                            totX += mouseInput.get(i);
                            totY += mouseInput.get(i + 1);
                        }
                        x = totX / 10.0;
                        y = totY / 10.0;

                        // Calculate angle and update
                        x = (x - theScene.getWidth() / 2.0);
                        y = (y - theScene.getHeight() / 2.0);

                        newAngle = (Math.toDegrees(Math.atan2(y, x)) + 90.0);
                        playerSnake.getHead().setAngle(newAngle);
                        if (newAngle % 90 != 0.0) {
                            // adjust velocities based on angle
                            double acute = ((playerSnake.getHead().getAngle() + 90.0) % 90);
                            
                            double xSpeed = Speed * acute / 90.0;
                            double ySpeed = Speed - xSpeed;

                            if (newAngle > -90.0 && newAngle < 0.0) // upper left quarter
                            {
                                bg.setBGVelX(ySpeed);
                                bg.setBGVelY(xSpeed);
                            } else if (newAngle > 0.0 && newAngle <= 90.0) // upp right corner
                            {
                                bg.setBGVelX(-xSpeed);
                                bg.setBGVelY(ySpeed);
                            } else if (newAngle > 90.0 && newAngle <= 180.0) // lower right corner
                            {
                                bg.setBGVelX(-ySpeed);
                                bg.setBGVelY(-xSpeed);
                            } else if (newAngle > 180.0 && newAngle <= 270.0) // lower left corner
                            {
                                bg.setBGVelX(xSpeed);
                                bg.setBGVelY(-ySpeed);
                            }
                            //Set the velocity and angle of the head
                            //This doesn't ever get updated but is need to calculate the changes in the body pieces
                            playerSnake.getHead().setVelocity(-bg.getBGVelX(), -bg.getBGVelY());
                            playerSnake.getHead().setAngle(newAngle);
                        }
                    }
                }

                // TODO: Add the death logic
                
                // Detecting collisions with player snake
                Iterator<Snake> snakesIter = snakes.iterator();
                Snake player = snakesIter.next(); // Player is first snake in `snakes` list
                while(snakesIter.hasNext()) {
                    Snake ai = snakesIter.next();
                    LinkedList<Sprite> aiBody = ai.getBody();
                    Iterator<Sprite> aiBodyIter = aiBody.iterator();
                    
                    LinkedList<Sprite> playerBody = player.getBody();
                    Iterator<Sprite> playerBodyIter = playerBody.iterator();
                    
                    // Player head collides with AI body
                    while(aiBodyIter.hasNext()) {
                        Sprite bodyPart = aiBodyIter.next();
                        if (player.getHead().intersects(bodyPart)){
                            player.setScore(0);
                            bg.setBGVelX(0);
                            bg.setBGVelY(0);
                            // Both AI snake and player die
                            // Go back to main menu
                            // Line below is just for testing. If game stops, no need to respawn AI snake
                            ai.getHead().setPosition(GameGridWidth/2 * Math.random() + 50, GameGridHeight/2 * Math.random() + 50);
                            System.out.println("Game should be over");
                        }
                    }
                    
                    // AI head collides with other snake body
                    for (Snake s : snakes) {
                        if (s == ai)
                            continue;
                        
                        LinkedList<Sprite> snakeBody = s.getBody();
                        Iterator<Sprite> snakeBodyIter = snakeBody.iterator();
                        
                        while(snakeBodyIter.hasNext()) {
                            Sprite bodyPart = snakeBodyIter.next();
                            if (ai.getHead().intersects(bodyPart)){
                                // AI player dies and respawns
                                ai.getHead().setPosition(GameGridWidth/2 * Math.random() + 50, GameGridHeight/2 * Math.random() + 50);
                            }    
                        }
                    }
                }   
                
                Iterator<Sprite> appleIter = appleList.iterator();
                while (appleIter.hasNext()) {
                    Sprite apple = appleIter.next();
                    Iterator<SnakeAI> aiIter = SAIs.iterator();
                    for (Snake s : snakes) {
                        if (s == playerSnake) {
                            if (playerSnake.getHead().intersects(apple)) { 
                                    //appleIter.remove();
                                    apples.SpanwAppleInSameQ(apple.getPosX(), apple.getPosY());
                                    apple.setPosition(apples.getXposition(), apples.getYpostion());
                                    s.setScore(s.getScore() + 1);
                                    playerSnake.setGrowCount(playerSnake.getGrowCount() + 1);
                                    continue;
                            }
                        } else {
                            SnakeAI ai = aiIter.next();
                            if(s.getHead().intersects(apple))
                            {
                                apples1.SpanwAppleInSameQ(apple.getPosX(), apple.getPosY());
                                apple.setPosition(apples.getXposition(), apples.getYpostion());
                                s.setGrowCount(s.getGrowCount() + 1);
                                s.setScore(s.getScore() + 1);
                                ai.mem = false;
                                continue;
                            }
                        }
                    }
                    

//                    
//                    if(theSnake1.getHead().intersects(apple))
//                    {
//                        apples1.SpanwAppleInSameQ(apple.getPosX(), apple.getPosY());
//                        apple.setPosition(apples.getXposition(), apples.getYpostion());
//                        theSnake1.setGrowCount(theSnake1.getGrowCount() + 1);
//                        aiScore1.value++;
//                        SAI1.mem = false;
//                        continue;
//                    }
//                    if(theSnake2.getHead().intersects(apple))
//                    {
//                        apples2.SpanwAppleInSameQ(apple.getPosX(), apple.getPosY());
//                        apple.setPosition(apples.getXposition(), apples.getYpostion());
//                        theSnake2.setGrowCount(theSnake2.getGrowCount() + 1);
//                        aiScore2.value++;
//                        SAI2.mem = false;
//                        continue;
//                    }
//                    if(theSnake3.getHead().intersects(apple))
//                    {
//                        apples3.SpanwAppleInSameQ(apple.getPosX(), apple.getPosY());
//                        apple.setPosition(apples.getXposition(), apples.getYpostion());
//                        theSnake3.setGrowCount(theSnake3.getGrowCount() + 1);
//                        aiScore3.value++;
//                        SAI3.mem = false;
//                        continue;
//                    }
                    
                    for (Snake s : snakes) {
                        if (s.getGrowCount() >= nextGrow) {
                            Sprite bodySnake = new Sprite();
                            bodySnake.setImage(s.getBodyColor());
                            bodySnake.setPosition(playerSnake);
                            bodySnake.setVelocity(playerSnake);
                            s.addBody(bodySnake);
                            s.setGrowCount(0);
                        }
                    }
//                    //If the snake should grow a new piece, grow a new piece.
//                    if (playerSnake.getGrowCount() >= nextGrow) {
//                        Sprite bodySnake = new Sprite();
//                        bodySnake.setImage("snake_body_purple.png");
//                        bodySnake.setPosition(playerSnake);
//                        bodySnake.setVelocity(playerSnake);
//                        playerSnake.addBody(bodySnake);
//                        playerSnake.setGrowCount(0);
//                    }
//                    
//                    if(theSnake1.getGrowCount() >= nextGrow){
//                        Sprite bodySnake1 = new Sprite();
//                        bodySnake1.setImage("snake_body_red.png");
//                        bodySnake1.setPosition(theSnake1);
//                        bodySnake1.setVelocity(theSnake1);
//                        theSnake1.addBody(bodySnake1);
//                        theSnake1.setGrowCount(0);
//                    }
//
//                    if(theSnake2.getGrowCount() >= nextGrow){
//                        Sprite bodySnake2 = new Sprite();
//                        bodySnake2.setImage("snake_body_green.png");
//                        bodySnake2.setPosition(theSnake2);
//                        bodySnake2.setVelocity(theSnake2);
//                        theSnake2.addBody(bodySnake2);
//                        theSnake2.setGrowCount(0);
//                    }
//
//                    if(theSnake3.getGrowCount() >= nextGrow){
//                        Sprite bodySnake3 = new Sprite();
//                        bodySnake3.setImage("snake_body_yellow.png");
//                        bodySnake3.setPosition(theSnake3);
//                        bodySnake3.setVelocity(theSnake3);
//                        theSnake3.addBody(bodySnake3);
//                        theSnake3.setGrowCount(0);
//                    }

                    for (SnakeAI ai : SAIs) {
                        if (ai.mem == false) {
                            Sprite nextApple = apple;
                            if (ai.picksClosest) {
                                nextApple = ai.shortestApple(appleList);
                            }
                            ai.memAngle = ai.calAngle(nextApple);
                            ai.mem = true;
                        }
                    }
//                    //The AI snake picks the next closest apple to it if it eats an apple
//                    if (SAI1.mem == false) {
//                        Sprite nextApple = apple;
//                        if (SAI1.picksClosest) {
//                            nextApple = SAI1.shortestApple(appleList);
//                        }
//                        SAI1.memAngle = SAI1.calAngle(nextApple);
//                        SAI1.mem = true;
//                    }
//                    //The AI snake picks the next closest apple to it if it eats an apple
//                    if (SAI2.mem == false) {
//                        Sprite nextApple = apple;
//                        if (SAI2.picksClosest) {
//                            nextApple = SAI2.shortestApple(appleList);
//                        }
//                        SAI2.memAngle = SAI2.calAngle(nextApple);
//                        SAI2.mem = true;
//                    }
//                    //The AI snake picks the next closest apple to it if it eats an apple
//                    if (SAI3.mem == false) {
//                        Sprite nextApple = apple;
//                        if (SAI3.picksClosest) {
//                            nextApple = SAI3.shortestApple(appleList);
//                        }
//                        SAI3.memAngle = SAI3.calAngle(nextApple);
//                        SAI3.mem = true;
//                    }

                }

                //Checks to see if a snake has hit a wall.
                Iterator<Sprite> wallIter = wallList.iterator();
                while (wallIter.hasNext()) {
                    Sprite wall = wallIter.next();
                    if (playerSnake.getHead().intersects(wall)) {
                        //TODO: return to menu on death.
                        player.setScore(0);
                        bg.setBGVelX(0);
                        bg.setBGVelY(0);
                        //reset back to 0
                    }
                    for (Snake snake : snakes) {
                        if (snake == playerSnake)
                            continue;
                        if (snake.getHead().intersects(wall)){
                            snake.getHead().setPosition(GameGridWidth/2 * Math.random() + 50, GameGridHeight/2 * Math.random() + 50);
                            snake.dropTail();
                        }
                    }
                }
                
                
                Iterator<Snake> snakeIter = snakes.iterator();
                Snake p = snakeIter.next(); // Skip player snake
                Iterator<SnakeAI> ais = SAIs.iterator();
                
                while(snakeIter.hasNext() && ais.hasNext()) {
                    Snake s = snakeIter.next();
                    SnakeAI ai = ais.next();
                    
                    s.getHead().setAngle(ai.memAngle);
                    s.getHead().setVelocity(Math.cos(Math.toRadians((ai.memAngle - 90.0))) * Speed / 2, Math.sin(Math.toRadians((ai.memAngle - 90.0))) * Speed / 2);
                    s.getHead().setPosition(s.getHead().getPosX() + bg.getBGVelX() * elapsedTime, s.getHead().getPosY() + bg.getBGVelY() * elapsedTime);
                    s.getHead().update(elapsedTime);
                }
                // update the Snake2's position relative to the change
                // background velocity
//                theSnake1.getHead().setAngle(SAI1.memAngle);
//                theSnake1.getHead().setVelocity(Math.cos(Math.toRadians((SAI1.memAngle - 90.0))) * Speed / 2, Math.sin(Math.toRadians((SAI1.memAngle - 90.0))) * Speed / 2);
//                theSnake1.getHead().setPosition(theSnake1.getHead().getPosX() + bg.getBGVelX() * elapsedTime, theSnake1.getHead().getPosY() + bg.getBGVelY() * elapsedTime);
//                theSnake1.getHead().update(elapsedTime);
//
//                                // update the Snake2's position relative to the change
//                // background velocity
//                theSnake2.getHead().setAngle(SAI2.memAngle);
//                theSnake2.getHead().setVelocity(Math.cos(Math.toRadians((SAI2.memAngle - 90.0))) * Speed / 2, Math.sin(Math.toRadians((SAI2.memAngle - 90.0))) * Speed / 2);
//                theSnake2.getHead().setPosition(theSnake2.getHead().getPosX() + bg.getBGVelX() * elapsedTime, theSnake2.getHead().getPosY() + bg.getBGVelY() * elapsedTime);
//                theSnake2.getHead().update(elapsedTime);
//
//                                // update the Snake2's position relative to the change
//                // background velocity
//                theSnake3.getHead().setAngle(SAI3.memAngle);
//                theSnake3.getHead().setVelocity(Math.cos(Math.toRadians((SAI3.memAngle - 90.0))) * Speed / 2, Math.sin(Math.toRadians((SAI3.memAngle - 90.0))) * Speed / 2);
//                theSnake3.getHead().setPosition(theSnake3.getHead().getPosX() + bg.getBGVelX() * elapsedTime, theSnake3.getHead().getPosY() + bg.getBGVelY() * elapsedTime);
//                theSnake3.getHead().update(elapsedTime);

                // render whole board
                gc.clearRect(0, 0, WindowWidth, WindowHeight);

                // update background velocity
                bg.setBGX(bg.getBGX() + (bg.getBGVelX() * elapsedTime));
                bg.setBGY(bg.getBGY() + (bg.getBGVelY() * elapsedTime));

                // draw the walls
                theScene.setFill(new ImagePattern(cracked, bg.getBGX(), bg.getBGY(), cracked.getWidth(), cracked.getHeight(), false));

                //Takes each body piece of the snake, sets it's position to the position in front of it
                //then adjusts for the background velocity
                //Then renders the snake
                for (int i = playerSnake.getSize() - 1; i > 0; i--) {
                    playerSnake.getSegment(i).setPosition(playerSnake.getSegment(i - 1).getPosX() + (bg.bgVelX * elapsedTime),
                    playerSnake.getSegment(i - 1).getPosY() + (bg.bgVelY * elapsedTime));
                    playerSnake.getSegment(i).setAngle(playerSnake.getSegment(i - 1).getAngle());
                    playerSnake.getSegment(i).render(gc);
                }
                
                //Renders the head of the snake
                playerSnake.getHead().render(gc);

                for (Snake s : snakes) {
                    if (s == snakes.get(0)) {
                        playerSnake.getHead().render(gc);
                    } else {
                        //Renders the AI snake
                        System.out.println(s);
                        for (int i = s.getSize() - 1; i > 0; i--) {
                            s.getSegment(i).setPosition(
                                s.getSegment(i - 1).getPosX() + (bg.bgVelX * elapsedTime),
                                s.getSegment(i - 1).getPosY() + (bg.bgVelY * elapsedTime));
                            s.getSegment(i).setAngle(s.getSegment(i - 1).getAngle());
                            s.getSegment(i).render(gc);
                        }
                        s.getHead().render(gc);
                    }
                }
//                //Renders the AI snake
//                for (int i = theSnake1.getSize() - 1; i > 0; i--) {
//                    theSnake1.getSegment(i).setPosition(
//                        theSnake1.getSegment(i - 1).getPosX() + (bg.bgVelX * elapsedTime),
//                        theSnake1.getSegment(i - 1).getPosY() + (bg.bgVelY * elapsedTime));
//                    theSnake1.getSegment(i).setAngle(theSnake1.getSegment(i - 1).getAngle());
//                    theSnake1.getSegment(i).render(gc);
//                }
//                theSnake1.getHead().render(gc);
//
//                //Renders the AI snake
//                for (int i = theSnake2.getSize() - 1; i > 0; i--) {
//                    theSnake2.getSegment(i).setPosition(
//                        theSnake2.getSegment(i - 1).getPosX() + (bg.bgVelX * elapsedTime),
//                        theSnake2.getSegment(i - 1).getPosY() + (bg.bgVelY * elapsedTime));
//                    theSnake2.getSegment(i).setAngle(theSnake2.getSegment(i - 1).getAngle());
//                    theSnake2.getSegment(i).render(gc);
//                }
//                theSnake2.getHead().render(gc);
//                
//                //Renders the AI snake
//                for (int i = theSnake3.getSize() - 1; i > 0; i--) {
//                    theSnake3.getSegment(i).setPosition(
//                        theSnake3.getSegment(i - 1).getPosX() + (bg.bgVelX * elapsedTime),
//                        theSnake3.getSegment(i - 1).getPosY() + (bg.bgVelY * elapsedTime));
//                    theSnake3.getSegment(i).setAngle(theSnake3.getSegment(i - 1).getAngle());
//                    theSnake3.getSegment(i).render(gc);
//                }
//                theSnake3.getHead().render(gc);
                
                //places the apples on the gameGrid and adjusts for backgroud speed
                for (Sprite apple : appleList) {
                    apple.setPosition((apple.getPosX() + bg.getBGVelX() * elapsedTime),
                            (apple.getPosY() + bg.getBGVelY() * elapsedTime));
                    apple.render(gc);
                }

                //places the walls on the gameGrid and adjusts for background speed
                for (Sprite wall : wallList) {
                    wall.setPosition((wall.getPosX() + bg.getBGVelX() * elapsedTime), (wall.getPosY() + bg.getBGVelY() * elapsedTime));
                    wall.render(gc);
                }

                // String pointsText = "Score: " + (100 * score.value) + " , angle: " + (SAI.memAngle - 90.0);
                String pointsText = "Score: " + (100 * player.getScore());
                gc.fillText(pointsText, 360, 24);
                gc.strokeText(pointsText, 360, 24);
            }

        }.start();

        primaryStage.show();
    }

  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
/**
 * Turns a long into a class with a public data field
 * @author kamuela94
 */
class LongValue {

    public long value;

    public LongValue(long i) {
        value = i;
    }
}

/**
 * Turns an int into a class with a public data field
 * @author kamuela94
 */
class IntValue {

    public int value;

    public IntValue(int i) {
        value = i;
    }
}

/**
 * Holds the information for the background velocities and positions
 * @author kamuela94
 */
class Background {

    // Background Position, Accessors, and Setters
    public double bgX = 0.0;
    public double bgY = 0.0;
    // Background Velocites, Accessors and Setters
    double bgVelX = 0.0;
    double bgVelY = 0.0;
    
    /**
    * 
    * @return The x value of the background velocity
    */
    double getBGVelX() {
        return bgVelX;
    }

    /**
     * 
     * @return The y value of the background velocity
     */
    double getBGVelY() {
        return bgVelY;
    }

    /**
     * Sets the x value of the background velocity
     * @param num the double to be set to background velocity X
     */
    void setBGVelX(double num) {
        bgVelX = num;
    }

    /**
     * Sets the y value of the background velocity
     * @param num the double to be set to background velocity Y
     */
    void setBGVelY(double num) {
        bgVelY = num;
    }

    /**
     * 
     * @return the position of the background on the x plane
     */
    double getBGX() {
        return bgX;
    }

    /**
     * 
     * @return the position of the background on the y plane
     */
    double getBGY() {
        return bgY;
    }
    
    /**
     * sets the position of the background to value num on the x plane
     * @param num 
     */
    void setBGX(double num) {
        bgX = num;
    }

    /**
     * sets the position of the background to value num on the y plane
     * @param num 
     */
    void setBGY(double num) {
        bgY = num;
    }
}
