/*
 * Copyright (c) 2022 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.fx.touchslider;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Demo extends Application {
    private TouchSlider volumeSlider;
    private TouchSlider bassSlider;
    private TouchSlider trebleSlider;
    private CheckBox    valueVisibleCheckBox;
    private CheckBox    nameVisibleCheckBox;
    private CheckBox    barBackgroundFilledCheckBox;
    private CheckBox    showZeroCheckBox;
    private CheckBox    startFromZeroCheckBox;
    private CheckBox    snapToZeroCheckBox;
    private CheckBox    returnToZeroCheckBox;
    private VBox        buttonBox;


    @Override public void init() {
        volumeSlider = TouchSliderBuilder.create()
                                         .prefSize(200, 600)
                                         .name("Volume")
                                         .orientation(Orientation.VERTICAL)
                                         .minValue(0)
                                         .range(100)
                                         .formatString("%.0f")
                                         .barBackgroundColor(Color.rgb(50, 50, 50, 0.5))
                                         .barColor(Color.rgb(50, 50, 50))
                                         .thumbColor(Color.rgb(100, 100, 100))
                                         .valueTextColor(Color.WHITE)
                                         .nameTextColor(Color.WHITE)
                                         .valueVisible(true)
                                         .nameVisible(true)
                                         .onTouchSliderEvent(e -> System.out.println("Volume: " + e.getValue()))
                                         .build();
        bassSlider = TouchSliderBuilder.create()
                                       .prefSize(200, 600)
                                       .name("Bass")
                                       .orientation(Orientation.VERTICAL)
                                       .minValue(-50)
                                       .range(100)
                                       .formatString("%.0f")
                                       .barBackgroundColor(Color.rgb(50, 50, 150, 0.5))
                                       .barColor(Color.rgb(50, 50, 150))
                                       .thumbColor(Color.rgb(0, 0, 100))
                                       .valueTextColor(Color.WHITE)
                                       .nameTextColor(Color.WHITE)
                                       .zeroColor(Color.WHITE)
                                       .showZero(true)
                                       .valueVisible(true)
                                       .nameVisible(true)
                                       .onTouchSliderEvent(e -> System.out.println("Bass: " + e.getValue()))
                                       .build();
        trebleSlider = TouchSliderBuilder.create()
                                         .prefSize(200, 600)
                                         .minValue(-100)
                                         .range(200)
                                         .sliderValue(0)
                                         .onTouchSliderEvent(e -> System.out.println("Treble: " + e.getValue()))
                                         .styleClass("treble-slider")
                                         .build();

        valueVisibleCheckBox = new CheckBox("Value Visible");
        valueVisibleCheckBox.selectedProperty().addListener((o, ov, nv) -> trebleSlider.setValueVisible(nv));
        valueVisibleCheckBox.setSelected(true);

        nameVisibleCheckBox = new CheckBox("Name Visible");
        nameVisibleCheckBox.setSelected(true);
        nameVisibleCheckBox.selectedProperty().addListener((o, ov, nv) -> trebleSlider.setNameVisible(nv));

        barBackgroundFilledCheckBox = new CheckBox("Bar background filled");
        barBackgroundFilledCheckBox.selectedProperty().addListener((o, ov, nv) -> trebleSlider.setBarBackbroundFilled(nv));

        showZeroCheckBox = new CheckBox("Show zero");
        showZeroCheckBox.setSelected(true);
        showZeroCheckBox.selectedProperty().addListener((o, ov, nv) -> trebleSlider.setShowZero(nv));

        startFromZeroCheckBox = new CheckBox("Start from zero");
        startFromZeroCheckBox.setSelected(true);
        startFromZeroCheckBox.selectedProperty().addListener((o, ov, nv) -> trebleSlider.setStartFromZero(nv));

        snapToZeroCheckBox = new CheckBox("Snap to zero");
        snapToZeroCheckBox.setSelected(true);
        snapToZeroCheckBox.selectedProperty().addListener((o, ov, nv) -> trebleSlider.setSnapToZero(nv));

        returnToZeroCheckBox = new CheckBox("Return to zero");
        returnToZeroCheckBox.setSelected(false);
        returnToZeroCheckBox.selectedProperty().addListener((o, ov, nv) -> trebleSlider.setReturnToZero(nv));

        buttonBox = new VBox(5, valueVisibleCheckBox, nameVisibleCheckBox, barBackgroundFilledCheckBox, showZeroCheckBox, startFromZeroCheckBox, snapToZeroCheckBox, returnToZeroCheckBox);
    }

    @Override public void start(Stage stage) {
        HBox pane = new HBox(volumeSlider, bassSlider, trebleSlider, buttonBox);
        pane.setPadding(new Insets(20));

        Scene scene = new Scene(pane);
        scene.getStylesheets().add(Demo.class.getResource("custom.css").toExternalForm());

        stage.setTitle("TouchSlider");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
