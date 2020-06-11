/*
 * Copyright (c) 2020 by Gerrit Grunwald
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;

import java.util.HashMap;


public class TouchSliderBuilder<B extends TouchSliderBuilder<B>> {
    private HashMap<String, Property> properties = new HashMap<>();


    // ******************** Constructors **************************************
    protected TouchSliderBuilder() {}


    // ******************** Methods *******************************************
    public static final TouchSliderBuilder create() {
        return new TouchSliderBuilder();
    }

    public final B name(final String name) {
        properties.put("name", new SimpleStringProperty(name));
        return (B)this;
    }

    public final B orientation(final Orientation orientation) {
        properties.put("orientation", new SimpleObjectProperty<>(orientation));
        return (B)this;
    }

    public final B minValue(final double minValue) {
        properties.put("minValue", new SimpleDoubleProperty(minValue));
        return (B)this;
    }

    public final B range(final double range) {
        properties.put("range", new SimpleDoubleProperty(range));
        return (B)this;
    }

    public final B sliderValue(final double value) {
        properties.put("sliderValue", new SimpleDoubleProperty(value));
        return (B)this;
    }

    public final B barBackgroundColor(final Color color) {
        properties.put("barBackgroundColor", new SimpleObjectProperty<>(color));
        return (B)this;
    }

    public final B barColor(final Color color) {
        properties.put("barColor", new SimpleObjectProperty<>(color));
        return (B)this;
    }

    public final B thumbColor(final Color color) {
        properties.put("thumbColor", new SimpleObjectProperty<>(color));
        return (B)this;
    }

    public final B valueTextColor(final Color color) {
        properties.put("valueTextColor", new SimpleObjectProperty<>(color));
        return (B)this;
    }

    public final B nameTextColor(final Color color) {
        properties.put("nameTextColor", new SimpleObjectProperty<>(color));
        return (B)this;
    }

    public final B zeroColor(final Color color) {
        properties.put("zeroColor", new SimpleObjectProperty<>(color));
        return (B)this;
    }

    public final B formatString(final String formatString) {
        properties.put("formatString", new SimpleStringProperty(formatString));
        return (B)this;
    }

    public final B valueVisible(final boolean visible) {
        properties.put("valueVisible", new SimpleBooleanProperty(visible));
        return (B)this;
    }

    public final B nameVisible(final boolean visible) {
        properties.put("nameVisible", new SimpleBooleanProperty(visible));
        return (B)this;
    }

    public final B barBackroundFilled(final boolean filled) {
        properties.put("barBackgroundFilled", new SimpleBooleanProperty(filled));
        return (B)this;
    }

    public final B showZero(final boolean show) {
        properties.put("showZero", new SimpleBooleanProperty(show));
        return (B)this;
    }

    public final B startFromZero(final boolean start) {
        properties.put("startFromZero", new SimpleBooleanProperty(start));
        return (B)this;
    }

    public final B snapToZero(final boolean snap) {
        properties.put("snapToZero", new SimpleBooleanProperty(snap));
        return (B)this;
    }

    public final B onTouchSliderEvent(final TouchSliderObserver observer) {
        properties.put("observer", new SimpleObjectProperty<>(observer));
        return (B)this;
    }

    public final B styleClass(final String styleClass) {
        properties.put("styleClass", new SimpleStringProperty(styleClass));
        return (B)this;
    }

    public final B prefSize(final double width, final double height) {
        properties.put("prefSize", new SimpleObjectProperty<>(new Dimension2D(width, height)));
        return (B)this;
    }
    public final B minSize(final double width, final double height) {
        properties.put("minSize", new SimpleObjectProperty<>(new Dimension2D(width, height)));
        return (B)this;
    }
    public final B maxSize(final double width, final double height) {
        properties.put("maxSize", new SimpleObjectProperty<>(new Dimension2D(width, height)));
        return (B)this;
    }

    public final B prefWidth(final double prefWidth) {
        properties.put("prefWidth", new SimpleDoubleProperty(prefWidth));
        return (B)this;
    }
    public final B prefHeight(final double prefHeight) {
        properties.put("prefHeight", new SimpleDoubleProperty(prefHeight));
        return (B)this;
    }

    public final B minWidth(final double minWidth) {
        properties.put("minWidth", new SimpleDoubleProperty(minWidth));
        return (B)this;
    }
    public final B minHeight(final double minHeight) {
        properties.put("minHeight", new SimpleDoubleProperty(minHeight));
        return (B)this;
    }

    public final B maxWidth(final double maxWidth) {
        properties.put("maxWidth", new SimpleDoubleProperty(maxWidth));
        return (B)this;
    }
    public final B maxHeight(final double maxHeight) {
        properties.put("maxHeight", new SimpleDoubleProperty(maxHeight));
        return (B)this;
    }

    public final B scaleX(final double scaleX) {
        properties.put("scaleX", new SimpleDoubleProperty(scaleX));
        return (B)this;
    }
    public final B scaleY(final double scaleY) {
        properties.put("scaleY", new SimpleDoubleProperty(scaleY));
        return (B)this;
    }

    public final B layoutX(final double layoutX) {
        properties.put("layoutX", new SimpleDoubleProperty(layoutX));
        return (B)this;
    }
    public final B layoutY(final double layoutY) {
        properties.put("layoutY", new SimpleDoubleProperty(layoutY));
        return (B)this;
    }

    public final B translateX(final double translateX) {
        properties.put("translateX", new SimpleDoubleProperty(translateX));
        return (B)this;
    }
    public final B translateY(final double translateY) {
        properties.put("translateY", new SimpleDoubleProperty(translateY));
        return (B)this;
    }

    public final B padding(final Insets insets) {
        properties.put("padding", new SimpleObjectProperty<>(insets));
        return (B)this;
    }
    
    public final TouchSlider build() {
        final TouchSlider touchSlider = new TouchSlider();
        for (String key : properties.keySet()) {
            if ("prefSize".equals(key)) {
                Dimension2D dim = ((ObjectProperty<Dimension2D>) properties.get(key)).get();
                touchSlider.setPrefSize(dim.getWidth(), dim.getHeight());
            } else if("minSize".equals(key)) {
                Dimension2D dim = ((ObjectProperty<Dimension2D>) properties.get(key)).get();
                touchSlider.setMinSize(dim.getWidth(), dim.getHeight());
            } else if("maxSize".equals(key)) {
                Dimension2D dim = ((ObjectProperty<Dimension2D>) properties.get(key)).get();
                touchSlider.setMaxSize(dim.getWidth(), dim.getHeight());
            } else if("prefWidth".equals(key)) {
                touchSlider.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if("prefHeight".equals(key)) {
                touchSlider.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            } else if("minWidth".equals(key)) {
                touchSlider.setMinWidth(((DoubleProperty) properties.get(key)).get());
            } else if("minHeight".equals(key)) {
                touchSlider.setMinHeight(((DoubleProperty) properties.get(key)).get());
            } else if("maxWidth".equals(key)) {
                touchSlider.setMaxWidth(((DoubleProperty) properties.get(key)).get());
            } else if("maxHeight".equals(key)) {
                touchSlider.setMaxHeight(((DoubleProperty) properties.get(key)).get());
            } else if("scaleX".equals(key)) {
                touchSlider.setScaleX(((DoubleProperty) properties.get(key)).get());
            } else if("scaleY".equals(key)) {
                touchSlider.setScaleY(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutX".equals(key)) {
                touchSlider.setLayoutX(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutY".equals(key)) {
                touchSlider.setLayoutY(((DoubleProperty) properties.get(key)).get());
            } else if ("translateX".equals(key)) {
                touchSlider.setTranslateX(((DoubleProperty) properties.get(key)).get());
            } else if ("translateY".equals(key)) {
                touchSlider.setTranslateY(((DoubleProperty) properties.get(key)).get());
            } else if ("padding".equals(key)) {
                touchSlider.setPadding(((ObjectProperty<Insets>) properties.get(key)).get());
            } else if ("name".equals(key)) {
                touchSlider.setName(((StringProperty) properties.get(key)).get());
            } else if ("orientation".equals(key)) {
                touchSlider.setOrientation(((ObjectProperty<Orientation>) properties.get(key)).get());
            } else if("minValue".equals(key)) {
                touchSlider.setMinValue(((DoubleProperty) properties.get(key)).get());
            } else if("range".equals(key)) {
                touchSlider.setRange(((DoubleProperty) properties.get(key)).get());
            } else if("barBackgroundColor".equals(key)) {
                touchSlider.setBarBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if("barColor".equals(key)) {
                touchSlider.setBarColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if("thumbColor".equals(key)) {
                touchSlider.setThumbColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if("valueTextColor".equals(key)) {
                touchSlider.setValueTextColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if("nameTextColor".equals(key)) {
                touchSlider.setNameTextColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if("zeroColor".equals(key)) {
                touchSlider.setZeroColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if("formatString".equals(key)) {
                touchSlider.setFormatString(((StringProperty) properties.get(key)).get());
            } else if("valueVisible".equals(key)) {
                touchSlider.setValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if("nameVisible".equals(key)) {
                touchSlider.setNameVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("barBackgroundFilled".equals(key)) {
                touchSlider.setBarBackbroundFilled(((BooleanProperty) properties.get(key)).get());
            } else if ("showZero".equals(key)) {
                touchSlider.setShowZero(((BooleanProperty) properties.get(key)).get());
            } else if ("startFromZero".equals(key)) {
                touchSlider.setStartFromZero(((BooleanProperty) properties.get(key)).get());
            } else if ("snapToZero".equals(key)) {
                touchSlider.setSnapToZero(((BooleanProperty) properties.get(key)).get());
            } else if ("observer".equals(key)) {
                touchSlider.addTouchSliderObserver(((ObjectProperty<TouchSliderObserver>) properties.get(key)).get());
            }
        }
        if (properties.containsKey("styleClass")) {
            touchSlider.getStyleClass().add(((StringProperty) properties.get("styleClass")).get());
        }
        if (properties.containsKey("sliderValue")) {
            touchSlider.setSliderValue(((DoubleProperty) properties.get("sliderValue")).get());
        }
        return touchSlider;
    }
}

