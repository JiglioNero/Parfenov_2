import functions.Function;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

public class Main extends Application {


    NumberTextField A;
    NumberTextField B;
    NumberTextField C;
    NumberTextField D;
    RadioButton alpha;
    RadioButton beta;
    RadioButton gamma;
    RadioButton delta;
    NumberTextField boundsCount;
    NumberTextField a;
    NumberTextField b;
    LineChart<Number, Number> chart;
    HBox root;

    CheckBox F;
    CheckBox f;
    int prevT;

    NumberTextField step;
    Label integral;


    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setScene(initScene());
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scene initScene() {
        root = new HBox();

        VBox leftPane = new VBox();
        leftPane.setMinWidth(300);
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.setSpacing(10);
        Label wParams = new Label("Window parameters");

        GridPane wGrid = new GridPane();
        wGrid.setHgap(10);
        wGrid.setVgap(10);
        Label left = new Label("Left:");
        A = new NumberTextField(-2);
        Label top = new Label("Top:");
        B = new NumberTextField(2);
        Label right = new Label("Right:");
        C = new NumberTextField(2);
        Label bottom = new Label("Bottom:");
        D = new NumberTextField(-2);
        Label aL = new Label("a:");
        a = new NumberTextField(-2);
        Label bL = new Label("b:");
        b = new NumberTextField(2);
        wGrid.add(left, 0, 0);
        wGrid.add(A, 1, 0);
        wGrid.add(right, 0, 1);
        wGrid.add(C, 1, 1);
        wGrid.add(bottom, 0, 2);
        wGrid.add(D, 1, 2);
        wGrid.add(top, 0, 3);
        wGrid.add(B, 1, 3);
        wGrid.add(aL, 0, 4);
        wGrid.add(a, 1, 4);
        wGrid.add(bL, 0, 5);
        wGrid.add(b, 1, 5);

        Label fParams = new Label("Function parameters");

        GridPane fGrid = new GridPane();
        fGrid.setHgap(10);
        fGrid.setVgap(10);
        Label alphaL = new Label("Alpha:");
        alpha = new RadioButton();
        Label betaL = new Label("Beta:");
        beta = new RadioButton();
        Label gammaL = new Label("Gamma:");
        gamma = new RadioButton();
        Label deltaL = new Label("Delta:");
        delta = new RadioButton();
        fGrid.add(alphaL, 0, 0);
        fGrid.add(alpha, 1, 0);
        fGrid.add(betaL, 0, 1);
        fGrid.add(beta, 1, 1);
        fGrid.add(gammaL, 0, 2);
        fGrid.add(gamma, 1, 2);
        fGrid.add(deltaL, 0, 3);
        fGrid.add(delta, 1, 3);

        ToggleGroup group = new ToggleGroup();
        alpha.setToggleGroup(group);
        beta.setToggleGroup(group);
        gamma.setToggleGroup(group);
        delta.setToggleGroup(group);
        alpha.setSelected(true);

        GridPane bGrid = new GridPane();
        bGrid.setHgap(10);
        bGrid.setVgap(10);
        Label nBoundL = new Label("Count of integration bounds:");
        boundsCount = new NumberTextField(500);
        bGrid.add(nBoundL, 0, 0);
        bGrid.add(boundsCount, 1, 0);

        HBox functions = new HBox();
        functions.setSpacing(10);
        f = new CheckBox("f(x)");
        f.setSelected(true);
        F = new CheckBox("F(x)");
        F.setSelected(true);
        functions.getChildren().addAll(f, F);

        GridPane res = new GridPane();
        step = new NumberTextField(0.01);
        integral = new Label();
        res.add(new Label("Step = "),0,0);
        res.add(step,1,0);
        res.add(new Label("I = "),0,1);
        res.add(integral,1,1);


        Scene scene = new Scene(root, 1300, 700);

        Button commit = new Button("Draw");
        commit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initPlot();
            }
        });

        Label name = new Label("ИВТ-32БО Бондарь Алеся.");
        name.setAlignment(Pos.BOTTOM_LEFT);

        leftPane.getChildren().addAll(wParams, wGrid, fParams, fGrid, bGrid, functions, res, commit,name);

        root.getChildren().add(leftPane);

        initPlot();

        return scene;
    }

    public void initPlot() {

        integral.setText("0");
        Function f = new Function() {
            @Override
            public double getValueAt(double x) {
                double a = 1;
                double b = 1;
                double g = 1;
                double d = 1;
                if(alpha.isSelected()){
                    a = x;
                }
                if(beta.isSelected()){
                    b = x;
                }
                if(gamma.isSelected()){
                    g = x;
                }
                if(delta.isSelected()){
                    d = x;
                }

                return a * (Math.sin(b / ((x - g) * (x - g)))) * Math.cos(d * 1);
                //return Math.sin(x);
            }
        };
        double h = ((double) (C.getNumber() - A.getNumber()))/(boundsCount.getNumber()-1);
        Function F = new Function() {
            @Override
            public double getValueAt(double x) {
                double start = A.getNumber();
                int t = (int) ((x - start)/h);
                double value1 = f.getValueAt(start + (t-1)*h);
                double value2 = f.getValueAt(start + (t-0.5)*h);
                double value3 = f.getValueAt(start + (t)*h);
                if (Double.isNaN(value1)){
                    value1 = Double.MIN_VALUE;
                }
                if (Double.isNaN(value2)){
                    value2 = Double.MIN_VALUE;
                }
                if (Double.isNaN(value3)){
                    value3 = Double.MIN_VALUE;
                }
                double tmp = h/6*(value1+4*value2+value3);
                if (prevT!=t) {
                    integral.setText(String.valueOf(Double.parseDouble(integral.getText()) + tmp));
                }
                prevT = t;
                return Double.parseDouble(integral.getText())+tmp;
            }
        };

        if(root.getChildren().contains(chart)) {
            root.getChildren().remove(chart);
        }

        chart = new LineChart<Number, Number>(new NumberAxis(A.getNumber(), C.getNumber(),(C.getNumber()-A.getNumber())/10), new NumberAxis(D.getNumber(), B.getNumber(),(B.getNumber()-D.getNumber())/10));
        chart.setMinWidth(1000);
        chart.getStylesheets().add(Main.class.getResource("res/ch.css").toExternalForm());
        root.getChildren().add(chart);

        double step = this.step.getNumber();
        ArrayList<ArrayList<Pair<Double, Double>>> data = new ArrayList<>();
        data.add(new ArrayList<>());
        data.add(new ArrayList<>());
        if(this.f.isSelected()){
            for (double j = A.getNumber(); j < C.getNumber(); j+=step) {
                data.get(0).add(new Pair<>(j, f.getValueAt(j)));
            }
        }
        if(this.F.isSelected()){
            for (double j = a.getNumber(); j < b.getNumber(); j+=h) {
                data.get(1).add(new Pair<>(j, F.getValueAt(j)));
            }
        }

        for(ArrayList<Pair<Double, Double>> fData : data){
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i < fData.size(); i++) {
                Number x = fData.get(i).getKey();
                Number y = fData.get(i).getValue();
                series.getData().add(new XYChart.Data<>(x, y));
            }
            chart.getData().add(series);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
