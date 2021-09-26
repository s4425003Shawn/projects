import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * The setting up GUI
 */
public class CrawlGui extends javafx.application.Application {
    //
    private static String[] arguments;
    private Button northButton;
    private Button southButton;
    private Button westButton;
    private Button eastButton;
    private Button lookButton;
    private Button examineButton;
    private Button dropButton;
    private Button takeButton;
    private Button fightButton;
    private Button saveButton;
    private TextArea textArea;
    private Room startRoom;
    private Room playerRoom;
    private Player player;
    private Object[] o;
    private BoundsMapper roomMap;
    private Cartographer canvas;

    /**
     * Launch the game by typing the saved file name
     *
     * @param args file name
     */
    public static void main(String[] args) {
        arguments = args;
        launch(args);

    }

    /**
     * North button function
     * If there is no exit in the specified direction, display “No door that
     * way”. If there is an exit in that
     * direction but you can’t leave, display “Something prevents you from
     * leaving”. Otherwise, move to the
     * specified room and display “You enter ” followed by the description
     * of the room.
     */
    public void respondToNorthButton() {
        if (!playerRoom.getExits().containsKey("North")) {
            textArea.appendText("No door that way\n");
        } else if (!playerRoom.leave(player)) {
            textArea.appendText("Something prevents you from leaving\n");
        } else {
            playerRoom.leave(player);
            playerRoom.getExits().get("North").enter(player);
            playerRoom = playerRoom.getExits().get("North");
            textArea.appendText("You enter " + playerRoom.getDescription() +
                    "\n");
            update();

        }
    }

    /**
     * South button function
     * If there is no exit in the specified direction, display “No door  that
     * way”. If there is an exit in that
     * direction but you can’t leave, display “Something prevents you from
     * leaving”. Otherwise, move to the
     * specified room and display “You enter ” followed by the description
     * of the room.
     */
    public void respondToSouthButton() {
        if (!playerRoom.getExits().containsKey("South")) {
            textArea.appendText("No door that way\n");
        } else if (!playerRoom.leave(player)) {
            textArea.appendText("Something prevents you from leaving\n");
        } else {
            playerRoom.leave(player);
            playerRoom.getExits().get("South").enter(player);
            playerRoom = playerRoom.getExits().get("South");
            textArea.appendText("You enter " + playerRoom.getDescription() +
                    "\n");
            update();
        }
    }

    /**
     * West button function
     * If there is no exit in the specified direction, display “No door that
     * way”. If there is an exit in that
     * direction but you can’t leave, display “Something prevents you from
     * leaving”. Otherwise, move to the
     * specified room and display “You enter ” followed by the description
     * of the room.
     */
    public void respondToWestButton() {
        if (!playerRoom.getExits().containsKey("West")) {
            textArea.appendText("No door that way\n");
        } else if (!playerRoom.leave(player)) {
            textArea.appendText("Something prevents you from leaving\n");
        } else {
            playerRoom.leave(player);
            playerRoom.getExits().get("West").enter(player);
            playerRoom = playerRoom.getExits().get("West");
            textArea.appendText("You enter " + playerRoom.getDescription() +
                    "\n");
            update();
        }
    }

    /**
     * East button function
     * If there is no exit in the specified direction, display “No door that
     * way”. If there is an exit in that
     * direction but you can’t leave, display “Something prevents you from
     * leaving”. Otherwise, move to the
     * specified room and display “You enter ” followed by the description
     * of the room.
     */
    public void respondToEastButton() {
        if (!playerRoom.getExits().containsKey("East")) {
            textArea.appendText("No door that way\n");
        } else if (!playerRoom.leave(player)) {
            textArea.appendText("Something prevents you from leaving\n");
        } else {
            playerRoom.leave(player);
            playerRoom.getExits().get("East").enter(player);
            playerRoom = playerRoom.getExits().get("East");
            textArea.appendText("You enter " + playerRoom.getDescription() +
                    "\n");
            update();
        }
    }

    /**
     * Look button function
     * Display the following information:
     * “description_of_room - you see:”
     * followed by the short descriptions of each Thing in the room  (add a
     * leading space for each item). Then
     * “You are carrying:”
     * followed by the short descriptions of each Thing (add a leading  space
     * for each item) you are carrying.
     * Then
     * "worth total_worth_of_carried_items in total"
     * (without the quotes and with the italics text replaced). Formatted
     * for one decimal place. See Figure 4
     * for an example.
     */
    public void respondToLookButton() {
        double value = 0;
        textArea.appendText(playerRoom.getDescription() + " - you see:\n");
        for (Thing t : playerRoom.getContents()) {
            textArea.appendText(" " + t.getShort() + "\n");
        }
        textArea.appendText("You are carrying:\n");
        for (Thing t : player.getContents()) {
            textArea.appendText(" " + t.getShort() + "\n");
            if (t instanceof Treasure) {
                value += ((Treasure) t).getValue();
            } else if (t instanceof Critter) {
                value += ((Critter) t).getValue();
            }
        }
        textArea.appendText("worth " + String.format("%.1f", value) + " in " +
                "total\n");
    }

    /**
     * Examine button function
     * Show a dialog box (see Figure 5) to get the short description of the
     * Thing to examine. The first
     * matching item will have its long description displayed. Check the
     * player’s inventory first, then if no
     * match is found, the contents of the current room. If no match is
     * found, display “Nothing found with
     * that name”.
     */
    public void respondToExamineButton() {
        TextInputDialog te = new TextInputDialog();
        te.initStyle(StageStyle.UNIFIED);
        te.setTitle("Examine what?");
        te.setHeaderText(null);
        te.setGraphic(null);
        Optional<String> res = te.showAndWait();
        if (res.isPresent()) {
            for (Thing t : player.getContents()) {
                if (res.get().equals(t.getShort())) {
                    textArea.appendText(t.getLong() + "\n");
                    return;
                }
            }
            for (Thing t : playerRoom.getContents()) {
                if (res.get().equals(t.getShort())) {
                    textArea.appendText(t.getLong() + "\n");
                    return;
                }
            }
            textArea.appendText("Nothing found with that name\n");
        }
    }

    /**
     * Drop button function
     * Show a dialog box (See Figure 6) to get the short description of the
     * item to remove from the player’s
     * inventory and add to the current room. If the player is not carrying
     * a matching item, display “Nothing
     * found with that name”
     */
    public void respondToDropButton() {
        TextInputDialog te = new TextInputDialog();
        te.initStyle(StageStyle.UNIFIED);
        te.setTitle("Item to drop?");
        te.setHeaderText(null);
        te.setGraphic(null);
        Optional<String> res = te.showAndWait();
        if (res.isPresent()) {
            for (Thing t : player.getContents()) {
                if (res.get().equals(t.getShort())) {
                    playerRoom.enter(player.drop(res.get()));
                    textArea.appendText("You drop " + t.getShort() + "\n");
                    update();
                    return;
                }
            }
            textArea.appendText("Nothing found with that name\n");
        }

    }

    /**
     * Take button function
     * Similar to Drop but the dialog box is to be titled “Take what?”.
     * Objects  of type Player should be
     * skipped when looking for short description matches. There are
     * additional  cases where the operation
     * will fail (silently):
     * • If an attempt is made to pick up a live Mob
     * • If the leave call to remove the item returns false. [Remember that,
     * to remove an item from a
     * room, you will need to call leave on the room].
     */
    public void respondToTakeButton() {
        TextInputDialog te = new TextInputDialog();
        te.initStyle(StageStyle.UNIFIED);
        te.setTitle("Take what?");
        te.setHeaderText(null);
        te.setGraphic(null);
        Optional<String> res = te.showAndWait();
        if (res.isPresent()) {
            for (Thing t : playerRoom.getContents()) {
                if (t.getShort().equals(res.get())) {
                    if (t instanceof Player) {
                        break;
                    } else if (t instanceof Mob && ((Mob) t).isAlive()) {
                        break;
                    } else if (playerRoom.leave(t)) {
                        player.add(t);
                        textArea.appendText("You took " + t.getShort() + "\n");
                        update();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Fight function button
     * Show a dialog box (titled “Fight what?”) to get the short description
     * of a Critter in the current
     * room to fight. If the fight occurs, display either “You won” or “Game
     * over” as appropriate. (If you
     * lose the fight, all of the buttons on the GUI should be disabled).
     * Silent failures will occur if:
     * • There is no matching Critter.
     * • There is a matching Critter but it is not alive.
     */
    public void respondToFightButton() {
        TextInputDialog te = new TextInputDialog();
        te.initStyle(StageStyle.UNIFIED);
        te.setTitle("Fight what?");
        te.setHeaderText(null);
        te.setGraphic(null);
        Optional<String> res = te.showAndWait();
        if (res.isPresent()) {
            for (Thing t : playerRoom.getContents()) {
                if (t.getShort().equals(res.get())) {
                    if (t instanceof Critter && ((Critter) t).isAlive()) {
                        player.fight((Mob) t);
                        if (player.isAlive()) {
                            textArea.appendText("You won\n");
                            update();
                        } else {
                            textArea.appendText("Game over\n");
                            lookButton.setDisable(true);
                            northButton.setDisable(true);
                            southButton.setDisable(true);
                            eastButton.setDisable(true);
                            westButton.setDisable(true);
                            examineButton.setDisable(true);
                            dropButton.setDisable(true);
                            takeButton.setDisable(true);
                            fightButton.setDisable(true);
                            saveButton.setDisable(true);
                            update();
                        }
                    }
                }
            }
        }
    }

    /**
     * Save button function
     * Show a dialog box (titled “Save filename?”) for a file name to use
     * with MapIO.saveMap. Display either
     * “Saved” or “Unable to save” as appropriate.
     */
    public void respondToSaveButton() {
        TextInputDialog te = new TextInputDialog();
        te.initStyle(StageStyle.UNIFIED);
        te.setTitle("Save filename?");
        te.setHeaderText(null);
        te.setGraphic(null);
        Optional<String> res = te.showAndWait();
        if (res.isPresent()) {
            if (MapIO.saveMap(startRoom, res.get())) {
                textArea.appendText("Saved\n");
            } else {
                textArea.appendText("Unable to save\n");
            }
        }
    }

    /**
     * The gui start setting everything include button and canvas and textarea
     *
     * @param stage The present stage
     */
    public void start(Stage stage) {

        if (arguments.length == 0) {
            System.out.println("Usage: java CrawlGui mapname");
            System.exit(1);
        } else if (MapIO.loadMap(arguments[0]) == null) {
            System.out.println("Unable to load file");
            System.exit(2);
        }
        //Preset
        o = MapIO.loadMap(arguments[0]);
        startRoom = (Room) o[1];
        player = (Player) o[0];
        startRoom.enter(player);
        playerRoom = startRoom;
        roomMap = new BoundsMapper(startRoom);
        roomMap.walk();
        stage.setTitle("Crawl - Explore");

        //Button area setting
        northButton = new Button("North");
        southButton = new Button("South");
        eastButton = new Button("East");
        westButton = new Button("West");
        lookButton = new Button("Look");
        examineButton = new Button("Examine");
        dropButton = new Button("Drop");
        takeButton = new Button("Take");
        fightButton = new Button("Fight");
        saveButton = new Button("Save");

        //Text area setting
        textArea = new TextArea("You find yourself in the " +
                startRoom.getDescription() + "\n");
        textArea.setPrefRowCount(10);
        textArea.setPrefColumnCount(10);

        //button handler
        northButton.setOnAction(new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent e) {
                                        respondToNorthButton();
                                    }   // end of handle method
                                }   // end of class
        );

        westButton.setOnAction(new EventHandler<ActionEvent>() {
                                   public void handle(ActionEvent e) {
                                       respondToWestButton();
                                   }   // end of handle method
                               }   // end of class
        );

        southButton.setOnAction(new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent e) {
                                        respondToSouthButton();
                                    }   // end of handle method
                                }   // end of class
        );
        eastButton.setOnAction(new EventHandler<ActionEvent>() {
                                   public void handle(ActionEvent e) {
                                       respondToEastButton();
                                   }   // end of handle method
                               }   // end of class
        );

        lookButton.setOnAction(new EventHandler<ActionEvent>() {
                                   public void handle(ActionEvent e) {
                                       respondToLookButton();
                                   }   // end of handle method
                               }   // end of class
        );
        examineButton.setOnAction(new EventHandler<ActionEvent>() {
                                      public void handle(ActionEvent e) {
                                          respondToExamineButton();
                                      }   // end of handle method
                                  }   // end of class
        );
        dropButton.setOnAction(new EventHandler<ActionEvent>() {
                                   public void handle(ActionEvent e) {
                                       respondToDropButton();
                                   }   // end of handle method
                               }   // end of class
        );

        takeButton.setOnAction(new EventHandler<ActionEvent>() {
                                   public void handle(ActionEvent e) {
                                       respondToTakeButton();
                                   }   // end of handle method
                               }   // end of class
        );
        fightButton.setOnAction(new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent e) {
                                        respondToFightButton();
                                    }   // end of handle method
                                }   // end of class
        );
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
                                   public void handle(ActionEvent e) {
                                       respondToSaveButton();
                                   }   // end of handle method
                               }   // end of class
        );
        BorderPane bp = new BorderPane();

        /*Map Area*/
        canvas = new Cartographer((roomMap.xMax - roomMap.xMin + 1) * 40,
                (roomMap.yMax - roomMap.yMin + 1) * 40);
        update();
        BorderPane mapArea = new BorderPane();
        mapArea.setCenter(canvas);
        bp.setCenter(mapArea);

        /*Adding button in GUI*/
        GridPane buttonArea = new GridPane();
        bp.setRight(buttonArea);

        buttonArea.add(northButton, 1, 0);
        buttonArea.add(southButton, 1, 2);
        buttonArea.add(westButton, 0, 1);
        buttonArea.add(eastButton, 2, 1);
        buttonArea.add(lookButton, 0, 3);
        buttonArea.add(examineButton, 1, 3, 2, 1);
        buttonArea.add(dropButton, 0, 4);
        buttonArea.add(takeButton, 1, 4);
        buttonArea.add(fightButton, 0, 5);
        buttonArea.add(saveButton, 0, 6);

        bp.setBottom(textArea);

        Scene scene = new Scene(bp, (roomMap.xMax - roomMap.xMin + 1) * 40 +
                250,
                (roomMap.yMax - roomMap.yMin + 1) * 40 + 280);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Update canvas in every movement
     */
    public void update() {
        //update every action
        clearAll();
        //Draw rooms
        for (Room r : roomMap.coords.keySet()) {
            canvas.drawRoom((roomMap.coords.get(r).x + (0 - roomMap.xMin)) *
                    40, (roomMap.coords.get(r)
                    .y + (0 - roomMap.yMin)) * 40);


            for (String label : r.getExits().keySet()) {
                switch (label) {
                    case "North":
                        canvas.drawExit((roomMap.coords.get(r).x + (0 -
                                roomMap.xMin)) * 40 + 20, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40, (roomMap
                                .coords.get(r).x + (0 - roomMap.xMin)) *
                                40 + 20, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 5);
                        break;
                    case "East":
                        canvas.drawExit((roomMap.coords.get(r).x + (0 -
                                roomMap.xMin)) * 40 + 35, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 20, (roomMap
                                .coords.get(r).x + (0 - roomMap.xMin)) *
                                40 + 40, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 20);
                        break;
                    case "South":
                        canvas.drawExit((roomMap.coords.get(r).x + (0 -
                                roomMap.xMin)) * 40 + 20, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 35, (roomMap
                                .coords.get(r).x + (0 - roomMap.xMin)) *
                                40 + 20, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 40);
                        break;
                    case "West":
                        canvas.drawExit((roomMap.coords.get(r).x + (0 -
                                roomMap.xMin)) * 40, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 20, (roomMap
                                .coords.get(r).x + (0 - roomMap.xMin)) *
                                40 + 5, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 20);
                        break;
                }
            }

            //draw player
            if (r.getContents().contains(player)) {
                canvas.drawPlayer((roomMap.coords.get(r).x + (0 - roomMap.xMin))
                        * 40 + 1, (roomMap.coords.get(r)
                        .y + (0 - roomMap.yMin)) * 40 + 10);
            }
            //Draw Contents in room
            for (Thing t : r.getContents()) {
                if (t instanceof Critter) {
                    if (((Critter) t).isAlive()) {
                        canvas.drawCritter((roomMap.coords.get(r).x + (0 - roomMap.xMin))
                                * 40 + 1, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 35);
                    } else if (!((Critter) t).isAlive()) {
                        canvas.drawDeadCritter((roomMap.coords.get(r).x + (0 -
                                roomMap.xMin))
                                * 40 + 25, (roomMap.coords.get(r)
                                .y + (0 - roomMap.yMin)) * 40 + 35);
                    }
                } else if (t instanceof Treasure) {
                    canvas.drawTreasure((roomMap.coords.get(r).x + (0 - roomMap.xMin))
                            * 40 + 30, (roomMap.coords.get(r)
                            .y + (0 - roomMap.yMin)) * 40 + 10);
                }
            }
        }
    }

    /**
     * Clear all the content in canvas
     */
    public void clearAll() {
        canvas.clear((roomMap.xMax - roomMap.xMin + 1) * 40,
                (roomMap.yMax - roomMap.yMin + 1) * 40);
    }
}
