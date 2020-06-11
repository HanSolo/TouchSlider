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

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.css.StyleableStringProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Window;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;


@DefaultProperty("children")
public class TouchSlider extends Region {
    private static final String                                    CSS_FILE              = "touch-slider.css";
    private static final double                                    PREFERRED_WIDTH       = 250;
    private static final double                                    PREFERRED_HEIGHT      = 250;
    private static final double                                    MINIMUM_WIDTH         = 50;
    private static final double                                    MINIMUM_HEIGHT        = 50;
    private static final double                                    MAXIMUM_WIDTH         = 1024;
    private static final double                                    MAXIMUM_HEIGHT        = 1024;
    private static final StyleablePropertyFactory<TouchSlider>     FACTORY               = new StyleablePropertyFactory<>(Region.getClassCssMetaData());
    private static final CssMetaData<TouchSlider, Orientation>     ORIENTATION           = FACTORY.createEnumCssMetaData(Orientation.class, "-orientation", s -> s.orientation, Orientation.HORIZONTAL, false);
    private static final CssMetaData<TouchSlider, Color>           BAR_BACKGROUND_COLOR  = FACTORY.createColorCssMetaData("-bar-background-color", s -> s.barBackgroundColor, Color.color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.3), false);
    private static final CssMetaData<TouchSlider, Color>           BAR_COLOR             = FACTORY.createColorCssMetaData("-bar-color", s -> s.barColor, Color.BLUE, false);
    private static final CssMetaData<TouchSlider, Color>           THUMB_COLOR           = FACTORY.createColorCssMetaData("-thumb-color", s -> s.thumbColor, Color.BLUE, false);
    private static final CssMetaData<TouchSlider, Color>           VALUE_TEXT_COLOR      = FACTORY.createColorCssMetaData("-value-text-color", s -> s.valueTextColor, Color.WHITE, false);
    private static final CssMetaData<TouchSlider, Color>           NAME_TEXT_COLOR       = FACTORY.createColorCssMetaData("-name-text-color", s -> s.nameTextColor, Color.WHITE, false);
    private static final CssMetaData<TouchSlider, Color>           ZERO_COLOR            = FACTORY.createColorCssMetaData("-zero-color", s -> s.zeroColor, Color.WHITE, false);
    private static final CssMetaData<TouchSlider, Boolean>         VALUE_VISIBLE         = FACTORY.createBooleanCssMetaData("-value-visible", s -> s.valueVisible, true, false);
    private static final CssMetaData<TouchSlider, Boolean>         NAME_VISIBLE          = FACTORY.createBooleanCssMetaData("-name-visible", s -> s.nameVisible, true, false);
    private static final CssMetaData<TouchSlider, String>          NAME                  = FACTORY.createStringCssMetaData("-name", s -> s.name, "", false);
    private static final CssMetaData<TouchSlider, Boolean>         BAR_BACKGROUND_FILLED = FACTORY.createBooleanCssMetaData("-bar-background-filled", s -> s.barBackbroundFilled, true, false);
    private static final CssMetaData<TouchSlider, Boolean>         SHOW_ZERO             = FACTORY.createBooleanCssMetaData("-show-zero", s -> s.showZero, true, false);
    private static final CssMetaData<TouchSlider, Boolean>         START_FROM_ZERO       = FACTORY.createBooleanCssMetaData("-start-from-zero", s -> s.startFromZero, false, false);
    private static final CssMetaData<TouchSlider, Boolean>         SNAP_TO_ZERO          = FACTORY.createBooleanCssMetaData("-snap-to-zero", s -> s.snapToZero, false, false);
    private static final CssMetaData<TouchSlider, String>          FORMAT_STRING         = FACTORY.createStringCssMetaData("-format-string", s -> s.formatString, "%.1f", false);
    private              String                                    userAgentStyleSheet;
    private              double                                    width;
    private              double                                    height;
    private              StyleableObjectProperty<Orientation>      orientation;
    private              StyleableObjectProperty<Color>            barBackgroundColor;
    private              StyleableObjectProperty<Color>            barColor;
    private              StyleableObjectProperty<Color>            thumbColor;
    private              StyleableObjectProperty<Color>            valueTextColor;
    private              StyleableObjectProperty<Color>            nameTextColor;
    private              StyleableObjectProperty<Color>            zeroColor;
    private              StyleableBooleanProperty                  valueVisible;
    private              StyleableBooleanProperty                  nameVisible;
    private              StyleableStringProperty                   name;
    private              StyleableBooleanProperty                  barBackbroundFilled;
    private              StyleableBooleanProperty                  showZero;
    private              StyleableBooleanProperty                  startFromZero;
    private              StyleableBooleanProperty                  snapToZero;
    private              double                                    oldValue;
    private              DoubleProperty                            value;
    private              DoubleProperty                            minValue;
    private              DoubleProperty                            range;
    private              StyleableStringProperty                   formatString;
    private              Canvas                                    canvas;
    private              GraphicsContext                           ctx;
    private              boolean                                   isZeroInRange;
    private              EventHandler<MouseEvent>                  mouseHandler;
    private              EventHandler<TouchEvent>                  touchHandler;
    private              CopyOnWriteArrayList<TouchSliderObserver> observers;


    // ******************** Constructors **************************************
    public TouchSlider() {
        orientation          = new StyleableObjectProperty<>(Orientation.HORIZONTAL) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "orientation"; }
            @Override public CssMetaData<? extends Styleable, Orientation> getCssMetaData() { return ORIENTATION; }
        };
        minValue             = new DoublePropertyBase(0) {
            @Override protected void invalidated() {
                isZeroInRange = get() < 0 && (get() + getRange()) > 0;
                redraw();
            }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "minValue"; }
        };
        range                = new DoublePropertyBase(1.0) {
            @Override protected void invalidated() {
                if (get() < 0) { set(1); }
                isZeroInRange = getMinValue() < 0 && (getMinValue() + get()) > 0;
                redraw();
            }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "range"; }
        };
        barBackgroundColor   = new StyleableObjectProperty<>(Color.color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.3)) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "barBackgroundColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return BAR_BACKGROUND_COLOR; }
        };
        barColor             = new StyleableObjectProperty<>(Color.BLUE) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "barColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return BAR_COLOR; }
        };
        thumbColor           = new StyleableObjectProperty<>(Color.BLUE) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "thumbColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return THUMB_COLOR; }
        };
        valueTextColor       = new StyleableObjectProperty<>(Color.WHITE) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "valueTextColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return VALUE_TEXT_COLOR; }
        };
        nameTextColor        = new StyleableObjectProperty<>(Color.WHITE) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "nameTextColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return NAME_TEXT_COLOR; }
        };
        zeroColor            = new StyleableObjectProperty<>(Color.WHITE) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "zeroColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return ZERO_COLOR; }
        };
        valueVisible         = new StyleableBooleanProperty() {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "valueVisible"; }
            @Override public CssMetaData<? extends Styleable, Boolean> getCssMetaData() { return VALUE_VISIBLE; }
        };
        nameVisible          = new StyleableBooleanProperty(true) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "nameVisible"; }
            @Override public CssMetaData<? extends Styleable, Boolean> getCssMetaData() { return NAME_VISIBLE; }
        };
        name                 = new StyleableStringProperty("") {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "name"; }
            @Override public CssMetaData<? extends Styleable, String> getCssMetaData() { return NAME; }
        };
        barBackbroundFilled  = new StyleableBooleanProperty(true) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "barBackgroundFilled"; }
            @Override public CssMetaData<? extends Styleable, Boolean> getCssMetaData() { return BAR_BACKGROUND_FILLED; }
        };
        showZero             = new StyleableBooleanProperty(true) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "showZero"; }
            @Override public CssMetaData<? extends Styleable, Boolean> getCssMetaData() { return SHOW_ZERO; }
        };
        startFromZero        = new StyleableBooleanProperty(false) {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "startFromZero"; }
            @Override public CssMetaData<? extends Styleable, Boolean> getCssMetaData() { return START_FROM_ZERO; }
        };
        snapToZero           = new StyleableBooleanProperty(false) {
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "snapToZero"; }
            @Override public CssMetaData<? extends Styleable, Boolean> getCssMetaData() { return SNAP_TO_ZERO; }
        };
        oldValue             = 0;
        value                = new DoublePropertyBase(0) {
            @Override protected void invalidated() {
                if (Double.compare(get(), oldValue) == 0) { return; }
                redraw();
                fireTouchSliderEvent(new TouchSliderEvent(TouchSlider.this, getMinValue() + getRange() * get()));
                oldValue = get();
            }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "value"; }
        };
        formatString         = new StyleableStringProperty("%.1f") {
            @Override protected void invalidated() { redraw(); }
            @Override public Object getBean() { return TouchSlider.this; }
            @Override public String getName() { return "formatString"; }
            @Override public CssMetaData<? extends Styleable, String> getCssMetaData() { return FORMAT_STRING; }
        };
        observers            = new CopyOnWriteArrayList<>();
        mouseHandler         = e -> {
            EventType<? extends MouseEvent> type = e.getEventType();
            double x = e.getX();
            double y = e.getY();
            if (MouseEvent.MOUSE_DRAGGED.equals(type)) {
                if (Orientation.HORIZONTAL.equals(getOrientation())) {
                    setValue(clamp(0.0, 1.0, x / width));
                } else {
                    setValue(clamp(0.0, 1.0, 1.0 - y / height));
                }
            } else if (MouseEvent.MOUSE_RELEASED.equals(type)) {
                if (getSnapToZero() && isZeroInRange) {
                    double value     = getMinValue() + (getRange() * getValue());
                    double snapRange = getRange() * 0.015;
                    if (value > -snapRange && value < snapRange) {
                        setValue((Math.abs(getMinValue()) / getRange()));
                    }
                }
            }
        };
        touchHandler         = e -> {
            EventType<? extends TouchEvent> type  = e.getEventType();
            TouchPoint                      point = e.getTouchPoint();
            double                          x     = point.getX();
            double                          y     = point.getY();
            if (TouchEvent.TOUCH_MOVED.equals(type)) {
                if (Orientation.HORIZONTAL.equals(getOrientation())) {
                    setValue(clamp(0.0, 1.0, x / width));
                } else {
                    setValue(clamp(0.0, 1.0, 1.0 - y / height));
                }
            } else if (TouchEvent.TOUCH_RELEASED.equals(type)) {
                if (getSnapToZero() && isZeroInRange) {
                    double value     = getMinValue() + (getRange() * getValue());
                    double snapRange = getRange() * 0.015;
                    if (value > -snapRange && value < snapRange) {
                        setValue((Math.abs(getMinValue()) / getRange()));
                    }
                }
            }
        };

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
            Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        getStyleClass().add("touch-slider");

        canvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        ctx    = canvas.getGraphicsContext2D();

        getChildren().setAll(canvas);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());

        if (Platform.isSupported(ConditionalFeature.INPUT_MULTITOUCH)) {
            canvas.addEventHandler(TouchEvent.TOUCH_MOVED, touchHandler);
            canvas.addEventHandler(TouchEvent.TOUCH_RELEASED, touchHandler);
        } else {
            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseHandler);
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseHandler);
        }
    }


    // ******************** Methods *******************************************
    @Override protected double computeMinWidth(final double HEIGHT) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT) { return super.computePrefWidth(HEIGHT); }
    @Override protected double computePrefHeight(final double WIDTH) { return super.computePrefHeight(WIDTH); }
    @Override protected double computeMaxWidth(final double HEIGHT) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH) { return MAXIMUM_HEIGHT; }

    public Orientation getOrientation() { return orientation.getValue(); }
    public void setOrientation(final Orientation orientation) { this.orientation.setValue(orientation); }
    public StyleableObjectProperty<Orientation> orientationProperty() { return orientation; }

    public double getMinValue() { return minValue.getValue().doubleValue(); }
    public void setMinValue(final double minValue) { this.minValue.setValue(minValue); }
    public ObservableValue minValueProperty() { return minValue; }

    public double getRange() { return range.getValue().doubleValue(); }
    public void setRange(final double range) { this.range.setValue(range); }
    public DoubleProperty rangeProperty() { return range; }

    public double getSliderValue() { return (getMinValue() + getRange() * getValue()); }
    public void setSliderValue(final double value) {
        double realValue = clamp(getMinValue(), getMinValue() + getRange(), value);
        setValue((realValue - getMinValue()) / getRange());
    }

    public Color getBarBackgroundColor() { return barBackgroundColor.getValue(); }
    public void setBarBackgroundColor(final Color color) { barBackgroundColor.setValue(color); }
    public StyleableObjectProperty<Color> barBackgroundColorProperty() { return barBackgroundColor; }

    public Color getBarColor() { return barColor.getValue(); }
    public void setBarColor(final Color color) { barColor.setValue(color); }
    public StyleableObjectProperty<Color> barColorProperty() { return barColor; }

    public Color getThumbColor() { return thumbColor.getValue(); }
    public void setThumbColor(final Color color) { thumbColor.setValue(color); }
    public StyleableObjectProperty<Color> thumbColorProperty() { return thumbColor; }

    public Color getValueTextColor() { return valueTextColor.getValue(); }
    public void setValueTextColor(final Color color) { valueTextColor.setValue(color); }
    public StyleableObjectProperty<Color> valueTextColorProperty() { return valueTextColor; }

    public Color getNameTextColor() { return nameTextColor.getValue(); }
    public void setNameTextColor(final Color color) { nameTextColor.setValue(color); }
    public StyleableObjectProperty<Color> nameTextColorProperty() { return nameTextColor; }

    public Color getZeroColor() { return zeroColor.getValue(); }
    public void setZeroColor(final Color color) { zeroColor.setValue(color); }
    public StyleableObjectProperty<Color> zeroColorProperty() { return zeroColor; }

    public String getName() { return name.getValue(); }
    public void setName(final String name) { this.name.setValue(name); }
    public StyleableStringProperty nameProperty() { return name; }

    public double getValue() { return value.get(); }
    public void setValue(final double value) { this.value.set(value); }
    public DoubleProperty valueProperty() { return value; }

    public boolean isValueVisible() { return valueVisible.getValue(); }
    public void setValueVisible(final boolean visible) { valueVisible.setValue(visible); }
    public StyleableBooleanProperty valueVisibleProperty() { return valueVisible; }

    public boolean isNameVisible() { return nameVisible.getValue(); }
    public void setNameVisible(final boolean visible) { nameVisible.setValue(visible); }
    public StyleableBooleanProperty nameVisibleProperty() { return nameVisible; }
    
    public String getFormatString() { return formatString.getValue(); }
    public void setFormatString(final String formatString) { this.formatString.setValue(formatString); }
    public StyleableStringProperty formatStringProperty() { return formatString; }

    public boolean getFilledBarBackground() { return barBackbroundFilled.getValue(); }
    public void setBarBackbroundFilled(final boolean filled) { barBackbroundFilled.setValue(filled); }
    public StyleableBooleanProperty filledBarBackgroundProperty() { return barBackbroundFilled; }

    public boolean getShowZero() { return showZero.getValue(); }
    public void setShowZero(final boolean show) { showZero.setValue(show); }
    public StyleableBooleanProperty showZeroProperty() { return showZero; }
    
    public boolean getStartFromZero() { return startFromZero.getValue(); }
    public void setStartFromZero(final boolean start) { startFromZero.setValue(start); }
    public StyleableBooleanProperty startFromZeroProperty() { return startFromZero; }

    public boolean getSnapToZero() { return snapToZero.getValue(); }
    public void setSnapToZero(final boolean snap) { snapToZero.setValue(snap); }
    public StyleableBooleanProperty snapToZeroProperty() { return snapToZero; }

    private double clamp(final double min, final double max, final double value) {
        if (value < min) { return min; }
        if (value > max) { return max; }
        return value;
    }


    // ******************** Event Handling ************************************
    public void addTouchSliderObserver(final TouchSliderObserver observer) { if (!observers.contains(observer)) { observers.add(observer); }}
    public void removeTouchSliderObserver(final TouchSliderObserver observer) { if (observers.contains(observer)) { observers.remove(observer); }}
    public void fireTouchSliderEvent(final TouchSliderEvent evt) { observers.forEach(observer -> observer.onTouchSliderEvent(evt)); }


    // ******************** Drawing *******************************************
    private void redraw() {
        final double value = getValue();
        ctx.clearRect(0, 0, width, height);
        ctx.setTextBaseline(VPos.CENTER);
        if (Orientation.HORIZONTAL.equals(getOrientation())) {
            double barHeight   = height * 0.75;
            double barY        = (height - barHeight) * 0.5;
            double arcSize     = clamp( 3, 10, barHeight * 0.1);
            double thumbHeight = barHeight * 1.2;

            if (getFilledBarBackground()) {
                ctx.setFill(getBarBackgroundColor());
                ctx.fillRoundRect(0, barY, width, barHeight, arcSize, arcSize);
            } else {
                double lineWidth = clamp(1, 5, barHeight * 0.01333333);
                ctx.setLineWidth(lineWidth);
                ctx.setStroke(getBarBackgroundColor());
                ctx.strokeRoundRect(lineWidth * 0.5, barY, width - lineWidth * 2 ,barHeight, arcSize, arcSize);
                ctx.setStroke(Color.TRANSPARENT);
            }

            ctx.setFill(getBarColor());
            if (getStartFromZero() && isZeroInRange) {
                double realValue = getMinValue() + getRange() * value;
                double zeroValue = (Math.abs(getMinValue()) / getRange());
                double zeroX     = width * zeroValue;
                double barWidth  = Math.abs(getValue() - zeroValue) * width;
                if (realValue > 0) {
                    ctx.fillRoundRect(zeroX, barY, barWidth, barHeight, arcSize, arcSize);
                    ctx.setFill(getThumbColor());
                    ctx.fillRoundRect(zeroX + barWidth - width * 0.015, (barY - barHeight * 0.1), width * 0.03, thumbHeight, arcSize, arcSize);
                } else if (realValue < 0) {
                    ctx.fillRoundRect(zeroX - barWidth, barY, barWidth, barHeight, arcSize, arcSize);
                    ctx.setFill(getThumbColor());
                    ctx.fillRoundRect(zeroX - barWidth - width * 0.015, (barY - barHeight * 0.1), width * 0.03, thumbHeight, arcSize, arcSize);
                } else {
                    ctx.setFill(getThumbColor());
                    ctx.fillRoundRect(zeroX - width * 0.015, (barY - barHeight * 0.1), width * 0.03, thumbHeight, arcSize, arcSize);
                }
            } else {
                ctx.fillRoundRect(0, barY, value * width, barHeight, arcSize, arcSize);
                ctx.setFill(getThumbColor());
                ctx.fillRoundRect(value * width - width * 0.015, (barY - barHeight * 0.1), width * 0.03, thumbHeight, arcSize, arcSize);
            }

            if (isValueVisible()) {
                double textGap = clamp(5, Double.MAX_VALUE, width * 0.95);
                ctx.setFill(getValueTextColor());
                ctx.setTextAlign(TextAlignment.RIGHT);
                ctx.setFont(Font.font(barHeight * 0.4));
                ctx.fillText(String.format(Locale.US, getFormatString(), (getMinValue() + getRange() * value)), textGap, barY + barHeight * 0.5, width * 0.5);
            }

            if (isNameVisible()) {
                double textGap = clamp(5, Double.MAX_VALUE, width * 0.05);
                ctx.setFill(getNameTextColor());
                ctx.setTextAlign(TextAlignment.LEFT);
                ctx.setFont(Font.font(barHeight * 0.4));
                ctx.fillText(getName(), textGap, barY + barHeight * 0.5);
            }

            if (isZeroInRange && getShowZero()) {
                double zeroX = width * Math.abs(getMinValue() / getRange());
                ctx.save();
                ctx.setStroke(getZeroColor());
                ctx.setLineDashes(2, 4);
                ctx.strokeLine(zeroX, barY, zeroX, barY + barHeight);
                ctx.restore();
            }
        } else {
            double barWidth   = width * 0.75;
            double barHeight  = height * value;
            double barX       = (width - barWidth) * 0.5;
            double arcSize    = clamp( 3, 10, barWidth * 0.1);
            double thumbWidth = barWidth * 1.2;

            if (getFilledBarBackground()) {
                ctx.setFill(getBarBackgroundColor());
                ctx.fillRoundRect(barX, 0, barWidth, height, arcSize, arcSize);
            } else {
                double lineWidth = clamp(1, 5, barWidth * 0.01333333);
                ctx.setLineWidth(lineWidth);
                ctx.setStroke(getBarBackgroundColor());
                ctx.strokeRoundRect(barX, lineWidth + 0.5, barWidth, height - lineWidth * 2, arcSize, arcSize);
                ctx.setStroke(Color.TRANSPARENT);
            }

            ctx.setFill(getBarColor());
            if (getStartFromZero() && isZeroInRange) {
                double realValue = getMinValue() + getRange() * value;
                double zeroValue = (Math.abs(getMinValue()) / getRange());
                double zeroY     = height - height * zeroValue;
                barHeight        = Math.abs(getValue() - zeroValue) * height;
                if (realValue > 0) {
                    ctx.fillRoundRect(barX, zeroY - barHeight, barWidth, barHeight, arcSize, arcSize);
                    ctx.setFill(getThumbColor());
                    ctx.fillRoundRect((barX - barWidth * 0.1), zeroY - barHeight - height * 0.015, thumbWidth, height * 0.03, arcSize, arcSize);
                } else if (realValue < 0) {
                    ctx.fillRoundRect(barX, zeroY, barWidth, barHeight, arcSize, arcSize);
                    ctx.setFill(getThumbColor());
                    ctx.fillRoundRect((barX - barWidth * 0.1), zeroY + barHeight - height * 0.015, thumbWidth, height * 0.03, arcSize, arcSize);
                } else {
                    ctx.setFill(getThumbColor());
                    ctx.fillRoundRect((barX - barWidth * 0.1), zeroY - height * 0.015, thumbWidth, height * 0.03, arcSize, arcSize);
                }
            } else {
                ctx.fillRoundRect(barX, height - barHeight, barWidth, barHeight, arcSize, arcSize);
                ctx.setFill(getThumbColor());
                ctx.fillRoundRect((barX - barWidth * 0.1), height - barHeight - height * 0.015, thumbWidth, height * 0.03, arcSize, arcSize);
            }

            if (isValueVisible()) {
                double textGap = clamp(5, Double.MAX_VALUE, height * 0.05);
                ctx.setFill(getValueTextColor());
                ctx.setTextAlign(TextAlignment.RIGHT);
                ctx.setFont(Font.font(barWidth * 0.4));
                ctx.save();
                ctx.translate((barX + barWidth * 0.5), textGap);
                ctx.rotate(-90);
                ctx.fillText(String.format(Locale.US, getFormatString(), (getMinValue() + getRange() * value)), 0, 0, height * 0.4);
                ctx.translate(-(barX + barWidth * 0.5), textGap);
                ctx.restore();
            }

            if (isNameVisible()) {
                double textGap = clamp(5, Double.MAX_VALUE, height * 0.95);
                ctx.setFill(getNameTextColor());
                ctx.setTextAlign(TextAlignment.LEFT);
                ctx.setFont(Font.font(barWidth * 0.4));
                ctx.save();
                ctx.translate((barX + barWidth * 0.5), textGap);
                ctx.rotate(270);
                ctx.fillText(getName(), 0, 0, height * 0.4);
                ctx.translate(-(barX + barWidth * 0.5), textGap);
                ctx.restore();
            }

            if (isZeroInRange && getShowZero()) {
                double zeroY = height - height * Math.abs(getMinValue() / getRange());
                ctx.save();
                ctx.setStroke(getZeroColor());
                ctx.setLineDashes(2, 4);
                ctx.strokeLine(barX, zeroY, barX + barWidth, zeroY);
                ctx.restore();
            }
        }
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (width > 0 && height > 0) {
            canvas.setWidth(width);
            canvas.setHeight(height);
            canvas.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);
            redraw();
        }
    }


    // ******************** Style related *************************************
    @Override public String getUserAgentStylesheet() {
        if (null == userAgentStyleSheet) { userAgentStyleSheet = TouchSlider.class.getResource(CSS_FILE).toExternalForm(); }
        return userAgentStyleSheet;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() { return FACTORY.getCssMetaData(); }
    @Override public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() { return FACTORY.getCssMetaData(); }
}
