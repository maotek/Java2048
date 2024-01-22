package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;
import java.util.stream.Collectors;

public class GameCtrl {

    private int points;
    private int seconds;

    private Parent parent;
    private Scene scene;

    List<Label> available;
    List<Label> all;

    private Thread timeThread;

    @FXML
    private Label score;

    @FXML
    private Label time;

    @FXML
    private GridPane grid;


    public class Pair {
        public int col;
        public int row;
    }


    public GameCtrl() {
        this.points = 0;
        this.seconds = 0;
    }

    public void setPoint(int points) {
        this.score.setText("Score\n" + String.valueOf(points));
    }

    public void setParent(Parent parent){
        this.parent = parent;
        parent.setOnMousePressed(event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                start();
            }
        });
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        scene.setOnKeyPressed(e->{
            switch(e.getCode()) {
                case W:
                    move(0);
                    break;
                case S:
                    move(1);
                    break;
                case A:
                    move(2);
                    break;
                case D:
                    move(3);
                    break;
            }
        });
    }

    public void start(){
        grid.setAlignment(Pos.CENTER);
        score.setAlignment(Pos.CENTER);
        time.setAlignment(Pos.CENTER);
        grid.getChildren().removeIf(node -> node instanceof Label);
        grid.setDisable(false);
        points = 0;
        seconds = 0;

        if(this.timeThread != null) {this.timeThread.interrupt();}
        startTime();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Label lb = new Label("");
                lb.setPrefWidth(94);
                lb.setPrefHeight(94);
                lb.setAlignment(Pos.CENTER);
                GridPane.setHalignment(lb, HPos.CENTER);
                GridPane.setValignment(lb, VPos.CENTER);
                lb.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 10");
                lb.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
                lb.setFont(new Font("Comic Sans MS", 30));
                lb.setVisible(true);
                lb.setVisible(false);
                grid.add(lb, i, j);
            }
        }

        available = new ArrayList<>();
        all = grid.getChildren().stream().filter(x->x instanceof Label).map(x->(Label)x).collect(Collectors.toList());
        available.addAll(all);

        spawn();
        spawn();
    }

    public void spawn() {
        if(available.isEmpty()) {return;}
        Random rn = new Random();
        int num = (rn.nextBoolean()) ? 2 : 4;
        points+=num;
        setPoint(points);
        int index = rn.nextInt(available.size());
        Label label = available.get(index);
        setEntry(label, num);
    }

    public void setEntry(Label l, int num) {
        setColor(l, num);
        l.setText(String.valueOf(num));
        l.setVisible(true);
        available.remove(l);
    }

    public void setColor(Label l, int num) {
        int level = (int)(Math.log(num) / Math.log(2));
        if(level > 12) {
            l.setStyle("-fx-background-color: rgba("+(200 - (51 * (level-13)))+ "," + (255 - (51 * (level-13))) + "," + 200 + ",0.7); -fx-background-radius: 10");
        } else if(level > 6) {
            l.setStyle("-fx-background-color: rgba("+(200 - (51 * (level-7)))+ "," + 255 + "," + (255 - (51 * (level-7))) + ",0.7); -fx-background-radius: 10");
        } else {
            l.setStyle("-fx-background-color: rgba("+255 + "," + (255 - (51 * level)) + "," + (255 - (51 * level)) + ",0.7); -fx-background-radius: 10");
        }
    }

    public void resetEntry(Label l) {
        l.setText("");
        available.add(l);
        l.setVisible(false);
    }

    public void move(int dir) {
        boolean moved = false;
        switch (dir) {
            case 0:
                for (int i = 0; i < 16; i+=4) {
                    int pivot = i;
                    for (int j = i; j < i+4; j++) {
                        Label current = all.get(j);
                        String currentVal = current.getText();
                        if(!currentVal.equals("")) {
                            if(all.get(pivot).equals(current)) {
                                continue;
                            }
                            if(all.get(pivot).getText().equals("")) {
                                resetEntry(current);
                                setEntry(all.get(pivot), Integer.parseInt(currentVal));
                                moved = true;
                            }
                            else {
                                if(all.get(pivot).getText().equals(currentVal)) {
                                    resetEntry(current);
                                    setEntry(all.get(pivot++), Integer.parseInt(currentVal) * 2);
                                    moved = true;

                                } else {
                                    // bug fix for not spawning.
                                    if(all.get(j-1).getText().equals("")) {moved=true;}
                                    resetEntry(current);
                                    setEntry(all.get(++pivot), Integer.parseInt(currentVal));


                                }

                            }

                        }
                    }
                }
                break;
            case 1:
                for (int i = 15; i >=0; i-=4) {
                    int pivot = i;
                    for (int j = i; j > i-4; j--) {
                        Label current = all.get(j);
                        String currentVal = current.getText();
                        if(!currentVal.equals("")) {
                            if(all.get(pivot).equals(current)) {
                                continue;
                            }
                            if(all.get(pivot).getText().equals("")) {
                                resetEntry(current);
                                setEntry(all.get(pivot), Integer.parseInt(currentVal));
                                moved = true;
                            }
                            else {
                                if(all.get(pivot).getText().equals(currentVal)) {
                                    resetEntry(current);
                                    setEntry(all.get(pivot--), Integer.parseInt(currentVal) * 2);
                                    moved = true;
                                } else {
                                    if(all.get(j+1).getText().equals("")) {moved=true;}
                                    resetEntry(current);
                                    setEntry(all.get(--pivot), Integer.parseInt(currentVal));
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < 4; i++) {
                    int pivot = i;
                    for (int j = i; j < 16; j+=4) {
                        Label current = all.get(j);
                        String currentVal = current.getText();
                        if(!currentVal.equals("")) {
                            if(all.get(pivot).equals(current)) {
                                continue;
                            }
                            if(all.get(pivot).getText().equals("")) {
                                resetEntry(current);
                                setEntry(all.get(pivot), Integer.parseInt(currentVal));
                                moved = true;
                            }
                            else {
                                if(all.get(pivot).getText().equals(currentVal)) {
                                    resetEntry(current);
                                    setEntry(all.get(pivot), Integer.parseInt(currentVal) * 2);
                                    moved = true;
                                    pivot+=4;
                                } else {
                                    if(all.get(j-4).getText().equals("")) {moved=true;}
                                    resetEntry(current);
                                    pivot+=4;
                                    setEntry(all.get(pivot), Integer.parseInt(currentVal));
                                }
                            }
                        }
                    }
                }
                break;
            case 3:
                for (int i = 15; i >= 12; i--) {
                    int pivot = i;
                    for (int j = i; j >= 0; j-=4) {
                        Label current = all.get(j);
                        String currentVal = current.getText();
                        if(!currentVal.equals("")) {
                            if(all.get(pivot).equals(current)) {
                                continue;
                            }
                            if(all.get(pivot).getText().equals("")) {
                                resetEntry(current);
                                setEntry(all.get(pivot), Integer.parseInt(currentVal));
                                moved = true;
                            }
                            else {
                                if(all.get(pivot).getText().equals(currentVal)) {
                                    resetEntry(current);
                                    setEntry(all.get(pivot), Integer.parseInt(currentVal) * 2);
                                    pivot-=4;
                                    moved = true;
                                } else {
                                    if(all.get(j+4).getText().equals("")) {moved=true;}
                                    resetEntry(current);
                                    pivot-=4;
                                    setEntry(all.get(pivot), Integer.parseInt(currentVal));
                                }
                            }
                        }
                    }
                }
                break;
        }
        if(!stillAMove()) {grid.setDisable(true); timeThread.interrupt();}
        if(moved) {spawn();}
    }

    public boolean stillAMove() {
        if(!available.isEmpty()) {
            return true;
        }
        for (int i = 0; i < 16; i+=4) {
            for (int j = i; j < i+4; j++) {
                if(j==15) {return false;}
                Label l = all.get(j);
                if(j > 11) {
                    if(l.getText().equals(all.get(j+1).getText())) {
                        return true;
                    }
                    continue;
                }
                if(j == i + 3) {
                    if(l.getText().equals(all.get(j+4).getText())) {
                        return true;
                    }
                    continue;
                }
                if((l.getText().equals(all.get(j+1).getText()) || l.getText().equals(all.get(j+4).getText()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void startTime() {
        Thread t = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> time.setText("Time\n" + seconds++));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Platform.runLater(() -> time.setText("LOSE!\nTime\n" + seconds++));
                    return;
                }
            }
        });
        this.timeThread = t;
        t.setDaemon(true);
        t.start();
    }
}
